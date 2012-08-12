package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.functions.Mith;
import uk.co.peopleandroid.jarvar.internals.Numeric;

public class Lit extends Blank implements Numeric {

	public int value;
	
	public Lit toLit() {
		if(this.next == this) {
			return Real.val(value);
		} else {
			try {
				Lit a = (Lit)this.copy();
				Lit b = a;
				do {
					b = (Lit)b.next;
					if(b.value == 0x22) {
						Lit c = (Lit)b.next;
						b.next = new Lit(0x22);
						b.next.next = c;
						if(b == a) {
							a = (Lit)a.next;
							break;//final quote handler
						} else {
							b = (Lit)b.next;
						}
					}
				} while(b != a);
				return (new Lit(0x22)).unite(a).unite(new Lit(0x22));
			} catch(ClassCastException e) {
				onError(error.fail);
				return null;
			}
		}
	}
	
	public int length() {
		int y = 1;
		Blank x = this.next;
		while(x != this) {
			y++;
			x = x.next;
		}
		return y;
	}
	
	public Blank step() {
		return this.next;
	}
	
	public boolean isZ() {
		return true;
	}
	
	public Lit(String s) {
		int j = s.length() - 2;
		if(j == -2) {
			value = 0;
			onError(error.malformed);
		} else {
			value = s.charAt(j + 1);
			next = this;
			Lit l, m;
			m = this;
			for(int i = 0; i < j; i++) {
				l = new Lit(s.charAt(i));
				m.next = l;
				m = l;
				l.next = this;
			}
		}
	}
	
	public int hash() {
		int t = 0;
		Lit n = this;
		try {
			do {
				t ^= value;
				n = (Lit)n.next;
			} while(n != this);
		} catch(Exception e) {
			return -1;
		}
		return t & 63;//range
	}
	
	public Blank read() {
		Lit q = new Lit(0x22);
		Func x = (Func)this.split(q);
		Func t = x;
		Func out = null;
		int y;
		if((y = x.length() % 2) == 0) {
			onError(error.malformed);
			return null;
		}
		int i;
		//magic quotes
		for(i = 0; i < y - 2; i++) {
			if(((Func)t.next).here == end) {
				((Lit)t.here).unite(new Lit(0x22))
					.unite((Lit)((Func)t.next.next).here);
				y -= 2;
				t.next = t.next.next.next;//skip 2
			}
			t = (Func)t.next;	
		}
		//parse on space
		t = x;
		q = new Lit(32);
		Func a;
		Ptr f;
		for(i = 0; i < y; i+=2) {
			a = (Func) ((Lit)t.here).split(q);
			if(a.here != end) {
				f = Vocab.find((Lit)a.here);
				if(f != null) {
					out = new Func(f, out);
				} else {
					try {
						out = new Func((Blank) (new Real(a.here)).demote(), out);
					} catch(ClassCastException e) {
						Vocab.addUser(f = new Unfound((Lit)a.here));
						out = new Func(f, out);
					}
				}
			}
			if(a.next != null)
				out = new Func(((Func)a.next).here, out);//literal
			// [ ] specials and reverse to do
		}
		return out.nest();
	}
	
	public boolean same(Lit as) {
		Lit a = this;
		Lit b = as;
		do {
			a = (Lit)a.next;
			b = (Lit)b.next;
			if(a.value != b.value) return false;//not equal
		} while(a != this);
		if(b == as) return true;
		return false;
	}
	
	public Lit unite(Lit with) {
		Blank x = with.next;
		with.next = this.next;
		this.next = x;
		return with;
	}
	
	public Blank split(Lit on) {
		Func out = null;
		Lit a = this;
		Lit x = a;//section start
		Lit z = a;//section end
		Lit y = a;//stop
		Lit b = on;
		do {
			do {
				a = (Lit)a.next;
				b = (Lit)b.next;
				if(a.value != b.value) break;//not equal
			} while(b != on && a != y);
			if(a.value == b.value && b == on) {
				//match
				if(z == x) {
					out = new Func(end, out);
				} else {
					z.next = x.next;//loop
					out = new Func(z, out);
				}
				x = z = a;
			} else {
				a = (Lit)a.next;//step
				z = (Lit)z.next;//add char
			}
		} while(a != y);
		if(y == x) {
			out = new Func(end, out);
		} else {
			y.next = x.next;//loop
			out = new Func(y, out);
		}
		return out.reverse();
	}
	
	public boolean rotary(Lit as) {
		Lit a = this;
		Lit b = as;
		do {
			a = (Lit)a.next;
			b = (Lit)b.next;
			if(a.value > b.value) return false;
			if(a.value < b.value) return true;
		} while(a != this && b != as);
		return true;
	}
	
	public Lit(int n) {
		value = n;
		next = this;
	}
	
	public Blank copy() {
		Lit a = this;
		Lit b = null, c, d = null;
		do {
			c = b;
			b = new Lit(a.value);
			a = (Lit)a.next;
			if(c != null) {
				c.next = b;
			} else {
				d = b;
			}
		} while(a != this);
		b.next = d;
		return d;
	}

	public Numeric add(Numeric x) {
		if(x.isZ())
			return new Lit(this.value + ((Lit)x).value);
		return ((Real)this.promote()).add(x);
	}

	public Numeric negate() {
		return new Lit(-this.value);
	}

	public Numeric mul(Numeric x) {
		if(x.isZ()) 
			return new Lit(this.value*((Lit)x).value);
		return ((Real)this.promote()).mul(x);
	}

	public Numeric invert() {
		return new Real(1.0/this.value);
	}

	public Numeric promote() {
		return new Real(value);
	}

	public Numeric demote() {
		return this;
	}
	
	public Numeric conj() {
		return this;
	}

	public boolean equal(Numeric x) {
		if(x.isZ())
			return this.value == ((Lit)x).value;
		return this.promote().equal(x);
	}

	public Numeric abs() {
		return this.value < 0?new Lit(-this.value):this;
	}

	public Numeric log() {
		return this.promote().log();
	}

	public Numeric exp() {
		return this.promote().exp();
	}

	public Numeric pow(Numeric x) {
		if(x.isZ())
			return new Real(Mith.powi(this.value, ((Lit)x).value));
		return this.promote().pow(x);
	}

	public boolean order(Numeric x) {
		//less than
		//much quicker than the real check
		if(x.isZ())
			return this.value < ((Lit)x).value;
		return this.promote().order(x);
	}
	
	public Numeric makeZ() {
		return this;
	}
}
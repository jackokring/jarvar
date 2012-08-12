package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.internals.Colour;

public class Func extends Blank {
	
	public Colour ink() {
		int len = this.length();
		Colour[] x = new Colour[len];
		Blank y = this;
		try {
			for(int i = 0; i < len;i++) {
				x[i] = ((Func)y).here.ink();
				y = y.step();
			}
		} catch(ClassCastException e) {
			onError(error.fail);
			return (new Blank()).ink();
		}
		return Colour.add(x);
	}

	public Blank here;
	static Func nester = null;
	
	public Blank step() {
		return this.next;
	}
	
	public Blank nest() {
		//compile nesting
		Func x = null;
		Func y = this;
		Func a;
		while(y != null) {
			if(y.here == Vocab.close) { //nest
				nester = new Func(x, nester);
				x = null;
				continue;
			}
			if(y.here == Vocab.open) { //un-nest
				Func b = x;//the nest
				x = (Func)nester.here;
				nester = (Func)nester.next;
				x = new Func(b, x);//place nested
				continue;
			}
			a = y;
			y = (Func)y.next;
			a.next = x;
			x = a;
		}
		if(nester != null) {
			onError(error.malformed);
			nester = null;
		}
		return x;
	}
	
	public int length() {
		int y = 1;
		Blank x = this.next;
		while(x != null) {
			y++;
			x = x.next;
		}
		return y;
	}
	
	public Blank reverse() {
		Func x = null;
		Func y = this;
		Func a;
		while(y != null) {
			a = y;
			y = (Func)y.next;
			a.next = x;
			x = a;
		}
		return x;
	}
	
	public Lit toLit() {
		Lit x = new Lit("[ ");
		Func y = this;
		do {
			x = x.unite(y.here.colour()).unite(new Lit(" "));
		} while(y != null);
		return x.unite(new Lit("]"));
	}
	
	public Func(Blank at, Blank to) {
		here = at;
		next = to;
	}
	
	public void assign(Blank val) {
		here = val;//no protection
	}
	
	public void evaluate() {
		if(here != null) {
			rstack = new Func(this.here, rstack);//push
		}
	}
	
	public Blank copy() {
		return new Func(here, next.copy());
	}
}
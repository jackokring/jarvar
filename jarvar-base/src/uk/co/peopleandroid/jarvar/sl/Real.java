package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.functions.Mith;
import uk.co.peopleandroid.jarvar.internals.Numeric;
import uk.co.peopleandroid.jarvar.platform.Storage;

public class Real extends Blank implements Numeric {
	
	double real;
	double imaginary;
	
	public Lit toLit() {
		Lit x = val(real);
		if(compare(imaginary, 0.0)) {
			x.unite(new Lit(" ")).unite(val(imaginary))
				.unite(new Lit(" ")).unite(Vocab.find(new Lit("imagine")).toLit())
				.unite(new Lit(" ")).unite(Vocab.find(new Lit("plus")).toLit());
		}
		return x;
	}
	
	static Lit val(double x) {
		if((int)x == x) return val((int)x);
		boolean flip = false;
		if(x < 0) {
			flip = true;
			x = -x;
		}
		int exp = (int)(Mith.log(x)/Mith.log(10));
		Lit y = val(exp);
		if(exp > 0) x /= Mith.powi(10, exp);
		if(exp < 0) x *= Mith.powi(10, exp);
		x *= 100000000;//8 digit shift
		Lit z = val((int)x);
		Lit dp = new Lit(0x2e);
		dp.next = z.next.next;
		z.next.next = dp;//add decimal
		if(flip) z = (new Lit(0x2d)).unite(z);//sign
		return exp == 0 ? z : z.unite(new Lit(0x65)).unite(y);
	}
	
	static Lit val(int x) {
		Lit y = null;
		boolean flip = false;
		int a;
		if(x < 0) {
			flip = true;
			x = -x;
		}
		do {
			a = x % 10;
			x /= 10;
			a += 48;
			if(y == null) y = new Lit(a);
			else y = (new Lit(a)).unite(y);
		} while(x > 0);
		if(flip) y = (new Lit("-")).unite(y);
		return y;
	}
	
	public Real(Blank b) {
		//parse double from lit
		double acc = 0;
		boolean flip = false;
		int dp = 0, dpf = 0, chk;
		Lit x = (Lit)b;
		if(((Lit)x.next).value == 0x2d) {
			x = (Lit)x.next;
			flip = true;
		}
		do {
			acc *= 10;//base
			dp += dpf;
			x = (Lit)x.next;
			if(x.value == 0x2e) {
				//decimal
				acc /= 10;//correct
				dpf = -1;
			}
			if(x.value == 0x45 || x.value == 0x65) {
				acc /= 10;
				b.next = x.next;
				dp += (int)(new Real(b)).real;
			}
			chk = x.value - 48;//ascii 0
			if(chk < 0 || chk > 9) throw new ClassCastException();
			acc += chk;
		} while(x != b);
		if(dp > 0) acc *= Mith.powi(10, dp);
		if(dp < 0) acc /= Mith.powi(10, dp);
		if(flip) acc = -acc;
		real = acc;
		imaginary = 0;
	}
	
	public Real(double x) {
		real = x;
		imaginary = 0;
	}
	
	public Real(double x, double y) {
		real = x;
		imaginary = y;
	}
	
	public Blank copy() {
		return new Real(this.real, this.imaginary);
	}
	
	public boolean is() {
		return true;
	}
	
	public boolean isZ() {
		return false;
	}

	public Numeric add(Numeric x) {
		if(x.isZ())
			return this.add(x.promote());
		return new Real(this.real + ((Real)x).real,
				this.imaginary + ((Real)x).imaginary);
	}

	public Numeric negate() {
		return new Real(-this.real, -this.imaginary);
	}

	public Numeric mul(Numeric x) {
		if(x.isZ())
			return this.mul(x.promote());
		return new Real(this.real * ((Real)x).real -
				this.imaginary * ((Real)x).imaginary,
				this.real * ((Real)x).imaginary +
				this.imaginary * ((Real)x).real);
	}
	
	double sq() {
		return this.real * this.real + this.imaginary * this.imaginary;
	}
	
	public Numeric abs() {
		return new Real(Storage.sqrt(this.sq()));
	}

	public Numeric invert() { 
		double d = this.sq();
		if(Storage.mathErr(d)) throw new ArithmeticException();
		return new Real(this.real / d, -this.imaginary / d);
	}

	public Numeric promote() {
		return this;
	}
	
	public boolean equal(Numeric x) {
		if(x.isZ())
			return this.equal(x.promote());
		return compare(this.real, ((Real)x).real)
			&& compare(this.imaginary, ((Real)x).imaginary);
	}
	
	public static boolean compare(double a, double b) {
		return Storage.abs(a - b) < 0.000000000000001;
	}

	public Numeric demote() {
		int t;
		if(compare(imaginary, 0) && compare(real, (t = (int)real)))
			return new Lit(t); 
		return this;
	}

	public Numeric conj() {
		return new Real(this.real, -this.imaginary);
	}

	public Numeric log() {
		double c = Storage.sqrt(sq());
		if(Storage.mathErr(c)) throw new ArithmeticException();
		double a = Mith.log(c);
		double b = compare(this.real, 0)
			? (imaginary < 0 ? -Storage.PI / 2 : Storage.PI / 2)
			: Mith.atan(this.imaginary / this.real);
		c = (real < 0) ? (b < 0 ? -Storage.PI : Storage.PI) : 0;
		return new Real(a, b + c);
	}

	public Numeric exp() {
		double a = Mith.exp(this.real);
		double b = Storage.sin(this.imaginary);
		double c = Storage.cos(this.imaginary);
		return new Real(a * c, a * b);
	}

	public Numeric pow(Numeric x) {
		if(x.isZ())
			x = x.promote();
		try {
			return x.mul(this.log()).exp();
		} catch(ArithmeticException e) {
			return new Real(0);
		}
	}

	public boolean order(Numeric x) {
		if(x.isZ())
			x = x.promote();
		double a, b;
		//uses the exponetial single sign ordering of reals
		//for this less than x
		if(Storage.mathErr(a = ((Real) this.exp()).sq())
				& Storage.mathErr(b = ((Real) x.exp()).sq())) {
			return !this.invert().order((Real)x.invert());
		} else return a < b;
	}
	
	public Numeric makeZ() {
		return new Lit((int)Storage.sqrt(this.sq()));
	}
}

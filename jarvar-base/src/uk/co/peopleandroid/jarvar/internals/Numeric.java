package uk.co.peopleandroid.jarvar.internals;

public interface Numeric {
	
	boolean isZ();
	Numeric makeZ();

	Numeric add(Numeric x);
	Numeric negate();
	Numeric mul(Numeric x);
	Numeric invert();
	Numeric promote();
	Numeric demote();
	Numeric conj();
	
	boolean equal(Numeric x);
	
	Numeric abs();
	Numeric log();
	Numeric exp();
	Numeric pow(Numeric x);
	
	boolean order(Numeric x);
}

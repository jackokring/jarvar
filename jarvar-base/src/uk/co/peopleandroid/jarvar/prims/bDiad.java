package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.internals.Numeric;
import uk.co.peopleandroid.jarvar.sl.Prim;

public abstract class bDiad extends Prim {

	public bDiad(String var) {
		super(var);
	}
	
	public abstract boolean doit(Numeric x, Numeric y);

	public void evaluate() {
		put(makeBool(doit((Numeric)get(), (Numeric)get())));
	}
}

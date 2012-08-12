package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.internals.Numeric;
import uk.co.peopleandroid.jarvar.sl.Blank;
import uk.co.peopleandroid.jarvar.sl.Prim;

public abstract class Diad extends Prim {

	public Diad(String var) {
		super(var);
	}
	
	public abstract Numeric doit(Numeric x, Numeric y);

	public void evaluate() {
		put((Blank)doit((Numeric)get(), (Numeric)get()).demote());
	}
}

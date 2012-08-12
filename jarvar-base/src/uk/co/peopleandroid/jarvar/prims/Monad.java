package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.internals.Numeric;
import uk.co.peopleandroid.jarvar.sl.Blank;
import uk.co.peopleandroid.jarvar.sl.Prim;

public abstract class Monad extends Prim {

	public Monad(String var) {
		super(var);
	}
	
	public abstract Numeric doit(Numeric x);

	public void evaluate() {
		put((Blank)doit((Numeric)get()).demote());
	}
}

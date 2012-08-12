package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.sl.Prim;

public class Not extends Prim {

	public Not(String var) {
		super(var);
	}

	public void evaluate() {
		put(makeBool(get() != end));
	}
}

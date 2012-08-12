package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.sl.Prim;

public class Syntax extends Prim {

	public Syntax(String var) {
		super(var);
	}

	public void evaluate() {
		onError(error.fail);
	}
}

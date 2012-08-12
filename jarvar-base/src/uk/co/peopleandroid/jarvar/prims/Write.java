package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.sl.*;

public class Write extends Prim {

	public Write(String var) {
		super(var);
	}

	public void evaluate() {
		//can also write function items
		get().assign(get());
	}
}

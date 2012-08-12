package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.sl.*;

public class Literal extends Prim {

	public Literal(String var) {
		super(var);
	}

	public void evaluate() {
		Blank b = ((Func)rstack.here).here;//the literal item
		rstack.here = rstack.here.next;
		stack = new Func(b, stack);
	}
}

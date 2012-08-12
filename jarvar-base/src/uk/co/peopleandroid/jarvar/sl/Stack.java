package uk.co.peopleandroid.jarvar.sl;

public class Stack extends Ptr {

	public Stack(Lit var, Blank val) {
		super(var, val);
	}

	public void evaluate() {
		rstack = new Func(((Func)here).here, rstack);
		here = here.next;
	}
	
	public void assign(Blank val) {
		here = new Func(val, here);
	}
}

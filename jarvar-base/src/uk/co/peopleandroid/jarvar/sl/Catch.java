package uk.co.peopleandroid.jarvar.sl;

public class Catch extends Blank {
	
	public Catch(Blank rest) {
		next = rest;
	}
	
	
	public Lit toLit() {
		return (new Lit("catch ")).unite(next.toLit());
	}
	
	public void evaluate() {
		//catch gets executed means not found by error
		//implying no error
		//implying must drop catch and catch function chain
		rstack = (Func)rstack.next;
	}
}

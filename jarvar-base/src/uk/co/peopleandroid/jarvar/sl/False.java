package uk.co.peopleandroid.jarvar.sl;

public class False extends Blank {

	public boolean is() {
		return false;
	}
	
	public Blank copy() {
		return this;
	}
	
	public Lit toLit() {
		return new Lit("false");
	}
}

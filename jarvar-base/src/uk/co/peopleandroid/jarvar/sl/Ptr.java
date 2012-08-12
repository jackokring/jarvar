package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.internals.Colour;

public class Ptr extends Func {
	
	public Colour ink() {
		if(here == null)
			return new Colour(128, 128, 128);
		return here.ink();
	}
	
	public Blank step() {
		return end;
	}
	
	public int length() {
		return 1;
	}
	
	public Lit toLit() {
		return (Lit)name.copy();
	}
	
	Lit name;
	public Vocab in;//null is global protected keyword (no redefine)!!
	//next is chain
	
	public Ptr(String var, Blank val) {
		super(val, null);//make value
		name = new Lit(var);//name it
	}
	
	public Ptr(Lit var, Blank val) {
		super(val, null);
		name = var;
	}
	
	public Blank copy() {
		return this;//variable identity
	}
}

package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.internals.Colour;
import uk.co.peopleandroid.jarvar.platform.Storage;
import uk.co.peopleandroid.jarvar.prims.*;

public class Prim extends Critical {
	
	public Prim(String var, Blank val) {
		super(var, val);
	}
	
	public Prim(String var) {
		super(var, null);
	}
	
	public Colour ink() {
		if(Storage.classTest(this, new WriteLiteral("lit")))
			return new Colour(255, 0, 255);
		if(Storage.classTest(this, new Literal("lit")))
			return new Colour(0, 0, 255);
		if(Storage.classTest(this, new Write("lit")))
			return new Colour(255, 0, 0);
		if(this.in == null)
			return new Colour(0, 255, 0);
		if(this.in == Vocab.std)
			return new Colour(0, 128, 0);
		return (new Blank()).ink();
	}
	
	public Blank makeBool(boolean b) {
		return b ? new Blank() : Blank.end;
	}
	
	public void evaluate() {//for dispatch
		int i = ((Lit) get()).value;
		Virtual v = (Virtual) get();
		v.p[i].evaluate();
	}
}

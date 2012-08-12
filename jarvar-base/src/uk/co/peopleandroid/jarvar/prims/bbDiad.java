package uk.co.peopleandroid.jarvar.prims;

import uk.co.peopleandroid.jarvar.sl.Blank;
import uk.co.peopleandroid.jarvar.sl.Prim;

public abstract class bbDiad extends Prim {

	public bbDiad(String var) {
		super(var);
	}
	
	public abstract boolean doit(Blank x, Blank y);

	public void evaluate() {
		Blank x = get(), y = get();
		if(x == y && x.is()) {
			put(x);
			return;
		}
		put(makeBool(doit(x, y)));
	}
}

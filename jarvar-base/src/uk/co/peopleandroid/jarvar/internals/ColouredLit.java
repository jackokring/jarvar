package uk.co.peopleandroid.jarvar.internals;

import uk.co.peopleandroid.jarvar.sl.Lit;

public class ColouredLit extends Lit {

	Colour is;
	
	public ColouredLit(int n, Colour c) {
		super(n);
		is = c;
	}
}

package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.platform.Storage;

public class Strong extends Ptr {
	
	public Strong(String var, Blank val) {
		super(var, val);
	}

	public void assign(Blank val) {
		if(this.here == null) super.assign(val);
		else if(val != null && Storage.classTest(this.here, val)) {
			here = val;
		} else {
			onError(error.writeNotPossible);//nope
		}
	}
}

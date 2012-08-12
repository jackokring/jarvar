package uk.co.peopleandroid.jarvar.sl;

public class Unfound extends Ptr {
	
	public Unfound(String var) {
		super(var, null);
	}
	
	public Unfound(Lit var) {
		super(var, null);
	}
	
	boolean find() {
		if(here == null) here = Vocab.find(name);
		if(here == null || here == this) {
			here = null;//fix-up unfound before error
			onError(error.badType);
			return false;
		}
		return true;
	}

	public void assign(Blank val) {
		try {
			if(find()) ((Ptr)here).assign(val);
		} catch(Exception e) {
			onError(error.fail);//bad cast done how?
		}
	}
	
	public void evaluate() {
		if(find()) here.evaluate();
	}
}

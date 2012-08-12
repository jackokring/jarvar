package uk.co.peopleandroid.jarvar.sl;

public class Critical extends Ptr {
	
	public Critical(String var, Blank val) {
		super(var, val);
	}
	
	public Critical(Lit var, Blank val) {
		super(var, val);
	}
	
	public void assign(Blank val) {
		onError(error.writeNotPossible);//no can do
	}
}

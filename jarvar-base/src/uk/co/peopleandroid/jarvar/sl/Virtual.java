package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.platform.Audio;
import uk.co.peopleandroid.jarvar.platform.Keys;
import uk.co.peopleandroid.jarvar.platform.Storage;
import uk.co.peopleandroid.jarvar.platform.Video;

public abstract class Virtual extends Vocab {

	public static Audio a;
	public static Video v;
	public static Keys k;
	public static Storage s;
	Prim[] p;
	
	public Virtual(String var) {
		super(var);
		create();
	}
	
	protected void does(Prim pr, int i) {
		try {
			p[i] = pr;
		} catch(Exception e) {
			Prim[] q = p;
			p = new Prim[i+1];
			for(int j = 0; j < q.length; j++) p[j] = q[j];
			p[i] = pr;
		}
	}
	
	public void evaluate() {
		put(this);
		super.evaluate();
	}
	
	abstract void activate();
	
	abstract void create();
}

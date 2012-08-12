package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.platform.Audio;
import uk.co.peopleandroid.jarvar.platform.Storage;

public class Throw extends Blank {
	
	static Catch catchEg = new Catch(null);
	
	public Lit toLit() {
		return new Lit("<ERROR>");
	}
	
	public Throw(String s) {
		this(new Lit(s));
	}
	
	public Throw(Lit s) {
		next = s;
	}
	
	public void evaluate() {
		Func x = rstack = (Func)rstack.next;
		try {
			while(rstack != null) {
				if(Storage.classTest(rstack.here, catchEg)) {
					rstack.here = rstack.here.next;//enter catch
					break;
				}
				rstack = (Func)rstack.next; 
			}
			put(x);//trace
			put(next);//type
		} catch(Exception e) {
			onError(error.fail);
		}
		if(rstack == null) Audio.beep();
	}
}

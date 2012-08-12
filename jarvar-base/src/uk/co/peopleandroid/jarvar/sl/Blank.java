package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.internals.*;

public class Blank {
	
	public Blank next; //list link
	
	protected final static Messages error = new Messages();
	public final static False end = new False();
	
	protected static Func stack;
	protected static Func rstack;
	
	public Lit toLit() {
		return new Lit("true");
	}
	
	public Lit colour() {
		Lit a = this.toLit();
		Lit cl = new ColouredLit(((Lit)a.next).value, this.ink());
		cl.next = a.next.next;
		a.next = cl;
		return a;
	}
	
	public int length() {
		return 1;
	}
	
	public Blank step() {
		return end;
	}
	
	public Colour ink() {
		return new Colour(0, 0, 0);//black
	}

	public void evaluate() {
		put(this);
	}
	
	protected static void onError(String s) {
		rstack = new Func(new Throw(s), rstack);
	}
	
	public void run() {
		//allow thread evaluation
		Func f = null;
		Func g = null;
		rstack = new Func(this, rstack);
		while(true) {
			try {
				while(rstack.here == null) rstack = (Func)rstack.next;
				g = (Func)((Func)(f = (Func)rstack.here)).next;
				rstack.here = g;//update
			} catch(ClassCastException e) {
				//blank of false
				onError(error.fail);
			} catch(NullPointerException z) {
				//stack underflow
				return;
			}
			try {
				f.evaluate();//nest?
			} catch(ArithmeticException a) {
				//mathematical exception
				onError(error.arithmetic);
			} catch(NullPointerException b) {
				onError(error.nothing);
			} catch(ClassCastException c) {
				onError(error.badType);
			} catch(ArrayIndexOutOfBoundsException d) {
				onError(error.virtual);
			} catch(OutOfMemoryError e) {
				onError(error.memory);
			}
		}
	}
	
	public boolean is() {
		return true;
	}
	
	public boolean and(Blank b) {
		return this.is() && b.is();
	}
	
	public boolean or(Blank b) {
		return this.is() || b.is();
	}
	
	public boolean not() {
		return !is();
	}
	
	public Blank get() {
		Blank b = stack.here;
		stack = (Func)stack.next;
		if(b == null) b = end;//fix via False
		return b;
	}
	
	public void put(Blank b) {
		stack = new Func(b, stack);
	}

	public void assign(Blank val) {
		onError(error.writeNotPossible);
	}
	
	public Blank copy() {
		return this;//differing nulls logic best!!
		//better this way to represent differing true voids
	}
}

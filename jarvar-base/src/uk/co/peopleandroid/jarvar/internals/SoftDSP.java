package uk.co.peopleandroid.jarvar.internals;

/* lazy DSP evaluation, while maintaining absolute
 * relative synchronization. This is to acurately
 * simulate massively parallel DSP, ensuring the
 * master processor does copying between DSPs
 * such that sequence is maintained. Reading element
 * 0 is the best way to check for DSP halt or
 * complete.
 * 
 * The requirement to have locking AND synchronized
 * is to maintain an easier hardware implementation,
 * as write locking maintains control even if
 * the DSP is performing a goto when retasked.
 * 
 *  The destroy method does more clean up, and has
 *  no memory leak. I wonder how neural nets map?
 */

public class SoftDSP extends Thread {
	
	boolean running = true;
	boolean locking = false;
	long ops;
	static SoftDSP list;
	SoftDSP next;
	
	public SoftDSP() {
		next = list;
		list = this;
		ops = mops();
		this.start();
	}

	public void run() {
		while(running) {
			eval();
			Thread.yield();
		}
		mem = null;
	}
	
	public synchronized int read(int x) {
		evalTo();
		return mem[x & 255];
	}
	
	public synchronized void write(int x, int val) {
		evalTo();
		if(x == 0) locking = true;
		mem[x & 255] = val;
	}
	
	void evalTo() {
		long maxOps = mops();
		while(running && maxOps > ops) eval();
	}
	
	synchronized long mops() {
		SoftDSP h = list;
		long max = 0;
		while(h != null) {
			if(ops > max) max = ops;
			h = h.next;
		}
		return max;
	}
	
	public synchronized void destroy() {
		running = false;
		SoftDSP h = list;
		SoftDSP q = null;
		while(h != null) {
			if(h == this) {
				if(q != null)
					q.next = h.next;
				else
					list = h.next;
				break;
			}
			q = h;
			h = h.next;
		}
	}
	
	int mem[] = new int[256];
	
	synchronized void eval() {
		int pc = mem[0];
		int a = pc >>> 24;//pc
		int b = (pc >> 16) & 255;//inf (NB denormalized not good?)
		int c = (pc >> 8) & 255;//zero
		int d = pc & 255;//NaN
		int i = mem[a];//instruction
		
		int ia = i >>> 24;
		int ib = (i >> 16) & 255;
		int ic = (i >> 8) & 255;
		int id = i & 255;
		
		float x = asF(ia);
		x *= x;
		x *= asF(id);
		x = asF(ic) - x;
		x *= asF(ib);
		a++;
		if(x == Float.NaN) a = d;
		if(x == Float.NEGATIVE_INFINITY || x == Float.POSITIVE_INFINITY) a = b;
		if(x == 0.0) a = c;
		pc = (a << 24) | (pc & 0xffffff);//restore pc
		if(!locking) {
			mem[0] = pc;
			mem[ia] = asI(x);//as jump vectoring allowed!!!
		} else {
			if(ia != 0) mem[ia] = asI(x);//as jump vectoring allowed!!!
			locking = false;//write 0 locking
		}
		ops++;
	}
	
	float asF(int idx) {
		return Float.intBitsToFloat(mem[idx]);
	}
	
	int asI(float val) {
		return Float.floatToIntBits(val);
	}
}
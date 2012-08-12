package uk.co.peopleandroid.jarvar.sl;

import uk.co.peopleandroid.jarvar.internals.*;
import uk.co.peopleandroid.jarvar.prims.*;

public class Vocab extends Ptr {

	static Ptr[] index = new Ptr[64];
	
	public static Strong con;

	public static Strong cur;
	public static Vocab std, app;
	static Ptr open, close;
	
	static {
		WordBuilder wb = new WordBuilder();//make some reserved words
		std = new Vocab("standard");//no current essential...!!
		Vocab.add(std);//add standard
		Vocab.add(open = new Syntax("["));//specials
		Vocab.add(close = new Syntax("]"));
		Vocab.add(con = new Strong("context", std));//no current!!
		Vocab.add(cur = new Strong("current", std));//no as defined current!!
		Vocab.add(app = new Vocab("app"));
		wb.extra();//add non reserved words
		cur.assign(app);//boost to app
		wb = new AppBuilder();
	}
	
	public void evaluate() {
		//no fail after context exists
		con.here = this;
	}
	
	public void assign(Blank val) {
		try {
			Ptr p = (Ptr)val;
			if(p.in == null || p.in == std) {
				onError(error.writeNotPossible);
				return;
			}
			p.in = this;
		} catch(Exception e) {
			onError(error.badType);
		}
	}
	
	public Vocab(String var) {
		super(var, null);
	}
	
	public Vocab(Lit var) {
		super(var, null);
	}
	
	static Blank deleteHandle;
	
	public static void delete(Ptr p) {
		if(p.in == null) {
			onError(error.writeNotPossible);
			return;
		}
		try {
			Vocab v = (Vocab)p;
			trawl(v);
		} catch(Exception e) {
			
		}
		if(find(p.name) == p) {
			if(deleteHandle == null) {
				index[p.name.hash()] = (Ptr)p.next;
				return;
			}
			deleteHandle.next = p.next;
		}
	}
	
	public static Blank all() {
		Ptr p;
		Blank out = null;
		for(int x = 0; x < 64; x++) {
			p = index[x];
			while(p != null) {
				if(p == find(p.name)) out = new Func(p, out);
				p = (Ptr)p.next;
			}
		}
		return out;
	}
	
	public static Ptr find(Lit name) {
		int i = name.hash();
		if(i == -1) {
			return null;
		}
		Vocab v;
		try {
			v = (Vocab)con.here;
		} catch(Exception e) {
			//special case of no context
			v = null;
		}
		Ptr t = index[i];
		boolean first = true;
		Ptr q = null;
		Ptr p = t;//preserved 1st
		while(t != null) {
			try {
				t = (Ptr)t.next;
				if(p.name.same(name)) {
					//check vocab
					while(v != null && p.in != v) {
						v = v.in;//term as reserved (not in any)
					}
					if(p.in == v) {
						//mtf and break
						if(!first) {
							q.next = (Ptr)t;
							p.next = index[i];
							index[i] = (Ptr)p;
						}
						break;
					}
				}
			} catch(Exception e) {
				onError(error.fail);
				return null;
			}
			first = false;//special case
			q = p; p = t;//move one
		}
		deleteHandle = q;
		return p;
	}
	
	static Func added;
	
	public static void restore() {
		Func x = added;
		Ptr y;
		con.assign(std);
		while(x != null) {
			while( (y = find(((Ptr)x.here).name)) != x.here) {
				delete(y);
			}
			x = (Func)x.next;
		}
	}
	
	static void trawl(Vocab v) {
		Ptr p;
		//make definitions make sense
		if(cur.here == v) cur.here = v.in;
		if(con.here == v) con.here = v.in;
		for(int x = 0; x < 64; x++) {
			p = index[x];
			while(p != null) {
				if(p.in == v) delete(find(p.name));
				p = (Ptr)p.next;
			}
		}
	}
	
	public Colour ink() {
		Ptr p;
		Func f = null;
		for(int x = 0; x < 64; x++) {
			p = index[x];
			while(p != null) {
				if(p.in == this) f = new Func(p, f);
				p = (Ptr)p.next;
			}
		}
		if(f == null) return new Colour(128, 128, 128);
		return f.ink();
	}
	
	public static void add(Ptr var) {
		added = new Func(var, added);//make list for restore
		addUser(var);
	}
	
	public static void addUser(Ptr var) {
		//without find check
		int i = var.name.hash();
		if(i == -1) {
			onError(error.malformed);
			return;//bad name
		}
		Ptr t = Vocab.find(var.name);
		if(t != null && t.in == null) {
			onError(error.writeNotPossible);
			return;
		}
		try {
			var.in = (Vocab)(cur.here);
		} catch(Exception e) {
			//special case of no current
			//var.in is null, and becomes global reserved
			var.in = null;
		}
		var.next = index[i];
		index[i] = var;
	}
}
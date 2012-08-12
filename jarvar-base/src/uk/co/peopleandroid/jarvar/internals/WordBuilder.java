package uk.co.peopleandroid.jarvar.internals;

import uk.co.peopleandroid.jarvar.prims.*;
import uk.co.peopleandroid.jarvar.sl.*;

public class WordBuilder {

	public WordBuilder() {
		//reserved words add(Ptr)
		somer();
	}
	
	
	void add(Ptr p) {
		Vocab.add(p);
	}
	
	public void extra() {
		//extra non reserved add(Ptr)
		maths();
		logics();//includes special reserved for forward defs
	}
	
	static Literal lit = new Literal("literal");
	Prim j;
	
	void somer() {
		Prim w, p;
		add(new Prim("prim"));
		add(w = new Write("write"));
		add(p = new Literal("parse") {
			public void evaluate() {
				Func f = (Func)rstack.next;
				Blank b = ((Func)f.here).here;//the parse item
				((Func)rstack.next).here = f.next;
				stack = new Func(b, stack);
			}
		});
		twoFunc("does", p, w);//parse write
		add(lit);
		twoFunc("static", lit, lit);
		twoFunc("false", lit, Blank.end);
		add(new Write("reserved") {
			public void evaluate() {
				((Ptr)get()).in = null;
			}
		});
		add(new Write("author") {
			public void evaluate() {
				Vocab.cur = Vocab.con;
			}
		});
		add(new Literal("if") {
			public void evaluate() {
				super.evaluate();
				Blank x = get();
				if(get().is()) rstack = new Func(x, rstack);
			}
		});
		add(new Literal("else") {
			public void evaluate() {
				super.evaluate();
				super.evaluate();
				Blank y = get();
				Blank x = get();
				if(get().is()) rstack = new Func(x, rstack);
				else rstack = new Func(y, rstack);
			}
		});
		add(new Literal("while") {
			public void evaluate() {
				super.evaluate();
				Blank x = get();
				if(get().is()) {
					Func y = new Func(this, new Func(x, null));
					rstack = new Func(y, rstack);
					rstack = new Func(x, rstack);
					//possible task switch point optimal here?
				}
			}
		});
		add(new WriteLiteral("to") {
			public void evaluate() {
				super.evaluate();//get next word Ptr
				get().assign(new Func(lit, new Func(get(), null)));
			}
		});
		add(new Write("create") {
			public void evaluate() {
				Vocab.addUser(new Ptr((Lit)get(), new Func(lit, new Func(get(), null))));
			}
		});
		add(new Write("stack") {
			public void evaluate() {
				Vocab.addUser(new Stack((Lit)get(), null));
			}
		});
		add(new Write("book") {
			public void evaluate() {
				Vocab x = new Vocab((Lit)get());
				Vocab.addUser(x);
				x.evaluate();//make context
			}
		});
		add(new Write("restore") {
			public void evaluate() {
				Vocab.restore();
			}
		});
		add(new Write("forget") {
			public void evaluate() {
				Vocab.delete((Ptr)get());
			}
		});
		add(new WriteLiteral("make") {
			public void evaluate() {
				super.evaluate();
				Blank x = get();
				Vocab.addUser(new Critical((Lit)get(), x));
			}
		});
		add(new Literal("cue") {
			//subclass of Parse as uses rstack
			//2nd and extends Literal too.
			public void evaluate() {
				rstack.next = new Func(get(), rstack.next);
			}
		});
		add(new Literal("catch") {
			public void evaluate() {
				super.evaluate();
				rstack.next = new Func(new Catch(get()), rstack.next);
			}
		});
		add(new Literal("error") {
			public void evaluate() {
				rstack.next = new Func(new Throw((Lit)get()), rstack.next);
			}
		});
		add(new Prim("all") {
			public void evaluate() {
				put(Vocab.all());
			}
		});
	}
	
	void logics() {
		add(new Prim("print") {
			public void evaluate() {
				put(get().colour());
			}
		});
		add(new Prim("read") {
			public void evaluate() {
				put(((Lit)get().copy()).read());
			}
		});
		add(new Prim("reverse") {
			public void evaluate() {
				put(((Func)get().copy()).reverse());
			}
		});
		add(new Prim("split") {
			public void evaluate() {
				Lit x = (Lit)get();
				put(((Lit)get().copy()).split(x));
			}
		});
		add(new bbDiad("same") {
			public boolean doit(Blank x, Blank y) {
				return ((Lit)x).same((Lit)y);
			}
		});
		add(new bbDiad("and") {
			public boolean doit(Blank x, Blank y) {
				return x.and(y);
			}
		});
		add(new bbDiad("or") {
			public boolean doit(Blank x, Blank y) {
				return x.or(y);
			}
		});
		add(new bbDiad("rotary") {
			public boolean doit(Blank x, Blank y) {
				return ((Lit)x).rotary((Lit)y);
			}
		});
		add(new Not("not"));
		this.twoFunc("true", Vocab.find(new Lit("false")),
				Vocab.find(new Lit("not")));
				Vocab.find(new Lit("true")).in = null;//reserve it
		add(new Prim("drop") {
			public void evaluate() {
				stack = (Func)stack.next;
			}
		});
		add(new Prim("swap") {
			public void evaluate() {
				Blank x = ((Func)stack.next).here;
				((Func)stack.next).here = ((Func)stack).here;
				((Func)stack).here = x;
			}
		});
		add(new Prim("over") {
			public void evaluate() {
				put(((Func)stack.next).here.copy());
			}
		});
		add(new Prim("length") {
			public void evaluate() {
				put(new Lit(get().length()));
			}
		});
		add(new Prim("unite") {
			public void evaluate() {
				Lit x = (Lit)get().copy();
				put(((Lit)get().copy()).unite(x));
			}
		});
		add(new Prim("index") {
			public void evaluate() {
				int y = ((Lit)get()).value;
				Blank z = get();
				int a = z.length();
				y %= a;
				if(y < 0) y = a + y;
				for(int x = 0; x < y; x++) z = z.step();
				put(z);
			}
		});
		add(new Prim("trim") {
			public void evaluate() {
				int y = ((Lit)get()).value;
				Func z = (Func)get().copy();
				Func b = z;
				Func c = null;
				int a = z.length();
				y %= a;
				if(y < 0) y = a + y;
				for(int x = 0; x < y; x++) {
					c = z;
					z = (Func)z.step();
				}
				if(c == null) b = null;
				else c.next = null;
				put(b);
			}
		});
		add(new Prim("dup") {
			public void evaluate() {
				Blank x = get();
				put(x);
				put(x.copy());
			}
		});
		add(new Prim("step") {
			public void evaluate() {
				put(get().step());
			}
		});
		add(new Prim("find") {
			public void evaluate() {
				put(Vocab.find((Lit)get()));
			}
		});
		add(new Prim("top") {
			public void evaluate() {
				Blank x = get();
				put(x.step());
				put(((Func)x).here);
			}
		});
		add(j = new Prim("join") {
			public void evaluate() {
				put(new Func(get(), get()));
			}
		});
		twoFunc("box", Vocab.find(new Lit("static")), j);
	}
	
	void maths() {
		add(new Monad("abs") {
			public Numeric doit(Numeric x) {
				return x.abs();
			}
		});
		add(new Monad("conj") {
			public Numeric doit(Numeric x) {
				return x.conj();
			}
		});
		add(new Monad("exp") {
			public Numeric doit(Numeric x) {
				return x.exp();
			}
		});
		add(new Monad("integer") {
			public Numeric doit(Numeric x) {
				return x.makeZ();
			}
		});
		Monad inv;
		add(inv = new Monad("invert") {
			public Numeric doit(Numeric x) {
				return x.invert();
			}
		});
		add(new Monad("log") {
			public Numeric doit(Numeric x) {
				return x.log();
			}
		});
		Monad ne;
		add(ne = new Monad("negate") {
			public Numeric doit(Numeric x) {
				return x.negate();
			}
		});
		Diad pl;
		add(pl = new Diad("plus") {
			public Numeric doit(Numeric x, Numeric y) {
				return x.add(y);
			}
		});
		twoFunc("minus", ne, pl);
		Diad ti;
		add(ti = new Diad("times") {
			public Numeric doit(Numeric x, Numeric y) {
				return x.mul(y);
			}
		});
		twoFunc("divide", inv, ti);
		twoFunc("imagine", new Real(0, 1), ti);
		add(new Diad("power") {
			public Numeric doit(Numeric x, Numeric y) {
				return y.pow(x);
			}
		});
		add(new bDiad("equal") {
			public boolean doit(Numeric x, Numeric y) {
				return x.equal(y);
			}
		});
		add(new bDiad("order") {
			//test top less than second
			public boolean doit(Numeric x, Numeric y) {
				return x.order(y);
			}
		});
	}
	
	public void twoFunc(String n, Blank a, Blank b) {
		add(new Critical(n, new Func(a, new Func(b, null))));
	}
}
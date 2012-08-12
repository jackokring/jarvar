package uk.co.peopleandroid.jarvar;

import uk.co.peopleandroid.jarvar.sl.*;

public class Compiler {
	
	static Vocab v;
	Blank result;
	
	static {
		v = Vocab.std;
	}
	
	public Compiler(String eval) {
		Lit a = new Lit(eval);
		result = a.read();
		result.run();
	}
	
	public Lit item() {
		return result.get().colour();
	}
}

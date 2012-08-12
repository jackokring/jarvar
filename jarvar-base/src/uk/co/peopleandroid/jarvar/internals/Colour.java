package uk.co.peopleandroid.jarvar.internals;

public class Colour {
	
	short red;
	short green;
	short blue;

	public Colour(int r, int g, int b) {
		red = (short)r;
		green = (short)g;
		blue = (short)b;
	}
	
	public static Colour add(Colour[] c) {
		int r = 0, g = 0, b = 0, cl = c.length;
		for(int i = 0; i < cl; i++) {
			r += c[i].red;
			g += c[i].green;
			b += c[i].blue;
		}
		return new Colour(r / cl, g / cl, b / cl);
	}
}

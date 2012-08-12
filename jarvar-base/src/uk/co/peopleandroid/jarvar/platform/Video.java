package uk.co.peopleandroid.jarvar.platform;

import uk.co.peopleandroid.jarvar.Graphics;

public abstract class Video {

	//to adapt a Graphics internal to
	//one made for the platform
	public static Graphics getGraphics(String s) {
		return null;
	}
}

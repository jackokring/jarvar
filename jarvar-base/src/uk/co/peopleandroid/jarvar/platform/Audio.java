package uk.co.peopleandroid.jarvar.platform;

import uk.co.peopleandroid.jarvar.Sound;

public abstract class Audio {
	
	//to adapt a Sound internal to
	//one made for the platform
	public static Sound getSound(String s) {
		return null;
		
	}
	
	public static void beep() {
		
	}
}

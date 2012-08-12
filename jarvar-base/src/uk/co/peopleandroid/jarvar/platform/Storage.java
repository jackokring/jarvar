package uk.co.peopleandroid.jarvar.platform;

import uk.co.peopleandroid.jarvar.sl.Blank;

public abstract class Storage {
	
	//this may need overridding in some platform scopes
	//and it removes the base libray dependance on Object
	//this is important for some platforms, especially
	//the native compiler platform, which implements
	//Audio, Video, Keys and Storage as native classes
	public static boolean classTest(Blank a, Blank b) {
		//A get class abstraction handler
		return a.getClass() == b.getClass();
	}
	
	public static boolean mathErr(double x) {
		return Double.isInfinite(x) || Double.isNaN(x);
	}
	
	public static double sin(double x) {
		return Math.sin(x);
	}
	
	public static double cos(double x) {
		return Math.cos(x);
	}
	
	public static double sqrt(double x) {
		return Math.sqrt(x);
	}
	
	public static double abs(double x) {
		return Math.abs(x);
	}
	
	final public static double PI = Math.PI;
}
package com.ogya.lokakarya.util;

public class Utils {
	public static String getHashCodeNumber() {
		int min = 100000;
		int max = 999999;
	    return "["+(int) ((Math.random() * (max - min)) + min)+"]";
	}
}

package com.faraday.citations;

public enum Category {
	LOVE, INSPIRING, FUN, POLITICS, LIFE;
	private static Category[] allValues = values();
    public static Category fromOrdinal(int n) {return allValues[n];}
}

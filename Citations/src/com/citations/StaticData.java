package com.citations;

import java.util.Random;

/**
 * @author luigi static variables and methods are here
 */
public class StaticData
{

	/**
	 * @param min
	 * @param max
	 * @return randomInt [min, max]
	 */
	public static int randInt(int min, int max)
	{

		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}

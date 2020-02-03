package com.edmistone.buildhelper.helpers;

import java.util.Arrays;

public class ArrayHelper
{
	public static <T> int indexOf(T[] array, T val)
	{
		return Arrays.asList(array).indexOf(val);
	}
}

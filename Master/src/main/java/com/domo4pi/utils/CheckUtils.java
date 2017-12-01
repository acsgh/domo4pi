package com.domo4pi.utils;

import java.util.regex.Pattern;

/**
 * This util class was written to export all the common check all the method preconditions.
 * 
 * @author Alberto Crespo Sánchez <albertocresposanchez@gmail.com>
 * @version 1.0 <11-02-2011> Initial version.
 */
public final class CheckUtils {

	/**
	 * Check one object. <br>
	 * And if it is null a exception will be throw.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @throws NullPointerException If the extension is null, If the value is null.
	 */
	public static void checkNull(String name, Object value) {
		if (value == null) {
			throw new NullPointerException("The param " + name + " can't be null.");
		}
	}

	/**
	 * Check one string. <br>
	 * And if it is null or empty a exception will be throw.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @throws NullPointerException If the extension is null, If the value is null.
	 * @throws IllegalArgumentException If the extension is empty, If the value is empty.
	 */
	public static void checkString(String name, String value) {
		checkString(name, value, false, false);
	}

	/**
	 * Check one string. And if it doesn't satisfy the condition a exception will be throw.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param canBeNull True the string can be null, false It can't.
	 * @param canBeEmpty True the string can be empty, false It can't.
	 * @throws NullPointerException If the extension is null, If the value can't be null and is null.
	 * @throws IllegalArgumentException If the extension is empty, If the value can't be empty and is empty.
	 */
	public static void checkString(String name, String value, boolean canBeNull, boolean canBeEmpty) {
		checkNull("name", name);

		if (name.isEmpty()) {
			throw new IllegalArgumentException("The param extension can't be empty.");
		}

		if (!canBeNull) {
			checkNull(name, value);
		}
		if ((!canBeEmpty) && (value != null) && (value.isEmpty())) {
			throw new IllegalArgumentException("The param " + name + " can't be empty.");
		}
	}

	/**
	 * Check one string. <br>
	 * And if it is null or not match the pattern a exception will be throw.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param pattern The pattern value.
	 * @throws NullPointerException If the extension is null, If the value is null, If the pattern is null.
	 * @throws IllegalArgumentException If the extension is empty, If the value doesn't match the pattern.
	 */
	public static void checkStringPattern(String name, String value, String pattern) {
		checkStringPattern(name, value, pattern, null);
	}


	/**
	 * Check one string. <br>
	 * And if it is null or not match the pattern a exception will be throw.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param pattern The pattern value.
	 * @param patternDescription The pattern description.
	 * @throws NullPointerException If the extension is null, If the value is null, If the pattern is null.
	 * @throws IllegalArgumentException If the extension is empty, If the value doesn't match the pattern.
	 */
	public static void checkStringPattern(String name, String value, String pattern, String patternDescription) {
		checkNull("name", name);
		checkNull("pattern", pattern);

		Pattern patternCompiled = Pattern.compile(pattern);

		if (!patternCompiled.matcher(value).matches()) {
			StringBuilder exceptionDescription = new StringBuilder("The param").append(name);
			
			if(patternDescription == null){
				exceptionDescription.append(", doesn't match with the pattern '").append(pattern).append("'");
			}else{
				exceptionDescription.append(", have to be ").append(patternDescription).append(".");
			}
			
			throw new IllegalArgumentException(exceptionDescription.toString());
		}
	}

	/**
	 * Check if one byte is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param max The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkByteSmaller(String name, byte value, byte max) {
		checkByteSmaller(name, value, "", max);
	}

	/**
	 * Check if one byte is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param maxName The extension of the min param
	 * @param max The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkByteSmaller(String name, byte value, String maxName, byte max) {
		checkByte(name, value, "", Byte.MIN_VALUE, maxName, max);
	}

	/**
	 * Check if one byte is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkByteBigger(String name, byte value, byte min) {
		checkByteBigger(name, value, "", min);
	}

	/**
	 * Check if one byte is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param minName The extension of the min param
	 * @param min The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkByteBigger(String name, byte value, String minName, byte min) {
		checkByte(name, value, minName, min, "", Byte.MAX_VALUE);
	}

	/**
	 * Check if one byte are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkByte(String name, byte value, byte min, byte max) {
		checkByte(name, value, "", min, "", max);
	}

	/**
	 * Check if one byte are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param minName The extension of the min param
	 * @param max The max value allowed to this param.
	 * @param maxName The extension of the max param
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkByte(String name, byte value, String minName, byte min, String maxName, byte max) {
		checkLong(name, value, minName, min, maxName, max);
	}

	/**
	 * Check if one short is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkShortSmaller(String name, short value, short max) {
		checkShortSmaller(name, value, "", max);
	}

	/**
	 * Check if one short is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param maxName The extension of the max param
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkShortSmaller(String name, short value, String maxName, short max) {
		checkShort(name, value, "", Short.MIN_VALUE, maxName, max);
	}

	/**
	 * Check if one short is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkShortBigger(String name, short value, short min) {
		checkShortBigger(name, value, "", min);
	}

	/**
	 * Check if one short is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param minName The extension of the min param
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkShortBigger(String name, short value, String minName, short min) {
		checkShort(name, value, minName, min, "", Short.MAX_VALUE);
	}

	/**
	 * Check if one short are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkShort(String name, short value, short min, short max) {
		checkShort(name, value, "", min, "", max);
	}

	/**
	 * Check if one short are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param minName The extension of the min param
	 * @param max The max value allowed to this param.
	 * @param maxName The extension of the max param
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkShort(String name, short value, String minName, short min, String maxName, short max) {
		checkLong(name, value, minName, min, maxName, max);
	}

	/**
	 * Check if one int is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkIntSmaller(String name, int value, int max) {
		checkIntSmaller(name, value, "", max);
	}

	/**
	 * Check if one int is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param maxName The extension of the max param
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkIntSmaller(String name, int value, String maxName, int max) {
		checkInt(name, value, "", Integer.MIN_VALUE, maxName, max);
	}

	/**
	 * Check if one int is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkIntBigger(String name, int value, int min) {
		checkIntBigger(name, value, "", min);
	}

	/**
	 * Check if one int is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param minName The extension of the min param
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkIntBigger(String name, int value, String minName, int min) {
		checkInt(name, value, minName, min, "", Integer.MAX_VALUE);
	}

	/**
	 * Check if one int are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkInt(String name, int value, int min, int max) {
		checkInt(name, value, "", min, "", max);
	}

	/**
	 * Check if one int are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param minName The extension of the min param
	 * @param max The max value allowed to this param.
	 * @param maxName The extension of the max param
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkInt(String name, int value, String minName, int min, String maxName, int max) {
		checkLong(name, value, minName, min, maxName, max);
	}

	/**
	 * Check if one long is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkLongSmaller(String name, long value, long min) {
		checkLongSmaller(name, value, "", min);
	}

	/**
	 * Check if one long is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param maxName The extension of the max param
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkLongSmaller(String name, long value, String maxName, long max) {
		checkLong(name, value, "", Long.MIN_VALUE, maxName, max);
	}

	/**
	 * Check if one long is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkLongBigger(String name, long value, long max) {
		checkLongBigger(name, value, "", max);
	}

	/**
	 * Check if one long is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param minName The extension of the min param
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkLongBigger(String name, long value, String minName, long min) {
		checkLong(name, value, minName, min, "", Long.MAX_VALUE);
	}

	/**
	 * Check if one long are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkLong(String name, long value, long min, long max) {
		checkLong(name, value, "", min, "", max);
	}

	/**
	 * Check if one long are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param minName The extension of the min param
	 * @param max The max value allowed to this param.
	 * @param maxName The extension of the max param
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkLong(String name, long value, String minName, long min, String maxName, long max) {
		checkString("name", name);
		checkString("minName", minName, false, true);
		checkString("maxName", maxName, false, true);

		if (min > max) {
			throw new IllegalArgumentException("The min can't be bigger than the max.");
		}

		if (value < min) {
			throw new IllegalArgumentException("The param " + name + " (" + value + ") can't be smaller than " + min
					+ ((!minName.isEmpty()) ? " (" + minName + ")" : "") + ".");
		}
		if (value > max) {
			throw new IllegalArgumentException("The param " + name + " (" + value + ") can't be bigger than " + max
					+ ((!maxName.isEmpty()) ? " (" + maxName + ")" : "") + ".");
		}
	}

	/**
	 * Check if one float is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkFloatSmaller(String name, float value, float max) {
		checkFloatSmaller(name, value, "", max);
	}

	/**
	 * Check if one float is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param maxName The extension of the max param
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkFloatSmaller(String name, float value, String maxName, float max) {
		checkFloat(name, value, "", Float.MIN_VALUE, maxName, max);
	}

	/**
	 * Check if one float is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkFloatBigger(String name, float value, float min) {
		checkFloatBigger(name, value, "", min);
	}

	/**
	 * Check if one float is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param minName The extension of the min param
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkFloatBigger(String name, float value, String minName, float min) {
		checkFloat(name, value, minName, min, "", Float.MAX_VALUE);
	}

	/**
	 * Check if one float are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkFloat(String name, float value, float min, float max) {
		checkFloat(name, value, "", min, "", max);
	}

	/**
	 * Check if one float are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param minName The extension of the min param
	 * @param max The max value allowed to this param.
	 * @param maxName The extension of the max param
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkFloat(String name, float value, String minName, float min, String maxName, float max) {
		checkDouble(name, value, minName, min, maxName, max);
	}

	/**
	 * Check if one double is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkDoubleSmaller(String name, double value, double max) {
		checkDoubleSmaller(name, value, "", max);
	}

	/**
	 * Check if one double is smaller than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param maxName The extension of the max param
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkDoubleSmaller(String name, double value, String maxName, double max) {
		checkDouble(name, value, "", Double.MIN_VALUE, maxName, max);
	}

	/**
	 * Check if one double is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkDoubleBigger(String name, double value, double min) {
		checkDoubleBigger(name, value, "", min);
	}

	/**
	 * Check if one double is bigger than one number.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param minName The extension of the min param
	 * @param min The min value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkDoubleBigger(String name, double value, String minName, double min) {
		checkDouble(name, value, minName, min, "", Double.MAX_VALUE);
	}

	/**
	 * Check if one double are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param max The max value allowed to this param.
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkDouble(String name, double value, double min, double max) {
		checkDouble(name, value, "", min, "", max);
	}

	/**
	 * Check if one double are in one interval exception will be throw if aren't it.</br> WARNING: The delimiter are included.
	 * 
	 * @param name The param extension.
	 * @param value The param value.
	 * @param min The min value allowed to this param.
	 * @param minName The extension of the min param
	 * @param max The max value allowed to this param.
	 * @param maxName The extension of the max param
	 * @throws IllegalArgumentException If the param is not included in the interval.
	 */
	public static void checkDouble(String name, double value, String minName, double min, String maxName, double max) {
		checkString("name", name);
		checkString("minName", minName, false, true);
		checkString("maxName", maxName, false, true);

		if (min > max) {
			throw new IllegalArgumentException("The min can't be bigger than the max.");
		}

		if (value < min) {
			throw new IllegalArgumentException("The param" + name + " (" + value + ") can't be smaller than " + min
					+ ((!minName.isEmpty()) ? " (" + minName + ")" : "") + ".");
		}
		if (value > max) {
			throw new IllegalArgumentException("The param" + name + " (" + value + ") can't be bigger than " + max
					+ ((!maxName.isEmpty()) ? " (" + maxName + ")" : "") + ".");
		}
	}
}

package com.domo4pi.utils;

import java.util.concurrent.TimeUnit;

public final class TimeUtils {
	private TimeUtils() {
	}

	public static String getIntervalInfo(long intervalDuration, TimeUnit unit) {
		TimeUnit[] timeUnits = TimeUnit.values();

		long[] values = new long[timeUnits.length];

		for (int i = 0; i < values.length; i++) {
			values[i] = (i == unit.ordinal()) ? intervalDuration : 0;
		}

		for (int i = 0; i < timeUnits.length - 1; i++) {
			long value = values[i];
			long nexUnitValue = timeUnits[i].convert(1, timeUnits[i + 1]);

			long nextUnitDelta = value / nexUnitValue;
			long remainder = value % nexUnitValue;

			values[i] = remainder;
			values[i + 1] = values[i + 1] + nextUnitDelta;
		}

		StringBuilder builder = new StringBuilder();

		for (int i = (timeUnits.length - 1); i >= 0; i--) {
			long valueTemp = values[i];
			TimeUnit unitTemp = timeUnits[i];

			if (valueTemp > 0) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(getValueString(valueTemp, unitTemp));
			}
		}

		if (builder.length() == 0) {
			builder.append(getValueString(intervalDuration, unit));
		}

		return builder.toString();
	}

	private static String getValueString(long value, TimeUnit unit) {
		String result = value + " " + unit.toString().toLowerCase();

		if (value == 1) {
			result = result.substring(0, result.length() - 1);
		}

		return result;
	}
}

package com.citi.reghub.rds.scheduler.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {
	private Util() {
		throw new UnsupportedOperationException("The Util class contains static methods only and cannot be instantiated.");
	}

	public static String formatDate(Calendar cal, String pattern) {
		if (cal == null) {
			return "";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
		return simpleDateFormat.format(cal.getTime());
	}

	public static String formatDate(Calendar cal) {
		String pattern = "EEEEE MMMMM dd yyyy HH:mm:ss.SSSZ";
		return formatDate(cal, pattern);
	}
}

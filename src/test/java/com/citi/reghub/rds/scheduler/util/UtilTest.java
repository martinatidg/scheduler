package com.citi.reghub.rds.scheduler.util;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilTest {
	@Test
	public void testFormatDateDefaultPattern() {
		Calendar timestamp = new GregorianCalendar(2017, 5, 19, 13, 0, 0);
		String expected = "Monday June 19 2017 13:00:00.000-0400";
		String actual = Util.formatDate(timestamp);
		assertEquals("Date format is incorrect.", expected, actual);
	}

	@Test
	public void testFormatDateProvidedPattern() {
		String pattern = "MMMMM dd yyyy HH:mm:ss.SSSZ";
		Calendar timestamp = new GregorianCalendar(2009, 7, 4, 5, 0, 0);
		String expected = "August 04 2009 05:00:00.000-0400";
		String actual = Util.formatDate(timestamp, pattern);
		assertEquals("Date format is incorrect.", expected, actual);
	
		pattern = "yyyy-MM-dd HH:mm:ss.SSSZ";
		timestamp = new GregorianCalendar(2009, 7, 4, 5, 0, 0);
		expected = "2009-08-04 05:00:00.000-0400";
		actual = Util.formatDate(timestamp, pattern);
		assertEquals("Date format is incorrect.", expected, actual);
	}
}

package com.technoverse.platformManager.utils;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date now() {
		return Calendar.getInstance().getTime();
	}

	public static Date resetToStartOfDay(Date d) {
		return Date.from(d.toInstant().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).toInstant());
	}

	/*	public static void main(String[] args) {
			System.out.println(resetToStartOfDay(Calendar.getInstance().getTime()));
		}
	*/
}

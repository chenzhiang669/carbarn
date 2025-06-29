package com.carbarn.inter.utils.tonglian.globalization;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CnpUtils {

	public static final String Y4M2D2H2M2S2 = "yyyyMMddHHmmss";
	public static final String Y4M2D2 = "yyyyMMdd";
	
	public static String getTimeString(String timeFormat, int nextTime) {
		Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.SECOND, nextTime);
		return new SimpleDateFormat(timeFormat).format(beforeTime.getTime());
	}
	
}

package com.frysksoft.nextalarmshare.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String timestampToString( long timestamp) {
		Date date = new Date(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

    public static String timestampToHomeAssistantString(long timestamp) {
		Date date = new Date(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(date);
    }
}

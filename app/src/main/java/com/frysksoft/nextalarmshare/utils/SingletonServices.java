package com.frysksoft.nextalarmshare.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import com.frysksoft.nextalarmshare.services.NextAlarmService;

import static android.content.Context.MODE_PRIVATE;

public class SingletonServices {
	public static String SHARED_PREFS = "NextAlarmServicePrefs";

	private static AlarmManager mAlarmManager;
	private static SharedPreferences mSharedPreferences;
	private static ConnectivityManager mConnectivityManager;
	private static NextAlarmService mNextAlarmService;

	public static SharedPreferences getSharedPreferences(Context context) {
		// First time?
		if (mSharedPreferences == null) {
			// Acquire system service
			mSharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
		}

		// Return cached instance
		return mSharedPreferences;
	}

	public static AlarmManager getAlarmManager(Context context) {
		// First time?
		if (mAlarmManager == null) {
			// Acquire system service
			mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		}

		// Return cached instance
		return mAlarmManager;
	}

	public static ConnectivityManager getConnectivityManager(Context context) {
		// First time?
		if (mConnectivityManager == null) {
			// Acquire system service
			mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}

		// Return cached instance
		return mConnectivityManager;
	}

	public static NextAlarmService getNextAlarmService() {
		// First time?
		if (mNextAlarmService == null) {
			// Acquire system service
			mNextAlarmService = new NextAlarmService();
		}

		// Return cached instance
		return mNextAlarmService;
	}
}
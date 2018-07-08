package com.frysksoft.nextalarmshare.receivers;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.frysksoft.nextalarmshare.utils.SingletonServices;

public class AlarmChanged extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// Make sure the right intent was provided
		if (intent.getAction().equals(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED)) {
			SingletonServices.getNextAlarmService().setNextAlarm(context);
		}
	}
}
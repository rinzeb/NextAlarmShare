package com.frysksoft.nextalarmshare.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.frysksoft.nextalarmshare.utils.SingletonServices;

public class BootCompleted extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// Make sure the right intent was provided
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			SingletonServices.getNextAlarmService().setNextAlarm(context);
		}
	}
}
package com.frysksoft.nextalarmshare.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.EditText;
import com.frysksoft.nextalarmshare.R;
import com.frysksoft.nextalarmshare.alarms.SystemClock;
import com.frysksoft.nextalarmshare.tasks.SendAlarm;
import com.frysksoft.nextalarmshare.utils.SingletonServices;
import com.frysksoft.nextalarmshare.utils.Utils;

public class NextAlarmService extends Service {

	public NextAlarmService() {
	}

	public void setNextAlarm(Context context) {
		long timestamp = SystemClock.getNextAlarmTriggerTimestamp(context);
        String haTime = Utils.timestampToHomeAssistantString(timestamp);
        SharedPreferences sharedPreferences = SingletonServices.getSharedPreferences(this);
        String localUrl = this.getSharedPrefIfExists(R.id.homeAssistantUrlValue, sharedPreferences);
        String remoteUrl = this.getSharedPrefIfExists(R.id.homeAssistantRemoteUrlValue, sharedPreferences);
        String password = this.getSharedPrefIfExists(R.id.homeAssistantTokenValue, sharedPreferences);
        String entity =  this.getSharedPrefIfExists(R.id.homeAssistantEntityValue, sharedPreferences);
        this.setAlarmInHomeAssistant(haTime, localUrl, entity, password);
        this.setAlarmInHomeAssistant(haTime, remoteUrl, entity, password);
    }

    private String getSharedPrefIfExists(int id, SharedPreferences preferences) {
	    if (preferences.contains(Integer.toString(id))) {
            return preferences.getString(Integer.toString(id), "");
        } else {
	        return "";
        }
    }

    private void setAlarmInHomeAssistant(String haTime, String url, String entity, String password) {
	    if (url == null || url.length() == 0) {
	        return;
        }
	    if (!url.endsWith("/")) {
	        url += "/";
        }
	    url += "api/services/input_datetime/set_datetime";
        new SendAlarm().execute(url, haTime, entity, password);
    }

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}

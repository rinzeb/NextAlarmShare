package com.frysksoft.nextalarmshare.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import com.frysksoft.nextalarmshare.R;
import com.frysksoft.nextalarmshare.alarms.SystemClock;
import com.frysksoft.nextalarmshare.tasks.SendAlarm;
import com.frysksoft.nextalarmshare.utils.SingletonServices;
import com.frysksoft.nextalarmshare.utils.Utils;

public class NextAlarmService extends Service {

    private int range = 120;
    private boolean _initialized = false;

	public NextAlarmService() {
	}

	public boolean isInitialized() {
	    return _initialized;
    }

	public void setNextAlarm(Context context) {
        Log.i("NextAlarmShare", "SetNextAlarm()");
        SharedPreferences sharedPreferences = SingletonServices.getSharedPreferences(context);
		long timestamp = SystemClock.getNextAlarmTriggerTimestamp(context);
        int seekbarTimeOffset = this.getSharedIntPrefIfExists(R.id.timeOffsetBar, sharedPreferences);
        int actualTimeOffset = this.getSeekbarProgressTime(seekbarTimeOffset);
        long actualTimeOffsetMs = actualTimeOffset * 60 * 1000;
        String haTime = Utils.timestampToHomeAssistantString(timestamp + actualTimeOffsetMs);
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

    private int getSharedIntPrefIfExists(int id, SharedPreferences preferences) {
        if (preferences.contains(Integer.toString(id))) {
            return preferences.getInt(Integer.toString(id), this.range/2);
        } else {
            return this.range/2;
        }
    }

    private int getSeekbarProgressTime(int progress) {
        return progress - this.range/2;
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
    public void onDestroy() {
	    super.onDestroy();
	    this._initialized = false;
    }

	@Override
	public void onCreate() {
		super.onCreate();
		this._initialized = true;
        this.setNextAlarm(this);
	}

	@Override public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}

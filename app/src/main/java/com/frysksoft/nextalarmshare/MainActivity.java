package com.frysksoft.nextalarmshare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.frysksoft.nextalarmshare.alarms.SystemClock;
import com.frysksoft.nextalarmshare.utils.SingletonServices;
import com.frysksoft.nextalarmshare.utils.Utils;

public class MainActivity extends Activity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("NextAlarmShare", "onCreate");
		this.prepareButtons();
		this.getSharedPrefs();
	}

    private void getSharedPrefs() {
        SharedPreferences preferences = SingletonServices.getSharedPreferences(this);
        this.setTextIfPreferenceExists(R.id.homeAssistantEntityValue, preferences);
        this.setTextIfPreferenceExists(R.id.homeAssistantTokenValue, preferences);
        this.setTextIfPreferenceExists(R.id.homeAssistantRemoteUrlValue, preferences);
        this.setTextIfPreferenceExists(R.id.homeAssistantUrlValue, preferences);
    }

    private void setTextIfPreferenceExists(int id, SharedPreferences preferences) {
        EditText et = findViewById(id);
        if (preferences.contains(Integer.toString(id))) {
            et.setText(preferences.getString(Integer.toString(id), ""));
        }
    }

    private void setSharedPrefs() {
        SharedPreferences.Editor editor = getSharedPreferences(SingletonServices.SHARED_PREFS, MODE_PRIVATE).edit();
        this.setSharedPref(R.id.homeAssistantEntityValue, editor);
        this.setSharedPref(R.id.homeAssistantTokenValue, editor);
        this.setSharedPref(R.id.homeAssistantRemoteUrlValue, editor);
        this.setSharedPref(R.id.homeAssistantUrlValue, editor);
        editor.apply();
    }

    private void setSharedPref(int id, SharedPreferences.Editor editor) {
        editor.putString( Integer.toString(id), ((EditText) findViewById(id)).getText().toString());
    }

    private void prepareButtons() {
		Button button = findViewById(R.id.sendAlarmButton);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Log.i("NextAlarmShare", "setNextAlarm");
                ((MainActivity)v.getContext()).setSharedPrefs();
				SingletonServices.getNextAlarmService().setNextAlarm(v.getContext());
			}
		});
	}

	@Override protected void onResume() {
		super.onResume();
		this.updateNextAlarmText();
		Log.i("NextAlarmShare", "onResume");
	}

	private void updateNextAlarmText() {
		long timestamp = SystemClock.getNextAlarmTriggerTimestamp(this);
		TextView tv = findViewById(R.id.currentAlarmTextValue);
		tv.setText(Utils.timestampToString(timestamp));
	}
}

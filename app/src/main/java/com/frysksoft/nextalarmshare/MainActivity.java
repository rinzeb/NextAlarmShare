package com.frysksoft.nextalarmshare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.frysksoft.nextalarmshare.alarms.SystemClock;
import com.frysksoft.nextalarmshare.utils.SingletonServices;
import com.frysksoft.nextalarmshare.utils.Utils;

public class MainActivity extends Activity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("NextAlarmShare", "onCreate");
        this.prepareButtons();
        this.prepareSeekbar();
		this.getSharedPrefs();
	}


    @Override protected void onResume() {
        super.onResume();
        this.updateNextAlarmText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                String version = "unknown";
                try {
                    PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "NextAlarmShare version: " + version, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

    private void getSharedPrefs() {
        SharedPreferences preferences = SingletonServices.getSharedPreferences(this);
        this.setTextIfPreferenceExists(R.id.homeAssistantEntityValue, preferences);
        this.setTextIfPreferenceExists(R.id.homeAssistantTokenValue, preferences);
        this.setTextIfPreferenceExists(R.id.homeAssistantRemoteUrlValue, preferences);
        this.setTextIfPreferenceExists(R.id.homeAssistantUrlValue, preferences);
        SeekBar sb = findViewById(R.id.timeOffsetBar);
        if (preferences.contains(Integer.toString(R.id.timeOffsetBar))) {
            sb.setProgress(preferences.getInt(Integer.toString(R.id.timeOffsetBar), 0));
        }
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
        editor.putInt( Integer.toString(R.id.timeOffsetBar), ((SeekBar) findViewById(R.id.timeOffsetBar)).getProgress());
        editor.apply();
    }

    private void setSharedPref(int id, SharedPreferences.Editor editor) {
        editor.putString( Integer.toString(id), ((EditText) findViewById(id)).getText().toString());
    }

    private void prepareButtons() {
		Button button = findViewById(R.id.sendAlarmButton);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
                ((MainActivity)v.getContext()).setNextAlarm();
			}
		});
	}

    private void prepareSeekbar() {
        SeekBar seek = findViewById(R.id.timeOffsetBar);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int range = seekBar.getMax() - 0; //minimum is always 0
                int actualProgress = progress - range/2;
                String progressText = Integer.toString(actualProgress) + "min";
                TextView tv = findViewById(R.id.timeOffsetChosenLabel);
                tv.setText(progressText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setNextAlarm() {
        this.setSharedPrefs();
        SingletonServices.getNextAlarmService().setNextAlarm(this);
    }

	private void updateNextAlarmText() {
		long timestamp = SystemClock.getNextAlarmTriggerTimestamp(this);
		TextView tv = findViewById(R.id.currentAlarmTextValue);
		tv.setText(Utils.timestampToString(timestamp));
	}
}

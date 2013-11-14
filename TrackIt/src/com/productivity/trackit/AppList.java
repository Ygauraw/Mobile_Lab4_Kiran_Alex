package com.productivity.trackit;

import java.util.Calendar;

import com.productivity.trackit.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class AppList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_list);
		if(!SysBootBroadcastReceiver.isServiceInitiated()){
			Calendar cal = Calendar.getInstance();

			//set AppUsageService as the pending intent for the repeated service
			Intent startServiceIntent = new Intent(this, AppUsageService.class);
			PendingIntent pintent = PendingIntent.getService(this, 0, startServiceIntent, 0);

			AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			// Start every 0.1 seconds
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), ProdUtils.SERVICE_PERIOD, pintent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_list, menu);
		return true;
	}

}

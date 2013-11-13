package com.example.trackit;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SysBootBroadcastReceiver extends BroadcastReceiver {
	
	static Boolean serviceInitiated = false;
	
	@Override
    public void onReceive(Context context, Intent intent) {
		
		//set boolean to true for repeating service already initiated
		serviceInitiated = true;
		
		Calendar cal = Calendar.getInstance();

		//set AppUsageService as the pending intent for the repeated service
		Intent startServiceIntent = new Intent(context, AppUsageService.class);
		PendingIntent pintent = PendingIntent.getService(context, 0, startServiceIntent, 0);

		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		// Start every 0.1 seconds
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 100, pintent); 
    }
	
	/**
	 * Checks if the repeating AppUsgeService has already been initiated
	 * @return true if repeating service already initiated
	 */
	static public Boolean isServiceInitiated(){
		return serviceInitiated;
	}
}

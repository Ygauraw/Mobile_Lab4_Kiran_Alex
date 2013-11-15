package com.example.trackit;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class AppList extends Activity {

	TextView text1;

    ExpandableAppsListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> labels;
    HashMap<String, List<String>> appsByLabel;
	
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
		
		text1 = (TextView)findViewById(R.id.productivity_score);
		
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandable_Apps_List);
        
        // preparing list data
        prepareAppsListData();
        
        //initialize adapter
        listAdapter = new ExpandableAppsListAdapter(this, labels, appsByLabel);
        
        // setting list adapter
        expListView.setAdapter(listAdapter);
		
	}

	private void prepareAppsListData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_list, menu);
		return true;
	}
	
	private void changeAppLabel(){
		
	}

}

package com.example.trackit;

import java.util.ArrayList;
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
        
     /*// Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });*/
		
	}

	private void prepareAppsListData() {
		//initiate the lists of labels and apps by label
		labels = new ArrayList<String>();
		appsByLabel = new HashMap<String, List<String>>();
		
		//add different labels
		labels.add("Productive Apps");
		labels.add("Unproductive Apps");
		labels.add("Unlabeled Apps");
		
		//get all the apps from the database to put in lists
		AppsDataSource appData = new AppsDataSource(this);
		appData.open();
		List<AppInfo> allApps;
		
		allApps = appData.getAllApps();
		
		List<String> productiveApps = new ArrayList<String>();
		List<String> unproductiveApps = new ArrayList<String>();
		List<String> unlabeledApps = new ArrayList<String>();
		
		int i;
		AppInfo currApp;
		String currAppLabel, appDisplay;
		for(i = 0; i < allApps.size(); i++){
			currApp = allApps.get(i);
			currAppLabel = currApp.getLabel();
			if(currAppLabel == ProdUtils.PRODUCTIVE_LABEL){
				
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_list, menu);
		return true;
	}

}

package com.example.trackit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AppList extends Activity {

	TextView text1;

    ExpandableAppsListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> labels;
    HashMap<String, List<String>> appsByLabel;
	AppsDataSource appData = new AppsDataSource(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_list);
		if(!SysBootBroadcastReceiver.isServiceInitiated()){
			SysBootBroadcastReceiver.setServiceInitiated(true);
			Calendar cal = Calendar.getInstance();

			//set AppUsageService as the pending intent for the repeated service
			Intent startServiceIntent = new Intent(this, AppUsageService.class);
			PendingIntent pintent = PendingIntent.getService(this, 0, startServiceIntent, 0);

			AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			// Start every 0.1 seconds
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), ProdUtils.SERVICE_PERIOD, pintent);
		}
		
		//get my current productivity
		double productivity = getTotalProductivity();
		DecimalFormat df = new DecimalFormat("#.##");
		
		//display the productivity
		text1 = (TextView)findViewById(R.id.productivity_score);
		text1.setText("Your Productivity is " + df.format(productivity*100) + "%");
		
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandable_Apps_List);
        
        // preparing list data
        prepareAppsListData();
        
        //initialize adapter
        listAdapter = new ExpandableAppsListAdapter(this, labels, appsByLabel);
        
        // setting list adapter
        expListView.setAdapter(listAdapter);
        
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int labelPosition, long id) {
            	/*Toast.makeText(getApplicationContext(),
                "Group Clicked " + labels.get(labelPosition),
                Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int labelPosition) {
                Toast.makeText(getApplicationContext(),
                        labels.get(labelPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int labelPosition) {
                Toast.makeText(getApplicationContext(),
                        labels.get(labelPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int labelPosition, int appPosition, long id) {
            	
            	LayoutInflater layoutInflater 
                = (LayoutInflater)getBaseContext()
                 .getSystemService(LAYOUT_INFLATER_SERVICE);  
               View popupView = layoutInflater.inflate(R.layout.select_label_popup, null);
               
               final PopupWindow popupWindow = new PopupWindow(
                       popupView, LayoutParams.WRAP_CONTENT,  
                             LayoutParams.WRAP_CONTENT);
               
               final Button btnProductive = (Button)popupView.findViewById(R.id.set_productive);
               final Button btnUnproductive = (Button)popupView.findViewById(R.id.set_unproductive);
               final Button btnNoLabel = (Button)popupView.findViewById(R.id.set_nolabel);
               final Button btnCancel = (Button)popupView.findViewById(R.id.cancel);


               final int whichLabel = labelPosition;
               final int whichApp = appPosition;
               
               OnClickListener listener = new Button.OnClickListener() {
            	    @Override
            	    public void onClick(View v) {
            	    	String newLabel;
            	        if (v.equals(btnProductive)) {
            	        	newLabel = ProdUtils.PRODUCTIVE_LABEL;
            	        	Toast.makeText(getApplicationContext(), "OK", whichApp);
            	        } 
            	        else if(v.equals(btnUnproductive)) {
            	            newLabel = ProdUtils.UNPRODUCTIVE_LABEL;
            	        }
            	        else{
            	        	newLabel = ProdUtils.NO_LABEL;
            	        }
            	        
            	        //get app name
            	        String appName;
            	        List<String> apps;
            	        //get the selected app
            	        apps = appsByLabel.get(labels.get(whichLabel));
            	        //get the name of the app
            	        appName = apps.get(whichApp).substring(0, apps.get(whichApp).lastIndexOf(" "));
            	        //trim the string to keep only app name
            	        appName = appName.trim();
            	        //set the new label for the app
            	        setLabel(appName, newLabel);
            	        
            	        //get my current productivity
            			double productivity = getTotalProductivity();
            			DecimalFormat df = new DecimalFormat("#.##");
            					
            			//display the productivity
            			text1 = (TextView)findViewById(R.id.productivity_score);
            			text1.setText("Your Productivity is " + df.format(productivity*100) + "%");
            	        
             		    // preparing list data
             	        prepareAppsListData();
             	        
             	        //initialize adapter
             	        listAdapter = new ExpandableAppsListAdapter(getApplicationContext(), labels, appsByLabel);
             	        
             	        // setting list adapter
             	        expListView.setAdapter(listAdapter);
            	        popupWindow.dismiss();
            	    }
            	};
               
               //set the listeners for the buttons to use the above logic
               btnProductive.setOnClickListener(listener);
               
               btnUnproductive.setOnClickListener(listener);
               
               btnNoLabel.setOnClickListener(listener);
               
               //button to cancel if no change is desired
               btnCancel.setOnClickListener(new Button.OnClickListener(){

            	     @Override
            	     public void onClick(View v) {
            	      // TODO Auto-generated method stub
            	      popupWindow.dismiss();
            	     }});

               //popup window drops down from where you clicked
               popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0); 
               
               return false;
            }
        });		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		//get my current productivity
		double productivity = getTotalProductivity();
		DecimalFormat df = new DecimalFormat("#.##");
				
		//display the productivity
		text1 = (TextView)findViewById(R.id.productivity_score);
		text1.setText("Your Productivity is " + df.format(productivity*100) + "%");
				
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
		//initiate the lists of labels and apps by label
		labels = new ArrayList<String>();
		appsByLabel = new HashMap<String, List<String>>();
		
		//add different labels
		labels.add("Productive Apps");
		labels.add("Unproductive Apps");
		labels.add("Unlabeled Apps");
		
		//get all the apps from the database to put in lists
		appData.open();
		List<AppInfo> allApps;
		
		allApps = appData.getAllApps();
		
		//lists of productive, unproductive, and unlabeled apps
		List<String> productiveApps = new ArrayList<String>();
		List<String> unproductiveApps = new ArrayList<String>();
		List<String> unlabeledApps = new ArrayList<String>();
		
		int i;
		AppInfo currApp;
		String currAppLabel, currAppDisplay;
		//loop to put the apps in designated lists
		for(i = 0; i < allApps.size(); i++){
			currApp = allApps.get(i);
			currAppLabel = currApp.getLabel();
			String formatRunTime = String.format(Locale.getDefault(),"%02d:%02d:%02d", 
					TimeUnit.MILLISECONDS.toHours(currApp.getRunTime()),
					TimeUnit.MILLISECONDS.toMinutes(currApp.getRunTime()) -  
					TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(currApp.getRunTime())), // The change is in this line
					TimeUnit.MILLISECONDS.toSeconds(currApp.getRunTime()) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currApp.getRunTime())));  
			currAppDisplay = String.format("%30s %-8s", currApp.getName(), formatRunTime);
			if(currAppLabel.equals(ProdUtils.PRODUCTIVE_LABEL)){
				productiveApps.add(currAppDisplay);
			}
			else if(currAppLabel.equals(ProdUtils.UNPRODUCTIVE_LABEL)){
				unproductiveApps.add(currAppDisplay);
			}
			else{
				unlabeledApps.add(currAppDisplay);
			}
		}
		
		//put apps into the groups
		appsByLabel.put(labels.get(0), productiveApps);
		appsByLabel.put(labels.get(1), unproductiveApps);
		appsByLabel.put(labels.get(2), unlabeledApps);
		
		appData.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_list, menu);
		return true;
	}
	
	private void setLabel(String appName, String newLabel){
		int i;
		//get all the apps
		List<AppInfo> allApps;
		//open database for editing
		appData.open();
		
		//get all the apps
		allApps = appData.getAllApps();
		
		//app iterator
		AppInfo currApp;
		//variables to store for current app
		long currAppRunTime, currAppStartTime;
		String currAppDateRecorded, currAppPackageName;
		boolean currAppActive;
		
		
		for(i = 0; i < allApps.size(); i++){
			currApp = allApps.get(i);
			if(currApp.getName().equals(appName)){
				//store the current app's info
				currAppRunTime = currApp.getRunTime();
				currAppStartTime = currApp.getStartTime();
				currAppDateRecorded = currApp.getDateRecorded();
				currAppActive = currApp.getActive();
				currAppPackageName = currApp.getPackageInfo().packageName;
				//delete the current app from database
				appData.deleteApp(currApp);
				//add app back to database with new label
				appData.createApp(currAppPackageName, newLabel, currAppRunTime,
						currAppStartTime, currAppActive, currAppDateRecorded);
				break;
			}
				
		}
		appData.close();
	}
	
	private double getTotalProductivity(){
		
		List<AppInfo> allApps;
		//open database for editing
		appData.open();
				
		//get all the apps
		allApps = appData.getAllApps();
		
		CalculateProductivity prodCalc = new CalculateProductivity(allApps);
		double productivity = prodCalc.getProductivity();
		
		appData.close();
		
		return productivity;
		
	}

}

package com.example.trackit;

import android.content.Context;
import android.content.pm.PackageInfo;

//class to store info for each application
public class AppInfo {
	
	//name of app
	private String name;
	//label (productive, unproductive, nolabel)
	private String label;
	//time that the app is running
	private long runTime;
	//start time of the app
	private long startTime;
	//package of the app
	private PackageInfo appPackage;
	//is the app currently active
	private boolean active;
	private Context mContext;
	//ID for the app
	private long appID;
	//record the date (reset counts daily)
	private String dateRecorded;
	
	//constructor
	public AppInfo(PackageInfo pinfo, Context context){
		this.appPackage = pinfo;
		mContext = context;
		setName();
		runTime = 0;
	}
	
	//set app ID
	public void setId(long id){
		this.appID = id;
	}
	
	//get app ID
	public long getId(){
		return appID;
	}
	
	//set the label (can be changed according to user)
	public void setLabel(String label){
		this.label = label;
	}
	
	//get the app's label
	public String getLabel(){
		return label;
	}
	
	//set the start time of the app
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}
	
	//get the start time
	public long getStartTime(){
		return startTime;
	}
	
	//get whether active or not
	public boolean getActive(){
		return active;
	}
	
	//set active or not
	public void setActive(boolean active){
		this.active = active;
	}
	
	//set the run time (as an app runs)
	public void setRunTime(long runTime){
		this.runTime = runTime;
	}
	
	//get the app total run time
	public long getRunTime(){
		return runTime;
	}
	
	//set the date recorded
	public void setDateRecorded(String dateRecorded){
		this.dateRecorded = dateRecorded;
	}
	
	//get the last date (update daily)
	public String getDateRecorded(){
		return dateRecorded;
	}
	
	//set the name of the app (automatic)
	private void setName(){
		String appName;
		//get app name from the package
		appName = appPackage.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
		this.name = appName;
	}
	
	//get the app name
	public String getName(){
		return name;
	}

	//get the package info for the app
	public PackageInfo getPackageInfo(){
		return appPackage;
	}

}

package com.example.trackit;

import android.content.Context;
import android.content.pm.PackageInfo;

public class AppInfo {
	
	private String name;
	private String label;
	private long runTime;
	private long startTime;
	private PackageInfo appPackage;
	private boolean active;
	private Context mContext;
	private long appID;
	private String dateRecorded;
	
	public AppInfo(PackageInfo pinfo, Context context){
		this.appPackage = pinfo;
		mContext = context;
		setName();
		runTime = 0;
	}
	
	public void setId(long id){
		this.appID = id;
	}
	
	public long getId(){
		return appID;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}
	
	public long getStartTime(){
		return startTime;
	}
	
	public boolean getActive(){
		return active;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public void setRunTime(long runTime){
		this.runTime = runTime;
	}
	
	public long getRunTime(){
		return runTime;
	}
	
	public void setDateRecorded(String dateRecorded){
		this.dateRecorded = dateRecorded;
	}
	
	public String getDateRecorded(){
		return dateRecorded;
	}
	
	private void setName(){
		String appName;
		appName = appPackage.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
		this.name = appName;
	}
	
	public String getName(){
		return name;
	}

	public PackageInfo getPackageInfo(){
		return appPackage;
	}

}

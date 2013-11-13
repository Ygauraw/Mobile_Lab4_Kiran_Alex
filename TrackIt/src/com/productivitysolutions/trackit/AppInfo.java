package com.productivitysolutions.trackit;

import android.content.Context;
import android.content.pm.PackageInfo;

public class AppInfo {
	
	String name;
	String label;
	long runTime;
	PackageInfo appPackage;
	boolean active;
	Context mContext;
	
	public AppInfo(PackageInfo pinfo, Context context){
		this.appPackage = pinfo;
		mContext = context;
		setName();
		runTime = 0;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public long getRunTime(){
		return runTime;
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

package com.example.trackit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class AppUsageService extends Service {
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AppUsageService", "Received start id " + startId + ": " + intent);
        //retrieve and store foreground app
        getForegroundApp();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_NOT_STICKY;
    }
	
	@Override
    public void onDestroy() {
        Log.i("AppUsageService", "Service stopped");
    }
	
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        AppUsageService getService() {
            return AppUsageService.this;
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
    
    /**
     * Gets the current foreground app being run by the user
     * Updates the SQLite database as to what app is in the foreground
     */
    private void getForegroundApp(){
    	//create object for app database
    	AppsDataSource appData = new AppsDataSource(this);
    	try{
			//open database
			appData.open();
			//get all apps in database
			List<AppInfo> allApps = appData.getAllApps();
  	
			//initialize variable for current app that is in foreground
			AppInfo currentActiveApp = null;
			
			//initialize activitymanager variable
			ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			//get running processes
			List<RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
			//FOR DEBUGGING
			int j;
			for(RunningAppProcessInfo rapi : runningAppProcesses){
				String[] packages = {""};
				if (rapi.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
						rapi.importance != RunningAppProcessInfo.IMPORTANCE_BACKGROUND){
					//list all package names of foreground process
					packages = rapi.pkgList;
				
					for(j=0; j< packages.length; j++){
						//print all packages
						Log.i(packages[j],String.valueOf(j));
					}
				}
			}
			//END FOR DEBUGGING
			//iterator for running processes
			Iterator<RunningAppProcessInfo> i = runningAppProcesses.iterator();
			while (i.hasNext()) {
				//a running process
				RunningAppProcessInfo info = i.next();
				//list all package names of foreground process
				String[] packages = info.pkgList;
				//get first package (main package)
				String foregroundpackage = packages[0];
				//if the current running process is in foreground and is not the android system
				if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
						info.importance != RunningAppProcessInfo.IMPORTANCE_BACKGROUND && 
						!foregroundpackage.equals("android")){
					//if top packages running are for launcher then don't record them
					if(!foregroundpackage.equals("com.android.launcher") &&
						!foregroundpackage.equals("com.android.systemui") && 
						!foregroundpackage.equals("com.google.android.syncadapters.contacts")){
						//get date and format
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
						Date date = new Date();
						Boolean exists = false;
						//an array to hold all matches from the database
						List<AppInfo> curApps = new ArrayList<AppInfo>();
						//see if app is already in database
						for(AppInfo app : allApps){
							//get all database matches
							if(app.getPackageInfo().packageName.equals(foregroundpackage)){
								curApps.add(app);
								exists = true;
							}
						}
						//iterator for running processes
						Iterator<AppInfo> iapp = curApps.iterator();
						while(iapp.hasNext()){
							AppInfo anApp = iapp.next();
							//remove all but the latest updated appinfo (the last one stored in the database)
							if(curApps.size() > 1){
								iapp.remove();
								appData.deleteApp(anApp);
							}
						}
						//create if new app
						if(!exists){
							currentActiveApp = appData.createApp(foregroundpackage, ProdUtils.NO_LABEL, 0, 
																	0, true, dateFormat.format(date));
						}
						//if app already exists get last one updated (only one not removed)
						else{
							currentActiveApp = curApps.get(0);
						}
					
						PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
						//if app is driving UI and screen is on
						if(pm.isScreenOn()){
							long newRunTime;
							long currentTime = System.currentTimeMillis();
							long startTime = currentActiveApp.getStartTime();
							//if this app's start time is too far in the past ignore the start time
							if((currentTime - startTime) > ProdUtils.BAD_TIME_GAP){
								startTime = currentTime;
							}
							//if still from today
							if(currentActiveApp.getDateRecorded().equals(dateFormat.format(date))){
								//set new run time
								newRunTime = currentActiveApp.getRunTime() + (currentTime - startTime);
								//log the current active app and its runtime
								Log.i(currentActiveApp.getName(), String.valueOf(newRunTime));
							}
							else{
								//if new day delete all app's data
								for(AppInfo app : allApps){
									appData.deleteApp(app);
									appData.createApp(app.getPackageInfo().packageName, app.getLabel(), 
											0, app.getStartTime(), false, dateFormat.format(date));
								}
								newRunTime = 0;
							}
							//delete old app data
							appData.deleteApp(currentActiveApp);
							//replace app data in database
							currentActiveApp = appData.createApp(currentActiveApp.getPackageInfo().packageName, 
									currentActiveApp.getLabel(), newRunTime, currentTime, false, dateFormat.format(date));
						}
					}
					//break if found foreground app
					break;
				}
			}
		}finally{
			//close database
			appData.close();
		}
    }
}

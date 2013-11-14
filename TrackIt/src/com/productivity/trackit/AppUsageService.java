package com.productivity.trackit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.IBinder;
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
    	AppsDataSource appData = new AppsDataSource(this);
    	appData.open();
    	List<AppInfo> allApps = appData.getAllAppss();
    	AppInfo lastActiveApp = null;
    	AppInfo currentActiveApp = null;
    	for(AppInfo app : allApps){
    		if(app.getActive()){
    			lastActiveApp = app;
    			break;
    		}
    	}
    	
    	ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = runningAppProcesses.iterator();
        while (i.hasNext()) {
            RunningAppProcessInfo info = i.next();

            if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) 
                {
            		String[] packages = info.pkgList;
            		String foregroundpackage = packages[0];
            		PackageManager pm = this.getPackageManager();
            		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					Date date = new Date();
            		try {
						PackageInfo packageinfo = pm.getPackageInfo (foregroundpackage, 0);
						Boolean exists = false;
						for(AppInfo app : allApps){
				    		if(app.getPackageInfo().equals(packageinfo)){
				    			currentActiveApp = app;
				    			exists = true;
				    			break;
				    		}
				    	}
						if(!exists){
							currentActiveApp = appData.createApp(foregroundpackage, ProdUtils.NO_LABEL, 0, 
														0, true, dateFormat.format(date));
						}
						if(!currentActiveApp.equals(lastActiveApp)){
							if(lastActiveApp != null){
								long newRunTime;
								if(lastActiveApp.getDateRecorded().equals(dateFormat.format(date))){
									newRunTime = lastActiveApp.getRunTime() + (System.currentTimeMillis() - lastActiveApp.getStartTime());
								}
								else{
									Calendar c = Calendar.getInstance(); //midnight
								    c.set(Calendar.HOUR_OF_DAY, 0);
								    c.set(Calendar.MINUTE, 0);
								    c.set(Calendar.SECOND, 0);
								    c.set(Calendar.MILLISECOND, 0);
									newRunTime = System.currentTimeMillis() - c.getTimeInMillis();
								}
								appData.deleteApp(lastActiveApp);
								appData.createApp(lastActiveApp.getPackageInfo().packageName, lastActiveApp.getLabel(), 
										newRunTime, 0, false, dateFormat.format(date));
							}
							appData.deleteApp(currentActiveApp);
							appData.createApp(currentActiveApp.getPackageInfo().packageName, currentActiveApp.getLabel(), 
									currentActiveApp.getRunTime(), System.currentTimeMillis(), true, dateFormat.format(date));
						}
						
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
               }
           }
        appData.close();
    }

}

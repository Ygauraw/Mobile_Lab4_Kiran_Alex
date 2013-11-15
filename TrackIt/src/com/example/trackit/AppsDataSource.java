package com.example.trackit;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private AppOpenHelper dbHelper;
  //define the columns of the databse
  private String[] allColumns = { AppOpenHelper.COLUMN_ID,
		  						  AppOpenHelper.COLUMN_PACKAGE,
		  						  AppOpenHelper.COLUMN_LABEL,
		  						  AppOpenHelper.COLUMN_RUNTIME,
		  						  AppOpenHelper.COLUMN_STARTTIME,
		  						  AppOpenHelper.COLUMN_ACTIVE,
		  						  AppOpenHelper.COLUMN_DATERECORDED};
  Context mContext;
//Initialize database helper and context
  public AppsDataSource(Context context) {
    dbHelper = new AppOpenHelper(context);
    mContext = context;
  }
//Open database
  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }
//Close database
  public void close() {
    dbHelper.close();
  }
//Creates an app and adds it to the database
  public AppInfo createApp(String appPackage, String label, long runTime, long startTime, boolean active, String dateRecorded) {
    ContentValues values = new ContentValues();
    values.put(AppOpenHelper.COLUMN_PACKAGE, appPackage);
    values.put(AppOpenHelper.COLUMN_LABEL, label);
    values.put(AppOpenHelper.COLUMN_RUNTIME, runTime);
    values.put(AppOpenHelper.COLUMN_STARTTIME, startTime);
    values.put(AppOpenHelper.COLUMN_ACTIVE, String.valueOf(active));
    values.put(AppOpenHelper.COLUMN_DATERECORDED, dateRecorded);
    //add values to database
    long insertId = database.insert(AppOpenHelper.APP_TABLE_NAME, null,
        values);
    //get a cursor for where the new app was added
    Cursor cursor = database.query(AppOpenHelper.APP_TABLE_NAME,
        allColumns, AppOpenHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    //get a new app object from the database
    AppInfo newApp = cursorToApp(cursor);
    //close the cursor
    cursor.close();
    return newApp;
  }
//deletes an app from the database
  public void deleteApp(AppInfo app) {
    long id = app.getId();
    database.delete(AppOpenHelper.APP_TABLE_NAME, AppOpenHelper.COLUMN_ID
        + " = " + id, null);
  }
//Returns a list of all apps in the database
  public List<AppInfo> getAllApps() {
	//Initialize list
    List<AppInfo> apps = new ArrayList<AppInfo>();
    //get cursor for database
    Cursor cursor = database.query(AppOpenHelper.APP_TABLE_NAME,
        allColumns, null, null, null, null, null);
 
    if(cursor.moveToFirst()){
    	//iterate through database
    	while (!cursor.isAfterLast()) {
    		//get the apps from database
    		AppInfo app = cursorToApp(cursor);
    		if(app != null){
    			//store app in list
    			apps.add(app);
    		}
    		cursor.moveToNext();
    	}
    }
    //close the cursor
    cursor.close();
    return apps;
  }
//creates an appinfo object from the location of the cursor in the database
  private AppInfo cursorToApp(Cursor cursor) {
	  //initialize a package manager
	  PackageManager pm = mContext.getPackageManager();
	  PackageInfo packageinfo = null;
	  AppInfo app = null;
		try {
			//get the packageinfo object form the package name in the first column
			packageinfo = pm.getPackageInfo (cursor.getString(1), 0);
			//creates a new appinfo object
			app = new AppInfo(packageinfo, mContext);
			//get the app's database id
			app.setId(cursor.getLong(0));
			//get the app's label
			app.setLabel(cursor.getString(2));
			//get the app's runtime
			app.setRunTime(cursor.getLong(3));
			//get the app's start time
			app.setStartTime(cursor.getLong(4));
			//get whether the app is active or not
			app.setActive(Boolean.valueOf(cursor.getString(5)));
			//get date that the runtime was recorded
			app.setDateRecorded(cursor.getString(6));
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    return app;
  }
} 


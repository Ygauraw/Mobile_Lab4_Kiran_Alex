package com.productivity.trackit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppOpenHelper extends SQLiteOpenHelper{
	
	public static final String APP_TABLE_NAME = "applist";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PACKAGE = "package";
	public static final String COLUMN_RUNTIME = "runtime";
	public static final String COLUMN_STARTTIME = "starttime";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_DATERECORDED = "daterecorded";

	private static final String DATABASE_NAME = "applist.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ APP_TABLE_NAME + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PACKAGE + " package name of app, " 
			+ COLUMN_RUNTIME + " time app was open today, " 
			+ COLUMN_STARTTIME + " time app was started, " 
			+ COLUMN_ACTIVE + " is this app currently running in foreground, " 
			+ COLUMN_DATERECORDED + " date of runtime);";

	public AppOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(AppOpenHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + APP_TABLE_NAME);
		onCreate(db);
	}
}

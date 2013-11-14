package com.productivity.trackit;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AppsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private AppOpenHelper dbHelper;
  private String[] allColumns = { AppOpenHelper.COLUMN_ID,
		  						  AppOpenHelper.COLUMN_PACKAGE,
		  						  AppOpenHelper.COLUMN_RUNTIME,
		  						  AppOpenHelper.COLUMN_STARTTIME,
		  						  AppOpenHelper.COLUMN_ACTIVE,
		  						  AppOpenHelper.COLUMN_DATERECORDED};

  public AppsDataSource(Context context) {
    dbHelper = new AppOpenHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public AppInfo createApp(String appPackage, long runTime, long startTime, boolean active, String dateRecorded) {
    ContentValues values = new ContentValues();
    values.put(AppOpenHelper.COLUMN_PACKAGE, appPackage);
    values.put(AppOpenHelper.COLUMN_RUNTIME, runTime);
    values.put(AppOpenHelper.COLUMN_STARTTIME, startTime);
    values.put(AppOpenHelper.COLUMN_ACTIVE, String.valueOf(active));
    values.put(AppOpenHelper.COLUMN_DATERECORDED, dateRecorded);
    long insertId = database.insert(AppOpenHelper.APP_TABLE_NAME, null,
        values);
    Cursor cursor = database.query(AppOpenHelper.APP_TABLE_NAME,
        allColumns, AppOpenHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    AppInfo newApp = cursorToApp(cursor);
    cursor.close();
    return newApp;
  }

  public void deleteApp(AppInfo app) {
    long id = app.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(AppOpenHelper.APP_TABLE_NAME, AppOpenHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<AppInfo> getAllAppss() {
    List<AppInfo> apps = new ArrayList<AppInfo>();

    Cursor cursor = database.query(AppOpenHelper.APP_TABLE_NAME,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      AppInfo app = cursorToApp(cursor);
      apps.add(app);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return apps;
  }

  private AppInfo cursorToApp(Cursor cursor) {
    AppInfo app = new AppInfo();
    app.setId(cursor.getLong(0));
    app.setComment(cursor.getString(1));
    return app;
  }
} 

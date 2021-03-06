package com.example.trackit;

public class ProdUtils {
	
	//final string to denote a productive app
	public static final String PRODUCTIVE_LABEL = "productive";
	
	//string to denote an unproductive app
	public static final String UNPRODUCTIVE_LABEL = "unproductive";
	
	//string to denote unlabelled app
	public static final String NO_LABEL = "no label";
	
	//AppUsageService period in milliseconds
	public static final long SERVICE_PERIOD = 1000;
	
	//AppUsageService time since last recording of time to discount the new recording in milliseconds
	public static final long BAD_TIME_GAP = 5000;

}

package com.example.trackit;

import java.util.List;

public class CalculateProductivity {
	
	//list for all the app objects
	static List<AppInfo> allApps;
	
	//constructor
	public CalculateProductivity(List<AppInfo> allApps){
		CalculateProductivity.allApps = allApps;
	}
	
	//method to find the total run time of all apps
	public static double totalRunTime(){
		double totalTime = 0;
		//add all the times together
		for(AppInfo app : allApps){
			totalTime += app.getRunTime();
		}
		return totalTime;
	}
	
	//get the time for all the productive apps
	private static double productiveRunTime(){
		double productiveTime = 0;
		for(AppInfo app : allApps){
			//only add time if app is productive
			if(app.getLabel().equals(ProdUtils.PRODUCTIVE_LABEL)){
				productiveTime += app.getRunTime();
			}
		}
		return productiveTime;
	}
	
	//find productivity percentage from all runtimes
	public static double getProductivity(){
		double prodPercentage = 0;
		prodPercentage = productiveRunTime()/totalRunTime();
		return prodPercentage;
	}

}

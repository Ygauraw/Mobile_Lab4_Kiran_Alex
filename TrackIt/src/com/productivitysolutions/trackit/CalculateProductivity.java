package com.productivitysolutions.trackit;

import java.util.ArrayList;

public class CalculateProductivity {
	
	static ArrayList<AppInfo> allApps;
	
	public CalculateProductivity(ArrayList<AppInfo> allApps){
		CalculateProductivity.allApps = allApps;
	}
	
	private static long totalRunTime(){
		long totalTime = 0;
		for(AppInfo app : allApps){
			totalTime += app.getRunTime();
		}
		return totalTime;
	}
	
	private static long productiveRunTime(){
		long productiveTime = 0;
		for(AppInfo app : allApps){
			if(app.getLabel().equals(ProdUtils.PRODUCTIVE_LABEL)){
				productiveTime += app.getRunTime();
			}
		}
		return productiveTime;
	}
	
	public static double getProductivity(){
		double prodPercentage = 0;
		prodPercentage = productiveRunTime()/totalRunTime();
		return prodPercentage;
	}

}

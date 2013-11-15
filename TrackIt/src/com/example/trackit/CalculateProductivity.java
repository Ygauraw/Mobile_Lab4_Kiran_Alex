package com.example.trackit;

import java.util.List;

public class CalculateProductivity {
	
	static List<AppInfo> allApps;
	
	public CalculateProductivity(List<AppInfo> allApps){
		CalculateProductivity.allApps = allApps;
	}
	
	public static double totalRunTime(){
		double totalTime = 0;
		for(AppInfo app : allApps){
			totalTime += app.getRunTime();
		}
		return totalTime;
	}
	
	private static double productiveRunTime(){
		double productiveTime = 0;
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

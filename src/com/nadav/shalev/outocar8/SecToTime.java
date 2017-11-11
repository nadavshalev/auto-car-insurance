package com.nadav.shalev.outocar8;

public class SecToTime {

	public static String Sec2Time(int longVal){
		String H,M,S;
		
		int hours = (int) longVal / 3600;
		int remainder = (int) longVal - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		if (hours==0){
			H = "0";
		}else{
			H = String.valueOf(hours);
		}
		
		if(mins==0){
			M = "00";
		}else if(mins<10){
			M = "0"+String.valueOf(mins);
		}else{
			M = String.valueOf(mins);
		}
		
		if(secs<10){
			S = "0"+String.valueOf(secs);
		}else{
			S = String.valueOf(secs);
		}
		
		
		String time = H+":"+M+":"+S;
		return time;
	}
}


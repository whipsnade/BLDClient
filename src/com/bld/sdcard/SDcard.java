package com.bld.sdcard;

import android.os.Environment;

public class SDcard {

	public static boolean IsCanUseSdCard() { 
	    try { 
	        return Environment.getExternalStorageState().equals( 
	                Environment.MEDIA_MOUNTED); 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return false; 
	} 
}

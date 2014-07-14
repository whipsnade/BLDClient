package com.bld.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationEngine {

	static LocationEngine instence;
	
	public static LocationEngine getinstence(){
		if(instence == null)
			instence = new LocationEngine();
		return instence;
	}
	LocationManager locationManager;
	
	public void setManager(LocationManager locationManager){
		this.locationManager=locationManager;
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
	}
	
	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
                    //如果需要去到GPS启动后取到的Location,必须用这个！
			double latitude = location.getLatitude();     //经度 
			double longitude = location.getLongitude(); //纬度 
			Log.e("位置", latitude +" "+longitude);
		}

		@Override
		public void onProviderDisabled(String provider) {
		// Provider被disable时触发此函数，比如GPS被关闭 
		
		}

		@Override
		public void onProviderEnabled(String provider) {
		//  Provider被enable时触发此函数，比如GPS被打开 
		
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数 
		
		}
	
	};
	
	public String getLocation(){
		try{
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
		double latitude = location.getLatitude();     //经度 
		double longitude = location.getLongitude(); //纬度 
		double altitude =  location.getAltitude();     //海拔 
		}
		catch(Exception e){
			Log.e("错误", e.getMessage());
		}
		
		return "";
	}

}

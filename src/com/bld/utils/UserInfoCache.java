package com.bld.utils;

public class UserInfoCache {

	private static UserInfoCache _instence;

	public static UserInfoCache getInstance() {

		if (_instence == null)
			_instence = new UserInfoCache();
		return _instence;

	}
	
	String userid;
	String username;
	double latitude;
	double longitude;
	String address;
	String telephone;
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}

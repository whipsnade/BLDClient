package com.bld.utils;

public class ConnectionUtils {
	
	private static ConnectionUtils _instence;
	
	public static ConnectionUtils getInstance(){
		
		if(_instence==null)
			_instence = new ConnectionUtils();
		return _instence;
		
	}
	public String ServerPath="http://192.168.1.106:8080/BLDService/";
	public String ProductUrl="ProductUtilService/getAllProduct";
	public String ImageUrl="http://192.168.1.106:8080/ImgServer/ProductImg/";
	
	
}

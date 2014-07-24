package com.bld.utils;

public class ConnectionUtils {
	
	private static ConnectionUtils _instence;
	public String ImageUrl="http://whipsnade.gicp.net:8080/ImgServer/ProductImg/";
	public String SuggestImageUrl="http://whipsnade.gicp.net:8080/ImgServer/SuggestImg/";
	public String ServerPath="http://whipsnade.gicp.net:8080/BLDService/";
	
	
//	public String ImageUrl="http://192.168.1.11:8090/ImgServer/ProductImg/";
//	public String SuggestImageUrl="http://192.168.1.11:8090/ImgServer/SuggestImg/";
//	public String ServerPath="http://192.168.1.11:8090/BLDService/";
	
//	public String ImageUrl="http://192.168.1.106:8080/ImgServer/ProductImg/";
//	public String SuggestImageUrl="http://192.168.1.106:8080/ImgServer/SuggestImg/";
//	public String ServerPath="http://192.168.1.106:8080/BLDService/";
	
	public static ConnectionUtils getInstance(){
		
		if(_instence==null)
			_instence = new ConnectionUtils();
		return _instence;
		
	}
	
	public String ProductUrl="ProductUtilService/getAllProduct";
	public String getAllSuggestUrl="ProductUtilService/getAllSuggest";
	
	public String getProductByCodeUrl(String code){
		return "ProductUtilService/getProductByCode?code="+code;
		}
	
	public String createOrder(String ids,double latitude,double longitude){
		return "OrderUtilService/createOrder?ids="+ids+"&latitude="+latitude+"&longitude="+longitude;
		}
	
	public String getProductByIDs(String ids){
		return "ProductUtilService/getProductByIDs?ids="+ids;
		}
	
	public String getAllStore(){
		return "StoreUtilService/getAllStore";
		}
}
	
	
	


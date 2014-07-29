package com.bld.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bld.network.HttpRequest;
import com.bld.object.Product;
import com.bld.object.Store;
import com.bld.object.Suggest;



public class DataBuilder {

	 //JSONObject person = new JSONObject();  
	static DataBuilder _builder;
	
	public static DataBuilder GetInstence()
	{
		if(_builder == null)
			_builder = new DataBuilder();
		return _builder;
	}
	
	
	
	
	
	public ArrayList<Product> getAllProduct(int index) throws UnsupportedEncodingException
	{
		ArrayList<Product> list = new  ArrayList<Product>();
		String res = HttpRequest.getAllProduct(index);
		res=URLDecoder.decode(res, "utf-8");
		if(res!=null)
		{
			try
			{
				JSONArray jsonArray =new  JSONArray(res); 
				//JSONObject jsonObject = new JSONObject(res);
			//	JSONArray jsonArray = jsonObject.getJSONArray("nodes"); 
				for(int i=0;i<jsonArray.length();i++){ 
                      JSONObject jsonObject2 = ((JSONObject)jsonArray.opt(i));
                     
                      list.add(recoverProduct(jsonObject2)); 
                    //  ProductList.put(pro.getSid(),pro); 
                 }
			}catch(JSONException e) 
			{  
	           // throw new RuntimeException(e);  
	        }  
				
		}
		return list;
	}
	
	public ArrayList<Suggest> getAllSuggest() throws UnsupportedEncodingException
	{
		ArrayList<Suggest> list = new  ArrayList<Suggest>();
		String res = HttpRequest.getAllSuggest();
		res=URLDecoder.decode(res, "utf-8");
		if(res!=null && !res.contentEquals("null"))
		{
			try
			{
				JSONArray jsonArray =new  JSONArray(res); 
				for(int i=0;i<jsonArray.length();i++){ 
                      JSONObject jsonObject2 = ((JSONObject)jsonArray.opt(i));
                      Suggest pro = new Suggest(); 
                      pro.setId(jsonObject2.getString("id")); 
                      pro.setImgPath(jsonObject2.getString("imgPath"));                 
                      pro.setTitle(jsonObject2.getString("title"));
                      list.add(pro); 

                 }
			}catch(JSONException e) 
			{  
	            //throw new RuntimeException(e);  
	        }  
				
		}
		return list;
	}
	
	public Product getByCodeProduct(String code) throws UnsupportedEncodingException
	{
		String res = HttpRequest.getProductByCode(code);
		res=URLDecoder.decode(res, "utf-8");
		if(res!=null)
		{
			try
			{
				JSONObject jsonObject2 =new  JSONObject(res); 
				
                      return recoverProduct(jsonObject2);
               
			}catch(JSONException e) 
			{  
	           // throw new RuntimeException(e);  
	        }  
				
		}
		return null;
	}
	
	public ArrayList<Product> createOrder(String ids,double latitude,double longitude) throws UnsupportedEncodingException
	{
		ArrayList<Product> list = new  ArrayList<Product>();
		String res = HttpRequest.createOrder(ids, latitude, longitude);
		res=URLDecoder.decode(res, "utf-8");
		if(res!=null)
		{
			try
			{
				JSONArray jsonArray =new  JSONArray(res); 
				//JSONObject jsonObject = new JSONObject(res);
			//	JSONArray jsonArray = jsonObject.getJSONArray("nodes"); 
				for(int i=0;i<jsonArray.length();i++){ 
                      JSONObject jsonObject2 = ((JSONObject)jsonArray.opt(i));
                      list.add(recoverProduct(jsonObject2)); 
                    //  ProductList.put(pro.getSid(),pro); 
                 }
			}catch(JSONException e) 
			{  
	            //throw new RuntimeException(e);  
	        }  
				
		}
		return list;
	}
	
	
	public ArrayList<Product> getProductByIDs(String ids) throws UnsupportedEncodingException
	{
		ArrayList<Product> list = new  ArrayList<Product>();
		String res = HttpRequest.getProductByIDs(ids);
		res=URLDecoder.decode(res, "utf-8");
		if(res!=null)
		{
			try
			{
				JSONArray jsonArray =new  JSONArray(res); 
				//JSONObject jsonObject = new JSONObject(res);
			//	JSONArray jsonArray = jsonObject.getJSONArray("nodes"); 
				for(int i=0;i<jsonArray.length();i++){ 
                      JSONObject jsonObject2 = ((JSONObject)jsonArray.opt(i));
                     
                      list.add(recoverProduct(jsonObject2)); 
                    //  ProductList.put(pro.getSid(),pro); 
                 }
			}catch(JSONException e) 
			{  
	            //throw new RuntimeException(e);  
	        }  
				
		}
		return list;
	}
	
	public ArrayList<Store> getAllStore() throws UnsupportedEncodingException
	{
		ArrayList<Store> list = new  ArrayList<Store>();
		String res = HttpRequest.getAllStore();
		res=URLDecoder.decode(res, "utf-8");
		if(res!=null)
		{
			try
			{
				JSONArray jsonArray =new  JSONArray(res); 
				for(int i=0;i<jsonArray.length();i++){ 
                      JSONObject jsonObject2 = ((JSONObject)jsonArray.opt(i));
                      list.add(recoverStore(jsonObject2)); 
                     
                 }
			}catch(JSONException e) 
			{  
	           Log.e("e",e.getMessage()); //throw new RuntimeException(e);  
	        }  
				
		}
		return list;
	}
	
	private Product recoverProduct(JSONObject jsonObject2) throws JSONException{
		 Product pro = new Product(); 
         pro.setImg(jsonObject2.getString("img")); 
         pro.setPrice(jsonObject2.getLong("price"));                 
         pro.setId(jsonObject2.getString("id"));
         pro.setName(jsonObject2.getString("name"));
         pro.setCode(jsonObject2.getString("code"));
         pro.setClassId(jsonObject2.getString("classId"));
         pro.setDescription(jsonObject2.getString("description"));
         pro.setNum(1);
         return pro;
	}
	
	private Store recoverStore(JSONObject jsonObject2) throws JSONException{
		Store store = new Store(); 
		store.setAddress(jsonObject2.getString("address"));             
		store.setId(jsonObject2.getString("id"));
		store.setName(jsonObject2.getString("name"));
		store.setPayment((short)jsonObject2.getInt("payment"));
		store.setTel(jsonObject2.getString("tel"));
		store.setLatitude(jsonObject2.getDouble("latitude"));
		store.setLongitude(jsonObject2.getDouble("longitude"));

        return store;
	}
}

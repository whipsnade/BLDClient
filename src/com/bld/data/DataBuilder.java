package com.bld.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bld.network.HttpRequest;
import com.bld.object.Product;
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
                      Product pro = new Product(); 
                      pro.setImg(jsonObject2.getString("img")); 
                      pro.setPrice(jsonObject2.getLong("price"));                 
                      pro.setId(jsonObject2.getString("id"));
                      pro.setName(jsonObject2.getString("name"));
                      pro.setCode(jsonObject2.getString("code"));
                      pro.setClassId(jsonObject2.getString("classId"));
                      pro.setDescription(jsonObject2.getString("description"));
                      list.add(pro); 
                    //  ProductList.put(pro.getSid(),pro); 
                 }
			}catch(JSONException e) 
			{  
	            throw new RuntimeException(e);  
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
				
                      Product pro = new Product(); 
                      pro.setImg(jsonObject2.getString("img")); 
                      pro.setPrice(jsonObject2.getLong("price"));                 
                      pro.setId(jsonObject2.getString("id"));
                      pro.setName(jsonObject2.getString("name"));
                      pro.setCode(jsonObject2.getString("code"));
                      pro.setClassId(jsonObject2.getString("classId"));
                      pro.setDescription(jsonObject2.getString("description"));
                      return pro;
               
			}catch(JSONException e) 
			{  
	            throw new RuntimeException(e);  
	        }  
				
		}
		return null;
	}
	

}

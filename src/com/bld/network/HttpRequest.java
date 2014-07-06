package com.bld.network;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.bld.utils.ConnectionUtils;

import android.util.Log;

public class HttpRequest {
	
	static String URL=ConnectionUtils.getInstance().ServerPath;
	
	public static String getAllProduct(int index) {
		
		String url=URL+ConnectionUtils.getInstance().ProductUrl;
		Log.e("更新", ""+url);
		return request(url);
	}
	
	public static String getSubjectItems() {
		
		String url=URL+"/zt";
		Log.e("更新", ""+url);
		return request(url);
	}
	
	private static String request(String url)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget=  new HttpGet(url);
		HttpResponse httpResponse;
		String response = null;

			try {
			httpResponse = httpClient.execute(httpget);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
			System.out.println("get information 200");
			response = EntityUtils.toString(httpResponse.getEntity());
			} else {
			response = "statusNum: " + statusCode;
			}
			} catch (Exception e) {
				
				Log.e("错误", e.getMessage());
			}
			return response;
		
	}
	
}

package com.bld.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.os.Environment;

import com.bld.network.CustomerHttpClient;

public class HttpDownLoadTask extends Task {

	HttpClient httpClient;
	@Override
	public void shutDownExecute() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object cacheData(Object parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object obtainData(Object result, Object parameter) throws Exception {
		// TODO Auto-generated method stub


		httpClient=CustomerHttpClient.getHttpClient();
		HttpPost httpPost = new HttpPost("http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk");
	    HttpResponse httpresponse=httpClient.execute(httpPost);
	    InputStream inputStream= httpresponse.getEntity().getContent();
	    File file=new File(Environment
				.getExternalStorageDirectory() + "/imageCache/aa.apk");
	    FileOutputStream fos=new FileOutputStream(file);
		  byte b[] = new byte[1024];
		  int j = 0;
		  while( (j = inputStream.read(b))!=-1)
		  {
			  fos.write(b,0,j);
		  }
		  fos.flush();
		  fos.close();

		return true;

	}

}
//package sitemap;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//public class SitemapDownloader
//{
//public static void main(String[] args) throws Exception
//{
//int min = 1;
//int max = 806;
//
//String url = "http://www.foxnews.com/sitemap.xml?idx=";
//
//while (min &lt; max)
//{
//Thread.sleep(500);
//
//HttpClient httpClient1 = new DefaultHttpClient();
//
//HttpGet httpGet1 = new HttpGet(url+min);
//HttpResponse httpResponse1 = httpClient1.execute(httpGet1);
//
//StatusLine statusLine = httpResponse1.getStatusLine();
//if(statusLine.getStatusCode() == 200)
//{
//
//File xml = new File("d:/sitemap/"+min+".xml");
//
//FileOutputStream outputStream = new FileOutputStream(xml);
//InputStream inputStream = httpResponse1.getEntity().getContent();
//byte b[] = new byte[1024];
//int j = 0;
//while( (j = inputStream.read(b))!=-1)
//{
//outputStream.write(b,0,j);
//}
//outputStream.flush();
//outputStream.close();
//
//min++;
//System.out.println("存储了XML: " +min);
//}
//
//httpClient1.getConnectionManager().shutdown();
//}
//}
//}
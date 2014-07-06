package com.bld.task;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;

public  class  HttpClientIO
{
	private InputStream inputStream;
	private HttpEntity httpEntity;
	private HttpClient httpClient;
	
	
	public  HttpClientIO(){
	
	}
	
	public  HttpClientIO(InputStream is,HttpEntity he,HttpClient hc){
		this.inputStream=is;
		this.httpEntity=he;
		this.httpClient=hc;
	}
	
	public void setInputStream1(InputStream is){
		this.inputStream=is;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public HttpEntity getHttpEntity() {
		return httpEntity;
	}
	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}
	public HttpClient getHttpClient() {
		return httpClient;
	}
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
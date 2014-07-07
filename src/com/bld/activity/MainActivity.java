package com.bld.activity;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bld.R;
import com.bld.adapter.ImageAdapter;
import com.bld.adapter.ViewFlowImageAdapter;
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.object.Suggest;
import com.bld.task.TaskQueue;
import com.bld.view.MainGridView;
import com.bld.view.MainGridView.OnScrollListener;
import com.bld.widget.CircleFlowIndicator;
import com.bld.widget.ViewFlow;




@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	MainGridView gridView_check;  
	ImageAdapter ia_check;
	ViewFlow viewFlow;
	CircleFlowIndicator indic;
	ViewFlowImageAdapter image_adapter;

	
	ArrayList<Product> ProductList = new ArrayList<Product>();
	ArrayList<Suggest> SuggestList = new ArrayList<Suggest>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitymain);
//
//
		 viewFlow = (ViewFlow) findViewById(R.id.viewflow);
	     indic  = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		 
		 gridView_check =  (MainGridView) findViewById(R.id.gridview_main);
		 image_adapter = new ViewFlowImageAdapter(this);
		 new Thread(downloadRun).start();
		 new Thread(suggestRun).start();
		 TaskQueue.setThreadMaxNum(10);
	}
	
	Handler handler = new Handler(){
		  @SuppressWarnings("unchecked")
		  public void handleMessage(Message msg) {
			List<Product> list =  (List<Product>)msg.obj;
			 ia_check = new ImageAdapter(MainActivity.this,list); 
			  gridView_check.setAdapter(ia_check);
		  }  
		 };
		 
		
		 Runnable suggestRun = new Runnable(){  
	    	 @Override  
			 public void run() {
	    		 try {
					SuggestList=DataBuilder.GetInstence().getAllSuggest();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		 viewFlow.addSuggestData(SuggestList,image_adapter,indic);

	    	 }
	    };
	    
	    
	 Runnable downloadRun = new Runnable(){  
		  
		 @Override  
		 public void run() {  
		     // TODO Auto-generated method stub  
			 try {
				ProductList=DataBuilder.GetInstence().getAllProduct(0);
			} catch (UnsupportedEncodingException e) {
				/// TODO Auto-generated catch block
				Log.e("错误", e.getMessage());
			}
			 
			  
			  handler.sendMessage(handler.obtainMessage(22, ProductList));
		
			  gridView_check.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onBottom(int scrollY) {
						// TODO Auto-generated method stub
			
					}

					@Override
					public void onTop(int scrollY) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onScroll(int scrollY) {
						// TODO Auto-generated method stub
						//Log.e("高度", ""+scrollY);
					}

					@Override
					public void onAutoScroll(int scrollY) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onUpdata(int scrollY) {
						//Log.e("更新", ""+scrollY);
						
						//  new Thread(getNextRun).start();
					}

					@Override
					public void onScrollUP(int scrollY) {
						// TODO Auto-generated method stub
						// System.out.println("锟斤拷锟较癸拷锟斤拷");
					}

					@Override
					public void onScrollDOWN(int scrollY) {
						// TODO Auto-generated method stub
						// System.out.println("锟斤拷锟铰癸拷锟斤拷");
					//if(scrolly)
						//new Thread(getNextRun).start();
					}

					@Override
					public void onScrollMid(int scrollY) {
						// TODO Auto-generated method stub
//						if(!loadOver){
//						Log.e("中段", ""+scrollY);
//						loadOver=true;
//						  new Thread(getNextRun).start();
//						}
					}

				});
			
		 }  
	};
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

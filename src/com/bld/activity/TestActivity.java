package com.bld.activity;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.task.TaskQueue;
import com.bld.test.ImageAdapter;
import com.bld.view.MainGridView;
import com.bld.view.MainGridView.OnScrollListener;

@SuppressLint("HandlerLeak")
public class TestActivity extends Activity {
	MainGridView gridView_check;  
	ImageAdapter ia_check;
	private final String image_path = "images";
	ArrayList<Product> ProductList = new ArrayList<Product>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
//
//
		  
		  gridView_check =  (MainGridView) findViewById(R.id.gridview_main);
		 new Thread(downloadRun).start();
		 TaskQueue.setThreadMaxNum(5);
	}
	
	Handler handler = new Handler(){
		  @SuppressWarnings("unchecked")
		  public void handleMessage(Message msg) {
			List<Product> list =  (List<Product>)msg.obj;
			 ia_check = new ImageAdapter(TestActivity.this,list); 
			  gridView_check.setAdapter(ia_check);
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

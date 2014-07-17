package com.bld.activity;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.bld.R;
import com.bld.adapter.GridItemClickListener;
import com.bld.adapter.GridItemSelectListener;
import com.bld.adapter.ImageAdapter;
import com.bld.adapter.ViewFlowImageAdapter;
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.object.Suggest;
import com.bld.task.HttpResourcesTask;
import com.bld.task.Task;
import com.bld.task.TaskQueue;
import com.bld.task.HttpResourcesTask.CacheType;
import com.bld.task.HttpResourcesTask.HttpType;
import com.bld.task.Task.OnFinishListen;
import com.bld.utils.ConnectionUtils;
import com.bld.widget.CircleFlowIndicator;

import com.bld.widget.ViewFlow;






@SuppressLint("HandlerLeak")
public class HomeActivity extends Activity implements GridItemSelectListener{
	ListView listView;  
	ImageAdapter madapter;
	ViewFlow viewFlow;
	CircleFlowIndicator indic;
	ViewFlowImageAdapter viewflow_adapter;
	View header; 
	Activity context;
	Integer tasksOver = 0;
	int tasksLength; 

	LinearLayout popmenu;
	public ArrayList<Product> ProductList = new ArrayList<Product>();
	public ArrayList<Product> select_list = new ArrayList<Product>();
	ArrayList<Suggest> SuggestList = new ArrayList<Suggest>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
//
//
		context=this;
		header = LayoutInflater.from(this).inflate(R.layout.viewflow, null);
		viewFlow = (ViewFlow) header.findViewById(R.id.viewflow);
	    indic  = (CircleFlowIndicator) header.findViewById(R.id.viewflowindic);

		listView =  (ListView) findViewById(R.id.gridview_main);
		viewFlow.mListView=listView;
		listView.addHeaderView(header);

		viewflow_adapter = new ViewFlowImageAdapter(this);
		
		madapter = new ImageAdapter(this);
		madapter.setNumColumns(3);

		madapter.setOnGridClickListener(madapter);
		madapter.setOnGridSelectListener(this);
		
		 new Thread(downloadRun).start();
		 new Thread(suggestRun).start();
		 TaskQueue.setThreadMaxNum(15);

		
	}
	
;  
	 
	    
	    
	Handler handler = new Handler(){
		  @Override
		@SuppressWarnings("unchecked")
		  public void handleMessage(Message msg) {
			listView.setAdapter(madapter);

		  }  
		 };
	
	Handler flow_handler = new Handler(){
			  @Override
			@SuppressWarnings("unchecked")
			  public void handleMessage(Message msg) {
				  suggestUpdate.run();

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
	    		 //viewFlow.addSuggestData(SuggestList,image_adapter,indic);
	    		 flow_handler.sendMessage(flow_handler.obtainMessage(23, SuggestList));
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
		
			
		 }  
	};
	
	Runnable suggestUpdate = new Runnable(){ @Override
		public void run() {
		tasksLength = SuggestList.size();
		tasksOver = 0;
		for(Suggest s : SuggestList)
		{
		String url=ConnectionUtils.getInstance().SuggestImageUrl+s.getImgPath();
		HttpResourcesTask task = new HttpResourcesTask(HomeActivity.this, HttpType.Img,
				CacheType.saveInSDcard);
		task.setParameter(url).setOnFinishListen(new OnFinishListen() {

					@Override
					public void OnFinish(Task t, Object data) {
						// TODO Auto-generated method stub
						if (data != null
								&& !(data instanceof Exception)) {
							Drawable TampDraw;  
							if( t.getResult() instanceof Drawable){
								TampDraw=(Drawable) t.getResult();
//								//TampDraw.
								View convertView = viewflow_adapter.mInflater.inflate(R.layout.image_item, null);
//								
								((ImageView) convertView.findViewById(R.id.imgView)).setTag(t.getTag());
								((ImageView) convertView.findViewById(R.id.imgView)).setImageDrawable(TampDraw);
								//((ImageView) convertView.findViewById(R.id.imgView)).setOnClickListener(childOnClickListen);
								ViewFlowImageAdapter.viewList.add(convertView);
							
									
								
							}
							viewFlow.setAdapter(viewflow_adapter);
							viewFlow.setFlowIndicator(indic);
						}
						synchronized (tasksOver) {
							tasksOver++;
							
						}
					}
				});
		task.setTag(s);
		task.start();
		
	 }
	} 
};


@Override
public void onGridItemSelected(View view, int position, long itemId) {
	// TODO Auto-generated method stub
	RadioButton selector=(RadioButton) view.findViewById(R.id.pro_selector);
		Product pro = ProductList.get(position);
			if(select_list.contains(pro))
			{
				selector.setChecked(false);
				select_list.remove(pro);
				
			}
			else{
				selector.setChecked(true);
				select_list.add(pro);
			}
			
	popmenu= (LinearLayout)HomeActivity.this.findViewById(R.id.pop_layout);
	popmenu.setVisibility(View.VISIBLE);
	ImageView btn = (ImageView)HomeActivity.this.findViewById(R.id.btn_add_cancel);
	btn.setOnClickListener(popMenuitemsOnClick);
	if(select_list.size()<=0){
		popmenu.setVisibility(View.GONE);
	}
}


private OnClickListener  popMenuitemsOnClick = new OnClickListener(){

	public void onClick(View v) {
		//menuWindow.dismiss();
		popmenu.setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.btn_buy:
			break;
		case R.id.btn_add_cart:				
			break;
		default:
			break;
		}
		
			
	}
	
};




}

package com.bld.activity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.bld.R;
import com.bld.adapter.OrderListAdapter;
import com.bld.adapter.StoreSelectAdapter;
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.object.Store;
import com.bld.utils.UserInfoCache;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OrderActivity extends Activity {
	public ArrayList<Product> orderProductList = new ArrayList<Product>();
	public ArrayList<Store> StoreList = new ArrayList<Store>();
	public Map<Store,Boolean> ShowStoreList = new HashMap<Store,Boolean>();
	public Store SelectStore;
	String selectIDS;
	ListView list;
	ListView store_listview;
	OrderListAdapter listadpter;
	StoreSelectAdapter storeadapter;
	TextView sumPriceText;
	View addressheader;
	View storefooter;
	TextView moreStore;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		list = (ListView) findViewById(R.id.order_list);
		listadpter = new OrderListAdapter(this);

		selectIDS = this.getIntent().getStringExtra("selectIDS");
		sumPriceText = (TextView) findViewById(R.id.order_sum_price);
		addressheader = LayoutInflater.from(this).inflate(R.layout.address, null);
		((TextView)addressheader.findViewById(R.id.addresstext)).setText(UserInfoCache.getInstance().getAddress());
		((TextView)addressheader.findViewById(R.id.usernametext)).setText(UserInfoCache.getInstance().getUsername());
		((TextView)addressheader.findViewById(R.id.phonetext)).setText(UserInfoCache.getInstance().getTelephone());
		
		list.addHeaderView(addressheader);
		
		storefooter=LayoutInflater.from(this).inflate(R.layout.store_select, null);
		storeadapter=new StoreSelectAdapter(this);
		store_listview = (ListView)storefooter.findViewById(R.id.store_list);
		moreStore=(TextView)storefooter.findViewById(R.id.more_store);
		moreStore.setOnClickListener(moreStoreClick);
		list.addFooterView(storefooter);
		
		getActionBar().setTitle("订单");
		if (selectIDS.length() > 0) {
			new Thread(getOrder).start();
			new Thread(getStore).start();
		}

	}

	Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			list.setAdapter(listadpter);
			RefreshSumPrice();
		}
	};
	
	Handler footer_handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			store_listview.setAdapter(storeadapter);
		}
	};

	Runnable getOrder = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				orderProductList = DataBuilder.GetInstence().createOrder(
						selectIDS, UserInfoCache.getInstance().getLatitude(),
						UserInfoCache.getInstance().getLongitude());
				handler.sendMessage(handler.obtainMessage(25, ""));
			} catch (UnsupportedEncodingException e) {
				// / TODO Auto-generated catch block
				Log.e("错误", e.getMessage());
			}

		}
	};
	
	Runnable getStore = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				StoreList = DataBuilder.GetInstence().getAllStore();
				ShowStoreList.put(StoreList.get(0),true);
				for(int i=1;i<StoreList.size();i++){
					ShowStoreList.put(StoreList.get(i),false);
				}
				
				SelectStore=StoreList.get(0);
				footer_handler.sendMessage(footer_handler.obtainMessage(26, ""));
			} catch (UnsupportedEncodingException e) {
				// / TODO Auto-generated catch block
				Log.e("错误", e.getMessage());
			}

		}
	};
	
	OnClickListener moreStoreClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			LayoutParams laParams=(LayoutParams)store_listview.getLayoutParams();
			laParams.height=StoreList.size()*200;
			store_listview.setLayoutParams(laParams);
			storeadapter.count=StoreList.size();
			storeadapter.notifyDataSetChanged();
			LinearLayout select_layout=(LinearLayout)storefooter.findViewById(R.id.store_select_layout);
			select_layout.removeViewAt(2);
		}
	};
	
	public void RefreshSumPrice(){
		double sum=0;
		for(int i =0; i<orderProductList.size();i++){
			sum+=(double)orderProductList.get(i).getNum()*orderProductList.get(i).getPrice();
		}
		DecimalFormat df =new java.text.DecimalFormat("#.00");  
		sumPriceText.setText(df.format(sum));
	}

}

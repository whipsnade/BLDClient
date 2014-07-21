package com.bld.activity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.bld.R;
import com.bld.adapter.OrderListAdapter;
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.utils.UserInfoCache;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class OrderActivity extends Activity {
	public ArrayList<Product> orderProductList = new ArrayList<Product>();
	String selectIDS;
	ListView list;
	OrderListAdapter listadpter;
	TextView sumPriceText;
	View addressheader;
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
		getActionBar().setTitle("订单");
		if (selectIDS.length() > 0) {
			new Thread(getOrder).start();
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
	
	public void RefreshSumPrice(){
		double sum=0;
		for(int i =0; i<orderProductList.size();i++){
			sum+=(double)orderProductList.get(i).getNum()*orderProductList.get(i).getPrice();
		}
		DecimalFormat df =new java.text.DecimalFormat("#.00");  
		sumPriceText.setText(df.format(sum));
	}

}

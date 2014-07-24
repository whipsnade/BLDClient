package com.bld.activity;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bld.R;
import com.bld.adapter.CartListAdapter;
import com.bld.adapter.OrderListAdapter;
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.object.Store;
import com.bld.utils.SharePreferencesOperator;
import com.bld.utils.UserInfoCache;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CartActivity extends Activity {
	public Map<Product, Boolean> ProductList = new HashMap<Product, Boolean>();

	String selectIDS;

	ListView list;
	CartListAdapter listadpter;
	TextView sumPriceText;
	View addressheader;
	CheckBox all_selector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_list);
		list = (ListView) findViewById(R.id.cart_list);
		listadpter = new CartListAdapter(this);

		selectIDS = SharePreferencesOperator.GetInstence().getCartData(this);
		sumPriceText = (TextView) findViewById(R.id.cart_sum_price);

		all_selector = (CheckBox) findViewById(R.id.all_selector);
		all_selector.setChecked(true);
		all_selector.setOnCheckedChangeListener(checkedchange);
		Button btn_buy = (Button) findViewById(R.id.cart_buy);
		btn_buy.setOnClickListener(popMenuitemsOnClick);

		if (selectIDS.length() > 0) {
			new Thread(getproduct).start();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!selectIDS.contentEquals(SharePreferencesOperator.GetInstence()
				.getCartData(this))) {
			selectIDS = SharePreferencesOperator.GetInstence()
					.getCartData(this);
			if (selectIDS.length() > 0) {
				
				new Thread(getproduct).start();
			}
		}
	}

	OnCheckedChangeListener checkedchange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int index = 0; index < list.getChildCount(); index++) {
					LinearLayout layout = (LinearLayout) list.getChildAt(index);
					CheckBox checkBox = (CheckBox) layout
							.findViewById(R.id.selector);
					checkBox.setChecked(true);
					allSelect(true);
				}
			}
			// else{
			// for (int index = 0; index < list.getChildCount(); index++) {
			// LinearLayout layout = (LinearLayout) list.getChildAt(index);
			// CheckBox checkBox = (CheckBox)
			// layout.findViewById(R.id.selector);
			// checkBox.setChecked(false);
			// allSelect(false);
			// }
			// }
		}

	};

	Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			list.setAdapter(listadpter);
			RefreshSumPrice();
		}
	};

	Runnable getproduct = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				List<Product> list = DataBuilder.GetInstence().getProductByIDs(
						selectIDS);
				ProductList.clear();
				for (int i = 0; i < list.size(); i++) {
					if (!ProductList.containsKey(list.get(i))) {
						ProductList.put(list.get(i), true);
					}
				}
				handler.sendMessage(handler.obtainMessage(26, ""));
			} catch (UnsupportedEncodingException e) {
				// / TODO Auto-generated catch block
				Log.e("错误", e.getMessage());
			}

		}
	};

	private OnClickListener popMenuitemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			// menuWindow.dismiss();

			Intent intent = new Intent(CartActivity.this, OrderActivity.class);
			intent.putExtra("selectIDS", getSelectIDS());
			startActivity(intent);
		}

	};

	public void RefreshSumPrice() {
		double sum = 0;
			
		Set<Map.Entry<Product, Boolean>> entry = ProductList.entrySet();
		for (Map.Entry<Product, Boolean> obj : entry) {

			if (obj.getValue()) {
				sum += (double) ((Product) obj.getKey())
						.getNum()
						* ((Product) obj.getKey()).getPrice();
			}
		}
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		sumPriceText.setText(df.format(sum));
	}

	private void allSelect(boolean flag) {
		Set<Map.Entry<Product, Boolean>> entry = ProductList.entrySet();
		for (Map.Entry<Product, Boolean> obj : entry) {
			obj.setValue(flag);
		}
		RefreshSumPrice();
	}

	public void RefreshSelect() {
		Set<Map.Entry<Product, Boolean>> entry = ProductList.entrySet();
		for (Map.Entry<Product, Boolean> obj : entry) {
			if (!obj.getValue()) {
				all_selector.setChecked(false);
				return;
			}
		}
		all_selector.setChecked(true);

	}

	private String getSelectIDS() {

		String rs = "";
		Set<Map.Entry<Product, Boolean>> entry = ProductList.entrySet();
		for (Map.Entry<Product, Boolean> obj : entry) {
			if (rs.length() > 0)
				rs += ",";
			if (obj.getValue()) {
				rs += obj.getKey().getId();
			}
		}

		return rs;
	}

}

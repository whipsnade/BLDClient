package com.bld.adapter;

import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.bld.R;
import com.bld.activity.CartActivity;
import com.bld.activity.HomeActivity;
import com.bld.activity.OrderActivity;
import com.bld.object.Product;
import com.bld.object.Store;
import com.bld.task.HttpResourcesTask;
import com.bld.task.Task;
import com.bld.task.HttpResourcesTask.CacheType;
import com.bld.task.HttpResourcesTask.HttpType;
import com.bld.task.Task.OnFinishListen;
import com.bld.utils.ConnectionUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class StoreSelectAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	OrderActivity ac;
	ArrayAdapter adapter;
	Integer tasksOver = 0;
	Integer tasksLength = 0;
	public int count = 1;  
	private static final String[] m = { "现付", "网络支付" };

	public StoreSelectAdapter(Context c) {
		mContext = c;
		// multiChoose = isMulti;
		this.mInflater = LayoutInflater.from(c);
		ac = (OrderActivity) mContext;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Store store = (Store) ac.ShowStoreList.keySet().toArray()[position];
		boolean flag = ac.ShowStoreList.get(store);
		Log.e("position",String.valueOf(position));
		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.store_select_item,
					null);
		}
		
		
		Spinner spinner = (Spinner) convertView
				.findViewById(R.id.store_payment);
		if (store.getPayment() > 1) {
			adapter = new ArrayAdapter<String>(ac,
					android.R.layout.simple_spinner_item, m);

		} else {
			adapter = new ArrayAdapter<String>(ac,
					android.R.layout.simple_spinner_item,
					new String[] { m[store.getPayment()] });
		}
		spinner.setAdapter(adapter);
		
		
		((TextView) convertView.findViewById(R.id.store_address)).setText(store
				.getAddress());
		((TextView) convertView.findViewById(R.id.store_name)).setText(store
				.getName());
		((TextView) convertView.findViewById(R.id.store_phone)).setText(store
				.getTel());

		final RadioButton radio = (RadioButton) convertView
				.findViewById(R.id.radio_btn);
		radio.setChecked(flag);
		radio.setTag(store);
		radio.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// 重置，确保最多只有一项被选中
				for (Store key : ac.ShowStoreList.keySet()) {
					ac.ShowStoreList.put(key, false);

				}

				Store temp = (Store) v.getTag();
				ac.SelectStore = temp;
				ac.ShowStoreList.put(temp, radio.isChecked());
				StoreSelectAdapter.this.notifyDataSetChanged();
			}
		});

		return convertView;
	}

	public void changeState(int position) {

		notifyDataSetChanged();
	}

}

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CartListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	CartActivity ac;

	Integer tasksOver = 0;
	Integer tasksLength = 0;
	private static HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	boolean canLoading = true;
	private ImageView currentImage;
	DecimalFormat df = new java.text.DecimalFormat("#.00");
	public CartListAdapter(Context c) {
		mContext = c;
		// multiChoose = isMulti;
		this.mInflater = LayoutInflater.from(c);
		ac = (CartActivity) mContext;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ac.ProductList.size();
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
		Product pro = (Product)ac.ProductList.keySet().toArray()[position];
		boolean flag=ac.ProductList.get(pro);
		String url = ConnectionUtils.getInstance().ImageUrl + pro.getImg();
		String title = pro.getName();
		double price = pro.getPrice();
		String ID = pro.getId();
		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.cart_item, null);

		} 
		else{
			Log.e(pro.getName(), "");
		}
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.cart_pro_image);
		imageView.setImageResource(R.color.white);

		((TextView) convertView.findViewById(R.id.cart_pro_title))
				.setText(title);
		((TextView) convertView.findViewById(R.id.cart_pro_price))
				.setText(df.format(pro.getPrice() * (double)pro.getNum()));
		((CheckBox)convertView.findViewById(R.id.selector)).setChecked(flag);
		((CheckBox)convertView.findViewById(R.id.selector)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Product temp =(Product)v.getTag();
				Log.e(temp.getName(), String.valueOf(true));
				if(((CheckBox)v).isChecked()){					
					ac.ProductList.put(temp,true);
				}
				else{
					ac.ProductList.put(temp,false);
				}
				
				ac.RefreshSelect();
			}
			
		});
		((CheckBox)convertView.findViewById(R.id.selector)).setTag(pro);
		asyncImageLoad(imageView, url);

		
		return convertView;
	}

	// 同步图片方法
	private void asyncImageLoad(ImageView imageView, String path) {
		// a.创建这个对象把imageView传进去.
		AsyncImageTask task = new AsyncImageTask(imageView);
		task.run(path);
	}

	// 同步图片私有类
	private final class AsyncImageTask {
		private ImageView imageView;

		public AsyncImageTask(ImageView imageView) {
			// 设定参数
			this.imageView = imageView;
		}

		public void run(String url) {
			HttpResourcesTask task = new HttpResourcesTask(mContext,
					HttpType.Img, CacheType.saveInSDcard);
			task.setParameter(url).setOnFinishListen(new OnFinishListen() {

				@Override
				public void OnFinish(Task t, Object data) {
					// TODO Auto-generated method stub
					if (data != null && !(data instanceof Exception)) {
						Drawable TampDraw = null;
						if (t.getResult() instanceof Drawable) {
							TampDraw = (Drawable) t.getResult();
						} else {
							TampDraw = mContext.getResources().getDrawable(
									R.drawable.android);
						}
						imageCache.put(t.getTag().toString(),
								new SoftReference<Drawable>(TampDraw));
						handler.sendMessage(handler.obtainMessage(10, TampDraw));

					}
					synchronized (tasksOver) {
						tasksOver++;
						if (tasksOver == tasksLength) {
							canLoading = true;
						}
					}
				}
			});
			task.setTag(url);
			task.start();
		}

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				Drawable uri = (Drawable) msg.obj;
				if (uri != null && imageView != null)

					imageView.setImageDrawable(uri);
			}
		};
	}


	public void handleImageView(ImageView imageView) {
		currentImage.setImageDrawable(null);

		currentImage = imageView;
	}

	public void changeState(int position) {

		notifyDataSetChanged();
	}

}

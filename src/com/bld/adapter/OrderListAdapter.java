package com.bld.adapter;

import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.bld.R;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	OrderActivity ac;

	Integer tasksOver = 0;
	Integer tasksLength = 0;
	private static HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	boolean canLoading = true;
	private ImageView currentImage;
	DecimalFormat df = new java.text.DecimalFormat("#.00");
	public OrderListAdapter(Context c) {
		mContext = c;
		// multiChoose = isMulti;
		this.mInflater = LayoutInflater.from(c);
		ac = (OrderActivity) mContext;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ac.orderProductList.size();
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
		Product pro = ac.orderProductList.get(position);

		String url = ConnectionUtils.getInstance().ImageUrl + pro.getImg();
		String title = pro.getName();
		double price = pro.getPrice();
		String ID = pro.getId();
		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.order_item, null);

		} 
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.order_pro_image);
		imageView.setImageResource(R.color.white);

		((TextView) convertView.findViewById(R.id.order_pro_title))
				.setText(title);
		((TextView) convertView.findViewById(R.id.order_pro_price))
				.setText(df.format(pro.getPrice() * (double)pro.getNum()));
		
		final EditText num_text = ((EditText) convertView
				.findViewById(R.id.order_num));
		num_text.setTag(position);
		
		num_text.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable editable) {
			
			}

			public void beforeTextChanged(CharSequence text, int start,
					int count, int after) {
			}
			
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {
				if (text.length() > 0) {

					String num = text.toString();
					int index=(Integer) num_text.getTag();
					ac.orderProductList.get(index).setNum(Integer.parseInt(num));
					LinearLayout parentRow = (LinearLayout) num_text
							.getParent().getParent().getParent();
					TextView sumprice = (TextView) parentRow.getChildAt(2);

					sumprice.setText(df.format(ac.orderProductList.get(index)
							.getPrice() * (double) Integer.parseInt(num)));
					ac.RefreshSumPrice();
				}
			}
		});
		
		num_text.setText(String.valueOf(ac.orderProductList.get(position).getNum()));
		asyncImageLoad(imageView, url);

		Button add = (Button) convertView.findViewById(R.id.btn_plus);
		add.setOnClickListener(numButtonClickListen);
		add.setTag(position);

		Button subtract = (Button) convertView.findViewById(R.id.btn_subtract);
		subtract.setOnClickListener(numButtonClickListen);
		subtract.setTag(position);
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

	OnClickListener numTextClickListen = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	OnClickListener numButtonClickListen = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			LinearLayout vwParentRow = (LinearLayout) view.getParent();
			LinearLayout parentRow = (LinearLayout) view.getParent()
					.getParent().getParent();
			EditText child = (EditText) vwParentRow.getChildAt(1);
			TextView sumprice = (TextView) parentRow.getChildAt(2);
			int num = Integer.parseInt(child.getText().toString());
			final int index = (Integer) view.getTag();
			double price = ac.orderProductList.get(index).getPrice();
			if (view.getId() == R.id.btn_plus) {

				num++;
			} else {
				if (num > 1) {
					num--;
				}

			}
		
			child.setText(String.valueOf(num));
//			ac.orderProductList.get(index).setNum(num);
//			sumprice.setText(df.format(price * (double) num));
//			ac.RefreshSumPrice();
		}
	};

	public void handleImageView(ImageView imageView) {
		currentImage.setImageDrawable(null);

		currentImage = imageView;
	}

	public void changeState(int position) {

		notifyDataSetChanged();
	}

}

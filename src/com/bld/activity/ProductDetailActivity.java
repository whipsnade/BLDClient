package com.bld.activity;


import com.bld.R;
import com.bld.data.DataBuilder;
import com.bld.object.Product;
import com.bld.task.HttpResourcesTask;
import com.bld.task.HttpResourcesTask.CacheType;
import com.bld.task.HttpResourcesTask.HttpType;
import com.bld.task.Task;
import com.bld.task.Task.OnFinishListen;
import com.bld.utils.ConnectionUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends Activity {

	Product product;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.activity_product_detail);
		}
		catch(Exception ex)
		{
			 Log.e("更新", ex.getMessage());
		}
		Intent intent=getIntent();
		product = (Product)intent.getSerializableExtra("selected_product");  
	   
	    TextView  title=(TextView) findViewById(R.id.info_name);
	    title.setText(product.getName());
	    TextView  description=(TextView) findViewById(R.id.info_text);
	    description.setText(product.getDescription());
	    
	    HttpResourcesTask task = new HttpResourcesTask(this, HttpType.Img,
				CacheType.saveInSDcard);
		task.setParameter(ConnectionUtils.getInstance().ImageUrl+product.getImg())
				.setOnFinishListen(new OnFinishListen() {

					@Override
					public void OnFinish(Task t, Object data) {
						// TODO Auto-generated method stub
						if (data != null
								&& !(data instanceof Exception)) {
							ImageView imageView = (ImageView) findViewById(R.id.proImg);
							Drawable d= (Drawable) t.getResult();
							int sWidth = d.getIntrinsicWidth();

							int height = (int) (d.getIntrinsicHeight());
							
							imageView.setImageDrawable(d);

						}
						
					}
				});
		task.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

}

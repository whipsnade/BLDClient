package com.bld.adapter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;    

import com.bld.R;
import com.bld.activity.HomeActivity;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;    
import android.view.ViewGroup;    
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ImageAdapter extends ListAsGridBaseAdapter  implements GridItemClickListener {
	 private Context mContext;                     
     private Vector<Integer> mImageIds = new Vector<Integer>();      
     private int lastPosition = -1;            
     private boolean multiChoose;               
     private LayoutInflater mInflater =null;
     HomeActivity ac;
     
 	Integer tasksOver = 0;
 	Integer tasksLength=0;
 	private static HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
 	boolean canLoading=true;
 	private ImageView currentImage;
 	
 	
     public ImageAdapter(Context c){  
    	 super(c);
             mContext = c;    
            // multiChoose = isMulti;    
             this.mInflater = LayoutInflater.from(c);
        	 ac = (HomeActivity)mContext;

     }    
     
//     public ImageAdapter(Context c, List<Product> list){    
//         mContext = c;    
//         pro_list=list;   
//         this.mInflater = LayoutInflater.from(c);
//     
//
// }    
   

          
     @Override 
     public int getItemCount() {    
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
     public View getItemView(int position, View convertView, ViewGroup parent) {    
   
    	 Product pro = ac.ProductList.get(position);


    	 String url=ConnectionUtils.getInstance().ImageUrl+pro.getImg();
    	 String title=pro.getName();
    	 String ID=pro.getId();
		if(convertView == null){
    	 convertView =this.mInflater.inflate(R.layout.shelf_item, null);
		}
		else{
			if(imageCache.containsKey(url)){
				
			    			 ImageView imageView=(ImageView)convertView.findViewById(R.id.ImageView01_checklayout);
			    			 TextView textView=(TextView)convertView.findViewById(R.id.title_view);
			    			 textView.setText(title);
			    			 LinearLayout selector = (LinearLayout) convertView
			    						.findViewById(R.id.border_linear);
			    				if(ac.select_list.contains(pro))
			    	 			{
			    	 				
			    					selector.setBackgroundResource(R.color.red);
			    	 			}
			    				else{
			    					selector.setBackgroundResource(R.color.white);
			    				}
			     			 SoftReference<Drawable> sr = imageCache.get(url);
			     			 if(sr.get()!=null){
			     				 imageView.setImageDrawable(sr.get());
			     				 return convertView;
			     			 }
			     			 
			    		 }						
		}
		 ImageView imageView=(ImageView)convertView.findViewById(R.id.ImageView01_checklayout);
		 imageView.setImageResource(R.color.white);
		 TextView textView=(TextView)convertView.findViewById(R.id.title_view);
		 textView.setText(title);
		 LinearLayout selector = (LinearLayout) convertView
					.findViewById(R.id.border_linear);
		 selector.setBackgroundResource(R.color.white);
		 asyncImageLoad(imageView, url);
		 convertView.setOnClickListener(imageOnClickListen);

	     return convertView;
     }    
     
     //同步图片方法
     private void asyncImageLoad(ImageView imageView, String path) {
         // a.创建这个对象把imageView传进去.
    	 AsyncImageTask task = new AsyncImageTask(imageView);
    	 task.run(path);
        }
     
     //同步图片私有类
     private final class AsyncImageTask{
       private ImageView imageView;
       public AsyncImageTask(ImageView imageView) {
        	 // 设定参数
        	 this.imageView = imageView;
      }
      
      public void run(String url){
      HttpResourcesTask task = new HttpResourcesTask(mContext, HttpType.Img,
				CacheType.saveInSDcard);
		task.setParameter(url)
				.setOnFinishListen(new OnFinishListen() {

					@Override
					public void OnFinish(Task t, Object data) {
						// TODO Auto-generated method stub
						if (data != null
								&& !(data instanceof Exception)) {
							Drawable TampDraw = null;  
							if( t.getResult() instanceof Drawable){
								TampDraw=(Drawable) t.getResult();
							}
							else
							{
								TampDraw =mContext.getResources().getDrawable(R.drawable.android);											
							}
							imageCache.put(t.getTag().toString(), new SoftReference<Drawable>(TampDraw));
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
      
      final Handler handler = new Handler(){

       @Override
	public void handleMessage(Message msg) {

    	   Drawable uri = (Drawable)msg.obj;
        if(uri!=null && imageView!= null)

         imageView.setImageDrawable(uri);
       }
      };
      }
      
     
     OnClickListener imageOnClickListen = new OnClickListener() {


 		@Override
 		public void onClick(View view) {
 			// TODO Auto-generated method stub
 			
 		}
 	};
 	
 	public void handleImageView(ImageView imageView){
        currentImage.setImageDrawable(null);

        currentImage = imageView;
    }
 	
 	
     public void changeState(int position){    
  
            
             notifyDataSetChanged();       
     }

     @Override
 	public void onGridItemClicked(View view, int position, long arg2) {
 		// TODO Auto-generated method stub
 		
 	}

	
	
}


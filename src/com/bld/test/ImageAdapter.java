package com.bld.test;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;    

import com.bld.R;
import com.bld.activity.TestActivity;
import com.bld.object.Product;
import com.bld.task.HttpResourcesTask;
import com.bld.task.Task;
import com.bld.task.HttpResourcesTask.CacheType;
import com.bld.task.HttpResourcesTask.HttpType;
import com.bld.task.Task.OnFinishListen;
import com.bld.utils.ConnectionUtils;

import android.app.AlertDialog;
import android.content.Context;    
import android.graphics.Bitmap;    
import android.graphics.drawable.BitmapDrawable;    
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;    
import android.view.ViewGroup;    
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	 private Context mContext;                     // ����Context    
     private Vector<Integer> mImageIds = new Vector<Integer>();    // ����һ��������ΪͼƬԴ    
     private List<Product> pro_list = new ArrayList<Product> ();
      
     private int lastPosition = -1;            //��¼��һ��ѡ�е�ͼƬλ�ã�-1��ʾδѡ���κ�ͼƬ    
     private boolean multiChoose;                //��ʾ��ǰ�������Ƿ������ѡ    
     private LayoutInflater mInflater =null;
 	Integer tasksOver = 0;
 	Integer tasksLength=0;
 	private static HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
 	boolean canLoading=true;
 	private ImageView currentImage;
 	
 	
     public ImageAdapter(Context c, boolean isMulti){    
             mContext = c;    
             multiChoose = isMulti;    
             this.mInflater = LayoutInflater.from(c);
         

     }    
     
     public ImageAdapter(Context c, List<Product> list){    
         mContext = c;    
         pro_list=list;   
         this.mInflater = LayoutInflater.from(c);
     

 }    
   
          
     @Override 
     public int getCount() {    
             // TODO Auto-generated method stub    
             return pro_list.size();    
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
     public View getView(int position, View convertView, ViewGroup parent) {    
             // TODO Auto-generated method stub    

    	 View view = null;
   
    	 Bitmap mainBmp = ((BitmapDrawable)mContext.getResources().getDrawable(R.drawable.android)).getBitmap();   
    	 String url=ConnectionUtils.getInstance().ImageUrl+pro_list.get(position).getImg();
    	 String title=pro_list.get(position).getName();
    	 if (convertView != null)    
    	 {  
    		 if(imageCache.containsKey(url)){
	
    			 ImageView imageView=(ImageView)convertView.findViewById(R.id.ImageView01_checklayout);
    			 TextView textView=(TextView)convertView.findViewById(R.id.title_view);
    			 textView.setText(title);
     			 SoftReference<Drawable> sr = imageCache.get(url);
    			 Drawable drawable = null;
    			 drawable = sr.get();
    			 imageView.setImageDrawable(drawable);
    			 return convertView;
    		 }
		
    	 }
		 
		 view =this.mInflater.inflate(R.layout.shelf_item, null);
		 ImageView imageView=(ImageView)view.findViewById(R.id.ImageView01_checklayout);
		 TextView textView=(TextView)view.findViewById(R.id.title_view);
		 textView.setText(title);
		 asyncImageLoad(imageView, url);
		 view.setOnClickListener(imageOnClickListen);
	     return view;
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

       public void handleMessage(Message msg) {

    	   Drawable uri = (Drawable)msg.obj;
        if(uri!=null && imageView!= null)

         imageView.setImageDrawable(uri);
       }
      };
      }
      
     
     OnClickListener imageOnClickListen = new OnClickListener() {


 		@Override
 		public void onClick(View arg0) {
 			// TODO Auto-generated method stub
 			new AlertDialog.Builder(arg0.getContext()) .setTitle("Android testʾ") .setMessage("image click") .show();
 		}
 	};
 	
 	public void handleImageView(ImageView imageView){
        currentImage.setImageDrawable(null);

        currentImage = imageView;
    }
 	
 	
     public void changeState(int position){    
  
            
             notifyDataSetChanged();       
     }    
}


package com.bld.http.imageCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.bld.sdcard.SDcard;

public class AsyncImageCache {

	// 将请求过的网络图片 缓存到sdcard卡中 不要忘记加sdcard权限
	public static final String fileroot = Environment
			.getExternalStorageDirectory() + "/imageCache/";
	// 创建所需要的路径
	static {
		// SDcard可用
		if (SDcard.IsCanUseSdCard()) {
			try {
				File f = new File(fileroot);
				if (!f.exists()) {
					f.mkdirs();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

	// 获得此url的在sdcard的文件引用
	public  static Drawable getImageCache(String url,boolean isGetinSDcard) {
		// 从缓存中查找drawable
		SoftReference<Drawable> sr = imageCache.get(url);
		Drawable drawable = null;
		if (sr != null) {
			System.out.println("获得缓存池的图片"+url);
			drawable = sr.get();
		}
		else
		{
			System.out.println("缓存池图片已被销毁"+url);
		}
	
		// 如果缓存池中不存在图片引用则从sdcard中取
		if (drawable == null && SDcard.IsCanUseSdCard() && isGetinSDcard) {
			String fileurl = fileroot + SimpleUrlFileName.getLongUrlName(url);
			File f=new File(fileurl);
				if (f.exists()) {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inJustDecodeBounds = true;
					opts.inSampleSize = 1;
					Bitmap bm = BitmapFactory.decodeFile(fileurl,opts);
					//判断是否是图片，否则删除
                    if(bm==null)
                    {
                    	f.delete();
                    	return null;
                    }		
					drawable = new BitmapDrawable(bm);
					//将Sdcard获得的的图片加入缓存池中
					imageCache.put(url, new SoftReference<Drawable>(drawable));
					System.out.println("从sdcard中获得图片"+url);
				}
//			}
		}

		return drawable;
	}
   //是否允许存入sdcard
	public  static void saveDrawable(String url, Drawable drawable,boolean isSaveinSDcard)
			throws FileNotFoundException {
		if (drawable == null || url == null)
			return;
		System.out.println("保存缓存图片");
		SoftReference<Drawable> sr = imageCache.get(url);
		if (sr == null) {
			sr = new SoftReference<Drawable>(drawable);
			imageCache.put(url, sr);
			System.out.println("加入缓存图片到缓存池");
		}

		// SDcard不可用则返回 或者不允许使用缓存
		if (!SDcard.IsCanUseSdCard() || !isSaveinSDcard) {
			return;
		}

		String filename = SimpleUrlFileName.getLongUrlName(url);
		String fileurl = fileroot + filename;
		File f = new File(fileurl);
		// 不存在则创建
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			FileOutputStream fos = new FileOutputStream(f);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

			BitmapDrawable bd = (BitmapDrawable) drawable;
			Bitmap bm = bd.getBitmap();
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);// 把Bitmap对象解析成流
			try {
				fOut.flush();
				fOut.close();
				System.out.println("保存图片到Sdcard中"+url);
			} catch (IOException e) {
				System.out.println("保存图片失败"+url);
				e.printStackTrace();
			}
			;

		}

	}

}

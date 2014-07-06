package com.bld.task;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bld.http.imageCache.AsyncImageCache;
import com.bld.network.CustomerHttpClient;
import com.bld.state.GNetworkInfo;

public final class HttpResourcesTask extends Task {

	// 枚举类型 图片或�?文本
	public static enum HttpType {
		Img(), Text();
	};

	/**
	 * 缓存方式
	 * */

	public static enum CacheType {
		saveInSDcard(), saveOnlyInCache(), noSave;
	};

	// //是否使用缓存
	private CacheType cacheType;
	private static String debug = HttpResourcesTask.class.getSimpleName();
	private HttpType httpType;

	private HttpClientIO hcio = new HttpClientIO();


	public HttpResourcesTask(Context context, HttpType httpType,CacheType cacheType) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.httpType = httpType;
		this.cacheType = cacheType;
	}

	// 绑定视图
	public HttpResourcesTask(TaskViewHolder taskViewHolder, HttpType httpType,CacheType cacheType) {
		// TODO Auto-generated constructor stub
		super(taskViewHolder);
		this.httpType = httpType;
		this.cacheType = cacheType;
	}

	@Override
	public void shutDownExecute() {
		// TODO Auto-generated method stub
		try {
			if (hcio != null) {

				if (this.status == TaskStatus.running) {
					Log.i(debug, "编号" + this.taskID + "任务中断");
				}
				setWithout();
				if (thread != null)
					thread.interrupt();
				if (hcio.getInputStream() != null)
					hcio.getInputStream().close();
				if (hcio.getHttpEntity() != null)
					hcio.getHttpEntity().consumeContent();
				// if (hcio.getHttpClient() != null) {
				//
				// // ((AndroidHttpClient) hcio.getHttpClient()).close();
				// }

			}
		} catch (Exception e) {

		}
	}

	// 从优先从内存池中获得图片引用，第二优先级从SDCARD中获得引�?
	@Override
	public Object cacheData(Object parameter) {
		// TODO Auto-generated method stub

		if (parameter == null || cacheType == CacheType.noSave) {
			return null;
		}

		// 是否使用缓存方式

		if (httpType == HttpType.Img) {
			// 从缓存池中获得图片引�?如果存在则返�?
			Drawable d = null;
			switch (cacheType) {
			case saveOnlyInCache:
				d = AsyncImageCache.getImageCache(parameter.toString(), false);
				break;
			case saveInSDcard:
				d = AsyncImageCache.getImageCache(parameter.toString(), true);
				break;

			default:
				break;
			}

			System.out.println(d);

				return d;
		}
		return null;
	}

	@Override
	public Object obtainData(Object result, Object parameter) throws Exception {
		// TODO Auto-generated method stub
		return httpRequest(parameter.toString(), hcio);
	}

	public Object httpRequest(String url, HttpClientIO hcio)
			throws ClientProtocolException, IOException, Exception {
		isThreadShouDouwn();
		HttpClient client = null;
		HttpParams params = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		isThreadShouDouwn();
		// 如果网络连接没有打开则关�?
		if (context != null
				&& GNetworkInfo.isNetworkAvailable(context) == false) {
			return null;
		}

		try {
			client = CustomerHttpClient.getHttpClient();
			// client = AndroidHttpClient.newInstance("Android");
			params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 6000);
			HttpConnectionParams.setSocketBufferSize(params, 6000);
			isThreadShouDouwn();
			response = client.execute(new HttpGet(url));
			isThreadShouDouwn();
			entity = response.getEntity();

			is = entity.getContent();
			isThreadShouDouwn();
			if (hcio != null) {
				hcio.setInputStream(is);
				hcio.setHttpClient(client);
				hcio.setHttpEntity(entity);
			}

			isThreadShouDouwn();

			// 长度
			double contentlength = entity.getContentLength();

			if (entity.getContentType().toString().indexOf("image") != -1) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] b = new byte[1024];
				double count = 0;
				int length = -1;
				while ((length = is.read(b)) != -1) {
					baos.write(b, 0, length);
					count += length;
					System.out.println("进度"
							+ (int) (count / contentlength * 100f));
					if (onProgressListen != null) {
						int progress = (int) (count / contentlength * 100f) - 1;
						new OnProgressListenRunnable(this, progress < 0 ? 0
								: progress, result);
					}
				}

				// 进度未实�?
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = false;
				opts.inSampleSize = 1;
				byte[] imagebyte = baos.toByteArray();
				Bitmap bitmap = BitmapFactory.decodeByteArray(imagebyte, 0,
						imagebyte.length,opts);
				baos.close();
	
				is.close();
				BitmapDrawable bd = new BitmapDrawable(bitmap);

				// 是否使用缓存方式
				switch (cacheType) {
				case saveOnlyInCache:
					AsyncImageCache.saveDrawable(url, bd, false);
					break;
				case saveInSDcard:
					AsyncImageCache.saveDrawable(url, bd, true);
				case noSave:
					AsyncImageCache.saveDrawable(url, bd, true);
				default:
					break;
				}

				return bd;
			}

			if (entity.getContentType().toString().indexOf("text") != -1) {
				// 进度未实�?
				//
				StringBuilder sb = new StringBuilder();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 1024);

				char[] c = new char[1024];
				double count = 0;
				int length = -1;

				while ((length = reader.read(c)) != -1) {
					sb.append(c, 0, length);
					count += length;
					System.out.println("进度"
							+ (int) (count / contentlength * 100f));
					if (onProgressListen != null) {
						int progress = (int) (count / contentlength * 100f) - 1;
						new OnProgressListenRunnable(this, progress < 0 ? 0
								: progress, result);

					}
				}

				reader.close();
				
				
				return sb.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 抛出
			throw e;
		} finally {
			if (is != null)
				is.close();
			if (entity != null)
				entity.consumeContent();
			// if (client != null) {
			// // ((AndroidHttpClient) client).close();
			// client.getConnectionManager().shutdown();
			// }

			
		}

		return null;

	}

	public void isThreadShouDouwn() throws Exception {
		if (Thread.interrupted() || getStatus() == TaskStatus.without) {
			throw withoutException;
		}
	}

}

package com.bld.activity;

import java.io.IOException;
import java.util.Vector;

import com.bld.R;
import com.bld.code.camera.CameraManager;
import com.bld.code.decoding.CaptureActivityHandler;
import com.bld.code.decoding.InactivityTimer;
import com.bld.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;

public class CaptureActivity extends Activity{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private State state;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;

	private enum State {
	    SCANNING,
	    SHOWING,
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode);
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		
		surfaceHolder.addCallback(callbacker);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	    
		vibrate = true;
		
		state=State.SCANNING;
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}


	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);

		surfaceHolder.removeCallback(callbacker);
		txtResult.setText(obj.getBarcodeFormat().toString() + ":"
				+ obj.getText());
		
//		
//		state=State.SHOWING;
//		Intent toplantdetail=new Intent();
//		toplantdetail.setClass(CaptureActivity.this, PlantDetail.class);
//		toplantdetail.putExtra("id", "0");
//		toplantdetail.putExtra("name", "����");
//		startActivity(toplantdetail);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	      if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && state==State.SHOWING) {
	    	    viewfinderView.setVisibility(View.VISIBLE);
	    	    handler.sendEmptyMessage(R.id.restart_preview);
	    	    state=State.SCANNING;
	            return true;
	      }
	      return super.onKeyDown(keyCode, event);
	}
	
    private Callback callbacker=new Callback(){
		
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
		
			}	
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initCamera(surfaceHolder);
		
			}
		
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				hasSurface = false;
		
			}
	};
	
	private static final long VIBRATE_DURATION = 200L;

		
	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}
package com.bld.activity;

import java.io.IOException;
import java.util.List;

import com.bld.R;
import com.bld.object.Product;
import com.bld.utils.UserInfoCache;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	private RadioGroup group;
	private TabHost tabHost;
	public static final String TAB_HOME = "tabHome";
	public static final String TAB_Songdian = "tab_songdian";
	public static final String TAB_Gouwuche = "tab_gouwuche";
	public static final String TAB_Wo = "tab_wo";

	private Geocoder geocoder;
	private LocationManager locationManager;
	private TelephonyManager telephonyManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// LocationEngine.getinstence().setManager((LocationManager)
		// getSystemService(Context.LOCATION_SERVICE));
		UserInfoCache.getInstance().setUsername("测试人员");
		try {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
			telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


			UserInfoCache.getInstance().setTelephone(
					telephonyManager.getLine1Number());
		} catch (Exception ex) {

		}
		geocoder = new Geocoder(this);

		group = (RadioGroup) findViewById(R.id.main_radio);
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME)
				.setContent(new Intent(this, HomeActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_Songdian)
				.setIndicator(TAB_Songdian)
				.setContent(new Intent(this, SongActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_Gouwuche)
				.setIndicator(TAB_Gouwuche)
				.setContent(new Intent(this, CartActivity.class)));
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button0:
					tabHost.setCurrentTabByTag(TAB_HOME);
					getActionBar().setTitle(UserInfoCache.getInstance().getAddress());
					break;
				case R.id.radio_button1:
					tabHost.setCurrentTabByTag(TAB_Songdian);
					break;
				case R.id.radio_button2:
					tabHost.setCurrentTabByTag(TAB_Gouwuche);
					getActionBar().setTitle("購物車");
					break;
				case R.id.radio_button3:
					tabHost.setCurrentTabByTag(TAB_Wo);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_code) {
			Intent intent = new Intent();

			intent.setClass(MainActivity.this, CaptureActivity.class);

			intent.putExtra("str", "come from first activity");

			startActivity(intent);
		}
		return true;
	}

	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// 如果需要去到GPS启动后取到的Location,必须用这个！
			double latitude = location.getLatitude(); // 经度
			double longitude = location.getLongitude(); // 纬度
			UserInfoCache.getInstance().setLatitude(latitude);
			UserInfoCache.getInstance().setLongitude(longitude);

			List<Address> addresses;
			try {
				addresses = geocoder.getFromLocation(latitude, longitude, 1);

				if (addresses.isEmpty()) {
					Log.e("location", "addressed is Empty");
				} else {
					if (addresses.size() > 0) {
						String add = addresses.get(0).getLocality()
								+ addresses.get(0).getSubLocality()
								+ addresses.get(0).getThoroughfare();
						Log.e("location", add);
						getActionBar().setTitle(add);
						UserInfoCache.getInstance().setAddress(add);

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("位置", latitude + " " + longitude);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// Provider被disable时触发此函数，比如GPS被关闭

		}

		@Override
		public void onProviderEnabled(String provider) {
			// Provider被enable时触发此函数，比如GPS被打开

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数

		}

	};

	// @Override
	// protected void onResume() {
	// super.onResume();
	// // 当GPS定位时，在这里注册requestLocationUpdates监听就非常重要而且必要。没有这句话，定位不能成功。
	// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	// 1000, 1, locationListener);
	//
	// }
	//
	// @Override
	// protected void onPause() {
	// super.onPause(); // 取消注册监听
	// if (locationManager != null) {
	// locationManager.removeUpdates(locationListener);
	// }
	// }
}
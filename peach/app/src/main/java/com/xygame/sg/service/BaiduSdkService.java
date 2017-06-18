package com.xygame.sg.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import base.action.Action;
import base.frame.VisitUnit;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

/**
 *
 * 百度地图服务.
 *
 */
public class BaiduSdkService extends Service {

	private boolean isSend = true;

	@Override
	public void onCreate() {
		super.onCreate();
		SGApplication.getInstance().initBaiduLaction(new MyLocationListenner());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location == null)
				return;
			double lat=location.getLatitude();
			double lon=location.getLongitude();
			BaiduPreferencesUtil.setAddress(SGApplication.getInstance().getBaseContext(), location.getAddrStr());
			BaiduPreferencesUtil.setCity(SGApplication.getInstance().getBaseContext(), location.getCity());
//			BaiduPreferencesUtil.setCountryCode(SGApplication.getInstance().getBaseContext(), location.getCountryCode());
//			BaiduPreferencesUtil.setCoutryName(SGApplication.getInstance().getBaseContext(), location.getCountry());
			BaiduPreferencesUtil.setEara(SGApplication.getInstance().getBaseContext(), location.getDistrict());
			BaiduPreferencesUtil.setLat(SGApplication.getInstance().getBaseContext(), String.valueOf(lat));
			BaiduPreferencesUtil.setLon(SGApplication.getInstance().getBaseContext(), String.valueOf(lon));
			BaiduPreferencesUtil.setProvice(SGApplication.getInstance().getBaseContext(), location.getProvince());
			BaiduPreferencesUtil.setStreet(SGApplication.getInstance().getBaseContext(), location.getStreet());
			
			String userId=UserPreferencesUtil.getUserId(SGApplication.getInstance().getBaseContext());
			boolean islogin=UserPreferencesUtil.isOnline(SGApplication.getInstance().getBaseContext());
			if(location.getCity()!=null){
				Intent intent = new Intent("cn.com.xygame.sg.loaction.service");
				sendBroadcast(intent);
			}
			if(userId!=null){
				if(islogin){
					if(isSend){
						new Action("#.login.ResponseBaidu(${baidu_location_service},userId="+userId+",lng="+String.valueOf(lon)+",lat="+String.valueOf(lat)+")", null, null, new VisitUnit()).run();
						timerCount(60 *10* 1000, 1000);
					}
				}
			}
		}

	}

	private void timerCount(int minute, int second) {
		// TODO Auto-generated method stub
		new CountDownTimer(minute, second) {

			@Override
			public void onTick(long millisUntilFinished) {
				isSend = false;
			}

			@Override
			public void onFinish() {
				isSend = true;
			}
		}.start();

	}

}

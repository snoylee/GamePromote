package com.xygame.sg.im;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.define.listview.OnRefreshListener;
import com.xygame.sg.define.listview.RefreshListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
public class BaiduLocation extends SGBaseActivity implements OnClickListener,OnRefreshListener, OnGetPoiSearchResultListener,
OnItemClickListener,  OnGetGeoCoderResultListener{

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	private ShareLocationBean locationBean;
	private String latitude,Longitude,address;
	MapView mMapView;
	BaiduMap mBaiduMap;
	private View rightButton,backButton;
	private TextView titleName,rightButtonText;
	boolean isFirstLoc = true;// 是否首次定位
	private ActionPoilInfoAdapter locatinAdatper;
	private RefreshListView actPlushList;
	private PoiSearch mPoiSearch = null;
	private int locationIndex = 0;
	BitmapDescriptor bitmapIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.le_activity_location);
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		backButton=findViewById(R.id.backButton);
		titleName=(TextView)findViewById(R.id.titleName);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		actPlushList = (RefreshListView)findViewById(
				R.id.actPlushList);
		titleName.setText("位置");
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText("发送");
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
		rightButton=findViewById(R.id.rightButton);
		rightButton.setVisibility(View.VISIBLE);
		rightButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		actPlushList.setOnRefreshListener(this);
		actPlushList.setOnItemClickListener(this);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setIsNeedAddress(true);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		locationBean=new ShareLocationBean();
		locatinAdatper = new ActionPoilInfoAdapter(this, null);
		actPlushList.setAdapter(locatinAdatper);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			if(latitude==null){
				latitude=String.valueOf(location.getLatitude());
			}
			if(Longitude==null){
				Longitude=String.valueOf(location.getLongitude());
			}
			if (isFirstLoc) {
				address = location.getAddrStr();
				LatLng llA = new LatLng(location.getLatitude(),
						location.getLongitude());
				OverlayOptions ooA = new MarkerOptions().position(llA)
						.icon(bitmapIcon).zIndex(9).draggable(true);
				mBaiduMap.addOverlay(ooA);
				LatLngBounds bounds = new LatLngBounds.Builder().include(llA)
						.build();
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds
						.getCenter());
				mBaiduMap.animateMapStatus(u);
				if (address != null) {
					mPoiSearch.searchNearby(new PoiNearbySearchOption()
							.keyword(location.getStreet()).location(new LatLng(location.getLatitude(), location.getLongitude())) .radius(20000)
							.pageNum(locationIndex));
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mLocClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.rightButton){
			if(latitude==null){
				Toast.makeText(BaiduLocation.this, "维度定位失败！", Toast.LENGTH_SHORT).show();
			}else{
				
				if(Longitude==null){
					Toast.makeText(BaiduLocation.this, "精度定位失败！", Toast.LENGTH_SHORT).show();
				}else{
					if(address==null){
						Toast.makeText(BaiduLocation.this, "获取地理位置失败！", Toast.LENGTH_SHORT).show();
					}else{
						locationBean.setAddress(address);
						locationBean.setLatitude(latitude);
						locationBean.setLongitude(Longitude);
						Intent intent = new Intent();
						intent.putExtra("resultObject", locationBean);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				}
			}
			
		}else if(v.getId()==R.id.backButton){
			finish();
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}

		double selectLat = result.getLocation().latitude;
		double selectLon = result.getLocation().longitude;
		address = result.getAddress();
		mBaiduMap.clear();
		LatLng llA = new LatLng(selectLat, selectLon);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bitmapIcon)
				.zIndex(9).draggable(true);
		mBaiduMap.addOverlay(ooA);
		LatLngBounds bounds = new LatLngBounds.Builder().include(llA).build();
		MapStatusUpdate u = MapStatusUpdateFactory
				.newLatLng(bounds.getCenter());
		mBaiduMap.setMapStatus(u);

		locationIndex = 0;
		mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(address)
				.location(new LatLng(selectLat, selectLon))
				.pageNum(locationIndex));
	}


	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void stopMyLoadIcon() {
		actPlushList.onRefreshFinish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (locatinAdatper.getCount() > 0) {
			PoiInfo item = locatinAdatper.getItem(arg2 - 1);
			address=item.address;
			latitude=String.valueOf(item.location.latitude);
			Longitude=String.valueOf(item.location.longitude);
			LatLng llA = new LatLng(item.location.latitude,
					item.location.longitude);
			OverlayOptions ooA = new MarkerOptions().position(llA)
					.icon(bitmapIcon).zIndex(9).draggable(true);
			mBaiduMap.clear();
			mBaiduMap.addOverlay(ooA);
			LatLngBounds bounds = new LatLngBounds.Builder().include(llA)
					.build();
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds
					.getCenter());
			mBaiduMap.setMapStatus(u);
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		stopMyLoadIcon();
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//			Toast.makeText(this, "未找到", Toast.LENGTH_LONG).show();
			return;
		} else {
			isFirstLoc = false;
			if (locationIndex == 0) {
				locatinAdatper.clearDatas();
			}
			List<PoiInfo> PoiInfos = result.getAllPoi();
			locatinAdatper.addDatas(PoiInfos);
			locatinAdatper.notifyDataSetChanged();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(latitude!=null){
			locationIndex = 0;
			mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(address)
					.location(new LatLng(Double.parseDouble(latitude), Double.parseDouble(Longitude))).pageNum(locationIndex));
		}
	}

	@Override
	public void onLoadMoring() {
		// TODO Auto-generated method stub
		if(latitude!=null){
			locationIndex = locationIndex + 1;
			mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(address)
					.location(new LatLng(Double.parseDouble(latitude), Double.parseDouble(Longitude))).pageNum(locationIndex));
		}
	}
}

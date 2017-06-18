package com.xygame.sg.im;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowBaiduLocation extends SGBaseActivity implements OnClickListener{

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private View backButton;
	private TextView titleName;
	private TransferBean tBean;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bitmapIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_map_show_layout);
		mMapView = (MapView) findViewById(R.id.bmapView);
		backButton=findViewById(R.id.backButton);
		titleName=(TextView)findViewById(R.id.titleName);
		titleName.setText(getIntent().getStringExtra("titleStr"));
		backButton.setOnClickListener(this);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
	}

	public void initOverlay() {
		LatLng llA = null;
		try {
			tBean=(TransferBean) getIntent().getSerializableExtra("bean");
			llA = new LatLng(Double.parseDouble(tBean.getLatitude()),Double.parseDouble(tBean.getLongitude()));
			OverlayOptions ooA = new MarkerOptions().position(llA).icon(bitmapIcon)
					.zIndex(9).draggable(true);
			mBaiduMap.addOverlay(ooA);
			LatLngBounds bounds = new LatLngBounds.Builder().include(llA).build();
	//
//			OverlayOptions ooGround = new GroundOverlayOptions()
//					.positionFromBounds(bounds).image(bitmapIcon).transparency(0.8f);
//			mBaiduMap.addOverlay(ooGround);

			MapStatusUpdate u = MapStatusUpdateFactory
					.newLatLng(bounds.getCenter());
			mBaiduMap.setMapStatus(u);
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		initOverlay();
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bitmapIcon.recycle();
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backButton){
			finish();
		}
	}
}
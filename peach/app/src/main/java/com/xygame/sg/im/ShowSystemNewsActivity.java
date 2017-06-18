package com.xygame.sg.im;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.List;

public class ShowSystemNewsActivity extends SGBaseActivity implements OnClickListener{
	private View backButton;
	private TextView titleName;
	private ListView listView;
	private SystemNewsAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_system_layout);
		backButton=findViewById(R.id.backButton);
		titleName=(TextView)findViewById(R.id.titleName);
		listView=(ListView)findViewById(R.id.listView);
		titleName.setText("模范儿");
		backButton.setOnClickListener(this);

		List<SGNewsBean> datas=NewsEngine.getAllSystemNews(this);

		adapter=new SystemNewsAdapter(this,datas);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backButton){
			finish();
		}
	}
}
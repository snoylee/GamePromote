package com.xygame.sg.activity.notice;

import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.CountryAdapter;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Country;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import base.data.net.http.JsonUtil;

public class PlushNoticePlaceCountryActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

	private View backButton, locationView, allTopView;
	private TextView titleName, locationCountryText;
	private ListView countryList;
	private CountryAdapter adapter;
	private List<Map<String, String>> datas;
	private String countryName, countryCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_editor_country_layout);
		initViews();
		initListeners();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		locationView = findViewById(R.id.locationView);
		allTopView = findViewById(R.id.allTopView);
		titleName = (TextView) findViewById(R.id.titleName);
		locationCountryText = (TextView) findViewById(R.id.locationCountryText);
		countryList = (ListView) findViewById(R.id.countryList);
	}

	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		locationView.setOnClickListener(this);
		countryList.setOnItemClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		String countryName = BaiduPreferencesUtil.getCoutryName(this);
		if (countryName != null) {
			allTopView.setVisibility(View.VISIBLE);
			locationCountryText.setText(countryName);
		} else {
			allTopView.setVisibility(View.GONE);
		}
		titleName.setText(getResources().getString(R.string.sg_comm_editor_country_title));
		datas = (List<Map<String, String>>) new JsonUtil().from(Country.txt);

		countryName = getIntent().getStringExtra("data");
		adapter = new CountryAdapter(this, datas, countryName);
		countryList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backButton:
			finish();
			break;

		case R.id.locationView:
			countryName = locationCountryText.getText().toString();
			countryCode = adapter.ctries.get(countryName);
			finishActivity();
			break;

		}
	}

	public void finishActivity() {
		Intent intent = new Intent();
		intent.putExtra("countryCode", countryCode);
		intent.putExtra("countryName", countryName);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	/**
	 * 重载方法
	 *
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		countryName = adapter.list.get(arg2);
		countryCode = adapter.ctries.get(countryName);
		finishActivity();
	}
}

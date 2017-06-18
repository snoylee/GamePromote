package com.xygame.sg.activity.personal;

import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.CarrierAdapter;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.frame.VisitUnit;

public class SelectJobTypeActivity extends SGBaseActivity implements View.OnClickListener, OnItemClickListener {
	private View backButton;
	private TextView titleName;
	private ListView countryList;
	private CarrierAdapter adapter;
	private CarrierBean carrierBean;

	private boolean submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_job_type);
		initViews();
		addListener();
	}

	private void initViews() {
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);

		titleName.setText(getText(R.string.title_activity_select_job_type));

		countryList = (ListView) findViewById(R.id.countryList);
		
		List<CarrierBean> datas = null;
		if (Constants.CARRE_PHOTOR.equals(UserPreferencesUtil.getUserType(this))) {
			datas = SGApplication.getInstance().getCmCarriers();
		} else {
			datas = SGApplication.getInstance().getModelCarriers();
		}
		if (getIntent().hasExtra("isModel")){
			boolean isModel = getIntent().getBooleanExtra("isModel",true);
			if (isModel){
				datas = SGApplication.getInstance().getModelCarriers();
			}
		}
		String strFlag = getIntent().getStringExtra("strFlag");
		if(datas!=null){
			adapter = new CarrierAdapter(this, datas,strFlag);
			countryList.setAdapter(adapter);
		}

		if (getIntent().hasExtra("isSubmit")){
			submit = getIntent().getBooleanExtra("isSubmit",true);
		} else {
			submit = false;
		}
	}

	private void addListener() {
		backButton.setOnClickListener(this);
		countryList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			finish();
			break;
		}
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
		carrierBean = adapter.getItem(arg2);
		transferLocationService();
	}

	public CarrierBean getCarrierBean() {
		return carrierBean;
	}

	public void setDatas(List<CarrierBean> datas) {
		
	}

	public void finishActivity() {
		Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
		sendBroadcast(intent);
		finish();
	}

	private void transferLocationService() {

		if (!submit){
			VisitUnit visit = new VisitUnit();
			new Action("#.personal.EditorUserCarrier(${editUser})", this, null, visit).run();
		} else {
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, carrierBean);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
}
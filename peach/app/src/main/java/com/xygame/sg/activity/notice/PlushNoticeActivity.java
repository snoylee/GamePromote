/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.CommonActivity;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.EditorTextContentActivity;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.personal.CameraBigTypeActivity;
import com.xygame.sg.activity.personal.SelectFirstCategoryActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.define.view.ChoiceDateWeekTimer;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.WheelUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class PlushNoticeActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName, typeText, themeText, addressText, startTimeText, endTimeText, reportTimeText;
	private ImageView rightbuttonIcon;
	private View backButton, rightButton, camerType, camerTheme, camerAddress, startTime, endTime, reportTime,
			nextButton, camerNum;
	private EditText camerNumText;
	/**
	 * 拍摄大类name
	 */
	private String priceTypeName, cameraTheme;
	/**
	 * 拍摄大类id
	 */
	private String priceType;
	private PlushNoticeAreaBean areaBean;
	private EditText introduceText;
	private PlushNoticeBean pnBean;
	private boolean isStrictCity = false;
	private String provinceName, cityName, provinceCode, cityCode;
	private List<ProvinceBean> datas;
	private List<ShootTypeBean> dataList;
	/**
	 * 重载方法
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_plush_notice_layout, null));

		initViews();
		initListeners();
		initDatas();
		setLocationPosion();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		camerType.setOnClickListener(this);
		camerTheme.setOnClickListener(this);
		camerAddress.setOnClickListener(this);
		startTime.setOnClickListener(this);
		endTime.setOnClickListener(this);
		reportTime.setOnClickListener(this);
		nextButton.setOnClickListener(this);
//		camerNum.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		introduceText = (EditText) findViewById(R.id.introduceText);
		titleName = (TextView) findViewById(R.id.titleName);
		typeText = (TextView) findViewById(R.id.typeText);
		themeText = (TextView) findViewById(R.id.themeText);
		addressText = (TextView) findViewById(R.id.addressText);
		startTimeText = (TextView) findViewById(R.id.startTimeText);
		endTimeText = (TextView) findViewById(R.id.endTimeText);
		reportTimeText = (TextView) findViewById(R.id.reportTimeText);
		camerNumText = (EditText) findViewById(R.id.camerNumText);

		backButton = findViewById(R.id.backButton);
		camerNum = findViewById(R.id.camerNum);
		rightButton = findViewById(R.id.rightButton);
		endTime = findViewById(R.id.endTime);
		camerType = findViewById(R.id.camerType);
		camerTheme = findViewById(R.id.camerTheme);
		camerAddress = findViewById(R.id.camerAddress);
		startTime = findViewById(R.id.startTime);
		reportTime = findViewById(R.id.reportTime);
		nextButton = findViewById(R.id.nextButton);

		rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		pnBean = new PlushNoticeBean();
		if("noPay".equals(getIntent().getStringExtra("noticeFlag"))){
			pnBean.setNoticeType("1");
		}else{
			pnBean.setNoticeType("2");
		}
		areaBean = new PlushNoticeAreaBean();
		titleName.setText("发布通告");
		rightButton.setVisibility(View.VISIBLE);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.sg_con_service);
		camerNumText.setText("1");
		datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
		datas.remove(0);
		dataList = SGApplication.getInstance().getTypeList();
		if (dataList==null){
			dataList=new ArrayList<>();
		}

		if (dataList.size()>0){
			ShootTypeBean selectedMap = dataList.get(0);
			pnBean.setCameraParantTypeId(selectedMap.getTypeId() + "");
			pnBean.setCameraParentTypeName(selectedMap.getTypeName());
			typeText.setText(selectedMap.getTypeName());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			String userId=UserPreferencesUtil.getUserId(this);
			Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
			sendBroadcast(intent);
			finish();
		} else if (v.getId() == R.id.camerType) {
			Intent firstIntent = new Intent(this, CameraBigTypeActivity.class);
			firstIntent.putExtra("selectFirstCategoryId", pnBean);
			startActivityForResult(firstIntent, 0);
		} else if (v.getId() == R.id.camerTheme) {
			Intent intent = new Intent(this, EditorTextContentActivity.class);
			intent.putExtra(Constants.EDITOR_TEXT_TITLE, "拍摄主题");
			intent.putExtra("oral", cameraTheme);
			intent.putExtra("hint", "请输入本次拍摄的主题内容...");
			intent.putExtra(Constants.TEXT_EDITOR_NUM, 15);
			startActivityForResult(intent, 1);
		} else if (v.getId() == R.id.rightButton) {
//			//联系客服
//			if (UserPreferencesUtil.getServicePhone(this)!=null&&!"null".equals(UserPreferencesUtil.getServicePhone(this))){
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPreferencesUtil.getServicePhone(this)));
//				startActivity(intent);
//			}else{
//				Toast.makeText(this, "系统维护中", Toast.LENGTH_SHORT).show();
//			}
		} else if (v.getId() == R.id.camerAddress) {
			Intent firstIntent = new Intent(this, PlushNoticePlaceActivity.class);
			firstIntent.putExtra("bean", areaBean);
			startActivityForResult(firstIntent, 2);
		} else if (v.getId() == R.id.startTime) {
			Intent firstIntent = new Intent(this, ChoiceDateWeekTimer.class);
			startActivityForResult(firstIntent, 4);
		} else if (v.getId() == R.id.endTime) {
			Intent firstIntent = new Intent(this, ChoiceDateWeekTimer.class);
			startActivityForResult(firstIntent, 5);
		} else if (v.getId() == R.id.reportTime) {
			Intent firstIntent = new Intent(this, ChoiceDateWeekTimer.class);
			startActivityForResult(firstIntent, 6);
		} else if (v.getId() == R.id.nextButton) {
			if (isTrue()) {
				pnBean.setCamerNum(camerNumText.getText().toString().trim());
				String tipContent=introduceText.getText().toString().trim();
				if(!"".equals(tipContent)){
					pnBean.setNoticeTip(tipContent);
				}
				Intent firstIntent = new Intent(this, PlushNoticeRequestModelActivity.class);
				firstIntent.putExtra("bean", pnBean);
				startActivityForResult(firstIntent, 3);
			}
		} else if (v.getId() == R.id.camerNum) {
			WheelUtil.startWheelActivity(7, WheelUtil.WHEEL_NUMBER_CM_NUM, this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			PlushNoticeBean tempBean=(PlushNoticeBean) data.getSerializableExtra(Constants.COMEBACK);
			priceTypeName = tempBean.getCameraParentTypeName();
			typeText.setText(priceTypeName);
			priceType = tempBean.getCameraParantTypeId();
			pnBean.setCameraParantTypeId(tempBean.getCameraParantTypeId());
			pnBean.setCameraParentTypeName(tempBean.getCameraParentTypeName());
			break;
		}
		case 1:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			cameraTheme = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
			themeText.setText(cameraTheme);
			pnBean.setCameraTheme(cameraTheme);
			break;
		case 2:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			areaBean = (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
			updateAreaText();
			pnBean.setCameraArea(areaBean);
			break;
		case 3:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			pnBean = (PlushNoticeBean) data.getSerializableExtra(Constants.COMEBACK);
			boolean flag = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			if (flag) {
				Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
				sendBroadcast(intent);
				Intent intent1 = new Intent(this, NoticeManagmentActivity.class);
				startActivity(intent1);
				finish();
			}
			break;
		case 4:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			FeedbackDateBean ftBean2 = (FeedbackDateBean) data.getSerializableExtra("bean");
			if (ftBean2 != null) {
				if (getLeftTime(Long.parseLong(ftBean2.getTimeLong()))){
					pnBean.setStarTime(ftBean2.getTimeLong());
					pnBean.setStartTimeDes(ftBean2.getDateAllDesc());
					pnBean.setFormatStartTime(ftBean2.getFormatDateStr());
					startTimeText.setText(pnBean.getStartTimeDes());
				}else{
					Toast.makeText(this,"拍摄开始时间大于当前时间24小时",Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case 5:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			FeedbackDateBean ftBean = (FeedbackDateBean) data.getSerializableExtra("bean");
			if (pnBean.getStarTime()!=null&&!"".equals(pnBean.getStarTime())){
				if (ftBean != null) {
					if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(ftBean.getTimeLong())),CalendarUtils.getHenGongDateDis(Long.parseLong(pnBean.getStarTime())))){
						pnBean.setEndTime(ftBean.getTimeLong());
						pnBean.setEndTimeDes(ftBean.getDateAllDesc());
						pnBean.setFormatEndTime(ftBean.getFormatDateStr());
						endTimeText.setText(pnBean.getEndTimeDes());
					}else{
						Toast.makeText(getApplicationContext(), "结束拍摄时间要大于开始拍摄时间", Toast.LENGTH_SHORT).show();
					}
				}
			}else{
				Toast.makeText(getApplicationContext(), "请先选择开始拍摄时间", Toast.LENGTH_SHORT).show();
			}
			break;
		case 6:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			FeedbackDateBean ftBean1 = (FeedbackDateBean) data.getSerializableExtra("bean");
			if (ftBean1 != null) {
//				long disTime=getLeftTime(Long.parseLong(ftBean1.getTimeLong()));
//				if(disTime>1){
					pnBean.setReportTime(ftBean1.getTimeLong());
					pnBean.setReportTimeDes(ftBean1.getDateAllDesc());
					pnBean.setFormatReportTime(ftBean1.getFormatDateStr());
					reportTimeText.setText(pnBean.getReportTimeDes());
//				}else{
//					Toast.makeText(getApplicationContext(), "报名截止时间不能小于开始拍摄时间24小时", Toast.LENGTH_SHORT).show();
//				}
			}
			break;
		case 7: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			pnBean.setCamerNum(s);
			camerNumText.setText(s);
			break;
		}
		default:
			break;
		}
	}
	
	public boolean getLeftTime(long startTime) {
		boolean flag=false;
		long hours=0;
		long minutes;
		long diff;
		long days=0;
		long limitTime=0;
		StringBuffer sb = new StringBuffer();
		try {
			Date d2 = new Date();
			Date d1 = new Date(startTime);
			diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
			limitTime=diff/1000;
			days = limitTime / (24 * 60 * 60);
			hours =  (limitTime % (24 * 60 * 60)) / (60 * 60);
			minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
			if (days != 0) {
				String dayStr = String.valueOf(days).concat("天");
				sb.append(dayStr);
			}
			if (hours != 0) {
				String hourStr = String.valueOf(hours).concat("小时");
				sb.append(hourStr);
			}
			if (minutes != 0) {
				String minStr = String.valueOf(minutes).concat("分");
				sb.append(minStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (days>0){
			flag=true;
		}
		return flag;
	}

	private void updateAreaText() {
		if (areaBean.getProvinceName() != null) {
			if (areaBean.getCityName() != null) {
				if (areaBean.getProvinceName().equals(areaBean.getCityName())) {
					addressText.setText(areaBean.getProvinceName());
				} else {
					addressText.setText(areaBean.getProvinceName().concat(" ").concat(areaBean.getCityName()));
				}
			} else {
				addressText.setText(areaBean.getProvinceName());
			}
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private boolean isTrue() {
		boolean flag = false;
		// addressText, startTimeText, endTimeText, reportTimeText,
		if ("".equals(typeText.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请选择拍摄类型", Toast.LENGTH_SHORT).show();
		} else {
			if ("".equals(themeText.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请选择拍摄主题", Toast.LENGTH_SHORT).show();
			} else {
				if ("".equals(addressText.getText().toString())) {
					Toast.makeText(getApplicationContext(), "请选择拍摄地点", Toast.LENGTH_SHORT).show();
				} else {
					if ("".equals(startTimeText.getText().toString())) {
						Toast.makeText(getApplicationContext(), "请选择拍摄开始时间", Toast.LENGTH_SHORT).show();
					} else {
						if ("".equals(endTimeText.getText().toString())) {
							Toast.makeText(getApplicationContext(), "请选择拍摄结束时间", Toast.LENGTH_SHORT).show();
						} else {
							if (StringUtil.isBigger(pnBean.getFormatStartTime(), pnBean.getFormatEndTime())) {
								Toast.makeText(getApplicationContext(), "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
							} else {
								if ("".equals(camerNumText.getText().toString().trim())){
									Toast.makeText(getApplicationContext(), "请输入摄影人数", Toast.LENGTH_SHORT).show();
								}else {
									flag = true;
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}

	private void setLocationPosion() {
		// TODO Auto-generated method stub
		provinceName = BaiduPreferencesUtil.getProvice(this);

		if (provinceName != null) {
			for (String str : Constants.CITY_STRICT) {
				if (provinceName.contains(str)) {
					isStrictCity = true;
					cityName = BaiduPreferencesUtil.getCity(this);
				}
			}
			if (isStrictCity) {
				getCodeAction();
				updateAreaText();
				pnBean.setCameraArea(areaBean);
			} else {
				cityName = BaiduPreferencesUtil.getCity(this);
				getCodeAction();
				updateAreaText();
				pnBean.setCameraArea(areaBean);
			}

		}
	}

	private void getCodeAction() {
		// TODO Auto-generated method stub
		try {
			for (ProvinceBean it : datas) {
				it.get();
				if (provinceName.contains(it.getProvinceName())) {
					provinceCode = it.getProvinceCode();
					if (isStrictCity) {
						List<CityBean> cityDatas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceCode));
						for (CityBean cIt : cityDatas) {
							cIt.get();
							for (String str : Constants.CITY_STRICT) {
								if (cIt.getCityName().contains(str)) {
									cityCode = cIt.getCityCode();
									areaBean.setCityId(cityCode);
									areaBean.setCityName(cityName);
									areaBean.setProvinceId(provinceCode);
									areaBean.setProvinceName(provinceName);
								}
							}
						}
					}else{
						areaBean.setProvinceId(provinceCode);
						areaBean.setProvinceName(provinceName);
						areaBean.setCityId(null);
						areaBean.setCityName(null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

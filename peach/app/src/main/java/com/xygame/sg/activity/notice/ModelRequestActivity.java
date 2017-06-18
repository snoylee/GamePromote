/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.CommonActivity;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.SelectCountryActivity;
import com.xygame.sg.activity.commen.bean.CountryBean;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.define.view.SingleWheelView;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.WheelUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class  ModelRequestActivity extends SGBaseActivity implements OnClickListener ,OnCheckedChangeListener{

	private TextView titleName, rightButtonText, sexText, locationText, countryText, ageSmallValue, bodySmallValue,
			bodyBigValue, WeightSmallValue, WeightBigValue, XWSmallValue, XWBigValue, YWSmallValue, YWBigValue,
			TWSmallValue, TWBigValue, ZBSmallValue, ZBBigValue, XMSmallValue, XMBigValue, modelNumsText, ageBigValue;
	private View backButton, rightButton, modelSex, modelNums, locationView, countryView, smallAgeView, bigAgeView,
			smallBodyView, bigBodyView, smallWeightView, bigWeightView, smallXWView, bigXWView, smallYWView, bigYWView,
			smallTWView, bigTWView, smallZBView, bigZBView, smallXMView, bigXMView;
	private EditText priceText, requestModelTip;
	private CheckBox isClf, isZsf;
	private ModelRequestBean modelBean;
	private boolean isNew;
	private int index;
	private PlushNoticeAreaBean areaBean;
	private String countryName;
	private boolean isStrictCity = false;
	private String provinceName, cityName, provinceCode, cityCode;
	private List<ProvinceBean> datas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_model_request_layout, null));

		initViews();
		initListeners();
		initDatas();
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
		modelSex.setOnClickListener(this);
		modelNums.setOnClickListener(this);
		locationView.setOnClickListener(this);
		countryView.setOnClickListener(this);
		smallAgeView.setOnClickListener(this);
		bigAgeView.setOnClickListener(this);
		smallBodyView.setOnClickListener(this);
		bigBodyView.setOnClickListener(this);
		smallWeightView.setOnClickListener(this);
		bigWeightView.setOnClickListener(this);
		smallXWView.setOnClickListener(this);
		bigXWView.setOnClickListener(this);
		smallYWView.setOnClickListener(this);
		bigYWView.setOnClickListener(this);
		smallTWView.setOnClickListener(this);
		bigTWView.setOnClickListener(this);
		smallZBView.setOnClickListener(this);
		bigZBView.setOnClickListener(this);
		smallXMView.setOnClickListener(this);
		bigXMView.setOnClickListener(this);
		isClf.setOnCheckedChangeListener(this);
		isZsf.setOnCheckedChangeListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		sexText = (TextView) findViewById(R.id.sexText);
		priceText = (EditText) findViewById(R.id.priceText);
		requestModelTip = (EditText) findViewById(R.id.requestModelTip);
		isClf = (CheckBox) findViewById(R.id.isClf);
		isZsf = (CheckBox) findViewById(R.id.isZsf);
		locationText = (TextView) findViewById(R.id.locationText);
		countryText = (TextView) findViewById(R.id.countryText);
		ageSmallValue = (TextView) findViewById(R.id.ageSmallValue);
		bodySmallValue = (TextView) findViewById(R.id.bodySmallValue);
		bodyBigValue = (TextView) findViewById(R.id.bodyBigValue);
		WeightSmallValue = (TextView) findViewById(R.id.WeightSmallValue);
		WeightBigValue = (TextView) findViewById(R.id.WeightBigValue);
		XWSmallValue = (TextView) findViewById(R.id.XWSmallValue);
		XWBigValue = (TextView) findViewById(R.id.XWBigValue);
		YWSmallValue = (TextView) findViewById(R.id.YWSmallValue);
		YWBigValue = (TextView) findViewById(R.id.YWBigValue);
		TWSmallValue = (TextView) findViewById(R.id.TWSmallValue);
		TWBigValue = (TextView) findViewById(R.id.TWBigValue);
		ZBSmallValue = (TextView) findViewById(R.id.ZBSmallValue);
		ZBBigValue = (TextView) findViewById(R.id.ZBBigValue);
		XMSmallValue = (TextView) findViewById(R.id.XMSmallValue);
		XMBigValue = (TextView) findViewById(R.id.XMBigValue);
		modelNumsText = (TextView) findViewById(R.id.modelNumsText);
		ageBigValue = (TextView) findViewById(R.id.ageBigValue);

		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		modelSex = findViewById(R.id.modelSex);
		modelNums = findViewById(R.id.modelNums);
		locationView = findViewById(R.id.locationView);
		countryView = findViewById(R.id.countryView);
		smallAgeView = findViewById(R.id.smallAgeView);
		bigAgeView = findViewById(R.id.bigAgeView);
		smallBodyView = findViewById(R.id.smallBodyView);
		bigBodyView = findViewById(R.id.bigBodyView);
		smallWeightView = findViewById(R.id.smallWeightView);
		bigWeightView = findViewById(R.id.bigWeightView);
		smallXWView = findViewById(R.id.smallXWView);
		bigXWView = findViewById(R.id.bigXWView);
		smallYWView = findViewById(R.id.smallYWView);
		bigYWView = findViewById(R.id.bigYWView);
		smallTWView = findViewById(R.id.smallTWView);
		bigTWView = findViewById(R.id.bigTWView);
		smallZBView = findViewById(R.id.smallZBView);
		bigZBView = findViewById(R.id.bigZBView);
		smallXMView = findViewById(R.id.smallXMView);
		bigXMView = findViewById(R.id.bigXMView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		areaBean=new PlushNoticeAreaBean();
		titleName.setText("招募模特要求");
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText("确定");
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
		priceText.setText("500");
		String flag = getIntent().getStringExtra("flag");

		if ("new".equals(flag)) {
			isNew = true;
			modelBean = new ModelRequestBean();
			index = getIntent().getIntExtra("index", 0);
			datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
			datas.remove(0);
			modelBean.setSexId(Constants.SEX_WOMAN);
			modelBean.setSexName("女");
			modelBean.setNeedPrice("500");
			modelBean.setNeedNum("1");
			setLocationPosion();
			updateAreaText();
		} else {
			isNew = false;
			modelBean = (ModelRequestBean) getIntent().getSerializableExtra("bean");
			updateAllViews();
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void updateAllViews() {
		// TODO Auto-generated method stub
		modelBean.setNeedPrice(priceText.getText().toString().trim());
		sexText.setText(modelBean.getSexName());
		modelNumsText.setText(modelBean.getNeedNum());
		priceText.setText(modelBean.getNeedPrice());
		if (modelBean.getCityName() != null) {
			areaBean.setCityId(modelBean.getCityId());
			areaBean.setCityName(modelBean.getCityName());
			areaBean.setProvinceId(modelBean.getProvinceId());
			areaBean.setProvinceName(modelBean.getProvinceName());
			locationText.setText(modelBean.getCityName());
		}
		if (modelBean.getCountryName() != null) {
			countryName=modelBean.getCountryName();
			countryText.setText(countryName);
		}
		if (modelBean.getSmallAge() != null) {
			ageSmallValue.setText(modelBean.getSmallAge());
		}
		if (modelBean.getBigAge() != null) {
			ageBigValue.setText(modelBean.getBigAge());
		}
		if (modelBean.getSmallBodyHight() != null) {
			bodySmallValue.setText(modelBean.getSmallBodyHight());
		}
		if (modelBean.getBigBodyHight() != null) {
			bodyBigValue.setText(modelBean.getBigBodyHight());
		}
		if (modelBean.getSmallWeight() != null) {
			WeightSmallValue.setText(modelBean.getSmallWeight());
		}
		if (modelBean.getBigWeight() != null) {
			WeightBigValue.setText(modelBean.getBigWeight());
		}
		if (modelBean.getSmallXiongWei() != null) {
			XWSmallValue.setText(modelBean.getSmallXiongWei());
		}
		if (modelBean.getBigXiongWei() != null) {
			XWBigValue.setText(modelBean.getBigXiongWei());
		}
		if (modelBean.getSmallYaoWei() != null) {
			YWSmallValue.setText(modelBean.getSmallYaoWei());
		}
		if (modelBean.getBigYaoWei() != null) {
			YWBigValue.setText(modelBean.getBigYaoWei());
		}
		if (modelBean.getSmallTunWei() != null) {
			TWSmallValue.setText(modelBean.getSmallTunWei());
		}
		if (modelBean.getBigTunWei() != null) {
			TWBigValue.setText(modelBean.getBigTunWei());
		}
		if (modelBean.getSmallCupName() != null) {
			ZBSmallValue.setText(modelBean.getSmallCupName());
		}
		if (modelBean.getBigCupName() != null) {
			ZBBigValue.setText(modelBean.getBigCupName());
		}
		if (modelBean.getSmallShoese() != null) {
			XMSmallValue.setText(modelBean.getSmallShoese());
		}
		if (modelBean.getBigShoese() != null) {
			XMBigValue.setText(modelBean.getBigShoese());
		}
		if (modelBean.isBaoXiaoCaiLv()) {
			isClf.setChecked(true);
		} else {
			isClf.setChecked(false);
		}
		if (modelBean.isBaoXiaoZhuSu()) {
			isZsf.setChecked(true);
		} else {
			isZsf.setChecked(false);
		}
		if (modelBean.getBeizhuStr() != null) {
			requestModelTip.setText(modelBean.getBeizhuStr());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.rightButton) {
			if(isTure()){
				modelBean.setNeedPrice(priceText.getText().toString().trim());
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, modelBean);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, isNew);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		} else if (v.getId() == R.id.modelSex) {
			WheelUtil.startWheelActivity(0, WheelUtil.WHEEL_NUMBER_RECRUIT_GENDER, this);
		} else if (v.getId() == R.id.modelNums) {
			WheelUtil.startWheelActivity(1, WheelUtil.WHEEL_NUMBER_RECRUIT_PEOPLE, this);
		}else if (v.getId() == R.id.locationView) {
			 Intent firstIntent = new Intent(this,PlushNoticePlaceProvinceActivity.class);
             firstIntent.putExtra("bean",areaBean);
             firstIntent.putExtra("noLimitFlag", true);
             startActivityForResult(firstIntent, 18);
		} else if (v.getId() == R.id.countryView) {
			Intent intent = new Intent(this, SelectCountryActivity.class);
//			intent.putExtra("data", countryName);
			startActivityForResult(intent, 19);
		} else if (v.getId() == R.id.smallAgeView) {
			WheelUtil.startWheelActivity(2, WheelUtil.WHEEL_NUMBER_AGE, this);
		} else if (v.getId() == R.id.bigAgeView) {
			WheelUtil.startWheelActivity(3, WheelUtil.WHEEL_NUMBER_AGE, this);
		} else if (v.getId() == R.id.smallBodyView) {
			WheelUtil.startWheelActivity(4, WheelUtil.WHEEL_NUMBER_HEIGHT, this);
		} else if (v.getId() == R.id.bigBodyView) {
			WheelUtil.startWheelActivity(5, WheelUtil.WHEEL_NUMBER_HEIGHT, this);
		} else if (v.getId() == R.id.smallWeightView) {
			WheelUtil.startWheelActivity(6, WheelUtil.WHEEL_NUMBER_WEIGHT, this);
		} else if (v.getId() == R.id.bigWeightView) {
			WheelUtil.startWheelActivity(7, WheelUtil.WHEEL_NUMBER_WEIGHT, this);
		} else if (v.getId() == R.id.smallXWView) {
			WheelUtil.startWheelActivity(8, WheelUtil.WHEEL_NUMBER_BREAST, this);
		} else if (v.getId() == R.id.bigXWView) {
			WheelUtil.startWheelActivity(9, WheelUtil.WHEEL_NUMBER_BREAST, this);
		} else if (v.getId() == R.id.bigYWView) {
			WheelUtil.startWheelActivity(10, WheelUtil.WHEEL_NUMBER_WAIST, this);
		} else if (v.getId() == R.id.smallYWView) {
			WheelUtil.startWheelActivity(11, WheelUtil.WHEEL_NUMBER_WAIST, this);
		} else if (v.getId() == R.id.smallTWView) {
			WheelUtil.startWheelActivity(12, WheelUtil.WHEEL_NUMBER_HIP, this);
		} else if (v.getId() == R.id.bigTWView) {
			WheelUtil.startWheelActivity(13, WheelUtil.WHEEL_NUMBER_HIP, this);
		} else if (v.getId() == R.id.smallZBView) {
			WheelUtil.startWheelActivity(14, WheelUtil.WHEEL_NUMBER_CUP, this);
		} else if (v.getId() == R.id.bigZBView) {
			WheelUtil.startWheelActivity(15, WheelUtil.WHEEL_NUMBER_CUP, this);
		} else if (v.getId() == R.id.smallXMView) {
			WheelUtil.startWheelActivity(16, WheelUtil.WHEEL_NUMBER_SHOSE, this);
		} else if (v.getId() == R.id.bigXMView) {
			WheelUtil.startWheelActivity(17, WheelUtil.WHEEL_NUMBER_SHOSE, this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			if ("男".equals(s)) {
				modelBean.setSexName(s);
				modelBean.setSexId(Constants.SEX_MAN);
			} else if ("女".equals(s)) {
				modelBean.setSexName(s);
				modelBean.setSexId(Constants.SEX_WOMAN);
			}
			updateAllViews();
			break;
		}
		case 1: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setNeedNum(s);
			updateAllViews();
			break;
		}
		case 2: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallAge(s);
			updateAllViews();
			break;
		}
		case 3: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigAge(s);
			updateAllViews();
			break;
		}
		case 4: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallBodyHight(s);
			updateAllViews();
			break;
		}
		case 5: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigBodyHight(s);
			updateAllViews();
			break;
		}
		case 6: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallWeight(s);
			updateAllViews();
			break;
		}
		case 7: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigWeight(s);
			updateAllViews();
			break;
		}
		case 8: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallXiongWei(s);
			updateAllViews();
			break;
		}
		case 9: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigXiongWei(s);
			updateAllViews();
			break;
		}
		case 10: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigYaoWei(s);
			updateAllViews();
			break;
		}
		case 11: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallYaoWei(s);
			updateAllViews();
			break;
		}
		case 12: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallTunWei(s);
			updateAllViews();
			break;
		}
		case 13: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigTunWei(s);
			updateAllViews();
			break;
		}
		case 14: {
			String[] scope = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallCupName(s);
			for(int i=0;i<scope.length;i++){
				if(s.equals(scope[i])){
					modelBean.setSamllCupId(String.valueOf(i+1));
				}
			}
			updateAllViews();
			break;
		}
		case 15: {
			String[] scope = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigCupName(s);
			for(int i=0;i<scope.length;i++){
				if(s.equals(scope[i])){
					modelBean.setBigCupId(String.valueOf(i+1));
				}
			}
			updateAllViews();
			break;
		}
		case 16: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setSmallShoese(s);
			updateAllViews();
			break;
		}
		case 17: {
			String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
			modelBean.setBigShoese(s);
			updateAllViews();
			break;
		}
		case 18:{
			areaBean= (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
			updateAreaText();
			break;
		}
		case 19:{
			CountryBean countryBean=(CountryBean)data.getSerializableExtra(Constants.COMEBACK);
			modelBean.setCountryId(countryBean.getcCode());
			modelBean.setCountryName(countryBean.getcName());
			updateAllViews();
			break;
		}
		default:
			break;
		}
	}
	
	private void updateAreaText(){
		if(areaBean.getProvinceName()!=null){
			modelBean.setProvinceId(areaBean.getProvinceId());
			modelBean.setProvinceName(areaBean.getProvinceName());
			modelBean.setCityId(areaBean.getCityId());
			modelBean.setCityName(areaBean.getCityName());
			updateAllViews();
		}
	}


	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private boolean isTure() {
		if(isNew){
			index=index+1;
			modelBean.set_id(String.valueOf(index));
		}
		String tipDesc=requestModelTip.getText().toString().trim();
		if(!"".equals(tipDesc)){
			modelBean.setBeizhuStr(tipDesc);
		}
		if("".equals(sexText.getText().toString())){
			Toast.makeText(getApplicationContext(), "请选择模特性别", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(modelNumsText.getText().toString())){
			Toast.makeText(getApplicationContext(), "请选择招募人数", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(priceText.getText().toString().trim())){
			Toast.makeText(getApplicationContext(), "请填写招募金额", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (Float.parseFloat(priceText.getText().toString().trim())<500){
			Toast.makeText(getApplicationContext(), "招募金额每人至少500元", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (modelBean.getProvinceName()==null){
			Toast.makeText(getApplicationContext(), "请选择所在地", Toast.LENGTH_SHORT).show();
			return false;
		}

		if(!"".equals(ageSmallValue.getText().toString())||!"".equals(ageBigValue.getText().toString())){
			if("".equals(ageSmallValue.getText().toString())||"".equals(ageBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善年龄信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSmallAge())>Integer.parseInt(modelBean.getBigAge())){
					Toast.makeText(getApplicationContext(), "最小年龄不能大于最大年龄", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(bodySmallValue.getText().toString())||!"".equals(bodyBigValue.getText().toString())){
			if("".equals(bodySmallValue.getText().toString())||"".equals(bodyBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善身高信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSmallBodyHight())>Integer.parseInt(modelBean.getBigBodyHight())){
					Toast.makeText(getApplicationContext(), "最小身高不能大于最大身高", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(WeightSmallValue.getText().toString())||!"".equals(WeightBigValue.getText().toString())){
			if("".equals(WeightSmallValue.getText().toString())||"".equals(WeightBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善体重信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSmallWeight())>Integer.parseInt(modelBean.getBigWeight())){
					Toast.makeText(getApplicationContext(), "最小体重不能大于最大体重", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(XWSmallValue.getText().toString())||!"".equals(XWBigValue.getText().toString())){
			if("".equals(XWSmallValue.getText().toString())||"".equals(XWBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善胸围信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSmallXiongWei())>Integer.parseInt(modelBean.getBigXiongWei())){
					Toast.makeText(getApplicationContext(), "最小胸围不能大于最大胸围", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(YWSmallValue.getText().toString())||!"".equals(YWBigValue.getText().toString())){
			if("".equals(YWSmallValue.getText().toString())||"".equals(YWBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善腰围信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSmallYaoWei())>Integer.parseInt(modelBean.getBigYaoWei())){
					Toast.makeText(getApplicationContext(), "最小腰围不能大于最大腰围", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(TWSmallValue.getText().toString())||!"".equals(TWBigValue.getText().toString())){
			if("".equals(TWSmallValue.getText().toString())||"".equals(TWBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善臀围信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSmallTunWei())>Integer.parseInt(modelBean.getBigTunWei())){
					Toast.makeText(getApplicationContext(), "最小臀围不能大于最大臀围", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(ZBSmallValue.getText().toString())||!"".equals(ZBBigValue.getText().toString())){
			if("".equals(ZBSmallValue.getText().toString())||"".equals(ZBBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善罩杯信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSamllCupId())>Integer.parseInt(modelBean.getBigCupId())){
					Toast.makeText(getApplicationContext(), "最小罩杯不能大于最大罩杯", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		if(!"".equals(XMSmallValue.getText().toString())||!"".equals(XMBigValue.getText().toString())){
			if("".equals(XMSmallValue.getText().toString())||"".equals(XMBigValue.getText().toString())){
				Toast.makeText(getApplicationContext(), "请完善鞋码信息", Toast.LENGTH_SHORT).show();
				return false;
			}else{
				if(Integer.parseInt(modelBean.getSamllCupId())>Integer.parseInt(modelBean.getBigCupId())){
					Toast.makeText(getApplicationContext(), "最小鞋码不能大于最大鞋码", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		
		
		return true;
	}

	/**
	 * 重载方法
	 */
	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		switch (view.getId()) {
		case R.id.isClf:
			isClf.setChecked(isChecked);
			modelBean.setBaoXiaoCaiLv(isChecked);
			break;
		case R.id.isZsf:
			isZsf.setChecked(isChecked);
			modelBean.setBaoXiaoZhuSu(isChecked);
			break;
		default:
			break;
		}
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
			} else {
				cityName = BaiduPreferencesUtil.getCity(this);
				getCodeAction();
				updateAreaText();
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

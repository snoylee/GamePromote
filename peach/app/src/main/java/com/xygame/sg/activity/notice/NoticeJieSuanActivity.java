/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.JieSuanInfoBean;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class NoticeJieSuanActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName,userName,zhaomuTxt,signTime,moteTip,daiPrice,dongjieMoney,jieSuanMoney,needPayMoney,gaiPrice;
	private View backButton,changeJieSuanView,comfirm;
	private NoticeStatusBean nsBean;
	private CircularImage userImage;
	private ImageLoader imageLoader;
	private boolean isPaySuccess = false;
	private String liuYan;
	private float dongjieTotalPrice = 0, jieSuanTotalPrice = 0, finalTotalPrice = 0;
	private JieSuanInfoBean jsInfoBean;
	private String payPassword;
	private String noticeId;
	public String getnoticeId(){
		return noticeId;
	}
	public String getpayPassword(){
		return payPassword;
	}
	public String getmemId(){
		return nsBean.getMemId();
	}
	
	public float getNeedPayPrice(){
		return dongjieTotalPrice;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.notice_jiesuan_layout, null));
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
		changeJieSuanView.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		gaiPrice=(TextView)findViewById(R.id.gaiPrice);
		backButton = findViewById(R.id.backButton);
		changeJieSuanView=findViewById(R.id.changeJieSuanView);
		userImage=(CircularImage)findViewById(R.id.userImage);
		userName=(TextView)findViewById(R.id.userName);
		zhaomuTxt=(TextView)findViewById(R.id.zhaomuTxt);
		signTime=(TextView)findViewById(R.id.signTime);
		moteTip=(TextView)findViewById(R.id.moteTip);
		daiPrice=(TextView)findViewById(R.id.daiPrice);
		dongjieMoney=(TextView)findViewById(R.id.dongjieMoney);
		jieSuanMoney=(TextView)findViewById(R.id.jieSuanMoney);
		needPayMoney=(TextView)findViewById(R.id.needPayMoney);
		comfirm=findViewById(R.id.comfirm);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		titleName.setText("结算付款");
		noticeId=getIntent().getStringExtra("noticeId");
		nsBean=(NoticeStatusBean) getIntent().getSerializableExtra("bean");
		updateAllViews();
		toPayDatas();
	}

	private void updateAllViews() {
		// TODO Auto-generated method stub
		userName.setText(nsBean.getUserNick());
		int j = Integer.parseInt(nsBean.getOrders());
		String[] numArray = Constants.CHARACTE_NUMS;
		if (j > numArray.length - 1) {
			zhaomuTxt.setText("招募 " + (j + 1));
		} else {
			zhaomuTxt.setText("招募".concat(numArray[j]));
		}
		if(!"null".equals(nsBean.getEndTime())){
			signTime.setText(
					"对方确认拍摄完成，还剩".concat(CalendarUtils.getLeftTime(Long.parseLong(nsBean.getEndTime()),0).concat("自动结算")));
		}else{
			signTime.setVisibility(View.GONE);
		}
		imageLoader.loadImage(nsBean.getUserIcon(), userImage, false);
		dongjieTotalPrice=Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(nsBean.getReward()))));
		jieSuanTotalPrice=dongjieTotalPrice;
		updatePriceViews();
	}
	
	private void updatePriceViews() {
		// TODO Auto-generated method stub
		finalTotalPrice=jieSuanTotalPrice-dongjieTotalPrice;
		daiPrice.setText("￥".concat(String.valueOf(dongjieTotalPrice)));
		dongjieMoney.setText("￥".concat(String.valueOf(dongjieTotalPrice)));
		jieSuanMoney.setText("-￥".concat(String.valueOf(jieSuanTotalPrice)));
		needPayMoney.setText("￥".concat(String.valueOf(finalTotalPrice)));
	}

	private void toPayDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.ToPayTask(${toPay})", this, null, visit).run();
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if(v.getId()==R.id.changeJieSuanView){
			Intent intent=new Intent(this, NoticeJieSuanChangeActivity.class);
			intent.putExtra("daiMoney", String.valueOf(StringUtil.getPrice(Long.parseLong(nsBean.getReward()))));
			startActivityForResult(intent, 0);
		}else if(v.getId()==R.id.comfirm){
			if(finalTotalPrice>0){
				Intent intent=new Intent(this, NoticePaymentZhuiJiaActivity.class);
				intent.putExtra("bean", jsInfoBean);
				intent.putExtra("dongjieTotalPrice", dongjieTotalPrice);
				intent.putExtra("jieSuanTotalPrice", jieSuanTotalPrice);
				intent.putExtra("finalTotalPrice", finalTotalPrice);
				intent.putExtra("noticeId", noticeId);
				intent.putExtra("memId", getmemId());
				startActivityForResult(intent, 1);
			}else{
				if(UserPreferencesUtil.havePayPassword(this)){
					Intent intent=new Intent(this, InputPasswordDialogActivity.class);
					intent.putExtra("price",String.valueOf(jieSuanTotalPrice));
					intent.putExtra("fromTo","向".concat(nsBean.getUserNick()).concat("付款"));
					startActivityForResult(intent, 2);
				}else{
					Toast.makeText(getApplicationContext(), "请先设置您的支付密码", Toast.LENGTH_SHORT).show();
				}
			}
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
			String shortStr=data.getStringExtra(Constants.COMEBACK);
			if(shortStr!=null){
				jieSuanTotalPrice=Float.parseFloat(shortStr);
				gaiPrice.setText("-￥".concat(String.valueOf(Float.parseFloat(shortStr))));
				updatePriceViews();
			}
			
			break;
		}
		case 1:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			isPaySuccess=data.getBooleanExtra(Constants.COMEBACK, false);
			if(isPaySuccess){
				Intent intent = new Intent("com.sg.jiesuan.pay.scuess.action");
				intent.putExtra(Constants.COMEBACK, isPaySuccess);
				intent.putExtra("userId", nsBean.getUserId());
				sendBroadcast(intent);
				finish();
			}
			break;
		case 2: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			payPassword=data.getStringExtra(Constants.COMEBACK);
			if(payPassword!=null){
				commitPay();
			}
			
			break;
		}
		default:
			break;
		}

	}

	public void getToPayInfo(Map map) {
		// TODO Auto-generated method stub
		liuYan=map.get("message").toString();
		if(!"null".equals(liuYan)){
			moteTip.setText(liuYan);
		}else{
			moteTip.setText("暂无");
		}
		jsInfoBean=new JieSuanInfoBean();
		jsInfoBean.setAvailAmount(map.get("availAmount").toString());
		jsInfoBean.setFrozenAmount(map.get("frozenAmount").toString());
		jsInfoBean.setFrozenCount(map.get("frozenCount").toString());
		jsInfoBean.setIncomeAmount(map.get("incomeAmount").toString());
		jsInfoBean.setMessage(map.get("message").toString());
		jsInfoBean.setOldNeedPayAmount(map.get("oldNeedPayAmount").toString());
		jsInfoBean.setParpayAmount(map.get("parpayAmount").toString());
		jsInfoBean.setShootCount(map.get("shootCount").toString());
		jsInfoBean.setWalletAmount(map.get("walletAmount").toString());
	}
	
	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.JieSuanPayTask(${noticePayToModel})", this, null, visit).run();
	}
	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void getPayInfo(Map map) {
		String status = map.get("status").toString();
		switch (Integer.parseInt(status)) {
		case 1:
			isPaySuccess = true;
			showDilog("恭喜您，支付成功");
			break;
		case 3:
			isPaySuccess = false;
			showDilog1("交易出错,请重试");
			break;
		case 4:
			isPaySuccess = false;
			showDilog1("支付密码错误");
			break;
		default:
			break;
		}
	}

	private void showDilog(String msg) {
		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
				Intent intent = new Intent("com.sg.jiesuan.pay.scuess.action");
				intent.putExtra(Constants.COMEBACK, isPaySuccess);
				intent.putExtra("userId", nsBean.getUserId());
				sendBroadcast(intent);
				finish();
			}
		});
		dialog.show();
	}

	private void showDilog1(String msg) {
		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}

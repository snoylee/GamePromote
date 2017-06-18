/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.activity.notice.bean.PaymentResponseBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.notice.bean.QianBaoBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class NoticePaymentActivity extends SGBasePaymentActivity implements OnClickListener {

	private TextView titleName, cameraTheme, cameraDate, modelNum, totalPriceText, avalidPrice, userQianBaoPrice,
			youHuiQuanPrice, showYufuPrice, showKeyongPrice, showComposePrice, finalPayPrice, countTip;
	private View backButton, youHuiView, qianBaoView, comfirmPay;
	private PlushNoticeBean pnBean;
	private LinearLayout modelRequestView, addPayWayView;
	private ImageView yueIcon;
	private boolean isSelectQianBao = false,isUserYouhui=false,isPaySuccess=false;
	private List<PayWaysBean> datas;
	private QianBaoBean qBean;
	private PayWaysBean payWayBean;
	private ComposeBean compBean;
	private String strFlag,compId,youHuiCount="0";
	private String payPassword;

	public String getPayPassword() {
		return payPassword;
	}
	public PlushNoticeBean getPnBean() {
		return pnBean;
	}

	public boolean isSelectQianBao() {
		return isSelectQianBao;
	}

	private float yufuTotalPrice = 0, keyunTotalPrice = 0, youhuiquanTotalPrice = 0, finalTotalPrice = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_notice_payment_layout, null));

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
		youHuiView.setOnClickListener(this);
		qianBaoView.setOnClickListener(this);
		comfirmPay.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		countTip = (TextView) findViewById(R.id.countTip);
		titleName = (TextView) findViewById(R.id.titleName);
		finalPayPrice = (TextView) findViewById(R.id.finalPayPrice);
		showComposePrice = (TextView) findViewById(R.id.showComposePrice);
		showKeyongPrice = (TextView) findViewById(R.id.showKeyongPrice);
		showYufuPrice = (TextView) findViewById(R.id.showYufuPrice);
		youHuiQuanPrice = (TextView) findViewById(R.id.youHuiQuanPrice);
		yueIcon = (ImageView) findViewById(R.id.yueIcon);
		userQianBaoPrice = (TextView) findViewById(R.id.userQianBaoPrice);
		addPayWayView = (LinearLayout) findViewById(R.id.addPayWayView);
		cameraTheme = (TextView) findViewById(R.id.cameraTheme);
		avalidPrice = (TextView) findViewById(R.id.avalidPrice);
		modelNum = (TextView) findViewById(R.id.modelNum);
		cameraDate = (TextView) findViewById(R.id.cameraDate);
		totalPriceText = (TextView) findViewById(R.id.totalPrice);
		modelRequestView = (LinearLayout) findViewById(R.id.modelRequestView);
		backButton = findViewById(R.id.backButton);
		youHuiView = findViewById(R.id.youHuiView);
		qianBaoView = findViewById(R.id.qianBaoView);
		comfirmPay = findViewById(R.id.comfirmPay);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		yueIcon.setImageResource(R.drawable.gou_null);
		strFlag=getIntent().getStringExtra("strFlag");
		pnBean = (PlushNoticeBean) getIntent().getSerializableExtra("bean");
		titleName.setText("确认订单");
		initFirstViews();
		initFourViews();
		loadPayTypes();
		loadUserCount();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void loadUserCompose() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadComposeCountTask(${getAllCouponsCount})", this, null, visit).run();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void loadUserCount() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadUserCountTask(${userMoney})", this, null, visit).run();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void loadPayTypes() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadPayTypesTask(${getPayTypes})", this, null, visit).run();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void initFirstViews() {
		// TODO Auto-generated method stub
		int manCount = 0, womanCount = 0, totalPersonCount = 0;
		cameraTheme.setText(pnBean.getCameraTheme());
		cameraDate.setText(CalendarUtils.getXieGongDateDis(Long.parseLong(pnBean.getStarTime())).concat("—")
				.concat(CalendarUtils.getXieGongDateDis(Long.parseLong(pnBean.getEndTime()))));
		List<ModelRequestBean> modelDatas = pnBean.getModelBeans();
		for (int i = 0; i < modelDatas.size(); i++) {
			ModelRequestBean item = modelDatas.get(i);
			View pView = getRequestView();
			ImageView tagIcon = (ImageView) pView.findViewById(R.id.tagIcon);
			TextView zhaomuTxt = (TextView) pView.findViewById(R.id.zhaomuTxt);
			TextView tagText = (TextView) pView.findViewById(R.id.tagText);
			TextView priceValue = (TextView) pView.findViewById(R.id.priceValue);
			TextView numText = (TextView) pView.findViewById(R.id.numText);
			zhaomuTxt.setVisibility(View.GONE);
			String pts=item.getNeedPrice();
			String priceText="";
			if("fromNoticeManagment".equals(strFlag)){
				if (pts.contains(".")){
					priceText=pts;
				}else{
					priceText=String.valueOf(StringUtil.getPrice(Long.parseLong(pts)));
				}
			}else{
				priceText=pts;
			}
			if ("男".equals(item.getSexName())) {
				tagIcon.setImageResource(R.drawable.sg_pl_man);
				tagText.setText("男");
				
				manCount = manCount + Integer.parseInt(item.getNeedNum());
				yufuTotalPrice = yufuTotalPrice
						+ (Float.parseFloat(priceText) * Integer.parseInt(item.getNeedNum()));
			} else if ("女".equals(item.getSexName())) {
				tagIcon.setImageResource(R.drawable.sg_pl_woman);
				tagText.setText("女");
				womanCount = womanCount + Integer.parseInt(item.getNeedNum());
				yufuTotalPrice = yufuTotalPrice
						+ (Float.parseFloat(priceText) * Integer.parseInt(item.getNeedNum()));
			}
			priceValue.setText("￥".concat(priceText));
			numText.setText(item.getNeedNum().concat("人"));
			modelRequestView.addView(pView);
		}
		totalPersonCount = manCount + womanCount;
		modelNum.setText(String.valueOf(totalPersonCount));
		totalPriceText.setText("￥".concat(String.valueOf(yufuTotalPrice)));
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void initThirdViews() {
		// TODO Auto-generated method stub
		addPayWayView.removeAllViews();
		for (int i = 0; i < datas.size(); i++) {
			PayWaysBean item = datas.get(i);
			View pView = getPayWayView();
			ImageView payWayIcon = (ImageView) pView.findViewById(R.id.payWayIcon);
			TextView payWayName = (TextView) pView.findViewById(R.id.payWayName);
			ImageView payWaySelect = (ImageView) pView.findViewById(R.id.payWaySelect);
			View lineView=pView.findViewById(R.id.lineView);
			if ("1".equals(item.getIconId())) {
				payWayIcon.setImageResource(R.drawable.zhifubao_pay);
			} else if ("2".equals(item.getIconId())) {
				payWayIcon.setImageResource(R.drawable.weixin_pay);
			} else if ("3".equals(item.getIconId())) {
				payWayIcon.setImageResource(R.drawable.union_pay);
			}
			payWayName.setText(item.getWayName());
			if (item.isSelect()) {
				payWaySelect.setImageResource(R.drawable.gou);
			} else {
				payWaySelect.setImageResource(R.drawable.gou_null);
			}
			pView.setOnClickListener(new ChoicePayWayListener(i));
			if (i==datas.size()-1){
				lineView.setVisibility(View.GONE);
			}
			addPayWayView.addView(pView);
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void initFourViews() {
		// TODO Auto-generated method stub
		finalTotalPrice = 0;
		finalTotalPrice = yufuTotalPrice - keyunTotalPrice - youhuiquanTotalPrice;

		if (keyunTotalPrice != 0.0 && youhuiquanTotalPrice == 0.0) {
			countTip.setText("预付金额".concat("￥".concat(String.valueOf(yufuTotalPrice))).concat("—可用余额")
					.concat("￥".concat(String.valueOf(keyunTotalPrice))));
		} else if (keyunTotalPrice == 0.0 && youhuiquanTotalPrice != 0.0) {
			countTip.setText("预付金额".concat("￥".concat(String.valueOf(yufuTotalPrice))).concat("—优惠券")
					.concat("￥".concat(String.valueOf(youhuiquanTotalPrice))));
		} else if (keyunTotalPrice != 0.0 && youhuiquanTotalPrice != 0.0) {
			countTip.setText("预付金额".concat("￥".concat(String.valueOf(yufuTotalPrice))).concat("—可用余额")
					.concat("￥".concat(String.valueOf(keyunTotalPrice))).concat("—优惠券")
					.concat("￥".concat(String.valueOf(youhuiquanTotalPrice))));
		} else if (keyunTotalPrice == 0.0 && youhuiquanTotalPrice == 0.0) {
			countTip.setText("预付金额".concat("￥".concat(String.valueOf(yufuTotalPrice))));
		}
		if (finalTotalPrice>0){
			BigDecimal   b   =   new BigDecimal(finalTotalPrice);
			finalTotalPrice= b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
			finalPayPrice.setText("￥".concat(String.valueOf(finalTotalPrice)));
		}else{
			finalTotalPrice = 0;
			finalPayPrice.setText("￥".concat(String.valueOf(0)));
		}
	}

	private void isChoiceQianBao() {
		if (isSelectQianBao) {
			yueIcon.setImageResource(R.drawable.gou);
			userQianBaoPrice.setText(String.valueOf(yufuTotalPrice));
			keyunTotalPrice=Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(qBean.getAmount()))));
			if (keyunTotalPrice>=yufuTotalPrice){
				youhuiquanTotalPrice=0;
			}
		} else {
			yueIcon.setImageResource(R.drawable.gou_null);
			userQianBaoPrice.setText("");
			keyunTotalPrice = 0;
		}
		initFourViews();
	}

	public View getRequestView() {
		return LayoutInflater.from(this).inflate(R.layout.sg_payment_model_request_item, null);
	}

	public View getPayWayView() {
		return LayoutInflater.from(this).inflate(R.layout.sg_payment_method_item, null);
	}

	@Override
	public void finish() {
		if("fromNoticeManagment".equals(strFlag)){
			boolean flag=getIntent().getBooleanExtra("payforchat",false);
			if (flag){
				Intent intent = new Intent(Constants.BROADCAST_ACTION_PAYMENT_FOR_CHAT);
				sendBroadcast(intent);
			}else{
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, isPaySuccess);
				setResult(Activity.RESULT_OK, intent);
			}
		}else{
			Intent intent=new Intent(this, NoticeManagmentActivity.class);
			startActivity(intent);
		}
		super.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.youHuiView) {
			String isClick=youHuiQuanPrice.getText().toString();
			if(!"0".equals(isClick)){
				payWayBean=getPayType();
				Intent intent = new Intent(this, NoticeComposeActivity.class);
				intent.putExtra("payWayBean", payWayBean);
				intent.putExtra("pnBean", pnBean);
				startActivityForResult(intent, 0);
			}
		} else if (v.getId() == R.id.qianBaoView) {
			isSelectQianBao = !isSelectQianBao;
			isChoiceQianBao();
			if (keyunTotalPrice>=yufuTotalPrice){
				isUserYouhui=false;
				setPayMothodDefault();
				compBean=null;
				compId=null;
				if("0".equals(youHuiCount)){
					youHuiQuanPrice.setText("当前没有可用优惠券");
					youHuiQuanPrice.setTextColor(getResources().getColor(R.color.dark_gray));
				}else{
					youHuiQuanPrice.setText("当前有".concat(youHuiCount).concat("张可用优惠券"));
					youHuiQuanPrice.setTextColor(getResources().getColor(R.color.red));
				}
			}
		} else if (v.getId() == R.id.comfirmPay) {
			if (finalTotalPrice>0){
				if (getPayWay()==null){
					Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
				}else{
					commitPay();
				}
			}else{
				if (UserPreferencesUtil.havePayPassword(this)){
					Intent intent = new Intent(this, InputPasswordDialogActivity.class);
					intent.putExtra("price",String.valueOf(yufuTotalPrice));
					intent.putExtra("fromTo","付款金额");
					startActivityForResult(intent, 1);
				} else {
					Toast.makeText(getApplicationContext(), "请先设置您的支付密码", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void setPayMothodDefault(){
		for (int i=0;i<datas.size();i++){
			datas.get(i).setSelect(false);
		}
		initThirdViews();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.CommitPayTask(${noticePay})", this, null, visit).run();
	}

	private PayWaysBean getPayWay() {
		PayWaysBean item = null;
		if (datas!=null){
			for (PayWaysBean it : datas) {
				if (it.isSelect()) {
					item = it;
				}
			}
		}
		return item;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			compBean=(ComposeBean) data.getSerializableExtra(Constants.COMEBACK);
			if(compBean!=null){
				isUserYouhui=true;
				youHuiQuanPrice.setText(compBean.getAmount());
				youHuiQuanPrice.setTextColor(getResources().getColor(R.color.red));
				youhuiquanTotalPrice=Float.parseFloat(compBean.getAmount());
				if (youhuiquanTotalPrice>=yufuTotalPrice){
					isUserYouhui=true;
					setPayMothodDefault();
					isSelectQianBao = false;
					isChoiceQianBao();
				}
				initFourViews();
			}
			
			break;
		}
			case 1: {
				if (Activity.RESULT_OK != resultCode || null == data) {
					return;
				}
				payPassword = data.getStringExtra(Constants.COMEBACK);
				if (payPassword != null) {
					commitPay();
				}

				break;
			}
		default:
			showPayResult(data);
			break;
		}

	}

	public void finishLoadPayTypesFaith() {
		loadUserCompose();
	}

	private class ChoicePayWayListener implements OnClickListener {
		private int index;

		/**
		 * <默认构造函数>
		 */
		public ChoicePayWayListener(int index) {
			// TODO Auto-generated constructor stub
			this.index = index;
		}

		/**
		 * 重载方法
		 * 
		 * @param v
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			updateWayDatas(index);
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param index
	 * @see [类、类#方法、类#成员]
	 */
	public void updateWayDatas(int index) {
		// TODO Auto-generated method stub
		for (int i = 0; i < datas.size(); i++) {
			if (i == index) {
				datas.get(index).setSelect(true);
			} else {
				datas.get(i).setSelect(false);
			}
		}
		keyunTotalPrice=0;
		youhuiquanTotalPrice=0;
		initFourViews();
		useSDKPay();
		initThirdViews();
	}

	private void useSDKPay(){
		isSelectQianBao = false;
		isUserYouhui=false;
		isChoiceQianBao();
		compBean=null;
		compId=null;
		if("0".equals(youHuiCount)){
			youHuiQuanPrice.setText("当前没有可用优惠券");
			youHuiQuanPrice.setTextColor(getResources().getColor(R.color.dark_gray));
		}else{
			youHuiQuanPrice.setText("当前有".concat(youHuiCount).concat("张可用优惠券"));
			youHuiQuanPrice.setTextColor(getResources().getColor(R.color.red));
		}
	}

	public PayWaysBean getPayType() {
		return getPayWay();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param listMap
	 * @see [类、类#方法、类#成员]
	 */
	public void finishLoadPayTypes(List<Map> listMap) {
		// TODO Auto-generated method stub
		datas = new ArrayList<PayWaysBean>();

		for (Map map : listMap) {
			if ("alipay".equals(map.get("source").toString()) && "true".equals(map.get("usedInAndroid").toString())) {
				PayWaysBean item = new PayWaysBean();
				item.setWayName("支付宝支付");
				item.setWayId(map.get("payType").toString());
				item.setIconId("1");
				datas.add(item);

			} else if ("weixin".equals(map.get("source").toString())
					&& "true".equals(map.get("usedInAndroid").toString())) {
				PayWaysBean item1 = new PayWaysBean();
				item1.setWayName("微信支付");
				item1.setWayId(map.get("payType").toString());
				item1.setIconId("2");
				datas.add(item1);
			} else if ("unionpay".equals(map.get("source").toString())
					&& "true".equals(map.get("usedInAndroid").toString())) {
				PayWaysBean item1 = new PayWaysBean();
				item1.setWayName("银联支付");
				item1.setWayId(map.get("payType").toString());
				item1.setIconId("3");
				datas.add(item1);
			}
		}
		if (datas.size()>0){
			datas.get(0).setSelect(true);
			initThirdViews();
		}
		loadUserCompose();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void finishLoadUserCount(Map map) {
		// TODO Auto-generated method stub
		qBean = new QianBaoBean();
		qBean.setAmount(map.get("amount").toString());
		qBean.setAvailableCash(map.get("availableCash").toString());
		qBean.setUserId(map.get("userId").toString());
		if("0".equals(qBean.getAmount())){
			avalidPrice.setText("暂无可用余额");
		}else{
			avalidPrice.setText(String.valueOf(StringUtil.getPrice(Long.parseLong(qBean.getAmount()))));
		}

		isChoiceQianBao();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void getPayInfo(Map map) {
		String status = map.get("status").toString();
		payWayBean = getPayWay();
		switch (Integer.parseInt(status)) {
		case 1:
			isPaySuccess=true;
			showDilog("恭喜您，支付成功");
			break;
		case 2:
			if("1".equals(payWayBean.getIconId())){
				String sdkInfo = map.get("sdkInfo").toString();
				requestAlipay(sdkInfo);
			}else if ("2".equals(payWayBean.getIconId())) {
				Map sdkInfo = (Map) map.get("sdkInfo");
				
				PayReq pr = getWxPayReq(sdkInfo.get("appid").toString(), sdkInfo.get("partnerid").toString(), sdkInfo.get("package").toString(),
						sdkInfo.get("prepayid").toString(), sdkInfo.get("noncestr").toString(), sdkInfo.get("timestamp").toString(),sdkInfo.get("sign").toString());
				// 调用微信支付
				payWx(wxAppId, pr);
			} else if("3".equals(payWayBean.getIconId())){
				String sdkInfo = map.get("sdkInfo").toString();
				requestUnionPay(sdkInfo);
			}
			break;
		case 3:
			showDilog1("交易出错,请重试");
			isPaySuccess=false;
			break;
			case 4:
				showDilog1("用户支付密码为空");
				isPaySuccess=false;
				break;
			case 5:
				showDilog1("支付密码错误");
				isPaySuccess=false;
				break;
		default:
			break;
		}
	}
	
	private void showDilog(String msg){
		OneButtonDialog dialog=new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}
	
	private void showDilog1(String msg){
		OneButtonDialog dialog=new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 重载方法
	 * @param flag
	 * @param tips
	 */
	@Override
	protected void payResult(Boolean flag, String tips) {
		// TODO Auto-generated method stub
		super.payResult(flag, tips);
		if(flag){
			isPaySuccess=true;
			showDilog("恭喜您，支付成功");
		}else{
			isPaySuccess=false;
			showDilog1(tips);
		}
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void finishLoadComposeCount(Map map) {
		// TODO Auto-generated method stub
		youHuiCount=map.get("count").toString();
		if("0".equals(youHuiCount)){
			youHuiQuanPrice.setText("当前没有可用优惠券");
			youHuiQuanPrice.setTextColor(getResources().getColor(R.color.dark_gray));
		}else{
			youHuiQuanPrice.setText("当前有".concat(youHuiCount).concat("张可用优惠券"));
			youHuiQuanPrice.setTextColor(getResources().getColor(R.color.red));
		}
	}

	public String getCouponCode() {
		// TODO Auto-generated method stub
		if(compBean!=null){
			compId=compBean.getCompId();
		}
		return compId;
	}
}

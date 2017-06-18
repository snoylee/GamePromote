/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.JieSuanInfoBean;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.notice.bean.QianBaoBean;
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
public class NoticePaymentZhuiJiaActivity extends SGBasePaymentActivity implements OnClickListener {

	private TextView titleName, avalidPrice, yuFuPrice, yuFuUsedPrice, userQianBaoPrice, dongJiePrice, yiJieSuanPrice,
			haiXuPrice, finalPayPrice;
	private View backButton, qianBaoView, comfirmPay, checkYuFuView;
	private LinearLayout addPayWayView;
	private ImageView yueIcon, yuFuIcon;
	private boolean isSelectQianBao = false, isPaySuccess = false, isKeYunYuFu = false;
	private List<PayWaysBean> datas;
	private PayWaysBean payWayBean;
	private String memId;
	private float dongjieTotalPrice = 0, jieSuanTotalPrice = 0, finalTotalPrice = 0, keYunYufuTotalPrice = 0,
			keyunTotalPrice = 0, finalFinalTotalPrice = 0;
	private JieSuanInfoBean jsInfoBean;

	private String payPassword;
	private String noticeId;

	public String getnoticeId() {
		return noticeId;
	}

	public float getPayPrice() {
		return jieSuanTotalPrice;
	}

	public String getmemId() {
		return memId;
	}

	public String getpayPassword() {
		return payPassword;
	}

	public boolean isUsePerpay() {
		return isKeYunYuFu;
	}

	public boolean isSelectQianBao() {
		return isSelectQianBao;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_notice_payment_formodel_layout, null));

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
		qianBaoView.setOnClickListener(this);
		comfirmPay.setOnClickListener(this);
		checkYuFuView.setOnClickListener(this);
		yuFuIcon.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		yuFuPrice = (TextView) findViewById(R.id.yuFuPrice);
		titleName = (TextView) findViewById(R.id.titleName);
		yuFuUsedPrice = (TextView) findViewById(R.id.yuFuUsedPrice);
		yueIcon = (ImageView) findViewById(R.id.yueIcon);
		yuFuIcon = (ImageView) findViewById(R.id.yuFuIcon);
		userQianBaoPrice = (TextView) findViewById(R.id.userQianBaoPrice);
		addPayWayView = (LinearLayout) findViewById(R.id.addPayWayView);
		avalidPrice = (TextView) findViewById(R.id.avalidPrice);
		dongJiePrice = (TextView) findViewById(R.id.dongJiePrice);
		yiJieSuanPrice = (TextView) findViewById(R.id.yiJieSuanPrice);
		haiXuPrice = (TextView) findViewById(R.id.haiXuPrice);
		finalPayPrice = (TextView) findViewById(R.id.finalPayPrice);
		backButton = findViewById(R.id.backButton);
		qianBaoView = findViewById(R.id.qianBaoView);
		comfirmPay = findViewById(R.id.comfirmPay);
		checkYuFuView = findViewById(R.id.checkYuFuView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		noticeId = getIntent().getStringExtra("noticeId");
		memId = getIntent().getStringExtra("memId");
		dongjieTotalPrice = getIntent().getFloatExtra("dongjieTotalPrice", 0);
		jieSuanTotalPrice = getIntent().getFloatExtra("jieSuanTotalPrice", 0);
		finalTotalPrice = getIntent().getFloatExtra("finalTotalPrice", 0);
		finalFinalTotalPrice = finalTotalPrice;
		jsInfoBean = (JieSuanInfoBean) getIntent().getSerializableExtra("bean");
		keYunYufuTotalPrice = Float
				.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(jsInfoBean.getAvailAmount()))));
		keyunTotalPrice = Float
				.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(jsInfoBean.getWalletAmount()))));

		yueIcon.setImageResource(R.drawable.gou_null);
		yuFuIcon.setImageResource(R.drawable.gou_null);
		titleName.setText("选择支付方式");

		updateAllPriceViews();
		updateFinalPrice();

		loadPayTypes();
	}

	private void updateFinalPrice() {
		finalPayPrice.setText("￥".concat(String.valueOf(finalFinalTotalPrice)));
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void loadPayTypes() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadPayTypesForJieSuanTask(${getPayTypes})", this, null, visit).run();
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
	private void updateAllPriceViews() {
		dongJiePrice.setText("￥".concat(String.valueOf(dongjieTotalPrice)));
		yiJieSuanPrice.setText("-￥".concat(String.valueOf(jieSuanTotalPrice)));
		haiXuPrice.setText("￥".concat(String.valueOf(finalTotalPrice)));
		yuFuPrice.setText("￥".concat(String.valueOf(keYunYufuTotalPrice)));
		avalidPrice.setText("￥".concat(String.valueOf(keyunTotalPrice)));
	}

	public View getPayWayView() {
		return LayoutInflater.from(this).inflate(R.layout.sg_payment_method_item, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.qianBaoView) {
			isSelectQianBao =true;
			isChoiceQianBao();
		} else if (v.getId() == R.id.yuFuIcon) {
			isKeYunYuFu = true;
			isChoiceKeYunYuMoney();
		} else if (v.getId() == R.id.comfirmPay) {
			if (finalFinalTotalPrice == 0) {
				if (UserPreferencesUtil.havePayPassword(this)) {
					Intent intent = new Intent(this, InputPasswordDialogActivity.class);
					startActivityForResult(intent, 1);
				} else {
					Toast.makeText(getApplicationContext(), "请先设置您的支付密码", Toast.LENGTH_SHORT).show();
				}
			} else {
				if (finalFinalTotalPrice > 0 && getPayWay() == null) {
					Toast.makeText(getApplicationContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
				} else {
					commitPay();
				}
			}
		} else if (v.getId() == R.id.checkYuFuView) {
			Intent intent = new Intent(this, YuFuTongJiActivity.class);
			intent.putExtra("bean", jsInfoBean);
			startActivity(intent);
		}
	}

	private void isChoiceQianBao() {
		if (isSelectQianBao) {
			yueIcon.setImageResource(R.drawable.gou);
			isSelectQianBao = true;
			if (keyunTotalPrice >= finalTotalPrice) {
				yuFuIcon.setImageResource(R.drawable.gou_null);
				for (int i = 0; i < datas.size(); i++) {
					datas.get(i).setSelect(false);
				}
				initThirdViews();
				isKeYunYuFu = false;
				yuFuUsedPrice.setText("");
			}
		}

		if (isKeYunYuFu) {
			if (keYunYufuTotalPrice >= finalTotalPrice) {
				finalFinalTotalPrice = 0;
			} else {
				finalFinalTotalPrice = finalTotalPrice - keYunYufuTotalPrice;
			}
			yuFuUsedPrice.setText("-￥".concat(String.valueOf(finalTotalPrice)));
		}

		if (isSelectQianBao) {
			if (isKeYunYuFu) {
				userQianBaoPrice.setText("-￥".concat(String.valueOf(finalFinalTotalPrice)));
				if (keyunTotalPrice >= finalFinalTotalPrice) {
					finalFinalTotalPrice = 0;
				} else {
					finalFinalTotalPrice = finalTotalPrice - keyunTotalPrice;
				}
			} else {
				userQianBaoPrice.setText("-￥".concat(String.valueOf(finalTotalPrice)));
				if (keyunTotalPrice >= finalTotalPrice) {
					finalFinalTotalPrice = 0;
				} else {
					finalFinalTotalPrice = finalTotalPrice - keyunTotalPrice;
				}
			}
		}

		updateFinalPrice();
	}

	private void isChoiceKeYunYuMoney() {
		// TODO Auto-generated method stub
		if (isKeYunYuFu) {
			yuFuIcon.setImageResource(R.drawable.gou);
			isKeYunYuFu = true;
			if (keYunYufuTotalPrice >= finalTotalPrice) {
				yueIcon.setImageResource(R.drawable.gou_null);
				for (int i = 0; i < datas.size(); i++) {
					datas.get(i).setSelect(false);
				}
				initThirdViews();
				isSelectQianBao = false;
				userQianBaoPrice.setText("");
			}
		}

		if (isKeYunYuFu) {
			if (keYunYufuTotalPrice >= finalTotalPrice) {
				finalFinalTotalPrice = 0;
			} else {
				finalFinalTotalPrice = finalTotalPrice - keYunYufuTotalPrice;
			}
			yuFuUsedPrice.setText("-￥".concat(String.valueOf(finalTotalPrice)));
		}

		if (isSelectQianBao) {
			if (isKeYunYuFu) {
				userQianBaoPrice.setText("-￥".concat(String.valueOf(finalFinalTotalPrice)));
				if (keyunTotalPrice >= finalFinalTotalPrice) {
					finalFinalTotalPrice = 0;
				} else {
					finalFinalTotalPrice = finalTotalPrice - keyunTotalPrice;
				}
			} else {
				userQianBaoPrice.setText("-￥".concat(String.valueOf(finalTotalPrice)));
				if (keyunTotalPrice >= finalTotalPrice) {
					finalFinalTotalPrice = 0;
				} else {
					finalFinalTotalPrice = finalTotalPrice - keyunTotalPrice;
				}
			}
		}

		updateFinalPrice();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.JieSuanZhuiJiaPayTask(${noticePayToModel})", this, null, visit).run();
	}

	private PayWaysBean getPayWay() {
		PayWaysBean item = null;
		for (PayWaysBean it : datas) {
			if (it.isSelect()) {
				item = it;
			}
		}
		return item;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
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

//		if (finalFinalTotalPrice > 0) {

			// TODO Auto-generated method stub
			for (int i = 0; i < datas.size(); i++) {
				if (i == index) {
					datas.get(index).setSelect(true);
				} else {
					datas.get(i).setSelect(false);
				}
			}
		yuFuIcon.setImageResource(R.drawable.gou_null);
		yueIcon.setImageResource(R.drawable.gou_null);
		isSelectQianBao = false;
		userQianBaoPrice.setText("");
		isKeYunYuFu = false;
		yuFuUsedPrice.setText("");
			initThirdViews();
//		}
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
		initThirdViews();
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
			isPaySuccess = true;
			showDilog("恭喜您，支付成功");
			break;
		case 2:
			if ("1".equals(payWayBean.getIconId())) {
				String sdkInfo = map.get("sdkInfo").toString();
				requestAlipay(sdkInfo);
			} else if ("2".equals(payWayBean.getIconId())) {
				Map sdkInfo = (Map) map.get("sdkInfo");

				PayReq pr = getWxPayReq(sdkInfo.get("appid").toString(), sdkInfo.get("partnerid").toString(),
						sdkInfo.get("package").toString(), sdkInfo.get("prepayid").toString(),
						sdkInfo.get("noncestr").toString(), sdkInfo.get("timestamp").toString(),
						sdkInfo.get("sign").toString());
				// 调用微信支付
				payWx(wxAppId, pr);
			} else if ("3".equals(payWayBean.getIconId())) {
				String sdkInfo = map.get("sdkInfo").toString();
				requestUnionPay(sdkInfo);
			}
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
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, isPaySuccess);
				setResult(Activity.RESULT_OK, intent);
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

	/**
	 * 重载方法
	 * 
	 * @param flag
	 * @param tips
	 */
	@Override
	protected void payResult(Boolean flag, String tips) {
		// TODO Auto-generated method stub
		super.payResult(flag, tips);
		if (flag) {
			isPaySuccess = true;
			showDilog("恭喜您，支付成功");
		} else {
			isPaySuccess = false;
			showDilog1(tips);
		}
	}
}

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
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.notice.bean.QianBaoBean;
import com.xygame.sg.activity.notice.bean.ZhuiJiaBean;
import com.xygame.sg.activity.personal.ZhuiJiaDialogActivity;
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
public class ZhuiJiaPaymentActivity extends SGBasePaymentActivity implements OnClickListener {

	private TextView titleName, finalPayPrice, countTip, userQianBaoPrice, avalidPrice, totalPrice, zjyfPrice,
			benciLuyunRen, yipaiSheRen, yipaiShePrice, yiLuyunRen, yiLunYunPrice, yuFuPrice;
	private View backButton, qianBaoView, comfirmPay;
	private PlushNoticeBean pnBean;
	private LinearLayout addPayWayView;
	private ImageView yueIcon;
	private boolean isSelectQianBao = false, isPaySuccess = false;
	private List<PayWaysBean> datas;
	private QianBaoBean qBean;
	private PayWaysBean payWayBean;
	private ZhuiJiaBean zjBean;

	public ZhuiJiaBean getZjBean() {
		return zjBean;
	}

	public PlushNoticeBean getPnBean() {
		return pnBean;
	}

	public boolean isSelectQianBao() {
		return isSelectQianBao;
	}

	private float yufuTotalPrice = 0, dongjieTotalPrice = 0, yiJieSuanTotalPrice = 0, xuDongJieTotalPrice = 0,
			finalTotalPrice = 0, finalFinalTotalPrice = 0, keyunTotalPrice = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.zhuijia_layout, null));

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
		totalPrice = (TextView) findViewById(R.id.totalPrice);
		zjyfPrice = (TextView) findViewById(R.id.zjyfPrice);
		benciLuyunRen = (TextView) findViewById(R.id.benciLuyunRen);
		yipaiSheRen = (TextView) findViewById(R.id.yipaiSheRen);
		yueIcon = (ImageView) findViewById(R.id.yueIcon);
		userQianBaoPrice = (TextView) findViewById(R.id.userQianBaoPrice);
		addPayWayView = (LinearLayout) findViewById(R.id.addPayWayView);
		yipaiShePrice = (TextView) findViewById(R.id.yipaiShePrice);
		avalidPrice = (TextView) findViewById(R.id.avalidPrice);
		yiLuyunRen = (TextView) findViewById(R.id.yiLuyunRen);
		yiLunYunPrice = (TextView) findViewById(R.id.yiLunYunPrice);
		yuFuPrice = (TextView) findViewById(R.id.yuFuPrice);
		backButton = findViewById(R.id.backButton);
		qianBaoView = findViewById(R.id.qianBaoView);
		comfirmPay = findViewById(R.id.comfirmPay);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		titleName.setText("追加预付款");
		zjBean = (ZhuiJiaBean) getIntent().getSerializableExtra("bean");
		updateTopViews();
		initFourViews();
		loadPayTypes();
		loadUserCount();
	}

	private void updateTopViews() {
		// TODO Auto-generated method stub
		yufuTotalPrice = Float.valueOf(String.valueOf(StringUtil.getPrice(Long.parseLong(zjBean.getYuFuMoney()))));
		dongjieTotalPrice = Float
				.valueOf(String.valueOf(StringUtil.getPrice(Long.parseLong(zjBean.getDaiDongJieMoney()))));
		yiJieSuanTotalPrice = Float
				.valueOf(String.valueOf(StringUtil.getPrice(Long.parseLong(zjBean.getYiJieSuanMoney()))));
		xuDongJieTotalPrice = Float
				.valueOf(String.valueOf(StringUtil.getPrice(Long.parseLong(zjBean.getXuDongJIeMoney()))));
		finalTotalPrice = xuDongJieTotalPrice - (yufuTotalPrice - dongjieTotalPrice - yiJieSuanTotalPrice);
		totalPrice.setText("￥" + finalTotalPrice);
		yuFuPrice.setText("￥" + yufuTotalPrice);
		benciLuyunRen.setText(zjBean.getBenCiLuYunRen());
		zjyfPrice.setText("-￥" + xuDongJieTotalPrice);
		yipaiSheRen.setText(zjBean.getYiPaiSheRen());
		yipaiShePrice.setText("-￥" + yiJieSuanTotalPrice);
		yiLuyunRen.setText(zjBean.getYiLuYunRen());
		yiLunYunPrice.setText("-￥" + dongjieTotalPrice);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void loadUserCount() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadUserCountForZhuiJIaTask(${userMoney})", this, null, visit).run();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void loadPayTypes() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadPayTypesForZhuiJiaTask(${getPayTypes})", this, null, visit).run();
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
		if (keyunTotalPrice >= finalTotalPrice) {
			finalFinalTotalPrice = 0;
			countTip.setText("钱包余额".concat("￥".concat(String.valueOf(keyunTotalPrice))).concat("—追加金额")
					.concat("￥".concat(String.valueOf(finalTotalPrice))));
		} else {
			finalFinalTotalPrice = finalTotalPrice - keyunTotalPrice;
			countTip.setText("追加金额".concat("￥".concat(String.valueOf(finalTotalPrice))).concat("—钱包余额")
					.concat("￥".concat(String.valueOf(keyunTotalPrice))));
		}

		finalPayPrice.setText("￥".concat(String.valueOf(finalFinalTotalPrice)));
	}

	private void isChoiceQianBao() {
		if (isSelectQianBao) {
			yueIcon.setVisibility(View.VISIBLE);
			yueIcon.setImageResource(R.drawable.gou);
			userQianBaoPrice.setText(String.valueOf(yufuTotalPrice));
			keyunTotalPrice=Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(qBean.getAmount()))));
		} else {
			yueIcon.setVisibility(View.GONE);
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
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, isPaySuccess);
		setResult(Activity.RESULT_OK, intent);
		super.finish();
	}

	private void setPayMothodDefault(){
		for (int i=0;i<datas.size();i++){
			datas.get(i).setSelect(false);
		}
		initThirdViews();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.qianBaoView) {
			isSelectQianBao = !isSelectQianBao;
			isChoiceQianBao();
			if (keyunTotalPrice>=yufuTotalPrice) {
				setPayMothodDefault();
			}
		} else if (v.getId() == R.id.comfirmPay) {
			if (finalFinalTotalPrice > 0) {
				if(getPayWay()==null){
					Toast.makeText(getApplicationContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
				}else{
					commitPay();
				}
			} else {
				if (UserPreferencesUtil.havePayPassword(this)) {
					Intent intent = new Intent(this, ZhuiJiaDialogActivity.class);
					startActivityForResult(intent, 0);
				} else {
					Toast.makeText(getApplicationContext(), "请先设置支付密码", Toast.LENGTH_SHORT).show();
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
			isPaySuccess = data.getBooleanExtra(Constants.COMEBACK, false);
			if (isPaySuccess) {
				finish();
			}
			break;
		}
		default:
			break;
		}

	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.SdkPayTask(${membersUpdate})", this, null, visit).run();
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
		isSelectQianBao=false;
		yueIcon.setImageResource(R.drawable.gou_null);
		yueIcon.setVisibility(View.GONE);
		userQianBaoPrice.setText("");
		keyunTotalPrice = 0;
		initThirdViews();
		initFourViews();

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
		if (datas.size() > 0) {
			datas.get(0).setSelect(true);
		}
		initThirdViews();
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
		if ("0".equals(qBean.getAmount())) {
			avalidPrice.setText("暂无可用余额");
		} else {
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
		// 1:担保交易支付成功或者启用余额支付余额大于等于待支付金额支付成功
		// 2:不启用余额支付或者钱包余额小于待支付金额唤起第三方支付sdk
		// 3:交易出错,请重试
		// 4:支付密码错误
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
			showDilog1("交易出错,请重试");
			isPaySuccess = false;
			break;
		case 4:
			showDilog1("支付密码错误");
			isPaySuccess = false;
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
				finish();
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

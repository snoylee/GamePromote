package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class ChunZhiActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName;
	private View backButton, comfirm;
	private EditText chunzhiMoney;
	private LinearLayout addPayWayView;
	private List<PayWaysBean> datas;
	private PayWaysBean payWayBean;

	
	public String getMoney(){
		return chunzhiMoney.getText().toString().trim(); 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.chunzhi_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		chunzhiMoney = (EditText) findViewById(R.id.chunzhiMoney);
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		addPayWayView = (LinearLayout) findViewById(R.id.addPayWayView);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
		chunzhiMoney.setOnKeyListener(new KeyEventListener());
		chunzhiMoney.setFilters(new InputFilter[]{StringUtils.getLengthFilter()});
	}

	private void initDatas() {
		titleName.setText("充值");
		loadPayTypes();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			if ("".equals(chunzhiMoney.getText().toString().trim())) {
				Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
			} else {
				payWayBean = getPayWay();
				if(payWayBean!=null){
					commitPay();
				}else{
					Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.ChunZhiPayTask(${recharge})", this, null, visit).run();
	}

	private void loadPayTypes() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadQBPayTypesTask(${getPayTypes})", this, null, visit).run();
	}

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

	public View getPayWayView() {
		return LayoutInflater.from(this).inflate(R.layout.sg_payment_method_item, null);
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

		initThirdViews();
	}

	public PayWaysBean getPayType() {
		return getPayWay();
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

	public void getPayInfo(Map map) {
		// TODO Auto-generated method stub
		if("1".equals(map.get("status").toString())){
			payWayBean = getPayWay();
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
		}else{
			Toast.makeText(getApplicationContext(), "交易出错,请重试", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void showDilog(String msg){
		OneButtonDialog dialog=new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, getMoney());
				setResult(Activity.RESULT_OK, intent);
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
			showDilog("恭喜您，支付成功");
		}else{
			showDilog1(tips);
		}
	}
}

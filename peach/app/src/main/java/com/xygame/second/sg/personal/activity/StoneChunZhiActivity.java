package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xygame.second.sg.personal.bean.PayMoneyBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class StoneChunZhiActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName;
	private View backButton, comfirm;
	private TextView chunzhiMoney;
	private LinearLayout addPayWayView;
	private List<PayWaysBean> datas;
	private PayWaysBean payWayBean;
	private PayMoneyBean moneyBean;
	
	public String getMoney(){
		return chunzhiMoney.getText().toString().trim(); 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_stone_chunzhi_layout);
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		chunzhiMoney = (TextView) findViewById(R.id.chunzhiMoney);
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		addPayWayView = (LinearLayout) findViewById(R.id.addPayWayView);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	private void initDatas() {
		moneyBean=(PayMoneyBean)getIntent().getSerializableExtra("bean");
		titleName.setText("充值");
		chunzhiMoney.setText(moneyBean.getMoney());
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
		try {
			RequestBean item = new RequestBean();
			JSONObject obj = new JSONObject();
			String amount=getMoney();
			String payType=getPayType().getWayId();
			Double db=new Double(Double.parseDouble(amount)*100);
			obj.put("amount",String.valueOf(db.intValue()));
			obj.put("payType",payType);
			ShowMsgDialog.showNoMsg(this, false);
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_PAY_MONEY);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_PAY_MONEY);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_PAY_MONEY:
				if ("0000".equals(data.getCode())) {
					getPayInfo(data.getRecord());
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case ConstTaskTag.QUERY_PAY_MONEY_TYPE:
				if ("0000".equals(data.getCode())) {
					finishLoadPayTypes(data.getRecord());
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void loadPayTypes() {
		try {
			RequestBean item = new RequestBean();
			JSONObject obj = new JSONObject();
			item.setData(obj);
			ShowMsgDialog.showNoMsg(this, true);
			item.setServiceURL(ConstTaskTag.QUEST_PAY_MONEY_TYPE);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_PAY_MONEY_TYPE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void finishLoadPayTypes(String listMap) {
		// TODO Auto-generated method stub
		try {
			if (ConstTaskTag.isTrueForArrayObj(listMap)){
				datas = new ArrayList<>();
				JSONArray array=new JSONArray(listMap);
				for (int i=0;i<array.length();i++){
					JSONObject object=array.getJSONObject(i);
					String source=StringUtils.getJsonValue(object, "source");
					String usedInAndroid=StringUtils.getJsonValue(object,"usedInAndroid");
					String payType=StringUtils.getJsonValue(object,"payType");
					if ("alipay".equals(source) && "true".equals(usedInAndroid)) {
						PayWaysBean item = new PayWaysBean();
						item.setWayName("支付宝支付");
						item.setWayId(payType);
						item.setIconId("1");
						datas.add(item);

					} else if ("weixin".equals(source)&& "true".equals(usedInAndroid)) {
						PayWaysBean item1 = new PayWaysBean();
						item1.setWayName("微信支付");
						item1.setWayId(payType);
						item1.setIconId("2");
						datas.add(item1);
					} else if ("unionpay".equals(source)	&& "true".equals(usedInAndroid)) {
						PayWaysBean item1 = new PayWaysBean();
						item1.setWayName("银联支付");
						item1.setWayId(payType);
						item1.setIconId("3");
						datas.add(item1);
					}
				}
				if (datas.size() > 0) {
					datas.get(0).setSelect(true);
				}
				initThirdViews();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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

	public void getPayInfo(String record) {
		try {
			if (!TextUtils.isEmpty(record)){
				JSONObject object=new JSONObject(record);
				payWayBean = getPayWay();
				if("1".equals(payWayBean.getIconId())){
					String sdkInfo = StringUtils.getJsonValue(object,"sdkInfo");
					requestAlipay(sdkInfo);
				}else if ("2".equals(payWayBean.getIconId())) {
					String sdkInfo= StringUtils.getJsonValue(object,"sdkInfo");
					JSONObject object1=new JSONObject(sdkInfo);
					PayReq pr = getWxPayReq(StringUtils.getJsonValue(object1, "appid"), StringUtils.getJsonValue(object1, "partnerid"), StringUtils.getJsonValue(object1, "package"),
							StringUtils.getJsonValue(object1, "prepayid"),  StringUtils.getJsonValue(object1, "noncestr"), StringUtils.getJsonValue(object1, "timestamp"),StringUtils.getJsonValue(object1, "sign"));
					// 调用微信支付
					payWx(wxAppId, pr);
				} else if("3".equals(payWayBean.getIconId())){
					String sdkInfo =  StringUtils.getJsonValue(object, "sdkInfo");
					requestUnionPay(sdkInfo);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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

	private void showDilog(String msg){
		OneButtonDialog dialog=new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		showPayResult(data);
	}
}

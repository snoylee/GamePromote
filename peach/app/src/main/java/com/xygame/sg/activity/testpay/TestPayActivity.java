/*
 * 文 件 名:  TestPayActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月10日
 */
package com.xygame.sg.activity.testpay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月10日
 * @action  [测试支付调用接口界面]
 */
public class TestPayActivity extends SGBasePaymentActivity implements OnClickListener{
	
	private View alipayView,unpayView,wxpayView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_test_pay);
		initViews();
		initListeners();
		initDatas();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		alipayView=findViewById(R.id.alipayView);
		unpayView=findViewById(R.id.unpayView);
		wxpayView=findViewById(R.id.wxpayView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		alipayView.setOnClickListener(this);
		unpayView.setOnClickListener(this);
		wxpayView.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		wxAppRegister();
	}

	/**
	 * 重载方法
	 * @param arg0
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.alipayView){
			requestAlipay("");
		}else if(v.getId()==R.id.unpayView){
			requestUnionPay("");
		}else if(v.getId()==R.id.wxpayView){
//			PayReq pr=getWxPayReq(wxAppId, mJson.getString("partnerid"), mJson.getString("package"), mJson.getString("prepayid"), mJson.getString("noncestr"), mJson.getString("timestamp"), mJson.getString("sign"));
//			//调用微信支付
//			payWx(wxAppId, pr);
		}
	}
	
	
//	private void parsePay(LeResponseBean data) throws JSONException {
//		JSONObject json = data.getSys();
//		String tn ="";
//		if(!json.isNull("sdkOrderInfo")){
//			tn = json.getString("sdkOrderInfo");
//		}
//		
//		String tempValue = payWayMap.get("keySet");
//		if ("appAlipay".equals(tempValue)) {
//			requestAlipay(tn);
//		} else if ("appUnionpay".equals(tempValue)) {
//			requestUnionPay(tn);
//		} else if("appWeixinpay".equals(tempValue)){
//			JSONObject mJson=new JSONObject(json.getString("sdkOrderInfo"));
//			wxAppId=mJson.getString("appid");
//			PayReq pr=getWxPayReq(wxAppId, mJson.getString("partnerid"), mJson.getString("package"), mJson.getString("prepayid"), mJson.getString("noncestr"), mJson.getString("timestamp"), mJson.getString("sign"));
//			//调用微信支付
//			payWx(wxAppId, pr);
//		}
//	}
}

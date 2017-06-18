/*
 * 文 件 名:  SGBasePaymentActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月10日
 */
package com.xygame.sg.activity.base;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.utils.payment.PayResult;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月10日
 * @action  [支付相关界面继承类]
 */
public class SGBasePaymentActivity extends FragmentActivity implements Handler.Callback{
	
	protected String wxAppId;
	/**
	 * 重载方法
	 * @param arg0
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		SGApplication.getInstance().addActivity(this);
		wxAppRegister();
	}

	@Override
	protected void onStart() {
		super.onStart();
//		if (UserPreferencesUtil.isOnline(this)) {
//			XMPPUtils.reConnect(this);
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 注册微信
	 */
	protected void wxAppRegister() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter
				.addAction("com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP");
		myIntentFilter
		.addAction("com.xygame.payment.wx.result");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterWxReceiver() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP"
					.equals(intent.getAction())) {
				final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

				// 将该app注册到微信
				api.registerApp(wxAppId);
			}else if("com.xygame.payment.wx.result".equals(intent.getAction())){
				String tips=intent.getStringExtra("tips");
				boolean flag=intent.getBooleanExtra("flags", false);
				payResult(flag, tips);
			}
		}
	};
	
	/**
	 * 获取微信支付配置参数
	 * @param wxAppId 公众账号ID
	 * @param partnerid 商户号
	 * @param packageValue 扩展字段
	 * @param prepayid 预支付交易会话ID
	 * @param noncestr 随机字符串
	 * @param timestamp 时间戳
	 * @param sign 签名
	 * @return
	 */
	protected PayReq getWxPayReq(String wxAppId,String partnerid,String packageValue,String prepayid,String noncestr,String timestamp,String sign){
		PayReq req= new PayReq();
		req.appId =wxAppId;
		req.partnerId = partnerid;
		req.prepayId = prepayid;
		req.packageValue = packageValue;
		req.nonceStr = noncestr;
		req.timeStamp = timestamp;
		req.sign = sign;
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		return req;
	}

	/**
	 * 调用微信支付
	 * @param wxAppId 微信ID号
	 * @param req 微信请求参数
	 */
	protected void payWx(String wxAppId, PayReq req) {
		IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp(wxAppId);
		msgApi.sendReq(req);
	}

	/**
	 * 调用银联支付
	 * @param tn 服务端请求返回的参数
	 */
	protected void requestUnionPay(String tn) {
		UPPayAssistEx.startPayByJAR(this, PayActivity.class, null, null, tn,
				"00");
	}

	/**
	 * 调用支付宝支付
	 * @param tn 服务端请求返回的参数
	 */
	protected void requestAlipay(final String tn) {
		// TODO Auto-generated method stub
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(SGBasePaymentActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(tn);

				Message msg = new Message();
				msg.what = 0;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 银联支付返回的结果处理 context为上下 data接受传递的参数Intent
	 * @param data
	 */
	public void showPayResult(Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
			payResult(true, msg);
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
			payResult(false, msg);
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
			payResult(false, msg);
		}
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					payResult(true, "支付成功");
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						payResult(false, "支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						payResult(false, "支付失败");
					}
				}
				break;
			}
			default:
				break;
			}
		}
	};
	
	/**
	 * 支付返回结果
	 * @param flag true为支付成功，false为支付失败
	 * @param tips 支付信息提示
	 */
	protected void payResult(Boolean flag,String tips){
		
	}

	@Override
	public void onDestroy() {
		unregisterWxReceiver();
//		System.runFinalization();
//		System.gc();
		super.onDestroy();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case ConstTaskTag.CONST_TAG_PUBLIC: {
				ResponseBean data = (ResponseBean) msg.obj;
				if (null == data) {
					responseFaith();
					Toast.makeText(this, "网络请求失败！", Toast.LENGTH_SHORT).show();
				} else {
					if ("0000".equals(data.getCode())) {
						getResponseBean(data);
					} else {
						Toast.makeText(this,data.getMsg(), Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			default:
				break;
		}
		return false;
	}

	protected void getResponseBean(ResponseBean data) {
	}

	protected void responseFaith() {
	}
}
package com.xygame.sg.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelbase.BaseResp.ErrCode;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xygame.sg.utils.Constants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constants.WeiXin_APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case ErrCode.ERR_AUTH_DENIED://认证被否决
				sendResultBroadcast(false, "认证被否决");
				break;
			case ErrCode.ERR_COMM://一般错误
				sendResultBroadcast(false, "一般错误");
				break;
			case ErrCode.ERR_OK://正确返回
				sendResultBroadcast(true, "支付成功");
				break;
			case ErrCode.ERR_SENT_FAILED://发送失败
				sendResultBroadcast(false, "发送失败");
				break;
			case ErrCode.ERR_UNSUPPORT://不支持错误
				sendResultBroadcast(false, "不支持错误");
				break;
			case ErrCode.ERR_USER_CANCEL://用户取消
				sendResultBroadcast(false, "用户取消");
				break;
			default:
				break;
			}
		}
	}
	
	private void sendResultBroadcast(Boolean flag,String tips){
		Intent intent = new Intent("com.xygame.payment.wx.result");
		intent.putExtra("flags", flag);
		intent.putExtra("tips", tips);
		sendBroadcast(intent);
		finish();
	}
}
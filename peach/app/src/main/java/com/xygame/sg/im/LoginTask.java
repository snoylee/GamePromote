package com.xygame.sg.im;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

/**
 *
 * 登录异步任务.
 *
 */
public class LoginTask extends AsyncTask<String, Integer, Integer> {
	private Context context;
	private String password,nickName;
	public LoginTask(Context context,String password,String nickName) {
		this.password=password;
		this.nickName=nickName;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		return login();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Integer result) {
		Intent intent = new Intent(Constants.XMPP_LOGIN_ACTION);
		context.sendBroadcast(intent);
		super.onPostExecute(result);
	}

	// 登录
	private Integer login() {
		String username = UserPreferencesUtil.getUserId(context);

		try {
			SGApplication.getInstance().openConnection();
			SGApplication.getInstance().getConnection().connect();
			SGApplication.getInstance().getConnection().login(username, password, "sgapp"); // 登录
			OfflineMsgManager.getInstance(context).dealOfflineMsg(SGApplication.getInstance().getConnection());//处理离线消息
			SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.available));
			DeliveryReceiptManager.getInstanceFor(SGApplication.getInstance().getConnection()).enableAutoReceipts();
			return XMPPUtils.LOGIN_SECCESS;
		} catch (Exception xee) {
			if (xee instanceof XMPPException) {
				XMPPException xe = (XMPPException) xee;
				final XMPPError error = xe.getXMPPError();
				int errorCode = 0;
				if (error != null) {
					errorCode = error.getCode();
				}
				if (errorCode == 401) {
					return XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS;
				} else if (errorCode == 403) {
					return XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS;
				} else {
					return XMPPUtils.SERVER_UNAVAILABLE;
				}
			} else {
				if (xee.getMessage().contains("Already logged in to server")){
					return XMPPUtils.LOGIN_SECCESS;
				}else{
					return XMPPUtils.LOGIN_ERROR;
				}
			}
		}
	}
}

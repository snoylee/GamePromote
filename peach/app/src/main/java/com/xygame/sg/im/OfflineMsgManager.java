package com.xygame.sg.im;

import java.util.Iterator;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

/**
 *
 * 离线信息管理类.
 *
 */
public class OfflineMsgManager {
	private static OfflineMsgManager offlineMsgManager = null;
	private Context context;

	private OfflineMsgManager(Context context) {
		this.context = context;
	}

	public static OfflineMsgManager getInstance(Context context) {
		if (offlineMsgManager == null) {
			offlineMsgManager = new OfflineMsgManager(context);
		}

		return offlineMsgManager;
	}

	/**
	 *
	 * 处理离线消息.
	 *
	 * @param connection
	 * @author shimiso
	 * @update 2012-7-9 下午5:45:32
	 */
	public void dealOfflineMsg(XMPPConnection connection) {
		OfflineMessageManager offlineManager = new OfflineMessageManager(
				connection);
		try {
			Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager
					.getMessages();
			Log.i("离线消息数量: ", "" + offlineManager.getMessageCount());
			while (it.hasNext()) {
				org.jivesoftware.smack.packet.Message message = it.next();
				Log.i("收到离线消息", "Received from 【" + message.getFrom()
						+ "】 message: " + message.getBody());
				if (message != null && message.getBody() != null
						&& !message.getBody().equals("null")) {
					String mssageBody = message.getBody();
						SGNewsBean msg = new SGNewsBean();
						JSONObject obj = new JSONObject(mssageBody);
						String ext = obj.getString("ext");
						JSONObject subObj = new JSONObject(ext);
						msg.setFromUser(subObj.getString("fromUser"));
						msg.setToUser(subObj.getString("toUser"));
						msg.setNoticeSubject(subObj.getString("noticeSubject"));
						msg.setRecruitLocIndex(subObj.getString("recruitLocIndex"));
						msg.setMsgContent(obj.getString("msgContent"));
						msg.setNewType(Constants.NEWS_CHAT);
						msg.setNoticeId(obj.getString("noticeId"));
						msg.setRecruitId(obj.getString("recruitId"));
						msg.setTimestamp(obj.getString("timestamp"));
						msg.setType(obj.getString("type"));
						msg.setUserId(UserPreferencesUtil.getUserId(context));
						msg.setMessageStatus(Constants.NEWS_UNREAD);
						msg.setInout(Constants.NEWS_RECIEVE);
						msg.setIsShow(Constants.NEWS_SHOW);
						msg.setMsgStatus(Constants.NEWS_SEND_SCUESS);
						if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(msg, context))) {
							msg.setFriendUserId(SenderUser.getFromUserId(msg, context));
						} else if (!UserPreferencesUtil.getUserId(context)
								.equals(SenderUser.getToUserId(msg, context))) {
							msg.setFriendUserId(SenderUser.getToUserId(msg, context));
						}
						NewsEngine.inserChatNew(context, msg);

				}
			}
			offlineManager.deleteMessages();
		if (UserPreferencesUtil.isOnline(context)) {
			Intent intent = new Intent("cn.com.xygame.sg.loaction.service");
			context.sendBroadcast(intent);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

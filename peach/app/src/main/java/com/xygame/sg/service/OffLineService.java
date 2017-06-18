package com.xygame.sg.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.xygame.sg.R;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONObject;

/**
 *
 * 聊天服务.
 *
 */
public class OffLineService extends Service implements  ChatManagerListener,MessageListener {
	private Context context;
	@Override
	public void onCreate() {
		context = this;
		super.onCreate();
		initChatManager();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initChatManager() {
		ChatManager chatManager = SGApplication.getInstance().getConnection().getChatManager();
		chatManager.addChatListener(this);
	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		// TODO Auto-generated method stub
		chat.addMessageListener(this);
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		if (message != null && message.getBody() != null && !message.getBody().equals("null")) {
			String mssageBody = message.getBody();
			try {
				if (UserPreferencesUtil.isOnline(this)) {
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
					msg.setUserId(UserPreferencesUtil.getUserId(this));
					msg.setMessageStatus(Constants.NEWS_UNREAD);
					msg.setInout(Constants.NEWS_RECIEVE);
					msg.setIsShow(Constants.NEWS_SHOW);
					msg.setMsgStatus(Constants.NEWS_SEND_SCUESS);
					if (!UserPreferencesUtil.getUserId(this).equals(SenderUser.getFromUserId(msg, this))) {
						msg.setFriendUserId(SenderUser.getFromUserId(msg, this));
					} else if (!UserPreferencesUtil.getUserId(this)
							.equals(SenderUser.getToUserId(msg, this))) {
						msg.setFriendUserId(SenderUser.getToUserId(msg, this));
					}
					NewsEngine.inserChatNew(this, msg);
					Intent intent = new Intent("com.xygame.sg.offline.msg.action");
					intent.putExtra("offLineBean",msg);
					sendBroadcast(intent);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
package com.xygame.sg.service;

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

import com.xygame.sg.R;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

/**
 *
 * 聊天服务.
 *
 */
public class IMChatService extends Service {
	private Context context;
	private NotificationManager notificationManager;
	private boolean isNoticeListeser = false;
	@Override
	public void onCreate() {
		context = this;
		super.onCreate();
		registerBoradcastReceiver();
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
		unregisterBroadcastReceiver();
		super.onDestroy();
	}

	private void initChatManager() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		XMPPConnection conn = SGApplication.getInstance().getConnection();
		conn.addPacketListener(pListener, new MessageTypeFilter(
				Message.Type.normal));
	}

	PacketListener pListener = new PacketListener() {

		@Override
		public void processPacket(Packet arg0) {
			Message message = (Message) arg0;
			if (message != null && message.getBody() != null
					&& !message.getBody().equals("null")) {
				String mssageBody=message.getBody();
				try{
					if(UserPreferencesUtil.isOnline(context)){
						String showContent="";
						SGNewsBean msg = new SGNewsBean();
						JSONObject obj=new JSONObject(mssageBody);
						String ext=obj.getString("ext");
						JSONObject subObj=new JSONObject(ext);
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
						if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(msg, context))){
							msg.setFriendUserId(SenderUser.getFromUserId(msg, context));
						}else if(!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(msg, context))){
							msg.setFriendUserId(SenderUser.getToUserId(msg, context));
						}
						NewsEngine.inserChatNew(context, msg);
						if ("1".equals(msg.getType())){
							showContent=msg.getMsgContent();
						}else if ("2".equals(msg.getType())){
							showContent="图片";
						}else if ("3".equals(msg.getType())){
							showContent="语音";
						}else if ("4".equals(msg.getType())){
							showContent="地理位置信息";
						}
						setNotiType(showContent, ChatActivity.class, msg);
						Intent intent = new Intent(XMPPUtils.NEW_MESSAGE_ACTION);
						intent.putExtra("newBean",msg);
						intent.putExtra("newsMessage", true);
						sendBroadcast(intent);
					}

				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	};

	/**
	 *
	 * 发出Notification的method.
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author shimiso
	 * @update 2012-5-14 下午12:01:55
	 */
	private void setNotiType( String contentText, Class activity,SGNewsBean userBean) {

		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		String contentTitle=getResources().getString(R.string.new_message);
		Intent notifyIntent = new Intent(this, activity);
		notifyIntent.putExtra("bean", userBean);
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		/* 创建PendingIntent作为设置递延运行的Activity */
		PendingIntent appIntent = PendingIntent.getActivity(this, 0,
				notifyIntent, 0);

		/* 创建Notication，并设置相关参数 */
		Notification myNoti = new Notification();
		// 点击自动消失
		myNoti.flags |= Notification.FLAG_AUTO_CANCEL;
		/* 设置statusbar显示的icon */
		myNoti.icon = R.drawable.ic_launcher;
		/* 设置statusbar显示的文字信息 */
		myNoti.tickerText = contentTitle;
		/* 设置notification发生时同时发出默认声音 */
		myNoti.defaults = Notification.DEFAULT_SOUND;
		/* 设置Notification留言条的参数 */
		myNoti.setLatestEventInfo(this, contentTitle, contentText, appIntent);
		/* 送出Notification */
		notificationManager.notify(0, myNoti);
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(XMPPUtils.NOTICE_LISTENER_ACTION);
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterBroadcastReceiver() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (XMPPUtils.NOTICE_LISTENER_ACTION.equals(action)) {
				isNoticeListeser = intent.getBooleanExtra("isNot", false);
			}
		}
	};
}
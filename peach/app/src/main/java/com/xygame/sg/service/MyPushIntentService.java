package com.xygame.sg.service;

import org.android.agoo.client.BaseConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.xygame.second.sg.utils.NotificationManagerHelper;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.main.MainFrameActivity;
import com.xygame.sg.pushelper.PushEngine;
import com.xygame.sg.pushelper.PushHeperBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Developer defined push intent service. 
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}. 
 * @author lucas
 *
 */
public class MyPushIntentService extends UmengBaseIntentService{
	private static final String TAG = MyPushIntentService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
		try {
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			String body=new JSONObject(message).getString("body");
			JSONObject object=new JSONObject(body);
			JSONObject mtObj=new JSONObject(new JSONObject(message).getString("extra"));
			String msgType=mtObj.getString("msgType");
			if ("45".equals(msgType)){
				String text="您收到一位大神的抢单";
				String title="抢单";
				setNotiType(title, text, MainFrameActivity.class);
				String custom= StringUtils.getJsonValue(object,"custom");
				Intent noticePage = new Intent();
				noticePage.setAction(XMPPUtils.ACTION_RECONNECT_STATE);
				noticePage.putExtra("custom", custom);
				sendBroadcast(noticePage);
			}else if ("30".equals(msgType)){
				Intent intent2 = new Intent("com.xygame.push.group.message.action");
				intent2.putExtra("body",body);
				sendBroadcast(intent2);
			}else if ("31".equals(msgType)){
				Intent intent2 = new Intent("com.xygame.push.disc.group.kickout.action");
				intent2.putExtra("body",body);
				sendBroadcast(intent2);
			}else{
				String text=object.getString("text");
				String title=object.getString("title");
				setNotiType(title, text, MainFrameActivity.class);
				Intent intent2 = new Intent("com.xygame.push.dynamic.message.action");
				sendBroadcast(intent2);
			}
//			boolean flag= CacheService.getInstance().getCacheUserStatus(ConstTaskTag.CACHE_USER_STATUS);
//			if (flag){
//				if (!"0".equals(msgType)){
//					PushHeperBean bean=new PushHeperBean();
//					bean.setContent(text);
//					bean.setTitle(title);
//					bean.setUserId(UserPreferencesUtil.getUserId(context));
//					PushEngine.inserPushBean(context, bean);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setNotiType(String contentTitle,
			String contentText,Class activity) {

		/*
		 * 创建新的Intent，作为点击Notification留言条时， 会运行的Activity
		 */
		Intent notifyIntent = new Intent(this, activity);
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
		NotificationManagerHelper.getInstance(this).getNotificationManager().notify(0, myNoti);
	}
}

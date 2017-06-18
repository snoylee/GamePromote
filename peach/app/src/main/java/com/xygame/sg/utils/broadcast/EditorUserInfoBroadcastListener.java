/*
 * 文 件 名:  EditorBodyInfoBroadcastListener.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月21日
 */
package com.xygame.sg.utils.broadcast;

import com.xygame.sg.utils.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月21日
 * @action  [监听修改用户信息广播]
 */
public class EditorUserInfoBroadcastListener {
	public static void registerEditorUserInfoListener(Context context,BroadcastReceiver mBroadcastReceiver){
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.ACTION_EDITOR_USER_INFO);
		myIntentFilter.addAction(Constants.ENROLL_SUCCESS);
		myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
		myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
		myIntentFilter.addAction(Constants.ACTION_RECORDER_SUCCESS);
		myIntentFilter.addAction("com.xygame.pinfo.action");
		context.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public static void unRegisterEditorUserInfoListener(Context context,BroadcastReceiver mBroadcastReceiver){
		context.unregisterReceiver(mBroadcastReceiver);
	}
}

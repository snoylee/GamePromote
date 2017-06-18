/*
 * 文 件 名:  UserPreferencesUtil.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [获取到的百度地图信息分享类]
 */
public class AliPreferencesUtil {

	public static void setAccessKey(Context context, String accessKey) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("accessKey", accessKey);
		editor.commit();
	}

	public static String getAccessKey(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		return loginShare.getString("accessKey", null);
	}

	public static void setBuckekName(Context context, String buckekName) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("buckekName", buckekName);
		editor.commit();
	}

	public static String getBuckekName(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		return loginShare.getString("buckekName", null);
	}

	public static void setScrectKey(Context context, String screctKey) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("screctKey", screctKey);
		editor.commit();
	}

	public static String getScrectKey(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		return loginShare.getString("screctKey", null);
	}
	
	public static void setAuthPicKey(Context context, String AuthPicKey) {
		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("AuthPicKey", AuthPicKey);
		editor.commit();
	}

//	public static String getAuthPicKey(Context context) {
//		SharedPreferences loginShare = context.getSharedPreferences("queryAliOSSKey", Context.MODE_MULTI_PROCESS);
//		return loginShare.getString("AuthPicKey", null);
//	}
}

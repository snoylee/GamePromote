package com.xygame.sg.im;

import com.xygame.sg.utils.LoginConfig;
import com.xygame.sg.utils.SGApplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Activity帮助支持类接口.
 * 
 * @author shimiso
 */
public interface IActivitySupport {
	/**
	 * 
	 * 获取EimApplication.
	 * 
	 * @author shimiso
	 * @update 2012-7-6 上午9:05:51
	 */
	SGApplication getSGApplication();

	/**
	 * 
	 * 终止服务.
	 * 
	 * @author shimiso
	 * @update 2012-7-6 上午9:05:51
	 */
//	public abstract void stopService();

	/**
	 * 
	 * 开启服务.
	 * 
	 * @author shimiso
	 * @update 2012-7-6 上午9:05:44
	 */
//	public abstract void startService();

	/**
	 * 
	 * 校验网络-如果没有网络就弹出设置,并返回true.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:03:56
	 */
	boolean validateInternet();

	/**
	 * 
	 * 校验网络-如果没有网络就返回true.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:05:15
	 */
	boolean hasInternetConnected();

	/**
	 * 
	 * 退出应用.
	 * 
	 * @author shimiso
	 * @update 2012-7-6 上午9:06:42
	 */
	void isExit();

	/**
	 * 
	 * 判断GPS是否已经开启.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:04:07
	 */
	boolean hasLocationGPS();

	/**
	 * 
	 * 判断基站是否已经开启.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:07:34
	 */
	boolean hasLocationNetWork();

	/**
	 * 
	 * 检查内存卡.
	 * 
	 * @author shimiso
	 * @update 2012-7-6 上午9:07:51
	 */
	void checkMemoryCard();

	/**
	 * 
	 * 显示toast.
	 * 
	 * @param text
	 *            内容
	 * @param longint
	 *            内容显示多长时间
	 * @author shimiso
	 * @update 2012-7-6 上午9:12:02
	 */
	void showToast(String text, int longint);

	/**
	 * 
	 * 短时间显示toast.
	 * 
	 * @param text
	 * @author shimiso
	 * @update 2012-7-6 上午9:12:46
	 */
	void showToast(String text);

	/**
	 * 
	 * 获取进度条.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:14:38
	 */
	ProgressDialog getProgressDialog();

	/**
	 * 
	 * 返回当前Activity上下文.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:19:54
	 */
	Context getContext();

	/**
	 * 
	 * 获取当前登录用户的SharedPreferences配置.
	 * 
	 * @return
	 * @author shimiso
	 * @update 2012-7-6 上午9:23:02
	 */
	SharedPreferences getLoginUserSharedPre();

	/**
	 * 
	 * 保存用户配置.
	 * 
	 * @param loginConfig
	 * @author shimiso
	 * @update 2012-7-6 上午9:58:31
	 */
	void saveLoginConfig(LoginConfig loginConfig);

	/**
	 * 
	 * 获取用户配置.
	 * 
	 * @param loginConfig
	 * @author shimiso
	 * @update 2012-7-6 上午9:59:49
	 */
	LoginConfig getLoginConfig();

	/**
	 * 
	 * 用户是否在线（当前网络是否重连成功）
	 * 
	 * @param loginConfig
	 * @author shimiso
	 * @update 2012-7-6 上午9:59:49
	 */
	boolean getUserOnlineState();

	/**
	 * 设置用户在线状态 true 在线 false 不在线
	 * 
	 * @param isOnline
	 */
	void setUserOnlineState(boolean isOnline);

	/**
	 * 
	 * 发出Notification的method.
	 * 
	 * @param iconId
	 *            图标
	 * @param contentTitle
	 *            标题
	 * @param contentText
	 *            你内容
	 * @param activity
	 * @author shimiso
	 * @update 2012-5-14 下午12:01:55
	 */
	void setNotiType(int iconId, String contentTitle,
					 String contentText, Class activity, String from);

	SGApplication getSGpplication();
}


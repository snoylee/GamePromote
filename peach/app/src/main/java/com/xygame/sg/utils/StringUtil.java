package com.xygame.sg.utils;


import android.content.ClipboardManager;
import android.content.Context;

public class StringUtil {
	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @return String
	 */
	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("") || str.trim().equals("－请选择－")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}
	
	public static double getPrice(long price){
		return price/100.0;
	}

	/**
	 * 请选择
	 */
	final static String PLEASE_SELECT = "请选择...";

	public static boolean notEmpty(Object o) {
		return o != null && !"".equals(o.toString().trim())
				&& !"null".equalsIgnoreCase(o.toString().trim())
				&& !"undefined".equalsIgnoreCase(o.toString().trim())
				&& !PLEASE_SELECT.equals(o.toString().trim())
				&& !"\"\"".equals(o.toString().trim());
	}

	public static boolean empty(Object o) {
		return o == null || "".equals(o.toString().trim())
				|| "null".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim())
				|| PLEASE_SELECT.equals(o.toString().trim())
				|| "\"\"".equals(o.toString().trim());
	}

	public static boolean num(Object o) {
		int n = 0;
		try {
			n = Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public static boolean decimal(Object o) {
		double n = 0;
		try {
			n = Double.parseDouble(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n > 0.0;
	}

	/**
	 * 给JID返回用户名
	 * 
	 * @param Jid
	 * @return
	 */
	public static String getUserNameByJid(String Jid) {
		if (empty(Jid)) {
			return null;
		}
		if (!Jid.contains("@")) {
			return Jid;
		}
		return Jid.split("@")[0];
	}

	/**
	 * 给用户名返回JID
	 * 
	 * @param jidFor
	 *            域名//如ahic.com.cn
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName, String jidFor) {
		if (empty(jidFor) || empty(jidFor)) {
			return null;
		}
		return userName + "@" + jidFor;
	}

	/**
	 * 给用户名返回JID,使用默认域名ahic.com.cn
	 * 
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName) {
		String jidFor = "getString";
		return getJidByName(userName, jidFor);
	}

	/**
	 * 根据给定的时间字符串，返回月 日 时 分 秒
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTomTime(String allDate) {
		return allDate.substring(5, 19);
	}

	/**
	 * 根据给定的时间字符串，返回月 日 时 分 月到分钟
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTime(String allDate) {
		return allDate.substring(5, 16);
	}

	public static boolean isNotEqual(Object a, Object b) {
		return !isEqual(a, b);
	}

	public static boolean isEqual(Object a, Object b) {
		if (a == null && b == null)
			return true;
		if (a != null && b != null)
			return a.toString().trim().equals(b.toString().trim());
		return false;
	}

	public static boolean isLower(Object a, Object b){
		if(a == null && b == null)
			return false;
		if(a != null && b != null)
			return a.toString().compareTo(b.toString()) < 0;
		return false;
	}
	
	public static boolean isBigger(Object a, Object b){
		if(a == null && b == null)
			return false;
		if(a != null && b != null)
			return a.toString().compareTo(b.toString()) > 0;
		return false;
	}

	public static boolean isBiggerAndEque(Object a, Object b){
		if(a == null && b == null)
			return false;
		if(a != null && b != null)
			return a.toString().compareTo(b.toString()) >= 0;
		return false;
	}

	public static boolean matches(Object o, String reg){
		if(o == null){
			return false;
		}
		try {
			return o.toString().trim().matches(reg);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public static String invisibleInfo(int start, int end, String mobiles)
	{
		char[] chars = mobiles.toCharArray();
		for(int i = start; i < end; i++)
		{
			chars[i] = '*';
		}
		return String.valueOf(chars);
	}

	public static int typeIdChange(int id){
		int topType = (id / 100) * 100;
		int targetType = id;
		if (topType == 100 || topType == 200 || topType == 600
				|| topType == 700) {
			targetType = 800;
		} else if (topType == 300 || topType == 400) {
			targetType = 900;
		} else if (topType == 500) {
			targetType = 1000;
		}
		return targetType;
	}

	/**
	 * 实现文本复制功能
	 * add by wangqianzhou
	 * @param content
	 */
	public static void copy(String content, Context context)
	{
// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}
	/**
	 * 实现粘贴功能
	 * add by wangqianzhou
	 * @param context
	 * @return
	 */
	public static String paste(Context context)
	{
// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getText().toString().trim();
	}
}

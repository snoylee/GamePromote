package com.xygame.sg.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.xy.im.util.EixstMultiRoomsUtils;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.main.MainFrameActivity;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.LoginTask;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.OfflineMsgManager;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.service.CacheService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tony on 2016/1/13.
 */
public class XMPPUtils {
	/**
	 * 所有的action的监听的必须要以"ACTION_"开头
	 *
	 */

	/**
	 * 花名册有删除的ACTION和KEY
	 */
	public static final String ROSTER_DELETED = "roster.deleted";
	public static final String ROSTER_DELETED_KEY = "roster.deleted.key";
	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 花名册有更新的ACTION和KEY
	 */
	public static final String ROSTER_UPDATED = "roster.updated";
	public static final String ROSTER_UPDATED_KEY = "roster.updated.key";

	/**
	 * 花名册有增加的ACTION和KEY
	 */
	public static final String ROSTER_ADDED = "roster.added";
	public static final String ROSTER_ADDED_KEY = "roster.added.key";

	/**
	 * 花名册中成员状态有改变的ACTION和KEY
	 */
	public static final String ROSTER_PRESENCE_CHANGED = "roster.presence.changed";
	public static final String ROSTER_PRESENCE_CHANGED_KEY = "roster.presence.changed.key";

	/**
	 * 收到好友邀请请求
	 */
	public static final String ROSTER_SUBSCRIPTION = "roster.subscribe";
	public static final String ROSTER_SUB_FROM = "roster.subscribe.from";
	public static final String NOTICE_ID = "notice.id";

	public static final String NEW_MESSAGE_ACTION = "roster.newmessage";
	public static final String NEW_TAKEOUT_ACTION = "com.xygame.sg.taked.out.action";
	public static final String NEW_MESSAGE_GROUP_NOTICE = "roster.newmessage.group.notice";
	public static final String NEW_MESSAGE_DISC_GROUP_NOTICE = "roster.newmessage.disc.group.notice";
	public static final String EDITOR_DISC_GROUP_INFO_ACTION = "roster.newmessage.edtor.disc.info";

	public static final String GROUP_NEW_MESSAGE_ACTION = "group.newmessage";

	public static final String NOTICE_LISTENER_ACTION = "allow.notice.faction.listener";

	public static final String UPDATE_MAIN_LOCATION = "com.xygame.sg.update.main.location";

	/**
	 * 我的消息
	 */
	public static final String MY_NEWS = "my.news";
	public static final String MY_NEWS_DATE = "my.news.date";

	/**
	 * 服务器的配置
	 */
	public static final String LOGIN_SET = "eim_login_set";// 登录设置
	public static final String USERNAME = "username";// 账户
	public static final String PASSWORD = "password";// 密码
	public static final String XMPP_HOST = "xmpp_host";// 地址
	public static final String XMPP_PORT = "xmpp_port";// 端口
	public static final String XMPP_SEIVICE_NAME = "xmpp_service_name";// 服务名
	public static final String IS_AUTOLOGIN = "isAutoLogin";// 是否自动登录
	public static final String IS_NOVISIBLE = "isNovisible";// 是否隐身
	public static final String IS_REMEMBER = "isRemember";// 是否记住账户密码
	public static final String IS_FIRSTSTART = "isFirstStart";// 是否首次启动
	/**
	 * 登录提示
	 */
	public static final int LOGIN_SECCESS = 0;// 成功
	public static final int HAS_NEW_VERSION = 1;// 发现新版本
	public static final int IS_NEW_VERSION = 2;// 当前版本为最新
	public static final int LOGIN_ERROR_ACCOUNT_PASS = 3;// 账号或者密码错误
	public static final int SERVER_UNAVAILABLE = 4;// 无法连接到服务器
	public static final int LOGIN_ERROR = 5;// 连接失败

	public static final String XMPP_CONNECTION_CLOSED = "xmpp_connection_closed";// 连接中断

	public static final String LOGIN = "login"; // 登录
	public static final String RELOGIN = "relogin"; // 重新登录

	/**
	 * 好友列表 组名
	 */
	public static final String ALL_FRIEND = "所有好友";// 所有好友
	public static final String NO_GROUP_FRIEND = "未分组好友";// 所有好友
	/**
	 * 系统消息
	 */
	public static final String ACTION_SYS_MSG = "action_sys_msg";// 消息类型关键字
	public static final String MSG_TYPE = "broadcast";// 消息类型关键字
	public static final String SYS_MSG = "sysMsg";// 系统消息关键字
	public static final String SYS_MSG_DIS = "系统消息";// 系统消息
	public static final String ADD_FRIEND_QEQUEST = "好友请求";// 系统消息关键字
	/**
	 * 请求某个操作返回的状态值
	 */
	public static final int SUCCESS = 0;// 存在
	public static final int FAIL = 1;// 不存在
	public static final int UNKNOWERROR = 2;// 出现莫名的错误.
	public static final int NETWORKERROR = 3;// 网络错误
	/***
	 * 企业通讯录根据用户ｉｄ和用户名去查找人员中的请求ｘｍｌ是否包含自组织
	 */
	public static final int containsZz = 0;
	/***
	 * 创建请求分组联系人列表xml分页参数
	 */
	public static final String currentpage = "1";// 当前第几页
	public static final String pagesize = "1000";// 当前页的条数

	/***
	 * 创建请求xml操作类型
	 */
	public static final String add = "00";// 增加
	public static final String rename = "01";// 增加
	public static final String remove = "02";// 增加

	/**
	 * 重连接
	 */
	/**
	 * 重连接状态acttion
	 *
	 */
	public static final String ACTION_RECONNECT_STATE = "action_reconnect_state";
	public static final String BORDCAST_PUSH_MESASAGE = "xy.com.referesh.push.message";
	public static final String BORDCAST_PUSH_MESASAGE_SUB = "xy.com.referesh.push.message.sub";
		public static final String ACTION_TO_MAINPAGE = "xy.com.sg.transfer.firstpage";
	/**
	 * 描述冲连接状态的关机子，寄放的intent的关键字
	 */
	public static final String RECONNECT_STATE = "reconnect_state";
	/**
	 * 描述冲连接，
	 */
	public static final boolean RECONNECT_STATE_SUCCESS = true;
	public static final boolean RECONNECT_STATE_FAIL = false;
	/**
	 * 是否在线的SharedPreferences名称
	 */
	public static final String PREFENCE_USER_STATE = "prefence_user_state";
	public static final String IS_ONLINE = "is_online";
	/**
	 * 精确到毫秒
	 */
	public static final String MS_FORMART = "yyyy-MM-dd HH:mm";

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);

	}

	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}

	public static String date2Str(Calendar c) {
		return date2Str(c, null);
	}

	public static String nonOrAfter(final long date, Context context) {
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		String timerStr = "";
		Date mDate = new Date(date);
		String mHour = df_DifferentHour.format(mDate);
		String hour = mHour.substring(0, 2);
		String min = mHour.substring(2, mHour.length());
		if (Integer.parseInt(hour) <= 12) {
			timerStr = "上午".concat(mHour);
		} else {
			switch (Integer.parseInt(hour)) {
			case 13:
				timerStr = "下午01".concat(min);
				break;
			case 14:
				timerStr = "下午02".concat(min);
				break;
			case 15:
				timerStr = "下午03".concat(min);
				break;
			case 16:
				timerStr = "下午04".concat(min);
				break;
			case 17:
				timerStr = "下午05".concat(min);
				break;
			case 18:
				timerStr = "下午06".concat(min);
				break;
			case 19:
				timerStr = "下午07".concat(min);
				break;
			case 20:
				timerStr = "下午08".concat(min);
				break;
			case 21:
				timerStr = "下午09".concat(min);
				break;
			case 22:
				timerStr = "下午10".concat(min);
				break;
			case 23:
				timerStr = "下午11".concat(min);
				break;
			case 24:
				timerStr = "下午12".concat(min);
				break;
			default:
				break;
			}
		}
		return timerStr;
	}

	public static String logTimeShow(final long date, Context context) {
		Calendar curTime = Calendar.getInstance();
		curTime.setTimeInMillis(date);
		int week = curTime.get(Calendar.WEEK_OF_MONTH);
		int dayOfYear = curTime.get(Calendar.DAY_OF_YEAR);
		Calendar time = Calendar.getInstance();
		int curWeek = time.get(Calendar.WEEK_OF_MONTH);
		int curDayOfYear = time.get(Calendar.DAY_OF_YEAR);

		String timerStr = "";
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat df_sameDayDifferentHour = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat df_DifferentDay = new SimpleDateFormat("MM-dd");
		SimpleDateFormat df_DifferentYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		Date mDate = new Date(date);
		Date currtDate = new Date();

		String currtYear = df_DifferentYear.format(currtDate);
		String mYear = df_DifferentYear.format(mDate);
		if (currtYear.equals(mYear)) {
			String currtForDay = df_DifferentDay.format(currtDate);
			String mForDay = df_DifferentDay.format(mDate);
			if (currtForDay.equals(mForDay)) {
				String mHour = df_DifferentHour.format(mDate);
				String hour = mHour.substring(0, 2);
				String min = mHour.substring(2, mHour.length());
				if (Integer.parseInt(hour) <= 12) {
					timerStr = "上午".concat(mHour);
				} else {
					switch (Integer.parseInt(hour)) {
					case 13:
						timerStr = "下午01".concat(min);
						break;
					case 14:
						timerStr = "下午02".concat(min);
						break;
					case 15:
						timerStr = "下午03".concat(min);
						break;
					case 16:
						timerStr = "下午04".concat(min);
						break;
					case 17:
						timerStr = "下午05".concat(min);
						break;
					case 18:
						timerStr = "下午06".concat(min);
						break;
					case 19:
						timerStr = "下午07".concat(min);
						break;
					case 20:
						timerStr = "下午08".concat(min);
						break;
					case 21:
						timerStr = "下午09".concat(min);
						break;
					case 22:
						timerStr = "下午10".concat(min);
						break;
					case 23:
						timerStr = "下午11".concat(min);
						break;
					case 24:
						timerStr = "下午12".concat(min);
						break;
					default:
						break;
					}
				}
			} else {
				if (week == curWeek) {
					if (dayOfYear == curDayOfYear - 1) {
						timerStr = context.getResources().getString(R.string.yesterday)
								.concat(nonOrAfter(date, context));
					} else if (dayOfYear == curDayOfYear - 2) {
						timerStr = context.getResources().getString(R.string.reyesterday)
								.concat(nonOrAfter(date, context));
					} else {
						long twoDays = getTwoDay(mDate, currtDate);
						if (twoDays < 7) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(mDate);
							int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
							if (week_index < 0) {
								week_index = 0;
							}
							timerStr = weeks[week_index];
						}
					}
				} else {
					timerStr = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
				}
			}
		} else {
			timerStr = df_sameDayDifferentHour.format(mDate);
		}

		return timerStr;
	}

	public static long getTwoDay(Date begin_date, Date end_date) {
		long day = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = format.format(Calendar.getInstance().getTime());

			if (begin_date == null) {
				begin_date = format.parse(sdate);
			}
			if (end_date == null) {
				end_date = format.parse(sdate);
			}
			day = (end_date.getTime() - begin_date.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return -1;
		}
		return day;
	}

	public static long compareTimers(String nowTime, String beforTime) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(MS_FORMART);
		Date now = df.parse(nowTime);
		Date date = df.parse(beforTime);
		long l = now.getTime() - date.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		return min;
	}

	public static String getShowTime(String oldTime) throws ParseException {
		long date = new SimpleDateFormat(MS_FORMART).parse(oldTime).getTime();
		Calendar curTime = Calendar.getInstance();
		curTime.setTimeInMillis(date);
		String timerStr = "";
		SimpleDateFormat df_sameDayDifferentHour = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat df_DifferentDay = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat df_DifferentYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		Date mDate = new Date(date);
		Date currtDate = new Date();

		String currtYear = df_DifferentYear.format(currtDate);
		String mYear = df_DifferentYear.format(mDate);
		if (currtYear.equals(mYear)) {
			String currtForDay = df_DifferentDay.format(currtDate);
			String mForDay = df_DifferentDay.format(mDate);
			if (currtForDay.equals(mForDay)) {
				timerStr = df_DifferentHour.format(mDate);
			} else {
				timerStr = df_DifferentDay.format(mDate);
			}
		} else {
			timerStr = df_sameDayDifferentHour.format(mDate);
		}

		return timerStr;
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
	}

	/**
	 * 获得当前日期的字符串格式
	 *
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}

	// 格式到秒
	public static String getMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

	}

	// 格式到天
	public static String getDay(long time) {

		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}

	// 格式到毫秒
	public static String getSMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

	}

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
		if (str == null || str.equalsIgnoreCase("null") || str.trim().equals("") || str.trim().equals("－请选择－")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	/**
	 * 请选择
	 */
	final static String PLEASE_SELECT = "请选择...";

	public static boolean notEmpty(Object o) {
		return o != null && !"".equals(o.toString().trim()) && !"null".equalsIgnoreCase(o.toString().trim())
				&& !"undefined".equalsIgnoreCase(o.toString().trim()) && !PLEASE_SELECT.equals(o.toString().trim())
				&& !"\"\"".equals(o.toString().trim());
	}

	public static boolean empty(Object o) {
		return o == null || "".equals(o.toString().trim()) || "null".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim()) || PLEASE_SELECT.equals(o.toString().trim())
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

	public static boolean isLower(Object a, Object b) {
		if (a == null && b == null)
			return false;
		if (a != null && b != null)
			return a.toString().compareTo(b.toString()) < 0;
		return false;
	}

	public static boolean isBigger(Object a, Object b) {
		if (a == null && b == null)
			return false;
		if (a != null && b != null)
			return a.toString().compareTo(b.toString()) > 0;
		return false;
	}

	public static boolean matches(Object o, String reg) {
		if (o == null) {
			return false;
		}
		try {
			return o.toString().trim().matches(reg);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static String invisibleInfo(int start, int end, String mobiles) {
		char[] chars = mobiles.toCharArray();
		for (int i = start; i < end; i++) {
			chars[i] = '*';
		}
		return String.valueOf(chars);
	}

	public static void loginXMPP(Context context, String password, String nickName) {
		LoginTask loginTask = new LoginTask(context, password, nickName);
		loginTask.execute();
	}

	public static String date2Str(long timeTimp, String format) {
		return date2Str(new Date(timeTimp), format);
	}

	public static String getUserJid(Context context, String userId) {
		return userId.concat("@").concat(context.getResources().getString(R.string.xmpp_service_name)).concat("/sgapp");
	}

	public static boolean isOnline(Context context, String userId) {
		boolean flag=false;
		Presence presence = SGApplication.getInstance().getConnection().getRoster()
				.getPresence(userId.concat("@").concat(context.getResources().getString(R.string.xmpp_service_name)));
		flag = presence.isAvailable();

		return flag;
	}

	public static int downMp3Task(String mp3Url, String rECORD_ROOT_PATH, String string) throws Exception {
		int result = new HttpDownloadUtil().downFile(mp3Url, rECORD_ROOT_PATH, string);
		return result;
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	public static boolean isHome(Context context){
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		List<String> strs = getHomes(context);
		if(strs != null && strs.size() > 0){
			return strs.contains(rti.get(0).topActivity.getPackageName());
		}else{
			return false;
		}
	}


	/**
	 * 获得属于桌面的应用的应用包名称
	 * @return 返回包含所有包名的字符串列表
	 */
	public static List<String> getHomes(Context context) {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		//属性
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo ri : resolveInfo){
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	public static void reConnect(Context context) {
			if (UserPreferencesUtil.isOnline(context)){
				if (SGApplication.getInstance().getConnection()!=null){
					if (!SGApplication.getInstance().getConnection().isAuthenticated()){
						ThreadPool.getInstance().excuseThread(new ReconnectXmppAct(context));
					}else{
						initDiscGroupAction(context);
						initGroupAction(context);
					}
				}
			}
	}

	private static class ReconnectXmppAct implements Runnable{
		private Context context;
		public ReconnectXmppAct(Context context){
			this.context=context;
		}
		@Override
		public void run() {
			try {
				SGApplication.getInstance().getConnection().connect();
				if (SGApplication.getInstance().getConnection().isConnected()){
					if (!SGApplication.getInstance().getConnection().isAuthenticated()){
						SGApplication.getInstance().getConnection().login(UserPreferencesUtil.getUserId(context), UserPreferencesUtil.getPwd(context), "sgapp"); // 登录
						OfflineMsgManager.getInstance(context).dealOfflineMsg(SGApplication.getInstance().getConnection());//处理离线消息
						SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.available));
						DeliveryReceiptManager.getInstanceFor(SGApplication.getInstance().getConnection()).enableAutoReceipts();
						initDiscGroupAction(context);
						initGroupAction(context);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void initDiscGroupAction(Context context){
		ThreadPool.getInstance().excuseThread(new InitDiscGroupRoom(context));
	}

	private static class InitDiscGroupRoom implements Runnable{
		private Context context;
		public InitDiscGroupRoom(Context context){
			this.context=context;
		}
		@Override
		public void run() {
			if(SGApplication.getInstance().getConnection().isConnected()){
				List<GroupBean> groupDatas=CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(context));
				if (groupDatas!=null){
					String serviceName=SGApplication.getInstance().getConnection().getServiceName();
					for (GroupBean item:groupDatas){
						DiscussionHistory history = new DiscussionHistory();
						GroupBean temp= GroupEngine.quaryGroupBean(context, item, UserPreferencesUtil.getUserId(context));
						if (temp!=null){
							if (!TextUtils.isEmpty(temp.getLastIntoTimer())){
								history.setSince(new Date(Long.parseLong(temp.getLastIntoTimer())));
							}else{
								history.setSince(new Date(System.currentTimeMillis()));
							}
						}else {
							history.setSince(new Date(System.currentTimeMillis()));
						}
						String roomId=item.getGroupId().concat("@").concat("conference.").concat(serviceName);
						try {
							MultiUserChat chat = new MultiUserChat(SGApplication.getInstance().getConnection(),
									roomId);
							chat.join(UserPreferencesUtil.getUserId(context),null,history, SmackConfiguration.getPacketReplyTimeout());
							EixstMultiRoomsUtils.addMutiRoomsItem(chat,item.getGroupId(),context);
						}catch (XMPPException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
	}

	public static void initGroupAction(Context context) {
		ThreadPool.getInstance().excuseThread(new SetGroupForMe(context));
	}

	private static class SetGroupForMe implements Runnable{
		private Context context;
		public SetGroupForMe(Context context){
			this.context=context;
		}
		@Override
		public void run() {
			if(SGApplication.getInstance().getConnection().isConnected()){
				List<GroupBean> groupDatas=CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(context));
				if (groupDatas!=null){
					String serviceName=SGApplication.getInstance().getConnection().getServiceName();
					for (GroupBean item:groupDatas){
						DiscussionHistory history = new DiscussionHistory();
						GroupBean temp= GroupEngine.quaryGroupBean(context, item, UserPreferencesUtil.getUserId(context));
						if (temp!=null){
							if (!TextUtils.isEmpty(temp.getLastIntoTimer())){
								history.setSince(new Date(Long.parseLong(temp.getLastIntoTimer())));
							}else{
								history.setSince(new Date(System.currentTimeMillis()));
							}
						}else {
							history.setSince(new Date(System.currentTimeMillis()));
						}
						history.setMaxStanzas(100);
						String roomId=item.getGroupId().concat("@").concat("conference.").concat(serviceName);
						try {
							MultiUserChat chat = new MultiUserChat(SGApplication.getInstance().getConnection(),
									roomId);
							chat.join(UserPreferencesUtil.getUserId(context),null,history, SmackConfiguration.getPacketReplyTimeout());
							EixstMultiRoomsUtils.addMutiRoomsItem(chat, item.getGroupId(),context);
						}catch (XMPPException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public static void takoutFriend(Context context,String groupId) {
		ThreadPool.getInstance().excuseThread(new TakeOutGroup(groupId,context));
	}

	private static class TakeOutGroup implements Runnable{
		private String groupId;
		private Context context;
		public TakeOutGroup(String groupId,Context context){
			this.context=context;
			this.groupId=groupId;
		}
		@Override
		public void run() {
			List<GroupBean> groupDatas=CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(context));
			if (groupDatas!=null){
				for (int i=0;i<groupDatas.size();i++){
					if (groupId.equals(groupDatas.get(i).getGroupId())){
						GroupEngine.inserDeleGroup(context,groupDatas.get(i));
						groupDatas.remove(i);
						break;
					}
				}
				CacheService.getInstance().cacheGroupDatas(UserPreferencesUtil.getUserId(context),groupDatas);
			}
			MultiUserChat chat=EixstMultiRoomsUtils.getMultiUserChat(groupId, context);
			if(SGApplication.getInstance().getConnection().isConnected()){
				if (chat!=null){
					chat.leave();
//					if (chat.isJoined()){
//						chat.leave();
//					}
				}
			}
			EixstMultiRoomsUtils.deleMultiUserChat(groupId,context);
		}
	}

	public static void addInDiscRoom(Context context,String body){
		ThreadPool.getInstance().excuseThread(new InitChatRoom(context,body));
	}

	private static class InitChatRoom implements Runnable{
		private String body;
		private Context context;
		public InitChatRoom(Context context,String body){
			this.context=context;
			this.body=body;
		}
		@Override
		public void run() {
			try {
				JSONObject object=new JSONObject(body);
				String custom=StringUtils.getJsonValue(object,"custom");
				JSONObject object1=new JSONObject(custom);
				String groupId=StringUtils.getJsonValue(object1,"groupId");
				String groupName=StringUtils.getJsonValue(object1, "groupName");
				String createUserId=StringUtils.getJsonValue(object1,"createUserId");
				if (SGApplication.getInstance().getConnection().isConnected()) {
					String serviceName = SGApplication.getInstance().getConnection().getServiceName();
					String roomId =groupId.concat("@").concat("conference.").concat(serviceName);
					MultiUserChat chat = new MultiUserChat(SGApplication.getInstance().getConnection(),
							roomId);
					DiscussionHistory history = new DiscussionHistory();
					history.setMaxChars(0);
					long beginTime=System.currentTimeMillis();
					Date date=new Date(beginTime);
					history.setSince(date);
					List<GroupBean> groupDatas=CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(context));
					if (groupDatas==null){
						groupDatas=new ArrayList<>();
					}
					GroupBean groupBean=new GroupBean();
					groupBean.setCreateUserId(createUserId);
					groupBean.setLastIntoTimer(String.valueOf(beginTime));
					groupBean.setGroupName(groupName);
					groupBean.setGroupId(groupId);
					groupDatas.add(groupBean);
					CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(context), groupDatas);
					GroupEngine.deleteDeleGroupByGroupId(context,groupBean.getGroupId(),UserPreferencesUtil.getUserId(context));
					EixstMultiRoomsUtils.addMutiRoomsItem(chat, groupId, context);
					Intent intent2 = new Intent(XMPPUtils.NEW_MESSAGE_DISC_GROUP_NOTICE);
					intent2.putExtra("newsMessage", false);
					intent2.putExtra("takedOut", false);
					context.sendBroadcast(intent2);
					chat.join(UserPreferencesUtil.getUserId(context), null, history, SmackConfiguration.getPacketReplyTimeout());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static void kickOutDiscRoom(Context context, String body) {
		ThreadPool.getInstance().excuseThread(new KickOutChatRoom(context,body));
	}

	private static class KickOutChatRoom implements Runnable{
		private String body;
		private Context context;
		public KickOutChatRoom(Context context,String body){
			this.context=context;
			this.body=body;
		}
		@Override
		public void run() {
			try {
				JSONObject object=new JSONObject(body);
				String custom=StringUtils.getJsonValue(object,"custom");
				JSONObject object1=new JSONObject(custom);
				String groupId=StringUtils.getJsonValue(object1,"groupId");
				List<GroupBean> groupDatas= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(context));
				GroupBean groupBean=null;
				if (groupDatas!=null){
					for (int i=0;i<groupDatas.size();i++){
						if (groupId.equals(groupDatas.get(i).getGroupId())){
							groupBean=groupDatas.get(i);
							GroupEngine.inserDeleGroup(context,groupBean);
							groupDatas.remove(i);
							break;
						}
					}
					CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(context),groupDatas);
				}

				MultiUserChat chat=EixstMultiRoomsUtils.getMultiUserChat(groupId, context);
				if(SGApplication.getInstance().getConnection().isConnected()){
					if (chat!=null){
						chat.leave();
					}
				}
				EixstMultiRoomsUtils.deleMultiUserChat(groupId, context);
				SGNewsBean sendMsgBean1=insertTipNews(context, "你被移除讨论组", groupBean);
				Intent intent2 = new Intent(XMPPUtils.NEW_MESSAGE_DISC_GROUP_NOTICE);
				intent2.putExtra("newBean", sendMsgBean1);
				intent2.putExtra("newsMessage", true);
				intent2.putExtra("takedOut",true);
				context.sendBroadcast(intent2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessgaeForXiaDan(Context context,String content,ToChatBean toChatBean){
		Chat chat = null;
		SGNewsBean sendBean = new SGNewsBean();
		SGNewsBean msgBean = new SGNewsBean();
		msgBean.setFromUser(SenderUser.getFromUserJsonStr(context));
		msgBean.setToUser(SenderUser.getToUserJsonStr(toChatBean.getUserId(), toChatBean.getUserIcon(),
				toChatBean.getUsernick()));
		msgBean.setNoticeSubject(toChatBean.getNoticeSubject());
		msgBean.setRecruitLocIndex(toChatBean.getRecruitLocIndex());
		msgBean.setNoticeId(toChatBean.getNoticeId());
		msgBean.setFriendNickName(toChatBean.getUsernick());
		msgBean.setFriendUserIcon(toChatBean.getUserIcon());
		msgBean.setFriendUserId(toChatBean.getUserId());
		msgBean.setRecruitId(toChatBean.getRecruitId());
		try {
			if (SGApplication.getInstance().getConnection() != null) {
				chat = SGApplication.getInstance().getConnection().getChatManager()
						.createChat(XMPPUtils.getUserJid(context, msgBean.getFriendUserId()), null);
			}
			// 创建消息实体
			long timestamp = System.currentTimeMillis();
			String messageContent = "";
			JSONObject bodyMsg = new JSONObject();
			bodyMsg.put("msgContent", content);
			bodyMsg.put("type", Constants.SEND_TEXT);
			JSONObject ext = new JSONObject();
			ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
			ext.put("toUser", new JSONObject(msgBean.getToUser()));
			ext.put("noticeSubject", msgBean.getNoticeSubject());
			ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
			bodyMsg.put("ext", ext);
			bodyMsg.put("timestamp", timestamp);
			bodyMsg.put("noticeId", msgBean.getNoticeId());
			bodyMsg.put("recruitId", msgBean.getRecruitId());
			messageContent = bodyMsg.toString();
			String time = XMPPUtils.date2Str(timestamp, XMPPUtils.MS_FORMART);
			Message message = new Message();
//			message.setProperty("immessage.time", time);
			message.setBody(messageContent);
			// 添加实体对象

			sendBean.setFromUser(msgBean.getFromUser());
			sendBean.setToUser(msgBean.getToUser());
			sendBean.setNoticeSubject(msgBean.getNoticeSubject());
			sendBean.setRecruitLocIndex(msgBean.getRecruitLocIndex());
			sendBean.setNoticeId(msgBean.getNoticeId());
			sendBean.setFriendNickName(msgBean.getFriendNickName());
			sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
			sendBean.setFriendUserId(msgBean.getFriendUserId());
			// +++++++++++++++++++++++++++++++++++++++++++++++
			sendBean.setMsgContent(content);
			sendBean.setNewType(Constants.NEWS_CHAT);
			sendBean.setRecruitId(msgBean.getRecruitId());
			sendBean.setTimestamp(String.valueOf(timestamp));
			sendBean.setType(Constants.SEND_TEXT);
			sendBean.setUserId(UserPreferencesUtil.getUserId(context));
			sendBean.setMessageStatus(Constants.NEWS_READ);
			sendBean.setInout(Constants.NEWS_END);
			sendBean.setIsShow(Constants.NEWS_SHOW);
			sendBean.setMsgStatus(Constants.NEWS_SENDING);
			// 新增本地数据
			NewsEngine.inserChatNew(context, sendBean);
			// 发送消息
			DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
//			String messageId=message.getPacketID();
			if (chat!=null){
				chat.sendMessage(message);
				sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
				NewsEngine.updateChatSendStatus(context, sendBean, UserPreferencesUtil.getUserId(context));
			}else{
				sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
				NewsEngine.updateChatSendStatus(context, sendBean, UserPreferencesUtil.getUserId(context));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
			NewsEngine.updateChatSendStatus(context, sendBean, UserPreferencesUtil.getUserId(context));
		}
	}

	public static SGNewsBean insertTipNews(Context context,String content,GroupBean groupBean){
		SGNewsBean sendBean = new SGNewsBean();
		// 创建消息实体
		long timestamp = System.currentTimeMillis();
		sendBean.setFromUser(SenderUser.getFromUserJsonStr(context));
		sendBean.setNoticeSubject(groupBean.getGroupName());
		sendBean.setRecruitLocIndex("");
		sendBean.setNoticeId(groupBean.getGroupId());
		sendBean.setFriendNickName("");
		sendBean.setFriendUserIcon("");
		sendBean.setFriendUserId("");
		// +++++++++++++++++++++++++++++++++++++++++++++++
		sendBean.setMsgContent(content);
		sendBean.setNewType(Constants.GROUP_CHAT);
		sendBean.setRecruitId("");
		sendBean.setTimestamp(String.valueOf(timestamp));
		sendBean.setType(Constants.SEND_TEXT_TIP);
		sendBean.setUserId(UserPreferencesUtil.getUserId(context));
		sendBean.setMessageStatus(Constants.NEWS_READ);
		sendBean.setInout(Constants.NEWS_END);
		sendBean.setIsShow(Constants.NEWS_SHOW);
		sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
		// 新增本地数据
		TempGroupNewsEngine.inserChatNew(context, sendBean);
		return sendBean;
	}

	public static void sendInviteFriendMsg(Context context,String friendName,SGNewsBean sendBean){
		try {
			MultiUserChat chat= EixstMultiRoomsUtils.getMultiUserChat(sendBean.getNoticeId(), context);
			String serviceName = SGApplication.getInstance().getConnection().getServiceName();
			String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);

			// 创建消息实体
			long timestamp = System.currentTimeMillis();
			String messageContent = "";
			JSONObject bodyMsg = new JSONObject();
			bodyMsg.put("msgContent", friendName);
			bodyMsg.put("type", ConstTaskTag.GROUP_DISC_FRIENDJOIN);
			JSONObject ext = new JSONObject();
			ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
			bodyMsg.put("ext", ext);
			bodyMsg.put("timestamp", timestamp);
			bodyMsg.put("groupId", sendBean.getNoticeId());
			bodyMsg.put("groupName",sendBean.getNoticeSubject());
			messageContent = bodyMsg.toString();
			org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
			message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
			message.setTo(roomId);
			message.setFrom(UserPreferencesUtil.getUserId(context).concat("@moreidols/sgapp"));
			message.setSubject(ConstTaskTag.GROUP_DISC_FRIENDJOIN);
			message.setBody(messageContent);
			chat.sendMessage(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendLoseGroupMsg(Context context,String friendName,SGNewsBean sendBean){
		try {
			MultiUserChat chat= EixstMultiRoomsUtils.getMultiUserChat(sendBean.getNoticeId(), context);
			String serviceName = SGApplication.getInstance().getConnection().getServiceName();
			String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);

			// 创建消息实体
			long timestamp = System.currentTimeMillis();
			String messageContent = "";
			JSONObject bodyMsg = new JSONObject();
			bodyMsg.put("msgContent", friendName);
			bodyMsg.put("type", ConstTaskTag.GROUP_DISC_LOSE);
			JSONObject ext = new JSONObject();
			ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
			bodyMsg.put("ext", ext);
			bodyMsg.put("timestamp", timestamp);
			bodyMsg.put("groupId", sendBean.getNoticeId());
			bodyMsg.put("groupName",sendBean.getNoticeSubject());
			messageContent = bodyMsg.toString();
			org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
			message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
			message.setTo(roomId);
			message.setFrom(UserPreferencesUtil.getUserId(context).concat("@moreidols/sgapp"));
			message.setSubject(ConstTaskTag.GROUP_DISC_LOSE);
			message.setBody(messageContent);
			chat.sendMessage(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static long getLeftSecondTime(long startTime) {
		long diff;
		long limitTime;
		Date d2 = new Date();
		Date d1 = new Date(startTime);
		diff = d2.getTime()-d1.getTime();// 这样得到的差值是微秒级别
		limitTime=diff/1000;
		return limitTime;
	}

	public static void exitAppUpdateBean(Context context){
		TimerCountBean timerCountBean=TimerEngine.quaryTimerBeansByDuringLength(context, UserPreferencesUtil.getUserId(context), Constants.IS_REQUEST_QIANG_FLAG);
		if (timerCountBean==null){
			timerCountBean=new TimerCountBean();
			List<GodQiangDanRebackBean> qiangDanRebackBeans= CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(context));
			if (qiangDanRebackBeans!=null){
				timerCountBean.setStartTime(qiangDanRebackBeans.get(qiangDanRebackBeans.size()-1).getOrderTime());
			}else{
				timerCountBean.setStartTime("");
			}
			timerCountBean.setDuringLength(Constants.IS_REQUEST_QIANG_FLAG);
			timerCountBean.setGroupId("1");
			timerCountBean.setUserId(UserPreferencesUtil.getUserId(context));
			TimerEngine.inserTimerBean(context,timerCountBean);
		}else{
			List<GodQiangDanRebackBean> qiangDanRebackBeans= CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(context));
			if (qiangDanRebackBeans!=null){
				timerCountBean.setStartTime(qiangDanRebackBeans.get(qiangDanRebackBeans.size()-1).getOrderTime());
			}else{
				timerCountBean.setStartTime("");
			}
			timerCountBean.setGroupId("1");
			TimerEngine.updateTimerBeanInfo(context,timerCountBean);
		}
	}

	public static void intoAppUpdateBean(Context context){
		TimerCountBean timerCountBean=TimerEngine.quaryTimerBeansByDuringLength(context, UserPreferencesUtil.getUserId(context), Constants.IS_REQUEST_QIANG_FLAG);
		if (timerCountBean==null){
			timerCountBean=new TimerCountBean();
			timerCountBean.setStartTime("");
			timerCountBean.setDuringLength(Constants.IS_REQUEST_QIANG_FLAG);
			timerCountBean.setGroupId("0");
			timerCountBean.setUserId(UserPreferencesUtil.getUserId(context));
			TimerEngine.inserTimerBean(context,timerCountBean);
		}else{
			timerCountBean.setStartTime("");
			timerCountBean.setGroupId("0");
			TimerEngine.updateTimerBeanInfo(context,timerCountBean);
		}
	}

	public static void sendMessageFromGodToCustom(Context context,SGNewsBean sgNewsBean){
		try{
			JSONObject obj=new JSONObject(sgNewsBean.getNoticeSubject());
			String orderType=StringUtils.getJsonValue(obj,"orderType");
			if ("2".equals(orderType)){
				String userId=StringUtils.getJsonValue(obj, "userId");
				String userNick=sgNewsBean.getFriendNickName();
				String userIcon=sgNewsBean.getFriendUserIcon();
				ToChatBean toChatBean = new ToChatBean();
				toChatBean.setRecruitLocIndex("");
				toChatBean.setNoticeId("");
				toChatBean.setNoticeSubject(userNick);
				toChatBean.setUserIcon(userIcon);
				toChatBean.setUserId(userId);
				toChatBean.setUsernick(userNick);
				toChatBean.setRecruitId("");
				sendMessgaeForXiaDan(context, UserPreferencesUtil.getUserNickName(context).concat(" 接受了您的订单"), toChatBean);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}

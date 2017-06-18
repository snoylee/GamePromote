package com.xygame.sg.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xygame.sg.R;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.LoginConfig;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import base.action.Action;
import base.frame.VisitUnit;

/**
 *
 * 聊天服务.
 *
 */
public class JugmentHomeService extends Service {

	private Context context;

	private boolean isHomeDesk=true,isConnecting=false;
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		runningThread();
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

	public void runningThread(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					initJugment();
					try {
						Thread.sleep(5000);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void initJugment(){
			isHomeDesk=isHome();
			Boolean isLine=UserPreferencesUtil.isOnline(SGApplication.getInstance().getBaseContext());
			if(isLine){
				if (isHomeDesk){
//					XMPPConnection connection = SGApplication.getInstance()
//							.getConnection();
//					if(connection!=null){
//						try{
//							SGApplication.getInstance().closeConnection();
//							android.os.Message m = handler.obtainMessage();
//							m.what=1;
//							m.sendToTarget();
//						}catch (Exception e){
//							e.printStackTrace();
//						}
//					}
				}else{
					XMPPConnection connection = SGApplication.getInstance()
							.getConnection();
					if(connection!=null){
						if (!isConnecting){
							isConnecting=true;
							reConnect();
						}
					}
				}
			}
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	public boolean isHome(){
		ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		List<String> strs = getHomes();
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
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
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

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					Toast.makeText(context, "用户已下线!", Toast.LENGTH_LONG).show();
					break;
				case 2:
					SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.available));
					Toast.makeText(context, "用户已上线!", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}

		}
	};

	/**
	 *
	 * 递归重连，直连上为止.
	 *
	 * @author shimiso
	 * @update 2012-7-10 下午2:12:25
	 */
	public void reConnect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							SGApplication.getInstance()
									.getConnection().connect();
							isConnecting=false;
							android.os.Message m = handler.obtainMessage();
							m.what=2;
							m.sendToTarget();
						} catch (XMPPException e) {
							isConnecting=false;
							e.printStackTrace();
						}

					}
				}).start();
			}
		}).start();
	}
}
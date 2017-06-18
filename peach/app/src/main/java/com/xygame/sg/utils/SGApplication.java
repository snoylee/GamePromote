package com.xygame.sg.utils;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.xygame.second.sg.utils.Contant;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.bean.comm.ModelStyleBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.search.UserSearch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import base.file.shpref.SPrefUtil;
import base.init.InitFacility;



/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月4日
 */
public class SGApplication extends MultiDexApplication {
    private  XMPPConnection connection;
	public static final String CALLBACK_RECEIVER_ACTION = "callback_receiver_action";
    private LocationClient mLocClient;
    private BDLocationListener listener;
    private static SGApplication mAppContext;
    private List<CarrierBean> carrierDatas = new ArrayList<>();
    private List<CarrierBean> modelCarriers = new ArrayList<CarrierBean>();
    private List<CarrierBean> cmCarriers = new ArrayList<CarrierBean>();
    private BroadcastReceiver pictrueListner;
    public static IUmengRegisterCallback mRegisterCallback;
    private Map<String,List<ShootTypeBean>> mapTypeList=new HashMap<>();
	public static IUmengUnregisterCallback mUnregisterCallback;
    
    private PushAgent mPushAgent;

    public List<CarrierBean> getCarrierDatas() {
        return carrierDatas;
    }

    public void setCarrierDatas(List<CarrierBean> carrierDatas) {
        this.carrierDatas = carrierDatas;
    }

    public List<CarrierBean> getCmCarriers() {
        return cmCarriers;
    }

    public void setCmCarriers(List<CarrierBean> cmCarriers) {
        this.cmCarriers = cmCarriers;
    }

    public List<CarrierBean> getModelCarriers() {
        return modelCarriers;
    }

    public void setModelCarriers(List<CarrierBean> modelCarriers) {
        this.modelCarriers = modelCarriers;
    }

    private List<Activity> activityList = new LinkedList<Activity>();

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}

    /**
     * 重载方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        SDKInitializer.initialize(this);
        initBase();
        AssetDataBaseManager.initDataBaseManager(getBaseContext());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .memoryCacheSize(2 * 1024 * 1024)
//                .discCacheSize(50*1024*1024)
                .discCache(new UnlimitedDiskCache(new File(ConstTaskTag.imageCacher())))//自定义缓存路径
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        UserPreferencesUtil.init(this);
        SPrefUtil.iniContext(this);
        initUmemSDK();
        initQuPaiSDK();
//        openConnection();
    }

    private void initQuPaiSDK() {
        QupaiService qupaiService = QupaiManager
                .getQupaiService(this);
        UISettings _UISettings = new UISettings() {

            @Override
            public boolean hasEditor() {
                return true;
            }

            @Override
            public boolean hasImporter() {
                return true;
            }

            @Override
            public boolean hasGuide() {
                return true;
            }

            @Override
            public boolean hasSkinBeautifer() {
                return true;
            }
        };

        MovieExportOptions movie_options = new MovieExportOptions.Builder()
                .setVideoBitrate(Contant.DEFAULT_BITRATE)
                .configureMuxer("movflags", "+faststart")
                .build();

        ProjectOptions projectOptions = new ProjectOptions.Builder()
                .setVideoSize(480, 480)
                .setVideoFrameRate(30)
                .setDurationRange(Contant.DEFAULT_MIN_DURATION_LIMIT,Contant.DEFAULT_DURATION_LIMIT)
                .get();

        ThumbnailExportOptions thumbnailExportOptions =new ThumbnailExportOptions.Builder()
                .setCount(1).get();

        VideoSessionCreateInfo info =new VideoSessionCreateInfo.Builder()
                .setWaterMarkPath(Contant.WATER_MARK_PATH)
                .setWaterMarkPosition(1)
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                .setBeautyProgress(80)
                .setBeautySkinOn(true)
                .setMovieExportOptions(movie_options)
                .setThumbnailExportOptions(thumbnailExportOptions)
                .build();

        qupaiService.initRecord(info,projectOptions,_UISettings);

        qupaiService.addMusic(0, "Barcelona", "assets://Qupai/music/Barcelona");
        qupaiService.addMusic(1, "Cascading", "assets://Qupai/music/Cascading");
        qupaiService.addMusic(2, "Do Now", "assets://Qupai/music/Do Now");
        qupaiService.addMusic(3, "Dreaming", "assets://Qupai/music/Dreaming");
        qupaiService.addMusic(4, "Firefly Waltz", "assets://Qupai/music/Firefly Waltz");
        qupaiService.addMusic(5, "Game Doubt", "assets://Qupai/music/Game Doubt");
        qupaiService.addMusic(6, "Jazz Club", "assets://Qupai/music/Jazz Club");
        qupaiService.addMusic(7, "Pipi", "assets://Qupai/music/Pipi");
        qupaiService.addMusic(8, "Sunday", "assets://Qupai/music/Sunday");
        qupaiService.addMusic(9, "You&Me", "assets://Qupai/music/You&Me");
    }

    {
        PlatformConfig.setWeixin(Constants.WeiXin_APP_ID, Constants.WeiXin_APP_KEY);
        PlatformConfig.setSinaWeibo("2581396869", "6865d28dc4c45df0bb3f6f6267252cbf");
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
    }

    private void initUmemSDK() {
        mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);
		/**
		 * 该Handler是在IntentService中被调用，故 1.
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 2.
		 * IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
		 * 或者可以直接启动Service
		 * */
		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			@Override
			public void dealWithCustomMessage(final Context context,
					final UMessage msg) {
				new Handler(getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						UTrack.getInstance(getApplicationContext())
								.trackMsgClick(msg);
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG)
								.show();
					}
				});
			}

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(
							context);
					RemoteViews myNotificationView = new RemoteViews(
							context.getPackageName(),
							R.layout.notification_view);
					myNotificationView.setTextViewText(R.id.notification_title,
							msg.title);
					myNotificationView.setTextViewText(R.id.notification_text,
							msg.text);
					myNotificationView.setImageViewBitmap(
							R.id.notification_large_icon,
							getLargeIcon(context, msg));
					myNotificationView.setImageViewResource(
							R.id.notification_small_icon,
							getSmallIconId(context, msg));
					builder.setContent(myNotificationView);
					builder.setAutoCancel(true);
					Notification mNotification = builder.build();
					// 由于Android
					// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
					mNotification.contentView = myNotificationView;
					return mNotification;
				default:
					// 默认为0，若填写的builder_id并不存在，也使用默认。
					return super.getNotification(context, msg);
				}
			}
		};
		mPushAgent.setMessageHandler(messageHandler);

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

		mRegisterCallback = new IUmengRegisterCallback() {

			@Override
			public void onRegistered(String registrationId) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CALLBACK_RECEIVER_ACTION);
				sendBroadcast(intent);
			}

		};
		mPushAgent.setRegisterCallback(mRegisterCallback);

		mUnregisterCallback = new IUmengUnregisterCallback() {

			@Override
			public void onUnregistered(String registrationId) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CALLBACK_RECEIVER_ACTION);
				sendBroadcast(intent);
			}
		};
		mPushAgent.setUnregisterCallback(mUnregisterCallback);
	}

	public void addImageListener(BroadcastReceiver pictrueListner) {
        this.pictrueListner = pictrueListner;
    }

    public void registerImagesReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        registerReceiver(pictrueListner, myIntentFilter);
    }

    public void unregisterImagesReceiver() {
        unregisterReceiver(pictrueListner);
    }

    public void registerImagesReceiver(BroadcastReceiver pictrueListner) {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        registerReceiver(pictrueListner, myIntentFilter);
    }

    public void unregisterImagesReceiver(BroadcastReceiver pictrueListner) {
        unregisterReceiver(pictrueListner);
    }


    private void initBase() {
        Constants.init();
        InitFacility.init(this, "com.xygame.sg.task");
    }

    public static SGApplication getInstance() {
        return mAppContext;
    }

    public void initBaiduLaction(BDLocationListener myListener) {
        if (mLocClient == null) {
            mLocClient = new LocationClient(this);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);// 打开gps
            option.setIsNeedAddress(true);
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
        }
        listener = myListener;
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();
    }

    public void stopLocClient() {
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
            if (listener != null) {
                mLocClient.unRegisterLocationListener(listener);
            }
        }
    }

    @Override
    public void attachBaseContext(Context base) {
//        MultiDex.install(base);
        super.attachBaseContext(base);
    }

	/**
	 *
	 * 返回一个有效的xmpp连接,如果无效则返回空.
	 *
	 * @return
	 * @update 2012-7-4 下午6:54:31
	 */
	public XMPPConnection getConnection() {
		return connection;
	}

    public void openConnection(){
        Connection.DEBUG_ENABLED = true;
        ProviderManager pm = ProviderManager.getInstance();
        configure(pm);
        LoginConfig loginConfig=getLoginConfig(this);
        ConnectionConfiguration config = new ConnectionConfiguration(loginConfig.getXmppHost(), loginConfig.getXmppPort(),
                loginConfig.getXmppServiceName());
        config.setRosterLoadedAtLogin(true);
        config.setSASLAuthenticationEnabled(false);// 不使用SASL验证，设置为false
        config
                .setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        // 允许自动连接
        config.setReconnectionAllowed(true);
        // 允许登陆成功后更新在线状态
        config.setSendPresence(true);
        // 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        connection=new XMPPConnection(config);
    }

    public void configure(ProviderManager pm) {
// Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
        }

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());

        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());

        // Chat State
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());

        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());

        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());

        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());

        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }

        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());

        // Offline Message Indicator
        pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());

        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());

        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());

        // Privacy
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());

        pm.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
        pm.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
    }

    public static LoginConfig getLoginConfig(Context context) {
        LoginConfig loginConfig = new LoginConfig();
        SharedPreferences preferences= context.getSharedPreferences(XMPPUtils.LOGIN_SET, 0);
        loginConfig.setXmppHost(preferences.getString(XMPPUtils.XMPP_HOST,
                context.getResources().getString(R.string.xmpp_host)));
        loginConfig.setXmppPort(preferences.getInt(XMPPUtils.XMPP_PORT,
                context.getResources().getInteger(R.integer.xmpp_port)));
        loginConfig.setUsername(preferences.getString(XMPPUtils.USERNAME,
                null));
        loginConfig.setPassword(preferences.getString(XMPPUtils.PASSWORD,
                null));
        loginConfig.setXmppServiceName(preferences.getString(
                XMPPUtils.XMPP_SEIVICE_NAME,
                context.getResources().getString(R.string.xmpp_service_name)));
        loginConfig.setAutoLogin(preferences.getBoolean(
                XMPPUtils.IS_AUTOLOGIN,
                context.getResources().getBoolean(R.bool.is_autologin)));
        loginConfig.setNovisible(preferences.getBoolean(
                XMPPUtils.IS_NOVISIBLE,
                context.getResources().getBoolean(R.bool.is_novisible)));
        loginConfig.setRemember(preferences.getBoolean(
                XMPPUtils.IS_REMEMBER,
                context.getResources().getBoolean(R.bool.is_remember)));
        loginConfig.setFirstStart(preferences.getBoolean(
                XMPPUtils.IS_FIRSTSTART, true));
        return loginConfig;
    }

    public void setTypeList(List<ShootTypeBean> typeList) {
        mapTypeList.put("listType",typeList);
    }

    public List<ShootTypeBean> getTypeList() {
        return mapTypeList.get("listType");
    }

    public String getBgImageUrl(String shootTypeId){
        List<ShootTypeBean> typeList=getTypeList();
        String strUrl="";
        if (typeList!=null){
            if (typeList!=null){
                for(ShootTypeBean it:typeList){
                    if(shootTypeId.equals(it.getTypeId())){
                        strUrl=it.getNoticeListBg();
                    }
                }
            }
        }
        return strUrl;
    }

    /**
     * 根据大类的id获取名称
     * @param typeId 大类的id
     * @return 大类名称
     */
    public String getTypeNameByTypeId(int typeId){
        List<ShootTypeBean> typeList=getTypeList();
        if (typeList!=null){
            for (ShootTypeBean shootTypeBean : typeList){
                int tempTypeId = shootTypeBean.getTypeId();
                if (tempTypeId == typeId){
                    return shootTypeBean.getTypeName();
                }
            }
        }
        return null;
    }



    //*******************
    //****模特风格
    private static  List<ModelStyleBean> modelTypeList = new ArrayList<ModelStyleBean>();

    public static void setModelTypeList(List<ModelStyleBean> modelTypeList) {
        SGApplication.modelTypeList = modelTypeList;
    }

    public static  List<ModelStyleBean> getModelTypeList() {
        return modelTypeList;
    }

    /**
     * 获取男模特的风格
     * @return
     */
    public static List<ModelStyleBean> getMaleStyleList(){
        List<ModelStyleBean> maleTypeList = new ArrayList<>();
        for (ModelStyleBean styleBean : modelTypeList ) {
            if (styleBean.getExclusType() == 1 || styleBean.getExclusType() == 2){
                maleTypeList.add(styleBean);
            }
        }
        return  maleTypeList;

    }

    /**
     * 获取女模特风格
     * @return
     */
    public static List<ModelStyleBean> getFemaleStyleList(){
        List<ModelStyleBean> femaleTypeList = new ArrayList<>();
        for (ModelStyleBean styleBean : modelTypeList ) {
            if (styleBean.getExclusType() == 0 || styleBean.getExclusType() == 2){
                femaleTypeList.add(styleBean);
            }
        }
        return  femaleTypeList;
    }
    //**********
}

package com.xygame.sg.utils.ImageActivity;

public class Constants {

    public static String preDataBaseVersion = "1.0";

    public static String curDataBaseVersion = "1.1";
	
	public static final int VERSION_MODE_DEV = 1;
	
	public static final int VERSION_MODE_REL = 0;
	  
	public static final int VERSION_MODE_TEST = 2;
	
	public static final int VERSION_MODE_ALIYUN = 3;
	
	public static int VERSION_MODE = VERSION_MODE_ALIYUN;
	
	public static final String ACTIVE_EVALUATION = "active_evaluation";

	public static final int ACTIVE_EVALUATION_TYPE_ACTIVE = 1;

	public static final int ACTIVE_EVALUATION_TYPE_PERSONAL = 2;

	public static final String USER_DETAIL_INFO = "user_detail_info";

	public static final String SEARCH_CONTENT = "search content";

	public static final String SEARCH_RESULT = "search result";
	
	//userId,resourceId,compUserId,compType
	public static final String  ReportUserId="ReportUserId";
	
	public static final String  reportTypeId="reportTypeid";
	
	public static final String reportPath="reportPath";
	
	public static final String resourceId="resourceId";
	
	public static final String checkText="请检查网络连接";

	public static final String SEARCH_ACTION = "search action";

	public static final int has_concern = 1;

	public static final String HAS_CONCERN = String.valueOf(has_concern);

	public static final String NO_CONCERN = "0";

	public static final int PAGE_INDEX = 1;

	public static final int PAGE_SIZE = 15;
	
	public static final String PAGE_TITLE = "page_title";

	public static final String QUERY_NEED_EVN = "/action/queryActUserEvaludate";
	
	public static final String starNumber="starNumber";
	public static final String ALL_TYPE = "全部";

	public static final String OSS_ACTIVITY_PHOTO_DIR = "activity";

	public static final String OSS_GROUP_PHOTO_DIR = "group";

	public static final int DIAL_PAGE_SIZE = 20;

	public static final int MAX_LEN_ACTIVE_EVALUATION = 60;

	public static final int MAX_LEN_DIAL = 200;

	public static final int LAYOUTID = 0x123;

	public static final String EVENT_DETAIL = "new event detail";

	public static final int MAX_TALKING_MSG = 10;

	public static final int PHONE_TYPE = 1;

	public static final String ACTIONID = "actionId";

	public static final String ACTIONDETAIL = "action detail";
	public static final String GAME_DETAIL_URL = "game_detail_url";

	public static final String NICK_NAME_KEY = "nickname";
	public static final String USER_ICON = "use icon";
	public static final String USER_LEVEL = "use level";
	public static final String USER_BIRTH = "birthday";
	public static final String USER_GENDER = "gender";
	public static final String USER_HEAD = "imagePath";
	public static final String USER_CELL = "cellphone";
	public static final String USER_PWD = "password";
	public static final String USER_VC = "verifycode";
	public static final String USER_TOKEN = "userToken";
	public static final String IS_LOGGIN = "islogin";
	public static final String USER_TYPE = "userType";
	public static final String ACTIVE_CONDITION = "activecondition";
	public static final String SAVE_ACTIVE = "saveactivite";

	public static final int reqcode_address = 123;
	public static final int resCode_address = 234;
	public static final int reqcode_photo = 1345;
	public static final int resCode_photo = 345;
	public static final int reqcode_cropphoto = 1200;
	public static final int closeActivi=1232;
	
	public static final String FirstFlag = "firstFlag";

	public static final String BACK_TARGET = "back target";

	public static final String SHOW_MAP = "show map";

	public static final String MAP_TITLE = "map title";

	public static final String LAST_ADDR = "last address";

	public static final String bd_coord = "gcj02";

	public static final String pro = "province";

    public static final String cityId = "cityId";

	public static final String PROVINCENAME = "provincename";

	public static final String CITYNAME = "cityname";

	public static final String LOCATION_ADDR = "address";

	public static final String LOCATION_LAT = "lat";
	public static final String LOCATION_LNG = "lng";

	public static final String CONDITION_KEY = "search";

	public static final String REFRESH_PHOTO = "refresh photo";
	// public static final String BASE_IAMGE_URL =
	// "http://172.16.10.34:8080/com.xygame.sg/";
	// <<<<<<< .mine
	// public static final String SERVER_HOST = "192.168.1.5";
	// =======
	// // public static final String SERVER_HOST = "192.168.1.223";
	// >>>>>>> .r257

	public static final String port = "8088";
	
	public static final String relPort = "8088";
	
	public static final String relHost = "58.246.76.234";

	// public static final String BASE_URL = "http://" + SERVER_HOST + ":" +
	// port + "/sg-ws/base";
	// public static final String BASE_URL =
	// "http://140.207.100.222:8080/sg-ws/base";//外网IP
	// public static final String BASE_URL =
	// "http://172.16.10.200/sg-ws/base";//内网IP
	
	public static final String SERVER_HOST = "192.168.1.223";
	
	public static final String SERVER_HOST_TEST = "192.168.1.5";
	
	public static final String port_test = "8081";

	public static String OPENFIRE_SERVER_HOST = "192.168.1.223";

	public static final int OPENFIRE_PORT = 5226;

	// public static final String port = "8081";
	
	public static final String ALIYUN_APP_HOST = "open.madoufans.com";
	
	public static final int ALIYUN_APP_PORT = 8080;
	
	public static final String ALIYUN_OPENFIRE_HOST = "openfile.madoufans.com";
	
	public static final int ALIYUN_OPENFIRE_PORT = 5226;

	public static final String BASE_URL = getURL();//"http://" + SERVER_HOST + ":" + port + "/sg-ws/base";
	
	public static String getURL(){
		StringBuffer sbUrl = new StringBuffer("http://");
		switch(VERSION_MODE){
		case VERSION_MODE_DEV:
			sbUrl.append(SERVER_HOST + ":" + port + "/sg-ws/base");
			break;
		case VERSION_MODE_REL:
			sbUrl.append(relHost + ":" + relPort + "/sg-ws/base");
			OPENFIRE_SERVER_HOST = relHost;
			break;
		case VERSION_MODE_TEST:
			sbUrl.append(SERVER_HOST_TEST + ":" + port_test + "/sg-ws/base");
//			OPENFIRE_SERVER_HOST = ALIYUN_OPENFIRE_HOST;
			break;
		case VERSION_MODE_ALIYUN:
			sbUrl.append(ALIYUN_APP_HOST + ":" + ALIYUN_APP_PORT + "/sg-ws/base");
			OPENFIRE_SERVER_HOST = ALIYUN_OPENFIRE_HOST;
			break;
		}
		return sbUrl.toString();
	}
	// public static final String BASE_URL =
	// "http://140.207.100.222:8080/sg-ws/base";//外网IP
	// public static final String BASE_URL =
	// "http://172.16.10.200/sg-ws/base";//内网IP
	// >>>>>>> .r250
	public static final String MMS_BASE_URL = BASE_URL;
	public static final String VERIFY_PHONE = "/user/verifyPhone";
	public static final String CHECK_VC = "/user/verifyPhoneCode";
	public static final String RESET_PWD = "/user/resetPassword";
	public static final String USER_LOGIN = "/user/login";
	public static final String REGIST_USER = "/user/register";
	public static final String UPLOAD_LOCATION = "/user/reportCoord";
	public static final String NEARBY_USER = "/user/queryNearbyUser";
	public static final String HOT_USER = "/user/queryHotestUser";
	public static final String GET_USER_INFO = "/user/queryUserDetail";
	public static final String ACTION_INFO = "/action/echoAction";
	public static final String MODIFY_ACTION = "/action/modifyAction";
	public static final String REPLY_MOOD = "/mood/replyMood";
	public static final String QUERY_NO = "/mood/queryMyMessageCount";
	public static final String QUERY_RELATIONSHIP = "/contacts/queryMySocialPreview";
	public static final String QUERY_GROUP = "/group/queryMyGroupPreview";

	public static final String QUERY_GAME_LIST = "/game/inflateGameCenter";

	public static final String REPLY_CONCERN = "/mood/commentMood";

	public static final String REPORT_ACTION = "/action/complaintAction";
	
	public static final String ComplaintGroup="/group/complaintGroup";

	public static final String GET_VERIFY_CODE = "/user/sendVerifyCode";// 获取验证码

	public static final String CREATE_ACTIVE = "/action/publishAction";
	public static final String ACTIVE_LIST = "/action/queryNearbyAction";
	public static final String GET_ACTIVE_TYPE = "/action/queryActionType";
	public static final String GET_ACT_COMPLAIN = "/sys/queryComplaintType";//举报类型

	public static final String GET_GroupDetailActivityType = "/group/queryGroupComplaintType";
	
	public static final String ACTIVE_INFO = "/action/queryActionDetail";
	public static final String ACTION_REVIEW = "/action/queryApplyJoinActUser";
	public static final String ACTION_ADJUST = "/action/auditActJoinMember";

	public static final String DELETE_MOOD = "/mood/deleteMood";

	public static final String CONVER_ACTION = "/action/applyJoinAction";
	public static final String CANCLE_ACTION = "/action/cancelJoinAction";

	public static final String ATTEND_LIST = "/action/queryJoinedActUser";

	public static final String QUERY_UserCreateAction = "/action/queryUserCreateAction";// 查询用户创建的活动

	public static final String QUERY_MYACTIVE = "/action/queryMyAction";// 我的活动

	public static final String QUERY_UserAllAction = "/action/queryUserAllActionBrief";// 查询用户所有活动信息（参加和创建）

	public static final String QUERY_APPLYACTIVE = "/action/queryUserApplyAction";// 查询已报名的活动

	public static final String QUERY_JOINACTIVE = "/action/queryUserJoinAction";// 查询已通过的活动

	public static final String QUERY_ACTMESSAGE = "/action/queryActMessage";// 查询活动留言

	public static final String QUERY_ACTCOMENT = "/action/queryActComment";// 查询活动评论

	public static final String PUBLISH_COMMENT = "/action/publishComment";// 发表评论

	public static final String REPLY_COMMENT = "/action/replyComment";// 回复评论

	public static final String LEAVE_MESSAGE = "/action/leaveMessage";// 留言

	public static final String REPLY_MESSAGE = "/action/replyMessage";// 回复留言

	public static final String EVALUATION_ACTIVE = "/action/evaluateAction";// 活动评价

	public static final String NEW_EVENT_PUBLISH = "/mood/publishMood";// 发布新鲜事

	public static final String NEW_EVENT_MESSAGELIST = "/mood/queryMyMessage";// 查询我的消息列表
	public static final String MY_ACTIVE = "30004";

	public static final int TIME_SPACING = 60;

	public static final String HTTP_RESULT_OK = "0";
	public static final String HTTP_CODE = "0000";
	

	public static final String BROADCASE_ACTION = "com.xygame.sg.locationchange";

	public static final String GROUP_ID = "groupId";
	
	public static final String USER_ID = "userId";
	public static final String ACTIVI_ID = "activiId";
	public static final String expiryTime="expiryTime";
	public static final String applyTime="applyTime";
	public static final String holdCode="holdCode";
	//个人资料界面的用户昵称非自己的昵称
	public static final String USER_NICENAME="userNeceName";

	public static final String USER_INFO = "user info";

	public static final String VERSION = "1.0";

	public static final String USER_SEX = "userSex";

	public static final String ACTIVE_TYPE = "actionType";
	
	public static final String Banner="actionBanner";

	public static final String REPORT_TYPE = "reportType";
	
	public static final String Activity_TYPE="Activity_TYPE";

    public static final String USER_INDUSTRY = "user_industry";

	public static final int GENDER_FEMALE = 0;

	public static final int GENDER_MAN = 1;

	public static final int USER_TYPE_NORMAL =1;

	public static final int USER_TYPE_SG = 2;

	public static final int USER_TYPE_SB = 3;

	public static final int USER_TYPE_COMPNAY = 4;

	public static final int USER_TYPE_BUSINESS = 5;

	public static final int USER_TYPE_PHOTOR = 6;

	public static final String ACTION_ID = "action_id";// 活动ID

	public static final String ACTION_STATUS = "action_status";// 活动状态

	public static final String DRAFT_ACTION = "draft action";// 草稿活动

	public static final String ACTION_IMAGE = "action image";// 活动图片

	public static final String PIC_PATH = "sgshow/cache/";

	public static final String QUERY_NEARBY_MOOD = "/mood/queryNearbyMood";
	public static final String OSS_NEWS_PHOTO_DIR = "news";
	public static final String OSS_report_PHOTO_DIR = "complaints";
	public static final String OSS_USER_PHOTOS="photoalbum";
	public static final String OSS_HEADER_DIR = "avatar";
	public static final String OSS_AUTH_DIR = "certification";
	public static final String QUERY_ATTEND_MOOD = "/mood/queryMyAttentedUserMood";
	public static final String DELETE_COMMENT = "/mood/deleteComment";
	public static final String QUERY_MOOD_DETAIL = "/mood/queryMoodDetail";
	public static final String DEL_BROWSED_MESSAGE = "/mood/delBrowsedMessage";
	public static final String QUERY_COMMENT = "/mood/queryComment";

	public static final String MOOD_ID = "mood id";
	public static final String MOOD_PICTURE = "moodPictures";
	public static final String POSITION = "position";
	public static final String POSITIONCODE = "positionCode";
    public static final String fansCount="fansCount";

	public static final int MOOD_MAX_SIZE = 10;
	public static final int MOOD_FIRST_PAGE = 1;

	public static final String MSG_BROADCAST = "msg.broadcast.notify";
	
	public static final String ShareContent="我正在使用【模范儿】～这里人人可以成为模特，人人可以找到模特"+Constants.ShareWebsite;
	
	public static final String ShareWebsite="http://www.xygame.com";
	
    public static final String approveText="认证失败";
    
    public static final String  echoUserAuth= "/auth/echoUserAuth";
    
    public static final String MESSAGE_COUNT_CHANGE = "message count_change";
    
    public static final String CONFLICT_TIME = "conflict_time";
    
    public static final String queryGroupMessage="/group/queryGroupMessage";
    
    public static final String publishGroupMessage="/group/publishGroupMessage";
    
    public static final String commentGroupMessage="/group/commentGroupMessage";
    
    public static final String replyGroupMessage="/group/replyGroupMessage";
    
    public static final String deleteMessageComment="/group/deleteMessageComment";
    
    public static final String deleteGroupMessage="/group/deleteGroupMessage";
    
    public static final String queryGroupMessageComment="/group/queryGroupMessageComment";
    //首页和引导页的数据接口
    public static final String querySysPic="/sys/querySysPic";
    //首页广告轮播接口
    public static final String queryHomeTopBanner="/sys/queryHomeTopBanner";
    //首页界面数据接口
    public static final String queryHomeHotAction ="/sys/queryHomeHotAction";

    public static final String queryIndustry ="/sys/queryUserIndustry";
	
	public static final String NO_LIMITED = "不限";

    public static final int defValue = -1;

    public static final String rangeSplit = "~";

    public static final String dashSplit = "-";

    public static final String USER_WALL_PHOTOS = "user wall photos";

    public static final String RES_ID = "res id";

    public static final String RES_URL = "res url";

    public static final int _1K = 1000;

    public static final String PHOTO_CDN = "http://res.madoufans.com/";

    public static final String PHOTO_RESOURCE = "http://app-sgshow.oss-cn-hangzhou.aliyuncs.com";

    public static final String CDN_STYLE_240 = "@1e_240w_240h_1c_0i_1o_80Q_1x.jpg";//"@0e_240w_240h_1c_1i_1o_100Q_1x.jpg";

    public static final String SEARCH_COND = "search cond";

    public static final String SEARCH_COUNT = "search cond count";

	public static final String SEARCH_KEYWORD = "search key word";

    public static final int REQ_NUMBER = 0x19;

    public static final int REQ_INDUSTRY = 0x20;

	public static final int REQ_RESUIT = 0x21;

    public static final String COMMENT_TOTAL_NUMBER = "comment total count";
    //相册举报的接口
    public static final String complaintGallery ="/me/complaintGallery";
    //举报用户
    public static final String  complaintUser="/me/complaintUser";
    //举报活动图片
    public static final String   complaintActivityPic= "/activity/complaintActivityPic";
    public static final String ReportText="请求中...";
    //取消关注提示语
    public static final String  AttentionHint="确定取消关注吗？";
    //活动关注提示语
    public static final  String ActiviAttenton="在报名参加对方活动前，必须先关注该用户"; 
    public static final  String ActiviApplyText="确定报名该活动？";
    public static final  String CloseActivi="是否关闭当前活动？";
    //红人界面回调code
    public static final int HotRestCode=200;
    //
    public static final String RestData="RestData";

	public static final int REQ_SEARCH_COND = 0x21;

    public static final String BUNDLE="Bundle";
    public static final String INTENT ="intent";
    public static final String ACTID="actid";
    
    public static final int RESULT=100;

	public static final String Active_RESCRUIT = "active rescruit";

	public static final int REQ_RESTAURAUNT = 0x22;

	public static final int REQ_ABOUTLOCATION = 0x23;

	//聊天界面跳转类型（这里指的是跳转过来怎么显示）
	public static final String GotoType="GotoType";
	//活动跳转到聊天界面的类型
	public static final String ActiviType="ActiviType";
	//活动创建者的对象data 
	public static final String ActiviCreate="ActiviCreate";
	//活动成员的对象data 
    public static final String ActiviMember="ActiviMembers";
    
    public static final String ApplyStatus="ApplyStatus";

	public static final int REQ_ACTIVEFOOD = 0x24;

	public static final String POI_NAME = "poi name";

	public static final String POI_ADDR = "poi address";

	public static final int REQ_ACTIVEREPEAT = 0x25;

	public static final int REQ_MOVIE = 0x26;

	public static final int REQ_CINEMA = 0x27;

	public static final String ACTIVE_STARTTIME = "active start time";

	public static final String ACTIVE_REPEAT = "active repeat cycle";

	public static final String ACTIVE_FOOD_TAGS = "active food tags";
	
	public static final String FILM_NAME = "moviename";



}

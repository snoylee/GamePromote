package com.xygame.sg.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;

import java.io.File;

/**
 * Created by tony on 2016/3/10.
 * 此类用来定义各种接口及回调反回标识
 */
public class ConstTaskTag {

    //通告群
    public static final String GROUP_TG_TEXT = "1001";//文本
    public static final String GROUP_TG_IMAGE = "1002";//图片
    public static final String GROUP_TG_VOICE = "1003";//语音
    public static final String GROUP_TG_POSION = "1004";//位置
    public static final String GROUP_TG_ACT = "1005";//通告
    public static final String GROUP_TG_VIDEO = "1006";//视频

    //讨论组
    public static final String GROUP_DISC_TEXT = "2001";//文本
    public static final String GROUP_DISC_IMAGE = "2002";//图片
    public static final String GROUP_DISC_VOICE = "2003";//语音
    public static final String GROUP_DISC_POSION = "2004";//位置
    public static final String GROUP_DISC_VIDEO = "2006";//视频
    public static final String GROUP_DISC_NAME = "2201";//修改组名称
    public static final String GROUP_DISC_FRIENDJOIN = "2202";//邀请好友加入
    public static final String GROUP_DISC_LOSE = "2203";//群主解散群


    //整数金额
    public static String getIntPrice(String price){
        String result="0";
        if (!TextUtils.isEmpty(price)){
            result=String.valueOf(Integer.parseInt(price)/100);
        }
        return result;
    }

    //精度金额
    public static double getDoublePrice(String price){
        double result=0;
        if (!TextUtils.isEmpty(price)){
            result=Long.parseLong(price)/100.0;
        }
        return result;
    }

    //图片保存sdk路径
    public static String getSDCardPath(){
        String finalStr=null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist){
            File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            finalStr=sdDir.toString().concat("/sg_images/");
            File finaldir=new File(finalStr);
            if (!finaldir.exists()){
                finaldir.mkdir();
            }
        }
        return finalStr;
    }

    //自定义图片存储路径
    public static String imageCacher(){

        File cacheDir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&&!Environment.isExternalStorageRemovable()) {
            cacheDir = SGApplication.getInstance().getExternalCacheDir();
            if(cacheDir == null){
                cacheDir = SGApplication.getInstance().getCacheDir();
            }
        }else {
            cacheDir = SGApplication.getInstance().getCacheDir();
        }
        String imageCacher = cacheDir.getAbsolutePath() + File.separator + "cacheImage" + File.separator;
        File f = new File(imageCacher);
        if (!f.exists()) {
            f.mkdir();
        }
        return imageCacher;
    }

    //缓存Key值
    public static final String CACHE_ACT_TYPE = "cacheActTypes";
    public static final String CACHE_GIFT_DATAS = "cacheGiftDatas";
    public static final String CACHE_PRICE_STEP = "cachePriceStepDatas";
    public static final String CACHE_JP_LOWER_PRICE = "cacheJpLowerPriceDatas";
    public static final String CACHE_NOTICE_STATUS= "cachenoticeStatusManagerBean";
    public static final String CACHE_GROUP_ROOM_DATAS= "cacheGroupRoomDatas";
    public static final String CACHE_BLACK_LIST_DATAS= "cacheBlackListDatas";
    public static final String CACHE_USER_STATUS= "cacheUserStatus";
    public static final String CACHE_QUPAI_TOKEN= "cacheQuPaiToken";
    public static final String CACHE_USER_ID= "cacheUserId";
    public static final String CACHE_SERVICE_BEAN= "cacheServiceBean";
    public static final String CACHE_GROUP_NOTICE_TIP= "cacheGroupNoticeTip";

    public static final String RECORD_VIDEO_APP_KEY= "20bc294c4218982";
    public static final String RECORD_VIDEO_APP_SECRET= "b5d5497e4f2c40d68b39adeab8393488";
    public static final String RECORD_VIDEO_NAME_SPACE= "more123";

    public static final String LOVE_OPTION[] = {"单身","恋爱中","已婚","分居","离异"};
    public static final String SALARY_ARRAY[] = {"保密","5000元/月以下","5000-10000元/月","10000-20000元/月","20000元/月以上"};

    public static boolean isTrueForArrayObj(String arrayString){
        return !TextUtils.isEmpty(arrayString) && !"[]".equals(arrayString) && !"null".equals(arrayString) && !"[null]".equals(arrayString);
    }

    public static String[] getHeightArray(){
        int max=220;
        int min=140;
        String[] tempData=new String[max-min+1];
        for (int i=min;i<=max;i++){
            tempData[i-min]=String.valueOf(i);
        }
        return tempData;
    }

    public static String[] getWeightArray(){
        int max=100;
        int min=35;
        String[] tempData=new String[max-min+1];
        for (int i=min;i<=max;i++){
            tempData[i-min]=String.valueOf(i);
        }
        return tempData;
    }

    //回收位置
    public static final int QUERY_LOAD_RZSP = 9949;
    public static final int QUERY_BAIDU_LOCATION = 9980;
    public static final int QUERY_JUST_GROUP_ALL = 9953;



    public static String baseUrl = Constants.baseUrl;

    //请求接口
    // 阿里云密钥信息接口
    public static String REQUEST_ALIY_ADRESS=baseUrl + "/sys/queryAliOSSKey";


    //回调标识
    public static final int CONST_TAG_PUBLIC = 0;//共用标识
    public static final int CONST_TAG_TEST =1;//测试标识


    //查询热门摄影师列表
    public static final String QUERY_HOTPHOTOGRAPHERS_URL=baseUrl + "/home/mplist/queryHotPhotographers";
    public static final int QUERY_HOTPHOTOGRAPHERS_INT = 1000;
    //查询全部摄影师列表
    public static final String QUERY_ALLPHOTOGRAPHERS_URL=baseUrl + "/home/mplist/queryAllPhotographers";
    public static final int QUERY_ALLPHOTOGRAPHERS_INT = 1001;
    //搜索模特摄影师信息
    public static final String SEARCH_MODEL_PHOTOPGS_URL=baseUrl + "/home/rank/searchUsers";
    public static final int SEARCH_MODEL_PHOTOPGS_INT = 1002;
    //获取模特风格
    public static final String QUERY_MODEL_STYLE_TYPE_URL=baseUrl + "/sys/queryModelStyleType";
    public static final int QUERY_MODEL_STYLE_TYPE_INT = 1003;
    //热门模特上方的轮播
    public static final String LOAD_BANNER_INFO_URL=baseUrl + "/home/loadBannerInfo";
    public static final int LOAD_BANNER_INFO_INT = 1004;
    //获取热门模特
    public static final String LOAD_HOT_MODEL_INFO_URL=baseUrl + "/home/rank/queryFansRank";
    public static final int LOAD_HOT_MODEL_INFO_INT = 1005;
    //获取热门模特
    public static final String LOAD_ALL_MODEL_INFO_URL=baseUrl + "/home/mplist/queryAllModels";
    public static final int LOAD_ALL_MODEL_INFO_INT = 1006;
    //拍摄大类
    public static final String QUERY_MODEL_SHOOT_TYPE_URL=baseUrl + "/sys/queryModelShootType";
    public static final int QUERY_MODEL_SHOOT_TYPE_INT = 1007;
    // 查看通告列表
    public static final String QUERY_NOTICES_URL=baseUrl + "/notice/pl/queryNotices";
    public static final int QUERY_NOTICES_INT = 1008;
    // 添加或取消关注
    public static final String EDIT_ATTENTION_URL=baseUrl + "/model/editAttention";
    public static final int EDIT_ATTENTION_INT = 1010;
    // 查询模特作品集
    public static final String QUERY_MODELGALLERY_URL=baseUrl + "/me/queryModelGallery";
    public static final int QUERY_MODELGALLERY_INT = 1011;
    // 模特资料(比较全)
    public static final String ECHO_MODELINFO_URL=baseUrl + "/me/echoModelInfo";
    public static final int  ECHO_MODELINFO_INT = 1012;
    // 用户通告
    public static final String QUERY_USERNOTICES_URL=baseUrl + "/notice/pl/queryUserNotices";
    public static final int  QUERY_USERNOTICES_INT = 1013;
    // 报价
    public static final String ECHO_MODELPRICE_URL=baseUrl + "/me/echoModelPrice";
    public static final int  ECHO_MODELPRICE_INT = 1014;

    //+++++++++++++++++++周鹏磊为以上接口，王琪为以下接口+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //查询用户认证私密图片的密钥
    public static final String QUEST_ALIY_UATH_KEY=baseUrl + "/sys/queryAuthPicKey";
    public static final int RESPOSE_ALIY_UATH_KEY = 9999;

    //阿里云密钥信息接口
    public static final String QUEST_ALIY_PARAMS=baseUrl + "/sys/queryAliOSSKey";
    public static final int RESPOSE_ALIY_PARAMS = 9998;

    //查询系统客服电话
    public static final String QUEST_SERVICE_PHONE=baseUrl + "/sys/queryFeedbackServicePhone";
    public static final int RESPOSE_SERVICE_PHONE = 9997;

    //查询用户职业类型
    public static final String QUEST_OCCUP_TYPE=baseUrl + "/sys/queryUserOccupType";
    public static final int RESPOSE_OCCUP_TYPE = 9996;

    //上报用户推送的设备码上报用户推送的设备码
    public static final String QUEST_DEVICE_TOKEN=baseUrl + "/user/bindPushDeviceToken";
    public static final int RESPOSE_DEVICE_TOKEN = 9995;

    //查看模特基本详情
    public static final String QUEST_MODEL_INFO=baseUrl + "/model/modelData";
    public static final int RESPOSE_MODEL_INFO = 9994;

    //上报用户推送的设备码上报用户推送的设备码
    public static final String QUEST_MODEL_GALLERY_INFO=baseUrl + "/model/modelDataGalleryInfo";
    public static final int RESPOSE_MODEL_GALLERY_INFO = 9993;

    //获取注册验证码
    public static final String QUEST_VERFY_COD=baseUrl + "/user/sendRegVerifyCode";
    public static final int RESPOSE_VERFY_COD = 9992;

    //商家注册接口
    public static final String QUEST_MERCHANT=baseUrl + "/user/registerCompany";
    public static final int RESPOSE_MERCHANT_COD = 9991;

    //验证码校验接口
    public static final String QUEST_CHECK_VERFIY=baseUrl + "/user/verifyPhoneCode";
    public static final int RESPOSE_CHECK_VERFIY_COD = 9990;

    //动态消息，未读消息
    public static final String QUEST_UNREAD_MSG=baseUrl + "/push/findLatestMessage";
    public static final int RESPOSE_UNREAD_MSG_COD = 9989;
    public static final int RESPOSE_UNREAD_MSG_COD_ = 9988;

    //Java和Js互调接口
    public static final int RESPOSE_JS_COD = 9987;

    //操作模特拍摄类型（增加，删除）
    public static final String QUEST_MODEL_TYPE=baseUrl + "/me/cudModelShootType";
    public static final int RESPOSE_MODEL_TYPE_COD = 9986;

    //上传头像视频
    public static final String QUEST_HEAD_VIDEO=baseUrl + "/me/editUserLogoVideo";
    public static final int RESPOSE_HEAD_VIDEO_COD = 9985;

    //经纪人认证
    public static final String QUEST_JJR_VERIFY=baseUrl + "/auth/authBroker";
    public static final int RESPOSE_JJR_VERIFY = 9984;

    //经纪人发布通告
    public static final String QUEST_JJR_PLUSH_NOTICE=baseUrl + "/broker/notice/publishBrokerNotice";
    public static final int RESPOSE_JJR_PLUSH_NOTICE = 9983;

    // 查看经纪人通告列表
    public static final String QUEST_JJR_NOTICE_LIST=baseUrl + "/broker/notice/queryBrokerNotices";
    public static final int QUERY_JJR_NOTICE_LIST = 9982;

    // 查看经纪人通告所搜列表
    public static final String QUEST_JJR_NOTICE_SEARCH_LIST=baseUrl + "/broker/notice/searchBrokerNotice";
    public static final int QUERY_JJR_NOTICE_SEARCH_LIST = 9981;

    // 经纪人通告详情
    public static final String QUEST_JJR_NOTICE_DETAIL=baseUrl + "/broker/notice/queryBrokerNoticeDetail";
    public static final int RESPOSE_JJR_NOTICE_DETAIL = 9979;

    // 经纪人通告详情报名接口
    public static final String QUEST_JJR_NOTICE_REPORT=baseUrl + "/broker/notice/applyNotice";
    public static final int RESPOSE_JJR_NOTICE_REPORT = 9978;

    // 经纪人关闭通告接口
    public static final String QUEST_JJR_NOTICE_CLOSE=baseUrl + "/broker/notice/closeNotice";
    public static final int RESPOSE_JJR_NOTICE_CLOSE = 9977;

    // 经纪人通告报名模特列表接口
    public static final String QUEST_JJR_NOTICE_MODELS=baseUrl + "/broker/notice/queryBrokerNoticeMembers";
    public static final int RESPOSE_JJR_NOTICE_MODELS = 9976;

    // 经纪人通告管理列表接口
    public static final String QUEST_JJR_NOTICE_MANAGE=baseUrl + "/broker/notice/queryBrokerManagerNotices";
    public static final int RESPOSE_JJR_NOTICE_MANAGE = 9975;

    // 经纪人通告管理列表接口
    public static final String QUEST_JJR_MODEL_MANAGE=baseUrl + "/broker/notice/queryModelManagerBrokerNotices";
    public static final int RESPOSE_JJR_MODEL_MANAGE = 9974;

    // 查看经纪人通告列表
    public static final String QUEST_JJR_DETAIL_NOTICE_LIST=baseUrl + "/broker/notice/queryBrokerDetailNotices";
    public static final int QUERY_JJR_DETAIL_NOTICE_LIST = 9981;

    // 查询模特粉丝榜
    public static final String QUEST_MODEL_FANS=baseUrl + "/home/rank/queryGoddessRank";
    public static final int QUERY_MODEL_FANS = 9980;

    // 查询模特推荐榜
    public static final String QUEST_MODEL_RECOMMEND=baseUrl + "/home/rank/querySpenderRank";
    public static final int QUERY_MODEL_RECOMMEND = 9979;

    // 查询系统banner
    public static final String QUEST_MODEL_BANNER=baseUrl + "/sys/querySystemBanner";
    public static final int QUERY_MODEL_BANNER = 9978;

    // 发布动态（朋友圈）
    public static final String QUEST_CIRCLE_PLUSH=baseUrl + "/mood/publishMood";
    public static final int QUERY_CIRCLE_PLUSH = 9977;

    // 发布动态（朋友圈）
    public static final String QUEST_CIRCLE_DATAS=baseUrl + "/mood/queryHomeMoods";
    public static final int QUERY_CIRCLE_DATAS = 9976;

    // 朋友圈点赞
    public static final String QUEST_CIRCLE_PRAISE=baseUrl + "/mood/praiseMood";
    public static final int QUERY_CIRCLE_PRAISE = 9975;
    public static final int QUERY_CIRCLE_PRAISE_ = 9972;

    // 查询动态回复的信息（点赞和评论），详情页面调用
    public static final String QUEST_CIRCLE_COMMENT_DETAIL=baseUrl + "/mood/queryMoodReply";
    public static final int QUERY_CIRCLE_COMMENT_DETAIL = 9974;

    // 查询动态回复的信息（评论），详情页面调用
    public static final String QUEST_CIRCLE_COMMENT=baseUrl + "/mood/commentMood";
    public static final int QUERY_CIRCLE_COMMENT = 9973;

    // 查询动态评论
    public static final String QUEST_CIRCLE_COMMENT_LIST=baseUrl + "/mood/queryMoodComments";
    public static final int QUERY_CIRCLE_COMMENT_LIST = 9971;

    // 查询钻石数
    public static final String QUEST_STONE_NUMS=baseUrl + "/vasset/queryMyWallet";
    public static final int QUERY_STONE_NUMS = 9970;

    // 钻石购买列表
    public static final String QUEST_STONE_LISTS=baseUrl + "/sys/queryRechargeableAmount";
    public static final int QUERY_STONE_LISTS = 9969;

    // 钻石提现查询
    public static final String QUEST_STONE_GET_MONEY_CHECK=baseUrl + "/vasset/queryWithdrawableAmount";
    public static final int QUERY_STONE_GET_MONEY_CHECK = 9968;

    // 提现
    public static final String QUEST_STONE_DrawCash=baseUrl + "/vasset/withdrawCash";
    public static final int QUERY_STONE_DrawCash = 9967;

    // 提现
    public static final String QUEST_STONE_DETAIL=baseUrl + "/vasset/queryUserAssetFlow";
    public static final int QUERY_STONE_DETAIL = 9966;

    // 猎取礼物
    public static final String QUEST_GIFTS=baseUrl + "/vasset/queryGifts";
    public static final int QUERY_GIFTS = 9965;

    // 打赏礼物
    public static final String QUEST_SEND_GIFTS=baseUrl + "/mood/awardGift";
    public static final int QUERY_SEND_GIFTS = 9964;

    // 上传认证视频
    public static final String QUEST_CERTIFY_VIDEO=baseUrl + "/auth/applyVideoAuth";
    public static final int QUERY_CERTIFY_VIDEO = 9963;

    // 上传认证身份证
    public static final String QUEST_CERTIFY_ID=baseUrl + "/auth/applyIdentityAuth";
    public static final int QUERY_CERTIFY_ID = 9962;

    // 登录
    public static final String QUEST_USER_LOGIN=baseUrl + "/user/login";
    public static final int QUERY_USER_LOGIN = 9961;

    // 获取忘记密码的验证码
    public static final String QUEST_GET_FORGET_VERFIY=baseUrl + "/user/sendResetVerifyCode";
    public static final int QUERY_GET_FORGET_VERFIY = 9959;

    // 校验忘记密码的验证码
    public static final String QUEST_CHECK_FORGET_VERFIY=baseUrl + "/user/verifyPhoneCode";
    public static final int QUERY_CHECK_FORGET_VERFIY = 9958;

    // 提交新密码
    public static final String QUEST_RESET_PWD=baseUrl + "/user/resetPassword";
    public static final int QUERY_RESET_PWD = 9957;

    // 获取忘记密码的验证码
    public static final String QUEST_GET_REGISTER_VERFIY=baseUrl + "/user/sendRegVerifyCode";
    public static final int QUERY_GET_REGISTER_VERFIY = 9956;

    // 获取忘记密码的验证码
    public static final String QUEST_REGISTER=baseUrl + "/user/register";
    public static final int QUERY_REGISTER = 9955;

    // 设置密码
    public static final String QUEST_SET_PWD=baseUrl + "/vasset/setVassetPassword";
    public static final int QUERY_SET_PWD = 9952;

    // 绑定支付宝
    public static final String QUEST_SET_ZFB=baseUrl + "/vasset/bindReceiptAccount";
    public static final int QUERY_SET_ZFB = 9951;

    // 解绑定支付宝
    public static final String QUEST_LOSE_ZFB=baseUrl + "/vasset/unbindReceiptAccount";
    public static final int QUERY_LOSE_ZFB = 9950;

    // 查看模特基本详情
    public static final String QUERY_MYOVERVIEW_URL=baseUrl + "/me/queryMyOverview";
    public static final int QUERY_MYOVERVIEW_INT = 1009;

    // 查看模特基本详情
    public static final String QUEST_ALL_MODEL=baseUrl + "/home/rank/queryAllUserRank";
    public static final int QUERY_ALL_MODEL = 9949;

    // 查看模特基本详情
    public static final String QUEST_ACT_TYPE=baseUrl + "/sys/queryActionType";
    public static final int QUERY_ACT_TYPE = 9948;
    public static final int QUERY_ACT_TYPE_ = 9947;

    //竟拍约的活动发布
    public static final String QUEST_ACT_PLUSH=baseUrl + "/act/auction/publishAuctionAction";
    public static final int QUERY_ACT_PLUSH = 9946;

    //竟拍约的活动列表
    public static final String QUEST_ACT_LIST=baseUrl + "/act/auction/queryAuctionActions";
    public static final int QUERY_ACT_LIST = 9945;

    //竟拍约的活动详情
    public static final String QUEST_ACT_DETAIL=baseUrl + "/act/auction/queryAuctionActionDetail";
    public static final int QUERY_ACT_DETAIL = 99454;

    //竟拍当前出价信息
    public static final String QUEST_ACT_CURR_BID=baseUrl + "/act/auction/queryCurrBidWinner";
    public static final int QUERY_ACT_CURR_BID = 9943;

    //竟拍当前出价
    public static final String QUEST_ACT_CURR_BIDER=baseUrl + "/act/auction/bid";
    public static final int QUERY_ACT_CURR_BIDER = 9942;

    //约拍的活动发布
    public static final String QUEST_ACT_PLUSH_=baseUrl + "/act/free/publishFreeAction";
    public static final int QUERY_ACT_PLUSH_ = 9941;

    //约拍的活动列表
    public static final String QUEST_FREE_ACT_LIST=baseUrl + "/act/free/queryFreeActions";
    public static final int QUERY_FREE_ACT_LIST = 9940;

    //约拍的活动详情
    public static final String QUEST_FREE_ACT_DETAIL=baseUrl + "/act/free/queryFreeActionDetail";
    public static final int QUERY_FREE_ACT_DETAIL = 9939;

    //约拍的活动详情
    public static final String QUEST_FREE_ACT_APPLY=baseUrl + "/act/free/apply";
    public static final int QUERY_FREE_ACT_APPLY = 9938;

    //获取礼物列表
    public static final String QUEST_GIFT=baseUrl + "/sys/querySysGifts";
    public static final int QUERY_GIFT = 9937;

    //获取当前余额
    public static final String QUEST_LEAVE_STONES=baseUrl + "/vasset/queryUserBalance";
    public static final int QUERY_LEAVE_STONES = 9936;

    //获取当前余额
    public static final String QUEST_SEND_GIFT=baseUrl + "/act/free/giveGift";
    public static final int QUERY_SEND_GIFT = 9935;

    //所有送礼物人列表
    public static final String QUEST_ALLGIFT_SENDER=baseUrl + "/act/free/queryActionPartDynamics";
    public static final int QUERY_ALLGIFT_SENDER = 9934;

    //所有送礼排行榜
    public static final String QUEST_ODER_SENDER=baseUrl + "/act/free/queryActionGiftRank";
    public static final int QUERY_ODER_SENDER = 9933;

    //所有送礼排行榜
    public static final String QUEST_FREE_LQ=baseUrl + "/act/free/hireMember";
    public static final int QUERY_FREE_LQ = 9932;
    public static final int QUERY_FREE_LQ1 = 9931;
    public static final int QUERY_FREE_LQ2 = 9930;

    //关闭活动
    public static final String QUEST_CLOSE_ACT=baseUrl + "/act/free/closeAction";
    public static final int QUERY_CLOSE_ACT = 9929;

    //查询竞拍活动底价
    public static final String QUEST_ACT_MIN_PRICE=baseUrl + "/act/auction/queryAuctionBasePrice";
    public static final int QUERY_ACT_MIN_PRICE = 9928;

    //查询竞拍幅度
    public static final String QUEST_PRICE_STEP=baseUrl + "/act/auction/queryAuctionPriceStep";
    public static final int QUERY_PRICE_STEP = 9927;

    //查询竞拍成员列表
    public static final String QUEST_JINPAI_LIST=baseUrl + "/act/auction/queryActionBidFlows";
    public static final int QUERY_JINPAI_LIST = 9926;

    //竞拍关闭
    public static final String QUEST_JINPAI_CLOSE=baseUrl + "/act/auction/closeAction";
    public static final int QUERY_JINPAI_CLOSE = 9925;

    //付费约的活动发布
    public static final String QUEST_ACT_FUFEI=baseUrl + "/act/normal/publishNormalAction";
    public static final int QUERY_ACT_FUFEI = 9924;

    //付费约的活动详情
    public static final String QUEST_ACT_FUFEI_DETAIL=baseUrl + "/act/normal/queryNormalActionDetail";
    public static final int QUERY_ACT_FUFEI_DETAIL = 99423;

    //付费约的活动报名
    public static final String QUEST_ACT_FUFEI_APPLY=baseUrl + "/act/normal/apply";
    public static final int QUERY_ACT_FUFEI_APPLY = 99422;

    //付费约的活动关闭
    public static final String QUEST_ACT_FUFEI_CLOSE=baseUrl + "/act/normal/closeAction";
    public static final int QUERY_ACT_FUFEI_CLOSE = 99421;

    //活动管理
    public static final String QUEST_ACT_MANAGER=baseUrl + "/me/queryUserActions";
    public static final int QUERY_ACT_MANAGER = 99420;

    // 回显用户资料
    public static final String QUEST_ME_INFO=baseUrl + "/me/echoUserInfo";
    public static final int QUERY_ME_INFO = 99419;

    // 修改用户基本资料
    public static final String QUEST_BASE_INFO=baseUrl + "/me/editUser";
    public static final int QUERY_BASE_INFO = 99418;
    //个性签名
    public static final String QUEST_BASE_INTODESC=baseUrl + "/me/editUserIntro";
    //其它基本信息
    public static final String QUEST_BASE_OTHER=baseUrl + "/me/editUserOther";
    //身高体重信息
    public static final String QUEST_BASE_HEIGHT_WEIGHT=baseUrl + "/me/editUserBody";

    // 修查看用户资料
    public static final String QUEST_USER_INFO=baseUrl + "/me/queryUserDetail";
    public static final int QUERY_USER_INFO = 99417;

    // 关注用户
    public static final String QUEST_USER_ATTEN=baseUrl + "/me/relation/attentUser";
    public static final int QUERY_USER_ATTEN = 99416;

    // 个人资料送礼排行
    public static final String QUEST_USER_GIFT_RANK=baseUrl + "/me/queryGiftRank";
    public static final int QUERY_USER_GIFT_RANK = 99415;

    // 个人资料中的可用活动
    public static final String QUEST_USER_ACTIONS=baseUrl + "/me/queryUserExhibitActions";
    public static final int QUERY_USER_ACTIONS = 99414;

    // 我关注的
    public static final String QUEST_USER_ATTENT=baseUrl + "/me/relation/queryMyAttentUsers";
    public static final int QUERY_USER_ATTENT = 99413;

    // 我的粉丝
    public static final String QUEST_USER_FANS=baseUrl + "/me/relation/queryMyFans";
    public static final int QUERY_USER_FANS = 99412;

    // 最近来访
    public static final String QUEST_USER_RECENTLY=baseUrl + "/me/relation/queryMyVisitor";
    public static final int QUERY_USER_RECENTLY = 99411;

    // 查询消息通告设置
    public static final String QUEST_SETTING_INFO=baseUrl + "/me/queryNotifySetting";
    public static final int QUERY_SETTING_INFO = 99410;

    // 设置消息通知
    public static final String QUEST_SETTING_NOTICE=baseUrl + "/me/setupNotify";
    public static final int QUERY_SETTING_NOTICE = 99409;

    // 查询黑名单
    public static final String QUEST_BLACK_LIST=baseUrl + "/me/relation/queryUserBlacklist";
    public static final int QUERY_BLACK_LIST= 99408;
    public static final int QUERY_BLACK_LIST1= 9960;
    public static final int QUERY_BLACK_LIST2= 9954;

    // 移除黑名单
    public static final String QUEST_BLACK_LIST_REMOVE=baseUrl + "/me/relation/removeFromBlacklist";
    public static final int QUERY_BLACK_LIST_REMOVE= 99407;

    // 拉入黑名单
    public static final String QUEST_ADD_BLACK_LIST=baseUrl + "/me/relation/pull2Blacklist";
    public static final int QUERY_ADD_BLACK_LIST = 99406;

    // 相册查询
    public static final String QUEST_GALLERY_PICS=baseUrl + "/me/gallery/queryUserGalleryPics";
    public static final int QUERY_GALLERY_PICS = 99405;

    // 增加相片的接口
    public static final String QUEST_GALLERY_ADD=baseUrl + "/me/gallery/addGalleryPic";
    public static final int QUERY_GALLERY_ADD = 99404;

    // 删除相片的接口
    public static final String QUEST_GALLERY_DELETE=baseUrl + "/me/gallery/deleteGalleryPic";
    public static final int QUERY_GALLERY_DELETE = 99403;

    // 查询通告的群组
    public static final String QUEST_GROUP_LIST=baseUrl + "/im/ngroup/queryNoticeGroups";
    public static final int QUERY_GROUP_LIST = 99402;

    // 查询通告的群详情
    public static final String QUEST_GROUP_DETAIL=baseUrl + "/im/ngroup/queryNoticeGroupDetail";
    public static final int QUERY_GROUP_DETAIL = 99401;

    // 查询通告的群成员
    public static final String QUEST_GROUP_MEMBERS=baseUrl + "/im/ngroup/queryNoticeGroupMembers";
    public static final int QUERY_GROUP_MEMBERS = 99400;

    // 群关注
    public static final String QUEST_GROUP_GUANZHU=baseUrl + "/im/ngroup/joinNoticeGroup";
    public static final int QUERY_GROUP_GUANZHU = 99399;

    // 群取消关注
    public static final String QUEST_GROUP_CANCEL_GUANZHU=baseUrl + "/im/ngroup/quitNoticeGroup";
    public static final int QUERY_GROUP_CANCEL_GUANZHU = 99398;

    // 查询所有群组
    public static final String QUEST_GROUP_ALL=baseUrl + "/im/ngroup/queryUserGroups";
    public static final int QUERY_GROUP_ALL = 99397;

    //活动报名拒绝或同意操作
    public static final String QUEST_ACT_APPLY=baseUrl + "/act/normal/auditApplicant";
    public static final int QUERY_ACT_APPLY = 99396;

    // 名片认证
    public static final String QUEST_CERTIFY_CARD=baseUrl + "/auth/applyVcardAuth";
    public static final int QUERY_CERTIFY_CARD = 99395;

    // 用户反馈
    public static final String QUEST_FEEDBACK=baseUrl + "/sys/sysFeedback";
    public static final int QUERY_FEEDBACK = 99394;

    // 查询分组
    public static final String QUEST_GZ_GROUP=baseUrl + "/me/relation/queryUserAttentionGroups";
    public static final int QUERY_GZ_GROUP = 99393;

    // 创建分组
    public static final String QUEST_GZ_GROUP_CREATE=baseUrl + "/me/relation/createUserAttentionGroup";
    public static final int QUERY_GZ_GROUP_CREATE = 99392;

    // 删除分组
    public static final String QUEST_GZ_GROUP_DELE=baseUrl + "/me/relation/delUserAttentionGroup";
    public static final int QUERY_GZ_GROUP_DELE = 99391;

    // 查询分组成员
    public static final String QUEST_GZ_GROUP_MEMBERS=baseUrl + "/me/relation/queryAttentGroupUsers";
    public static final int QUERY_GZ_GROUP_MEMBERS = 99390;

    // 分组
    public static final String QUEST_DIVID_GROUP=baseUrl + "/me/relation/changeUserBelongGroup";
    public static final int QUERY_DIVID_GROUP = 99389;

    // 分组
    public static final String QUEST_DIVID_GROUP_SEARCH=baseUrl + "/me/relation/searchAttentGroupUsers";
    public static final int QUERY_DIVID_GROUP_SEARCH = 99388;

    // 通告群公告
    public static final String QUEST_NOTICE_GROUP_TIP=baseUrl + "/im/ngroup/queryNoticeGroupAffiche";
    public static final int QUERY_NOTICE_GROUP_TIP = 99387;

    // 充值付款
    public static final String QUEST_PAY_MONEY=baseUrl + "/vasset/recharge";
    public static final int QUERY_PAY_MONEY = 99386;

    // 付款类型
    public static final String QUEST_PAY_MONEY_TYPE=baseUrl + "/vasset/queryPayChannels";
    public static final int QUERY_PAY_MONEY_TYPE = 99385;

    // 查询余额及充值类型
    public static final String QUEST_LEFT_MONEY_CHECK=baseUrl + "/vasset/queryRechargeAmountChannels";
    public static final int QUERY_LEFT_MONEY_CHECK = 99384;

    // 创建分组
    public static final String QUEST_CREATE_DISC_GROUP=baseUrl + "/im/discgroup/createDiscussGroup";
    public static final int QUERY_CREATE_DISC_GROUP = 99383;
    public static final int QUERY_CREATE_DISC_GROUP2 = 99382;

    // 讨论组成员
    public static final String QUEST_DISC_GROUP_MEMBER=baseUrl + "/im/discgroup/queryDiscussGroupMembers";
    public static final int QUERY_DISC_GROUP_MEMBER = 99381;

    // 修改群聊名称
    public static final String QUEST_EDITOR_DISC_GROUP_NAME=baseUrl + "/im/discgroup/modifyGroupName";
    public static final int QUERY_EDITOR_DISC_GROUP_NAME = 99380;

    // 解散群
    public static final String QUEST_LOSE_DISC_GROUP=baseUrl + "/im/discgroup/disbandGroup";
    public static final int QUERY_LOSE_DISC_GROUP = 99379;

    // 踢出群
    public static final String QUEST_DISC_GROUP_KICK=baseUrl + "/im/discgroup/kickOutMember";
    public static final int QUERY_DISC_GROUP_KICK = 99378;

    // 退出群聊
    public static final String QUEST_DISC_GROUP_TAKOUT=baseUrl + "/im/discgroup/quitDiscussGroup";
    public static final int QUERY_DISC_GROUP_TAKOUT = 99377;

    // 加入群聊成员
    public static final String QUEST_DISC_GROUP_ADD=baseUrl + "/im/discgroup/appendMember";
    public static final int QUERY_DISC_GROUP_ADD = 99376;
    public static final int QUERY_DISC_GROUP_ADD1 = 99375;

    //定额活动列表
    public static final String QUEST_NORMAL_ACT_LIST=baseUrl + "/act/normal/queryNormalActions";
    public static final int QUERY_NORMAL_ACT_LIST = 99374;

    //查询是否相互关注过
    public static final String QUEST_HAS_ATTENT=baseUrl + "/me/relation/hasMutualAttent";
    public static final int QUERY_HAS_ATTENT = 99373;

    //查询大神类型
    public static final String QUEST_SERVER_TYPE=baseUrl + "/sys/queryServiceType";
    public static final int QUERY_SERVER_TYPE = 99372;
    public static final int QUERY_SERVER_TYPE1 = 99351;

    //查询大神类型状态
    public static final String QUEST_SERVER_TYPE_STATUS=baseUrl + "/auth/queryExpertAuthStatus";
    public static final int QUERY_SERVER_TYPE_STATUS = 99371;

    //申请大神
    public static final String QUEST_AUTH_GOD=baseUrl + "/auth/applyExpertAuth";
    public static final int QUERY_AUTH_GOD = 99370;

    //查询大神
    public static final String QUEST_ECHO_GOD=baseUrl + "/me/aptitude/echoUserAptitude";
    public static final int QUERY_ECHO_GOD = 99369;

    //接单设置
    public static final String QUEST_SET_ORDER=baseUrl + "/me/aptitude/queryUserAptitudeOrderSetting";
    public static final int QUERY_SET_ORDER = 99368;

    //查询大神类型价格
    public static final String QUEST_SERVER_TYPE_PRICE=baseUrl + "/sys/queryServicePrice";
    public static final int QUERY_SERVER_TYPE_PRICE = 99367;

    //修改接口
    public static final String QUEST_EDITOR_SERVER_TYPE=baseUrl + "/me/aptitude/setupUserAptitudeOrder";
    public static final int QUERY_EDITOR_SERVER_TYPE = 99366;

    //查询订单首页用户的接口
    public static final String QUEST_HOME_USERS=baseUrl + "/order/queryOrderHomeUsers";
    public static final int QUERY_HOME_USERS = 99365;

    //大神详情
    public static final String QUEST_GOD_DETAIL=baseUrl + "/me/aptitude/queryUserExpertDetail";
    public static final int QUERY_GOD_DETAIL = 99364;

    //下单详情
    public static final String QUEST_GOD_PRICE=baseUrl + "/me/aptitude/queryUserExpertPrice";
    public static final int QUERY_GOD_PRICE = 99363;

    //下单提交
    public static final String QUEST_COMMIT_ORDER=baseUrl + "/order/submitInviteOrder";
    public static final int QUERY_COMMIT_ORDER = 99362;

    //同意下单
    public static final String QUEST_ORDER_STATUS=baseUrl + "/order/dealOrder";
    public static final int QUERY_ORDER_STATUS = 99361;

    //订单详情
    public static final String QUEST_ORDER_DETAIL=baseUrl + "/order/queryOrderDetail";
    public static final int QUERY_ORDER_DETAIL = 99360;

    //完成订单
    public static final String QUEST_ORDER_CANCEL=baseUrl + "/order/confirmComplete";
    public static final int QUERY_ORDER_CANCEL = 99359;
    public static final int QUERY_ORDER_CANCEL1 = 99358;

    //取消订单
    public static final String QUEST_ORDER_CANCEL1=baseUrl + "/order/cancleOrder";
    public static final int QUERY_ORDER_DIS = 99357;

    //申请退款
    public static final String QUEST_ORDER_APPLY=baseUrl + "/order/applyCancleOrder";
    public static final int QUERY_ORDER_APPLY = 99356;

    //申请退款中回执消息处理
    public static final String QUEST_OPERATE_APPLY=baseUrl + "/order/dealApplyCancleOrder";
    public static final int QUERY_OPERATE_APPLY = 99355;

    //周榜和月榜
    public static final String QUEST_GOD_LIST=baseUrl + "/order/queryOrderStatRank";
    public static final int QUERY_GOD_LIST = 99354;

    //周榜和月榜
    public static final String QUEST_GOD_LIST_QUTO=baseUrl + "/order/queryOrderUers";
    public static final int QUERY_GOD_LIST_QUTO = 99353;

    //订单中心接口
    public static final String QUEST_ORDER_CENTER=baseUrl + "/order/queryUserOrders";
    public static final int QUERY_ORDER_CENTER = 99352;

    //举报类型接口
    public static final String QUEST_JUBAO_TYPE=baseUrl + "/sys/queryComplaintType";
    public static final int QUERY_JUBAO_TYPE= 99350;

    //提交举报
    public static final String QUEST_JUBAO_COMMIT=baseUrl + "/sys/complaintResource";
    public static final int QUERY_JUBAO_COMMIT= 99349;
    public static final int QUERY_JUBAO_COMMIT1= 99348;

    //立即约订单接口
    public static final String QUEST_IM_ORDER=baseUrl + "/preorder/broadcastOrder";
    public static final int QUERY_IM_ORDER= 99347;

    //大神抢单接口
    public static final String QUEST_QIANG_FOR_GOD=baseUrl + "/preorder/fastReceiveOrder";
    public static final int QUERY_QIANG_FOR_GOD= 99346;
    public static final int QUERY_QIANG_FOR_GOD1= 99345;

    //接单接口
    public static final String QUEST_QIANG_RESULT_GOD=baseUrl + "/preorder/retrieveRecruitableMembers";
    public static final int QUERY_QIANG_RESULT_GOD= 99344;

    //提交订单接口
    public static final String QUEST_COMMIT_ORDER_QIANG=baseUrl + "/preorder/enrollOrderUser";
    public static final int QUERY_COMMIT_ORDER_QIANG= 99343;

    //订单支付单接口
    public static final String QUEST_COMMIT_ORDER_PAY=baseUrl + "/order/payOrder";
    public static final int QUERY_COMMIT_ORDER_PAY= 99342;

    //取消订单接口
    public static final String QUEST_CANCEL_ORDER_QIANG=baseUrl + "/preorder/canclePreOrder";
    public static final int QUERY_CANCEL_ORDER_QIANG= 99341;

   public static void showConfigDialog(Context context,String tip){
       OneButtonDialog dialog = new OneButtonDialog(context, tip, R.style.dineDialog,
               new ButtonOneListener() {
                   @Override
                   public void confrimListener(Dialog dialog) {
                   }
               });
       dialog.show();
   }
}

package com.xygame.sg.utils;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.utils.SSLSocketFactoryEx;
import com.xygame.sg.activity.image.ImagePagerActivity;
import com.xygame.sg.activity.image.ImagePagerSettingFengMianActivity;
import com.xygame.sg.activity.personal.bean.TransFerImagesBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import base.action.CenterRepo;
import base.data.net.http.JsonUtil;
import base.frame.DataUnit;

/**
 * Created by minhua on 2015/11/4.
 */
public class Constants {

    public static String SOURCE_URL="res.madoufans.com";

    public static String AIM_URL="res.moreidols.com";

    public static String IS_REQUEST_QIANG_FLAG="100";//用户标识位用它来指定查询其对应的groupId：0表示：不调接口，1表示要调接口,对应的startTime为抢单时间

    public static String QIANGDAN_TIMER="orderExpireTime";//抢单时间

    public static String PAY_TIMER="payExpireTime";//支付时间

    public static String TARG_URL = "http://weixin.moreidols.com/share/index?title=%E5%96%B5%E4%BC%B4%E4%BC%B4%E5%88%86%E4%BA%AB&url=wxa3adeb4e3f8563be%3a%2f%2fbanban%2f";

    public static String[] DUAN_WEI = {"黄铜", "白银", "黄金", "铂金", "钻石", "王者"};

    public static final int DECIMAL_DIGITS = 2;//输入框小数的位数

    public static class Configure {
        Set<DataUnit> dataUnit = new HashSet<DataUnit>();
        public static Configure configure = new Configure();

        public static Configure getInstance() {
            return configure;
        }

        public void setDataUnit(DataUnit dataUnit) {
            this.dataUnit.add(dataUnit);
        }

        public void reset() {
            for (DataUnit dataunit : dataUnit) {
                dataunit.baseUrl = Constants.baseUrl;
                dataunit.init();
            }
        }
    }

    public static final String COMEBACK = "finishAcitity";
    public static final String SIGLE_WHEEL_ITEM = "SIGLE_WHEEL_ITEM";

    public static final boolean DEBUG = true;//debug开关，可以看到输出日志
//    public static final boolean DEBUG=false;//debug开关，打正式包时关闭输出日志

    public static final List<String> baseUrls = new ArrayList<String>();// 阿里云
//        public static String baseUrl = "https://open.madoufans.com:8089/nsg-ws/base";//审核地址
//	public static String baseUrl = "http://192.168.10.28:8088/nsg-ws/base";// 223测试地址
//public static String baseUrl = "http://192.168.20.200:8081/nsg-ws/base";// 星瑞
//    public static String baseUrl = "http://192.168.20.96:8080/nsg-ws/base";//
    public static String baseUrl = "https://open.madoufans.com:8011/nsg-ws/base";//生产地址

    // 备选地址
    static {
        baseUrls.add("http://open.madoufans.com:8088/nsg-ws/base");
        baseUrls.add("http://open.madoufans.com:8080/nsg-ws/base");
        baseUrls.add("http://192.168.1.223:8088/nsg-ws/base");
        baseUrls.add("http://192.168.1.4:8080/nsg-ws/base");
        baseUrls.add("http://192.168.1.5:8081/nsg-ws/base");
        baseUrls.add("http://192.168.2.161:8090/nsg-ws/base");
    }

    public static int Mode = -1;
    public static final int MODE_TEST = 0;


    public static void init() {
        CenterRepo.getInsatnce().getRepo().put("baseurl", baseUrl);
        // 获取注册验证码
        CenterRepo.getInsatnce().getRepo().put("user_sendRegVerifyCode", baseUrl + "/user/sendRegVerifyCode");
        // 验证验证与手机号是否合法
        CenterRepo.getInsatnce().getRepo().put("user_verifyPhoneCode", baseUrl + "/user/verifyPhoneCode");
        // 注册接口
        CenterRepo.getInsatnce().getRepo().put("user_register", baseUrl + "/user/register");
        // 阿里云密钥信息接口
//        CenterRepo.getInsatnce().getRepo().put("ali_params", baseUrl + "/sys/queryAliOSSKey");
        // 登录接口
        CenterRepo.getInsatnce().getRepo().put("sg_login", baseUrl + "/user/login");
        // 重置密码验证码获取
        CenterRepo.getInsatnce().getRepo().put("user_forgetVerifyCode", baseUrl + "/user/sendResetVerifyCode");
        // 提交新密码
        CenterRepo.getInsatnce().getRepo().put("user_commitnewpwssword", baseUrl + "/user/resetPassword");

        // 百度定位服务地址
        CenterRepo.getInsatnce().getRepo().put("baidu_location_service", baseUrl + "/user/reportCoord");
        CenterRepo.getInsatnce().getRepo().put("authModelInfo", baseUrl + "/auth/authModelInfo");
        // 编辑头像
        CenterRepo.getInsatnce().getRepo().put("editUserLogoVideo", baseUrl + "/me/editUserLogoVideo");

        // 模特创建作品
        CenterRepo.getInsatnce().getRepo().put("createModelGallery", baseUrl + "/me/createModelGallery");

        // 查询模特作品集
        CenterRepo.getInsatnce().getRepo().put("queryModelGallery", baseUrl + "/me/queryModelGallery");
        // 查看模特基本详情
        CenterRepo.getInsatnce().getRepo().put("modelData", baseUrl + "/model/modelData");
        // 查看模特基本详情
        CenterRepo.getInsatnce().getRepo().put("queryMyOverview", baseUrl + "/me/queryMyOverview");
        // 模特资料(比较全)
        CenterRepo.getInsatnce().getRepo().put("echoModelInfo", baseUrl + "/me/echoModelInfo");
        // 报价
        CenterRepo.getInsatnce().getRepo().put("echoModelPrice", baseUrl + "/me/echoModelPrice");
        // 操作模特报价（增加、编辑、删除）
        CenterRepo.getInsatnce().getRepo().put("cudModelPrice", baseUrl + "/me/cudModelPrice");
        // 拍摄大类
        CenterRepo.getInsatnce().getRepo().put("queryModelShootType", baseUrl + "/sys/queryModelShootType");
        // 编辑用户身体信息
        CenterRepo.getInsatnce().getRepo().put("editUserBody", baseUrl + "/me/editUserBody");

        // 编辑用户生日
        CenterRepo.getInsatnce().getRepo().put("editUser", baseUrl + "/me/editUser");

        // 编辑用户风格
        CenterRepo.getInsatnce().getRepo().put("addModelStyle", baseUrl + "/me/addModelStyle");

        // 编辑用户简介信息
        CenterRepo.getInsatnce().getRepo().put("editUserIntro", baseUrl + "/me/editUserIntro");

        // 操作模特作品（增加、删除）
        CenterRepo.getInsatnce().getRepo().put("cudModelGalleryPic", baseUrl + "/me/cudModelGalleryPic");

        // 操作用户简历（增加、编辑、删除）
        CenterRepo.getInsatnce().getRepo().put("cudModelResume", baseUrl + "/me/cudModelResume");

        // 查询用户职业类型
        CenterRepo.getInsatnce().getRepo().put("queryUserOccupType", baseUrl + "/sys/queryUserOccupType");

        // 查询模特风格类型
        CenterRepo.getInsatnce().getRepo().put("queryModelStyleType", baseUrl + "/sys/queryModelStyleType");

        // 编辑相册
        CenterRepo.getInsatnce().getRepo().put("editModelGallery", baseUrl + "/me/editModelGallery");

        // 查询模特某作品集详情
        CenterRepo.getInsatnce().getRepo().put("queryModelGalleryDetail", baseUrl + "/me/queryModelGalleryDetail");
        // 添加或取消关注
        CenterRepo.getInsatnce().getRepo().put("editAttention", baseUrl + "/model/editAttention");

        // 获取模特相册信息
        CenterRepo.getInsatnce().getRepo().put("modelDataGalleryInfo", baseUrl + "/model/modelDataGalleryInfo");

        // 获取模特相册信息
        CenterRepo.getInsatnce().getRepo().put("praiseModelGallery", baseUrl + "/me/praiseModelGallery");

        // 获取模特相册信息
        CenterRepo.getInsatnce().getRepo().put("cancelPraiseModelGallery", baseUrl + "/me/cancelPraiseModelGallery");

        // 查询模特作品集点赞者信息
        CenterRepo.getInsatnce().getRepo().put("queryGalleryPraiseUser", baseUrl + "/me/queryGalleryPraiseUser");

        // 查询模特某作品点赞者信息
        CenterRepo.getInsatnce().getRepo().put("queryGalleryPicPraiseUser", baseUrl + "/me/queryGalleryPicPraiseUser");

        // 发布通告
        CenterRepo.getInsatnce().getRepo().put("publishNotice", baseUrl + "/notice/pl/publishNotice");
        // 查看通告列表
        CenterRepo.getInsatnce().getRepo().put("queryNotices", baseUrl + "/notice/pl/queryNotices");
        // 查看通告详情
        CenterRepo.getInsatnce().getRepo().put("queryNoticeDetail", baseUrl + "/notice/detail");
        // 搜素通告
        CenterRepo.getInsatnce().getRepo().put("searchNotices", baseUrl + "/notice/pl/searchNotices");
        // 收藏通告
        CenterRepo.getInsatnce().getRepo().put("collectNotices", baseUrl + "/notice/collect");
        // 通告报名
        CenterRepo.getInsatnce().getRepo().put("applyNotices", baseUrl + "/notice/apply");
        // 用户通告
        CenterRepo.getInsatnce().getRepo().put("queryUserNotices", baseUrl + "/notice/pl/queryUserNotices");
        // 获取支付方式
        CenterRepo.getInsatnce().getRepo().put("getPayTypes", baseUrl + "/pay/getPayTypes");
        // 获取支付方式
        CenterRepo.getInsatnce().getRepo().put("queryPayChannels", baseUrl + "/vasset/queryPayChannels");
        // 提现
        CenterRepo.getInsatnce().getRepo().put("withdrawCash", baseUrl + "/vasset/withdrawCash");
        // 获取用户余额
        CenterRepo.getInsatnce().getRepo().put("userMoney", baseUrl + "/pay/userMoney");
        // 通告支付
        CenterRepo.getInsatnce().getRepo().put("noticePay", baseUrl + "/pay/noticePay");
        //
        CenterRepo.getInsatnce().getRepo().put("membersQuery", baseUrl + "/notice/me/members");
        //
        CenterRepo.getInsatnce().getRepo().put("membersUpdate", baseUrl + "/notice/me/membersUpdate");
        // 通过列表
        CenterRepo.getInsatnce().getRepo().put("noticeList", baseUrl + "/notice/me/list");
        // 查询所有可用优惠券数量
        CenterRepo.getInsatnce().getRepo().put("getAllCouponsCount", baseUrl + "/coupon/getAllCouponsCount");
        // 查看所有优惠券
        CenterRepo.getInsatnce().getRepo().put("getAllCoupons", baseUrl + "/coupon/getAllCoupons");
        // 查询系统举报类型
        CenterRepo.getInsatnce().getRepo().put("queryComplaintType", baseUrl + "/sys/queryComplaintType");
        // 举报系统资源
        CenterRepo.getInsatnce().getRepo().put("complaintResource", baseUrl + "/sys/complaintResource");
        // 查询结算支付
        CenterRepo.getInsatnce().getRepo().put("memberPay", baseUrl + "/notice/me/memberPay");
        // 通过状态查询
        CenterRepo.getInsatnce().getRepo().put("flows", baseUrl + "/notice/me/flows");
        // 待结算跳转到付款界面
        CenterRepo.getInsatnce().getRepo().put("toPay", baseUrl + "/notice/me/toPay");
        // 查询评价信息
        CenterRepo.getInsatnce().getRepo().put("queryEvaluate", baseUrl + "/notice/me/queryEvaluate");
        // 查询通告列表
        CenterRepo.getInsatnce().getRepo().put("queryNoticeSign", baseUrl + "/notice/sign/queryNoticeSign");
        // 评价
        CenterRepo.getInsatnce().getRepo().put("evaluate", baseUrl + "/notice/me/evaluate");
        // 通告签到
        CenterRepo.getInsatnce().getRepo().put("noticeSign", baseUrl + "/notice/sign/noticeSign");
        // 充值
        CenterRepo.getInsatnce().getRepo().put("recharge", baseUrl + "/pay/recharge");
        // 充值
        CenterRepo.getInsatnce().getRepo().put("vassetRecharge", baseUrl + "/vasset/recharge");
        // 查询是否设置充值密码
        CenterRepo.getInsatnce().getRepo().put("hasOpsPassword", baseUrl + "/pay/hasOpsPassword");
        // 添加或修改支付密码
        CenterRepo.getInsatnce().getRepo().put("addOrEditOpsPassword", baseUrl + "/pay/addOrEditOpsPassword");
        // 收支明细
        CenterRepo.getInsatnce().getRepo().put("listChange", baseUrl + "/pay/listChange");
        // 查询所有优惠券
        CenterRepo.getInsatnce().getRepo().put("listCoupons", baseUrl + "/coupon/listCoupons");
        // 激活优惠券
        CenterRepo.getInsatnce().getRepo().put("registCouponCode", baseUrl + "/coupon/registCouponCode");
        // 查看绑定的支付宝
        CenterRepo.getInsatnce().getRepo().put("getReceiptAccount", baseUrl + "/pay/getReceiptAccount");
        // 绑定支付宝
        CenterRepo.getInsatnce().getRepo().put("addReceiptAccount", baseUrl + "/pay/addReceiptAccount");
        // 解绑支付宝
        CenterRepo.getInsatnce().getRepo().put("removeReceiptAccount", baseUrl + "/pay/removeReceiptAccount");
        // 模特提现
        CenterRepo.getInsatnce().getRepo().put("cash", baseUrl + "/pay/cash");
        // 摄影师提现
        CenterRepo.getInsatnce().getRepo().put("refund", baseUrl + "/pay/refund");
        // 调整报名状态
        CenterRepo.getInsatnce().getRepo().put("membersUpdate", baseUrl + "/notice/me/membersUpdate");
        // 激活用户
        CenterRepo.getInsatnce().getRepo().put("activateUser", baseUrl + "/user/activateUser");
        // 查询用户关注的人
        CenterRepo.getInsatnce().getRepo().put("queryMyAttentUsers", baseUrl + "/me/queryMyAttentUsers");
        // 查询用户收藏的通告
        CenterRepo.getInsatnce().getRepo().put("queryUserCollectNotices", baseUrl + "/notice/pl/queryUserCollectNotices");
        // 查询用户的访客
        CenterRepo.getInsatnce().getRepo().put("queryMyVisitor", baseUrl + "/me/queryMyVisitor");
        // 设置消息通知
        CenterRepo.getInsatnce().getRepo().put("setupNotify", baseUrl + "/me/setupNotify");
        // 查询消息通告设置
        CenterRepo.getInsatnce().getRepo().put("queryNotifySetting", baseUrl + "/me/queryNotifySetting");
        // 修改用户密码
        CenterRepo.getInsatnce().getRepo().put("editUserPassword", baseUrl + "/me/editUserPassword");
        // 系统反馈
        CenterRepo.getInsatnce().getRepo().put("sysFeedback", baseUrl + "/sys/sysFeedback");
        // 查询反馈客服电话（系统反馈界面调用）
        CenterRepo.getInsatnce().getRepo().put("queryFeedbackServicePhone", baseUrl + "/sys/queryFeedbackServicePhone");// 查询用户认证私密图片的密钥
//        CenterRepo.getInsatnce().getRepo().put("queryAuthPicKey", baseUrl + "/sys/queryAuthPicKey");// 查询用户认证私密图片的密钥
        // 摄影师付款给模特
        CenterRepo.getInsatnce().getRepo().put("noticePayToModel", baseUrl + "/pay/noticePayToModel");
        //录用前查看付款金额
        CenterRepo.getInsatnce().getRepo().put("membersUpdateFirst", baseUrl + "/notice/me/membersUpdateFirst");
        //查询模特签到信息
        CenterRepo.getInsatnce().getRepo().put("queryModelNoticeSign", baseUrl + "/notice/sign/queryModelNoticeSign");
        //确认拍摄完成
        CenterRepo.getInsatnce().getRepo().put("ackShootDone", baseUrl + "/notice/me/ackShootDone");
        //查询通告结算时的手续费
        CenterRepo.getInsatnce().getRepo().put("queryNoticeLiquidatFee", baseUrl + "/sys/queryNoticeLiquidatFee");
        //模特评价通告
        CenterRepo.getInsatnce().getRepo().put("evaluateNotice", baseUrl + "/notice/me/evaluateNotice");
        //上报用户推送的设备码上报用户推送的设备码
//        CenterRepo.getInsatnce().getRepo().put("reportDeviceToken", baseUrl + "/user/reportDeviceToken");
        //关闭通告
        CenterRepo.getInsatnce().getRepo().put("closeNotice", baseUrl + "/notice/me/close");
        //关闭通告页面的信息
        CenterRepo.getInsatnce().getRepo().put("toCloseNotice", baseUrl + "/notice/me/toClose");
        //热门模特上方的轮播
        CenterRepo.getInsatnce().getRepo().put("loadBannerInfo", baseUrl + "/home/loadBannerInfo");
        //获取热门模特
        CenterRepo.getInsatnce().getRepo().put("loadHotModelInfo", baseUrl + "/home/loadHotModelInfo");
        //模特全部接口
        CenterRepo.getInsatnce().getRepo().put("loadAllModelInfo", baseUrl + "/home/loadAallModelInfo");
        //模特全部接口
        CenterRepo.getInsatnce().getRepo().put("loadAllModelInfo", baseUrl + "/home/loadAallModelInfo");
        //获取百度缩图
        CenterRepo.getInsatnce().getRepo().put("staticimage", "http://api.map.baidu.com/staticimage");
        //用户认证类型表
        CenterRepo.getInsatnce().getRepo().put("queryUserType", baseUrl + "/auth/queryUserType");
        //普通模特身份证是否重复
        CenterRepo.getInsatnce().getRepo().put("modelIdCardExists", baseUrl + "/auth/modelIcCardExists");
        //高级模特认证
        CenterRepo.getInsatnce().getRepo().put("authProModelInfo", baseUrl + "/auth/authProModelInfo");
        //摄影师认证
        CenterRepo.getInsatnce().getRepo().put("authPhotographerInfo", baseUrl + "/auth/authPhotographerInfo");
        //摄影师身份证是否重复
        CenterRepo.getInsatnce().getRepo().put("photographerIcCardExists", baseUrl + "/auth/photographerIcCardExists");
        //通告详情
        CenterRepo.getInsatnce().getRepo().put("inviteList", baseUrl + "/notice/invite/list");
        //收藏或者取消收藏
        CenterRepo.getInsatnce().getRepo().put("inviteAction", baseUrl + "/notice/invite/send");
        //查询系统客服电话
//        CenterRepo.getInsatnce().getRepo().put("queryCustomerServicePhone", baseUrl + "/sys/queryCustomerServicePhone");
        // 摄影师评价模特
        CenterRepo.getInsatnce().getRepo().put("evaluateModel", baseUrl + "/notice/me/evaluateModel");
        // 动态消息，未读消息
        CenterRepo.getInsatnce().getRepo().put("findLatestMessage", baseUrl + "/push/findLatestMessage");
        // 获取评价列表
        CenterRepo.getInsatnce().getRepo().put("scoreList", baseUrl + "/model/scoreList");
        // 获取评价列表
        CenterRepo.getInsatnce().getRepo().put("setupGalleryCover", baseUrl + "/me/setupGalleryCover");

        // 申请解除录用
        CenterRepo.getInsatnce().getRepo().put("sendOffRequest", baseUrl + "/notice/me/sendOffRequest");
        //拒绝
        CenterRepo.getInsatnce().getRepo().put("refuseFireNotice", baseUrl + "/notice/me/refuseFireNotice");
        //同意解除的接口
        CenterRepo.getInsatnce().getRepo().put("agreeFireNotice", baseUrl + "/notice/me/agreeFireNotice");
        //参加活动的接口
        CenterRepo.getInsatnce().getRepo().put("joinBDActivity", baseUrl + "/bd/act/joinBDActivity");
        //绑定手机号
        CenterRepo.getInsatnce().getRepo().put("bindUserPhone", baseUrl + "/user/bindUserPhone");
    }

    public static final int PHOTO_WIDE = 480;

    public static final int PHOT_HIGHT = 800;

    public static final String SEND_NOTICE_TEXT = "-1";//发布的通告消息

    public static final String SEND_TEXT = "1";

    public static final String SEND_SOUND = "3";

    public static final String SEND_IMAGE = "2";

    public static final String SEND_LOCATION = "4";

    public static final String SEND_ACT = "5";

    public static final String SEND_VIDEO = "9";

    public static final String SEND_TEXT_TIP = "10";

    public static final String USER_PREFERNCE = "userShareInfo";

    public static final String BAIDU_PREFERNCE = "baodiInfo";

    public static final String SEX_WOMAN = "0";// 女

    public static final String SEX_MAN = "1";// 男

    public static final String USER_VERIFIED_CODE = "2";// 用户已经通过了认证
    public static final String USER_NO_VERIFIED_CODE = "0";// 未申请认证
    public static final String USER_VERIFING_CODE = "1";// 审核中
    public static final String USER_VERIFING_REFUSED_CODE = "3";// 未通过审核

    public static final String CARRE_MODEL = "2";// 普通模特
    public static final String PRO_MODEL = "4";// 高级模特

    public static final String CARRE_PHOTOR = "8";//摄影

    public static final String CARRE_MERCHANT = "16";//经纪人

    public static final String[] CHARACTE_NUMS = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三",
            "", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九",
            "三十", "三十一", "三十二", "三十三", "三十四", "三十五", "三十六", "三十七", "三十八", "三十九", "四十", "四十一", "四十二", "四十三", "四十四",
            "四十五", "四十六", "四十七", "四十八", "四十九", "五十", "五十一", "五十二", "五十三", "五十四", "五十五", "五十六", "五十七", "五十八", "五十九",
            "六十", "六十一", "六十二", "六十三", "六十四", "六十五", "六十六", "六十七", "六十八", "六十九", "七十", "七十一", "七十二", "七十三", "七十四",
            "七十五", "七十六", "七十七", "七十八", "七十九", "八十", "八十一", "八十二", "八十三", "八十四", "八十五", "八十六", "八十七", "八十八", "八十九",
            "九十", "九十一", "九十二", "九十三", "九十四", "九十五", "九十六", "九十七", "九十八", "九十九"};

    /**
     * 联系人轮询的时间间隔, 默认 30 分钟
     */
    public static final int CONTACTS_SCHEDULE_INTERVAL = 30 * 60 * 1000;

    public static final int FIX_SCALE = 2;

    /**
     * 默认线程阻塞时间：10秒
     */
    public final static int THREAD_BLOCK_TIMEOUT_DEFAULT = 10000;

    /**
     * TCP 默认连接超时，3秒钟
     */
    public static final int TIMEOUT_CONNECTION = 10000;

    /**
     * TCP 默认读取数据超时，30秒钟
     */
    public static final int TIMEOUT_SOCKET = 30000;

    public static final String RESULT_CODE = "true";

    public static final String AVATAR_PATH = "avatar";// 头像目录

    public static final String ACTIVITY_PATH = "activity";// 活动图片目录

    public static final String NEWS_PATH = "news";// 新鲜事目录

    public static final String MESSAGE_PATH = "message";// 聊天图片目录

    public static final String GROUP_PATH = "group";// 群组图片目录

    public static final String PHOTOBUM_PATH = "photoalbum";// 相册目录

    public static final String COMPLAINITS_PATH = "complaints";// 举报图片目录

    public static final String VERIFICATION_PATH = "userverification";// 用户验证资料目录

    public static final String NOTICE_PHOTOES_PATH = "circular_pic";// 通告图片目录

    public static final String LOGIN_ACCOUNT = "1";// 手机账号登录

    public static final String LOGIN_WEIXIN = "2";// 微信账号登录

    public static final String LOGIN_WEIBO = "3";// 微博账号登录

    public static final String LOGIN_QQ = "4";// QQ账号登录

    /**
     * QQ appId
     */
    public static final String QQ_APP_ID = "1104997366";
    public static final String QQ_APP_KEY = "ZM3PVDXJrMLnEFTb";
    /**
     * Tencent appId
     */
    public static final String WeiXin_APP_ID = "wxa3adeb4e3f8563be";
    public static final String WeiXin_APP_KEY = "d4624c36b6795d1d99dcf0547af5443d";

    public static void imageBrower(Context ctx, int position, String[] urls, int requestcode) {
        Intent intent = new Intent(ctx, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity) ctx).startActivityForResult(intent, requestcode);
    }

    public static String getImageName(Context context) {
        String imageName = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        imageName = String.valueOf(System.currentTimeMillis()).concat("_0_").concat(DEVICE_ID).concat(".png");
        return imageName;
    }

    /**
     * 不带扩展名
     *
     * @param context
     * @return
     */
    public static String getVideoName(Context context) {
        String imageName = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        imageName = String.valueOf(System.currentTimeMillis()).concat("_0_").concat(DEVICE_ID);
        return imageName;
    }

    public static String getMP4Name(Context context) {
        String imageName = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        imageName = String.valueOf(System.currentTimeMillis()).concat("_0_").concat(DEVICE_ID).concat(".mp4");
        return imageName;
    }

    public static String getMP3Name(Context context) {
        String imageName = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        imageName = String.valueOf(System.currentTimeMillis()).concat("_0_").concat(DEVICE_ID).concat(".mp3");
        return imageName;
    }

    public static String getAmrName(Context context) {
        String imageName = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        imageName = String.valueOf(System.currentTimeMillis()).concat("_0_").concat(DEVICE_ID).concat(".amr");
        return imageName;
    }

    /**
     * 显示大图是否可编辑
     *
     * @param
     * @param position 所选图片当前位置
     * @param urls     所有图片的地址
     * @date 2015年11月10日上午10:00:42
     */
    public static void imageBrower(Context ctx, int position, String[] urls, boolean isEditor) {
        Intent intent = new Intent(ctx, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_DELETE, isEditor);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void imageBrower(String timerDesc, Context ctx, int position, String[] urls, String[] urlsId, boolean isEditor) {
        Intent intent = new Intent(ctx, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_DELETE, isEditor);
        intent.putExtra("timerDesc", timerDesc);
        intent.putExtra("urlsId", urlsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void imageBrowerSetFengMain(Context ctx, int position, String[] urls, boolean isEditor, String glaryId, TransFerImagesBean transBean) {
        Intent intent = new Intent(ctx, ImagePagerSettingFengMianActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra("glaryId", glaryId);
        intent.putExtra("transBean", transBean);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_DELETE, isEditor);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static final String SIGLE_WHEEL_TITLE = "sigleWheelTitle";// 单轮标题

    public static final String SIGLE_WHEEL_VALUE = "sigleWheelContent";// 单轮所选内容

    public static final String SIGLE_WHEEL_BACK_VALUE = "sigleWheelBackValue";// 单轮所选内容反回值

    public static final int CHAT_PIC_NUM = 9;// 聊天图片发送
    public static final int ACT_PIC_NUM = 7;// 模特合建作品上传图片
    public static final int TOUSU_PIC_NUM = 9;// 投诉上传图片
    public static final int MODEL_PIC_NUM = 9;// 模特合建作品上传图片
    public static final int MODEL_HEADPIC_NUM = 3;// 编辑头像最多3张
    public static final String TRANS_PIC_NUM = "picNum";// 图片传参数数量常量
    public static final String IS_DELE_IMAGES = "isDeleteImages";// 是否删掉图片操作
    public static final String IMAGE_BROADCAST_LISTENER = "com.xy.sg.image.listener";// 图片操作的监听
    public static final String XMPP_LOGIN_ACTION = "com.xy.sg.xmpp.login";// openfire login
    public static final String NOTICE_REQUEST_GROUP = "com.xy.sg.notice.request.group";// openfire login

    public static final String PHOTOES_CAMERA = "camera";// 拍照图片
    public static final String PHOTOES_GALARY = "galary";// 相册图片
    public static final String ACTION_GOD_RECORD = "com.sg.record.forgod.action";// 大神认证录音
    public static final String EDITOR_TEXT_TITLE = "editorContentTitle";// 文本编辑页面标题
    public static final String TEXT_EDITOR_NUM = "wordNum";// 文本编辑框内容数量
    public static final String ACTION_LOGIN_SUCCESS = "com.sg.login.success";// 登录成功action
    public static final String ACTION_LOGIN_FAILTH = "com.sg.login.failth";// 登录失败action
    public static final String ACTION_LOGIN_BACK_IS_SUCCESS_FLAG = "isSuccess";// 登录是否成功
    public static final String ACTION_EDITOR_USER_INFO = "com.sg.editor.userinfo";// 编辑用户action
    public static final String[] CITY_STRICT = {"北京", "上海", "天津", "重庆"};// 四个直辖市
    public static List<Map<String, String>> COUNTRY_DATA = (List<Map<String, String>>) new JsonUtil().from(Country.txt);// 获取国家数据
    public static final String ACTION_PRICE_SUCCESS = "add_price_success";// 添加报价成功
    public static final int ADD_PRICE = 200;
    public static final int EDIT_PRICE = 300;
    public static final int DELETE_PRICE = 400;
    public static final int SORT_PRICE = 500;
    public static final String EDIT_OR_QUERY_FLAG = "editOrQueryFlag";
    public static final String EDIT_INFO_FLAG = "EDIT_INFO_FLAG";// 进入编辑页面
    public static final String QUERY_INFO_FLAG = "QUERY_INFO_FLAG";// 进入查看页面
    public static final String ENROLL_SUCCESS = "ENROLL_SUCCESS";// 进入查看页面
    public static final String ACTION_RECRUIT_CHANGE = "action_recruit_change";
    public static final String JUBAO_TYPE_ZUOPING_TUPIAN = "1";// 作品图片(举报类型)
    public static final String JUBAO_TYPE_YUNHU = "2";// 用户(举报类型)
    public static final String JUBAO_TYPE_XIANGCE = "3";// 相册(举报类型)
    public static final String JUBAO_TYPE_TONGGAO = "4";// 通告(举报类型)
    public static final String JUBAO_TYPE_CIRCLE = "5";// 朋友圈(举报类型)
    public static final String JUBAO_TYPE_DISC_GROUP = "6";// 讨论组(举报类型)
    public static final String COLLECT_STATUS_CHANGE = "collect_status_change";// 通告收藏状态改变
    public static final String MODEL_ATTENTION_STATUS_CHANGE = "model_attention_status_change";// 模特关注状态改变
    public static final String CM_ATTENTION_STATUS_CHANGE = "cm_attention_status_change";// 摄影师关注状态改变
    public static final String COMMON_URL = "http://appweb.moreidols.com";
    public static final String WHO_IS_URL = COMMON_URL + "/html/about/whois.html";
    public static final String MODEL_FAQ_URL = COMMON_URL + "/html/help/model.html";
    public static final String CM_FAQ_URL = COMMON_URL + "/html/help/photography.html";
    public static final String JJR_FAQ_URL = COMMON_URL + "/html/help/broker.html";
    public static final String ACTION_RECORDER_SUCCESS = "RECORDER_SUCCESS";
    public static final String ACTION_RECORDER_CIRCLE = "action_record_circle";
    public static final String ACTION_RECORDER_CIRCLE_COMMENT = "action_record_circle_comment";
    public static final String MAKE_MONEY = COMMON_URL + "/html/about/makemoney.html";
    public static final String ABOUT_HELP = COMMON_URL + "/html/about/help.html";

    /**
     * 本地数据库版本控制
     */
    public static final int DATABASE_VERSION = 5;

    /**
     * 消息类型
     */
    public static final String NEWS_DYNAMIC = "10";//动态
    public static final String NEWS_CHAT = "0";//聊天
    public static final String GROUP_CHAT = "999";//群组聊天
    public static final String TARGET_MEMBER = "1";//跳转到该模特报名的通告[招募N]人员管理页面
    public static final String TARGET_REMOVE_APPLY = "2";//跳转申请解除录用界面
    public static final String TARGET_REMOVE_SUCCESS = "3";//跳转到成功解除录用记录页面
    public static final String TARGET_BALANCE = "4";//跳转到结算付款界面
    public static final String TARGET_SHOW_EVALUATE = "5";//跳转到查看评价界面
    public static final String TARGET_NOTICE_DETAIL = "6";//跳转到被邀请报名的通告详情
    public static final String TARGET_HIRED = "7";//跳转被录用界面
    public static final String TARGET_INCOME = "8";//跳转查看收入界面
    public static final String NEWS_SYSTEM = "9";//系统
    public static final String TARGET_CAMERA_ENMPLOM_MODEL = "12";//摄影师申请解除录用
    public static final String TARGET_MODEL_REFUSE_ENMPLOM_MODEL = "13";//模特解拒绝除录用
    public static final String TARGET_JINPAI_ACT = "14";//竞拍活动
    public static final String TARGET_FREE_ACT = "15";//送礼活动
    public static final String TARGET_FUFEI_ACT = "16";//付费活动
    public static final String TARGET_TAKOUT_MEMBER = "17";//通告群踢人通知
    public static final String TARGET_CLOSE_NO = "18";//通告封号通知
    public static final String TARGET_XIADAN_ACT = "32";//下单通知信息
    public static final String TARGET_XIADAN_FEEDBACK_ACT = "33";//下单通知信息回执
    public static final String TARGET_XIADAN_JIESUAN_ACT = "34";//结算通知信息回执
    public static final String TARGET_XIADAN_CANCEL_ACT = "35";//取消订单信息回执
    public static final String TARGET_XIADAN_APPLY_ACT = "36";//申请退款信息回执
    public static final String TARGET_XIADAN_APPLY_REPORT_ACT = "37";//申请退款操作成功后信息回执对方
    public static final String TARGET_XIADAAN_SERVING = "999";//服务中
    public static final String TARGET_XIADAAN_CLOSE = "998";//已关闭
    public static final String TARGET_XIADAAN_REFUSE = "997";//已拒绝
    public static final String TARGET_XIADAAN_WAITTING_SERVICE = "996";//待服务
    public static final String TARGET_XIADAAN_PASSED = "995";//已过期
    public static final String TARGET_QIANG_PUSH = "40";//抢单订单生成推送


    public static final String NEWS_READ = "0";//已读消息
    public static final String NEWS_UNREAD = "1";//未读消息
    //用于操作通告消息是否操作过（同意或拒绝）
    public static final String NEWS_OPERATOR = "2";

    public static final String NEWS_END = "0";//发送消息
    public static final String NEWS_RECIEVE = "1";//接收消息
    public static final String NEWS_SEND_FAITH = "-1";//消息发送失败
    public static final String NEWS_SENDING = "0";//消息发送中
    public static final String NEWS_SEND_SCUESS = "1";//消息发送成功
    public static final String NEWS_SHOW = "1";//消息显示
    public static final String NEWS_HIDE = "-1";//消息不显示
    public static final int REQUEST_REGISTER_LOGIN_XMPP = 6;//登录openfire服务标识
    public static final int REQUEST_REGISTER_LOGIN_UNMEM = 7;//友盟账号绑定

    public static final String LOCAL_IMAGE_DMO = "res.madoufans.com";//阿里云本地服务图片存放在址
    public static final String LOCAL_SMAL_IMAGE = "@1e_240w_240h_1c_0i_1o_90Q_1x.jpg";//阿里云本地服务图片存放缩略图在址
    public static final String ALIY_IMAGE_DMO = "app-sgshow.oss-cn-hangzhou.aliyuncs.com";//阿里云服务原图片存放在址
    public static final String WEB_IMAGE_DMO = "wxcdn.moreidols.com";//阿里云网页版图片存放在址
    public static final String WEB_SMALL_IMAGE_DMO = "@2o_1l_1000w_90q.src";//网页版缩略图片存放在址

    private String title = "";
    private String subTitle = "";
    private String shareUrl = "http://www.moreidols.com/";
    private String iconUrl = "";
    public static final String SHARE_TITLE_KEY = "title";
    public static final String SHARE_SUBTITLE_KEY = "subTitle";
    public static final String SHARE_URL_KEY = "shareUrl";
    public static final String SHARE_ICONURL_KEY = "iconUrl";
    public static final String INIT_YEAR = "initYear";
    public static final String INIT_MONTH = "initMonth";
    public static final String INIT_DAY = "initDay";

    public static final String AGREEMENT_URL = COMMON_URL + "/html/about/agreement.html";
    public static final String USER_TYPE_JSON = "USER_TYPE_JSON_CACHE";
    public static final String BROADCAST_ACTION_PAYMENT_FOR_CHAT = "cn.com.xygame.sg.pay.for.use.chat";

    public static final String EDITOR_DISC_GROUP_NAME = "editorDiscGroupName";
    public static final String LOSE_DISC_GROUP = "loseDiscGroup";
    public static final String EXIT_DISC_GROUP = "quitDiscGroup";
    public static final String ADD_DISC_GROUP = "addDiscGroup";
    public static final String KIKT_DISC_GROUP = "kiktDiscGroup";

    public static SSLContext getSSLContext() {
        // 生成SSLContext对象
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");
            // 从assets中加载证书
            InputStream inStream = SGApplication.getInstance().getAssets().open("ios_cer.cer");

            // 证书工厂
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate cer = cerFactory.generateCertificate(inStream);

            // 密钥库
            KeyStore kStore = KeyStore.getInstance("PKCS12");
            kStore.load(null, null);
            kStore.setCertificateEntry("trust", cer);// 加载证书到密钥库中

            // 密钥管理器
            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(kStore, null);// 加载密钥库到管理器

            // 信任管理器
            TrustManagerFactory tFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tFactory.init(kStore);// 加载密钥库到信任管理器

            // 初始化
            sslContext.init(keyFactory.getKeyManagers(), tFactory.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * 信任所有主机-对于任何证书都不做检查
     */
    public static void trustAllHosts() {
    // Create a trust manager that does not validate certificate chains
    // Android 采用X509的证书信息机制
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }


            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }


            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};


    // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<GodLableBean> getGodLableDatas(String jsonStr){
        List<GodLableBean> fuFeiDatas = new ArrayList<>();
        if (!TextUtils.isEmpty(jsonStr)){
            try {
                JSONArray array = new JSONArray(jsonStr);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    GodLableBean item = new GodLableBean();
                    item.setTitleId(StringUtils.getJsonValue(object, "titleId"));
                    item.setTitleName(StringUtils.getJsonValue(object, "titleName"));
                    item.setPrice(StringUtils.getJsonValue(object, "titlePrice"));
                    item.setLocalIndex(StringUtils.getJsonValue(object, "locIndex"));
                    fuFeiDatas.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fuFeiDatas;
    }

    public static int orderStatus(String startTime,String orderStatus,String payStatus){
        int order=Integer.parseInt(orderStatus);
        int pay=Integer.parseInt(payStatus);
//        boolean unfinished=order==6||order==7||(order==2&&pay==1);
//        boolean unfinished=order==2&&pay==1;
        boolean unfinished=order==7||(order==2&&pay==1);
        if (pay==0){//未支付状态
            return 0;
        }else if (order==1&&CalendarUtils.getStartTime(startTime).after(new Date())){//待确认
            return 1;
        } else if (order==2&&pay==1&&CalendarUtils.getStartTime(startTime).after(new Date())){//待服务
            return 2;
        }else if (!CalendarUtils.getStartTime(startTime).after(new Date())&&unfinished){//服务中
            return 3;
        }else if (order==8||pay==3){//已完成
            return 4;
        }else {//已取消
            return 5;
        }
    }

    public static void setupLocation(Context context){
        ThreadPool.getInstance().excuseThread(new JugmentUse(context));
    }

    private static class JugmentUse implements Runnable {
       private ProvinceBean areaBean;
       private Context context;
        public JugmentUse(Context context){
            this.context=context;
        }
        @Override
        public void run() {
            List<ProvinceBean> datas = (List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0);
            for (ProvinceBean it : datas) {
                it.get();
                if (BaiduPreferencesUtil.getProvice(context).contains(it.getProvinceName())) {
                    areaBean = it;
                    List<CityBean> cityBeans =AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(areaBean.getProvinceCode()));
                    for (CityBean city:cityBeans){
                        city.get();
                        if (BaiduPreferencesUtil.getCity(context).contains(city.getCityName())){
                            areaBean.setCityBean(city);
                            break;
                        }
                    }
                    break;
                }
            }
            CacheService.getInstance().cacheCommenAreaBean(COMMEN_AREA_FLAG,areaBean);
        }
    }

    public static final String BG_TIP_LOL = "请上传您的段位截图（需包含个人ID）并填写LOL相关的说明，申请线下大神还需在【技能视频】上传自拍视频";
    public static final String BG_TIP_VOICE ="请上传封面照并在【技能语音】上传自己的语音哦";
    public static final String BG_TIP_VIDEO ="请上传封面照并在【技能视频】上传自拍视频";
    public static final String BG_TIP_MOVING ="请上传封面照并在【技能视频】上传自拍视频";
    public static final String BG_TIP_EATING ="请上传本人的吃货时刻美图并在【技能视频】上传自拍视频";
    public static final String BG_TIP_LVYOUO ="请上传您的旅游照片（风景图里要有本人出镜哦~）";
    public static final String BG_TIP_COMM ="请上传封面照";
    public static final String DEFINE_LOL_ID ="1_9";
    public static final String COMMEN_AREA_FLAG ="CommenAreaFlag";

    public static String getMoneyFormat(String moneyStr){
        DecimalFormat df   = new DecimalFormat("######0.00");
        Double total=Double.parseDouble(moneyStr);
        String totalFoma=df.format(total);
        return totalFoma;
    }
}

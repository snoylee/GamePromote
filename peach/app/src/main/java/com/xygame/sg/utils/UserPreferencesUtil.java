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

import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.service.CacheService;

import base.action.CenterRepo;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月5日
 * @action [用户信息分享类]
 */
public class UserPreferencesUtil {

    private static Context cotext;

    public static void setUserToken(Context context, String userToken) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("userToken", userToken);
        editor.commit();
    }

    public static String getUserToken(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("userToken", null);
    }

    public static void setCheckOff(Context context, String CheckOff) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("CheckOff", CheckOff);
        editor.commit();
    }

    public static String getCheckOff(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("CheckOff", null);
    }

    public static void setUserNickName(Context context, String nickName) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("nickName", nickName);
        editor.commit();
    }

    public static String getUserNickName(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("nickName", null);
    }

    public static void setCellPhone(Context context, String CellPhone) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("CellPhone", CellPhone);
        editor.commit();
    }

    public static String getCellPhone(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("CellPhone", null);
    }

    public static void setHeadPic(Context context, String HeadPic) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("HeadPic", HeadPic);
        editor.commit();
    }

    public static String getHeadPic(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("HeadPic", null);
    }

    public static void setPwd(Context context, String Pwd) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("Pwd", Pwd);
        editor.commit();
    }

    public static String getPwd(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("Pwd", null);
    }

    public static void setSex(Context context, String Sex) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("Sex", Sex);
        editor.commit();
    }

    public static String getSex(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("Sex", null);
    }

    public static void setHeartText(Context context, String HeartText) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("HeartText", HeartText);
        editor.commit();
    }

    public static String getHeartText(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("HeartText", null);
    }

    public static void setUserPin(Context context, String UserPin) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserPin", UserPin);
        editor.commit();
    }

    public static String getUserPin(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserPin", null);
    }

    public static void setBirthday(Context context, String Birthday) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("Birthday", Birthday);
        editor.commit();
    }

    public static String getBirthday(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("Birthday", null);
    }

    public static void setUserType(Context context, String UserType) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserType", UserType);
        editor.commit();
    }

    public static String getUserType(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserType", "");
    }

    public static void setorderExpireTime(Context context, String orderExpireTime) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("orderExpireTime", orderExpireTime);
        editor.commit();
    }

    public static String getorderExpireTime(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("orderExpireTime", "");
    }

    public static void setpayExpireTime(Context context, String payExpireTime) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("payExpireTime", payExpireTime);
        editor.commit();
    }

    public static String getpayExpireTime(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("payExpireTime", "");
    }

    public static void setUserId(Context context, String UserId) {
//        CacheService.getInstance().cacheUserId(ConstTaskTag.CACHE_USER_ID,UserId);
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserId", UserId);
        CenterRepo.getInsatnce().getRepo().put("userId", UserId);
        editor.commit();
    }

    public static void init(Context context) {
        cotext = context;
    }

//    public static String getUserId() {
////        return CacheService.getInstance().getCacheUserId(ConstTaskTag.CACHE_USER_ID);
//        SharedPreferences loginShare = context.getSharedPreferences(
//                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
//        String userid = loginShare.getString("UserId", null);
//    }

    public static String getUserId(Context context) {
//        return CacheService.getInstance().getCacheUserId(ConstTaskTag.CACHE_USER_ID);
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        String userid = loginShare.getString("UserId", null);
//        if (userid != null && !userid.equals("") && !userid.equals("null")) {
//            CenterRepo.getInsatnce().getRepo().put("userId", userid);
//        }
        return userid;
    }

    public static void setUserVerifyStatus(Context context, String UserVerifyStatus) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserVerifyStatus", UserVerifyStatus);
        editor.commit();
    }

    public static String getUserVerifyStatus(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserVerifyStatus", null);
    }

    public static void setUserLoginType(Context context, String UserLoginType) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserLoginType", UserLoginType);
        editor.commit();
    }

    public static String getUserLoginType(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserLoginType", null);
    }

    public static void setOtherPlatfromId(Context context, String OtherPlatfromId) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("OtherPlatfromId", OtherPlatfromId);
        editor.commit();
    }

    public static String getOtherPlatfromId(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("OtherPlatfromId", null);
    }

    public static void setIsFirstGodQinagReback(Context context, Boolean IsOnline) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putBoolean("FirstGodQinagReback", IsOnline);
        editor.commit();
    }
    public static Boolean isFirstGodQinagReback(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getBoolean("FirstGodQinagReback", false);
    }

    public static void setIsReadGetMoney(Context context, Boolean IsOnline) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putBoolean("isReadGetMoney", IsOnline);
        editor.commit();
    }
    public static Boolean isReadGetMoney(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getBoolean("isReadGetMoney", false);
    }

    public static void setIsOnline(Context context, Boolean IsOnline) {
        if (!IsOnline){
            UserPreferencesUtil.setUserAttentCount(context,null);
            UserPreferencesUtil.setUserVisitCount(context, null);
            UserPreferencesUtil.setUserAge(context, null);
            UserPreferencesUtil.setSex(context, null);
            UserPreferencesUtil.setHeartText(context, null);
            UserPreferencesUtil.setHeadPic(context, null);
            UserPreferencesUtil.setUserPin(context, null);
            UserPreferencesUtil.setUserNickName(context, null);
            UserPreferencesUtil.setUserVideoAuthUrl(context, null);
            UserPreferencesUtil.setUserVideoAuth(context, null);
            UserPreferencesUtil.setUserIDAuth(context, null);
            UserPreferencesUtil.setUserCardAuth(context, null);
            UserPreferencesUtil.setUserId(context,null);
        }
        CacheService.getInstance().cacheUserStatus(ConstTaskTag.CACHE_USER_STATUS,IsOnline);
//        SharedPreferences loginShare = context.getSharedPreferences(
//                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
//        Editor editor = loginShare.edit();
//        editor.putBoolean("IsOnline", IsOnline);
//        editor.commit();
    }

    public static Boolean isOnline(Context context) {
//        SharedPreferences loginShare = context.getSharedPreferences(
//                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
//        return loginShare.getBoolean("IsOnline", false);
        boolean flag= CacheService.getInstance().getCacheUserStatus(ConstTaskTag.CACHE_USER_STATUS);
        return flag;
    }

    public static void setHavePayPassword(Context context, String havePayPassword) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("havePayPassword", havePayPassword);
        editor.commit();
    }

    public static Boolean havePayPassword(Context context) {
        boolean flag = false;
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        String value = loginShare.getString("havePayPassword", null);
        if ("1".equals(value)) {
            flag = true;
        }
        return flag;
    }

    public static String isGetPayPassword(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("havePayPassword", null);
    }

    public static Boolean isModel(Context context) {
        boolean flag = false;
        String typeStr = getUserType(context);
        if (Constants.CARRE_MODEL.equals(typeStr) || Constants.PRO_MODEL.equals(typeStr)) {
            flag = true;
        } else if (Constants.CARRE_PHOTOR.equals(typeStr)) {
            flag = false;
        }
        return flag;
    }

    public static void judgMentFristRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("share", Context.MODE_MULTI_PROCESS);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            SGNewsBean msg = new SGNewsBean();
            msg.setMsgContent("欢迎来到模范儿大家庭，这里有大量高颜值美女和帅哥，还有技术倍儿棒的摄影大师，赶紧动动手指，约拍大片吧！！");
            msg.setNewType(Constants.NEWS_SYSTEM);
            msg.setTimestamp(String.valueOf(System.currentTimeMillis()));
            NewsEngine.inserChatNew(context, msg);
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }

    public static void setRefract(Context context, Boolean flag) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putBoolean("isRefract", flag);
        editor.commit();
    }

    public static Boolean isRefract(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getBoolean("isRefract", false);
    }


    public static void setIsNewConnect(Context context, Boolean isNewConnect) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putBoolean("isNewConnect", isNewConnect);
        editor.commit();
    }

    public static Boolean isNewConnect(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getBoolean("isNewConnect", false);
    }

    public static void setIsRefreshRequest(Context context, Boolean IsRefreshRequest) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putBoolean("IsRefreshRequest", IsRefreshRequest);
        editor.commit();
    }

    public static Boolean isRefreshRequest(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getBoolean("IsRefreshRequest", false);
    }

    public static void setUserAge(Context context, String UserAge) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserAge", UserAge);
        editor.commit();
    }

    public static String getUserAge(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserAge", null);
    }

    public static void setUserIDAuth(Context context, String UserIDAuth) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserIDAuth", UserIDAuth);
        editor.commit();
    }

    public static String getUserIDAuth(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserIDAuth", null);
    }

    public static void setExpertAuth(Context context, String ExpertAuth) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("ExpertAuth", ExpertAuth);
        editor.commit();
    }

    public static String getExpertAuth(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("ExpertAuth", null);
    }

    public static void setUserCardAuth(Context context, String UserCardAuth) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserCardAuth", UserCardAuth);
        editor.commit();
    }

    public static String getUserCardAuth(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserCardAuth", null);
    }

    public static void setUserVideoAuth(Context context, String UserVideoAuth) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserVideoAuth", UserVideoAuth);
        editor.commit();
    }

    public static String getUserVideoAuth(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserVideoAuth", null);
    }

    public static void setUserVideoAuthUrl(Context context, String UserVideoAuthUrl) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserVideoAuthUrl", UserVideoAuthUrl);
        editor.commit();
    }

    public static String getUserVideoAuthUrl(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserVideoAuthUrl", null);
    }

    public static void setUserAttentCount(Context context, String UserAttentCount) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserAttentCount", UserAttentCount);
        editor.commit();
    }

    public static String getUserAttentCount(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserAttentCount", null);
    }

    public static void setUserVisitCount(Context context, String UserVisitCount) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserVisitCount", UserVisitCount);
        editor.commit();
    }

    public static String getUserVisitCount(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserVisitCount", null);
    }

    public static void setUserIDAuthRefuseReason(Context context, String UserIDAuthRefuseReason) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserIDAuthRefuseReason", UserIDAuthRefuseReason);
        editor.commit();
    }

    public static String getUserIDAuthRefuseReason(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserIDAuthRefuseReason", null);
    }

    public static void setLoginName(Context context, String LoginName) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("LoginName", LoginName);
        editor.commit();
    }

    public static String getLoginName(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("LoginName", null);
    }

    public static void setLoginPwd(Context context, String LoginPwd) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("LoginPwd", LoginPwd);
        editor.commit();
    }

    public static String getLoginPwd(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("LoginPwd", null);
    }

    public static void setRegisterType(Context context, String RegisterType) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("RegisterType", RegisterType);
        editor.commit();
    }

    public static String getRegisterType(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("RegisterType", null);
    }

    public static void setUserVideoAuthRefuseReason(Context context, String UserVideoAuthRefuseReason) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        Editor editor = loginShare.edit();
        editor.putString("UserVideoAuthRefuseReason", UserVideoAuthRefuseReason);
        editor.commit();
    }

    public static String getUserVideoAuthRefuseReason(Context context) {
        SharedPreferences loginShare = context.getSharedPreferences(
                Constants.USER_PREFERNCE, Context.MODE_MULTI_PROCESS);
        return loginShare.getString("UserVideoAuthRefuseReason", null);
    }
}

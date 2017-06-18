/*
 * 文 件 名:  SGBaseFragment.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月2日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.xygame.sg.activity.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.im.IActivitySupport;
import com.xygame.sg.task.init.AuthPicKeyParams;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.LoginConfig;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月2日
 * @action  实现全局的控制
 */
public class SGBaseFragment extends Fragment implements Handler.Callback {

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ConstTaskTag.CONST_TAG_PUBLIC: {
                ResponseBean data = (ResponseBean) msg.obj;
                if (null == data) {
                    responseFaith();
                    Toast.makeText(getActivity(), "网络请求失败！", Toast.LENGTH_SHORT).show();
                } else {
                    getResponseBean(data);
                }
                break;
            }
            case Constants.REQUEST_REGISTER_LOGIN_XMPP:{
                xmppRespose();
                break;
            }
            default:
                break;
        }
        return false;
    }

    protected void xmppRespose() {
    }

    protected void getResponseBean(ResponseBean data) {
    }

    protected void responseFaith() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        System.runFinalization();
//        System.gc();
    }

    //延迟加载用开始
    protected boolean isVisible;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }
    /**
     * 请求数据
     */
    protected void lazyLoad(){

    }
    //延迟加载用结束
}


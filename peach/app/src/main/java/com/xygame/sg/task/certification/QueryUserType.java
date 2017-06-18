/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.certification;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.ProModelIdentyActivity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.List;

import base.action.Action.Param;
import base.action.asyc.BackRunnable;
import base.action.asyc.CallbackRunnable;
import base.action.asyc.Compt;


public class QueryUserType extends NetWorkUtil {


    @Override
    public String runResult(String result) {
        System.out.println(result);
        return super.runResult(result);
    }


    @Override
    public String runUrl(String url) {
        System.out.println(url);
        return super.runUrl(url);
    }


    @Override
    public void callback(Param aparam, Object object) {
        ShowMsgDialog.cancel();
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            String resStr = aparam.getResultunit().get("record").toString();
            if (resStr != "null") {
//                String jsonStr = JSON.toJSONString(aparam.getResultunit().get("record"));
//                List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//                judge(aparam, resultList);
            }
        } else {
            String msg = aparam.getResultunit().getRawMap().get("msg");
            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        super.callback(aparam, object);
    }


    @Override
    public Object run(String methodname, List<String> params, Param aparam) {
        ShowMsgDialog.showNoMsg(aparam.getFragment().getActivity(), false);
        return super.run(methodname, params, aparam);
    }

    private void judge(final Param aparam, List<IdentyBean> resultList) {
        if (resultList == null || resultList.size() == 0) {
            return;
        }
        IdentyBean identyBean = resultList.get(0);
        int authStatus = identyBean.getAuthStatus();
        int userType = identyBean.getUserType();
        String refuseReason = identyBean.getRefuseReason();
        if (resultList.size() == 1) {
            switch (userType) {
                case 2://普通模特
                    switch (authStatus) {
                        case 0://未申请认证
                            aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ModelIdentyFirstActivity.class));
                            break;
                        case 1://审核中
                            verifing(aparam, refuseReason);
                            break;
                        case 2://普通模特认证通过
                            modelVerified(aparam, "恭喜, 您已通过模特认证!");
                            break;
                        case 3://普通模特申请认证拒绝
                            modelVerifyRefused(aparam, refuseReason);
                            break;
                    }
                    break;
                case 8://摄影师
                    switch (authStatus) {
                        case 0:
                            aparam.getActivity().startActivity(new Intent(aparam.getActivity(), CMIdentyFirstActivity.class));
                            break;
                        case 1:
                            verifing(aparam, refuseReason);
                            break;
                        case 2:
                            proOrCmVerified(aparam, "恭喜, 您已通过摄影师认证!");
                            break;
                        case 3:
                            cmVerifyRefused(aparam, refuseReason);
                            break;
                    }
            }
        } else {
            identyBean = resultList.get(1);
            authStatus = identyBean.getAuthStatus();
            refuseReason = identyBean.getRefuseReason();
            switch (authStatus) {
                case 1:
                    verifing(aparam, refuseReason);
                    break;
                case 2:
                    proOrCmVerified(aparam, "恭喜, 您已通过高级模特认证!");
                    break;
                case 3:
                    proModelVerifyRefused(aparam, refuseReason);
                    break;
            }
        }
    }

    /**
     * 摄影师或高级模特申请认证通过
     * @param aparam
     * @param refuseReason
     */
    private void proOrCmVerified(final Param aparam, String refuseReason){
        UserPreferencesUtil.setUserVerifyStatus(aparam.getActivity(), Constants.USER_VERIFIED_CODE);
        ShowMsgDialog.showOne(aparam.getActivity(), refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
            }
        });
        new Compt().putTask(new BackRunnable() {
            @Override
            public void run() throws Exception {
                Thread.sleep(5000);
            }
        }, new CallbackRunnable() {
            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                ShowMsgDialog.cancel();
                return false;
            }
        }).run();
    }

    /**
     * 普通模特申请认证被拒，重新申请
     * @param aparam
     * @param refuseReason
     */
    private void modelVerifyRefused(final Param aparam, String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(aparam.getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
        ShowMsgDialog.showOne(aparam.getActivity(), "抱歉，您提交的资料未通过模特认证，原因是：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
                aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ModelIdentyFirstActivity.class));

            }
        });
        new Compt().putTask(new BackRunnable() {
            @Override
            public void run() throws Exception {
                Thread.sleep(5000);
            }
        }, new CallbackRunnable() {
            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                if (ShowMsgDialog.isShowing()){
                    ShowMsgDialog.cancel();
                    aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ModelIdentyFirstActivity.class));
                }
                return false;
            }
        }).run();
    }
    /**
     * 高级模特申请认证被拒，重新申请
     * @param aparam
     * @param refuseReason
     */
    private void proModelVerifyRefused(final Param aparam, String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(aparam.getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
        ShowMsgDialog.showOne(aparam.getActivity(), "抱歉，您提交的资料未通过模特认证，原因是：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
                aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ProModelIdentyActivity.class));
            }
        });
        new Compt().putTask(new BackRunnable() {
            @Override
            public void run() throws Exception {
                Thread.sleep(5000);
            }
        }, new CallbackRunnable() {
            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                if (ShowMsgDialog.isShowing()){
                    ShowMsgDialog.cancel();
                    aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ProModelIdentyActivity.class));
                }
                return false;
            }
        }).run();
    }
    /**
     * 摄影师申请认证被拒，重新申请
     * @param aparam
     * @param refuseReason
     */
    private void cmVerifyRefused(final Param aparam, String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(aparam.getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
        ShowMsgDialog.showOne(aparam.getActivity(), "抱歉，您提交的资料未通过摄影师认证，原因是：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
                aparam.getActivity().startActivity(new Intent(aparam.getActivity(), CMIdentyFirstActivity.class));

            }
        });
        new Compt().putTask(new BackRunnable() {
            @Override
            public void run() throws Exception {
                Thread.sleep(5000);
            }
        }, new CallbackRunnable() {
            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                if (ShowMsgDialog.isShowing()){
                    ShowMsgDialog.cancel();
                    aparam.getActivity().startActivity(new Intent(aparam.getActivity(), CMIdentyFirstActivity.class));
                }
                return false;
            }
        }).run();
    }
    /**
     * 普通模特申请认证通过 跳转申请高级模特
     * @param aparam
     * @param s
     */
    private void modelVerified(final Param aparam, String s) {
        UserPreferencesUtil.setUserVerifyStatus(aparam.getActivity(), Constants.USER_VERIFIED_CODE);
        ShowMsgDialog.showOne(aparam.getActivity(), s, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                if (ShowMsgDialog.isShowing()){
                    ShowMsgDialog.cancel();
                    aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ProModelIdentyActivity.class));
                }
            }
        });
        new Compt().putTask(new BackRunnable() {
            @Override
            public void run() throws Exception {
                Thread.sleep(5000);
            }
        }, new CallbackRunnable() {
            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                if (ShowMsgDialog.isShowing()){
                    ShowMsgDialog.cancel();
                    aparam.getActivity().startActivity(new Intent(aparam.getActivity(), ProModelIdentyActivity.class));
                }
                return false;
            }
        }).run();
    }

    /**
     * 普通模特、高级模特或摄影师申请认证中
     * @param aparam
     * @param refuseReason
     */
    private void verifing(final Param aparam, String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(aparam.getActivity(), Constants.USER_VERIFING_CODE);
        if (StringUtils.isEmpty(refuseReason)) {
            refuseReason = "";
        }
        ShowMsgDialog.showOne(aparam.getActivity(), "审核中：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
            }
        });
        new Compt().putTask(new BackRunnable() {
            @Override
            public void run() throws Exception {
                Thread.sleep(5000);
            }
        }, new CallbackRunnable() {
            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                ShowMsgDialog.cancel();
                return false;
            }
        }).run();
    }

}

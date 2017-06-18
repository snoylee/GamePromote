/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.certification;

import android.widget.Toast;

import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.JJRIdentyFirstActivity;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;

import base.action.Action.Param;


public class IdCardExists extends NetWorkUtil {


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
//            String resStr = aparam.getResultunit().get("record").toString();
//            if (resStr != "null") {
//                String jsonStr = JSON.toJSONString((List<Map>) (aparam.getResultunit().get("record")));
//                List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//                judge(aparam, resultList);
//            }
            if (aparam.getActivity() instanceof ModelIdentyFirstActivity){
                ((ModelIdentyFirstActivity) aparam.getActivity()).responseIdCheck();
            } else if (aparam.getActivity() instanceof CMIdentyFirstActivity){
                ((CMIdentyFirstActivity) aparam.getActivity()).responseIdCheck();
            }else if (aparam.getActivity() instanceof JJRIdentyFirstActivity){
                ((JJRIdentyFirstActivity) aparam.getActivity()).responseIdCheck();
            }

        } else {
            String msg = aparam.getResultunit().getRawMap().get("msg");
            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        super.callback(aparam, object);
    }


    @Override
    public Object run(String methodname, List<String> params, Param aparam) {
        ShowMsgDialog.showNoMsg(aparam.getActivity(), false);
        return super.run(methodname, params, aparam);
    }



}

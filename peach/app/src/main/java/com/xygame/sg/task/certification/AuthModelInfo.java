/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.certification;

import android.widget.Toast;

import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.personal.ModelIdentyThirdActivity;
import com.xygame.sg.activity.personal.bean.IdentyTranBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import base.action.Action.Param;


public class AuthModelInfo extends NetWorkUtil {


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
//        ShowMsgDialog.cancel();
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            ((ModelIdentyThirdActivity)aparam.getActivity()).finishUpload(aparam.getResultunit().getRawMap().get("record"));
        } else {
            String msg = aparam.getResultunit().getRawMap().get("msg");
            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        super.callback(aparam, object);
    }


    @Override
    public Object run(String methodname, List<String> params, Param aparam) {
//        ShowMsgDialog.showNoMsg(aparam.getActivity(), false);
        ModelIdentyThirdActivity activity = ((ModelIdentyThirdActivity) aparam.getActivity());
        IdentyTranBean identyTranBean = activity.getIdentyTranBean();
        PlushNoticeAreaBean areaBean=activity.getAreaBean();
        params.add("userId="+UserPreferencesUtil.getUserId(activity));
        params.add("realName="+identyTranBean.getRealName());
        params.add("idCard="+identyTranBean.getIdCard());
//        params.add("picCardHold="+identyTranBean.getHalfUrl());
        params.add("picCardFront="+identyTranBean.getFrontUrl());
        params.add("picCardBack="+identyTranBean.getBackUrl());
        if (identyTranBean.getVideoUrl()!=null){
            params.add("videoUrl="+identyTranBean.getVideoUrl());
        }
        params.add("height="+identyTranBean.getUserHeight());
        params.add("weight="+identyTranBean.getUserWeight());
        params.add("bust="+identyTranBean.getUserBust());
        params.add("waist="+identyTranBean.getUserWaist());
        params.add("hip="+identyTranBean.getUserHip());
        params.add("cup="+StringUtils.getCupId(identyTranBean.getUserCup()));
//        params.add("shosecode="+identyTranBean.getUserShoesCode());

        params.add("province="+areaBean.getProvinceId());
        params.add("city="+areaBean.getCityId());

        params.add("opusDesc="+identyTranBean.getOpusDesc());
        String picUrl = getJsonStr(identyTranBean.getUploadImages());
        params.add("picUrl="+picUrl);
        return super.run(methodname, params, aparam);
    }
    private String getJsonStr(List<PhotoesBean> datas){
        List<String> strList = new ArrayList<>();
        for (PhotoesBean it:datas){
            strList.add(it.getImageUrl());
        }
//        return JSON.toJSONString(strList);
        return null;
    }


}

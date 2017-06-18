package com.xygame.sg.task.model;

import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;

import base.action.Action;

/**
 * Created by xy on 2016/1/17.
 */
public class LoadBannerInfo extends NetWorkUtil {

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
    public void callback(Action.Param aparam, Object object) {
//        String resultCode = aparam.getResultunit().getRawMap().get("success");
//        if (Constants.RESULT_CODE.equals(resultCode)) {
//            String resStr = aparam.getResultunit().get("record").toString();
//            ModelHotFragment fragment = ((ModelHotFragment) aparam.getFragment());
//            if (resStr != "null") {
//                String jsonStr = JSON.toJSONString((List<Map>)(aparam.getResultunit().get("record")));
//                List<BannerBean> bannerList = JSON.parseArray(jsonStr, BannerBean.class);
//                fragment.responseBannerList(bannerList);
//            }
//        } else {
//            String msg = aparam.getResultunit().getRawMap().get("msg");
//            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
//        }
        super.callback(aparam, object);
    }


    @Override
    public Object run(String methodname, List<String> params, Action.Param aparam) {
        return super.run(methodname, params, aparam);
    }
}

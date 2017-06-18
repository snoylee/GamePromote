package com.xygame.sg.task.model;

import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;

import base.action.Action;

/**
 * Created by xy on 2016/1/17.
 */
public class LoadHotModelInfo extends NetWorkUtil {

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
//        ModelHotFragment fragment = ((ModelHotFragment) aparam.getFragment());
//        if (fragment.isShowLoading()){
//            ShowMsgDialog.cancel();
//        }
//        String resultCode = aparam.getResultunit().getRawMap().get("success");
//        if (Constants.RESULT_CODE.equals(resultCode)) {
//            String resStr = aparam.getResultunit().get("record").toString();
//            if (resStr != "null") {
//                String jsonStr = JSON.toJSONString((Map)(aparam.getResultunit().get("record")));
//                HotModelBean hotModelBean = JSON.parseObject(jsonStr, HotModelBean.class);
//                fragment.responseHotModel(hotModelBean);
//            }
//        } else {
//            String msg = aparam.getResultunit().getRawMap().get("msg");
//            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
//        }
        super.callback(aparam, object);
    }


    @Override
    public Object run(String methodname, List<String> params, Action.Param aparam) {
//        ModelHotFragment fragment = ((ModelHotFragment) aparam.getFragment());
//        if (fragment.isShowLoading()){
//            ShowMsgDialog.showNoMsg(aparam.getFragment().getActivity(), true);
//        }
        return super.run(methodname, params, aparam);
    }
}

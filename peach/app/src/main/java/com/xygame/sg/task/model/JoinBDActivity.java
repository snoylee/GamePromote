package com.xygame.sg.task.model;

import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;

import base.action.Action;

/**
 * Created by xy on 2016/1/17.
 */
public class JoinBDActivity extends NetWorkUtil {

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
        ShowMsgDialog.cancel();
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        String code = aparam.getResultunit().getRawMap().get("code");
        String msg = aparam.getResultunit().getRawMap().get("msg");
//        if (Constants.RESULT_CODE.equals(resultCode)) {
//            CommonWebViewActivity activity = ((CommonWebViewActivity) aparam.getActivity());
//            activity.resAct(code,msg);
//        } else {
//            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
//        }
        CommonWebViewActivity activity = ((CommonWebViewActivity) aparam.getActivity());
        activity.resAct(code,msg);
        super.callback(aparam, object);
    }


    @Override
    public Object run(String methodname, List<String> params, Action.Param aparam) {
        ShowMsgDialog.showNoMsg(aparam.getActivity(), true);
        return super.run(methodname, params, aparam);
    }
}

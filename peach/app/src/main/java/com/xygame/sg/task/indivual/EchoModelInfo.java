package com.xygame.sg.task.indivual;

import android.widget.Toast;

import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;
import java.util.Map;

import base.action.Action;

/**
 * Created by xy on 2015/11/21.
 */
public class EchoModelInfo extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
//        ShowMsgDialog.show(param.getActivity(), "请求中...", false);
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
//        ShowMsgDialog.cancel();
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            Map resultMap=(Map) aparam.getResultunit().get("record");
//            if (aparam.getFragment() instanceof DataTabFragment){
//                DataTabFragment dataTabFragment = (DataTabFragment) aparam.getFragment();
//                dataTabFragment.responseHandler(resultMap);
//            } else
//            if (aparam.getFragment() instanceof CMDataTabFragment){
//                CMDataTabFragment dataTabFragment = (CMDataTabFragment) aparam.getFragment();
//                dataTabFragment.responseHandler(resultMap);
//            }
        } else {
            String msg=aparam.getResultunit().getRawMap().get("msg");
            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        super.callback(aparam, object);
    }

    @Override
    public String runUrl(String url) {
        System.out.println(url);
        return super.runUrl(url);
    }

    @Override
    public String runResult(String result) {
        System.out.println(result);
        return super.runResult(result);
    }
}

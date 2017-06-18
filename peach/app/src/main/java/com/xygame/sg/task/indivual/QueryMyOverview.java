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
public class QueryMyOverview extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            Map resultMap=(Map) aparam.getResultunit().get("record");
//            if (aparam.getFragment() instanceof PersonalFragment ){
//                PersonalFragment personalFragment = (PersonalFragment) aparam.getFragment();
//                personalFragment.responseHandler(resultMap);
//            } else
//            if (aparam.getActivity() instanceof PersonInfoActivity){
//                PersonInfoActivity personInfoActivity = (PersonInfoActivity) aparam.getActivity();
//                personInfoActivity.setBanner(resultMap);
//            } else
//            if (aparam.getActivity() instanceof CMPersonInfoActivity){
//                CMPersonInfoActivity cmpersonInfoActivity = (CMPersonInfoActivity) aparam.getActivity();
//                cmpersonInfoActivity.setBanner(resultMap);
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

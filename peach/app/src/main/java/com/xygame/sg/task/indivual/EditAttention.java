package com.xygame.sg.task.indivual;

import java.util.List;
import java.util.Map;

import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.PersonalPhotoesBrowsersActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import android.widget.Toast;
import base.action.Action;

/**
 * Created by xy on 2015/11/20.
 */
public class EditAttention extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        ShowMsgDialog.showNoMsg(param.getActivity(),true);
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
    	ShowMsgDialog.cancel();
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            Map resultMap=(Map) aparam.getResultunit().get("record");

//            if (aparam.getActivity() instanceof PersonInfoActivity){
//                PersonInfoActivity personInfoActivity = (PersonInfoActivity) aparam.getActivity();
//                personInfoActivity.setAttention(resultMap);
//            } else
//            if (aparam.getActivity() instanceof CMPersonInfoActivity){
//                CMPersonInfoActivity cmpersonInfoActivity = (CMPersonInfoActivity) aparam.getActivity();
//                cmpersonInfoActivity.setAttention(resultMap);
//            }else
            if (aparam.getActivity() instanceof PersonalPhotoesBrowsersActivity){
            	PersonalPhotoesBrowsersActivity cmpersonInfoActivity = (PersonalPhotoesBrowsersActivity) aparam.getActivity();
                cmpersonInfoActivity.setAttention(resultMap);
            }
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

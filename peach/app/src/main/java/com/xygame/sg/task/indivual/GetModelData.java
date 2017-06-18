package com.xygame.sg.task.indivual;

import java.util.List;
import java.util.Map;

import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.PersonalPhotoesBrowsersActivity;
import com.xygame.sg.activity.personal.fragment.CMNoticeTabFragment;
import com.xygame.sg.activity.personal.fragment.PriceTabFragment;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import android.widget.Toast;
import base.action.Action;

/**
 * Created by xy on 2015/11/20.
 */
public class GetModelData extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            Map resultMap=(Map) aparam.getResultunit().get("record");
//            if (aparam.getActivity() instanceof PersonInfoActivity && aparam.getFragment()==null){
//                PersonInfoActivity personInfoActivity = (PersonInfoActivity) aparam.getActivity();
//                personInfoActivity.setBanner(resultMap);
//            } else
//            if (aparam.getActivity() instanceof CMPersonInfoActivity && aparam.getFragment()==null){
//                CMPersonInfoActivity cmpersonInfoActivity = (CMPersonInfoActivity) aparam.getActivity();
//                cmpersonInfoActivity.setBanner(resultMap);
//            }
//            else if (aparam.getActivity() instanceof PersonalPhotoesBrowsersActivity){
//            	PersonalPhotoesBrowsersActivity cmpersonInfoActivity = (PersonalPhotoesBrowsersActivity) aparam.getActivity();
//                cmpersonInfoActivity.parsePersonRefresh(resultMap);
//            }else
//            if (aparam.getFragment() instanceof PriceTabFragment){
//                PriceTabFragment fragment = (PriceTabFragment) aparam.getFragment();
//                fragment.parseComment(resultMap);
//            } else
//            if (aparam.getFragment() instanceof CMNoticeTabFragment){
//                CMNoticeTabFragment fragment = (CMNoticeTabFragment) aparam.getFragment();
//                fragment.parseComment(resultMap);
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

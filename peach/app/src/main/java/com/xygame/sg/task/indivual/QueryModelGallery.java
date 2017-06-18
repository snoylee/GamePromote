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
public class QueryModelGallery extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            List<Map> resultList=(List) aparam.getResultunit().get("record");
//            if (aparam.getFragment() instanceof WorkTabFragment){
//                WorkTabFragment workTabFragment = (WorkTabFragment) aparam.getFragment();
//                workTabFragment.responseHandler(resultList);
//            } else
//            if (aparam.getFragment() instanceof CMWorkTabFragment){
//                CMWorkTabFragment cmworkTabFragment = (CMWorkTabFragment) aparam.getFragment();
//                cmworkTabFragment.responseHandler(resultList);
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

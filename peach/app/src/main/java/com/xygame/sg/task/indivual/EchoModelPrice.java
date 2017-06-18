package com.xygame.sg.task.indivual;

import android.widget.Toast;

import com.xygame.sg.activity.personal.adapter.CMNoticeAdapter;
import com.xygame.sg.activity.personal.fragment.CMDataTabFragment;
import com.xygame.sg.activity.personal.fragment.CMNoticeTabFragment;
import com.xygame.sg.activity.personal.fragment.DataTabFragment;
import com.xygame.sg.activity.personal.fragment.PriceTabFragment;
import com.xygame.sg.activity.personal.fragment.WorkTabFragment;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;
import java.util.Map;

import base.action.Action;

/**
 * Created by xy on 2015/11/21.
 */
public class EchoModelPrice extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
//        ShowMsgDialog.showNoMsg(param.getActivity(), true);
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
//        ShowMsgDialog.cancel();
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            List<Map> resultList=(List) aparam.getResultunit().get("record");

//            if (aparam.getFragment() instanceof PriceTabFragment){
//                PriceTabFragment priceTabFragment = (PriceTabFragment) aparam.getFragment();
//                priceTabFragment.responseHandler(resultList);
//            }
//            else if (aparam.getFragment() instanceof CMNoticeTabFragment){
//                CMNoticeTabFragment cmNoticeTabFragment = (CMNoticeTabFragment) aparam.getFragment();
//                cmNoticeTabFragment.responseHandler(resultList);
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

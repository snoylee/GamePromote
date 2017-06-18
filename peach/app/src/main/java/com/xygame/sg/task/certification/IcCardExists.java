package com.xygame.sg.task.certification;

import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;

import base.action.Action;
import base.action.task.Http;

/**
 * Created by minhua on 2015/11/12.
 */
public class IcCardExists extends Http {
    @Override
    public Object run(String methodname, List<String> params, Action.Param aparam) {

        return super.run(methodname, params, aparam);
    }

    @Override
    protected Object callbackControl(Action.Param param, Object object) {
        String msg = param.getResultunit().getString("msg");
        if (msg != null && msg.toLowerCase().equals("成功")) {
            return super.callbackControl(param, object);
        } else {
            ShowMsgDialog.cancel();
            ShowMsgDialog.show(param.getActivity(), msg, true);

            return null;
        }

    }
}

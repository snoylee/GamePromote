package com.xygame.sg.task.utils;

import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/10.
 */
public class Alert extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        if(params.size()==2&&params.get(1).equals("true")){
            ShowMsgDialog.show(param.getActivity(),params.get(0),false);
        }else if(params.get(0).equals("false")){
            ShowMsgDialog.cancel();
        }else if(params.size()==3&&params.get(1).equals("true")){
            ShowMsgDialog.show(param.getActivity(),params.get(0),Boolean.parseBoolean(params.get(2)));
        }
        return super.run(methodname, params, param);
    }
}

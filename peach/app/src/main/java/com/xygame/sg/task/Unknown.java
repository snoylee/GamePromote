package com.xygame.sg.task;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/22.
 */
public class Unknown extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        if(params.get(0).equals("")||params.get(0).equals("不限")){
            return  "";
        }
        return super.run(methodname, params, param);
    }
}

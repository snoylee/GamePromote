package com.xygame.sg.task.utils;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/13.
 */
public class Split extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        int i = Integer.parseInt(params.get(1));
        if(params.get(0).equals("")){
            return "";
        }
        return params.get(0).split("-")[i];
    }
}

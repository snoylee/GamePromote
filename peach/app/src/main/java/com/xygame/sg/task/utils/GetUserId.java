package com.xygame.sg.task.utils;

import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/12.
 */
public class GetUserId extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return UserPreferencesUtil.getUserId(param.getActivity());
    }
}

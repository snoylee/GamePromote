package com.xygame.sg.task.comm;

import java.util.List;

import base.action.Action;
import base.action.CenterRepo;
import base.action.Task;

/**
 * Created by minhua on 2015/11/22.
 */
public class GetCityName extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return CenterRepo.getInsatnce().getRepo().get("province_city");
    }
}

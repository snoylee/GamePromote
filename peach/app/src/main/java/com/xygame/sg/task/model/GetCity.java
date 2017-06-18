package com.xygame.sg.task.model;

import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/18.
 */
public class GetCity extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        AssetDataBaseManager.CityBean cityBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(params.get(0).toString()));
        if (cityBean == null) {
            return "0";
        } else {
            return cityBean.getName();
        }
    }
}

package com.xygame.sg.task.comm;

import com.xygame.sg.activity.personal.EditorCityActivity;
import com.xygame.sg.activity.personal.EditorCountryActivity;
import com.xygame.sg.activity.personal.EditorProvinceActivity;

import java.util.List;

import base.action.Action;
import base.action.task.StartActivity;

/**
 * Created by minhua on 2015/11/22.
 */
public class StartCountrySelector extends StartActivity {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        EditorCountryActivity.submit = true;

        return super.run(methodname, params, param);
    }
}

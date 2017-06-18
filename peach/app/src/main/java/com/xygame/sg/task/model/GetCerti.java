package com.xygame.sg.task.model;

import android.view.View;

import com.xygame.sg.R;

import java.util.List;

import base.action.Action;
import base.action.CenterRepo;
import base.action.Task;

/**
 * Created by minhua on 2015/11/17.
 */
public class GetCerti extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        int certi = Integer.parseInt(params.get(0));
        ((View)param.getOnview().getParent()).setVisibility(certi==2?View.VISIBLE:View.INVISIBLE);
        return "";
    }

}

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
public class FilterCerti extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {

        View pwdControl = param.getOnview();
        View showPwdView = pwdControl.findViewById(R.id.showPwdView);
        View hidePwdView = pwdControl.findViewById(R.id.hidePwdView);
        if(showPwdView.getVisibility()==View.VISIBLE){
            showPwdView.setVisibility(View.GONE);
        }else{
            showPwdView.setVisibility(View.VISIBLE);
        }
        if(hidePwdView.getVisibility()==View.VISIBLE){
            hidePwdView.setVisibility(View.GONE);
            CenterRepo.getInsatnce().getRepo().put("modelType", "false");
        }else{
            hidePwdView.setVisibility(View.VISIBLE);
            CenterRepo.getInsatnce().getRepo().put("modelType", "true");
        }
        return "";
    }

}

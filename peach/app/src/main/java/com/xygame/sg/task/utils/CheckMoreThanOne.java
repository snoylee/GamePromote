package com.xygame.sg.task.utils;

import android.widget.Toast;

import com.xygame.sg.activity.base.CommonActivity;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/13.
 */
public class CheckMoreThanOne extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        CommonActivity aty = (CommonActivity) param.getActivity();
        if(aty.adapter.cnt.size()==0){
            Toast.makeText(aty,"至少上传一张作品",Toast.LENGTH_LONG).show();
            return null;
        }
        return super.run(methodname, params, param);
    }
}

package com.xygame.sg.task.certification;

import android.widget.Toast;

import com.xygame.sg.task.utils.IDCard;

import java.text.ParseException;
import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/14.
 */
public class CheckIdcard extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        String result = "";
        try {
            result = IDCard.IDCardValidate(params.get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(!result.equals("")){
            Toast.makeText(param.getActivity(),result,Toast.LENGTH_SHORT).show();
            return null;
        }
        return super.run(methodname, params, param);
    }


}

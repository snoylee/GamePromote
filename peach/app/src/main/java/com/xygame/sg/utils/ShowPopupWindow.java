package com.xygame.sg.utils;

import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

import base.RRes;
import base.ViewBinder;
import base.action.Action;
import base.action.Task;
import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/11/25.
 */
public class ShowPopupWindow extends Task {
    PopupWindow mPopupWindow;

    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        String str = params.get(1);
        mPopupWindow = new PopupWindow(new ViewBinder(param.getActivity(), new VisitUnit()).inflate(RRes.get(str).getAndroidValue(), null), -1, -1, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(param.getActivity().findViewById(RRes.get(params.get(0)).getAndroidValue()));
        return super.run(methodname, params, param);
    }
}

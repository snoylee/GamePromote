package com.xygame.sg.task.utils;

import java.util.List;

import base.action.Action;
import base.action.AsynTask;

/**
 * Created by minhua on 2015/11/10.
 */
public class TestSleep extends AsynTask {
    @Override
    public void callback(Action.Param aparam, Object object) {

    }

    @Override
    public Object threadRun(String methodname, List<String> params, Action.Param param) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}

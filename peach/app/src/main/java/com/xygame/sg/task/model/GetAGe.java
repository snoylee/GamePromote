package com.xygame.sg.task.model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import base.action.Action;
import base.action.Task;
import base.adapter.Logs;

/**
 * Created by minhua on 2015/11/18.
 */
public class GetAGe extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return params.get(0);
//        Logs.i("------------- "+params.get(0));
//        if(params.get(0).length()<8){
//return "";
//        }
//        String m = params.get(0).substring(4, 6);
//        String y = params.get(0).substring(0, 4);
//        String d = params.get(0).substring(6, 8);
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("yyyyMMdd").parse(y + m + d);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long balance = new Date().getTime() - date.getTime();
//        int ybalance = new Date().getYear() - date.getYear();
//        if (balance < 1000 * 60 * 60 * 24 * 365) {
//            ybalance -= 1;
//        }
////        int igender = Integer.parseInt(params.get(0));
////        String gender = "女";
////        if(igender==0){
////            gender = "女";
////        }else if(igender==1){
////            gender = "男";
////        }
//
//        return ybalance + "";
    }
}

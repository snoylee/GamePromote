package com.xygame.sg.utils;

import android.app.Activity;
import android.content.Intent;

import com.xygame.sg.define.view.SingleWheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2016/2/4.
 */
public class WheelUtil {
    public static WheelNumber WHEEL_NUMBER_HEIGHT = new WheelNumber(genScope(150, 210, 1), "cm", 20);
    public static WheelNumber WHEEL_NUMBER_WEIGHT = new WheelNumber(genScope(40, 75, 1), "kg", 5);
    public static WheelNumber WHEEL_NUMBER_BREAST = new WheelNumber(genScope(50, 110, 1), "cm", 30);
    public static WheelNumber WHEEL_NUMBER_WAIST = new WheelNumber(genScope(50, 110, 1), "cm", 10);
    public static WheelNumber WHEEL_NUMBER_HIP = new WheelNumber(genScope(50, 110, 1), "cm", 30);
    public static WheelNumber WHEEL_NUMBER_CUP = new WheelNumber(new String[]{"A", "B", "C", "D", "E", "F", "G"}, "", 1);
    public static WheelNumber WHEEL_NUMBER_AGE = new WheelNumber(genScope(15, 60, 1), "岁", 5);
    public static WheelNumber WHEEL_NUMBER_SHOSE = new WheelNumber(genScope(30, 50, 1), "", 6);
    public static WheelNumber WHEEL_NUMBER_CM_NUM = new WheelNumber(genScope(1, 20, 1), "人数", 0);
    public static WheelNumber WHEEL_NUMBER_RECRUIT_PEOPLE = new WheelNumber(genScope(1, 100, 1), "人数", 0);
    public static WheelNumber WHEEL_NUMBER_RECRUIT_GENDER = new WheelNumber(new String[]{"男", "女"}, "", 1);



    /****
     * 启动滚轮
     *
     * @param sentRequestcode 发送出去的requestcode
     * @param wheelNumber     滚轮类型
     * @param activvity
     */
    public static void startWheelActivity(int sentRequestcode, WheelNumber wheelNumber, Activity activvity) {
        String title = wheelNumber.getTitle();
        String[] scope = wheelNumber.getScope();
        int init = wheelNumber.getInitIndex();
        if (scope != null) {
            Intent intent = new Intent(activvity, SingleWheelView.class);
            intent.putExtra(Constants.SIGLE_WHEEL_TITLE, title);
            intent.putExtra(Constants.SIGLE_WHEEL_VALUE, scope);
            intent.putExtra(Constants.SIGLE_WHEEL_ITEM, init);
            activvity.startActivityForResult(intent, sentRequestcode);
        }
    }

    public static class WheelNumber {
        public String[] scope;
        public String title;
        public int initIndex;

        public String[] getScope() {
            return scope;
        }

        public String getTitle() {
            return title;
        }

        public int getInitIndex() {
            return initIndex;
        }

        public WheelNumber(String[] scope, String title, int initIndex) {
            this.scope = scope;
            this.title = title;
            this.initIndex = initIndex;
        }
    }

    public static String[] genScope(float b, float e, float step) {
        List<String> l = new ArrayList<String>();
        for (float i = b; i <= e; i += step) {
            l.add("" + new Float(i).intValue());
        }
        String[] a = new String[l.size()];
        l.toArray(a);
        return a;
    }
}

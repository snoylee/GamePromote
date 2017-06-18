package com.xygame.second.sg.comm.bean;

import com.xygame.second.sg.jinpai.bean.JinPaiCanYuBean;
import com.xygame.second.sg.sendgift.bean.GiftPresenter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/8/16.
 */
public class TransferGift implements Serializable {
    private List<GiftPresenter> vector;

    private List<JinPaiCanYuBean> jinPaiCanYuBeans;

    private String[] whellStr;

    public String[] getWhellStr() {
        return whellStr;
    }

    public void setWhellStr(String[] whellStr) {
        this.whellStr = whellStr;
    }

    public List<JinPaiCanYuBean> getJinPaiCanYuBeans() {
        return jinPaiCanYuBeans;
    }

    public void setJinPaiCanYuBeans(List<JinPaiCanYuBean> jinPaiCanYuBeans) {
        this.jinPaiCanYuBeans = jinPaiCanYuBeans;
    }

    public List<GiftPresenter> getVector() {
        return vector;
    }

    public void setVector(List<GiftPresenter> vector) {
        this.vector = vector;
    }
}

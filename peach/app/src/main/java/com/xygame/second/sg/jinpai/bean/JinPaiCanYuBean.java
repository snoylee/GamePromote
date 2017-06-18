package com.xygame.second.sg.jinpai.bean;

import com.xygame.second.sg.sendgift.bean.Presenter;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/17.
 */
public class JinPaiCanYuBean implements Serializable{
    private String bidPrice,bidTime;
    private Presenter presenter;

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}

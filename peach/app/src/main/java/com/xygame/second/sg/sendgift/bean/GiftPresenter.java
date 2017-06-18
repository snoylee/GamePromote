package com.xygame.second.sg.sendgift.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/13.
 */
public class GiftPresenter implements Serializable {
    private Presenter presenter;
    private String giftId,giftNums,sendGiftTime;

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftNums() {
        return giftNums;
    }

    public void setGiftNums(String giftNums) {
        this.giftNums = giftNums;
    }

    public String getSendGiftTime() {
        return sendGiftTime;
    }

    public void setSendGiftTime(String sendGiftTime) {
        this.sendGiftTime = sendGiftTime;
    }
}

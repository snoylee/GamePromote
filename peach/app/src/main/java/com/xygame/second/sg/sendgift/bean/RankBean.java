package com.xygame.second.sg.sendgift.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/15.
 */
public class RankBean implements Serializable {
    private Presenter presenter;
    private String amount;

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

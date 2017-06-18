package com.xygame.second.sg.personal.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/6/1.
 */
public class PayMoneyBean implements Serializable{
    private String virMoney,money;

    public String getVirMoney() {
        return virMoney;
    }

    public void setVirMoney(String virMoney) {
        this.virMoney = virMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

package com.xygame.second.sg.personal.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/6/14.
 */
public class GetStoneMoneyBean implements Serializable{
    private Long cash;
    private Long diamond;
    private Long withdrawRate;
    private Long minWithdrawCashAmount;

    public Long getCash() {
        return cash;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

    public Long getDiamond() {
        return diamond;
    }

    public void setDiamond(Long diamond) {
        this.diamond = diamond;
    }

    public Long getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(Long withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public Long getMinWithdrawCashAmount() {
        return minWithdrawCashAmount;
    }

    public void setMinWithdrawCashAmount(Long minWithdrawCashAmount) {
        this.minWithdrawCashAmount = minWithdrawCashAmount;
    }
}

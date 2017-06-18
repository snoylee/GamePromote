package com.xygame.second.sg.xiadan.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/24.
 */
public class XiaDanPriceListBean implements Serializable {
    private String priceId,skillCode,priceRate;

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getSkillCode() {
        return skillCode;
    }

    public void setSkillCode(String skillCode) {
        this.skillCode = skillCode;
    }

    public String getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(String priceRate) {
        this.priceRate = priceRate;
    }
}

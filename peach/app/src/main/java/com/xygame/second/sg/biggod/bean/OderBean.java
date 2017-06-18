package com.xygame.second.sg.biggod.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/21.
 */
public class OderBean implements Serializable {
    private String skillCode,skillTitle,status,priceRate,localIndex,priceId;
    private boolean isDealWith=false;

    private boolean priceFlag=false;

    private boolean rateFlag=false;

    private String newPriceId;

    public String getNewPriceId() {
        return newPriceId;
    }

    public void setNewPriceId(String newPriceId) {
        this.newPriceId = newPriceId;
    }

    public boolean isPriceFlag() {
        return priceFlag;
    }

    public void setPriceFlag(boolean priceFlag) {
        this.priceFlag = priceFlag;
    }

    public boolean isRateFlag() {
        return rateFlag;
    }

    public void setRateFlag(boolean rateFlag) {
        this.rateFlag = rateFlag;
    }

    public boolean isDealWith() {
        return isDealWith;
    }

    public void setIsDealWith(boolean isDealWith) {
        this.isDealWith = isDealWith;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getLocalIndex() {
        return localIndex;
    }

    public void setLocalIndex(String localIndex) {
        this.localIndex = localIndex;
    }

    public String getSkillCode() {
        return skillCode;
    }

    public void setSkillCode(String skillCode) {
        this.skillCode = skillCode;
    }

    public String getSkillTitle() {
        return skillTitle;
    }

    public void setSkillTitle(String skillTitle) {
        this.skillTitle = skillTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(String priceRate) {
        this.priceRate = priceRate;
    }
}

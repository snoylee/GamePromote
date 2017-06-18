package com.xygame.sg.activity.commen.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/3/28.
 */
public class MerchantRegisterBean implements Serializable{
    private String cellphoneText,passWordText,cerfyText,merchantNameText,addressText,nameText,phoneText,qqText,introduceText,shortNameText,headImage,regIdStr,regImage;

    public String getCellphoneText() {
        return cellphoneText;
    }

    public void setCellphoneText(String cellphoneText) {
        this.cellphoneText = cellphoneText;
    }

    public String getPassWordText() {
        return passWordText;
    }

    public void setPassWordText(String passWordText) {
        this.passWordText = passWordText;
    }

    public String getCerfyText() {
        return cerfyText;
    }

    public void setCerfyText(String cerfyText) {
        this.cerfyText = cerfyText;
    }

    public String getMerchantNameText() {
        return merchantNameText;
    }

    public void setMerchantNameText(String merchantNameText) {
        this.merchantNameText = merchantNameText;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getPhoneText() {
        return phoneText;
    }

    public void setPhoneText(String phoneText) {
        this.phoneText = phoneText;
    }

    public String getQqText() {
        return qqText;
    }

    public void setQqText(String qqText) {
        this.qqText = qqText;
    }

    public String getIntroduceText() {
        return introduceText;
    }

    public void setIntroduceText(String introduceText) {
        this.introduceText = introduceText;
    }

    public String getShortNameText() {
        return shortNameText;
    }

    public void setShortNameText(String shortNameText) {
        this.shortNameText = shortNameText;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getRegIdStr() {
        return regIdStr;
    }

    public void setRegIdStr(String regIdStr) {
        this.regIdStr = regIdStr;
    }

    public String getRegImage() {
        return regImage;
    }

    public void setRegImage(String regImage) {
        this.regImage = regImage;
    }
}

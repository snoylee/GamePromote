package com.xygame.second.sg.xiadan.bean;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/26.
 */
public class TransIfoToDetailBean implements Serializable {
    private String userNick,userImage,time,address,siglePrice,totalPrice,inviteUserId,oralTextStr,fialDate,curTimeNums;

    public String getCurTimeNums() {
        return curTimeNums;
    }

    public void setCurTimeNums(String curTimeNums) {
        this.curTimeNums = curTimeNums;
    }

    private JinPaiBigTypeBean currPinLeiBean;

    public String getFialDate() {
        return fialDate;
    }

    public void setFialDate(String fialDate) {
        this.fialDate = fialDate;
    }

    public String getOralTextStr() {
        return oralTextStr;
    }

    public void setOralTextStr(String oralTextStr) {
        this.oralTextStr = oralTextStr;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiglePrice() {
        return siglePrice;
    }

    public void setSiglePrice(String siglePrice) {
        this.siglePrice = siglePrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public JinPaiBigTypeBean getCurrPinLeiBean() {
        return currPinLeiBean;
    }

    public void setCurrPinLeiBean(JinPaiBigTypeBean currPinLeiBean) {
        this.currPinLeiBean = currPinLeiBean;
    }
}

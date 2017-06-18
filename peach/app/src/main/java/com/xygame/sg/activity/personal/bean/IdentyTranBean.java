package com.xygame.sg.activity.personal.bean;

import com.xygame.sg.bean.comm.PhotoesBean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xy on 2016/1/20.
 * 在认证的三个步骤中传递数据
 */
public class IdentyTranBean implements Serializable{
    private String realName;
    private String idCard;
    private String halfPath;
    private String frontPath;
    private String backPath;
    private String halfUrl;
    private String frontUrl;
    private String backUrl;
    private String videoPath;
    private String videoUrl;
    private String userHeight,userWeight,userBust,userWaist,userHip,userCup,userShoesCode;
    private String opusDesc;
    private List<PhotoesBean> uploadImages;
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHalfPath() {
        return halfPath;
    }

    public void setHalfPath(String halfPath) {
        this.halfPath = halfPath;
    }

    public String getFrontPath() {
        return frontPath;
    }

    public void setFrontPath(String frontPath) {
        this.frontPath = frontPath;
    }

    public String getBackPath() {
        return backPath;
    }

    public void setBackPath(String backPath) {
        this.backPath = backPath;
    }

    public String getHalfUrl() {
        return halfUrl;
    }

    public void setHalfUrl(String halfUrl) {
        this.halfUrl = halfUrl;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserBust() {
        return userBust;
    }

    public void setUserBust(String userBust) {
        this.userBust = userBust;
    }

    public String getUserWaist() {
        return userWaist;
    }

    public void setUserWaist(String userWaist) {
        this.userWaist = userWaist;
    }

    public String getUserHip() {
        return userHip;
    }

    public void setUserHip(String userHip) {
        this.userHip = userHip;
    }

    public String getUserCup() {
        return userCup;
    }

    public void setUserCup(String userCup) {
        this.userCup = userCup;
    }

    public String getUserShoesCode() {
        return userShoesCode;
    }

    public void setUserShoesCode(String userShoesCode) {
        this.userShoesCode = userShoesCode;
    }



    public String getOpusDesc() {
        return opusDesc;
    }

    public void setOpusDesc(String opusDesc) {
        this.opusDesc = opusDesc;
    }

    public List<PhotoesBean> getUploadImages() {
        return uploadImages;
    }

    public void setUploadImages(List<PhotoesBean> uploadImages) {
        this.uploadImages = uploadImages;
    }
}

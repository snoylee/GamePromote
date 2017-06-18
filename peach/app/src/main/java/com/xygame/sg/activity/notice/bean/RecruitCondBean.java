package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class RecruitCondBean implements Serializable {
    /**
     * 性别（不传或-1表示全部）
     */
    private int gender=-1;
    /**
     * 最小价格（不限就不传或-1)（单位：分）
     */
    private long minPrice=0;
    /**
     *最大价格（不限就不传或-1)（单位：分）
     */
    private long maxPrice=0;
    /**
     * 是否报销差旅费（1：报销，2：不报销，不传或-1表示不限）
     */
    private int isAffordTravelFee;
    /**
     * 是否报销住宿费（1：报销，2：不报销，不传或-1表示不限）
     */
    private int isAffordAccomFee;

    public RecruitCondBean() {
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getIsAffordTravelFee() {
        return isAffordTravelFee;
    }

    public void setIsAffordTravelFee(int isAffordTravelFee) {
        this.isAffordTravelFee = isAffordTravelFee;
    }

    public int getIsAffordAccomFee() {
        return isAffordAccomFee;
    }

    public void setIsAffordAccomFee(int isAffordAccomFee) {
        this.isAffordAccomFee = isAffordAccomFee;
    }
}

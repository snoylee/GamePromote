package com.xygame.sg.activity.model.bean;

import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.personal.bean.StyleBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class AllModelReqBean implements Serializable {
    private Integer gender ;
    private String country;
    private Integer city;
    private Integer occupType;
    private Integer ageBegin;
    private Integer ageEnd;
    private Integer heightBegin;
    private Integer heightEnd;
    private Integer weightBegin;
    private Integer weightEnd;
    private Integer bustBegin;
    private Integer bustEnd;
    private Integer waistBegin;
    private Integer waistEnd;
    private Integer hipBegin;
    private Integer hipEnd;
    private Integer cupBegin;
    private Integer cupEnd;
    private Integer shoesCodeBegin;
    private Integer shoesCodeEnd;
    private List<Integer> styleType;//模特风格
    private Integer priceType;//拍摄类型
    private boolean modelType;//是否是模特认证 （true、false）
    private String usernick;
    private Integer ntelligentSort=1;//智能排序（1：默认排序、2：价格最高、3：价格最低、4：接单量最多）
    private PageBean page;

    private List<StyleBean>  selectDatas = new ArrayList<StyleBean>();

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getWaistBegin() {
        return waistBegin;
    }

    public void setWaistBegin(Integer waistBegin) {
        this.waistBegin = waistBegin;
    }

    public Integer getAgeBegin() {
        return ageBegin;
    }

    public void setAgeBegin(Integer ageBegin) {
        this.ageBegin = ageBegin;
    }

    public Integer getOccupType() {
        return occupType;
    }

    public void setOccupType(Integer occupType) {
        this.occupType = occupType;
    }

    public Integer getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(Integer ageEnd) {
        this.ageEnd = ageEnd;
    }

    public Integer getHeightEnd() {
        return heightEnd;
    }

    public void setHeightEnd(Integer heightEnd) {
        this.heightEnd = heightEnd;
    }

    public Integer getWeightEnd() {
        return weightEnd;
    }

    public void setWeightEnd(Integer weightEnd) {
        this.weightEnd = weightEnd;
    }

    public Integer getWeightBegin() {
        return weightBegin;
    }

    public void setWeightBegin(Integer weightBegin) {
        this.weightBegin = weightBegin;
    }

    public Integer getHeightBegin() {
        return heightBegin;
    }

    public void setHeightBegin(Integer heightBegin) {
        this.heightBegin = heightBegin;
    }

    public Integer getBustBegin() {
        return bustBegin;
    }

    public void setBustBegin(Integer bustBegin) {
        this.bustBegin = bustBegin;
    }

    public Integer getBustEnd() {
        return bustEnd;
    }

    public void setBustEnd(Integer bustEnd) {
        this.bustEnd = bustEnd;
    }

    public Integer getWaistEnd() {
        return waistEnd;
    }

    public void setWaistEnd(Integer waistEnd) {
        this.waistEnd = waistEnd;
    }

    public Integer getHipBegin() {
        return hipBegin;
    }

    public void setHipBegin(Integer hipBegin) {
        this.hipBegin = hipBegin;
    }

    public Integer getHipEnd() {
        return hipEnd;
    }

    public void setHipEnd(Integer hipEnd) {
        this.hipEnd = hipEnd;
    }

    public Integer getCupBegin() {
        return cupBegin;
    }

    public void setCupBegin(Integer cupBegin) {
        this.cupBegin = cupBegin;
    }

    public Integer getCupEnd() {
        return cupEnd;
    }

    public void setCupEnd(Integer cupEnd) {
        this.cupEnd = cupEnd;
    }

    public Integer getShoesCodeBegin() {
        return shoesCodeBegin;
    }

    public void setShoesCodeBegin(Integer shoesCodeBegin) {
        this.shoesCodeBegin = shoesCodeBegin;
    }

    public Integer getShoesCodeEnd() {
        return shoesCodeEnd;
    }

    public void setShoesCodeEnd(Integer shoesCodeEnd) {
        this.shoesCodeEnd = shoesCodeEnd;
    }

    public List<Integer> getStyleType() {
        return styleType;
    }

    public void setStyleType(List<Integer> styleType) {
        this.styleType = styleType;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public boolean isModelType() {
        return modelType;
    }

    public void setModelType(boolean modelType) {
        this.modelType = modelType;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public Integer getNtelligentSort() {
        return ntelligentSort;
    }

    public void setNtelligentSort(Integer ntelligentSort) {
        this.ntelligentSort = ntelligentSort;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<StyleBean> getSelectDatas() {
        return selectDatas;
    }

    public void setSelectDatas(List<StyleBean> selectDatas) {
        this.selectDatas = selectDatas;
    }
}

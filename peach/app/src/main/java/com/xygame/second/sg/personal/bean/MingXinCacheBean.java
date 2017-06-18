package com.xygame.second.sg.personal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/6/16.
 */
public class MingXinCacheBean implements Serializable {
//    private List<StoneMXItemBean> cacheDatas;
    private int pageIndex;
    private String reqTime;
    private boolean isLoading;

//    public List<StoneMXItemBean> getCacheDatas() {
//        return cacheDatas;
//    }
//
//    public void setCacheDatas(List<StoneMXItemBean> cacheDatas) {
//        this.cacheDatas = cacheDatas;
//    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

}

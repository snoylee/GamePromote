package com.xygame.sg.activity.cm.bean;

import com.xygame.sg.activity.notice.bean.PageBean;

import java.io.Serializable;

/**
 * Created by moreidols on 16/3/18.
 */
public class PageReq implements Serializable {
    /**
     * 分页信息对象
     */
    private PageBean page = new PageBean();

    /**
     * 第一页时不传（其它页传服务器返回来的值）
     */
    private String reqTime;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }


}

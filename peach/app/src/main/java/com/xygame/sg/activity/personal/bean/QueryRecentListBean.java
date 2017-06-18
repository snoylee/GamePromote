package com.xygame.sg.activity.personal.bean;

import com.xygame.sg.activity.notice.bean.PageBean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class QueryRecentListBean implements Serializable {
    /**
     * 分页信息对象
     */
    private PageBean page = new PageBean();
    /**
     * 查询类型（1：模特，2：摄影师）
     */
    private int qtype;

    private int firstAccess;

    private Long reqTime;
    private Long lastReadTime;

    public QueryRecentListBean() {
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }

    public int getFirstAccess() {
        return firstAccess;
    }

    public void setFirstAccess(int firstAccess) {
        this.firstAccess = firstAccess;
    }

    public Long getReqTime() {
        return reqTime;
    }

    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }

    public Long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}

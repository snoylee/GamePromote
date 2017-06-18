package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class QueryUserNoticesListBean implements Serializable{
    /**
     * 分页信息对象
     */
    private PageBean page = new PageBean();

    /**
     * 第一页时不传（其它页传服务器返回来的值）
     */
    private String reqtime;
    /**
     * 查看用户的类型（游客时不用传）
     */
    private String userId;



    public QueryUserNoticesListBean() {
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public String getReqtime() {
        return reqtime;
    }

    public void setReqtime(String reqtime) {
        this.reqtime = reqtime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

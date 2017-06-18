package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class QueryNoticesListBean implements Serializable{
    /**
     * 分页信息对象
     */
    private PageBean page = new PageBean();
    /**
     * 拍摄类型（不传或-1表示全部）
     */
    private int shootType = -1;
    /**
     * 排序方式（1：默认，2：价格最高）
     */
    private int orderType = 1;
    /**
     * 第一页时不传（其它页传服务器返回来的值）
     */
    private String reqtime;
    /**
     * 查看用户的类型（游客时不用传）
     */
    private String utype;
    private QueryNoticesCond cond = new QueryNoticesCond();


    public QueryNoticesListBean() {
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public int getShootType() {
        return shootType;
    }

    public void setShootType(int shootType) {
        this.shootType = shootType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getReqtime() {
        return reqtime;
    }

    public void setReqtime(String reqtime) {
        this.reqtime = reqtime;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public QueryNoticesCond getCond() {
        return cond;
    }

    public void setCond(QueryNoticesCond cond) {
        this.cond = cond;
    }

}

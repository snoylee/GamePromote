package com.xygame.sg.activity.personal.bean;

import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.notice.bean.QueryNoticesCond;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class QueryAttentionsListBean implements Serializable{
    /**
     * 分页信息对象
     */
    private PageBean page = new PageBean();
    /**
     * 查询类型（1：模特，2：摄影师）
     */
    private int qtype;



    public QueryAttentionsListBean() {
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
}

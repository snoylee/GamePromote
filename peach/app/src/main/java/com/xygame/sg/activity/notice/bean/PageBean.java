package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 * 用于分页传参数
 */
public class PageBean implements Serializable{
    /**
     * 页码（从1开始）
     */
    private int pageIndex;
    /**
     * 每页的数据量
     */
    private int pageSize;

    public PageBean() {
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

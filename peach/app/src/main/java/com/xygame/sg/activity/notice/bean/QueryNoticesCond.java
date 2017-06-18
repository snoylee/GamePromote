package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class QueryNoticesCond implements Serializable {
    /**
     * 拍摄信息（没有就不传）
     */
    private ShootCondBean shootCond = new ShootCondBean();
    /**
     * 招募信息（没有就不传）
     */
    private RecruitCondBean recruitCond = new RecruitCondBean();

    /**
     * 是否支付（不限：不传或-1，1:支付)
     */
    private int repayStatus = -1;

    public QueryNoticesCond() {
    }

    public ShootCondBean getShootCond() {
        return shootCond;
    }

    public void setShootCond(ShootCondBean shootCond) {
        this.shootCond = shootCond;
    }

    public RecruitCondBean getRecruitCond() {
        return recruitCond;
    }

    public void setRecruitCond(RecruitCondBean recruitCond) {
        this.recruitCond = recruitCond;
    }
    public int getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(int repayStatus) {
        this.repayStatus = repayStatus;
    }
}

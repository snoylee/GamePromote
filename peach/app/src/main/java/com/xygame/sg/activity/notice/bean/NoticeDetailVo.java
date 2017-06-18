/**
 *
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhoujian
 * @date 2015年12月8日 下午4:05:18
 * @desc 通告详情
 */
public class NoticeDetailVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4846758351227818807L;

    //基础信息
    private long noticeId;
    private int shootType;
    private int noticeType;
    private String subject;
    private long publishUserId;
    private String nick;
    private String userLogo;
    private Long startTime;
    private Long endTime;
    private int pgCount;//摄影师人数
    private int openStatus;
    private int anonStatus;//匿名状态
    private String remark;
    private int remarkType;
    private Date createTime;
    private int recordStatus;

    private int payStatus;//0：待审核；1：招募中；2：拍摄中；3：已完成；4：已关闭
    private int payAmount;
//    private Date payTime;
    private String payTime;

    private int authStatus;
    private String authTime;
    private String refuseReason;



    private int userStatus;//摄影师认真状态：1：未认证；2：审核通过；3：审核不通过

    //已报名人数
    private int appliedSum;
    //是否已收藏
    private int hasCollected;//1:是;0:否
    private int isClose;//1:已关闭；0：未关闭
    private int applied;//是否已报名：1：报名；0：未报名

    //参考作品
    private List<NoticeReferPicVo> referPics = new ArrayList<NoticeReferPicVo>();
    //拍摄信息
    private NoticeShootVo shoot;

    private List<NoticeRecruitVo> recruits = new ArrayList<NoticeRecruitVo>();

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public int getShootType() {
        return shootType;
    }

    public void setShootType(int shootType) {
        this.shootType = shootType;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(long publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getPgCount() {
        return pgCount;
    }

    public void setPgCount(int pgCount) {
        this.pgCount = pgCount;
    }

    public int getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    public int getAnonStatus() {
        return anonStatus;
    }

    public void setAnonStatus(int anonStatus) {
        this.anonStatus = anonStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRemarkType() {
        return remarkType;
    }

    public void setRemarkType(int remarkType) {
        this.remarkType = remarkType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public List<NoticeReferPicVo> getReferPics() {
        return referPics;
    }

    public void setReferPics(List<NoticeReferPicVo> referPics) {
        this.referPics = referPics;
    }

    public NoticeShootVo getShoot() {
        return shoot;
    }

    public void setShoot(NoticeShootVo shoot) {
        this.shoot = shoot;
    }

    public int getAppliedSum() {
        return appliedSum;
    }

    public void setAppliedSum(int appliedSum) {
        this.appliedSum = appliedSum;
    }

    public int getHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(int hasCollected) {
        this.hasCollected = hasCollected;
    }

    public List<NoticeRecruitVo> getRecruits() {
        return recruits;
    }

    public void setRecruits(List<NoticeRecruitVo> recruits) {
        this.recruits = recruits;
    }

//    public int getIsClose() {
//        return isClose;
//    }
//
//    public void setIsClose(int isClose) {
//        this.isClose = isClose;
//    }
    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public int getApplied() {
        return applied;
    }

    public void setApplied(int applied) {
        this.applied = applied;
    }
}

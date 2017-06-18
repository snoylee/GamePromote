package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/4/21.
 */
public class JJRNoticeBean implements Serializable {
    private String noticeId,subject,noticeContent,applyCount;
    private boolean expand;
    private JJRPublisher publisher;
    private String hasApply,noticeStatus;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(String applyCount) {
        this.applyCount = applyCount;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public JJRPublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(JJRPublisher publisher) {
        this.publisher = publisher;
    }

    public String getHasApply() {
        return hasApply;
    }

    public void setHasApply(String hasApply) {
        this.hasApply = hasApply;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }
}

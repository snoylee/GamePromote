package com.xygame.sg.im;

import java.io.Serializable;

/**
 * Created by tony on 2016/1/13.
 */
public class SGNewsBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private String isShow;

    private boolean isShowMsgTimer;

    private String friendUserId,friendUserIcon,friendNickName;

    private String msgStatus;

    private String _id,msgContent,type,timestamp,noticeId,recruitId,newType,userId,messageStatus,fromUser,toUser,noticeSubject,recruitLocIndex,inout;

    private String operatorFlag;

    public String getOperatorFlag() {
        return operatorFlag;
    }

    public void setOperatorFlag(String operatorFlag) {
        this.operatorFlag = operatorFlag;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(String recruitId) {
        this.recruitId = recruitId;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getNoticeSubject() {
        return noticeSubject;
    }

    public void setNoticeSubject(String noticeSubject) {
        this.noticeSubject = noticeSubject;
    }

    public String getRecruitLocIndex() {
        return recruitLocIndex;
    }

    public void setRecruitLocIndex(String recruitLocIndex) {
        this.recruitLocIndex = recruitLocIndex;
    }

    public String getInout() {
        return inout;
    }

    public void setInout(String inout) {
        this.inout = inout;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendUserIcon() {
        return friendUserIcon;
    }

    public void setFriendUserIcon(String friendUserIcon) {
        this.friendUserIcon = friendUserIcon;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public boolean isShowMsgTimer() {
        return isShowMsgTimer;
    }

    public void setIsShowMsgTimer(boolean isShowMsgTimer) {
        this.isShowMsgTimer = isShowMsgTimer;
    }
}

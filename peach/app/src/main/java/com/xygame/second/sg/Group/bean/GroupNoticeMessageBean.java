package com.xygame.second.sg.Group.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/9/7.
 */
public class GroupNoticeMessageBean implements Serializable{
    private String packetId, groupJid,sendUserId,msgType,msgTimer,msgContent,noticeJson,userId,_id,msgStatus;

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupJid() {
        return groupJid;
    }

    public void setGroupJid(String groupJid) {
        this.groupJid = groupJid;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTimer() {
        return msgTimer;
    }

    public void setMsgTimer(String msgTimer) {
        this.msgTimer = msgTimer;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getNoticeJson() {
        return noticeJson;
    }

    public void setNoticeJson(String noticeJson) {
        this.noticeJson = noticeJson;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }
}

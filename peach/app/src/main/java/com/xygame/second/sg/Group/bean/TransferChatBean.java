package com.xygame.second.sg.Group.bean;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.io.Serializable;

/**
 * Created by tony on 2016/9/8.
 */
public class TransferChatBean implements Serializable {
    private MultiUserChat chat;

    public MultiUserChat getChat() {
        return chat;
    }

    public void setChat(MultiUserChat chat) {
        this.chat = chat;
    }
}

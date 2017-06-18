package com.xygame.sg.bean.circle;

import java.io.Serializable;

/**
 * Created by tony on 2016/5/20.
 */
public class CircleCommenters implements Serializable {
    private String commentId;
    private String content;
    private long commentTime;
    private CircleCommenter commenter;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public CircleCommenter getCommenter() {
        return commenter;
    }

    public void setCommenter(CircleCommenter commenter) {
        this.commenter = commenter;
    }
}

package com.xygame.sg.bean.circle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/5/24.
 */
public class Commenters implements Serializable{
    private String commentId;
    private String content;
    private Long commentTime;
    private Commenter commenter;

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

    public Long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Long commentTime) {
        this.commentTime = commentTime;
    }

    public Commenter getCommenter() {
        return commenter;
    }

    public void setCommenter(Commenter commenter) {
        this.commenter = commenter;
    }
}

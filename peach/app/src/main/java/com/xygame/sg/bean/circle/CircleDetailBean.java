package com.xygame.sg.bean.circle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/5/25.
 */
public class CircleDetailBean implements Serializable {

    private CommentObject comment;

    public CommentObject getCommenter() {
        return comment;
    }

    public void setCommenter(CommentObject commenter) {
        this.comment = commenter;
    }
}

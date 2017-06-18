package com.xygame.sg.bean.circle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/5/24.
 */
public class CommentObject implements Serializable {
    private Long reqTime;
    private List<Commenters> comments;

    private List<CirclePraisers> praisers;

    public List<CirclePraisers> getPraisers() {
        return praisers;
    }

    public void setPraisers(List<CirclePraisers> praisers) {
        this.praisers = praisers;
    }
    public Long getReqTime() {
        return reqTime;
    }

    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }

    public List<Commenters> getCommenters() {
        return comments;
    }

    public void setCommenters(List<Commenters> commenters) {
        this.comments = commenters;
    }

}

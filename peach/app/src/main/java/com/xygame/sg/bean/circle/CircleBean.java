package com.xygame.sg.bean.circle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/5/20.
 */
public class CircleBean implements Serializable {
    private String moodId;
    private CirclePublisher publisher;
    private String content;
    private Integer locProvince;
    private Integer locCity;
    private Long createTime;
    private Integer hasPraise;
    private Integer commentCount;
    private Integer praiseCount;
    private List<CircleRess> ress;
    private List<CirclePraisers> praisers;
    private List<CircleCommenters> commenters;
    private Integer giftCount;

    public String getMoodId() {
        return moodId;
    }

    public void setMoodId(String moodId) {
        this.moodId = moodId;
    }

    public CirclePublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(CirclePublisher publisher) {
        this.publisher = publisher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLocProvince() {
        return locProvince==null?0:locProvince;
    }

    public void setLocProvince(int locProvince) {
        this.locProvince = locProvince;
    }

    public int getLocCity() {
        return locCity==null?0:locCity;
    }

    public void setLocCity(int locCity) {
        this.locCity = locCity;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getHasPraise() {
        return hasPraise;
    }

    public void setHasPraise(int hasPraise) {
        this.hasPraise = hasPraise;
    }

    public List<CircleRess> getRess() {
        return ress;
    }

    public void setRess(List<CircleRess> ress) {
        this.ress = ress;
    }

    public List<CirclePraisers> getPraisers() {
        return praisers;
    }

    public void setPraisers(List<CirclePraisers> praisers) {
        this.praisers = praisers;
    }

    public List<CircleCommenters> getCommenters() {
        return commenters;
    }

    public void setCommenters(List<CircleCommenters> commenters) {
        this.commenters = commenters;
    }

    public Integer getCommentCount() {
        return commentCount==null?0:commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getPraiseCount() {
        return praiseCount==null?0:praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
    }

    public Integer getGiftCount() {
        return giftCount==null?0:giftCount;
    }

    public void setGiftCount(Integer giftCount) {
        this.giftCount = giftCount;
    }
}

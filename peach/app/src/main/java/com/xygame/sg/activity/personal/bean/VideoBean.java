package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * Created by xy on 2016/1/13.
 */
public class VideoBean implements Serializable {
    private Long videoSize;
    private Long videoTime;
    private String videoUrl;
    private String id;

    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    public Long getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(Long videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.xygame.sg.activity.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class HotModelBean implements Serializable{
    private String count;
    private List<ModelBean> models;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ModelBean> getModels() {
        return models;
    }

    public void setModels(List<ModelBean> models) {
        this.models = models;
    }
}

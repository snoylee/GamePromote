package com.xygame.sg.activity.personal.bean;

import com.xygame.sg.activity.commen.bean.ShootTypeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/4/12.
 */
public class TempTypeBean implements Serializable {

    private List<ShootTypeBean> dateList;

    public List<ShootTypeBean> getDateList() {
        return dateList;
    }

    public void setDateList(List<ShootTypeBean> dateList) {
        this.dateList = dateList;
    }
}

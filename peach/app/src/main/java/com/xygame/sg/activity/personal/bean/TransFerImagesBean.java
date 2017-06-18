package com.xygame.sg.activity.personal.bean;

import com.xygame.sg.bean.comm.PhotoesSubBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/2/25.
 */
public class TransFerImagesBean implements Serializable{
    private List<PhotoesSubBean> subDatas;

    public List<PhotoesSubBean> getSubDatas() {
        return subDatas;
    }

    public void setSubDatas(List<PhotoesSubBean> subDatas) {
        this.subDatas = subDatas;
    }
}

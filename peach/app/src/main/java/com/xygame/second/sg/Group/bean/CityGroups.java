package com.xygame.second.sg.Group.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/11/3.
 */
public class CityGroups implements Serializable {
    private String provinceId,groupLogo;
    private int noticeCout;
    private List<GoupNoticeBean> goupNoticeBeans;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getGroupLogo() {
        return groupLogo;
    }

    public void setGroupLogo(String groupLogo) {
        this.groupLogo = groupLogo;
    }

    public int getNoticeCout() {
        return noticeCout;
    }

    public void setNoticeCout(int noticeCout) {
        this.noticeCout = noticeCout;
    }

    public List<GoupNoticeBean> getGoupNoticeBeans() {
        return goupNoticeBeans;
    }

    public void setGoupNoticeBeans(List<GoupNoticeBean> goupNoticeBeans) {
        this.goupNoticeBeans = goupNoticeBeans;
    }
}

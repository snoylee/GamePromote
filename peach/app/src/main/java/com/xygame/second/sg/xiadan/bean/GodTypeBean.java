package com.xygame.second.sg.xiadan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/12/23.
 */
public class GodTypeBean implements Serializable{
    private String skillCode;
    private List<GodUserBean> userBeans;

    public String getSkillCode() {
        return skillCode;
    }

    public void setSkillCode(String skillCode) {
        this.skillCode = skillCode;
    }

    public List<GodUserBean> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans(List<GodUserBean> userBeans) {
        this.userBeans = userBeans;
    }
}

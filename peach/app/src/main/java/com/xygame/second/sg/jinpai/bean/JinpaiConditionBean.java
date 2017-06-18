package com.xygame.second.sg.jinpai.bean;

import com.xygame.second.sg.jinpai.PopAutoBean;
import com.xygame.second.sg.jinpai.PopSexBean;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/20.
 */
public class JinpaiConditionBean implements Serializable {
    private PopAutoBean autoBean;
    private PopSexBean sexBean;

    public PopAutoBean getAutoBean() {
        return autoBean;
    }

    public void setAutoBean(PopAutoBean autoBean) {
        this.autoBean = autoBean;
    }

    public PopSexBean getSexBean() {
        return sexBean;
    }

    public void setSexBean(PopSexBean sexBean) {
        this.sexBean = sexBean;
    }
}

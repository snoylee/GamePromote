package com.xygame.second.sg.jinpai.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/7/15.
 */
public class TransActionBean implements Serializable{
    private JinPaiBigTypeBean bigTypeBean;
    private List<JinPaiSmallTypeBean> smallTypeBeans;

    public JinPaiBigTypeBean getBigTypeBean() {
        return bigTypeBean;
    }

    public void setBigTypeBean(JinPaiBigTypeBean bigTypeBean) {
        this.bigTypeBean = bigTypeBean;
    }

    public List<JinPaiSmallTypeBean> getSmallTypeBeans() {
        return smallTypeBeans;
    }

    public void setSmallTypeBeans(List<JinPaiSmallTypeBean> smallTypeBeans) {
        this.smallTypeBeans = smallTypeBeans;
    }
}

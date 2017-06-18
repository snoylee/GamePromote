package com.xygame.second.sg.xiadan.bean;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/12/24.
 */
public class TransferTypeBean implements Serializable {
    private List<JinPaiBigTypeBean> myTypes;

    public List<JinPaiBigTypeBean> getMyTypes() {
        return myTypes;
    }

    public void setMyTypes(List<JinPaiBigTypeBean> myTypes) {
        this.myTypes = myTypes;
    }
}

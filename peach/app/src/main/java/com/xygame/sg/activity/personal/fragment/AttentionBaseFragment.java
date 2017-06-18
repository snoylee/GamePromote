package com.xygame.sg.activity.personal.fragment;

import com.xygame.sg.activity.personal.adapter.AttentionCMAdapter;
import com.xygame.sg.activity.personal.bean.AttentUserView;
import com.xygame.sg.activity.personal.bean.QueryAttentionsListBean;

import java.util.List;

import base.frame.ParentFragment;

/**
 * Created by xy on 2016/1/6.
 */
public class AttentionBaseFragment  extends ParentFragment {
    private QueryAttentionsListBean queryBean = new QueryAttentionsListBean();
    public  void responseAttentionList(List<AttentUserView> resultAttentionList) {

    }

    public QueryAttentionsListBean getQueryBean() {
        return queryBean;
    }
}

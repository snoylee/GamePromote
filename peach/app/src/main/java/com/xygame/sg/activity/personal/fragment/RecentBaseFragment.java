package com.xygame.sg.activity.personal.fragment;

import com.xygame.sg.activity.personal.bean.AttentUserView;
import com.xygame.sg.activity.personal.bean.QueryAttentionsListBean;
import com.xygame.sg.activity.personal.bean.QueryRecentListBean;
import com.xygame.sg.activity.personal.bean.UserSeeHistoryView;

import java.util.List;

import base.frame.ParentFragment;

/**
 * Created by xy on 2016/1/6.
 */
public abstract class RecentBaseFragment extends ParentFragment {
    private QueryRecentListBean queryBean = new QueryRecentListBean();
    public  void responseRecentList(UserSeeHistoryView seeHistoryView) {

    }

    public QueryRecentListBean getQueryBean() {
        return queryBean;
    }

    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}

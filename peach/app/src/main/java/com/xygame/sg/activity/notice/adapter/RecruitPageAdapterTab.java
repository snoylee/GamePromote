package com.xygame.sg.activity.notice.adapter;

import android.support.v4.app.Fragment;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.fragment.EliminateFragment;
import com.xygame.sg.activity.notice.fragment.EmployFragment;
import com.xygame.sg.activity.notice.fragment.EnrollFragment;
import com.xygame.sg.activity.notice.fragment.TBDFragment;
import com.xygame.sg.activity.personal.fragment.DataTabFragment;
import com.xygame.sg.activity.personal.fragment.PriceTabFragment;
import com.xygame.sg.activity.personal.fragment.WorkTabFragment;


/**
 * @author sunyoujun
 */
public enum RecruitPageAdapterTab {
    PAGE_TAB1(0, EnrollFragment.class, R.string.page_tab1),
    PAGE_TAB2(1, TBDFragment.class, R.string.page_tab2),
    PAGE_TAB3(2, EliminateFragment.class, R.string.page_tab3),
    PAGE_TAB4(3, EmployFragment.class, R.string.page_tab3);

    public final int tabIndex;

    public final Class<? extends Fragment> clazz;

    public final int resId;

    public final int fragmentId;

    RecruitPageAdapterTab(int index, Class<? extends Fragment> clazz, int resId) {
        this.tabIndex = index;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
    }

    public static final RecruitPageAdapterTab fromTabIndex(int tabIndex) {
        for (RecruitPageAdapterTab value : RecruitPageAdapterTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }
}

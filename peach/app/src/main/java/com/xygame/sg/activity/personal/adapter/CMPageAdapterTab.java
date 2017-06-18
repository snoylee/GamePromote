package com.xygame.sg.activity.personal.adapter;

import android.support.v4.app.Fragment;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.fragment.CMDataTabFragment;
import com.xygame.sg.activity.personal.fragment.CMNoticeTabFragment;
import com.xygame.sg.activity.personal.fragment.CMWorkTabFragment;
import com.xygame.sg.activity.personal.fragment.DataTabFragment;
import com.xygame.sg.activity.personal.fragment.PriceTabFragment;
import com.xygame.sg.activity.personal.fragment.WorkTabFragment;


/**
 * 
 * @author sunyoujun
 * 
 */
public enum CMPageAdapterTab {
	PAGE_TAB1(0, CMWorkTabFragment.class, R.string.page_tab1),

	PAGE_TAB2(1, CMDataTabFragment.class, R.string.page_tab2),
	;

	public final int tabIndex;

	public final Class<? extends Fragment> clazz;

	public final int resId;

	public final int fragmentId;

	CMPageAdapterTab(int index, Class<? extends Fragment> clazz, int resId) {
		this.tabIndex = index;
		this.clazz = clazz;
		this.resId = resId;
		this.fragmentId = index;
	}

	public static final CMPageAdapterTab fromTabIndex(int tabIndex) {
		for (CMPageAdapterTab value : CMPageAdapterTab.values()) {
			if (value.tabIndex == tabIndex) {
				return value;
			}
		}

		return null;
	}
}

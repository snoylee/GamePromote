package com.xygame.sg.activity.personal.fragment;

import android.support.v4.app.Fragment;
import android.widget.AbsListView;

import com.xygame.sg.activity.base.SGBaseFragment;

import base.frame.ParentFragment;

public abstract class ScrollTabHolderFragment extends ParentFragment implements ScrollTabHolder {

	private int fragmentId;

	protected ScrollTabHolder scrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		this.scrollTabHolder = scrollTabHolder;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
			int pagePosition) {
		// nothing
	}

	@Override
	public void onHeaderScroll(boolean isRefreashing, int value, int pagePosition) {

	}

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
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
//	protected abstract void lazyLoad();
	protected void lazyLoad(){

	}

}
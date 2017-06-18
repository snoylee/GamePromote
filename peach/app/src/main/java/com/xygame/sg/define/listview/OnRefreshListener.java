package com.xygame.sg.define.listview;

public interface OnRefreshListener {

	/**
	 * 下拉刷新执行的刷新任务, 使用时, 
	 * 当刷新完毕之后, 需要手动的调用onRefreshFinish(), 去隐藏头布局
	 */
	void onRefresh();
	
	/**
	 * 当加载更多时回调
	 * 当加载更多完毕之后, 需要手动的调用onRefreshFinish(), 去隐藏脚布局
	 */
	void onLoadMoring();
}

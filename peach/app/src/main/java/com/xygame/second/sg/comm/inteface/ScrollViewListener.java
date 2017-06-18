package com.xygame.second.sg.comm.inteface;

import com.xygame.second.sg.defineview.CustomScrollView;

/**
 * Created by tony on 2016/8/18.
 */
public interface ScrollViewListener {
    void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy);
}

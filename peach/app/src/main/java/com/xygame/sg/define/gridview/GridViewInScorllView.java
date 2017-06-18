/*
 * 文 件 名:  GridViewInScorllView.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月10日
 */
package com.xygame.sg.define.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年12月10日
 * @action  [功能描述]
 */
public class GridViewInScorllView extends GridView { 

    public GridViewInScorllView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public GridViewInScorllView(Context context) { 
        super(context); 
    } 

    public GridViewInScorllView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
} 
/*
 * 文 件 名:  VerticalTextView.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月10日
 */
package com.xygame.sg.define.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年12月10日
 * @action  [功能描述]
 */
public class VerticalTextView extends TextView{
	   final boolean topDown;


	   public VerticalTextView(Context context, AttributeSet attrs){
	      super(context, attrs);
	      final int gravity = getGravity();
	      if(Gravity.isVertical(gravity) && (gravity&Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
	         setGravity((gravity&Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
	         topDown = false;
	      }else
	         topDown = true;
	   }


	   @Override
	   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	      super.onMeasure(heightMeasureSpec, widthMeasureSpec);
	      setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	   }


	   @Override
	   protected boolean setFrame(int l, int t, int r, int b){
	      return super.setFrame(l, t, l+(b-t), t+(r-l));
	   }


	   @Override
	   public void draw(Canvas canvas){
	      if(topDown){
	         canvas.translate(getHeight(), 0);
	         canvas.rotate(90);
	      }else {
	         canvas.translate(0, getWidth());
	         canvas.rotate(90);
	      }
	      canvas.clipRect(0, 0, getWidth(), getHeight(), android.graphics.Region.Op.REPLACE);
	      super.draw(canvas);
	   }
	}
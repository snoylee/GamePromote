package com.xygame.sg.define.draggrid;

import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

public class DataTools {
	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 *  px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			return context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取屏幕宽高（像素）
	 * @param context
	 * @return
	 */
	public static int[] getScrennSize(Context context){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int[] sizeArr = new int[2];
		sizeArr[0] = dm.widthPixels;
		sizeArr[1] = dm.heightPixels;
		return sizeArr;
	}
}

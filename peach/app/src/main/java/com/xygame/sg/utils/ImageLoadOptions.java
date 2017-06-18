package com.xygame.sg.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.xygame.second.sg.utils.CircleDisplayer;

/**
 * Created by moreidols on 16/2/25.
 */
public class ImageLoadOptions {
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(false)
            .cacheOnDisc(true)
            .considerExifParams(true)
//            .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//            .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
            .displayer(new SimpleBitmapDisplayer())
            .build();
    public static DisplayImageOptions optionsInMem = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
//            .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//            .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
            .displayer(new SimpleBitmapDisplayer())
            .build();

        //配置参数
    public static DisplayImageOptions optionsCircle = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new CircleDisplayer())
                .build();
}

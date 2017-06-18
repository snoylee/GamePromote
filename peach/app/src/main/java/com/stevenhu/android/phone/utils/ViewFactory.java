package com.stevenhu.android.phone.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xygame.sg.R;
import com.xygame.sg.utils.VideoImageLoader;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 *
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView=(ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		if (url.contains(".mp4")){
			VideoImageLoader videoImageLoader = VideoImageLoader.getInstance();
			videoImageLoader.DisplayImage(url,imageView);
		}else{
			ImageLoader.getInstance().displayImage(url, imageView);
		}
		return imageView;
	}

	public static ImageView loadOrigaImage(Context context, String url) {
		ImageView imageView=(ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner_main, null);
		com.xygame.sg.image.ImageLoader.getInstance(3, com.xygame.sg.image.ImageLoader.Type.LIFO).loadOrigaImage(url, imageView,true);
		return imageView;
	}
}

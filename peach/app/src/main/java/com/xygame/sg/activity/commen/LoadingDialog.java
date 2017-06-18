package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.utils.SGApplication;

public class LoadingDialog extends Dialog {


	public LoadingDialog(Context context, int attrsr) {
		super(context,attrsr);
	}

	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_loading_dialog_layout);
		ProgressBar progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
		if (android.os.Build.VERSION.SDK_INT > 22) {//android 6.0替换clip的加载动画
			final Drawable drawable = SGApplication.getInstance().getApplicationContext().getResources().getDrawable(R.drawable.liveing_anim_60);
			progressBar2.setIndeterminateDrawable(drawable);
		}
	}



}

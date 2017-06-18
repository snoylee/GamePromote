/*
 * 文 件 名:  PickPhotoesView.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月12日
 */
package com.xygame.sg.define.view;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月12日
 * @action [选择拍照或相册的界面]
 */
public class RecordOptionsView extends SGBaseActivity {

	/**
	 * 重载方法
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_options);
		findViewById(R.id.dismiss).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, "null");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.order).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, "order");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.delete).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK,"delete");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}
}

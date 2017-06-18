/*
 * 文 件 名:  PickPhotoesView.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月12日
 */
package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

/**
 *头像视频处理动作
 */
public class PickVideoView extends SGBaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_photo_pop);
		TextView delete = (TextView) findViewById(R.id.camera);
		delete.setText("删除视频");
		TextView recorder = (TextView) findViewById(R.id.galary);
		recorder.setText("重新上传");
		findViewById(R.id.dismiss).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, "null");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

		findViewById(R.id.galary).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, "recorder");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.camera).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK,"delete");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}
}

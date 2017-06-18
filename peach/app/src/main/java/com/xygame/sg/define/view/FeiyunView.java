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
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月12日
 * @action [选择拍照或相册的界面]
 */
public class FeiyunView extends SGBaseActivity {

	private int timeDuringInt;
	private TextView oneText,twoText,fourText,eightText;
	private View oneView,twoView,fourView,eightView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fei_yun_layout);
		timeDuringInt=getIntent().getIntExtra("timeDuringInt",0);
		oneText=(TextView)findViewById(R.id.oneText);
		twoText=(TextView)findViewById(R.id.twoText);
		fourText=(TextView)findViewById(R.id.fourText);
		eightText=(TextView)findViewById(R.id.eightText);

		oneView=findViewById(R.id.oneView);
		twoView=findViewById(R.id.twoView);
		fourView=findViewById(R.id.fourView);
		eightView=findViewById(R.id.eightView);

		updateViews();

		findViewById(R.id.dismiss).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, 0);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.oneButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, 1);
				intent.putExtra("flag","当然你买单");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.twoButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK,2);
				intent.putExtra("flag","我是土豪我请你");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.fourButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK,3);
				intent.putExtra("flag","AA比较公平");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		findViewById(R.id.eightButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK,8);
				intent.putExtra("flag","全天（8小时）");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	private void updateViews() {
		switch (timeDuringInt){
			case 0:
				oneText.setTextColor(getResources().getColor(R.color.black));
				twoText.setTextColor(getResources().getColor(R.color.black));
				fourText.setTextColor(getResources().getColor(R.color.black));
				eightText.setTextColor(getResources().getColor(R.color.black));
				oneView.setVisibility(View.GONE);
				twoView.setVisibility(View.GONE);
				fourView.setVisibility(View.GONE);
				eightView.setVisibility(View.GONE);
				break;
			case 1:
				oneText.setTextColor(getResources().getColor(R.color.dark_green));
				oneView.setVisibility(View.VISIBLE);
				break;
			case 2:
				twoText.setTextColor(getResources().getColor(R.color.dark_green));
				twoView.setVisibility(View.VISIBLE);
				break;
			case 3:
				fourText.setTextColor(getResources().getColor(R.color.dark_green));
				fourView.setVisibility(View.VISIBLE);
				break;
			case 8:
				eightText.setTextColor(getResources().getColor(R.color.dark_green));
				eightView.setVisibility(View.VISIBLE);
				break;
		}
	}
}

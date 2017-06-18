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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.FreeTimeBean;
import com.xygame.second.sg.comm.bean.TimerDuringBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月12日
 * @action [选择拍照或相册的界面]
 */
public class TimeFreeDuringView extends SGBaseActivity {

	private LinearLayout contenter;
	private TimerDuringBean timerDuringBean;
	private List<FreeTimeBean> timers;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_free_during_layout);
		contenter=(LinearLayout)findViewById(R.id.contenter);
		timerDuringBean=(TimerDuringBean)getIntent().getSerializableExtra("timerDuringBean");
		updateViews();

		findViewById(R.id.rightButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		findViewById(R.id.comfirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isGo()){
					timerDuringBean.setTimers(timers);
					Intent intent = new Intent();
					intent.putExtra(Constants.COMEBACK, timerDuringBean);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}else{
					Toast.makeText(TimeFreeDuringView.this,"至少选择一项",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private boolean isGo(){
		boolean flag=false;
		for (FreeTimeBean it:timers){
			if (it.isSelect()){
				flag=true;
				break;
			}
		}
		return flag;
	}

	private void updateViews() {
		timers=new ArrayList<>();
		FreeTimeBean item1=new FreeTimeBean();
		item1.setName("上午（9：00-12:00）");
		item1.setIsSelect(false);
		item1.setId("1");
		timers.add(item1);

		FreeTimeBean item2=new FreeTimeBean();
		item2.setName("下午（12：00-18:00）");
		item2.setIsSelect(false);
		item2.setId("2");
		timers.add(item2);

		FreeTimeBean item3=new FreeTimeBean();
		item3.setName("晚上（18：00-22:00）");
		item3.setIsSelect(false);
		item3.setId("3");
		timers.add(item3);
		refreshViews();
	}

	private void refreshViews() {
		contenter.removeAllViews();
		for (int i=0;i<timers.size();i++){
			FreeTimeBean item=timers.get(i);
			View view = LayoutInflater.from(this).inflate(
					R.layout.free_time_item, null);
			TextView Text=(TextView)view.findViewById(R.id.Text);
			View maxView=view.findViewById(R.id.View);
			Text.setText(item.getName());
			if (item.isSelect()){
				Text.setTextColor(getResources().getColor(R.color.dark_green));
				maxView.setVisibility(View.VISIBLE);
			}else{
				Text.setTextColor(getResources().getColor(R.color.black));
				maxView.setVisibility(View.GONE);
			}
			view.setOnClickListener(new UpdateStatusItem(i));
			contenter.addView(view);
		}
	}

	private class UpdateStatusItem implements OnClickListener{
		private int index;
		public UpdateStatusItem(int index){
			this.index=index;
		}

		@Override
		public void onClick(View v) {
			changeStus(index);
		}
	}

	private void changeStus(int index) {
		timers.get(index).setIsSelect(!timers.get(index).isSelect());
		refreshViews();
	}
}

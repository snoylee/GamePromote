package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

public class XiaDanTimeView extends SGBaseActivity implements OnClickListener{

	private WheelView timeNumView,dateView,hourView,minView;
	private String[] timeNums={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
	private String[] dayDatas={"今天","明天","后天"};
	private String[] allHours={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
	private String[] allMin={"00","15","30","45"};
	private String[] dymicMins,dymicHours;
	private String todayDate,tomorrowDate,todyAfterTomorrowDate;
	private Button mBtnConfirm,btn_cancel;
	private String curTimeNums,curDate,curHour,curMin,dateOral;
	private boolean isDymicMin=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ping_lei_timer);
		initView();
		initDatas();
		initListers();
		timeNumView.setWheelBackground(R.color.white);
		dateView.setWheelBackground(R.color.white);
		hourView.setWheelBackground(R.color.white);
		minView.setWheelBackground(R.color.white);
	}

	private void initView() {
		timeNumView = (WheelView) findViewById(R.id.timeNumView);
		dateView = (WheelView) findViewById(R.id.dateView);
		hourView = (WheelView) findViewById(R.id.hourView);
		minView = (WheelView) findViewById(R.id.minView);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
	}

	private void initListers() {
		btn_cancel.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		dateView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				dateOral=dayDatas[dateView.getCurrentItem()];
				getCurDate();
				showHourAndMin();
			}
		});
		hourView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if ("今天".equals(dateOral)){
					if (hourView.getCurrentItem()<dymicHours.length){
						curHour=dymicHours[hourView.getCurrentItem()];
						updateMinView();
					}
				}else{
					curHour=allHours[hourView.getCurrentItem()];
				}
			}
		});
		minView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (isDymicMin){
					if (minView.getCurrentItem()<dymicMins.length){
						curMin=dymicMins[minView.getCurrentItem()];
					}
				}else{
					curMin=allMin[minView.getCurrentItem()];
				}
			}
		});
		timeNumView.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				curTimeNums = timeNums[timeNumView.getCurrentItem()];
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_confirm){
			transBeanSelect();
		}else if(v.getId()==R.id.btn_cancel){
			finish();
		}
	}

	protected void transBeanSelect() {
		if ("现在".equals(curHour)){
			int currHour=CalendarUtils.getCurrHour();
			String currHourStr=String.valueOf(currHour);
			if (currHourStr.length()==1){
				curHour="0".concat(currHourStr);
			}else{
				curHour=currHourStr;
			}
		}
		String fialDay=curDate.concat(" ").concat(curHour).concat(":").concat(curMin);
		Intent intent = new Intent();
		intent.putExtra("curTimeNums", curTimeNums);
		intent.putExtra("fialDate",fialDay);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void initDatas() {
		todayDate=CalendarUtils.getCaracterDateStr(System.currentTimeMillis());
		tomorrowDate= CalendarUtils.getDateFromLong(System.currentTimeMillis(), 1);
		todyAfterTomorrowDate= CalendarUtils.getDateFromLong(System.currentTimeMillis(), 2);

		dateView.setViewAdapter(new ArrayWheelAdapter<String>(this, dayDatas));
		timeNumView.setViewAdapter(new ArrayWheelAdapter<String>(this, timeNums));

		dateView.setVisibleItems(7);
		dateView.setCurrentItem(0);
		// 设置可见条目数量
		timeNumView.setVisibleItems(7);
		timeNumView.setCurrentItem(0);

		curTimeNums = timeNums[timeNumView.getCurrentItem()];
		dateOral=dayDatas[dateView.getCurrentItem()];
		getCurDate();
		showHourAndMin();
	}

	private void getCurDate(){//"今天","明天","后天"
		if ("今天".equals(dateOral)){
			curDate=todayDate;
		}else if ("明天".equals(dateOral)){
			curDate=tomorrowDate;
		}else if ("后天".equals(dateOral)){
			curDate=todyAfterTomorrowDate;
		}
	}

	private void showHourAndMin(){
		if ("今天".equals(dateOral)){
			isDymicMin=false;
			int currHour=CalendarUtils.getCurrHour();
			int currMin=CalendarUtils.getCurrMin();
			String currHourStr=String.valueOf(currHour);
			if (currHourStr.length()==1){
				currHourStr="0".concat(currHourStr);
			}
			int currPostion=0;
			for (int i=0;i<allHours.length;i++){
				if (allHours[i].equals(currHourStr)){
					currPostion=i;
					break;
				}
			}
			int hourCurrIndex=1,totalDayHourSize=allHours.length-currPostion;
			dymicHours=new String[totalDayHourSize];
			dymicHours[0]="现在";
			for (int i=currPostion+1;i<allHours.length;i++){
				dymicHours[hourCurrIndex]=allHours[i];
				hourCurrIndex=hourCurrIndex+1;
			}
			hourView.setViewAdapter(new ArrayWheelAdapter<String>(this, dymicHours));
			hourView.setVisibleItems(7);
			hourView.setCurrentItem(0);
			curHour=dymicHours[0];
			curMin=getCurrMin(currMin+15);
			minView.setViewAdapter(new ArrayWheelAdapter<String>(this, new String[]{"","",""}));
			minView.setVisibleItems(7);
			minView.setCurrentItem(0);
		}else{
			isDymicMin=false;
			hourView.setViewAdapter(new ArrayWheelAdapter<String>(this, allHours));
			minView.setViewAdapter(new ArrayWheelAdapter<String>(this, allMin));
			hourView.setVisibleItems(7);
			hourView.setCurrentItem(0);
			minView.setVisibleItems(7);
			minView.setCurrentItem(0);
			curHour=allHours[hourView.getCurrentItem()];
			curMin=allMin[minView.getCurrentItem()];
		}
	}

	private String getCurrMin(int currMinStr){
		String shortMinStr=null;
		if (currMinStr>=0&&currMinStr<=15){
			shortMinStr="15";
		}else if (currMinStr>15&&currMinStr<=30){
			shortMinStr="30";
		}else if (currMinStr>30&&currMinStr<=45){
			shortMinStr="45";
		}else if (currMinStr>45&&currMinStr<=60){
			shortMinStr="00";
			curHour=dymicHours[1];
		}else if (currMinStr>60&&currMinStr<=75){
			shortMinStr="15";
			curHour=dymicHours[1];
		}
		return shortMinStr;
	}

	private void updateMinView(){
		if (hourView.getCurrentItem()==0){
			minView.setViewAdapter(new ArrayWheelAdapter<String>(this, new String[]{"", "", ""}));
			minView.setVisibleItems(7);
			minView.setCurrentItem(0);
			isDymicMin=true;
		}else if (hourView.getCurrentItem()==1){
			int currMin=CalendarUtils.getCurrMin();
			currMin=currMin+30;
			if (currMin>=60&&currMin<=75){
				curMin="15";
				dymicMins=new String[]{"15","30","45"};
				minView.setViewAdapter(new ArrayWheelAdapter<String>(this, dymicMins));
				minView.setVisibleItems(7);
				minView.setCurrentItem(0);
				isDymicMin=true;
			}else if (currMin>75&&currMin<=90){
				isDymicMin=true;
				curMin="30";
				dymicMins=new String[]{"30","45"};
				minView.setViewAdapter(new ArrayWheelAdapter<String>(this, dymicMins));
				minView.setVisibleItems(7);
				minView.setCurrentItem(0);
			}else{
				isDymicMin=false;
				minView.setViewAdapter(new ArrayWheelAdapter<String>(this, allMin));
				minView.setVisibleItems(7);
				minView.setCurrentItem(0);
				curMin=allMin[minView.getCurrentItem()];
			}
		}else{
			isDymicMin=false;
			minView.setViewAdapter(new ArrayWheelAdapter<String>(this, allMin));
			minView.setVisibleItems(7);
			minView.setCurrentItem(0);
			curMin=allMin[minView.getCurrentItem()];
		}
	}
}

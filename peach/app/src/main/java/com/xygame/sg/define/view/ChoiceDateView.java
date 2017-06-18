package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

public class ChoiceDateView extends SGBaseActivity implements OnClickListener{

	private WheelView yearWv,monthWv,dayWv;
	String[] years;
	String[] months;
	String[] days;
	private String[] typeName;
	private FeedbackDateBean ftBean;
	private Button mBtnConfirm,btn_cancel;
	private String curYear,curMonth,curDay;

	private String initYear,initMonth,initDay;
	private int startYear = 1927,endYear = 2005;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_date);
		if (getIntent().hasExtra(Constants.INIT_YEAR)){
			initYear = getIntent().getStringExtra(Constants.INIT_YEAR);
		} else {
			initYear = CalendarUtils.getCurrentYear()+"";
		}
		if (getIntent().hasExtra(Constants.INIT_MONTH)){
			initMonth = getIntent().getStringExtra(Constants.INIT_MONTH);
		} else {
			initMonth = CalendarUtils.getCurrentMonth()+"";
		}
		if (getIntent().hasExtra(Constants.INIT_DAY)){
			initDay = getIntent().getStringExtra(Constants.INIT_DAY);
		} else {
			initDay = CalendarUtils.getCurrentDay() + "";
		}


		initView();
		initDatas();
		initListers();
		yearWv.setWheelBackground(R.color.white);
		monthWv.setWheelBackground(R.color.white);
		dayWv.setWheelBackground(R.color.white);
	}

	private void initView() {
		yearWv = (WheelView) findViewById(R.id.yearWv);
		monthWv = (WheelView) findViewById(R.id.monthWv);
		dayWv = (WheelView) findViewById(R.id.dayWv);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
	}

	private void initListers() {
		btn_cancel.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		yearWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				curYear = years[yearWv.getCurrentItem()].substring(0, years[monthWv.getCurrentItem()].length()-1);
				ftBean.setYear(curYear);
			}
		});
		monthWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String finalMonth;
				curMonth = months[monthWv.getCurrentItem()].substring(0, months[monthWv.getCurrentItem()].length() - 1);
				days = CalendarUtils.getDays(Integer.parseInt(curYear), Integer.parseInt(curMonth));
				dayWv.setViewAdapter(new ArrayWheelAdapter<String>(ChoiceDateView.this, days));
				if (Integer.parseInt(curDay) > CalendarUtils.getMonthLastDay(Integer.parseInt(curYear), Integer.parseInt(curMonth))){
					dayWv.setCurrentItem(CalendarUtils.getIndexOfItem(days,"1日"));
				} else {
					dayWv.setCurrentItem(CalendarUtils.getIndexOfItem(days,curDay+"日"));
				}
				curDay = days[dayWv.getCurrentItem()].substring(0, days[dayWv.getCurrentItem()].length()-1);
				String finalDay ="";
				if(Integer.parseInt(curDay)>=10){
					finalDay=curDay;
				}else{
					finalDay="0".concat(curDay);
				}
				ftBean.setDay(finalDay);

				if(Integer.parseInt(curMonth)>=10){
					finalMonth=curMonth;
				}else{
					finalMonth="0".concat(curMonth);
				}
				ftBean.setMonth(finalMonth);
			}
		});
		dayWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String finalDay;
				String subDay=days[dayWv.getCurrentItem()].substring(0, days[dayWv.getCurrentItem()].length()-1);
				if(Integer.parseInt(subDay)>=10){
					finalDay=subDay;
				}else{
					finalDay="0".concat(subDay);
				}
				ftBean.setDay(finalDay);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_confirm){
			if(ftBean!=null){
				ftBean.setFlag(getIntent().getIntExtra("flag", 0));
				transBeanSelect(ftBean);
			}
		}else if(v.getId()==R.id.btn_cancel){
			finish();
		}
	}

	protected void transBeanSelect(FeedbackDateBean ftBean2) {
		Intent intent = new Intent();
		intent.putExtra("bean", ftBean2);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void initDatas() {
		ftBean = new FeedbackDateBean();
		years = CalendarUtils.getYears(startYear, endYear);
		months = CalendarUtils.getMonths();

		yearWv.setViewAdapter(new ArrayWheelAdapter<String>(this, years));
		monthWv.setViewAdapter(new ArrayWheelAdapter<String>(this, months));

		// 设置可见条目数量
		yearWv.setVisibleItems(7);
		monthWv.setVisibleItems(7);
		dayWv.setVisibleItems(7);
		yearWv.setCurrentItem(CalendarUtils.getIndexOfItem(years, initYear+"年"));
		monthWv.setCurrentItem(CalendarUtils.getIndexOfItem(months, initMonth+"月"));
		curYear = years[yearWv.getCurrentItem()].substring(0, years[monthWv.getCurrentItem()].length()-1);
		curMonth = months[monthWv.getCurrentItem()].substring(0, months[monthWv.getCurrentItem()].length()-1);

		days = CalendarUtils.getDays(Integer.parseInt(curYear), Integer.parseInt(curMonth));
		dayWv.setViewAdapter(new ArrayWheelAdapter<String>(this, days));

		dayWv.setCurrentItem(CalendarUtils.getIndexOfItem(days,initDay+"日"));
		ftBean.setYear(curYear);

		String finalMonth;
		if(Integer.parseInt(curMonth)>=10){
			finalMonth=curMonth;
		}else{
			finalMonth="0".concat(curMonth);
		}
		ftBean.setMonth(finalMonth);
		String finalDay;
		curDay = days[dayWv.getCurrentItem()].substring(0, days[dayWv.getCurrentItem()].length()-1);
		if(Integer.parseInt(curDay)>=10){
			finalDay=curDay;
		}else{
			finalDay="0".concat(curDay);
		}
		ftBean.setDay(finalDay);
	}
}

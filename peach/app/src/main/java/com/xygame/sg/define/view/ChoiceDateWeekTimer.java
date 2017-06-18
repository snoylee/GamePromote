package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

public class ChoiceDateWeekTimer extends SGBaseActivity implements OnClickListener{
	
	private WheelView yearWv,monthWv,dayWv,weekWv,hourWv,minWv;
	String[] years;
	String[] months;
	String[] days;
	String[] weeks = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	String[] weekDatas={"周日"};
	String[] hoursArray={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
//	String[] minsArray={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
	String[] minsArray={"00","30"};
	private FeedbackDateBean ftBean;
	private Button mBtnConfirm,btn_cancel;
	private SimpleDateFormat sdf,df_ms;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_fulldate_layout);
		initView();
		initListers();
		initDatas();
		yearWv.setWheelBackground(R.color.white);
		monthWv.setWheelBackground(R.color.white);
		dayWv.setWheelBackground(R.color.white);
		
		weekWv.setWheelBackground(R.color.white);
		hourWv.setWheelBackground(R.color.white);
		minWv.setWheelBackground(R.color.white);
	}

	private void initView() {
		yearWv = (WheelView) findViewById(R.id.yearWv);
		monthWv = (WheelView) findViewById(R.id.monthWv);
		dayWv = (WheelView) findViewById(R.id.dayWv);
		
		weekWv = (WheelView) findViewById(R.id.weekWv);
		hourWv = (WheelView) findViewById(R.id.hourWv);
		minWv = (WheelView) findViewById(R.id.minWv);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
	}

	private void initListers() {
		btn_cancel.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		yearWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				ftBean.setYear(years[yearWv.getCurrentItem()].substring(0, years[monthWv.getCurrentItem()].length()-1));
//				weekWv.setCurrentItem(getWeekIndex(ftBean.getYear(), ftBean.getMonth(), ftBean.getDay()));
				ftBean.setWeek(weeks[getWeekIndex(ftBean.getYear(), ftBean.getMonth(), ftBean.getDay())]);
				weekDatas[0]=ftBean.getWeek();
				weekWv.setViewAdapter(new ArrayWheelAdapter<String>(ChoiceDateWeekTimer.this,
						weekDatas));
			}
		});
		monthWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String finalMonth;
				String subMonth=months[monthWv.getCurrentItem()].substring(0, months[monthWv.getCurrentItem()].length()-1);

				String curYear = years[yearWv.getCurrentItem()].substring(0, years[monthWv.getCurrentItem()].length()-1);
				String curMonth = months[monthWv.getCurrentItem()].substring(0, months[monthWv.getCurrentItem()].length()-1);
				String curDay = days[dayWv.getCurrentItem()].substring(0, days[dayWv.getCurrentItem()].length()-1);
				days = CalendarUtils.getDays(Integer.parseInt(curYear), Integer.parseInt(curMonth));
				dayWv.setViewAdapter(new ArrayWheelAdapter<String>(ChoiceDateWeekTimer.this, days));
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


				if(Integer.parseInt(subMonth)>=10){
					finalMonth=subMonth;
				}else{
					finalMonth="0".concat(subMonth);
				}
				ftBean.setMonth(finalMonth);
//				weekWv.setCurrentItem(getWeekIndex(ftBean.getYear(), ftBean.getMonth(), ftBean.getDay()));
				ftBean.setWeek(weeks[getWeekIndex(ftBean.getYear(), ftBean.getMonth(), ftBean.getDay())]);
				weekDatas[0]=ftBean.getWeek();
				weekWv.setViewAdapter(new ArrayWheelAdapter<String>(ChoiceDateWeekTimer.this,
						weekDatas));
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
//				weekWv.setCurrentItem(getWeekIndex(ftBean.getYear(), ftBean.getMonth(), ftBean.getDay()));
				ftBean.setWeek(weeks[getWeekIndex(ftBean.getYear(), ftBean.getMonth(), ftBean.getDay())]);
				weekDatas[0]=ftBean.getWeek();
				weekWv.setViewAdapter(new ArrayWheelAdapter<String>(ChoiceDateWeekTimer.this,
						weekDatas));
			}
		});
		
		
		
		hourWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				ftBean.setHour(hoursArray[hourWv.getCurrentItem()]);
			}
		});
		
		
		minWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				ftBean.setMin(minsArray[minWv.getCurrentItem()]);
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
		String formatDateStr=ftBean2.getYear().concat("-").concat(ftBean2.getMonth()).concat("-").concat(ftBean2.getDay()).concat(" ").concat(ftBean2.getHour()).concat(":").concat(ftBean2.getMin());
		String oral=ftBean2.getYear().concat("-").concat(ftBean2.getMonth()).concat("-").concat(ftBean2.getDay()).concat(" ").concat(ftBean2.getWeek()).concat(" ").concat(ftBean2.getHour()).concat(":").concat(ftBean2.getMin());
		ftBean2.setDateAllDesc(oral);
		ftBean2.setFormatDateStr(formatDateStr);
		ftBean2.setTimeLong(getLongTime(ftBean2.getYear(), ftBean2.getMonth(), ftBean2.getDay(), ftBean2.getHour(), ftBean2.getMin()));
		Intent intent = new Intent();
		intent.putExtra("bean", ftBean2);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void initDatas() {
		sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		df_ms = new SimpleDateFormat( "HH:mm:ss" );
		ftBean = new FeedbackDateBean();
		years = CalendarUtils.getYears(2006,2030);
		months = CalendarUtils.getMonths();
		days = CalendarUtils.getDays(CalendarUtils.getCurrentYear(), CalendarUtils.getCurrentMonth());
		yearWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				years));
		monthWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				months));
		dayWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				days));
		
		hourWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				hoursArray));
		minWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				minsArray));
		weekWv.setFocusableInTouchMode(false);
		Date mDate=new Date(System.currentTimeMillis());
		String currDate=sdf.format(mDate);
		String[] dateArray=currDate.split("-");
		String currTime=df_ms.format(mDate);
		String[] timeArray=currTime.split(":");
		// 设置可见条目数量
		yearWv.setVisibleItems(7);
		monthWv.setVisibleItems(7);
		dayWv.setVisibleItems(7);
		
		weekWv.setVisibleItems(7);
		hourWv.setVisibleItems(7);
		minWv.setVisibleItems(7);
		ftBean.setYear(dateArray[0]);
		ftBean.setMonth(dateArray[1]);
		ftBean.setDay(dateArray[2]);
		ftBean.setWeek(weeks[getWeekIndex(ftBean.getYear(), ftBean.getMonth(),ftBean.getDay())]);
		
		weekDatas[0]=ftBean.getWeek();
		weekWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				weekDatas));
		
		String monthFinal=String.valueOf(Integer.parseInt( ftBean.getMonth()));
		String dayFinal=String.valueOf(Integer.parseInt(ftBean.getDay()));
		yearWv.setCurrentItem(CalendarUtils.getIndexOfItem(years,ftBean.getYear().concat("年")));
		monthWv.setCurrentItem(CalendarUtils.getIndexOfItem(months,monthFinal.concat("月")));
		dayWv.setCurrentItem(CalendarUtils.getIndexOfItem(days,dayFinal.concat("日")));
		weekWv.setCurrentItem(0);
		for(int i=0;i<hoursArray.length;i++){
			if(timeArray[0].equals(hoursArray[i])){
				hourWv.setCurrentItem(i);
				ftBean.setHour(hoursArray[i]);
			}
		}
		ftBean.setMin(minsArray[0]);
//		for(int i=0;i<minsArray.length;i++){
//			if(timeArray[1].equals(minsArray[i])){
//				minWv.setCurrentItem(i);
//				ftBean.setMin(minsArray[i]);
//			}
//		}
	}
	
	private int getWeekIndex(String yy,String MM,String dd){
		int week_index=0;
		String dateStr=yy.concat("-").concat(MM).concat("-").concat(dd);
		try {
			Date date = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (week_index < 0) {
				week_index = 0;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return week_index;
	}
	
	
	private String getLongTime(String yy,String MM,String dd,String HH,String mm){
		String str=null;
		String dateStr=yy.concat("-").concat(MM).concat("-").concat(dd).concat(" ").concat(HH).concat(":").concat(mm);
		try {
			SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
			Date date = format.parse(dateStr);
			str=String.valueOf(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}

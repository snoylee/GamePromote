package com.xygame.sg.define.view;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChoiceYMView extends SGBaseActivity implements OnClickListener{
	
	private WheelView yearWv,monthWv;
	String[] years;
	String[] months;
	private String[] typeName;
	private FeedbackDateBean ftBean;
	private Button mBtnConfirm,btn_cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_layout);
		initView();
		initListers();
		initDatas();
		yearWv.setWheelBackground(R.color.white);
		monthWv.setWheelBackground(R.color.white);
	}

	private void initView() {
		yearWv = (WheelView) findViewById(R.id.yearWv);
		monthWv = (WheelView) findViewById(R.id.monthWv);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
	}

	private void initListers() {
		btn_cancel.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		yearWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				ftBean.setYear(years[yearWv.getCurrentItem()].substring(0, years[yearWv.getCurrentItem()].length()-1));
			}
		});
		monthWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				String finalMonth;
				String subMonth=months[monthWv.getCurrentItem()].substring(0, months[monthWv.getCurrentItem()].length()-1);
				if(Integer.parseInt(subMonth)>=10){
					finalMonth=subMonth;
				}else{
					finalMonth="0".concat(subMonth);
				}
				ftBean.setMonth(finalMonth);
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
		years = CalendarUtils.getYears(1979,2030);
		months = CalendarUtils.getMonths();
		yearWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				years));
		monthWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
				months));
		// 设置可见条目数量
		yearWv.setVisibleItems(7);
		monthWv.setVisibleItems(7);
		yearWv.setCurrentItem(CalendarUtils.getIndexOfItem(years,CalendarUtils.getCurrentYear()+"年"));
		monthWv.setCurrentItem(CalendarUtils.getIndexOfItem(months,CalendarUtils.getCurrentMonth()+"月"));
		ftBean.setYear(years[yearWv.getCurrentItem()].substring(0, years[yearWv.getCurrentItem()].length()-1));
		String subMonth=months[monthWv.getCurrentItem()].substring(0, months[monthWv.getCurrentItem()].length()-1);
		String finalMonth;
		if(Integer.parseInt(subMonth)>=10){
			finalMonth=subMonth;
		}else{
			finalMonth="0".concat(subMonth);
		}
		ftBean.setMonth(finalMonth);
	}
}

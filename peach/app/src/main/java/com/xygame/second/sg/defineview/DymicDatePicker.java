package com.xygame.second.sg.defineview;

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
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DymicDatePicker extends SGBaseActivity implements OnClickListener {

    private WheelView yearWv, monthWv, dayWv,hourWv,minWv;
    String[] years;
    String[] months;
    String[] days;
    String[] hoursArray={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    String[] minsArray={"00","30"};
    private FeedbackDateBean ftBean;
    private Button mBtnConfirm, btn_cancel;
    private String currYear, currMonth, currDay;
    private long dateTime;
    private int dateDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dymic_date_picker);
        initView();
        initListers();
        initDatas();
        yearWv.setWheelBackground(R.color.white);
        monthWv.setWheelBackground(R.color.white);
        dayWv.setWheelBackground(R.color.white);

        hourWv.setWheelBackground(R.color.white);
        minWv.setWheelBackground(R.color.white);
    }

    private void initDatas() {
        ftBean = new FeedbackDateBean();
        dateTime = getIntent().getLongExtra("dateTime", 0);
        dateDistance = getIntent().getIntExtra("dateDistance", 0);
        String currTimeStr = CalendarUtils.getDate(dateTime);
        String[] currTimeArray = currTimeStr.split("-");
        currYear = currTimeArray[0];
        currMonth = currTimeArray[1];
        currDay = currTimeArray[2];
        List<String> Years = new ArrayList<>();
        Years.add(currYear);
        List<String> Month = new ArrayList<>();
        Month.add(currMonth);
        List<String> Days = new ArrayList<>();
        Days.add(currDay);
        boolean addYear=true,addMonth=true;
        for (int i = 1; i < dateDistance; i++) {
            String timeStr = CalendarUtils.getDateDistance(dateTime, i);
            String[] timeArray = timeStr.split("-");
            if (addYear){
                if (!currYear.equals(timeArray[0])) {
                    Years.add(timeArray[0]);
                    addYear=false;
                }
            }
            if (addMonth){
                if (!currMonth.equals(timeArray[1])) {
                    Month.add(timeArray[1]);
                    addMonth=false;
                }
            }
            Days.add(timeArray[2]);
        }

        years = new String[Years.size()];
        for (int i = 0; i < Years.size(); i++) {
            years[i] = Years.get(i).concat("年");
        }
        months = new String[Month.size()];
        for (int i = 0; i < Month.size(); i++) {
            months[i] = Month.get(i).concat("月");
        }
        days = new String[Days.size()];
        for (int i = 0; i < Days.size(); i++) {
            days[i] = Days.get(i).concat("日");
        }

        yearWv.setViewAdapter(new ArrayWheelAdapter<>(this,
                years));
        monthWv.setViewAdapter(new ArrayWheelAdapter<>(this,
                months));
        dayWv.setViewAdapter(new ArrayWheelAdapter<>(this,
                days));
        hourWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
                hoursArray));
        minWv.setViewAdapter(new ArrayWheelAdapter<String>(this,
                minsArray));

        // 设置可见条目数量
        yearWv.setVisibleItems(7);
        monthWv.setVisibleItems(7);
        dayWv.setVisibleItems(7);
        hourWv.setVisibleItems(7);
        minWv.setVisibleItems(7);

        SimpleDateFormat df_ms = new SimpleDateFormat( "HH:mm:ss" );
        Date mDate=new Date(dateTime);
        String currTime=df_ms.format(mDate);
        String[] timeArray=currTime.split(":");
        for(int i=0;i<hoursArray.length;i++){
            if(timeArray[0].equals(hoursArray[i])){
                hourWv.setCurrentItem(i);
                ftBean.setHour(hoursArray[i]);
            }
        }
        ftBean.setMin(minsArray[0]);

        ftBean.setYear(years[0]);
        ftBean.setMonth(months[0]);
        ftBean.setDay(days[0]);

        yearWv.setCurrentItem(0);
        monthWv.setCurrentItem(0);
        dayWv.setCurrentItem(0);
    }

    private void initView() {
        yearWv = (WheelView) findViewById(R.id.yearWv);
        monthWv = (WheelView) findViewById(R.id.monthWv);
        dayWv = (WheelView) findViewById(R.id.dayWv);

        hourWv = (WheelView) findViewById(R.id.hourWv);
        minWv = (WheelView) findViewById(R.id.minWv);

        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void initListers() {
        btn_cancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        yearWv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ftBean.setYear(years[yearWv.getCurrentItem()]);
            }
        });
        monthWv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String subMonth = months[monthWv.getCurrentItem()];
                ftBean.setMonth(subMonth);
            }
        });
        dayWv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String subDay = days[dayWv.getCurrentItem()];
                ftBean.setDay(subDay);
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
        if (v.getId() == R.id.btn_confirm) {
            if (ftBean != null) {
                ftBean.setFlag(getIntent().getIntExtra("flag", 0));
                transBeanSelect(ftBean);
            }
        } else if (v.getId() == R.id.btn_cancel) {
            finish();
        }
    }

    protected void transBeanSelect(FeedbackDateBean ftBean2) {
        String formatDateStr = ftBean2.getYear().concat(ftBean2.getMonth()).concat(ftBean2.getDay()).concat(" ").concat(ftBean2.getHour()).concat(":").concat(ftBean2.getMin());
        ftBean2.setDateAllDesc(formatDateStr);
        ftBean2.setFormatDateStr(formatDateStr);
        ftBean2.setTimeLong(getLongTime(formatDateStr));
        Intent intent = new Intent();
        intent.putExtra("bean", ftBean2);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String getLongTime(String formatDateStr) {
        String str = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date date = format.parse(formatDateStr);
            str = String.valueOf(date.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
}

/*
 * 文 件 名:  PickPhotoesView.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月12日
 */
package com.xygame.second.sg.defineview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.xygame.second.sg.comm.adapter.ServiceDateAdater;
import com.xygame.second.sg.comm.adapter.ServiceTimeAdapter;
import com.xygame.second.sg.comm.bean.ServiceTimeBean;
import com.xygame.second.sg.comm.bean.ServiceTimeDateBean;
import com.xygame.second.sg.comm.bean.TimerDuringBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.CalendarUtils;
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
public class DymicServiceTimeDialog extends SGBaseActivity {

    private GridView dateGrid, timerGrid;
    private long dateTime;
    private int dateDistance;
    private List<ServiceTimeDateBean> dateDatas;
    private ServiceDateAdater serviceDateAdater;
    private ServiceTimeAdapter serviceTimeAdapter;
    private TimerDuringBean timerDuringBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dymic_service_time_layout);
        dateGrid = (GridView) findViewById(R.id.dateGrid);
        timerGrid = (GridView) findViewById(R.id.timerGrid);
        serviceDateAdater = new ServiceDateAdater(this, null);
        dateGrid.setAdapter(serviceDateAdater);
        serviceTimeAdapter = new ServiceTimeAdapter(this, null);
        timerGrid.setAdapter(serviceTimeAdapter);
        dateGrid.setOnItemClickListener(new CityItemListener());
        timerGrid.setOnItemClickListener(new AreaItemListener());
        initDatas();
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
                if (isGo()) {
                    timerDuringBean.setDateDatas(dateDatas);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, timerDuringBean);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(DymicServiceTimeDialog.this, "请选择档期时间", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDatas() {
        timerDuringBean = (TimerDuringBean) getIntent().getSerializableExtra("timerDuringBean");
        if (timerDuringBean.getDateDatas() == null) {
            dateDatas = new ArrayList<>();
            dateTime = getIntent().getLongExtra("dateTime", 0);
            dateDistance = getIntent().getIntExtra("dateDistance", 0);
            for (int i = 0; i < dateDistance; i++) {
                String yy, MM, dd;
                ServiceTimeDateBean item = new ServiceTimeDateBean();
                item.setId(String.valueOf(i));
                String timeStr = CalendarUtils.getDateDistance(dateTime, i);
                String[] timeArray = timeStr.split("-");
                yy = timeArray[0];
                dd = timeArray[2];
                if (timeArray[1].length() > 0) {
                    MM = timeArray[1];
                    item.setDate(MM.concat("-").concat(dd));
                } else {
                    MM = "0".concat(timeArray[1]);
                    item.setDate(MM.concat("-").concat(dd));
                }
                item.setDscStr(yy.concat("-").concat(MM).concat("-").concat(dd));
                item.setWeekened(CalendarUtils.WEEKS[CalendarUtils.getWeekIndex(yy, MM, dd)]);
                if (i==0){
                    item.setTimeBeans(getCurrDateTimeBeans());
                }else{
                    item.setTimeBeans(getTimeBeans());
                }
                dateDatas.add(item);
            }
        } else {
            dateDatas = timerDuringBean.getDateDatas();
        }

        serviceDateAdater.updateDatas(dateDatas);
        serviceTimeAdapter.updateAreaDatas(dateDatas.get(0).getTimeBeans());
    }

    private List<ServiceTimeBean> getCurrDateTimeBeans() {
        List<ServiceTimeBean> timeBeans = new ArrayList<>();
        int currHour=CalendarUtils.getHourInt(System.currentTimeMillis(),0);

        ServiceTimeBean item1 = new ServiceTimeBean();
        item1.setId("9");
        item1.setIsSelect(false);
        item1.setTime("09:00");
        if (currHour>=9){
            item1.setIsUsing(false);
        }else{
            item1.setIsUsing(true);
        }
        timeBeans.add(item1);

        ServiceTimeBean item2 = new ServiceTimeBean();
        item2.setId("10");
        item2.setIsSelect(false);
        item2.setTime("10:00");
        if (currHour>=10){
            item2.setIsUsing(false);
        }else{
            item2.setIsUsing(true);
        }
        timeBeans.add(item2);

        ServiceTimeBean item11 = new ServiceTimeBean();
        item11.setId("11");
        item11.setIsSelect(false);
        item11.setTime("11:00");
        if (currHour>=11){
            item11.setIsUsing(false);
        }else{
            item11.setIsUsing(true);
        }
        timeBeans.add(item11);

        ServiceTimeBean item12 = new ServiceTimeBean();
        item12.setId("12");
        item12.setIsSelect(false);
        item12.setTime("12:00");
        if (currHour>=12){
            item12.setIsUsing(false);
        }else{
            item12.setIsUsing(true);
        }
        timeBeans.add(item12);

        ServiceTimeBean item13 = new ServiceTimeBean();
        item13.setId("13");
        item13.setIsSelect(false);
        item13.setTime("13:00");
        if (currHour>=13){
            item13.setIsUsing(false);
        }else{
            item13.setIsUsing(true);
        }
        timeBeans.add(item13);

        ServiceTimeBean item14 = new ServiceTimeBean();
        item14.setId("14");
        item14.setIsSelect(false);
        item14.setTime("14:00");
        if (currHour>=14){
            item14.setIsUsing(false);
        }else{
            item14.setIsUsing(true);
        }
        timeBeans.add(item14);

        ServiceTimeBean item15 = new ServiceTimeBean();
        item15.setId("15");
        item15.setIsSelect(false);
        item15.setTime("15:00");
        if (currHour>=15){
            item15.setIsUsing(false);
        }else{
            item15.setIsUsing(true);
        }
        timeBeans.add(item15);

        ServiceTimeBean item16 = new ServiceTimeBean();
        item16.setId("16");
        item16.setIsSelect(false);
        item16.setTime("16:00");
        if (currHour>=16){
            item16.setIsUsing(false);
        }else{
            item16.setIsUsing(true);
        }
        timeBeans.add(item16);

        ServiceTimeBean item17 = new ServiceTimeBean();
        item17.setId("17");
        item17.setIsSelect(false);
        item17.setTime("17:00");
        if (currHour>=17){
            item17.setIsUsing(false);
        }else{
            item17.setIsUsing(true);
        }
        timeBeans.add(item17);

        ServiceTimeBean item18 = new ServiceTimeBean();
        item18.setId("18");
        item18.setIsSelect(false);
        item18.setTime("18:00");
        if (currHour>=18){
            item18.setIsUsing(false);
        }else{
            item18.setIsUsing(true);
        }
        timeBeans.add(item18);

        ServiceTimeBean item19 = new ServiceTimeBean();
        item19.setId("19");
        item19.setIsSelect(false);
        item19.setTime("19:00");
        if (currHour>=19){
            item19.setIsUsing(false);
        }else{
            item19.setIsUsing(true);
        }
        timeBeans.add(item19);

        ServiceTimeBean item20 = new ServiceTimeBean();
        item20.setId("20");
        item20.setIsSelect(false);
        item20.setTime("20:00");
        if (currHour>=20){
            item20.setIsUsing(false);
        }else{
            item20.setIsUsing(true);
        }
        timeBeans.add(item20);

        ServiceTimeBean item21 = new ServiceTimeBean();
        item21.setId("21");
        item21.setIsSelect(false);
        item21.setTime("21:00");
        if (currHour>=21){
            item21.setIsUsing(false);
        }else{
            item21.setIsUsing(true);
        }
        timeBeans.add(item21);

        ServiceTimeBean item22 = new ServiceTimeBean();
        item22.setId("22");
        item22.setIsSelect(false);
        item22.setTime("22:00");
        if (currHour>=22){
            item22.setIsUsing(false);
        }else{
            item22.setIsUsing(true);
        }
        timeBeans.add(item22);

        ServiceTimeBean item23 = new ServiceTimeBean();
        item23.setId("-1");
        item23.setIsSelect(false);
        item23.setTime("全部时间");
        if (currHour>=23){
            item23.setIsUsing(false);
        }else{
            item23.setIsUsing(true);
        }
        timeBeans.add(item23);
        return timeBeans;
    }

    private List<ServiceTimeBean> getTimeBeans() {
        List<ServiceTimeBean> timeBeans = new ArrayList<>();
        ServiceTimeBean item1 = new ServiceTimeBean();
        item1.setId("9");
        item1.setIsSelect(false);
        item1.setTime("09:00");
        item1.setIsUsing(true);
        timeBeans.add(item1);

        ServiceTimeBean item2 = new ServiceTimeBean();
        item2.setId("10");
        item2.setIsSelect(false);
        item2.setTime("10:00");
        item2.setIsUsing(true);
        timeBeans.add(item2);

        ServiceTimeBean item11 = new ServiceTimeBean();
        item11.setId("11");
        item11.setIsSelect(false);
        item11.setTime("11:00");
        item11.setIsUsing(true);
        timeBeans.add(item11);

        ServiceTimeBean item12 = new ServiceTimeBean();
        item12.setId("12");
        item12.setIsSelect(false);
        item12.setTime("12:00");
        item12.setIsUsing(true);
        timeBeans.add(item12);

        ServiceTimeBean item13 = new ServiceTimeBean();
        item13.setId("13");
        item13.setIsSelect(false);
        item13.setTime("13:00");
        item13.setIsUsing(true);
        timeBeans.add(item13);

        ServiceTimeBean item14 = new ServiceTimeBean();
        item14.setId("14");
        item14.setIsSelect(false);
        item14.setTime("14:00");
        item14.setIsUsing(true);
        timeBeans.add(item14);

        ServiceTimeBean item15 = new ServiceTimeBean();
        item15.setId("15");
        item15.setIsSelect(false);
        item15.setTime("15:00");
        item15.setIsUsing(true);
        timeBeans.add(item15);

        ServiceTimeBean item16 = new ServiceTimeBean();
        item16.setId("16");
        item16.setIsSelect(false);
        item16.setTime("16:00");
        item16.setIsUsing(true);
        timeBeans.add(item16);

        ServiceTimeBean item17 = new ServiceTimeBean();
        item17.setId("17");
        item17.setIsSelect(false);
        item17.setTime("17:00");
        item17.setIsUsing(true);
        timeBeans.add(item17);

        ServiceTimeBean item18 = new ServiceTimeBean();
        item18.setId("18");
        item18.setIsSelect(false);
        item18.setTime("18:00");
        item18.setIsUsing(true);
        timeBeans.add(item18);

        ServiceTimeBean item19 = new ServiceTimeBean();
        item19.setId("19");
        item19.setIsSelect(false);
        item19.setTime("19:00");
        item19.setIsUsing(true);
        timeBeans.add(item19);

        ServiceTimeBean item20 = new ServiceTimeBean();
        item20.setId("20");
        item20.setIsSelect(false);
        item20.setTime("20:00");
        item20.setIsUsing(true);
        timeBeans.add(item20);

        ServiceTimeBean item21 = new ServiceTimeBean();
        item21.setId("21");
        item21.setIsSelect(false);
        item21.setTime("21:00");
        item21.setIsUsing(true);
        timeBeans.add(item21);

        ServiceTimeBean item22 = new ServiceTimeBean();
        item22.setId("22");
        item22.setIsSelect(false);
        item22.setTime("22:00");
        item22.setIsUsing(true);
        timeBeans.add(item22);

        ServiceTimeBean item23 = new ServiceTimeBean();
        item23.setId("-1");
        item23.setIsSelect(false);
        item23.setTime("全部时间");
        item23.setIsUsing(true);
        timeBeans.add(item23);
        return timeBeans;
    }

    private boolean isGo() {
        boolean flag = false;
        for (ServiceTimeDateBean it : dateDatas) {
            boolean subFlag = false;
            for (ServiceTimeBean item : it.getTimeBeans()) {
                if (item.isSelect()) {
                    subFlag = true;
                    break;
                }
            }
            if (subFlag) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private class CityItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ServiceTimeDateBean item = serviceDateAdater.getItem(position);
            serviceDateAdater.setCurTrue(item);
            serviceTimeAdapter.updateAreaDatas(item.getTimeBeans());
        }
    }

    private class AreaItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ServiceTimeBean areaBean = serviceTimeAdapter.getItem(position);
            if (areaBean.isUsing()){
                serviceTimeAdapter.updateSelect(areaBean);
            }
        }
    }
}

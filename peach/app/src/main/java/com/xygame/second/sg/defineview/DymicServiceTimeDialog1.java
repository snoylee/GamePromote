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
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.adapter.BuySchedulesTimeAdapter;
import com.xygame.second.sg.comm.adapter.ServiceDateAdater;
import com.xygame.second.sg.comm.adapter.ServiceSchedulesTimeAdapter;
import com.xygame.second.sg.comm.bean.ServiceTimeBean;
import com.xygame.second.sg.comm.bean.ServiceTimeDateBean;
import com.xygame.second.sg.comm.bean.TimerDuringBean;
import com.xygame.second.sg.jinpai.UsedTimeBean;
import com.xygame.second.sg.jinpai.bean.ScheduleTimeBean;
import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月12日
 * @action [选择拍照或相册的界面]
 */
public class DymicServiceTimeDialog1 extends SGBaseActivity implements OnClickListener {

    private GridView dateGrid, timerGrid;
    private TextView totalText;
    private long dateTime;
    private int dateDistance;
    private List<ServiceTimeDateBean> dateDatas;
    private ServiceDateAdater serviceDateAdater;
    private BuySchedulesTimeAdapter serviceTimeAdapter;
    private TimerDuringBean timerDuringBean;
    private List<ScheduleTimeBean> scheduleTimeBeans;
    private String singlePrice,actId,actTitle,userId;
    private int singlePriceInt, totalPriceInt;
    private Map<String, Map<Integer, Boolean>> selectedMap = new HashMap<>();
    private ServiceTimeDateBean currDateBean;
    private List<UsedTimeBean> usedTimeBeans;
    private String transDate,transStartTime,transEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dymic_service1_time_layout);
        totalText = (TextView) findViewById(R.id.totalText);
        dateGrid = (GridView) findViewById(R.id.dateGrid);
        timerGrid = (GridView) findViewById(R.id.timerGrid);
        serviceDateAdater = new ServiceDateAdater(this, null);
        dateGrid.setAdapter(serviceDateAdater);
        serviceTimeAdapter = new BuySchedulesTimeAdapter(this, null);
        timerGrid.setAdapter(serviceTimeAdapter);
        dateGrid.setOnItemClickListener(new CityItemListener());
        timerGrid.setOnItemClickListener(new AreaItemListener());
        findViewById(R.id.rightButton).setOnClickListener(this);
        findViewById(R.id.comfirm).setOnClickListener(this);
        singlePrice = getIntent().getStringExtra("singlePrice");
        singlePriceInt = Integer.parseInt(singlePrice);
        totalText.setText("0");
        actId=getIntent().getStringExtra("actId");
        actTitle=getIntent().getStringExtra("actTitle");
        userId=getIntent().getStringExtra("userId");
        ThreadPool.getInstance().excuseThread(new InitDateTime());
    }

    private Handler mHadler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    serviceDateAdater.updateDatas(dateDatas);
                    currDateBean=dateDatas.get(0);
                    serviceTimeAdapter.updateAreaDatas(currDateBean.getTimeBeans());
                    break;
                case 1:
                    totalText.setText(String.valueOf(totalPriceInt));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class InitDateTime implements Runnable {

        @Override
        public void run() {
            initDatas();
            mHadler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rightButton:
                finish();
                break;
            case R.id.comfirm:
                if (isGo()){
                    comfirmDialog("确定要支付：".concat(totalText.getText().toString()));
                }else{
                    Toast.makeText(this,"请选择时间",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void comfirmDialog(String tip){
        TwoButtonDialog dialog = new TwoButtonDialog(this,tip, R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
                        commitAction();
					}

					@Override
					public void cancelListener() {
					}
				});
				dialog.show();
    }

    private boolean isGo() {
        boolean flag=false;
        List<ServiceTimeBean> tempTimes=currDateBean.getTimeBeans();
        for (ServiceTimeBean timeBean:tempTimes){
            if (timeBean.isUsing()){
                if (timeBean.isSelect()){
                    flag= true;
                    break;
                }
            }
        }
        return flag;
    }

    private void commitAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            object.put("actTitle",actTitle);
            object.put("userId",userId);
            String fromTimeStr="",toTimeStr="";
            List<ServiceTimeBean> tempTimes=currDateBean.getTimeBeans();
            List<ServiceTimeBean> selectTimes=new ArrayList<>();
            for (ServiceTimeBean timeBean:tempTimes) {
                if (timeBean.isUsing()) {
                    if (timeBean.isSelect()){
                        selectTimes.add(timeBean);
                    }
                }
            }
            transStartTime=selectTimes.get(0).getTime();
            fromTimeStr=currDateBean.getDscStr().concat(" ").concat(transStartTime);
            transEndTime=selectTimes.get(selectTimes.size()-1).getTime();
            toTimeStr=currDateBean.getDscStr().concat(" ").concat(transEndTime);
            transDate=currDateBean.getDscStr();
            object.put("bookStartTime",CalendarUtils.getTimeLong(fromTimeStr));
            if (TextUtils.isEmpty(toTimeStr)){
                transEndTime=transStartTime;
                object.put("bookEndTime",CalendarUtils.getTimeLong(fromTimeStr));
            }else {
                object.put("bookEndTime",CalendarUtils.getTimeLong(toTimeStr));
            }
            item.setData(object);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_FUFEI_APPLY);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_FUFEI_APPLY);
        } catch (Exception e1) {
            ShowMsgDialog.cancel();
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_FUFEI_APPLY:
                if ("0000".equals(data.getCode())) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, true);
                    intent.putExtra("actDate",transDate);
                    intent.putExtra("actStartTime",transStartTime);
                    intent.putExtra("actEndTime",transEndTime);
                    intent.putExtra("singlePrice",singlePrice);
                    intent.putExtra("totalPrice",String.valueOf(totalPriceInt));
                    intent.putExtra("actId",data.getRecord());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else if ("6012".equals(data.getCode())) {
                    jinPai7004Act("余额不足", data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void jinPai7004Act(String tip, final String record) {
        TwoButtonDialog dialog = new TwoButtonDialog(this, tip, "以后再说", "前往充值", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        Intent intent = new Intent(DymicServiceTimeDialog1.this, PayStoneActivity.class);
                        intent.putExtra("stoneValue", String.valueOf(ConstTaskTag.getDoublePrice(record)));
                        startActivity(intent);
                    }
                });
        dialog.show();
    }

    private void initDatas() {
        timerDuringBean = (TimerDuringBean) getIntent().getSerializableExtra("timerDuringBean");
        scheduleTimeBeans = timerDuringBean.getScheduleTimeBeans();
        usedTimeBeans=timerDuringBean.getUsedTimeBeans();

        dateDatas = new ArrayList<>();
        dateTime = getIntent().getLongExtra("dateTime", 0);
        dateDistance = getIntent().getIntExtra("dateDistance", 0);
        for (int i = 0; i < dateDistance; i++) {
            String yueStr, riStr;
            ServiceTimeDateBean item = new ServiceTimeDateBean();
            item.setId(String.valueOf(i));
            int yy = CalendarUtils.getYearInt(dateTime, i);
            int MM = CalendarUtils.getMonthInt(dateTime, i);
            int dd = CalendarUtils.getDayInt(dateTime, i);
            if (MM < 10) {
                yueStr = "0" + MM;
            } else {
                yueStr = "" + MM;
            }
            if (dd < 10) {
                riStr = "0" + dd;
            } else {
                riStr = "" + dd;
            }
            item.setDscStr(String.valueOf(yy).concat("-").concat(yueStr.concat("-").concat(riStr)));
            item.setDate(yueStr.concat("-").concat(riStr));
            item.setWeekened(CalendarUtils.WEEKS[CalendarUtils.getWeekIndex(String.valueOf(yy), yueStr, riStr)]);
            item.setTimeBeans(getTimeBeans());
            dateDatas.add(item);
        }

        for (int i = 0; i < dateDatas.size(); i++) {
            String locaDate = dateDatas.get(i).getDate();
            for (ScheduleTimeBean item : scheduleTimeBeans) {
                String serverDate = CalendarUtils.getMonth_DayFromLongToString(item.getServiceDay());
                if (locaDate.equals(serverDate)) {
                    Map<Integer, Boolean> timeIndexs = getTimeMap();
                    List<ServiceTimeBean> timeBeans = dateDatas.get(i).getTimeBeans();
                    if (!TextUtils.isEmpty(item.getHour9())) {
                        timeBeans.get(0).setIsSelect(false);
                        timeBeans.get(0).setIsUsed(false);
                        timeBeans.get(0).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(0).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour10())) {
                        timeBeans.get(1).setIsSelect(false);
                        timeBeans.get(1).setIsUsed(false);
                        timeBeans.get(1).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(1).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour11())) {
                        timeBeans.get(2).setIsSelect(false);
                        timeBeans.get(2).setIsUsed(false);
                        timeBeans.get(2).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(2).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour12())) {
                        timeBeans.get(3).setIsSelect(false);
                        timeBeans.get(3).setIsUsed(false);
                        timeBeans.get(3).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(3).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour13())) {
                        timeBeans.get(4).setIsSelect(false);
                        timeBeans.get(4).setIsUsed(false);
                        timeBeans.get(4).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(4).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour14())) {
                        timeBeans.get(5).setIsSelect(false);
                        timeBeans.get(5).setIsUsed(false);
                        timeBeans.get(5).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(5).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour15())) {
                        timeBeans.get(6).setIsSelect(false);
                        timeBeans.get(6).setIsUsed(false);
                        timeBeans.get(6).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(6).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour16())) {
                        timeBeans.get(7).setIsSelect(false);
                        timeBeans.get(7).setIsUsed(false);
                        timeBeans.get(7).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(7).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour17())) {
                        timeBeans.get(8).setIsSelect(false);
                        timeBeans.get(8).setIsUsed(false);
                        timeBeans.get(8).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(8).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour18())) {
                        timeBeans.get(9).setIsSelect(false);
                        timeBeans.get(9).setIsUsed(false);
                        timeBeans.get(9).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(9).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour19())) {
                        timeBeans.get(10).setIsSelect(false);
                        timeBeans.get(10).setIsUsed(false);
                        timeBeans.get(10).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(10).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour20())) {
                        timeBeans.get(11).setIsSelect(false);
                        timeBeans.get(11).setIsUsed(false);
                        timeBeans.get(11).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(11).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour21())) {
                        timeBeans.get(12).setIsSelect(false);
                        timeBeans.get(12).setIsUsed(false);
                        timeBeans.get(12).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(12).getIndex(), true);
                    }
                    if (!TextUtils.isEmpty(item.getHour22())) {
                        timeBeans.get(13).setIsSelect(false);
                        timeBeans.get(13).setIsUsed(false);
                        timeBeans.get(13).setIsUsing(true);
                        timeIndexs.put(timeBeans.get(13).getIndex(), true);
                    }
                    if (TextUtils.isEmpty(item.getHour9())&&TextUtils.isEmpty(item.getHour10())&&TextUtils.isEmpty(item.getHour11())&&TextUtils.isEmpty(item.getHour12())&&TextUtils.isEmpty(item.getHour13())&&TextUtils.isEmpty(item.getHour14())
                            &&TextUtils.isEmpty(item.getHour15())&&TextUtils.isEmpty(item.getHour16())&&TextUtils.isEmpty(item.getHour17())&&TextUtils.isEmpty(item.getHour18())&&TextUtils.isEmpty(item.getHour19())&&TextUtils.isEmpty(item.getHour20())
                            &&TextUtils.isEmpty(item.getHour21())&&TextUtils.isEmpty(item.getHour22())){
                        for (int p=0;p<timeBeans.size();p++){
                            timeBeans.get(p).setIsSelect(false);
                            timeBeans.get(p).setIsUsed(false);
                            timeBeans.get(p).setIsUsing(true);
                            timeIndexs.put(timeBeans.get(p).getIndex(), true);
                        }
                    }
                    selectedMap.put(locaDate, timeIndexs);
                    dateDatas.get(i).setTimeBeans(timeBeans);
                }
            }

            for (UsedTimeBean usedBean:usedTimeBeans){
                if (locaDate.equals(CalendarUtils.getMonth_DayFromLongToString(usedBean.getBookStartTime()))){
                    List<ServiceTimeBean> timeBeans = dateDatas.get(i).getTimeBeans();
                    String[] startTimeArray=CalendarUtils.getHour_MinFromLongToString(usedBean.getBookStartTime()).split(":");
                    String[] startEndArray=CalendarUtils.getHour_MinFromLongToString(usedBean.getBookEndTime()).split(":");
                    int start=Integer.parseInt(startTimeArray[0]);
                    int end=Integer.parseInt(startEndArray[0]);
                    for (int k=0;k<timeBeans.size();k++){
                        if (timeBeans.get(k).getIndex()>=start&&timeBeans.get(k).getIndex()<=end){
                            timeBeans.get(k).setIsSelect(false);
                            timeBeans.get(k).setIsUsed(true);
                            timeBeans.get(k).setIsUsing(false);
                        }
                    }
                    dateDatas.get(i).setTimeBeans(timeBeans);
                }
            }
        }

        List<ServiceTimeBean> tempTimes=dateDatas.get(0).getTimeBeans();

        int currHour=CalendarUtils.getHourInt(System.currentTimeMillis(), 0);
        for (int j=0;j<tempTimes.size();j++){
            if (tempTimes.get(j).isUsing()){
                if (currHour>=Integer.parseInt(tempTimes.get(j).getId())){
                    tempTimes.get(j).setIsSelect(false);
                    tempTimes.get(j).setIsUsed(true);
                    tempTimes.get(j).setIsUsing(false);
                }
            }
        }

        dateDatas.get(0).setTimeBeans(tempTimes);

        List<ServiceTimeDateBean> tempRemoveDatas=new ArrayList<>();
        for (int j=0;j<dateDatas.size();j++){
            boolean isShow=false;
            List<ServiceTimeBean> timeBeans = dateDatas.get(j).getTimeBeans();
            for (ServiceTimeBean item:timeBeans){
                if (item.isUsing()){
                    isShow=true;
                    break;
                }
            }
            if (!isShow){
                tempRemoveDatas.add(dateDatas.get(j));
            }
        }
        if (tempRemoveDatas.size()>0){
            for (ServiceTimeDateBean it:tempRemoveDatas){
                for (int k=0;k<dateDatas.size();k++){
                    if (it.getDscStr().equals(dateDatas.get(k).getDscStr())){
                        dateDatas.remove(k);
                        break;
                    }
                }
            }
        }
    }

    private Map<Integer, Boolean> getTimeMap(){
        Map<Integer, Boolean> tempMap=new HashMap<>();
        for (int i=9;i<23;i++){
            tempMap.put(i,false);
        }
        return tempMap;
    }

    private List<ServiceTimeBean> getTimeBeans() {
        List<ServiceTimeBean> timeBeans = new ArrayList<>();
        ServiceTimeBean item1 = new ServiceTimeBean();
        item1.setId("9");
        item1.setIndex(9);
        item1.setIsSelect(false);
        item1.setTime("09:00");
        item1.setIsSelect(false);
        item1.setIsUsed(true);
        item1.setIsUsing(false);
        timeBeans.add(item1);

        ServiceTimeBean item2 = new ServiceTimeBean();
        item2.setId("10");
        item2.setIndex(10);
        item2.setIsSelect(false);
        item2.setTime("10:00");
        item2.setIsSelect(false);
        item2.setIsUsed(true);
        item2.setIsUsing(false);
        timeBeans.add(item2);

        ServiceTimeBean item11 = new ServiceTimeBean();
        item11.setId("11");
        item11.setIndex(11);
        item11.setIsSelect(false);
        item11.setTime("11:00");
        item11.setIsSelect(false);
        item11.setIsUsed(true);
        item11.setIsUsing(false);
        timeBeans.add(item11);

        ServiceTimeBean item12 = new ServiceTimeBean();
        item12.setId("12");
        item12.setIndex(12);
        item12.setIsSelect(false);
        item12.setTime("12:00");
        item12.setIsSelect(false);
        item12.setIsUsed(true);
        item12.setIsUsing(false);
        timeBeans.add(item12);

        ServiceTimeBean item13 = new ServiceTimeBean();
        item13.setId("13");
        item13.setIndex(13);
        item13.setIsSelect(false);
        item13.setTime("13:00");
        item13.setIsSelect(false);
        item13.setIsUsed(true);
        item13.setIsUsing(false);
        timeBeans.add(item13);

        ServiceTimeBean item14 = new ServiceTimeBean();
        item14.setId("14");
        item14.setIndex(14);
        item14.setIsSelect(false);
        item14.setTime("14:00");
        item14.setIsSelect(false);
        item14.setIsUsed(true);
        item14.setIsUsing(false);
        timeBeans.add(item14);

        ServiceTimeBean item15 = new ServiceTimeBean();
        item15.setId("15");
        item15.setIndex(15);
        item15.setIsSelect(false);
        item15.setTime("15:00");
        item15.setIsSelect(false);
        item15.setIsUsed(true);
        item15.setIsUsing(false);
        timeBeans.add(item15);

        ServiceTimeBean item16 = new ServiceTimeBean();
        item16.setId("16");
        item16.setIndex(16);
        item16.setIsSelect(false);
        item16.setTime("16:00");
        item16.setIsSelect(false);
        item16.setIsUsed(true);
        item16.setIsUsing(false);
        timeBeans.add(item16);

        ServiceTimeBean item17 = new ServiceTimeBean();
        item17.setId("17");
        item17.setIndex(17);
        item17.setIsSelect(false);
        item17.setTime("17:00");
        item17.setIsSelect(false);
        item17.setIsUsed(true);
        item17.setIsUsing(false);
        timeBeans.add(item17);

        ServiceTimeBean item18 = new ServiceTimeBean();
        item18.setId("18");
        item18.setIndex(18);
        item18.setIsSelect(false);
        item18.setTime("18:00");
        item18.setIsSelect(false);
        item18.setIsUsed(true);
        item18.setIsUsing(false);
        timeBeans.add(item18);

        ServiceTimeBean item19 = new ServiceTimeBean();
        item19.setId("19");
        item19.setIndex(19);
        item19.setIsSelect(false);
        item19.setTime("19:00");
        item19.setIsSelect(false);
        item19.setIsUsed(true);
        item19.setIsUsing(false);
        timeBeans.add(item19);

        ServiceTimeBean item20 = new ServiceTimeBean();
        item20.setId("20");
        item20.setIndex(20);
        item20.setIsSelect(false);
        item20.setTime("20:00");
        item20.setIsSelect(false);
        item20.setIsUsed(true);
        item20.setIsUsing(false);
        timeBeans.add(item20);

        ServiceTimeBean item21 = new ServiceTimeBean();
        item21.setId("21");
        item21.setIndex(21);
        item21.setIsSelect(false);
        item21.setTime("21:00");
        item21.setIsSelect(false);
        item21.setIsUsed(true);
        item21.setIsUsing(false);
        timeBeans.add(item21);

        ServiceTimeBean item22 = new ServiceTimeBean();
        item22.setId("22");
        item22.setIndex(22);
        item22.setIsSelect(false);
        item22.setTime("22:00");
        item22.setIsSelect(false);
        item22.setIsUsed(true);
        item22.setIsUsing(false);
        timeBeans.add(item22);
        return timeBeans;
    }

//    private boolean isGo() {
//        boolean flag = false;
//        for (ServiceTimeDateBean it : dateDatas) {
//            boolean subFlag = false;
//            for (ServiceTimeBean item : it.getTimeBeans()) {
//                if (item.isSelect()) {
//                    subFlag = true;
//                    break;
//                }
//            }
//            if (subFlag) {
//                flag = true;
//                break;
//            }
//        }
//        return flag;
//    }

    private class CityItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (currDateBean!=null){
                serviceDateAdater.updateItem(currDateBean);
            }
            currDateBean = serviceDateAdater.getItem(position);
            serviceDateAdater.setCurTrue(currDateBean);
            serviceTimeAdapter.updateAreaDatas(currDateBean.getTimeBeans());
            ThreadPool.getInstance().excuseThread(new UpdateTotalPrice());
        }
    }

    private class AreaItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ServiceTimeBean itemBean = serviceTimeAdapter.getItem(position);
            if (itemBean.isUsing()) {
                serviceTimeAdapter.updateTimeItem(position);
                ThreadPool.getInstance().excuseThread(new UpdateTotalPrice());
//                serviceTimeAdapter.notifyDataSetChanged();
//                if (currDateBean.getFromPoint() == 0 && currDateBean.getToPoint() == 0) {
//                    currDateBean.setFromPoint(itemBean.getIndex());
//                    serviceTimeAdapter.selectItem(itemBean.getIndex());
//                } else {
//                    if (currDateBean.getFromPoint() != 0 && currDateBean.getToPoint() == 0) {
//                        justHaveFromPoint(itemBean);
//                    } else if (currDateBean.getFromPoint() != 0 && currDateBean.getToPoint() != 0) {
//                        justHaveFromToPoint(itemBean);
//                    }
//                }
//                serviceTimeAdapter.notifyDataSetChanged();
//
//                ThreadPool.getInstance().excuseThread(new UpdateTotalPrice());
            }
        }
    }

    private class UpdateTotalPrice implements Runnable{

        @Override
        public void run() {
            int mCount=0;
            List<ServiceTimeDateBean> dateBeans=serviceDateAdater.getDateDatas();
            for (ServiceTimeDateBean item:dateBeans){
                List<ServiceTimeBean> timeBeans= item.getTimeBeans();
                for (ServiceTimeBean timeBean:timeBeans){
                    if (timeBean.isSelect()){
                        mCount=mCount+1;
                    }
                }
            }
            totalPriceInt=singlePriceInt*mCount;
            mHadler.sendEmptyMessage(1);
        }
    }

    private void justHaveFromToPoint(ServiceTimeBean itemBean) {
        Map<Integer, Boolean> selectDatas = selectedMap.get(currDateBean.getDate());
        if (itemBean.getIndex() > currDateBean.getFromPoint() && itemBean.getIndex() < currDateBean.getToPoint()) {
            for (int i = currDateBean.getFromPoint(); i <= itemBean.getIndex(); i++) {
                serviceTimeAdapter.selectItem(i);
            }
            for (int i = itemBean.getIndex()+1; i <= currDateBean.getToPoint(); i++) {
                serviceTimeAdapter.cancelSelectItem(i);
            }
            currDateBean.setToPoint(itemBean.getIndex());
        } else if (itemBean.getIndex() < currDateBean.getFromPoint()) {
            boolean flag = true;
            for (int i = itemBean.getIndex(); i <= currDateBean.getFromPoint(); i++) {
                if (!selectDatas.get(i)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int i = itemBean.getIndex(); i <= currDateBean.getFromPoint(); i++) {
                    serviceTimeAdapter.selectItem(i);
                }
                currDateBean.setFromPoint(itemBean.getIndex());
            } else {
                for (int i = currDateBean.getFromPoint(); i <= currDateBean.getToPoint(); i++) {
                    serviceTimeAdapter.cancelSelectItem(i);
                }
                serviceTimeAdapter.selectItem(itemBean.getIndex());
                currDateBean.setFromPoint(itemBean.getIndex());
                currDateBean.setToPoint(0);
            }
        } else if (itemBean.getIndex() > currDateBean.getToPoint()) {
            boolean flag = true;
            for (int i = currDateBean.getToPoint(); i <= itemBean.getIndex(); i++) {
                boolean ffflag=selectDatas.get(i);
                if (!ffflag) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int i = currDateBean.getToPoint(); i <= itemBean.getIndex(); i++) {
                    serviceTimeAdapter.selectItem(i);
                }
                currDateBean.setToPoint(itemBean.getIndex());
            } else {
                for (int i = currDateBean.getFromPoint(); i <= currDateBean.getToPoint(); i++) {
                    serviceTimeAdapter.cancelSelectItem(i);
                }
                serviceTimeAdapter.selectItem(itemBean.getIndex());
                currDateBean.setFromPoint(itemBean.getIndex());
                currDateBean.setToPoint(0);
            }
        }
    }

    private void justHaveFromPoint(ServiceTimeBean itemBean) {
        Map<Integer, Boolean> selectDatas = selectedMap.get(currDateBean.getDate());
        if (itemBean.getIndex() > currDateBean.getFromPoint()) {
            boolean flag = true;
            for (int i = currDateBean.getFromPoint(); i <= itemBean.getIndex(); i++) {
                if (!selectDatas.get(i)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int i = currDateBean.getFromPoint(); i <= itemBean.getIndex(); i++) {
                    serviceTimeAdapter.selectItem(i);
                }
                currDateBean.setToPoint(itemBean.getIndex());
            } else {
                serviceTimeAdapter.cancelSelectItem(currDateBean.getFromPoint());
                currDateBean.setFromPoint(itemBean.getIndex());
                serviceTimeAdapter.selectItem(itemBean.getIndex());
            }
        } else if (itemBean.getIndex() < currDateBean.getFromPoint()) {
            boolean flag = true;
            for (int i = itemBean.getIndex(); i <= currDateBean.getFromPoint(); i++) {
                if (!selectDatas.get(i)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int i = itemBean.getIndex(); i <= currDateBean.getFromPoint(); i++) {
                    serviceTimeAdapter.selectItem(i);
                }
                currDateBean.setToPoint(currDateBean.getFromPoint());
                currDateBean.setFromPoint(itemBean.getIndex());
            } else {
                serviceTimeAdapter.cancelSelectItem(currDateBean.getFromPoint());
                currDateBean.setFromPoint(itemBean.getIndex());
                serviceTimeAdapter.selectItem(itemBean.getIndex());
            }
        }
    }
}

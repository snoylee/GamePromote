package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.define.view.WaitGodOptionView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by tony on 2016/12/22.
 */
public class WaitGodActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView timerCount,titleName,godCount;
    private View backButton,numTipBackGround,rightButton;
    private ImageView rightbuttonIcon;
    private TimerCountBean timerCountBean;
    private final int maxPushCount=5;
    private final int[] ADD_NUMS={55,66,78,89,94,105,117,127,136,148,151};
    private final int[] STOP_NUMS={553,628,436,725,366,371,284,655,573,639,488};
    private int totalNums=0,countAssort=0;
    private String whereFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_god_layout);
        registerBoradcastReceiver();
        initViews();
        addListener();
        initDatas();
    }

    private void initViews() {
        godCount=(TextView)findViewById(R.id.godCount);
        rightButton=findViewById(R.id.rightButton);
        rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
        numTipBackGround=findViewById(R.id.numTipBackGround);
        timerCount=(TextView)findViewById(R.id.timerCount);
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
    }

    private void initDatas() {
        whereFrom=getIntent().getStringExtra("whereFrom");
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.more_icon);
        titleName.setText("等待大神");
        numTipBackGround.getBackground().setAlpha(150);
        timerCountBean= TimerEngine.quaryTimerBeansByDuringLength(this, UserPreferencesUtil.getUserId(this), Constants.QIANGDAN_TIMER);
        execuseTimerWork(timerCountBean);
        if ("xiaDan".equals(whereFrom)){
            godCount.setText(String.valueOf(totalNums));
            ThreadPool.getInstance().excuseThread(new WaittingForThread());
        }else{
            godCount.setText(String.valueOf( STOP_NUMS[getRandomIndex()]));
        }
    }

    private void execuseTimerWork(TimerCountBean timerCountBean){
        long pastTime=XMPPUtils.getLeftSecondTime(Long.parseLong(timerCountBean.getStartTime()));
        long totalTime=Long.parseLong(UserPreferencesUtil.getorderExpireTime(this))*60;
        if (pastTime<totalTime){
            new Timer10Min(new Long(totalTime-pastTime).intValue()*1000, 1000,timerCountBean).start();
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
               finish();
                break;
            case R.id.rightButton:{
                Intent intent=new Intent(this, WaitGodOptionView.class);
                startActivityForResult(intent,0);
                break;
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.setAction(XMPPUtils.ACTION_TO_MAINPAGE);
        intent.putExtra("oralTimer", "toFirstPage");
        sendBroadcast(intent);
        super.finish();
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.ACTION_SYS_MSG);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.ACTION_SYS_MSG.equals(intent.getAction())) {
                Intent intentTiao=new Intent(WaitGodActivity.this, WaitGodTwoActivity.class);
                startActivity(intentTiao);
                finish();
            }
        }
    };

    private class Timer10Min extends CountDownTimer {
        private  TimerCountBean timerCountBean;
        public Timer10Min(int minute, int second, TimerCountBean timerCountBean){
            super(minute, second);
            this.timerCountBean=timerCountBean;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long diff=millisUntilFinished / 1000;
            long minutes = ((diff % (24 * 60 * 60)) % (60 * 60)) / 60;
            long second=((diff % (24 * 60 * 60)) % (60 * 60)) % 60;
            String isSuccess="";
            if (minutes!=0){
                isSuccess=String.valueOf(minutes).concat(":").concat(String.valueOf(second));
            }else{
                isSuccess=String.valueOf(second);
            }
            timerCount.setText(isSuccess);
        }

        @Override
        public void onFinish() {
            UserPreferencesUtil.setIsReadGetMoney(SGApplication.getInstance().getBaseContext(), false);
            timerCountBean.setStartTime("0");
            TimerEngine.updateTimerBean(SGApplication.getInstance().getBaseContext(), timerCountBean);
            CacheService.getInstance().clearGodQiangDanRebackBeanCache();
            finish();
        }
    }

    private void cancelOrderAction() {
        try {
            RequestBean item = new RequestBean();
            item.setIsPublic(false);
            JSONObject object = new JSONObject();
            object.put("orderId",timerCountBean.getGroupId());
            item.setData(object);
            item.setServiceURL(ConstTaskTag.QUEST_CANCEL_ORDER_QIANG);
            ShowMsgDialog.showNoMsg(this, false);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CANCEL_ORDER_QIANG);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_CANCEL_ORDER_QIANG:
                if ("0000".equals(data.getCode())){
                    UserPreferencesUtil.setIsReadGetMoney(SGApplication.getInstance().getBaseContext(), false);
                    timerCountBean.setStartTime("0");
                    TimerEngine.updateTimerBean(SGApplication.getInstance().getBaseContext(), timerCountBean);
                    CacheService.getInstance().clearGodQiangDanRebackBeanCache();
                    finish();
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
               int index=data.getIntExtra(Constants.COMEBACK,0);
                switch (index){
                    case 1:
                        cancelOrderAction();
                        break;
                    case 2:
                        break;
                }
                break;
            }
            default:
                break;
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    godCount.setText(String.valueOf(totalNums));
                    if (countAssort<=maxPushCount){
                        ThreadPool.getInstance().excuseThread(new WaittingForThread());
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class WaittingForThread implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(getRandomMinil());
                countAssort=countAssort+1;
                totalNums=totalNums+ADD_NUMS[getRandomIndex()];
                mHandler.sendEmptyMessage(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private int getRandomIndex() {
        int max=10;
        int min=0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
       return s;
    }

    private int getRandomMinil() {
        int max=5000;
        int min=1000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
}
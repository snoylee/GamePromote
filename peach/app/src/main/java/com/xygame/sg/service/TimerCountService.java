package com.xygame.sg.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xygame.second.sg.utils.XmppConnectionManager;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import java.util.Date;
import java.util.List;

/**
 * Created by tony on 2016/2/23.
 */
public class TimerCountService extends Service {

    @Override
    public void onCreate() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(XMPPUtils.BORDCAST_PUSH_MESASAGE);
        registerReceiver(reConnectionBroadcastReceiver, mFilter);

        List<TimerCountBean> timers=TimerEngine.quaryTimerBeans(SGApplication.getInstance().getBaseContext(),UserPreferencesUtil.getUserId(SGApplication.getInstance().getBaseContext()));
        for (TimerCountBean it:timers){
           if (!TextUtils.isEmpty(it.getStartTime())&&!"0".equals(it.getStartTime())){
               excuteAction(it);
           }
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(reConnectionBroadcastReceiver);
        super.onDestroy();
    }

    BroadcastReceiver reConnectionBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(XMPPUtils.BORDCAST_PUSH_MESASAGE)) {
                TimerCountBean timerCountBean=(TimerCountBean)intent.getSerializableExtra("timerCountBean");
                excuteAction(timerCountBean);
            }
        }

    };

    private void excuteAction(TimerCountBean timerCountBean){
        if (Constants.QIANGDAN_TIMER.equals(timerCountBean.getDuringLength())){
            long pastTime=getLeftSecondTime(Long.parseLong(timerCountBean.getStartTime()));
            long totalTime=Long.parseLong(Constants.QIANGDAN_TIMER)*1000;
            if (pastTime<totalTime){
//                timerCount10Min(new Long(totalTime-pastTime).intValue(), 1000);
                new Timer10Min(new Long(totalTime-pastTime).intValue(), 1000,timerCountBean).start();
            }
        }else if (Constants.PAY_TIMER.equals(timerCountBean.getDuringLength())){
            long pastTime=getLeftSecondTime(Long.parseLong(timerCountBean.getStartTime()));
            long totalTime=Long.parseLong(Constants.PAY_TIMER)*1000;
            if (pastTime<totalTime){
                new Timer30Min(new Long(totalTime-pastTime).intValue(), 1000,timerCountBean).start();
            }
        }
    }

    private class Timer10Min extends CountDownTimer{
        private  TimerCountBean timerCountBean;
        public Timer10Min(int minute, int second, TimerCountBean timerCountBean){
            super(minute, second);
            this.timerCountBean=timerCountBean;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long second=millisUntilFinished / 1000;
            long minutes = second / 60;
            String isSuccess="";
            if (minutes!=0){
                isSuccess=String.valueOf(minutes).concat(":").concat(String.valueOf(second));
            }else{
                isSuccess=String.valueOf(second);
            }
            Intent intent = new Intent();
            intent.setAction(XMPPUtils.ACTION_RECONNECT_STATE);
            intent.putExtra("oralTimer", isSuccess);
            sendBroadcast(intent);
        }

        @Override
        public void onFinish() {
            UserPreferencesUtil.setIsReadGetMoney(SGApplication.getInstance().getBaseContext(),false);
            timerCountBean.setStartTime("0");
            TimerEngine.updateTimerBean(SGApplication.getInstance().getBaseContext(),timerCountBean);
            Intent intent = new Intent();
            intent.setAction(XMPPUtils.ACTION_RECONNECT_STATE);
            intent.putExtra("oralTimer", "timerFinish");
            sendBroadcast(intent);
        }
    }

    private class Timer30Min extends CountDownTimer{
        private  TimerCountBean timerCountBean;
        public Timer30Min(int minute, int second, TimerCountBean timerCountBean){
            super(minute, second);
            this.timerCountBean=timerCountBean;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long second=millisUntilFinished / 1000;
            long minutes = ((second % (24 * 60 * 60)) % (60 * 60)) / 60;
            String isSuccess="";
            if (minutes!=0){
                isSuccess=String.valueOf(minutes).concat(":").concat(String.valueOf(second));
            }else{
                isSuccess=String.valueOf(second);
            }
            Intent intent = new Intent();
            intent.setAction(XMPPUtils.BORDCAST_PUSH_MESASAGE_SUB);
            intent.putExtra("oralTimer", isSuccess);
            sendBroadcast(intent);
        }

        @Override
        public void onFinish() {
            UserPreferencesUtil.setIsReadGetMoney(SGApplication.getInstance().getBaseContext(),false);
            timerCountBean.setStartTime("0");
            TimerEngine.updateTimerBean(SGApplication.getInstance().getBaseContext(),timerCountBean);
            Intent intent = new Intent();
            intent.setAction(XMPPUtils.BORDCAST_PUSH_MESASAGE_SUB);
            intent.putExtra("oralTimer", "timerFinish");
            sendBroadcast(intent);
        }
    }

    private void timerCount10Min(int minute, int second) {
        // TODO Auto-generated method stub
        new CountDownTimer(minute, second) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                long second=millisUntilFinished / 1000;
                long minutes = ((second % (24 * 60 * 60)) % (60 * 60)) / 60;
                String isSuccess="";
                if (minutes!=0){
                   isSuccess=String.valueOf(minutes).concat(":").concat(String.valueOf(second));
                }else{
                    isSuccess=String.valueOf(second);
                }
                Intent intent = new Intent();
                intent.setAction(XMPPUtils.ACTION_RECONNECT_STATE);
                intent.putExtra("oralTimer", isSuccess);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                UserPreferencesUtil.setIsReadGetMoney(SGApplication.getInstance().getBaseContext(),false);
                Intent intent = new Intent();
                intent.setAction(XMPPUtils.ACTION_RECONNECT_STATE);
                intent.putExtra("oralTimer", "timerFinish");
                sendBroadcast(intent);
            }
        }.start();

    }

    public long getLeftSecondTime(long startTime) {
        long diff;
        long limitTime=0;
        Date d2 = new Date();
        Date d1 = new Date(startTime);
        diff = d2.getTime()-d1.getTime();// 这样得到的差值是微秒级别
        limitTime=diff/1000;
        return limitTime;
    }
}

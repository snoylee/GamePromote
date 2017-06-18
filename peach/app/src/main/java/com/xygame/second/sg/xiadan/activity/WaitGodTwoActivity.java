package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.CancelGodListListener;
import com.xygame.second.sg.xiadan.activity.waitfor.BlackMemberBean;
import com.xygame.second.sg.xiadan.activity.waitfor.ListViewCompat;
import com.xygame.second.sg.xiadan.activity.waitfor.SlideAdapter;
import com.xygame.second.sg.xiadan.activity.waitfor.SlideView;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.define.view.WaitGodOptionView;
import com.xygame.sg.define.view.XuanGodPopView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/22.
 */
public class WaitGodTwoActivity extends SGBaseActivity implements View.OnClickListener , SlideView.OnSlideListener, CancelGodListListener{
    private TextView timerCount,titleName;
    private View backButton,rightButton;
    private ImageView rightbuttonIcon;

    private SlideView mLastSlideViewWithStatusOn;
    private SlideAdapter adapter;
    private ListViewCompat mListView;
    private BlackMemberBean tempActionBean;
    private MediaPlayer mediaPlayer;
    private TimerCountBean timerCountBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_god_two_layout);
        registerBoradcastReceiver();
        initViews();
        addListener();
        initDatas();
    }

    private void initViews() {
        mListView = (ListViewCompat) findViewById(R.id.list);
        rightButton=findViewById(R.id.rightButton);
        rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
        timerCount=(TextView)findViewById(R.id.timerCount);
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
//        View waitgod_footview= LayoutInflater.from(this).inflate(R.layout.waitgod_footview,null);
//        mListView.addFooterView(waitgod_footview);
    }

    private void initDatas() {
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.more_icon);
        titleName.setText("等待大神");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        adapter = new SlideAdapter(this, null);
        adapter.addCancelBlackListListener(this);
        adapter.addSlidViewListener(this);
        mListView.setAdapter(adapter);
        timerCountBean= TimerEngine.quaryTimerBeansByDuringLength(this, UserPreferencesUtil.getUserId(this), Constants.QIANGDAN_TIMER);
        if (timerCountBean!=null){
            execuseTimerWork();
        }
        ShowMsgDialog.showNoMsg(this, true);
        ThreadPool.getInstance().excuseThread(new InitTransferDatas());
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

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ShowMsgDialog.cancel();
                    List<BlackMemberBean> vector=( List<BlackMemberBean> )msg.obj;
                    adapter.addDatas(vector);
                    boolean isfirstShowView=UserPreferencesUtil.isFirstGodQinagReback(WaitGodTwoActivity.this);
                    if (!isfirstShowView){
                        UserPreferencesUtil.setIsFirstGodQinagReback(WaitGodTwoActivity.this,true);
                        Intent intent=new Intent(WaitGodTwoActivity.this,WaitGodTwoFirstReviewActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class InitTransferDatas implements Runnable{
        @Override
        public void run() {
            List<BlackMemberBean> vector=new ArrayList<>();
            List<GodQiangDanRebackBean> qiangDanRebackBeans= CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(WaitGodTwoActivity.this));
            if (qiangDanRebackBeans!=null){
                for (GodQiangDanRebackBean item:qiangDanRebackBeans){
                    BlackMemberBean blackMemberBean=new BlackMemberBean();
                    blackMemberBean.setUsernick(item.getUsernick());
                    blackMemberBean.setUserIcon(item.getUserIcon());
                    blackMemberBean.setAge(item.getAge());
                    blackMemberBean.setGender(item.getGender());
                    blackMemberBean.setOrderNums(item.getOrderCount());
                    blackMemberBean.setUserId(item.getUserId());
                    blackMemberBean.setVideoUrl(item.getVideoUrl());
                    blackMemberBean.setVoiceUrl(item.getVoiceUrl());
                    blackMemberBean.setPriceId(item.getPriceId());
                    blackMemberBean.setPriceRate(item.getPriceRate());
                    blackMemberBean.setSkillTitle(item.getSkillTitle());
                    blackMemberBean.setSkillCode(item.getSkillCode());
                    blackMemberBean.setStartTime(item.getStartTime());
                    blackMemberBean.setHoldTime(item.getHoldTime());
                    blackMemberBean.setOrderId(item.getOrderId());
                    blackMemberBean.setOral(item.getOral());
                    blackMemberBean.setAddress(item.getAddress());
                    vector.add(blackMemberBean);
                }
            }
            Message message=new Message();
            message.obj=vector;
            message.what=0;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    @Override
    public void cancelBlackListListener(BlackMemberBean blackMemberBean) {
        GodQiangDanRebackBean godQiangDanRebackBean=new GodQiangDanRebackBean();
        godQiangDanRebackBean.setUserId(blackMemberBean.getUserId());
        godQiangDanRebackBean.setAge(blackMemberBean.getAge());
        godQiangDanRebackBean.setGender(blackMemberBean.getGender());
        godQiangDanRebackBean.setPriceId(blackMemberBean.getPriceId());
        godQiangDanRebackBean.setPriceRate(blackMemberBean.getPriceRate());
        godQiangDanRebackBean.setSkillTitle(blackMemberBean.getSkillTitle());
        godQiangDanRebackBean.setUserIcon(blackMemberBean.getUserIcon());
        godQiangDanRebackBean.setUsernick(blackMemberBean.getUsernick());
        godQiangDanRebackBean.setVideoUrl(blackMemberBean.getVideoUrl());
        godQiangDanRebackBean.setVoiceUrl(blackMemberBean.getVoiceUrl());
        godQiangDanRebackBean.setOrderCount(blackMemberBean.getOrderNums());
        godQiangDanRebackBean.setSkillCode(blackMemberBean.getSkillCode());
        godQiangDanRebackBean.setStartTime(blackMemberBean.getStartTime());
        godQiangDanRebackBean.setHoldTime(blackMemberBean.getHoldTime());
        godQiangDanRebackBean.setOrderId(blackMemberBean.getOrderId());
        godQiangDanRebackBean.setAddress(blackMemberBean.getAddress());
        godQiangDanRebackBean.setOral(blackMemberBean.getOral());
        Intent intent =new Intent(this,QiangGodDetailActivity.class);
        intent.putExtra("userId",blackMemberBean.getUserId());
        intent.putExtra("skillCode",blackMemberBean.getSkillCode());
        intent.putExtra("orderId",blackMemberBean.getOrderId());
        intent.putExtra("bean",godQiangDanRebackBean);
        startActivity(intent);
        this.tempActionBean = blackMemberBean;
    }

    @Override
    public void playGodVoice(BlackMemberBean blackMemberBean) {
        this.tempActionBean = blackMemberBean;
        new PlayMp3Listener(blackMemberBean.getVoiceUrl());
    }

    @Override
    public void playGodVideo(BlackMemberBean blackMemberBean) {
        this.tempActionBean = blackMemberBean;
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("vidoUrl", blackMemberBean.getVideoUrl());
        startActivity(intent);
    }

    @Override
    public void intoDetailAction(BlackMemberBean blackMemberBean) {
        this.tempActionBean = blackMemberBean;
        GodQiangDanRebackBean godQiangDanRebackBean=new GodQiangDanRebackBean();
        godQiangDanRebackBean.setUserId(blackMemberBean.getUserId());
        godQiangDanRebackBean.setAge(blackMemberBean.getAge());
        godQiangDanRebackBean.setGender(blackMemberBean.getGender());
        godQiangDanRebackBean.setPriceId(blackMemberBean.getPriceId());
        godQiangDanRebackBean.setPriceRate(blackMemberBean.getPriceRate());
        godQiangDanRebackBean.setSkillTitle(blackMemberBean.getSkillTitle());
        godQiangDanRebackBean.setUserIcon(blackMemberBean.getUserIcon());
        godQiangDanRebackBean.setUsernick(blackMemberBean.getUsernick());
        godQiangDanRebackBean.setVideoUrl(blackMemberBean.getVideoUrl());
        godQiangDanRebackBean.setVoiceUrl(blackMemberBean.getVoiceUrl());
        godQiangDanRebackBean.setOrderCount(blackMemberBean.getOrderNums());
        godQiangDanRebackBean.setSkillCode(blackMemberBean.getSkillCode());
        godQiangDanRebackBean.setStartTime(blackMemberBean.getStartTime());
        godQiangDanRebackBean.setHoldTime(blackMemberBean.getHoldTime());
        godQiangDanRebackBean.setOrderId(blackMemberBean.getOrderId());
        godQiangDanRebackBean.setAddress(blackMemberBean.getAddress());
        godQiangDanRebackBean.setOral(blackMemberBean.getOral());
        Intent intent =new Intent(this,XuanGodPopView.class);
        intent.putExtra("bean", godQiangDanRebackBean);
        startActivity(intent);
    }

    private class PlayMp3Listener implements View.OnClickListener {
        private String url;

        public PlayMp3Listener(String url) {
            this.url = url;
        }


        @Override
        public void onClick(View v) {
            playerWorker(url);
        }
    }

    private void playerWorker(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.ACTION_SYS_MSG);
        myIntentFilter.addAction(XMPPUtils.ADD_FRIEND_QEQUEST);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.ACTION_SYS_MSG.equals(intent.getAction())) {
                String reSet=getIntent().getStringExtra("flagTag");
                if ("reSet".equals(reSet)){
                    ShowMsgDialog.showNoMsg(WaitGodTwoActivity.this, true);
                    ThreadPool.getInstance().excuseThread(new InitTransferDatas());
                }else{
                    GodQiangDanRebackBean godQiangDanRebackBean=(GodQiangDanRebackBean)intent.getSerializableExtra("godQiangDanRebackBean");
                    if (godQiangDanRebackBean!=null){
                        addListItem(godQiangDanRebackBean);
                    }
                }
            }else if (XMPPUtils.ADD_FRIEND_QEQUEST.equals(intent.getAction())){
                finish();
            }
        }
    };

    private void addListItem(GodQiangDanRebackBean item) {
        BlackMemberBean blackMemberBean=new BlackMemberBean();
        blackMemberBean.setUsernick(item.getUsernick());
        blackMemberBean.setUserIcon(item.getUserIcon());
        blackMemberBean.setAge(item.getAge());
        blackMemberBean.setGender(item.getGender());
        blackMemberBean.setOrderNums(item.getOrderCount());
        blackMemberBean.setUserId(item.getUserId());
        blackMemberBean.setVideoUrl(item.getVideoUrl());
        blackMemberBean.setVoiceUrl(item.getVoiceUrl());
        blackMemberBean.setPriceId(item.getPriceId());
        blackMemberBean.setPriceRate(item.getPriceRate());
        blackMemberBean.setSkillTitle(item.getSkillTitle());
        blackMemberBean.setSkillCode(item.getSkillCode());
        blackMemberBean.setStartTime(item.getStartTime());
        blackMemberBean.setHoldTime(item.getHoldTime());
        blackMemberBean.setOrderId(item.getOrderId());
        blackMemberBean.setOral(item.getOral());
        blackMemberBean.setAddress(item.getAddress());
        adapter.addItem(blackMemberBean);
    }

    private void execuseTimerWork(){
        long pastTime=XMPPUtils.getLeftSecondTime(Long.parseLong(timerCountBean.getStartTime()));
        long totalTime=Long.parseLong(UserPreferencesUtil.getorderExpireTime(this))*60;
        if (pastTime<totalTime){
            new Timer10Min(new Long(totalTime-pastTime).intValue()*1000, 1000,timerCountBean).start();
        }
    }

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
}
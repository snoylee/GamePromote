package com.xygame.second.sg.jinpai.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JinPaiPriceActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName, stoneNums, stoneStep, lastJinpaier, takeTotalPrice, stoneStep1, leftTimer;
    private View backButton, rightButton, comfimButton, jianButton, addButton;
    private ImageView rightbuttonIcon;
    private EditText jpPriceText;
    private String actId, leftTimerStr;
    private long hours;
    private long minutes;
    private long seconds;
    private long diff;
    private long days;
    private boolean isJinpai=true,isAllowRefresh=true;
    private String currPrice,currPriceOwner,totalPriceCount,userId,plusherId,actTitle,userBalance,stepValue="100",refreshFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_price_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        leftTimer = (TextView) findViewById(R.id.leftTimer);
        addButton = findViewById(R.id.addButton);
        jianButton = findViewById(R.id.jianButton);
        jpPriceText = (EditText) findViewById(R.id.jpPriceText);
        comfimButton = findViewById(R.id.comfimButton);
        stoneStep1 = (TextView) findViewById(R.id.stoneStep1);
        takeTotalPrice = (TextView) findViewById(R.id.takeTotalPrice);
        lastJinpaier = (TextView) findViewById(R.id.lastJinpaier);
        stoneStep = (TextView) findViewById(R.id.stoneStep);
        stoneNums = (TextView) findViewById(R.id.stoneNums);
        rightButton = findViewById(R.id.rightButton);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
    }

    private void initDatas() {
        stoneStep.setText(stepValue);
        stoneStep1.setText(stepValue);
        actId = getIntent().getStringExtra("actId");
        leftTimerStr = getIntent().getStringExtra("leftTimerStr");
        actTitle=getIntent().getStringExtra("actTitle");
        plusherId=getIntent().getStringExtra("plusherId");
        titleName.setText("活动出价");
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.refresh_icon);
        queryCurrBidWinner("1");
    }

    private void addListener() {
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        comfimButton.setOnClickListener(this);
        jianButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.rightButton:
                if (isAllowRefresh){
                    queryCurrBidWinner("0");
                }
                break;
            case R.id.comfimButton:
                if (isJinpai){
                    if (isGo()){
                        commitJinPaiAction();
                    }
                }else{
                    Toast.makeText(this,"竞拍已截止",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.jianButton:
                updatePriceEditor(0);
                break;
            case R.id.addButton:
                updatePriceEditor(1);
                break;
        }
    }

    private void commitJinPaiAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);

            object.put("actTitle", actTitle);
            object.put("userId", plusherId);
            object.put("bidPrice", jpPriceText.getText().toString().trim());

            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_ACT_CURR_BIDER);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_CURR_BIDER);
    }

    private Boolean isGo(){
        String currNewPrice=jpPriceText.getText().toString().trim();
        int currPriceValue=Integer.parseInt(currPrice);
        int leftPriceValue=Integer.parseInt(userBalance);
        int currNewPriceValue=Integer.parseInt(currNewPrice);
        if (currNewPriceValue<=currPriceValue){
            Toast.makeText(this, "出价必须大于当前价格", Toast.LENGTH_SHORT).show();
            return false;
        }
        int addPrice=currNewPriceValue-currPriceValue;
        int stepInt=Integer.parseInt(stepValue);
        if (addPrice%stepInt!=0){
            Toast.makeText(this, "出价金额加价幅度必须是当前加价幅度的倍数", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (leftPriceValue<currNewPriceValue){
            Toast.makeText(this, "您的帐号余额不足", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userId.equals(UserPreferencesUtil.getUserId(this))){
            Toast.makeText(this, "您不能连续竞拍", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updatePriceEditor(int falg){
        String currPrice=jpPriceText.getText().toString().trim();
        int currPriceValue;
        if (!TextUtils.isEmpty(currPrice)){
            currPriceValue=Integer.parseInt(currPrice);;
        }else {
            currPriceValue=0;
        }

        switch (falg){
            case 0:
                if (currPriceValue>100){
                    currPriceValue=currPriceValue-100;
                    jpPriceText.setText(String.valueOf(currPriceValue));
                }
                break;
            case 1:
                currPriceValue=currPriceValue+100;
                jpPriceText.setText(String.valueOf(currPriceValue));
                break;
        }
    }

    public void queryCurrBidWinner(String flag) {
        this.refreshFlag=flag;
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            if ("1".equals(flag)){
                object.put("hasBalance", "1");
            }
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if ("1".equals(flag)){
            ShowMsgDialog.showNoMsg(this, true);
        }else{
            ShowMsgDialog.showNoMsg(this, false);
        }
        item.setServiceURL(ConstTaskTag.QUEST_ACT_CURR_BID);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_CURR_BID);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_CURR_BID:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ACT_CURR_BIDER:
                if ("0000".equals(data.getCode())) {
                    currPrice= jpPriceText.getText().toString().trim();
                    currPriceOwner=UserPreferencesUtil.getUserNickName(this);
                    userId=UserPreferencesUtil.getUserId(this);
                    totalPriceCount=String.valueOf(Integer.parseInt(totalPriceCount) + 1);
                    updatePageView();
                    Toast.makeText(this, "竞拍成功", Toast.LENGTH_SHORT).show();
                } else if ("7004".equals(data.getCode())){
                    parseDatas(data.getRecord());
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(String record) {
        startWorkTimer();
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object=new JSONObject(record);
                userId = StringUtils.getJsonValue(object, "userId");
                currPriceOwner=StringUtils.getJsonValue(object, "usernick");
                currPrice=StringUtils.getJsonValue(object, "bidPrice");
                totalPriceCount=StringUtils.getJsonValue(object, "totalBidCount");
                userBalance=StringUtils.getJsonValue(object, "userBalance");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            userId = "";
            currPrice="0";
            currPriceOwner="";
            totalPriceCount="0";
            userBalance="0";
        }
        updatePageView();
        if (!"1".equals(refreshFlag)){
            isAllowRefresh=false;
            timerCount(5000, 1000);
        }
    }

    private void timerCount(int minute, int second) {
        // TODO Auto-generated method stub
        new CountDownTimer(minute, second) {

            @Override
            public void onTick(long millisUntilFinished) {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                handler.sendEmptyMessage(3);
            }
        }.start();

    }

    private void updatePageView() {
        stoneNums.setText(currPrice);
        lastJinpaier.setText(currPriceOwner);
        takeTotalPrice.setText(totalPriceCount);
        jpPriceText.setText(currPrice);
    }

    private void startWorkTimer(){
        getTime();
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);
    }

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    diff = diff - 1000;
                    getShowTime();
                    if (diff > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                    }else{
                        isJinpai=false;
                        leftTimer.setText("竞拍已截止");
                    }
                    break;
                case 2:
                    isAllowRefresh=false;
                    break;
                case 3:
                    isAllowRefresh=true;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getShowTime() {
        days = diff / (1000 * 60 * 60 * 24);
        hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60))
                / (1000 * 60);
        seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        updateTimer();
    }

    private void getTime() {
        try {
            Date d1 = new Date(Long.parseLong(leftTimerStr));
            Date d2 = new Date();
            diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long limitTime=diff/1000;
            days = limitTime / (24 * 60 * 60);
            hours =  (limitTime % (24 * 60 * 60)) / (60 * 60);
            minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
            seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            updateTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTimer() {
        StringBuffer sb = new StringBuffer();
        if (days != 0) {
            String dayStr = String.valueOf(days).concat("天");
            sb.append(dayStr);
        }
        if (hours != 0) {
            String hourStr = String.valueOf(hours).concat("小时");
            sb.append(hourStr);
        }
        if (minutes != 0) {
            String minStr = String.valueOf(minutes).concat("分");
            sb.append(minStr);
        }
        if (seconds != 0) {
            String minStr = String.valueOf(seconds).concat("秒");
            sb.append(minStr);
        }
        leftTimer.setText(sb.toString().replace("-", ""));
    }
}

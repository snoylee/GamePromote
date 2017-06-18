package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tendcloud.tenddata.TCAgent;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.second.sg.xiadan.bean.TransIfoToDetailBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 2016/12/23.
 */
public class OrderPayActivity extends SGBasePaymentActivity implements View.OnClickListener{
    private TextView titleName, userName,godAppName,priceValue,timeCountText,totalValue;
    private View backButton, payButtonView,yuELayout,aliPayLayout,WeiXinLayout;
    private CircularImage avatar_iv,godAppIcon;
    private String addressTextString,curTimeNums,userIcon,userNick,userId,oralTextStr,startTime,singlePriceValue,totalPriceValue,timerTextStr,fialDate,payTypeIdStr,orderId,payTimeStr;
    private ImageLoader mImageLoader;
    private  JinPaiBigTypeBean currPinLeiBean;
    private int payType;
    private ImageView yueIcon,alipayIcon,weixinIcon;
    private String whereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_pay_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        avatar_iv = (CircularImage) findViewById(R.id.avatar_iv);
        godAppIcon = (CircularImage) findViewById(R.id.godAppIcon);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        userName=(TextView)findViewById(R.id.userName);
        timeCountText = (TextView) findViewById(R.id.timeCountText);
        priceValue = (TextView) findViewById(R.id.priceValue);
        godAppName = (TextView) findViewById(R.id.godAppName);
        totalValue = (TextView) findViewById(R.id.totalValue);
        payButtonView = findViewById(R.id.payButtonView);
        yuELayout = findViewById(R.id.yuELayout);
        aliPayLayout = findViewById(R.id.aliPayLayout);
        WeiXinLayout = findViewById(R.id.WeiXinLayout);
        yueIcon=(ImageView)findViewById(R.id.yueIcon);
        alipayIcon=(ImageView)findViewById(R.id.alipayIcon);
        weixinIcon=(ImageView)findViewById(R.id.weixinIcon);
    }

    private void initDatas() {
        whereFrom=getIntent().getStringExtra("whereFrom");
        orderId=getIntent().getStringExtra("orderId");
        payType=1;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("支付订单");
        payTimeStr=getIntent().getStringExtra("payTimeStr");
        userIcon=getIntent().getStringExtra("userIcon");
        userNick=getIntent().getStringExtra("userNick");
        userId=getIntent().getStringExtra("userId");
        startTime=getIntent().getStringExtra("startTime");
        curTimeNums=getIntent().getStringExtra("curTimeNums");
        addressTextString=getIntent().getStringExtra("addressTextString");
        oralTextStr=getIntent().getStringExtra("oralTextStr");
        timerTextStr=getIntent().getStringExtra("timerText");
        singlePriceValue=getIntent().getStringExtra("singlePriceValue");
        totalPriceValue=getIntent().getStringExtra("totalPriceValue");
        fialDate=getIntent().getStringExtra("fialDate");
        currPinLeiBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("currPinLeiBean");
        mImageLoader.loadImage(userIcon, avatar_iv, true);
        userName.setText(userNick);
        godAppName.setText(currPinLeiBean.getName());
        priceValue.setText(singlePriceValue);
        timeCountText.setText("*".concat(curTimeNums));

        totalValue.setText("支付".concat(Constants.getMoneyFormat(totalPriceValue)).concat("元"));
        mImageLoader.loadImage(currPinLeiBean.getUrl(), godAppIcon, true);
    }

    private void addListener() {
        yuELayout.setOnClickListener(this);
        backButton.setOnClickListener(this);
        aliPayLayout.setOnClickListener(this);
        WeiXinLayout.setOnClickListener(this);
        payButtonView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.payButtonView:
                switchCommitWay();
                break;
            case R.id.yuELayout:
                payType=1;
                yueIcon.setImageResource(R.drawable.gou);
                alipayIcon.setImageResource(R.drawable.gou_null);
                weixinIcon.setImageResource(R.drawable.gou_null);
                break;
            case R.id.aliPayLayout:
                payType=2;
                yueIcon.setImageResource(R.drawable.gou_null);
                alipayIcon.setImageResource(R.drawable.gou);
                weixinIcon.setImageResource(R.drawable.gou_null);
                break;
            case R.id.WeiXinLayout:
                payType=3;
                yueIcon.setImageResource(R.drawable.gou_null);
                alipayIcon.setImageResource(R.drawable.gou_null);
                weixinIcon.setImageResource(R.drawable.gou);
                break;
        }
    }

    private void switchCommitWay() {
        switch (payType){
            case 1:
                if ("qiangDan".equals(whereFrom)){
                    payTypeIdStr="appbal";
                    commitQiangDanAction();
                }else{
                    commitAction();
                }
                break;
            case 2:
                payTypeIdStr="moreidolsAppAlipay2";
                if ("qiangDan".equals(whereFrom)){
                    commitQiangDanAction();
                }else{
                    commitPay();
                }
                break;
            case 3:
                payTypeIdStr="moreidolsWeixinPay";
                if ("qiangDan".equals(whereFrom)){
                    commitQiangDanAction();
                }else{
                    commitPay();
                }
                break;
        }
    }

    private void commitQiangDanAction() {
        try {
            if (!CalendarUtils.isPassed10Min(UserPreferencesUtil.getpayExpireTime(this),payTimeStr)){
                RequestBean item = new RequestBean();
                JSONObject obj = new JSONObject();
                obj.put("orderId", orderId);
                obj.put("payType", payTypeIdStr);
                obj.put("orderUsernick", userNick);
                obj.put("skillName", currPinLeiBean.getName());
                ShowMsgDialog.showNoMsg(this, false);
                item.setData(obj);
                item.setServiceURL(ConstTaskTag.QUEST_COMMIT_ORDER_PAY);
                ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_COMMIT_ORDER_PAY);
            }else{
                showPassDialog("抱歉！此订单已过期");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void showPassDialog(String s) {
        OneButtonDialog dialog = new OneButtonDialog(this, s, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {

					}
				});
				dialog.show();
    }

    private void commitPay() {
        try {
            RequestBean item = new RequestBean();
            JSONObject obj = new JSONObject();
            obj.put("amount", Integer.parseInt(totalPriceValue)*100);
            obj.put("payType", payTypeIdStr);
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_PAY_MONEY);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_PAY_MONEY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void commitAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("inviteUserId", userId);
            obj.put("inviteUsernick",userNick);
            obj.put("skillCode", currPinLeiBean.getId());
            obj.put("orderUsernick", UserPreferencesUtil.getUserNickName(this));
            obj.put("skillName",currPinLeiBean.getName());
            obj.put("startTime", startTime);
            obj.put("holdTime", curTimeNums);
            if (!TextUtils.isEmpty(addressTextString)){
                obj.put("address", addressTextString);
            }
            if (!TextUtils.isEmpty(oralTextStr)){
                obj.put("orderDesc", oralTextStr);
            }
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_COMMIT_ORDER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_COMMIT_ORDER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_COMMIT_ORDER_PAY:
                if ("0000".equals(data.getCode())) {
                    if ("moreidolsAppAlipay2".equals(payTypeIdStr)) {
                        getPayInfo(data.getRecord());
                    } else if ("moreidolsWeixinPay".equals(payTypeIdStr)) {
                        getPayInfo(data.getRecord());
                    }else{
                        intoOrderDetail();
                    }
                } else if ("6012".equals(data.getCode())){
                    if (payType==2||payType==3){
                        ShowMsgDialog.showNoMsg(this,false);
                        ThreadPool.getInstance().excuseThread(new HoldPushTime());
                    }else{
                        showTwoButtonDialog("您账户余额不足，是否前往充值？",data.getRecord());
                    }
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_COMMIT_ORDER:
                if ("0000".equals(data.getCode())) {
                    if ("qiangDan".equals(whereFrom)){
                        intoOrderDetail();
                    }else{
                        TransIfoToDetailBean item=new TransIfoToDetailBean();
                        item.setAddress(addressTextString);
                        item.setCurrPinLeiBean(currPinLeiBean);
                        item.setSiglePrice(singlePriceValue);
                        item.setTime(timerTextStr);
                        item.setTotalPrice(totalPriceValue);
                        item.setUserImage(userIcon);
                        item.setUserNick(userNick);
                        item.setInviteUserId(userId);
                        item.setOralTextStr(oralTextStr);
                        item.setFialDate(CalendarUtils.getTimeLong1(fialDate));
                        Intent intent=new Intent(this,OrderDetailActivity.class);
                        intent.putExtra("bean", item);
                        intent.putExtra("orderId", data.getRecord());
                        startActivity(intent);
                        Intent intent2 = new Intent();
                        setResult(Activity.RESULT_OK, intent2);
                        finish();
                    }
                } else if ("6012".equals(data.getCode())){
                    if (payType==2||payType==3){
                        ShowMsgDialog.showNoMsg(this,false);
                        ThreadPool.getInstance().excuseThread(new HoldPushTime());
                    }else{
                        showTwoButtonDialog("您账户余额不足，是否前往充值？",data.getRecord());
                    }
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;

            case ConstTaskTag.QUERY_PAY_MONEY:
                if ("0000".equals(data.getCode())) {
                    getPayInfo(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class HoldPushTime implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                handler.sendEmptyMessage(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ShowMsgDialog.cancel();
                    if ("qiangDan".equals(whereFrom)){
                        intoOrderDetail();
                    }else{
                        commitAction();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void getPayInfo(String record) {
        try {
            if (!TextUtils.isEmpty(record)) {
                JSONObject object = new JSONObject(record);
                if ("moreidolsAppAlipay2".equals(payTypeIdStr)) {
                    String sdkInfo = StringUtils.getJsonValue(object, "sdkInfo");
                    requestAlipay(sdkInfo);
                } else if ("moreidolsWeixinPay".equals(payTypeIdStr)) {
                    String sdkInfo = StringUtils.getJsonValue(object, "sdkInfo");
                    JSONObject object1 = new JSONObject(sdkInfo);
                    PayReq pr = getWxPayReq(StringUtils.getJsonValue(object1, "appid"), StringUtils.getJsonValue(object1, "partnerid"), StringUtils.getJsonValue(object1, "package"),
                            StringUtils.getJsonValue(object1, "prepayid"), StringUtils.getJsonValue(object1, "noncestr"), StringUtils.getJsonValue(object1, "timestamp"), StringUtils.getJsonValue(object1, "sign"));
                    // 调用微信支付
                    payWx(wxAppId, pr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载方法
     *
     * @param flag
     * @param tips
     */
    @Override
    protected void payResult(Boolean flag, String tips) {
        // TODO Auto-generated method stub
        super.payResult(flag, tips);
        if (flag) {
            Map kv = new HashMap();
            kv.put("统计类型", "充值");
            kv.put("充值金额", totalPriceValue.concat("元"));
            TCAgent.onEvent(this, "钱包充值", "充值位", kv);
            if ("qiangDan".equals(whereFrom)){
                intoOrderDetail();
            }else{
                commitAction();
            }
        } else {
            showDilog1(tips);
        }
    }

    private void showDilog1(String msg) {
        OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

            @Override
            public void confrimListener(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showTwoButtonDialog(String tip, final String record){
        TwoButtonDialog dialog = new TwoButtonDialog(this, tip, "以后再说", "前往充值", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        Intent intent = new Intent(OrderPayActivity.this, PayStoneActivity.class);
                        intent.putExtra("stoneValue", String.valueOf(ConstTaskTag.getDoublePrice(record)));
                        startActivityForResult(intent, 0);
                    }
                });
        dialog.show();
    }

    private void intoOrderDetail(){

        OneButtonDialog dialog = new OneButtonDialog(this, "支付成功！", R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
                        comfirmIntoOrderDetail();
					}
				});
				dialog.show();
    }

    private void comfirmIntoOrderDetail(){
        TransIfoToDetailBean item=new TransIfoToDetailBean();
        item.setAddress(addressTextString);
        item.setCurrPinLeiBean(currPinLeiBean);
        item.setSiglePrice(singlePriceValue);
        item.setTime(timerTextStr);
        item.setTotalPrice(totalPriceValue);
        item.setUserImage(userIcon);
        item.setUserNick(userNick);
        item.setInviteUserId(userId);
        item.setOralTextStr(oralTextStr);
        item.setCurTimeNums(curTimeNums);
        item.setFialDate(fialDate);
        Intent intent=new Intent(this,OrderDetailQiangActivity.class);
        intent.putExtra("bean", item);
        intent.putExtra("orderId", orderId);
        startActivity(intent);
        Intent broadCast=new Intent(XMPPUtils.MSG_TYPE);
        broadCast.putExtra("orderId",orderId);
        sendBroadcast(broadCast);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showPayResult(data);
//        switch (requestCode) {
//            case 0: {
//                if (Activity.RESULT_OK != resultCode || null == data) {
//                    return;
//                }
//                Intent intent = new Intent();
//                intent.putExtra(Constants.COMEBACK, "tixian");
//                String tixianValue = data.getStringExtra("tixianValue");
//                intent.putExtra("moneyValue", tixianValue);
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//                break;
//            }
//            default:
//                showPayResult(data);
//                break;
//        }
    }
}

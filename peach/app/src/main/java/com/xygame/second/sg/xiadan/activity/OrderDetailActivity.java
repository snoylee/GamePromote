package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.TransIfoToDetailBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by tony on 2016/12/23.
 */
public class OrderDetailActivity extends SGBaseActivity implements View.OnClickListener{
    private TextView titleName, singlePriceValue,totalPriceValue,pingleiText,timerText,addressText,userNickText,rightButtonText,statusText;
    private View backButton, areaView,rightButton,twoButtonView,refuseButton,agreeButton;
    private Button commitButton,payButton;
    private TextView oralText;
    private CircularImage headImage;
    private String addressTextString,fialDate,curTimeNums,userIcon,userNick,userId,orderPrice,orderAmount,oralTextStr,orderId,orderStatus,agreeOrRefuseFlag,payStatus,createTime;
    private ImageLoader mImageLoader;
    private  JinPaiBigTypeBean currPinLeiBean;
    private TransIfoToDetailBean transIfoToDetailBean;
    private SGNewsBean sgNewsBean;
    private String fromFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_layout);
        registerBoradcastReceiver();
        initViews();
        addListener();
        initDatas();
    }

    private void initViews() {
        agreeButton=findViewById(R.id.agreeButton);
        refuseButton=findViewById(R.id.refuseButton);
        twoButtonView=findViewById(R.id.twoButtonView);
        rightButton=findViewById(R.id.rightButton);
        commitButton = (Button)findViewById(R.id.commitButton);
        payButton=(Button)findViewById(R.id.payButton);
        areaView = findViewById(R.id.areaView);
        statusText=(TextView)findViewById(R.id.statusText);
        singlePriceValue = (TextView) findViewById(R.id.singlePriceValue);
        oralText = (TextView) findViewById(R.id.oralText);
        timerText = (TextView) findViewById(R.id.timerText);
        totalPriceValue = (TextView) findViewById(R.id.totalPriceValue);
        pingleiText = (TextView) findViewById(R.id.pingleiText);
        addressText = (TextView) findViewById(R.id.addressText);
        headImage = (CircularImage) findViewById(R.id.headImage);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        userNickText=(TextView)findViewById(R.id.userNickText);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
    }

    private void initDatas() {
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("订单详情");
        transIfoToDetailBean=(TransIfoToDetailBean)getIntent().getSerializableExtra("bean");
        orderId=getIntent().getStringExtra("orderId");
        if (transIfoToDetailBean!=null){
            rightButtonText.setVisibility(View.VISIBLE);
            rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
            updateDrictView();
        }else{
            fromFlag=getIntent().getStringExtra("fromFlag");
            sgNewsBean=(SGNewsBean)getIntent().getSerializableExtra("newsBean");
            loadProjectDatas();
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        refuseButton.setOnClickListener(this);
        agreeButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.commitButton:
                agreeConfirmDialog("确定要完成订单吗？");
                break;
            case R.id.rightButton:
                if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(fialDate)), CalendarUtils.getHenGongDateDis(System.currentTimeMillis()))){
                    Intent intent=new Intent(this,CancelReasonActivity.class);
                    intent.putExtra("acceptTitleStr","取消订单");
                    intent.putExtra("orderId", orderId);
                    startActivityForResult(intent, 0);
                }else{
                    Intent intent=new Intent(this,CancelReasonActivity.class);
                    intent.putExtra("acceptTitleStr","申请退款");
                    intent.putExtra("orderId", orderId);
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.refuseButton:
                if ("6".equals(orderStatus)){
                    oprateTwoButtonAct("4");
                }else{
                    if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(fialDate)),CalendarUtils.getHenGongDateDis(System.currentTimeMillis()))){
                        agreeOrRefuseFlag="3";
                        requestOpratOrder();
                    }else{
                        timePassed();
                    }
                }
                break;
            case R.id.agreeButton:
                if ("6".equals(orderStatus)){
                    oprateTwoButtonAct("3");
                } else {
                    if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(fialDate)),CalendarUtils.getHenGongDateDis(System.currentTimeMillis()))){
                        agreeOrRefuseFlag="2";
                        requestOpratOrder();
                    }else{
                        timePassed();
                    }
                }
                break;
            case R.id.payButton:
                doAction();
                break;
        }
    }

    private void doAction() {
        String singlePriceValueStr= ConstTaskTag.getIntPrice(orderPrice);
        String totalPriceValueStr=ConstTaskTag.getIntPrice(orderAmount);
        Intent intent=new Intent(this,OrderPayActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("userNick",userNick);
        intent.putExtra("currPinLeiBean",currPinLeiBean);
        intent.putExtra("startTime", fialDate);
        intent.putExtra("curTimeNums",curTimeNums);
        intent.putExtra("addressTextString",addressTextString);
        intent.putExtra("oralTextStr",oralTextStr);
        intent.putExtra("userIcon",userIcon);
        intent.putExtra("orderId",orderId);
        intent.putExtra("timerText",timerText.getText().toString());
        intent.putExtra("singlePriceValue",singlePriceValueStr);
        intent.putExtra("totalPriceValue",totalPriceValueStr);
        intent.putExtra("payTimeStr",createTime);
        intent.putExtra("fialDate", fialDate);
        intent.putExtra("whereFrom", "qiangDan");
        startActivity(intent);
    }

    public void oprateTwoButtonAct(String flag){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("orderStatus",flag);
            object.put("orderId",orderId);
            item.setData(object);
            item.setServiceURL(ConstTaskTag.QUEST_OPERATE_APPLY);
            ShowMsgDialog.showNoMsg(this,false);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_OPERATE_APPLY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void operateApplyNews(){
        statusText.setText("已取消");
        twoButtonView.setVisibility(View.GONE);
        if (sgNewsBean!=null){
            sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
            NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
        }
    }

    public void timePassed() {
        if (sgNewsBean!=null){
            sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
            NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
        }
        rightButtonText.setVisibility(View.GONE);
        commitButton.setVisibility(View.GONE);
        twoButtonView.setVisibility(View.GONE);
        statusText.setText("已取消");
    }

    private void agreeConfirmDialog(String tip) {
        TwoButtonDialog dialog = new TwoButtonDialog(this, tip, "确定", "取消", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        agreeOrderAction();
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialog.show();
    }

    public void requestOpratOrder() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("orderStatus",agreeOrRefuseFlag);
            object.put("orderId",orderId);
            item.setData(object);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_STATUS);
            ShowMsgDialog.showNoMsg(this, false);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_CANCEL1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void agreeOrderAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_CANCEL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_CANCEL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void loadProjectDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_DETAIL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_DETAIL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ORDER_DETAIL:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ORDER_CANCEL:
                if ("0000".equals(data.getCode())) {
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAN_JIESUAN_ACT);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                    if ("xiaDanManage".equals(fromFlag)){
                        Intent intent2 = new Intent(XMPPUtils.GROUP_NEW_MESSAGE_ACTION);
                        intent2.putExtra("orderId",orderId);
                        intent2.putExtra("isFinish","ok");
                        sendBroadcast(intent2);
                    }
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ORDER_CANCEL1:
                if ("0000".equals(data.getCode())) {
                    twoButtonView.setVisibility(View.GONE);
                    if (sgNewsBean!=null){
                        if ("2".equals(agreeOrRefuseFlag)){
                            agreeSucess();
                        }else if ("3".equals(agreeOrRefuseFlag)){
                            refuseSucess();
                        }
                    }
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_OPERATE_APPLY:
                if ("0000".equals(data.getCode())){
                    operateApplyNews();
                }else{
                    Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void agreeSucess() {
        sgNewsBean.setType(Constants.TARGET_XIADAAN_WAITTING_SERVICE);
        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
        try{
            JSONObject obj=new JSONObject(sgNewsBean.getNoticeSubject());
            String userId=StringUtils.getJsonValue(obj, "userId");
            String userNick=sgNewsBean.getFriendNickName();
            String userIcon=sgNewsBean.getFriendUserIcon();
            ToChatBean toChatBean = new ToChatBean();
            toChatBean.setRecruitLocIndex("");
            toChatBean.setNoticeId("");
            toChatBean.setNoticeSubject(userNick);
            toChatBean.setUserIcon(userIcon);
            toChatBean.setUserId(userId);
            toChatBean.setUsernick(userNick);
            toChatBean.setRecruitId("");
            XMPPUtils.sendMessgaeForXiaDan(this, UserPreferencesUtil.getUserNickName(this).concat(" 接受了您的订单"), toChatBean);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void refuseSucess() {
        statusText.setText("已取消");
        sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object=new JSONObject(record);
                String order=StringUtils.getJsonValue(object,"order");
                String user=StringUtils.getJsonValue(object,"user");

                JSONObject orderJson=new JSONObject(order);
                String skillCode=StringUtils.getJsonValue(orderJson, "skillCode");
                payStatus=StringUtils.getJsonValue(orderJson,"payStatus");
                List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                for (JinPaiBigTypeBean it:typeDatas){
                    if (skillCode.equals(it.getId())){
                        currPinLeiBean=it;
                        break;
                    }
                }
                curTimeNums=StringUtils.getJsonValue(orderJson,"holdTime");
                addressTextString=StringUtils.getJsonValue(orderJson,"address");
                oralTextStr=StringUtils.getJsonValue(orderJson,"orderDesc");
                fialDate=StringUtils.getJsonValue(orderJson,"startTime");
                orderStatus=StringUtils.getJsonValue(orderJson,"orderStatus");
                orderPrice=StringUtils.getJsonValue(orderJson,"orderPrice");
                orderAmount=StringUtils.getJsonValue(orderJson,"orderAmount");
                createTime=StringUtils.getJsonValue(orderJson,"createTime");

                JSONObject userJson=new JSONObject(user);
                userIcon=StringUtils.getJsonValue(userJson,"userIcon");
                userNick=StringUtils.getJsonValue(userJson,"usernick");
                userId=StringUtils.getJsonValue(userJson,"userId");
                updateViewFromService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateViewFromService(){
        userNickText.setText(userNick);
        mImageLoader.loadImage(userIcon, headImage, true);
        pingleiText.setText(currPinLeiBean.getName());
        timerText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(fialDate)).concat(" ").concat(curTimeNums).concat("次"));
        singlePriceValue.setText(ConstTaskTag.getIntPrice(orderPrice).concat("*").concat(curTimeNums));
        totalPriceValue.setText(ConstTaskTag.getIntPrice(orderAmount));
        if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
            areaView.setVisibility(View.GONE);
        }else {
            areaView.setVisibility(View.VISIBLE);
            addressText.setText(addressTextString);
        }
        if (!TextUtils.isEmpty(oralTextStr)){
            oralText.setVisibility(View.VISIBLE);
            oralText.setText(oralTextStr);
        }else{
            oralText.setVisibility(View.GONE);
        }
        switch (Constants.orderStatus(fialDate, orderStatus,payStatus)){
            case 0:
                if (UserPreferencesUtil.getUserId(this).equals(userId)){
                    statusText.setText("未支付");
                    commitButton.setVisibility(View.GONE);
                    twoButtonView.setVisibility(View.GONE);
                    payButton.setVisibility(View.GONE);
                }else{
                    rightButtonText.setVisibility(View.GONE);
                    commitButton.setVisibility(View.GONE);
                    twoButtonView.setVisibility(View.GONE);
                    rightButtonText.setText("取消订单");
                    payButton.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (UserPreferencesUtil.getUserId(this).equals(userId)){
                    statusText.setText("待确认");
                    commitButton.setVisibility(View.GONE);
                    twoButtonView.setVisibility(View.VISIBLE);
                }else{
                    rightButtonText.setVisibility(View.VISIBLE);
                    commitButton.setVisibility(View.GONE);
                    twoButtonView.setVisibility(View.GONE);
                    rightButtonText.setText("取消订单");
                }

                break;
            case 2:
                if (!userId.equals(UserPreferencesUtil.getUserId(this))){
                    statusText.setText("待服务");
                    twoButtonView.setVisibility(View.GONE);
                    rightButtonText.setVisibility(View.VISIBLE);
                    rightButtonText.setText("取消订单");
                    rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
                    commitButton.setVisibility(View.VISIBLE);
                    commitButton.setOnClickListener(this);
                    commitButton.setText("完成订单");
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAAN_WAITTING_SERVICE);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                }else {
                    statusText.setText("待服务");
                    commitButton.setVisibility(View.GONE);
                    twoButtonView.setVisibility(View.GONE);
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAAN_WAITTING_SERVICE);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                }
                break;
            case 3:
                if (!userId.equals(UserPreferencesUtil.getUserId(this))){
                    statusText.setText("服务中");
                    if ("7".equals(orderStatus)){
                        twoButtonView.setVisibility(View.GONE);
                        rightButtonText.setVisibility(View.GONE);
                        commitButton.setVisibility(View.GONE);
                    }else {
                        twoButtonView.setVisibility(View.GONE);
                        rightButtonText.setVisibility(View.VISIBLE);
                        rightButtonText.setText("取消订单");
                        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
                        commitButton.setVisibility(View.VISIBLE);
                        commitButton.setOnClickListener(this);
                        commitButton.setText("完成订单");
                    }
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAAN_SERVING);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                }else{
                    statusText.setText("服务中");
                    if ("6".equals(orderStatus)){
                        if (!userId.equals(UserPreferencesUtil.getUserId(this))){
                            twoButtonView.setVisibility(View.GONE);
                        }else{
                            twoButtonView.setVisibility(View.VISIBLE);
                        }
                        rightButtonText.setVisibility(View.GONE);
                        commitButton.setVisibility(View.GONE);
                    }else{
                        twoButtonView.setVisibility(View.GONE);
                        rightButtonText.setVisibility(View.GONE);
                        commitButton.setVisibility(View.GONE);
                    }
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAAN_SERVING);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                }
                break;
            case 4:
                statusText.setText("已完成");
                commitButton.setVisibility(View.GONE);
                twoButtonView.setVisibility(View.GONE);
                if (sgNewsBean!=null){
                    sgNewsBean.setType(Constants.TARGET_XIADAN_JIESUAN_ACT);
                    NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                }
                break;
            case 5:
                statusText.setText("已取消");
                if ("6".equals(orderStatus)){
                    if (!userId.equals(UserPreferencesUtil.getUserId(this))){
                        twoButtonView.setVisibility(View.GONE);
                    }else{
                        twoButtonView.setVisibility(View.VISIBLE);
                    }
                    rightButtonText.setVisibility(View.GONE);
                    commitButton.setVisibility(View.GONE);
                }else{
                    twoButtonView.setVisibility(View.GONE);
                    rightButtonText.setVisibility(View.GONE);
                    commitButton.setVisibility(View.GONE);
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                }
                break;
        }
        if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(fialDate)), CalendarUtils.getHenGongDateDis(System.currentTimeMillis()))){
            rightButtonText.setText("取消订单");
        }else{
            if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(System.currentTimeMillis()), CalendarUtils.getTimeByTimeAndHour(Long.parseLong(fialDate),24))){
                rightButtonText.setVisibility(View.GONE);
                commitButton.setVisibility(View.GONE);
                twoButtonView.setVisibility(View.GONE);
            }else{
                rightButtonText.setText("申请退款");
            }
        }
    }

    private void updateDrictView() {
        rightButtonText.setVisibility(View.VISIBLE);
        commitButton.setVisibility(View.GONE);
        twoButtonView.setVisibility(View.GONE);
        rightButtonText.setText("取消订单");
        currPinLeiBean=transIfoToDetailBean.getCurrPinLeiBean();
        fialDate=transIfoToDetailBean.getFialDate();
        userIcon=transIfoToDetailBean.getUserImage();
        userNick=transIfoToDetailBean.getUserNick();
        userId=transIfoToDetailBean.getInviteUserId();
        pingleiText.setText(currPinLeiBean.getName());
        userNickText.setText(userNick);
        mImageLoader.loadImage(userIcon, headImage, true);
        singlePriceValue.setText(transIfoToDetailBean.getSiglePrice());
        totalPriceValue.setText(transIfoToDetailBean.getTotalPrice());
        statusText.setText("待确认");
        if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
            areaView.setVisibility(View.GONE);
        }else {
            areaView.setVisibility(View.VISIBLE);
            addressText.setText(transIfoToDetailBean.getAddress());
        }
        timerText.setText(transIfoToDetailBean.getTime());
        oralTextStr=transIfoToDetailBean.getOralTextStr();
        if (!TextUtils.isEmpty(oralTextStr)){
            oralText.setVisibility(View.VISIBLE);
            oralText.setText(oralTextStr);
        }else{
            oralText.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG,false);
                if (result) {
                    if (sgNewsBean!=null){
                        sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
                        NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
                    }
                    if ("xiaDanManage".equals(fromFlag)){
                        Intent intent2 = new Intent(XMPPUtils.GROUP_NEW_MESSAGE_ACTION);
                        intent2.putExtra("orderId",orderId);
                        intent2.putExtra("isFinish","no");
                        sendBroadcast(intent2);
                    }
                    finish();
                }else{
                    String flagStr=data.getStringExtra("flagStr");
                    String strTip=data.getStringExtra("strTip");
                    showComfirmDailog(flagStr,strTip);
                }
                break;
            }
            default:
                break;
        }
    }

    private void showComfirmDailog(final String flagStr, String strTip){
        TwoButtonDialog dialog = new TwoButtonDialog(this, strTip, R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
                        if ("9003".equals(flagStr)){
                            Intent intent=new Intent(OrderDetailActivity.this,CancelReasonActivity.class);
                            intent.putExtra("acceptTitleStr","申请退款");
                            intent.putExtra("orderId", orderId);
                            startActivityForResult(intent, 0);
                        }else if ("9004".equals(flagStr)){
                            Intent intent=new Intent(OrderDetailActivity.this,CancelReasonActivity.class);
                            intent.putExtra("acceptTitleStr","取消订单");
                            intent.putExtra("orderId", orderId);
                            startActivityForResult(intent, 0);
                        }
					}

					@Override
					public void cancelListener() {
						finish();
					}
				});
				dialog.show();
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.MSG_TYPE);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.MSG_TYPE.equals(intent.getAction())) {
                finish();
            }
        }
    };
}

package com.xygame.second.sg.xiadan.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.TransIfoToDetailBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsGodEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.util.List;

public class QiangDanDetailActivity extends SGBaseActivity implements View.OnClickListener{
    private TextView titleName, singlePriceValue,totalPriceValue,pingleiText,timerText,addressText,userNickText;
    private View backButton, areaView,bottomView;
    private TextView oralText;
    private CircularImage headImage;
    private String jsonStr,orderId,userId,createTime,userImage,userName;
    private ImageLoader mImageLoader;
    private  JinPaiBigTypeBean currPinLeiBean;
    private SGNewsBean newsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiang_dan_detail_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        bottomView=findViewById(R.id.bottomView);
        areaView = findViewById(R.id.areaView);
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
    }

    private void initDatas() {
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("订单详情");
        userImage=getIntent().getStringExtra("userImage");
        userName=getIntent().getStringExtra("userName");
        newsBean=(SGNewsBean)getIntent().getSerializableExtra("newsBean");
        jsonStr=getIntent().getStringExtra("jsonStr");

        initJsonDatas();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        bottomView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.bottomView:
                oprateTwoButtonAct();
                break;
        }
    }

    private void initJsonDatas() {
        try {
            if ("0".equals(newsBean.getInout())){
                bottomView.setVisibility(View.GONE);
            }else{
                bottomView.setVisibility(View.VISIBLE);
            }
            JSONObject jsonObject=new JSONObject(jsonStr);
            orderId= StringUtils.getJsonValue(jsonObject,"orderId");
            userId=StringUtils.getJsonValue(jsonObject,"userId");
            String skillCode=StringUtils.getJsonValue(jsonObject,"skillCode");
            String startTime=StringUtils.getJsonValue(jsonObject,"startTime");
            String holdTime=StringUtils.getJsonValue(jsonObject,"holdTime");
            createTime=StringUtils.getJsonValue(jsonObject,"createTime");
            String priceId=StringUtils.getJsonValue(jsonObject,"priceId");
            String priceRate=StringUtils.getJsonValue(jsonObject,"priceRate");
            String orderDesc=StringUtils.getJsonValue(jsonObject,"orderDesc");
            String address=StringUtils.getJsonValue(jsonObject,"address");

            List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
            if (fuFeiDatas!=null){
                for (PriceBean t:fuFeiDatas){
                    if (t.getId().equals(priceId)){
                        int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(priceRate))/100;
                        singlePriceValue.setText(String.valueOf(value).concat("*").concat(holdTime));
                        totalPriceValue.setText(String.valueOf(value*Integer.parseInt(holdTime)));
                        break;
                    }
                }
            }

            List<JinPaiBigTypeBean> typeDatas= CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
            for (JinPaiBigTypeBean it:typeDatas) {
                if (it.getId().equals(skillCode)) {
                    currPinLeiBean=it;
                    break;
                }
            }
            pingleiText.setText(currPinLeiBean.getName());
            userNickText.setText(userName);
            mImageLoader.loadImage(userImage, headImage, true);

            if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
                areaView.setVisibility(View.GONE);
            }else {
                areaView.setVisibility(View.VISIBLE);
                addressText.setText(address);
            }
            timerText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat("  ").concat(holdTime).concat("小时"));
            if (!TextUtils.isEmpty(orderDesc)){
                oralText.setVisibility(View.VISIBLE);
                oralText.setText(orderDesc);
            }else{
                oralText.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void oprateTwoButtonAct(){
        try {
            RequestBean item = new RequestBean();
            item.setIsPublic(false);
            JSONObject object = new JSONObject();
            object.put("orderId",orderId);
            object.put("createTime",createTime);
            item.setData(object);
            item.setServiceURL(ConstTaskTag.QUEST_QIANG_FOR_GOD);
            ShowMsgDialog.showNoMsg(this, false);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_QIANG_FOR_GOD1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_QIANG_FOR_GOD1:
                if ("0000".equals(data.getCode())){
                    bottomView.setVisibility(View.GONE);
                    timePassed();
                }else if ("9500".equals(data.getCode())||"9503".equals(data.getCode())){
                    showOneButtonDialog(data.getMsg());
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void timePassed() {
        newsBean.setInout("0");
        NewsGodEngine.updateOptionDynamic(this, UserPreferencesUtil.getUserId(this), newsBean);
    }


    private void showOneButtonDialog(String msg) {
        OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        timePassed();
                    }
                });
        dialog.show();
    }
}

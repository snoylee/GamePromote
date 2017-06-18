package com.xygame.second.sg.xiadan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.TransIfoToDetailBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

public class OrderDetailQiangActivity extends SGBaseActivity implements View.OnClickListener{
    private TextView titleName, singlePriceValue,totalPriceValue,pingleiText,timerText,addressText,userNickText;
    private View backButton, areaView,bottomView;
    private TextView oralText;
    private CircularImage headImage;
    private String orderId;
    private ImageLoader mImageLoader;
    private  JinPaiBigTypeBean currPinLeiBean;
    private TransIfoToDetailBean transIfoToDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail_qiang_layout);
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
        transIfoToDetailBean=(TransIfoToDetailBean)getIntent().getSerializableExtra("bean");
        orderId=getIntent().getStringExtra("orderId");
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("订单详情");
        if (transIfoToDetailBean!=null){//本地数据
            initJsonDatas();
        }else{//网络数据

        }
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
//                oprateTwoButtonAct();
                break;
        }
    }

    private void initJsonDatas() {
        try {
            bottomView.setVisibility(View.GONE);
            currPinLeiBean=transIfoToDetailBean.getCurrPinLeiBean();
            String startTime=transIfoToDetailBean.getTime();
            String orderDesc=transIfoToDetailBean.getOralTextStr();
            String address=transIfoToDetailBean.getAddress();
            singlePriceValue.setText(transIfoToDetailBean.getSiglePrice().concat("*").concat(transIfoToDetailBean.getCurTimeNums()));
            totalPriceValue.setText(transIfoToDetailBean.getTotalPrice());

            pingleiText.setText(currPinLeiBean.getName());
            userNickText.setText(transIfoToDetailBean.getUserNick());
            mImageLoader.loadImage(transIfoToDetailBean.getUserImage(), headImage, true);

            if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
                areaView.setVisibility(View.GONE);
            }else {
                areaView.setVisibility(View.VISIBLE);
                addressText.setText(address);
            }
            timerText.setText(startTime);
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
//            object.put("createTime",createTime);
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
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

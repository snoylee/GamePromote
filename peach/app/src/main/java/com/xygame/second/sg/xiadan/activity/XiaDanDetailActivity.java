package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.second.sg.xiadan.bean.TransIfoToDetailBean;
import com.xygame.second.sg.xiadan.bean.TransferTypeBean;
import com.xygame.second.sg.xiadan.bean.XiaDanPriceListBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.EditorTextContentActivity;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PingLeiPopView;
import com.xygame.sg.define.view.XiaDanTimeView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/23.
 */
public class XiaDanDetailActivity extends SGBaseActivity implements View.OnClickListener{
    private TextView titleName, singlePriceValue,totalPriceValue,pingleiText,timerText,addressText,userNickText;
    private View backButton, pingleiView,timerView,areaView,commitButton;
    private EditText oralText;
    private CircularImage headImage;
    private List<JinPaiBigTypeBean> myTypes;
    private String addressTextString,fialDate,curTimeNums,userIcon,userNick,userId,priceRate,oralTextStr,singlePriceValueStr,totalPriceValueStr;
    private ImageLoader mImageLoader;
    private  JinPaiBigTypeBean currPinLeiBean;
    private List<XiaDanPriceListBean> sikllPriceBean;
    private PriceBean currPriceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiadan_detail_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        commitButton = findViewById(R.id.commitButton);
        areaView = findViewById(R.id.areaView);
        timerView = findViewById(R.id.timerView);
        pingleiView = findViewById(R.id.pingleiView);
        singlePriceValue = (TextView) findViewById(R.id.singlePriceValue);
        oralText = (EditText) findViewById(R.id.oralText);
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
        userIcon=getIntent().getStringExtra("userIcon");
        userNick=getIntent().getStringExtra("userNick");
        userId=getIntent().getStringExtra("userId");
        currPinLeiBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("bean");
        if (currPinLeiBean!=null){
            pingleiText.setText(currPinLeiBean.getName());
        }
        titleName.setText("下单");
        userNickText.setText(userNick);
        mImageLoader.loadImage(userIcon, headImage, true);
        loadProjectDatas();
    }

    private void addListener() {
        pingleiView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        areaView.setOnClickListener(this);
        timerView.setOnClickListener(this);
        commitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.commitButton:
                if (isGro()){
                    oralTextStr=oralText.getText().toString().trim();
                    Intent intent=new Intent(this,OrderPayActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("userNick",userNick);
                    intent.putExtra("currPinLeiBean",currPinLeiBean);
                    intent.putExtra("startTime", CalendarUtils.getTimeLong1(fialDate));
                    intent.putExtra("curTimeNums",curTimeNums);
                    intent.putExtra("addressTextString",addressTextString);
                    intent.putExtra("oralTextStr",oralTextStr);
                    intent.putExtra("userIcon",userIcon);
                    intent.putExtra("timerText",timerText.getText().toString());
                    intent.putExtra("singlePriceValue",singlePriceValueStr);
                    intent.putExtra("totalPriceValue",totalPriceValueStr);
                    intent.putExtra("fialDate",fialDate);
                    startActivityForResult(intent, 3);
                }
                break;
            case R.id.pingleiView:
                TransferTypeBean transferTypeBean=new TransferTypeBean();
                transferTypeBean.setMyTypes(myTypes);
                Intent intent3 = new Intent(this, PingLeiPopView.class);
                intent3.putExtra("bean",transferTypeBean);
                startActivityForResult(intent3, 0);
                break;
            case R.id.areaView:
                Intent intent = new Intent(this, EditorTextContentActivity.class);
                intent.putExtra(Constants.EDITOR_TEXT_TITLE, "填写地址");
                intent.putExtra("oral", addressTextString);
                intent.putExtra("hint", "职业类型不超过20个字");
                intent.putExtra(Constants.TEXT_EDITOR_NUM, 20);
                startActivityForResult(intent, 1);
                break;
            case R.id.timerView:
                Intent intent2 = new Intent(this, XiaDanTimeView.class);
                startActivityForResult(intent2, 2);
                break;
        }
    }

    private boolean isGro(){
        if (currPinLeiBean==null){
            Toast.makeText(this,"请选择品类",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(fialDate)){
            Toast.makeText(this,"请选择时间",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (areaView.getVisibility()==View.VISIBLE){
            if (TextUtils.isEmpty(addressTextString)){
                Toast.makeText(this,"请填写地点",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void commitAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("inviteUserId", userId);
            obj.put("inviteUsernick",userNick);
            obj.put("skillCode", currPinLeiBean.getId());
            obj.put("startTime", CalendarUtils.getTimeLong1(fialDate));
            obj.put("holdTime", curTimeNums);
            obj.put("address", addressTextString);
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

    public void loadProjectDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_GOD_PRICE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GOD_PRICE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_COMMIT_ORDER:
                if ("0000".equals(data.getCode())) {
                    TransIfoToDetailBean item=new TransIfoToDetailBean();
                    item.setAddress(addressTextString);
                    item.setCurrPinLeiBean(currPinLeiBean);
                    item.setSiglePrice(singlePriceValue.getText().toString());
                    item.setTime(timerText.getText().toString());
                    item.setTotalPrice(totalPriceValue.getText().toString());
                    item.setUserImage(userIcon);
                    item.setUserNick(userNick);
                    item.setInviteUserId(userId);
                    item.setOralTextStr(oralTextStr);
                    item.setFialDate(CalendarUtils.getTimeLong1(fialDate));
                    Intent intent=new Intent(this,OrderDetailActivity.class);
                    intent.putExtra("bean",item);
                    intent.putExtra("orderId",data.getRecord());
                    startActivity(intent);
                    finish();
                } else if ("6012".equals(data.getCode())){
                    showTwoButtonDialog("您账户余额不足，是否前往充值？",data.getRecord());
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GOD_PRICE:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showTwoButtonDialog(String tip, final String record){
        TwoButtonDialog dialog = new TwoButtonDialog(this, tip, "以后再说", "前往充值", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        Intent intent = new Intent(XiaDanDetailActivity.this, PayStoneActivity.class);
                        intent.putExtra("stoneValue", String.valueOf(ConstTaskTag.getDoublePrice(record)));
                        startActivityForResult(intent, 0);
                    }
                });
        dialog.show();
    }

    private void parseDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                sikllPriceBean=new ArrayList<>();
                myTypes=new ArrayList<>();
                List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                JSONArray jsonArray = new JSONArray(record);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject orderJson=jsonArray.getJSONObject(i);
                    XiaDanPriceListBean item=new XiaDanPriceListBean();
                    item.setPriceId(StringUtils.getJsonValue(orderJson, "priceId"));
                    item.setSkillCode(StringUtils.getJsonValue(orderJson, "skillCode"));
                    item.setPriceRate(StringUtils.getJsonValue(orderJson, "priceRate"));
                    sikllPriceBean.add(item);
                    for (JinPaiBigTypeBean it:typeDatas){
                        if (item.getSkillCode().equals(it.getId())){
                            myTypes.add(it);
                            break;
                        }
                    }
                }
                if (currPinLeiBean!=null){
                    freshSkillViews();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void freshSkillViews(){

        if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
            areaView.setVisibility(View.GONE);
        }else {
            areaView.setVisibility(View.VISIBLE);
        }

        if (sikllPriceBean!=null){
            for (XiaDanPriceListBean item:sikllPriceBean){
                if (currPinLeiBean.getId().equals(item.getSkillCode())){
                    String priceId=item.getPriceId();
                    priceRate=item.getPriceRate();
                    List<PriceBean> fuFeiDatas = CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                    if (fuFeiDatas != null) {
                        for (PriceBean t : fuFeiDatas) {
                            if (t.getId().equals(priceId)) {
                                currPriceBean=t;
                                int value = (Integer.parseInt(t.getPrice()) * Integer.parseInt(priceRate)) / 100;
                                singlePriceValueStr=String.valueOf(value);
                                if (!TextUtils.isEmpty(curTimeNums)){
                                    singlePriceValue.setText(singlePriceValueStr.concat("*").concat(curTimeNums));
                                    totalPriceValueStr=String.valueOf(value*Integer.parseInt(curTimeNums));
                                    totalPriceValue.setText(totalPriceValueStr);
                                }else{
                                    totalPriceValueStr=singlePriceValueStr;
                                    singlePriceValue.setText(singlePriceValueStr);
                                    totalPriceValue.setText(singlePriceValueStr);
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                JinPaiBigTypeBean currBean=(JinPaiBigTypeBean)data.getSerializableExtra("bean");
                if (currBean!=null){
                    currPinLeiBean=currBean;
                    pingleiText.setText(currPinLeiBean.getName());
                    timerText.setText("");
                    addressText.setText("");
                    singlePriceValue.setText("");
                    totalPriceValue.setText("");
                    addressTextString=null;
                    fialDate=null;
                    curTimeNums=null;
                    priceRate=null;
                    currPriceBean=null;
                    freshSkillViews();
                }
                break;
            case 1:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                addressTextString = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                addressText.setText(addressTextString);
                break;
            case 2:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                fialDate=data.getStringExtra("fialDate");
                curTimeNums=data.getStringExtra("curTimeNums");
                timerText.setText(fialDate.concat(" ").concat(curTimeNums).concat("小时"));
                updatePriceView();
                break;
            case 3:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                finish();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePriceView(){
        if (currPriceBean!=null){
            int value = (Integer.parseInt(currPriceBean.getPrice()) * Integer.parseInt(priceRate)) / 100;
            singlePriceValueStr=String.valueOf(value);
            if (!TextUtils.isEmpty(curTimeNums)){
                singlePriceValue.setText(singlePriceValueStr.concat("*").concat(curTimeNums));
                totalPriceValueStr=String.valueOf(value*Integer.parseInt(curTimeNums));
                totalPriceValue.setText(totalPriceValueStr);
            }else{
                totalPriceValueStr=singlePriceValueStr;
                singlePriceValue.setText(totalPriceValueStr);
                totalPriceValue.setText(totalPriceValueStr);
            }
        }
    }
}

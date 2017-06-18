package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.TransferTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.EditorTextContentActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.define.view.GodLalbeChoiceActivity;
import com.xygame.sg.define.view.PingLeiPopView;
import com.xygame.sg.define.view.XiaDanTimeView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by tony on 2016/12/23.
 */
public class QiangXiaDanDetailActivity extends SGBaseActivity implements View.OnClickListener{

    private View backButton,pingleiView,lableTextView,timerView,areaView,commitButton,backgroundColor,backgroundColor1,backgroundColor2,gouView,gouView1,gouView2;
    private TextView titleName,pingleiText,lableText,timerText,addressText,typeName,typeName1,typeName2;
    private EditText oralText;
    private JinPaiBigTypeBean currPinLeiBean;
    private int sexFlag=1;
    private String addressTextString,fialDate,curTimeNums,oralTextStr;
    private GodLableBean godLableBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qiang_xiadan_detail_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton=findViewById(R.id.backButton);
        lableText=(TextView)findViewById(R.id.lableText);
        lableTextView=findViewById(R.id.lableTextView);
        commitButton = findViewById(R.id.commitButton);
        areaView = findViewById(R.id.areaView);
        timerView = findViewById(R.id.timerView);
        pingleiView = findViewById(R.id.pingleiView);
        backgroundColor =findViewById(R.id.backgroundColor);
        oralText = (EditText) findViewById(R.id.oralText);
        timerText = (TextView) findViewById(R.id.timerText);
        backgroundColor1 = findViewById(R.id.backgroundColor1);
        pingleiText = (TextView) findViewById(R.id.pingleiText);
        addressText = (TextView) findViewById(R.id.addressText);
        backgroundColor2 =  findViewById(R.id.backgroundColor2);
        gouView = findViewById(R.id.gouView);
        gouView1 = findViewById(R.id.gouView1);
        gouView2 = findViewById(R.id.gouView2);
        typeName = (TextView) findViewById(R.id.typeName);
        typeName1=(TextView)findViewById(R.id.typeName1);
        typeName2=(TextView)findViewById(R.id.typeName2);
        titleName=(TextView)findViewById(R.id.titleName);
    }

    private void initDatas() {
        currPinLeiBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("jinPaiBigTypeBean");
        if (currPinLeiBean!=null){
            pingleiText.setText(currPinLeiBean.getName());
            if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
                areaView.setVisibility(View.GONE);
            }else {
                areaView.setVisibility(View.VISIBLE);
            }
        }
        titleName.setText("我要下单");
        godLableBean=new GodLableBean();
        godLableBean.setTitleName("全部");
        godLableBean.setTitleId("-1");
        lableText.setText(godLableBean.getTitleName());
    }

    private void addListener() {
        lableTextView.setOnClickListener(this);
        pingleiView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        areaView.setOnClickListener(this);
        timerView.setOnClickListener(this);
        commitButton.setOnClickListener(this);
        backgroundColor.setOnClickListener(this);
        backgroundColor1.setOnClickListener(this);
        backgroundColor2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backgroundColor2:
                sexFlag=3;
                switchSex();
                break;
            case R.id.backgroundColor1:
                sexFlag=2;
                switchSex();
                break;
            case R.id.backgroundColor:
                sexFlag=1;
                switchSex();
                break;
            case R.id.lableTextView:
                Intent intent2=new Intent(this,GodLalbeChoiceActivity.class);
                intent2.putExtra("fromWhere","qiangXiaDanDetail");
                intent2.putExtra("bean",currPinLeiBean);
                startActivityForResult(intent2, 4);
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.commitButton:
                if (isGro()){
                    oralTextStr=oralText.getText().toString().trim();
                    commitAction();
                }
                break;
            case R.id.pingleiView:
                List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                TransferTypeBean transferTypeBean=new TransferTypeBean();
                transferTypeBean.setMyTypes(typeDatas);
                Intent intent3 = new Intent(this, PingLeiPopView.class);
                intent3.putExtra("bean",transferTypeBean);
                startActivityForResult(intent3, 0);
                break;
            case R.id.areaView:
                Intent intent1 = new Intent(this, EditorTextContentActivity.class);
                intent1.putExtra(Constants.EDITOR_TEXT_TITLE, "填写地址");
                intent1.putExtra("oral", addressTextString);
                intent1.putExtra("hint", "职业类型不超过20个字");
                intent1.putExtra(Constants.TEXT_EDITOR_NUM, 20);
                startActivityForResult(intent1, 1);
                break;
            case R.id.timerView:
                Intent intent4 = new Intent(this, XiaDanTimeView.class);
                startActivityForResult(intent4, 2);
                break;
        }
    }

    private void switchSex() {
        switch (sexFlag){
            case 1:
                backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
                typeName.setTextColor(getResources().getColor(R.color.white));
                gouView.setVisibility(View.VISIBLE);

                backgroundColor1.setBackgroundResource(R.drawable.shape_rect_white);
                typeName1.setTextColor(getResources().getColor(R.color.dark_green));
                gouView1.setVisibility(View.GONE);

                backgroundColor2.setBackgroundResource(R.drawable.shape_rect_white);
                typeName2.setTextColor(getResources().getColor(R.color.dark_green));
                gouView2.setVisibility(View.GONE);
                break;
            case 2:
                backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
                typeName.setTextColor(getResources().getColor(R.color.dark_green));
                gouView.setVisibility(View.GONE);

                backgroundColor1.setBackgroundResource(R.drawable.shape_rect_dark_green10);
                typeName1.setTextColor(getResources().getColor(R.color.white));
                gouView1.setVisibility(View.VISIBLE);

                backgroundColor2.setBackgroundResource(R.drawable.shape_rect_white);
                typeName2.setTextColor(getResources().getColor(R.color.dark_green));
                gouView2.setVisibility(View.GONE);
                break;
            case 3:
                backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
                typeName.setTextColor(getResources().getColor(R.color.dark_green));
                gouView.setVisibility(View.GONE);

                backgroundColor1.setBackgroundResource(R.drawable.shape_rect_white);
                typeName1.setTextColor(getResources().getColor(R.color.dark_green));
                gouView1.setVisibility(View.GONE);

                backgroundColor2.setBackgroundResource(R.drawable.shape_rect_dark_green10);
                typeName2.setTextColor(getResources().getColor(R.color.white));
                gouView2.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean isGro(){
        if (currPinLeiBean==null){
            Toast.makeText(this, "请选择品类", Toast.LENGTH_SHORT).show();
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
        try {
            ShowMsgDialog.showNoMsg(this, false);
            RequestBean item = new RequestBean();
            JSONObject obj = new JSONObject();
            obj.put("skillCode", currPinLeiBean.getId());
            if (!"-1".equals(godLableBean.getTitleId())){
                obj.put("titleCode", godLableBean.getTitleId());
            }
            switch (sexFlag){
//                case 1:
//                    obj.put("inviteGender", -1);
//                    break;
                case 2:
                    obj.put("inviteGender", 0);
                    break;
                case 3:
                    obj.put("inviteGender", 1);
                    break;
            }
            obj.put("startTime", CalendarUtils.getTimeLong1(fialDate));
            obj.put("holdTime", curTimeNums);
            if (!TextUtils.isEmpty(addressTextString)){
                obj.put("address", addressTextString);
            }
            if (!TextUtils.isEmpty(oralTextStr)){
                obj.put("orderDesc", oralTextStr);
            }
            List<ProvinceBean> provinceBeanList = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
            String currProvinceName = BaiduPreferencesUtil.getProvice(this);
            for (ProvinceBean provinceBean:provinceBeanList){
                provinceBean.get();
                if (currProvinceName.contains(provinceBean.getProvinceName())){
                    obj.put("locProvince",provinceBean.getProvinceCode());
                    List<CityBean> datas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceBean.getProvinceCode()));
                    for (CityBean cityBean:datas){
                        cityBean.get();
                        String currCityName = BaiduPreferencesUtil.getCity(this);
                        if (currCityName.contains(cityBean.getCityName())){
                            obj.put("locCity",cityBean.getCityCode());
                            break;
                        }
                    }
                    break;
                }
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_IM_ORDER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_IM_ORDER);
        } catch (Exception e1) {
            ShowMsgDialog.cancel();
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_IM_ORDER:
                if ("0000".equals(data.getCode())) {
                    try {
                        String orderExpireTime= "";
                        String payExpireTime="";
                        String orderId=data.getRecord();
                        UserPreferencesUtil.setIsReadGetMoney(this, true);
                        Intent intent=new Intent(this,WaitGodActivity.class);
                        intent.putExtra("whereFrom","xiaDan");
                        intent.putExtra("orderId",orderId);
                        startActivity(intent);
                        TimerCountBean timerCountBean=new TimerCountBean();
                        timerCountBean.setPayExpireTime(payExpireTime);
                        timerCountBean.setOrderExpireTime(orderExpireTime);
                        timerCountBean.setStartTime(String.valueOf(System.currentTimeMillis()));
                        timerCountBean.setUserId(UserPreferencesUtil.getUserId(this));
                        timerCountBean.setGroupId(orderId);
                        timerCountBean.setDuringLength(Constants.QIANGDAN_TIMER);
                        TimerCountBean sqlBean=TimerEngine.quaryTimerBeansByDuringLength(this,UserPreferencesUtil.getUserId(this),Constants.QIANGDAN_TIMER);
                        if (sqlBean!=null){
                            TimerEngine.updateTimerBeanInfo(this,timerCountBean);
                        }else{
                            TimerEngine.inserTimerBean(this,timerCountBean);
                        }
                        Intent intent1 = new Intent();
                        intent1.putExtra("toFirstPage","toFirstPage");
                        setResult(Activity.RESULT_OK, intent1);
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
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
                    if ("900".equals(currPinLeiBean.getId())||"1100".equals(currPinLeiBean.getId())||"1200".equals(currPinLeiBean.getId())){
                        areaView.setVisibility(View.GONE);
                    }else {
                        areaView.setVisibility(View.VISIBLE);
                    }
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
                break;
            case 3:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                finish();
                break;
            case 4:
                String flagStr=data.getStringExtra("flagStr");
                if ("have".equals(flagStr)){
                    godLableBean=(GodLableBean)data.getSerializableExtra(Constants.COMEBACK);
                    lableText.setText(godLableBean.getTitleName());
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

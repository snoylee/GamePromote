package com.xygame.second.sg.jinpai.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.JinPaiLowerPriceBean;
import com.xygame.second.sg.jinpai.adapter.JinPaiBigTypeAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.TransActionBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JinPaiPlushActivity extends SGBaseActivity implements View.OnClickListener{
    private TextView titleName;
    private View backButton,nextStep;
    private GridView photoList;
    private JinPaiBigTypeAdapter adapter;
    private String wichFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_plush_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        nextStep=findViewById(R.id.nextStep);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        photoList = (GridView) findViewById(R.id.photoList);
    }

    private void initDatas() {
        wichFlag=getIntent().getStringExtra("wichFlag");
        titleName.setText("发布活动");
        List<JinPaiBigTypeBean> vector=null;
        if ("fufeiYue".equals(wichFlag)){
            vector =CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        }else{
            vector =CacheService.getInstance().getCacheActDatas(ConstTaskTag.CACHE_ACT_TYPE);
        }
        List<JinPaiLowerPriceBean> lowerPriceBeans=CacheService.getInstance().getCacheJbLowerPrice(ConstTaskTag.CACHE_JP_LOWER_PRICE);
        if (lowerPriceBeans==null){
            loadBiggestPriceOfType();
        }
        if (vector!=null){
            if (vector.size()>0){
                adapter=new JinPaiBigTypeAdapter(this,vector);
                photoList.setAdapter(adapter);
            }else{
                requestActType();
            }
        }else {
            requestActType();
        }
    }

    private void addListener() {
        nextStep.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.nextStep:
                if (adapter!=null){
                    if (adapter.getCount()>0){
                        JinPaiBigTypeBean item=adapter.getSelectedItem();
                        if (item!=null){
                            String record=item.getSubStr();
                            if (!TextUtils.isEmpty(record)&&!"[null]".equals(record)&&!"[]".equals(record)){
                                Intent intent=new Intent(this,JinPaiPlushTwoActivity.class);
                                intent.putExtra("bean",item);
                                intent.putExtra("wichFlag", wichFlag);
                                startActivityForResult(intent, 0);
                            }else{
                                TransActionBean transActionBean=new TransActionBean();
                                transActionBean.setBigTypeBean(item);
                                Intent intent=new Intent();
                                if ("jingpaiYue".equals(wichFlag)){
                                    intent.setClass(this,JinPaiPlushThreeActivity.class);
                                }else if ("fufeiYue".equals(wichFlag)){
                                    intent.setClass(this,JinPaiPlushThreeFuFeiActivity.class);
                                }
                                intent.putExtra("bean",transActionBean);
                                startActivityForResult(intent, 0);
                            }
                        }else{
                            Toast.makeText(this,"请选择活动类型",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;
        }
    }

    public void requestActType(){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object=new JSONObject();
            object.put("ownerType",1);
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this,true);
        item.setServiceURL(ConstTaskTag.QUEST_ACT_TYPE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_TYPE);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_ACT_TYPE:
                if ("0000".equals(data.getCode())){
                    parseDatas(data.getRecord());
                }else{
                    Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                List<JinPaiBigTypeBean> jinPaiDatas = new ArrayList<>(),mianFeiDatas = new ArrayList<>(),fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiBigTypeBean item = new JinPaiBigTypeBean();
                    item.setIsSelect(false);
                    item.setName(StringUtils.getJsonValue(object, "typeName"));
                    item.setId(StringUtils.getJsonValue(object, "typeId"));
                    item.setSubStr(StringUtils.getJsonValue(object, "subTypes"));
                    item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
                    String ownerType =StringUtils.getJsonValue(object,"ownerType");
                    switch (Integer.parseInt(ownerType)){//ownerType 1：竞拍活动，2：免费活动，3：定额活动
                        case 1:
                            jinPaiDatas.add(item);
                            break;
                        case 2:
                            mianFeiDatas.add(item);
                            break;
                        case 3:
                            fuFeiDatas.add(item);
                            break;
                    }
                }
                CacheService.getInstance().cacheActDatas(ConstTaskTag.CACHE_ACT_TYPE, jinPaiDatas);
                CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, fuFeiDatas);
                if ("fufeiYue".equals(wichFlag)){
                    adapter=new JinPaiBigTypeAdapter(this,fuFeiDatas);
                }else{
                    adapter=new JinPaiBigTypeAdapter(this,jinPaiDatas);
                }
                photoList.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.COMEBACK,false);
                if (result) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK,true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            }
            default:
                break;
        }
    }
}

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
import com.xygame.second.sg.jinpai.adapter.JinPaiSmallTypeAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.second.sg.jinpai.bean.TransActionBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JinPaiPlushTwoActivity extends SGBaseActivity implements View.OnClickListener{
    private TextView titleName;
    private View backButton,nextStep,proStep;
    private GridView photoList;
    private JinPaiSmallTypeAdapter adapter;
    private JinPaiBigTypeBean bigTypeBean;
    private String wichFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_plush_two_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        proStep=findViewById(R.id.proStep);
        nextStep=findViewById(R.id.nextStep);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        photoList = (GridView) findViewById(R.id.photoList);
    }

    private void initDatas() {
        wichFlag=getIntent().getStringExtra("wichFlag");
        bigTypeBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("bean");
        titleName.setText("发布活动");
        initSubViews(bigTypeBean.getSubStr());
        List<JinPaiLowerPriceBean> lowerPriceBeans= CacheService.getInstance().getCacheJbLowerPrice(ConstTaskTag.CACHE_JP_LOWER_PRICE);
        if (lowerPriceBeans==null){
            loadBiggestPriceOfType();
        }
    }

    private void initSubViews(String record) {
        if (!TextUtils.isEmpty(record)){
            try {
                List<JinPaiSmallTypeBean> value=new ArrayList<>();
                JSONArray array=new JSONArray(record);
                for (int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    JinPaiSmallTypeBean item =new JinPaiSmallTypeBean();
                    item.setTypeName(StringUtils.getJsonValue(object, "typeName"));
                    item.setTypeId(StringUtils.getJsonValue(object, "typeId"));
                    item.setIsSelected(false);
                    value.add(item);
                }
                adapter=new JinPaiSmallTypeAdapter(this,value);
                photoList.setAdapter(adapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void addListener() {
        proStep.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proStep:
                finish();
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.nextStep:
                List<JinPaiSmallTypeBean> vector=adapter.getSmallTypes();
                if (vector.size()>0){
                    TransActionBean transActionBean=new TransActionBean();
                    transActionBean.setBigTypeBean(bigTypeBean);
                    transActionBean.setSmallTypeBeans(vector);
                    Intent intent=new Intent();
                    if ("jingpaiYue".equals(wichFlag)){
                        intent.setClass(this,JinPaiPlushThreeActivity.class);
                    }else if ("fufeiYue".equals(wichFlag)){
                        intent.setClass(this,JinPaiPlushThreeFuFeiActivity.class);
                    }
                    intent.putExtra("bean",transActionBean);
                    startActivityForResult(intent, 0);
                }else{
                    Toast.makeText(this, "至少选择一个活动种类", Toast.LENGTH_SHORT).show();
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

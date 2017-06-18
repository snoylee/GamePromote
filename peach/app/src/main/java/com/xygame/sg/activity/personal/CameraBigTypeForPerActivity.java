package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.personal.adapter.CategoryForPerAdapter;
import com.xygame.sg.activity.personal.adapter.SelectFirstCategoryListAdapter;
import com.xygame.sg.activity.personal.bean.TempTypeBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraBigTypeForPerActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton,rightButton;
    private TextView titleName,rightButtonText;
    private ListView category_lv;
    private CategoryForPerAdapter adapter;
    private List<ShootTypeBean> dateList,deleList,newList;
    private Map<String, Boolean> beanMap;
    private TempTypeBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_first_category);
        initViews();
        addListener();
        initData();
    }
    private void initData() {
        bean=(TempTypeBean)getIntent().getSerializableExtra("bean");
        dateList=bean.getDateList();
        deleList=new ArrayList<>();
        newList=new ArrayList<>();
        beanMap=new HashMap<>();
        List<ShootTypeBean> dataList=SGApplication.getInstance().getTypeList();
        if (dataList!=null){
            for (ShootTypeBean item:dataList){
                beanMap.put(String.valueOf(item.getTypeId()),false);
            }
        }

        for (ShootTypeBean it:dateList){
            beanMap.put(String.valueOf(it.getTypeId()),true);
        }
        adapter = new CategoryForPerAdapter(this,beanMap);
        category_lv.setAdapter(adapter);
    }

    private void initViews() {
        rightButton=findViewById(R.id.rightButton);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        titleName.setText("拍摄类型");//(getText(R.string.title_activity_select_first_category));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("完成");
        category_lv = (ListView) findViewById(R.id.category_lv);
    }

    private void addListener() {
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.rightButton:
                List<ShootTypeBean> data= adapter.getSelectDatas();
                getDatas(data);
                if (deleList.size()>0||newList.size()>0){
                    commitType();
                }
                break;
        }
    }

    private void getDatas(List<ShootTypeBean> data) {
        deleList.clear();
        newList.clear();
        if (data.size()>0){
            if (dateList.size()==0){
                newList.addAll(data);
            }else{
                getNewsDatas(data);
                getDeleDatas(data);
            }
        }else{
            deleList.addAll(dateList);
        }
    }

    private void getNewsDatas(List<ShootTypeBean> data){
        for (ShootTypeBean selectItem:data){
            boolean flag=true;
            for (ShootTypeBean yuanItem:dateList){
                if (selectItem.getTypeId()==yuanItem.getTypeId()){
                    flag=false;
                    break;
                }
            }
            if (flag){
                newList.add(selectItem);
            }
        }
    }

    private void getDeleDatas(List<ShootTypeBean> data){
        for (ShootTypeBean yuanItem:dateList){
            boolean flag=true;
            for (ShootTypeBean selectItem:data){
                if (selectItem.getTypeId()==yuanItem.getTypeId()){
                    flag=false;
                    break;
                }
            }
            if (flag){
                deleList.add(yuanItem);
            }
        }
    }

    public void commitType(){
        RequestBean item = new RequestBean();
        try {
            JSONObject obj=new JSONObject();
            obj.put("userId", UserPreferencesUtil.getUserId(this));
            if (newList.size()>0){
                JSONArray newArray=new JSONArray();
                for (ShootTypeBean yuanItem:newList){
                    JSONObject newObj=new JSONObject();
                    newObj.put("typeId",yuanItem.getTypeId());
                    newArray.put(newObj);
                }
                obj.put("appendTypes",newArray);
            }
            if (deleList.size()>0){
                JSONArray newArray=new JSONArray();
                for (ShootTypeBean yuanItem:deleList){
                    JSONObject newObj=new JSONObject();
                    newObj.put("typeId",yuanItem.getTypeId());
                    newArray.put(newObj);
                }
                obj.put("delTypes",newArray);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_MODEL_TYPE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_MODEL_TYPE_COD);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        if ("0000".equals(data.getCode())){
            finishActivity();
        }else{
            Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
    }

    public void finishActivity(){
        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Constants.ACTION_EDITOR_USER_INFO);
        sendBroadcast(intent);
        finish();
    }
}

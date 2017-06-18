package com.xygame.second.sg.comm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.adapter.AreaAdapter;
import com.xygame.second.sg.comm.adapter.PublicCityAdater;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.ArrayList;
import java.util.List;

public class PublicCityActivity extends SGBaseActivity implements OnClickListener{

    private View backButton, rightButton;
    private TextView titleName, rightButtonText;
    private ListView parentList;
    private GridView childList;
    private PublicCityAdater cityAdater;
    private AreaAdapter areaAdapter;
    private ProvinceBean areaBean;
    private List<CityBean> cityDatas = new ArrayList<>();
    private List<CityBean> areaBeans;
    private List<CityBean> tempCityDatasTJ, tempCityDatasCQ, tempCityDatasXG, tempCityDatasAM, tempCityDatasTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_choice_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        rightButton = findViewById(R.id.rightButton);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        parentList = (ListView) findViewById(R.id.areaList);
        childList = (GridView) findViewById(R.id.photoList);
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        parentList.setOnItemClickListener(new CityItemListener());
        rightButton.setOnClickListener(this);
        childList.setOnItemClickListener(new AreaItemListener());
    }

    private void initDatas() {
        titleName.setText("选择地区");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("确定");
        areaBean = (ProvinceBean) getIntent().getSerializableExtra("bean");
        cityAdater = new PublicCityAdater(this, null);
        areaAdapter = new AreaAdapter(this, null);
        parentList.setAdapter(cityAdater);
        childList.setAdapter(areaAdapter);
        ShowMsgDialog.showNoMsg(this, false);
        ThreadPool.getInstance().excuseThread(new InitDatas());
    }

    private void initCityDatas() {
        List<CityBean> tempCityDatas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(areaBean.getProvinceCode()));
        if (areaBean.getProvinceName().contains("北京")) {
            cityDatas.add(tempCityDatas.get(0));
        } else if (areaBean.getProvinceName().contains("天津")) {
            tempCityDatasTJ = tempCityDatas;
            cityDatas.add(tempCityDatas.get(0));
        } else if (areaBean.getProvinceName().contains("重庆")) {
            tempCityDatasCQ = tempCityDatas;
            cityDatas.add(tempCityDatas.get(0));
        } else if (areaBean.getProvinceName().contains("上海")) {
            cityDatas.add(tempCityDatas.get(0));
        } else if (areaBean.getProvinceName().contains("香港")) {
            tempCityDatasXG = tempCityDatas;
            cityDatas.add(tempCityDatas.get(0));
        } else if (areaBean.getProvinceName().contains("澳门")) {
            tempCityDatasAM = tempCityDatas;
            cityDatas.add(tempCityDatas.get(0));
        } else if (areaBean.getProvinceName().contains("台湾")) {
            tempCityDatasTW = tempCityDatas;
            cityDatas.add(tempCityDatas.get(0));
        } else {
            cityDatas.addAll(tempCityDatas);
        }
    }

    private void initAreaDatas(CityBean cityBean) {
        if (areaBean.getProvinceName().contains("北京")) {
            cityBean.get();
            areaBeans = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else if (areaBean.getProvinceName().contains("天津")) {
            List<CityBean> tempCityDatas = new ArrayList<>();
            tempCityDatas.addAll(tempCityDatasTJ);
            tempCityDatas.remove(0);
            areaBeans = tempCityDatas;
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else if (areaBean.getProvinceName().contains("重庆")) {
            List<CityBean> tempCityDatas = new ArrayList<>();
            tempCityDatas.addAll(tempCityDatasCQ);
            tempCityDatas.remove(0);
            areaBeans = tempCityDatas;
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else if (areaBean.getProvinceName().contains("上海")) {
            cityBean.get();
            areaBeans = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else if (areaBean.getProvinceName().contains("香港")) {
            List<CityBean> tempCityDatas = new ArrayList<>();
            tempCityDatas.addAll(tempCityDatasXG);
            tempCityDatas.remove(0);
            areaBeans = tempCityDatas;
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else if (areaBean.getProvinceName().contains("澳门")) {
            List<CityBean> tempCityDatas = new ArrayList<>();
            tempCityDatas.addAll(tempCityDatasAM);
            tempCityDatas.remove(0);
            areaBeans = tempCityDatas;
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else if (areaBean.getProvinceName().contains("台湾")) {
            List<CityBean> tempCityDatas = new ArrayList<>();
            tempCityDatas.addAll(tempCityDatasTW);
            tempCityDatas.remove(0);
            areaBeans = tempCityDatas;
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);
        } else {
            cityBean.get();
            areaBeans = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
            CityBean temple = new CityBean();
            temple.setName("全部");
            temple.setIsSelect(false);
            temple.setId(-1);
            areaBeans.add(0, temple);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.rightButton:
                ShowMsgDialog.showNoMsg(this, false);
                ThreadPool.getInstance().excuseThread(new JugmentUse());
                break;
        }
    }

    private class InitDatas implements Runnable {

        @Override
        public void run() {
            initCityDatas();
            mHandler.sendEmptyMessage(2);
        }
    }

    private class JugmentUse implements Runnable {
        @Override
        public void run() {
            List<CityBean> areaBeans = areaAdapter.getAreaDatas();
            boolean temBoolean1 = false;
            for (CityBean it2 : areaBeans) {
                if (it2.isSelect()) {
                    temBoolean1 = true;
                    break;
                }
            }
            if (temBoolean1) {
                CityBean item = cityAdater.getCurrCity();
                item.get();
                item.setAreaBeans(areaBeans);
                areaBean.setCityBean(item);
                mHandler.sendEmptyMessage(0);
            } else {
                mHandler.sendEmptyMessage(1);
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ShowMsgDialog.cancel();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, areaBean);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                case 1:
                    ShowMsgDialog.cancel();
                    Toast.makeText(PublicCityActivity.this, "请选择地区", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    ShowMsgDialog.cancel();
                    cityAdater.updateDatas(cityDatas);
                    CityBean item = cityDatas.get(0);
                    item.get();
                    cityAdater.setCurTrue(item);
                    initAreaDatas(item);
                    areaAdapter.updateAreaDatas(areaBeans);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class CityItemListener implements OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CityBean item = cityAdater.getItem(position);
            item.get();
            cityAdater.setCurTrue(item);
            initAreaDatas(item);
            areaAdapter.updateAreaDatas(areaBeans);
        }
    }

    private class AreaItemListener implements OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CityBean areaBean=areaAdapter.getItem(position);
            areaAdapter.updateSelect(areaBean);
        }
    }
}
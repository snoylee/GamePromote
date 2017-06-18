package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.adapter.comm.CityAdapter;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PlushNoticeCityAreaActivity extends SGBaseActivity implements
        OnClickListener, OnItemClickListener {
    private TextView titleName;
    private View backButton;
    private ListView countryList;
    private CityAdapter adapter;
    private List<CityBean> datas;
    private PlushNoticeAreaBean areaBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_city_area_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        countryList = (ListView) findViewById(R.id.countryList);
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        countryList.setOnItemClickListener(this);
    }

    private void initDatas() {
        areaBean=(PlushNoticeAreaBean) getIntent().getSerializableExtra("trasBean");
        titleName.setText(areaBean.getCityName());
        datas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(areaBean.getCityId()));
        adapter = new CityAdapter(this, datas);
        countryList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }

    /**
     * 重载方法
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        CityBean cyBean = adapter.getItem(arg2);
        List<CityBean> temp=new ArrayList<>();
        temp.add(cyBean);
        areaBean.setCityAreas(temp);
       finishActivity();
    }

    public void finishActivity() {
    	Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, areaBean);
		setResult(Activity.RESULT_OK, intent);
		finish();
    }

}

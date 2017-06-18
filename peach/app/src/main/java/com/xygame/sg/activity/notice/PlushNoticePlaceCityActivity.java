package com.xygame.sg.activity.notice;

import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.adapter.comm.CityAdapter;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.action.CenterRepo;
import base.frame.VisitUnit;

public class PlushNoticePlaceCityActivity extends SGBaseActivity implements
        OnClickListener, OnItemClickListener {
    private TextView titleName;
    private View backButton;
    private ListView countryList;
    private CityAdapter adapter;
    private List<CityBean> datas;
    private String provinceName, cityName, provinceCode, cityCode;
    private ProvinceBean prBean;
    private PlushNoticeAreaBean areaBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_editor_city_layout);
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
        // TODO Auto-generated method stub
        prBean = (ProvinceBean) getIntent().getSerializableExtra("bean");
        provinceCode = prBean.getProvinceCode();
        provinceName = prBean.getProvinceName();
        titleName.setText(provinceName);
        areaBean=(PlushNoticeAreaBean) getIntent().getSerializableExtra("trasBean");
        datas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(prBean.getProvinceCode()));
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
        cityCode = cyBean.getCityCode();
        cityName = cyBean.getCityName();
        areaBean.setCityId(cityCode);
        areaBean.setCityName(cityName);
       finishActivity();
    }

    public String getProvinceName() {
        return provinceName;
    }


    public String getCityName() {
        return cityName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void finishActivity() {

//        Intent intent = new Intent(this, PlushNoticeCityAreaActivity.class);
//        intent.putExtra("trasBean", areaBean);
//        startActivityForResult(intent, 0);
    	Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, areaBean);
		setResult(Activity.RESULT_OK, intent);
		finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                areaBean= (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, areaBean);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            }
            default:
                break;
        }
    }
}

package com.xygame.sg.activity.personal;

import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
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

public class EditorCityActivity extends SGBaseActivity implements
        OnClickListener, OnItemClickListener {
    private TextView titleName;
    private View backButton;
    private ListView countryList;
    private CityAdapter adapter;
    private List<CityBean> datas;
    private String provinceName, cityName, provinceCode, cityCode;
    private ProvinceBean prBean;
    public static boolean submitdisabled;

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
        transferLocationService();
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

        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
        CenterRepo.getInsatnce().getRepo().put("city", cityCode);
        CenterRepo.getInsatnce().getRepo().put("province_city", prBean.getProvinceName() + " " + cityName);
        sendBroadcast(intent);
        submitdisabled = false;
        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
		setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void transferLocationService() {
        VisitUnit visit = new VisitUnit();
        if (!submitdisabled) {
            new Action("#.personal.EditorUserCity(${editUser})", this, null, visit).run();
        }else{
            finishActivity();
        }
    }

}

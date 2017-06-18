package com.xygame.sg.activity.personal;

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

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.adapter.comm.ProvinceAdapter;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;

import java.util.List;

import base.action.Action;
import base.frame.VisitUnit;

public class EditorProvinceActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

    public static boolean submitdisabled;

    private View backButton, locationView, allTopView;
    private TextView titleName, locationCountryText;
    private ListView countryList;
    private ProvinceAdapter adapter;
    private List<ProvinceBean> datas;
    private ProvinceBean item;
    private boolean isStrictCity = false;
    private String provinceName, cityName, provinceCode, cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_editor_provice_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        backButton = findViewById(R.id.backButton);
        locationView = findViewById(R.id.locationView);
        allTopView = findViewById(R.id.allTopView);
        titleName = (TextView) findViewById(R.id.titleName);
        locationCountryText = (TextView) findViewById(R.id.locationCountryText);
        countryList = (ListView) findViewById(R.id.countryList);
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        locationView.setOnClickListener(this);
        countryList.setOnItemClickListener(this);
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        setLocationPosion();
        titleName.setText(getResources().getString(R.string.sg_editor_location_title));

        datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
        boolean noLimitFlag = getIntent().getBooleanExtra("noLimitFlag", false);
        if (noLimitFlag) {
            datas.remove(0);
        }
        adapter = new ProvinceAdapter(this, datas);
        countryList.setAdapter(adapter);
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    private void setLocationPosion() {
        // TODO Auto-generated method stub
        provinceName = BaiduPreferencesUtil.getProvice(this);

        if (provinceName != null) {
            for (String str : Constants.CITY_STRICT) {
                if (provinceName.contains(str)) {
                    isStrictCity = true;
                    cityName = BaiduPreferencesUtil.getCity(this);
                }
            }
            if (isStrictCity) {

                allTopView.setVisibility(View.VISIBLE);
                locationCountryText.setText(provinceName);
            } else {
                cityName = BaiduPreferencesUtil.getCity(this);
                allTopView.setVisibility(View.VISIBLE);
                locationCountryText.setText(provinceName.concat(" ").concat(cityName));
            }

        } else {
            allTopView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;

            case R.id.locationView:
                getCodeAction();
                transferService();
                break;

        }
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    private void getCodeAction() {
        // TODO Auto-generated method stub
        try {
            for (ProvinceBean it : datas) {
                it.get();
                if (provinceName.contains(it.getProvinceName())) {
                    provinceCode = it.getProvinceCode();
                    if (isStrictCity) {
                        List<CityBean> cityDatas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceCode));
                        for (CityBean cIt : cityDatas) {
                            cIt.get();
                            for (String str : Constants.CITY_STRICT) {
                                if (cIt.getCityName().contains(str)) {
                                    cityCode = cIt.getCityCode();
                                    if (submitdisabled) {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        item = adapter.getItem(arg2);

        provinceName = item.getProvinceName();
        provinceCode = item.getProvinceCode();
        if (provinceName.contains("不限")) {


            finish();
            return;
        }
        transferLocationService();
    }

    public ProvinceBean getProvinceBean() {
        return item;
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
        sendBroadcast(intent);
        submitdisabled = false;
        finish();
    }

    VisitUnit visit = new VisitUnit();

    private void transferLocationService() {
        boolean isStrictCity = false;
        for (String str : Constants.CITY_STRICT) {
            if (provinceName.contains(str)) {
                isStrictCity = true;
            }
        }
        if (isStrictCity) {
            List<CityBean> cityDatas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceCode));
            for (CityBean cIt : cityDatas) {
                cIt.get();
                for (String str : Constants.CITY_STRICT) {
                    if (cIt.getCityName().contains(str)) {
                        cityCode = cIt.getCityCode();
                    }
                }
            }
        }
        if (isStrictCity) {
            if (!submitdisabled) {
                transferService();
            } else {

                finishActivity();
            }
        } else if (item.getParentId() == 0) {
            Intent intent = new Intent(this, EditorCityActivity.class);
            intent.putExtra("bean", getProvinceBean());
            startActivityForResult(intent, 0);
        }

    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    private void transferService() {
        // TODO Auto-generated method stub
        if (!submitdisabled) {
            new Action("#.personal.EditorUserProvince(${editUser})", this, null, visit).run();
        } else {
            finishActivity();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                String result = data.getStringExtra(Constants.COMEBACK);
                if (Constants.COMEBACK.equals(result)) {
                    finish();
                }
                break;
            }
            default:
                break;
        }
    }

}
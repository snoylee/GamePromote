package com.xygame.sg.activity.personal;

import java.util.List;
import java.util.Map;

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
import base.data.net.http.JsonUtil;
import base.frame.VisitUnit;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.CountryAdapter;
import com.xygame.sg.activity.commen.bean.CountryBean;
import com.xygame.sg.task.personal.EditorUserCountry;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.Country;

public class EditorCountryActivity extends SGBaseActivity implements
        OnClickListener, OnItemClickListener {

    private View backButton, locationView, allTopView;
    private TextView titleName, locationCountryText;
    private ListView countryList;
    private CountryAdapter adapter;
    private List<Map<String, String>> datas;
    private String countryName;
    public static boolean submit;

    private CountryBean countryBean = new CountryBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_editor_country_layout);
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
        String countryName = BaiduPreferencesUtil.getCoutryName(this);
        if (countryName != null) {
            allTopView.setVisibility(View.VISIBLE);
            locationCountryText.setText(countryName);
        } else {
            allTopView.setVisibility(View.GONE);
        }
        titleName.setText(getResources().getString(
                R.string.sg_comm_editor_country_title));
        datas = (List<Map<String, String>>) new JsonUtil().from(Country.txt);


        countryName = getIntent().getStringExtra("data");
        if (getIntent().hasExtra("isSubmit")){
            submit = getIntent().getBooleanExtra("isSubmit",true);
        } else {
            submit = false;
        }
        adapter = new CountryAdapter(this, datas, countryName);
        countryList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (submit) {
            adapter.list.add(0, "不限");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;

            case R.id.locationView:
                countryName = locationCountryText.getText().toString();
//                if (submit) {
//                    String code = adapter.ctries.get(countryName);
//                    if (code != null) {
//                        CenterRepo.getInsatnce().getRepo().put("country", code);
//                        CenterRepo.getInsatnce().getRepo().put("country_name", countryName);
//                    }
//                    finishActivity();
//                    return;
//                }
                countryBean.setcCode(adapter.ctries.get(countryName));
                countryBean.setcName(countryName);
                transferLocationService();
                break;

        }
    }

    public void finishActivity() {
        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
        intent.putExtra("flagStr",true);
        sendBroadcast(intent);

        submit = false;
        finish();
    }

    private void transferLocationService() {
        VisitUnit visit = new VisitUnit();
        if (!submit) {
//            new Action("#.personal.EditorUserCountry(${editUser})", this, null, visit).run();
            new Action(EditorUserCountry.class,"${editUser}", this, null, visit).run();
        } else {
            submit = false;
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK, countryBean);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public String getCountryName() {
        return adapter.ctries.get(countryName);
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
        countryName = adapter.list.get(arg2);
        if (countryName.equals("不限")) {
            CenterRepo.getInsatnce().getRepo().put("country", "");
            CenterRepo.getInsatnce().getRepo().put("country_name", "");
            countryBean.setcCode("");
            countryBean.setcName("");
        } else {
            CenterRepo.getInsatnce().getRepo().put("country", adapter.ctries.get(countryName));
            CenterRepo.getInsatnce().getRepo().put("country_name", countryName);
            countryBean.setcCode(adapter.ctries.get(countryName));
            countryBean.setcName(countryName);
        }
        transferLocationService();
    }
}

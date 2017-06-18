package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.adapter.SelectCountryAdapter;
import com.xygame.sg.activity.commen.bean.CountryBean;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectCountryActivity extends SGBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View backButton;
    private TextView titleName;
    private ListView countryList;
    private SelectCountryAdapter adapter;
    private List<CountryBean> datas = new ArrayList<CountryBean>();
    private CountryBean countryBean = new CountryBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        initViews();
        initDatas();
        addListener();
    }
    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getText(R.string.title_activity_select_country));

        countryList = (ListView) findViewById(R.id.countryList);


    }
    private void initDatas() {
        CountryBean chinaBean = new CountryBean();
        chinaBean.setcCode("CN");
        chinaBean.setcName("中国");
        CountryBean otherBean = new CountryBean();
        otherBean.setcCode("Z0");
        otherBean.setcName("外籍");
        datas.add(chinaBean);
        datas.add(otherBean);
        if (datas != null) {
            adapter = new SelectCountryAdapter(this, datas);
            countryList.setAdapter(adapter);
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        countryList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        countryBean = adapter.getItem(arg2);
        Intent intent = new Intent();
        intent.putExtra(Constants.COMEBACK, countryBean);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

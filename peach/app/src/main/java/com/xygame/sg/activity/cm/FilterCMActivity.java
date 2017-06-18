package com.xygame.sg.activity.cm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.cm.bean.AllCMReqBean;
import com.xygame.sg.activity.cm.bean.CMCondBean;
import com.xygame.sg.activity.commen.SelectCountryActivity;
import com.xygame.sg.activity.commen.bean.CountryBean;
import com.xygame.sg.activity.notice.PlushNoticePlaceProvinceActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FilterCMActivity extends SGBaseActivity implements View.OnClickListener {

    private View backButton;
    private TextView titleName;

    private ScrollView root_sv;
    private RadioGroup sex_rg;
    private RadioButton sex_all_rb;
    private RadioButton male_rb;
    private RadioButton female_rb;
    private TextView country_tv;
    private TextView city_tv;

    private RoundTextView reset_bt;
    private Button filter_bt;//

    private PlushNoticeAreaBean areaBean;
    private CountryBean countryBean;

    private static final int COUNTRY_REQ = 2;
    private static final int CITY_REQ = 3;


    private AllCMReqBean allCMReqBean;

    private List<CountryBean> countryDatas = new ArrayList<CountryBean>();

    public static int selectCon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_cm);
        allCMReqBean = (AllCMReqBean) getIntent().getSerializableExtra("requestBean");
        initViews();
        initDatas();
        addListener();
    }


    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);

        titleName.setText(getText(R.string.title_activity_filter_model));


        root_sv = (ScrollView) findViewById(R.id.root_sv);
        sex_rg = (RadioGroup) findViewById(R.id.sex_rg);
        sex_all_rb = (RadioButton) findViewById(R.id.sex_all_rb);
        male_rb = (RadioButton) findViewById(R.id.male_rb);
        female_rb = (RadioButton) findViewById(R.id.female_rb);
        country_tv = (TextView) findViewById(R.id.country_tv);
        city_tv = (TextView) findViewById(R.id.city_tv);


        reset_bt = (RoundTextView) findViewById(R.id.reset_bt);
        filter_bt = (Button) findViewById(R.id.filter_bt);
    }

    private void initDatas() {
        countryBean = new CountryBean();
        areaBean = new PlushNoticeAreaBean();

        CMCondBean cond = allCMReqBean.getCond();
        Integer gender = cond.getGender();
        if (gender == null) {
            sex_all_rb.setChecked(true);
            male_rb.setChecked(false);
            female_rb.setChecked(false);

        } else if (gender == Integer.parseInt(Constants.SEX_MAN)) {
            sex_all_rb.setChecked(false);
            male_rb.setChecked(true);
            female_rb.setChecked(false);

        } else if (gender == Integer.parseInt(Constants.SEX_WOMAN)) {
            sex_all_rb.setChecked(false);
            male_rb.setChecked(false);
            female_rb.setChecked(true);
        }

        //国家
        CountryBean chinaBean = new CountryBean();
        chinaBean.setcCode("CN");
        chinaBean.setcName("中国");
        CountryBean otherBean = new CountryBean();
        otherBean.setcCode("Z0");
        otherBean.setcName("外籍");
        countryDatas.add(chinaBean);
        countryDatas.add(otherBean);
        String country = cond.getCountry();
        if (!StringUtils.isEmpty(country)) {
            for (CountryBean it : countryDatas) {
                if (it.getcCode().equals(country)) {
                    String countryName = it.getcName();
                    countryBean.setcName(countryName);
                    countryBean.setcCode(country);
                    country_tv.setText(countryName);
                    break;
                }
            }
        }
        //拍摄地
        Integer cityId = cond.getCity();

        if (cityId != null) {
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(cityId);
            String address = it.getName();
            city_tv.setText(address);
            areaBean.setCityId(cityId + "");
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        country_tv.setOnClickListener(this);
        city_tv.setOnClickListener(this);
        reset_bt.setOnClickListener(this);
        filter_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.reset_bt:
                reset();
                break;
            case R.id.country_tv:
                Intent countryIntent = new Intent(this, SelectCountryActivity.class);
                startActivityForResult(countryIntent, COUNTRY_REQ);
                break;
            case R.id.city_tv:
                Intent cityIntent = new Intent(this, PlushNoticePlaceProvinceActivity.class);
                cityIntent.putExtra("bean", areaBean);
                startActivityForResult(cityIntent, CITY_REQ);
                break;
            case R.id.filter_bt:
                toFilter();
                break;
        }
    }

    private void toFilter() {
        Intent mIntent = new Intent();
        //性别
        switch (sex_rg.getCheckedRadioButtonId()) {
            case R.id.sex_all_rb:
                allCMReqBean.getCond().setGender(null);
                break;
            case R.id.male_rb:
                allCMReqBean.getCond().setGender(1);
                break;
            case R.id.female_rb:
                allCMReqBean.getCond().setGender(0);
                break;
        }
        //国籍
        if (!StringUtils.isEmpty(country_tv.getText().toString())) {
            allCMReqBean.getCond().setCountry(countryBean.getcCode());
        }
        //拍摄地
        if (!StringUtils.isEmpty(city_tv.getText().toString())) {
            if (!StringUtils.isEmpty(areaBean.getCityId())) {
                allCMReqBean.getCond().setCity(Integer.parseInt(areaBean.getCityId()));
            } else {
                allCMReqBean.getCond().setCity(Integer.parseInt(areaBean.getProvinceId()));
            }
        }
        selectCon = getSelectCon();
        mIntent.putExtra("requestBean", allCMReqBean);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private void reset() {
        selectCon = 0;
        //性别
        sex_rg.check(R.id.sex_all_rb);
        allCMReqBean.getCond().setGender(null);
        //国籍
        country_tv.setText("");
        countryBean = new CountryBean();
        allCMReqBean.getCond().setCountry(null);
        //拍摄地
        city_tv.setText("");
        areaBean = new PlushNoticeAreaBean();
        allCMReqBean.getCond().setCity(null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case COUNTRY_REQ:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                countryBean = (CountryBean) data.getSerializableExtra(Constants.COMEBACK);
                if (!StringUtils.isEmpty(countryBean.getcName())) {
                    country_tv.setText(countryBean.getcName());
                } else {
                    country_tv.setText("");
                }
                break;
            case CITY_REQ:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                areaBean = (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
                if (areaBean.getProvinceName() != null) {
                    if (areaBean.getCityName() != null) {
                        if (areaBean.getProvinceName().equals(areaBean.getCityName())) {
                            city_tv.setText(areaBean.getProvinceName());
                        } else {
                            city_tv.setText(areaBean.getProvinceName().concat(" ").concat(areaBean.getCityName()));
                        }
                    } else {
                        city_tv.setText(areaBean.getProvinceName());
                    }
                }
                break;

            default:
                break;
        }
    }

    private int getSelectCon() {
        int selectCon = 0;
        //性别
        if (allCMReqBean.getCond().getGender() != null) {
            selectCon++;
        }
        //国籍
        if (!StringUtils.isEmpty(allCMReqBean.getCond().getCountry())) {
            selectCon++;
        }
        //拍摄地
        if (allCMReqBean.getCond().getCity() != null) {
            selectCon++;
        }
        return selectCon;
    }

}

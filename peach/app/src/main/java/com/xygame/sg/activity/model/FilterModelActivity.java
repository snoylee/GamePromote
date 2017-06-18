package com.xygame.sg.activity.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.SelectCountryActivity;
import com.xygame.sg.activity.commen.bean.CountryBean;
import com.xygame.sg.activity.model.bean.AllModelReqBean;
import com.xygame.sg.activity.notice.PlushNoticePlaceProvinceActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.personal.SelectJobTypeActivity;
import com.xygame.sg.activity.personal.adapter.StyleAdapter;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.activity.personal.bean.ModelStyleVo;
import com.xygame.sg.activity.personal.bean.StyleBean;
import com.xygame.sg.bean.comm.ModelStyleBean;
import com.xygame.sg.bean.comm.TransStyleBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.IRangeSeekBar;
import com.xygame.sg.utils.NoScrollGridView;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.frame.VisitUnit;

public class FilterModelActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private TextView rightButtonText;

    private ScrollView root_sv;
    private RadioGroup sex_rg;
    private RadioButton sex_all_rb;
    private RadioButton male_rb;
    private RadioButton female_rb;
    private TextView country_tv;
    private TextView city_tv;
    private TextView occup_type_tv;
    private LinearLayout male_style_ll;
    private LinearLayout female_style_ll;
//    private View male_female_divider;
    private NoScrollGridView male_style_gv;
    private NoScrollGridView female_style_gv;

    private IRangeSeekBar age_rsb;
    private IRangeSeekBar height_rsb;
    private IRangeSeekBar weight_rsb;
    private LinearLayout cup_ll;
    private IRangeSeekBar cup_rsb;
    private IRangeSeekBar bust_rsb;
    private IRangeSeekBar waist_rsb;
    private IRangeSeekBar hip_rsb;
    private IRangeSeekBar shoecode_rsb;

    private RoundTextView reset_bt;
    private Button filter_bt;//

    private PlushNoticeAreaBean areaBean;
    private CountryBean countryBean;
    private CarrierBean carrierBean;
    private TransStyleBean tsBean;

    private static final int COUNTRY_REQ = 2;
    private static final int CITY_REQ = 3;
    private static final int OCCUP_REQ = 4;
    private static final int MALE_STYLE_REQ = 5;
    private static final int FEMALE_STYLE_REQ = 6;

    private StyleAdapter maleStyleAdapter;
    private StyleAdapter femaleStyleAdapter;


    VisitUnit visitUnit = new VisitUnit(this);

    private AllModelReqBean allModelReqBean;
    List<StyleBean> maleStyleList = new ArrayList<StyleBean>();
    List<StyleBean> femaleStyleList = new ArrayList<StyleBean>();
    private List<CountryBean> countryDatas = new ArrayList<CountryBean>();

    public static int selectCon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_filter_model, null));
        allModelReqBean = (AllModelReqBean) getIntent().getSerializableExtra("requestBean");
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        titleName.setText(getText(R.string.title_activity_filter_model));
//        rightButtonText.setVisibility(View.VISIBLE);
//        rightButtonText.setText(getText(R.string.reset_condition));

        root_sv = (ScrollView) findViewById(R.id.root_sv);
        sex_rg = (RadioGroup) findViewById(R.id.sex_rg);
        sex_all_rb = (RadioButton) findViewById(R.id.sex_all_rb);
        male_rb = (RadioButton) findViewById(R.id.male_rb);
        female_rb = (RadioButton) findViewById(R.id.female_rb);
        country_tv = (TextView) findViewById(R.id.country_tv);
        city_tv = (TextView) findViewById(R.id.city_tv);
        occup_type_tv = (TextView) findViewById(R.id.occup_type_tv);
        female_style_ll = (LinearLayout) findViewById(R.id.female_style_ll);
        male_style_ll = (LinearLayout) findViewById(R.id.male_style_ll);
//        male_female_divider = findViewById(R.id.male_female_divider);
        male_style_gv = (NoScrollGridView) findViewById(R.id.male_style_gv);
        female_style_gv = (NoScrollGridView) findViewById(R.id.female_style_gv);
        age_rsb = (IRangeSeekBar) findViewById(R.id.age_rsb);
        height_rsb = (IRangeSeekBar) findViewById(R.id.height_rsb);
        weight_rsb = (IRangeSeekBar) findViewById(R.id.weight_rsb);
        cup_ll = (LinearLayout) findViewById(R.id.cup_ll);
        cup_rsb = (IRangeSeekBar) findViewById(R.id.cup_rsb);
        bust_rsb = (IRangeSeekBar) findViewById(R.id.bust_rsb);
        waist_rsb = (IRangeSeekBar) findViewById(R.id.waist_rsb);
        hip_rsb = (IRangeSeekBar) findViewById(R.id.hip_rsb);
        shoecode_rsb = (IRangeSeekBar) findViewById(R.id.shoecode_rsb);

        reset_bt = (RoundTextView) findViewById(R.id.reset_bt);
        filter_bt = (Button) findViewById(R.id.filter_bt);
    }

    private void initDatas() {
        countryBean = new CountryBean();
        areaBean = new PlushNoticeAreaBean();
        carrierBean = new CarrierBean();
        tsBean = new TransStyleBean();
        List<ModelStyleBean> typeList = SGApplication.getModelTypeList();
        List<StyleBean> selectDatas = allModelReqBean.getSelectDatas();

        if (selectDatas != null) {
            for (StyleBean styleBean : selectDatas) {
                for (ModelStyleBean modelStyleBean : typeList) {
                    if (styleBean.getStyleId().equals(modelStyleBean.getTypeId() + "")) {
                        if (modelStyleBean.getExclusType() == 1) {
                            maleStyleList.add(styleBean);
                        } else if (modelStyleBean.getExclusType() == 0) {
                            femaleStyleList.add(styleBean);
                        } else if (modelStyleBean.getExclusType() == 2) {
                            maleStyleList.add(styleBean);
                            femaleStyleList.add(styleBean);
                        }
                    }
                }
            }
        }
        maleStyleAdapter = new StyleAdapter(this, maleStyleList);
        femaleStyleAdapter = new StyleAdapter(this, femaleStyleList);
        male_style_gv.setAdapter(maleStyleAdapter);
        female_style_gv.setAdapter(femaleStyleAdapter);
        //性别
        Integer gender = allModelReqBean.getGender();
        if (gender == null) {
            sex_all_rb.setChecked(true);
            male_rb.setChecked(false);
            female_rb.setChecked(false);
            cup_ll.setVisibility(View.GONE);
//            male_female_divider.setVisibility(View.VISIBLE);
            female_style_ll.setVisibility(View.GONE);
            male_style_ll.setVisibility(View.GONE);
        } else if (gender == Integer.parseInt(Constants.SEX_MAN)) {
            sex_all_rb.setChecked(false);
            male_rb.setChecked(true);
            female_rb.setChecked(false);
            cup_ll.setVisibility(View.GONE);
//            male_female_divider.setVisibility(View.GONE);
            female_style_ll.setVisibility(View.GONE);
            male_style_ll.setVisibility(View.GONE);
            male_style_gv.setAdapter(maleStyleAdapter);
        } else if (gender == Integer.parseInt(Constants.SEX_WOMAN)) {
            sex_all_rb.setChecked(false);
            male_rb.setChecked(false);
            female_rb.setChecked(true);
            cup_ll.setVisibility(View.GONE);
//            male_female_divider.setVisibility(View.GONE);
            female_style_ll.setVisibility(View.VISIBLE);
            male_style_ll.setVisibility(View.GONE);
            female_style_gv.setAdapter(femaleStyleAdapter);
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
        String country = allModelReqBean.getCountry();
        if (!StringUtils.isEmpty(country)) {
//            List<Map<String, String>> datas = Constants.COUNTRY_DATA;
//            for (Map<String, String> it : datas) {
//                if (it.get("country_code").equals(country)) {
//                    String countryName = it.get("country_name");
//                    countryBean.setcName(countryName);
//                    carrierBean.setCarrierCode(country);
//                    country_tv.setText(countryName);
//                    break;
//                }
//            }
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
        Integer cityId = allModelReqBean.getCity();

        if (cityId != null) {
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(cityId);
            String address = it.getName();
            city_tv.setText(address);
            areaBean.setCityId(cityId + "");
        }
        //职业
        Integer occTypeId = allModelReqBean.getOccupType();
        if (occTypeId != null) {
            List<CarrierBean> datas = SGApplication.getInstance().getModelCarriers();
            if (!"null".equals(datas) && datas != null) {
                for (CarrierBean it : datas) {
                    if (it.getTypeId().equals(occTypeId + "")) {
                        carrierBean.setCarrierCode(occTypeId + "");
                        carrierBean.setTypeId(occTypeId+"");
                        occup_type_tv.setText(it.getCarrierName());
                    }
                }
            }
        }
        //价格
        Integer ageBegin = allModelReqBean.getAgeBegin();
        Integer ageEnd = allModelReqBean.getAgeEnd();
        if (ageBegin != null) {
            age_rsb.setSelectedMinValue(ageBegin);
        }
        if (ageEnd != null) {
            age_rsb.setSelectedMaxValue(ageEnd);
        }
        Integer heightBegin = allModelReqBean.getHeightBegin();
        Integer heightEnd = allModelReqBean.getHeightEnd();
        if (heightBegin != null) {
            height_rsb.setSelectedMinValue(heightBegin);
        }
        if (heightEnd != null) {
            height_rsb.setSelectedMaxValue(heightEnd);
        }
        Integer weightBegin = allModelReqBean.getWeightBegin();
        Integer weightEnd = allModelReqBean.getWeightEnd();
        if (weightBegin != null) {
            weight_rsb.setSelectedMinValue(weightBegin);
        }
        if (weightEnd != null) {
            weight_rsb.setSelectedMaxValue(weightEnd);
        }
        Integer cupBegin = allModelReqBean.getCupBegin();
        Integer cupEnd = allModelReqBean.getCupEnd();
        if (cupBegin != null) {
            cup_rsb.setSelectedMinValue(cupBegin);
        }
        if (cupEnd != null) {
            cup_rsb.setSelectedMaxValue(cupEnd);
        }
        Integer bustBegin = allModelReqBean.getBustBegin();
        Integer bustEnd = allModelReqBean.getBustEnd();
        if (bustBegin != null) {
            bust_rsb.setSelectedMinValue(bustBegin);
        }
        if (bustEnd != null) {
            bust_rsb.setSelectedMaxValue(bustEnd);
        }
        Integer waistBegin = allModelReqBean.getWaistBegin();
        Integer waistEnd = allModelReqBean.getWaistEnd();
        if (waistBegin != null) {
            waist_rsb.setSelectedMinValue(waistBegin);
        }
        if (waistEnd != null) {
            waist_rsb.setSelectedMaxValue(waistEnd);
        }
        Integer hipBegin = allModelReqBean.getHipBegin();
        Integer hipEnd = allModelReqBean.getHipEnd();
        if (hipBegin != null) {
            hip_rsb.setSelectedMinValue(hipBegin);
        }
        if (hipEnd != null) {
            hip_rsb.setSelectedMaxValue(hipEnd);
        }
        Integer shoesCodeBegin = allModelReqBean.getShoesCodeBegin();
        Integer shoesCodeEnd = allModelReqBean.getShoesCodeEnd();
        if (shoesCodeBegin != null) {
            shoecode_rsb.setSelectedMinValue(shoesCodeBegin);
        }
        if (shoesCodeEnd != null) {
            shoecode_rsb.setSelectedMaxValue(shoesCodeEnd);
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
//        rightButtonText.setOnClickListener(this);
        reset_bt.setOnClickListener(this);
        sex_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.male_rb) {
                    cup_ll.setVisibility(View.GONE);
//                    male_female_divider.setVisibility(View.GONE);
                    female_style_ll.setVisibility(View.GONE);
                    male_style_ll.setVisibility(View.GONE);
                } else if (i == R.id.female_rb) {
                    cup_ll.setVisibility(View.GONE);
//                    male_female_divider.setVisibility(View.GONE);
                    female_style_ll.setVisibility(View.GONE);
                    male_style_ll.setVisibility(View.GONE);
                } else {
                    cup_ll.setVisibility(View.GONE);
//                    male_female_divider.setVisibility(View.VISIBLE);
                    female_style_ll.setVisibility(View.GONE);
                    male_style_ll.setVisibility(View.GONE);
                }
            }
        });
        country_tv.setOnClickListener(this);
        city_tv.setOnClickListener(this);
        occup_type_tv.setOnClickListener(this);
        male_style_ll.setOnClickListener(this);
        female_style_ll.setOnClickListener(this);
        filter_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
//            case R.id.rightButtonText:
//                reset();
//                break;
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
            case R.id.occup_type_tv:
                Intent occupIntent = new Intent(this, SelectJobTypeActivity.class);
                occupIntent.putExtra("isSubmit", true);//实际是不需要提交
                occupIntent.putExtra("strFlag", "");
                occupIntent.putExtra("isModel", true);
                startActivityForResult(occupIntent, OCCUP_REQ);
                break;
            case R.id.male_style_ll:
                Intent maleIntent = new Intent(this, SelectStyleActivity.class);
                tsBean.setStyleList(new ArrayList<ModelStyleVo>());
                maleIntent.putExtra("bean", tsBean);
                maleIntent.putExtra("typeFlag", "male");
                startActivityForResult(maleIntent, MALE_STYLE_REQ);
                break;
            case R.id.female_style_ll:
                Intent femaleIntent = new Intent(this, SelectStyleActivity.class);
                tsBean.setStyleList(new ArrayList<ModelStyleVo>());
                femaleIntent.putExtra("bean", tsBean);
                femaleIntent.putExtra("typeFlag", "female");
                startActivityForResult(femaleIntent, FEMALE_STYLE_REQ);
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
                allModelReqBean.setGender(null);
                break;
            case R.id.male_rb:
                allModelReqBean.setGender(1);
                break;
            case R.id.female_rb:
                allModelReqBean.setGender(0);
                break;
        }
        //国籍
        if (!StringUtils.isEmpty(country_tv.getText().toString())) {
            allModelReqBean.setCountry(countryBean.getcCode());
        }
        //拍摄地
        if (!StringUtils.isEmpty(city_tv.getText().toString())) {
            if (!StringUtils.isEmpty(areaBean.getCityId())) {
                allModelReqBean.setCity(Integer.parseInt(areaBean.getCityId()));
            } else {
                allModelReqBean.setCity(Integer.parseInt(areaBean.getProvinceId()));
            }
        }
        //职业
        if (!StringUtils.isEmpty(occup_type_tv.getText().toString())) {
            allModelReqBean.setOccupType(Integer.parseInt(carrierBean.getTypeId()));
        }
        //风格
        List<StyleBean> styleBeanList = new ArrayList<StyleBean>();
        List<Integer> styleList = new ArrayList<Integer>();
        switch (sex_rg.getCheckedRadioButtonId()) {
            case R.id.sex_all_rb:
                styleBeanList.addAll(maleStyleAdapter.getChannnelLst());
                styleBeanList.addAll(femaleStyleAdapter.getChannnelLst());
                break;
            case R.id.male_rb:
                styleBeanList.addAll(maleStyleAdapter.getChannnelLst());
                break;
            case R.id.female_rb:
                styleBeanList.addAll(femaleStyleAdapter.getChannnelLst());
                break;
        }
        for (StyleBean styleBean : styleBeanList) {
            styleList.add(Integer.parseInt(styleBean.getStyleId()));
        }
        if (styleList.size() > 0) {
            allModelReqBean.setStyleType(styleList);
            allModelReqBean.setSelectDatas(styleBeanList);
        }

        //年龄
        String beginAge = visitUnit.getDataUnit().getRepo().get("ageBegin") + "";
        String endAge = visitUnit.getDataUnit().getRepo().get("ageEnd") + "";
        if (!StringUtils.isEmpty(beginAge)) {
            allModelReqBean.setAgeBegin(Integer.parseInt(beginAge));
        } else {
            allModelReqBean.setAgeBegin(null);
        }
        if (!StringUtils.isEmpty(endAge)) {
            allModelReqBean.setAgeEnd(Integer.parseInt(endAge));
        } else {
            allModelReqBean.setAgeEnd(null);
        }

        //身高
        String heightBegin = visitUnit.getDataUnit().getRepo().get("heightBegin") + "";
        String heightEnd = visitUnit.getDataUnit().getRepo().get("heightEnd") + "";
        if (!StringUtils.isEmpty(heightBegin)) {
            allModelReqBean.setHeightBegin(Integer.parseInt(heightBegin));
        } else {
            allModelReqBean.setHeightBegin(null);
        }
        if (!StringUtils.isEmpty(heightEnd)) {
            allModelReqBean.setHeightEnd(Integer.parseInt(heightEnd));
        } else {
            allModelReqBean.setHeightEnd(null);
        }

        //体重
        String weightBegin = visitUnit.getDataUnit().getRepo().get("weightBegin") + "";
        String weightEnd = visitUnit.getDataUnit().getRepo().get("weightEnd") + "";
        if (!StringUtils.isEmpty(weightBegin)) {
            allModelReqBean.setWeightBegin(Integer.parseInt(weightBegin));
        } else {
            allModelReqBean.setWeightBegin(null);
        }
        if (!StringUtils.isEmpty(weightEnd)) {
            allModelReqBean.setWeightEnd(Integer.parseInt(weightEnd));
        } else {
            allModelReqBean.setWeightEnd(null);
        }

        //罩杯
        if (sex_rg.getCheckedRadioButtonId() == R.id.male_rb) {
            allModelReqBean.setCupBegin(null);
            allModelReqBean.setCupEnd(null);
        } else {
            String cupBegin = visitUnit.getDataUnit().getRepo().get("cupBegin") + "";
            String cupEnd = visitUnit.getDataUnit().getRepo().get("cupEnd") + "";
            if (!StringUtils.isEmpty(cupBegin)) {
                allModelReqBean.setCupBegin(Integer.parseInt(cupBegin));
            } else {
                allModelReqBean.setCupBegin(null);
            }
            if (!StringUtils.isEmpty(cupEnd)) {
                allModelReqBean.setCupEnd(Integer.parseInt(cupEnd));
            } else {
                allModelReqBean.setCupEnd(null);
            }
        }

        //胸围
        String bustBegin = visitUnit.getDataUnit().getRepo().get("bustBegin") + "";
        String bustEnd = visitUnit.getDataUnit().getRepo().get("bustEnd") + "";
        if (!StringUtils.isEmpty(bustBegin)) {
            allModelReqBean.setBustBegin(Integer.parseInt(bustBegin));
        } else {
            allModelReqBean.setBustBegin(null);
        }
        if (!StringUtils.isEmpty(bustEnd)) {
            allModelReqBean.setBustEnd(Integer.parseInt(bustEnd));
        } else {
            allModelReqBean.setBustEnd(null);
        }

        //腰围
        String waistBegin = visitUnit.getDataUnit().getRepo().get("waistBegin") + "";
        String waistEnd = visitUnit.getDataUnit().getRepo().get("waistEnd") + "";
        if (!StringUtils.isEmpty(waistBegin)) {
            allModelReqBean.setWaistBegin(Integer.parseInt(waistBegin));
        } else {
            allModelReqBean.setWaistBegin(null);
        }
        if (!StringUtils.isEmpty(waistEnd)) {
            allModelReqBean.setWaistEnd(Integer.parseInt(waistEnd));
        } else {
            allModelReqBean.setWaistEnd(null);
        }
        //臀围
        String hipBegin = visitUnit.getDataUnit().getRepo().get("hipBegin") + "";
        String hipEnd = visitUnit.getDataUnit().getRepo().get("hipEnd") + "";
        if (!StringUtils.isEmpty(hipBegin)) {
            allModelReqBean.setHipBegin(Integer.parseInt(hipBegin));
        } else {
            allModelReqBean.setHipBegin(null);
        }
        if (!StringUtils.isEmpty(hipEnd)) {
            allModelReqBean.setHipEnd(Integer.parseInt(hipEnd));
        } else {
            allModelReqBean.setHipEnd(null);
        }
        //鞋码
        String shoesCodeBegin = visitUnit.getDataUnit().getRepo().get("shoesCodeBegin") + "";
        String shoesCodeEnd = visitUnit.getDataUnit().getRepo().get("shoesCodeEnd") + "";
        if (!StringUtils.isEmpty(shoesCodeBegin)) {
            allModelReqBean.setShoesCodeBegin(Integer.parseInt(shoesCodeBegin));
        } else {
            allModelReqBean.setShoesCodeBegin(null);
        }
        if (!StringUtils.isEmpty(shoesCodeEnd)) {
            allModelReqBean.setShoesCodeEnd(Integer.parseInt(shoesCodeEnd));
        } else {
            allModelReqBean.setShoesCodeEnd(null);
        }

        selectCon = getSelectCon();
        mIntent.putExtra("requestBean", allModelReqBean);
        mIntent.putExtra("selectCon",selectCon);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private void reset() {
        selectCon = 0;
        //性别
        sex_rg.check(R.id.sex_all_rb);
        allModelReqBean.setGender(null);
        //国籍
        country_tv.setText("");
        countryBean = new CountryBean();
        allModelReqBean.setCountry(null);
        //拍摄地
        city_tv.setText("");
        areaBean = new PlushNoticeAreaBean();
        allModelReqBean.setCity(null);
        //职业
        occup_type_tv.setText("");
        carrierBean = new CarrierBean();
        allModelReqBean.setOccupType(null);
        //风格
        tsBean.getSelectDatas().clear();
        maleStyleList.clear();
        femaleStyleList.clear();
        maleStyleAdapter.notifyDataSetChanged();
        femaleStyleAdapter.notifyDataSetChanged();
        allModelReqBean.setSelectDatas(new ArrayList<StyleBean>());
//        allModelReqBean.setStyleType(new ArrayList<Integer>());
        allModelReqBean.setStyleType(null);

        root_sv.smoothScrollTo(0,0);
        //年龄
        age_rsb.reset();
        allModelReqBean.setAgeBegin(null);
        allModelReqBean.setAgeEnd(null);
        //身高
        height_rsb.reset();
        allModelReqBean.setHeightBegin(null);
        allModelReqBean.setHeightEnd(null);
        //体重
        weight_rsb.reset();
        allModelReqBean.setWeightBegin(null);
        allModelReqBean.setWeightEnd(null);
        //罩杯
        cup_rsb.reset();
        allModelReqBean.setCupBegin(null);
        allModelReqBean.setCupEnd(null);
        //胸围
        bust_rsb.reset();
        allModelReqBean.setBustBegin(null);
        allModelReqBean.setBustEnd(null);
        //腰围
        waist_rsb.reset();
        allModelReqBean.setWaistBegin(null);
        allModelReqBean.setWaistEnd(null);
        //臀围
        hip_rsb.reset();
        allModelReqBean.setHipBegin(null);
        allModelReqBean.setHipEnd(null);
        //鞋码
        shoecode_rsb.reset();
        allModelReqBean.setShoesCodeBegin(null);
        allModelReqBean.setShoesCodeEnd(null);
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
            case OCCUP_REQ: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                carrierBean = (CarrierBean) data.getSerializableExtra(Constants.COMEBACK);
                if (!StringUtils.isEmpty(carrierBean.getCarrierName())) {
                    occup_type_tv.setText(carrierBean.getCarrierName());
                } else {
                    occup_type_tv.setText("");
                }
                break;
            }
            case MALE_STYLE_REQ: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                tsBean = (TransStyleBean) data.getSerializableExtra(Constants.COMEBACK);
                maleStyleList.clear();
                maleStyleList.addAll(tsBean.getSelectDatas());
                maleStyleAdapter = new StyleAdapter(this, maleStyleList);
                male_style_gv.setAdapter(maleStyleAdapter);
                break;
            }
            case FEMALE_STYLE_REQ: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                tsBean = (TransStyleBean) data.getSerializableExtra(Constants.COMEBACK);
                femaleStyleList.clear();
                femaleStyleList.addAll(tsBean.getSelectDatas());
                femaleStyleAdapter = new StyleAdapter(this, femaleStyleList);
                female_style_gv.setAdapter(femaleStyleAdapter);
                break;
            }
            default:
                break;
        }
    }

    private int getSelectCon(){
        int selectCon = 0;
        //性别

        if (allModelReqBean.getGender() != null){
            selectCon++;
        }
        //国籍
        if (!StringUtils.isEmpty(allModelReqBean.getCountry())){
            selectCon++;
        }
        //拍摄地
        if (allModelReqBean.getCity() != null){
            selectCon++;
        }

        //职业
        if (allModelReqBean.getOccupType() != null){
            selectCon++;
        }
        //风格
//        if (allModelReqBean.getStyleType()!=null &&allModelReqBean.getStyleType().size()>0){
//            selectCon++;
//        }
        //年龄

        if (allModelReqBean.getAgeBegin() != null||allModelReqBean.getAgeEnd() != null) {
            selectCon++;
        }

        //身高
        if (allModelReqBean.getHeightBegin() != null||allModelReqBean.getHeightEnd() != null) {
            selectCon++;
        }
        //体重
        if (allModelReqBean.getWeightBegin() != null||allModelReqBean.getWeightEnd() != null) {
            selectCon++;
        }
//        //罩杯
//        if (allModelReqBean.getCupBegin() != null) {
//            selectCon++;
//        }
//        if (allModelReqBean.getCupEnd() != null) {
//            selectCon++;
//        }
//        //胸围
//        if (allModelReqBean.getBustBegin() != null) {
//            selectCon++;
//        }
//        if (allModelReqBean.getBustEnd() != null) {
//            selectCon++;
//        }
//        //腰围
//        if (allModelReqBean.getWaistBegin() != null) {
//            selectCon++;
//        }
//        if (allModelReqBean.getWaistEnd() != null) {
//            selectCon++;
//        }
//        //臀围
//        if (allModelReqBean.getHipBegin() != null) {
//            selectCon++;
//        }
//        if (allModelReqBean.getHipEnd() != null) {
//            selectCon++;
//        }
//        //鞋码
//        if (allModelReqBean.getShoesCodeBegin() != null) {
//            selectCon++;
//        }
//        if (allModelReqBean.getShoesCodeEnd() != null) {
//            selectCon++;
//        }
        return selectCon;
    }
}


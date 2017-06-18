package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.notice.bean.QueryNoticesCond;
import com.xygame.sg.activity.notice.bean.QueryNoticesListBean;
import com.xygame.sg.activity.notice.bean.RecruitCondBean;
import com.xygame.sg.activity.notice.bean.ShootCondBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.MyIRangeSeekBar;
import com.xygame.sg.utils.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import base.ViewBinder;
import base.frame.VisitUnit;

public class FilterNoticeActivity extends SGBaseActivity implements View.OnClickListener{
    private View backButton;
    private TextView titleName;
    private TextView rightButtonText;

    private RadioGroup sex_rg;
    private RadioButton sex_all_rb;
    private RadioButton male_rb;
    private RadioButton female_rb;
    private LinearLayout shoot_address_ll;//拍摄地
    private TextView shoot_address_tv;//拍摄地
    private TextView start_time_tv;
    private TextView end_time_tv;
    private MyIRangeSeekBar price_rsb;
    private CheckBox is_pre_payed_cb;//是否预付
    private CheckBox is_travel_pay_cb;//是否报销差旅费
    private CheckBox is_hotel_pay_cb;//是否报销住宿费

    private RoundTextView reset_bt;
    private Button filter_bt;

    private PlushNoticeAreaBean areaBean;

    private static final int START_TIME_REQ = 2;
    private static final int END_TIME_REQ = 3;
    private static final int SHOOT_ADDRESS_REQ= 4;

    VisitUnit visitUnit = new VisitUnit(this);

    private QueryNoticesListBean queryNoticesListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_filter_notice, null));
        queryNoticesListBean = (QueryNoticesListBean) getIntent().getSerializableExtra("requestBean");
        initViews();
        initDatas();
        addListener();
    }



    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        titleName.setText(getText(R.string.title_activity_filter_notice));
//        rightButtonText.setVisibility(View.VISIBLE);
//        rightButtonText.setText(getText(R.string.reset_condition));

        sex_rg = (RadioGroup) findViewById(R.id.sex_rg);
        sex_all_rb = (RadioButton) findViewById(R.id.sex_all_rb);
        male_rb = (RadioButton) findViewById(R.id.male_rb);
        female_rb = (RadioButton) findViewById(R.id.female_rb);
        shoot_address_ll = (LinearLayout) findViewById(R.id.shoot_address_ll);
        shoot_address_tv = (TextView) findViewById(R.id.shoot_address_tv);
        start_time_tv = (TextView) findViewById(R.id.start_time_tv);
        end_time_tv = (TextView) findViewById(R.id.end_time_tv);
        price_rsb = (MyIRangeSeekBar) findViewById(R.id.price_rsb);
        is_pre_payed_cb = (CheckBox) findViewById(R.id.is_pre_payed_cb);
        is_travel_pay_cb = (CheckBox) findViewById(R.id.is_travel_pay_cb);
        is_hotel_pay_cb = (CheckBox) findViewById(R.id.is_hotel_pay_cb);
        filter_bt = (Button) findViewById(R.id.filter_bt);
        reset_bt = (RoundTextView) findViewById(R.id.reset_bt);
    }
    private void initDatas() {

        RecruitCondBean recruitCond = queryNoticesListBean.getCond().getRecruitCond();
        ShootCondBean shootCond = queryNoticesListBean.getCond().getShootCond();
        areaBean = new PlushNoticeAreaBean();
        if (!StringUtils.isEmpty(shootCond.getCity()+"")){
            areaBean.setCityId(shootCond.getCity()+"");
        }
        if (!StringUtils.isEmpty(shootCond.getProvince()+"")){
            areaBean.setProvinceId(shootCond.getProvince()+"");
        }
        //性别
        int gender = recruitCond.getGender();
        if (gender == -1){
            sex_all_rb.setChecked(true);
            male_rb.setChecked(false);
            female_rb.setChecked(false);
        } else if (gender == Integer.parseInt(Constants.SEX_MAN)){
            sex_all_rb.setChecked(false);
            male_rb.setChecked(true);
            female_rb.setChecked(false);
        } else if (gender == Integer.parseInt(Constants.SEX_WOMAN)){
            sex_all_rb.setChecked(false);
            male_rb.setChecked(false);
            female_rb.setChecked(true);
        }
        //拍摄地
        int provinceId = shootCond.getProvince();
        int cityId = shootCond.getCity();
        String address = "",provinceName="",cityName="";
        if (provinceId != -1&&provinceId != 0){
            ProvinceBean province= AssetDataBaseManager.getManager().queryProviceById(provinceId);
            address+=province.getName()+" ";
            provinceName=province.getName();
        }
        if (cityId != -1&&cityId != 0){
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(cityId);
            address +=it.getName();
            cityName=it.getName();
        }
        if (address.contains("北京")||address.contains("重庆")||address.contains("上海")||address.contains("天津")){
            shoot_address_tv.setText(cityName);
        }else{
            shoot_address_tv.setText(address);
        }

//        //开始和结束时间
//        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
//        String startTime = shootCond.getStartTime();
//        String endTime = shootCond.getEndTime();
//        if (!StringUtils.isEmpty(startTime)){
//            start_time_tv.setText(sdf.format(new Date(Long.parseLong(startTime))));
//        }
//        if (!StringUtils.isEmpty(endTime)){
//            end_time_tv.setText(sdf.format(new Date(Long.parseLong(endTime))));
//        }
//        //价格
//        long minPrice = recruitCond.getMinPrice();
//        long maxPrice = recruitCond.getMaxPrice();
//        if (minPrice != -1){
//            price_rsb.setSelectedMinValue(minPrice / 100);
//        }
//        if (maxPrice != -1 && maxPrice <5000*100){
//            price_rsb.setSelectedMaxValue(maxPrice / 100);
//        }

        int rePayStatus = queryNoticesListBean.getCond().getRepayStatus();//是否支付（不限：不传或-1，1:支付)
        if (rePayStatus == 1 ){
            is_pre_payed_cb.setChecked(true);
        } else {
            is_pre_payed_cb.setChecked(false);
        }

        if (recruitCond.getIsAffordTravelFee() == 1 ){//是否报销差旅费（1：报销，2：不报销，不传或-1表示不限）
            is_travel_pay_cb.setChecked(true);
        } else {
            is_travel_pay_cb.setChecked(false);
        }

        if (recruitCond.getIsAffordAccomFee() == 1 ){// 是否报销住宿费（1：报销，2：不报销，不传或-1表示不限）
            is_hotel_pay_cb.setChecked(true);
        } else {
            is_hotel_pay_cb.setChecked(false);
        }

    }

    private void addListener() {
        backButton.setOnClickListener(this);
        shoot_address_ll.setOnClickListener(this);
        start_time_tv.setOnClickListener(this);
        end_time_tv.setOnClickListener(this);
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
            case R.id.start_time_tv:
                startActivityForResult(new Intent(this, ChoiceDateView.class), START_TIME_REQ);
                break;
            case R.id.end_time_tv:
                startActivityForResult(new Intent(this, ChoiceDateView.class), END_TIME_REQ);
                break;
            case R.id.shoot_address_ll:
                Intent firstIntent = new Intent(this,PlushNoticePlaceProvinceActivity.class);
                firstIntent.putExtra("bean", areaBean);
                firstIntent.putExtra("noLimitFlag",true);
                startActivityForResult(firstIntent, SHOOT_ADDRESS_REQ);
                break;
            case R.id.filter_bt:
                toFilter();
                break;
        }
    }

    private void toFilter() {
        Intent mIntent = new Intent();
        //性别
        switch (sex_rg.getCheckedRadioButtonId()){
            case R.id.sex_all_rb:
                queryNoticesListBean.getCond().getRecruitCond().setGender(-1);
                break;
            case R.id.male_rb:
                queryNoticesListBean.getCond().getRecruitCond().setGender(Integer.parseInt(Constants.SEX_MAN));
                break;
            case R.id.female_rb:
                queryNoticesListBean.getCond().getRecruitCond().setGender(Integer.parseInt(Constants.SEX_WOMAN));

                break;
        }
        //拍摄地
        if (!StringUtils.isEmpty(shoot_address_tv.getText().toString())){
            if (!StringUtils.isEmpty(areaBean.getCityId())){
                queryNoticesListBean.getCond().getShootCond().setCity(Integer.parseInt(areaBean.getCityId()));
            }
            if (!StringUtils.isEmpty(areaBean.getProvinceId())){
                queryNoticesListBean.getCond().getShootCond().setProvince(Integer.parseInt(areaBean.getProvinceId()));
            }
        }

        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
        //开始时间
//        if (!StringUtils.isEmpty(start_time_tv.getText().toString())){
//            String startStr =  start_time_tv.getText().toString();
//            try {
//                queryNoticesListBean.getCond().getShootCond().setStartTime(sdf.parse(startStr).getTime()+"");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        //结束时间
//        if (!StringUtils.isEmpty(end_time_tv.getText().toString())){
//            String startStr =  end_time_tv.getText().toString();
//            try {
//                queryNoticesListBean.getCond().getShootCond().setEndTime(sdf.parse(startStr).getTime() + "");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
        //价格
//        String minPriceStr = visitUnit.getDataUnit().getRepo().get("priceBegin")+"";
//        String maxPriceStr = visitUnit.getDataUnit().getRepo().get("priceEnd")+"";
//        if (minPriceStr != null && !minPriceStr.equals("null")){
//
//            if (minPriceStr.equals("")){
//                queryNoticesListBean.getCond().getRecruitCond().setMinPrice(-1);
//            } else {
//                queryNoticesListBean.getCond().getRecruitCond().setMinPrice(Integer.parseInt(minPriceStr)*100);
//            }
//        }
//        if (!StringUtils.isEmpty(maxPriceStr)){
//            if (Integer.parseInt(maxPriceStr)!=5000){
//                queryNoticesListBean.getCond().getRecruitCond().setMaxPrice(Integer.parseInt(maxPriceStr)*100);
//            } else {
//                queryNoticesListBean.getCond().getRecruitCond().setMaxPrice(-1);
//            }
//
//        }

        if (is_pre_payed_cb.isChecked()){
            queryNoticesListBean.getCond().setRepayStatus(1);
        } else {
            queryNoticesListBean.getCond().setRepayStatus(-1);
        }

        if (is_travel_pay_cb.isChecked()){
            queryNoticesListBean.getCond().getRecruitCond().setIsAffordTravelFee(1);
        } else {
            queryNoticesListBean.getCond().getRecruitCond().setIsAffordTravelFee(-1);
        }

        if (is_hotel_pay_cb.isChecked()){
            queryNoticesListBean.getCond().getRecruitCond().setIsAffordAccomFee(1);
        } else {
            queryNoticesListBean.getCond().getRecruitCond().setIsAffordAccomFee(-1);
        }

        mIntent.putExtra("requestBean",queryNoticesListBean);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private void reset() {
        //性别
        sex_rg.check(R.id.sex_all_rb);
        //拍摄地
        shoot_address_tv.setText("");
        areaBean = new PlushNoticeAreaBean();
        //开始和结束时间
        start_time_tv.setText("");
        end_time_tv.setText("");
        price_rsb.reset();
        is_pre_payed_cb.setChecked(false);
        is_travel_pay_cb.setChecked(false);
        is_hotel_pay_cb.setChecked(false);
        queryNoticesListBean.setCond(new QueryNoticesCond());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case START_TIME_REQ:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Serializable item2 = data.getSerializableExtra("bean");
                if (item2 != null) {
                    FeedbackDateBean ftBean = (FeedbackDateBean) item2;
                    String sTimer = ftBean.getYear() + "-" + ftBean.getMonth() + "-" + ftBean.getDay();
                    start_time_tv.setText(sTimer);
                }
                break;
            case END_TIME_REQ:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Serializable item3 = data.getSerializableExtra("bean");
                if (item3 != null) {
                    FeedbackDateBean ftBean = (FeedbackDateBean) item3;
                    String eTimer = ftBean.getYear() + "-" + ftBean.getMonth() + "-" + ftBean.getDay();
                    end_time_tv.setText(eTimer);
                }
                break;
            case SHOOT_ADDRESS_REQ: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                areaBean= (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
                if(areaBean.getProvinceName()!=null){
                    if(areaBean.getCityName()!=null){
                        if(areaBean.getProvinceName().equals(areaBean.getCityName())){
                            shoot_address_tv.setText(areaBean.getProvinceName());
                        }else{
                            shoot_address_tv.setText(areaBean.getProvinceName().concat(" ").concat(areaBean.getCityName()));
                        }
                    }else{
                        shoot_address_tv.setText(areaBean.getProvinceName());
                    }
                }
                break;
            }
            default:
                break;
        }
    }
}

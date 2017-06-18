package com.xygame.sg.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitListVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/12/24.
 */
public class LinearLayoutForListView extends LinearLayout {
    private Context context;
    private TypedArray bgColors;
    private boolean flag;
//    private String[] tipsArr;
    public LinearLayoutForListView(Context context,boolean flag) {
        super(context);
        this.context = context;
        this.flag=flag;
        init();
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Resources res = getResources();
        bgColors = res.obtainTypedArray(R.array.notice_request_bg);
//        tipsArr = res.getStringArray(R.array.request_ser_tip);
    }

    public void bindLinearLayout(List<NoticeRecruitVo> recruitList,boolean isExpand ) {
        int count = recruitList.size();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            View convertView = LayoutInflater.from(getContext()).inflate(R.layout.notice_list_request_item, null);
            LinearLayout collapse_ll = (LinearLayout) convertView.findViewById(R.id.collapse_ll);
            RoundTextView request_no_tip_tv = (RoundTextView) convertView.findViewById(R.id.request_no_tip_tv);
            View divider_line = convertView.findViewById(R.id.divider_line);

            if (isExpand){
                request_no_tip_tv.setVisibility(View.VISIBLE);
//                request_no_tip_tv.setText(tipsArr[i]);
                request_no_tip_tv.setText("招募"+StringUtils.transition((i+1)+""));

                if (i>=20){
                    request_no_tip_tv.getDelegate().setBackgroundColor(bgColors.getColor(i-20,0));
                }else {
                    request_no_tip_tv.getDelegate().setBackgroundColor(bgColors.getColor(i,0));
                }
                collapse_ll.setVisibility(View.VISIBLE);
            } else {
                request_no_tip_tv.setVisibility(View.GONE);
                request_no_tip_tv.setText("招募"+StringUtils.transition((i+1)+""));
                if (i>=20){
                    request_no_tip_tv.getDelegate().setBackgroundColor(bgColors.getColor(i-20,0));
                }else {
                    request_no_tip_tv.getDelegate().setBackgroundColor(bgColors.getColor(i,0));
                }
                collapse_ll.setVisibility(View.GONE);
            }
//            bgColors.recycle();
            if(i==0 && !isExpand){
                request_no_tip_tv.setVisibility(View.VISIBLE);
                request_no_tip_tv.setText("招募要求");
                request_no_tip_tv.getDelegate().setBackgroundColor(R.color.color_grey_999999);
            }
            if (i==(count-1)){
                divider_line.setVisibility(View.GONE);
            }

            NoticeRecruitListVo noticeRecruitListVo = recruitList.get(i);
            initView(noticeRecruitListVo,convertView);

            addView(convertView, i);
        }
    }

    public void bindLinearLayout(NoticeRecruitVo noticeRecruitListVo ) {
        this.removeAllViews();
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.notice_list_request_item, null);
        LinearLayout collapse_ll = (LinearLayout) convertView.findViewById(R.id.collapse_ll);
        RoundTextView request_no_tip_tv = (RoundTextView) convertView.findViewById(R.id.request_no_tip_tv);
        View divider_line = convertView.findViewById(R.id.divider_line);

        request_no_tip_tv.setVisibility(View.GONE);
        divider_line.setVisibility(View.GONE);

        initView(noticeRecruitListVo,convertView);

        addView(convertView);
    }


    public void bindLinearLayout(NoticeDetailVo notice) {
        List<NoticeRecruitVo> recruitList = notice.getRecruits();
        long publishUserId = notice.getPublishUserId();
        int count = recruitList.size();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            View convertView = LayoutInflater.from(getContext()).inflate(R.layout.notice_detail_list_request_item, null);
            RoundTextView request_no_tip_tv = (RoundTextView) convertView.findViewById(R.id.request_no_tip_tv);
            RelativeLayout enroll_rl = (RelativeLayout) convertView.findViewById(R.id.enroll_rl);
            RelativeLayout query_rl = (RelativeLayout) convertView.findViewById(R.id.query_rl);
            TextView applySum_tv = (TextView) convertView.findViewById(R.id.applySum_tv);//
            RoundTextView encroll_tv = (RoundTextView) convertView.findViewById(R.id.encroll_tv);
            TextView query_applySum_tv = (TextView) convertView.findViewById(R.id.query_applySum_tv);
            TextView okSum_tv = (TextView) convertView.findViewById(R.id.okSum_tv);
            TextView query_tv = (TextView) convertView.findViewById(R.id.query_tv);
            TextView no_pay_tip_tv = (TextView) convertView.findViewById(R.id.no_pay_tip_tv);
            LinearLayout enroll_status_ll = (LinearLayout) convertView.findViewById(R.id.enroll_status_ll);

//            request_no_tip_tv.setText(tipsArr[i]);
            request_no_tip_tv.setText("招募"+StringUtils.transition((i+1)+""));
            if (i>=20){
                request_no_tip_tv.getDelegate().setBackgroundColor(bgColors.getColor(i-20,0));
            }else {
                request_no_tip_tv.getDelegate().setBackgroundColor(bgColors.getColor(i,0));
            }
//            bgColors.recycle();

            NoticeRecruitVo noticeRecruitListVo = recruitList.get(i);
            initView(noticeRecruitListVo, convertView);

            if (UserPreferencesUtil.isOnline(context)){
                if (UserPreferencesUtil.getUserId(context).equals(publishUserId + "")){
                    query_rl.setVisibility(View.VISIBLE);
                    enroll_rl.setVisibility(View.GONE);
                    query_applySum_tv.setText(noticeRecruitListVo.getApplySum()+"");
                    okSum_tv.setText(noticeRecruitListVo.getOkSum()+"");
                    if(notice.getNoticeType() == 2 && notice.getPayStatus() ==1 ){
                        no_pay_tip_tv.setVisibility(VISIBLE);
                        enroll_status_ll.setVisibility(GONE);
                    } else {
                        no_pay_tip_tv.setVisibility(GONE);
                        enroll_status_ll.setVisibility(VISIBLE);
                    }
                } else{
                    query_rl.setVisibility(View.GONE);
                    enroll_rl.setVisibility(View.VISIBLE);
                    applySum_tv.setText(noticeRecruitListVo.getApplySum()+"");
                }
            } else {
                query_rl.setVisibility(View.GONE);
                enroll_rl.setVisibility(View.VISIBLE);
                applySum_tv.setText(noticeRecruitListVo.getApplySum() + "");
            }

            //是否已报名：1：报名；0：未报名
            if (noticeRecruitListVo.getApplied()==1){
//                encroll_tv.setText("已报名");
//                encroll_tv.setTextColor(getResources().getColor(R.color.text_gray_s));
//                encroll_tv.getDelegate().setStrokeColor(getResources().getColor(R.color.text_gray_s));
//                encroll_tv.setEnabled(false);
                if (noticeRecruitListVo.getRecordStatus() == 4){
                    encroll_tv.setText("聊天");
                } else {
                    encroll_tv.setText("已报名");
                    encroll_tv.setTextColor(getResources().getColor(R.color.text_gray_s));
                    encroll_tv.getDelegate().setStrokeColor(getResources().getColor(R.color.text_gray_s));
                    encroll_tv.setEnabled(false);
                }


            } else {
                encroll_tv.setText("我要报名");
                encroll_tv.setTextColor(getResources().getColor(R.color.dark_green));
                encroll_tv.getDelegate().setStrokeColor(getResources().getColor(R.color.dark_green));
                encroll_tv.setEnabled(true);
            }
            if (context instanceof NoticeDetailActivity){
                if (flag){
                    enroll_rl.setOnClickListener(((NoticeDetailActivity) context).new EnrollItemClickListener(recruitList, i));
                }else{
                    encroll_tv.setTextColor(getResources().getColor(R.color.dark_gray));
                    encroll_tv.getDelegate().setStrokeColor(getResources().getColor(R.color.dark_gray));
                    encroll_tv.setEnabled(false);
                }
                query_rl.setOnClickListener(((NoticeDetailActivity) context).new QueryItemClickListener(recruitList, i));
            }
            addView(convertView, i);
        }
    }

    public View initView(NoticeRecruitListVo noticeRecruitListVo,View convertView){
        LinearLayout basic_request_ll = (LinearLayout) convertView.findViewById(R.id.basic_request_ll);
        ImageView sex_iv = (ImageView) convertView.findViewById(R.id.sex_iv);
        TextView sex_tv = (TextView) convertView.findViewById(R.id.sex_tv);
        TextView num_tv = (TextView) convertView.findViewById(R.id.num_tv);
        TextView price_tv = (TextView) convertView.findViewById(R.id.price_tv);
        LinearLayout travel_hotel_ll = (LinearLayout) convertView.findViewById(R.id.travel_hotel_ll);
        ImageView travel_iv = (ImageView) convertView.findViewById(R.id.travel_iv);
        TextView travel_tv = (TextView) convertView.findViewById(R.id.travel_tv);
        ImageView hotel_iv = (ImageView) convertView.findViewById(R.id.hotel_iv);
        TextView hotel_tv = (TextView) convertView.findViewById(R.id.hotel_tv);
        LinearLayout request_message_ll = (LinearLayout) convertView.findViewById(R.id.request_message_ll);
        TextView request_message_tv = (TextView) convertView.findViewById(R.id.request_message_tv);

        if (noticeRecruitListVo.getGender()==0 ){//女
            sex_iv.setImageResource(R.drawable.sg_pl_woman);
            sex_tv.setText("女");
        } else {
            sex_iv.setImageResource(R.drawable.sg_pl_man);
            sex_tv.setText("男");
        }
        num_tv.setText(noticeRecruitListVo.getCount()+"人");
        String price = noticeRecruitListVo.getReward()/100.0+"";
        if (price.endsWith(".0")){
            price = price.replace(".0","");
        }
        price_tv.setText("￥"+price);


        List<Map<String,String>> bassicRequestList = getData(noticeRecruitListVo);
        int all = bassicRequestList.size();
        int oneLineItem = 3;
        int numLine = all%oneLineItem==0 ? all/oneLineItem : all/oneLineItem+1;
        int innerLoop = 0;
        for (int j = 0 ;j< numLine;j++){
            LinearLayout item_ll = new LinearLayout(getContext());
            item_ll.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, DataTools.dip2px(getContext(),20),0,0);
            item_ll.setLayoutParams(params);
            for (;innerLoop<all;innerLoop++){
                LinearLayout sub_itm_ll = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.notice_request_sub_item,null);
                ImageView item_iv = (ImageView) sub_itm_ll.findViewById(R.id.item_iv);
                TextView item_tv = (TextView) sub_itm_ll.findViewById(R.id.item_tv);
                item_iv.setImageResource(Integer.parseInt(bassicRequestList.get(innerLoop).get("imgId")));
                item_tv.setText(bassicRequestList.get(innerLoop).get("name"));

                if (innerLoop % oneLineItem != 0 ){
                    LayoutParams subParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    subParams.setMargins(DataTools.dip2px(getContext(),12),0,0,0);
                    sub_itm_ll.setLayoutParams(subParams);
                }
                item_ll.addView(sub_itm_ll);
                if (innerLoop % oneLineItem ==(oneLineItem -1) ){
                    innerLoop++;
                    break;
                }
            }

            basic_request_ll.addView(item_ll);
        }


        if (noticeRecruitListVo.getAffordTravelFee() == 1){
            travel_tv.setText("报销异地差旅费");
            travel_tv.setVisibility(VISIBLE);
            travel_iv.setVisibility(VISIBLE);
        } else {
            travel_tv.setText("不报销异地差旅费");
            travel_tv.setVisibility(GONE);
            travel_iv.setVisibility(GONE);
        }

        if (noticeRecruitListVo.getAffordAccomFee() == 1){
            hotel_tv.setText("报销异地住宿费");
            hotel_tv.setVisibility(VISIBLE);
            hotel_iv.setVisibility(VISIBLE);
        } else {
            hotel_tv.setText("不报销异地住宿费");
            hotel_tv.setVisibility(GONE);
            hotel_iv.setVisibility(GONE);
        }

        if (travel_tv.getVisibility() == GONE && hotel_tv.getVisibility() == GONE ){
            travel_hotel_ll.setVisibility(GONE);
        } else {
            travel_hotel_ll.setVisibility(VISIBLE);
        }

        String messageStr = noticeRecruitListVo.getRemark();
        if (!StringUtils.isEmpty(messageStr)){
            request_message_ll.setVisibility(VISIBLE);
            request_message_tv.setText(messageStr);
        } else {
            request_message_ll.setVisibility(GONE);
        }
        return convertView;
    }

    public static List<Map<String,String>> getData(NoticeRecruitListVo noticeRecruitListVo){
        List<Map<String,String>> bassicRequestList = new ArrayList<Map<String,String>>();
        //城市
        String cityStr = noticeRecruitListVo.getCity()+"";
        if (!StringUtils.isEmpty(cityStr) && !cityStr.equals("0")){
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(noticeRecruitListVo.getCity());
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",it.getName());
            map.put("imgId", R.drawable.address_icon + "");
            bassicRequestList.add(map);
        }
        //国家
        String country = noticeRecruitListVo.getCountry();
        if (!StringUtils.isEmpty(country)){
            List<Map<String, String>> datas = Constants.COUNTRY_DATA;
            for (Map<String, String> it : datas) {
                if (it.get("country_code").equals(country)) {
                    String countryName = it.get("country_name");
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name",countryName);
                    map.put("imgId",R.drawable.sg_pl_country+"");
                    bassicRequestList.add(map);
                    break;
                }
            }
        }
        //年龄
        String ageStr = "";
        boolean isShowAge = false;
        String minAgeStr = noticeRecruitListVo.getMinAge()+"";
        String maxAgeStr = noticeRecruitListVo.getMaxAge()+"";
        if ((!StringUtils.isEmpty(minAgeStr)&& !minAgeStr.equals("-1")) && (StringUtils.isEmpty(maxAgeStr)||maxAgeStr.equals("-1"))){
            ageStr="大于"+minAgeStr+"岁";
            isShowAge = true;
        } else if ((!StringUtils.isEmpty(maxAgeStr) && !maxAgeStr.equals("-1")) && (StringUtils.isEmpty(minAgeStr)||minAgeStr.equals("-1"))){
            ageStr="小于"+maxAgeStr+"岁";
            isShowAge = true;
        }else if ((!StringUtils.isEmpty(maxAgeStr)&& !maxAgeStr.equals("-1")) && (!StringUtils.isEmpty(minAgeStr)&&!minAgeStr.equals("-1"))){
            ageStr=minAgeStr+"-"+maxAgeStr+"岁";
            isShowAge = true;
        }else if ((StringUtils.isEmpty(maxAgeStr)|| maxAgeStr.equals("-1"))&&(StringUtils.isEmpty(minAgeStr)||minAgeStr.equals("-1"))){
            isShowAge = false;
        }
        if (isShowAge){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",ageStr);
            map.put("imgId",R.drawable.sg_pl_age+"");
            bassicRequestList.add(map);
        }

        //身高
        String heightStr = "";
        boolean isShowHeight = false;
        String minHeightStr = noticeRecruitListVo.getMinHeight()+"";
        String maxHeightStr = noticeRecruitListVo.getMaxHeight()+"";
        if ((!StringUtils.isEmpty(minHeightStr)&& !minHeightStr.equals("-1"))&&(StringUtils.isEmpty(maxHeightStr)||maxHeightStr.equals("-1"))){
            heightStr="大于"+minHeightStr+"cm";
            isShowHeight = true;
        } else if ((!StringUtils.isEmpty(maxHeightStr)&& !maxHeightStr.equals("-1")) && (StringUtils.isEmpty(minHeightStr)||minHeightStr.equals("-1"))){
            heightStr="小于"+maxHeightStr+"cm";
            isShowHeight = true;
        }else if ((!StringUtils.isEmpty(maxHeightStr)&& !maxHeightStr.equals("-1")) && (!StringUtils.isEmpty(minHeightStr)&&!minHeightStr.equals("-1"))){
            heightStr=minHeightStr+"-"+maxHeightStr+"cm";
            isShowHeight = true;
        }else if ((StringUtils.isEmpty(maxHeightStr)|| maxHeightStr.equals("-1"))&&(StringUtils.isEmpty(minHeightStr)||minHeightStr.equals("-1"))){
            isShowHeight = false;
        }
        if (isShowHeight){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",heightStr);
            map.put("imgId",R.drawable.sg_pl_sg+"");
            bassicRequestList.add(map);
        }
        //体重
        String weightStr = "";
        boolean isShowWeight = false;
        String minWeightStr = noticeRecruitListVo.getMinWeight()+"";
        String maxWeightStr = noticeRecruitListVo.getMaxWeight()+"";
        if ((!StringUtils.isEmpty(minWeightStr)&& !minWeightStr.equals("-1"))&&(StringUtils.isEmpty(maxWeightStr)||maxWeightStr.equals("-1"))){
            weightStr="大于"+minWeightStr+"kg";
            isShowWeight = true;
        } else if ((!StringUtils.isEmpty(maxWeightStr)&& !maxWeightStr.equals("-1")) && (StringUtils.isEmpty(minWeightStr)||minWeightStr.equals("-1"))){
            weightStr="小于"+maxWeightStr+"kg";
            isShowWeight = true;
        }else if ((!StringUtils.isEmpty(maxWeightStr)&& !maxWeightStr.equals("-1")) && (!StringUtils.isEmpty(minWeightStr)&&!minWeightStr.equals("-1"))){
            weightStr=minWeightStr+"-"+maxWeightStr+"kg";
            isShowWeight = true;
        }else if ((StringUtils.isEmpty(maxWeightStr)|| maxWeightStr.equals("-1"))&&(StringUtils.isEmpty(minWeightStr)||minWeightStr.equals("-1"))){
            isShowWeight = false;
        }
        if (isShowWeight){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",weightStr);
            map.put("imgId",R.drawable.sg_pl_weight+"");
            bassicRequestList.add(map);
        }
        //胸围
        String chestStr = "";
        boolean isShowChest = false;
        String minChestStr = noticeRecruitListVo.getMinBust()+"";
        String maxChestStr = noticeRecruitListVo.getMaxBust() +"";
        if ((!StringUtils.isEmpty(minChestStr)&& !minChestStr.equals("-1"))&&(StringUtils.isEmpty(maxChestStr)||maxChestStr.equals("-1"))){
            chestStr="大于"+minChestStr+"cm";
            isShowChest = true;
        } else if ((!StringUtils.isEmpty(maxChestStr)&& !maxChestStr.equals("-1")) && (StringUtils.isEmpty(minChestStr)||minChestStr.equals("-1"))){
            chestStr="小于"+maxChestStr+"cm";
            isShowChest = true;
        }else if ((!StringUtils.isEmpty(maxChestStr)&& !maxChestStr.equals("-1")) && (!StringUtils.isEmpty(minChestStr)&&!minChestStr.equals("-1"))){
            chestStr=minChestStr+"-"+maxChestStr+"cm";
            isShowChest = true;
        }else if ((StringUtils.isEmpty(maxChestStr)|| maxChestStr.equals("-1"))&&(StringUtils.isEmpty(minChestStr)||minChestStr.equals("-1"))){
            isShowChest = false;
        }
        if (isShowChest){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",chestStr);
            map.put("imgId",R.drawable.sg_pl_chest+"");
            bassicRequestList.add(map);
        }

        //腰围
        String waistStr = "";
        boolean isShowWaist = false;
        String minWaistStr = noticeRecruitListVo.getMinWaist()+"";
        String maxWaistStr = noticeRecruitListVo.getMaxWaist() +"";
        if ((!StringUtils.isEmpty(minWaistStr)&& !minWaistStr.equals("-1"))&&(StringUtils.isEmpty(maxWaistStr)||maxWaistStr.equals("-1"))){
            waistStr="大于"+minWaistStr+"cm";
            isShowWaist = true;
        } else if ((!StringUtils.isEmpty(maxWaistStr)&& !maxWaistStr.equals("-1")) && (StringUtils.isEmpty(minWaistStr)||minWaistStr.equals("-1"))){
            waistStr="小于"+maxWaistStr+"cm";
            isShowWaist = true;
        }else if ((!StringUtils.isEmpty(maxWaistStr)&& !maxWaistStr.equals("-1")) && (!StringUtils.isEmpty(minWaistStr)&&!minWaistStr.equals("-1"))){
            waistStr=minWaistStr+"-"+maxWaistStr+"cm";
            isShowWaist = true;
        }else if ((StringUtils.isEmpty(maxWaistStr)|| maxWaistStr.equals("-1"))&&(StringUtils.isEmpty(minWaistStr)||minWaistStr.equals("-1"))){
            isShowWaist = false;
        }
        if (isShowWaist){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",waistStr);
            map.put("imgId",R.drawable.sg_pl_waist+"");
            bassicRequestList.add(map);
        }
        //臀围
        String hipStr = "";
        boolean isShowHip = false;
        String minHipStr = noticeRecruitListVo.getMinHip()+"";
        String maxHipStr = noticeRecruitListVo.getMaxHip() +"";
        if ((!StringUtils.isEmpty(minHipStr)&& !minHipStr.equals("-1"))&&(StringUtils.isEmpty(maxHipStr)||maxHipStr.equals("-1"))){
            hipStr="大于"+minHipStr+"cm";
            isShowHip = true;
        } else if ((!StringUtils.isEmpty(maxHipStr)&& !maxHipStr.equals("-1")) && (StringUtils.isEmpty(minHipStr)||minHipStr.equals("-1"))){
            hipStr="小于"+maxHipStr+"cm";
            isShowHip = true;
        }else if ((!StringUtils.isEmpty(maxHipStr)&& !maxHipStr.equals("-1")) && (!StringUtils.isEmpty(minHipStr)&&!minHipStr.equals("-1"))){
            hipStr=minHipStr+"-"+maxHipStr+"cm";
            isShowHip = true;
        }else if ((StringUtils.isEmpty(maxHipStr)|| maxHipStr.equals("-1"))&&(StringUtils.isEmpty(minHipStr)||minHipStr.equals("-1"))){
            isShowHip = false;
        }
        if (isShowHip){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",hipStr);
            map.put("imgId",R.drawable.sg_pl_hip+"");
            bassicRequestList.add(map);
        }

        //罩杯
        String braStr = "";
        boolean isShowBra = false;
        String[] braArr = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J","K"};
        String minBraStr = noticeRecruitListVo.getMinCup()+"";
        String maxBraStr = noticeRecruitListVo.getMaxCup()+"";
        if ((!StringUtils.isEmpty(minBraStr)&& !minBraStr.equals("-1")&& !minBraStr.equals("0"))&&(StringUtils.isEmpty(maxBraStr)||maxBraStr.equals("-1")||maxBraStr.equals("0"))){
            String minBraTempStr = braArr[noticeRecruitListVo.getMinCup()-1];
            braStr="大于"+minBraTempStr;
            isShowBra = true;
        } else if ((!StringUtils.isEmpty(maxBraStr)&& !maxBraStr.equals("-1")&& !maxBraStr.equals("0")) && (StringUtils.isEmpty(minBraStr)||minBraStr.equals("-1")||minBraStr.equals("0"))){
            String maxBraTempStr = braArr[noticeRecruitListVo.getMaxCup()-1];
            braStr="小于"+maxBraTempStr;
            isShowBra = true;
        }else if ((!StringUtils.isEmpty(maxBraStr)&& !maxBraStr.equals("-1")&& !maxBraStr.equals("0")) && (!StringUtils.isEmpty(minBraStr)&&!minBraStr.equals("-1")&&!minBraStr.equals("0"))){
            String minBraTempStr = braArr[noticeRecruitListVo.getMinCup()-1];
            String maxBraTempStr = braArr[noticeRecruitListVo.getMaxCup()-1];
            braStr=minBraTempStr+"-"+maxBraTempStr;
            isShowBra = true;
        }else if ((StringUtils.isEmpty(maxBraStr)|| maxBraStr.equals("-1")|| maxBraStr.equals("0"))&&(StringUtils.isEmpty(minBraStr)||minBraStr.equals("-1")||minBraStr.equals("0"))){
            isShowBra = false;
        }
        if (isShowBra){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",braStr);
            map.put("imgId",R.drawable.sg_pl_bra+"");
            bassicRequestList.add(map);
        }

        //鞋码
        String footStr = "";
        boolean isShowFoot = false;
        String minFootStr = noticeRecruitListVo.getMinShoescode()+"";
        String maxFootStr = noticeRecruitListVo.getMaxShoescode() +"";
        if ((!StringUtils.isEmpty(minFootStr)&& !minFootStr.equals("-1"))&&(StringUtils.isEmpty(maxFootStr)||maxFootStr.equals("-1"))){
            footStr="大于"+minFootStr+"码";
            isShowFoot = true;
        } else if ((!StringUtils.isEmpty(maxFootStr)&& !maxFootStr.equals("-1")) && (StringUtils.isEmpty(minFootStr)||minFootStr.equals("-1"))){
            footStr="小于"+maxFootStr+"码";
            isShowFoot = true;
        }else if ((!StringUtils.isEmpty(maxFootStr)&& !maxFootStr.equals("-1")) && (!StringUtils.isEmpty(minFootStr)&&!minFootStr.equals("-1"))){
            footStr=minFootStr+"-"+maxFootStr+"码";
            isShowFoot = true;
        }else if ((StringUtils.isEmpty(maxFootStr)|| maxFootStr.equals("-1"))&&(StringUtils.isEmpty(minFootStr)||minFootStr.equals("-1"))){
            isShowFoot = false;
        }
        if (isShowFoot){
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",footStr);
            map.put("imgId",R.drawable.sg_pl_foot+"");
            bassicRequestList.add(map);
        }
        return  bassicRequestList;
    }

    public void setReportStatus(boolean b) {

    }
}

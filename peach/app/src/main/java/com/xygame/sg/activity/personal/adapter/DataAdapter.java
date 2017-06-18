package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import base.action.CenterRepo;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.personal.CameraBigTypeForPerActivity;
import com.xygame.sg.activity.personal.CommentAllActivity;
import com.xygame.sg.activity.personal.EditNickNameActivity;
import com.xygame.sg.activity.personal.EditRecordActivity;
import com.xygame.sg.activity.personal.EditSummaryActivity;
import com.xygame.sg.activity.personal.EditorBirthdayActivity;
import com.xygame.sg.activity.personal.EditorBodyInfoActivity;
import com.xygame.sg.activity.personal.EditorCountryActivity;
import com.xygame.sg.activity.personal.EditorProvinceActivity;
import com.xygame.sg.activity.personal.EditorStyleActivity;
import com.xygame.sg.activity.personal.STSEditRecordActivity;
import com.xygame.sg.activity.personal.SelectJobTypeActivity;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.ModelResumeVo;
import com.xygame.sg.activity.personal.bean.ModelStyleVo;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.activity.personal.bean.TempTypeBean;
import com.xygame.sg.activity.personal.bean.UserBody;
import com.xygame.sg.activity.personal.bean.UserInfoView;
import com.xygame.sg.activity.personal.bean.UserType;
import com.xygame.sg.bean.comm.TransResumeBean;
import com.xygame.sg.bean.comm.TransStyleBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/16. 资料页面的adapter
 */
public class DataAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private UserInfoView dataMap;
    private TransStyleBean tsBean;
    private LayoutParams lp;
    private ModelDataVo modelDataVo ;
    private String countryName;
    private TransResumeBean tsResumeBean;
    private String userHeight = null, userWeight = null, userCup = null, userBust = null, userWaist = null,
            userHip = null, userShoesCode = null;
    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;

    public void addModelDataVo(ModelDataVo modelDataVo){
        this.modelDataVo=modelDataVo;
    }

    public DataAdapter(Context context, UserInfoView dataMap, boolean isQuery) {
        super();
        this.context = context;
        this.dataMap = dataMap;
        this.isQuery = isQuery;
        inflater = LayoutInflater.from(context);
        tsBean = new TransStyleBean();
        lp = new LayoutParams(20, 20);
    }

    public void setData(UserInfoView dataMap) {
        this.dataMap = dataMap;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return dataMap;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.person_data_item, null);

        TextView body_info_tip_tv = (TextView) view.findViewById(R.id.body_info_tip_tv);
        TextView record_tip_tv = (TextView) view.findViewById(R.id.record_tip_tv);
        TextView summary_tip_tv = (TextView) view.findViewById(R.id.summary_tip_tv);
        TextView style_tip_tv = (TextView) view.findViewById(R.id.style_tip_tv);
        // ImageView record_arrow_iv = (ImageView)
        // view.findViewById(R.id.record_arrow_iv);
        ImageView summary_arrow_iv = (ImageView) view.findViewById(R.id.summary_arrow_iv);
        ImageView style_arrow_iv = (ImageView) view.findViewById(R.id.style_arrow_iv);
        ImageView record_rl_arrow_iv = (ImageView) view.findViewById(R.id.record_rl_arrow_iv);

        ImageView nick_name_iv = (ImageView) view.findViewById(R.id.nick_name_iv);
        ImageView age_iv = (ImageView) view.findViewById(R.id.age_iv);
        ImageView country_iv = (ImageView) view.findViewById(R.id.country_iv);
        ImageView area_iv = (ImageView) view.findViewById(R.id.area_iv);
        ImageView job_type_iv = (ImageView) view.findViewById(R.id.job_type_iv);
        ImageView paishe_style_rl_icon=(ImageView)view.findViewById(R.id.paishe_style_rl_icon);

        //评价部分
        RelativeLayout trade_num_rl = (RelativeLayout) view.findViewById(R.id.trade_num_rl);
        RatingBar score_rating = (RatingBar) view.findViewById(R.id.score_rating);
        TextView trade_num_tv = (TextView) view.findViewById(R.id.trade_num_tv);
        TextView comment_num_tv = (TextView) view.findViewById(R.id.comment_num_tv);
        if (modelDataVo!=null){
            trade_num_tv.setText(" ("+modelDataVo.getScore().getTradeCount()+")");
            comment_num_tv.setText(" ("+modelDataVo.getScore().getEvlCount()+")");
            score_rating.setRating((float)modelDataVo.getScore().getEvlScore());
            trade_num_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentAllActivity.class);
                    intent.putExtra("seeUserId",modelDataVo.getSeeUserId()+"");
                    List<UserType> utypesList = modelDataVo.getUserTypes();
                    UserType utypes = new UserType();
                    if (utypesList.size() > 1) {
                        utypes = utypesList.get(1);
                    } else {
                        utypes = utypesList.get(0);
                    }
                    String userType = utypes.getUtype()+"";
                    intent.putExtra("utype",userType);
                    context.startActivity(intent);
                }
            });
        }

        if (isQuery) {
            body_info_tip_tv.setText("形体资料");
            record_tip_tv.setText("履历");
            summary_tip_tv.setText("简介");
            style_tip_tv.setText("风格");

            // record_arrow_iv.setVisibility(View.GONE);
            summary_arrow_iv.setVisibility(View.GONE);
            style_arrow_iv.setVisibility(View.GONE);
            nick_name_iv.setVisibility(View.GONE);
            age_iv.setVisibility(View.GONE);
            country_iv.setVisibility(View.GONE);
            area_iv.setVisibility(View.GONE);
            job_type_iv.setVisibility(View.GONE);
        }

        LinearLayout body_info_ll = (LinearLayout) view.findViewById(R.id.body_info_ll);
        TextView height_tv = (TextView) view.findViewById(R.id.height_tv);
        TextView weight_tv = (TextView) view.findViewById(R.id.weight_tv);
        TextView bra_tv = (TextView) view.findViewById(R.id.bra_tv);
        TextView bwh_tv = (TextView) view.findViewById(R.id.bwh_tv);
        TextView foot_tv = (TextView) view.findViewById(R.id.foot_tv);
        String[] scope = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        UserBody userBody = dataMap.getUserBody();
        if (userBody != null) {
            String height = userBody.getHeight()+"";
            if (!TextUtils.isEmpty(height) && !"null".equals(height)) {
                height_tv.setText(height.concat("CM"));
                userHeight = height;
            }
            String weight = userBody.getWeight()+"";
            if (!TextUtils.isEmpty(weight) && !"null".equals(weight)) {
                weight_tv.setText(weight.concat("KG"));
                userWeight = weight;
            }
            String cup = userBody.getCup()+"";
            if (!TextUtils.isEmpty(cup) && !"null".equals(cup) && !"0".equals(cup)) {
                userCup = cup;
                bra_tv.setText(scope[Integer.parseInt(userCup) - 1]);
            }
            String bust = userBody.getBust()+"";
            String waist = userBody.getWaist()+"";
            String hip = userBody.getHip()+"";
            if (!TextUtils.isEmpty(bust) && !"null".equals(bust) && !TextUtils.isEmpty(waist)
                    && !"null".equals(waist) && !TextUtils.isEmpty(hip) && !"null".equals(hip)) {
                bwh_tv.setText(bust + "-" + waist + "-" + hip);
                userBust = bust;
                userWaist = waist;
                userHip = hip;
            }
            String shoesCode = userBody.getShoesCode()+"";
            if (!TextUtils.isEmpty(shoesCode) && !"null".equals(shoesCode)) {
                foot_tv.setText(shoesCode.concat("码"));
                userShoesCode = shoesCode;
            }
        }

        List<ModelResumeVo> resumeList = dataMap.getResumes();

        if (!isQuery) {
            body_info_ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditorBodyInfoActivity.class);
                    intent.putExtra("userHeight", userHeight);
                    intent.putExtra("userWeight", userWeight);
                    intent.putExtra("userCup", userCup);
                    intent.putExtra("userBust", userBust);
                    intent.putExtra("userWaist", userWaist);
                    intent.putExtra("userHip", userHip);
                    intent.putExtra("userShoesCode", userShoesCode);
                    context.startActivity(intent);
                }
            });
        }

        LinearLayout record_rl = (LinearLayout) view.findViewById(R.id.record_rl);

        if (resumeList != null && !resumeList.isEmpty()) {
            if (isQuery) {
                record_rl_arrow_iv.setVisibility(View.GONE);
            } else {
                record_rl_arrow_iv.setVisibility(View.VISIBLE);
            }
            tsResumeBean = new TransResumeBean();
            tsResumeBean.setResumeList(resumeList);
            for (int j = 0; j < resumeList.size(); j++) {
//                Map map = resumeList.get(j);
                ModelResumeVo map = resumeList.get(j);
                View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
                View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
                record_arrow_iv.setVisibility(View.GONE);
                TextView resumeText = (TextView) mView.findViewById(R.id.resumeText);
                TextView resumeTimer = (TextView) mView.findViewById(R.id.resumeTimer);
                View bottomLineView = mView.findViewById(R.id.bottomLineView);
                bottomLineView.setVisibility(View.GONE);
                String startTime = "", endTime = "";
                if (map.getStartDate() != null) {
                    startTime = CalendarUtils.getXieGongYMDStr(map.getStartDate());
                }
                if (map.getEndDate() != null) {
                    endTime = CalendarUtils.getXieGongYMDStr(map.getEndDate());
                }
                String timer = startTime.concat("—").concat(endTime);
                String context = map.getExperDesc();
                resumeTimer.setText(timer);
                resumeText.setText(context);
                record_rl.addView(mView);
            }

        } else {
            if (isQuery) {
                record_rl_arrow_iv.setVisibility(View.GONE);
                View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
                TextView resumeTimer = (TextView) mView.findViewById(R.id.resumeTimer);
                View bottomLineView = mView.findViewById(R.id.bottomLineView);
                View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
                record_arrow_iv.setVisibility(View.GONE);
                bottomLineView.setVisibility(View.GONE);
                resumeTimer.setHint("暂无履历");
                record_rl.addView(mView);
            } else {
                record_rl_arrow_iv.setVisibility(View.GONE);
                View mView = LayoutInflater.from(context).inflate(R.layout.sg_resume_item, null);
                TextView resumeText = (TextView) mView.findViewById(R.id.resumeText);
                View bottomLineView = mView.findViewById(R.id.bottomLineView);
                View record_arrow_iv = mView.findViewById(R.id.record_arrow_iv);
                record_arrow_iv.setVisibility(View.VISIBLE);
                bottomLineView.setVisibility(View.GONE);
//				resumeText.setHint("暂无履历，请添加！");
                record_rl.addView(mView);
            }
        }

        if (!isQuery) {
            record_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, STSEditRecordActivity.class);
                    intent.putExtra("data", tsResumeBean);
                    context.startActivity(intent);
                }
            });
        }

        TextView summary_tv = (TextView) view.findViewById(R.id.summary_tv);
        final String summary = dataMap.getIntroDesc();
        if (!TextUtils.isEmpty(summary) && !summary.equals("null")) {
            summary_tv.setText(summary);
        } else {
            if (isQuery) {
                summary_tv.setHint("暂无简介");
            }
        }
        RelativeLayout summary_rl = (RelativeLayout) view.findViewById(R.id.summary_rl);

        View paishe_style_rl=view.findViewById(R.id.paishe_style_rl);
        TextView paishe_style_rl_tv=(TextView)view.findViewById(R.id.paishe_style_rl_tv);

        List<Integer> shootTypes=dataMap.getShootTypes();
        List<ShootTypeBean> dataList=SGApplication.getInstance().getTypeList();
       final List<ShootTypeBean> dateList=new ArrayList<>();
        if (shootTypes!=null){
            if (dataList!=null){
                for (ShootTypeBean it:dataList){
                    for (int typeId:shootTypes){
                        if (typeId==it.getTypeId()){
                            dateList.add(it);
                            break;
                        }
                    }
                }
            }
        }
        if (dateList.size()>0) {
            StringBuilder paiSheName=new StringBuilder();
            for (ShootTypeBean item:dateList){
                paiSheName.append(item.getTypeName().concat(" "));
            }
            paishe_style_rl_tv.setText(paiSheName.toString());
        }else{
            paishe_style_rl_tv.setHint("请选择你的拍摄类型");
        }

        if (!isQuery) {
            paishe_style_rl_icon.setVisibility(View.VISIBLE);
            paishe_style_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TempTypeBean bean=new TempTypeBean();
                    bean.setDateList(dateList);
                    Intent intent = new Intent(context, CameraBigTypeForPerActivity.class);
                    intent.putExtra("bean", bean);
                    context.startActivity(intent);
                }
            });
        }else{
            paishe_style_rl_icon.setVisibility(View.GONE);
        }

        if (!isQuery) {
            summary_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditSummaryActivity.class);
                    intent.putExtra("summary", summary);
                    context.startActivity(intent);
                }
            });
        }
        TextView selet_style_label_tv = (TextView) view.findViewById(R.id.selet_style_label_tv);
        List<ModelStyleVo> styleList =  dataMap.getStyles();
        tsBean.setStyleList(styleList);
        if (styleList != null && !styleList.isEmpty()) {
            selet_style_label_tv.setVisibility(View.GONE);
            LinearLayout style_ll = (LinearLayout) view.findViewById(R.id.style_ll);
            for (ModelStyleVo it : styleList) {
                View convertView = LayoutInflater.from(context).inflate(R.layout.sg_sytle_item_, null);
                RoundTextView styleText = (RoundTextView) convertView.findViewById(R.id.styleText);
                styleText.setText(it.getStyleName());
                styleText.setTextColor(Color.rgb(it.getHueR(), it.getHueG(), it.getHueB()));
                styleText.getDelegate().setStrokeColor(Color.rgb(it.getHueR(), it.getHueG(), it.getHueB()));
                View mView = new View(context);
                mView.setLayoutParams(lp);
                style_ll.addView(convertView);
                style_ll.addView(mView);
            }
        } else {
            if (isQuery) {
                selet_style_label_tv.setHint("暂未选风格");
            }
        }

        View style_rl = view.findViewById(R.id.style_rl);

        if (!isQuery) {
            style_rl.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(context, EditorStyleActivity.class);
                    intent.putExtra("bean", tsBean);
                    context.startActivity(intent);
                }
            });
        }

        TextView nickname_tv = (TextView) view.findViewById(R.id.nickname_tv);
        TextView mofan_num_tv = (TextView) view.findViewById(R.id.mofan_num_tv);
        TextView sex_tv = (TextView) view.findViewById(R.id.sex_tv);
        TextView age_tv = (TextView) view.findViewById(R.id.age_tv);
        TextView country_tv = (TextView) view.findViewById(R.id.country_tv);
        TextView area_tv = (TextView) view.findViewById(R.id.area_tv);
        TextView job_type_tv = (TextView) view.findViewById(R.id.job_type_tv);

        String usernick = dataMap.getUsernick();
        if (!TextUtils.isEmpty(usernick) && !usernick.equals("null")) {
            nickname_tv.setText(usernick);
        }
        String mofan_num = dataMap.getUserPin();
        if (!TextUtils.isEmpty(mofan_num) && !mofan_num.equals("null")) {
            mofan_num_tv.setText(mofan_num);
        }
        String sex = dataMap.getGender()+"";
        if (!TextUtils.isEmpty(sex) && !sex.equals("null")) {

            if (sex.equals(Constants.SEX_MAN)) {
                sex_tv.setText("男");
            } else {
                sex_tv.setText("女");
            }
        }
        String age = dataMap.getAge()+"";
        if (!TextUtils.isEmpty(age) && !age.equals("null")) {
            age_tv.setText(age.concat("岁"));
        }
        String country = dataMap.getCountry();
        if (!TextUtils.isEmpty(country) && !country.equals("null")) {
            List<Map<String, String>> datas = Constants.COUNTRY_DATA;
            for (Map<String, String> it : datas) {
                if (it.get("country_code").equals(country)) {
                    countryName = it.get("country_name");
                    country_tv.setText(countryName);
                }
            }
        }
        String area = dataMap.getCity()+"";
        if (!TextUtils.isEmpty(area) && !area.equals("null")) {
            CityBean it = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(area));
            area_tv.setText(it.getName());
        }
        String job_type = dataMap.getOccupType()+"";
        List<CarrierBean> datas = SGApplication.getInstance().getModelCarriers();
        if (!"null".equals(datas) && datas != null) {
            if (!TextUtils.isEmpty(job_type) && !job_type.equals("null")) {
                for (CarrierBean it : datas) {
                    if (it.getTypeId().equals(job_type)) {
                        job_type_tv.setText(it.getCarrierName());
                    }
                }
            }
        }

        RelativeLayout nick_name_rl = (RelativeLayout) view.findViewById(R.id.nick_name_rl);
        RelativeLayout job_type_rl = (RelativeLayout) view.findViewById(R.id.job_type_rl);
        View userinfoAge = view.findViewById(R.id.userinfoAge);
        View userInfoCountry = view.findViewById(R.id.userInfoCountry);
        View userInfoCity = view.findViewById(R.id.userInfoCity);

        if (!isQuery) {
            nick_name_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditNickNameActivity.class);
                    context.startActivity(intent);
                }
            });
            job_type_rl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SelectJobTypeActivity.class);
                    intent.putExtra("strFlag", dataMap.getOccupType()+"");
                    context.startActivity(intent);
                }
            });
            userinfoAge.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditorBirthdayActivity.class);
                    context.startActivity(intent);
                }
            });
            userInfoCountry.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditorCountryActivity.class);
                    intent.putExtra("data", countryName);
                    context.startActivity(intent);
                }
            });
            userInfoCity.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditorProvinceActivity.class);
                    intent.putExtra("noLimitFlag", true);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }
}

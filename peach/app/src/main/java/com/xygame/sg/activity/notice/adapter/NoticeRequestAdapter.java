package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.NoticeRecruitListVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/12/12.
 */
public class NoticeRequestAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private boolean isExpand = false;
    private List<NoticeRecruitListVo> recruitList;
    public NoticeRequestAdapter(Context context,List<NoticeRecruitListVo> recruitList,boolean isExpand) {
        super();
        this.context = context;
        this.recruitList = recruitList;
        this.isExpand = isExpand;
        inflater = LayoutInflater.from(context);
    }
    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return recruitList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notice_list_request_item, null);
            holder = new ViewHolder();
//            holder.collapse_ll = (LinearLayout) convertView.findViewById(R.id.collapse_ll);
//            holder.sex_iv = (ImageView) convertView.findViewById(R.id.sex_iv);
//            holder.sex_tv = (TextView) convertView.findViewById(R.id.sex_tv);
//            holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
//            holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
//            holder.city_tv = (TextView) convertView.findViewById(R.id.city_tv);
//            holder.country_tv = (TextView) convertView.findViewById(R.id.country_tv);
//            holder.age_tv = (TextView) convertView.findViewById(R.id.age_tv);
//            holder.height_tv = (TextView) convertView.findViewById(R.id.height_tv);
//            holder.travel_tv = (TextView) convertView.findViewById(R.id.travel_tv);
//            holder.hotel_tv = (TextView) convertView.findViewById(R.id.hotel_tv);
//            holder.divider_line = convertView.findViewById(R.id.divider_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (isExpand){
//            holder.collapse_ll.setVisibility(View.VISIBLE);
//        }
//
//        if (i==(getCount()-1)){
//            holder.divider_line.setVisibility(View.GONE);
//        }
//
//        NoticeRecruitListVo noticeRecruitListVo = recruitList.get(i);
//        if (noticeRecruitListVo.getGender()==0 ){//女
//            holder.sex_iv.setImageResource(R.drawable.sg_pl_woman);
//            holder.sex_tv.setText("女");
//        } else {
//            holder.sex_iv.setImageResource(R.drawable.sg_pl_man);
//            holder.sex_tv.setText("男");
//        }
//        holder.num_tv.setText(noticeRecruitListVo.getCount()+"");
//        holder.price_tv.setText("￥"+noticeRecruitListVo.getReward()/100);
//
//        if (noticeRecruitListVo.getAffordTravelFee() == 1){
//            holder.travel_tv.setText("报销异地差旅费");
//        } else {
//            holder.travel_tv.setText("不报销异地差旅费");
//        }
//
//        if (noticeRecruitListVo.getAffordAccomFee() == 1){
//            holder.hotel_tv.setText("报销异地住宿费");
//        } else {
//            holder.hotel_tv.setText("不报销异地住宿费");
//        }
//
//        String country = noticeRecruitListVo.getCountry();
//        if (!StringUtils.isEmpty(country)){
//            List<Map<String, String>> datas = Constants.COUNTRY_DATA;
//            for (Map<String, String> it : datas) {
//                if (it.get("country_code").equals(country)) {
//                    String countryName = it.get("country_name");
//                    holder.country_tv.setText(countryName);
//                    break;
//                }
//            }
//        } else {
//            holder.country_tv.setText("不限");
//        }
//
//        String cityStr = noticeRecruitListVo.getCity()+"";
//        if (!StringUtils.isEmpty(cityStr)){
//            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(noticeRecruitListVo.getCity());
//            holder.city_tv.setText(it.getName());
//        } else {
//            holder.city_tv.setText("不限");
//        }
//
//        String ageStr = "";
//        String minAgeStr = noticeRecruitListVo.getMinAge()+"";
//        if (!StringUtils.isEmpty(minAgeStr)&& !minAgeStr.equals("-1")){
//            ageStr+=minAgeStr+"-";
//        } else {
//            ageStr+="不限"+"-";
//        }
//
//        String maxAgeStr = noticeRecruitListVo.getMaxAge()+"";
//        if (!StringUtils.isEmpty(maxAgeStr)&& !maxAgeStr.equals("-1")){
//            ageStr+=maxAgeStr;
//        } else {
//            ageStr+="不限";
//        }
//        holder.age_tv.setText(ageStr);
//        String heightStr = "";
//        String minHeightStr = noticeRecruitListVo.getMinHeight()+"";
//        if (!StringUtils.isEmpty(minHeightStr)&& !minHeightStr.equals("-1")){
//            heightStr+=minHeightStr+"-";
//        } else {
//            heightStr+="不限"+"-";
//        }
//
//        String maxHeightStr = noticeRecruitListVo.getMaxHeight()+"";
//        if (!StringUtils.isEmpty(maxHeightStr)&& !maxHeightStr.equals("-1")){
//            heightStr+=maxHeightStr;
//        } else {
//            heightStr+="不限";
//        }
//        holder.height_tv.setText(heightStr);


        return convertView;
    }

    private class ViewHolder {
        private LinearLayout collapse_ll;
        private ImageView sex_iv;
        private TextView sex_tv;
        private TextView num_tv;
        private TextView price_tv;
        private TextView city_tv;
        private TextView country_tv;
        private TextView age_tv;
        private TextView height_tv;
        private TextView travel_tv;
        private TextView hotel_tv;
        private View divider_line;
    }
}

package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.bean.NoticeRecruitListVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.LinearLayoutForListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/12/12.
 */
public class NoticeDetailRequestAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private boolean isExpand = false;
    private List<NoticeRecruitVo> recruitList;
    private long publishUserId;
    public NoticeDetailRequestAdapter(Context context, List<NoticeRecruitVo> recruitList,long publishUserId, boolean isExpand) {
        super();
        this.context = context;
        this.recruitList = recruitList;
        this.isExpand = isExpand;
        this.publishUserId = publishUserId;
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
            convertView = inflater.inflate(R.layout.notice_detail_list_request_item, null);
            holder = new ViewHolder();
            holder.collapse_ll = (LinearLayout) convertView.findViewById(R.id.collapse_ll);
            holder.sex_iv = (ImageView) convertView.findViewById(R.id.sex_iv);
            holder.sex_tv = (TextView) convertView.findViewById(R.id.sex_tv);
            holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
            holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
            holder.basic_request_ll = (LinearLayout) convertView.findViewById(R.id.basic_request_ll);

            holder.travel_hotel_ll = (LinearLayout) convertView.findViewById(R.id.travel_hotel_ll);
            holder.travel_iv = (ImageView) convertView.findViewById(R.id.travel_iv);
            holder.travel_tv = (TextView) convertView.findViewById(R.id.travel_tv);
            holder.hotel_iv = (ImageView) convertView.findViewById(R.id.hotel_iv);
            holder.hotel_tv = (TextView) convertView.findViewById(R.id.hotel_tv);
            holder.request_message_ll = (LinearLayout) convertView.findViewById(R.id.request_message_ll);
            holder.request_message_tv = (TextView) convertView.findViewById(R.id.request_message_tv);

            holder.enroll_rl = (RelativeLayout) convertView.findViewById(R.id.enroll_rl);
            holder.query_rl = (RelativeLayout) convertView.findViewById(R.id.query_rl);
            holder.applySum_tv = (TextView) convertView.findViewById(R.id.applySum_tv);
            holder.encroll_tv = (TextView) convertView.findViewById(R.id.encroll_tv);
            holder.query_applySum_tv = (TextView) convertView.findViewById(R.id.query_applySum_tv);
            holder.okSum_tv = (TextView) convertView.findViewById(R.id.okSum_tv);
            holder.query_tv = (TextView) convertView.findViewById(R.id.query_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isExpand){
            holder.collapse_ll.setVisibility(View.VISIBLE);
        }


        NoticeRecruitVo noticeRecruitListVo = recruitList.get(i);
        NoticeRecruitListVo noticeRecruitListBean = new NoticeRecruitListVo();
        if (noticeRecruitListVo.getGender()==0 ){//女
            holder.sex_iv.setImageResource(R.drawable.sg_pl_woman);
            holder.sex_tv.setText("女");
        } else {
            holder.sex_iv.setImageResource(R.drawable.sg_pl_man);
            holder.sex_tv.setText("男");
        }
        holder.num_tv.setText(noticeRecruitListVo.getCount()+"人");
        holder.price_tv.setText("￥"+noticeRecruitListVo.getReward()/100);

        List<Map<String,String>> bassicRequestList = new ArrayList<Map<String,String>>();


        bassicRequestList = LinearLayoutForListView.getData(noticeRecruitListVo);

        int all = bassicRequestList.size();
        int oneLineItem = 3;
        int numLine = all%oneLineItem==0 ? all/oneLineItem : all/oneLineItem+1;
        int innerLoop = 0;
        for (int j = 0 ;j< numLine;j++){
            LinearLayout item_ll = new LinearLayout(context);
            item_ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, DataTools.dip2px(context, 20),0,0);
            item_ll.setLayoutParams(params);
            for (;innerLoop<all;innerLoop++){
                LinearLayout sub_itm_ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.notice_request_sub_item,null);
                ImageView item_iv = (ImageView) sub_itm_ll.findViewById(R.id.item_iv);
                TextView item_tv = (TextView) sub_itm_ll.findViewById(R.id.item_tv);
                item_iv.setImageResource(Integer.parseInt(bassicRequestList.get(innerLoop).get("imgId")));
                item_tv.setText(bassicRequestList.get(innerLoop).get("name"));

                if (innerLoop % oneLineItem != 0 ){
                    LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    subParams.setMargins(DataTools.dip2px(context,15),0,0,0);
                    sub_itm_ll.setLayoutParams(subParams);
                }
                item_ll.addView(sub_itm_ll);
                if (innerLoop % oneLineItem ==(oneLineItem -1) ){
                    innerLoop++;
                    break;
                }
            }

            holder.basic_request_ll.addView(item_ll);
        }



        if (noticeRecruitListVo.getAffordTravelFee() == 1){
            holder.travel_tv.setText("报销异地差旅费");
        } else {
            holder.travel_tv.setText("不报销异地差旅费");
        }

        if (noticeRecruitListVo.getAffordAccomFee() == 1){
            holder.hotel_tv.setText("报销异地住宿费");
        } else {
            holder.hotel_tv.setText("不报销异地住宿费");
        }

        if (noticeRecruitListVo.getAffordTravelFee() == 1){
            holder.travel_tv.setText("报销异地差旅费");
            holder.travel_tv.setVisibility(View.VISIBLE);
            holder.travel_iv.setVisibility(View.VISIBLE);
        } else {
            holder.travel_tv.setText("不报销异地差旅费");
            holder.travel_tv.setVisibility(View.GONE);
            holder.travel_iv.setVisibility(View.GONE);
        }

        if (noticeRecruitListVo.getAffordAccomFee() == 1){
            holder.hotel_tv.setText("报销异地住宿费");
            holder.hotel_tv.setVisibility(View.VISIBLE);
            holder.hotel_iv.setVisibility(View.VISIBLE);
        } else {
            holder.hotel_tv.setText("不报销异地住宿费");
            holder.hotel_tv.setVisibility(View.GONE);
            holder.hotel_iv.setVisibility(View.GONE);
        }

        if (holder.travel_tv.getVisibility() == View.GONE && holder.hotel_tv.getVisibility() == View.GONE ){
            holder.travel_hotel_ll.setVisibility(View.GONE);
        } else {
            holder.travel_hotel_ll.setVisibility(View.VISIBLE);
        }

        String messageStr = noticeRecruitListVo.getRemark();
        if (!StringUtils.isEmpty(messageStr)){
            holder.request_message_ll.setVisibility(View.VISIBLE);
            holder.request_message_tv.setText(messageStr);
        } else {
            holder.request_message_ll.setVisibility(View.GONE);
        }




        if (UserPreferencesUtil.isOnline(context)){
            if (UserPreferencesUtil.getUserId(context).equals(publishUserId + "")){
                holder.query_rl.setVisibility(View.VISIBLE);
                holder.enroll_rl.setVisibility(View.GONE);
                holder.query_applySum_tv.setText(noticeRecruitListVo.getApplySum()+"");
                holder.okSum_tv.setText(noticeRecruitListVo.getOkSum()+"");
            } else{
                holder.query_rl.setVisibility(View.GONE);
                holder.enroll_rl.setVisibility(View.VISIBLE);
                holder.applySum_tv.setText(noticeRecruitListVo.getApplySum()+"");
            }
        } else {
            holder.query_rl.setVisibility(View.GONE);
            holder.enroll_rl.setVisibility(View.VISIBLE);
            holder.applySum_tv.setText(noticeRecruitListVo.getApplySum() + "");
        }

        //是否已报名：1：报名；0：未报名
        if (noticeRecruitListVo.getApplied()==1){
            holder.encroll_tv.setText("已报名");
            holder.encroll_tv.setEnabled(false);
        } else {
            holder.encroll_tv.setText("我要报名");
            holder.encroll_tv.setEnabled(true);
        }

        if (context instanceof NoticeDetailActivity){
            holder.enroll_rl.setOnClickListener(((NoticeDetailActivity)context).new EnrollItemClickListener(recruitList,i));
            holder.query_rl.setOnClickListener(((NoticeDetailActivity) context).new QueryItemClickListener(recruitList,i));
        }

        return convertView;
    }

    private class ViewHolder {
        private LinearLayout collapse_ll;
        private ImageView sex_iv;
        private TextView sex_tv;
        private TextView num_tv;
        private TextView price_tv;
        private LinearLayout basic_request_ll;
//        private TextView city_tv;
//        private TextView country_tv;
//        private TextView age_tv;
//        private TextView height_tv;
        private LinearLayout travel_hotel_ll;
        private ImageView travel_iv;
        private TextView travel_tv;
        private ImageView hotel_iv;
        private TextView hotel_tv;
        private LinearLayout request_message_ll;
        private TextView request_message_tv;

        private RelativeLayout enroll_rl;
        private RelativeLayout query_rl;
        private TextView applySum_tv;
        private TextView encroll_tv;
        private TextView query_applySum_tv;
        private TextView okSum_tv;
        private TextView query_tv;
    }
}

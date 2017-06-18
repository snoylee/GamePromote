package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.bean.UserSeeHistoryView;
import com.xygame.sg.activity.personal.bean.UserSeeHistoryVo;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class RecentModelAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader;
    private UserSeeHistoryView seeHistoryView;
    private List<UserSeeHistoryVo> historys = new ArrayList<UserSeeHistoryVo>();


    public RecentModelAdapter(Context context, UserSeeHistoryView seeHistoryView) {
        super();
        this.context = context;
        this.seeHistoryView = seeHistoryView;
        historys = this.seeHistoryView.getHistorys();

        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }


    @Override
    public int getCount() {
        return historys.size();
    }

    @Override
    public UserSeeHistoryVo getItem(int i) {
        return historys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recent_model_item, null);
            holder = new ViewHolder();
            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.sex_age_tv = (TextView) convertView.findViewById(R.id.sex_age_tv);
            holder.type_icon_tv = (TextView) convertView.findViewById(R.id.type_icon_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.visit_time_tv = (TextView) convertView.findViewById(R.id.visit_time_tv);
            holder.new_iv = (ImageView) convertView.findViewById(R.id.new_iv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final UserSeeHistoryVo recent = historys.get(i);

        holder.nick_name_tv.setText(recent.getUsernick());
        String userIcon = recent.getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.avatar_iv, false);
        }


        holder.sex_age_tv.setText(recent.getAge()+"");
        String sexStr = recent.getGender()+"";
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            Drawable femaleDrawable = context.getResources().getDrawable(R.drawable.female);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            Drawable maleDrawable = context.getResources().getDrawable(R.drawable.male);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
        }

        String typeStr = recent.getUserType()+"";
        if (typeStr.equals(Constants.CARRE_MODEL) && recent.getAuthStatus() == 2){//用户类型：2：模特；4：高级模特；0：未认证的模特（没有认证数据）
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_bg);
        } else if(typeStr.equals(Constants.PRO_MODEL) && recent.getAuthStatus() == 2){
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_bg);
//            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_pro_identy_icon);
//            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
//            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_pro_type_icon_bg);
//            holder.type_tv.setText("高级模特");
//            holder.type_tv.setBackgroundResource(R.drawable.identy_pro_type_bg);
        }else if(typeStr.equals("0")){
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
        }

        Date visitTime = recent.getVisitTime();
        Long lastReadTime = seeHistoryView.getLastReadTime();
        Long reqTime = seeHistoryView.getReqTime();
        if((lastReadTime!=null&&visitTime.getTime()>lastReadTime||lastReadTime==null)&&visitTime.getTime()<=reqTime) {
            holder.new_iv.setVisibility(View.VISIBLE);
        } else {
            holder.new_iv.setVisibility(View.GONE);
        }
        if (recent.isGone()){
            holder.new_iv.setVisibility(View.GONE);
        }
        holder.visit_time_tv.setText(new DateTime(visitTime).toDateTimeString(DateTime.MONTH_DAY_HOUR_MINUTE)+" 访问了您的主页");

//        holder.avatar_iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, PersonInfoActivity.class);
//                intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
//                intent.putExtra("userId", recent.getUserId() + "");
//                intent.putExtra("userNick", recent.getUsernick());
//                context.startActivity(intent);
//            }
//        });
        holder.avatar_iv.setOnClickListener(new AvatarOnClickListener(holder,recent));
        return convertView;
    }


    class AvatarOnClickListener implements View.OnClickListener{

        private ViewHolder holder;
        private UserSeeHistoryVo recent;
        public AvatarOnClickListener(ViewHolder holder,UserSeeHistoryVo recent) {
            this.holder = holder;
            this.recent = recent;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PersonInfoActivity.class);
            intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
            intent.putExtra("userId", recent.getUserId() + "");
            intent.putExtra("userNick", recent.getUsernick());
//            holder.new_iv.setVisibility(View.GONE);
            if (holder.new_iv.isShown()){
                recent.setIsGone(true);
                notifyDataSetChanged();
            }
            context.startActivity(intent);
        }
    }
    private class ViewHolder {
        private ImageView avatar_iv;
        private TextView nick_name_tv;
        private TextView sex_age_tv;
        private TextView type_icon_tv;
        private TextView type_tv;
        private TextView visit_time_tv;
        private ImageView new_iv;
    }

}

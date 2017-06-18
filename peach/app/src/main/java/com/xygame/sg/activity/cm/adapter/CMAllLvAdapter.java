package com.xygame.sg.activity.cm.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.cm.bean.AllPhotographerView;
import com.xygame.sg.activity.cm.bean.AllPhotographerVo;
import com.xygame.sg.activity.model.bean.AllModelItemBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

/**
 * Created by xy on 2016/1/17.
 */
public class CMAllLvAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private AllPhotographerView allPhotographers = new AllPhotographerView();
    private ImageLoader imageLoader;

    public CMAllLvAdapter(Context context, AllPhotographerView allPhotographers) {
        super();
        this.context = context;
        this.allPhotographers = allPhotographers;

        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        if (allPhotographers!=null && allPhotographers.getPhotops() != null && allPhotographers.getPhotops().size() > 0 ) {
            return allPhotographers.getPhotops().size();
        }
        return 0;
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
            convertView = inflater.inflate(R.layout.cm_all_lv_item, null);
            holder = new ViewHolder();
            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.sex_age_tv = (TextView) convertView.findViewById(R.id.sex_age_tv);
            holder.type_icon_tv = (TextView) convertView.findViewById(R.id.type_icon_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AllPhotographerVo cmBean = allPhotographers.getPhotops().get(i);

        holder.nick_name_tv.setText(cmBean.getUsernick());
        String userIcon = cmBean.getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.avatar_iv, true);
        }

        String cityStr = cmBean.getCity()+"";
        if (!StringUtils.isEmpty(cityStr)){
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(cmBean.getCity());
            if (it.getName()== null){
                holder.location_tv.setText("");
            } else {
                holder.location_tv.setText(it.getName());
            }
        } else {
            holder.location_tv.setText("");
        }
        holder.sex_age_tv.setText(cmBean.getAge()+"");
        String sexStr = cmBean.getGender()+"";
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            Drawable femaleDrawable = context.getResources().getDrawable(R.drawable.female);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            Drawable maleDrawable = context.getResources().getDrawable(R.drawable.male);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
        }

        String typeStr = cmBean.getUserType()+"";

        if (typeStr.equals(Constants.CARRE_PHOTOR)){
            if (cmBean.getAuthStatus() == 2){
                Drawable modelDrawable = context.getResources().getDrawable(R.drawable.cameraman_identy_icon);
                holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
                holder.type_tv.setText("摄影师");
                holder.type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
            }else{
                Drawable modelDrawable = context.getResources().getDrawable(R.drawable.cameraman_identy_icon);
                holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
                holder.type_tv.setText("摄影师");
                holder.type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
            }

        } else if(Constants.CARRE_MERCHANT.equals(typeStr)){
            if (cmBean.getAuthStatus() == 2){
                Drawable modelDrawable = context.getResources().getDrawable(R.drawable.cameraman_identy_icon);
                holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
                holder.type_tv.setText("经纪人");
                holder.type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
            }else{
                Drawable modelDrawable = context.getResources().getDrawable(R.drawable.cameraman_identy_icon);
                holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
                holder.type_tv.setText("经纪人");
                holder.type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
            }

        } else {
            //没认证不显示
            holder.type_icon_tv.setVisibility(View.GONE);
            holder.type_tv.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView avatar_iv;
        private TextView nick_name_tv;
        private TextView sex_age_tv;
        private TextView type_icon_tv;
        private TextView type_tv;
        private TextView location_tv;

    }
}

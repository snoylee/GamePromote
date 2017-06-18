package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.bean.AttentUserView;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class AttentionModelAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader;
    private List<AttentUserView> attentions = new ArrayList<AttentUserView>();


    public AttentionModelAdapter(Context context, List<AttentUserView> attentions) {
        super();
        this.context = context;
        this.attentions = attentions;


        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }


    @Override
    public int getCount() {
        return attentions.size();
    }

    @Override
    public AttentUserView getItem(int i) {
        return attentions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.attention_model_item, null);
            holder = new ViewHolder();
            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.sex_age_tv = (TextView) convertView.findViewById(R.id.sex_age_tv);
            holder.type_icon_tv = (TextView) convertView.findViewById(R.id.type_icon_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.label_ll = (LinearLayout) convertView.findViewById(R.id.label_ll);
            holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        final AttentUserView attention = attentions.get(i);

        holder.nick_name_tv.setText(attention.getUsernick());
        String userIcon = attention.getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.avatar_iv, false);
        }

        String cityStr = attention.getCity()+"";
        if (!StringUtils.isEmpty(cityStr)){
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(attention.getCity());
            if (it.getName()== null){
                holder.location_tv.setText("不限");
            } else {
                holder.location_tv.setText(it.getName());
            }
        } else {
            holder.location_tv.setText("不限");
        }
        holder.sex_age_tv.setText(attention.getAge()+"");
        String sexStr = attention.getGender()+"";
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            Drawable femaleDrawable = context.getResources().getDrawable(R.drawable.female);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            Drawable maleDrawable = context.getResources().getDrawable(R.drawable.male);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
        }

        String typeStr = attention.getUserType()+"";
        if (typeStr.equals(Constants.CARRE_MODEL)&& attention.getAuthStatus() == 2){//用户类型：2：模特；4：高级模特；0：未认证的模特（没有认证数据）
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_bg);
        } else if(typeStr.equals(Constants.PRO_MODEL)&& attention.getAuthStatus() == 2){
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

//        List<Integer> labels = attention.getPriceTypes();
//        if (holder.label_ll.getChildCount()>0){
//            holder.label_ll.removeAllViews();
//        }
//        for (int j = 0;j< (labels.size() > 3 ? 3 :  labels.size());j++){
//            TextView label_item = (TextView) inflater.inflate(R.layout.label_item, null);
//            LinearLayout.LayoutParams layoutParamsLl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParamsLl.setMargins(0,0, 10, 0);//4个参数按顺序分别是左上右下
//            label_item.setLayoutParams(layoutParamsLl);
//            int typeId = labels.get(j);
//            label_item.setText(SGApplication.getInstance().getTypeNameByTypeId(typeId));
//            holder.label_ll.addView(label_item);
//        }
//
//        if (attention.getPrice() != null){
//            holder.price_tv.setText("￥"+attention.getPrice());
//        } else {
//            holder.price_tv.setText("￥"+0);
//        }

        holder.avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonInfoActivity.class);
                intent.putExtra(Constants.EDIT_OR_QUERY_FLAG,Constants.QUERY_INFO_FLAG);
                intent.putExtra("userId",attention.getUserId()+"");
                intent.putExtra("userNick",attention.getUsernick());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private ImageView avatar_iv;
        private TextView nick_name_tv;
        private TextView sex_age_tv;
        private TextView type_icon_tv;
        private TextView type_tv;
        private TextView location_tv;
        private LinearLayout label_ll;
        private TextView price_tv;
    }

}

package com.xygame.sg.activity.commen.adapter;

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
import com.xygame.sg.activity.commen.bean.SearchUserView;
import com.xygame.sg.activity.commen.bean.SearchUserVo;
import com.xygame.sg.activity.commen.bean.UserType;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;

import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class SearchUserLvAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private SearchUserView users = new SearchUserView();
    private ImageLoader imageLoader;

    public SearchUserLvAdapter(Context context, SearchUserView users) {
        super();
        this.context = context;
        this.users = users;

        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        if (users!=null && users.getUsers() != null && users.getUsers().size() > 0 ) {
            return users.getUsers().size();
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
            convertView = inflater.inflate(R.layout.search_user_lv_item, null);
            holder = new ViewHolder();
            holder.item_root_ll = (LinearLayout) convertView.findViewById(R.id.item_root_ll);
            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.sex_age_tv = (TextView) convertView.findViewById(R.id.sex_age_tv);
            holder.type_icon_tv = (TextView) convertView.findViewById(R.id.type_icon_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.label_ll = (LinearLayout) convertView.findViewById(R.id.label_ll);
            holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
            holder.price_tip_tv = (TextView) convertView.findViewById(R.id.price_tip_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SearchUserVo userVo = users.getUsers().get(i);

        holder.nick_name_tv.setText(userVo.getUsernick());
        String userIcon = userVo.getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.avatar_iv, true);
        }

        String cityStr = userVo.getCity()+"";
        if (!StringUtils.isEmpty(cityStr)){
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(userVo.getCity());
            if (it.getName()== null){
                holder.location_tv.setVisibility(View.GONE);
                holder.location_tv.setText("");
            } else {
                holder.location_tv.setVisibility(View.VISIBLE);
                holder.location_tv.setText(it.getName());
            }
        } else {
            holder.location_tv.setText("");
            holder.location_tv.setVisibility(View.GONE);
        }
        holder.sex_age_tv.setText(userVo.getAge()+"");
        String sexStr = userVo.getGender()+"";
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            Drawable femaleDrawable = context.getResources().getDrawable(R.drawable.female);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            Drawable maleDrawable = context.getResources().getDrawable(R.drawable.male);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
        }

        holder.type_icon_tv.setVisibility(View.GONE);
        holder.type_tv.setVisibility(View.GONE);
        return convertView;
    }

    private class ViewHolder {
        private LinearLayout item_root_ll;
        private ImageView avatar_iv;
        private TextView nick_name_tv;
        private TextView sex_age_tv;
        private TextView type_icon_tv;
        private TextView type_tv;
        private TextView location_tv;
        private LinearLayout label_ll;
        private TextView price_tv;
        private TextView price_tip_tv;
    }
}

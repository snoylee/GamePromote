package com.xygame.second.sg.Group.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/6.
 */
public class GroupMemberAdapter extends BaseAdapter implements SectionIndexer{
    private Context context;
    private List<BlackMemberBean> vector;
    private ImageLoader mImageLoader;
    public GroupMemberAdapter(Context context, List<BlackMemberBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public BlackMemberBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.black_list_item, parent, false);
            viewHolder.avatar_iv = (CircularImage) convertView.findViewById(R.id.avatar_iv);
            viewHolder.sexIcon = (ImageView) convertView
                    .findViewById(R.id.sexIcon);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder. sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            viewHolder. sex_age_bg = convertView.findViewById(R.id.sex_age_bg);
            viewHolder. tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BlackMemberBean item = vector.get(position);

//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if(position == getPositionForSection(section)){
//            viewHolder.tvLetter.setVisibility(View.VISIBLE);
//            viewHolder.tvLetter.setText(item.getSortLetters());
//        }else{
//            viewHolder.tvLetter.setVisibility(View.GONE);
//        }

        viewHolder.userName.setText(item.getUsernick());
        viewHolder.sexAge.setText(item.getAge());
        String sexStr = item.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }
        mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        viewHolder. avatar_iv.setOnClickListener(new IntoPersonalDeltail(item));
        return convertView;
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private BlackMemberBean presenter;
        public IntoPersonalDeltail(BlackMemberBean presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(BlackMemberBean presenter) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick", presenter.getUsernick() == null ? "" : presenter.getUsernick());
        intent.putExtra("userId", presenter.getUserId());
        context. startActivity(intent);
    }


    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<BlackMemberBean> datas) {
        this.vector = datas;
        notifyDataSetChanged();
    }

    public void updateDatas(BlackMemberBean tempActionBean) {
        for (int i = 0; i < vector.size(); i++) {
            if (tempActionBean.getUserId().equals(vector.get(i).getUserId())) {
                vector.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = vector.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return vector.get(position).getSortLetters().charAt(0);
    }

    private static class ViewHolder {
        public CircularImage avatar_iv;
        public TextView userName, sexAge, tvLetter;
        public ImageView sexIcon;
        public View sex_age_bg;
    }
}
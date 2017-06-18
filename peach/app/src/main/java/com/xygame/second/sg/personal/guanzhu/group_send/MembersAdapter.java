package com.xygame.second.sg.personal.guanzhu.group_send;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/2.
 */
public class MembersAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<MemberBean> vector;
    private ImageLoader mImageLoader;

    public MembersAdapter(Context context, List<MemberBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<MemberBean> getSelectedMember(){
        List<MemberBean> tempDatas=new ArrayList<>();
        for (MemberBean item:vector){
            if (item.isSelected()){
                tempDatas.add(item);
            }
        }
        return tempDatas;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public MemberBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView =LayoutInflater.from(context).inflate(
                    R.layout.member_list_item, parent, false);
            viewHolder.selectIcon = (ImageView) convertView
                    .findViewById(R.id.selectIcon);
            viewHolder.sexIcon=(ImageView) convertView
                    .findViewById(R.id.sexIcon);
            viewHolder.avatar_iv=(CircularImage)convertView.findViewById(R.id.avatar_iv);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            viewHolder.tvLetter=(TextView)convertView.findViewById(R.id.catalog);
            viewHolder.sex_age_bg =convertView
                    .findViewById(R.id.sex_age_bg);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MemberBean item = vector.get(position);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(item.getSortLetters());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
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
        if (item.isSelected()){
            viewHolder.selectIcon.setImageResource(R.drawable.gou);
        }else{
            viewHolder.selectIcon.setImageResource(R.drawable.gou_null);
        }
        if (!TextUtils.isEmpty(item.getUserIcon())){
            mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        }else{
            viewHolder.avatar_iv.setImageResource(R.drawable.new_system_icon);
        }
        return convertView;
    }

    public void addDatas(List<MemberBean> datas) {
        this.vector = datas;
        notifyDataSetChanged();
    }

    public void updateDatas(int index) {
        vector.get(index).setIsSelected(!vector.get(index).isSelected());
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

    public void selectAll() {
        for (int i=0;i<vector.size();i++){
            vector.get(i).setIsSelected(true);
        }
        notifyDataSetChanged();
    }

    public void cancellAllSelect() {
        for (int i=0;i<vector.size();i++){
            vector.get(i).setIsSelected(false);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        CircularImage avatar_iv;
        TextView sexAge,userName,tvLetter;
        ImageView selectIcon,sexIcon;
        View sex_age_bg;
    }
}

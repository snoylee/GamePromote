package com.xygame.second.sg.personal.guanzhu.member;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/2.
 */
public class MemberSelectAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<GroupMemberBean> vector;
    private ImageLoader mImageLoader;

    public MemberSelectAdapter(Context context, List<GroupMemberBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<GroupMemberBean> getSelectDatas(){
        List<GroupMemberBean> temp=new ArrayList<>();
        for (GroupMemberBean it:vector){
            if (it.isSelect()){
                temp.add(it);
            }
        }
        return temp;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public GroupMemberBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateItem(GroupMemberBean item){
        for (int i=0;i<vector.size();i++){
            if (item.getUserId().equals(vector.get(i).getUserId())){
                vector.get(i).setIsSelect(!vector.get(i).isSelect());
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupMemberBean item = vector.get(position);
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.gz_group_list__select_item, parent, false);
            viewHolder. avatar_iv = (CircularImage) convertView.findViewById(R.id.avatar_iv);
            viewHolder.sexIcon = (ImageView) convertView
                    .findViewById(R.id.sexIcon);
            viewHolder.selectIcon=(ImageView)convertView.findViewById(R.id.selectIcon);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            viewHolder.sex_age_bg = convertView.findViewById(R.id.sex_age_bg);
            viewHolder.viewBackColor=convertView.findViewById(R.id.viewBackColor);
            viewHolder.deleteHolder = convertView.findViewById(R.id.deleteGroup);
            viewHolder.dividGroup=convertView.findViewById(R.id.dividGroup);
            viewHolder.tvLetter=(TextView)convertView.findViewById(R.id.catalog);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if(position == getPositionForSection(section)){
//            viewHolder.tvLetter.setVisibility(View.VISIBLE);
//            viewHolder.tvLetter.setText(item.getSortLetters());
//        }else{
//            viewHolder.tvLetter.setVisibility(View.GONE);
//        }

        if (item.isAlivable()){
            viewHolder.viewBackColor.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            viewHolder.viewBackColor.setBackgroundColor(context.getResources().getColor(R.color.light_search_gray));
        }

        viewHolder.userName.setText(item.getUsernick());
        viewHolder.sexAge.setText(item.getAge());
        if (item.isSelect()){
            viewHolder.selectIcon.setImageResource(R.drawable.gou);
        }else {
            viewHolder.selectIcon.setImageResource(R.drawable.gou_null);
        }
        String sexStr = item.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }
        if (!TextUtils.isEmpty(item.getUserIcon())){
            mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        }else{
            viewHolder.avatar_iv.setImageResource(R.drawable.new_system_icon);
        }
        viewHolder.avatar_iv.setOnClickListener(new AddClickListener(item));
        return convertView;
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

    private class AddClickListener implements View.OnClickListener {
        private GroupMemberBean item;

        public AddClickListener(GroupMemberBean item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            intoDetailAction(item);
        }
    }

    private void intoDetailAction(GroupMemberBean item) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick", item.getUsernick() == null ? "" : item.getUsernick());
        intent.putExtra("userId", item.getUserId());
        context.startActivity(intent);
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<GroupMemberBean> datas) {
        this.vector = datas;
        notifyDataSetChanged();
    }

    public void updateDatas(GroupMemberBean tempActionBean) {
        for (int i=0;i<vector.size();i++){
            if (tempActionBean.getUserId().equals(vector.get(i).getUserId())){
                vector.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void deleteMembers(List<String> deleteMembers) {
        for (String it:deleteMembers){
            for (int i=0;i<vector.size();i++){
                if (it.equals(vector.get(i).getUserId())){
                    vector.remove(i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
    private class ViewHolder {
        CircularImage avatar_iv;
        TextView userName, sexAge, tvLetter;
        ImageView sexIcon,selectIcon;
        View sex_age_bg,viewBackColor;
        View deleteHolder, dividGroup;
    }
}

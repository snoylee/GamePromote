package com.xygame.second.sg.personal.guanzhu.member;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.inteface.GroupMemberListListener;
import com.xygame.second.sg.comm.inteface.JustClickListener;
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
public class AllMemberAdapterBenFen extends BaseAdapter{
    private Context context;
    private List<GroupMemberBean> vector;
    private ImageLoader mImageLoader;
    private SlideView.OnSlideListener onSlideListener;
    private GroupMemberListListener cancelBlackListListener;

    public AllMemberAdapterBenFen(Context context, List<GroupMemberBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<GroupMemberBean> getDatas(){
        return vector;
    }

    public void addSlidViewListener(SlideView.OnSlideListener onSlideListener){
        this.onSlideListener=onSlideListener;
    }

    public void addCancelBlackListListener(GroupMemberListListener cancelBlackListListener){
        this.cancelBlackListListener=cancelBlackListListener;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupMemberBean item = vector.get(position);
        ViewHolder viewHolder = null;
        SlideView slideView = (SlideView) convertView;
        if (null == slideView) {

            View itemView = LayoutInflater.from(context).inflate(
                    R.layout.black_list_item, parent, false);
            slideView = new SlideView(context);
            slideView.setContentView(itemView);
            viewHolder = new ViewHolder(slideView);
            slideView.setOnSlideListener(onSlideListener);
            slideView.addClickListener(new AddClickListener(item));
            slideView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) slideView.getTag();
        }

        item.slideView = slideView;
        item.slideView.shrink();
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
        if (!TextUtils.isEmpty(item.getUserIcon())){
            mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        }else{
            viewHolder.avatar_iv.setImageResource(R.drawable.new_system_icon);
        }
        viewHolder.deleteHolder.setOnClickListener(new CancelBlackListAction(item));
        viewHolder.dividGroup.setOnClickListener(new DividGroup(item));
        return slideView;
    }

    private class AddClickListener implements JustClickListener {
        private  GroupMemberBean item;
        public AddClickListener( GroupMemberBean item ){
            this.item=item;
        }

        @Override
        public void justClickAction() {
            intoDetailAction(item);
        }
    }

    private void intoDetailAction(GroupMemberBean item) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick", item.getUsernick() == null ? "" : item.getUsernick());
        intent.putExtra("userId", item.getUserId());
        context. startActivity(intent);
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<GroupMemberBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            this.vector = datas;
        } else {
            this.vector.addAll(datas);
        }
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

    private static class ViewHolder {
        public  CircularImage avatar_iv;
        public  TextView userName, sexAge,tvLetter;
        public  ImageView sexIcon;
        public  View sex_age_bg;
        public  View deleteHolder,dividGroup;
        ViewHolder(View view) {

            avatar_iv = (CircularImage) view.findViewById(R.id.avatar_iv);
            sexIcon = (ImageView) view
                    .findViewById(R.id.sexIcon);
            userName = (TextView) view.findViewById(R.id.userName);
            sexAge = (TextView) view.findViewById(R.id.sexAge);
            sex_age_bg = view.findViewById(R.id.sex_age_bg);
            deleteHolder = view.findViewById(R.id.cancelAtt);
            dividGroup=view.findViewById(R.id.dividGroup);
            tvLetter=(TextView)view.findViewById(R.id.catalog);

        }
    }

    private class CancelBlackListAction implements View.OnClickListener{
        private GroupMemberBean item;
        public CancelBlackListAction(GroupMemberBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            cancelBlackListListener.cancelGZListener(item);
        }
    }

    private class DividGroup implements View.OnClickListener{
        private GroupMemberBean item;
        public DividGroup(GroupMemberBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            cancelBlackListListener.dividerListener(item);
        }
    }
}

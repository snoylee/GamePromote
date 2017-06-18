package com.xygame.second.sg.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.ActManagerBean;
import com.xygame.second.sg.comm.inteface.FansAttetListener;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.bean.GuanZhuFansBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/1.
 */
public class MyFansAdapter extends BaseAdapter {
    private Context context;
    private List<GuanZhuFansBean> vector;
    private ImageLoader mImageLoader;
    private String userId;

    private FansAttetListener actionListener;

    public void addActListener(FansAttetListener actionListener){
        this.actionListener=actionListener;
    }

    public MyFansAdapter(Context context, List<GuanZhuFansBean> vector,String userId) {
        this.context = context;
        this.userId=userId;
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
    public GuanZhuFansBean getItem(int position) {
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
                    R.layout.fans_guanzhu_item, parent, false);

            viewHolder.avatar_iv=(CircularImage)convertView.findViewById(R.id.avatar_iv);
            viewHolder.sexIcon = (ImageView) convertView
                    .findViewById(R.id.sexIcon);
            viewHolder.rightbuttonIcon = (ImageView) convertView
                    .findViewById(R.id.rightbuttonIcon);
            viewHolder.userName=(TextView)convertView.findViewById(R.id.userName);
            viewHolder.sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            viewHolder.userSign = (TextView) convertView.findViewById(R.id.userSign);
            viewHolder.timerText=(TextView)convertView.findViewById(R.id.timerText);
            viewHolder.sex_age_bg=convertView.findViewById(R.id.sex_age_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.timerText.setVisibility(View.GONE);
        GuanZhuFansBean item = vector.get(position);
        viewHolder.userName.setText(item.getUsernick());
        viewHolder.sexAge.setText(item.getAge());
        if (!TextUtils.isEmpty(item.getIntroDesc())){
            viewHolder.userSign.setText(item.getIntroDesc());
        }else{
            viewHolder.userSign.setVisibility(View.GONE);
        }
        String sexStr = item.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }
        mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        if (userId.equals(UserPreferencesUtil.getUserId(context))){
            viewHolder.rightbuttonIcon.setVisibility(View.VISIBLE);
            if ("1".equals(item.getHasAttent())){
                viewHolder.rightbuttonIcon.setImageResource(R.drawable.yiguanzhu_icon);
                viewHolder.rightbuttonIcon.setOnClickListener(new CancelAttationListener(item));
            }else{
                viewHolder.rightbuttonIcon.setImageResource(R.drawable.weiguanzhu_icon);
                viewHolder.rightbuttonIcon.setOnClickListener(new ComfirmAttationListener(item));
            }
        }else{
            viewHolder.rightbuttonIcon.setVisibility(View.GONE);
        }
        viewHolder. avatar_iv.setOnClickListener(new IntoPersonalDeltail(item));
        return convertView;
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private GuanZhuFansBean presenter;
        public IntoPersonalDeltail(GuanZhuFansBean presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(GuanZhuFansBean presenter) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick", presenter.getUsernick() == null ? "" : presenter.getUsernick());
        intent.putExtra("userId", presenter.getUserId());
        context. startActivity(intent);
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<GuanZhuFansBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            this.vector = datas;
        } else {
            this.vector.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void updateDatas(GuanZhuFansBean tempActionBean, boolean tempBoolean) {
        for (int i=0;i<vector.size();i++){
            if (tempActionBean.getUserId().equals(vector.get(i).getUserId())){
                if (!tempBoolean){
                    vector.get(i).setHasAttent("1");
                }else{
                    vector.get(i).setHasAttent("");
                }
                break;
            }
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        CircularImage avatar_iv;
        TextView userName, sexAge,userSign,timerText;
        ImageView sexIcon, rightbuttonIcon;
        View sex_age_bg;
    }

    private class CancelAttationListener implements View.OnClickListener{
        private GuanZhuFansBean item;
        public CancelAttationListener(GuanZhuFansBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            actionListener.attetionCancel(item);
        }
    }

    private class ComfirmAttationListener implements View.OnClickListener{
        private GuanZhuFansBean item;
        public ComfirmAttationListener(GuanZhuFansBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            actionListener.attetionComfim(item);
        }
    }
}
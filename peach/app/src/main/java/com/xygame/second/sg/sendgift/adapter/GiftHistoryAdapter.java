package com.xygame.second.sg.sendgift.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.GiftBean;
import com.xygame.second.sg.comm.inteface.LuQuListener;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.bean.GiftPresenter;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class GiftHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<GiftPresenter> vector;
    private ImageLoader mImageLoader;
    private List<GiftBean> giftDatas;
    private String fromFlag,publihserId,hireUserId;
    private LuQuListener luQuListener;
    private boolean isLvYunAialable;

    public GiftHistoryAdapter(Context context, List<GiftPresenter> vector, String fromFlag,String publihserId,boolean isLvYunAialable) {
        this.isLvYunAialable=isLvYunAialable;
        this.context = context;
        this.fromFlag = fromFlag;
        this.publihserId=publihserId;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        giftDatas = CacheService.getInstance().getCacheGiftDatas(ConstTaskTag.CACHE_GIFT_DATAS);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<GiftPresenter> getDatas(){
        return vector;
    }

    public void setLuQuActListener(LuQuListener luQuListener){
        this.luQuListener=luQuListener;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public GiftPresenter getItem(int position) {
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
                    R.layout.gift_history_item, parent, false);
            viewHolder.giftTimer = (TextView) convertView.findViewById(R.id.giftTimer);
            viewHolder.giftNumText = (TextView) convertView.findViewById(R.id.giftNumText);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            viewHolder.lqText=(TextView)convertView.findViewById(R.id.lqText);
            viewHolder.sexIcon = (ImageView) convertView
                    .findViewById(R.id.sexIcon);
            viewHolder.giftImage = (ImageView) convertView.findViewById(R.id.giftImage);
            viewHolder.sex_age_bg = convertView
                    .findViewById(R.id.sex_age_bg);
            viewHolder.giftTextFlag = convertView.findViewById(R.id.giftTextFlag);
            viewHolder.giftView = convertView.findViewById(R.id.giftView);
            viewHolder.noGiftView = convertView.findViewById(R.id.noGiftView);
            viewHolder.lvQuButton = convertView.findViewById(R.id.lvQuButton);
            viewHolder.avatar_iv = (CircularImage) convertView.findViewById(R.id.avatar_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GiftPresenter item = vector.get(position);
        Presenter presenter = item.getPresenter();
        mImageLoader.loadImage(presenter.getUserIcon(), viewHolder.avatar_iv, true);
        viewHolder. avatar_iv.setOnClickListener(new IntoPersonalDeltail(presenter));
        viewHolder.userName.setText(presenter.getUserNick());
        String sexStr = presenter.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
            viewHolder.sexAge.setText(presenter.getAge());
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder.sexAge.setText(presenter.getAge());
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }

        if (!TextUtils.isEmpty(item.getGiftId()) && !"null".equals(item.getGiftId())) {
            viewHolder.giftView.setVisibility(View.VISIBLE);
            viewHolder.noGiftView.setVisibility(View.GONE);
            if (giftDatas != null) {
                for (GiftBean itemq : giftDatas) {
                    if (item.getGiftId().equals(itemq.getId())) {
                        mImageLoader.loadOrigaImage(itemq.getShowUrl(), viewHolder.giftImage, true);
                        break;
                    }
                }
            }
            viewHolder.giftNumText.setText("X".concat(item.getGiftNums()));
            viewHolder.giftTextFlag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.giftTextFlag.setVisibility(View.GONE);
            viewHolder.giftView.setVisibility(View.GONE);
            viewHolder.noGiftView.setVisibility(View.VISIBLE);
        }
        viewHolder.giftTimer.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getSendGiftTime())));
        if (publihserId.equals(UserPreferencesUtil.getUserId(context))){
            if (isLvYunAialable){
                viewHolder.lvQuButton.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(hireUserId)){
                    if (presenter.getUserId().equals(hireUserId)){
                        viewHolder.lqText.setText("已录用");
                    }
                    viewHolder.lqText.setTextColor(context.getResources().getColor(R.color.gray));
                    viewHolder.lvQuButton.setBackgroundResource(R.drawable.shape_rect_gray);
                }else{
                    viewHolder.lvQuButton.setOnClickListener(new LuQuActionListener(presenter));
                }
            }else{
                viewHolder.lvQuButton.setVisibility(View.GONE);
            }
        }else{
            viewHolder.lvQuButton.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private Presenter presenter;
        public IntoPersonalDeltail(Presenter presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(Presenter presenter) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick", presenter.getUserNick() == null ? "" : presenter.getUserNick());
        intent.putExtra("userId", presenter.getUserId());
        context. startActivity(intent);
    }

    public void updateUseStatus(String hireUserId) {
        this.hireUserId=hireUserId;
        notifyDataSetChanged();
    }

    private class LuQuActionListener implements View.OnClickListener{

        private Presenter presenter;

        public LuQuActionListener(Presenter presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            luQuListener.luQuAction(presenter);
        }
    }

    private static class ViewHolder {
        CircularImage avatar_iv;
        TextView sexAge, userName, giftTimer, giftNumText,lqText;
        ImageView sexIcon, giftImage;
        View sex_age_bg, giftTextFlag, giftView, noGiftView, lvQuButton;
    }

    public void addDatas(List<GiftPresenter> datas, int mCurrentPage,String hireUserId) {
        if (mCurrentPage == 1) {
            this.vector = datas;
            this.hireUserId=hireUserId;
        } else {
            this.vector.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }
}
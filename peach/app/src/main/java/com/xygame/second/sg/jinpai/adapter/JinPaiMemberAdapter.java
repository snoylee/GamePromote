package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiCanYuBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.bean.GiftPresenter;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

public class JinPaiMemberAdapter extends BaseAdapter {
    private Context context;
    private List<JinPaiCanYuBean> vector;
    private ImageLoader imageLoader;

    public JinPaiMemberAdapter(Context context, List<JinPaiCanYuBean> vector) {
        this.context = context;
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
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
    public JinPaiCanYuBean getItem(int position) {
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
                    R.layout.jing_pai_members_item, parent, false);

            viewHolder.avatar_iv = (CircularImage) convertView.findViewById(R.id.avatar_iv);
            viewHolder.fpValueText = (TextView) convertView.findViewById(R.id.fpValueText);
            viewHolder.giftTimer = (TextView) convertView.findViewById(R.id.giftTimer);
            viewHolder.bottomLineView = convertView.findViewById(R.id.bottomLineView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bottomLineView.setVisibility(View.GONE);
        JinPaiCanYuBean item = vector.get(position);
        Presenter presenter = item.getPresenter();
        imageLoader.loadImage(presenter.getUserIcon(), viewHolder.avatar_iv, true);
        viewHolder. avatar_iv.setOnClickListener(new IntoPersonalDeltail(presenter));
        viewHolder.giftTimer.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getBidTime())));
        viewHolder.fpValueText.setText(item.getBidPrice());
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

    public void addDatas(List<JinPaiCanYuBean> giftPresenters, int hotPage) {
        if (hotPage==1){
            this.vector=giftPresenters;
        }else{
            this.vector.addAll(giftPresenters);
        }
        notifyDataSetChanged();
    }

    public List<JinPaiCanYuBean> getDatas() {
        return vector;
    }

    private static class ViewHolder {
        CircularImage avatar_iv;
        TextView fpValueText;
        TextView giftTimer;
        View bottomLineView;
    }
}
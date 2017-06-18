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

import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class SendGiftAdapter extends BaseAdapter {
    private Context context;
    private List<JinPaiBean> vector;
    private ImageLoader mImageLoader;

    public SendGiftAdapter(Context context, List<JinPaiBean> vector) {
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
    public JinPaiBean getItem(int position) {
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
                    R.layout.cub_sg_item, parent, false);
            viewHolder.jbLeftTimer = (TextView) convertView.findViewById(R.id.jbLeftTimer);
            viewHolder.jbTitle = (TextView) convertView.findViewById(R.id.jbTitle);
            viewHolder.plusherName = (TextView) convertView.findViewById(R.id.plusherName);
            viewHolder.id_img = (ImageView) convertView
                    .findViewById(R.id.id_img);
            viewHolder.wqView = convertView
                    .findViewById(R.id.wqView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JinPaiBean item = vector.get(position);
        viewHolder.jbTitle.setText(item.getActTitle());
        viewHolder.plusherName.setText(item.getUsernick());
        if (TextUtils.isEmpty(item.getScanCount())){
            viewHolder.jbLeftTimer.setText("0");
        }else {
            viewHolder.jbLeftTimer.setText(item.getScanCount());
        }
        mImageLoader.loadImage(item.getShowCoverUrl(), viewHolder.id_img, true);
//        viewHolder. id_img.setOnClickListener(new IntoPersonalDeltail(item));
        return convertView;
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private JinPaiBean presenter;
        public IntoPersonalDeltail(JinPaiBean presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(JinPaiBean presenter) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick", presenter.getUsernick() == null ? "" : presenter.getUsernick());
        intent.putExtra("userId", presenter.getUserId());
        context. startActivity(intent);
    }

    private static class ViewHolder {
        TextView jbLeftTimer;
        TextView jbTitle, plusherName;
        ImageView id_img;
        View wqView;
    }

    public void addDatas(List<JinPaiBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            this.vector = datas;
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
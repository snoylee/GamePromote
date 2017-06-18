package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.ActManagerBean;
import com.xygame.second.sg.jinpai.activity.JinPaiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiFuFeiDetailActivity;
import com.xygame.second.sg.sendgift.activity.YuePaiDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;

import java.util.ArrayList;
import java.util.List;

public class ActManagerAdapter extends BaseAdapter {
    private Context context;
    private List<ActManagerBean> vector;
    private ImageLoader mImageLoader;

    public ActManagerAdapter(Context context, List<ActManagerBean> vector) {
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
    public ActManagerBean getItem(int position) {
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
                    R.layout.act_manager_item, parent, false);

            viewHolder.priceTip=(TextView)convertView.findViewById(R.id.priceTip);
            viewHolder.priceView=convertView.findViewById(R.id.priceView);
            viewHolder.nameText = (TextView) convertView.findViewById(R.id.nameText);
            viewHolder.cherryNums = (TextView) convertView.findViewById(R.id.cherryNums);
            viewHolder.iconImage = (ImageView) convertView
                    .findViewById(R.id.iconImage);
            viewHolder.joinOrPlush = (ImageView) convertView
                    .findViewById(R.id.joinOrPlush);
            viewHolder.actType = (ImageView) convertView.findViewById(R.id.actType);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ActManagerBean item = vector.get(position);
        viewHolder.priceView.setVisibility(View.VISIBLE);
        viewHolder.nameText.setText(item.getActTitle());
        viewHolder.cherryNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
        if ("1".equals(item.getPartType())){//发布
            viewHolder.joinOrPlush.setImageResource(R.drawable.act_flag_plush);
        }else if ("2".equals(item.getPartType())){//参与
            viewHolder.joinOrPlush.setImageResource(R.drawable.act_flag_join);
        }
        if ("1".equals(item.getActNature())){//竞拍约
            viewHolder.priceTip.setText("目前");
            viewHolder.actType.setImageResource(R.drawable.act_flag_jinpai);
        }else if ("2".equals(item.getActNature())){//付费约
            viewHolder.priceTip.setText("每小时");
            viewHolder.actType.setImageResource(R.drawable.act_flag_fufei);
        }else if ("3".equals(item.getActNature())){//送礼约
            viewHolder.actType.setImageResource(R.drawable.act_flag_gift);
            viewHolder.priceView.setVisibility(View.GONE);
        }
		mImageLoader.loadImage(item.getShowCoverUrl(), viewHolder.iconImage, true);

        convertView.setOnClickListener(new IntoDetial(item));
        return convertView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        View priceView;
        TextView nameText, cherryNums,priceTip;
        ImageView iconImage, joinOrPlush, actType;
    }

    public void addDatas(List<ActManagerBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            this.vector = datas;
        } else {
            this.vector.addAll(datas);
        }
        notifyDataSetChanged();
    }

    private class IntoDetial implements View.OnClickListener{
        private ActManagerBean item;
        public IntoDetial(ActManagerBean item){
            this.item=item;
        }

        @Override
        public void onClick(View v) {
            intoAction(item);
        }
    }

    private void intoAction(ActManagerBean item) {
        Intent intent=new Intent();
        if ("1".equals(item.getActNature())){
            intent.setClass(context, JinPaiDetailActivity.class);
        }else if ("2".equals(item.getActNature())){
            intent.setClass(context, JinPaiFuFeiDetailActivity.class);
        }else if ("3".equals(item.getActNature())){
            intent.setClass(context, YuePaiDetailActivity.class);
            intent.putExtra("fromFlag", "editor");
        }
        intent.putExtra("actId", item.getActId());
        intent.putExtra("userId",item.getUserId());
        context.startActivity(intent);
    }
}
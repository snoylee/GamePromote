package com.xygame.second.sg.xiadan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.biggod.activity.GodDetailActivity;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.GodUserBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/29.
 */
public class GodTypeAdapter extends BaseAdapter {
    private Context context;
    private List<GodUserBean> vector;
    private ImageLoader mImageLoader;
    private JinPaiBigTypeBean jinPaiBigTypeBean;

    public GodTypeAdapter(Context context, List<GodUserBean> vector,JinPaiBigTypeBean jinPaiBigTypeBean) {
        this.context = context;
        this.jinPaiBigTypeBean=jinPaiBigTypeBean;
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
    public GodUserBean getItem(int position) {
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
                    R.layout.god_type_item, parent, false);
            viewHolder.sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            viewHolder. sexIcon = (ImageView) convertView.findViewById(R.id.sexIcon);
            viewHolder. sex_age_bg = convertView.findViewById(R.id.sex_age_bg);
            viewHolder. userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder. avatar_iv = (CircularImage) convertView.findViewById(R.id.avatar_iv);
            viewHolder. userLable=(TextView)convertView.findViewById(R.id.userLable);
            viewHolder. priceValue=(TextView)convertView.findViewById(R.id.priceValue);
            viewHolder.jieDanNums=(TextView)convertView.findViewById(R.id.jieDanNums);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GodUserBean item = vector.get(position);

        viewHolder.jieDanNums.setText("接单".concat(item.getOrderCount()).concat("次"));
        viewHolder.userName.setText(item.getUsernick());
        String sexStr = item.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder. sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder. sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
            viewHolder. sexAge.setText(item.getAge());
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder. sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder. sexAge.setText(item.getAge());
            viewHolder. sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }
        List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
        if (fuFeiDatas!=null){
            for (PriceBean t:fuFeiDatas){
                if (t.getId().equals(item.getPriceId())){
                    int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
                    viewHolder.priceValue.setText(String.valueOf(value));
                    break;
                }
            }
        }
        List<GodLableBean> typeLable= Constants.getGodLableDatas(jinPaiBigTypeBean.getSubStr());
        if (typeLable!=null){
            for (GodLableBean it:typeLable){
                if (it.getTitleId().equals(item.getSillTitle())){
                    viewHolder.userLable.setText(it.getTitleName());
                    break;
                }
            }
        }
        mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        convertView.setOnClickListener(new IntoDetial(item));
        return convertView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private CircularImage avatar_iv;
        TextView userName, sexAge,userLable,priceValue,jieDanNums;
        ImageView sexIcon;
        View sex_age_bg;
    }

    public void addDatas(List<GodUserBean> datas, int mCurrentPage) {
        if (mCurrentPage == 1) {
            this.vector = datas;
        } else {
            this.vector.addAll(datas);
        }
        notifyDataSetChanged();
    }

    private class IntoDetial implements View.OnClickListener {
        private GodUserBean item;

        public IntoDetial(GodUserBean item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            intoAction(item);
        }
    }

    private void intoAction(GodUserBean item) {
        Intent intent=new Intent(context,GodDetailActivity.class);
        intent.putExtra("userId",item.getUserId());
        intent.putExtra("userName",item.getUsernick());
        intent.putExtra("skillCode",jinPaiBigTypeBean.getId());
        context.startActivity(intent);
    }
}

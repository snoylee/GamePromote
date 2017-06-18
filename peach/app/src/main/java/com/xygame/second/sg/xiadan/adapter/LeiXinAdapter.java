package com.xygame.second.sg.xiadan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.GodUserBean;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/22.
 */
public class LeiXinAdapter extends BaseAdapter {
    private Context context;
    private List<GodUserBean> vector;
    private ImageLoader mImageLoader;
    private JinPaiBigTypeBean bigTypeBean;

    public LeiXinAdapter(Context context, List<GodUserBean> vector) {
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
    public GodUserBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GodUserBean item=vector.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.leixin_item_item, parent, false);
        }
        ImageView imageview = (ImageView) convertView
                .findViewById(R.id.id_img);
        TextView userNick=(TextView)convertView.findViewById(R.id.userNick);
        TextView userLable=(TextView)convertView.findViewById(R.id.userLable);
        TextView priceValue=(TextView)convertView.findViewById(R.id.priceValue);
        userNick.setText(item.getUsernick());
        List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
        if (fuFeiDatas!=null){
            for (PriceBean t:fuFeiDatas){
                if (t.getId().equals(item.getPriceId())){
                    int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
                    priceValue.setText(String.valueOf(value));
                    break;
                }
            }
        }

        List<GodLableBean> typeLable=Constants.getGodLableDatas(bigTypeBean.getSubStr());
        if (typeLable!=null){
            for (GodLableBean it:typeLable){
                if (it.getTitleId().equals(item.getSillTitle())){
                    userLable.setText(it.getTitleName());
                    break;
                }
            }
        }

        mImageLoader.loadImage(item.getUserIcon(), imageview, false);
        return convertView;
    }

    public void addTypeBean(JinPaiBigTypeBean it) {
        bigTypeBean=new JinPaiBigTypeBean();
        bigTypeBean.setId(it.getId());
        bigTypeBean.setUrl(it.getUrl());
        bigTypeBean.setName(it.getName());
        bigTypeBean.setSubStr(it.getSubStr());
        bigTypeBean.setCategoryName(it.getCategoryName());
        bigTypeBean.setIsSelect(it.isSelect());
    }

    public void updatePhotoes(List<GodUserBean> userBeans) {
        this.vector=userBeans;
    }
}
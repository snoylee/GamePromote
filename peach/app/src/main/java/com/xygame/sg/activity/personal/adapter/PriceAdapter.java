package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.CommentAllActivity;
import com.xygame.sg.activity.personal.EditPriceActivity;
import com.xygame.sg.activity.personal.EditSecondCategoryActivity;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.ModelPriceVo;
import com.xygame.sg.activity.personal.bean.QueryModelPriceView;
import com.xygame.sg.activity.personal.bean.UserType;
import com.xygame.sg.define.draggrid.DataTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/16.
 */
public class PriceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private List<QueryModelPriceView> dataList;
    private ModelDataVo modelDataVo ;

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;

    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_COMMENT = 1;

    public PriceAdapter(Context context,List<QueryModelPriceView> dataList,ModelDataVo modelDataVo,boolean isQuery) {
        super();
        this.context = context;
        this.dataList = dataList;
        this.isQuery = isQuery;
        this.modelDataVo = modelDataVo;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_COMMENT;
        } else {
            return TYPE_DEFAULT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        if (type == TYPE_COMMENT){
            convertView = inflater.inflate(R.layout.comment_outline_layout, null);
            RelativeLayout trade_num_rl = (RelativeLayout) convertView.findViewById(R.id.trade_num_rl);
            RatingBar score_rating = (RatingBar) convertView.findViewById(R.id.score_rating);
            TextView trade_num_tv = (TextView) convertView.findViewById(R.id.trade_num_tv);
            TextView comment_num_tv = (TextView) convertView.findViewById(R.id.comment_num_tv);

            trade_num_tv.setText(" ("+modelDataVo.getScore().getTradeCount()+")");
            comment_num_tv.setText(" ("+modelDataVo.getScore().getEvlCount()+")");
            score_rating.setRating((float)modelDataVo.getScore().getEvlScore());
            trade_num_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,CommentAllActivity.class);
                    intent.putExtra("seeUserId",modelDataVo.getSeeUserId()+"");
                    List<UserType> utypesList = modelDataVo.getUserTypes();
                    UserType utypes = new UserType();
                    if (utypesList.size() > 1) {
                        utypes = utypesList.get(1);
                    } else {
                        utypes = utypesList.get(0);
                    }
                    String userType = utypes.getUtype()+"";
                    intent.putExtra("utype",userType);
                    context.startActivity(intent);
                }
            });

        } else if(type == TYPE_DEFAULT){
            convertView = inflater.inflate(R.layout.person_price_item, null);
            View divider_line = convertView.findViewById(R.id.divider_line);
            LinearLayout price_item_root_ll = (LinearLayout) convertView.findViewById(R.id.price_item_root_ll);


            final QueryModelPriceView itemData = dataList.get(i-1);
            TextView first_category_tv = (TextView) convertView.findViewById(R.id.first_category_tv);
            first_category_tv.setText(itemData.getPriceTypeName());

            List<ModelPriceVo> subItemList = itemData.getPrices();
            LinearLayout price_item_ll = (LinearLayout) convertView.findViewById(R.id.price_item_ll);
            for (int j=0;j<subItemList.size();j++){
                ModelPriceVo map = subItemList.get(j);
                LinearLayout price_sub_item_view = (LinearLayout) inflater.inflate(R.layout.fragment_price_list_sub_item, null);
                LinearLayout.LayoutParams layoutParamsLl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParamsLl.setMargins(0,20, 0, 0);//4个参数按顺序分别是左上右下
                price_sub_item_view.setLayoutParams(layoutParamsLl);

                TextView second_category_tv = (TextView) price_sub_item_view.findViewById(R.id.second_category_tv);
                TextView series_price_tv = (TextView) price_sub_item_view.findViewById(R.id.series_price_tv);
                TextView price_note_tv = (TextView) price_sub_item_view.findViewById(R.id.price_note_tv);

                second_category_tv.setText(map.getItemName());
                series_price_tv.setText("￥"+map.getPrice()+"/"+map.getPriceUnit());
                if (map.getLimitParter() == null){
                    price_note_tv.setVisibility(View.GONE);
                } else {
                    price_note_tv.setText("每次拍摄人数不得超过："+map.getLimitParter()+"人");
                }
                price_item_ll.addView(price_sub_item_view);
            }
            if (i ==1){
                FrameLayout.LayoutParams layoutParamsLl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                layoutParamsLl.setMargins(0, DataTools.dip2px(context,10), 0, 0);//4个参数按顺序分别是左上右下
                price_item_root_ll.setLayoutParams(layoutParamsLl);
            } else if (i == getCount()-1) {
                FrameLayout.LayoutParams layoutParamsLl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                layoutParamsLl.setMargins(0, 0, 0, DataTools.dip2px(context,20));//4个参数按顺序分别是左上右下
                price_item_root_ll.setLayoutParams(layoutParamsLl);
                View bottom_divider_line = inflater.inflate(R.layout.gray_divider_line, null);
                price_item_root_ll.addView(bottom_divider_line);
            }

            if (getCount() == i+1){
                View bottom_divider_line = inflater.inflate(R.layout.gray_divider_line, null);
                price_item_root_ll.addView(bottom_divider_line);
            }

            if (1 < i && i < getCount()) {
                LinearLayout.LayoutParams layoutParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                layoutParamsV.setMargins(DataTools.dip2px(context, 30), 0, 0, 0);//4个参数按顺序分别是左上右下
                divider_line.setLayoutParams(layoutParamsV);
            }

            ImageView edit_iv = (ImageView) convertView.findViewById(R.id.edit_iv);

            if (!isQuery){
                edit_iv.setVisibility(View.VISIBLE);
                price_item_root_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editintent = new Intent(context,EditPriceActivity.class);
//                        List<Map> mapList = new ArrayList<Map>();
//                        mapList.add(itemData);
                        editintent.putExtra("toEditPriceItemMap",itemData);
                        context.startActivity(editintent);
                    }
                });
            } else {
                edit_iv.setVisibility(View.GONE);
            }
        }



        return convertView;
    }


}

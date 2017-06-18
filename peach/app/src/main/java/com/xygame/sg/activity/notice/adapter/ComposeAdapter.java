package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.utils.CalendarUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeAdapter extends BaseAdapter {
	private List<ComposeBean> strList;
	private Context context;
	
	public ComposeAdapter(Context context, List<ComposeBean> strList) {
		super();
		this.context = context;
		if (strList == null) {
			this.strList = new ArrayList<ComposeBean>();
		}else{
			this.strList = strList;
		}
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strList.size();
	}

	@Override
	public ComposeBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addDatas(List<ComposeBean> datas){
		this.strList.addAll(datas);
		notifyDataSetChanged();
	}
	
	public void clearDatas(){
		this.strList.clear();
	}
	
	public void updateSelected(int index){
		if("false".equals(strList.get(index).getCanUse())){
			Toast.makeText(context, "此优惠券不可用", Toast.LENGTH_SHORT).show();
		}else{
			for(int i=0;i<strList.size();i++){
				if(index==i){
					strList.get(i).setSelect(true);
				}else{
					strList.get(i).setSelect(false);
				}
			}
			notifyDataSetChanged();
		}
	}
	
	public ComposeBean getComposeBean(){
		ComposeBean it=null;
		for(ComposeBean item:strList){
			if(item.isSelect()){
				it=item;
			}
		}
		return it;
	}
	
	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private View composeBackground;
		private ImageView select_icon;
		private TextView manPrice,useDate,tipText,youHuiPrice,composeName,yangIcon;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.compose_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.composeBackground=convertView.findViewById(R.id.composeBackground);
			viewHolder.manPrice = (TextView) convertView
					.findViewById(R.id.manPrice);
			viewHolder.useDate =(TextView)convertView
					.findViewById(R.id.useDate);
			viewHolder.tipText=(TextView)convertView.findViewById(R.id.tipText);
			viewHolder.composeName=(TextView)convertView.findViewById(R.id.composeName);
			viewHolder.yangIcon=(TextView)convertView.findViewById(R.id.yangIcon);
			viewHolder.youHuiPrice=(TextView)convertView.findViewById(R.id.youHuiPrice);
			viewHolder.select_icon=(ImageView)convertView.findViewById(R.id.select_icon);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ComposeBean item=strList.get(position);
		if("true".equals(item.getCanUse())){
			viewHolder.composeBackground.setBackgroundResource(R.drawable.coupon);
		}else{
			viewHolder.composeBackground.setBackgroundResource(R.drawable.coupon_unse);
		}
		viewHolder.composeName.setText(item.getCoupName());
		if(item.isSelect()){
			viewHolder.select_icon.setVisibility(View.VISIBLE);
		}else{
			viewHolder.select_icon.setVisibility(View.GONE);
		}
		if("1".equals(item.getUseType())){
			viewHolder.yangIcon.setVisibility(View.VISIBLE);
			viewHolder.youHuiPrice.setVisibility(View.VISIBLE);
			viewHolder.youHuiPrice.setText(item.getYouHuiPrice());
		}else{
			viewHolder.yangIcon.setVisibility(View.GONE);
			viewHolder.youHuiPrice.setVisibility(View.VISIBLE);
			viewHolder.youHuiPrice.setText(item.getOffdiscount());
		}
		if(item.getManPrice()!=null&&!"null".equals(item.getManPrice())&&!"".equals(item.getManPrice())){
			if("0".equals(item.getManPrice())){
				viewHolder.manPrice.setVisibility(View.GONE);
			}else{
				viewHolder.manPrice.setVisibility(View.VISIBLE);
				viewHolder.manPrice.setText("满".concat(item.getManPrice()).concat("可用"));
			}
		}else{
			viewHolder.manPrice.setVisibility(View.GONE);
		}
		if(item.getUseDate()!=null&&!"null".equals(item.getUseDate())&&!"".equals(item.getUseDate())){
			viewHolder.useDate.setVisibility(View.VISIBLE);
			viewHolder.useDate.setText("有效期：".concat(CalendarUtils.getDateStr(Long.parseLong(item.getUseDate()))));
		}else{
			viewHolder.useDate.setVisibility(View.GONE);
		}
		if(item.getTipText()!=null&&!"null".equals(item.getTipText())&&!"".equals(item.getTipText())){
			viewHolder.tipText.setVisibility(View.VISIBLE);
			viewHolder.tipText.setText(item.getTipText());
		}else{
			viewHolder.tipText.setVisibility(View.GONE);
		}
		return convertView;
	}

	public View getSubView(){
		return LayoutInflater.from(context).inflate(R.layout.sg_model_request_item_item,
				null);
	}
	
	public View getFristSubView(){
		return LayoutInflater.from(context).inflate(R.layout.sg_model_request_item_item_item,
				null);
	}
}

package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModelRequestAdapter extends BaseAdapter {
	private List<ModelRequestBean> strList;
	private Context context;
	private LayoutParams lp;
	public ModelRequestAdapter(Context context, List<ModelRequestBean> strList) {
		super();
		this.context = context;
		lp = new LayoutParams(30, 10);
		if (strList == null) {
			this.strList = new ArrayList<ModelRequestBean>();
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
	public ModelRequestBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addNewItem(ModelRequestBean it){
		strList.add(it);
		notifyDataSetChanged();
	}
	
	public void updateItem(ModelRequestBean it){
		for(int i=0;i<strList.size();i++){
			if(it.get_id().equals(strList.get(i).get_id())){
				strList.set(i, it);
				notifyDataSetChanged();
				break;
			}
		}
	}
	
	public List<ModelRequestBean> getDatas(){
		return strList;
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private LinearLayout firstLine,secondLine,thirdLine,secondMoreLine;
		private TextView priceValue;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_model_request_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.priceValue = (TextView) convertView
					.findViewById(R.id.priceValue);
			viewHolder.firstLine =(LinearLayout)convertView
					.findViewById(R.id.firstLine);
			viewHolder.secondLine=(LinearLayout)convertView.findViewById(R.id.secondLine);
			viewHolder.thirdLine=(LinearLayout)convertView.findViewById(R.id.thirdLine);
			viewHolder.secondMoreLine=(LinearLayout)convertView.findViewById(R.id.secondMoreLine);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.firstLine.setVisibility(View.VISIBLE);
		viewHolder.secondLine.setVisibility(View.VISIBLE);
		viewHolder.thirdLine.setVisibility(View.VISIBLE);
		viewHolder.secondMoreLine.setVisibility(View.VISIBLE);
		
		viewHolder.firstLine.removeAllViews();
		viewHolder.secondLine.removeAllViews();
		viewHolder.thirdLine.removeAllViews();
		viewHolder.secondMoreLine.removeAllViews();
		ModelRequestBean item=strList.get(position);
		viewHolder.priceValue.setText(item.getNeedPrice());
		ImageView tagIcon;
		TextView tagText;
		if(item.getSexName()!=null){
			View secondViewsfirst=getFristSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			if("男".equals(item.getSexName())){
				tagIcon.setImageResource(R.drawable.sg_pl_man);
			}else if("女".equals(item.getSexName())){
				tagIcon.setImageResource(R.drawable.sg_pl_woman);
			}
			tagText.setText(item.getSexName());
			viewHolder.firstLine.addView(secondViewsfirst);
		}
		
		if(item.getNeedNum()!=null){
			View secondViewsfirst=getSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_ren_num);
			tagText.setText(item.getNeedNum());
			viewHolder.firstLine.addView(secondViewsfirst);
		}
		
		if(item.getSmallAge()!=null){
			View secondViewsfirst=getFristSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_age);
			tagText.setText(item.getSmallAge().concat("-").concat(item.getBigAge()).concat("岁"));
			viewHolder.secondMoreLine.addView(secondViewsfirst);
		}
		
		if(item.getSmallBodyHight()!=null){
			View secondViewsfirst=getSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_sg);
			tagText.setText(item.getSmallBodyHight().concat("cm").concat("-").concat(item.getBigBodyHight()).concat("cm"));
			viewHolder.secondMoreLine.addView(secondViewsfirst);
		}
		
		if(item.getCityName()!=null){
			View secondViewsfirst=getFristSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_city);
			tagText.setText(item.getCityName());
			viewHolder.secondLine.addView(secondViewsfirst);
		}
		
		if(item.getCountryName()!=null){
			View secondViewsfirst=getSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_country);
			tagText.setText(item.getCountryName());
			viewHolder.secondLine.addView(secondViewsfirst);
		}
		
		if(item.isBaoXiaoCaiLv()){
			View secondViewsfirst=getSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_clf);
			tagText.setText("报销异地差旅费");
			viewHolder.thirdLine.addView(secondViewsfirst);
			View nullView5=new View(context);
			nullView5.setLayoutParams(lp);
			viewHolder.thirdLine.addView(nullView5);
		}
		
		if(item.isBaoXiaoZhuSu()){
			View secondViewsfirst=getSubView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_zsf);
			tagText.setText("报销异地住宿费");
			viewHolder.thirdLine.addView(secondViewsfirst);
		}
		
		if(viewHolder.firstLine.getChildCount()==0){
			viewHolder.firstLine.setVisibility(View.GONE);
		}
		if(viewHolder.secondMoreLine.getChildCount()==0){
			viewHolder.secondMoreLine.setVisibility(View.GONE);
		}
		if(viewHolder.secondLine.getChildCount()==0){
			viewHolder.secondLine.setVisibility(View.GONE);
		}
		if(viewHolder.thirdLine.getChildCount()==0){
			viewHolder.thirdLine.setVisibility(View.GONE);
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

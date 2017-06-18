package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.CarrierBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CarrierAdapter extends BaseAdapter {
	private List<CarrierBean> strList;
	private Context context;
	private String strFlag;
	public CarrierAdapter(Context context, List<CarrierBean> strList,String strFlag) {
		super();
		this.context = context;
		this.strFlag=strFlag;
		if (strList == null) {
			this.strList = new ArrayList<CarrierBean>();
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
	public CarrierBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private View bottomLine;
		private ImageView selectIcon;
		private TextView countryText;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_country_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.countryText = (TextView) convertView
					.findViewById(R.id.countryText);
			viewHolder.bottomLine =convertView
					.findViewById(R.id.bottomLine);
			viewHolder.selectIcon=(ImageView)convertView.findViewById(R.id.selectIcon);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(position==strList.size()-1){
			viewHolder.bottomLine.setVisibility(View.GONE);
		}else{
			viewHolder.bottomLine.setVisibility(View.VISIBLE);
		}
		CarrierBean item=strList.get(position);
		viewHolder.countryText.setText(item.getCarrierName());
		if(strFlag.equals(item.getTypeId())){
			viewHolder.selectIcon.setVisibility(View.VISIBLE);
			viewHolder.selectIcon.setImageResource(R.drawable.sg_list_select_icon);
			viewHolder.countryText.setTextColor(context.getResources().getColor(R.color.dark_green));
		}else{
			viewHolder.selectIcon.setVisibility(View.GONE);
			viewHolder.countryText.setTextColor(context.getResources().getColor(R.color.black));
		}
		
		return convertView;
	}

}

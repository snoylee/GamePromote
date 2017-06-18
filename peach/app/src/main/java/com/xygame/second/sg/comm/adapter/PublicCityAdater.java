package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicCityAdater extends BaseAdapter {

	private Context context;
	private List<CityBean> datas;
	private Map<String, String> isSelectMap;
	public PublicCityAdater(Context context, List<CityBean> datas) {
		this.context = context;
		isSelectMap=new HashMap<>();
		if (null == datas) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CityBean getItem(int position) {
		CityBean item=datas.get(position);
		item.get();
		return item;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView holdView;
		if (view == null) {
			holdView = new HoldView();
			view = LayoutInflater.from(context).inflate(
					R.layout.act_area_item_layout, null);
			holdView.areaName = (TextView) view.findViewById(R.id.areaName);
			holdView.pBackground=view.findViewById(R.id.pBackground);
			holdView.optionIcon=(ImageView)view.findViewById(R.id.optionIcon);
			holdView.pLine=view.findViewById(R.id.pLine);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		CityBean item=datas.get(position);
		item.get();
		holdView.areaName.setText(item.getName());

		if (item.getCityCode().equals(isSelectMap.get("flagPostion"))){
			holdView.pBackground.setBackgroundColor(context.getResources().getColor(R.color.white));
			holdView.pLine.setVisibility(View.INVISIBLE);
		}else{
			holdView.pBackground.setBackgroundColor(context.getResources().getColor(R.color.main_menu_bg));
			holdView.pLine.setVisibility(View.VISIBLE);
		}
		
		return view;
	}

	public void setCurTrue(CityBean item){
		isSelectMap.put("flagPostion", item.getCityCode());
		notifyDataSetChanged();
	}

	public void updateDatas(List<CityBean> vector) {
		this.datas=vector;
		CityBean item=vector.get(0);
		item.get();
		isSelectMap.put("flagPostion", item.getCityCode());
		notifyDataSetChanged();
	}

	public CityBean getCurrCity() {
		CityBean tempBean=null;
		for (CityBean it:datas){
			it.get();
			if (it.getCityCode().equals(isSelectMap.get("flagPostion"))){
				tempBean=it;
				break;
			}
		}
		return tempBean;
	}

	class HoldView {
		View pBackground,pLine;
		TextView areaName;
		ImageView optionIcon;
	}

}

package com.xygame.second.sg.descover.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ProvinceBean;

public class ParentAreaAdater extends BaseAdapter {

	private Context context;
	private List<ProvinceBean> datas;
	private Map<String, String> isSelectMap;

	public ParentAreaAdater(Context context, List<ProvinceBean> datas) {
		this.context = context;
		isSelectMap=new HashMap<>();
		if (null == datas) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
		}
		ProvinceBean it=datas.get(1);
		it.get();
		isSelectMap.put("flagPostion",it.getProvinceCode());
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ProvinceBean getItem(int position) {
		ProvinceBean item=datas.get(position);
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

		ProvinceBean item=datas.get(position);
		item.get();
		holdView.areaName.setText(item.getProvinceName());

		if (item.getProvinceCode().equals(isSelectMap.get("flagPostion"))){
			holdView.pBackground.setBackgroundColor(context.getResources().getColor(R.color.white));
			holdView.pLine.setVisibility(View.INVISIBLE);
		}else{
			holdView.pBackground.setBackgroundColor(context.getResources().getColor(R.color.main_menu_bg));
			holdView.pLine.setVisibility(View.VISIBLE);
		}
		
		return view;
	}
	
	public void setCurTrue(ProvinceBean item){
		isSelectMap.put("flagPostion", item.getProvinceCode());
		notifyDataSetChanged();
	}

	class HoldView {
		View pBackground,pLine;
		TextView areaName;
		ImageView optionIcon;
	}

}

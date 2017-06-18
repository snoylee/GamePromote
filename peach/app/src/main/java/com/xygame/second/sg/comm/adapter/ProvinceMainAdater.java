package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

public class ProvinceMainAdater extends BaseAdapter {

	private Context context;
	private List<ProvinceBean> datas;
	public ProvinceMainAdater(Context context, List<ProvinceBean> datas) {
		this.context = context;
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
					R.layout.area_main_item, null);
			holdView.areaName = (TextView) view.findViewById(R.id.pName);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		ProvinceBean item=datas.get(position);
		item.get();
		holdView.areaName.setText(item.getProvinceName());
		return view;
	}

	class HoldView {
		TextView areaName;
	}

}

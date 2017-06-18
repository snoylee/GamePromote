package com.xygame.second.sg.descover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.util.ArrayList;
import java.util.List;

public class ChildAreaAdater extends BaseAdapter {

	private Context context;
	private List<CityBean> datas;

	public ChildAreaAdater(Context context,PlushNoticeAreaBean areaBean) {
		this.context = context;
		List<CityBean> temp= AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(areaBean.getProvinceId()));
		CityBean itBean=temp.get(0);
		itBean.get();
		if (itBean.getCityName().contains("北京")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("天津")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("重庆")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("上海")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("香港")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("澳门")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("台湾")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else{
			this.datas=temp;
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CityBean getItem(int position) {
		CityBean item=datas.get(position);
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
					R.layout.act_area_block_item_layout, null);
			holdView.blockName = (TextView) view.findViewById(R.id.blockName);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		CityBean item=datas.get(position);
		item.get();
		holdView.blockName.setText(item.getCityName());
		return view;
	}
	
	public void clearDatas(){
		datas.clear();
	}

	public void updateList(ProvinceBean provinceBean) {
		List<CityBean> temp= AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceBean.getProvinceCode()));
		CityBean itBean=temp.get(0);
		itBean.get();
		if (itBean.getCityName().contains("北京")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("天津")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("重庆")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("上海")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("香港")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("澳门")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else if (itBean.getCityName().contains("台湾")){
			this.datas=new ArrayList<>();
			this.datas.add(itBean);
		}else{
			this.datas=temp;
		}
		notifyDataSetChanged();
	}

	class HoldView {
		TextView blockName;
	}


}

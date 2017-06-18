package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ProvinceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopServiceParentAdater extends BaseAdapter {

	private Context context;
	private List<JinPaiBigTypeBean> datas;
	private Map<String, String> isSelectMap;
	public PopServiceParentAdater(Context context, List<JinPaiBigTypeBean> datas) {
		this.context = context;
		if (null == datas) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
			isSelectMap=new HashMap<>();
			JinPaiBigTypeBean it=datas.get(1);
			isSelectMap.put("flagPostion",it.getId());
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public JinPaiBigTypeBean getItem(int position) {
		return datas.get(position);
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

		JinPaiBigTypeBean item=datas.get(position);
		holdView.areaName.setText(item.getName());

		if (item.getId().equals(isSelectMap.get("flagPostion"))){
			holdView.pBackground.setBackgroundColor(context.getResources().getColor(R.color.white));
			holdView.pLine.setVisibility(View.INVISIBLE);
		}else{
			holdView.pBackground.setBackgroundColor(context.getResources().getColor(R.color.main_menu_bg));
			holdView.pLine.setVisibility(View.VISIBLE);
		}
		
		return view;
	}
	
	public void setCurTrue(JinPaiBigTypeBean item){
		isSelectMap.put("flagPostion", item.getId());
		notifyDataSetChanged();
	}

	public void updateDatas(List<JinPaiBigTypeBean> vector) {
		this.datas=vector;
		isSelectMap=new HashMap<>();
		JinPaiBigTypeBean it=datas.get(1);
		isSelectMap.put("flagPostion",it.getId());
		notifyDataSetChanged();
	}

	class HoldView {
		View pBackground,pLine;
		TextView areaName;
		ImageView optionIcon;
	}

}

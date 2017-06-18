package com.xygame.sg.im;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xygame.sg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActionPoilInfoAdapter extends BaseAdapter{
	
	private List<PoiInfo> PoiInfos;
	
	private Context context;
	
	public ActionPoilInfoAdapter(Context context,List<PoiInfo> PoiInfos){
		this.context=context;
		if(PoiInfos==null){
			this.PoiInfos=new ArrayList<PoiInfo>();
		}else{
			this.PoiInfos=PoiInfos;
		}
	}
	
	public void setContacter(List<PoiInfo> PoiInfos) {
		this.PoiInfos=PoiInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PoiInfos.size();
	}

	@Override
	public PoiInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		return PoiInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public final void addItem(PoiInfo item) {

		if (!PoiInfos.contains(item)) {
			PoiInfos.add(item);
		}

	}

	public void clearDatas() {
		PoiInfos.clear();
	}

	public void addDatas(List<PoiInfo> result) {
		if (null != result && !result.isEmpty()) {
			for (PoiInfo item : result) {
				addItem(item);
			}
		}
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		PoiInfo PoiInfo = PoiInfos.get(arg0);
		ChoildHolder childHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.le_action_poilinfo_item, null);
			childHolder = new ChoildHolder();
			childHolder.itemName = (TextView) convertView.findViewById(R.id.itemName);
			childHolder.itemAddreess = (TextView) convertView.findViewById(R.id.itemAddreess);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChoildHolder) convertView.getTag();
		}
		childHolder.itemName.setText(PoiInfo.name);
		childHolder.itemAddreess.setText(PoiInfo.address);
		return convertView;
	}

	class ChoildHolder {
		TextView itemName,itemAddreess;
	}
}

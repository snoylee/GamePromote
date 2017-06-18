package com.xygame.sg.adapter.comm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.CityBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DividGroupAdapter extends BaseAdapter{

	private List<GZ_GroupBeanTemp> datas;
	private Context context;
	private Map<String,String> selectPostion;

	public DividGroupAdapter(Context context, List<GZ_GroupBeanTemp> datas) {
		this.context=context;
		selectPostion=new HashMap<>();
		if(datas!=null){
			this.datas=datas;
			selectPostion.put("constFlag", this.datas.get(0).getId());
		}else{
			this.datas=new ArrayList<GZ_GroupBeanTemp>();
		}
	}

	

	/**
	 * 重载方法
	 * @return
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	/**
	 * 重载方法
	 * @param arg0
	 * @return
	 */
	@Override
	public GZ_GroupBeanTemp getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	/**
	 * 重载方法
	 * @param arg0
	 * @return
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.gz_group_item, null);
			holder.optionIcon=(ImageView)convertView.findViewById(R.id.optionIcon);
			holder.groupName = (TextView)convertView.findViewById(R.id.groupName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		GZ_GroupBeanTemp item=datas.get(position);
		if (selectPostion.get("constFlag").equals(item.getId())){
			holder.optionIcon.setImageResource(R.drawable.gou);
		}else{
			holder.optionIcon.setImageResource(R.drawable.gou_null);
		}
		holder.groupName.setText(item.getName());
		return convertView;
	}

	public void updatePostion(int position) {
		selectPostion.put("constFlag", this.datas.get(position).getId());
		notifyDataSetChanged();
	}

	static class ViewHolder{
		TextView groupName;
		ImageView optionIcon;
	}
}
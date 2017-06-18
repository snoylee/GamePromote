package com.xygame.sg.adapter.comm;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.CityBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter{
	
	private List<CityBean> datas;
	private Context context;

	public CityAdapter(Context context,List<CityBean> datas) {
		this.context=context;
		if(datas!=null){
			CityBean itBean=datas.get(0);
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
				this.datas=datas;
			}
		}else{
			this.datas=new ArrayList<CityBean>();
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
	public CityBean getItem(int arg0) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_editor_provice_item, null);
			holder.arrowsIcon=(ImageView)convertView.findViewById(R.id.arrowsIcon);
			holder.proviceName = (TextView)convertView.findViewById(R.id.proviceName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.arrowsIcon.setVisibility(View.GONE);
		CityBean item=datas.get(position);
		item.get();
		holder.proviceName.setText(item.getCityName());
		return convertView;
	}
	
	static class ViewHolder{
		TextView proviceName;
		ImageView arrowsIcon;
	}
}
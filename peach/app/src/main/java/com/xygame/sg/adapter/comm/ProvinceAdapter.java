package com.xygame.sg.adapter.comm;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProvinceAdapter extends BaseAdapter{
	
	private List<ProvinceBean> datas;
	private Context context;

	public ProvinceAdapter(Context context,List<ProvinceBean> datas) {
		this.context=context;
		if(datas!=null){
			this.datas=datas;
		}else{
			this.datas=new ArrayList<ProvinceBean>();
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
	public ProvinceBean getItem(int arg0) {
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
			holder.bottomLine=convertView.findViewById(R.id.bottomLine);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ProvinceBean item=datas.get(position);
		item.get();
		holder.proviceName.setText(item.getProvinceName());
		String provinceName=item.getProvinceName();
		boolean isStrictCity=false;
		for(String str:Constants.CITY_STRICT){
			if (provinceName.contains(str)) {
				isStrictCity=true;
			}
		}
		if (isStrictCity) {
			holder.arrowsIcon.setVisibility(View.GONE);
		}else{
			holder.arrowsIcon.setVisibility(View.VISIBLE);
		}
		
		if(position==datas.size()-1){
			holder.bottomLine.setVisibility(View.GONE);
		}else{
			holder.bottomLine.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView proviceName;
		ImageView arrowsIcon;
		View bottomLine;
	}
}
package com.xygame.sg.adapter.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ReportBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportAdapter extends BaseAdapter{
	
	private List<ReportBean> datas;
	private Context context;
	private HashMap<String, Integer> sMap;
	private int prePostion;

	public ReportAdapter(Context context,List<ReportBean> datas) {
		this.context=context;
		if(datas!=null){
			this.datas=datas;
		}else{
			this.datas=new ArrayList<ReportBean>();
		}
		sMap=new HashMap<String, Integer>();
		sMap.put("flag", 0);
		prePostion=0;
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
	
	public void addDatas(List<ReportBean> datas){
		this.datas.addAll(datas);
		notifyDataSetChanged();
	}

	/**
	 * 重载方法
	 * @param arg0
	 * @return
	 */
	@Override
	public ReportBean getItem(int arg0) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_report_frist_item_layout, null);
			holder.selectIcon=(ImageView)convertView.findViewById(R.id.selectIcon);
			holder.content = (TextView)convertView.findViewById(R.id.content);
			holder.bottomLine=convertView.findViewById(R.id.bottomLine);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ReportBean item=datas.get(position);
		if(sMap.get("flag")==position){
			holder.selectIcon.setImageResource(R.drawable.gou);
		}else{
			holder.selectIcon.setImageResource(R.drawable.gou_null);
		}
		
		holder.content.setText(item.getReportTxt());
		
		if(position==datas.size()-1){
			holder.bottomLine.setVisibility(View.GONE);
		}else{
			holder.bottomLine.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView content;
		ImageView selectIcon;
		View bottomLine;
	}
	
	public ReportBean getSelectItem(){
		ReportBean it=null;
		for(ReportBean item:datas){
			if(item.isReport()){
				it=item;
			}
		}
		return it;
	}
	
	public void selectItem(int index){
		sMap.put("flag", index);
		if(prePostion==index){
			datas.get(index).setReport(true);
		}else{
			datas.get(index).setReport(true);
			datas.get(prePostion).setReport(false);
		}
		prePostion=index;
		notifyDataSetChanged();
	}
}
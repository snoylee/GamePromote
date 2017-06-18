package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.utils.CalendarUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeleteResumeAdapter extends BaseAdapter{
	
	private List<RsumeBean> datas;
	private Context context;

	public DeleteResumeAdapter(Context context,List<RsumeBean> datas) {
		this.context=context;
		if(datas!=null){
			this.datas=datas;
		}else{
			this.datas=new ArrayList<RsumeBean>();
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
	public RsumeBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}
	
	public List<RsumeBean> getSelectDatas(){
		List<RsumeBean> tempDatas=new ArrayList<RsumeBean>();
		for(RsumeBean it:datas){
			if(it.isReport()){
				tempDatas.add(it);
			}
		}
		return tempDatas;
	}
	
	public List<RsumeBean> getLeaveDatas(List<RsumeBean> deleDatas){
		for(RsumeBean it:deleDatas){
			for(int i=0;i<datas.size();i++){
				if(it.getResumeId().equals(datas.get(i).getResumeId())){
					datas.remove(i);
				}
			}
		}
		return datas;
	}
	
	public List<RsumeBean> getDatas(){
		return datas;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_delete_resume_item, null);
			holder.selectIcon=(ImageView)convertView.findViewById(R.id.selectIcon);
			holder.time_tv = (TextView)convertView.findViewById(R.id.time_tv);
			holder.record_content_tv = (TextView)convertView.findViewById(R.id.record_content_tv);
			holder.bottomLine=convertView.findViewById(R.id.bottomLine);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RsumeBean item=datas.get(position);
		if(item.isReport()){
			holder.selectIcon.setImageResource(R.drawable.gou);
		}else{
			holder.selectIcon.setImageResource(R.drawable.gou_null);
		}
		
		String startTime="",endTime="";
		if(!"null".equals(item.getStartTime())){
			startTime=CalendarUtils.getXieGongDateStr(Long.parseLong(item.getStartTime()));
		}
		if(!"null".equals(item.getEndTime())){
			endTime=CalendarUtils.getXieGongDateStr(Long.parseLong(item.getEndTime()));
		}
		String timer = startTime.concat("—").concat(endTime);
		holder.time_tv.setText(timer);
		holder.record_content_tv.setText(item.getExperDesc());		
		
		if(position==datas.size()-1){
			holder.bottomLine.setVisibility(View.GONE);
		}else{
			holder.bottomLine.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView time_tv,record_content_tv;
		ImageView selectIcon;
		View bottomLine;
	}
	
	public void selectItem(int index){
		datas.get(index).setReport(!datas.get(index).isReport());
		notifyDataSetChanged();
	}
}
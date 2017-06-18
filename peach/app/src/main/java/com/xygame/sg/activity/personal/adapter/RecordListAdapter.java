package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
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

/**
 * Created by xy on 2015/11/19.
 */
public class RecordListAdapter extends BaseAdapter {
    private Context context;
    private List<RsumeBean> datas;
    public RecordListAdapter(Context context,List<RsumeBean> datas) {
        super();
        this.context = context;
        if(datas==null){
        	this.datas=new ArrayList<RsumeBean>();
        }else{
        	this.datas=datas;
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public RsumeBean getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    
    public void addNewBean(RsumeBean bean){
    	datas.add(bean);
    }
    
    public void modifyBean(RsumeBean bean){
    	for(int i=0;i<datas.size();i++){
    		if(bean.getResumeId().equals(datas.get(i).getResumeId())){
    			datas.get(i).setEndTime(bean.getEndTime());
    			datas.get(i).setExperDesc(bean.getExperDesc());
    			datas.get(i).setStartTime(bean.getStartTime());
    		}
    	}
    }
    
    public void addDatas(List<RsumeBean> resumes){
    	datas.addAll(resumes);
    }
    
    public List<RsumeBean> getDatas(){
    	return datas;
    }
    
    public void clearDatas(){
    	datas.clear();
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.record_item, null);
			holder.action_iv=(ImageView)convertView.findViewById(R.id.action_iv);
			holder.time_tv = (TextView)convertView.findViewById(R.id.time_tv);
			holder.record_content_tv = (TextView)convertView.findViewById(R.id.record_content_tv);
			holder.divider_line=convertView.findViewById(R.id.divider_line);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.action_iv.setImageResource(R.drawable.edit_category_icon);
		RsumeBean item=datas.get(position);
		String startTime="",endTime="";
		if(!"null".equals(item.getStartTime())){
			startTime=CalendarUtils.getXieGongYMDStr(Long.parseLong(item.getStartTime()));
		}
		if(!"null".equals(item.getEndTime())){
			endTime=CalendarUtils.getXieGongYMDStr(Long.parseLong(item.getEndTime()));
		}
		String timer = startTime.concat("—").concat(endTime);
		holder.time_tv.setText(timer);
		holder.record_content_tv.setText(item.getExperDesc());		
		if(position==datas.size()-1){
			holder.divider_line.setVisibility(View.GONE);
		}else{
			holder.divider_line.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView time_tv,record_content_tv;
		ImageView action_iv;
		View divider_line;
	}
}

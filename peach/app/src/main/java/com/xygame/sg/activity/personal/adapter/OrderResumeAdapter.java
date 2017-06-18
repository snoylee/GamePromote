package com.xygame.sg.activity.personal.adapter;

import java.util.List;

import com.haarman.listviewanimations.ArrayAdapter;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.utils.CalendarUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xy on 2015/11/19.
 */
public class OrderResumeAdapter extends ArrayAdapter<RsumeBean> {
    private Context context;
    public OrderResumeAdapter(Context context,List<RsumeBean> datas) {
        super(datas);
        this.context = context;
    }

    @Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
    
    public List<RsumeBean> getDatas(){
    	return getAll();
    }
    
    @SuppressWarnings("unused")
	@Override
	public View getView(int position, View mView, ViewGroup parent) {
    	View convertView=null;
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
		holder.action_iv.setImageResource(R.drawable.sg_moving_icon);
		RsumeBean item=getItem(position);
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
		if(position==getCount()-1){
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

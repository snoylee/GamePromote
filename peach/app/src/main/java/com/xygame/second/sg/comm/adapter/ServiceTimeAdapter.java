package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.ServiceTimeBean;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.http.ThreadPool;

import java.util.ArrayList;
import java.util.List;

public class ServiceTimeAdapter extends BaseAdapter {
	private Context context;
	private List<ServiceTimeBean> vector;
	public ServiceTimeAdapter(Context context, List<ServiceTimeBean> vector) {
		this.context = context;
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<>();
		}
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public ServiceTimeBean getItem(int position) {
		return vector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (null == convertView)
		{
			viewHolder = new ViewHolder();
			convertView =LayoutInflater.from(context).inflate(
					R.layout.service_time_item,null);

			viewHolder.timeText = (TextView) convertView.findViewById(R.id.timeText);
			viewHolder.backgroundColor =convertView
					.findViewById(R.id.pBackground);
			viewHolder.innerBackground=convertView.findViewById(R.id.innerBackground);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ServiceTimeBean item=vector.get(position);
		viewHolder.timeText.setText(item.getTime());

		if (item.isUsing()){
			if (item.isSelect()){
				viewHolder.timeText.setTextColor(context.getResources().getColor(R.color.dark_green));
				viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green0);
			}else{
				viewHolder.timeText.setTextColor(context.getResources().getColor(R.color.black));
				viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white0);
			}
			viewHolder.innerBackground.setBackgroundResource(R.drawable.shape_rect_white0);
		}else{
			viewHolder.timeText.setTextColor(context.getResources().getColor(R.color.black));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_gray0);
			viewHolder.innerBackground.setBackgroundResource(R.drawable.shape_rect_dark_gray0);
		}

		return convertView;
	}

	public void updateAreaDatas(List<ServiceTimeBean> areaBeans) {
		this.vector=areaBeans;
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		TextView timeText;
		View backgroundColor,innerBackground;
	}

	public void updateSelect(ServiceTimeBean item) {
		ThreadPool.getInstance().excuseThread(new JugmentUse(item));
	}

	private class JugmentUse implements Runnable {
		private ServiceTimeBean item;
		public JugmentUse(ServiceTimeBean item){
			this.item=item;
		}
		@Override
		public void run() {
			if ("-1".equals(item.getId())){
				boolean isUnUsed=false;
				if (item.isSelect()){
					for (int i=0;i<vector.size()-1;i++){
						if (vector.get(i).isUsing()){
							vector.get(i).setIsSelect(false);
						}else{
							isUnUsed=true;
						}
					}
				}else{
					for (int i=0;i<vector.size()-1;i++){
						if (vector.get(i).isUsing()){
							vector.get(i).setIsSelect(true);
						}else{
							isUnUsed=true;
						}
					}
				}
				vector.get(vector.size()-1).setIsSelect(!item.isSelect());
				vector.get(vector.size()-1).setIsUnUsed(isUnUsed);
			}else{
				boolean tempAllFlag=true;
				for (int i=0;i<vector.size()-1;i++){
					if (vector.get(i).getId()==item.getId()){
						vector.get(i).setIsSelect(!item.isSelect());
					}
					if (!vector.get(i).isSelect()){
						tempAllFlag=false;
					}
				}
				vector.get(vector.size()-1).setIsSelect(tempAllFlag);
			}
			mHandler.sendEmptyMessage(1);
		}
	}

	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					notifyDataSetChanged();
					break;
			}
			super.handleMessage(msg);
		}
	};
}
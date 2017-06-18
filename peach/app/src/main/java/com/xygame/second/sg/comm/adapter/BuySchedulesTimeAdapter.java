package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.ServiceTimeBean;
import com.xygame.sg.R;
import com.xygame.sg.http.ThreadPool;

import java.util.ArrayList;
import java.util.List;

public class BuySchedulesTimeAdapter extends BaseAdapter {
	private Context context;
	private List<ServiceTimeBean> vector;
	public BuySchedulesTimeAdapter(Context context, List<ServiceTimeBean> vector) {
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

	public void updateTimeItem(int position) {
		int leftIndex=position-1;
		int rightIndex=position+1;
		if (leftIndex>=0&&vector.get(leftIndex).isSelect()){
			vector.get(position).setIsSelect(!vector.get(position).isSelect());
		}if (rightIndex<=vector.size()-1&&vector.get(rightIndex).isSelect()){
			vector.get(position).setIsSelect(!vector.get(position).isSelect());
		}else if (leftIndex>=0&&rightIndex<=vector.size()-1&&vector.get(leftIndex).isSelect()&&vector.get(rightIndex).isSelect()){
			Toast.makeText(context,"不能取消中间点",Toast.LENGTH_SHORT).show();
		}else if (leftIndex>=0&&rightIndex<=vector.size()-1&&!vector.get(leftIndex).isSelect()&&!vector.get(rightIndex).isSelect()){
			for (int i=0;i<vector.size();i++){
				if (vector.get(i).isUsing()){
					if (position==i){
						vector.get(i).setIsSelect(!vector.get(i).isSelect());
					}else{
						vector.get(i).setIsSelect(false);
					}
				}
			}
		}else if (leftIndex<0&&vector.get(rightIndex).isSelect()){
			vector.get(position).setIsSelect(!vector.get(position).isSelect());
		}else if (leftIndex<0&&!vector.get(rightIndex).isSelect()){
			for (int i=0;i<vector.size();i++){
				if (vector.get(i).isUsing()){
					if (0==i){
						vector.get(i).setIsSelect(!vector.get(i).isSelect());
					}else{
						vector.get(i).setIsSelect(false);
					}
				}
			}
		}else if (rightIndex==vector.size()&&vector.get(leftIndex).isSelect()){
			vector.get(position).setIsSelect(!vector.get(position).isSelect());
		}else if (rightIndex==vector.size()&&!vector.get(leftIndex).isSelect()){
			for (int i=0;i<vector.size();i++){
				if (vector.get(i).isUsing()){
					if (vector.size()-1==i){
						vector.get(i).setIsSelect(!vector.get(i).isSelect());
					}else{
						vector.get(i).setIsSelect(false);
					}
				}
			}
		}
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		TextView timeText;
		View backgroundColor,innerBackground;
	}

	public void selectItem(int index) {
		for (int i=0;i<vector.size();i++){
			if (index==vector.get(i).getIndex()){
				vector.get(i).setIsSelect(true);
				break;
			}
		}
	}

	public void cancelSelectItem(int index) {
		for (int i=0;i<vector.size();i++){
			if (index==vector.get(i).getIndex()){
				vector.get(i).setIsSelect(false);
				break;
			}
		}
	}
}
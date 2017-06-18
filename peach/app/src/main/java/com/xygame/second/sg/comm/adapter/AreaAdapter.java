package com.xygame.second.sg.comm.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.AreaBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.ArrayList;
import java.util.List;

public class AreaAdapter extends BaseAdapter {
	private Context context;
	private List<CityBean> vector;
	public AreaAdapter(Context context, List<CityBean> vector) {
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
	public CityBean getItem(int position) {
		CityBean item=vector.get(position);
		item.get();
		return item;
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
					R.layout.cub_jp_small_type_item, parent, false);

			viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
			viewHolder.gouView = convertView
					.findViewById(R.id.gouView);
			viewHolder.backgroundColor =convertView
					.findViewById(R.id.backgroundColor);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CityBean item=vector.get(position);
		viewHolder.typeName.setText(item.getName());
		if (item.isSelect()){
			viewHolder.gouView.setVisibility(View.VISIBLE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
		}else{
			viewHolder.gouView.setVisibility(View.GONE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
		}
		return convertView;
	}

	public void updateAreaDatas(List<CityBean> areaBeans) {
		this.vector=areaBeans;
		notifyDataSetChanged();
	}

	public List<CityBean> getAreaDatas(){
		return vector;
	}

	private static class ViewHolder
	{
		TextView typeName;
		View backgroundColor,gouView;
	}

	public void updateSelect(CityBean item) {
		ThreadPool.getInstance().excuseThread(new JugmentUse(item));
	}

	private class JugmentUse implements Runnable {
		private CityBean item;
		public JugmentUse(CityBean item){
			this.item=item;
		}
		@Override
		public void run() {
			if (item.getId()==-1){
				if (item.isSelect()){
					for (int i=1;i<vector.size();i++){
						vector.get(i).setIsSelect(false);
					}
				}else{
					for (int i=1;i<vector.size();i++){
						vector.get(i).setIsSelect(true);
					}
				}
				vector.get(0).setIsSelect(!item.isSelect());
			}else{
				boolean tempAllFlag=true;
				for (int i=1;i<vector.size();i++){
					if (vector.get(i).getId()==item.getId()){
						vector.get(i).setIsSelect(!item.isSelect());
					}
					if (!vector.get(i).isSelect()){
						tempAllFlag=false;
					}
				}
				vector.get(0).setIsSelect(tempAllFlag);
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
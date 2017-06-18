package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;

import java.util.ArrayList;
import java.util.List;

public class AreaShowAdapter extends BaseAdapter {
	private Context context;
	private List<CityBean> vector;
	public AreaShowAdapter(Context context, List<CityBean> vector) {
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
		viewHolder.gouView.setVisibility(View.GONE);
		viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
		viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
		return convertView;
	}

	private static class ViewHolder
	{
		TextView typeName;
		View backgroundColor,gouView;
	}
}
package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.List;

public class JinPaiDetailSmallTypeAdapter extends BaseAdapter {
	private Context context;
	private List<JinPaiSmallTypeBean> vector;
	public JinPaiDetailSmallTypeAdapter(Context context, List<JinPaiSmallTypeBean> vector) {
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
	public JinPaiSmallTypeBean getItem(int position) {
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
		JinPaiSmallTypeBean item=vector.get(position);
		viewHolder.typeName.setText(item.getTypeName());
		if (item.isSelected()){
			viewHolder.gouView.setVisibility(View.VISIBLE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
		}else{
			viewHolder.gouView.setVisibility(View.GONE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
		}
//		convertView.setOnClickListener(new TypeClickListener(item));
		return convertView;
	}

	private static class ViewHolder
	{
		TextView typeName;
		View backgroundColor,gouView;
	}

	private class TypeClickListener implements View.OnClickListener{
		private JinPaiSmallTypeBean item;
		public TypeClickListener(JinPaiSmallTypeBean item){
			this.item=item;
		}

		@Override
		public void onClick(View v) {
			updateSelect(item);
		}
	}

	private void updateSelect(JinPaiSmallTypeBean item) {
		for (int i=0;i<vector.size();i++){
			if (vector.get(i).getTypeId().equals(item.getTypeId())){
				vector.get(i).setIsSelected(!item.isSelected());
				break;
			}
		}
		notifyDataSetChanged();
	}

	public List<JinPaiSmallTypeBean> getSmallTypes(){
		List<JinPaiSmallTypeBean> datas=new ArrayList<>();
		for (JinPaiSmallTypeBean it:vector){
			if (it.isSelected()){
				datas.add(it);
			}
		}
		return datas;
	}
}
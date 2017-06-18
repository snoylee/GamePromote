package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JinPaiSmallTypeAdapter extends BaseAdapter {
	private Context context;
	private List<JinPaiSmallTypeBean> vector;
	private Map<String,String> markMap=new HashMap<>();
	public JinPaiSmallTypeAdapter(Context context, List<JinPaiSmallTypeBean> vector) {
		this.context = context;
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<>();
		}
		String id=this.vector.get(0).getTypeId();
		markMap.put("markMap",id);
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public JinPaiSmallTypeBean getItem(int position) {
		return vector.get(position);
	}

	public JinPaiSmallTypeBean getSelectedItem(){
		JinPaiSmallTypeBean it=null;
		for (JinPaiSmallTypeBean item:vector){
			if (item.getTypeId().equals(markMap.get("markMap"))){
				it=item;
				break;
			}
		}
		return it;
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

		String id=markMap.get("markMap");
		if (item.getTypeId().equals(id)){
			viewHolder.gouView.setVisibility(View.VISIBLE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_dark_green10);
		}else{
			viewHolder.gouView.setVisibility(View.GONE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
			viewHolder.backgroundColor.setBackgroundResource(R.drawable.shape_rect_white);
		}
		convertView.setOnClickListener(new TypeClickListener(item));
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
		markMap.put("markMap",item.getTypeId());
		notifyDataSetChanged();
		notifyDataSetChanged();
	}

	public List<JinPaiSmallTypeBean> getSmallTypes(){
		List<JinPaiSmallTypeBean> datas=new ArrayList<>();
		for (JinPaiSmallTypeBean it:vector){
			if (it.getTypeId().equals(markMap.get("markMap"))){
				datas.add(it);
			}
		}
		return datas;
	}
}
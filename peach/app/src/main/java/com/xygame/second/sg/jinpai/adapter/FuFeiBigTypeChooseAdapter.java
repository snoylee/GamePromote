package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuFeiBigTypeChooseAdapter extends BaseAdapter {
	private Context context;
	private List<JinPaiBigTypeBean> vector;
	private ImageLoader mImageLoader;
	private Map<String,String> markMap=new HashMap<>();
	public FuFeiBigTypeChooseAdapter(Context context, List<JinPaiBigTypeBean> vector) {
		this.context = context;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<>();
		}
//		String id=this.vector.get(0).getId();
//		markMap.put("markMap",id);
	}

	public void updateDatas(List<JinPaiBigTypeBean> vector){
		this.vector=vector;
		notifyDataSetChanged();
	}

	public void updateJinPaiDatas(List<JinPaiBigTypeBean> vector){
		for (int i=0;i<vector.size();i++){
			if (!Constants.DEFINE_LOL_ID.equals(vector.get(i).getId())){
				this.vector.add(vector.get(i));
			}
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public JinPaiBigTypeBean getItem(int position) {
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
					R.layout.cub_jp_big_type_item, parent, false);

			viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
			viewHolder.typeImage = (CircularImage) convertView
					.findViewById(R.id.typeImage);
			viewHolder.selectedBack =convertView
					.findViewById(R.id.selectedBack);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JinPaiBigTypeBean item=vector.get(position);
		viewHolder.typeName.setText(item.getName());
		String id=markMap.get("markMap");
		if (item.getId().equals(id)){
			viewHolder.selectedBack.setVisibility(View.VISIBLE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.dark_green));
		}else{
			viewHolder.selectedBack.setVisibility(View.GONE);
			viewHolder.typeName.setTextColor(context.getResources().getColor(R.color.black));
		}
		mImageLoader.loadOrigaImage(item.getUrl(), viewHolder.typeImage, true);
		return convertView;
	}

	private static class ViewHolder
	{
		TextView typeName;
		CircularImage typeImage;
		View selectedBack;
	}

	public void updateSelect(JinPaiBigTypeBean item) {
		markMap.put("markMap",item.getId());
		notifyDataSetChanged();
	}
}
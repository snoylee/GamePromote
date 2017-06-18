package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.FanErBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JinPaiAdapter extends BaseAdapter {
	private Context context;
	private List<JinPaiBean> vector;
	private ImageLoader mImageLoader;
	public JinPaiAdapter(Context context, List<JinPaiBean> vector) {
		this.context = context;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
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
	public JinPaiBean getItem(int position) {
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
					R.layout.cub_jp_item, parent, false);

			viewHolder.jbNums = (TextView) convertView.findViewById(R.id.jbNums);
			viewHolder.jbLeftTimer = (TextView) convertView.findViewById(R.id.jbLeftTimer);
			viewHolder.jbTitle = (TextView) convertView.findViewById(R.id.jbTitle);
			viewHolder.plusherName = (TextView) convertView.findViewById(R.id.plusherName);
			viewHolder.dqText=convertView.findViewById(R.id.dqText);
			viewHolder.id_img = (ImageView) convertView
					.findViewById(R.id.id_img);
			viewHolder.wqView =convertView
					.findViewById(R.id.wqView);
			viewHolder.ffYView=convertView.findViewById(R.id.ffYView);
			viewHolder.timeCent=convertView.findViewById(R.id.timeCent);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		JinPaiBean item=vector.get(position);
		viewHolder.jbNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
		viewHolder.jbTitle.setText(item.getActTitle());
		viewHolder.plusherName.setText(item.getUsernick());
//		if ("1".equals(item.getActNature())){//1是竞拍2是直接约
//			viewHolder.timeCent.setVisibility(View.GONE);
//			viewHolder.ffYView.setVisibility(View.VISIBLE);
//			viewHolder.dqText.setVisibility(View.VISIBLE);
//			viewHolder.jbLeftTimer.setText(CalendarUtils.getLeftTime(Long.parseLong(item.getBidEndTime()), 0));
//		}else{
//			viewHolder.timeCent.setVisibility(View.VISIBLE);
//			viewHolder.ffYView.setVisibility(View.GONE);
//			viewHolder.dqText.setVisibility(View.GONE);
//		}

		viewHolder.timeCent.setVisibility(View.GONE);
		viewHolder.ffYView.setVisibility(View.VISIBLE);
		viewHolder.dqText.setVisibility(View.VISIBLE);
		viewHolder.jbLeftTimer.setText(CalendarUtils.getLeftTime(Long.parseLong(item.getBidEndTime()), 0));

		mImageLoader.loadImage(item.getShowCoverUrl(), viewHolder.id_img, true);
		return convertView;
	}

	public void clearDatas(){
		this.vector.clear();
		notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		TextView jbNums,jbLeftTimer,jbTitle,plusherName;
		ImageView id_img;
		View wqView,dqText,ffYView,timeCent;
	}

	public void addDatas(List<JinPaiBean> datas, int mCurrentPage) {
		if (mCurrentPage == 1) {
			this.vector = datas;
		} else {
			this.vector.addAll(datas);
		}
		notifyDataSetChanged();
	}
}
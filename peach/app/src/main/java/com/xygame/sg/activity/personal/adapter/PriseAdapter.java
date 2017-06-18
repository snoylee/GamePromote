package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.PriseBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.TimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PriseAdapter extends BaseAdapter {
	private List<PriseBean> strList;
	private Context context;
	private boolean isAll;
	private ImageLoader imageLoader;
	public PriseAdapter(Context context, List<PriseBean> strList,boolean isAll) {
		super();
		this.context = context;
		this.isAll=isAll;
		imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if (strList == null) {
			this.strList = new ArrayList<PriseBean>();
		}else{
			this.strList = strList;
		}
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strList.size();
	}

	@Override
	public PriseBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addDatas(List<PriseBean> datas){
		this.strList.addAll(datas);
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private CircularImage userImage;
		private ImageView imageView;
		private TextView userName,tipText,timerText;
	}

	/**
	 * 添加数据
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_priser_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			viewHolder.tipText = (TextView) convertView
					.findViewById(R.id.tipText);
			viewHolder.timerText = (TextView) convertView
					.findViewById(R.id.timerText);
			viewHolder.userImage =(CircularImage)convertView
					.findViewById(R.id.userImage);
			viewHolder.imageView=(ImageView)convertView.findViewById(R.id.imageView);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(isAll){
			viewHolder.tipText.setVisibility(View.VISIBLE);
			viewHolder.imageView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tipText.setVisibility(View.GONE);
			viewHolder.imageView.setVisibility(View.GONE);
		}
		PriseBean item=strList.get(position);
		viewHolder.userName.setText(item.getUsernick());
		viewHolder.timerText.setText(TimeUtils.formatTime(Long.parseLong(item.getPraiseTimeMillis())));
		imageLoader.loadImage(item.getUserIcon(), viewHolder.userImage, false);
		imageLoader.loadImage(item.getResUrl(), viewHolder.imageView, false);
		return convertView;
	}

}

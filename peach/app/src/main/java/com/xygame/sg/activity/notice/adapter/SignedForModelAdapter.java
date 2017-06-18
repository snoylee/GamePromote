package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.ModelSignedBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SignedForModelAdapter extends BaseAdapter {
	private List<ModelSignedBean> strList;
	private Context context;
	private ImageLoader imageLoader;
	public SignedForModelAdapter(Context context, List<ModelSignedBean> strList) {
		super();
		this.context = context;
		imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if (strList == null) {
			this.strList = new ArrayList<ModelSignedBean>();
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
	public ModelSignedBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addItem(ModelSignedBean item){
		this.strList.add(item);
		notifyDataSetChanged();
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private CircularImage userImage;
		private TextView userName,timerText;
	}

	/**
	 * 添加数据
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.model_signed_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			viewHolder.timerText = (TextView) convertView
					.findViewById(R.id.timerText);
			viewHolder.userImage =(CircularImage)convertView
					.findViewById(R.id.userImage);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ModelSignedBean item=strList.get(position);
		viewHolder.userName.setText(item.getUsernick());
		viewHolder.timerText.setText("签到时间：".concat(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getSignTime()))));
		imageLoader.loadImage(item.getUserIcon(), viewHolder.userImage, true);
		return convertView;
	}

}

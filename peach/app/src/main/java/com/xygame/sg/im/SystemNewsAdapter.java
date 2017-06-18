package com.xygame.sg.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.roundimageview.RoundedImageView;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class SystemNewsAdapter extends BaseAdapter {
	private List<SGNewsBean> strList;
	private Context context;
	private ImageLoader imageLoader;
	public SystemNewsAdapter(Context context, List<SGNewsBean> strList) {
		super();
		this.context = context;
		imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if (strList == null) {
			this.strList = new ArrayList<SGNewsBean>();
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
	public SGNewsBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void clearDatas(){
		strList.clear();
	}
	
	public void addDatas(List<SGNewsBean> datas){
		strList.clear();
		strList.addAll(datas);
		notifyDataSetChanged();
	}
	/**
	 * 添加数据
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView;
		final SGNewsBean message = strList.get(position);
		if (convertView == null) {
			holdView = new HoldView();
			convertView = LayoutInflater.from(context).inflate(R.layout.show_system_item, null);
			holdView.from_head = (CircularImage) convertView.findViewById(R.id.from_head);
			holdView.formclient_row_date = (TextView) convertView.findViewById(R.id.formclient_row_date);
			holdView.formclient_row_msg = (TextView) convertView.findViewById(R.id.formclient_row_msg);
			convertView.setTag(holdView);
		}else{
			holdView = (HoldView) convertView.getTag();
		}
		holdView.formclient_row_date.setText(TimeUtils.formatTime(Long.parseLong(message.getTimestamp())));
		holdView.from_head.setImageResource(R.drawable.new_system_icon);
		String ss = message.getMsgContent();
		holdView.formclient_row_msg.setText(ss);
		return convertView;
	}

	class HoldView {
		TextView formclient_row_date,formclient_row_msg;
		CircularImage from_head;
	}
}

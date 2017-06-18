package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.StyleBean;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class OtherAdapter extends BaseAdapter {
	private Context context;
	public List<StyleBean> channelList;
	/** 是否可见 */
	boolean isVisible = true;
	/** 要删除的position */
	public int remove_position = -1;

	public OtherAdapter(Context context, List<StyleBean> channelList) {
		this.context = context;
		if (channelList == null) {
			this.channelList = new ArrayList<StyleBean>();
		} else {
			this.channelList = channelList;
		}
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public StyleBean getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.sg_sytle_item, null);
		RoundTextView styleText = (RoundTextView) view.findViewById(R.id.styleText);
		StyleBean item = channelList.get(position);
		styleText.setText(item.getStyleName());
		styleText.setTextColor(Color.rgb(item.getColorR(), item.getColorG(), item.getColorB()));
		styleText.getDelegate().setStrokeColor(Color.rgb(item.getColorR(), item.getColorG(), item.getColorB()));

		if (!isVisible && (position == -1 + channelList.size())) {
			styleText.setText("");
			styleText.setTextColor(0);
			styleText.getDelegate().setStrokeColor(0);
		}
		if (remove_position == position) {
			styleText.setText("");
			styleText.setTextColor(0);
			styleText.getDelegate().setStrokeColor(0);
		}
		return view;
	}

	/** 获取频道列表 */
	public List<StyleBean> getChannnelLst() {
		return channelList;
	}

	/** 添加频道列表 */
	public void addItem(StyleBean channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
		// notifyDataSetChanged();
	}

	/** 删除频道列表 */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	/** 设置频道列表 */
	public void setListDate(List<StyleBean> list) {
		channelList = list;
	}

	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}

	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
}
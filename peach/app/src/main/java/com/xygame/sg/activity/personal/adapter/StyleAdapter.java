package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.StyleBean;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by xy on 2015/11/16. 资料页面的adapter
 */
public class StyleAdapter extends BaseAdapter {
	private Context context;
	private List<StyleBean> datas;

	/** TAG */
	private final static String TAG = "DragAdapter";
	/** 是否显示底部的ITEM */
	private boolean isItemShow = false;
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;
	/** 是否可见 */
	boolean isVisible = true;
	/** 要删除的position */
	public int remove_position = -1;

	public StyleAdapter(Context context, List<StyleBean> datas) {
		super();
		this.context = context;
		if (datas == null) {
			this.datas = new ArrayList<StyleBean>();
		} else {
			this.datas = datas;
		}
	}

	public List<StyleBean> getSelectedDatas(){
		return datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public StyleBean getItem(int i) {
		return datas.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	/**
	 * 添加数据
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.sg_sytle_item, null);
		RoundTextView styleText = (RoundTextView) view.findViewById(R.id.styleText);
		StyleBean item = datas.get(position);
		styleText.setText(item.getStyleName());
		styleText.setTextColor(Color.rgb(item.getColorR(), item.getColorG(), item.getColorB()));
		styleText.getDelegate().setStrokeColor(Color.rgb(item.getColorR(), item.getColorG(), item.getColorB()));

		// if ((position == 0) || (position == 1)){
		//// item_text.setTextColor(context.getResources().getColor(R.color.black));
		// viewHolder.styleText.setEnabled(false);
		// }
		if (isChanged && (position == holdPosition) && !isItemShow) {
			styleText.setText("");
			styleText.setTextColor(0);
			styleText.getDelegate().setStrokeColor(0);
			styleText.setSelected(true);
			styleText.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + datas.size())) {
			styleText.setText("");
			styleText.setTextColor(0);
			styleText.getDelegate().setStrokeColor(0);
			styleText.setSelected(true);
			styleText.setEnabled(true);
		}
		if (remove_position == position) {
			styleText.setText("");
			styleText.setTextColor(0);
			styleText.getDelegate().setStrokeColor(0);
		}
		return view;
	}

	/** 添加频道列表 */
	public void addItem(StyleBean channel) {
		datas.add(channel);
		notifyDataSetChanged();
	}

	/** 拖动变更频道排序 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		StyleBean dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
		if (dragPostion < dropPostion) {
			datas.add(dropPostion + 1, dragItem);
			datas.remove(dragPostion);
		} else {
			datas.add(dropPostion, dragItem);
			datas.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	/** 获取频道列表 */
	public List<StyleBean> getChannnelLst() {
		return datas;
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** 删除频道列表 */
	public void remove() {
		datas.remove(remove_position);
		remove_position = -1;
		notifyDataSetChanged();
	}

	/** 设置频道列表 */
	public void setListDate(List<StyleBean> list) {
		datas = list;
	}

	/** 获取是否可见 */
	public boolean isVisible() {
		return isVisible;
	}

	/** 设置是否可见 */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** 显示放下的ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}

package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.PopAutoBean;
import com.xygame.second.sg.jinpai.PopSexBean;
import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.List;

public class PopAutoAdater extends BaseAdapter {

	private Context context;
	private List<PopAutoBean> datas;

	public PopAutoAdater(Context context, List<PopAutoBean> datas) {
		this.context = context;
		if (datas == null) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
		}
	}

	public void updateDatas( List<PopAutoBean> datas){
		this.datas=datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public PopAutoBean getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView holdView;
		if (view == null) {
			holdView = new HoldView();
			view = LayoutInflater.from(context).inflate(
					R.layout.pop_list_item, null);
			holdView.blockName = (TextView) view.findViewById(R.id.itemText);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		PopAutoBean item = datas.get(position);
		holdView.blockName.setText(item.getName());
		return view;
	}

	class HoldView {
		TextView blockName;
	}
}

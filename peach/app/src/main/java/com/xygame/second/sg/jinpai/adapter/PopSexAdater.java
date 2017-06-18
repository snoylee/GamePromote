package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.PopSexBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopSexAdater extends BaseAdapter {

	private Context context;
	private List<PopSexBean> datas;

	public PopSexAdater(Context context, List<PopSexBean> datas) {
		this.context = context;
		if (datas == null) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
		}
	}

	public void updateDatas(List<PopSexBean> datas){
		this.datas=datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public PopSexBean getItem(int position) {
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

		PopSexBean item = datas.get(position);
		holdView.blockName.setText(item.getSex());
		return view;
	}

	class HoldView {
		TextView blockName;
	}
}

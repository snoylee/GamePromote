package com.xygame.sg.activity.testpay;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;

public class TestListAdapter extends BaseAdapter{
	
	private Context context;
	private List<Integer> datas;
	
	public TestListAdapter(Context context,List<Integer> datas) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.datas=datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addDatas(List<Integer> ds){
		datas.addAll(datas);
	}
	
	public void clearDatas(){
		datas.clear();
	}

	private class ViewHolder {
		private TextView text;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.sg_test_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.text);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text.setText(datas.get(position)+"");
		return convertView;
	}

}

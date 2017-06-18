package com.xygame.second.sg.personal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.personal.bean.PayMoneyBean;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.FanErBean;

import java.util.ArrayList;
import java.util.List;

public class MoneyStoneAdapter extends BaseAdapter {
	private List<PayMoneyBean> strList;
	private Context context;
	public MoneyStoneAdapter(Context context, List<PayMoneyBean> strList) {
		super();
		this.context = context;
		if (strList == null) {
			this.strList = new ArrayList<PayMoneyBean>();
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
	public PayMoneyBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void addAllDatas(List<PayMoneyBean> datas) {
		this.strList=datas;
		notifyDataSetChanged();
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private View payButton;
		private TextView stoneNums,payValue;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.second_paystone_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.stoneNums=(TextView)convertView.findViewById(R.id.stoneNums);
			viewHolder.payValue=(TextView)convertView.findViewById(R.id.payValue);
			viewHolder.payButton=convertView.findViewById(R.id.payButton);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PayMoneyBean item=strList.get(position);
		viewHolder.stoneNums.setText(item.getVirMoney());
		viewHolder.payValue.setText(item.getMoney());
		return convertView;
	}
}
package com.xygame.sg.adapter.comm;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.bean.QueryNoticesListBean;

public class ActionOptionAdapter extends BaseAdapter{
	
	private List<ShootTypeBean> ActionOptionBeans;
	private QueryNoticesListBean queryNoticesListBean;
	private Context context;
	
	public ActionOptionAdapter(Context context,List<ShootTypeBean> ActionOptionBeans,QueryNoticesListBean queryNoticesListBean){
		this.context=context;
		this.queryNoticesListBean=queryNoticesListBean;
		if(ActionOptionBeans==null){
			this.ActionOptionBeans=new ArrayList<ShootTypeBean>();
		}else{
			this.ActionOptionBeans=ActionOptionBeans;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ActionOptionBeans.size();
	}

	@Override
	public ShootTypeBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return ActionOptionBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ShootTypeBean ActionOptionBean = ActionOptionBeans.get(arg0);
		ChoildHolder childHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.le_action_option_list_item, null);
			childHolder = new ChoildHolder();
			childHolder.itemName = (TextView) convertView.findViewById(R.id.itemName);
			childHolder.selected_iv=convertView.findViewById(R.id.selected_iv);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChoildHolder) convertView.getTag();
		}
		childHolder.itemName.setText(ActionOptionBean.getTypeName());
		if (ActionOptionBean.getTypeId()==queryNoticesListBean.getShootType()){
			childHolder.itemName.setTextColor(context.getResources().getColor(R.color.dark_green));
			childHolder.selected_iv.setVisibility(View.VISIBLE);
		}else{
			childHolder.itemName.setTextColor(context.getResources().getColor(R.color.black));
			childHolder.selected_iv.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ChoildHolder {
		TextView itemName;
		View selected_iv;
	}
}

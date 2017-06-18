package com.xygame.second.sg.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.personal.bean.StoneMXItemBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.MingXiBean;
import com.xygame.sg.bean.circle.CircleBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class StoneMingXiAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<StoneMXItemBean> datas;

	public StoneMingXiAdapter(Context context, LayoutInflater inflater, List<StoneMXItemBean> datas) {
		this.inflater = inflater;
		this.context=context;
		if (datas != null) {
			this.datas = datas;
		} else {
			this.datas = new ArrayList<StoneMXItemBean>();
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public StoneMXItemBean getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void clearDatas(){
		this.datas.clear();
	}

	public List<StoneMXItemBean> getAllDatas(){
		return datas;
	}

	public void addDatas(List<StoneMXItemBean> datas,int mCurrentPage) {
		if (mCurrentPage==1){
			this.datas=datas;
		}else {
			this.datas.addAll(datas);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.stone_mingxi_item, null);
			holder = new ViewHolder();
			holder.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
			holder.orderNo = (TextView) convertView.findViewById(R.id.orderNo);
			holder.dateTime = (TextView) convertView.findViewById(R.id.dateTime);
			holder.priceValue = (TextView) convertView.findViewById(R.id.priceValue);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StoneMXItemBean MingXiBean = datas.get(position);
		switch (MingXiBean.getDealType()) {
		case 1:
			holder.iconImage.setImageResource(R.drawable.qb_mx_cz);
			break;
		case 2:
			holder.iconImage.setImageResource(R.drawable.qb_mx_tx);
			break;
		default:
			if (MingXiBean.getFinanceType()==1){
				holder.iconImage.setImageResource(R.drawable.qb_zhifu);
			}else{
				holder.iconImage.setImageResource(R.drawable.qb_shouru);
			}
			break;
		}
		
		holder.orderNo.setText(MingXiBean.getDealDesc());
		holder.dateTime.setText(CalendarUtils.getHenGongDateDis(MingXiBean.getDealTime()));
		if(MingXiBean.getFinanceType()==1){//1表示支出，2表示收入
			holder.priceValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(MingXiBean.getAmount()))));
			holder.priceValue.setTextColor(context.getResources().getColor(R.color.red));
		}else if(MingXiBean.getFinanceType()==2){
			holder.priceValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(MingXiBean.getAmount()))));
			holder.priceValue.setTextColor(context.getResources().getColor(R.color.green));
		}
		
		return convertView;
	}

	private class ViewHolder {
		private ImageView iconImage;
		private TextView orderNo, dateTime, priceValue;
	}
}

package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.MingXiBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MingXiAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<MingXiBean> datas;

	public MingXiAdapter(Context context,LayoutInflater inflater, List<MingXiBean> datas) {
		this.inflater = inflater;
		this.context=context;
		if (datas != null) {
			this.datas = datas;
		} else {
			this.datas = new ArrayList<MingXiBean>();
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public MingXiBean getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void clearDatas(){
		this.datas.clear();
	}
	
	public void addDatas(List<MingXiBean> datas){
		this.datas.addAll(datas);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_custom_title_listview_section_item, null);
			holder = new ViewHolder();
			holder.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
			holder.orderNo = (TextView) convertView.findViewById(R.id.orderNo);
			holder.dateTime = (TextView) convertView.findViewById(R.id.dateTime);
			holder.priceValue = (TextView) convertView.findViewById(R.id.priceValue);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MingXiBean MingXiBean = datas.get(position);
		switch (Integer.parseInt(MingXiBean.getDealType())) {
		case 1:
			holder.iconImage.setImageResource(R.drawable.qb_mx_cz);
			break;
		case 2:
			holder.iconImage.setImageResource(R.drawable.qb_mx_cz);
			break;

		case 3:
			holder.iconImage.setImageResource(R.drawable.qb_xiaofei);
			break;
		case 4:
			holder.iconImage.setImageResource(R.drawable.qb_mx_tx);
			break;
		case 5:
			holder.iconImage.setImageResource(R.drawable.qb_mx_tx);
			break;
		case 6:
			holder.iconImage.setImageResource(R.drawable.qb_mx_yf);
			break;
		case 7:
			holder.iconImage.setImageResource(R.drawable.qb_shouru);
			break;
		case 8:
			holder.iconImage.setImageResource(R.drawable.qb_moren);
			break;
		case 9:
			holder.iconImage.setImageResource(R.drawable.qb_shouxufei);
			break;
		default:
			break;
		}
		
		holder.orderNo.setText("订单号：".concat(MingXiBean.getChangeRecordId()));
		holder.dateTime.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(MingXiBean.getDealTime())));
		if("1".equals(MingXiBean.getFinanceType())){
			holder.priceValue.setText("-".concat(String.valueOf(StringUtil.getPrice(Long.parseLong(MingXiBean.getAmount())))));
			holder.priceValue.setTextColor(context.getResources().getColor(R.color.black));
		}else{
			holder.priceValue.setText("+".concat(String.valueOf(StringUtil.getPrice(Long.parseLong(MingXiBean.getAmount())))));
			holder.priceValue.setTextColor(context.getResources().getColor(R.color.green));
		}
		
		return convertView;
	}

	private class ViewHolder {
		private ImageView iconImage;
		private TextView orderNo, dateTime, priceValue;
	}
}

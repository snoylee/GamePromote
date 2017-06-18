package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.ServiceTimeBean;
import com.xygame.second.sg.comm.bean.ServiceTimeDateBean;
import com.xygame.sg.R;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDateAdater extends BaseAdapter {
	private Context context;
	private List<ServiceTimeDateBean> datas;
	private Map<String, String> isSelectMap;
	public ServiceDateAdater(Context context, List<ServiceTimeDateBean> datas) {
		this.context = context;
		isSelectMap=new HashMap<>();
		if (null == datas) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ServiceTimeDateBean getItem(int position) {
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
					R.layout.service_time_date_item, null);
			holdView.weekText = (TextView) view.findViewById(R.id.weekText);
			holdView.pBackground=view.findViewById(R.id.pBackground);
			holdView.dateText=(TextView)view.findViewById(R.id.dateText);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		ServiceTimeDateBean item=datas.get(position);
		holdView.dateText.setText(item.getDate());
		holdView.weekText.setText(item.getWeekened());
		if (item.getId().equals(isSelectMap.get("flagPostion"))){
			holdView.pBackground.setBackgroundResource(R.drawable.shape_rect_dark_green);
			holdView.dateText.setTextColor(context.getResources().getColor(R.color.dark_green));
			holdView.weekText.setTextColor(context.getResources().getColor(R.color.dark_green));
		}else{
			holdView.pBackground.setBackgroundResource(R.drawable.shape_rect_light_gray);
			holdView.dateText.setTextColor(context.getResources().getColor(R.color.black));
			holdView.weekText.setTextColor(context.getResources().getColor(R.color.black));
		}

		return view;
	}

	public void setCurTrue(ServiceTimeDateBean item){
		isSelectMap.put("flagPostion", item.getId());
		notifyDataSetChanged();
	}

	public List<ServiceTimeDateBean> getDateDatas(){
		return datas;
	}

	public void updateDatas(List<ServiceTimeDateBean> vector) {
		this.datas=vector;
		ServiceTimeDateBean item=vector.get(0);
		isSelectMap.put("flagPostion", item.getId());
		notifyDataSetChanged();
	}

	public void updateItem(ServiceTimeDateBean currDateBean) {
		for (int i=0;i<datas.size();i++){
			if (currDateBean.getDate().equals(datas.get(i).getDate())){
				datas.get(i).setFromPoint(0);
				datas.get(i).setToPoint(0);
				List<ServiceTimeBean> temples=datas.get(i).getTimeBeans();
				for (int j=0;j<temples.size();j++){
					if(temples.get(j).isUsing()){
						temples.get(j).setIsSelect(false);
						temples.get(j).setIsUsed(false);
						temples.get(j).setIsUsing(true);
					}
				}
				datas.get(i).setTimeBeans(temples);
			}
		}
	}

	class HoldView {
		View pBackground;
		TextView dateText,weekText;
	}

}

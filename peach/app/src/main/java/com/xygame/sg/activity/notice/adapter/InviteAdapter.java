package com.xygame.sg.activity.notice.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.update.UpdateStatus;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.activity.notice.bean.InviteBean;
import com.xygame.sg.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

public class InviteAdapter extends BaseAdapter {
	private List<InviteBean> strList;
	private Context context;

	public InviteAdapter(Context context, List<InviteBean> strList) {
		super();
		this.context = context;
		if (strList == null) {
			this.strList = new ArrayList<InviteBean>();
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
	public InviteBean getItem(int arg0) {
		// TODO Auto-generated method stub
		return strList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	public void addDatas(List<InviteBean> datas){
		this.strList.clear();
		this.strList.addAll(datas);
		notifyDataSetChanged();
	}
	
	public void clearDatas(){
		this.strList.clear();
	}
	
	public List<InviteBean> getSelectedDatas(){
		List<InviteBean> datas=new ArrayList<>();
		for (InviteBean item:strList){
			if (item.isSelect()){
				datas.add(item);
			}
		}
		return datas;
	}
	
	public InviteBean getInviteBean(){
		InviteBean it=null;
		for(InviteBean item:strList){
			if(item.isSelect()){
				it=item;
			}
		}
		return it;
	}
	
	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private TextView noticeSubject,timer,buttonView;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.invite_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.timer = (TextView) convertView
					.findViewById(R.id.timer);
			viewHolder.noticeSubject =(TextView)convertView
					.findViewById(R.id.noticeSubject);
			viewHolder.buttonView=(TextView)convertView.findViewById(R.id.buttonView);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		InviteBean item=strList.get(position);
		if("0".equals(item.getFlag())){//未发送
			viewHolder.buttonView.setBackgroundResource(R.drawable.shape_rect_dark_green);
			viewHolder.buttonView.setTextColor(context.getResources().getColor(R.color.white));
			if (item.isSelect()){
				viewHolder.buttonView.setText("取消");
			}else{
				viewHolder.buttonView.setText("邀请");
			}
			viewHolder.buttonView.setOnClickListener(new UpdateStatus(item,position));
		}else if("1".equals(item.getFlag())){//已发送
			viewHolder.buttonView.setBackgroundResource(R.drawable.shape_rect_input_white);
			viewHolder.buttonView.setTextColor(context.getResources().getColor(R.color.black));
			viewHolder.buttonView.setText("已邀请");
		}
		viewHolder.noticeSubject.setText(item.getSubject());
		viewHolder.timer.setText(CalendarUtils.getXieGongDateDis(Long.parseLong(item.getStartTime())).concat("—").concat(CalendarUtils.getXieGongDateDis(Long.parseLong(item.getEndTime()))));
		return convertView;
	}

	private class UpdateStatus implements View.OnClickListener{
		private InviteBean item;
		private int index;
		public UpdateStatus(InviteBean item,int index){
			this.item=item;
			this.index=index;
		}

		@Override
		public void onClick(View v) {
			changeStatus(item,index);
		}
	}

	private void changeStatus(InviteBean item, int index) {
		strList.get(index).setIsSelect(!strList.get(index).isSelect());
		notifyDataSetChanged();
	}
}

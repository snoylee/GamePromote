package com.xygame.sg.define.photogrid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.utils.CalendarUtils;

public class PhotoEditorAdapter extends BaseAdapter {
	private Context context;
	private List<PhotoesTotalBean> vector;

	/**
	 * 搜索Adapter初始化
	 */
	public PhotoEditorAdapter(Context context,List<PhotoesTotalBean> vector) {
		this.context = context;
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<PhotoesTotalBean>();
		}
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public PhotoesTotalBean getItem(int position) {
		return vector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addPhotoes(List<PhotoesTotalBean> datas){
		vector.addAll(datas);
	}
	
	public void deletePhotoes(List<PhotoesTotalBean> datas){
		vector.clear();
		vector.addAll(datas);		
	}
	
	public void delete(int prIndex){
		vector.remove(prIndex);
	}
	
	public void deleteItems(int prIndex,int index){
		vector.get(prIndex).getImageObjects().remove(index);
	}
	
	public List<PhotoesTotalBean> getDatas(){
		return vector;
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private TextView timerText;
		private GridView contentGridView;
		private View bottomLine;
		private ImageView selectTotalIcon;
	}

	/**
	 * 添加数据
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gridview_listview,
					null);
			viewHolder = new ViewHolder();
			viewHolder.timerText = (TextView) convertView
					.findViewById(R.id.timerText);
			viewHolder.contentGridView = (GridView) convertView
					.findViewById(R.id.contentGridView);
			viewHolder.bottomLine = convertView
					.findViewById(R.id.bottomLine);
			viewHolder.selectTotalIcon=(ImageView)convertView.findViewById(R.id.selectTotalIcon);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PhotoesTotalBean item=vector.get(position);
		viewHolder.timerText.setText(CalendarUtils.getDateStr(Long.parseLong(item.getDateTimer())));
		if(item.getIsSelect()){
			viewHolder.selectTotalIcon.setImageResource(R.drawable.gou);
		}else{
			viewHolder.selectTotalIcon.setImageResource(R.drawable.gou_null);
		}
		if(position==vector.size()-1){
			viewHolder.bottomLine.setVisibility(View.GONE);
		}else{
			viewHolder.bottomLine.setVisibility(View.VISIBLE);
		}
		final List<PhotoesSubBean> imageUrls=item.getImageObjects();
		GridViewSelectAdapter gridAdapter=new GridViewSelectAdapter(context, imageUrls);
		viewHolder.contentGridView .setAdapter(gridAdapter);
		viewHolder.contentGridView.setOnItemClickListener(new OnGridItemListener(gridAdapter,position));
		viewHolder.selectTotalIcon.setOnClickListener(new OnGridClickListener(gridAdapter,position));
		return convertView;
	}
	
	class OnGridClickListener implements OnClickListener{
		private GridViewSelectAdapter gridAdapter;
		private int index;
		
		public OnGridClickListener(GridViewSelectAdapter gridAdapter,int index) {
			// TODO Auto-generated constructor stub
			this.gridAdapter=gridAdapter;
			this.index=index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			vector.get(index).setIsSelect(!vector.get(index).getIsSelect());
			boolean flag=vector.get(index).getIsSelect();
			gridAdapter.updateState(flag);
			gridAdapter.notifyDataSetChanged();
			vector.get(index).setImageObjects(gridAdapter.getImaqges());
			notifyDataSetChanged();
		}
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
	
	class OnGridItemListener implements OnItemClickListener{
		private int index;
		private GridViewSelectAdapter adapter;
		
		public OnGridItemListener(GridViewSelectAdapter adapter,int index) {
			// TODO Auto-generated constructor stub
			this.adapter=adapter;
			this.index=index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			adapter.changeState(arg2);
			adapter.notifyDataSetChanged();
			List<PhotoesSubBean> tempDatas=adapter.getImaqges();
			boolean isAll=true,preIsAll=vector.get(index).getIsSelect();
			for(PhotoesSubBean it:tempDatas){
				if(!it.getIsSelect()){
					isAll=false;
				}
			}
			vector.get(index).setImageObjects(tempDatas);
			vector.get(index).setIsSelect(isAll);
			if(preIsAll!=isAll){
				notifyDataSetChanged();
			}
		}
		
	}
}
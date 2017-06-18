package com.xygame.sg.define.photogrid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.TransFerImagesBean;
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;

public class PhotoAdapter extends BaseAdapter {
	private Context context;
	private List<PhotoesTotalBean> vector;
	private String glaryId;

	/**
	 * 搜索Adapter初始化
	 */
	public PhotoAdapter(Context context,List<PhotoesTotalBean> vector,String glaryId) {
		this.context = context;
		this.glaryId=glaryId;
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
	
	public List<PhotoesTotalBean> getDatas(){
		return vector;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addPhotoes(List<PhotoesTotalBean> datas){
		vector.addAll(datas);
	}
	
	public void addItem(PhotoesTotalBean datas){
		boolean flag=true;
		for(int i=0;i<vector.size();i++){
			if(CalendarUtils.getDateStr(Long.parseLong(vector.get(i).getDateTimer())).equals(CalendarUtils.getDateStr(Long.parseLong(datas.getDateTimer())))){
				List<PhotoesSubBean> yuandata=vector.get(i).getImageObjects();
				List<PhotoesSubBean> xindata=datas.getImageObjects();
				yuandata.addAll(xindata);
				vector.get(i).setImageObjects(yuandata);
				flag=false;
			}
		}
		if(flag){
			List<PhotoesTotalBean> tempVector=new ArrayList<PhotoesTotalBean>();
			tempVector.add(datas);
			tempVector.addAll(vector);
			vector.clear();
			vector.addAll(tempVector);
		}
		notifyDataSetChanged();
	}
	
	public void deletePhotoes(List<PhotoesTotalBean> datas){
		vector.clear();
		vector.addAll(datas);		
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private GridView contentGridView;
		private View bottomLine;
		private ImageView selectTotalIcon;
		private TextView timerText;
		private GridViewUnselectAdapter gridAdapter;
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
			viewHolder.bottomLine =convertView
					.findViewById(R.id.bottomLine);
			viewHolder.selectTotalIcon=(ImageView)convertView.findViewById(R.id.selectTotalIcon);
			viewHolder.gridAdapter=new GridViewUnselectAdapter(context, null);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(position==vector.size()-1){
			viewHolder.bottomLine.setVisibility(View.GONE);
		}else{
			viewHolder.bottomLine.setVisibility(View.VISIBLE);
		}
		viewHolder.selectTotalIcon.setImageResource(R.drawable.sg_timeline_icon);
		
		final List<String> imageUrls=new ArrayList<String>();
		final List<PhotoesSubBean> subDatas=vector.get(position).getImageObjects();
		for(PhotoesSubBean it:subDatas){
			if (it.getUrl()!=null){
				imageUrls.add(it.getUrl());
			}else{
				imageUrls.add(it.getImageUrls());
			}

		}
		viewHolder.timerText.setText(CalendarUtils.getDateStr(Long.parseLong(vector.get(position).getDateTimer())));
		viewHolder.contentGridView .setAdapter(viewHolder.gridAdapter);
		viewHolder.gridAdapter.updatePhotoes(imageUrls);
		viewHolder.gridAdapter.notifyDataSetChanged();
		viewHolder.contentGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TransFerImagesBean transBean=new TransFerImagesBean();
				transBean.setSubDatas(subDatas);
				Constants.imageBrowerSetFengMain(context,
						arg2, imageUrls
								.toArray(new String[imageUrls.size()]), true, glaryId,transBean);
			}
		});
		return convertView;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
	
}
package com.xygame.sg.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CirclePriserAdapter extends BaseAdapter {
	private Context context;
	private List<CirclePraisers> vector;
	private ImageLoader mImageLoader;
	public CirclePriserAdapter(Context context, List<CirclePraisers> vector) {
		this.context = context;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<CirclePraisers>();
		}
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public CirclePraisers getItem(int position) {
		return vector.get(position);
	}
	
	public void addDatas(List<CirclePraisers> datas,int flag){
		vector.clear();
		if (flag==9){
			for(int i=0;i<9;i++){
				vector.add(datas.get(i));
			}
		}else{
			vector.addAll(datas);
		}
	}

	public void addDatas(List<CirclePraisers> datas){
		vector.clear();
		if (datas.size()>6){
			for(int i=0;i<6;i++){
				vector.add(datas.get(i));
			}
		}else{
			this.vector=datas;
		}
		notifyDataSetChanged();
	}

	public void addAllDatas(List<CirclePraisers> datas){
		vector.addAll(datas);
		notifyDataSetChanged();
	}

	public void updateAllDatas(List<CirclePraisers> datas){
		this.vector=datas;
		notifyDataSetChanged();
	}
	
	public List<CirclePraisers> getDatas(){
		return vector;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView =LayoutInflater.from(context).inflate(
					R.layout.circle_priser_item, parent, false);
		}
		CircularImage imageview = (CircularImage) convertView
				.findViewById(R.id.id_img);
		mImageLoader.loadImage(getItem(position).getUserIcon(), imageview, true);
		return convertView;
	}

	public void addMember(CirclePraisers item) {
		vector.add(0,item);
		notifyDataSetChanged();
	}

	public void removingMember(String userId) {
		for (int i=0;i<vector.size();i++){
			if (userId.equals(vector.get(i).getUserId())){
				vector.remove(i);
				break;
			}
		}
		notifyDataSetChanged();
	}

	public void addUserDatas(List<UserBeanInfo> userBeanInfos) {
		for (UserBeanInfo item:userBeanInfos){
			CirclePraisers it=new CirclePraisers();
			it.setUserIcon(item.getUserImage());
			it.setUserId(item.getUserId());
			vector.add(it);
		}
		notifyDataSetChanged();
	}

	public void reovingUserDatas(List<UserBeanInfo> userBeanInfos) {
		for (UserBeanInfo item:userBeanInfos){
			for (int i=0;i<vector.size();i++){
				if (item.getUserId().equals(vector.get(i).getUserId())){
					vector.remove(i);
				}
			}
		}
		notifyDataSetChanged();
	}
}
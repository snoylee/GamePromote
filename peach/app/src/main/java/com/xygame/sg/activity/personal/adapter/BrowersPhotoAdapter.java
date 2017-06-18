package com.xygame.sg.activity.personal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.image.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class BrowersPhotoAdapter extends BaseAdapter {
	private Context context;
	private List<BrowerPhotoesBean> vector;
	private ImageLoader mImageLoader;
	public BrowersPhotoAdapter(Context context,List<BrowerPhotoesBean> vector) {
		this.context = context;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<BrowerPhotoesBean>();
		}
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public BrowerPhotoesBean getItem(int position) {
		return vector.get(position);
	}
	
	public void addDatas(List<BrowerPhotoesBean> datas){
		vector.clear();
		vector.addAll(datas);
	}
	
	public List<BrowerPhotoesBean> getDatas(){
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
					R.layout.item_fragment_list_imgs, parent, false);
		}
		ImageView imageview = (ImageView) convertView
				.findViewById(R.id.id_img);
		mImageLoader.loadImage(getItem(position).getImageUrl(), imageview, false);
		return convertView;
	}
}
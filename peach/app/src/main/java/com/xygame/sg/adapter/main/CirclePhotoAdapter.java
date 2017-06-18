package com.xygame.sg.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.bean.circle.CircleRess;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CirclePhotoAdapter extends BaseAdapter {
	private Context context;
	private List<CircleRess> vector;
	private ImageLoader mImageLoader;
	public CirclePhotoAdapter(Context context, List<CircleRess> vector) {
		this.context = context;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<CircleRess>();
		}
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public CircleRess getItem(int position) {
		return vector.get(position);
	}
	
	public void addDatas(List<CircleRess> datas){
		vector.clear();
		vector.addAll(datas);
	}
	
	public List<CircleRess> getDatas(){
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
		mImageLoader.loadImage(getItem(position).getResUrl(), imageview, false);
		return convertView;
	}
}
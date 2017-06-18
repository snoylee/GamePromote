package com.xygame.sg.define.photogrid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;

public class GridViewUnselectAdapter extends BaseAdapter{
	private Context context;
	private List<String> imageUrls;
	private ImageLoader imageLoader;
	public GridViewUnselectAdapter(Context context,List<String> imageUrls) {
		// TODO Auto-generated constructor stub
		this.context=context;
		imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(imageUrls==null){
			this.imageUrls=new ArrayList<String>();
		}else{
			this.imageUrls=imageUrls;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}
	
	public void updatePhotoes(List<String> datas){
		imageUrls.clear();
		imageUrls.addAll(datas);		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_gridview,
					null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.image);
			viewHolder.select_icon = (ImageView) convertView
					.findViewById(R.id.select_icon);
			viewHolder.topView = convertView
					.findViewById(R.id.topView);
			convertView.setTag(viewHolder);
		} else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String imageUrl=imageUrls.get(position);
		if (imageUrl.contains("http://")) {
			imageLoader.loadImage(imageUrl, viewHolder.image , false);
		} else {
			ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(
					imageUrl, viewHolder.image );
		}

		return convertView;
	}

	private static class ViewHolder {
		private ImageView image,select_icon;
		private View topView;
	}
}

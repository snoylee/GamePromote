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
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;

public class GridViewSelectAdapter extends BaseAdapter{
	private Context context;
	private List<PhotoesSubBean> imageUrls;
	private ImageLoader imageLoader;
	public GridViewSelectAdapter(Context context,List<PhotoesSubBean> imageUrls) {
		// TODO Auto-generated constructor stub
		this.context=context;
		imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(imageUrls==null){
			this.imageUrls=new ArrayList<PhotoesSubBean>();
		}else{
			this.imageUrls=imageUrls;
		}
	}
	
	public void updatePhotoes(List<PhotoesSubBean> datas){
		imageUrls.clear();
		imageUrls.addAll(datas);		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls.size();
	}

	@Override
	public PhotoesSubBean getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void changeState(int index){
		imageUrls.get(index).setIsSelect(!imageUrls.get(index).getIsSelect());
	}
	
	public List<PhotoesSubBean> getImaqges(){
		return imageUrls;
	}
	
	public void updateState(boolean flag){
		for(int i=0;i<imageUrls.size();i++){
			imageUrls.get(i).setIsSelect(flag);
		}
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
		PhotoesSubBean item=imageUrls.get(position);
		String imageUrl=item.getImageUrls();
		if (imageUrl.contains("http://")) {
			imageLoader.loadImage(imageUrl, viewHolder.image , false);
		} else {
			ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(
					imageUrl, viewHolder.image );
		}
		
		if(item.getIsSelect()){
			viewHolder.select_icon.setVisibility(View.VISIBLE);
			viewHolder.select_icon.setBackgroundResource(R.drawable.gou);
			viewHolder.topView.setVisibility(View.VISIBLE);
			viewHolder.topView.getBackground().setAlpha(100);
		}else{
			viewHolder.select_icon.setVisibility(View.VISIBLE);
			viewHolder.select_icon.setBackgroundResource(R.drawable.gou_null);
			viewHolder.topView.setVisibility(View.GONE);
		}

		return convertView;
	}

	private static class ViewHolder {
		private ImageView image,select_icon;
		private View topView;
	}
}

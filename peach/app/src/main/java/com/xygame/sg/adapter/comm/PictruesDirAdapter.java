package com.xygame.sg.adapter.comm;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ImageFloder;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;

public class PictruesDirAdapter extends BaseAdapter {
	private List<ImageFloder> data;
	private Context context;

	public PictruesDirAdapter(List<ImageFloder> datas, Context context) {
		if (null == data) {
			this.data = new ArrayList<ImageFloder>();
		} else {
			this.data = datas;
		}
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public ImageFloder getItem(int i) {
		// TODO Auto-generated method stub
		return data.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		HoldView holdView;
		if (view == null) {
			holdView = new HoldView();
			view = LayoutInflater.from(context).inflate(R.layout.list_dir_item,
					null);
			holdView.id_dir_item_count = (TextView) view
					.findViewById(R.id.id_dir_item_count);
			holdView.id_dir_item_name = (TextView) view
					.findViewById(R.id.id_dir_item_name);
			holdView.id_dir_item_image = (ImageView) view
					.findViewById(R.id.id_dir_item_image);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		ImageFloder item = data.get(position);

		holdView.id_dir_item_name.setText(item.getName().replace("/", ""));
		holdView.id_dir_item_count.setText(String.valueOf(item.getCount()).concat("张"));
		ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(
				item.getFirstImagePath(), holdView.id_dir_item_image);
		return view;
	}

	class HoldView {
		ImageView id_dir_item_image;
		TextView id_dir_item_name, id_dir_item_count;
	}

	public final void addItem(ImageFloder item) {

		if (!data.contains(item)) {
			data.add(item);
		}

	}

	public void clearDatas() {
		data.clear();
	}

	public void addDatas(List<ImageFloder> result) {
		if (null != result && !result.isEmpty()) {
			for (ImageFloder item : result) {
				addItem(item);
			}
		}
	}
}

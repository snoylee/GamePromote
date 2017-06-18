package com.xygame.second.sg.jinpai.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopServiceChildAdater extends BaseAdapter {

	private Context context;
	private List<JinPaiSmallTypeBean> datas;

	public PopServiceChildAdater(Context context, List<JinPaiSmallTypeBean> datas) {
		this.context = context;
		if (datas == null) {
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
	public JinPaiSmallTypeBean getItem(int position) {
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
					R.layout.act_area_block_item_layout, null);
			holdView.blockName = (TextView) view.findViewById(R.id.blockName);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		JinPaiSmallTypeBean item = datas.get(position);
		holdView.blockName.setText(item.getTypeName());
		return view;
	}

	class HoldView {
		TextView blockName;
	}

	public void updateList(JinPaiBigTypeBean item) {
		if (!TextUtils.isEmpty(item.getSubStr())){
			try {
				List<JinPaiSmallTypeBean> value=new ArrayList<>();
				JSONArray array=new JSONArray(item.getSubStr());
				for (int i=0;i<array.length();i++){
					JSONObject object=array.getJSONObject(i);
					JinPaiSmallTypeBean item1 =new JinPaiSmallTypeBean();
					item1.setTypeName(StringUtils.getJsonValue(object, "typeName"));
					item1.setTypeId(StringUtils.getJsonValue(object, "typeId"));
					item1.setIsSelected(false);
					value.add(item1);
				}
				if (value.size()>0){
					JinPaiSmallTypeBean item1 =new JinPaiSmallTypeBean();
					item1.setTypeName("全部");
					item1.setTypeId("-1");
					item1.setIsSelected(false);
					value.add(0,item1);
				}
				this.datas=value;
				notifyDataSetChanged();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}

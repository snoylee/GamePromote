package com.xygame.second.sg.comm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.util.ArrayList;
import java.util.List;

public class CityAdater extends BaseAdapter {

	private Context context;
	private List<CityBean> datas;

	public CityAdater(Context context, List<CityBean> datas) {
		this.context = context;
		if (datas == null) {
			this.datas = new ArrayList<>();
		} else {
			this.datas = datas;
		}
	}

	public List<CityBean> getCityAreaDatas(){
		return datas;
	}

	public void updateCityDatas(ProvinceBean item){
		item.get();
		List<CityBean> cityDatas=new ArrayList<>();
		List<CityBean> tempCityDatas= AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(item.getProvinceCode()));
		if (item.getProvinceName().contains("北京")){
			cityDatas.add(tempCityDatas.get(0));
			CityBean cityBean=tempCityDatas.get(0);
			cityBean.get();
			List<CityBean> areaBeans=AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else if (item.getProvinceName().contains("天津")){
			cityDatas.add(tempCityDatas.get(0));
//			CityBean cityBean=tempCityDatas.get(0);
//			cityBean.get();
			tempCityDatas.remove(0);
			List<CityBean> areaBeans=tempCityDatas;//AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else if (item.getProvinceName().contains("重庆")){
			cityDatas.add(tempCityDatas.get(0));
//			CityBean cityBean=tempCityDatas.get(0);
//			cityBean.get();
			tempCityDatas.remove(0);
			List<CityBean> areaBeans=tempCityDatas;//AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else if (item.getProvinceName().contains("上海")){
			cityDatas.add(tempCityDatas.get(0));
			CityBean cityBean=tempCityDatas.get(0);
			cityBean.get();
			List<CityBean> areaBeans=AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else if (item.getProvinceName().contains("香港")){
			cityDatas.add(tempCityDatas.get(0));
//			CityBean cityBean=tempCityDatas.get(0);
//			cityBean.get();
			tempCityDatas.remove(0);
			List<CityBean> areaBeans=tempCityDatas;//AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else if (item.getProvinceName().contains("澳门")){
			cityDatas.add(tempCityDatas.get(0));
//			CityBean cityBean=tempCityDatas.get(0);
//			cityBean.get();
			tempCityDatas.remove(0);
			List<CityBean> areaBeans=tempCityDatas;//AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else if (item.getProvinceName().contains("台湾")){
			cityDatas.add(tempCityDatas.get(0));
//			CityBean cityBean=tempCityDatas.get(0);
//			cityBean.get();
			tempCityDatas.remove(0);
			List<CityBean> areaBeans=tempCityDatas;//AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
			CityBean temple=new CityBean();
			temple.setName("全部");
			temple.setIsSelect(false);
			temple.setId(-1);
			areaBeans.add(0, temple);
			cityDatas.get(0).setAreaBeans(areaBeans);
		}else{
			cityDatas.addAll(tempCityDatas);
			for (int j=0;j<cityDatas.size();j++){
				CityBean cityBean=cityDatas.get(j);
				cityBean.get();
				List<CityBean> areaBeans=AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(cityBean.getCityCode()));
				CityBean temple=new CityBean();
				temple.setName("全部");
				temple.setIsSelect(false);
				temple.setId(-1);
				areaBeans.add(0, temple);
				cityDatas.get(j).setAreaBeans(areaBeans);
			}
		}

		this.datas=cityDatas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CityBean getItem(int position) {
		CityBean item=datas.get(position);
		item.get();
		return item;
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
					R.layout.city_block_item_layout, null);
			holdView.blockName = (TextView) view.findViewById(R.id.blockName);
			holdView.photoList = (GridView) view.findViewById(R.id.photoList);
			holdView.adapter=new AreaAdapter(context,null);
			view.setTag(holdView);
		} else {
			holdView = (HoldView) view.getTag();
		}

		CityBean item = datas.get(position);
		holdView.blockName.setText(item.getName());
		holdView.photoList .setAdapter(holdView.adapter);
		holdView.adapter.updateAreaDatas(item.getAreaBeans());
		holdView.photoList.setOnItemClickListener(new ClickItemListeners(holdView.adapter));
		return view;
	}

	class HoldView {
		TextView blockName;
		GridView photoList;
		AreaAdapter adapter;
	}

	class ClickItemListeners implements AdapterView.OnItemClickListener{
		private AreaAdapter adapter;
		public ClickItemListeners(AreaAdapter adapter){
			this.adapter=adapter;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			CityBean areaBean=adapter.getItem(position);
			adapter.updateSelect(areaBean);
			notifyDataSetChanged();
		}
	}
}

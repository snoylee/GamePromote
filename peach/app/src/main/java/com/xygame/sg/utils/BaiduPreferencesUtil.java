/*
 * 文 件 名:  UserPreferencesUtil.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.utils;

import java.util.List;

import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.task.utils.AssetDataBaseManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [获取到的百度地图信息分享类]
 */
public class BaiduPreferencesUtil {
	
	public static void setIsUpdate(Context context, Boolean isUpdate) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putBoolean("isUpdate", isUpdate);
		editor.commit();
	}

	public static Boolean IsUpdate(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getBoolean("isUpdate", false);
	}

	public static void setCoutryName(Context context, String coutryName) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("coutryName", coutryName);
		editor.commit();
	}

	public static String getCoutryName(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("coutryName", null);
	}

	public static void setCountryCode(Context context, String CountryCode) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("CountryCode", CountryCode);
		editor.commit();
	}

	public static String getCountryCode(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("CountryCode", null);
	}
	
	public static void setAddress(Context context, String Address) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("Address", Address);
		editor.commit();
	}

	public static String getAddress(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("Address", null);
	}
	
	public static void setStreet(Context context, String Street) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("Street", Street);
		editor.commit();
	}

	public static String getStreet(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("Street", null);
	}
	
	public static void setEara(Context context, String Eara) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("Eara", Eara);
		editor.commit();
	}

	public static String getEara(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("Eara", null);
	}
	
	public static void setProvice(Context context, String Provice) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("Provice", Provice);
		editor.commit();
	}

	public static String getProvice(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("Provice", null);
	}
	
	public static void setCity(Context context, String City) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("City", City);
		editor.commit();
	}

	public static String getCity(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("City", null);
	}
	
	public static void setLon(Context context, String Lon) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("Lon", Lon);
		editor.commit();
	}

	public static String getLon(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("Lon", null);
	}
	
	public static void setLat(Context context, String Lat) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		Editor editor = loginShare.edit();
		editor.putString("Lat", Lat);
		editor.commit();
	}
	
	public static String getLat(Context context) {
		SharedPreferences loginShare = context.getSharedPreferences(
				Constants.BAIDU_PREFERNCE, Context.MODE_MULTI_PROCESS);
		return loginShare.getString("Lat", null);
	}
	
	
	public static List<ProvinceBean> allProvinces=((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
	
	public static ProvinceBean getLocalProvinceBean(String baiduProviceName){
		ProvinceBean item=null;
		List<ProvinceBean> datas=allProvinces;
		for(ProvinceBean it:datas){
			it.get();
			if(baiduProviceName.contains(it.getProvinceName())){
				item=it;
			}
		}
		return item;
	}
	
	public static List<CityBean> getLocalCitys(ProvinceBean item){
		List<CityBean> datas=AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(item.getProvinceCode()));
		return datas;
	}
	
	public static CityBean getLocalCityBean(ProvinceBean pbBean,String baiduCityName){
		CityBean item=null;
		List<CityBean> datas=getLocalCitys(pbBean);
		for(CityBean it:datas){
			it.get();
			if(baiduCityName.contains(it.getCityName())){
				item=it;
			}
		}
		return item;
	}
}

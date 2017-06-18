/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.model;

import com.xygame.sg.activity.model.bean.AllModelReqBean;
import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;

import base.action.Action.Param;


public class LoadAllModelInfo extends NetWorkUtil {
	

	@Override
	public String runResult(String result) {

		System.out.println(result);
		return super.runResult(result);
	}
	

	@Override
	public String runUrl(String url) {
		System.out.println(url);
		return super.runUrl(url);
	}


	@Override
	public void callback(Param aparam, Object object) {
//		if (aparam.getFragment() instanceof ModelAllFragment){
//			ModelAllFragment fragment = ((ModelAllFragment) aparam.getFragment());
//			if (fragment.isShowLoading()){
//				ShowMsgDialog.cancel();
//			}
//		} else if (aparam.getActivity() instanceof SearchModelActivity){
//			SearchModelActivity activity = ((SearchModelActivity) aparam.getActivity());
//			if (activity.isShowLoading()){
//				ShowMsgDialog.cancel();
//			}
//		}
//		String resultCode = aparam.getResultunit().getRawMap().get("success");
//		if (Constants.RESULT_CODE.equals(resultCode)) {
//			String resStr = aparam.getResultunit().get("record").toString();
//			if (aparam.getFragment() instanceof ModelAllFragment){
//				ModelAllFragment fragment = ((ModelAllFragment) aparam.getFragment());
//				if(resStr != "null"){
//					Map resultMap = (Map) (aparam.getResultunit().get("record"));
//					List<Map> resultListMap = (List<Map>) resultMap.get("models");
//					String jsonStr = JSON.toJSONString(resultListMap);
//					List<AllModelItemBean> resultModelList = JSON.parseArray(jsonStr, AllModelItemBean.class);
//					fragment.responseModelsList(resultModelList);
//				} else {
//					fragment.responseModelsList(null);
//				}
//			} else if (aparam.getActivity() instanceof SearchModelActivity){
//				SearchModelActivity activity = ((SearchModelActivity) aparam.getActivity());
//				if(resStr != "null"){
//					Map resultMap = (Map) (aparam.getResultunit().get("record"));
//					List<Map> resultListMap = (List<Map>) resultMap.get("models");
//					String jsonStr = JSON.toJSONString(resultListMap);
//					List<AllModelItemBean> resultModelList = JSON.parseArray(jsonStr, AllModelItemBean.class);
//					activity.responseModelsList(resultModelList);
//				} else {
//					activity.responseModelsList(null);
//				}
//			}
//
//		}else {
//			String msg=aparam.getResultunit().getRawMap().get("msg");
//			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
//		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		AllModelReqBean reqBean = null;

//		if (aparam.getFragment() instanceof ModelAllFragment){
//			ModelAllFragment fragment = ((ModelAllFragment) aparam.getFragment());
//			if (fragment.isShowLoading()){
//				ShowMsgDialog.showNoMsg(aparam.getFragment().getActivity(), true);
//			}
//			reqBean = fragment.getAllModelReqBean();
//		} else if (aparam.getActivity() instanceof SearchModelActivity){
//			SearchModelActivity activity = ((SearchModelActivity) aparam.getActivity());
//			if (activity.isShowLoading()){
//				ShowMsgDialog.showNoMsg(aparam.getActivity(), true);
//			}
//			reqBean = activity.getAllModelReqBean();
//		}
//		params.add("page=" + JSON.toJSONString(reqBean.getPage()));
		params.add("gender="+reqBean.getGender());
		params.add("country="+reqBean.getCountry());
		params.add("city="+reqBean.getCity());
		params.add("occupType="+reqBean.getOccupType());
		params.add("ageBegin="+reqBean.getAgeBegin());
		params.add("ageEnd="+reqBean.getAgeEnd());
		params.add("heightBegin="+reqBean.getHeightBegin());
		params.add("heightEnd="+reqBean.getHeightEnd());
		params.add("weightBegin="+reqBean.getWeightBegin());
		params.add("weightEnd="+reqBean.getWeightEnd());
		params.add("bustBegin="+reqBean.getBustBegin());
		params.add("bustEnd="+reqBean.getBustEnd());
		params.add("waistBegin="+reqBean.getWaistBegin());
		params.add("waistEnd="+reqBean.getWaistEnd());
		params.add("hipBegin="+reqBean.getHipBegin());
		params.add("hipEnd="+reqBean.getHipEnd());
		params.add("cupBegin="+reqBean.getCupBegin());
		params.add("cupEnd="+reqBean.getCupEnd());
		params.add("shoesCodeBegin="+reqBean.getShoesCodeBegin());
		params.add("shoesCodeEnd="+reqBean.getShoesCodeEnd());
//		params.add("styleType="+JSON.toJSONString(reqBean.getStyleType()));
		params.add("priceType="+reqBean.getPriceType());
		params.add("modelType="+reqBean.isModelType());
		params.add("usernick="+reqBean.getUsernick());
		params.add("ntelligentSort="+reqBean.getNtelligentSort());
		return super.run(methodname, params, aparam);
	}
}

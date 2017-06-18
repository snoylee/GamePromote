/*
 * 文 件 名:  SGBaseActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月2日
 */
package com.xygame.sg.activity.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月2日
 * @action  [基本Activity实现Actvityr的全局管理]
 */
public class SGMainBaseActivity extends FragmentActivity implements Handler.Callback {

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
//		List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
//		if (jinPaiBigTypeBeans == null) {
//			requestActType();
//		}
//		if ( AliPreferencesUtil.getAuthPicKey(this)==null){
//			requestAuthPicKey();
//		}
		if (AliPreferencesUtil.getBuckekName(this)==null){
			requestAliyParams();
		}
//		if (SGApplication.getInstance().getCarrierDatas().size() == 0){
//			requestOccupType();
//		}
//		if (SGApplication.getInstance().getTypeList() == null){
//			requestModelShootType();
//		}
//		if (SGApplication.getModelTypeList().size() == 0){
//			requestModelStyleType();
//		}
//		queryCustomerServicePhone();//加载客服电话
	}

	public void requestActType() {
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			JSONObject object = new JSONObject();
			item.setData(object);
			item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_LOAD_RZSP);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void requestModelShootType(){
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			JSONObject obj = new JSONObject();
			obj.put("containSub",0);
			obj.put("ver",2);
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUERY_MODEL_SHOOT_TYPE_URL);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_SHOOT_TYPE_INT);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void requestModelStyleType(){
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			JSONObject obj = new JSONObject();
			obj.put("exclus",2);
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUERY_MODEL_STYLE_TYPE_URL);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_STYLE_TYPE_INT);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void requestBlackList(){
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			JSONObject obj = new JSONObject();
			obj.put("page", new JSONObject().put("pageIndex", 1).put("pageSize", 500));
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_BLACK_LIST);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_BLACK_LIST1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void requestAliyParams(){
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			item.setData(new JSONObject());
			item.setServiceURL(ConstTaskTag.QUEST_ALIY_PARAMS);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_ALIY_PARAMS);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void requestAuthPicKey(){
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			item.setData(new JSONObject());
			item.setServiceURL(ConstTaskTag.QUEST_ALIY_UATH_KEY);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_ALIY_UATH_KEY);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case ConstTaskTag.CONST_TAG_PUBLIC: {
				ResponseBean data = (ResponseBean) msg.obj;
				if (data.isPublic()){
					if (data.getCode()==null){
						System.out.print(data.getMsg());
					}else{
						dealPublicData(data);
					}
				}else{
					if (data.getCode()==null){
						Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
						responseFaith();
					}else{
						getResponseBean(data);
					}
				}
				break;
			}
			default:
				break;
		}
		return false;
	}

	protected void getResponseBean(ResponseBean data) {
	}

	protected void responseFaith() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		System.runFinalization();
//		System.gc();
	}

	private void dealPublicData(ResponseBean data) {
		switch (data.getPosionSign()){
			case ConstTaskTag.RESPOSE_ALIY_UATH_KEY:
				if ("0000".equals(data.getCode())) {
					String record=data.getRecord();
					if (!TextUtils.isEmpty(record)) {
						try{
							JSONArray arry=new JSONArray(record);
							for (int i=0;i<arry.length();i++){
								JSONObject obj=arry.getJSONObject(i);
								String propName=obj.get("propName").toString();
								String propValue=obj.get("propValue").toString();
								if("auth_privacy_pic_skey".equals(propName)){
									AliPreferencesUtil.setAuthPicKey(this, propValue);
								}
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
				break;
			case ConstTaskTag.RESPOSE_ALIY_PARAMS:{
				if ("0000".equals(data.getCode())) {
					String record1=data.getRecord();
					if (!TextUtils.isEmpty(record1)) {
						try{
							JSONArray arry=new JSONArray(record1);
							for (int i=0;i<arry.length();i++){
								JSONObject obj=arry.getJSONObject(i);
								if("ali_oss_secret_id".equals(obj.get("propName").toString())){
									String accessKey=obj.get("propValue").toString();
									AliPreferencesUtil.setAccessKey(this, accessKey);
								}
								if("ali_oss_secret_key".equals(this.toString())){
									String screctKey=obj.get("propValue").toString();
									AliPreferencesUtil.setScrectKey(this, screctKey);
								}

								if("ali_oss_bucket_name".equals(obj.get("propName").toString())){
									String buckekName=obj.get("propValue").toString();
									AliPreferencesUtil.setBuckekName(this, buckekName);
								}
							}
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case ConstTaskTag.QUERY_BLACK_LIST1:{
				if ("0000".equals(data.getCode())) {
					parseBlackListDatas(data);
				}
				break;
			}
			case ConstTaskTag.RESPOSE_DEVICE_TOKEN:{
//				Toast.makeText(this,"设备号上传成功",Toast.LENGTH_SHORT).show();
				break;
			}
			case ConstTaskTag.QUERY_MODEL_STYLE_TYPE_INT:{
				String jsonStr=data.getRecord();
				//将拍摄类型缓存到全局变量
//				if (!StringUtils.isEmpty(jsonStr)){
//					if (SGApplication.getModelTypeList().size()==0){
//						SGApplication.setModelTypeList(JSON.parseArray(jsonStr, ModelStyleBean.class));
//					}
//				}

				break;
			}
			case ConstTaskTag.QUERY_LOAD_RZSP:{
				parseXIaDanDatas(data.getRecord());
				break;
			}
		}
	}

	private void parseXIaDanDatas(String record) {
		if (ConstTaskTag.isTrueForArrayObj(record)) {
			try {
				List<JinPaiBigTypeBean> fuFeiDatas = new ArrayList<>();
				JSONArray array = new JSONArray(record);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					JinPaiBigTypeBean item = new JinPaiBigTypeBean();
					item.setIsSelect(false);
					item.setName(StringUtils.getJsonValue(object, "typeName"));
					item.setId(StringUtils.getJsonValue(object, "typeId"));
					item.setSubStr(StringUtils.getJsonValue(object, "titles"));
					item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
					item.setCategoryName(StringUtils.getJsonValue(object, "categoryName"));
					if ("900".equals(item.getId())){
						JinPaiBigTypeBean subItem = new JinPaiBigTypeBean();
						subItem.setCategoryName(item.getCategoryName());
						subItem.setId(Constants.DEFINE_LOL_ID);
						subItem.setSubStr(item.getSubStr());
						subItem.setUrl(item.getUrl());
						subItem.setName("LOL");
						fuFeiDatas.add(subItem);
					}
					fuFeiDatas.add(item);
				}
				CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, fuFeiDatas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void parseBlackListDatas(ResponseBean data) {
		String resposeStr = data.getRecord();
		if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
			List<BlackMemberBean> datas = new ArrayList<>();
			try {
				if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
					JSONArray array2 = new JSONArray(resposeStr);
					for (int i = 0; i < array2.length(); i++) {
						JSONObject object1 = array2.getJSONObject(i);
						BlackMemberBean item = new BlackMemberBean();
						item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
						item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
						item.setUserId(StringUtils.getJsonValue(object1, "userId"));
						item.setAge(StringUtils.getJsonValue(object1, "age"));
						item.setGender(StringUtils.getJsonValue(object1, "gender"));
						datas.add(item);
					}
				}
				CacheService.getInstance().cacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this),datas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
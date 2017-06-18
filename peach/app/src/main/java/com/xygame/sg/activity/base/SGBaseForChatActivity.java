/*
 * 文 件 名:  SGBaseActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月2日
 */
package com.xygame.sg.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.tendcloud.tenddata.TCAgent;
import com.xygame.second.sg.Group.bean.GroupNoticeTip;
import com.xygame.second.sg.comm.bean.SystemServiceBean;
import com.xygame.second.sg.jinpai.JinPaiLowerPriceBean;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.utils.AuthTest;
import com.xygame.second.sg.utils.Contant;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

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
public class SGBaseForChatActivity extends FragmentActivity implements Handler.Callback {
	protected SGApplication eimApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eimApplication = (SGApplication) getApplication();
		SGApplication.getInstance().addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		TCAgent.onResume(this);
//		XMPPUtils.reConnect(this);

		if (UserPreferencesUtil.isOnline(this)){
			if (CacheService.getInstance().getCacheAccessToken(ConstTaskTag.CACHE_QUPAI_TOKEN)==null){
				AuthTest.getInstance().initAuth(this,Contant.APP_KEY,Contant.APP_SECRET,Contant.space);
			}
			List<BlackMemberBean> blackListDatasDatas=CacheService.getInstance().getCacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this));
			if (blackListDatasDatas==null){
				requestBlackList();
			}
		}
//		if ( AliPreferencesUtil.getAuthPicKey(this)==null){
//			requestAuthPicKey();
//		}
		if (AliPreferencesUtil.getBuckekName(this)==null){
			requestAliyParams();
		}
		queryCustomerServicePhone();
	}

	public void requestGroupChatTip(){
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			JSONObject obj = new JSONObject();
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_NOTICE_GROUP_TIP);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_NOTICE_GROUP_TIP);
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
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_BLACK_LIST2);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	protected void queryCustomerServicePhone() {
		// TODO Auto-generated method stub
		SystemServiceBean serviceBean=CacheService.getInstance().getCacheSystemServiceBean(ConstTaskTag.CACHE_SERVICE_BEAN);
		if (serviceBean==null){
			requestServicePhone();
		}
	}

	public void requestServicePhone(){
		try {
			RequestBean item = new RequestBean();
			item.setIsPublic(true);
			item.setData(new JSONObject());
			item.setServiceURL(ConstTaskTag.QUEST_SERVICE_PHONE);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_SERVICE_PHONE);
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

	public void requestGifts(){
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			item.setData(new JSONObject());
			item.setServiceURL(ConstTaskTag.QUEST_GIFT);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GIFT);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void loadBiggestPriceOfType() {
		RequestBean item = new RequestBean();
		item.setIsPublic(true);
		try {
			JSONObject obj = new JSONObject();
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_ACT_MIN_PRICE);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_MIN_PRICE);
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
						if (data.getRecord()!=null&&!"null".equals(data.getRecord())){
							dealPublicData(data);
						}
					}
				}else{
					if (data.getCode()==null){
//						Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
						responseFaith(data);
					}else{
						getResponseBean(data);
					}
				}
				break;
			}
			case Constants.REQUEST_REGISTER_LOGIN_XMPP:{
				xmppRespose();
				break;
			}
			default:
				break;
		}
		return false;
	}

	protected void xmppRespose() {
	}

	protected void getResponseBean(ResponseBean data) {
	}

	protected void responseFaith(ResponseBean data) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
			case ConstTaskTag.RESPOSE_SERVICE_PHONE:{
				if ("0000".equals(data.getCode())) {
					String record2=data.getRecord();
					if (!TextUtils.isEmpty(record2)) {
						try{
							JSONArray arry=new JSONArray(record2);
							SystemServiceBean serviceBean=new SystemServiceBean();
							for (int i=0;i<arry.length();i++){
								JSONObject obj=arry.getJSONObject(i);
								if("sys_fb_service_phone".equals(obj.get("propName").toString())){
									String accessKey=obj.get("propValue").toString();
									serviceBean.setServicePhone(accessKey);
								}else if("sys_fb_service_qq".equals(obj.get("propName").toString())){
									String accessKey=obj.get("propValue").toString();
									serviceBean.setServiceQQ(accessKey);
								}else if("sys_fb_service_mail".equals(obj.get("propName").toString())){
									String accessKey=obj.get("propValue").toString();
									serviceBean.setServiceEmail(accessKey);
								}else if("sys_fb_service_time".equals(obj.get("propName").toString())){
									String accessKey=obj.get("propValue").toString();
									serviceBean.setServiceTime(accessKey);
								}
							}
							CacheService.getInstance().cacheSystemServiceBean(ConstTaskTag.CACHE_SERVICE_BEAN,serviceBean);
							showServiceInfo();
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case ConstTaskTag.QUERY_BLACK_LIST2:
				if ("0000".equals(data.getCode())) {
					parseBlackListDatas(data);
				}
				break;
			case ConstTaskTag.QUERY_NOTICE_GROUP_TIP:
				if ("0000".equals(data.getCode())) {
					parseGroupTimeDatas(data);
				}
				break;
			case ConstTaskTag.RESPOSE_DEVICE_TOKEN:{
//				Toast.makeText(this,"设备号上传成功",Toast.LENGTH_SHORT).show();
				break;
			}
			case ConstTaskTag.QUERY_ACT_MIN_PRICE:
				if ("0000".equals(data.getCode())) {
					parseActPriceDisDatas(data.getRecord());
				}
				break;
		}
	}

	public void showServiceInfo() {
	}

	private void parseGroupTimeDatas(ResponseBean data) {
		String resposeStr = data.getRecord();
		if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
			List<GroupNoticeTip> datas = new ArrayList<>();
			try {
				if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
					JSONArray array2 = new JSONArray(resposeStr);
					for (int i = 0; i < array2.length(); i++) {
						JSONObject object1 = array2.getJSONObject(i);
						GroupNoticeTip item = new GroupNoticeTip();
						item.setGroupId(StringUtils.getJsonValue(object1, "groupId"));
						item.setGroupTip(StringUtils.getJsonValue(object1, "groupAffiche"));
						datas.add(item);
					}
				}
				CacheService.getInstance().cacheGroupNoticeTip(ConstTaskTag.CACHE_GROUP_NOTICE_TIP, datas);
				execuseShowTipAction();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void execuseShowTipAction() {

	}

	private void parseActPriceDisDatas(String record) {
		try {
			if (!TextUtils.isEmpty(record) && !"[]".equals(record) && !"[null]".equals(record) && !"null".equals(record)) {
				List<JinPaiLowerPriceBean> datas = new ArrayList<>();
				JSONArray array1 = new JSONArray(record);
				for (int i = 0; i < array1.length(); i++) {
					JSONObject object = array1.getJSONObject(i);
					JinPaiLowerPriceBean psBean = new JinPaiLowerPriceBean();
					psBean.setHighPrice(StringUtils.getJsonIntValue(object, "highPrice"));
					psBean.setLowPrice(StringUtils.getJsonIntValue(object, "lowPrice"));
					psBean.setTypeId(StringUtils.getJsonValue(object, "typeId"));
					datas.add(psBean);
				}
				if (datas.size() > 0) {
					CacheService.getInstance().cacheJbLowerPricePrice(ConstTaskTag.CACHE_JP_LOWER_PRICE, datas);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
/*
 * 文 件 名:  NewsFragment.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月2日
 */
package com.xygame.sg.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.im.util.ExitAppAction;
import com.xygame.second.sg.Group.GroupNewNoticeEngeer;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.utils.NotificationManagerHelper;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsAdapter;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.NewsGodEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.ClearEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月2日
 * @action  [消息界面]
 */
public class NewsFragment extends SGBaseFragment implements View.OnClickListener,TextWatcher{

	private View rightButton,rightButtonText,rightbuttonIcon;
	private ListView listView;
	private ClearEditText searchContent;
	private NewsAdapter adapter;
	private boolean isDelete=false;
	private TextView search_tv;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerBoradcastReceiver();
		search_tv = (TextView) getView().findViewById(R.id.search_tv);
		rightButton=getView().findViewById(R.id.rightButton);

		rightButtonText=getView().findViewById(R.id.rightButtonText);
		rightbuttonIcon=getView().findViewById(R.id.rightbuttonIcon);
		listView=(ListView)getView().findViewById(R.id.listView);
		searchContent=(ClearEditText)getView().findViewById(R.id.searchContent);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.GONE);
		search_tv.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		searchContent.addTextChangedListener(this);
		searchContent.setOnKeyListener(new KeyEventListener());
		adapter=new NewsAdapter(getActivity(),null,isDelete);
		listView.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.sg_news_activity, null);
	}

//	@Override
//	public void onStart() {
//		super.onStart();
//		if (UserPreferencesUtil.isOnline(getActivity())) {
//			getUnreadMessage();
//		}
//	}

	@Override
	public void onResume() {
		super.onResume();
		if (UserPreferencesUtil.isOnline(getActivity())){
			loadNewsDatas();
		}
	}

	public void isRquestServiceType(){
		List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
		if (typeDatas == null) {
			requestActType();
		}
	}

	public void requestActType() {
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject object = new JSONObject();
			item.setData(object);
			item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JUST_GROUP_ALL);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void getUnreadMessage(){
		RequestBean item = new RequestBean();
		try {
			String userId= UserPreferencesUtil.getUserId(getActivity());
			item.setData(new JSONObject().put("curUserId",userId));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		item.setServiceURL(ConstTaskTag.QUEST_UNREAD_MSG);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_UNREAD_MSG_COD);
	}

	private void loadNewsDatas() {
//		ShowMsgDialog.showNoMsg(this,true);
		//获取本地数据
		List<SGNewsBean> datas= NewsEngine.loadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
		List<SGNewsBean> datas2= NewsGodEngine.loadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
		List<SGNewsBean> groupDatas= TempGroupNewsEngine.loadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
		List<SGNewsBean> noticeDatas= NewsEngine.loadNoticeChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
		datas.addAll(groupDatas);
		datas.addAll(noticeDatas);
		datas.addAll(datas2);
		if (datas.size()>1){
			ThreadPool.getInstance().excuseThread(new MaoPaoNewsSort());
		}else{
			adapter.addDatas(datas);
		}
	}

	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
//					ShowMsgDialog.cancel();
					List<SGNewsBean> resultDatas=(List<SGNewsBean>)msg.obj;
					adapter.addDatas(resultDatas);
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.rightButton){
			isDelete=!isDelete;
			if (isDelete){
				rightbuttonIcon.setVisibility(View.GONE);
				rightButtonText.setVisibility(View.VISIBLE);
			}else{
				rightbuttonIcon.setVisibility(View.VISIBLE);
				rightButtonText.setVisibility(View.GONE);
			}
			adapter.changeListEditor(isDelete);
		}else if(v.getId()== R.id.search_tv){
			String str=searchContent.getText().toString().trim();
			loadSearchNewsDatas(str);
		}
	}

	@Override
	public void onDestroy() {
		unregisterBroadcastReceiver();
		super.onDestroy();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_ACTION);
		myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
		myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
		myIntentFilter.addAction(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
		myIntentFilter.addAction("com.xygame.push.dynamic.message.list.action");
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterBroadcastReceiver() {
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())) {
				boolean newFlag=intent.getBooleanExtra("newsMessage",false);
				if (newFlag){
					loadNewsDatas();
				}

			}else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
				// 登录失败或取消登录
			}else if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
				loadNewsDatas();
			}else if ("com.xygame.push.dynamic.message.list.action".equals(intent.getAction())){
				loadNewsDatas();
			}else  if (XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION.equals(intent.getAction())) {
				String flag=intent.getStringExtra("flag");
				if (Constants.EDITOR_DISC_GROUP_NAME.equals(flag)){
					loadNewsDatas();
				}else if (Constants.LOSE_DISC_GROUP.equals(flag)){
					SGNewsBean sendBean=(SGNewsBean)intent.getSerializableExtra("bean");
					TempGroupNewsEngine.deleteDisHistory(getActivity(),sendBean,UserPreferencesUtil.getUserId(getActivity()));
					loadNewsDatas();
				}else if(Constants.EXIT_DISC_GROUP.equals(flag)){
					SGNewsBean sendBean=(SGNewsBean)intent.getSerializableExtra("bean");
					TempGroupNewsEngine.deleteDisHistory(getActivity(),sendBean,UserPreferencesUtil.getUserId(getActivity()));
					loadNewsDatas();
				}
			}
		}
	};

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			search_tv.setEnabled(true);
		} else {
			search_tv.setEnabled(false);
			loadNewsDatas();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private void loadSearchNewsDatas(final String content) {
		List<SGNewsBean> datas= NewsEngine.loadSearchAllChatNews(getActivity(), content,UserPreferencesUtil.getUserId(getActivity()));
		if (datas.size()>0){
			adapter.addDatas(datas);
		}else{
			Toast.makeText(getActivity(), "抱歉，有找到您想要的结果", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.RESPOSE_UNREAD_MSG_COD:
				if ("0000".equals(data.getCode())){
					newsDatas(data.getRecord());
				}
				break;
			case ConstTaskTag.QUERY_JUST_GROUP_ALL:{
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

	public void newsDatas(String data) {
		try {
			JSONArray array=new JSONArray(data);
			for (int i=0;i<array.length();i++){
				JSONObject subMap=array.getJSONObject(i);
				SGNewsBean msg = new SGNewsBean();
				msg.setRecruitId(StringUtils.getJsonValue(subMap, "userIcon"));//暂时保存用户头像
				msg.setNoticeId(StringUtils.getJsonValue(subMap, "userNick"));//暂时保存为用户昵称
				msg.setNoticeSubject(StringUtils.getJsonValue(subMap, "extra"));//扩展消息，JSON字符串，与msgType协助控制调整
				msg.setMsgContent(StringUtils.getJsonValue(subMap, "msgContent"));
				msg.setNewType(Constants.NEWS_DYNAMIC);
				msg.setUserId(UserPreferencesUtil.getUserId(getActivity()));
				msg.setMessageStatus(Constants.NEWS_UNREAD);
				msg.setTimestamp(StringUtils.getJsonValue(subMap, "createTime"));
				msg.setFromUser(StringUtils.getJsonValue(subMap, "relateUserId"));
				msg.setToUser(StringUtils.getJsonValue(subMap, "toUserId"));
				msg.setFriendUserId(StringUtils.getJsonValue(subMap, "relateUserId"));
				String msgType =StringUtils.getJsonValue(subMap, "msgType");
				if (Constants.TARGET_MEMBER.equals(msgType)) {
					msg.setType(Constants.TARGET_MEMBER);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_REMOVE_APPLY.equals(msgType)) {
					msg.setType(Constants.TARGET_REMOVE_APPLY);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_REMOVE_SUCCESS.equals(msgType)) {
					msg.setType(Constants.TARGET_REMOVE_SUCCESS);
					msg.setInout("0");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_BALANCE.equals(msgType)) {
					msg.setType(Constants.TARGET_BALANCE);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_SHOW_EVALUATE.equals(msgType)) {
					msg.setType(Constants.TARGET_SHOW_EVALUATE);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_NOTICE_DETAIL.equals(msgType)) {
					msg.setType(Constants.TARGET_NOTICE_DETAIL);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_HIRED.equals(msgType)) {
					msg.setType(Constants.TARGET_HIRED);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_INCOME.equals(msgType)) {
					msg.setType(Constants.TARGET_INCOME);
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_CAMERA_ENMPLOM_MODEL.equals(msgType)) {
					msg.setType(Constants.TARGET_CAMERA_ENMPLOM_MODEL);
					msg.setInout("-1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_MODEL_REFUSE_ENMPLOM_MODEL.equals(msgType)) {
					msg.setType(Constants.TARGET_CAMERA_ENMPLOM_MODEL);
					msg.setInout("1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
					NewsEngine.inserChatNew(getActivity(), msg);
				} else if (Constants.TARGET_JINPAI_ACT.equals(msgType)){
					msg.setType(Constants.TARGET_JINPAI_ACT);
					NewsEngine.inserChatNew(getActivity(), msg);
				}else if (Constants.TARGET_FREE_ACT.equals(msgType)){
					msg.setType(Constants.TARGET_FREE_ACT);
					NewsEngine.inserChatNew(getActivity(), msg);
				}else if (Constants.TARGET_FUFEI_ACT.equals(msgType)){
					msg.setType(Constants.TARGET_FUFEI_ACT);
					NewsEngine.inserChatNew(getActivity(), msg);
				}else  if (Constants.TARGET_TAKOUT_MEMBER.equals(msgType)){
					msg.setType(Constants.TARGET_TAKOUT_MEMBER);
					JSONObject object=new JSONObject(msg.getNoticeSubject());
					String groupId=StringUtils.getJsonValue(object,"groupId");
					XMPPUtils.takoutFriend(getActivity(), groupId);
					GroupNewNoticeEngeer.inserBlackGroup(getActivity(), UserPreferencesUtil.getUserId(getActivity()), groupId);
					NewsEngine.inserChatNew(getActivity(), msg);
				}else if (Constants.TARGET_CLOSE_NO.equals(msgType)){
					msg.setType(Constants.TARGET_CLOSE_NO);
					NewsEngine.inserChatNew(getActivity(), msg);
				}else if (Constants.TARGET_XIADAN_ACT.equals(msgType)){
					msg.setType(Constants.TARGET_XIADAN_ACT);
					msg.setInout("-1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
					NewsEngine.inserChatNew(getActivity(), msg);
				}else if (Constants.TARGET_XIADAN_FEEDBACK_ACT.equals(msgType)){
					msg.setType(Constants.TARGET_XIADAN_FEEDBACK_ACT);
					NewsEngine.inserChatNew(getActivity(), msg);
				}else {
					msg.setType("");
					NewsEngine.inserChatNew(getActivity(), msg);
				}
			}
			loadNewsDatas();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void clearData() {
		adapter.clearDatas();
		adapter.notifyDataSetChanged();
	}

	public void quaryData() {
		if (UserPreferencesUtil.isOnline(getActivity())){
			loadNewsDatas();
		}
	}

	private class MaoPaoNewsSort implements Runnable{
		@Override
		public void run() {
			//获取本地数据
			List<SGNewsBean> datas= NewsEngine.loadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
			List<SGNewsBean> groupDatas= TempGroupNewsEngine.loadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
			List<SGNewsBean> noticeDatas= NewsEngine.loadNoticeChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
			List<SGNewsBean> datas2= NewsGodEngine.loadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
			datas.addAll(datas2);
			datas.addAll(groupDatas);
			datas.addAll(noticeDatas);
			if (datas.size()>1){
				//取时间列表
				List<String> timeList=new ArrayList<>();
				for (SGNewsBean timeItem:datas){
					timeList.add(timeItem.getTimestamp());
				}
				//List转换为数组
				String[] newsArray = timeList.toArray(new String[timeList.size()]);
				//开始冒泡排序
				String temp = "";
				for (int i = 0; i < newsArray.length - 1; i++) {
					for (int j = 0; j < newsArray.length - 1 - i; j++) {
						java.util.Calendar c1=java.util.Calendar.getInstance();
						java.util.Calendar c2=java.util.Calendar.getInstance();
						c1.setTime(new Date(Long.parseLong(newsArray[j])));
						c2.setTime(new Date(Long.parseLong(newsArray[j + 1])));
						int result=c1.compareTo(c2);
						if(result<0){
							temp = newsArray[j];
							newsArray[j] = newsArray[j + 1];
							newsArray[j + 1] = temp;
						}
					}
				}
				//数组转List
				List<String> timeResultList = Arrays.asList(newsArray);
				//初始化排序（按时间最大在前）后结果List
				List<SGNewsBean> resultNewsDatas=new ArrayList<>();
				for (String timeRsultItem:timeResultList){
					for (SGNewsBean yuanItem:datas){
						if (timeRsultItem.equals(yuanItem.getTimestamp())){
							resultNewsDatas.add(yuanItem);
						}
					}
				}
				Message message=new Message();
				message.what=0;
				message.obj=resultNewsDatas;
				mHandler.sendMessage(message);
			}else{
				Message message=new Message();
				message.what=0;
				message.obj=datas;
				mHandler.sendMessage(message);
			}
		}
	}
}
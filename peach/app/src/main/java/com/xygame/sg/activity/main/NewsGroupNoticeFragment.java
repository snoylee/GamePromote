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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.ordermsg.NewsNoticeAdapter;
import com.xygame.second.sg.utils.NotificationManagerHelper;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsAdapter;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.ClearEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月2日
 * @action  [消息界面]
 */
public class NewsGroupNoticeFragment extends SGBaseActivity implements View.OnClickListener,TextWatcher{

	private View rightButton,rightButtonText,rightbuttonIcon,backButton;
	private ListView listView;
	private ClearEditText searchContent;
	private NewsNoticeAdapter adapter;
	private boolean isDelete=false;
	private TextView search_tv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_news_activity);
		registerBoradcastReceiver();
		search_tv = (TextView) findViewById(R.id.search_tv);
		rightButton=findViewById(R.id.rightButton);
		backButton=findViewById(R.id.backButton);
		rightButtonText=findViewById(R.id.rightButtonText);
		rightbuttonIcon=findViewById(R.id.rightbuttonIcon);
		listView=(ListView)findViewById(R.id.listView);
		searchContent=(ClearEditText)findViewById(R.id.searchContent);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.GONE);
		search_tv.setOnClickListener(this);
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		searchContent.addTextChangedListener(this);
		searchContent.setOnKeyListener(new KeyEventListener());
		adapter=new NewsNoticeAdapter(this,null,isDelete);
		listView.setAdapter(adapter);
		NotificationManagerHelper.getInstance(this).getNotificationManager().cancel(0);
		getUnreadMessage();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (UserPreferencesUtil.isOnline(this)){
			loadNewsDatas();
		}
	}

	private void loadNewsDatas() {
//		SGNewsBean systemNews=NewsEngine.getOneSystemNews(this);
		List<SGNewsBean> datas= NewsEngine.loadNoticeChatNews(this, UserPreferencesUtil.getUserId(this));
//		if (systemNews!=null){
//			datas.add(systemNews);
//		}
		adapter.addDatas(datas);
	}

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
		}else if (v.getId()==R.id.backButton){
			finish();
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
		myIntentFilter.addAction("com.xygame.push.dynamic.message.list.action");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterBroadcastReceiver() {
		unregisterReceiver(mBroadcastReceiver);
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
		List<SGNewsBean> datas= NewsEngine.loadSearchAllChatNews(this, content,UserPreferencesUtil.getUserId(this));
		if (datas.size()>0){
			adapter.addDatas(datas);
		}else{
			Toast.makeText(this, "抱歉，有找到您想要的结果", Toast.LENGTH_SHORT).show();
		}
	}

	public void getUnreadMessage(){
		RequestBean item = new RequestBean();
		try {
			String userId= UserPreferencesUtil.getUserId(this);
			item.setData(new JSONObject().put("curUserId",userId));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		item.setServiceURL(ConstTaskTag.QUEST_UNREAD_MSG);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_UNREAD_MSG_COD);
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
				msg.setUserId(UserPreferencesUtil.getUserId(this));
				msg.setMessageStatus(Constants.NEWS_UNREAD);
				msg.setTimestamp(StringUtils.getJsonValue(subMap, "createTime"));
				msg.setFromUser(StringUtils.getJsonValue(subMap, "relateUserId"));
				msg.setToUser(StringUtils.getJsonValue(subMap, "toUserId"));
				msg.setFriendUserId(StringUtils.getJsonValue(subMap, "relateUserId"));
				String msgType =StringUtils.getJsonValue(subMap, "msgType");
//				if (Constants.TARGET_MEMBER.equals(msgType)) {
//					msg.setType(Constants.TARGET_MEMBER);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_REMOVE_APPLY.equals(msgType)) {
//					msg.setType(Constants.TARGET_REMOVE_APPLY);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_REMOVE_SUCCESS.equals(msgType)) {
//					msg.setType(Constants.TARGET_REMOVE_SUCCESS);
//					msg.setInout("0");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_BALANCE.equals(msgType)) {
//					msg.setType(Constants.TARGET_BALANCE);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_SHOW_EVALUATE.equals(msgType)) {
//					msg.setType(Constants.TARGET_SHOW_EVALUATE);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_NOTICE_DETAIL.equals(msgType)) {
//					msg.setType(Constants.TARGET_NOTICE_DETAIL);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_HIRED.equals(msgType)) {
//					msg.setType(Constants.TARGET_HIRED);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_INCOME.equals(msgType)) {
//					msg.setType(Constants.TARGET_INCOME);
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_CAMERA_ENMPLOM_MODEL.equals(msgType)) {
//					msg.setType(Constants.TARGET_CAMERA_ENMPLOM_MODEL);
//					msg.setInout("-1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_MODEL_REFUSE_ENMPLOM_MODEL.equals(msgType)) {
//					msg.setType(Constants.TARGET_CAMERA_ENMPLOM_MODEL);
//					msg.setInout("1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
//					NewsEngine.inserChatNew(this, msg);
//				} else if (Constants.TARGET_JINPAI_ACT.equals(msgType)){
//					msg.setType(Constants.TARGET_JINPAI_ACT);
//					NewsEngine.inserChatNew(this, msg);
//				}else if (Constants.TARGET_FREE_ACT.equals(msgType)){
//					msg.setType(Constants.TARGET_FREE_ACT);
//					NewsEngine.inserChatNew(this, msg);
//				}else if (Constants.TARGET_FUFEI_ACT.equals(msgType)){
//					msg.setType(Constants.TARGET_FUFEI_ACT);
//					NewsEngine.inserChatNew(this, msg);
//				}else {
//					msg.setType("");
//					NewsEngine.inserChatNew(this, msg);
//				}
			}
			loadNewsDatas();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
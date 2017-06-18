package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xy.im.util.EixstMultiRoomsUtils;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.personal.blacklist.BlackListActivity;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.testpay.TestFuctionAction;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DataCleanManager;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.frame.VisitUnit;


public class SettingActivity extends SGBaseActivity implements OnClickListener{
	
	private TextView titleName;
	private View backButton,intoTestButton;
	private RelativeLayout message_setting_rl;
	private RelativeLayout safe_rl;
	private View score_rl,blackList;
	private RelativeLayout opinion_rl;
	private RelativeLayout about_rl;
	private RelativeLayout quit_rl;
	private RelativeLayout clean_cache_rl;
	private TextView cache_size_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_setting_layout);
		initViews();
		initListeners();
		initDatas();
	}

	private void initListeners() {
		backButton.setOnClickListener(this);
		message_setting_rl.setOnClickListener(this);
		safe_rl.setOnClickListener(this);
		opinion_rl.setOnClickListener(this);
		about_rl.setOnClickListener(this);
		quit_rl.setOnClickListener(this);
		clean_cache_rl.setOnClickListener(this);
		intoTestButton.setOnClickListener(this);
		score_rl.setOnClickListener(this);
		blackList.setOnClickListener(this);
	}


	private void initViews() {
		blackList=findViewById(R.id.blackList);
		score_rl=findViewById(R.id.score_rl);
		titleName=(TextView)findViewById(R.id.titleName);
		backButton=findViewById(R.id.backButton);
		message_setting_rl = (RelativeLayout)findViewById(R.id.message_setting_rl);
		safe_rl=(RelativeLayout)findViewById(R.id.safe_rl);
		opinion_rl = (RelativeLayout)findViewById(R.id.opinion_rl);
		about_rl = (RelativeLayout)findViewById(R.id.about_rl);
		clean_cache_rl = (RelativeLayout)findViewById(R.id.clean_cache_rl);
		cache_size_tv=(TextView)findViewById(R.id.cache_size_tv);
		quit_rl = (RelativeLayout)findViewById(R.id.quit_rl);
		intoTestButton=findViewById(R.id.intoTestButton);
	}


	private void initDatas() {
		titleName.setText("设置");
		try {
			cache_size_tv.setText(DataCleanManager.getCacheSize(getExternalCacheDir()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backButton){
			finish();
		}else if(v.getId()==R.id.message_setting_rl){
			Intent intent=new Intent(this, SettingNoticeActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.safe_rl){
			Intent intent=new Intent(this, SettingSafeActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.opinion_rl){
			Intent intent=new Intent(this, SettingOpinionActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.about_rl){
			Intent intent=new Intent(this, SettingWhoActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.clean_cache_rl){
			DataCleanManager.cleanExternalCache(getApplication());
			cache_size_tv.setText("");
		}else if(v.getId()==R.id.quit_rl){
			TwoButtonDialog dialog=new TwoButtonDialog(this, "确定退出吗？", R.style.dineDialog,new ButtonTwoListener() {
				@Override
				public void confrimListener() {
					loginOUt();
				}
				@Override
				public void cancelListener() {
				}
			});
			dialog.show();
		}else if(v.getId()==R.id.intoTestButton){
			Intent intent=new Intent(this, TestFuctionAction.class);
			startActivity(intent);
		}else if (v.getId()==R.id.score_rl){
			Uri uri = Uri.parse("market://details?id="+getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}else if (v.getId()==R.id.blackList){
			Intent intent111=new Intent(this,BlackListActivity.class);
			startActivity(intent111);
		}
	}
	
	private void loginOUt() {
		XMPPUtils.exitAppUpdateBean(SettingActivity.this);
		ShowMsgDialog.showNoMsg(this,false);
		ThreadPool.getInstance().excuseThread(new ExitAppAction());
	}

	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					ShowMsgDialog.cancel();
					Intent intent = new Intent(Constants.ACTION_LOGIN_FAILTH);
					sendBroadcast(intent);
					finish();
					break;
			}
		}
	};

	private class ExitAppAction implements Runnable{
		@Override
		public void run() {
			UserPreferencesUtil.setRefract(SettingActivity.this, false);
			UserPreferencesUtil.setIsOnline(SettingActivity.this, false);
			UserPreferencesUtil.setUserId(SettingActivity.this, null);
			UserPreferencesUtil.setCellPhone(SettingActivity.this, null);
			UserPreferencesUtil.setLoginName(SettingActivity.this, null);
			Map<String,MultiUserChat> multiUserChatMap= EixstMultiRoomsUtils.getAllMutiRooms(SettingActivity.this);
			List<GroupBean> disgroupDatas=CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(SettingActivity.this));
			List<GroupBean> groupDatas=CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(SettingActivity.this));
			if (disgroupDatas!=null){
				for (int i=0;i<disgroupDatas.size();i++){
					GroupBean item=disgroupDatas.get(i);
					GroupBean temp = GroupEngine.quaryGroupBean(SettingActivity.this, item, UserPreferencesUtil.getUserId(SettingActivity.this));
					item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
					if (temp == null) {
						GroupEngine.inserGroup(SettingActivity.this, item);
					}
//					if (temp != null) {
//						GroupEngine.updateGroupLastTime(SettingActivity.this, item, UserPreferencesUtil.getUserId(SettingActivity.this));
//					} else {
//						GroupEngine.inserGroup(SettingActivity.this, item);
//					}
					MultiUserChat multiUserChat=multiUserChatMap.get(item.getGroupId());
					if (multiUserChat!=null){
						if (SGApplication.getInstance().getConnection().isConnected()) {
							multiUserChat.leave();
						}
					}
				}
			}
			if (groupDatas!=null){
				for (int i=0;i<groupDatas.size();i++){
					GroupBean item=groupDatas.get(i);
					GroupBean temp = GroupEngine.quaryGroupBean(SettingActivity.this, item, UserPreferencesUtil.getUserId(SettingActivity.this));
					item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
					if (temp == null) {
						GroupEngine.inserGroup(SettingActivity.this, item);
					}
//					if (temp != null) {
//						GroupEngine.updateGroupLastTime(SettingActivity.this, item, UserPreferencesUtil.getUserId(SettingActivity.this));
//					} else {
//						GroupEngine.inserGroup(SettingActivity.this, item);
//					}
					MultiUserChat multiUserChat=multiUserChatMap.get(item.getGroupId());
					if (multiUserChat!=null){
						if (SGApplication.getInstance().getConnection().isConnected()) {
							multiUserChat.leave();
						}
					}
				}
			}
			if(SGApplication.getInstance().getConnection()!=null){
				if (SGApplication.getInstance().getConnection().isConnected()){
					SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.unavailable));
				}
				SGApplication.getInstance().getConnection().disconnect();
			}
			mHandler.sendEmptyMessage(0);
		}
	}
}

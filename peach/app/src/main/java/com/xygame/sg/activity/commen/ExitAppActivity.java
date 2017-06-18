package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.xy.im.util.EixstMultiRoomsUtils;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.ping.PingManager;

import java.util.List;
import java.util.Map;

public class ExitAppActivity extends Activity implements View.OnClickListener {
    private TextView tipText;
    private TextView comfirmView;
    private ButtonOneListener comfirmListener;
    private String textTip="",lastLoginTime;
    private String confirmViewText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_exitapp_layout);
        initViews();
        initListener();
        initDatas();
    }

    private void initViews() {
        tipText = (TextView) findViewById(R.id.tipText);
        comfirmView = (TextView)findViewById(R.id.comfirmView);
    }


    private void initListener() {
        comfirmView.setOnClickListener(this);
    }


    private void initDatas() {
        lastLoginTime=getIntent().getStringExtra("lastLoginTime");
        tipText.setText(textTip);
        if (lastLoginTime.contains("conflict")){
            lastLoginTime=String.valueOf(System.currentTimeMillis());
        }
        tipText.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(lastLoginTime)));
        if (!StringUtils.isEmpty(confirmViewText)){
            comfirmView.setText(confirmViewText);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comfirmView) {
            ShowMsgDialog.showNoMsg(this,false);
            ThreadPool.getInstance().excuseThread(new ExitAppAction());
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ShowMsgDialog.cancel();
                    Intent intent2 = new Intent("com.xygame.exit.app.action");
                    sendBroadcast(intent2);
                    finish();
                    break;
            }
        }
    };

    private class ExitAppAction implements Runnable{
        @Override
        public void run() {
            UserPreferencesUtil.setIsOnline(ExitAppActivity.this, false);
            UserPreferencesUtil.setUserId(ExitAppActivity.this, null);
            UserPreferencesUtil.setCellPhone(ExitAppActivity.this, null);
            UserPreferencesUtil.setRefract(ExitAppActivity.this, false);
            UserPreferencesUtil.setLoginName(ExitAppActivity.this, null);
            Map<String,MultiUserChat> multiUserChatMap= EixstMultiRoomsUtils.getAllMutiRooms(ExitAppActivity.this);
            List<GroupBean> disgroupDatas=CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(ExitAppActivity.this));
            List<GroupBean> groupDatas=CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(ExitAppActivity.this));
            if (disgroupDatas!=null){
                for (int i=0;i<disgroupDatas.size();i++){
                    GroupBean item=disgroupDatas.get(i);
                    GroupBean temp = GroupEngine.quaryGroupBean(ExitAppActivity.this, item, UserPreferencesUtil.getUserId(ExitAppActivity.this));
                    item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
                    if (temp == null) {
                        GroupEngine.inserGroup(ExitAppActivity.this, item);
                    }
//                    if (temp != null) {
//                        GroupEngine.updateGroupLastTime(ExitAppActivity.this, item, UserPreferencesUtil.getUserId(ExitAppActivity.this));
//                    } else {
//                        GroupEngine.inserGroup(ExitAppActivity.this, item);
//                    }
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
                    GroupBean temp = GroupEngine.quaryGroupBean(ExitAppActivity.this, item, UserPreferencesUtil.getUserId(ExitAppActivity.this));
                    item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
                    if (temp == null) {
                        GroupEngine.inserGroup(ExitAppActivity.this, item);
                    }
//                    if (temp != null) {
//                        GroupEngine.updateGroupLastTime(ExitAppActivity.this, item, UserPreferencesUtil.getUserId(ExitAppActivity.this));
//                    } else {
//                        GroupEngine.inserGroup(ExitAppActivity.this, item);
//                    }
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

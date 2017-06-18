package com.xygame.second.sg.Group.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.GroupNewNoticeEngeer;
import com.xygame.second.sg.Group.adapter.GroupNoticeChatAdapter;
import com.xygame.second.sg.Group.bean.GoupNoticeBean;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.Group.bean.GroupNoticeItemBean;
import com.xygame.second.sg.Group.bean.GroupNoticeMessageBean;
import com.xygame.second.sg.Group.bean.GroupNoticeTip;
import com.xygame.second.sg.comm.inteface.ResendMsgListener;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.base.SGBaseForChatActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 聊天对话.
 */
public class GroupNoticeChatActivity extends SGBaseForChatActivity implements OnClickListener, ResendMsgListener {
    private View backButton, plushNoticeView, groupInfoView, sendButton;
    private ListView listView;
    private EditText chatContent;
    private TextView titleName,chatTipText;
    private GoupNoticeBean goupNoticeBean;
    private MultiUserChat chat;
    private GroupNoticeMessageBean sendMsgBean;
    private GroupNoticeChatAdapter adapter;
    private boolean isTempleChat = false;
    private GroupBean item;
//    private  boolean booleanFlag=false;
    private long tempMemberJoinTimer,tempRecieverMsgTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_group_notice);
        registerBoradcastReceiver();
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        chatTipText=(TextView)findViewById(R.id.chatTipText);
        backButton = findViewById(R.id.backButton);
        plushNoticeView = findViewById(R.id.plushNoticeView);
        groupInfoView = findViewById(R.id.groupInfoView);
        sendButton = findViewById(R.id.sendButton);
        listView = (ListView) findViewById(R.id.listView);
        titleName = (TextView) findViewById(R.id.titleName);
        chatContent = (EditText) findViewById(R.id.chatContent);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        plushNoticeView.setOnClickListener(this);
        groupInfoView.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }

    private void initDatas() {
        goupNoticeBean = (GoupNoticeBean) getIntent().getSerializableExtra("bean");
        item = new GroupBean();
        item.setUserId(UserPreferencesUtil.getUserId(this));
        item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
        item.setGroupId(goupNoticeBean.getGroupId());
        titleName.setText(goupNoticeBean.getGroupName());
        List<GroupNoticeTip> tipDatas=CacheService.getInstance().getCacheGroupNoticeTip(ConstTaskTag.CACHE_GROUP_NOTICE_TIP);
        if (tipDatas!=null){
            updateGroupTips(tipDatas);
        }else{
            requestGroupChatTip();
        }
//        List<GroupBean> groupDatas=CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(this));
//        if (groupDatas!=null){
//            for (GroupBean item:groupDatas){
//                if (goupNoticeBean.getGroupId().equals(item.getGroupId())){
//                    booleanFlag=true;
//                    break;
//                }
//            }
//        }
        initRoomAction();
//        if (booleanFlag){
//            List<GroupNoticeMessageBean> datas = GroupNewNoticeEngeer.quaryGroupNoticeMsg(GroupNoticeChatActivity.this, UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this), goupNoticeBean.getGroupId());
//            adapter = new GroupNoticeChatAdapter(GroupNoticeChatActivity.this, datas);
//            adapter.addResendListener(GroupNoticeChatActivity.this);
//            listView.setAdapter(adapter);
//        }else{
//            GroupNewNoticeEngeer.deleteGroupNoticeMsg(GroupNoticeChatActivity.this, UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this), goupNoticeBean.getGroupId());
//            adapter = new GroupNoticeChatAdapter(GroupNoticeChatActivity.this, null);
//            adapter.addResendListener(GroupNoticeChatActivity.this);
//            listView.setAdapter(adapter);
//        }
        List<GroupNoticeMessageBean> datas = GroupNewNoticeEngeer.quaryGroupNoticeMsg(GroupNoticeChatActivity.this, UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this), goupNoticeBean.getGroupId());
        adapter = new GroupNoticeChatAdapter(GroupNoticeChatActivity.this, datas);
        adapter.addResendListener(GroupNoticeChatActivity.this);
        listView.setAdapter(adapter);
    }

    @Override
    public void execuseShowTipAction() {
        super.execuseShowTipAction();
        List<GroupNoticeTip> tipDatas=CacheService.getInstance().getCacheGroupNoticeTip(ConstTaskTag.CACHE_GROUP_NOTICE_TIP);
        updateGroupTips(tipDatas);
    }

    private void updateGroupTips(List<GroupNoticeTip> tipDatas) {
        for (GroupNoticeTip item:tipDatas){
            if (goupNoticeBean.getGroupId().equals(item.getGroupId())){
                chatTipText.setVisibility(View.VISIBLE);
                chatTipText.setText(item.getGroupTip());
//                chatTipText.getBackground().setAlpha(40);
                break;
            }
        }
    }

    private void initRoomAction() {
        ThreadPool.getInstance().excuseThread(new InitChatRoom());
    }

    private class InitChatRoom implements Runnable{
        @Override
        public void run() {
            Map<String, MultiUserChat> eixstMultiRooms = CacheService.getInstance().getCacheGroupRoomDatas(UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this));
            if (eixstMultiRooms != null) {
                if (eixstMultiRooms.containsKey(goupNoticeBean.getGroupId())) {
                    chat = eixstMultiRooms.get(goupNoticeBean.getGroupId());
                    if (SGApplication.getInstance().getConnection().isConnected()) {
                        if (!chat.isJoined()) {
                            try {
                                DiscussionHistory history = new DiscussionHistory();
                                GroupBean temp = GroupEngine.quaryGroupBean(GroupNoticeChatActivity.this, item, UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this));
                                if (temp != null) {
                                    if (!TextUtils.isEmpty(temp.getLastIntoTimer())) {
                                        history.setSince(new Date(Long.parseLong(temp.getLastIntoTimer())));
                                    } else {
                                        history.setSince(new Date(System.currentTimeMillis()));
                                    }
                                } else {
                                    history.setSince(new Date(System.currentTimeMillis()));
                                }
                                history.setMaxStanzas(100);
                                chat.join(UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this), null, history, SmackConfiguration.getPacketReplyTimeout());
                                tempMemberJoinTimer=System.currentTimeMillis();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (chat == null) {
                try {
                    if (SGApplication.getInstance().getConnection().isConnected()) {
                        String serviceName = SGApplication.getInstance().getConnection().getServiceName();
                        String roomId = goupNoticeBean.getGroupId().concat("@").concat("conference.").concat(serviceName);
                        chat = new MultiUserChat(SGApplication.getInstance().getConnection(),
                                roomId);
                        DiscussionHistory history = new DiscussionHistory();
                        GroupBean temp = GroupEngine.quaryGroupBean(GroupNoticeChatActivity.this, item, UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this));
                        if (temp != null) {
                            if (!TextUtils.isEmpty(temp.getLastIntoTimer())) {
                                history.setSince(new Date(Long.parseLong(temp.getLastIntoTimer())));
                            } else {
                                Date date=new Date(System.currentTimeMillis());
                                history.setSince(date);
                            }
                        } else {
                            Date date=new Date(System.currentTimeMillis());
                            history.setSince(date);
                        }
                        history.setMaxStanzas(100);
                        chat.join(UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this), null, history, SmackConfiguration.getPacketReplyTimeout());
                        tempMemberJoinTimer=System.currentTimeMillis();
                        isTempleChat = true;
                    }
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.plushNoticeView) {
            Intent intent = new Intent(this, PlushGroupNoticeActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.groupInfoView) {
            Intent intent = new Intent(this, GroupInfoActivity.class);
            intent.putExtra("goupNoticeBean", goupNoticeBean);
            intent.putExtra("isTempleChat", isTempleChat);
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.sendButton) {
            if (!"".equals(chatContent.getText().toString().trim())) {
                sendMessage(chatContent.getText().toString().trim());
            } else {
                Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage(String josonToString) {
        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = goupNoticeBean.getGroupId().concat("@").concat("conference.").concat(serviceName);
            long timestamp = System.currentTimeMillis();

            //本地消息
            sendMsgBean = new GroupNoticeMessageBean();
            sendMsgBean.setMsgStatus("1");
            sendMsgBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendMsgBean.setNoticeJson("");
            sendMsgBean.setGroupJid(goupNoticeBean.getGroupId());
            sendMsgBean.setMsgContent(josonToString);
            sendMsgBean.setMsgTimer(String.valueOf(timestamp));
            sendMsgBean.setMsgType(Constants.SEND_TEXT);
            // 创建消息实体
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", josonToString);
            bodyMsg.put("type", Constants.SEND_TEXT);
            JSONObject ext = new JSONObject();
            JSONObject fromUserObj = new JSONObject();
            fromUserObj.put("userId", UserPreferencesUtil.getUserId(this));
            fromUserObj.put("userIcon", UserPreferencesUtil.getHeadPic(this));
            fromUserObj.put("usernick", UserPreferencesUtil.getUserNickName(this));
            sendMsgBean.setSendUserId(fromUserObj.toString());
            ext.put("fromUser", fromUserObj);
            JSONObject toUserObj = new JSONObject();
            toUserObj.put("userId", sendMsgBean.getGroupJid());
            ext.put("toUser", toUserObj);
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_TG_TEXT);
            adapter.addItem(sendMsgBean);
            chatContent.setText("");

            //更新本地消息
            GroupNewNoticeEngeer.inserGroupNoticeMsg(this, sendMsgBean);
            //发送消息
            chat.sendMessage(message);

        } catch (Exception e) {
            sendMsgBean.setMsgStatus("2");
            adapter.updateItemSendStatus(sendMsgBean);
            GroupNewNoticeEngeer.updatMsgStatus(this, sendMsgBean);
            initRoomAction();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterBroadcastReceiver();
        if (isTempleChat) {
            if (SGApplication.getInstance().getConnection().isConnected()) {
                if (chat != null) {
                    chat.leave();
                }
            }
        }
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_GROUP_NOTICE);
        myIntentFilter.addAction("com.xygame.group.takeout.action");
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.NEW_MESSAGE_GROUP_NOTICE.equals(intent.getAction())) {
                tempRecieverMsgTimer=System.currentTimeMillis();
                boolean flag = intent.getBooleanExtra("newsMessage", false);
                if (flag) {
                    sendMsgBean = (GroupNoticeMessageBean) intent.getSerializableExtra("newBean");
                    handler.sendEmptyMessage(0);
//                    if (!booleanFlag){
//                        long diff=tempRecieverMsgTimer-tempMemberJoinTimer;
//                        long limitTime = diff / 1000;
//                        if (limitTime>3){
//                            sendMsgBean = (GroupNoticeMessageBean) intent.getSerializableExtra("newBean");
//                            handler.sendEmptyMessage(0);
//                        }else{
//                            GroupNewNoticeEngeer.deleteGroupNoticeMsg(GroupNoticeChatActivity.this, UserPreferencesUtil.getUserId(GroupNoticeChatActivity.this), goupNoticeBean.getGroupId());
//                        }
//                    }else{
//                        sendMsgBean = (GroupNoticeMessageBean) intent.getSerializableExtra("newBean");
//                        handler.sendEmptyMessage(0);
//                    }
                }
            }else if ("com.xygame.group.takeout.action".equals(intent.getAction())){
                String groupId=intent.getStringExtra("groupId");
                if (groupId.equals(goupNoticeBean.getGroupId())){
                    showComfimDiloag("您已被踢出该群");
                }
            }
        }
    };

    private void showComfimDiloag(String tip){
        OneButtonDialog dialog = new OneButtonDialog(this,tip, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        isTempleChat=true;
                        finish();
                    }
                });
        dialog.show();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0:
                        adapter.addItem(sendMsgBean);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                GroupNoticeItemBean itemBean = (GroupNoticeItemBean) data.getSerializableExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG);
                if (itemBean != null) {
                    sendNotice(itemBean);
                }
                break;
            }
            case 1:
                isTempleChat = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
                break;
            default:
                break;
        }
    }

    private void sendNotice(GroupNoticeItemBean itemBean) {
        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = goupNoticeBean.getGroupId().concat("@").concat("conference.").concat(serviceName);
            long timestamp = System.currentTimeMillis();

            JSONObject contentObj = new JSONObject();
            contentObj.put("title", itemBean.getTitle());
            contentObj.put("time", itemBean.getTimer());
            contentObj.put("address", itemBean.getAddress());
            contentObj.put("price", itemBean.getPrice());
            contentObj.put("oral", itemBean.getOral());
            String contentStr = contentObj.toString();

            //本地消息
            sendMsgBean = new GroupNoticeMessageBean();
            sendMsgBean.setMsgStatus("1");
            sendMsgBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendMsgBean.setNoticeJson("");
            sendMsgBean.setGroupJid(goupNoticeBean.getGroupId());
            sendMsgBean.setMsgContent(contentStr);
            sendMsgBean.setMsgTimer(String.valueOf(timestamp));
            sendMsgBean.setMsgType(Constants.SEND_NOTICE_TEXT);
            // 创建消息实体
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", contentStr);
            bodyMsg.put("type", Constants.SEND_NOTICE_TEXT);
            JSONObject ext = new JSONObject();
            JSONObject fromUserObj = new JSONObject();
            fromUserObj.put("userId", UserPreferencesUtil.getUserId(this));
            fromUserObj.put("userIcon", UserPreferencesUtil.getHeadPic(this));
            fromUserObj.put("usernick", UserPreferencesUtil.getUserNickName(this));
            sendMsgBean.setSendUserId(fromUserObj.toString());
            ext.put("fromUser", fromUserObj);
            JSONObject toUserObj = new JSONObject();
            toUserObj.put("userId", sendMsgBean.getGroupJid());
            ext.put("toUser", toUserObj);
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_TG_ACT);
            adapter.addItem(sendMsgBean);
            chatContent.setText("");

            //更新本地消息
            GroupNewNoticeEngeer.inserGroupNoticeMsg(this, sendMsgBean);
            //发送消息
            chat.sendMessage(message);
            String count=goupNoticeBean.getNoticeCount();
            goupNoticeBean.setNoticeCount(String.valueOf(Integer.parseInt(count) + 1));

        } catch (Exception e) {
            sendMsgBean.setMsgStatus("2");
            adapter.updateItemSendStatus(sendMsgBean);
            GroupNewNoticeEngeer.updatMsgStatus(this, sendMsgBean);
            initRoomAction();
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        GroupBean temp = GroupEngine.quaryGroupBean(this, item, UserPreferencesUtil.getUserId(this));
        item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
        if (temp != null) {
            GroupEngine.updateGroupLastTime(this, item, UserPreferencesUtil.getUserId(this));
        } else {
            GroupEngine.inserGroup(this, item);
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.COMEBACK, goupNoticeBean);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void reSendMessage(Object object) {
        sendMsgBean = (GroupNoticeMessageBean) object;
        reSendNoticeMsg();
    }

    private void reSendNoticeMsg() {
        try {
            if (Constants.SEND_TEXT.equals(sendMsgBean.getMsgType())) {
                String serviceName = SGApplication.getInstance().getConnection().getServiceName();
                String roomId = goupNoticeBean.getGroupId().concat("@").concat("conference.").concat(serviceName);
                String messageContent = "";
                JSONObject bodyMsg = new JSONObject();
                bodyMsg.put("msgContent", sendMsgBean.getMsgContent());
                bodyMsg.put("type", Constants.SEND_TEXT);
                JSONObject ext = new JSONObject();
                JSONObject fromUserObj = new JSONObject();
                fromUserObj.put("userId", UserPreferencesUtil.getUserId(this));
                fromUserObj.put("userIcon", UserPreferencesUtil.getHeadPic(this));
                fromUserObj.put("usernick", UserPreferencesUtil.getUserNickName(this));
                ext.put("fromUser", fromUserObj);
                JSONObject toUserObj = new JSONObject();
                toUserObj.put("userId", sendMsgBean.getGroupJid());
                ext.put("toUser", toUserObj);
                bodyMsg.put("ext", ext);
                bodyMsg.put("timestamp", sendMsgBean.getMsgTimer());
                messageContent = bodyMsg.toString();
                //赋值给消息对像
                org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
                message.setBody(messageContent);
                message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                message.setTo(roomId);
                message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
                message.setSubject(ConstTaskTag.GROUP_TG_TEXT);
                chat.sendMessage(message);
                sendMsgBean.setMsgStatus("1");
                adapter.updateItemSendStatus(sendMsgBean);
                GroupNewNoticeEngeer.updatMsgStatus(this, sendMsgBean);
            } else {
                String serviceName = SGApplication.getInstance().getConnection().getServiceName();
                String roomId = goupNoticeBean.getGroupId().concat("@").concat("conference.").concat(serviceName);
                String messageContent = "";
                JSONObject bodyMsg = new JSONObject();
                bodyMsg.put("msgContent", sendMsgBean.getMsgContent());
                bodyMsg.put("type", Constants.SEND_NOTICE_TEXT);
                JSONObject ext = new JSONObject();
                JSONObject fromUserObj = new JSONObject();
                fromUserObj.put("userId", UserPreferencesUtil.getUserId(this));
                fromUserObj.put("userIcon", UserPreferencesUtil.getHeadPic(this));
                fromUserObj.put("usernick", UserPreferencesUtil.getUserNickName(this));
                ext.put("fromUser", fromUserObj);
                JSONObject toUserObj = new JSONObject();
                toUserObj.put("userId", sendMsgBean.getGroupJid());
                ext.put("toUser", toUserObj);
                bodyMsg.put("ext", ext);
                bodyMsg.put("timestamp", sendMsgBean.getMsgTimer());
                messageContent = bodyMsg.toString();
                //赋值给消息对像
                org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
                message.setBody(messageContent);
                message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
                message.setTo(roomId);
                message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
                message.setSubject(ConstTaskTag.GROUP_TG_ACT);
                chat.sendMessage(message);
                String count=goupNoticeBean.getNoticeCount();
                goupNoticeBean.setNoticeCount(String.valueOf(Integer.parseInt(count) + 1));
                sendMsgBean.setMsgStatus("1");
                adapter.updateItemSendStatus(sendMsgBean);
                GroupNewNoticeEngeer.updatMsgStatus(this, sendMsgBean);
            }
        } catch (Exception e) {
            sendMsgBean.setMsgStatus("2");
            adapter.updateItemSendStatus(sendMsgBean);
            GroupNewNoticeEngeer.updatMsgStatus(this, sendMsgBean);
            e.printStackTrace();
        }
    }
}

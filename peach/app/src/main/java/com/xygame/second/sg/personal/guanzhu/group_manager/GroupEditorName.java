package com.xygame.second.sg.personal.guanzhu.group_manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.im.util.EixstMultiRoomsUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONObject;

/**
 * Created by tony on 2016/11/14.
 */
public class GroupEditorName extends SGBaseActivity implements View.OnClickListener {

    private TextView titleName;
    private View backButton,rightButton;
    private EditText groupName;
    private TextView rightButtonText;
    private SGNewsBean sendBean;
    private String discGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_name_editor_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }


    private void initViews() {
        rightButton=findViewById(R.id.rightButton);
        backButton=findViewById(R.id.backButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        titleName=(TextView)findViewById(R.id.titleName);
        groupName=(EditText)findViewById(R.id.groupName);
    }


    private void initDatas() {
        sendBean=(SGNewsBean)getIntent().getSerializableExtra("chatBean");
        titleName.setText("群聊名称");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        rightButtonText.setText("完成");
        groupName.setText(sendBean.getNoticeSubject());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }else if(v.getId()==R.id.rightButton){
            discGroupName=groupName.getText().toString().trim();
            if (!TextUtils.isEmpty(discGroupName)){
                commitAction();
            }else{
                Toast.makeText(this,"请输入群聊名称",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void commitAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId",sendBean.getNoticeId());
            obj.put("groupName",discGroupName);
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_EDITOR_DISC_GROUP_NAME);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_EDITOR_DISC_GROUP_NAME);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void sendEditorGroupNameMsg(String discGroupName,String groupId){
        try {
            MultiUserChat chat= EixstMultiRoomsUtils.getMultiUserChat(sendBean.getNoticeId(), this);
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);

            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", discGroupName);
            bodyMsg.put("type", ConstTaskTag.GROUP_DISC_NAME);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("groupId", sendBean.getNoticeId());
            bodyMsg.put("modfyUserName", UserPreferencesUtil.getUserNickName(this));
            messageContent = bodyMsg.toString();
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_NAME);
            message.setBody(messageContent);
            chat.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_EDITOR_DISC_GROUP_NAME:
                if ("0000".equals(data.getCode())) {
                    sendEditorGroupNameMsg(discGroupName,sendBean.getNoticeId());
                    Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                    intent.putExtra("flag", Constants.EDITOR_DISC_GROUP_NAME);
                    intent.putExtra(Constants.EDITOR_DISC_GROUP_NAME,discGroupName);
                    intent.putExtra("groupName",discGroupName);
                    intent.putExtra("groupId",sendBean.getNoticeId());
                    sendBroadcast(intent);
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }
}
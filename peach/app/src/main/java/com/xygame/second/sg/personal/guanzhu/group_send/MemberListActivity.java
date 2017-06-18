package com.xygame.second.sg.personal.guanzhu.group_send;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.guanzhu.group_send.sort.CharacterParser;
import com.xygame.second.sg.personal.guanzhu.group_send.sort.PinyinComparator;
import com.xygame.second.sg.personal.guanzhu.group_send.sort.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoTextListener;
import com.xygame.sg.activity.commen.TwoButtonLargeTextDialog;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 2016/9/2.
 */
public class MemberListActivity extends SGBaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener,AdapterView.OnItemClickListener {
    private TextView titleName,rightButtonText;
    private View backButton,nextButton,rightButton;
    private ListView mListView;
    private SideBar sideBar;
    private TextView dialog;
    private MembersAdapter adapter;
    private TransferMemberBean transferMemberBean;
    private TwoButtonLargeTextDialog dialogWindows;
    private List<MemberBean> selectedDatas;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        rightButton=findViewById(R.id.rightButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        nextButton=findViewById(R.id.nextButton);
        mListView = (ListView) findViewById(R.id.list);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
    }

    private void initListeners() {
        rightButton.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        nextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        sideBar.setOnTouchingLetterChangedListener(this);
    }

    private void initDatas() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        transferMemberBean=(TransferMemberBean)getIntent().getSerializableExtra("bean");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("全选");
        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        titleName.setText("选择收信人");
        List<MemberBean> vector=transferMemberBean.getVector();
        for (int i=0;i<vector.size();i++){
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(vector.get(i).getUsernick());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                vector.get(i).setSortLetters(sortString.toUpperCase());
            }else{
                vector.get(i).setSortLetters("#");
            }
        }
        Collections.sort(vector, pinyinComparator);
        adapter=new MembersAdapter(this,vector);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.nextButton){
            selectedDatas=adapter.getSelectedMember();
            if (selectedDatas.size()>0){
                showSendDialog();
            }else{
                Toast.makeText(this,"抱歉！请选择收信人",Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId()==R.id.rightButton){
            adapter.selectAll();
        }
    }

    private void showSendDialog() {
        dialogWindows=new TwoButtonLargeTextDialog(this, "群发信息", "输入您想要发送的内容", R.style.dineDialog,
                new ButtonTwoTextListener() {

                    @Override
                    public void confrimListener(String content) {
                        groupSendAction(content);
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialogWindows.show();
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mListView.setSelection(position);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.updateDatas(position);
    }

    private void groupSendAction(String content){
        if (!TextUtils.isEmpty(content)) {
            ShowMsgDialog.showNoMsg(this, false);
            ThreadPool.getInstance().excuseThread(new ExcuseSendAction(content));
        }else{
            Toast.makeText(this, "请填写群发信息", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0:
                    ShowMsgDialog.cancel();
                    dialogWindows.dismiss();
                    Toast.makeText(MemberListActivity.this,"群发完成",Toast.LENGTH_SHORT).show();
                    adapter.cancellAllSelect();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class ExcuseSendAction implements Runnable{
        private String content;
        public ExcuseSendAction(String content){
            this.content=content;
        }
        @Override
        public void run() {
            for (MemberBean item:selectedDatas){
                Chat chat=null;
                ToChatBean toChatBean = new ToChatBean();
                toChatBean.setRecruitLocIndex("");
                toChatBean.setNoticeId("");
                toChatBean.setNoticeSubject(item.getUsernick());
                toChatBean.setUserIcon(item.getUserIcon());
                toChatBean.setUserId(item.getUserId());
                toChatBean.setUsernick(item.getUsernick());
                toChatBean.setRecruitId("");

                SGNewsBean msgBean = new SGNewsBean();
                msgBean.setFromUser(SenderUser.getFromUserJsonStr(MemberListActivity.this));
                msgBean.setToUser(SenderUser.getToUserJsonStr(toChatBean.getUserId(), toChatBean.getUserIcon(),
                        toChatBean.getUsernick()));
                msgBean.setNoticeSubject(toChatBean.getNoticeSubject());
                msgBean.setRecruitLocIndex(toChatBean.getRecruitLocIndex());
                msgBean.setNoticeId(toChatBean.getNoticeId());
                msgBean.setFriendNickName(toChatBean.getUsernick());
                msgBean.setFriendUserIcon(toChatBean.getUserIcon());
                msgBean.setFriendUserId(toChatBean.getUserId());
                msgBean.setRecruitId(toChatBean.getRecruitId());
                SGNewsBean tempBean = NewsEngine.getSGNewBeanByFriendUserId(MemberListActivity.this, UserPreferencesUtil.getUserId(MemberListActivity.this),
                        msgBean.getFriendUserId());
                if (tempBean != null) {
                    msgBean = tempBean;
                    msgBean.setIsShow(Constants.NEWS_SHOW);
                    NewsEngine.updateHideOrShowChatItem(MemberListActivity.this, msgBean, UserPreferencesUtil.getUserId(MemberListActivity.this));
                }

                try {
                    if (SGApplication.getInstance().getConnection()!=null){
                        chat = SGApplication.getInstance().getConnection().getChatManager()
                                .createChat(XMPPUtils.getUserJid(MemberListActivity.this, msgBean.getFriendUserId()), null);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                SGNewsBean sendBean = new SGNewsBean();
                try {
                    // 创建消息实体
                    long timestamp = System.currentTimeMillis();
                    String messageContent = "";
                    JSONObject bodyMsg = new JSONObject();
                    bodyMsg.put("msgContent", content);
                    bodyMsg.put("type", Constants.SEND_TEXT);
                    JSONObject ext = new JSONObject();
                    ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
                    ext.put("toUser", new JSONObject(msgBean.getToUser()));
                    ext.put("noticeSubject", msgBean.getNoticeSubject());
                    ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
                    bodyMsg.put("ext", ext);
                    bodyMsg.put("timestamp", timestamp);
                    bodyMsg.put("noticeId", msgBean.getNoticeId());
                    bodyMsg.put("recruitId", msgBean.getRecruitId());
                    messageContent = bodyMsg.toString();
                    String time = XMPPUtils.date2Str(timestamp, XMPPUtils.MS_FORMART);
                    Message message = new Message();
//			message.setProperty("immessage.time", time);
                    message.setBody(messageContent);
                    // 添加实体对象

                    sendBean.setFromUser(msgBean.getFromUser());
                    sendBean.setToUser(msgBean.getToUser());
                    sendBean.setNoticeSubject(msgBean.getNoticeSubject());
                    sendBean.setRecruitLocIndex(msgBean.getRecruitLocIndex());
                    sendBean.setNoticeId(msgBean.getNoticeId());
                    sendBean.setFriendNickName(msgBean.getFriendNickName());
                    sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
                    sendBean.setFriendUserId(msgBean.getFriendUserId());
                    // +++++++++++++++++++++++++++++++++++++++++++++++
                    sendBean.setMsgContent(content);
                    sendBean.setNewType(Constants.NEWS_CHAT);
                    sendBean.setRecruitId(msgBean.getRecruitId());
                    sendBean.setTimestamp(String.valueOf(timestamp));
                    sendBean.setType(Constants.SEND_TEXT);
                    sendBean.setUserId(UserPreferencesUtil.getUserId(MemberListActivity.this));
                    sendBean.setMessageStatus(Constants.NEWS_READ);
                    sendBean.setInout(Constants.NEWS_END);
                    sendBean.setIsShow(Constants.NEWS_SHOW);
                    sendBean.setMsgStatus(Constants.NEWS_SENDING);
                    // 新增本地数据
                    NewsEngine.inserChatNew(MemberListActivity.this, sendBean);
                    // 发送消息
                    DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
//			String messageId=message.getPacketID();
                    chat.sendMessage(message);
                    sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                    NewsEngine.updateChatSendStatus(MemberListActivity.this, sendBean, UserPreferencesUtil.getUserId(MemberListActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                    sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                    NewsEngine.updateChatSendStatus(MemberListActivity.this, sendBean, UserPreferencesUtil.getUserId(MemberListActivity.this));
                }
            }
            mHandler.sendEmptyMessage(0);
        }
    }
}

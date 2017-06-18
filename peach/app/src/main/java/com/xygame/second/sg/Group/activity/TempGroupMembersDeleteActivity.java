package com.xygame.second.sg.Group.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.im.util.OnwerKickMember;
import com.xygame.second.sg.Group.adapter.GroupMemberAdapter;
import com.xygame.second.sg.Group.adapter.TempGroupMemberDeleAdapter;
import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.personal.blacklist.ListViewCompat;
import com.xygame.second.sg.personal.blacklist.sort.CharacterParser;
import com.xygame.second.sg.personal.blacklist.sort.PinyinComparator;
import com.xygame.second.sg.personal.blacklist.sort.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.im.TransferBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 2016/9/6.
 */
public class TempGroupMembersDeleteActivity extends SGBaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener ,AdapterView.OnItemClickListener{
    private TextView titleName,rightButtonText;
    private View backButton,rightButton;
    private TempGroupMemberDeleAdapter adapter;
    private ListView mListView;
    private SideBar sideBar;
    private TextView dialog;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private SGNewsBean sendBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_group_member_dele_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        mListView = (ListView) findViewById(R.id.list);
        rightButton=findViewById(R.id.rightButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        sideBar.setOnTouchingLetterChangedListener(this);
        mListView.setOnItemClickListener(this);
        rightButton.setOnClickListener(this);
    }

    private void initDatas() {
        sendBean=(SGNewsBean)getIntent().getSerializableExtra("chatBean");
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText("群成员");
        adapter = new TempGroupMemberDeleAdapter(this, null);
        mListView.setAdapter(adapter);
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.rightButton){
            List<BlackMemberBean> tempDatas=adapter.getSelectDatas();
            if (tempDatas.size()>0){
                if (SGApplication.getInstance().getConnection().isConnected()) {
                    commitAction(tempDatas);
                }else {
                    Toast.makeText(this,"服务器连接失败，稍后重试",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"请选择成员",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void commitAction( List<BlackMemberBean> tempDatas) {
        RequestBean item = new RequestBean();
        try {
            JSONArray array=new JSONArray();
            for (BlackMemberBean it:tempDatas){
                array.put(Long.parseLong(it.getUserId()));
            }
            JSONObject obj = new JSONObject();
            obj.put("groupId", sendBean.getNoticeId());
            obj.put("groupName",sendBean.getNoticeSubject());
            obj.put("members",array);
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DISC_GROUP_KICK);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DISC_GROUP_KICK);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId", sendBean.getNoticeId());
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DISC_GROUP_MEMBER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_MEMBERS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GROUP_MEMBERS:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_DISC_GROUP_KICK:
                if ("0000".equals(data.getCode())) {
                    StringBuffer sb=new StringBuffer();
                    TransferBean transferBean=new TransferBean();
                    List<BlackMemberBean> tempDatas=adapter.getSelectDatas();
                    List<UserBeanInfo> userBeanInfos=new ArrayList<>();
                    for (BlackMemberBean it:tempDatas){
                        UserBeanInfo item=new UserBeanInfo();
                        item.setUserId(it.getUserId());
                        item.setUserImage(it.getUserIcon());
                        item.setUserName(it.getUsernick());
                        userBeanInfos.add(item);
                        sb.append(it.getUsernick().concat("、"));
                    }
                    String content=sb.substring(0, sb.length() - 1);
                    SGNewsBean resultBean=insertTipNews(content.concat("被踢出了讨论组"), sendBean);
                    transferBean.setUserBeanInfos(userBeanInfos);
                    Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                    intent.putExtra("flag", Constants.KIKT_DISC_GROUP);
                    intent.putExtra("transferBean",transferBean);
                    intent.putExtra("resultBean1",resultBean);
                    sendBroadcast(intent);
                    adapter.removing(userBeanInfos);
                    rightButtonText.setText("（"+adapter.getSelectDatas().size()+"）");
                    ThreadPool.getInstance().excuseThread(new OnwerKickMember(this,sendBean,userBeanInfos));
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private SGNewsBean insertTipNews(String content, SGNewsBean resource){
        SGNewsBean sendBean = new SGNewsBean();
        // 创建消息实体
        long timestamp = System.currentTimeMillis();
        sendBean.setFromUser(SenderUser.getFromUserJsonStr(this));
        sendBean.setNoticeSubject(resource.getNoticeSubject());
        sendBean.setRecruitLocIndex("");
        sendBean.setNoticeId(resource.getNoticeId());
        sendBean.setFriendNickName("");
        sendBean.setFriendUserIcon("");
        sendBean.setFriendUserId("");
        // +++++++++++++++++++++++++++++++++++++++++++++++
        sendBean.setMsgContent(content);
        sendBean.setNewType(Constants.GROUP_CHAT);
        sendBean.setRecruitId("");
        sendBean.setTimestamp(String.valueOf(timestamp));
        sendBean.setType(Constants.SEND_TEXT_TIP);
        sendBean.setUserId(UserPreferencesUtil.getUserId(this));
        sendBean.setMessageStatus(Constants.NEWS_READ);
        sendBean.setInout(Constants.NEWS_END);
        sendBean.setIsShow(Constants.NEWS_SHOW);
        sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
        // 新增本地数据
        TempGroupNewsEngine.inserChatNew(this, sendBean);
        return sendBean;
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            try {
                if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
                    List<BlackMemberBean> datas = new ArrayList<>();
                    JSONArray array2 = new JSONArray(resposeStr);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        BlackMemberBean item = new BlackMemberBean();
                        item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        item.setAge(StringUtils.getJsonValue(object1, "age"));
                        item.setGender(StringUtils.getJsonValue(object1, "gender"));
                        if (!item.getUserId().equals(UserPreferencesUtil.getUserId(this))){
                            //汉字转换成拼音
                            String pinyin = characterParser.getSelling(item.getUsernick());
                            String sortString = pinyin.substring(0, 1).toUpperCase();

                            // 正则表达式，判断首字母是否是英文字母
                            if(sortString.matches("[A-Z]")){
                                item.setSortLetters(sortString.toUpperCase());
                            }else{
                                item.setSortLetters("#");
                            }
                            datas.add(item);
                        }
                    }
                    // 根据a-z进行排序源数据
                    Collections.sort(datas, pinyinComparator);
                    adapter.addDatas(datas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        BlackMemberBean item=adapter.getItem(position);
        if (UserPreferencesUtil.getUserId(this).equals(item.getUserId())){
            Toast.makeText(this,"自己不能踢出自己",Toast.LENGTH_SHORT).show();
        }else{
            adapter.updateItemStatus(item);
            rightButtonText.setText("（"+adapter.getSelectDatas().size()+"）");
        }
    }
}
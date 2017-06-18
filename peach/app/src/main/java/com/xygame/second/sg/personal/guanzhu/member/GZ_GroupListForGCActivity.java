package com.xygame.second.sg.personal.guanzhu.member;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBean;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTransfer;
import com.xygame.second.sg.personal.guanzhu.group_send.TransferMemberBean;
import com.xygame.second.sg.personal.guanzhu.member.sort.CharacterParser;
import com.xygame.second.sg.personal.guanzhu.member.sort.PinyinComparator;
import com.xygame.second.sg.personal.guanzhu.member.sort.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.im.TempGroupChatActivity;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.im.TransferBean;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
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
 * Created by tony on 2016/10/8.
 */
public class GZ_GroupListForGCActivity extends SGBaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener,AdapterView.OnItemClickListener{
    private TextView titleName,rightButtonText;
    private View backButton,searchView,rightButton;
    private MemberSelectAdapter adapter;
    private ListView mListView;
    private List<GroupMemberBean> selectMembers;
    private GZ_GroupBeanTransfer bean;
    private GZ_GroupBeanTemp currGroupBean;
    private List<GroupMemberBean> selectPersons=new ArrayList<>();
    private SideBar sideBar;
    private  TransferBean transferBean;
    private TextView dialog;
    private SGNewsBean sendBean;
    private GroupMemberBean currSelectedUnavilbeBean;
    private String groupName;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private List<GZ_GroupBeanTemp> alldatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_select_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        rightButton=findViewById(R.id.rightButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        searchView=findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.list);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
    }

    private void initListeners() {
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        searchView.setOnClickListener(this);
        sideBar.setOnTouchingLetterChangedListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void initDatas() {
        currSelectedUnavilbeBean=(GroupMemberBean)getIntent().getSerializableExtra("currUserId");
        transferBean=(TransferBean)getIntent().getSerializableExtra("transferBean");
        sendBean=(SGNewsBean)getIntent().getSerializableExtra("sendBean");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        bean = (GZ_GroupBeanTransfer) getIntent().getSerializableExtra("bean");
        currGroupBean = bean.getCurrBean();
        selectMembers=bean.getSelectMembers0();
        alldatas=bean.getAlldatas();
        updateMembers();
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText(currGroupBean.getName());

        adapter = new MemberSelectAdapter(this, null);
        mListView.setAdapter(adapter);
        loadDatas();
    }

    private void updateMembers() {
        int count=0;
        for (GZ_GroupBeanTemp it:alldatas){
            List<GroupMemberBean> tempDatas = it.getTempDatas();
            if (tempDatas!=null){
                count=count+tempDatas.size();
            }
        }
        if (selectMembers!=null){
            count=count+selectMembers.size();
        }
        rightButtonText.setText("确定（"+count+"）");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            TransferMemberBean bean=new TransferMemberBean();
            bean.setSelectMembers(adapter.getSelectDatas());
            Intent intent=new Intent("com.xygame.group.selectmembers.action");
            intent.putExtra("groupId",currGroupBean.getId());
            intent.putExtra("bean",bean);
            intent.putExtra("isClose",false);
            sendBroadcast(intent);
            finish();
        }else if (v.getId()==R.id.searchView){
        }else if (v.getId()==R.id.rightButton){
            if (sendBean!=null){
                String tempStr=rightButtonText.getText().toString();
                if (!"确定（0）".equals(tempStr)){
                    groupName=sendBean.getNoticeSubject();
                    addMembersAction();
                }else{
                    Toast.makeText(this,"请选择联系人",Toast.LENGTH_SHORT).show();
                }
            }else {
                String tempStr=rightButtonText.getText().toString();
                if (!"确定（0）".equals(tempStr)){
                    createGroupAction();
                }else{
                    Toast.makeText(this,"请选择联系人",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void addMembersAction() {
        RequestBean item = new RequestBean();
        try {
            JSONArray jsonArray=new JSONArray();
            List<GroupMemberBean> tempDatas=adapter.getSelectDatas();
            if (tempDatas!=null){
                for (GroupMemberBean memberBean:tempDatas){
                    jsonArray.put(Long.parseLong(memberBean.getUserId()));
                    selectPersons.add(memberBean);
                }
            }
            for (GZ_GroupBeanTemp it:alldatas){
                List<GroupMemberBean> tempDatas1 = it.getTempDatas();
                if (tempDatas1!=null){
                    for (GroupMemberBean memberBean:tempDatas1){
                        jsonArray.put(Long.parseLong(memberBean.getUserId()));
                        selectPersons.add(memberBean);
                    }
                }
            }
            JSONObject obj = new JSONObject();
            obj.put("groupName", sendBean.getNoticeSubject());
            obj.put("groupId",sendBean.getNoticeId());
            obj.put("members",jsonArray);
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DISC_GROUP_ADD);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DISC_GROUP_ADD1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void createGroupAction() {
        RequestBean item = new RequestBean();
        try {
            JSONArray jsonArray=new JSONArray();
            List<GroupMemberBean> tempDatas=adapter.getSelectDatas();
            if (tempDatas!=null){
                for (GroupMemberBean memberBean:tempDatas){
                    jsonArray.put(Long.parseLong(memberBean.getUserId()));
                    selectPersons.add(memberBean);
                }
            }
            for (GZ_GroupBeanTemp it:alldatas){
                List<GroupMemberBean> tempDatas1 = it.getTempDatas();
                if (tempDatas1!=null){
                    for (GroupMemberBean memberBean:tempDatas1){
                        jsonArray.put(Long.parseLong(memberBean.getUserId()));
                        selectPersons.add(memberBean);
                    }
                }
            }
            if (getSubCount()>=2){
                JSONObject obj = new JSONObject();
                obj.put("groupName", UserPreferencesUtil.getUserNickName(this).concat("的讨论组"));
                obj.put("memberIds",jsonArray);
                ShowMsgDialog.showNoMsg(this, false);
                item.setData(obj);
                item.setServiceURL(ConstTaskTag.QUEST_CREATE_DISC_GROUP);
                ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CREATE_DISC_GROUP2);
            }else{
                Toast.makeText(this,"至少邀请两位好友才能创建讨论组哦",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TransferMemberBean bean=new TransferMemberBean();
            bean.setSelectMembers(adapter.getSelectDatas());
            Intent intent=new Intent("com.xygame.group.selectmembers.action");
            intent.putExtra("groupId",currGroupBean.getId());
            intent.putExtra("bean",bean);
            intent.putExtra("isClose",false);
            sendBroadcast(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId",currGroupBean.getId());
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_GZ_GROUP_MEMBERS);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GZ_GROUP_MEMBERS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_DISC_GROUP_ADD1:
                if ("0000".equals(data.getCode())) {
                    parseAddGroupInfo(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_CREATE_DISC_GROUP2:
                if ("0000".equals(data.getCode())) {
                    parseGroupInfo(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GZ_GROUP_MEMBERS:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseAddGroupInfo(ResponseBean data) {
        try {
            String unjoinUsers=data.getRecord();
            if (ConstTaskTag.isTrueForArrayObj(unjoinUsers)){
                TransferBean transferBean=new TransferBean();
                List<UserBeanInfo> userBeanInfos=new ArrayList<>();
                StringBuffer sbJion=new StringBuffer(),sbUnJion=new StringBuffer();
                JSONArray array=new JSONArray(unjoinUsers);
                List<GroupMemberBean> deleteDatas=new ArrayList<>();
                for (int i=0;i<array.length();i++){
                    String removeId=array.getString(i);
                    for (int j=0;j<selectPersons.size();j++){
                        if (removeId.equals(selectPersons.get(j).getUserId())){
                            sbUnJion.append(selectPersons.get(j).getUsernick().concat("、"));
                            deleteDatas.add(selectPersons.get(j));
                            break;
                        }
                    }
                }
                String content1=sbUnJion.substring(0,sbUnJion.length()-1);
                SGNewsBean resultBean1= insertTipNews(true,content1.concat("没有关注你无法加入讨论组"), sendBean.getNoticeId());
                for (GroupMemberBean memberBean:deleteDatas){
                    for (int i=0;i<selectPersons.size();i++){
                        if (memberBean.getUserId().equals(selectPersons.get(i).getUserId())){
                            selectPersons.remove(i);
                        }
                    }
                }
                for (GroupMemberBean memberBean:selectPersons){
                    UserBeanInfo item=new UserBeanInfo();
                    item.setUserId(memberBean.getUserId());
                    item.setUserImage(memberBean.getUserIcon());
                    item.setUserName(memberBean.getUsernick());
                    userBeanInfos.add(item);
                    sbJion.append(memberBean.getUsernick().concat("、"));
                }
                transferBean.setUserBeanInfos(userBeanInfos);
                SGNewsBean resultBean2=null;
                if (sbJion.length()>0){
                    String content=sbJion.substring(0,sbJion.length()-1);
                    resultBean2=insertTipNews(true,content.concat("加入了讨论组"),sendBean.getNoticeId());
                    XMPPUtils.sendInviteFriendMsg(this,content,sendBean);
                }
                Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                intent.putExtra("flag", Constants.ADD_DISC_GROUP);
                intent.putExtra("transferBean",transferBean);
                intent.putExtra("resultBean1",resultBean1);
                intent.putExtra("resultBean2",resultBean2);
                sendBroadcast(intent);
                finish();
            }else {
                TransferBean transferBean=new TransferBean();
                List<UserBeanInfo> userBeanInfos=new ArrayList<>();
                StringBuffer sb=new StringBuffer();
                for (GroupMemberBean memberBean:selectPersons){
                    UserBeanInfo item=new UserBeanInfo();
                    item.setUserId(memberBean.getUserId());
                    item.setUserImage(memberBean.getUserIcon());
                    item.setUserName(memberBean.getUsernick());
                    userBeanInfos.add(item);
                    sb.append(memberBean.getUsernick().concat("、"));
                }
                transferBean.setUserBeanInfos(userBeanInfos);
                String content=sb.substring(0,sb.length()-1);
                SGNewsBean resultBean=insertTipNews(true,content.concat("加入了讨论组"), sendBean.getNoticeId());
                XMPPUtils.sendInviteFriendMsg(this,content,sendBean);
                Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                intent.putExtra("flag", Constants.ADD_DISC_GROUP);
                intent.putExtra("transferBean",transferBean);
                intent.putExtra("resultBean1",resultBean);
                sendBroadcast(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseGroupInfo(ResponseBean data) {
        try {
            if (!TextUtils.isEmpty(data.getRecord())){
                JSONObject jsonObject=new JSONObject(data.getRecord());
                String groupId=StringUtils.getJsonValue(jsonObject,"groupId");
                String unjoinUsers=StringUtils.getJsonValue(jsonObject,"unjoinUsers");
                if (ConstTaskTag.isTrueForArrayObj(unjoinUsers)){
                    StringBuffer sbJion=new StringBuffer(),sbUnJion=new StringBuffer();
                    JSONArray array=new JSONArray(unjoinUsers);
                    List<GroupMemberBean> deleteDatas=new ArrayList<>();
                    for (int i=0;i<array.length();i++){
                        String removeId=array.getString(i);
                        for (int j=0;j<selectPersons.size();j++){
                            if (removeId.equals(selectPersons.get(j).getUserId())){
                                sbUnJion.append(selectPersons.get(j).getUsernick().concat("、"));
                                deleteDatas.add(selectPersons.get(j));
                                break;
                            }
                        }
                    }
                    if (deleteDatas.size()==selectPersons.size()){
                        ConstTaskTag.showConfigDialog(this,"您邀请的好友并未关注您，相互关注之后才能创建讨论组哦");
                    }else{
                        String content1=sbUnJion.substring(0,sbUnJion.length()-1);
                        SGNewsBean sendBean= insertTipNews(false,content1.concat("没有关注你无法加入讨论组"), groupId);
                        for (GroupMemberBean memberBean:deleteDatas){
                            for (int i=0;i<selectPersons.size();i++){
                                if (memberBean.getUserId().equals(selectPersons.get(i).getUserId())){
                                    selectPersons.remove(i);
                                }
                            }
                        }
                        for (GroupMemberBean memberBean:selectPersons){
                            sbJion.append(memberBean.getUsernick().concat("、"));
                        }
                        if (sbJion.length()>0){
                            String content=sbJion.substring(0,sbJion.length()-1);
                            sendBean=insertTipNews(false,content.concat("加入了讨论组"),groupId);
                        }

                        List<GroupBean> value= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(this));
                        if (value==null){
                            value=new ArrayList<>();
                        }
                        GroupBean groupBean=new GroupBean();
                        groupBean.setGroupId(sendBean.getNoticeId());
                        groupBean.setGroupName(sendBean.getNoticeSubject());
                        groupBean.setCreateUserId(UserPreferencesUtil.getUserId(this));
                        value.add(groupBean);
                        CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(this),value);

                        Intent activityIntent=new Intent(this,TempGroupChatActivity.class);
                        activityIntent.putExtra("isHost",true);
                        activityIntent.putExtra("chatBean",sendBean);
                        startActivity(activityIntent);
                        Intent intent = new Intent("com.xygame.group.selectmembers.action");
                        intent.putExtra("isClose", true);
                        sendBroadcast(intent);
                        finish();
                    }
                }else{
                    StringBuffer sb=new StringBuffer();
                    for (GroupMemberBean memberBean:selectPersons){
                        sb.append(memberBean.getUsernick().concat("、"));
                    }
                    String content=sb.substring(0,sb.length()-1);
                    SGNewsBean sendBean=insertTipNews(false,content.concat("加入了讨论组"), groupId);

                    List<GroupBean> value= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(this));
                    if (value==null){
                        value=new ArrayList<>();
                    }
                    GroupBean groupBean=new GroupBean();
                    groupBean.setGroupId(sendBean.getNoticeId());
                    groupBean.setGroupName(sendBean.getNoticeSubject());
                    groupBean.setCreateUserId(UserPreferencesUtil.getUserId(this));
                    value.add(groupBean);
                    CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(this),value);

                    Intent activityIntent=new Intent(this,TempGroupChatActivity.class);
                    activityIntent.putExtra("isHost",true);
                    activityIntent.putExtra("chatBean",sendBean);
                    startActivity(activityIntent);
                    Intent intent = new Intent("com.xygame.group.selectmembers.action");
                    intent.putExtra("isClose", true);
                    sendBroadcast(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SGNewsBean insertTipNews(boolean isOld,String content,String groupId){
        SGNewsBean sendBean = new SGNewsBean();
        // 创建消息实体
        long timestamp = System.currentTimeMillis();
        sendBean.setFromUser(SenderUser.getFromUserJsonStr(this));
        if (isOld){
            sendBean.setNoticeSubject(groupName);
        }else{
            sendBean.setNoticeSubject(UserPreferencesUtil.getUserNickName(this).concat("的讨论组"));
        }
        sendBean.setRecruitLocIndex("");
        sendBean.setNoticeId(groupId);
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
            List<GroupMemberBean> datas = new ArrayList<>();
            try {
                JSONObject object=new JSONObject(resposeStr);
//                fansReqTime=StringUtils.getJsonValue(object,"reqTime");
                String users= StringUtils.getJsonValue(object,"users");
                if (ConstTaskTag.isTrueForArrayObj(users)) {
                    JSONArray array2 = new JSONArray(users);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        GroupMemberBean item = new GroupMemberBean();
                        item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        item.setAge(StringUtils.getJsonValue(object1, "age"));
                        item.setGender(StringUtils.getJsonValue(object1, "gender"));

                        if (transferBean!=null){
                            List<CirclePraisers> tempDatas=transferBean.getDiscGroupMembers();
                            for (CirclePraisers it:tempDatas){
                                if (item.getUserId().equals(it.getUserId())){
                                    item.setIsAlivable(false);
                                    break;
                                }
                            }
                        }else{
                            item.setIsAlivable(true);
                        }
                        //汉字转换成拼音
                        String pinyin = characterParser.getSelling(item.getUsernick());
                        String sortString = pinyin.substring(0, 1).toUpperCase();

                        // 正则表达式，判断首字母是否是英文字母
                        if(sortString.matches("[A-Z]")){
                            item.setSortLetters(sortString.toUpperCase());
                        }else{
                            item.setSortLetters("#");
                        }
                        if (currSelectedUnavilbeBean!=null){
                            if (item.getUserId().equals(currSelectedUnavilbeBean.getUserId())){
                                item.setIsAlivable(false);
                                item.setIsSelect(true);
                            }
                        }
                        if (selectMembers!=null){
                            for (GroupMemberBean it:selectMembers){
                                if (it.getUserId().equals(item.getUserId())){
                                    item.setIsSelect(true);
                                    break;
                                }
                            }
                        }
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 根据a-z进行排序源数据
            Collections.sort(datas, pinyinComparator);
            adapter.addDatas(datas);
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
        GroupMemberBean item=adapter.getItem(position);
        if (item.isAlivable()){
            adapter.updateItem(item);
            updateMembersCurrPage();
        }
    }

    private void updateMembersCurrPage() {
        int count=0;
        for (GZ_GroupBeanTemp it:alldatas){
            List<GroupMemberBean> tempDatas = it.getTempDatas();
            if (tempDatas!=null){
                count=count+tempDatas.size();
            }
        }
        List<GroupMemberBean> selectDatas=adapter.getSelectDatas();
        count=count+selectDatas.size();
        rightButtonText.setText("确定（"+count+"）");
    }

    private int getSubCount(){
        int count=0;
        for (GZ_GroupBeanTemp it:alldatas){
            List<GroupMemberBean> tempDatas = it.getTempDatas();
            if (tempDatas!=null){
                count=count+tempDatas.size();
            }
        }
        List<GroupMemberBean> selectDatas=adapter.getSelectDatas();
        count=count+selectDatas.size();
        return count;
    }
}
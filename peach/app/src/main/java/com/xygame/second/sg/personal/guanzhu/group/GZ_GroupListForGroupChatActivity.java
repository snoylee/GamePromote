package com.xygame.second.sg.personal.guanzhu.group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.comm.inteface.DeleteGZ_GroupListener;
import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.second.sg.personal.guanzhu.group.sort.CharacterParser;
import com.xygame.second.sg.personal.guanzhu.group.sort.PinyinComparator;
import com.xygame.second.sg.personal.guanzhu.group.sort.SideBar;
import com.xygame.second.sg.personal.guanzhu.group_send.TransferMemberBean;
import com.xygame.second.sg.personal.guanzhu.member.GZ_AllMemberListActivity;
import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
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
public class GZ_GroupListForGroupChatActivity extends SGBaseActivity implements View.OnClickListener, SlideView.OnSlideListener, DeleteGZ_GroupListener, SideBar.OnTouchingLetterChangedListener{
    private TextView titleName, rightButtonText,personalCount;
    private View backButton, rightButton;
    private SlideView mLastSlideViewWithStatusOn;
    private SlideForGroupChatAdapter adapter;
    private ListViewCompat mListView;
    private GZ_GroupBean tempActionBean;
    private String constName="未分组";
    private String groupCount="0";
    private SGNewsBean sendBean;
    private  TransferBean transferBean;
    private SideBar sideBar;
    private TextView dialog;
    private SGNewsBean currUserBean;
    private GroupMemberBean currSelectedUnavilbeBean;
    private List<GroupMemberBean> selectPersons=new ArrayList<>();
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<GroupMemberBean> selectMembers0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gz_list_layout);
        registerBoradcastReceiver();
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        rightButton = findViewById(R.id.rightButton);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        mListView = (ListViewCompat) findViewById(R.id.list);
        View view = LayoutInflater.from(this).inflate(
                R.layout.gz_list_item, null);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        personalCount=(TextView)view.findViewById(R.id.personalCount);
        userName.setText(constName);
        view.setOnClickListener(new IntoMemberlist());
        mListView.addHeaderView(view);
    }

    private void initListeners() {
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        sideBar.setOnTouchingLetterChangedListener(this);
    }

    private void initDatas() {
        currUserBean=(SGNewsBean)getIntent().getSerializableExtra("currUserId");
        transferBean=(TransferBean)getIntent().getSerializableExtra("transferBean");
        sendBean=(SGNewsBean)getIntent().getSerializableExtra("chatBean");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        if (currUserBean!=null){
            selectMembers0=new ArrayList<>();
            currSelectedUnavilbeBean = new GroupMemberBean();
            currSelectedUnavilbeBean.setUsernick(currUserBean.getFriendNickName());
            currSelectedUnavilbeBean.setUserIcon(currUserBean.getFriendUserIcon());
            currSelectedUnavilbeBean.setUserId(currUserBean.getFriendUserId());
            selectMembers0.add(currSelectedUnavilbeBean);
            rightButtonText.setText("确定（" + selectMembers0.size()+"）");
        }else {
            rightButtonText.setText("确定（0）");
        }

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText("从分组中选择联系人");
        personalCount.setText(groupCount.concat("人"));

        adapter = new SlideForGroupChatAdapter(this, null,sendBean,transferBean,currSelectedUnavilbeBean,selectMembers0);
        adapter.addCancelBlackListListener(this);
        adapter.addSlidViewListener(this);
        mListView.setAdapter(adapter);
//        List<GZ_GroupBean> datas=CacheService.getInstance().getCacheGroupDatasDatas(UserPreferencesUtil.getUserId(this));
//        if (datas!=null){
//            adapter.addDatas(datas);
//        }else{
        loadDatas();
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            if (sendBean!=null){
                String tempStr=rightButtonText.getText().toString();
                if (!"确定（0）".equals(tempStr)){
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
            List<GZ_GroupBean> alldatas=adapter.getDatas();
            for (GZ_GroupBean it:alldatas){
                List<GroupMemberBean> tempDatas=it.getTempDatas();
                if (tempDatas!=null){
                    for (GroupMemberBean memberBean:tempDatas){
                        jsonArray.put(Long.parseLong(memberBean.getUserId()));
                        selectPersons.add(memberBean);
                    }
                }
            }
            if (selectMembers0!=null){
                for (GroupMemberBean memberBean:selectMembers0){
                    jsonArray.put(Long.parseLong(memberBean.getUserId()));
                    selectPersons.add(memberBean);
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("groupName", sendBean.getNoticeSubject());
            obj.put("groupId",sendBean.getNoticeId());
            obj.put("members",jsonArray);
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DISC_GROUP_ADD);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DISC_GROUP_ADD);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void createGroupAction() {
        RequestBean item = new RequestBean();
        try {
            JSONArray jsonArray=new JSONArray();
            List<GZ_GroupBean> alldatas=adapter.getDatas();
            for (GZ_GroupBean it:alldatas){
                List<GroupMemberBean> tempDatas=it.getTempDatas();
                if (tempDatas!=null){
                    for (GroupMemberBean memberBean:tempDatas){
                        jsonArray.put(Long.parseLong(memberBean.getUserId()));
                        selectPersons.add(memberBean);
                    }
                }
            }
            if (selectMembers0!=null){
                for (GroupMemberBean memberBean:selectMembers0){
                    jsonArray.put(Long.parseLong(memberBean.getUserId()));
                    selectPersons.add(memberBean);
                }
            }
            if (getSubCount()>=2){
                JSONObject obj = new JSONObject();
                obj.put("groupName", UserPreferencesUtil.getUserNickName(this).concat("的讨论组"));
                obj.put("memberIds",jsonArray);
                ShowMsgDialog.showNoMsg(this, false);
                item.setData(obj);
                item.setServiceURL(ConstTaskTag.QUEST_CREATE_DISC_GROUP);
                ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CREATE_DISC_GROUP);
            }else{
                Toast.makeText(this,"至少邀请两位好友才能创建讨论组哦",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    @Override
    public void cancelBlackListListener(Integer position, GZ_GroupBean blackMemberBean) {
        this.tempActionBean = blackMemberBean;
        showComfirmDialog();
    }

    private void showComfirmDialog(){
        TwoButtonDialog dialog = new TwoButtonDialog(this,"确定删除该组吗？" , "确定", "取消", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        removeAction();
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialog.show();
    }

    private void removeAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("groupId", tempActionBean.getId());
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_GZ_GROUP_DELE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GZ_GROUP_DELE);
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_GZ_GROUP);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GZ_GROUP);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_DISC_GROUP_ADD:
                if ("0000".equals(data.getCode())) {
                    parseAddGroupInfo(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_CREATE_DISC_GROUP:
                if ("0000".equals(data.getCode())) {
                    parseGroupInfo(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GZ_GROUP:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GZ_GROUP_DELE:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
//                    CacheService.getInstance().cacheGroupDatasDatas(UserPreferencesUtil.getUserId(this), adapter.getDatas());
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
                    CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(this), value);

                    Intent activityIntent=new Intent(this,TempGroupChatActivity.class);
                    activityIntent.putExtra("isHost",true);
                    activityIntent.putExtra("chatBean",sendBean);
                    startActivity(activityIntent);
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
            sendBean.setNoticeSubject(sendBean.getNoticeSubject());
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
            List<GZ_GroupBean> datas = new ArrayList<>();
            try {
                if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
                    JSONArray array2 = new JSONArray(resposeStr);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        GZ_GroupBean item = new GZ_GroupBean();
                        item.setName(StringUtils.getJsonValue(object1, "groupName"));
                        item.setId(StringUtils.getJsonValue(object1, "id"));
                        if (item.getId().equals("0")){
                            groupCount=StringUtils.getJsonValue(object1, "count");
                        }else{
                            item.setCount(StringUtils.getJsonValue(object1, "count"));
                            //汉字转换成拼音
                            String pinyin = characterParser.getSelling(item.getName());
                            String sortString = pinyin.substring(0, 1).toUpperCase();

                            // 正则表达式，判断首字母是否是英文字母
                            if (sortString.matches("[A-Z]")) {
                                item.setSortLetters(sortString.toUpperCase());
                            } else {
                                item.setSortLetters("#");
                            }
                            datas.add(item);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 根据a-z进行排序源数据
            Collections.sort(datas, pinyinComparator);
//            CacheService.getInstance().cacheGroupDatasDatas(UserPreferencesUtil.getUserId(this),datas);
            adapter.addDatas(datas);
            personalCount.setText(groupCount.concat("人"));
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mListView.setSelection(position-1);
        }
    }

    private class IntoMemberlist implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            intoMemberList();
        }
    }

    private void intoMemberList() {
        GZ_GroupBeanTemp it = new GZ_GroupBeanTemp();
        it.setId("0");
        it.setName(constName);
        GZ_GroupBeanTransfer transfer = new GZ_GroupBeanTransfer();
        transfer.setCurrBean(it);
        List<GZ_GroupBean> dapterDatas = adapter.getDatas();
        List<GZ_GroupBeanTemp> alldatas = new ArrayList<>();
        for (GZ_GroupBean its : dapterDatas) {
            GZ_GroupBeanTemp it1 = new GZ_GroupBeanTemp();
            it1.setId(its.getId());
            it1.setName(its.getName());
            it1.setTempDatas(its.getTempDatas());
            alldatas.add(it1);
        }
        alldatas.add(0, it);
        transfer.setAlldatas(alldatas);
        transfer.setSelectMembers0(selectMembers0);
        Intent intent = new Intent(this, GZ_AllMemberListActivity.class);
        intent.putExtra("sendBean",sendBean);
        intent.putExtra("transferBean",transferBean);
        intent.putExtra("currUserId",currSelectedUnavilbeBean);
        intent.putExtra("bean", transfer);
        startActivity(intent);
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.xygame.group.dynamic.count.action");
        myIntentFilter.addAction("com.xygame.group.selectmembers.action");
        myIntentFilter.addAction(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.xygame.group.dynamic.count.action".equals(intent.getAction())) {
                String sourceGroupId=intent.getStringExtra("sourceGroupId");
                String targGroupId=intent.getStringExtra("targGroupId");
                boolean isAdd=intent.getBooleanExtra("addFlag",false);
                adapter.updateGroupCount(sourceGroupId,targGroupId,isAdd);
            }else if ("com.xygame.group.selectmembers.action".equals(intent.getAction())){
                boolean isClose=intent.getBooleanExtra("isClose", false);
                if (isClose){
                    finish();
                }else{
                    String groupId=intent.getStringExtra("groupId");
                    TransferMemberBean bean=(TransferMemberBean)intent.getSerializableExtra("bean");
                    if ("0".equals(groupId)){
                        selectMembers0=bean.getSelectMembers();
                        adapter.setUnDividerGroup(selectMembers0);
                    }else{
                        adapter.updateSelectMember(groupId,bean.getSelectMembers());
                    }
                    updateMembers();
                }
            }else  if (XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION.equals(intent.getAction())) {
                String flag=intent.getStringExtra("flag");
                if (Constants.ADD_DISC_GROUP.equals(flag)){
                   finish();
                }
            }
        }
    };
    private void updateMembers() {
        List<GZ_GroupBean> alldatas=adapter.getDatas();
        int count=0;
        for (GZ_GroupBean it:alldatas){
            List<GroupMemberBean> tempDatas=it.getTempDatas();
            if (tempDatas!=null){
                count=count+tempDatas.size();
            }
        }
        if (selectMembers0!=null){
            count=count+selectMembers0.size();
        }
        rightButtonText.setText("确定（"+count+"）");
    }
    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    private int getSubCount(){
        List<GZ_GroupBean> alldatas=adapter.getDatas();
        int count=0;
        for (GZ_GroupBean it:alldatas){
            List<GroupMemberBean> tempDatas=it.getTempDatas();
            if (tempDatas!=null){
                count=count+tempDatas.size();
            }
        }
        if (selectMembers0!=null){
            count=count+selectMembers0.size();
        }
        return count;
    }
}
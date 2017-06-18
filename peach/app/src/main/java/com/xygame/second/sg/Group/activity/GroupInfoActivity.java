package com.xygame.second.sg.Group.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.bean.GoupNoticeBean;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.Group.bean.TransferChatBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.adapter.main.CirclePriserAdapter;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天对话.
 */
public class GroupInfoActivity extends SGBaseActivity implements OnClickListener ,AdapterView.OnItemClickListener {
    private View backButton, rightButton, moreVie, myGroupNameView,guanZhuButtonView;
    private TextView titleName, oralText, memberNums, myGroupName,guanZhuTipText;
    private ImageView rightbuttonIcon;
    private TextView groupName, groupTip;
    private ImageView groupIamge;
    private ImageLoader mImageLoader;
    private CirclePriserAdapter membersAdapter;
    private GridView membersGrid;
    private GoupNoticeBean goupNoticeBean;
    private String createUserId=null;
    private boolean isGuanZhu=false,isMember=false;
    private boolean isTempleChat;
    private MultiUserChat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_notice_info);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        guanZhuTipText=(TextView)findViewById(R.id.guanZhuTipText);
        guanZhuButtonView=findViewById(R.id.guanZhuButtonView);
        membersGrid = (GridView) findViewById(R.id.membersGrid);
        myGroupName = (TextView) findViewById(R.id.myGroupName);
        myGroupNameView = findViewById(R.id.myGroupNameView);
        moreVie = findViewById(R.id.moreVie);
        memberNums = (TextView) findViewById(R.id.memberNums);
        oralText = (TextView) findViewById(R.id.oralText);
        groupIamge = (ImageView) findViewById(R.id.groupIamge);
        groupName = (TextView) findViewById(R.id.groupName);
        groupTip = (TextView) findViewById(R.id.groupTip);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
    }

    private void initListeners() {
        guanZhuButtonView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        moreVie.setOnClickListener(this);
        myGroupNameView.setOnClickListener(this);
    }

    private void initDatas() {
        rightButton.setVisibility(View.GONE);
        isTempleChat=getIntent().getBooleanExtra("isTempleChat",false);
        goupNoticeBean = (GoupNoticeBean) getIntent().getSerializableExtra("goupNoticeBean");
        membersAdapter = new CirclePriserAdapter(this, null);
        membersGrid.setAdapter(membersAdapter);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("通告群");
        rightbuttonIcon.setVisibility(View.GONE);
//        rightbuttonIcon.setImageResource(R.drawable.more_icon);
        List<GroupBean> groupDatas= CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
        if (groupDatas!=null){
            for (GroupBean item:groupDatas){
                if (goupNoticeBean.getGroupId().equals(item.getGroupId())){
                    isMember=true;
                    break;
                }
            }
        }
        loadGroupInfoData();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            Intent intent = new Intent(this, ShareBoardView.class);
            intent.putExtra("flagFrom", "group");
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.moreVie) {
            Intent intent = new Intent(this, GroupMembersActivity.class);
            intent.putExtra("goupNoticeBean",goupNoticeBean);
            startActivity(intent);
        } else if (v.getId() == R.id.myGroupNameView) {

        }else if (v.getId()==R.id.guanZhuButtonView){
            if (isGuanZhu){//1为关注，2为取消关注
                cancelGuanZhuAction();
            }else{
                guanZhuAction();
            }
        }
    }

    private void cancelGuanZhuAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("groupId", goupNoticeBean.getGroupId());
            item.setData(object);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_GROUP_CANCEL_GUANZHU);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_CANCEL_GUANZHU);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    private void guanZhuAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("groupId", goupNoticeBean.getGroupId());
            item.setData(object);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_GROUP_GUANZHU);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_GUANZHU);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void loadGroupInfoData() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId", goupNoticeBean.getGroupId());
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_GROUP_DETAIL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_DETAIL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GROUP_DETAIL:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GROUP_GUANZHU:
                isGuanZhu=true;
                guanZhuTipText.setText("已关注");
                updateCacheDatas();
                break;
            case ConstTaskTag.QUERY_GROUP_CANCEL_GUANZHU:
                isGuanZhu=false;
                guanZhuTipText.setText("关注");
                updateCacheDatas();
                break;
        }
    }

    private void parseDatas(String record) {

        try {
            if (!TextUtils.isEmpty(record)){
                JSONObject object=new JSONObject(record);
                String hasAttent=StringUtils.getJsonValue(object,"hasAttent");
                if ("1".equals(hasAttent)){
                    isGuanZhu=true;
                    guanZhuTipText.setText("已关注");
                }else{
                    isGuanZhu=false;
                    guanZhuTipText.setText("关注");
                }
                createUserId=StringUtils.getJsonValue(object,"createUserId");
                String groupDesc=StringUtils.getJsonValue(object,"groupDesc");
                oralText.setText(groupDesc);
                groupTip.setText("已有".concat(StringUtils.getJsonValue(object, "noticeCount")).concat("份通告"));
                memberNums.setText(StringUtils.getJsonValue(object, "memberCount"));
                groupName.setText(StringUtils.getJsonValue(object, "groupName"));
                mImageLoader.loadImage(StringUtils.getJsonValue(object, "groupLogo"), groupIamge, true);
                String members=StringUtils.getJsonValue(object,"members");
                if (ConstTaskTag.isTrueForArrayObj(members)) {
                    List<CirclePraisers> value = new ArrayList<>();
                    JSONArray array = new JSONArray(members);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        CirclePraisers item = new CirclePraisers();
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        if (UserPreferencesUtil.getUserId(this).equals(item.getUserId())){
                            if (isMember){
                                value.add(item);
                            }
                        }else{
                            value.add(item);
                        }
                    }
                    membersAdapter=new CirclePriserAdapter(this,value);
                    membersGrid.setAdapter(membersAdapter);
                    membersGrid.setOnItemClickListener(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CirclePraisers presenter=membersAdapter.getItem(position);
        Intent intent = new Intent(this, PersonalDetailActivity.class);
        intent.putExtra("userNick","");
        intent.putExtra("userId", presenter.getUserId());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("jubao".equals(flag)) {
                    boolean islogin = UserPreferencesUtil.isOnline(this);
                    if (!islogin) {
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    } else {
                        if (createUserId!=null){
                            Intent intent = new Intent(this, ReportFristActivity.class);
                            intent.putExtra("resType", Constants.JUBAO_TYPE_CIRCLE);
                            intent.putExtra("userId", createUserId);
                            intent.putExtra("resourceId", goupNoticeBean.getGroupId());
                            startActivity(intent);
                        }else{
                            Toast.makeText(this,"数据完整后重试",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if ("blackList".equals(flag)) {
                    boolean islogin = UserPreferencesUtil.isOnline(this);
                    if (!islogin) {
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    } else {
//                        commitJuBaoAction();
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private void updateCacheDatas() {
       ThreadPool.getInstance().excuseThread(new UpdateCache());
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, isTempleChat);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

    private class UpdateCache implements Runnable{
        @Override
        public void run() {
            try {
                List<GroupBean> groupDatas= CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                Map<String,MultiUserChat> eixstMultiRooms=CacheService.getInstance().getCacheGroupRoomDatas(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                if (groupDatas==null){
                    groupDatas=new ArrayList<>();
                }
                if (eixstMultiRooms==null){
                    eixstMultiRooms=new HashMap<>();
                }
                if (isGuanZhu){
                    GroupBean item=new GroupBean();
                    item.setGroupId(goupNoticeBean.getGroupId());
                    item.setGroupoType("1");
                    item.setUserId(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                    groupDatas.add(item);
                    if(SGApplication.getInstance().getConnection().isConnected()){
                        String serviceName=SGApplication.getInstance().getConnection().getServiceName();
                        String roomId=item.getGroupId().concat("@").concat("conference.").concat(serviceName);
                        chat = new MultiUserChat(SGApplication.getInstance().getConnection(),
                                roomId);
                        GroupEngine.deleteDeleGroupByGroupId(GroupInfoActivity.this,item.getGroupId(),UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                        isTempleChat=false;
                        mHandler.sendEmptyMessage(0);
                        if (!chat.isJoined()){
                            DiscussionHistory history = new DiscussionHistory();
                            GroupBean temp= GroupEngine.quaryGroupBean(GroupInfoActivity.this, item, UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                            if (temp!=null){
                                if (!TextUtils.isEmpty(temp.getLastIntoTimer())){
                                    history.setSince(new Date(Long.parseLong(temp.getLastIntoTimer())));
                                }else{
                                    history.setSince(new Date(System.currentTimeMillis()));
                                }
                            }else{
                                history.setSince(new Date(System.currentTimeMillis()));
                            }
                            chat.join(UserPreferencesUtil.getUserId(GroupInfoActivity.this),null,history, SmackConfiguration.getPacketReplyTimeout());
                        }
                        eixstMultiRooms.put(item.getGroupId(),chat);
                    }
                }else{
                    for (int i=0;i<groupDatas.size();i++){
                        if (goupNoticeBean.getGroupId().equals(groupDatas.get(i).getGroupId())){
                            GroupBean item=new GroupBean();
                            item.setGroupId(groupDatas.get(i).getGroupId());
                            item.setGroupoType("1");
                            item.setUserId(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                            GroupEngine.inserDeleGroup(GroupInfoActivity.this,item);
                            groupDatas.remove(i);
                            isTempleChat=true;
                            break;
                        }
                    }
                    if (eixstMultiRooms.containsKey(goupNoticeBean.getGroupId())){
                        eixstMultiRooms.remove(goupNoticeBean.getGroupId());
                    }
                    mHandler.sendEmptyMessage(1);
                }
                CacheService.getInstance().cacheGroupDatas(UserPreferencesUtil.getUserId(GroupInfoActivity.this),groupDatas);
                CacheService.getInstance().cacheGroupRoomDatas(UserPreferencesUtil.getUserId(GroupInfoActivity.this), eixstMultiRooms);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    isMember=true;
                    CirclePraisers item = new CirclePraisers();
                    item.setUserIcon(UserPreferencesUtil.getHeadPic(GroupInfoActivity.this));
                    item.setUserId(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                    membersAdapter.addMember(item);
                    break;
                case 1:
                    isMember=false;
                    membersAdapter.removingMember(UserPreferencesUtil.getUserId(GroupInfoActivity.this));
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

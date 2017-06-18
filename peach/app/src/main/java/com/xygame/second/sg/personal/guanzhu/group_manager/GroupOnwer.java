package com.xygame.second.sg.personal.guanzhu.group_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.im.util.LosenDiscGroup;
import com.xygame.second.sg.Group.activity.TempGroupMembersActivity;
import com.xygame.second.sg.Group.activity.TempGroupMembersDeleteActivity;
import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupListForGroupChatActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.adapter.main.CirclePriserAdapter;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.TransferBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/11/14.
 */
public class GroupOnwer extends SGBaseActivity implements View.OnClickListener {

    private TextView titleName;
    private View backButton,intoMemberList,editorGroupName,addMemeberButton,canceMemberButton,loseGroup,recent_arrow_iv,editorMembersButton;
    private GridView gridview;
    private TextView memberNums,groupName;
    private SGNewsBean sendBean;
    private CirclePriserAdapter adapter;
    private boolean isKickedOut=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_onwer_detail_layout);
        registerBoradcastReceiver();
        initViews();
        initListeners();
        initDatas();
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION.equals(intent.getAction())) {
               String flag=intent.getStringExtra("flag");
                if (Constants.EDITOR_DISC_GROUP_NAME.equals(flag)){
                    String discGroupName=intent.getStringExtra(Constants.EDITOR_DISC_GROUP_NAME);
                    setEditorGroupName(discGroupName);
                }else if (Constants.ADD_DISC_GROUP.equals(flag)){
                    TransferBean transferBean=(TransferBean)intent.getSerializableExtra("transferBean");
                    List<UserBeanInfo> userBeanInfos=transferBean.getUserBeanInfos();
                    adapter.addUserDatas(userBeanInfos);
                }else if (Constants.KIKT_DISC_GROUP.equals(flag)){
                    TransferBean transferBean=(TransferBean)intent.getSerializableExtra("transferBean");
                    List<UserBeanInfo> userBeanInfos=transferBean.getUserBeanInfos();
                    adapter.reovingUserDatas(userBeanInfos);
                }
            }
        }
    };

    private void setEditorGroupName(String discGroupName){
        groupName.setText(discGroupName);
        sendBean.setNoticeSubject(discGroupName);
//        TempGroupNewsEngine.updateGroupName(this,discGroupName,sendBean.getNoticeId(), UserPreferencesUtil.getUserId(this));
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        intoMemberList.setOnClickListener(this);
        editorGroupName.setOnClickListener(this);
        addMemeberButton.setOnClickListener(this);
        canceMemberButton.setOnClickListener(this);
        loseGroup.setOnClickListener(this);
    }


    private void initViews() {
        editorMembersButton=findViewById(R.id.editorMembersButton);
        recent_arrow_iv=findViewById(R.id.recent_arrow_iv);
        gridview=(GridView)findViewById(R.id.gridview);
        loseGroup=findViewById(R.id.loseGroup);
        backButton=findViewById(R.id.backButton);
        intoMemberList=findViewById(R.id.intoMemberList);
        memberNums=(TextView)findViewById(R.id.memberNums);
        groupName=(TextView)findViewById(R.id.groupName);
        editorGroupName=findViewById(R.id.editorGroupName);
        titleName=(TextView)findViewById(R.id.titleName);
        addMemeberButton=findViewById(R.id.addMemeberButton);
        canceMemberButton=findViewById(R.id.canceMemberButton);
    }


    private void initDatas() {
        isKickedOut=getIntent().getBooleanExtra("isKickedOut",false);
        sendBean=(SGNewsBean)getIntent().getSerializableExtra("chatBean");
        titleName.setText("聊天信息");
        groupName.setText(sendBean.getNoticeSubject());
        if (isKickedOut){
            editorMembersButton.setVisibility(View.GONE);
            recent_arrow_iv.setVisibility(View.GONE);
            loseGroup.setVisibility(View.GONE);
        }else{
            editorMembersButton.setVisibility(View.VISIBLE);
            recent_arrow_iv.setVisibility(View.VISIBLE);
            loseGroup.setVisibility(View.VISIBLE);
        }
        adapter=new CirclePriserAdapter(this,null);
        gridview.setAdapter(adapter);
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }else if(v.getId()==R.id.intoMemberList){
            Intent intent=new Intent(this, TempGroupMembersActivity.class);
            intent.putExtra("groupId",sendBean.getNoticeId());
            startActivity(intent);
        }else if(v.getId()==R.id.editorGroupName){
            if (!isKickedOut){
                Intent intent=new Intent(this, GroupEditorName.class);
                intent.putExtra("chatBean",sendBean);
                startActivity(intent);
            }
        }else if(v.getId()==R.id.addMemeberButton){
            TransferBean transferBean=new TransferBean();
            transferBean.setDiscGroupMembers(adapter.getDatas());
            Intent intent = new Intent(this, GZ_GroupListForGroupChatActivity.class);
            intent.putExtra("chatBean",sendBean);
            intent.putExtra("transferBean",transferBean);
            startActivity(intent);
        }else if(v.getId()==R.id.canceMemberButton){
            Intent intent=new Intent(this, TempGroupMembersDeleteActivity.class);
            intent.putExtra("chatBean",sendBean);
            startActivity(intent);
        }else if(v.getId()==R.id.loseGroup){
            commitAction();
        }
    }

    private void commitAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId",sendBean.getNoticeId());
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_LOSE_DISC_GROUP);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_LOSE_DISC_GROUP);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId",sendBean.getNoticeId());
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DISC_GROUP_MEMBER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DISC_GROUP_MEMBER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_DISC_GROUP_MEMBER:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_LOSE_DISC_GROUP:
                if ("0000".equals(data.getCode())) {
                    XMPPUtils.sendLoseGroupMsg(this,"该讨论组已被解散",sendBean);
                    ThreadPool.getInstance().excuseThread(new LosenDiscGroup(this,sendBean.getNoticeId()));
                    Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                    intent.putExtra("bean",sendBean);
                    intent.putExtra("flag", Constants.LOSE_DISC_GROUP);
                    sendBroadcast(intent);
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<CirclePraisers> datas = new ArrayList<>();
            try {
                if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
                    JSONArray array2 = new JSONArray(resposeStr);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        CirclePraisers item = new CirclePraisers();
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        datas.add(item);
                    }
                }
                memberNums.setText(String.valueOf(datas.size()));
                adapter.addDatas(datas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
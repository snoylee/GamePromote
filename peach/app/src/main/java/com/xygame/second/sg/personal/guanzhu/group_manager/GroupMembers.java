package com.xygame.second.sg.personal.guanzhu.group_manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.activity.TempGroupMembersActivity;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.adapter.main.CirclePriserAdapter;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.SGNewsBean;
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
import java.util.List;

/**
 * Created by tony on 2016/11/14.
 */
public class GroupMembers extends SGBaseActivity implements View.OnClickListener {

    private TextView titleName;
    private View backButton,intoMemberList,editorGroupName,addMemeberButton,canceMemberButton,loseGroup,recent_arrow_iv;
    private GridView gridview;
    private TextView memberNums,groupName;
    private SGNewsBean sendBean;
    private CirclePriserAdapter adapter;
    private boolean isKickedOut=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_members_detail_layout);
        initViews();
        initListeners();
        initDatas();
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
        if (isKickedOut){
            recent_arrow_iv.setVisibility(View.GONE);
            loseGroup.setVisibility(View.GONE);
        }else{
            recent_arrow_iv.setVisibility(View.VISIBLE);
            loseGroup.setVisibility(View.VISIBLE);
        }
        titleName.setText("聊天信息");
        groupName.setText(sendBean.getNoticeSubject());
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
                ShowMsgDialog.showNoMsg(this,true);
                ThreadPool.getInstance().excuseThread(new CheckCacheDiscGroup());
            }
        }else if(v.getId()==R.id.addMemeberButton){
//            Intent intent=new Intent(this, SettingOpinionActivity.class);
//            startActivity(intent);
        }else if(v.getId()==R.id.canceMemberButton){
//            Intent intent=new Intent(this, SettingWhoActivity.class);
//            startActivity(intent);
        }else if(v.getId()==R.id.loseGroup){
            commtAction();
        }
    }

    private Handler mHandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    ShowMsgDialog.cancel();
                    String groupHostId=(String)msg.obj;
                    if(!TextUtils.isEmpty(groupHostId)){
                        Intent intent = new Intent(GroupMembers.this, ReportFristActivity.class);
                        intent.putExtra("resType", Constants.JUBAO_TYPE_DISC_GROUP);
                        intent.putExtra("userId", groupHostId);
                        intent.putExtra("resourceId", sendBean.getNoticeId());
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    private class CheckCacheDiscGroup implements Runnable{
        @Override
        public void run() {
            List<GroupBean> groupDatas= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(GroupMembers.this));
            String groupHostId="";
            for (GroupBean item:groupDatas){
                if (sendBean.getNoticeId().equals(item.getGroupId())){
                    groupHostId=item.getCreateUserId();
                    break;
                }
            }
            Message message=new Message();
            message.obj=groupHostId;
            mHandle.sendMessage(message);
        }
    }

    private void commtAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId",sendBean.getNoticeId());
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DISC_GROUP_TAKOUT);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DISC_GROUP_TAKOUT);
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
            case ConstTaskTag.QUERY_DISC_GROUP_TAKOUT:
                if ("0000".equals(data.getCode())) {
                    XMPPUtils.takoutFriend(this,sendBean.getNoticeId());
                    Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                    intent.putExtra("bean",sendBean);
                    intent.putExtra("flag", Constants.EXIT_DISC_GROUP);
                    sendBroadcast(intent);
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_DISC_GROUP_MEMBER:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
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
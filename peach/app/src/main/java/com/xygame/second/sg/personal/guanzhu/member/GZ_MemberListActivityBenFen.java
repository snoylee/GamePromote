package com.xygame.second.sg.personal.guanzhu.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.GroupMemberListListener;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTransfer;
import com.xygame.second.sg.personal.guanzhu.group_send.MemberBean;
import com.xygame.second.sg.personal.guanzhu.group_send.MemberListActivity;
import com.xygame.second.sg.personal.guanzhu.group_send.TransferMemberBean;
import com.xygame.second.sg.personal.guanzhu.search.GZ_MemberSearchListActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.AlertListDialog;
import com.xygame.sg.activity.commen.DividGroupListener;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/10/8.
 */
public class GZ_MemberListActivityBenFen extends SGBaseActivity implements View.OnClickListener, SlideView.OnSlideListener, GroupMemberListListener,ListViewCompatForAllMembers.OnRefreshListener,
        ListViewCompatForAllMembers.OnLoadListener{
    private TextView titleName,rightButtonText;
    private View backButton,rightButton,searchView;
    private SlideView mLastSlideViewWithStatusOn;
    private AllMemberAdapter adapter;
    private ListViewCompatForAllMembers mListView;
    private GroupMemberBean tempActionBean;
    private GZ_GroupBeanTransfer bean;
    private GZ_GroupBeanTemp currGroupBean;
    private List<GZ_GroupBeanTemp> allDatas;

    private int pageSize = 21;//每页显示的数量
    private int fansPage = 1;//当前显示页数
    private String fansReqTime;
    private boolean fansIsLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_member_list_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        searchView=findViewById(R.id.searchView);
        rightButton=findViewById(R.id.rightButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        mListView = (ListViewCompatForAllMembers) findViewById(R.id.list);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
    }

    private void initListeners() {
        searchView.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
    }

    private void initDatas() {
        String vidoStatus = UserPreferencesUtil.getUserVideoAuth(this);
        String idStatus = UserPreferencesUtil.getUserIDAuth(this);
        String cardStatus = UserPreferencesUtil.getUserCardAuth(this);
        if ("2".equals(idStatus)&&"2".equals(cardStatus)||"2".equals(idStatus)&&"2".equals(vidoStatus)){
            rightButtonText.setVisibility(View.VISIBLE);
            rightButtonText.setText("群发");
            rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        }
        bean = (GZ_GroupBeanTransfer) getIntent().getSerializableExtra("bean");
        currGroupBean = bean.getCurrBean();
        allDatas = bean.getAlldatas();
        for (int i=0;i<allDatas.size();i++){
            if (currGroupBean.getId().equals(allDatas.get(i).getId())){
                allDatas.remove(i);
                break;
            }
        }
        titleName.setText(currGroupBean.getName());

        adapter = new AllMemberAdapter(this, null);
        adapter.addCancelBlackListListener(this);
//        adapter.addSlidViewListener(this);
        mListView.setAdapter(adapter);
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.rightButton){
            List<GroupMemberBean> datas=adapter.getDatas();
            if (datas.size()>0){
                List<MemberBean> vector=new ArrayList<>();
                TransferMemberBean item=new TransferMemberBean();
                for (GroupMemberBean it:datas){
                    MemberBean bean=new MemberBean();
                    bean.setAge(it.getAge());
                    bean.setGender(it.getGender());
                    bean.setSortLetters(it.getSortLetters());
                    bean.setUserIcon(it.getUserIcon());
                    bean.setUserId(it.getUserId());
                    bean.setUsernick(it.getUsernick());
                    vector.add(bean);
                }
                item.setVector(vector);
                Intent intent=new Intent(this, MemberListActivity.class);
                intent.putExtra("bean",item);
                startActivity(intent);
            }else{
                Toast.makeText(this,"抱歉！该组没有成员",Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId()==R.id.searchView){
            Intent intent=new Intent(this, GZ_MemberSearchListActivity.class);
            intent.putExtra("bean",bean);
            startActivityForResult(intent, 0);
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

    private void removeAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("userId", tempActionBean.getUserId());
            object.put("status", "2");
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_USER_ATTEN);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_ATTEN);
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId",currGroupBean.getId());
            obj.put("page", new JSONObject().put("pageIndex", fansPage).put("pageSize", pageSize));
            if (fansPage > 1) {
                obj.put("reqTime", fansReqTime);
            }
//            else {
//                ShowMsgDialog.showNoMsg(this, true);
//            }
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
            case ConstTaskTag.QUERY_GZ_GROUP_MEMBERS:
                mListView.onRefreshComplete();
                mListView.onLoadComplete();
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_ATTEN:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_DIVID_GROUP:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
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
            List<GroupMemberBean> datas = new ArrayList<>();
            try {
                JSONObject object=new JSONObject(resposeStr);
                fansReqTime=StringUtils.getJsonValue(object,"reqTime");
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
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                fansIsLoading = false;
            }
            adapter.addDatas(datas, fansPage);
            mListView.setResultSize(datas.size());
        }else {
            fansIsLoading = false;
        }
    }


    @Override
    public void cancelGZListener(GroupMemberBean blackMemberBean) {
        tempActionBean=blackMemberBean;
        removeAction();
    }

    @Override
    public void dividerListener(GroupMemberBean blackMemberBean) {
        tempActionBean=blackMemberBean;
        if (allDatas.size()>0){
            showDialog();
        }else{
            Toast.makeText(this,"抱歉，您当前没有可用分组",Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        AlertListDialog dialogCreate = new AlertListDialog(this, allDatas, R.style.dineDialog,
                new DividGroupListener() {
                    @Override
                    public void devideAction(GZ_GroupBeanTemp item) {
                        movingGroup(item);
                    }
                });
        dialogCreate.show();
    }

    private void movingGroup(GZ_GroupBeanTemp groupBean) {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("userId", tempActionBean.getUserId());
            object.put("groupId",  groupBean.getId());
            object.put("preGroupId",currGroupBean.getId());
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_DIVID_GROUP);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DIVID_GROUP);
    }

    @Override
    public void onLoad() {
        //加载操作
        if (fansIsLoading) {
            fansPage = fansPage + 1;
            loadDatas();
        } else {
            falseDatas();
        }
    }

    @Override
    public void onRefresh() {
        //刷新操作
        fansIsLoading = true;
        fansPage = 1;
        loadDatas();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    mListView.onLoadComplete();
                    break;
                default:
                    break;
            }

        }
    };


    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 0;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                TransferDeleMemberBean bean=(TransferDeleMemberBean)data.getSerializableExtra(Constants.COMEBACK);
                if (bean!=null) {
                    adapter.deleteMembers(bean.getDeleteMembers());
                }
                break;
            }
            default:
                break;
        }
    }
}
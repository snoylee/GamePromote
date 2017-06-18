package com.xygame.second.sg.Group;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.xygame.second.sg.Group.activity.GroupTypeActivity;
import com.xygame.second.sg.Group.adapter.GroupNoticeAdapter;
import com.xygame.second.sg.Group.bean.CityGroups;
import com.xygame.second.sg.Group.bean.GoupNoticeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.main.NewsFragment;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/5.
 */
public class GroupFrament extends SGBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener , PullToRefreshBase.OnRefreshListener2{

    private View rightButton,backButton;
    private PullToRefreshListView2 listView;
    private GroupNoticeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_fragment);
        registerLoginListener();
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton=findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        listView = (PullToRefreshListView2)findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    private void addListener() {
        rightButton.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        adapter = new GroupNoticeAdapter(this, null);
        listView.setAdapter(adapter);
        loadGroupDatas();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rightButton:
                boolean islogin = UserPreferencesUtil.isOnline(this);
                if (!islogin) {
                    Intent intent11 = new Intent(this, LoginWelcomActivity.class);
                    startActivity(intent11);
                }else{
                    Intent intent7 = new Intent(this, NewsFragment.class);
                    startActivity(intent7);
                }
                break;
            case R.id.nuReadNews:
                boolean islogin1 = UserPreferencesUtil.isOnline(this);
                if (!islogin1) {
                    Intent intent11 = new Intent(this, LoginWelcomActivity.class);
                    startActivity(intent11);
                }else{
                    Intent intent8 = new Intent(this, NewsFragment.class);
                    startActivity(intent8);
                }
                break;

            case R.id.backButton:
                finish();
                break;
        }
    }

    public void loadGroupDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_GROUP_LIST);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_LIST);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GROUP_LIST:
                listView.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                List<CityGroups> cityGroupses=new ArrayList<>();
                List<GoupNoticeBean> goupNoticeBeans=null;
                JSONArray array = new JSONArray(record);
                String cityGroupId="";
                CityGroups cityGroups=null;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    GoupNoticeBean item = new GoupNoticeBean();
                    item.setGroupId(StringUtils.getJsonValue(object, "groupId"));
                    item.setGroupName(StringUtils.getJsonValue(object, "groupName"));
                    item.setGroupType(StringUtils.getJsonValue(object, "groupType"));
                    item.setNoticeCount(StringUtils.getJsonValue(object, "noticeCount"));
                    if (!StringUtils.getJsonValue(object, "province").equals(cityGroupId)){
                        cityGroupId=StringUtils.getJsonValue(object, "province");
                        if (goupNoticeBeans!=null){
                            cityGroups.setGoupNoticeBeans(goupNoticeBeans);
                            int totalCount=0;
                            for (GoupNoticeBean shortBean:goupNoticeBeans){
                                totalCount=totalCount+Integer.parseInt(shortBean.getNoticeCount());
                            }
                            cityGroups.setNoticeCout(totalCount);
                            cityGroupses.add(cityGroups);
                        }
                        goupNoticeBeans = new ArrayList<>();
                        cityGroups=new CityGroups();
                        cityGroups.setGroupLogo(StringUtils.getJsonValue(object, "groupLogo"));
                        cityGroups.setProvinceId(cityGroupId);
                        goupNoticeBeans.add(item);
                    }else{
                        goupNoticeBeans.add(item);
                        if (i==array.length()-1){
                            cityGroups.setGoupNoticeBeans(goupNoticeBeans);
                            int totalCount=0;
                            for (GoupNoticeBean shortBean:goupNoticeBeans){
                                totalCount=totalCount+Integer.parseInt(shortBean.getNoticeCount());
                            }
                            cityGroups.setNoticeCout(totalCount);
                            cityGroupses.add(cityGroups);
                        }
                    }
                }
                if (cityGroupses.size() > 0) {
                    adapter.addDatas(cityGroupses);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        loadGroupDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())) {
                boolean newFlag = intent.getBooleanExtra("newsMessage", false);
                if (newFlag) {
                }
            } else if ("com.xygame.push.dynamic.message.list.action".equals(intent.getAction())) {
            }
        }
    };

    public void registerLoginListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_ACTION);
        myIntentFilter.addAction("com.xygame.push.dynamic.message.list.action");
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener() {
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterLoginListener();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CityGroups item = adapter.getItem(position-1);
        Intent intent = new Intent(this, GroupTypeActivity.class);
        intent.putExtra("bean", item);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 1:
                CityGroups goupNoticeBean=(CityGroups)data.getSerializableExtra(Constants.COMEBACK);
                adapter.updateItemNums(goupNoticeBean);
                break;
            default:
                break;
        }
    }
}

package com.xygame.second.sg.personal.guanzhu.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.GroupMemberListListener;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTransfer;
import com.xygame.second.sg.personal.guanzhu.group_send.MemberBean;
import com.xygame.second.sg.personal.guanzhu.group_send.TransferMemberBean;
import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;
import com.xygame.second.sg.personal.guanzhu.member.SlideAdapter;
import com.xygame.second.sg.personal.guanzhu.member.TransferDeleMemberBean;
import com.xygame.second.sg.personal.guanzhu.member.sort.CharacterParser;
import com.xygame.second.sg.personal.guanzhu.member.sort.PinyinComparator;
import com.xygame.second.sg.personal.guanzhu.member.sort.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.AlertListDialog;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.DividGroupListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 2016/10/8.
 */
public class GZ_MemberSearchListActivity extends SGBaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener, GroupMemberListListener{
    private SlideAdapter adapter;
    private ListView mListView;
    private GroupMemberBean tempActionBean;
    private GZ_GroupBeanTransfer bean;
    private GZ_GroupBeanTemp currGroupBean;
    private List<GZ_GroupBeanTemp> allDatas;
    private SideBar sideBar;
    private TextView dialog;
    private EditText inputTextView;
    private View searchButton;
    private TransferMemberBean transferMemberBean;
    private List<String> deleteMembers;
    private  GZ_GroupBeanTemp targGroup;
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
        setContentView(R.layout.member_search_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        searchButton=findViewById(R.id.searchButton);
        inputTextView=(EditText)findViewById(R.id.inputTextView);
        mListView = (ListView) findViewById(R.id.list);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
    }

    private void initListeners() {
//        mListView.setOnItemClickListener(this);
        searchButton.setOnClickListener(this);
        sideBar.setOnTouchingLetterChangedListener(this);
    }

    private void initDatas() {
        deleteMembers=new ArrayList<>();
        transferMemberBean=(TransferMemberBean)getIntent().getSerializableExtra("beanTransfer");
        bean = (GZ_GroupBeanTransfer) getIntent().getSerializableExtra("bean");
        currGroupBean = bean.getCurrBean();
        allDatas = bean.getAlldatas();
        for (int i=0;i<allDatas.size();i++){
            if (currGroupBean.getId().equals(allDatas.get(i).getId())){
                allDatas.remove(i);
                break;
            }
        }
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        adapter = new SlideAdapter(this, null);
        adapter.addCancelBlackListListener(this);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.searchButton){
            if (!TextUtils.isEmpty(inputTextView.getText().toString().trim())){
                adapter.clearDatas();
                if (transferMemberBean!=null){
                    searchLoadDatas(inputTextView.getText().toString().trim());
                }else{
                    loadDatas();
                }
            }
        }
    }

    private void searchLoadDatas(String content) {
        ShowMsgDialog.showNoMsg(this,false);
        ThreadPool.getInstance().excuseThread(new SearchLocalData(content));
    }

    private class SearchLocalData implements Runnable{
        private String content;
        public SearchLocalData(String content){
            this.content=content;
        }
        @Override
        public void run() {
            List<MemberBean> vector=transferMemberBean.getVector();
            List<GroupMemberBean> datas = new ArrayList<>();
            for (MemberBean memberBean:vector){
                if (memberBean.getUsernick().contains(content)){
                    GroupMemberBean item = new GroupMemberBean();
                    item.setUsernick(memberBean.getUsernick());
                    item.setUserIcon(memberBean.getUserIcon());
                    item.setUserId(memberBean.getUserId());
                    item.setAge(memberBean.getAge());
                    item.setGender(memberBean.getGender());
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
            Message mesg=new Message();
            mesg.what=0;
            mesg.obj=datas;
            mHandler.sendMessage(mesg);
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    ShowMsgDialog.cancel();
                    List<GroupMemberBean> datas=( List<GroupMemberBean>)msg.obj;
                    Collections.sort(datas, pinyinComparator);
                    adapter.addDatas(datas);
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
            obj.put("page", new JSONObject().put("pageIndex", 1).put("pageSize", 50));
            obj.put("content",inputTextView.getText().toString().trim());
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_DIVID_GROUP_SEARCH);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_DIVID_GROUP_SEARCH);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_DIVID_GROUP_SEARCH:
                if ("0000".equals(data.getCode())) {
                    inputTextView.setText("");
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_ATTEN:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
                    deleteMembers.add(tempActionBean.getUserId());
                    Intent intent=new Intent("com.xygame.group.dynamic.count.action");
                    intent.putExtra("sourceGroupId",currGroupBean.getId());
                    intent.putExtra("addFlag", false);
                    sendBroadcast(intent);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_DIVID_GROUP:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
                    deleteMembers.add(tempActionBean.getUserId());
                    Intent intent=new Intent("com.xygame.group.dynamic.count.action");
                    intent.putExtra("targGroupId",targGroup.getId());
                    intent.putExtra("sourceGroupId",currGroupBean.getId());
                    intent.putExtra("addFlag", true);
                    sendBroadcast(intent);
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
    public void cancelGZListener(GroupMemberBean blackMemberBean) {
        tempActionBean=blackMemberBean;
        showComfirmDialog();
    }

    private void showComfirmDialog(){
        TwoButtonDialog dialog = new TwoButtonDialog(this,"确定取消关注他吗？" , "确定", "取消", R.style.dineDialog,
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
        targGroup=groupBean;
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
    public void finish() {
        TransferDeleMemberBean bean=new TransferDeleMemberBean();
        bean.setDeleteMembers(deleteMembers);
        Intent intent = new Intent();
        intent.putExtra(Constants.COMEBACK, bean);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        GroupMemberBean item=adapter.getItem(position);
//        Intent intent = new Intent(this, PersonalDetailActivity.class);
//        intent.putExtra("userNick", item.getUsernick() == null ? "" : item.getUsernick());
//        intent.putExtra("userId", item.getUserId());
//        startActivity(intent);
//    }
}
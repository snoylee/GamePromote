package com.xygame.second.sg.personal.guanzhu.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.GroupMemberListListener;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTransfer;
import com.xygame.second.sg.personal.guanzhu.group_send.MemberBean;
import com.xygame.second.sg.personal.guanzhu.group_send.MemberListActivity;
import com.xygame.second.sg.personal.guanzhu.group_send.TransferMemberBean;
import com.xygame.second.sg.personal.guanzhu.member.sort.CharacterParser;
import com.xygame.second.sg.personal.guanzhu.member.sort.PinyinComparator;
import com.xygame.second.sg.personal.guanzhu.member.sort.SideBar;
import com.xygame.second.sg.personal.guanzhu.search.GZ_MemberSearchListActivity;
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
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 2016/10/8.
 */
public class GZ_MemberGroupListActivity extends SGBaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener, GroupMemberListListener{
    private TextView titleName,rightButtonText;
    private View backButton,searchView,rightButton;
    private SlideAdapter adapter;
    private ListView mListView;
    private GroupMemberBean tempActionBean;
    private GZ_GroupBeanTransfer bean;
    private GZ_GroupBeanTemp currGroupBean;
    private List<GZ_GroupBeanTemp> allDatas;
    private SideBar sideBar;
    private TextView dialog;
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
        setContentView(R.layout.group_member_list_layout);
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
    }

    private void initDatas() {
        String experAuth = UserPreferencesUtil.getExpertAuth(this);
        String cardStatus = UserPreferencesUtil.getUserCardAuth(this);
        if ("2".equals(cardStatus)||"2".equals(experAuth)){
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
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText(currGroupBean.getName());

        adapter = new SlideAdapter(this, null);
        adapter.addCancelBlackListListener(this);
        mListView.setAdapter(adapter);
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.searchView){
            List<GroupMemberBean> datas=adapter.getDatas();
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
            Intent intent=new Intent(this, GZ_MemberSearchListActivity.class);
            intent.putExtra("bean",bean);
            intent.putExtra("beanTransfer",item);
            startActivityForResult(intent,0);
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
            case ConstTaskTag.QUERY_GZ_GROUP_MEMBERS:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_ATTEN:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
                    Intent intent=new Intent("com.xygame.group.dynamic.count.action");
                    intent.putExtra("sourceGroupId",currGroupBean.getId());
                    intent.putExtra("addFlag",false);
                    sendBroadcast(intent);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_DIVID_GROUP:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
                    Intent intent=new Intent("com.xygame.group.dynamic.count.action");
                    intent.putExtra("targGroupId",targGroup.getId());
                    intent.putExtra("sourceGroupId",currGroupBean.getId());
                    intent.putExtra("addFlag",true);
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
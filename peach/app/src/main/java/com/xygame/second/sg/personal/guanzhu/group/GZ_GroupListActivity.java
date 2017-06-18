package com.xygame.second.sg.personal.guanzhu.group;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.DeleteGZ_GroupListener;
import com.xygame.second.sg.jinpai.activity.JinPaiPlushActivity;
import com.xygame.second.sg.personal.guanzhu.group.sort.CharacterParser;
import com.xygame.second.sg.personal.guanzhu.group.sort.PinyinComparator;
import com.xygame.second.sg.personal.guanzhu.group.sort.SideBar;
import com.xygame.second.sg.personal.guanzhu.member.GZ_MemberListActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.ButtonTwoTextListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonTextDialog;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
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
public class GZ_GroupListActivity extends SGBaseActivity implements View.OnClickListener, SlideView.OnSlideListener, DeleteGZ_GroupListener, SideBar.OnTouchingLetterChangedListener{
    private TextView titleName, personalCount;
    private View backButton, rightButton;
    private SlideView mLastSlideViewWithStatusOn;
    private SlideAdapter adapter;
    private ListViewCompat mListView;
    private GZ_GroupBean tempActionBean;
    private TwoButtonTextDialog dialogCreate;
    private String groupName,constName="未分组";
    private String groupCount="0";
    private ImageView rightbuttonIcon;
    private SideBar sideBar;
    private TextView dialog;
    private PopupWindow mPopWindow;
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
        setContentView(R.layout.gz_list_layout);
        registerBoradcastReceiver();
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        rightButton = findViewById(R.id.rightButton);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
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
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.add_icon);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText("关注");
        personalCount.setText(groupCount.concat("人"));

        adapter = new SlideAdapter(this, null);
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
            String experAuth = UserPreferencesUtil.getExpertAuth(this);
            String cardStatus = UserPreferencesUtil.getUserCardAuth(this);
            if ("2".equals(cardStatus)||"2".equals(experAuth)){
                showPOP();
            }else{
                showCreateDialog();
            }
        }
    }

    private void showPOP() {
        mPopWindow = initPopWindow(this,
                R.layout.pop_group_layout, true);
        View root = mPopWindow.getContentView();
        View newGroup = root.findViewById(R.id.newGroup);
        View groupChat = root.findViewById(R.id.groupChat);

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
            }
        });

        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                showCreateDialog();
            }
        });

        groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                Intent intent = new Intent(GZ_GroupListActivity.this, GZ_GroupListForGroupChatActivity.class);
                startActivity(intent);
            }
        });

        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(findViewById(R.id.optionsView));
        }
        mPopWindow.setOutsideTouchable(true);
    }

    private void showCreateDialog() {
        dialogCreate = new TwoButtonTextDialog(this, "添加分组", "请为新分组输入名称", R.style.dineDialog,
                new ButtonTwoTextListener() {

                    @Override
                    public void confrimListener(String content) {
                        addGroupAction(content);
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialogCreate.show();
    }

    private void addGroupAction(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (constName.equals(content)) {
                Toast.makeText(GZ_GroupListActivity.this, "组名不能重复", Toast.LENGTH_SHORT).show();
            } else {
                boolean temFlag = false;
                List<GZ_GroupBean> tempDatas = adapter.getDatas();
                for (GZ_GroupBean item : tempDatas) {
                    if (item.getName().equals(content)) {
                        temFlag = true;
                        break;
                    }
                }
                if (!temFlag) {
                    dialogCreate.dismiss();
                    groupName = content;
                    commitAction();
                } else {
                    Toast.makeText(GZ_GroupListActivity.this, "组名不能重复", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(GZ_GroupListActivity.this, "新分组名称不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void addGroup(String groupID) {
        List<GZ_GroupBean> datas = adapter.getDatas();
        GZ_GroupBean item = new GZ_GroupBean();
        item.setId(groupID);
        item.setName(groupName);
        item.setCount("0");
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
        Collections.sort(datas, pinyinComparator);
//        CacheService.getInstance().cacheGroupDatasDatas(UserPreferencesUtil.getUserId(this), datas);
        adapter.addDatas(datas);
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

    private void commitAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("groupName", groupName);
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_GZ_GROUP_CREATE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GZ_GROUP_CREATE);
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
            case ConstTaskTag.QUERY_GZ_GROUP:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GZ_GROUP_CREATE:
                if ("0000".equals(data.getCode())) {
                    addGroup(data.getRecord());
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
            alldatas.add(it1);
        }
        alldatas.add(0,it);
        transfer.setAlldatas(alldatas);
        Intent intent = new Intent(this, GZ_MemberListActivity.class);
        intent.putExtra("bean", transfer);
        startActivity(intent);
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.xygame.group.dynamic.count.action");
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
            }
        }
    };


    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    private PopupWindow initPopWindow(Context context, int popWinLayout,
                                      boolean isDismissMenuOutsideTouch) {

        PopupWindow mPopWin = new PopupWindow(
                ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(popWinLayout, null),
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        if (isDismissMenuOutsideTouch)
            mPopWin.setBackgroundDrawable(new BitmapDrawable());
        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
        if (mPopWin.isShowing()) {
            mPopWin.dismiss();
            mPopWin = null;
        }
        return mPopWin;
    }
}
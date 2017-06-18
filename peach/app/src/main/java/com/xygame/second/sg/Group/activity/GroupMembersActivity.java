package com.xygame.second.sg.Group.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.adapter.GroupMemberAdapter;
import com.xygame.second.sg.Group.bean.GoupNoticeBean;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.personal.blacklist.ListViewCompat;
import com.xygame.second.sg.personal.blacklist.sort.CharacterParser;
import com.xygame.second.sg.personal.blacklist.sort.PinyinComparator;
import com.xygame.second.sg.personal.blacklist.sort.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tony on 2016/9/6.
 */
public class GroupMembersActivity extends SGBaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener {
    private TextView titleName;
    private View backButton;
    private GroupMemberAdapter adapter;
    private ListViewCompat mListView;
    private GoupNoticeBean goupNoticeBean;
    private SideBar sideBar;
    private TextView dialog;
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
        setContentView(R.layout.black_list_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        sideBar.setOnTouchingLetterChangedListener(this);
    }

    private void initDatas() {
        goupNoticeBean=(GoupNoticeBean)getIntent().getSerializableExtra("goupNoticeBean");
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText("群成员");
        mListView = (ListViewCompat) findViewById(R.id.list);
        adapter = new GroupMemberAdapter(this, null);
        mListView.setAdapter(adapter);
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("groupId", goupNoticeBean.getGroupId());
            obj.put("page", new JSONObject().put("pageIndex", 1).put("pageSize", 500));
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_GROUP_MEMBERS);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_MEMBERS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GROUP_MEMBERS:
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
            try {
                JSONObject object=new JSONObject(resposeStr);
                String members=StringUtils.getJsonValue(object,"members");
                if (ConstTaskTag.isTrueForArrayObj(members)) {
                    List<BlackMemberBean> datas = new ArrayList<>();
                    JSONArray array2 = new JSONArray(members);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        BlackMemberBean item = new BlackMemberBean();
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
                    // 根据a-z进行排序源数据
                    Collections.sort(datas, pinyinComparator);
                    adapter.addDatas(datas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
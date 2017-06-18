package com.xygame.second.sg.personal.blacklist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.CancelBlackListListener;
import com.xygame.second.sg.personal.blacklist.sort.CharacterParser;
import com.xygame.second.sg.personal.blacklist.sort.PinyinComparator;
import com.xygame.second.sg.personal.blacklist.sort.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
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
 * Created by tony on 2016/9/2.
 */
public class BlackListActivity extends SGBaseActivity implements View.OnClickListener, SlideView.OnSlideListener, CancelBlackListListener, SideBar.OnTouchingLetterChangedListener {
    private TextView titleName;
    private View backButton;
    private SlideView mLastSlideViewWithStatusOn;
    private SlideAdapter adapter;
    private ListViewCompat mListView;
    private BlackMemberBean tempActionBean;

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
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        titleName.setText("黑名单");
        mListView = (ListViewCompat) findViewById(R.id.list);
        adapter = new SlideAdapter(this, null);
        adapter.addCancelBlackListListener(this);
        adapter.addSlidViewListener(this);
        mListView.setAdapter(adapter);
        List<BlackMemberBean> blackListDatasDatas= CacheService.getInstance().getCacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this));
        if (blackListDatasDatas==null){
            loadDatas();
        }else{
            adapter.addDatas(blackListDatasDatas);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
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
    public void cancelBlackListListener(BlackMemberBean blackMemberBean) {
        this.tempActionBean = blackMemberBean;
        removeAction();
    }

    private void removeAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("freezeUserId", tempActionBean.getUserId());
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_BLACK_LIST_REMOVE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_BLACK_LIST_REMOVE);
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", 1).put("pageSize", 500));
            ShowMsgDialog.showNoMsg(this, true);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_BLACK_LIST);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_BLACK_LIST);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_BLACK_LIST:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_BLACK_LIST_REMOVE:
                if ("0000".equals(data.getCode())) {
                    adapter.updateDatas(tempActionBean);
                    removeFromBlackList();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void removeFromBlackList() {
        List<BlackMemberBean> blackListDatasDatas= CacheService.getInstance().getCacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this));
        for (int i=0;i<blackListDatasDatas.size();i++){
            if (tempActionBean.getUserId().equals(blackListDatasDatas.get(i).getUserId())){
                blackListDatasDatas.remove(i);
                break;
            }
        }
        CacheService.getInstance().cacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this),blackListDatasDatas);
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<BlackMemberBean> datas = new ArrayList<>();
            try {
                if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
                    JSONArray array2 = new JSONArray(resposeStr);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        BlackMemberBean item = new BlackMemberBean();
                        item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        item.setAge(StringUtils.getJsonValue(object1, "age"));
                        item.setGender(StringUtils.getJsonValue(object1, "gender"));
//                        //汉字转换成拼音
//                        String pinyin = characterParser.getSelling(item.getUsernick());
//                        String sortString = pinyin.substring(0, 1).toUpperCase();
//
//                        // 正则表达式，判断首字母是否是英文字母
//                        if(sortString.matches("[A-Z]")){
//                            item.setSortLetters(sortString.toUpperCase());
//                        }else{
//                            item.setSortLetters("#");
//                        }
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 根据a-z进行排序源数据
//            Collections.sort(datas, pinyinComparator);
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
}

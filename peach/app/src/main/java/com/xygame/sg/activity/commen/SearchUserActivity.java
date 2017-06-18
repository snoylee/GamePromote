package com.xygame.sg.activity.commen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.cm.bean.SearchReqBean;
import com.xygame.sg.activity.commen.adapter.SearchUserLvAdapter;
import com.xygame.sg.activity.commen.bean.SearchUserView;
import com.xygame.sg.activity.commen.bean.SearchUserVo;
import com.xygame.sg.activity.notice.adapter.SearchHisAdapter;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.ClearEditText;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.data.net.http.JsonUtil;

public class SearchUserActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 ,AdapterView.OnItemClickListener{
    private ImageView back_iv;
    private ClearEditText search_content_et;
    private TextView search_tv;


    private PullToRefreshListView2 listView;
    private ListView search_history_lv;
    private TextView clear_history_tv;
    private LinearLayout history_ll;

    private List<String> searchHisStrList = new ArrayList<String>();

    private SearchUserLvAdapter lvAdapter;
    private int pageSize = 15;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private Long reqTime;

    private SearchReqBean searchReqBean;
    private SearchUserView users = new SearchUserView();

    private SharedPreferences preferences;
    private static final String CACHE_SPF_NAME = "search_user_his_spf";
    private static final String CACHE_KEY_NAME = "search_user_his_key";

    private boolean isShowLoading = true;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;
    private boolean fansIsLoading = true;

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        preferences = getSharedPreferences(CACHE_SPF_NAME, 0);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        back_iv = (ImageView) findViewById(R.id.back_iv);
        search_content_et = (ClearEditText) findViewById(R.id.search_content_et);
        search_tv = (TextView) findViewById(R.id.search_tv);
        search_history_lv = (ListView) findViewById(R.id.search_history_lv);
        history_ll = (LinearLayout) findViewById(R.id.history_ll);
        listView = (PullToRefreshListView2) findViewById(R.id.result_lv);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        clear_history_tv = (TextView) findViewById(R.id.clear_history_tv);

        empty_root_ll = (LinearLayout) findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) findViewById(R.id.primary_tip_tv);
        sub_tip_tv = (TextView) findViewById(R.id.sub_tip_tv);
    }

    private void initDatas() {
        String searchHisStrs = preferences.getString(CACHE_KEY_NAME,"");
        load(searchHisStrs);
        searchReqBean = new SearchReqBean();
        searchReqBean.getPage().setPageSize(pageSize);
        searchReqBean.getPage().setPageIndex(mCurrentPage);
        lvAdapter = new SearchUserLvAdapter(this, users);
        listView.setAdapter(lvAdapter);

    }

    private void addListener() {
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        back_iv.setOnClickListener(this);
        search_tv.setOnClickListener(this);
        search_content_et.setOnKeyListener(new KeyEventListener());
        search_content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    search_tv.setEnabled(true);
                } else {
                    search_tv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        clear_history_tv.setOnClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (users.getUsers().size() > 0) {
            SearchUserVo userBean = users.getUsers().get(i - 1);
            Intent intent = new Intent(SearchUserActivity.this, PersonalDetailActivity.class);
            intent.putExtra("userId", userBean.getUserId()+"");
            intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_tv:
                search();
                break;
            case R.id.clear_history_tv:
                preferences.edit().clear().apply();
                searchHisStrList.clear();
                load(searchHisStrList.toString());
                break;
        }
    }

    private void search() {
        String searchStr = search_content_et.getText().toString();
        searchHisStrList.remove(searchStr);
        searchHisStrList.add(searchStr);
        String str = searchHisStrList.toString();
        preferences.edit().putString(CACHE_KEY_NAME,str ).apply();

        history_ll.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        searchReqBean.setContent(searchStr);
        mCurrentPage = 1;
        fansIsLoading = true;
        searchReqBean.setReqTime(null);
        searchReqBean.getPage().setPageIndex(mCurrentPage);
        requestData();
    }

    private void load(String result) {
        if (result != null && !result.equals("")) {
            searchHisStrList = (List) new JsonUtil().from(result);
            if (searchHisStrList != null && searchHisStrList.size() > 0) {
                List hisTemp = new ArrayList();
                if (searchHisStrList.size() > 5) {
                    hisTemp.addAll(searchHisStrList.subList(searchHisStrList.size() - 5, searchHisStrList.size()));
                    searchHisStrList.clear();
                    searchHisStrList.addAll(hisTemp);
                }

                List toReverseList = new ArrayList();
                toReverseList.addAll(searchHisStrList);
                Collections.reverse(toReverseList);
                SearchHisAdapter adapter = new SearchHisAdapter(this,toReverseList);
                search_history_lv.setAdapter(adapter);
                search_history_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String searchStr = ((TextView) view.findViewById(R.id.history_text)).getText().toString();
                        search_content_et.setText(searchStr);
                        search();
                    }
                });
                history_ll.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                history_ll.setVisibility(View.GONE);
            }
        } else {
            history_ll.setVisibility(View.GONE);
        }
    }

    private void requestData() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", mCurrentPage).put("pageSize", pageSize));
            obj.put("content", searchReqBean.getContent());
            if (mCurrentPage > 1) {
                obj.put("reqTime", reqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.SEARCH_MODEL_PHOTOPGS_URL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.SEARCH_MODEL_PHOTOPGS_INT);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        if ("0000".equals(data.getCode())) {
            if (listView.isRefreshing()){
                listView.onRefreshComplete();
            }
            parseJson(data);
        } else {
            Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
            if (listView.isRefreshing()){
                listView.onRefreshComplete();
            }
        }

    }

    private void parseJson(ResponseBean data) {
        SearchUserView resUsers=new SearchUserView();
        if (!TextUtils.isEmpty(data.getRecord()) && !"null".equals(data.getRecord())) {
            List<SearchUserVo> datas = new ArrayList<>();
            try {
                JSONObject object1 = new JSONObject(data.getRecord());
                String actions = StringUtils.getJsonValue(object1, "users");
                reqTime = Long.parseLong(StringUtils.getJsonValue(object1, "reqTime"));
                resUsers.setReqTime(reqTime);
                if (!TextUtils.isEmpty(actions) && !"[]".equals(actions) && !"[null]".equals(actions) && !"null".equals(actions)) {
                    JSONArray jsonArray = new JSONArray(actions);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object2 = jsonArray.getJSONObject(i);
                        SearchUserVo item = new SearchUserVo();
                        item.setAge(StringUtils.getJsonIntValue(object2, "age"));
                        item.setCity(StringUtils.getJsonIntValue(object2, "city"));
                        item.setGender(StringUtils.getJsonIntValue(object2, "gender"));
                        item.setProvince(StringUtils.getJsonIntValue(object2, "province"));
                        item.setUserIcon(StringUtils.getJsonValue(object2, "userIcon"));
                        item.setUserId(StringUtils.getJsonLongValue(object2, "userId"));
                        item.setUsernick(StringUtils.getJsonValue(object2, "usernick"));
                        datas.add(item);
                    }
                }
                resUsers.setUsers(datas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (resUsers != null && resUsers.getUsers().size()>0) {
            if (resUsers.getUsers().size()<pageSize){
                fansIsLoading = false;
            }
            listView.setVisibility(View.VISIBLE);
            reqTime = resUsers.getReqTime();
            if (mCurrentPage > 1) {
                users.getUsers().addAll(resUsers.getUsers());
            } else {
                users.getUsers().clear();
                users.getUsers().addAll(resUsers.getUsers());
            }
            lvAdapter.notifyDataSetChanged();
            empty_root_ll.setVisibility(View.GONE);
            primary_tip_tv.setText("");
            sub_tip_tv.setText("");
        }else{
            fansIsLoading = false;
            if (mCurrentPage == 1){
                lvAdapter = new SearchUserLvAdapter(this, resUsers);
                listView.setAdapter(lvAdapter);
                listView.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
                primary_tip_tv.setText("没有找到合适的内容!");
                sub_tip_tv.setText("请更改搜索关键字");
            }
        }

        if (listView.isRefreshing()){
            listView.onRefreshComplete();
        }
    }





    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        fansIsLoading = true;
        mCurrentPage = 1;
        searchReqBean.setReqTime(null);
        searchReqBean.getPage().setPageIndex(mCurrentPage);
        requestData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (fansIsLoading){
            mCurrentPage += 1;
            searchReqBean.setReqTime(reqTime + "");
            searchReqBean.getPage().setPageIndex(mCurrentPage);
            requestData();
        }else{
            falseDatasModel();
        }
    }

    private void falseDatasModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    listView.onRefreshComplete();
                    Toast.makeText(SearchUserActivity.this,"已全部加载",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


    public PullToRefreshListView2 getListView() {
        return listView;
    }



    public boolean isShowLoading() {
        return isShowLoading;
    }
}

package com.xygame.sg.activity.model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.model.adapter.ModelAllLvAdapter;
import com.xygame.sg.activity.model.bean.AllModelItemBean;
import com.xygame.sg.activity.model.bean.AllModelReqBean;
import com.xygame.sg.activity.notice.adapter.NoticeAdapter;
import com.xygame.sg.activity.notice.adapter.SearchHisAdapter;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.task.model.LoadAllModelInfo;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.ClearEditText;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.data.net.http.JsonUtil;
import base.frame.VisitUnit;

public class SearchModelActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 ,AdapterView.OnItemClickListener{
    private ImageView back_iv;
    private ClearEditText search_content_et;
    private TextView search_tv;


    private PullToRefreshListView2 listView;
    private ListView search_history_lv;
    private TextView clear_history_tv;
    private LinearLayout history_ll;
    VisitUnit visitUnit = new VisitUnit(this);

    private List<String> searchHisStrList = new ArrayList<String>();

    private ModelAllLvAdapter lvAdapter;
    private int pageSize = 15;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private PageBean page = new PageBean();
    private AllModelReqBean allModelReqBean;
    private List<AllModelItemBean> models;
    private SharedPreferences preferences;
    private static final String CACHE_SPF_NAME = "search_model_his_spf";
    private static final String CACHE_KEY_NAME = "search_model_his_key";

    private List<Map> shootTypetList = new ArrayList<Map>();//拍摄类型数据
    private boolean isShowLoading = true;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_search_model, null));
        shootTypetList = (List<Map>) (getIntent().getSerializableExtra("shootTypetList"));
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
        allModelReqBean = new AllModelReqBean();
        page.setPageIndex(mCurrentPage);
        page.setPageSize(pageSize);
        allModelReqBean.setPage(page);
        models = new ArrayList<AllModelItemBean>();
        lvAdapter = new ModelAllLvAdapter(this, models);
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
        if (models.size() > 0) {
            AllModelItemBean modelBean = null;
            modelBean = models.get(i-1);
            Intent intent = new Intent(SearchModelActivity.this, PersonalDetailActivity.class);
            intent.putExtra("userNick", modelBean.getUsernick());
            intent.putExtra("userId", modelBean.getUserId());
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
                mCurrentPage = 1;
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
        if (!StringUtils.isEmpty(search_content_et.getText().toString())){
            allModelReqBean.setUsernick(search_content_et.getText().toString());
        }
        new Action(LoadAllModelInfo.class, "${loadAllModelInfo}", this, null, visitUnit).run();
    }



    public void responseModelsList(List<AllModelItemBean> resultModelList) {
        if (resultModelList != null && resultModelList.size()>0) {
            listView.setVisibility(View.VISIBLE);
            if (mCurrentPage > 1) {
                models.addAll(resultModelList);
            } else {
                models.clear();
                models.addAll(resultModelList);
            }
            lvAdapter.notifyDataSetChanged();
            empty_root_ll.setVisibility(View.GONE);
            primary_tip_tv.setText("");
            sub_tip_tv.setText("");
        }else{
            if (mCurrentPage == 1){
                models = new ArrayList<AllModelItemBean>();
                lvAdapter = new ModelAllLvAdapter(this, models);
                listView.setAdapter(lvAdapter);
                listView.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
                primary_tip_tv.setText("没有找到合适的模特!");
                sub_tip_tv.setText("请更改搜索关键字");
            }
        }
        listView.onRefreshComplete();
    }
    

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mCurrentPage = 1;
        page.setPageIndex(mCurrentPage);
        requestData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mCurrentPage += 1;
        page.setPageIndex(mCurrentPage);
        requestData();
    }



    public PullToRefreshListView2 getListView() {
        return listView;
    }

    public AllModelReqBean getAllModelReqBean() {
        return allModelReqBean;
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }
}

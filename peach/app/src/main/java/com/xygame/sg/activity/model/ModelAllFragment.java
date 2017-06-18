package com.xygame.sg.activity.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.main.NewModelFragment;
import com.xygame.sg.activity.model.adapter.ModelAllGvAdapter;
import com.xygame.sg.activity.model.adapter.ModelAllLvAdapter;
import com.xygame.sg.activity.model.bean.AllModelBean;
import com.xygame.sg.activity.model.bean.AllModelItemBean;
import com.xygame.sg.activity.model.bean.AllModelReqBean;
import com.xygame.sg.activity.notice.ShootTypePopWindow;
import com.xygame.sg.activity.notice.SortPopWindow;
import com.xygame.sg.activity.notice.adapter.ShootTypeAdapter;
import com.xygame.sg.activity.notice.adapter.SortListAdapter;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshGridView;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.frame.VisitUnit;


public class ModelAllFragment extends SGBaseActivity implements PullToRefreshBase.OnRefreshListener2 ,AdapterView.OnItemClickListener, View.OnClickListener, ShootTypePopWindow.IOnShootTypeItemSelectListener, SortPopWindow.IOnSortItemSelectListener{
    private LinearLayout condition_ll;
    private LinearLayout shoottype_ll;
    private TextView shoottype_tv;
    private ImageView shoottype_iv;
    private LinearLayout sort_ll;
    private TextView sort_tv;
    private ImageView sort_iv;
    private LinearLayout filter_ll;
    private TextView filter_tv;
    private RelativeLayout lv_rl;
    private PullToRefreshListView2 all_model_lv;
    private RelativeLayout gv_rl;
    private PullToRefreshGridView all_model_gv;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;

    private AllModelReqBean allModelReqBean;
    private List<AllModelItemBean> models;

    private int pageSize = 21;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private PageBean page = new PageBean();
    private VisitUnit unit = new VisitUnit();
    private int showType = NewModelFragment.SHOW_AS_GRIDVIEW;
    private ModelAllLvAdapter lvAdapter;
    private ModelAllGvAdapter gvAdapter;


    private ShootTypePopWindow shootTypePopWindow;
    private ShootTypeAdapter shootTypeAdapter;
    private int lastSelectShootPos = 0;//全部

    private SortPopWindow sortPopWindow;
    private SortListAdapter sortListAdapter;
    private List<Map> sortList;
    private int lastSelectSortPos = 0;

    private boolean isShowLoading = false;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private boolean isBottom = false;
    private View search_iv,switch_show_cb;
    private String reqTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_model_all);
        isPrepared = true;
        initViews();
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isPrepared || mHasLoadedOnce ) {
            return;
        }
        mHasLoadedOnce = true;
        initDatas();
        addListener();
        setIsShowLoading(true);
        queryData();
    }

    private void initViews() {
        switch_show_cb = findViewById(R.id.switch_show_cb);
        search_iv =findViewById(R.id.search_iv);
        condition_ll = (LinearLayout) findViewById(R.id.condition_ll);
        shoottype_ll = (LinearLayout) findViewById(R.id.shoottype_ll);
        shoottype_tv = (TextView) findViewById(R.id.shoottype_tv);
        shoottype_iv = (ImageView) findViewById(R.id.shoottype_iv);
        sort_ll = (LinearLayout) findViewById(R.id.sort_ll);
        sort_tv = (TextView) findViewById(R.id.sort_tv);
        sort_iv = (ImageView) findViewById(R.id.sort_iv);
        filter_ll = (LinearLayout) findViewById(R.id.filter_ll);
        filter_tv = (TextView) findViewById(R.id.filter_tv);

        lv_rl = (RelativeLayout)findViewById(R.id.lv_rl);
        all_model_lv = (PullToRefreshListView2)findViewById(R.id.all_model_lv);
        gv_rl = (RelativeLayout) findViewById(R.id.gv_rl);
        all_model_gv = (PullToRefreshGridView) findViewById(R.id.all_model_gv);
        if (showType == NewModelFragment.SHOW_AS_GRIDVIEW){
            gv_rl.setVisibility(View.VISIBLE);
            lv_rl.setVisibility(View.GONE);
        } else if (showType == NewModelFragment.SHOW_AS_LISTVIEW){
            gv_rl.setVisibility(View.GONE);
            lv_rl.setVisibility(View.VISIBLE);
        }

        empty_root_ll = (LinearLayout)findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView)findViewById(R.id.primary_tip_tv);

    }

    private void initDatas() {
        allModelReqBean = new AllModelReqBean();
        page.setPageIndex(mCurrentPage);
        page.setPageSize(pageSize);
        allModelReqBean.setPage(page);
        models = new ArrayList<AllModelItemBean>();

        lvAdapter = new ModelAllLvAdapter(this, models);
        all_model_lv.setAdapter1(lvAdapter);

        gvAdapter = new ModelAllGvAdapter(this, models);
        all_model_gv.setAdapter1(gvAdapter);

        shootTypePopWindow = new ShootTypePopWindow(this);
        //初始化排序
        sortList = new ArrayList<Map>();
        String[] sortTypeIdArr = {1 + "", 2 + ""};
        String[] sortTypeNameArr = {"默认排序",  "接单量最多"};
        for (int i = 0; i < sortTypeIdArr.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("sortTypeId", sortTypeIdArr[i]);
            map.put("sortTypeName", sortTypeNameArr[i]);
            sortList.add(map);
        }
        sortPopWindow = new SortPopWindow(this);
        sortListAdapter = new SortListAdapter(this, sortList);
        sortPopWindow.setAdatper(sortListAdapter);


    }

    private void initShootTypePop() {
        shootTypeAdapter = new ShootTypeAdapter(this);
        shootTypePopWindow.setAdatper(shootTypeAdapter);
        shootTypePopWindow.setItemSelectListener(this);
        shootTypePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                shoottype_iv.setImageResource(R.drawable.down_gray);
            }
        });
    }


    private void addListener() {
        switch_show_cb.setOnClickListener(this);
        search_iv.setOnClickListener(this);
        all_model_lv.setOnRefreshListener(this);
        all_model_lv.setMode(PullToRefreshBase.Mode.BOTH);
        all_model_lv.setOnItemClickListener(this);
        all_model_gv.setOnRefreshListener(this);
        all_model_gv.setMode(PullToRefreshBase.Mode.BOTH);
        all_model_gv.setOnItemClickListener(this);

        shoottype_ll.setOnClickListener(this);
        sort_ll.setOnClickListener(this);
        filter_ll.setOnClickListener(this);
        sortPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                sort_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                sort_iv.setImageResource(R.drawable.down_gray);
            }
        });
        sortPopWindow.setItemSelectListener(this);

        empty_root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
    }

    private void queryData() {
        if (SGApplication.getInstance().getTypeList()!=null){
            initShootTypePop();
            requestData();
        } else {
            requestShootType();
        }
    }
    private void requestShootType() {

        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("containSub",0);
            obj.put("ver",2);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUERY_MODEL_SHOOT_TYPE_URL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_SHOOT_TYPE_INT);
    }
    private void requestData() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex",allModelReqBean.getPage().getPageIndex()).put("pageSize", allModelReqBean.getPage().getPageSize()));
            if (allModelReqBean.getPriceType()==null||0==allModelReqBean.getPriceType()){
            }else{
                obj.put("shootType", allModelReqBean.getPriceType());
            }
            obj.put("orderType", allModelReqBean.getNtelligentSort());

            if (allModelReqBean.getPage().getPageIndex()>1){
                obj.put("reqTime", reqTime);
            }

            JSONObject condObj=new JSONObject();
            boolean isUse=false;
            if (allModelReqBean.getAgeBegin() != null||allModelReqBean.getAgeEnd() != null) {
                isUse=true;
                if (allModelReqBean.getAgeBegin() != null&&allModelReqBean.getAgeEnd() != null){
                    condObj.put("age",new JSONObject().put("min",allModelReqBean.getAgeBegin()).put("max",allModelReqBean.getAgeEnd()));
                }
                if (allModelReqBean.getAgeBegin() == null&&allModelReqBean.getAgeEnd() != null){
                    condObj.put("age",new JSONObject().put("max", allModelReqBean.getAgeEnd()));
                }
                if (allModelReqBean.getAgeBegin() != null&&allModelReqBean.getAgeEnd() == null){
                    condObj.put("age",new JSONObject().put("min", allModelReqBean.getAgeBegin()));
                }
            }

            if (allModelReqBean.getGender() != null||!StringUtils.isEmpty(allModelReqBean.getCountry())||allModelReqBean.getOccupType() != null||allModelReqBean.getCity() != null){
                JSONObject baseObj=new JSONObject();
                if (allModelReqBean.getGender() != null){
                    baseObj.put("gender", allModelReqBean.getGender());
                }
                if (!StringUtils.isEmpty(allModelReqBean.getCountry())){
                    baseObj.put("country", allModelReqBean.getCountry());
                }
                if (allModelReqBean.getCity() != null){
                    baseObj.put("city", allModelReqBean.getCity());
                }
                if (allModelReqBean.getOccupType() != null){
                    baseObj.put("occupType", allModelReqBean.getOccupType());
                }
                isUse=true;
                condObj.put("base",baseObj);
            }

            if (allModelReqBean.getHeightBegin() != null||allModelReqBean.getHeightEnd() != null||allModelReqBean.getWeightBegin() != null||allModelReqBean.getWeightEnd() != null) {
                JSONObject bodyObj=new JSONObject();
                if (allModelReqBean.getHeightBegin() != null&&allModelReqBean.getHeightEnd() != null){
                    bodyObj.put("height", new JSONObject().put("min",allModelReqBean.getHeightBegin()).put("max", allModelReqBean.getHeightEnd()));
                }
                if (allModelReqBean.getHeightBegin() == null&&allModelReqBean.getHeightEnd() != null){
                    bodyObj.put("height", new JSONObject().put("max", allModelReqBean.getHeightEnd()));
                }
                if (allModelReqBean.getHeightBegin() != null&&allModelReqBean.getHeightEnd() == null){
                    bodyObj.put("height", new JSONObject().put("min", allModelReqBean.getHeightBegin()));
                }

                if (allModelReqBean.getWeightBegin() != null&&allModelReqBean.getWeightEnd() != null){
                    bodyObj.put("weight", new JSONObject().put("min",allModelReqBean.getWeightBegin()).put("max", allModelReqBean.getWeightEnd()));
                }
                if (allModelReqBean.getWeightBegin() == null&&allModelReqBean.getWeightEnd() != null){
                    bodyObj.put("weight", new JSONObject().put("max", allModelReqBean.getWeightEnd()));
                }
                if (allModelReqBean.getWeightBegin() != null&&allModelReqBean.getWeightEnd() == null){
                    bodyObj.put("weight", new JSONObject().put("min", allModelReqBean.getWeightBegin()));
                }
                isUse=true;
                condObj.put("body",bodyObj);
            }

            if (isUse){
                obj.put("cond", condObj);
            }

            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.LOAD_ALL_MODEL_INFO_URL);
        if (isShowLoading()) {
            ShowMsgDialog.showNoMsg(this, true);
        }
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.LOAD_ALL_MODEL_INFO_INT);
    }
    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);


        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_MODEL_SHOOT_TYPE_INT:
                responseShootType(data);
                break;
            case ConstTaskTag.LOAD_ALL_MODEL_INFO_INT:
                if ("0000".equals(data.getCode())) {
                    responseModelsList(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    if (all_model_lv.isRefreshing()){
                        all_model_lv.onRefreshComplete();
                    }else if (all_model_gv.isRefreshing()){
                        all_model_gv.onRefreshComplete();
                    }
                }
                break;
        }

    }

    public void responseShootType(ResponseBean data) {
        if (ConstTaskTag.isTrueForArrayObj(data.getRecord())){
            try {
                List<ShootTypeBean> resultList=new ArrayList<>();
                JSONArray array =new JSONArray(data.getRecord());
                for (int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    ShootTypeBean item=new ShootTypeBean();
//                    item.setIconDefaultUrl();
                }
                SGApplication.getInstance().setTypeList(resultList);
                if(resultList!=null&&resultList.size()>0){
                    initShootTypePop();
                    requestData();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void switchShowView(int setShowType) {
        showType= setShowType;
        if (setShowType == NewModelFragment.SHOW_AS_GRIDVIEW){
            gv_rl.setVisibility(View.VISIBLE);
            lv_rl.setVisibility(View.GONE);
            gvAdapter.notifyDataSetChanged();
            if (gvAdapter.getCount() == 0){
                gv_rl.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
            }
        } else if (setShowType == NewModelFragment.SHOW_AS_LISTVIEW){
            gv_rl.setVisibility(View.GONE);
            lv_rl.setVisibility(View.VISIBLE);
            lvAdapter.notifyDataSetChanged();
            if (lvAdapter.getCount() == 0){
                lv_rl.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
            }
        }
    }



    public void responseModelsList(ResponseBean data) {
//        AllModelBean resAllModelBean = JSON.parseObject(data.getRecord(), AllModelBean.class);
//        List<AllModelItemBean> resultModelList = resAllModelBean.getModels();
//        if (resultModelList != null) {
//            reqTime=String.valueOf(resAllModelBean.getReqTime());
//            isBottom = resultModelList.size() < pageSize;
//            if (mCurrentPage > 1) {
//                models.addAll(resultModelList);
//            } else {
//                models.clear();
//                models.addAll(resultModelList);
//            }
//            if (showType == NewModelFragment.SHOW_AS_GRIDVIEW){
//                models.removeAll(resultModelList);
//                models.addAll(resultModelList);
//                gvAdapter.notifyDataSetChanged();
//                all_model_gv.getRefreshableView().smoothScrollToPosition(models.size()-resultModelList.size());
//            } else if (showType == NewModelFragment.SHOW_AS_LISTVIEW){
//                lvAdapter.notifyDataSetChanged();
//                all_model_lv.getRefreshableView().smoothScrollToPosition(models.size()-resultModelList.size());
//            }
//            if(resultModelList.size() == 0 && mCurrentPage==1){
//                if (showType == NewModelFragment.SHOW_AS_GRIDVIEW){
//                    gv_rl.setVisibility(View.GONE);
//                } else if (showType == NewModelFragment.SHOW_AS_LISTVIEW){
//                    lv_rl.setVisibility(View.GONE);
//                }
//                empty_root_ll.setVisibility(View.VISIBLE);
//                primary_tip_tv.setText("没有找到模特!");
//            }
//        }else{
//            Toast.makeText(this,"抱歉，没有搜索到你想要的结果",Toast.LENGTH_SHORT).show();
//        }
//
//        if (all_model_lv.isRefreshing()){
//            all_model_lv.onRefreshComplete();
//        }else if (all_model_gv.isRefreshing()){
//            all_model_gv.onRefreshComplete();
//        }
    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        setIsShowLoading(false);
        isBottom = false;
        mCurrentPage = 1;
        page.setPageIndex(mCurrentPage);
        allModelReqBean.setPage(page);
        requestData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!isBottom){
            setIsShowLoading(false);
            mCurrentPage += 1;
            page.setPageIndex(mCurrentPage);
            //筛选完后page对象已变
            allModelReqBean.setPage(page);
            requestData();
        } else {
            if (all_model_lv.isRefreshing()){
                new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case 1:
                                all_model_lv.onRefreshComplete();
                                Toast.makeText(ModelAllFragment.this, "没有更多模特了!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }.sendEmptyMessageDelayed(1, 1000);
            }else if (all_model_gv.isRefreshing()){
                all_model_gv.onRefreshComplete();
                Toast.makeText(ModelAllFragment.this,"没有更多模特了!",Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void onRefresh() {
        setIsShowLoading(true);
        isBottom = false;
        if (showType == NewModelFragment.SHOW_AS_GRIDVIEW){
            gv_rl.setVisibility(View.VISIBLE);
            lv_rl.setVisibility(View.GONE);
        } else if (showType == NewModelFragment.SHOW_AS_LISTVIEW){
            lv_rl.setVisibility(View.VISIBLE);
            gv_rl.setVisibility(View.GONE);
        }
        empty_root_ll.setVisibility(View.GONE);
        mCurrentPage = 1;
        page.setPageIndex(mCurrentPage);
        allModelReqBean.setPage(page);
        requestData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (models.size() > 0) {
            AllModelItemBean modelBean = null;
            if (showType == NewModelFragment.SHOW_AS_GRIDVIEW){
                modelBean = models.get(i);
            } else if (showType == NewModelFragment.SHOW_AS_LISTVIEW){
                modelBean = models.get(i-1);
            }
            Intent intent = new Intent(this, PersonalDetailActivity.class);
            intent.putExtra("userNick", modelBean.getUsernick());
            intent.putExtra("userId", modelBean.getUserId());
            intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shoottype_ll:
                selectShootType();
                break;
            case R.id.sort_ll:
                sort();
                break;
            case R.id.filter_ll:
                filter();
                break;
            case R.id.search_iv:
                if (SGApplication.getInstance().getTypeList()!=null) {
                    Intent intent = new Intent(this, SearchUserActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.switch_show_cb:
                finish();
                break;
        }
    }

    private void selectShootType() {

        if (shootTypeAdapter.getmDataList().size()==0 ){
            shootTypeAdapter = new ShootTypeAdapter(this);
            shootTypePopWindow.setAdatper(shootTypeAdapter);
        }
        shoottype_tv.setTextColor(getResources().getColor(R.color.dark_green));
        shoottype_iv.setImageResource(R.drawable.down_green);
        shootTypePopWindow.showAsDropDown(condition_ll);
        shootTypeAdapter.setSelectedItem(lastSelectShootPos);
    }

    private void sort() {
        sort_tv.setTextColor(getResources().getColor(R.color.dark_green));
        sort_iv.setImageResource(R.drawable.down_green);
        sortPopWindow.showAsDropDown(condition_ll);
        sortListAdapter.setSelectedItem(lastSelectSortPos);
    }

    private void filter() {
        Intent intent = new Intent(this, FilterModelActivity.class);
        intent.putExtra("requestBean", allModelReqBean);
        startActivityForResult(intent, NewModelFragment.FILTER_MODEL_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case NewModelFragment.FILTER_MODEL_REQUEST:
                    allModelReqBean = (AllModelReqBean) data.getSerializableExtra("requestBean");
                    int selectCon=data.getIntExtra("selectCon",0);
                    toFilter(selectCon);
                    break;
            }
        }
    }

    public void toFilter(int selectCon){
        if (selectCon!= 0) {
            filter_tv.setText("筛选(" + selectCon + ")");
        } else {
            filter_tv.setText("筛选");
        }
        onRefresh();
    }


    /**
     * 当选中一项拍摄类型后的回调方法
     *
     * @param pos
     */
    @Override
    public void onShootTypeItemClick(int pos) {
        lastSelectShootPos = pos;
        if (pos == 0) {
            shoottype_tv.setText("全部");
            allModelReqBean.setPriceType(null);
        } else {
            List<ShootTypeBean>  typeList=SGApplication.getInstance().getTypeList();
            if (typeList!=null){
                String type_name = typeList.get(pos - 1).getTypeName();
                if (type_name.length() > 4) {
                    type_name = type_name.substring(0, 4);
                }
                shoottype_tv.setText(type_name);
                allModelReqBean.setPriceType(typeList.get(pos - 1).getTypeId());
            }
        }

        onRefresh();
    }


    @Override
    public void onSortItemClick(int pos) {
        lastSelectSortPos = pos;
        sort_tv.setText((String) sortList.get(pos).get("sortTypeName"));
        allModelReqBean.setNtelligentSort(Integer.parseInt((String) sortList.get(pos).get("sortTypeId")));
        onRefresh();
    }
    public AllModelReqBean getAllModelReqBean() {
        return allModelReqBean;
    }
    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setIsShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }
}

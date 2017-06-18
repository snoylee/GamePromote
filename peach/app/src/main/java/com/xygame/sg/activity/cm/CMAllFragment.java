package com.xygame.sg.activity.cm;


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

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.cm.adapter.CMAllGvAdapter;
import com.xygame.sg.activity.cm.adapter.CMAllLvAdapter;
import com.xygame.sg.activity.cm.adapter.OccupTypeAdapter;
import com.xygame.sg.activity.cm.bean.AllCMReqBean;
import com.xygame.sg.activity.cm.bean.AllPhotographerView;
import com.xygame.sg.activity.cm.bean.AllPhotographerVo;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.main.CMFragment;
import com.xygame.sg.activity.notice.SortPopWindow;
import com.xygame.sg.activity.notice.adapter.SortListAdapter;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshGridView;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CMAllFragment extends SGBaseActivity implements PullToRefreshBase.OnRefreshListener2 ,AdapterView.OnItemClickListener, View.OnClickListener, OccupTypePopWindow.IOnOccupTypeItemSelectListener, SortPopWindow.IOnSortItemSelectListener{


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
    private PullToRefreshListView2 all_cm_lv;
    private RelativeLayout gv_rl;
    private PullToRefreshGridView all_cm_gv;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;

    private AllCMReqBean allCMReqBean;
    private AllPhotographerView allPhotographers = new AllPhotographerView();

    private int pageSize = 21;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private PageBean page = new PageBean();
    private int showType = CMFragment.SHOW_AS_GRIDVIEW;

    private CMAllLvAdapter lvAdapter;
    private CMAllGvAdapter gvAdapter;
    private Long reqTime;


    private OccupTypePopWindow occupTypePopWindow;
    private OccupTypeAdapter occupTypeAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cmall);
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
        requestData();
    }

    private void initViews() {
        switch_show_cb = findViewById(R.id.switch_show_cb);
        search_iv =findViewById(R.id.search_iv);
        condition_ll = (LinearLayout) findViewById(R.id.condition_ll);
        shoottype_ll = (LinearLayout) findViewById(R.id.shoottype_ll);
        shoottype_tv = (TextView)findViewById(R.id.shoottype_tv);
        shoottype_iv = (ImageView)findViewById(R.id.shoottype_iv);
        sort_ll = (LinearLayout) findViewById(R.id.sort_ll);
        sort_tv = (TextView) findViewById(R.id.sort_tv);
        sort_iv = (ImageView) findViewById(R.id.sort_iv);
        filter_ll = (LinearLayout)findViewById(R.id.filter_ll);
        filter_tv = (TextView) findViewById(R.id.filter_tv);

        lv_rl = (RelativeLayout) findViewById(R.id.lv_rl);
        all_cm_lv = (PullToRefreshListView2)findViewById(R.id.all_cm_lv);
        gv_rl = (RelativeLayout)findViewById(R.id.gv_rl);
        all_cm_gv = (PullToRefreshGridView)findViewById(R.id.all_cm_gv);
        if (showType == CMFragment.SHOW_AS_GRIDVIEW){
            gv_rl.setVisibility(View.VISIBLE);
            lv_rl.setVisibility(View.GONE);
        } else if (showType == CMFragment.SHOW_AS_LISTVIEW){
            gv_rl.setVisibility(View.GONE);
            lv_rl.setVisibility(View.VISIBLE);
        }

        empty_root_ll = (LinearLayout) findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) findViewById(R.id.primary_tip_tv);

    }

    private void initDatas() {
        allCMReqBean = new AllCMReqBean();
        page.setPageIndex(mCurrentPage);
        page.setPageSize(pageSize);
        allCMReqBean.setPage(page);

        lvAdapter = new CMAllLvAdapter(this, allPhotographers);
        all_cm_lv.setAdapter1(lvAdapter);

        gvAdapter = new CMAllGvAdapter(this, allPhotographers);
        all_cm_gv.setAdapter1(gvAdapter);


        //初始化排序
        sortList = new ArrayList<Map>();
        String[] sortTypeIdArr = {1 + "", 2 + "", 3 + ""};
        String[] sortTypeNameArr = {"默认排序", "最新通告", "通告最多"};
        for (int i = 0; i < sortTypeIdArr.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("sortTypeId", sortTypeIdArr[i]);
            map.put("sortTypeName", sortTypeNameArr[i]);
            sortList.add(map);
        }
        sortPopWindow = new SortPopWindow(this);
        sortListAdapter = new SortListAdapter(this, sortList);
        sortPopWindow.setAdatper(sortListAdapter);

        occupTypePopWindow = new OccupTypePopWindow(this);
        occupTypeAdapter = new OccupTypeAdapter(this);
        occupTypePopWindow.setAdatper(occupTypeAdapter);
        occupTypePopWindow.setItemSelectListener(this);
        occupTypePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        all_cm_lv.setOnRefreshListener(this);
        all_cm_lv.setMode(PullToRefreshBase.Mode.BOTH);
        all_cm_lv.setOnItemClickListener(this);
        all_cm_gv.setOnRefreshListener(this);
        all_cm_gv.setMode(PullToRefreshBase.Mode.BOTH);
        all_cm_gv.setOnItemClickListener(this);

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

    private void requestData() {
//        RequestBean item = new RequestBean();
//        try {
//            JSONObject obj = new JSONObject(JSON.toJSONString(allCMReqBean));
//            item.setData(obj);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        item.setServiceURL(ConstTaskTag.QUERY_ALLPHOTOGRAPHERS_URL);
////        if (isShowLoading()){
////            ShowMsgDialog.showNoMsg(getActivity(), true);
////        }
//        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ALLPHOTOGRAPHERS_INT);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        if ("0000".equals(data.getCode())) {
            parseJson(data);
        } else {
            Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
            if (all_cm_lv.isRefreshing()){
                all_cm_lv.onRefreshComplete();
            }else if (all_cm_gv.isRefreshing()){
                all_cm_gv.onRefreshComplete();
            }
        }

    }

    private void parseJson(ResponseBean data) {
//        AllPhotographerView resAllPhotographers = JSON.parseObject(data.getRecord(), AllPhotographerView.class);
//        if (resAllPhotographers != null) {
//            reqTime = resAllPhotographers.getReqTime();
//            isBottom = resAllPhotographers.getPhotops().size() < pageSize;
//            if (mCurrentPage > 1) {
//                allPhotographers.getPhotops().addAll(resAllPhotographers.getPhotops());
//            } else {
//                allPhotographers.getPhotops().clear();
//                allPhotographers.getPhotops().addAll(resAllPhotographers.getPhotops());
//            }
//            if (showType == CMFragment.SHOW_AS_GRIDVIEW){
//                allPhotographers.getPhotops().removeAll(resAllPhotographers.getPhotops());
//                allPhotographers.getPhotops().addAll(resAllPhotographers.getPhotops());
//                gvAdapter.notifyDataSetChanged();
//                all_cm_gv.getRefreshableView().smoothScrollToPosition(allPhotographers.getPhotops().size()-resAllPhotographers.getPhotops().size());
//            } else if (showType == CMFragment.SHOW_AS_LISTVIEW){
//                lvAdapter.notifyDataSetChanged();
//                all_cm_lv.getRefreshableView().smoothScrollToPosition(allPhotographers.getPhotops().size()-resAllPhotographers.getPhotops().size());
//            }
//            if(resAllPhotographers.getPhotops().size() == 0 && mCurrentPage==1){
//                if (showType == CMFragment.SHOW_AS_GRIDVIEW){
//                    gv_rl.setVisibility(View.GONE);
//                } else if (showType == CMFragment.SHOW_AS_LISTVIEW){
//                    lv_rl.setVisibility(View.GONE);
//                }
//                empty_root_ll.setVisibility(View.VISIBLE);
//                primary_tip_tv.setText("没有找到摄影师!");
//            }
//        }else{
//            Toast.makeText(this, "抱歉，没有搜索到你想要的结果", Toast.LENGTH_SHORT).show();
//        }
//
//        if (all_cm_lv.isRefreshing()){
//            all_cm_lv.onRefreshComplete();
//        }else if (all_cm_gv.isRefreshing()){
//            all_cm_gv.onRefreshComplete();
//        }
    }

    public void switchShowView(int setShowType) {
        showType= setShowType;
        if (setShowType == CMFragment.SHOW_AS_GRIDVIEW){
            gv_rl.setVisibility(View.VISIBLE);
            lv_rl.setVisibility(View.GONE);
            gvAdapter.notifyDataSetChanged();
            if (gvAdapter.getCount() == 0){
                gv_rl.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
            }
        } else if (setShowType == CMFragment.SHOW_AS_LISTVIEW){
            gv_rl.setVisibility(View.GONE);
            lv_rl.setVisibility(View.VISIBLE);
            lvAdapter.notifyDataSetChanged();
            if (lvAdapter.getCount() == 0){
                lv_rl.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        setIsShowLoading(false);
        isBottom = false;
        mCurrentPage = 1;
        page.setPageIndex(mCurrentPage);
        allCMReqBean.setPage(page);
        allCMReqBean.setReqTime(null);
        requestData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!isBottom){
            setIsShowLoading(false);
            mCurrentPage += 1;
            page.setPageIndex(mCurrentPage);
            //筛选完后page对象已变
            allCMReqBean.setReqTime(reqTime + "");
            allCMReqBean.setPage(page);
            requestData();
        } else {
            if (all_cm_lv.isRefreshing()){
                new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case 1:
                                all_cm_lv.onRefreshComplete();
                                Toast.makeText(CMAllFragment.this, "没有更多摄影师了!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }.sendEmptyMessageDelayed(1, 1000);
            }else if (all_cm_gv.isRefreshing()){
                all_cm_gv.onRefreshComplete();
                Toast.makeText(CMAllFragment.this,"没有更多摄影师了!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void onRefresh() {
        setIsShowLoading(true);
        isBottom = false;
        if (showType == CMFragment.SHOW_AS_GRIDVIEW){
            gv_rl.setVisibility(View.VISIBLE);
            lv_rl.setVisibility(View.GONE);
        } else if (showType == CMFragment.SHOW_AS_LISTVIEW){
            lv_rl.setVisibility(View.VISIBLE);
            gv_rl.setVisibility(View.GONE);
        }
        empty_root_ll.setVisibility(View.GONE);
        mCurrentPage = 1;
        page.setPageIndex(mCurrentPage);
        allCMReqBean.setPage(page);
        requestData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (allPhotographers.getPhotops().size() > 0) {
            AllPhotographerVo cmBean = null;
            if (showType == CMFragment.SHOW_AS_GRIDVIEW){
                cmBean = allPhotographers.getPhotops().get(i);
            } else if (showType == CMFragment.SHOW_AS_LISTVIEW){
                cmBean = allPhotographers.getPhotops().get(i - 1);
            }
            Intent intent = new Intent(this, CMPersonInfoActivity.class);
            intent.putExtra("userNick", cmBean.getUsernick());
            intent.putExtra("userId", cmBean.getUserId()+"");
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
                Intent intent = new Intent(this, SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.switch_show_cb:
                finish();
                break;
        }
    }

    private void selectShootType() {

        shoottype_tv.setTextColor(getResources().getColor(R.color.dark_green));
        shoottype_iv.setImageResource(R.drawable.down_green);
        occupTypePopWindow.showAsDropDown(condition_ll);
        occupTypeAdapter.setSelectedItem(lastSelectShootPos);
    }

    private void sort() {
        sort_tv.setTextColor(getResources().getColor(R.color.dark_green));
        sort_iv.setImageResource(R.drawable.down_green);
        sortPopWindow.showAsDropDown(condition_ll);
        sortListAdapter.setSelectedItem(lastSelectSortPos);
    }

    private void filter() {
        Intent intent = new Intent(this, FilterCMActivity.class);
        intent.putExtra("requestBean", allCMReqBean);
        startActivityForResult(intent, CMFragment.FILTER_CM_REQUEST);
    }


    public void toFilter(AllCMReqBean resAllCMReqBean){
        if (FilterCMActivity.selectCon != 0) {
            filter_tv.setText("筛选(" + FilterCMActivity.selectCon + ")");
        } else {
            filter_tv.setText("筛选");
        }
        allCMReqBean = resAllCMReqBean;
        onRefresh();
    }


    @Override
    public void onOccupTypeItemClick(int pos) {
        lastSelectShootPos = pos;
        if (pos == 0) {
            shoottype_tv.setText("全部");
            allCMReqBean.setOccupType(null);
        } else {
            CarrierBean carrierBean = SGApplication.getInstance().getCmCarriers().get(pos - 1);
            String type_name = carrierBean.getCarrierName();
            if (type_name.length() > 4) {
                type_name = type_name.substring(0, 4);
            }
            shoottype_tv.setText(type_name);
            allCMReqBean.setOccupType(Integer.parseInt(carrierBean.getTypeId()));
        }

        onRefresh();
    }


    @Override
    public void onSortItemClick(int pos) {
        lastSelectSortPos = pos;
        sort_tv.setText((String) sortList.get(pos).get("sortTypeName"));
        allCMReqBean.setOrderType(Integer.parseInt((String) sortList.get(pos).get("sortTypeId")));
        onRefresh();
    }


    public AllCMReqBean getAllCMReqBean() {
        return allCMReqBean;
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setIsShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }

}

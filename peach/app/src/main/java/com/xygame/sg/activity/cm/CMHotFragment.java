package com.xygame.sg.activity.cm;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.cm.adapter.CMHotAdapter;
import com.xygame.sg.activity.cm.bean.HotPhotographerView;
import com.xygame.sg.activity.cm.bean.HotPhotographerVo;
import com.xygame.sg.activity.cm.bean.PageReq;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshGridView;

import org.json.JSONObject;


public class CMHotFragment extends SGBaseFragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    private PullToRefreshGridView hot_cm_gv;

    private CMHotAdapter adapter;
    private int pageSize = 15;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private HotPhotographerView hotPhotographers = new HotPhotographerView();
    private Long reqTime;

    private PageReq pageReq;

    private boolean isQuery = true;

    private boolean isShowLoading = false;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    private boolean isBottom = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        isPrepared = true;
        return mInflater.inflate(R.layout.fragment_cmhot, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initDatas();
        lazyLoad();

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        mHasLoadedOnce = true;
        addListener();
        setIsShowLoading(true);
        requestData();
    }

    private void findViews() {
        hot_cm_gv = (PullToRefreshGridView) getView().findViewById(R.id.hot_cm_gv);
        hot_cm_gv.setMode(PullToRefreshBase.Mode.BOTH);

        empty_root_ll = (LinearLayout) getView().findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) getView().findViewById(R.id.primary_tip_tv);
        sub_tip_tv = (TextView) getView().findViewById(R.id.sub_tip_tv);
    }

    private void initDatas() {
        adapter = new CMHotAdapter(getParentFragment().getActivity(), hotPhotographers);
        hot_cm_gv.setAdapter(adapter);
        //查询条件
        pageReq = new PageReq();
        pageReq.getPage().setPageSize(pageSize);
        pageReq.getPage().setPageIndex(mCurrentPage);
    }

    private void addListener() {
        hot_cm_gv.setOnRefreshListener(this);
        hot_cm_gv.setOnItemClickListener(this);

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
//            JSONObject obj = new JSONObject(JSON.toJSONString(pageReq));
//            item.setData(obj);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        item.setServiceURL(ConstTaskTag.QUERY_HOTPHOTOGRAPHERS_URL);
////        if (isShowLoading()){
////            ShowMsgDialog.showNoMsg(getActivity(), true);
////        }
//
//        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_HOTPHOTOGRAPHERS_INT);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        if ("0000".equals(data.getCode())) {
            parseJson(data);
        } else {
            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
            hot_cm_gv.onRefreshComplete();
        }

    }

    private void parseJson(ResponseBean data) {
//        HotPhotographerView resultHotPhotographers = JSON.parseObject(data.getRecord(), HotPhotographerView.class);
//        if (resultHotPhotographers != null) {
//            reqTime = resultHotPhotographers.getReqTime();
//            hot_cm_gv.setVisibility(View.VISIBLE);
//            isBottom = resultHotPhotographers.getPhotops().size() < pageSize;
//            if (mCurrentPage > 1) {
//                hotPhotographers.setReqTime(resultHotPhotographers.getReqTime());
//                hotPhotographers.getPhotops().addAll(resultHotPhotographers.getPhotops());
//                adapter.notifyDataSetChanged();
//            } else {
//                hotPhotographers = resultHotPhotographers;
//                adapter = new CMHotAdapter(getActivity(), hotPhotographers);
//                hot_cm_gv.setAdapter(adapter);
//                if (hotPhotographers.getPhotops().size() == 0) {
//                    hot_cm_gv.setVisibility(View.GONE);
//                    empty_root_ll.setVisibility(View.VISIBLE);
//                    primary_tip_tv.setText("没有找到摄影师!");
//                }
//            }
//        }
//
//        hot_cm_gv.onRefreshComplete();
    }


    public void onRefresh() {
        setIsShowLoading(true);
        hot_cm_gv.setVisibility(View.GONE);
        empty_root_ll.setVisibility(View.GONE);
        mCurrentPage = 1;
        pageReq.setReqTime(null);
        pageReq.getPage().setPageIndex(mCurrentPage);
        requestData();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        setIsShowLoading(false);
        isBottom = false;
        mCurrentPage = 1;
        pageReq.setReqTime(null);
        pageReq.getPage().setPageIndex(mCurrentPage);
        requestData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!isBottom){
            setIsShowLoading(false);
            mCurrentPage += 1;
            pageReq.setReqTime(reqTime + "");
            pageReq.getPage().setPageIndex(mCurrentPage);
            requestData();
        } else {
            hot_cm_gv.onRefreshComplete();
            Toast.makeText(getActivity(),"没有更多摄影师了!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (hotPhotographers.getPhotops().size() > 0) {
            HotPhotographerVo photographerVo = hotPhotographers.getPhotops().get(i);//54302062472593408
            Intent intent = new Intent(getParentFragment().getActivity(), CMPersonInfoActivity.class);
            intent.putExtra("userNick", photographerVo.getUsernick() + "");
            intent.putExtra("userId", photographerVo.getUserId() + "");
            intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
            startActivity(intent);
        }
    }


    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setIsShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }

}

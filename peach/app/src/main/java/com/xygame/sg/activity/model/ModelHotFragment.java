package com.xygame.sg.activity.model;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.model.adapter.ModelHotAdapter;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.activity.model.bean.HotModelBean;
import com.xygame.sg.activity.model.bean.ModelBean;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.NestedGridView;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ModelHotFragment extends SGBaseFragment implements PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshScrollView refresh_sv;
    private NestedGridView hot_model_n_gv;
    private List<BannerBean> banerList;
    private LinearLayout banner_ll;

    private int pageSize = 15;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private PageBean page = new PageBean();
    private HotModelBean hotModelBean;
    private List<ModelBean> models;
    private ModelHotAdapter modelsAdapter;

    private boolean isShowLoading = true;

    private LinearLayout model_ll;
    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;
    private ImageLoader imageLoader;

    private boolean isBottom = false;

    public ModelHotFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_model_hot, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
        addListener();
        requestData();
    }

    private void initViews() {
        refresh_sv = (PullToRefreshScrollView) getView().findViewById(R.id.refresh_sv);
        refresh_sv.setMode(PullToRefreshBase.Mode.BOTH);
        refresh_sv.getRefreshableView().smoothScrollTo(0, 0);
        hot_model_n_gv = (NestedGridView) getView().findViewById(R.id.hot_model_n_gv);
        hot_model_n_gv.setFocusable(false);

        banner_ll = (LinearLayout) getView().findViewById(R.id.banner_ll);

        model_ll = (LinearLayout) getView().findViewById(R.id.model_ll);
        empty_root_ll = (LinearLayout) getView().findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) getView().findViewById(R.id.primary_tip_tv);
        sub_tip_tv = (TextView) getView().findViewById(R.id.sub_tip_tv);
        empty_root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIsShowLoading(true);
                mCurrentPage = 1;
                page.setPageIndex(mCurrentPage);
                requestBannerList();
                requestHotModelList();
            }
        });
    }

    private void initDatas() {
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        page.setPageIndex(mCurrentPage);
        page.setPageSize(pageSize);
        hotModelBean = new HotModelBean();
        models = new ArrayList<ModelBean>();
        hot_model_n_gv.setVerticalSpacing(DataTools.dip2px(getParentFragment().getActivity(), 3));
        modelsAdapter = new ModelHotAdapter(getParentFragment().getActivity(), models);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(modelsAdapter);
        alphaInAnimationAdapter.setAbsListView(hot_model_n_gv);
        hot_model_n_gv.setAdapter(alphaInAnimationAdapter);
    }

    private void addListener() {

        hot_model_n_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (models.size() > 0) {
                    ModelBean modelBean = models.get(i);
                    Intent intent = new Intent(getParentFragment().getActivity(), PersonalDetailActivity.class);
                    intent.putExtra("userNick", modelBean.getUserNick());
                    intent.putExtra("userId", modelBean.getUserId());
                    intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
                    startActivity(intent);
                }
            }
        });
        refresh_sv.setOnRefreshListener(this);
    }

    private void requestData() {
        requestBannerList();
        requestHotModelList();
    }


    private void requestBannerList() {
        RequestBean item = new RequestBean();
        try {
            item.setData(new JSONObject());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.LOAD_BANNER_INFO_URL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.LOAD_BANNER_INFO_INT);
    }

    private void requestHotModelList() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex",page.getPageIndex()).put("pageSize",page.getPageSize()));
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (isShowLoading()) {
            ShowMsgDialog.showNoMsg(getActivity(), true);
        }
        item.setServiceURL(ConstTaskTag.LOAD_HOT_MODEL_INFO_URL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.LOAD_HOT_MODEL_INFO_INT);
    }

    public void responseBannerList(ResponseBean data) {
//        List<BannerBean> resBannerList = JSON.parseArray(data.getRecord(), BannerBean.class);
//        if (resBannerList != null) {
//            banerList = resBannerList;
//            int size = banerList.size();
//            banner_ll.removeAllViews();
//            for (int i = 0; i < size; i++) {
//                View view = getActivity().getLayoutInflater().inflate(R.layout.model_banner_item, null);
//                ImageView banner_iv = (ImageView) view.findViewById(R.id.banner_iv);
//                final BannerBean bannerBean = banerList.get(i);
//                String picUrl = bannerBean.getPicUrl();
//                if (!StringUtils.isEmpty(picUrl)) {
//                    imageLoader.loadImage(picUrl, banner_iv, true);
//                }
//                banner_ll.addView(view);
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getParentFragment().getActivity(), CommonWebViewActivity.class);
//                        intent.putExtra("webUrl", bannerBean.getTargetUrl());
//                        intent.putExtra("title", bannerBean.getTitle());
//                        intent.putExtra("bannerBean", bannerBean);
//                        startActivity(intent);
//                    }
//                });
//            }
//        }
    }

    public void responseHotModel(ResponseBean data) {
//        HotModelBean resHotModelBean = JSON.parseObject(data.getRecord(), HotModelBean.class);
//        if (resHotModelBean != null) {
//            isBottom = resHotModelBean.getModels().size() < pageSize;
//            if (mCurrentPage > 1) {
//                models.addAll(resHotModelBean.getModels());
//            } else {
//                models.clear();
//                models.addAll(resHotModelBean.getModels());
//            }
//            modelsAdapter.notifyDataSetChanged();
//        }
//        refresh_sv.onRefreshComplete();
//        if (mCurrentPage == 1) {
//            refresh_sv.getRefreshableView().post(new Runnable() {
//                //让scrollview跳转到顶部，必须放在runnable()方法中
//                @Override
//                public void run() {
//                    refresh_sv.getRefreshableView().scrollTo(0, 0);
//                }
//            });
//        }

    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.LOAD_BANNER_INFO_INT:
                responseBannerList(data);
                break;
            case ConstTaskTag.LOAD_HOT_MODEL_INFO_INT:
                ShowMsgDialog.cancel();
                responseHotModel(data);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isBottom = false;
        setIsShowLoading(false);
        mCurrentPage = 1;
        page.setPageIndex(mCurrentPage);
        requestBannerList();
        requestHotModelList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!isBottom){
            setIsShowLoading(false);
            mCurrentPage += 1;
            page.setPageIndex(mCurrentPage);
            requestHotModelList();
        } else {
            refresh_sv.onRefreshComplete();
            Toast.makeText(getActivity(),"没有更多模特了!",Toast.LENGTH_SHORT).show();
        }
    }

    private void setGridViewHeight(GridView gv) {
        ViewGroup.LayoutParams params = gv.getLayoutParams();
//        int totalHeight = gv.getMeasuredHeight();
        int totalHeight = 0;
        int size = 0;
        int count = gv.getAdapter().getCount();
        if (count % gv.getNumColumns() == 0) {
            size = count / gv.getNumColumns();
        } else {
            size = count / gv.getNumColumns() + 1;
        }
        for (int i = 0; i < size; i++) {
            View listItem = gv.getAdapter().getView(0, null, gv);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        params.height = totalHeight + DataTools.dip2px(getParentFragment().getActivity(), 50);
        gv.setLayoutParams(params);
    }


    public int getmCurrentPage() {
        return mCurrentPage;
    }

    public PullToRefreshScrollView getRefresh_sv() {
        return refresh_sv;
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setIsShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }
}

/*
 * 文 件 名:  NoticeFragment.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月2日
 */
package com.xygame.sg.activity.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.FilterNoticeActivity;
import com.xygame.sg.activity.notice.PlushNoticeActivity;
import com.xygame.sg.activity.notice.SearchNoticeActivity;
import com.xygame.sg.activity.notice.ShootTypePopView;
import com.xygame.sg.activity.notice.SortPopWindow;
import com.xygame.sg.activity.notice.adapter.NoticeAdapter;
import com.xygame.sg.activity.notice.adapter.SortListAdapter;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.QueryNoticesCond;
import com.xygame.sg.activity.notice.bean.QueryNoticesListBean;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.adapter.comm.ActionOptionAdapter;
import com.xygame.sg.define.view.ChoiceNoticeView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.broadcast.EditorUserInfoBroadcastListener;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeSYFragment extends SGBaseFragment implements PullToRefreshBase.OnRefreshListener2, View.OnClickListener,  SortPopWindow.IOnSortItemSelectListener {
    private LinearLayout condition_ll;
    private LinearLayout shoottype_ll;
    private TextView shoottype_tv;
    private ImageView shoottype_iv;
    private LinearLayout sort_ll;
    private TextView sort_tv;
    private ImageView sort_iv;
    private LinearLayout filter_ll;
    private TextView filter_tv;
    private PullToRefreshListView2 listView;
    private ImageView search_iv;
    private RelativeLayout new_notice_rl;

    private NoticeAdapter adapter;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private NoticeListBean noticeList = new NoticeListBean();//通告主体数据
    private Long reqTime;

    private TextView titleName;

    private PopupWindow shootTypePop,mPopWindow;
    private ShootTypePopView shootTypePopView;

    private SortPopWindow sortPopWindow;
    private SortListAdapter sortListAdapter;
    private List<Map> sortList;
    private int lastSelectSortPos = 0;

    private QueryNoticesListBean queryNoticesListBean;


    private boolean isQuery = true;


    private static final int FILTER_REQUEST = 1;
    private boolean isShowLoading = false;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private boolean isBottom = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        isPrepared = true;
        return mInflater.inflate(R.layout.sg_sy_notice_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditorUserInfoBroadcastListener.registerEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        findViews();
        initDatas();
        lazyLoad();
    }
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible|| mHasLoadedOnce ) {
            return;
        }
        mHasLoadedOnce = true;
        addListener();
        setIsShowLoading(true);
    }

    private void findViews() {
        titleName=(TextView)getView().findViewById(R.id.titleName);
        condition_ll = (LinearLayout) getView().findViewById(R.id.condition_ll);
        shoottype_ll = (LinearLayout) getView().findViewById(R.id.shoottype_ll);
        shoottype_tv = (TextView) getView().findViewById(R.id.shoottype_tv);
        shoottype_iv = (ImageView) getView().findViewById(R.id.shoottype_iv);
        sort_ll = (LinearLayout) getView().findViewById(R.id.sort_ll);
        sort_tv = (TextView) getView().findViewById(R.id.sort_tv);
        sort_iv = (ImageView) getView().findViewById(R.id.sort_iv);
        filter_ll = (LinearLayout) getView().findViewById(R.id.filter_ll);
        filter_tv = (TextView) getView().findViewById(R.id.filter_tv);
        listView = (PullToRefreshListView2) getView().findViewById(R.id.refreshlistview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        search_iv = (ImageView) getView().findViewById(R.id.search_iv);
        new_notice_rl = (RelativeLayout) getView().findViewById(R.id.new_notice_rl);
        if (UserPreferencesUtil.isOnline(getActivity()) ){
            if ((UserPreferencesUtil.getUserType(getActivity()).equals(Constants.CARRE_MODEL) || UserPreferencesUtil.getUserType(getActivity()).equals(Constants.PRO_MODEL))) {
                new_notice_rl.setVisibility(View.INVISIBLE);
            } else {
                new_notice_rl.setVisibility(View.VISIBLE);
            }
        }else{
            new_notice_rl.setVisibility(View.VISIBLE);
        }

        titleName.setVisibility(View.VISIBLE);
        empty_root_ll = (LinearLayout) getView().findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) getView().findViewById(R.id.primary_tip_tv);
        sub_tip_tv = (TextView) getView().findViewById(R.id.sub_tip_tv);
    }

    private void initDatas() {
        adapter = new NoticeAdapter(getActivity(), noticeList, isQuery);
        listView.setAdapter(adapter);


        //初始化排序
        sortList = new ArrayList<Map>();
        String[] sortTypeIdArr = {1 + "", 2 + "",3+""};
        String[] sortTypeNameArr = {"默认排序", "价格最高","最近拍摄"};
        for (int i = 0; i < sortTypeIdArr.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("sortTypeId", sortTypeIdArr[i]);
            map.put("sortTypeName", sortTypeNameArr[i]);
            sortList.add(map);
        }
        sortPopWindow = new SortPopWindow(getActivity());
        sortListAdapter = new SortListAdapter(getActivity(), sortList);
        sortPopWindow.setAdatper(sortListAdapter);

        //查询条件
        queryNoticesListBean = new QueryNoticesListBean();
        queryNoticesListBean.getPage().setPageSize(pageSize);
        queryNoticesListBean.getPage().setPageIndex(mCurrentPage);

    }

    public int adapterCout(){
        return adapter.getCount();
    }

    private void addListener() {
        shoottype_ll.setOnClickListener(this);
        listView.setOnRefreshListener(this);
        sort_ll.setOnClickListener(this);
        filter_ll.setOnClickListener(this);
        search_iv.setOnClickListener(this);
        new_notice_rl.setOnClickListener(this);
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

        if (SGApplication.getInstance().getTypeList()!=null){
            requestNoticesList();
        } else {
            requestShootType();
        }
    }

    private void requestNoticesList() {
//        new Action(QueryNotices.class, "${queryNotices}", getActivity(), this, unit).run();
//        RequestBean item = new RequestBean();
//        try {
//            JSONObject obj = new JSONObject(JSON.toJSONString(queryNoticesListBean));
//            item.setData(obj);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
////        if (isShowLoading()){
////            ShowMsgDialog.showNoMsg(getActivity(), true);
////        }
//        item.setServiceURL(ConstTaskTag.QUERY_NOTICES_URL);
//        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_NOTICES_INT);
    }
    private void requestShootType() {
//        unit.getDataUnit().getRepo().put("containSub", "1");//是否包含小类（0：不包含，1：包含）
//        new Action(QueryModelShootType.class,"${queryModelShootType},containSub=${containSub}", getActivity(), this, unit).run();
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
    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_MODEL_SHOOT_TYPE_INT:
                responseShootType(data);
                break;
            case ConstTaskTag.QUERY_NOTICES_INT:
                responseNoticeList(data);
                break;
        }
    }
    public void responseShootType(ResponseBean data) {
//        List<ShootTypeBean> resultList = JSON.parseArray(data.getRecord(), ShootTypeBean.class);
//        SGApplication.getInstance().setTypeList(resultList);
//        requestNoticesList();
    }

    private void initShootTypePop() {
        shootTypePop = new PopupWindow(getActivity());
        shootTypePopView = new ShootTypePopView(getActivity());
        final RelativeLayout r = new RelativeLayout(getActivity());

        int displayHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int maxHeight = (int) (displayHeight * 0.718);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, maxHeight);
        r.addView(shootTypePopView, rl);

        shootTypePop.setContentView(r);
        shootTypePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        shootTypePop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        shootTypePop.setFocusable(true);
        shootTypePop.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x55000000);
        shootTypePop.setBackgroundDrawable(dw);
        shootTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                shoottype_iv.setImageResource(R.drawable.down_gray);
            }
        });
        shootTypePopView.setOnSelectListener(new ShootTypePopView.OnSelectListener() {
            @Override
            public void setSelectedType(ShootTypeBean typeBean, ShootSubTypeBean subTypeBean) {
                if (shootTypePop.isShowing()) {
                    shootTypePop.dismiss();
                }
                if (typeBean.getTypeId() == -1) {
                    shoottype_tv.setText("全部");
                    queryNoticesListBean.setShootType(-1);
                } else {
                    String type_name = subTypeBean.getTypeName();
                    if (type_name.length() > 4) {
                        type_name = type_name.substring(0, 4);
                    }
                    shoottype_tv.setText(type_name);
                    queryNoticesListBean.setShootType(subTypeBean.getTypeId());
                }
                queryNoticesListBean.setCond(new QueryNoticesCond());
                filter_tv.setText("筛选");

                onRefresh();

            }
        });
    }

    public void responseNoticeList(ResponseBean data) {
//        NoticeListBean resultNoticeList = JSON.parseObject(data.getRecord(), NoticeListBean.class);
//        if (resultNoticeList != null) {
//            reqTime = resultNoticeList.getReqTime();
//            listView.setVisibility(View.VISIBLE);
//            isBottom = resultNoticeList.getNotices().size() < pageSize;
//            if (mCurrentPage > 1) {
//                noticeList.setReqTime(resultNoticeList.getReqTime());
//                noticeList.getNotices().addAll(resultNoticeList.getNotices());
//                adapter.notifyDataSetChanged();
//            } else {
//                noticeList = resultNoticeList;
//                adapter = new NoticeAdapter(getActivity(), noticeList, isQuery);
//                listView.setAdapter(adapter);
//                if(noticeList.getNotices().size() == 0){
//                    listView.setVisibility(View.GONE);
//                    empty_root_ll.setVisibility(View.VISIBLE);
//                    primary_tip_tv.setText("没有找到通告!");
//                }
//
//            }
//        }
//
//        listView.onRefreshComplete();
    }

    public void onRefresh() {
        setIsShowLoading(true);
        isBottom = false;
        listView.setVisibility(View.GONE);
        empty_root_ll.setVisibility(View.GONE);
        mCurrentPage = 1;
        queryNoticesListBean.setReqtime(null);
        queryNoticesListBean.getPage().setPageIndex(mCurrentPage);
        requestNoticesList();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        setIsShowLoading(false);
        isBottom = false;
        mCurrentPage = 1;
        queryNoticesListBean.setReqtime(null);
        queryNoticesListBean.getPage().setPageIndex(mCurrentPage);
        requestNoticesList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

        if (!isBottom){
            setIsShowLoading(false);
            mCurrentPage += 1;
            queryNoticesListBean.setReqtime(reqTime + "");
            queryNoticesListBean.getPage().setPageIndex(mCurrentPage);
            requestNoticesList();
        } else {
            if (listView.isRefreshing()){
                new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what){
                            case 1:
                                listView.onRefreshComplete();
                                Toast.makeText(getActivity(), "没有更多通告了!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }.sendEmptyMessageDelayed(1,1000);
            }

        }
    }

    @Override
    public void onDestroy() {
        EditorUserInfoBroadcastListener.unRegisterEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        super.onDestroy();
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           if (Constants.ENROLL_SUCCESS.equals(intent.getAction())) {
                onRefresh();
            }
        }
    };


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
                search();
                break;
            case R.id.new_notice_rl:
                addNotice();
                break;
        }
    }


    private void selectShootType() {
         if (SGApplication.getInstance().getTypeList()!= null){
             if ( SGApplication.getInstance().getTypeList().size() >0){
                 showOptionMenu();
             }
         } else {
             requestShootType();
         }
    }

    private void sort() {
        sort_tv.setTextColor(getResources().getColor(R.color.dark_green));
        sort_iv.setImageResource(R.drawable.down_green);
        sortPopWindow.showAsDropDown(condition_ll);
        sortListAdapter.setSelectedItem(lastSelectSortPos);
    }

    private void filter() {
        Intent intent = new Intent(getActivity(), FilterNoticeActivity.class);
        intent.putExtra("requestBean", queryNoticesListBean);
        startActivityForResult(intent, FILTER_REQUEST);
    }

    private void search() {
        if (SGApplication.getInstance().getTypeList()!=null) {
            Intent intent = new Intent(getActivity(), SearchNoticeActivity.class);
            startActivity(intent);
        }
    }

    private void addNotice() {
        if (UserPreferencesUtil.isOnline(getActivity()) && UserPreferencesUtil.getUserType(getActivity()).equals(Constants.CARRE_PHOTOR)) {
            Intent intent = new Intent(getActivity(), ChoiceNoticeView.class);
            startActivityForResult(intent, 0);
        } else {
            Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
            startActivity(intent);
        }
    }





    @Override
    public void onSortItemClick(int pos) {
        lastSelectSortPos = pos;
        sort_tv.setText((String) sortList.get(pos).get("sortTypeName"));
        queryNoticesListBean.setOrderType(Integer.parseInt((String) sortList.get(pos).get("sortTypeId")));
        queryNoticesListBean.setCond(new QueryNoticesCond());
        onRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (Activity.RESULT_OK != resultCode || null == data) {
                        return;
                    }
                    String flag = data.getStringExtra(Constants.COMEBACK);
                    if ("galary".equals(flag)) {
                        Intent intent = new Intent(getActivity(), PlushNoticeActivity.class);
                        intent.putExtra("noticeFlag", "pay");
                        startActivity(intent);
                    } else if ("camera".equals(flag)) {
                        if (Constants.USER_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))) {
                            Intent intent = new Intent(getActivity(), PlushNoticeActivity.class);
                            intent.putExtra("noticeFlag", "noPay");
                            startActivity(intent);
                        } else if (Constants.USER_NO_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))) {
                            showDialog(getString(R.string.to_publish_no_verify));
                        } else if (Constants.USER_VERIFING_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))) {
                            showOneButtonDialog(getString(R.string.to_publish_verifing));
                        } else if (Constants.USER_VERIFING_REFUSED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))) {
                            showDialog(getString(R.string.to_publish_refused));
                        }
                    }
                    break;
                case FILTER_REQUEST:
                    queryNoticesListBean = (QueryNoticesListBean) data.getSerializableExtra("requestBean");
                    int difNum = getNumCount();
                    if (difNum != 0) {
                        filter_tv.setText("筛选(" + difNum + ")");
                    } else {
                        filter_tv.setText("筛选");
                    }
                    onRefresh();
                    break;
            }
        }
    }

    private int getNumCount(){
        int count=0;
        if (queryNoticesListBean.getCond().getRecruitCond().getGender()!=-1){
            count=count+1;
        }
        if (queryNoticesListBean.getCond().getShootCond().getCity()!=-1){
            count=count+1;
        }
        if (queryNoticesListBean.getCond().getRepayStatus()!=-1){
            count=count+1;
        }
        if (queryNoticesListBean.getCond().getRecruitCond().getIsAffordAccomFee()!=-1){
            count=count+1;
        }
        if (queryNoticesListBean.getCond().getRecruitCond().getIsAffordTravelFee()!=-1){
            count=count+1;
        }
        return count;
    }

    private void showOneButtonDialog(String tip) {
        OneButtonDialog dialog = new OneButtonDialog(getActivity(), tip, R.style.dineDialog, new ButtonOneListener() {

            @Override
            public void confrimListener(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialog(String tip) {
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(), "尚未认证的摄影师只能发布预付费通告或立即前往认证噢！", R.style.dineDialog, new ButtonTwoListener() {

            @Override
            public void confrimListener() {
                goToCerti();
            }

            @Override
            public void cancelListener() {
            }
        });
        dialog.show();
    }

    private void goToCerti() {
        Intent intent = new Intent(getActivity(), CMIdentyFirstActivity.class);
        startActivity(intent);
    }

    public QueryNoticesListBean getQueryNoticesListBean() {
        return queryNoticesListBean;
    }

    public boolean isShowLoading() {
        return isShowLoading;
    }

    public void setIsShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }

    private PopupWindow initPopWindow(Context context, int popWinLayout,
                                      boolean isDismissMenuOutsideTouch) {

        PopupWindow mPopWin = new PopupWindow(
                ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(popWinLayout, null),
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams .MATCH_PARENT);
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

    private void showOptionMenu(){
        shoottype_tv.setTextColor(getResources().getColor(R.color.dark_green));
        shoottype_iv.setImageResource(R.drawable.down_green);
        mPopWindow = initPopWindow(getActivity(),
                R.layout.le_action_option_pop_layout, true);
        View root = mPopWindow.getContentView();
        root.getBackground().setAlpha(50);

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                    shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                    shoottype_iv.setImageResource(R.drawable.down_gray);
                }
            }
        });

        View topOption = root.findViewById(R.id.topOption);
        View bottomNullView = root.findViewById(R.id.bottomNullView);
        ListView optionList = (ListView) root.findViewById(R.id.optionList);
        TextView allTip=(TextView)root.findViewById(R.id.allTip);
        View selected_iv=root.findViewById(R.id.selected_iv);
        List<ShootTypeBean> resultList =SGApplication.getInstance().getTypeList();
        if (resultList==null){
            resultList=new ArrayList<>();
        }
        if (queryNoticesListBean.getShootType()==-1){
            allTip.setTextColor(getResources().getColor(R.color.dark_green));
            selected_iv.setVisibility(View.VISIBLE);
        }else{
            allTip.setTextColor(getResources().getColor(R.color.black));
            selected_iv.setVisibility(View.GONE);
        }

        final ActionOptionAdapter adapter = new ActionOptionAdapter(getActivity(), resultList,queryNoticesListBean);
        optionList.setAdapter(adapter);

        optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mPopWindow.dismiss();
                mPopWindow = null;
                ShootTypeBean subTypeBean=adapter.getItem(arg2);
                String type_name = subTypeBean.getTypeName();
                if (type_name.length() > 4) {
                    type_name = type_name.substring(0, 4);
                }
                shoottype_tv.setText(type_name);
                queryNoticesListBean.setShootType(subTypeBean.getTypeId());
                queryNoticesListBean.setCond(new QueryNoticesCond());
                filter_tv.setText("筛选");
                shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                shoottype_iv.setImageResource(R.drawable.down_gray);
                onRefresh();
            }
        });

        topOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                shoottype_tv.setText("全部");
                queryNoticesListBean.setShootType(-1);
                mPopWindow.dismiss();
                mPopWindow = null;
                queryNoticesListBean.setCond(new QueryNoticesCond());
                filter_tv.setText("筛选");
                shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                shoottype_iv.setImageResource(R.drawable.down_gray);
                onRefresh();
            }
        });

        bottomNullView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mPopWindow.dismiss();
                mPopWindow = null;
                shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                shoottype_iv.setImageResource(R.drawable.down_gray);
            }
        });

        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            mPopWindow = null;
            shoottype_tv.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
            shoottype_iv.setImageResource(R.drawable.down_gray);
        } else {
            mPopWindow.showAsDropDown(condition_ll);
        }
    }

    public void queryDatas() {
        requestData();
    }
}
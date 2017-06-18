package com.xygame.second.sg.jinpai;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.activity.ActManagerActivity;
import com.xygame.second.sg.comm.activity.PlaceChoiceMainActivity;
import com.xygame.second.sg.comm.inteface.TransActMain;
import com.xygame.second.sg.jinpai.activity.JinPaiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiFuFeiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiPlushActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiPlushYaoYueActivity;
import com.xygame.second.sg.jinpai.adapter.FuFeiAdapter;
import com.xygame.second.sg.jinpai.adapter.FuFeiBigTypeChooseAdapter;
import com.xygame.second.sg.jinpai.adapter.JinPaiAdapter;
import com.xygame.second.sg.jinpai.adapter.JinPaiBigTypeChooseAdapter;
import com.xygame.second.sg.jinpai.adapter.PopAutoAdater;
import com.xygame.second.sg.jinpai.adapter.PopServiceParentAdater;
import com.xygame.second.sg.jinpai.adapter.PopSexAdater;
import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinpaiConditionBean;
import com.xygame.second.sg.personal.activity.VideoCertifyActivity;
import com.xygame.second.sg.personal.activity.VideoCertifyChangeActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.main.NewsFragment;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshGridView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FuFeiFragment extends SGBaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    private TextView cityText, serviceText, sexText, autoText,nuReadNews;
    private ImageView serviceImage, sexImage, autoImage;
    private View plushButton, serviceView, sexView, autoView,provinceButton,rightButton;
    private View header;
    private FrameLayout frameView;
    private GridView photoList;
    private FuFeiBigTypeChooseAdapter typeAdapter;
    private FuFeiAdapter adapter;
    private PopupWindow mPopWindow;
    private PullToRefreshGridView2 listView;
    private List<PopSexBean> sexDatas = new ArrayList<>();
    private List<PopAutoBean> autoDatas = new ArrayList<>();
    private int pageSize = 21;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private String hotReqTime;
    private boolean hotIsLoading = true;
    private JinpaiConditionBean conditionBean;
    private boolean isJustBigType=false;
    private  JinPaiBigTypeBean bigTypeBean;
    private ProvinceBean areaBean;
    private TransActMain transActMainListener;
    List<JinPaiBigTypeBean> jinPaiBigTypeBeans;

    public void setTransToPersonalListener(TransActMain transActMainListener){
        this.transActMainListener=transActMainListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        quaryUnReadNews();
        areaBean =
                CacheService.getInstance().getCacheJPProvinceBean("JPProvinceBean");
        if (areaBean != null) {
            cityText.setText(areaBean.getProvinceName());
        } else {
            if (BaiduPreferencesUtil.getProvice(getActivity()) != null) {
                ThreadPool.getInstance().excuseThread(new JugmentUse());
            }
        }
    }

    private class JugmentUse implements Runnable {
        @Override
        public void run() {
            List<ProvinceBean> datas = (List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0);
            for (ProvinceBean it : datas) {
                it.get();
                if (BaiduPreferencesUtil.getProvice(getActivity()).contains(it.getProvinceName())) {
                    areaBean = it;
                    break;
                }
            }
            mHandler.sendEmptyMessage(0);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    cityText.setText(areaBean.getProvinceName());
                    CacheService.getInstance().cacheJPProvinceBean("JPProvinceBean", areaBean);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerLoginListener();
        initViews();
        initDatas();
        addListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jinpai_fragment, null);
    }

    private void initViews() {
        rightButton=getView().findViewById(R.id.rightButton);
        nuReadNews=(TextView)getView().findViewById(R.id.nuReadNews);
        provinceButton=getView().findViewById(R.id.provinceButton);
        serviceImage = (ImageView) getView().findViewById(R.id.serviceImage);
        sexImage = (ImageView) getView().findViewById(R.id.sexImage);
        autoImage = (ImageView) getView().findViewById(R.id.autoImage);

        serviceText = (TextView) getView().findViewById(R.id.serviceText);
        sexText = (TextView) getView().findViewById(R.id.sexText);
        autoText = (TextView) getView().findViewById(R.id.autoText);

        serviceView = getView().findViewById(R.id.serviceView);
        sexView = getView().findViewById(R.id.sexView);
        autoView = getView().findViewById(R.id.autoView);

        frameView = (FrameLayout) getView().findViewById(R.id.frameView);
        listView = new PullToRefreshGridView2(getActivity());
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        plushButton = getView().findViewById(R.id.plushButton);
        cityText = (TextView) getView().findViewById(R.id.cityText);

        header = LayoutInflater.from(getActivity()).inflate(R.layout.jinpai_list_head_view, null);
        photoList = (GridView) header.findViewById(R.id.photoList);
        listView.addHeadViewAction(header);//添加头部
        listView.setNumColumns(2);
        listView.setVerticalSpacing(5);
        listView.setHorizontalSpacing(5);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        frameView.addView(listView);
    }

    private void initDatas() {
        conditionBean = new JinpaiConditionBean();
        PopSexBean sexBean3 = new PopSexBean();
        sexBean3.setId("-1");
        sexBean3.setSex("不限男女");
        conditionBean.setSexBean(sexBean3);
        sexDatas.add(sexBean3);
        PopSexBean sexBean1 = new PopSexBean();
        sexBean1.setId(Constants.SEX_MAN);
        sexBean1.setSex("只看男");
        sexDatas.add(sexBean1);
        PopSexBean sexBean2 = new PopSexBean();
        sexBean2.setId(Constants.SEX_WOMAN);
        sexBean2.setSex("只看女");
        sexDatas.add(sexBean2);
        PopAutoBean autoBean1 = new PopAutoBean();
        autoBean1.setId("1");
        autoBean1.setName("最新发布");
        conditionBean.setAutoBean(autoBean1);
        autoDatas.add(autoBean1);
        PopAutoBean autoBean2 = new PopAutoBean();
        autoBean2.setId("2");
        autoBean2.setName("高价优先");
        autoDatas.add(autoBean2);
        PopAutoBean autoBean3 = new PopAutoBean();
        autoBean3.setId("3");
        autoBean3.setName("低价最低");
        autoDatas.add(autoBean3);

        typeAdapter = new FuFeiBigTypeChooseAdapter(getActivity(), null);
        photoList.setAdapter(typeAdapter);
        adapter = new FuFeiAdapter(getActivity(), null);
        listView.setAdapter(adapter);
    }

    public void jinPaiType(){
        jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        if (jinPaiBigTypeBeans != null) {
            if (jinPaiBigTypeBeans.size() > 0) {
                loadJinPaiDatas();
            } else {
                requestActType();
            }
        } else {
            requestActType();
        }
    }

    public int getJinPaiDatas() {
        return adapter.getCount();
    }

    private void loadJinPaiDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            obj.put("orderType", conditionBean.getAutoBean().getId());
            JSONObject object1 =new JSONObject();;

            if (areaBean.getProvinceCode()!=null){
                if (!"1".equals(areaBean.getProvinceCode())){
                    object1.put("location", areaBean.getProvinceCode());
                }
            }

            if (!"-1".equals(conditionBean.getSexBean().getId())) {
                object1.put("gender", conditionBean.getSexBean().getId());
            }

            if (bigTypeBean != null) {
                if (!"-1".equals(bigTypeBean.getId())) {
                    object1.put("actType", bigTypeBean.getId());
                }
            }

            obj.put("cond", object1);
            if (hotPage > 1) {
                obj.put("reqTime", hotReqTime);
            } else {
                adapter.clearDatas();
                ShowMsgDialog.showNoMsg(getActivity(), true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_NORMAL_ACT_LIST);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_NORMAL_ACT_LIST);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void parseHotModelDatas(ResponseBean data) {
        if (!TextUtils.isEmpty(data.getRecord()) && !"null".equals(data.getRecord())) {
            List<JinPaiBean> datas = new ArrayList<>();
            try {
                JSONObject object1 = new JSONObject(data.getRecord());
                String actions = StringUtils.getJsonValue(object1, "actions");
                hotReqTime = StringUtils.getJsonValue(object1, "reqTime");
                if (!TextUtils.isEmpty(actions) && !"[]".equals(actions) && !"[null]".equals(actions) && !"null".equals(actions)) {
                    JSONArray jsonArray = new JSONArray(actions);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object2 = jsonArray.getJSONObject(i);
                        JinPaiBean item = new JinPaiBean();
                        item.setActId(StringUtils.getJsonValue(object2, "actId"));
                        item.setActTitle(StringUtils.getJsonValue(object2, "actTitle"));
                        item.setBidEndTime(StringUtils.getJsonValue(object2, "endTime"));
                        item.setShowCoverUrl(StringUtils.getJsonValue(object2, "showCoverUrl"));
                        item.setTotalBidCount(StringUtils.getJsonValue(object2, "totalBidCount"));
                        item.setUserId(StringUtils.getJsonValue(object2, "userId"));
                        item.setUsernick(StringUtils.getJsonValue(object2, "usernick"));
                        item.setPrice(StringUtils.getJsonValue(object2, "price"));
//                        item.setActNature(StringUtils.getJsonValue(object2, "actNature"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                hotIsLoading = false;
            }
            adapter.addDatas(datas, hotPage);
            if (hotPage==1&&adapter.getCount()>4){
                listView.setSelection(2);
            }
        } else {
            hotIsLoading = false;
        }
    }

    private void addListener() {
        nuReadNews.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        serviceView.setOnClickListener(this);
        sexView.setOnClickListener(this);
        autoView.setOnClickListener(this);
        provinceButton.setOnClickListener(this);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        plushButton.setOnClickListener(this);
        photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bigTypeBean = typeAdapter.getItem(position);
                isJustBigType = true;
                serviceText.setText(bigTypeBean.getName());
                hotIsLoading = true;
                hotPage = 1;
                loadJinPaiDatas();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plushButton:
                boolean islogin = UserPreferencesUtil.isOnline(getActivity());
                if (islogin){
//                    String idStatus = UserPreferencesUtil.getUserIDAuth(getActivity());
                    String videoStatus = UserPreferencesUtil.getUserVideoAuth(getActivity());
//                    if ("2".equals(idStatus)) {
//                        if ("2".equals(videoStatus)) {
//                           Intent intent = new Intent(getActivity(), JinPaiPlushActivity.class);
//                            intent.putExtra("wichFlag", "fufeiYue");
//                            startActivityForResult(intent, 0);
//                        } else {
//                            transActMainListener.toPersonPage();
//                            showForwardsDialog("您还没有进行视频认证，认证后才能发布活动", 2);
//                        }
//                    } else {
//                        transActMainListener.toPersonPage();
//                        showForwardsDialog("您还没有进行身份认证，认证后才能发布活动",1);
//                    }
                    if ("2".equals(videoStatus)) {
                        Intent intent = new Intent(getActivity(), JinPaiPlushActivity.class);
                        intent.putExtra("wichFlag", "fufeiYue");
                        startActivityForResult(intent, 0);
                    } else {
                        transActMainListener.toPersonPage();
                        showForwardsDialog("您还没有进行视频认证，认证后才能发布活动", 2);
                    }
                }else {
                    Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.serviceView:
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    updateOptionsViews(0);
                } else {
                    List<JinPaiBigTypeBean> vector = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                    if (vector != null) {
                        if (vector.size() > 0) {
                            List<JinPaiBigTypeBean> temp = new ArrayList<>();
                            temp.addAll(vector);
                            showServicePOP(temp);
                            updateOptionsViews(1);
                        } else {
                            Toast.makeText(getActivity(), "加载中，稍后重试", Toast.LENGTH_SHORT).show();
                            requestActType();
                        }
                    } else {
                        Toast.makeText(getActivity(), "加载中，稍后重试", Toast.LENGTH_SHORT).show();
                        requestActType();
                    }

                }
                break;
            case R.id.sexView:
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    updateOptionsViews(0);
                } else {
                    showCommenPOP("sex");
                    updateOptionsViews(2);
                }
                break;
            case R.id.autoView:
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    updateOptionsViews(0);
                } else {
                    showCommenPOP("auto");
                    updateOptionsViews(3);
                }
                break;
            case R.id.provinceButton:
                Intent intent=new Intent(getActivity(), PlaceChoiceMainActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.rightButton:
                boolean islogin1 = UserPreferencesUtil.isOnline(getActivity());
                if (!islogin1) {
                    Intent intent11 = new Intent(getActivity(), LoginWelcomActivity.class);
                    startActivity(intent11);
                }else{
                    Intent intent7 = new Intent(getActivity(), NewsFragment.class);
                    getActivity().startActivity(intent7);
                }
                break;
            case R.id.nuReadNews:
                boolean islogin2 = UserPreferencesUtil.isOnline(getActivity());
                if (!islogin2) {
                    Intent intent11 = new Intent(getActivity(), LoginWelcomActivity.class);
                    startActivity(intent11);
                }else{
                    Intent intent8 = new Intent(getActivity(), NewsFragment.class);
                    getActivity().startActivity(intent8);
                }
                break;
        }
    }

    private void showForwardsDialog(String content,final int flag){
        OneButtonDialog dialog = new OneButtonDialog(getActivity(), content,"去认证", R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        actForwardsAct(flag);
                    }
                });
        dialog.show();
    }

    private void actForwardsAct(int index){
        switch (index){
            case 1:
                String idStatus=UserPreferencesUtil.getUserIDAuth(getActivity());
                if (idStatus!=null){
                    if ("0".equals(idStatus)){
                        getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                    }else if ("1".equals(idStatus)){
                        showOneButtonDialog("您的身份认证审核中，请耐心等待");
                    }else if ("4".equals(idStatus)){
                        showOneButtonDialog("您的身份认证审核中，请耐心等待");
                    }else if ("2".equals(idStatus)){
                        showOneButtonDialog("恭喜您，您的身份认证已通过");
                    }else if ("3".equals(idStatus)){
                        showTwoButtonDialog("抱歉！您的身份认证被拒绝，原因：".concat(UserPreferencesUtil.getUserIDAuthRefuseReason(getActivity())), "下次再说", "前往认证");
                    }
                }else{
                    getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                }
                break;
            case 2:
                String videoStatus=UserPreferencesUtil.getUserVideoAuth(getActivity());
                if (videoStatus!=null){
                    if ("0".equals(videoStatus)){
                        Intent intent12 = new Intent(getActivity(), VideoCertifyActivity.class);
                        startActivity(intent12);
                    }else if ("1".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else if ("4".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else if ("2".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else if ("3".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }
                }else{
                    Intent intent13 = new Intent(getActivity(), VideoCertifyActivity.class);
                    startActivity(intent13);
                }
                break;
        }
    }

    private void showTwoButtonDialog(String content, String leftText, String rightText) {
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(), content, leftText, rightText, R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                    }
                });
        dialog.show();
    }

    private void showOneButtonDialog(String content){
        OneButtonDialog dialog = new OneButtonDialog(getActivity(), content, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                    }
                });
        dialog.show();
    }

    private void showServicePOP(List<JinPaiBigTypeBean> vector) {
        JinPaiBigTypeBean firstItem = new JinPaiBigTypeBean();
        firstItem.setId("-1");
        firstItem.setName("全部服务");
        vector.add(0, firstItem);
        final PopServiceParentAdater poarentTypeAdater;
        mPopWindow = initPopWindow(getActivity(),
                R.layout.pop_list_layout, true);
        View root = mPopWindow.getContentView();
        root.getBackground().setAlpha(50);

        ListView popListView = (ListView) root.findViewById(R.id.listView);
        View bottomNullView = root.findViewById(R.id.bottomNullView);

        poarentTypeAdater = new PopServiceParentAdater(getActivity(), vector);
        popListView.setAdapter(poarentTypeAdater);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                bigTypeBean = poarentTypeAdater.getItem(arg2);
                serviceText.setText(bigTypeBean.getName());
                hotIsLoading = true;
                hotPage = 1;
                isJustBigType = false;
                loadJinPaiDatas();
                updateOptionsViews(0);
                mPopWindow.dismiss();
            }
        });

        bottomNullView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                updateOptionsViews(0);
                mPopWindow.dismiss();
            }
        });

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
                updateOptionsViews(0);
            }
        });


        if (mPopWindow.isShowing()) {
            updateOptionsViews(0);
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(getView().findViewById(R.id.optionsView));
        }
    }

    private void showCommenPOP(final String flag) {
        final PopSexAdater sexAdapter = new PopSexAdater(getActivity(), null);
        ;
        final PopAutoAdater autoAdapter = new PopAutoAdater(getActivity(), null);
        mPopWindow = initPopWindow(getActivity(),
                R.layout.pop_list_layout, true);
        View root = mPopWindow.getContentView();
        root.getBackground().setAlpha(50);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
                updateOptionsViews(0);
            }
        });
        ListView popListView = (ListView) root.findViewById(R.id.listView);
        View bottomNullView = root.findViewById(R.id.bottomNullView);

        if ("sex".equals(flag)) {
            popListView.setAdapter(sexAdapter);
            sexAdapter.updateDatas(sexDatas);
        } else {
            popListView.setAdapter(autoAdapter);
            autoAdapter.updateDatas(autoDatas);
        }

        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("sex".equals(flag)) {
                    PopSexBean item = sexAdapter.getItem(position);
                    conditionBean.setSexBean(item);
                    sexText.setText(item.getSex());
                } else {
                    PopAutoBean item = autoAdapter.getItem(position);
                    conditionBean.setAutoBean(item);
                    autoText.setText(item.getName());
                }
                hotIsLoading = true;
                hotPage = 1;
                isJustBigType = false;
                loadJinPaiDatas();
                updateOptionsViews(0);
                mPopWindow.dismiss();
            }
        });

        bottomNullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOptionsViews(0);
                mPopWindow.dismiss();
            }
        });

        if (mPopWindow.isShowing()) {
            updateOptionsViews(0);
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(getView().findViewById(R.id.optionsView));
        }
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
            updateOptionsViews(0);
            mPopWin.dismiss();
            mPopWin = null;
        }
        return mPopWin;
    }

    public void requestActType() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(getActivity(), true);
        item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_SERVER_TYPE:
                if ("0000".equals(data.getCode())) {
                    loadJinPaiDatas();
                    parseDatas(data.getRecord());
                }
                break;
            case ConstTaskTag.QUERY_NORMAL_ACT_LIST:
                jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                if (jinPaiBigTypeBeans!=null&&typeAdapter.getCount()==0){
                    typeAdapter.updateJinPaiDatas(jinPaiBigTypeBeans);
                }
                listView.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                List<JinPaiBigTypeBean> fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiBigTypeBean item = new JinPaiBigTypeBean();
                    item.setIsSelect(false);
                    item.setName(StringUtils.getJsonValue(object, "typeName"));
                    item.setId(StringUtils.getJsonValue(object, "typeId"));
                    item.setSubStr(StringUtils.getJsonValue(object, "titles"));
                    item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
                    item.setCategoryName(StringUtils.getJsonValue(object, "categoryName"));
                    if ("900".equals(item.getId())){
                        JinPaiBigTypeBean subItem = new JinPaiBigTypeBean();
                        subItem.setCategoryName(item.getCategoryName());
                        subItem.setId(Constants.DEFINE_LOL_ID);
                        subItem.setSubStr(item.getSubStr());
                        subItem.setUrl(item.getUrl());
                        subItem.setName("LOL");
                        fuFeiDatas.add(subItem);
                    }
                    fuFeiDatas.add(item);
                }
                CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, fuFeiDatas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 2) {
            JinPaiBean item=adapter.getItem(position - 2);
            String actId=item.getActId();
            Intent intent=new Intent();
            intent.setClass(getActivity(), JinPaiFuFeiDetailActivity.class);
            intent.putExtra("actId", actId);
            intent.putExtra("userId", item.getUserId());
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        if (jinPaiBigTypeBeans!=null&&typeAdapter.getCount()==0){
            typeAdapter.updateJinPaiDatas(jinPaiBigTypeBeans);
        }else{
            requestActType();
        }
        hotIsLoading = true;
        hotPage = 1;
        loadJinPaiDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (hotIsLoading) {
            hotPage = hotPage + 1;
            loadJinPaiDatas();
        } else {
            falseDatasModel();
        }
    }

    private void falseDatasModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message m = handler.obtainMessage();
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    listView.onRefreshComplete();
                    Toast.makeText(getActivity(), "已全部加载", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    private void updateOptionsViews(Integer index) {

        switch (index) {
            case 0:
                serviceImage.setImageResource(R.drawable.down_gray);
                sexImage.setImageResource(R.drawable.down_gray);
                autoImage.setImageResource(R.drawable.down_gray);

                serviceText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                autoText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            case 1:
                serviceImage.setImageResource(R.drawable.down_green);
                sexImage.setImageResource(R.drawable.down_gray);
                autoImage.setImageResource(R.drawable.down_gray);

                serviceText.setTextColor(getResources().getColor(R.color.dark_green));
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                autoText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            case 2:
                serviceImage.setImageResource(R.drawable.down_gray);
                sexImage.setImageResource(R.drawable.down_green);
                autoImage.setImageResource(R.drawable.down_gray);

                serviceText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                sexText.setTextColor(getResources().getColor(R.color.dark_green));
                autoText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            case 3:
                serviceImage.setImageResource(R.drawable.down_gray);
                sexImage.setImageResource(R.drawable.down_gray);
                autoImage.setImageResource(R.drawable.down_green);

                serviceText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                autoText.setTextColor(getResources().getColor(R.color.dark_green));
                break;
            default:
                break;
        }
    }

    public void setLoadFlag(boolean b) {
        this.isJustBigType=b;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.COMEBACK,false);
                if (result) {
                    Intent intent11 = new Intent(getActivity(), ActManagerActivity.class);
                    startActivity(intent11);
                    transActMainListener.toPersonPage();
                    hotIsLoading = true;
                    hotPage = 1;
                    loadJinPaiDatas();
                }
                break;
            }
            case 1:
                areaBean=(ProvinceBean)data.getSerializableExtra(Constants.COMEBACK);
                cityText.setText(areaBean.getProvinceName());
                CacheService.getInstance().cacheJPProvinceBean("JPProvinceBean", areaBean);
                hotIsLoading = true;
                hotPage = 1;
                loadJinPaiDatas();
                break;
            case 2:
                boolean result = data.getBooleanExtra(Constants.COMEBACK,false);
                String isClose=data.getStringExtra("isClose");
                if (result) {
                    transActMainListener.toPersonPage();
                    hotIsLoading = true;
                    hotPage = 1;
                    loadJinPaiDatas();
                }
                if ("close".equals(isClose)){
                    hotIsLoading = true;
                    hotPage = 1;
                    loadJinPaiDatas();
                }
                break;
            case 3:
                boolean result1 = data.getBooleanExtra(Constants.COMEBACK,false);
                if (result1) {
                    hotIsLoading = true;
                    hotPage = 1;
                    loadJinPaiDatas();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())){
                boolean newFlag=intent.getBooleanExtra("newsMessage",false);
                if (newFlag){
                    quaryUnReadNews();
                }
            }else if ("com.xygame.push.dynamic.message.list.action".equals(intent.getAction())){
                quaryUnReadNews();
            }
        }
    };

    public void registerLoginListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_ACTION);
        myIntentFilter.addAction("com.xygame.push.dynamic.message.list.action");
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterLoginListener();
    }

    private void quaryUnReadNews() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            int totalCount = 0;
//            int pushCount= PushEngine.getUnreadCount(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int chatNewsCount = NewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int daymicNewsCount = NewsEngine.quaryUnReadDaymicNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int discNewsCount = TempGroupNewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            totalCount = chatNewsCount + daymicNewsCount+discNewsCount;
            if (totalCount != 0) {
                nuReadNews.setVisibility(View.VISIBLE);
                nuReadNews.setText(String.valueOf(totalCount));
            } else {
                nuReadNews.setVisibility(View.GONE);
            }
        }else{
            nuReadNews.setVisibility(View.GONE);
        }
    }
}

package com.xygame.sg.activity.main;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.SearchJJRNoticeActivity;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.PlushJJRNoticeActivity;
import com.xygame.sg.activity.notice.PlushNoticeActivity;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.JJRIdentyFirstActivity;
import com.xygame.sg.activity.personal.TabEntity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.define.view.ChoiceNoticeView;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.IndexViewPager;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.broadcast.EditorUserInfoBroadcastListener;

import java.util.ArrayList;
import java.util.List;


public class NewCMFragment extends SGBaseFragment implements View.OnClickListener{
    private View new_notice_rl;
    private CommonTabLayout tab_l;
    private ImageView search_iv;
    private String[] titles = {"摄影师", "经纪人"};
    private int[] iconUnselectIds = {
            R.drawable.tab_works_unselect, R.drawable.tab_price_unselect};
    private int[] iconSelectIds = {
            R.drawable.tab_works_select, R.drawable.tab_price_select};
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();
    private IndexViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SlidingPagerAdapter adapter;
    private int pageIndex=0;

    private NoticeSYFragment sysNoticeFrame;
    private JJRNoticeFragment jjrNoticeFrame;

    public NewCMFragment() {
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
        addListener();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_fragment_cm, null);
    }

    private void initViews() {
        new_notice_rl = getView().findViewById(R.id.new_notice_rl);
        search_iv = (ImageView) getView().findViewById(R.id.search_iv);
        tab_l = (CommonTabLayout) getView().findViewById(R.id.tab_l);
        viewPager = (IndexViewPager) getView().findViewById(R.id.pager);
    }

    private void initDatas() {
        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
        sysNoticeFrame=new NoticeSYFragment();
        jjrNoticeFrame=new JJRNoticeFragment();
        fragments.add(sysNoticeFrame);
        fragments.add(jjrNoticeFrame);

        adapter = new SlidingPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setScanScroll(false);
        viewPager.setAdapter(adapter);
        if (UserPreferencesUtil.isOnline(getActivity()) ){
            if ((UserPreferencesUtil.getUserType(getActivity()).equals(Constants.CARRE_MODEL) || UserPreferencesUtil.getUserType(getActivity()).equals(Constants.PRO_MODEL))) {
                new_notice_rl.setVisibility(View.INVISIBLE);
            } else if (Constants.CARRE_MERCHANT.equals(UserPreferencesUtil.getUserType(getActivity()))){
                new_notice_rl.setVisibility(View.VISIBLE);
            } else {
                new_notice_rl.setVisibility(View.VISIBLE);
            }
        }else{
            new_notice_rl.setVisibility(View.VISIBLE);
        }
    }

    private void addListener() {
        search_iv.setOnClickListener(this);
        new_notice_rl.setOnClickListener(this);
        EditorUserInfoBroadcastListener.registerEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        tab_l.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                pageIndex=position;
                viewPager.setCurrentItem(position);
                if (position==0){
                    if (sysNoticeFrame.adapterCout()==0){
                        sysNoticeFrame.queryDatas();
                    }
                }else{
                    if (jjrNoticeFrame.adapterCout()==0){
                        jjrNoticeFrame.queryDatas();
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_EDITOR_USER_INFO.equals(intent.getAction())) {

            } else if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                if (UserPreferencesUtil.isOnline(getActivity())) {
                    if (Constants.CARRE_PHOTOR.equals(UserPreferencesUtil.getUserType(getActivity()))) {
                        new_notice_rl.setVisibility(View.VISIBLE);
                    } else if (Constants.CARRE_MERCHANT.equals(UserPreferencesUtil.getUserType(getActivity()))){
                        new_notice_rl.setVisibility(View.VISIBLE);
                    }else {
                        new_notice_rl.setVisibility(View.INVISIBLE);
                    }
                }else{
                    new_notice_rl.setVisibility(View.VISIBLE);
                }
            }else  if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                new_notice_rl.setVisibility(View.VISIBLE);
            }
        }
    };

    public void runRequest() {
        if (sysNoticeFrame.adapterCout()==0){
            sysNoticeFrame.queryDatas();
        }
    }

    private class SlidingPagerAdapter extends FragmentPagerAdapter {
        public SlidingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv:
                if (pageIndex==0){
                    search();
                }else{
                    Intent intent=new Intent(getActivity(),SearchJJRNoticeActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.new_notice_rl:
                addNotice();
                break;
        }
    }

    private void search() {
        Intent intent = new Intent(getActivity(), SearchUserActivity.class);
        startActivity(intent);
    }

    private void addNotice() {
        if (UserPreferencesUtil.isOnline(getActivity()) && Constants.CARRE_PHOTOR.equals(UserPreferencesUtil.getUserType(getActivity()))) {
            Intent intent = new Intent(getActivity(), ChoiceNoticeView.class);
            startActivityForResult(intent, 0);
        } else if (UserPreferencesUtil.isOnline(getActivity()) && Constants.CARRE_MERCHANT.equals(UserPreferencesUtil.getUserType(getActivity()))) {
            isPlushJJRNotice();
        }else{
            Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
            startActivity(intent);
        }
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
            }
        }
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
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(), tip,"好，去认证","暂时不认证", R.style.dineDialog, new ButtonTwoListener() {

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

    @Override
    public void onDestroy() {
        EditorUserInfoBroadcastListener.unRegisterEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        super.onDestroy();
    }

    private void isPlushJJRNotice(){
//        String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(getActivity());
//        if (!StringUtils.isEmpty(jsonStr)){
//            List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//            judge(resultList);
//        }
    }

    private void judge( List<IdentyBean> resultList) {
        if (resultList == null || resultList.size() == 0) {
            return;
        }
        IdentyBean identyBean = resultList.get(0);
        int authStatus = identyBean.getAuthStatus();
        int userType = identyBean.getUserType();
        String refuseReason = identyBean.getRefuseReason();
        if (resultList.size() == 1) {
            switch (userType) {
                case 16:
                    switch (authStatus) {
                        case 0:
                            showJJRDialog("您的经纪人认证还未认证，请先认证等审核后再发布！");
                            break;
                        case 1:
                            showOneButtonDialog("您的经纪人认证正在审核中！");
                            break;
                        case 2:
                            Intent intent = new Intent(getActivity(), PlushJJRNoticeActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            showJJRDialog("您的经纪人认证未通过，请先重新认证！");
                            break;
                    }
                    break;
            }
        }
    }

    private void showJJRDialog(String tip) {
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(), tip,"好，去认证","暂时不认证", R.style.dineDialog, new ButtonTwoListener() {

            @Override
            public void confrimListener() {
                getActivity().startActivity(new Intent(getActivity(), JJRIdentyFirstActivity.class));
            }

            @Override
            public void cancelListener() {
            }
        });
        dialog.show();
    }
}

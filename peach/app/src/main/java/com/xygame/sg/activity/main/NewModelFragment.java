package com.xygame.sg.activity.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.model.ModelAllFragment;
import com.xygame.sg.activity.model.ModelHotFragment;
import com.xygame.sg.activity.model.bean.AllModelReqBean;
import com.xygame.sg.activity.personal.TabEntity;
import com.xygame.sg.utils.IndexViewPager;
import com.xygame.sg.utils.SGApplication;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.frame.ParentFragment;
import base.frame.VisitUnit;

public class NewModelFragment extends ParentFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private VisitUnit unit = new VisitUnit();
    private CommonTabLayout tab_l;
    private ImageView search_iv;
    private String[] titles = {"热门", "全部"};
    private int[] iconUnselectIds = {
            R.drawable.tab_works_unselect, R.drawable.tab_price_unselect};
    private int[] iconSelectIds = {
            R.drawable.tab_works_select, R.drawable.tab_price_select};
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();
    private CheckBox switch_show_cb;
    private IndexViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SlidingPagerAdapter adapter;
    public static final int SHOW_AS_LISTVIEW = 1;
    public static final int SHOW_AS_GRIDVIEW = 2;
    public static final int FILTER_MODEL_REQUEST = 3;

    public NewModelFragment() {
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
        switch_show_cb = (CheckBox) getView().findViewById(R.id.switch_show_cb);
        search_iv = (ImageView) getView().findViewById(R.id.search_iv);
        tab_l = (CommonTabLayout) getView().findViewById(R.id.tab_l);
        viewPager = (IndexViewPager) getView().findViewById(R.id.pager);
    }

    private void initDatas() {
        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
        fragments.add(new ModelHotFragment());
//        fragments.add(new ModelAllFragment());

        adapter = new SlidingPagerAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setScanScroll(false);
        viewPager.setAdapter(adapter);

    }

    private void addListener() {
        tab_l.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    switch_show_cb.setVisibility(View.GONE);
                } else {
                    switch_show_cb.setVisibility(View.VISIBLE);
                }
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        switch_show_cb.setOnCheckedChangeListener(this);
        search_iv.setOnClickListener(this);
    }

    private void requestData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new ViewBinder(container.getContext(), unit.setDisplay(this)).inflate(R.layout.fragment_new_model, null);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        ModelAllFragment fragment = (ModelAllFragment) fragments.get(1);
//        if (b) {
//            //显示gridview
//            fragment.switchShowView(SHOW_AS_GRIDVIEW);
//        } else {
//            //显示listview
//            fragment.switchShowView(SHOW_AS_LISTVIEW);
//        }
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
                search();
                break;
        }
    }

    private void search() {
        if (SGApplication.getInstance().getTypeList()!=null) {
            Intent intent = new Intent(getActivity(), SearchUserActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FILTER_MODEL_REQUEST:
                    AllModelReqBean allModelReqBean = (AllModelReqBean) data.getSerializableExtra("requestBean");
//                    ModelAllFragment fragment = (ModelAllFragment) fragments.get(1);
//                    fragment.toFilter(allModelReqBean);
                    break;
            }
        }
    }


}

package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.fragment.AttentionCMFragment;
import com.xygame.sg.activity.personal.fragment.AttentionModelFragment;
import com.xygame.sg.activity.personal.fragment.RecentCMFragment;
import com.xygame.sg.activity.personal.fragment.RecentModelFragment;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.frame.VisitUnit;

public class RecentVisitActivity extends SGBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private TextView titleName;
    private View backButton;

    private ViewPager viewPager;
    private CommonTabLayout tab_l;
    private String[] titles = {"模特", "摄影师"};
    private int[] iconUnselectIds = {
            R.drawable.tab_works_unselect, R.drawable.tab_price_unselect,};
    private int[] iconSelectIds = {
            R.drawable.tab_works_select, R.drawable.tab_price_select};
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private SlidingPagerAdapter adapter;

    private long lastReadTime;
    private long reqTime;

    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();
    VisitUnit visitUnit = new VisitUnit(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_recent_visit, null));

        initViews();
        initData();
        initListeners();

    }

    private void initViews() {
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
        titleName.setText(getResources().getString(R.string.title_activity_recent_visit));
        tab_l = (CommonTabLayout) findViewById(R.id.tab_l);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }
    private void initData() {
        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
        tab_l.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        fragments.add(new RecentModelFragment());
        fragments.add(new RecentCMFragment());
        adapter = new SlidingPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }
    private void initListeners() {
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }
    }

    //===================implements ViewPager.OnPageChangeListener======begin
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tab_l.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //===================implements ViewPager.OnPageChangeListener======end
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

    public long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }
}

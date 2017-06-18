package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.fragment.LikeListFragment;
import com.xygame.sg.activity.personal.fragment.MyAttentionFragment;

import java.util.ArrayList;


/**
 * 点赞用户页面
 */
public class LikeListActivity extends SGBaseActivity {
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private CommonTabLayout tab_l;
    private String[] titles = {"全部", "我关注的人"};
    private int[] iconUnselectIds = {
            R.drawable.tab_works_unselect, R.drawable.tab_price_unselect};
    private int[] iconSelectIds = {
            R.drawable.tab_works_select, R.drawable.tab_price_select};
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);
        findViews();
        setupPager();
        setupTabs();
    }

    private void findViews() {
        tab_l = (CommonTabLayout) findViewById(R.id.like_tab_l);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }


    private void setupPager() {
        fragments.add(new LikeListFragment());
        fragments.add(new MyAttentionFragment());
        viewPager.setAdapter(new TabViewPagerAdapter(getSupportFragmentManager()));
    }

    private void setupTabs() {

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

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
    }

    private class TabViewPagerAdapter extends FragmentPagerAdapter {
        public TabViewPagerAdapter(FragmentManager fm) {
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

}

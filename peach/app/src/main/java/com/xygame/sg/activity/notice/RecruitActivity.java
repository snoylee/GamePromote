package com.xygame.sg.activity.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nineoldandroids.view.ViewHelper;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.RecruitSlidingPagerAdapter;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberGroupVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.notice.fragment.OnFragmentInteractionListener;
import com.xygame.sg.activity.personal.TabEntity;
import com.xygame.sg.activity.personal.fragment.ScrollTabHolder;
import com.xygame.sg.task.notice.QueryNoticeDeatail;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.broadcast.EditorUserInfoBroadcastListener;
import com.xygame.sg.widget.LinearLayoutForListView;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class RecruitActivity extends SGBaseActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener, View.OnClickListener, OnFragmentInteractionListener {
    private View backButton;
    private TextView titleName;

    private LinearLayout header;
    private boolean hasMeasured = false;

    private int headerHeight;
    private int headerTranslationDis;
    private ViewPager viewPager;
    private RecruitSlidingPagerAdapter adapter;

    private CommonTabLayout tab_l;
    private String[] titles = {"报名", "待定", "淘汰", "录用"};
    private int[] iconUnselectIds = {
            R.drawable.tab_works_unselect, R.drawable.tab_price_unselect,
            R.drawable.tab_data_unselect, R.drawable.tab_data_unselect};
    private int[] iconSelectIds = {
            R.drawable.tab_works_select, R.drawable.tab_price_select,
            R.drawable.tab_data_select, R.drawable.tab_data_select};

    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();

    public static final int ENROLL_FRAGMENT = 0;
    public static final int TBD_FRAGMENT = 1;
    public static final int ELIMINATE_FRAGMENT = 2;
    public static final int EMPLOY_FRAGMENT = 3;
    public static final int ENROLL_STATUS = 1;//报名
    public static final int TBD_STATUS = 2;//待定
    public static final int ELIMINATE_STATUS = 3;//淘汰
    public static final int EMPLOY_STATUS = 4;//录用
    public static final int REMOVE_STATUS = 5;//解除录用

    VisitUnit visitUnit = new VisitUnit();
    private boolean isQuery = false;

    private NoticeRecruitVo noticeRecruitListVo;
    private int position = 0;
    private String noticeId;
    private String recruitId;
    private String titleStrId;
    private NoticeDetailVo notice;
//    private int isClose;//1:已关闭；0：未关闭

    private LinearLayout notice_request_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_recruit, null));
        noticeId = getIntent().getStringExtra("noticeId");
        recruitId = getIntent().getStringExtra("recruitId");
        position = getIntent().getIntExtra("position", 1);

        findView();
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ENROLL_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_RECRUIT_CHANGE);
        myIntentFilter.addAction(Constants.BROADCAST_ACTION_PAYMENT_FOR_CHAT);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void findView() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);


        tab_l = (CommonTabLayout) findViewById(R.id.tab_l);
        viewPager = (ViewPager) findViewById(R.id.pager);
        header = (LinearLayout) findViewById(R.id.header);
        notice_request_ll = (LinearLayout) findViewById(R.id.notice_request_ll);
    }

    private void initViews() {
        //初始化上面的招募要求
        LinearLayoutForListView view = new LinearLayoutForListView(this,true);
        view.setOrientation(LinearLayout.VERTICAL);
        view.bindLinearLayout(noticeRecruitListVo);
        if (notice_request_ll.getChildCount() > 0){
            notice_request_ll.removeAllViews();
        }
        notice_request_ll.addView(view);

        ViewTreeObserver vto = header.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {
                    headerHeight = header.getMeasuredHeight();
                    hasMeasured = true;
                    initDatas();
                }
                return true;
            }
        });

    }

    private void initDatas() {
        headerTranslationDis = -(headerHeight - getResources().getDimensionPixelSize(R.dimen.recruit_tab_height));

        adapter = new RecruitSlidingPagerAdapter(getSupportFragmentManager(), this, viewPager, noticeRecruitListVo.getRecruitId(), noticeId);
        adapter.setTabHolderScrollingListener(this);//控制页面上滑
        viewPager.setOffscreenPageLimit(adapter.getCacheCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        if(tabEntities.size() > 0){
            tabEntities.clear();
        }
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
    }

    private void addListener() {
        backButton.setOnClickListener(this);
    }

    private void requestData() {
        visitUnit.getDataUnit().getRepo().put("noticeId", noticeId);
        visitUnit.getDataUnit().getRepo().put("curUserId", UserPreferencesUtil.getUserId(this));
        new Action(QueryNoticeDeatail.class, "${queryNoticeDetail},noticeId=${noticeId},curUserId=${curUserId}", this, null, visitUnit).run();
    }
    public void responseNotice(NoticeDetailVo resultNotice) {
        notice = resultNotice;
        List<NoticeRecruitVo> nrvDatas=notice.getRecruits();//recruitId
        for(int i=0;i<nrvDatas.size();i++){
            if(recruitId.equals(String.valueOf(nrvDatas.get(i).getRecruitId()))){
                noticeRecruitListVo = notice.getRecruits().get(i);
                break;
            }
        }
        titleStrId = "招募"+ StringUtils.transition(noticeRecruitListVo.getLocIndex()+"");
        titleName.setText(titleStrId);
        noticeId = notice.getNoticeId()+"";
        initViews();
        addListener();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tab_l.setCurrentTab(position);
        reLocation = true;
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        if (NEED_RELAYOUT) {
            currentHolder.adjustScroll(header.getHeight() + headerTop);// 修正滚出去的偏移量
        } else {
            currentHolder.adjustScroll((int) (header.getHeight() + ViewHelper.getTranslationY(header)));// 修正滚出去的偏移量
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    private boolean reLocation = false;

    private int headerScrollSize = 0;

    public static final boolean NEED_RELAYOUT = Integer.valueOf(Build.VERSION.SDK).intValue() < Build.VERSION_CODES.HONEYCOMB;

    private int headerTop = 0;

    // 刷新头部显示时，没有onScroll回调，只有当刷新时会有
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
                         int pagePosition) {
        if (viewPager.getCurrentItem() != pagePosition) {
            return;
        }
        if (headerScrollSize == 0 && reLocation) {
            reLocation = false;
            return;
        }
        reLocation = false;
        int scrollY = Math.max(-getScrollY(view), headerTranslationDis);
        if (NEED_RELAYOUT) {
            headerTop = scrollY;
            header.post(new Runnable() {
                @Override
                public void run() {
                    header.layout(0, headerTop, header.getWidth(), headerTop + header.getHeight());
                }
            });
        } else {
            ViewHelper.setTranslationY(header, scrollY);
        }
    }

    /**
     * 主要算这玩意，PullToRefreshListView插入了一个刷新头部，因此要根据不同的情况计算当前的偏移量</br>
     * <p/>
     * 当刷新时： 刷新头部显示，因此偏移量要加上刷新头的数值 未刷新时： 偏移量不计算头部。
     * <p/>
     * firstVisiblePosition >1时，listview中的项开始显示，姑且认为每一项等高来计算偏移量（其实只要显示一个项，向上偏移
     * 量已经大于头部的最大偏移量，因此不准确也没有关系）
     *
     * @param view
     * @return
     */
    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int top = c.getTop();
        int firstVisiblePosition = view.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            return -top + headerScrollSize;
        } else if (firstVisiblePosition == 1) {
            return -top;
        } else {
            return -top + (firstVisiblePosition - 2) * c.getHeight() + headerHeight;
        }
    }

    // 与onHeadScroll互斥，不能同时执行
    @Override
    public void onHeaderScroll(boolean isRefreashing, int value, int pagePosition) {
        if (viewPager.getCurrentItem() != pagePosition) {
            return;
        }
        headerScrollSize = value;
        if (NEED_RELAYOUT) {
            header.post(new Runnable() {
                @Override
                public void run() {
                    header.layout(0, -headerScrollSize, header.getWidth(), -headerScrollSize + header.getHeight());
                }
            });
        } else {
            ViewHelper.setTranslationY(header, -value);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;

        }
    }


    @Override
	public void onDestroy() {
        super.onDestroy();
        EditorUserInfoBroadcastListener.unRegisterEditorUserInfoListener(this, mBroadcastReceiver);
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           if (Constants.ACTION_RECRUIT_CHANGE.equals(intent.getAction())) {
                requestData();
           } else if (Constants.BROADCAST_ACTION_PAYMENT_FOR_CHAT.equals(intent.getAction())){
               notice.setPayStatus(3);
               adapter.notifyDataSetChanged();
           }
        }
    };

    public int getHeaderHeight() {
        return headerHeight;
    }


    public NoticeDetailVo getNotice() {
        return notice;
    }

    public NoticeRecruitVo getNoticeRecruitListVo() {
        return noticeRecruitListVo;
    }

    @Override
    public void onFragmentInteraction(int fragmentId, List<NoticeMemberGroupVo> groups) {
        List<Integer> statusList = new ArrayList<Integer>();
        List<Integer> noStatusList = new ArrayList<Integer>();
        for (int i = 0; i < groups.size(); i++) {
            NoticeMemberGroupVo noticeMemberGroupVo = groups.get(i);
            int sum = noticeMemberGroupVo.getSum();
            switch (noticeMemberGroupVo.getStatus()) {
                case 1:
                    if (sum > 0) {
                        titles[0] = "报名(" + sum + ")";
                    } else {
                        titles[0] = "报名";
                    }
                    statusList.add(1);
                    break;
                case 2:
                    if (sum > 0) {
                        titles[1] = "待定(" + sum + ")";
                    } else {
                        titles[1] = "待定";
                    }
                    statusList.add(2);
                    break;
                case 3:
                    if (sum > 0) {
                        titles[2] = "淘汰(" + sum + ")";
                    } else {
                        titles[2] = "淘汰";
                    }
                    statusList.add(3);
                    break;
                case 4:
                    if (sum > 0) {
                        titles[3] = "录用(" + sum + ")";
                    } else {
                        titles[3] = "录用";
                    }
                    statusList.add(4);
                    break;
            }
        }
        for (int i = 0;i<4;i++){
            if (!statusList.contains(i+1)){
                noStatusList.add(i+1);
            }
        }

        for (int i=0;i<noStatusList.size();i++){
            switch (noStatusList.get(i)) {
                case 1:
                    titles[0] = "报名";
                    break;
                case 2:
                    titles[1] = "待定";
                    break;
                case 3:
                    titles[2] = "淘汰";
                    break;
                case 4:
                    titles[3] = "录用";
                    break;
            }
        }
        tabEntities.clear();
        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}

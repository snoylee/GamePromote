package com.xygame.second.sg.xiadan.activity.godlist;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.GodListListener;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2017/2/27.
 */
public class GodListChoice extends SGBaseActivity implements View.OnClickListener, SlideView.OnSlideListener, GodListListener {

    private TextView titleName;
    private View backButton;
    private SlideView mLastSlideViewWithStatusOn;
    private SlideAdapter adapter;
    private ListViewCompat mListView;
    private BlackMemberBean tempActionBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_listchoice_layout);
        initViews();
        initListeners();
        initDatas();
    }


    private void initViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        //实例化汉字转拼音类
        titleName.setText("黑名单");
        mListView = (ListViewCompat) findViewById(R.id.list);
        adapter = new SlideAdapter(this, null);
        adapter.addCancelBlackListListener(this);
        adapter.addSlidViewListener(this);
        mListView.setAdapter(adapter);
        List<BlackMemberBean> vector=new ArrayList<>();
       for (int i=0;i<10;i++){
           BlackMemberBean blackMemberBean=new BlackMemberBean();
           blackMemberBean.setUsernick("Hello");
           blackMemberBean.setAge("23");
           blackMemberBean.setGender("1");
           blackMemberBean.setOrderNums("3");
           blackMemberBean.setUserId("35241658461");
           vector.add(blackMemberBean);
       }
        adapter.addDatas(vector);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    @Override
    public void cancelBlackListListener(BlackMemberBean blackMemberBean) {
        this.tempActionBean = blackMemberBean;
        Toast.makeText(this, "kicked merge", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void playGodVoice(BlackMemberBean blackMemberBean) {
        Toast.makeText(this, "playVoice", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void playGodVideo(BlackMemberBean blackMemberBean) {
        Toast.makeText(this, "playVideo", Toast.LENGTH_SHORT).show();
    }
}

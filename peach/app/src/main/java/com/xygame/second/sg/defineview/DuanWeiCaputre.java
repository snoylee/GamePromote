/*
 * 文 件 名:  PickPhotoesView.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月12日
 */
package com.xygame.second.sg.defineview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xygame.second.sg.jinpai.bean.DuanWeiBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月12日
 * @action [选择拍照或相册的界面]
 */
public class DuanWeiCaputre extends SGBaseActivity implements OnClickListener {

    private WheelView yearWv;
    private String[] years= Constants.DUAN_WEI;
    private DuanWeiBean ftBean;
    private Button mBtnConfirm, btn_cancel;
    private String curYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_duanwei);
        initView();
        initDatas();
        initListers();
        yearWv.setWheelBackground(R.color.white);
    }

    private void initView() {
        yearWv = (WheelView) findViewById(R.id.yearWv);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void initListers() {
        btn_cancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        yearWv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                curYear = years[yearWv.getCurrentItem()];
                ftBean.setName(curYear);
                ftBean.setId(String.valueOf(yearWv.getCurrentItem()+1));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            if (ftBean != null) {
                transBeanSelect();
            }
        } else if (v.getId() == R.id.btn_cancel) {
            finish();
        }
    }

    protected void transBeanSelect() {
        Intent intent = new Intent();
        intent.putExtra("bean", ftBean);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void initDatas() {
        ftBean = new DuanWeiBean();
        yearWv.setViewAdapter(new ArrayWheelAdapter<String>(this, years));
        // 设置可见条目数量
        yearWv.setVisibleItems(7);
        yearWv.setCurrentItem(0);
        curYear = years[yearWv.getCurrentItem()];
        ftBean.setName(curYear);
        ftBean.setId(String.valueOf(yearWv.getCurrentItem() + 1));
    }
}

package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.TransferGift;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

public class SingleWheelView extends SGBaseActivity implements OnClickListener {
    private TextView choiceTitle;
    private WheelView wheelValue;
    private String[] whellStr;
    private String finalStr;
    private TransferGift transferGift;
    private Button mBtnConfirm, btn_cancel;
    private String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_sigle_wheel_layout);
        initView();
        initDatas();
        initListers();

    }

    private void initView() {
        choiceTitle = (TextView) findViewById(R.id.choiceTitle);
        wheelValue = (WheelView) findViewById(R.id.wheelValue1);
        wheelValue.setWheelBackground(R.color.white);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    private void initListers() {
        btn_cancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        wheelValue.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                finalStr = whellStr[wheelValue.getCurrentItem()];
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            if (finalStr != null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.SIGLE_WHEEL_BACK_VALUE, finalStr);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else if (v.getId() == R.id.btn_cancel) {
            finish();
        }
    }

    private void initDatas() {
        titleStr=getIntent().getStringExtra("titleStr");
        transferGift=(TransferGift)getIntent().getSerializableExtra("bean");
        whellStr=transferGift.getWhellStr();
        choiceTitle.setText(titleStr);
        wheelValue.setViewAdapter(new ArrayWheelAdapter<String>(this, whellStr));

        // 设置可见条目数量
        wheelValue.setVisibleItems(7);
        wheelValue.setCurrentItem(0);
        finalStr = whellStr[0];
    }
}

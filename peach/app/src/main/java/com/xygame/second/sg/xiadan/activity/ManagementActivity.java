package com.xygame.second.sg.xiadan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xygame.second.sg.comm.activity.ActManagerActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;

/**
 * Created by tony on 2016/12/23.
 */
public class ManagementActivity extends SGBaseActivity implements View.OnClickListener{

    private View intoJinPai,backButton,intoXiaDan;
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manag_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        intoJinPai=findViewById(R.id.intoJinPai);
        intoXiaDan = findViewById(R.id.intoXiaDan);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
    }

    private void initDatas() {
        titleName.setText("活动管理");
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        intoJinPai.setOnClickListener(this);
        intoXiaDan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.intoJinPai:
                Intent intent11 = new Intent(this, ActManagerActivity.class);
                startActivity(intent11);
                break;
            case R.id.intoXiaDan:
                Intent intent1 = new Intent(this, XiaDanManageActivity.class);
                startActivity(intent1);
                break;
        }
    }
}

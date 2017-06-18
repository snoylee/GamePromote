package com.xygame.second.sg.jinpai.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xygame.second.sg.comm.activity.ActManagerActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;


public class JinPaiPlushYaoYueActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton, fufeiYue, jingPaiYue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_plush_yy_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        fufeiYue = findViewById(R.id.fufeiYue);
        jingPaiYue = findViewById(R.id.jingPaiYue);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
    }

    private void initDatas() {
        titleName.setText("发布邀约");
    }

    private void addListener() {
        jingPaiYue.setOnClickListener(this);
        fufeiYue.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.fufeiYue:
                Intent intent = new Intent(this, JinPaiPlushActivity.class);
                intent.putExtra("wichFlag", "fufeiYue");
                startActivityForResult(intent, 0);
                break;
            case R.id.jingPaiYue:
                Intent intent1 = new Intent(this, JinPaiPlushActivity.class);
                intent1.putExtra("wichFlag", "jingpaiYue");
                startActivityForResult(intent1, 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
                if (result) {
                    Intent intent11 = new Intent(this, ActManagerActivity.class);
                    startActivity(intent11);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            }
            default:
                break;
        }
    }
}

package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.ViewBinder;
import base.frame.VisitUnit;

public class SettingSafeActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton;
    private RelativeLayout modify_password_rl;
    private TextView mobile_tv;

    private VisitUnit visitUnit = new VisitUnit(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_setting_safe, null));

        initViews();
        initListeners();
        initDatas();
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        modify_password_rl.setOnClickListener(this);
    }


    private void initViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        mobile_tv = (TextView) findViewById(R.id.mobile_tv);
        modify_password_rl = (RelativeLayout) findViewById(R.id.modify_password_rl);
    }


    private void initDatas() {
        titleName.setText(getResources().getString(R.string.title_activity_setting_safe));
        mobile_tv.setText(UserPreferencesUtil.getCellPhone(this));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.modify_password_rl) {
            Intent intent = new Intent(this, SettingModifyPassActivity.class);
            startActivity(intent);
        }
    }

}

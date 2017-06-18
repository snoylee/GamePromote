package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.TDevice;

import base.ViewBinder;
import base.frame.VisitUnit;

public class SettingAboutActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton,gotoShare;
    private RelativeLayout score_rl;
    private RelativeLayout who_rl;
    private TextView version_tv;

    VisitUnit visitUnit = new VisitUnit(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_setting_about, null));

        initViews();
        initListeners();
        initDatas();
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        score_rl.setOnClickListener(this);
        who_rl.setOnClickListener(this);
        gotoShare.setOnClickListener(this);
    }


    private void initViews() {
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
        gotoShare=findViewById(R.id.gotoShare);
        score_rl = (RelativeLayout)findViewById(R.id.score_rl);
        who_rl=(RelativeLayout)findViewById(R.id.who_rl);
        version_tv=(TextView)findViewById(R.id.version_tv);
    }


    private void initDatas() {
        titleName.setText(getResources().getString(R.string.title_activity_setting_about));
        version_tv.setText(TDevice.getCurrentVersionName(this));

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }else if(v.getId()==R.id.score_rl){
            Uri uri = Uri.parse("market://details?id="+getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(v.getId()==R.id.who_rl){
			Intent intent=new Intent(this, SettingWhoActivity.class);
			startActivity(intent);
        }else if (v.getId()==R.id.gotoShare){
            Intent intent = new Intent(this,ShareBoardView.class);
            intent.putExtra(Constants.SHARE_TITLE_KEY,"推荐你一个真实模特约拍的平台");
            intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"模范儿（moreidols）是一个高颜值专业摄影约拍平台，致力于连接摄影师和模特。");
            startActivity(intent);
        }
    }



}

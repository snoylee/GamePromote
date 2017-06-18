package com.xygame.sg.define.view;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.notice.NoticeCloseActivity;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.utils.Constants;

public class CloseShareOptionsView extends SGBaseActivity implements View.OnClickListener {
    private TextView share_tv;
    private TextView gray_divider_tv;
    private TextView close_tv;
    private TextView dismiss;
    private boolean isShowClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_share_options_view);
        isShowClose = getIntent().getBooleanExtra("isShowClose",false);
//        isShowClose = true;

        initView();
        addListener();
    }

    private void initView() {
        share_tv = (TextView)findViewById(R.id.share_tv);
        gray_divider_tv = (TextView)findViewById(R.id.gray_divider_tv);
        close_tv = (TextView)findViewById(R.id.close_tv);
        dismiss = (TextView)findViewById(R.id.dismiss);
        if (isShowClose){
            gray_divider_tv.setVisibility(View.VISIBLE);
            close_tv.setVisibility(View.VISIBLE);
            close_tv.setText("关闭通告");
        } else{
            gray_divider_tv.setVisibility(View.VISIBLE);
            close_tv.setVisibility(View.VISIBLE);
            close_tv.setText("通告已关闭");
        }
    }

    private void addListener() {
        share_tv.setOnClickListener(this);
        close_tv.setOnClickListener(this);
        dismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.share_tv:
                intent.putExtra(Constants.COMEBACK,"share");
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.close_tv:
                if (isShowClose){
                    intent.putExtra(Constants.COMEBACK,"close");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.dismiss:
                finish();
                break;
        }
    }
}

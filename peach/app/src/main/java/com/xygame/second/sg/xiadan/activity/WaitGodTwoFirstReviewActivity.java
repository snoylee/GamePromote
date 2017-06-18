package com.xygame.second.sg.xiadan.activity;

import android.os.Bundle;
import android.view.View;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;

/**
 * Created by tony on 2016/12/22.
 */
public class WaitGodTwoFirstReviewActivity extends SGBaseActivity implements View.OnClickListener {
    private View rootView,iKnownView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_god_two_firstview_layout);
        initViews();
        addListener();
    }

    private void initViews() {
        iKnownView=findViewById(R.id.iKnownView);
        rootView=findViewById(R.id.rootView);
//        rootView.getBackground().setAlpha(150);
    }

    private void addListener() {
        iKnownView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iKnownView:
               finish();
                break;
        }
    }
}
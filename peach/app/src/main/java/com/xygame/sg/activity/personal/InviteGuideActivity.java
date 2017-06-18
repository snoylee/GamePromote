package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.SGApplication;

public class InviteGuideActivity extends SGBaseActivity {
    private RelativeLayout root_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_guide);
        root_rl = (RelativeLayout) findViewById(R.id.root_rl);
        root_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}

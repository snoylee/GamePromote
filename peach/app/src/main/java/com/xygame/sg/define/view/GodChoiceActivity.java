package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

public class GodChoiceActivity extends SGBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_choice);
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "null");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.bofangView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "player");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.sanchuView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "delete");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

}

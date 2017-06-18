package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;

import base.ViewBinder;
import base.frame.VisitUnit;

public class TutorialActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton;
    VisitUnit visitUnit = new VisitUnit(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_tutorial, null));

        initViews();
        initListeners();

    }

    private void initViews() {
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
        titleName.setText(getResources().getString(R.string.title_activity_attention));

    }

    private void initListeners() {
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }
    }


}

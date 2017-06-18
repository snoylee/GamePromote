package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;

import base.frame.VisitUnit;

public class MemberCenterActivity extends SGBaseActivity implements  View.OnClickListener  {
    private View backButton;
    private TextView titleName;
    VisitUnit visitUnit = new VisitUnit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_center);
        initViews();
        initDatas();
        addListener();
        requestData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_member_center));


    }

    private void initDatas() {

    }

    private void addListener() {
        backButton.setOnClickListener(this);

    }


    private void requestData() {
//        visitUnit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(this));
//
//        if (editOrQueryFlag.equals(Constants.EDIT_INFO_FLAG)) {
//            new Action(QueryMyOverview.class, "${queryMyOverview},userId=${userId}", this, null, visitUnit).run();
//        } else if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {
//            visitUnit.getDataUnit().getRepo().put("seeUserId", queryedUserId);//要查看的用户id
//            new Action(GetModelData.class, "${modelData},seeUserId=${seeUserId},userId=${userId}", this, null, visitUnit).run();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }


}

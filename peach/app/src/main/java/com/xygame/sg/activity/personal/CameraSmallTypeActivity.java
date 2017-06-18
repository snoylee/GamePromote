package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.personal.adapter.SelectSecondCategoryListAdapter;
import com.xygame.sg.utils.Constants;

import java.util.List;

import base.ViewBinder;
import base.frame.VisitUnit;

public class CameraSmallTypeActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private ListView category_lv;
    private SelectSecondCategoryListAdapter adapter;
    private PlushNoticeBean pnBean;
    private List<ShootSubTypeBean> dateList;

    VisitUnit visitUnit = new VisitUnit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_select_first_category, null));
        pnBean = (PlushNoticeBean)getIntent().getSerializableExtra("selectFirstCategoryId");
        dateList=(List<ShootSubTypeBean>)getIntent().getSerializableExtra("selectedMap");
        initViews();
        addListener();
        initData();
    }

    private void initData() {
        adapter = new SelectSecondCategoryListAdapter(this,dateList,pnBean.getCameraTypeId());
        category_lv.setAdapter(adapter);
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getText(R.string.title_activity_select_second_category));
        category_lv = (ListView) findViewById(R.id.category_lv);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShootSubTypeBean selectedMap = dateList.get(i);
                adapter.setSelectedId(selectedMap.getTypeId()+"");
                adapter.notifyDataSetChanged();

                pnBean.setCameraTypeId(selectedMap.getTypeId()+"");
                pnBean.setCameraTypeName(selectedMap.getTypeName());
                Intent mIntent = new Intent();
                mIntent.putExtra(Constants.COMEBACK,pnBean);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
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

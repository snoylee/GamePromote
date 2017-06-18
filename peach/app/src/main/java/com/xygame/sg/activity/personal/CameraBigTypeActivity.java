package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.personal.adapter.SelectFirstCategoryListAdapter;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.frame.VisitUnit;

public class CameraBigTypeActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private ListView category_lv;
    private SelectFirstCategoryListAdapter adapter;
    private PlushNoticeBean pnBean;
    private List<ShootTypeBean> dateList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_first_category);
        pnBean = (PlushNoticeBean)getIntent().getSerializableExtra("selectFirstCategoryId");
        initViews();
        addListener();
        initData();
    }
    private void initData() {
        dateList = SGApplication.getInstance().getTypeList();
        adapter = new SelectFirstCategoryListAdapter(this,pnBean.getCameraParantTypeId());
        category_lv.setAdapter(adapter);
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("拍摄类型");//(getText(R.string.title_activity_select_first_category));
        category_lv = (ListView) findViewById(R.id.category_lv);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShootTypeBean selectedMap = dateList.get(i);
                adapter.setSelectFirstCategoryId(selectedMap.getTypeId() + "");
                adapter.notifyDataSetChanged();
                pnBean.setCameraParantTypeId(selectedMap.getTypeId() + "");
                pnBean.setCameraParentTypeName(selectedMap.getTypeName());
                Intent mIntent = new Intent();
                mIntent.putExtra(Constants.COMEBACK, pnBean);
                setResult(RESULT_OK, mIntent);
                finish();

//                List<ShootSubTypeBean> mapList = selectedMap.getSubTypes();
//                Intent firstIntent = new Intent(CameraBigTypeActivity.this, CameraSmallTypeActivity.class);
//                firstIntent.putExtra("selectFirstCategoryId", pnBean);
//                firstIntent.putExtra("selectedMap", (Serializable) mapList);
//                startActivityForResult(firstIntent, 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                PlushNoticeBean tempBean=(PlushNoticeBean) data.getSerializableExtra(Constants.COMEBACK);
                Intent mIntent = new Intent();
                mIntent.putExtra(Constants.COMEBACK, tempBean);
                setResult(RESULT_OK, mIntent);
                finish();
                break;
            }
            default:
                break;
        }
    }
}

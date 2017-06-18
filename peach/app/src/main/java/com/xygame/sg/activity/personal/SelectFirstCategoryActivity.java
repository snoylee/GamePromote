package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.personal.adapter.SelectFirstCategoryListAdapter;
import com.xygame.sg.utils.SGApplication;

public class SelectFirstCategoryActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private ListView category_lv;
    private SelectFirstCategoryListAdapter adapter;
    private String selectFirstCategoryId = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_first_category);
        selectFirstCategoryId = getIntent().getStringExtra("selectFirstCategoryId");
        initViews();
        addListener();
        initData();
    }



    private void initData() {
        adapter = new SelectFirstCategoryListAdapter(this,selectFirstCategoryId);
        category_lv.setAdapter(adapter);
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);

        titleName.setText(getText(R.string.title_activity_select_first_category));

        category_lv = (ListView) findViewById(R.id.category_lv);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShootTypeBean shootTypeBean = SGApplication.getInstance().getTypeList().get(i);
                adapter.setSelectFirstCategoryId(shootTypeBean.getTypeId()+"");
                adapter.notifyDataSetChanged();
                Intent mIntent = new Intent();
                mIntent.putExtra("selectedMap", shootTypeBean);
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

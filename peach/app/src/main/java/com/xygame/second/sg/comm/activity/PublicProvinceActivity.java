package com.xygame.second.sg.comm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.second.sg.comm.adapter.ProvinceMainAdater;
import com.xygame.second.sg.comm.adapter.PublicProvinceAdater;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicProvinceActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

    private View backButton;
    private TextView titleName;
//    private GridView gridview;
//    private PublicProvinceAdater adapter;
    private List<ProvinceBean> datas;
    private ProvinceBean areaBean;

    private ListView listView;
    private ProvinceMainAdater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_province_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        listView = (ListView) findViewById(R.id.listView);
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initDatas() {
        titleName.setText("选择地区");
        ShowMsgDialog.showNoMsg(this, false);
        ThreadPool.getInstance().excuseThread(new InitDatas());
    }

    private class InitDatas implements Runnable {
        @Override
        public void run() {
            datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
            datas.remove(0);
            Map<Integer, ProvinceBean> tempMap = new HashMap<>();
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).getName().contains("北京")) {
                    tempMap.put(1, datas.get(i));
                    datas.remove(i);
                } else if (datas.get(i).getName().contains("上海")) {
                    tempMap.put(2, datas.get(i));
                    datas.remove(i);
                } else if (datas.get(i).getName().contains("天津")) {
                    tempMap.put(3, datas.get(i));
                    datas.remove(i);
                } else if (datas.get(i).getName().contains("重庆")) {
                    tempMap.put(4, datas.get(i));
                    datas.remove(i);
                }
            }
            datas.add(0, tempMap.get(4));
            datas.add(0, tempMap.get(3));
            datas.add(0, tempMap.get(2));
            datas.add(0, tempMap.get(1));
            mHandler.sendEmptyMessage(0);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ShowMsgDialog.cancel();
                    adapter = new ProvinceMainAdater(PublicProvinceActivity.this, datas);
                    listView.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        areaBean = adapter.getItem(position);
        Intent intent = new Intent(this, PublicCityActivity.class);
        intent.putExtra("bean", areaBean);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                areaBean = (ProvinceBean) data.getSerializableExtra(Constants.COMEBACK);
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, areaBean);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            }
            default:
                break;
        }
    }
}
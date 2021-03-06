package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.RecordListAdapter;
import com.xygame.sg.activity.personal.bean.ModelResumeVo;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.activity.personal.bean.TransferDeleResumeBean;
import com.xygame.sg.bean.comm.TransResumeBean;
import com.xygame.sg.define.view.RecordOptionsView;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by tony on 2016/5/6.
 */
public class STSEditRecordActivity extends SGBaseActivity implements View.OnClickListener, OnItemClickListener {

    private View backButton, addNewRusuem;
    private TextView titleName;
    private TextView rightButtonText;
    private TransferDeleResumeBean transferBean;
    private ListView record_lv;
    private TransResumeBean tsResumeBean;
    private RecordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_edit_record);
        initViews();
        addListener();
    }

    private void initViews() {

        backButton = findViewById(R.id.backButton);
        addNewRusuem = findViewById(R.id.addNewRusuem);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        titleName.setText(getText(R.string.title_activity_edit_record));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.manager));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));
        transferBean = new TransferDeleResumeBean();
        record_lv = (ListView) findViewById(R.id.record_lv);
        tsResumeBean = (TransResumeBean) getIntent().getSerializableExtra("data");
        if (tsResumeBean != null) {
            initParseDatas();
        } else {
            adapter = new RecordListAdapter(this, null);
            record_lv.setAdapter(adapter);
        }
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    private void initParseDatas() {
        // TODO Auto-generated method stub
        List<ModelResumeVo>  resumeList = tsResumeBean.getResumeList();
        List<RsumeBean> datas = new ArrayList<RsumeBean>();
        for (ModelResumeVo map : resumeList) {
            RsumeBean it = new RsumeBean();
            it.setEndTime(map.getEndDate()+"");
            it.setExperDesc(map.getExperDesc());
            it.setLocIndex(map.getLocIndex());
            it.setResumeId(map.getId()+"");
            it.setStartTime(map.getStartDate()+"");
            datas.add(it);
        }
        adapter = new RecordListAdapter(this, datas);
        record_lv.setAdapter(adapter);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        addNewRusuem.setOnClickListener(this);
        record_lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                Intent intent1 = new Intent(Constants.ACTION_EDITOR_USER_INFO);
                sendBroadcast(intent1);
                finish();
                break;
            case R.id.rightButtonText:
                startActivityForResult(new Intent(this, RecordOptionsView.class), 0);
                break;

            case R.id.addNewRusuem:
                Intent intent = new Intent(this, EditDetailRecordActivity.class);
                intent.putExtra("type", "new");
                intent.putExtra("actionName", "新增履历");
                intent.putExtra("index", adapter.getCount());
                startActivityForResult(intent, 1);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
            sendBroadcast(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("order".equals(flag)) {
                    if (adapter.getCount() > 0) {
                        transferBean.setDeleDatas(adapter.getDatas());
                        Intent intent = new Intent(this, OrderResumeActivity.class);
                        intent.putExtra("bean", transferBean);
                        startActivityForResult(intent, 3);
                    } else {
                        Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                } else if ("delete".equals(flag)) {
                    if (adapter.getCount() > 0) {
                        transferBean.setDeleDatas(adapter.getDatas());
                        Intent intent = new Intent(this, DeleteResumeActivity.class);
                        intent.putExtra("bean", transferBean);
                        startActivityForResult(intent, 3);
                    } else {
                        Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case 1:
                RsumeBean resumeBean = (RsumeBean) data.getSerializableExtra(Constants.COMEBACK);
                adapter.addNewBean(resumeBean);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                RsumeBean resumeBean1 = (RsumeBean) data.getSerializableExtra(Constants.COMEBACK);
                adapter.modifyBean(resumeBean1);
                adapter.notifyDataSetChanged();
                break;
            case 3:
                TransferDeleResumeBean transferBean = (TransferDeleResumeBean) data
                        .getSerializableExtra(Constants.COMEBACK);
                if (transferBean != null) {
                    adapter.clearDatas();
                    adapter.addDatas(transferBean.getDeleDatas());
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 重载方法
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        RsumeBean item = adapter.getItem(arg2);
        Intent intent = new Intent(this, EditDetailRecordActivity.class);
        intent.putExtra("type", "old");
        intent.putExtra("bean", item);
        startActivityForResult(intent, 2);
    }
}
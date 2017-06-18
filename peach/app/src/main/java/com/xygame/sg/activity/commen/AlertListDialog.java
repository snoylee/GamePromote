package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.sg.R;
import com.xygame.sg.adapter.comm.DividGroupAdapter;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.StringUtils;

import java.util.List;

public class AlertListDialog extends Dialog implements AdapterView.OnItemClickListener {
    private ListView list;
    private List<GZ_GroupBeanTemp> allDatas;
    private DividGroupListener listener;
    private DividGroupAdapter adapter;
    private Context context;
    private GZ_GroupBeanTemp item;

    public AlertListDialog(Context context, int attrsr) {
        super(context, attrsr);
    }

    public AlertListDialog(Context context, List<GZ_GroupBeanTemp> allDatas, int attrs, DividGroupListener listener) {
        super(context, attrs);
        this.context = context;
        this.listener = listener;
        this.allDatas = allDatas;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_list_dialog_layout);
        initViews();
        initListener();
        initDatas();
    }


    private void initViews() {
        list = (ListView) findViewById(R.id.list);
    }


    private void initListener() {
        list.setOnItemClickListener(this);
    }


    private void initDatas() {
        adapter = new DividGroupAdapter(context, allDatas);
        list.setAdapter(adapter);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item = adapter.getItem(position);
        adapter.updatePostion(position);
        ThreadPool.getInstance().excuseThread(new DelayRuntime());
    }

    private class DelayRuntime implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                mHandler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listener.devideAction(item);
                    dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

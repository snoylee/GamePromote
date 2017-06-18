package com.xygame.second.sg.personal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.adapter.UserPhotoesAdapter;
import com.xygame.second.sg.personal.bean.PhotoBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/8/22.
 */
public class PohoesBrowserActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

    private TextView titleName;
    private View backButton;
    private PullToRefreshListView2 fansList;
    private int pageSize = 21;//每页显示的数量
    private int fansPage = 1;//当前显示页数
    private String fansReqTime;
    private boolean fansIsLoading = true;
    private UserPhotoesAdapter adapter;
    private String quaryUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photoes_layout);
        initViews();
        initListeners();
        initDatas();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        fansList.setOnRefreshListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        fansList = (PullToRefreshListView2) findViewById(R.id.fansList);
        fansList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        quaryUserId = getIntent().getStringExtra("quaryUserId");
        titleName.setText("相册");
        adapter = new UserPhotoesAdapter(this, null);
        fansList.setAdapter(adapter);
        loadDatas();
    }

    private void loadDatas() {//index:1进行中 2已完成 3已关闭
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", fansPage).put("pageSize", pageSize));
            obj.put("userId", quaryUserId);
            if (fansPage > 1) {
                obj.put("reqTime", fansReqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_GALLERY_PICS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GALLERY_PICS);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新操作
        fansIsLoading = true;
        fansPage = 1;
        loadDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载操作
        if (fansIsLoading) {
            fansPage = fansPage + 1;
            loadDatas();
        } else {
            falseDatas();
        }
    }

    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message m = mHandler.obtainMessage();
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GALLERY_PICS:
                fansList.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<PhotoBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                fansReqTime = object.getString("reqTime");
                String actions = object.getString("pics");
                if (ConstTaskTag.isTrueForArrayObj(actions)) {
                    JSONArray array = new JSONArray(actions);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject photoObject = array.getJSONObject(i);
                        PhotoBean item = new PhotoBean();
                        item.setCreateTime(StringUtils.getJsonValue(photoObject, "createTime"));
                        item.setResUrl(StringUtils.getJsonValue(photoObject, "resUrl"));
                        item.setResId(StringUtils.getJsonValue(photoObject, "resId"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                fansIsLoading = false;
            }
            ShowMsgDialog.showNoMsg(this, true);
            ThreadPool.getInstance().excuseThread(new AddImageIntoViews(datas));
        } else {
            fansIsLoading = false;
        }
    }

    private class AddImageIntoViews implements Runnable {
        private List<PhotoBean> datas;

        private AddImageIntoViews(List<PhotoBean> datas) {
            this.datas = datas;
        }

        @Override
        public void run() {
            adapter.addDatas(datas, fansPage);
            mHandler.sendEmptyMessage(0);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ShowMsgDialog.cancel();
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    fansList.onRefreshComplete();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.bean.ActManagerBean;
import com.xygame.second.sg.jinpai.activity.JinPaiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiFuFeiDetailActivity;
import com.xygame.second.sg.jinpai.adapter.ActManagerAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.sendgift.activity.YuePaiDetailActivity;
import com.xygame.second.sg.xiadan.adapter.GodOrderBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.init.TelliUtil;

/**
 * Created by tony on 2016/12/26.
 */
public class XiaDanManageActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2{

    private TextView titleName;
    private View backButton;
    private PullToRefreshListView2 PaiSList;
    private int pageSize = 15;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private String hotReqTime;
    private boolean hotIsLoading = true;
    private XiaDanManagementAdapter paiSAdapter;
    private GodOrderBean godOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiadan_manag_layout);
        registerBoradcastReceiver();
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
        PaiSList.setOnRefreshListener(this);
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
        PaiSList = (PullToRefreshListView2)findViewById(R.id.noticeList);
        PaiSList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        titleName.setText("订单管理");
        paiSAdapter = new XiaDanManagementAdapter(this, null);
        PaiSList.setAdapter(paiSAdapter);
        loadDatas();
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            if (hotPage > 1) {
                obj.put("reqTime", hotReqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_CENTER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_CENTER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
//                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
//                if (result) {
//                    if (flagIndex==0){
//                        daiSAdapter.updatePayStatus(noticeBeanItem);
//                    }else if (flagIndex==1){
//                        zhaoMAdapter.updatePayStatus(noticeBeanItem);
//                    }
//
//                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新操作
        hotIsLoading = true;
        hotPage = 1;
        loadDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载操作
        if (hotIsLoading) {
            hotPage = hotPage + 1;
            loadDatas();
        } else {
            falseDatas();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    PaiSList.onRefreshComplete();
                    break;
                case 1:
                    String orderId=(String)msg.obj;
                    paiSAdapter.updateStatusByOrderId(orderId);
                    break;
                default:
                    break;
            }
        }
    };


    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 0;
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
            case ConstTaskTag.QUERY_ORDER_CENTER:
                PaiSList.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ORDER_CANCEL:
                if ("0000".equals(data.getCode())) {
                    godOrderBean.setPayStatus("3");
                    paiSAdapter.updateItem(godOrderBean);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseHotModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<GodOrderBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                hotReqTime = object.getString("reqTime");
                String actions = object.getString("orders");
                if (ConstTaskTag.isTrueForArrayObj(actions)) {
                    JSONArray array2 = new JSONArray(actions);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        GodOrderBean item = new GodOrderBean();
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setHoldTime(StringUtils.getJsonValue(object1, "holdTime"));
                        item.setInviteUserId(StringUtils.getJsonValue(object1, "inviteUserId"));
                        item.setOrderAmount(StringUtils.getJsonValue(object1, "orderAmount"));
                        item.setOrderId(StringUtils.getJsonValue(object1, "orderId"));
                        item.setOrderStatus(StringUtils.getJsonValue(object1, "orderStatus"));
                        item.setPayStatus(StringUtils.getJsonValue(object1, "payStatus"));
                        item.setSkillCode(StringUtils.getJsonValue(object1, "skillCode"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
                        item.setStartTime(StringUtils.getJsonValue(object1,"startTime"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                hotIsLoading = false;
            }
            paiSAdapter.addDatas(datas, hotPage);
        } else {
            hotIsLoading = false;
        }
    }

    private class XiaDanManagementAdapter extends BaseAdapter {
        private Context context;
        private List<GodOrderBean> vector;
        private ImageLoader mImageLoader;

        public XiaDanManagementAdapter(Context context, List<GodOrderBean> vector) {
            this.context = context;
            mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
            if (vector != null) {
                this.vector = vector;
            } else {
                this.vector = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return vector.size();
        }

        @Override
        public GodOrderBean getItem(int position) {
            return vector.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.xiadan_manager_item, parent, false);

                viewHolder.userIcon = (CircularImage) convertView.findViewById(R.id.userIcon);
                viewHolder.startTime = (TextView)convertView.findViewById(R.id.startTime);
                viewHolder.payStatusText=(TextView)convertView.findViewById(R.id.payStatusText);
                viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
                viewHolder.priceValue = (TextView) convertView.findViewById(R.id.priceValue);
                viewHolder.agreeButton = (TextView) convertView
                        .findViewById(R.id.agreeButton);
                viewHolder.orderFlagImage=(ImageView)convertView.findViewById(R.id.orderFlagImage);
                viewHolder.payButton=(TextView)convertView.findViewById(R.id.payButton);
                viewHolder.godAppIcon = (CircularImage) convertView
                        .findViewById(R.id.godAppIcon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            GodOrderBean item = vector.get(position);
            if (UserPreferencesUtil.getUserId(context).equals(item.getInviteUserId())){
                viewHolder.orderFlagImage.setImageResource(R.drawable.jiedanda);
            }else{
                viewHolder.orderFlagImage.setImageResource(R.drawable.xiadanda);
            }
            viewHolder.startTime.setText(CalendarUtils.getYMDHMStr(Long.parseLong(item.getStartTime())).concat(" ").concat(item.getHoldTime()).concat("次"));
            List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
            if (jinPaiBigTypeBeans != null) {
                for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
                    if (item.getSkillCode().equals(it.getId())){
                        viewHolder.godAppIcon.setVisibility(View.VISIBLE);
                        viewHolder.typeName.setText(it.getName());
                        mImageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
                        break;
                    }
                }
            }else{
                viewHolder.typeName.setText("未知");
                viewHolder.godAppIcon.setImageResource(R.drawable.new_system_icon);
            }
            viewHolder.priceValue.setText(ConstTaskTag.getIntPrice(item.getOrderAmount()));
            mImageLoader.loadImage(item.getUserIcon(), viewHolder.userIcon, true);
            switch (Constants.orderStatus(item.getStartTime(),item.getOrderStatus(),item.getPayStatus())){
                case 0:
                    viewHolder.agreeButton.setText("未支付");
                    viewHolder.payButton.setVisibility(View.GONE);
                    viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
                    viewHolder.payStatusText.setText("未支付");
                    break;
                case 1:
                    viewHolder.agreeButton.setText("待确认");
                    viewHolder.payButton.setVisibility(View.GONE);
                    viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
                    viewHolder.payStatusText.setText("已支付");
                    break;
                case 2:
                    viewHolder.payStatusText.setText("已支付");
                    viewHolder.agreeButton.setText("待服务");
                    if (item.getUserId().equals(UserPreferencesUtil.getUserId(context))){
                        viewHolder.payButton.setVisibility(View.VISIBLE);
                        viewHolder.payButton.setTextColor(context.getResources().getColor(R.color.dark_green));
                        viewHolder.payButton.setOnClickListener(new FinishOrder(item));
                    }else{
                        viewHolder.payButton.setVisibility(View.GONE);
                    }
                    viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
                    break;
                case 3:
                    viewHolder.payStatusText.setText("已支付");
                    viewHolder.agreeButton.setText("服务中");
                    if (item.getUserId().equals(UserPreferencesUtil.getUserId(context))){
                        if ("7".equals(item.getOrderStatus())){
                            viewHolder.payButton.setVisibility(View.GONE);
                        }else{
                            viewHolder.payButton.setVisibility(View.VISIBLE);
                            viewHolder.payButton.setTextColor(context.getResources().getColor(R.color.dark_green));
                            viewHolder.payButton.setOnClickListener(new FinishOrder(item));
                        }
                    }else{
                        viewHolder.payButton.setVisibility(View.GONE);
                    }
                    viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
                    break;
                case 4:
                    viewHolder.payStatusText.setText("已支付");
                    viewHolder.payButton.setVisibility(View.GONE);
                    viewHolder.agreeButton.setText("已完成");
                    viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
                    break;
                case 5:
                    viewHolder.payStatusText.setText("已支付");
                    viewHolder.payButton.setVisibility(View.GONE);
                    viewHolder.agreeButton.setText("已取消");
                    viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
                    break;
            }
            convertView.setOnClickListener(new IntoDetial(item));
            return convertView;
        }

        public void clearDatas() {
            this.vector.clear();
            notifyDataSetChanged();
        }

        public void updateItem(GodOrderBean godOrderBean) {
            for (int i=0;i<vector.size();i++){
                if (godOrderBean.getOrderId().equals(vector.get(i).getOrderId())){
                    vector.get(i).setPayStatus(godOrderBean.getPayStatus());
                    break;
                }
            }
            notifyDataSetChanged();
        }

        public void updateItem(String orderId) {
            for (int i=0;i<vector.size();i++){
                if (orderId.equals(vector.get(i).getOrderId())){
                    vector.get(i).setPayStatus("3");
                    break;
                }
            }
            notifyDataSetChanged();
        }

        public void updateItemOrderStatus(String orderId) {
            for (int i=0;i<vector.size();i++){
                if (orderId.equals(vector.get(i).getOrderId())){
                    vector.get(i).setOrderStatus("4");
                    break;
                }
            }
            notifyDataSetChanged();
        }

        public void updateStatusByOrderId(String orderId) {
            for (int i=0;i<vector.size();i++){
                if (vector.get(i).getOrderId().equals(orderId)){
                    vector.get(i).setPayStatus("1");
                    break;
                }
            }
            notifyDataSetChanged();
        }


        private class ViewHolder {
            TextView startTime,typeName,priceValue,agreeButton,payButton,payStatusText;
            CircularImage userIcon,godAppIcon;
            ImageView orderFlagImage;
        }

        public void addDatas(List<GodOrderBean> datas, int mCurrentPage) {
            if (mCurrentPage == 1) {
                this.vector = datas;
            } else {
                this.vector.addAll(datas);
            }
            notifyDataSetChanged();
        }

        private class IntoDetial implements View.OnClickListener {
            private GodOrderBean item;

            public IntoDetial(GodOrderBean item) {
                this.item = item;
            }

            @Override
            public void onClick(View v) {
                intoAction(item);
            }
        }

        private void intoAction(GodOrderBean item) {
            Intent intent=new Intent(context,OrderDetailActivity.class);
            intent.putExtra("orderId",item.getOrderId());
            intent.putExtra("fromFlag","xiaDanManage");
            context.startActivity(intent);
        }

        private class FinishOrder implements View.OnClickListener {
            private GodOrderBean item;

            public FinishOrder(GodOrderBean item) {
                this.item = item;
            }

            @Override
            public void onClick(View v) {
                finishedAction(item);
            }
        }
    }

    private void finishedAction(GodOrderBean bean) {
        godOrderBean=bean;
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderId", bean.getOrderId());
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_CANCEL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_CANCEL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.GROUP_NEW_MESSAGE_ACTION);
        myIntentFilter.addAction(XMPPUtils.MSG_TYPE);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.GROUP_NEW_MESSAGE_ACTION.equals(intent.getAction())) {
               String orderId=intent.getStringExtra("orderId");
                String isFinish=intent.getStringExtra("isFinish");
                if ("ok".equals(isFinish)){
                    paiSAdapter.updateItem(orderId);
                }else{
                    paiSAdapter.updateItemOrderStatus(orderId);
                }
            }else if (XMPPUtils.MSG_TYPE.equals(intent.getAction())) {
                String orderId=intent.getStringExtra("orderId");
                android.os.Message m = handler.obtainMessage();
                m.obj=orderId;
                m.what = 1;
                m.sendToTarget();
            }
        }
    };
}
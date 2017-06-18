package com.xygame.sg.activity.personal;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.NoticeStatusManagerBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SettingNoticeActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName,noticeStatusText;
    private View backButton;
    private CheckBox newNewsBox;
    private CheckBox systemBox;
    private CheckBox chatBox;
    private CheckBox orderBox;
    private int chatNotify=1,orderNotify=1,sysNotify=1;//1是开，2是关
    private String actionFlag="";
    private NoticeStatusManagerBean noticeStatusManagerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notice);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        noticeStatusText=(TextView)findViewById(R.id.noticeStatus);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        newNewsBox = (CheckBox) findViewById(R.id.newNewsBox);
        systemBox = (CheckBox) findViewById(R.id.systemBox);
        chatBox = (CheckBox) findViewById(R.id.chatBox);
        orderBox = (CheckBox) findViewById(R.id.orderBox);

    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        newNewsBox.setOnClickListener(this);
        systemBox.setOnClickListener(this);
        orderBox.setOnClickListener(this);
        chatBox.setOnClickListener(this);
    }

    private void initDatas() {
        titleName.setText(getResources().getString(R.string.title_activity_setting_notice));
        boolean noticeStatus=checkPermission(this);
        if (noticeStatus){
            noticeStatusText.setText("已开启");
        }else{
            noticeStatusText.setText("已关闭");
        }
        noticeStatusManagerBean= CacheService.getInstance().getCacheNoticeStatusManagerBean(ConstTaskTag.CACHE_NOTICE_STATUS);
        if (noticeStatusManagerBean==null){
            noticeStatusManagerBean=new NoticeStatusManagerBean();
            loadSetInfo();
        }else{
            chatNotify=noticeStatusManagerBean.getChatNotify();
            orderNotify=noticeStatusManagerBean.getOrderNotify();
            sysNotify=noticeStatusManagerBean.getSysNotify();
            updateViews();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.orderBox:
                actionFlag = "orderNotify";
                setStatus("orderNotify", orderNotify);
                break;
            case R.id.chatBox:
                actionFlag = "chatNotify";
                setStatus("chatNotify", chatNotify);
                break;
            case R.id.systemBox:
                actionFlag = "sysNotify";
                setStatus("sysNotify", sysNotify);
                break;
            case R.id.newNewsBox:
                break;
        }
    }

    private void setStatus(String status,int flag){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            int temFlag=1;
            if (flag==1){
                temFlag=2;
            }else if (flag==2){
                temFlag=1;
            }
            object.put(status,temFlag);
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_SETTING_NOTICE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SETTING_NOTICE);
    }

    private void loadSetInfo() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_SETTING_INFO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SETTING_INFO);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_SETTING_INFO:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SETTING_NOTICE:
                if ("0000".equals(data.getCode())) {
                    if (actionFlag.equals("chatNotify")){
                        if (chatNotify==1){
                            chatNotify=2;
                        }else if (chatNotify==2){
                            chatNotify=1;
                        }
                    }else if (actionFlag.equals("orderNotify")){
                        if (orderNotify==1){
                            orderNotify=2;
                        }else if (orderNotify==2){
                            orderNotify=1;
                        }
                    }else if (actionFlag.equals("sysNotify")){
                        if (sysNotify==1){
                            sysNotify=2;
                        }else if (sysNotify==2){
                            sysNotify=1;
                        }
                    }
                    updateViews();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void updateViews() {
        if (chatNotify==1){
            chatBox.setChecked(true);
        }else if (chatNotify==2){
            chatBox.setChecked(false);
        }

        if (orderNotify==1){
            orderBox.setChecked(true);
        }else if (orderNotify==2){
            orderBox.setChecked(false);
        }

        if (sysNotify==1){
            systemBox.setChecked(true);
        }else if (sysNotify==2){
            systemBox.setChecked(false);
        }
        noticeStatusManagerBean.setChatNotify(chatNotify);
        noticeStatusManagerBean.setOrderNotify(orderNotify);
        noticeStatusManagerBean.setSysNotify(sysNotify);
        CacheService.getInstance().cacheNoticeStatusManagerBean(ConstTaskTag.CACHE_NOTICE_STATUS, noticeStatusManagerBean);
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object = new JSONObject(record);
                chatNotify= StringUtils.getJsonIntValue(object,"chatNotify");
                orderNotify=StringUtils.getJsonIntValue(object,"orderNotify");
                sysNotify=StringUtils.getJsonIntValue(object,"sysNotify");
                updateViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context
     * @return true 代表有有权限，或者检测失败   返回false代表没有权限
     */
    private boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT < 18 || Build.VERSION.SDK_INT > 22) {
            return true;
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Class<? extends AppOpsManager> class1 = mAppOps.getClass();
        try {
            Method method = class1.getDeclaredMethod("noteOpNoThrow", int.class, int.class, String.class);
            if (method.invoke(mAppOps, 11, Binder.getCallingUid(), context.getPackageName()).equals(AppOpsManager.MODE_ALLOWED)) {
                return true;
            } else {
                return false;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return true;
        }
    }
}

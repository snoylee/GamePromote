package com.xygame.second.sg.biggod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GodApplactionFristActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName;
    private View backButton;
    private LinearLayout bodyContentView;
    private List<JinPaiBigTypeBean> typeDatas;
    private Map<String,String> statusMap;
    private Map<String,List<JinPaiBigTypeBean>> leiMap;
    private List<String> nameLists;
    private ImageLoader mImageLoader;
    private boolean isLoadStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_apl_frist_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        bodyContentView=(LinearLayout)findViewById(R.id.bodyContentView);
    }

    private void initListensers() {
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        leiMap=new HashMap<>();
        nameLists=new ArrayList<>();
        statusMap=new HashMap<>();
        titleName.setText("申请项目");
        typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        isLoadStatus=getIntent().getBooleanExtra("isLoadStatus",false);
        if (typeDatas!=null){
            if (isLoadStatus){
                loadStatusDatas();
            }else {
                updateAllViews();
            }
        }else{
            loadProjectDatas();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    public void loadProjectDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void loadStatusDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE_STATUS);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE_STATUS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_SERVER_TYPE:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SERVER_TYPE_STATUS:
                if ("0000".equals(data.getCode())) {
                    parseStatusDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseStatusDatas(String record) {
        try {
            if (ConstTaskTag.isTrueForArrayObj(record)){
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String typeId=StringUtils.getJsonValue(object, "skillCode");
                    String typeStatus=StringUtils.getJsonValue(object, "authStatus");
                    statusMap.put(typeId,typeStatus);
                }
            }

            updateAllViews();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                List<JinPaiBigTypeBean> fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiBigTypeBean item = new JinPaiBigTypeBean();
                    item.setIsSelect(false);
                    item.setName(StringUtils.getJsonValue(object, "typeName"));
                    item.setId(StringUtils.getJsonValue(object, "typeId"));
                    item.setSubStr(StringUtils.getJsonValue(object, "titles"));
                    item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
                    item.setCategoryName(StringUtils.getJsonValue(object, "categoryName"));
                    if ("900".equals(item.getId())){
                        JinPaiBigTypeBean subItem = new JinPaiBigTypeBean();
                        subItem.setCategoryName(item.getCategoryName());
                        subItem.setId(Constants.DEFINE_LOL_ID);
                        subItem.setSubStr(item.getSubStr());
                        subItem.setUrl(item.getUrl());
                        subItem.setName("LOL");
                        fuFeiDatas.add(subItem);
                    }
                    fuFeiDatas.add(item);
                }
                CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, fuFeiDatas);
                if (isLoadStatus){
                    loadStatusDatas();
                }else {
                    updateAllViews();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAllViews() {
        if (typeDatas==null){
            typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        }
        for (JinPaiBigTypeBean item:typeDatas){
            String categoryName=item.getCategoryName();
            List<JinPaiBigTypeBean> tempDatas=leiMap.get(categoryName);
            if (tempDatas==null){
                tempDatas=new ArrayList<>();
                nameLists.add(categoryName);
                tempDatas.add(item);
                leiMap.put(categoryName,tempDatas);
            }else{
                tempDatas.add(item);
                leiMap.put(categoryName,tempDatas);
            }
        }
        for (String item:nameLists){
            View allView= LayoutInflater.from(this).inflate(R.layout.god_app_item_layout, null);
            TextView godGroupName=(TextView)allView.findViewById(R.id.godGroupName);
            LinearLayout godGroupList=(LinearLayout)allView.findViewById(R.id.godGroupList);
            godGroupName.setText(item);
            List<JinPaiBigTypeBean> tempDatas=leiMap.get(item);
            for (JinPaiBigTypeBean it:tempDatas){
                if (!"1000".equals(it.getId())&&!"900".equals(it.getId())){
                    View subView= LayoutInflater.from(this).inflate(R.layout.god_app_item_item_layout, null);
                    CircularImage godAppIcon=(CircularImage)subView.findViewById(R.id.godAppIcon);
                    TextView godAppName=(TextView)subView.findViewById(R.id.godAppName);
                    TextView godAppStatus=(TextView)subView.findViewById(R.id.godAppStatus);
                    godAppName.setText(it.getName());
                    if (Constants.DEFINE_LOL_ID.equals(it.getId())){
                        String onLine=statusMap.get("900");
                        String offLine=statusMap.get("1000");
                        if (!TextUtils.isEmpty(onLine)&&!TextUtils.isEmpty(offLine)){
                            if ("1".equals(offLine)) {
                                godAppStatus.setText("审核中");
                            }else if ("4".equals(offLine)) {
                                godAppStatus.setText("审核中");
                                godAppStatus.setText("");
                            }else if ("2".equals(offLine)) {
                                godAppStatus.setText("已通过");
                            }else if ("0".equals(offLine) ||"3".equals(offLine)) {
                                godAppStatus.setText("");
                            }
                        }else{
                            if (TextUtils.isEmpty(onLine)&&TextUtils.isEmpty(offLine)){
                                godAppStatus.setText("");
                            }else if (!TextUtils.isEmpty(onLine)&&TextUtils.isEmpty(offLine)){
                                if ("1".equals(onLine)) {
                                    godAppStatus.setText("审核中");
                                }else if ("4".equals(onLine)) {
                                    godAppStatus.setText("审核中");
                                }else if ("2".equals(onLine)) {
                                    godAppStatus.setText("已通过");
                                }else if ( TextUtils.isEmpty(onLine)  || "0".equals(onLine) ||"3".equals(onLine)) {
                                    godAppStatus.setText("");
                                }
                            }else  if (!TextUtils.isEmpty(offLine)&&TextUtils.isEmpty(onLine)){
                                if ("1".equals(offLine)) {
                                    godAppStatus.setText("审核中");
                                }else if ("4".equals(offLine)) {
                                    godAppStatus.setText("审核中");
                                }else if ("2".equals(offLine)) {
                                    godAppStatus.setText("已通过");
                                }else if (TextUtils.isEmpty(offLine)  || "0".equals(offLine) ||"3".equals(offLine)) {
                                    godAppStatus.setText("");
                                }
                            }
                        }
                    }else{
                        String vidoStatus= statusMap.get(it.getId());
                        if ("1".equals(vidoStatus)) {
                            godAppStatus.setText("审核中");
                        }else if ("4".equals(vidoStatus)) {
                            godAppStatus.setText("审核中");
                            godAppStatus.setText("");
                        }else if ("2".equals(vidoStatus)) {
                            godAppStatus.setText("已通过");
                        }else if ( TextUtils.isEmpty(vidoStatus)  || "0".equals(vidoStatus) ||"3".equals(vidoStatus)) {
                            godAppStatus.setText("");
                        }
                    }
                    mImageLoader.loadImage(it.getUrl(), godAppIcon, true);
                    subView.setOnClickListener(new IntoNextPage(it));
                    godGroupList.addView(subView);
                }
            }
            bodyContentView.addView(allView);
        }
    }

    private class IntoNextPage implements OnClickListener{
        private JinPaiBigTypeBean item;
        public IntoNextPage(JinPaiBigTypeBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            checkIntoNextPage(item);
        }
    }

    private void checkIntoNextPage(JinPaiBigTypeBean item) {
        if (Constants.DEFINE_LOL_ID.equals(item.getId())){
            String onLine=statusMap.get("900");
            String offLine=statusMap.get("1000");
            if (!TextUtils.isEmpty(onLine)&&!TextUtils.isEmpty(offLine)){
                if ("1".equals(offLine)) {
                    showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
                }else if ("4".equals(offLine)) {
                    showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
                }else if ("2".equals(offLine)) {
                    Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                    intent.putExtra("godFlag","editor");
                    intent.putExtra("typeId","1000");
                    intent.putExtra("bean",item);
                    startActivityForResult(intent, 0);
                }else if ("0".equals(offLine) ||"3".equals(offLine)) {
                    Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                    intent.putExtra("bean",item);
                    startActivityForResult(intent, 0);
                }
            }else{
                if (TextUtils.isEmpty(onLine)&&TextUtils.isEmpty(offLine)){
                    Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                    intent.putExtra("bean",item);
                    startActivityForResult(intent, 0);
                }else if (!TextUtils.isEmpty(onLine)&&TextUtils.isEmpty(offLine)){
                    if ("1".equals(onLine)) {
                        showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
                    }else if ("4".equals(onLine)) {
                        showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
                    }else if ("2".equals(onLine)) {
                        Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                        intent.putExtra("godFlag","editor");
                        intent.putExtra("typeId","900");
                        intent.putExtra("bean",item);
                        startActivityForResult(intent, 0);
                    }else if ( TextUtils.isEmpty(onLine)  || "0".equals(onLine) ||"3".equals(onLine)) {
                        Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                        intent.putExtra("bean",item);
                        startActivityForResult(intent, 0);
                    }
                }else  if (!TextUtils.isEmpty(offLine)&&TextUtils.isEmpty(onLine)){
                    if ("1".equals(offLine)) {
                        showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
                    }else if ("4".equals(offLine)) {
                        showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
                    }else if ("2".equals(offLine)) {
                        Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                        intent.putExtra("godFlag","editor");
                        intent.putExtra("typeId","1000");
                        intent.putExtra("bean",item);
                        startActivityForResult(intent, 0);
                    }else if (TextUtils.isEmpty(offLine)  || "0".equals(offLine) ||"3".equals(offLine)) {
                        Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                        intent.putExtra("bean",item);
                        startActivityForResult(intent, 0);
                    }
                }
            }
        }else{
            String vidoStatus=statusMap.get(item.getId());
            if ("1".equals(vidoStatus)) {
                showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
            }else if ("4".equals(vidoStatus)) {
                showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
            }else if ("2".equals(vidoStatus)) {
                Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                intent.putExtra("godFlag","editor");
                intent.putExtra("bean",item);
                startActivityForResult(intent, 0);
            }else if ( TextUtils.isEmpty(vidoStatus)  || "0".equals(vidoStatus) ||"3".equals(vidoStatus)) {
                Intent intent=new Intent(this,GodApplactionSecondActivity.class);
                intent.putExtra("bean",item);
                startActivityForResult(intent,0);
            }
        }
    }

    private void showOneButtonDialog(String content) {
        OneButtonDialog dialog = new OneButtonDialog(this, content, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
              boolean flag=data.getBooleanExtra(Constants.COMEBACK,false);
                if (flag){
                    finish();
                }
                break;
            default:
                break;
        }
    }

}

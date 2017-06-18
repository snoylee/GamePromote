package com.xygame.second.sg.biggod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.DiscBean;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.OderBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.SetPriceActivity;
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
import java.util.List;

public class GodApplactionEditorActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName,orderTip,rightButtonText;
    private View backButton,rightButton;
    private LinearLayout orderListView;
    private  List<OderBean> orderDatas,dealWithDatas;
    private ImageLoader mImageLoader;
    private int currOprPosion;
    private boolean gradeFlag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_apl_editor_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        orderListView=(LinearLayout)findViewById(R.id.orderListView);
        orderTip =(TextView) findViewById(R.id.orderTip);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButton=findViewById(R.id.rightButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
    }

    private void initListensers() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }

    private void initDatas() {
        dealWithDatas=new ArrayList<>();
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("接单设置");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("保存");
        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        List<JinPaiBigTypeBean> typeDatas= CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        if (typeDatas!=null){
            List<PriceBean> fuFeiDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
            if (fuFeiDatas!=null){
                loadSetOder();
            }else{
                loadPriceDatas();
            }
        }else{
            loadProjectDatas();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.rightButton){
            if (isGo()){
                saveActions();
            }else{
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isGo(){
        dealWithDatas.clear();
        boolean flag=false;
        for (OderBean item:orderDatas){
            if (item.isDealWith()){
                dealWithDatas.add(item);
                flag=true;
            }
        }
        return flag;
    }

    public void saveActions() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            JSONArray array=new JSONArray();
            for (OderBean oderBean:dealWithDatas){
                JSONObject subJson=new JSONObject();
                if (oderBean.isPriceFlag()){
                    subJson.put("priceId",oderBean.getPriceId());
                    subJson.put("typeId",oderBean.getSkillCode());
                    subJson.put("priceRate",oderBean.getPriceRate());
                }
                if (oderBean.isRateFlag()) {
                    subJson.put("typeId",oderBean.getSkillCode());
                    subJson.put("recordStatus",oderBean.getStatus());
                }
                array.put(subJson);
            }
            obj.put("sets",array);
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_EDITOR_SERVER_TYPE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_EDITOR_SERVER_TYPE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void loadPriceDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE_PRICE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE_PRICE);
        } catch (Exception e1) {
            e1.printStackTrace();
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

    private void loadSetOder() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_SET_ORDER);
            ShowMsgDialog.showNoMsg(this, true);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SET_ORDER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        ShowMsgDialog.cancel();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_EDITOR_SERVER_TYPE:
                if ("0000".equals(data.getCode())) {
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SERVER_TYPE_PRICE:
                if ("0000".equals(data.getCode())) {
                    parsePriceDatas(data.getRecord());
                } else {
                    addBottomView();
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SERVER_TYPE:
                if ("0000".equals(data.getCode())) {
                    parseServerTypeDatas(data.getRecord());
                } else {
                    addBottomView();
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SET_ORDER:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    addBottomView();
                    Toast.makeText(this,data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        addBottomView();
        ShowMsgDialog.cancel();
        Toast.makeText(this,data.getMsg(), Toast.LENGTH_SHORT).show();
    }

    private void parsePriceDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                List<PriceBean> fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    PriceBean item = new PriceBean();
                    item.setPrice(StringUtils.getJsonValue(object, "basePrice"));
                    item.setId(StringUtils.getJsonValue(object, "priceId"));
                    item.setTitleId(StringUtils.getJsonValue(object, "titleId"));
                    item.setTypeId(StringUtils.getJsonValue(object, "typeId"));
                    fuFeiDatas.add(item);
                }
                CacheService.getInstance().cachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS, fuFeiDatas);
                loadSetOder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseServerTypeDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
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
                List<PriceBean> priceDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                if (priceDatas!=null){
                    loadSetOder();
                }else{
                    loadPriceDatas();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                orderDatas= new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    OderBean item = new OderBean();
                    item.setPriceRate(StringUtils.getJsonValue(object, "priceRate"));
                    item.setSkillCode(StringUtils.getJsonValue(object, "skillCode"));
                    item.setSkillTitle(StringUtils.getJsonValue(object, "skillTitle"));
                    item.setStatus(StringUtils.getJsonValue(object, "status"));
                    item.setPriceId(StringUtils.getJsonValue(object, "priceId"));
                    orderDatas.add(item);
                }
              if (orderDatas.size()>0){
                  orderTip.setVisibility(View.VISIBLE);
                  gradeFlag=true;
                  updateAllViews();
              }else{
                  addBottomView();
              }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addBottomView(){
        View view= LayoutInflater.from(this).inflate(R.layout.how_to_earn_money, null);
        View intoHowEarnMoneyView=view.findViewById(R.id.intoHowEarnMoneyView);
        intoHowEarnMoneyView.setOnClickListener(new IntoHowToEarnMoneyPage());
        orderListView.addView(view);
    }

    private class IntoHowToEarnMoneyPage implements OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent9 = new Intent(GodApplactionEditorActivity.this, CommonWebViewActivity.class);
            intent9.putExtra("webUrl",Constants.MAKE_MONEY);
            intent9.putExtra("title","如何赚钱");
            startActivity(intent9);
        }
    }

    private void updateAllViews(){
        orderListView.removeAllViews();
        List<JinPaiBigTypeBean> typeDatas= CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        for (int i=0;i<orderDatas.size();i++){
            OderBean item=orderDatas.get(i);
            for (JinPaiBigTypeBean it:typeDatas){
                if (it.getId().equals(item.getSkillCode())){
                    View view= LayoutInflater.from(this).inflate(R.layout.order_item,null);
                    CircularImage godAppIcon=(CircularImage)view.findViewById(R.id.godAppIcon);
                    TextView godAppName=(TextView)view.findViewById(R.id.godAppName);
                    CheckBox chatBox=(CheckBox)view.findViewById(R.id.chatBox);
                    TextView priceValue=(TextView)view.findViewById(R.id.priceValue);
                    View modifyPriceButton=view.findViewById(R.id.modifyPriceButton);
                    if ("1".equals(item.getStatus())){
                        chatBox.setChecked(true);
                    }else  if ("3".equals(item.getStatus())){
                        chatBox.setChecked(false);
                    }
                    mImageLoader.loadImage(it.getUrl(), godAppIcon, true);
                    godAppName.setText(it.getName());
                    List<PriceBean> fuFeiDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                    int value=0;
                    String resultPriceId=item.getNewPriceId()==null?item.getPriceId():item.getNewPriceId();
                    for (PriceBean t:fuFeiDatas){
                        if (t.getId().equals(resultPriceId)){
                            if (gradeFlag){
                                List<GodLableBean> tempLables=Constants.getGodLableDatas(it.getSubStr());
                                for (GodLableBean xxx:tempLables){
                                    if (item.getSkillTitle().equals(xxx.getTitleId())){
                                        orderDatas.get(i).setLocalIndex(xxx.getLocalIndex());
                                        break;
                                    }
                                }
                            }
                            value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
                            break;
                        }
                    }
                    if (value==0){
                        for (PriceBean t:fuFeiDatas){
                            if (t.getTypeId().equals(item.getSkillCode())&&t.getTitleId().equals(item.getSkillTitle())){
                                if (gradeFlag){
                                    List<GodLableBean> tempLables=Constants.getGodLableDatas(it.getSubStr());
                                    for (GodLableBean xxx:tempLables){
                                        if (item.getSkillTitle().equals(xxx.getTitleId())){
                                            orderDatas.get(i).setLocalIndex(xxx.getLocalIndex());
                                            break;
                                        }
                                    }
                                }
                                value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
                                break;
                            }
                        }
                    }
                    if (value==0){
                        for (PriceBean t:fuFeiDatas){
                            if (t.getTypeId().equals(item.getSkillCode())){
                                if (gradeFlag){
                                    List<GodLableBean> tempLables=Constants.getGodLableDatas(it.getSubStr());
                                    for (GodLableBean xxx:tempLables){
                                        if (item.getSkillTitle().equals(xxx.getTitleId())){
                                            orderDatas.get(i).setLocalIndex(xxx.getLocalIndex());
                                            break;
                                        }
                                    }
                                }
                                value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
                                break;
                            }
                        }
                    }
                    priceValue.setText(String.valueOf(value));
                    chatBox.setOnClickListener(new UpdateOrderStatus(i));
                    modifyPriceButton.setOnClickListener(new UpdateOrderPrice(i,it));
                    orderListView.addView(view);
                    break;
                }
            }
        }
        addBottomView();
    }

    private class UpdateOrderStatus implements OnClickListener{
        private int currId;
        public UpdateOrderStatus(int currId){
            this.currId=currId;
        }

        @Override
        public void onClick(View v) {
            updateStatusAction(currId);
        }
    }

    private void updateStatusAction(int currId) {
        OderBean item=orderDatas.get(currId);
        if ("1".equals(item.getStatus())){
            orderDatas.get(currId).setStatus("3");
        }else if ("3".equals(item.getStatus())){
            orderDatas.get(currId).setStatus("1");
        }
        orderDatas.get(currId).setIsDealWith(true);
        orderDatas.get(currId).setRateFlag(true);
        gradeFlag=false;
        updateAllViews();
    }

    private class UpdateOrderPrice implements OnClickListener{
        private int index;
        private JinPaiBigTypeBean it;
        public UpdateOrderPrice(int index,JinPaiBigTypeBean it){
            this.index=index;
            this.it=it;
        }

        @Override
        public void onClick(View v) {
            updatePriceAction(index,it);
        }
    }

    private void updatePriceAction(int index,JinPaiBigTypeBean it) {
        currOprPosion=index;
        OderBean item=orderDatas.get(index);
        Intent intent=new Intent(this, SetPriceActivity.class);
        intent.putExtra("bean",item);
        intent.putExtra("JinPaiBigTypeBean",it);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0:
                DiscBean discBean=(DiscBean)data.getSerializableExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG);
                PriceBean priceBean=(PriceBean)data.getSerializableExtra("priceBean");
                orderDatas.get(currOprPosion).setPriceRate(discBean.getId());
                orderDatas.get(currOprPosion).setPriceId(priceBean.getId());
                orderDatas.get(currOprPosion).setIsDealWith(true);
                orderDatas.get(currOprPosion).setPriceFlag(true);
                gradeFlag=false;
                updateAllViews();
                break;
            default:
                break;
        }
    }
}

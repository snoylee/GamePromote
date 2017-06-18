package com.xygame.second.sg.xiadan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.activity.ActManagerActivity;
import com.xygame.second.sg.xiadan.bean.ReasonBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/23.
 */
public class CancelReasonActivity extends SGBaseActivity implements View.OnClickListener{

    private View backButton,commitButton;
    private TextView titleName;
    private final String titleStr1="取消订单",getTitleStr2="申请退款";
    private String acceptTitleStr,orderId;
    private LinearLayout reasonLayout;
    private EditText oralText;
    private List<ReasonBean> reasonBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_reason_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        reasonLayout=(LinearLayout)findViewById(R.id.reasonLayout);
        oralText = (EditText)findViewById(R.id.oralText);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        commitButton=findViewById(R.id.commitButton);
    }

    private void initDatas() {
        orderId=getIntent().getStringExtra("orderId");
        acceptTitleStr=getIntent().getStringExtra("acceptTitleStr");
        if (titleStr1.equals(acceptTitleStr)){
            titleName.setText(titleStr1);
            initCancelDatas();
        }else if (getTitleStr2.equals(acceptTitleStr)){
            titleName.setText(getTitleStr2);
            initTuiKuanDatas();
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        commitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.commitButton:
                if (isGo()){
                    jinPai6012Act("确定要取消此订单吗？");
                }
                break;
        }
    }

    private void jinPai6012Act(String tip) {
        TwoButtonDialog dialog = new TwoButtonDialog(this, tip, "确定", "取消", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        if (titleStr1.equals(acceptTitleStr)){
                            cancelOrderAction();
                        }else if (getTitleStr2.equals(acceptTitleStr)){
                            applyOrderAction();
                        }
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialog.show();
    }

    public void applyOrderAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);
            ReasonBean curReason=null;
            for (ReasonBean it:reasonBeans){
                if (it.isSelect()){
                    curReason=it;
                    break;
                }
            }
            String shortTip=oralText.getText().toString().trim();
            if (!TextUtils.isEmpty(shortTip)){
                obj.put("opsReason", shortTip);
            }
            if (curReason!=null){
                obj.put("opsReasonType", curReason.getId());
            }
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_APPLY);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_APPLY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void cancelOrderAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);
            ReasonBean curReason=null;
            for (ReasonBean it:reasonBeans){
                if (it.isSelect()){
                    curReason=it;
                    break;
                }
            }
            String shortTip=oralText.getText().toString().trim();
            if (!TextUtils.isEmpty(shortTip)){
                obj.put("opsReason", shortTip);
            }
            if (curReason!=null){
                obj.put("opsReasonType", curReason.getId());
            }
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ORDER_CANCEL1);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_DIS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ORDER_DIS:
                if ("0000".equals(data.getCode())) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else if ("9003".equals(data.getCode())){
                    Intent intent = new Intent();
                    intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
                    intent.putExtra("flagStr","9003");
                    intent.putExtra("strTip","当前订单状态是为申请退款，您确定要申请退款吗？");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ORDER_APPLY:
                if ("0000".equals(data.getCode())) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else if ("9004".equals(data.getCode())){
                    Intent intent = new Intent();
                    intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
                    intent.putExtra("flagStr","9004");
                    intent.putExtra("strTip","当前订单状态是为取消订单，您确定要取消订单吗？");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isGo(){
        ReasonBean curReason=null;
        String shortTip;
        for (ReasonBean item:reasonBeans){
            if (item.isSelect()){
                curReason=item;
                break;
            }
        }
        shortTip=oralText.getText().toString().trim();
        if (curReason==null&& TextUtils.isEmpty(shortTip)){
            Toast.makeText(this,"选择原因和其它原因说明必须有一条",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initCancelDatas(){
        reasonBeans=new ArrayList<>();
        ReasonBean item1=new ReasonBean();
        item1.setContent("大神未响应");
        item1.setId("1");
        item1.setIsSelect(true);
        reasonBeans.add(item1);

        ReasonBean item2=new ReasonBean();
        item2.setContent("大神要求取消订单");
        item2.setId("2");
        item2.setIsSelect(false);
        reasonBeans.add(item2);

        ReasonBean item3=new ReasonBean();
        item3.setContent("订单信息填写有误");
        item3.setId("3");
        item3.setIsSelect(false);
        reasonBeans.add(item3);

        ReasonBean item4=new ReasonBean();
        item4.setContent("我临时有事");
        item4.setId("4");
        item4.setIsSelect(false);
        reasonBeans.add(item4);

        ReasonBean item5=new ReasonBean();
        item5.setContent("尝试性操作");
        item5.setId("5");
        item5.setIsSelect(false);
        reasonBeans.add(item5);
        updateReasonViews();
    }

    private void initTuiKuanDatas(){
        reasonBeans=new ArrayList<>();
        ReasonBean item1=new ReasonBean();
        item1.setContent("还未履约");
        item1.setId("1");
        item1.setIsSelect(true);
        reasonBeans.add(item1);

        ReasonBean item2=new ReasonBean();
        item2.setContent("迟到早退");
        item2.setId("2");
        item2.setIsSelect(false);
        reasonBeans.add(item2);

        ReasonBean item3=new ReasonBean();
        item3.setContent("沟通问题");
        item3.setId("3");
        item3.setIsSelect(false);
        reasonBeans.add(item3);
        updateReasonViews();
    }

    private void updateReasonViews(){
        reasonLayout.removeAllViews();
        for (ReasonBean item:reasonBeans){
            View view= LayoutInflater.from(this).inflate(R.layout.reason_item,null);
            TextView content=(TextView)view.findViewById(R.id.content);
            ImageView optionIcon=(ImageView)view.findViewById(R.id.optionIcon);
            content.setText(item.getContent());
            if (item.isSelect()){
                optionIcon.setImageResource(R.drawable.gou);
            }else{
                optionIcon.setImageResource(R.drawable.gou_null);
            }
            view.setOnClickListener(new ClickItemChangeListener(item));
            reasonLayout.addView(view);
        }
    }

    private class ClickItemChangeListener implements View.OnClickListener{
        private ReasonBean item;
        public ClickItemChangeListener(ReasonBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            refeshViews(item);
        }
    }

    private void refeshViews(ReasonBean item) {
        for (int i=0;i<reasonBeans.size();i++){
            if (item.getId().equals(reasonBeans.get(i).getId())){
                reasonBeans.get(i).setIsSelect(! reasonBeans.get(i).isSelect());
            }else{
                reasonBeans.get(i).setIsSelect(false);
            }
        }
        updateReasonViews();
    }
}

package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.bean.GetStoneMoneyBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.ButtonTwoTextListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonNumsDialog;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.activity.personal.BangAlipayActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONObject;

import java.util.Calendar;

public class GetMoneyActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName, userIncoming,withdrawRate,multiple;
    private View backButton, comfirmButtonView,isShowView;
    private BangAlipayBean alipayBean;
    private double yuE;
    private int peiShu;
    private  int tiMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_getmoney_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        isShowView=findViewById(R.id.isShowView);
        comfirmButtonView = findViewById(R.id.comfirmButtonView);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        userIncoming = (TextView) findViewById(R.id.userIncoming);
        withdrawRate=(TextView)findViewById(R.id.withdrawRate);
        multiple=(TextView)findViewById(R.id.multiple);
    }

    private void initListensers() {
        comfirmButtonView.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        alipayBean=(BangAlipayBean)getIntent().getSerializableExtra("alipayBean");
        titleName.setText("收入");
        peiShu=Integer.parseInt(multiple.getText().toString());
        yuE=0;
        loadStoneMoney();
    }

    private void showTipDialog() {
        TwoButtonDialog dialog = new TwoButtonDialog(this, "您还没有绑定支付宝号，绑定完后才可以申请提现，现在去绑定吧~", "好，去绑定", "暂时不绑定", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.comfirmButtonView) {
            if (alipayBean == null) {
                showTipDialog();
            } else {
                showCreateDialog();
            }
        }
    }

    private void showCreateDialog() {
        TwoButtonNumsDialog dialogCreate = new TwoButtonNumsDialog(this, "收入提现", "请输入提现金额", R.style.dineDialog,
                new ButtonTwoTextListener() {

                    @Override
                    public void confrimListener(String content) {
                        if (!TextUtils.isEmpty(content)){
                            commitPay(content);
                        }else {
                            Toast.makeText(GetMoneyActivity.this,"请输入提现金额",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialogCreate.show();
    }

    private void commitPay(String money) {
       tiMoney=Integer.parseInt(money);
        if (tiMoney%peiShu==0){
            RequestBean item = new RequestBean();
            try {
                JSONObject obj = new JSONObject();
                obj.put("withdrawType","2");//1:余额提现，2：收入提现
                obj.put("amount",tiMoney*100);
                item.setData(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_STONE_DrawCash);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_STONE_DrawCash);
        }else{
            Toast.makeText(GetMoneyActivity.this,"提现金额支持".concat(String.valueOf(peiShu).concat("的整数倍")),Toast.LENGTH_SHORT).show();
        }
    }

    public void loadStoneMoney() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_STONE_GET_MONEY_CHECK);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_STONE_GET_MONEY_CHECK);
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_STONE_GET_MONEY_CHECK:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_STONE_DrawCash:
                if ("0000".equals(data.getCode())) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, "tixian");
                    intent.putExtra("tiMoney", String.valueOf(tiMoney*100));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(ResponseBean data) {
        try {
            if (!TextUtils.isEmpty(data.getRecord())) {
                JSONObject object = new JSONObject(data.getRecord());
                if (!TextUtils.isEmpty(StringUtils.getJsonValue(object, "cash"))){
                    yuE=ConstTaskTag.getDoublePrice(StringUtils.getJsonValue(object, "cash"));
                    userIncoming.setText(String.valueOf(yuE));
                }else{
                    yuE=0;
                    userIncoming.setText(String.valueOf(yuE));
                }
                if (!TextUtils.isEmpty(StringUtils.getJsonValue(object, "withdrawRate"))){
                    isShowView.setVisibility(View.VISIBLE);
                    withdrawRate.setText(String.valueOf(100-StringUtils.getJsonIntValue(object, "withdrawRate")).concat("%"));
                }else{
                    isShowView.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(StringUtils.getJsonValue(object, "multiple"))){
                    multiple.setText(StringUtils.getJsonValue(object, "multiple"));
                    peiShu=Integer.parseInt(StringUtils.getJsonValue(object, "multiple"));
                }else{
                    peiShu=Integer.parseInt(multiple.getText().toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

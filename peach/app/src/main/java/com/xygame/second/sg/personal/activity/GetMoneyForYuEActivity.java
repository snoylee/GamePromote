package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.ButtonTwoTextListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonNumsDialog;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONObject;

public class GetMoneyForYuEActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName, yuEValue;
    private View backButton, comfirmButtonView;
    private double yuE;
   private EditText tixianValue;
    private int tiMoney=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getmoney_yue_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        comfirmButtonView = findViewById(R.id.comfirmButtonView);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        yuEValue = (TextView) findViewById(R.id.yuEValue);
        tixianValue=(EditText)findViewById(R.id.tixianValue);
    }

    private void initListensers() {
        comfirmButtonView.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        String yuEStr=getIntent().getStringExtra("yuEStr");
        yuEValue.setText("￥".concat(yuEStr));
        titleName.setText("提现");
        yuE=Double.parseDouble(yuEStr);
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
           String tixianStr=tixianValue.getText().toString().trim();
            if (!TextUtils.isEmpty(tixianStr)){
                tiMoney=Integer.parseInt(tixianStr);
                double tixian=Double.parseDouble(tixianStr);
                if (tixian<=yuE){
                    commitPay();
                }else{
                    Toast.makeText(GetMoneyForYuEActivity.this,"余额不足",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(GetMoneyForYuEActivity.this,"请输入提现金额",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void commitPay() {
        try {
            RequestBean item = new RequestBean();
            JSONObject obj = new JSONObject();
            obj.put("withdrawType","1");//1:余额提现，2：收入提现
            obj.put("amount",tiMoney*100);
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_STONE_DrawCash);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_STONE_DrawCash);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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
            case ConstTaskTag.QUERY_STONE_DrawCash:
                if ("0000".equals(data.getCode())) {
                    Intent intent = new Intent();
                    intent.putExtra("tixianValue", String.valueOf(tiMoney*100));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

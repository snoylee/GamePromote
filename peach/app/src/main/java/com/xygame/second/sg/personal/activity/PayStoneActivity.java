package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tendcloud.tenddata.TCAgent;
import com.xygame.second.sg.personal.adapter.MoneyValueAdapter;
import com.xygame.second.sg.personal.bean.MoneyValueBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
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

public class PayStoneActivity extends SGBasePaymentActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName, stoneNums, rightButtonText;
    private View backButton, rightButton, comfirm;
    private GridView childList;
    private MoneyValueAdapter moneyValueAdapter;
    private LinearLayout addPayWayView;
    private List<PayWaysBean> datas;
    private PayWaysBean payWayBean;
    private BangAlipayBean alipayBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_paystone_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        comfirm = findViewById(R.id.comfirm);
        addPayWayView = (LinearLayout) findViewById(R.id.addPayWayView);
        childList = (GridView) findViewById(R.id.photoList);
        rightButton = findViewById(R.id.rightButton);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        stoneNums = (TextView) findViewById(R.id.stoneNums);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
    }

    private void initListensers() {
        comfirm.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        childList.setOnItemClickListener(new AreaItemListener());
    }

    private class AreaItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MoneyValueBean areaBean = moneyValueAdapter.getItem(position);
            moneyValueAdapter.updateSelect(areaBean);
        }
    }

    private void initDatas() {
        titleName.setText("余额");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("提现");
        alipayBean = (BangAlipayBean) getIntent().getSerializableExtra("alipayBean");
        String stoneValue = getIntent().getStringExtra("stoneValue");
        stoneNums.setText(stoneValue);
        moneyValueAdapter = new MoneyValueAdapter(this, null);
        childList.setAdapter(moneyValueAdapter);
        loadPayStoneList();
    }

    public void loadPayStoneList() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_LEFT_MONEY_CHECK);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_LEFT_MONEY_CHECK);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            if (alipayBean == null) {
                showTipDialog();
            } else {
                Intent intent = new Intent(this, GetMoneyForYuEActivity.class);
                intent.putExtra("yuEStr", stoneNums.getText().toString());
                startActivityForResult(intent, 0);
            }
        } else if (v.getId() == R.id.comfirm) {
            payWayBean = getPayWay();
            if (payWayBean != null) {
                commitPay();
            } else {
                Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
            }
        }
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
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_PAY_MONEY:
                if ("0000".equals(data.getCode())) {
                    getPayInfo(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_LEFT_MONEY_CHECK:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void commitPay() {
        try {
            RequestBean item = new RequestBean();
            JSONObject obj = new JSONObject();
            String payType = getPayType().getWayId();
            MoneyValueBean moneyvalue = moneyValueAdapter.getMoneyValueBean();
            obj.put("amount", moneyvalue.getMoneyValue());
            obj.put("payType", payType);
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_PAY_MONEY);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_PAY_MONEY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void parseDatas(ResponseBean data) {

        if (!TextUtils.isEmpty(data.getRecord())) {
            try {
                JSONObject object1 = new JSONObject(data.getRecord());
                String payChannel = StringUtils.getJsonValue(object1, "payChannel");
                if (ConstTaskTag.isTrueForArrayObj(payChannel)) {
                    datas = new ArrayList<>();
                    JSONArray array = new JSONArray(payChannel);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String source = StringUtils.getJsonValue(object, "source");
                        String usedInAndroid = StringUtils.getJsonValue(object, "usedInAndroid");
                        String payType = StringUtils.getJsonValue(object, "payType");
                        if ("alipay".equals(source) && "true".equals(usedInAndroid)) {
                            PayWaysBean item = new PayWaysBean();
                            item.setWayName("支付宝支付");
                            item.setWayId(payType);
                            item.setIconId("1");
                            datas.add(item);

                        } else if ("weixin".equals(source) && "true".equals(usedInAndroid)) {
                            PayWaysBean item1 = new PayWaysBean();
                            item1.setWayName("微信支付");
                            item1.setWayId(payType);
                            item1.setIconId("2");
                            datas.add(item1);
                        } else if ("unionpay".equals(source) && "true".equals(usedInAndroid)) {
                            PayWaysBean item1 = new PayWaysBean();
                            item1.setWayName("银联支付");
                            item1.setWayId(payType);
                            item1.setIconId("3");
                            datas.add(item1);
                        }
                    }
                    if (datas.size() > 0) {
                        datas.get(0).setSelect(true);
                    }
                    initThirdViews();
                }

                String amounts = StringUtils.getJsonValue(object1, "amounts");
                if (ConstTaskTag.isTrueForArrayObj(amounts)) {
                    List<MoneyValueBean> vector = new ArrayList<>();
                    JSONArray array = new JSONArray(amounts);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        MoneyValueBean item = new MoneyValueBean();
                        item.setId(String.valueOf(i));
                        item.setIsSelect(false);
                        item.setMoneyValue(StringUtils.getJsonValue(object, "cashAmount"));
                        if ("100".equals(ConstTaskTag.getIntPrice(item.getMoneyValue()))) {
                            item.setIsSelect(true);
                        }
                        vector.add(item);
                    }
                    moneyValueAdapter.updateAreaDatas(vector);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initThirdViews() {
        // TODO Auto-generated method stub
        addPayWayView.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            PayWaysBean item = datas.get(i);
            View pView = getPayWayView();
            ImageView payWayIcon = (ImageView) pView.findViewById(R.id.payWayIcon);
            TextView payWayName = (TextView) pView.findViewById(R.id.payWayName);
            ImageView payWaySelect = (ImageView) pView.findViewById(R.id.payWaySelect);
            View lineView = pView.findViewById(R.id.lineView);
            if ("1".equals(item.getIconId())) {
                payWayIcon.setImageResource(R.drawable.zhifubao_pay);
            } else if ("2".equals(item.getIconId())) {
                payWayIcon.setImageResource(R.drawable.weixin_pay);
            } else if ("3".equals(item.getIconId())) {
                payWayIcon.setImageResource(R.drawable.union_pay);
            }
            payWayName.setText(item.getWayName());
            if (item.isSelect()) {
                payWaySelect.setImageResource(R.drawable.gou);
            } else {
                payWaySelect.setImageResource(R.drawable.gou_null);
            }
            pView.setOnClickListener(new ChoicePayWayListener(i));
            if (i == datas.size() - 1) {
                lineView.setVisibility(View.GONE);
            }
            addPayWayView.addView(pView);
        }
    }

    public View getPayWayView() {
        return LayoutInflater.from(this).inflate(R.layout.sg_payment_method_item, null);
    }

    private class ChoicePayWayListener implements OnClickListener {
        private int index;

        /**
         * <默认构造函数>
         */
        public ChoicePayWayListener(int index) {
            // TODO Auto-generated constructor stub
            this.index = index;
        }

        /**
         * 重载方法
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            updateWayDatas(index);
        }
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @param index
     * @see [类、类#方法、类#成员]
     */
    public void updateWayDatas(int index) {
        // TODO Auto-generated method stub
        for (int i = 0; i < datas.size(); i++) {
            if (i == index) {
                datas.get(index).setSelect(true);
            } else {
                datas.get(i).setSelect(false);
            }
        }

        initThirdViews();
    }

    public PayWaysBean getPayType() {
        return getPayWay();
    }

    private PayWaysBean getPayWay() {
        PayWaysBean item = null;
        for (PayWaysBean it : datas) {
            if (it.isSelect()) {
                item = it;
            }
        }
        return item;
    }

    public void getPayInfo(String record) {
        try {
            if (!TextUtils.isEmpty(record)) {
                JSONObject object = new JSONObject(record);
                payWayBean = getPayWay();
                if ("1".equals(payWayBean.getIconId())) {
                    String sdkInfo = StringUtils.getJsonValue(object, "sdkInfo");
                    requestAlipay(sdkInfo);
                } else if ("2".equals(payWayBean.getIconId())) {
                    String sdkInfo = StringUtils.getJsonValue(object, "sdkInfo");
                    JSONObject object1 = new JSONObject(sdkInfo);
                    PayReq pr = getWxPayReq(StringUtils.getJsonValue(object1, "appid"), StringUtils.getJsonValue(object1, "partnerid"), StringUtils.getJsonValue(object1, "package"),
                            StringUtils.getJsonValue(object1, "prepayid"), StringUtils.getJsonValue(object1, "noncestr"), StringUtils.getJsonValue(object1, "timestamp"), StringUtils.getJsonValue(object1, "sign"));
                    // 调用微信支付
                    payWx(wxAppId, pr);
                } else if ("3".equals(payWayBean.getIconId())) {
                    String sdkInfo = StringUtils.getJsonValue(object, "sdkInfo");
                    requestUnionPay(sdkInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDilog1(String msg) {
        OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

            @Override
            public void confrimListener(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 重载方法
     *
     * @param flag
     * @param tips
     */
    @Override
    protected void payResult(Boolean flag, String tips) {
        // TODO Auto-generated method stub
        super.payResult(flag, tips);
        if (flag) {
            MoneyValueBean moneyvalue = moneyValueAdapter.getMoneyValueBean();
            Map kv = new HashMap();
            kv.put("统计类型", "充值");
            kv.put("充值金额", ConstTaskTag.getIntPrice(moneyvalue.getMoneyValue()).concat("元"));
            TCAgent.onEvent(this, "钱包充值", "充值位", kv);
            showDilog("恭喜您，支付成功");
        } else {
            showDilog1(tips);
        }
    }

    private void showDilog(String msg) {
        OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

            @Override
            public void confrimListener(Dialog dialog) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "chongzhi");
                MoneyValueBean moneyvalue = moneyValueAdapter.getMoneyValueBean();
                intent.putExtra("moneyValue", moneyvalue.getMoneyValue());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "tixian");
                String tixianValue = data.getStringExtra("tixianValue");
                intent.putExtra("moneyValue", tixianValue);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            }
            default:
                showPayResult(data);
                break;
        }
    }
}

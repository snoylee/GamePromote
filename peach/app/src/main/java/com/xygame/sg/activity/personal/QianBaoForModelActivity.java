package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.activity.GetMoneyActivity;
import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.second.sg.personal.activity.StoneMingXiActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

public class QianBaoForModelActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName, setPwd, setAlipay, rightButtonText, incomValue, stoneValue;
    private View backButton, ZhiFuMiMaView, tiXianView, chongZhiView, bangAlipayView, bangDingLine, tiXianLine, tiXianTip, rightButton;
//    private boolean isSetPwd = false;
    private BangAlipayBean alipayBean;
    private int yuE=0,shouRu=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qianbao_model_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        stoneValue = (TextView) findViewById(R.id.stoneValue);
        incomValue = (TextView) findViewById(R.id.incomValue);
        rightButton = findViewById(R.id.rightButton);
        tiXianTip = findViewById(R.id.tiXianTip);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        chongZhiView = findViewById(R.id.chongZhiView);
        tiXianView = findViewById(R.id.tiXianView);
        tiXianLine = findViewById(R.id.tiXianLine);
        bangAlipayView = findViewById(R.id.bangAlipayView);
        bangDingLine = findViewById(R.id.bangDingLine);
        ZhiFuMiMaView = findViewById(R.id.ZhiFuMiMaView);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        setPwd = (TextView) findViewById(R.id.setPwd);
        setAlipay = (TextView) findViewById(R.id.setAlipay);
    }

    private void initListensers() {
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        bangAlipayView.setOnClickListener(this);
        ZhiFuMiMaView.setOnClickListener(this);
        tiXianView.setOnClickListener(this);
        chongZhiView.setOnClickListener(this);
    }

    private void initDatas() {
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("明细");
        titleName.setText("钱包");
        String experAuth=UserPreferencesUtil.getExpertAuth(this);
        if ( !TextUtils.isEmpty(experAuth)) {if ("2".equals(experAuth)){
                tiXianView.setVisibility(View.VISIBLE);
                tiXianLine.setVisibility(View.VISIBLE);
            }else{
            tiXianView.setVisibility(View.GONE);
            tiXianLine.setVisibility(View.GONE);
            }
        } else {
            tiXianView.setVisibility(View.GONE);
            tiXianLine.setVisibility(View.GONE);
        }
        loadStoneNums();
    }

    public void loadStoneNums() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_STONE_NUMS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_STONE_NUMS);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_STONE_NUMS:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data);
                } else {
                    stoneValue.setText("0");
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(ResponseBean data) {
        if (!"null".equals(data.getRecord()) && !TextUtils.isEmpty(data.getRecord())) {
            try {
                JSONObject object = new JSONObject(data.getRecord());
                yuE=Integer.parseInt(StringUtils.getJsonValue(object, "balance"));
                shouRu=Integer.parseInt(StringUtils.getJsonValue(object, "income"));;
                stoneValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(StringUtils.getJsonValue(object, "balance"))));
                incomValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(StringUtils.getJsonValue(object, "income"))));
//                UserPreferencesUtil.setHavePayPassword(this, StringUtils.getJsonValue(object, "hasPwd"));
                if (!object.isNull("recAccount")) {
                    String recAccount = StringUtils.getJsonValue(object, "recAccount");
                    if (!TextUtils.isEmpty(recAccount)) {
                        JSONObject object1 = new JSONObject(recAccount);
                        alipayBean = new BangAlipayBean();
                        alipayBean.setAccountName(StringUtils.getJsonValue(object1, "accountName"));
                        alipayBean.setAccountNo(StringUtils.getJsonValue(object1, "accountNo"));
                        setAlipay.setText("支付宝账户已绑定");
                        setAlipay.setTextColor(getResources().getColor(R.color.dark_green));
                    } else {
                        setAlipay.setHint("尚未绑定支付宝账户");
                    }
                } else {
                    setAlipay.setHint("尚未绑定支付宝账户");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            stoneValue.setText("0");
            setAlipay.setHint("尚未绑定支付宝账户");
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.ZhiFuMiMaView) {
//            if (isSetPwd) {
//                Intent intent1 = new Intent(this, ReSetPayPwdActivity.class);
//                startActivity(intent1);
//            } else {
//                Intent intent = new Intent(this, SetPayPwdActivity.class);
//                startActivityForResult(intent, 1);
//            }
        } else if (v.getId() == R.id.rightButton) {
            Intent intent = new Intent(this, StoneMingXiActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tiXianView) {
            Intent intent = new Intent(this, GetMoneyActivity.class);
            intent.putExtra("alipayBean",alipayBean);
            startActivityForResult(intent, 5);
        } else if (v.getId() == R.id.chongZhiView) {
            Intent intent = new Intent(this, PayStoneActivity.class);
            intent.putExtra("stoneValue", stoneValue.getText().toString());
            intent.putExtra("alipayBean",alipayBean);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.bangAlipayView) {
            if (alipayBean == null) {
                Intent intent = new Intent(this, BangAlipayActivity.class);
                startActivityForResult(intent, 2);
            } else {
                Intent intent = new Intent(this, UnBangAlipayActivity.class);
//                intent.putExtra("isSetPwd", isSetPwd);
                intent.putExtra("alipayBean", alipayBean);
                startActivityForResult(intent, 3);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("chongzhi".equals(flag)) {
                    String moneyValue=data.getStringExtra("moneyValue");
                    int tiXian=Integer.parseInt(moneyValue);
                    yuE=yuE+tiXian;
                    stoneValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(yuE))));
                }else if ("tixian".equals(flag)){
                    String moneyValue=data.getStringExtra("moneyValue");
                    int tiXian=Integer.parseInt(moneyValue);
                    yuE=yuE-tiXian;
                    stoneValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(yuE))));
                }else  if (Constants.COMEBACK.equals(flag)) {
                    Intent intent = new Intent(QianBaoForModelActivity.this, BangAlipayActivity.class);
                    startActivityForResult(intent, 2);
                }
                break;
            }
            case 1:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String money = data.getStringExtra(Constants.COMEBACK);
                if (money != null) {
                    UserPreferencesUtil.setHavePayPassword(this, "1");
//                    isSetPwd = true;
                    setPwd.setText("密码已设置");
                    setPwd.setTextColor(getResources().getColor(R.color.dark_green));
                }
                break;
            case 2:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flag1 = data.getStringExtra(Constants.COMEBACK);
                if (Constants.COMEBACK.equals(flag1)) {
                    setAlipay.setText("支付宝账户已绑定");
                    setAlipay.setTextColor(getResources().getColor(R.color.dark_green));
                    alipayBean = (BangAlipayBean) data.getSerializableExtra("bean");
                }
                break;
            case 3:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flag = data.getStringExtra(Constants.COMEBACK);
                if (Constants.COMEBACK.equals(flag)) {
                    setAlipay.setText("");
                    setAlipay.setHint("尚未绑定支付宝账户");
                    alipayBean = null;
                }
                break;
            case 4:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                long lastStones = data.getLongExtra("lastStones", 0);
                if (lastStones != 0) {
                    stoneValue.setText(String.valueOf(lastStones));
                }
                break;
            case 5:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String isTrue = data.getStringExtra(Constants.COMEBACK);
                if (Constants.COMEBACK.equals(isTrue)) {
                    Intent intent = new Intent(QianBaoForModelActivity.this, BangAlipayActivity.class);
                    startActivityForResult(intent, 2);
                }else if ("tixian".equals(isTrue)){
                    String tiMoney=data.getStringExtra("tiMoney");
                    int tiXian=Integer.parseInt(tiMoney);
                    shouRu=shouRu-tiXian;
                    incomValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(shouRu))));
                }
                break;
            default:
                break;
        }

    }
}

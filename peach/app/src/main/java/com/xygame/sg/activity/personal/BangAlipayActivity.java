package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.PatternUtils;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

public class BangAlipayActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName;
	private View backButton, comfirm;
	private EditText name,alipayCount;
	private BangAlipayBean alipayBean;
	public String getalipayCount(){
		return alipayCount.getText().toString().trim();
	}
	
	public String getName(){
		return name.getText().toString().trim();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bang_alipay_layout);
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		name=(EditText)findViewById(R.id.name);
		alipayCount=(EditText)findViewById(R.id.alipayCount);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	private void initDatas() {
		titleName.setText("绑定支付宝");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			if("".equals(name.getText().toString().trim())){
				Toast.makeText(getApplicationContext(), "请输入您的真实姓名", Toast.LENGTH_SHORT).show();
			}else{
				String phone=alipayCount.getText().toString().trim();
				if("".equals(phone)){
					Toast.makeText(getApplicationContext(), "请输入您的支付宝账户", Toast.LENGTH_SHORT).show();
				}else{
					if (phone.matches(PatternUtils.MOBILE_PHONE)||phone.matches(PatternUtils.EMAIL)){
						commitPay();
					}else{
						Toast.makeText(getApplicationContext(), "支付宝账户格式不正确", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}
	
	private void commitPay() {
		String accountName=getName();
		String accountNo=getalipayCount();
		RequestBean item = new RequestBean();
		try {
			JSONObject obj = new JSONObject();
			obj.put("accountName",accountName);
			obj.put("accountNo",accountNo);
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ShowMsgDialog.show(this, "请求中...", false);
		item.setServiceURL(ConstTaskTag.QUEST_SET_ZFB);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SET_ZFB);
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_SET_ZFB:
				if ("0000".equals(data.getCode())) {
					alipayBean=new BangAlipayBean();
					alipayBean.setAccountNo(alipayCount.getText().toString().trim());
					alipayBean.setAccountName(name.getText().toString().trim());
					Intent intent = new Intent();
					intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
					intent.putExtra("bean",alipayBean);
					setResult(Activity.RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	protected void responseFaith() {
		super.responseFaith();
		Toast.makeText(this, "绑定失败", Toast.LENGTH_SHORT).show();
	}
}

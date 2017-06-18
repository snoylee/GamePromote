package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

public class ReSetPayPwdActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName;
	private View backButton, comfirm;
	private EditText firstPwd, secondPwd,oldPwd;
	
	public String getOldPws(){
		return oldPwd.getText().toString().trim();
	}
	
	public String getNewPws(){
		return firstPwd.getText().toString().trim();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_pay_pwd_layout);
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		firstPwd = (EditText) findViewById(R.id.firstPwd);
		secondPwd = (EditText) findViewById(R.id.secondPwd);
		oldPwd=(EditText)findViewById(R.id.oldPwd);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	private void initDatas() {
		titleName.setText("设置支付密码");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			if (isgo()) {
				commitPay();
			}
		}
	}

	private boolean isgo() {
		boolean flag = true;
		String fP = firstPwd.getText().toString().trim();
		String sp = secondPwd.getText().toString().trim();
		String op=oldPwd.getText().toString().trim();
		if ("".equals(op)) {
			Toast.makeText(getApplicationContext(), "请输入旧支付密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(fP)) {
			Toast.makeText(getApplicationContext(), "请输入6位支付密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fP.length() > 6) {
			Toast.makeText(getApplicationContext(), "支付密码长度在6位之内", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(sp)) {
			Toast.makeText(getApplicationContext(), "请输入确认支付密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (sp.length() > 6) {
			Toast.makeText(getApplicationContext(), "确认密码长度在6位之内", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!fP.equals(sp)) {
			Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fP.equals(sp)) {
			return true;
		}
		return flag;
	}

	private void commitPay() {
		String password = DigestUtils.md5Hex(getNewPws());
		String olderPwd=DigestUtils.md5Hex(getOldPws());
		RequestBean item = new RequestBean();
		try {
			JSONObject obj = new JSONObject();
			obj.put("oldPassword",olderPwd);
			obj.put("opsPassword",password);
			obj.put("type",2);
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ShowMsgDialog.show(this, "密码设置中...", false);
		item.setServiceURL(ConstTaskTag.QUEST_SET_PWD);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SET_PWD);
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_SET_PWD:
				if ("0000".equals(data.getCode())) {
					parseDatas(data);
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void parseDatas(ResponseBean data) {
		if ("0000".equals(data.getCode())) {
			Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void responseFaith() {
		super.responseFaith();
		Toast.makeText(this, "密码设置失败", Toast.LENGTH_SHORT).show();
	}
}

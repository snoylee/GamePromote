package com.xygame.sg.activity.personal;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.define.view.MyEditText;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class UnBangDialogActivity extends SGBasePaymentActivity implements OnClickListener ,MyEditText.OnEditTextListener{
	/**
	 * 公用变量部分
	 */
	private View closeLoginWel;
	private MyEditText inputPwd;

	private String pwdStr;
	
	public String getPwd(){
		return pwdStr;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_unbang_dialog_layout);
		initViews();
		initListensers();
	}

	private void initViews() {
		closeLoginWel = findViewById(R.id.closeLoginWel);
		inputPwd=(MyEditText)findViewById(R.id.inputPwd);
	}

	private void initListensers() {
		closeLoginWel.setOnClickListener(this);
		inputPwd.setOnEditTextListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.closeLoginWel) {
			finish();
		}
	}
	
	private void commitPay() {
		String password= DigestUtils.md5Hex(getPwd());
		RequestBean item = new RequestBean();
		try {
			JSONObject obj = new JSONObject();
//			obj.put("opsPassword",password);
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ShowMsgDialog.show(this, "请求中...", false);
		item.setServiceURL(ConstTaskTag.QUEST_LOSE_ZFB);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_LOSE_ZFB);
	}

	@Override
	public void inputComplete(int state, String password) {
		pwdStr=password;
		commitPay();
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_LOSE_ZFB:
				if ("0000".equals(data.getCode())) {
					Toast.makeText(getApplicationContext(), "解绑成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
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

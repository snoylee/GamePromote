package com.xygame.sg.activity.personal;

import java.util.Map;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.activity.notice.bean.ZhuiJiaBean;
import com.xygame.sg.define.view.MyEditText;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class ZhuiJiaDialogActivity extends SGBasePaymentActivity implements OnClickListener ,MyEditText.OnEditTextListener{
	/**
	 * 公用变量部分
	 */
	private View closeLoginWel;
	private MyEditText inputPwd;
	private boolean isPaySuccess=false;
	private ZhuiJiaBean zjBean;
	private String pwdStr;

	public ZhuiJiaBean getZjBean() {
		return zjBean;
	}

	public String getPwd() {
		return pwdStr;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_unbang_dialog_layout, null));
		initViews();
		initListensers();
		zjBean=(ZhuiJiaBean) getIntent().getSerializableExtra("bean");
	}

	private void initViews() {
		closeLoginWel = findViewById(R.id.closeLoginWel);
		inputPwd = (MyEditText) findViewById(R.id.inputPwd);
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

	@Override
	public void inputComplete(int state, String password) {
		pwdStr=password;
		commitPay();
	}

	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.AccountPayTask(${membersUpdate})", this, null, visit).run();
	}

	public void getPayInfo(Map map) {
//		1:担保交易支付成功或者启用余额支付余额大于等于待支付金额支付成功
//		2:不启用余额支付或者钱包余额小于待支付金额唤起第三方支付sdk
//		3:交易出错,请重试
//		4:支付密码错误
		String status = map.get("status").toString();
		switch (Integer.parseInt(status)) {
		case 1:
			isPaySuccess = true;
			showDilog("恭喜您，支付成功");
			break;
		case 3:
			showDilog1("交易出错,请重试");
			isPaySuccess = false;
			break;
		case 4:
			showDilog1("支付密码错误");
			isPaySuccess = false;
			break;
		default:
			break;
		}
	}

	private void showDilog(String msg) {
		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}

	private void showDilog1(String msg) {
		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, isPaySuccess);
		setResult(Activity.RESULT_OK, intent);
		super.finish();
	}
}

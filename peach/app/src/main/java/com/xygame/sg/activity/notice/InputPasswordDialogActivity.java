package com.xygame.sg.activity.notice;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.define.view.MyEditText;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.frame.VisitUnit;

public class InputPasswordDialogActivity extends SGBasePaymentActivity implements OnClickListener,MyEditText.OnEditTextListener {
	/**
	 * 公用变量部分
	 */
	private View closeLoginWel, comfirm;
	private MyEditText inputPwd;
	private TextView priceText,fromTo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_unbang_dialog_layout, null));
		initViews();
		initListensers();
//		initDatas();
	}

	private void initDatas() {
		fromTo.setText(getIntent().getStringExtra("fromTo"));
		priceText.setText("￥".concat(getIntent().getStringExtra("price")));
	}

	private void initViews() {
		closeLoginWel = findViewById(R.id.closeLoginWel);
		priceText=(TextView)findViewById(R.id.priceText);
		fromTo=(TextView)findViewById(R.id.fromTo);
		inputPwd = (MyEditText) findViewById(R.id.inputPwd);
//		inputPwd.requestFocus();
//		inputPwd.setFocusable(true);
	}

	private void initListensers() {
		closeLoginWel.setOnClickListener(this);
		inputPwd.setOnEditTextListener(this);
//		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//		imm.showSoftInput(inputPwd, InputMethodManager.SHOW_FORCED);
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
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK,password);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}

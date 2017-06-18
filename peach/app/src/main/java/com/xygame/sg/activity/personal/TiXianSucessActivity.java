package com.xygame.sg.activity.personal;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import base.ViewBinder;
import base.frame.VisitUnit;

public class TiXianSucessActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName,seccuessMoney;
	private View backButton, comfirm;
	private String money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.tixian_sucess_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		seccuessMoney=(TextView)findViewById(R.id.seccuessMoney);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	private void initDatas() {
		money=getIntent().getStringExtra("money");
		titleName.setText("提现");
		String tixian="￥".concat(String.valueOf(Float.parseFloat(money)));
		seccuessMoney.setText(tixian);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			finish();
		}
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, "Y");
		setResult(Activity.RESULT_OK, intent);
		super.finish();
	}
}

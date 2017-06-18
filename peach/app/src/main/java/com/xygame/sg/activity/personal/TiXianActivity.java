package com.xygame.sg.activity.personal;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.notice.InputPasswordDialogActivity;
import com.xygame.sg.activity.notice.bean.QianBaoBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.KeyEventListener;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class TiXianActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName,ketiMoney,yuMoney;
	private View backButton, comfirm;
	private QianBaoBean qBean;
	private EditText tixianMoney;

	private String payPassword;

	public String getPayPassword() {
		return payPassword;
	}
	
	public String gettixianMoney(){
		return tixianMoney.getText().toString().trim();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.tixian_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		ketiMoney=(TextView)findViewById(R.id.ketiMoney);
		yuMoney=(TextView)findViewById(R.id.yuMoney);
		tixianMoney=(EditText)findViewById(R.id.tixianMoney);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
		tixianMoney.setOnKeyListener(new KeyEventListener());
		tixianMoney.setFilters(new InputFilter[] { StringUtils.getLengthFilter() });
	}

	private void initDatas() {
		qBean=(QianBaoBean) getIntent().getSerializableExtra("bean");
		titleName.setText("提现");
		String tixian="￥".concat(String.valueOf(StringUtil.getPrice(Long.parseLong(qBean.getAmount()))));
		ketiMoney.setText(tixian);
		yuMoney.setText(tixian);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			if("".equals(tixianMoney.getText().toString().trim())){
				Toast.makeText(getApplicationContext(), "请输入提现金额", Toast.LENGTH_SHORT).show();
			}else{
				if(StringUtil.getPrice(Long.parseLong(qBean.getAmount()))>0){
					if (UserPreferencesUtil.havePayPassword(this)) {
						Intent intent = new Intent(this, InputPasswordDialogActivity.class);
						startActivityForResult(intent, 1);
					} else {
						Toast.makeText(getApplicationContext(), "请先设置您的支付密码", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "余额不足", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private void commitPay() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.TiXianPayTask(${refund})", this, null, visit).run();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String money=data.getStringExtra(Constants.COMEBACK);
			if(money!=null){
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, "Y");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}			
			break;
		}
			case 1: {
				if (Activity.RESULT_OK != resultCode || null == data) {
					return;
				}
				payPassword = data.getStringExtra(Constants.COMEBACK);
				if (payPassword != null) {
					commitPay();
				}

				break;
			}
		default:
			break;
		}

	}

	public void finishTiXian() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this, TiXianSucessActivity.class);
		intent.putExtra("money", tixianMoney.getText().toString().trim());
		startActivityForResult(intent, 0);
	}
}

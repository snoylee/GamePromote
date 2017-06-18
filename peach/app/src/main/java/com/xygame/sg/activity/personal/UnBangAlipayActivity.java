package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.bean.BangAlipayBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

public class UnBangAlipayActivity extends SGBasePaymentActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName;
	private View backButton, comfirm;
	private TextView name,alipayCount;
	private BangAlipayBean alipayBean;
//	private boolean isSetPwd;
	private String[] arrayXing={"*","**","***","****","*****","******","*******","********"};
	
	public String getName(){
		return name.getText().toString().trim();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unbang_alipay_layout);
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		comfirm = findViewById(R.id.comfirm);
		name=(TextView)findViewById(R.id.name);
		alipayCount=(TextView)findViewById(R.id.alipayCount);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	private void initDatas() {
		titleName.setText("绑定支付宝");
//		isSetPwd=getIntent().getBooleanExtra("isSetPwd", false);
		alipayBean=(BangAlipayBean) getIntent().getSerializableExtra("alipayBean");
		String nameStr=alipayBean.getAccountName();
		String noCount=alipayBean.getAccountNo();
		if(nameStr.length()>8){
			char chas=nameStr.charAt(nameStr.length()-1);
			String chaStr=String.valueOf(chas);
			name.setText(arrayXing[7].concat(chaStr));
		}else{
			char chas=nameStr.charAt(nameStr.length()-1);
			String chaStr=String.valueOf(chas);
			name.setText(arrayXing[nameStr.length()-2].concat(chaStr));
		}
		alipayCount.setText(arrayXing[5].concat(noCount.substring(noCount.length() - 3, noCount.length())));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			showComfirmDialog("确定要解除绑定");
//			if(!isSetPwd){
//				Toast.makeText(getApplicationContext(), "请先设置您的支付密码", Toast.LENGTH_SHORT).show();
//			}else{
//				Intent intent=new Intent(this, UnBangDialogActivity.class);
//				startActivityForResult(intent, 0);
//			}
		}
	}

	private void showComfirmDialog(String tip){
		OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						commitPay();
					}
				});
				dialog.show();
	}

	private void commitPay() {
		RequestBean item = new RequestBean();
		try {
			JSONObject obj = new JSONObject();
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ShowMsgDialog.show(this, "请求中...", false);
		item.setServiceURL(ConstTaskTag.QUEST_LOSE_ZFB);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_LOSE_ZFB);
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
}

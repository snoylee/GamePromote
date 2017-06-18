/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class PlushNoticePaySuccessActivity extends SGBaseActivity implements OnClickListener{

	private View comfirmButton;
	private PlushNoticeBean pnBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_plush_pay_sucess_layout, null));
		pnBean=(PlushNoticeBean) getIntent().getSerializableExtra("bean");
		comfirmButton = findViewById(R.id.comfirmButton);
		comfirmButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.comfirmButton) {
			Intent intent=new Intent(this, NoticePaymentActivity.class);
			intent.putExtra("bean", pnBean);
			startActivity(intent);
			Intent intent1 = new Intent();
			intent1.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
			setResult(Activity.RESULT_OK, intent1);
			finish();
		}
	}
}

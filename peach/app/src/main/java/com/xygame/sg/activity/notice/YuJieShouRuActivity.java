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
import com.xygame.sg.activity.notice.bean.NoticeStatusBeanForModel;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class YuJieShouRuActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName,yuJiJieSuan,shouXuPrice,gaiPrice,sxValue;
	private View backButton;
	private NoticeStatusBeanForModel nsBean;
	private float sxf=0;
	private String sxfBaiFenBi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.yuji_shouru_layout, null));
		initViews();
		initListeners();
		initDatas();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		yuJiJieSuan=(TextView)findViewById(R.id.yuJiJieSuan);
		backButton = findViewById(R.id.backButton);
		shouXuPrice=(TextView)findViewById(R.id.shouXuPrice);
		gaiPrice=(TextView)findViewById(R.id.gaiPrice);
		sxValue=(TextView)findViewById(R.id.sxValue);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		titleName.setText("预计收入");
		sxf=getIntent().getFloatExtra("sxf", 0);
		sxfBaiFenBi=getIntent().getStringExtra("sxfBaiFenBi");
		nsBean=(NoticeStatusBeanForModel) getIntent().getSerializableExtra("bean");
		sxValue.setText("通告手续费（"+sxfBaiFenBi+"%）：");
		updateAllViews();
	}

	private void updateAllViews() {
		// TODO Auto-generated method stub
		yuJiJieSuan.setText("￥".concat(nsBean.getFinalAmount()));
		shouXuPrice.setText("-￥".concat(String.valueOf(sxf)));
		gaiPrice.setText("￥".concat((String.valueOf(Float.parseFloat(nsBean.getFinalAmount())-sxf))));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		}
	}
}

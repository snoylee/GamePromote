/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBasePaymentActivity;
import com.xygame.sg.activity.notice.bean.JieSuanInfoBean;
import com.xygame.sg.utils.StringUtil;

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
public class YuFuTongJiActivity extends SGBasePaymentActivity implements OnClickListener {

	private TextView titleName,yuFuPrice,yiLuyunRen,yiLunYunPrice,yipaiSheRen,yipaiShePrice,totalPrice;
	private View backButton;
	private JieSuanInfoBean jsInfoBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.zhifu_tongji_layout, null));

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
		totalPrice = (TextView) findViewById(R.id.totalPrice);
		yipaiSheRen = (TextView) findViewById(R.id.yipaiSheRen);
		yipaiShePrice = (TextView) findViewById(R.id.yipaiShePrice);
		yiLuyunRen = (TextView) findViewById(R.id.yiLuyunRen);
		yiLunYunPrice = (TextView) findViewById(R.id.yiLunYunPrice);
		yuFuPrice = (TextView) findViewById(R.id.yuFuPrice);
		backButton = findViewById(R.id.backButton);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		jsInfoBean = (JieSuanInfoBean) getIntent().getSerializableExtra("bean");
		titleName.setText("预付款统计");
		updateViews();
	}

	private void updateViews() {
		// TODO Auto-generated method stub
		yuFuPrice.setText("￥".concat(String.valueOf(Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(jsInfoBean.getParpayAmount())))))));
		yiLuyunRen.setText(jsInfoBean.getFrozenCount());
		yiLunYunPrice.setText("-￥".concat(String.valueOf(Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(jsInfoBean.getFrozenAmount())))))));
		yipaiSheRen.setText(jsInfoBean.getShootCount());
		yipaiShePrice.setText("-￥".concat(String.valueOf(Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(jsInfoBean.getIncomeAmount())))))));
		
		totalPrice.setText("￥".concat(String.valueOf(Float.parseFloat(String.valueOf(StringUtil.getPrice(Long.parseLong(jsInfoBean.getAvailAmount())))))));
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		}
	}
}

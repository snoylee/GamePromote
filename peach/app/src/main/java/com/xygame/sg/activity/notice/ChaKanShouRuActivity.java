/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.math.BigDecimal;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.NoticeStatusBeanForModel;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class ChaKanShouRuActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName,yuJiJieSuan,shouXuPrice,gaiPrice,userName,zhaomuTxt,timer;
	private View backButton;
	private ImageLoader imageLoader;
	private CircularImage userImage;
	private NoticeStatusBeanForModel nsBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.chakan_shouru_layout, null));
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
		userImage=(CircularImage)findViewById(R.id.userImage);
		gaiPrice=(TextView)findViewById(R.id.gaiPrice);
		userName=(TextView)findViewById(R.id.userName);
		zhaomuTxt=(TextView)findViewById(R.id.zhaomuTxt);
		timer=(TextView)findViewById(R.id.timer);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		nsBean=(NoticeStatusBeanForModel) getIntent().getSerializableExtra("bean");
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		titleName.setText("查看收入");
		updateAllViews();
		loadShouXuFeiDatas();
	}

	private void updateAllViews() {
		// TODO Auto-generated method stub
		userName.setText(nsBean.getUserNick());
		int j = Integer.parseInt(nsBean.getOrders());
		String[] numArray = Constants.CHARACTE_NUMS;
		if (j > numArray.length - 1) {
			zhaomuTxt.setText("招募 " + (j + 1));
		} else {
			zhaomuTxt.setText("招募".concat(numArray[j]));
		}
		
		if(!"null".equals(nsBean.getFinishTime())){
			timer .setVisibility(View.VISIBLE);
			timer .setText(
					"结算时间：".concat(CalendarUtils.getHenGongDateDis(Long.parseLong(nsBean.getFinishTime()))));
		}else{
			timer .setVisibility(View.GONE);
		}
		yuJiJieSuan.setText("￥".concat(nsBean.getFinalAmount()));
		imageLoader.loadImage(nsBean.getUserIcon(), userImage, false);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		}
	}
	
	private void loadShouXuFeiDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadShouXuFeiTask2(${queryNoticeLiquidatFee})", this, null, visit).run();
	}
	
	public void finishLoadSXF(String sx) {
		// TODO Auto-generated method stub
		float sxf=Float.parseFloat(nsBean.getFinalAmount())*(Float.parseFloat(sx)/100);
		BigDecimal b = new BigDecimal(sxf);
		sxf = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		shouXuPrice.setText(String.valueOf(sxf));
		gaiPrice.setText("￥".concat(String.valueOf(Float.parseFloat(nsBean.getFinalAmount())-sxf)));
	}
}

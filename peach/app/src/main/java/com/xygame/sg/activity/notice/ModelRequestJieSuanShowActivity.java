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
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;

import android.content.Intent;
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
public class ModelRequestJieSuanShowActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName,userName,zhaomuTxt,daiJieSuan,gaiPrice,timer;
	private View backButton,changeJieSuanView,comfirm;
	private CircularImage userImage;
	private ImageLoader imageLoader;
	private TextView contentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.model_request_jiesuan_show_layout, null));
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
		changeJieSuanView.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		timer=(TextView)findViewById(R.id.timer);
		titleName = (TextView) findViewById(R.id.titleName);
		daiJieSuan=(TextView)findViewById(R.id.daiJieSuan);
		backButton = findViewById(R.id.backButton);
		changeJieSuanView=findViewById(R.id.changeJieSuanView);
		userImage=(CircularImage)findViewById(R.id.userImage);
		userName=(TextView)findViewById(R.id.userName);
		zhaomuTxt=(TextView)findViewById(R.id.zhaomuTxt);
		gaiPrice=(TextView)findViewById(R.id.gaiPrice);
		contentText=(TextView)findViewById(R.id.contentText);
		comfirm=findViewById(R.id.comfirm);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		titleName.setText("拍摄完成要求结算");
		updateAllViews();
		toPayDatas();
	}

	private void updateAllViews() {
		// TODO Auto-generated method stub
//		userName.setText(nsBean.getUserNick());
	}
	

	private void toPayDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.ToPayTask(${toPay})", this, null, visit).run();
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if(v.getId()==R.id.changeJieSuanView){
			Intent intent=new Intent(this, YuJieShouRuActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.comfirm){
		}
	}
	
//	public void getPayInfo(Map map) {
//		String status = map.get("status").toString();
//		switch (Integer.parseInt(status)) {
//		case 1:
//			isPaySuccess = true;
//			showDilog("恭喜您，支付成功");
//			break;
//		case 3:
//			isPaySuccess = false;
//			showDilog1("交易出错,请重试");
//			break;
//		case 4:
//			isPaySuccess = false;
//			showDilog1("支付密码错误");
//			break;
//		default:
//			break;
//		}
//	}
//
//	private void showDilog(String msg) {
//		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {
//
//			@Override
//			public void confrimListener(Dialog dialog) {
//				dialog.dismiss();
//				Intent intent = new Intent("com.sg.jiesuan.pay.scuess.action");
//				intent.putExtra(Constants.COMEBACK, isPaySuccess);
//				intent.putExtra("userId", nsBean.getUserId());
//				sendBroadcast(intent);
//				finish();
//			}
//		});
//		dialog.show();
//	}
//
//	private void showDilog1(String msg) {
//		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog, new ButtonOneListener() {
//
//			@Override
//			public void confrimListener(Dialog dialog) {
//				dialog.dismiss();
//			}
//		});
//		dialog.show();
//	}
}

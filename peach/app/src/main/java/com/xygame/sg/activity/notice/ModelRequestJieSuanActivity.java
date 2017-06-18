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
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
public class ModelRequestJieSuanActivity extends SGBaseActivity implements OnClickListener, TextWatcher {

	private TextView titleName, userName, zhaomuTxt, daiJieSuan, gaiPrice, numCount, contentTextShow;
	private View backButton, changeJieSuanView, comfirm, showEditor;
	private CircularImage userImage;
	private ImageLoader imageLoader;
	private EditText contentText;
	private NoticeStatusBeanForModel nsBean;
	private String noticeId, payPassword;
	private float sxf = 0;
	private String sxfBaiFenBi;
	private String checkFlag;

	public String getMenId() {
		return nsBean.getMemId();
	}

	public String getNoticeId() {
		return noticeId;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public String getLiuYan() {
		return contentText.getText().toString().trim();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.model_request_jiesuan_layout, null));
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
		contentText.addTextChangedListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		contentTextShow = (TextView) findViewById(R.id.contentTextShow);
		titleName = (TextView) findViewById(R.id.titleName);
		daiJieSuan = (TextView) findViewById(R.id.daiJieSuan);
		numCount = (TextView) findViewById(R.id.numCount);
		backButton = findViewById(R.id.backButton);
		changeJieSuanView = findViewById(R.id.changeJieSuanView);
		userImage = (CircularImage) findViewById(R.id.userImage);
		userName = (TextView) findViewById(R.id.userName);
		zhaomuTxt = (TextView) findViewById(R.id.zhaomuTxt);
		gaiPrice = (TextView) findViewById(R.id.gaiPrice);
		contentText = (EditText) findViewById(R.id.contentText);
		comfirm = findViewById(R.id.comfirm);
		showEditor = findViewById(R.id.showEditor);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		numCount.setText("0/200");
		noticeId = getIntent().getStringExtra("noticeId");
		nsBean = (NoticeStatusBeanForModel) getIntent().getSerializableExtra("bean");
		checkFlag = getIntent().getStringExtra("checkFlag");
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		titleName.setText("拍摄完成，要求结算");
		if ("checkFlag".equals(checkFlag)) {
			contentTextShow.setVisibility(View.VISIBLE);
			if(!"null".equals(nsBean.getApplyDesc())){
				contentTextShow.setText(nsBean.getApplyDesc());
			}
			showEditor.setVisibility(View.GONE);
			comfirm.setVisibility(View.GONE);
		} else {
			contentTextShow.setVisibility(View.GONE);
			showEditor.setVisibility(View.VISIBLE);
			comfirm.setVisibility(View.VISIBLE);
		}
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
		daiJieSuan.setText("￥".concat(nsBean.getFinalAmount()));
		imageLoader.loadImage(nsBean.getUserIcon(), userImage, true);
	}

	private void loadShouXuFeiDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadShouXuFeiTask(${queryNoticeLiquidatFee})", this, null, visit).run();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.changeJieSuanView) {
			Intent intent = new Intent(this, YuJieShouRuActivity.class);
			intent.putExtra("sxf", sxf);
			intent.putExtra("sxfBaiFenBi",sxfBaiFenBi);
			intent.putExtra("bean", nsBean);
			startActivity(intent);
		} else if (v.getId() == R.id.comfirm) {
			if (UserPreferencesUtil.havePayPassword(this)) {
				Intent intent = new Intent(this, InputPasswordDialogActivity.class);
				intent.putExtra("price",String.valueOf(Float.parseFloat(nsBean.getFinalAmount()) - sxf));
				intent.putExtra("fromTo",nsBean.getUserNick().concat("要求付款"));
				startActivityForResult(intent, 0);
			} else {
				Toast.makeText(getApplicationContext(), "请先设置您的支付密码", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			payPassword = data.getStringExtra(Constants.COMEBACK);
			if (payPassword != null) {
				commitJieSuan();
			}

			break;
		}
		default:
			break;
		}

	}

	private void commitJieSuan() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.CommitJieSuanForModelTask(${ackShootDone})", this, null, visit).run();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String str = s.toString();
		numCount.setText(str.length() + "/200");
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	public void finishLoadSXF(String sx) {
		sxfBaiFenBi=sx;
		sxf = Float.parseFloat(nsBean.getFinalAmount()) * (Float.parseFloat(sx) / 100);
		BigDecimal b = new BigDecimal(sxf);
		sxf = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		gaiPrice.setText("￥".concat(String.valueOf(Float.parseFloat(nsBean.getFinalAmount()) - sxf)));
	}

	public void finishCommit() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.sg.jiesuan.pay.scuess.formodel.action");
		intent.putExtra("userId", nsBean.getUserId());
		intent.putExtra("liuyan",getLiuYan());
		sendBroadcast(intent);
		finish();
	}
}

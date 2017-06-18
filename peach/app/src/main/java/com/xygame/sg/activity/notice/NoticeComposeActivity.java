/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.adapter.ComposeAdapter;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.task.notice.ComposeRegisterTask;
import com.xygame.sg.task.notice.LoadAllComposeTask;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * @author 王琪
 * @date 2015年11月9日
 * @action [我的优惠券]
 */
public class NoticeComposeActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {
	private ComposeBean SelectItem;
	private TextView titleName, rightButtonText;
	private View backButton, duiHuanButton, rightButton;
	private ListView composeList;
	private ComposeAdapter adapter;
	private PayWaysBean payWayBean;
	private PlushNoticeBean pnBean;
	private EditText duiHuanMa;

	public String getduiHuanMa() {
		return duiHuanMa.getText().toString().trim();
	}

	public PlushNoticeBean getPnBean() {
		return pnBean;
	}

	public PayWaysBean getPayWayBean() {
		return payWayBean;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.compose_layout, null));

		initViews();
		initListeners();
		initDatas();
	}


	private void initListeners() {
		backButton.setOnClickListener(this);
		duiHuanButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		composeList.setOnItemClickListener(this);
	}


	private void initViews() {
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		composeList = (ListView) findViewById(R.id.composeList);
		backButton = findViewById(R.id.backButton);
		duiHuanButton = findViewById(R.id.duiHuanButton);
		duiHuanMa = (EditText) findViewById(R.id.duiHuanMa);
		rightButton = findViewById(R.id.rightButton);
	}


	private void initDatas() {
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		titleName.setText("我的优惠券");
		rightButtonText.setText("确定");
		adapter = new ComposeAdapter(this, null);
		composeList.setAdapter(adapter);
		payWayBean = (PayWaysBean) getIntent().getSerializableExtra("payWayBean");
		pnBean = (PlushNoticeBean) getIntent().getSerializableExtra("pnBean");
		loadAllCouponsDatas();
	}


	private void loadAllCouponsDatas() {
		VisitUnit visit = new VisitUnit();
		new Action(LoadAllComposeTask.class,"${getAllCoupons}", this, null, visit).run();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.rightButton) {
			ComposeBean it = adapter.getComposeBean();
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, it);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.duiHuanButton) {
			if (!"".equals(duiHuanMa.getText().toString().trim())) {
				uploadScanerInfo();
			} else {
				Toast.makeText(getApplicationContext(), "输入优惠券兑换码", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void finishLoadCompose(List<Map> map) {
		if (map!=null){
			List<ComposeBean> datas = new ArrayList<ComposeBean>();
			for (Map subMap : map) {
				ComposeBean item = new ComposeBean();
				item.setCompId(subMap.get("couponCode").toString());
				item.setManPrice(String.valueOf(StringUtil.getPrice(Long.parseLong(subMap.get("minimumPrice").toString()))));
				item.setSelect(false);
				item.setTipText(subMap.get("coupDesc").toString());
				item.setUseDate(subMap.get("endTime").toString());
				item.setYouHuiPrice(String.valueOf(StringUtil.getPrice(Long.parseLong(subMap.get("offAmount").toString()))));
				item.setAmount(String.valueOf(StringUtil.getPrice(Long.parseLong(subMap.get("amount").toString()) )));
				item.setUseType(subMap.get("useType").toString());
				item.setCoupName(subMap.get("coupName").toString());
				item.setOffdiscount(subMap.get("offDiscount").toString());
				item.setCanUse(subMap.get("canUse").toString());
				datas.add(item);
			}
			adapter.clearDatas();
			adapter.addDatas(datas);
		}

	}

	/**
	 * 重载方法
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		adapter.updateSelected(arg2);
	}

	private void uploadScanerInfo() {
		VisitUnit visit = new VisitUnit();
		new Action(ComposeRegisterTask.class,"${registCouponCode}", this, null, visit).run();
	}

	public void finishUpload() {
		duiHuanMa.setText("");
		OneButtonDialog dialog = new OneButtonDialog(NoticeComposeActivity.this, "兑换成功", R.style.dineDialog,
				new ButtonOneListener() {
					@Override
					public void confrimListener(Dialog dialog) {
						loadAllCouponsDatas();
						dialog.dismiss();
					}
				});
		dialog.show();
	}
}

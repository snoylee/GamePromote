/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.ComposeAdapter;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.notice.bean.QianBaoBean;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
public class ComposeActivity extends SGBaseActivity implements OnClickListener,OnItemClickListener{
	private ComposeBean SelectItem;
	private TextView titleName,rightButtonText;
	private View backButton,duiHuanButton,rightButton;
	private ListView composeList;
	private ComposeAdapter adapter;
	private PayWaysBean payWayBean;
	private PlushNoticeBean pnBean;
	
	public PlushNoticeBean getPnBean() {
		return pnBean;
	}
	public PayWaysBean getPayWayBean() {
		return payWayBean;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.compose_layout, null));

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
		duiHuanButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
//		composeList.setOnItemClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		composeList=(ListView)findViewById(R.id.composeList);
		backButton = findViewById(R.id.backButton);
		duiHuanButton = findViewById(R.id.duiHuanButton);
		rightButton=findViewById(R.id.rightButton);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		rightButton.setVisibility(View.GONE);
		rightButtonText.setVisibility(View.GONE);
		titleName.setText("我的优惠券");
		rightButtonText.setText("确定");
		adapter=new ComposeAdapter(this, null);
		composeList.setAdapter(adapter);
		payWayBean=(PayWaysBean) getIntent().getSerializableExtra("payWayBean");
		pnBean=(PlushNoticeBean) getIntent().getSerializableExtra("pnBean");
		loadAllCouponsDatas();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void loadAllCouponsDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadAllComposeTask(${getAllCoupons})", this, null, visit).run();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		}else if(v.getId()==R.id.rightButton){
			ComposeBean it=adapter.getComposeBean();
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, it);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void finishLoadCompose(List<Map> map) {
		List<ComposeBean> datas=new ArrayList<ComposeBean>();
		for(Map subMap:map){
			ComposeBean item=new ComposeBean();
			item.setCompId(subMap.get("couponCode").toString());
			item.setManPrice(String.valueOf(Long.parseLong(subMap.get("minimumPrice").toString()) / 100));
			item.setSelect(false);
			item.setTipText(subMap.get("coupDesc").toString());
			item.setUseDate(subMap.get("endTime").toString());
			item.setYouHuiPrice(String.valueOf(Long.parseLong(subMap.get("offAmount").toString())/100));
			item.setAmount(String.valueOf(Long.parseLong(subMap.get("amount").toString())/100));
			item.setUseType(subMap.get("useType").toString());
			item.setCoupName(subMap.get("coupName").toString());
			item.setOffdiscount(subMap.get("offDiscount").toString());
			item.setCanUse(subMap.get("canUse").toString());
			datas.add(item);
		}
		adapter.addDatas(datas);
	}
	/**
	 * 重载方法
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		adapter.updateSelected(arg2);
	}
}

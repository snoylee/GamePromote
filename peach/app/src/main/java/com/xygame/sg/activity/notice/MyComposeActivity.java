/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.adapter.ComposeAdapter;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.activity.notice.bean.NoticeBean;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.utils.KeyEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class MyComposeActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName, zhaoMView, paiSView, finishView;
	private View backButton,duiHuanButton;

	/**
	 * viewPage部分
	 */
	private LayoutInflater mInflater;
	private ViewPager mPager;// 页内容
	private List<View> listViews; // 页面列表

	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

	private ListView zhaoMList, PaiSList, finishList;
	private int loadFlag = 1;// 优惠券 1：可用 2：已使用  3：已过期
	private ComposeAdapter zhaoMAdapter, paiSAdapter, finishAdapter;
	private NoticeBean signedNoticeBean;
	private String scanerStr;
	private EditText duiHuanMa;
	
	public String getduiHuanMa(){
		return duiHuanMa.getText().toString().trim();
	}
	
	public String getScanerStr(){
		return scanerStr;
	}

	public int getLoadFlag() {
		return loadFlag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.my_compose_layout, null));
		InitImageView();
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
		duiHuanMa.setOnKeyListener(new KeyEventListener());
		backButton.setOnClickListener(this);
		zhaoMView.setOnClickListener(this);
		paiSView.setOnClickListener(this);
		finishView.setOnClickListener(this);
		duiHuanButton.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		duiHuanButton=findViewById(R.id.duiHuanButton);
		titleName = (TextView) findViewById(R.id.titleName);
		backButton = findViewById(R.id.backButton);
		duiHuanMa=(EditText)findViewById(R.id.duiHuanMa);
		zhaoMView = (TextView) findViewById(R.id.zhaoMView);
		paiSView = (TextView) findViewById(R.id.paiSView);
		finishView = (TextView) findViewById(R.id.finishView);

		mPager = (ViewPager) findViewById(R.id.mPager);
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.my_compose_sub_layout, null));
		listViews.add(mInflater.inflate(R.layout.my_compose_sub_layout, null));
		listViews.add(mInflater.inflate(R.layout.my_compose_sub_layout, null));
		zhaoMList = (ListView) listViews.get(0).findViewById(R.id.noticeList);
		PaiSList = (ListView) listViews.get(1).findViewById(R.id.noticeList);
		finishList = (ListView) listViews.get(2).findViewById(R.id.noticeList);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		titleName.setText("我的优惠券");
		zhaoMAdapter = new ComposeAdapter(this,null);
		paiSAdapter = new ComposeAdapter(this,null);
		finishAdapter = new ComposeAdapter(this,null);
		zhaoMList.setAdapter(zhaoMAdapter);
		PaiSList.setAdapter(paiSAdapter);
		finishList.setAdapter(finishAdapter);
		mPager.setCurrentItem(0);
		updateTitleText(0);
		loadDatas();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.zhaoMView) {
			mPager.setCurrentItem(0);
		} else if (v.getId() == R.id.paiSView) {
			mPager.setCurrentItem(1);
		} else if (v.getId() == R.id.finishView) {
			mPager.setCurrentItem(2);
		} else if (v.getId() == R.id.duiHuanButton) {
			if(!"".equals(duiHuanMa.getText().toString().trim())){
				uploadScanerInfo();
			}else{
				Toast.makeText(getApplicationContext(), "输入优惠券兑换码", Toast.LENGTH_SHORT).show();
			}
		} 
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				loadFlag = 1;
				if (zhaoMAdapter.getCount() == 0) {
					loadDatas();
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				loadFlag = 2;
				if (paiSAdapter.getCount() == 0) {
					loadDatas();
				}
				break;

			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				loadFlag = 3;
				if (finishAdapter.getCount() == 0) {
					loadDatas();
				}
				break;
			}
			currIndex = arg0;
			updateTitleText(arg0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	private void updateTitleText(int index) {
		switch (index) {
		case 0:
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_green));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 1:
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_green));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 2:
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_green));
			break;
		default:
			break;
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void loadDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.MyComposeTask(${listCoupons})", this, null, visit).run();
	}


	/**
	 * <一句话功能简述> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void getNoticeList(List<Map> listmap) {
		List<ComposeBean> datas=new ArrayList<ComposeBean>();
		for(Map subMap:listmap){
			ComposeBean item=new ComposeBean();
			item.setCompId(subMap.get("couponCode").toString());
			item.setManPrice(String.valueOf(Long.parseLong(subMap.get("minimumPrice").toString()) / 100));
			item.setSelect(false);
			item.setTipText(subMap.get("coupDesc").toString());
			item.setUseDate(subMap.get("endTime").toString());
			item.setYouHuiPrice(String.valueOf(Long.parseLong(subMap.get("offAmount").toString()) / 100));
			item.setAmount(String.valueOf(Long.parseLong(subMap.get("amount").toString()) / 100));
			item.setUseType(subMap.get("useType").toString());
			item.setCoupName(subMap.get("coupName").toString());
			item.setOffdiscount(subMap.get("offDiscount").toString());
			if (loadFlag==1){
				item.setCanUse("true");
			}else{
				item.setCanUse("false");
			}
			datas.add(item);
		}
		switch (loadFlag) {
		case 1:
			zhaoMAdapter.clearDatas();
			zhaoMAdapter.addDatas(datas);
			break;
		case 2:
			paiSAdapter.addDatas(datas);
			break;
		case 3:
			finishAdapter.addDatas(datas);
			break;
		default:
			break;
		}
	}

	private void uploadScanerInfo() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.RegisterComposeTask(${registCouponCode})", this, null, visit).run();
	}

	public void finishUpload() {
		duiHuanMa.setText("");
		// TODO Auto-generated method stub
		OneButtonDialog dialog=new OneButtonDialog(MyComposeActivity.this, "兑换成功", R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				loadFlag = 1;
				loadDatas();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}

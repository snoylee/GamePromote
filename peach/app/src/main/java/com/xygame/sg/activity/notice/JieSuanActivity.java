package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.JieSuanAdapter;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.adapter.comm.MyPagerAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class JieSuanActivity extends SGBaseActivity implements OnClickListener{
	/**
	 * 公用变量部分
	 */
	private TextView allNoticeView, myNoticeView, titleName;
	private ListView myPriseList,priseList;
	private View backButton;
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
	
	private JieSuanAdapter daiAdapter,yiAdapter;
	private String noticeId,status="1";
	
	public String getNoticeId() {
		return noticeId;
	}

	public String getStatus() {
		return status;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.jiesuan_layout, null));
		InitImageView();
		initViews();
		initListensers();
		initDatas();
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		allNoticeView = (TextView) findViewById(R.id.allNoticeView);
		myNoticeView = (TextView) findViewById(R.id.myNoticeView);
		mPager = (ViewPager) findViewById(R.id.mPager);
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.prise_person_layout,
				null));
		listViews.add(mInflater.inflate(
				R.layout.prise_person_my_layout, null));
		priseList = (ListView) listViews.get(0).findViewById(
				R.id.priseList);
		myPriseList = (ListView) listViews.get(1).findViewById(
				R.id.myPriseList);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	private void initListensers() {
		allNoticeView.setOnClickListener(new MyOnClickListener(0));
		myNoticeView.setOnClickListener(new MyOnClickListener(1));
		backButton.setOnClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		registerBoradcastReceiver();
		noticeId=getIntent().getStringExtra("noticeId");
		titleName.setText("结算付款");
		daiAdapter=new JieSuanAdapter(this, null,"1",noticeId);
		yiAdapter=new JieSuanAdapter(this, null,"2",noticeId);
		priseList.setAdapter(daiAdapter);
		myPriseList.setAdapter(yiAdapter);
		mPager.setCurrentItem(0);
		loadDaiJieDatas();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} 
	}
	
	private void loadDaiJieDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadMemberPayTask(${memberPay})", this, null, visit).run();
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				}
				allNoticeView.setTextColor(getResources().getColor(R.color.dark_green));
				myNoticeView.setTextColor(getResources().getColor(R.color.dark_gray));
				status="1";
				if(daiAdapter.getCount()==0){
					loadDaiJieDatas();
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				allNoticeView.setTextColor(getResources().getColor(R.color.dark_gray));
				myNoticeView.setTextColor(getResources().getColor(R.color.dark_green));
				status="2";
				if(yiAdapter.getCount()==0){
					loadPayedMemberDatas();
				}
				break;
			}
			currIndex = arg0;
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
	
	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void loadPayedMemberDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadMemberPayTask2(${memberPay})", this, null, visit).run();
	}

	public void finishLoadMemberPays(List<Map> map,String currTime) {
		// TODO Auto-generated method stub
		daiAdapter.clearDatas();
		List<NoticeStatusBean> datas=new ArrayList<NoticeStatusBean>();
		for(Map sMap:map){
			NoticeStatusBean it=new NoticeStatusBean();
			it.setEndTime(sMap.get("endTime").toString());
			it.setOrders(sMap.get("orders").toString());
			it.setRecruitId(sMap.get("recruitId").toString());
			it.setReward(sMap.get("reward").toString());
			it.setUserIcon(sMap.get("userIcon").toString());
			it.setUserId(sMap.get("userId").toString());
			it.setUserNick(sMap.get("userNick").toString());
			it.setMemId(sMap.get("memId").toString());
			it.setFinishTime(sMap.get("finishTime").toString());
			datas.add(it);
		}
		daiAdapter.addDatas(datas,currTime);
	}

	public void finishLoadPayedMember(List<Map> map) {
		// TODO Auto-generated method stub
		yiAdapter.clearDatas();
		List<NoticeStatusBean> datas=new ArrayList<NoticeStatusBean>();
		for(Map sMap:map){
			NoticeStatusBean it=new NoticeStatusBean();
			it.setEndTime(sMap.get("endTime").toString());
			it.setOrders(sMap.get("orders").toString());
			it.setRecruitId(sMap.get("recruitId").toString());
			it.setReward(sMap.get("finalAmount").toString());
			it.setUserIcon(sMap.get("userIcon").toString());
			it.setUserId(sMap.get("userId").toString());
			it.setUserNick(sMap.get("userNick").toString());
			if("0".equals(sMap.get("flag").toString())){
				it.setPrise(false);
			}else if("1".equals(sMap.get("flag").toString())){
				it.setPrise(true);
			}
			it.setFinishTime(sMap.get("finishTime").toString());
			datas.add(it);
		}
		yiAdapter.addDatas(datas,"0");
	}
	
	@Override
	public void onDestroy() {
		unregisterBroadcastReceiver();
		super.onDestroy();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.sg.finish.coment.action");
		myIntentFilter.addAction("com.sg.jiesuan.pay.scuess.action");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterBroadcastReceiver() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.sg.finish.coment.action".equals(intent.getAction())) {
				String userId=intent.getStringExtra("userId");
				yiAdapter.setNoticeCommentStatus(userId);
			}else if("com.sg.jiesuan.pay.scuess.action".equals(intent.getAction())){
//				String userId=intent.getStringExtra("userId");
//				NoticeStatusBean item=daiAdapter.removeDaiToYi(userId);
//				daiAdapter.notifyDataSetChanged();
//				yiAdapter.addItem(item);
				yiAdapter.clearDatas();
				mPager.setCurrentItem(0);
				status="1";
				loadDaiJieDatas();
			}
		}
	};
}

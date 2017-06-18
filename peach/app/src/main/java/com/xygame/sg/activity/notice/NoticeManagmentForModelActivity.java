/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.NoticeBean;
import com.xygame.sg.activity.notice.bean.NoticeItemBean;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;
import com.xygame.sg.zxing.activity.MipcaActivityCapture;

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
public class NoticeManagmentForModelActivity extends SGBaseActivity implements OnClickListener,PullToRefreshBase.OnRefreshListener2 {

	private TextView titleName, zhaoMView, paiSView, finishView, closeView;
	private View backButton,rightButton;

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

	private PullToRefreshListView2 zhaoMList, PaiSList, finishList, closeList;
	private int loadFlag = 1;// 通告状态：0：待审核；1：招募中；2：拍摄中；3：已完成；4：已关闭
	private NoticeAdapter zhaoMAdapter, paiSAdapter, finishAdapter, closeAdapter;
	// private int payItemIndex;
//	private NoticeBean signedNoticeBean;
	private String scanerStr;

	public String getScanerStr() {
		return scanerStr;
	}

	public int getLoadFlag() {
		return loadFlag;
	}

	private boolean isRefresh=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.notice_managment_formodel_layout, null));
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
		backButton.setOnClickListener(this);
		zhaoMView.setOnClickListener(this);
		paiSView.setOnClickListener(this);
		finishView.setOnClickListener(this);
		closeView.setOnClickListener(this);
		rightButton.setOnClickListener(this);

		zhaoMList.setOnRefreshListener(this);
		PaiSList.setOnRefreshListener(this);
		finishList.setOnRefreshListener(this);
		closeList.setOnRefreshListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		backButton = findViewById(R.id.backButton);
		rightButton=findViewById(R.id.rightButton);
		zhaoMView = (TextView) findViewById(R.id.zhaoMView);
		paiSView = (TextView) findViewById(R.id.paiSView);
		finishView = (TextView) findViewById(R.id.finishView);
		closeView = (TextView) findViewById(R.id.closeView);

		mPager = (ViewPager) findViewById(R.id.mPager);
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		zhaoMList = (PullToRefreshListView2) listViews.get(0).findViewById(R.id.noticeList);
		PaiSList = (PullToRefreshListView2) listViews.get(1).findViewById(R.id.noticeList);
		finishList = (PullToRefreshListView2) listViews.get(2).findViewById(R.id.noticeList);
		closeList = (PullToRefreshListView2) listViews.get(3).findViewById(R.id.noticeList);

		zhaoMList.setMode(PullToRefreshBase.Mode.BOTH);
		PaiSList.setMode(PullToRefreshBase.Mode.BOTH);
		finishList.setMode(PullToRefreshBase.Mode.BOTH);
		closeList.setMode(PullToRefreshBase.Mode.BOTH);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 4 - bmpW) / 2;// 计算偏移量
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
		registerBoradcastReceiver();
		titleName.setText("通告");
		zhaoMAdapter = new NoticeAdapter(null, 1);
		paiSAdapter = new NoticeAdapter(null, 2);
		finishAdapter = new NoticeAdapter(null, 3);
		closeAdapter = new NoticeAdapter(null, 4);
		zhaoMList.setAdapter(zhaoMAdapter);
		PaiSList.setAdapter(paiSAdapter);
		finishList.setAdapter(finishAdapter);
		closeList.setAdapter(closeAdapter);
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
		} else if (v.getId() == R.id.closeView) {
			mPager.setCurrentItem(3);
		}else if (v.getId()==R.id.rightButton){
			Intent intent=new Intent(this, NoticeManagmentForModelToJJRActivity.class);
			startActivity(intent);
			finish();
			//添加客服业务
//			if (UserPreferencesUtil.getServicePhone(this)!=null&&!"null".equals(UserPreferencesUtil.getServicePhone(this))){
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPreferencesUtil.getServicePhone(this)));
//				startActivity(intent);
//			}else{
//				Toast.makeText(this, "系统维护中", Toast.LENGTH_SHORT).show();
//			}
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;
		int three = one + two;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
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
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
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
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				}
				loadFlag = 3;
				if (finishAdapter.getCount() == 0) {
					loadDatas();
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				}
				loadFlag = 4;
				if (closeAdapter.getCount() == 0) {
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
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 1:
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_green));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 2:
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_green));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 3:
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			closeView.setTextColor(getResources().getColor(R.color.dark_green));
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
		new Action("#.notice.NoticeListForModelTask(${noticeList})", this, null, visit).run();
	}

	class NoticeAdapter extends BaseAdapter {
		private List<NoticeBean> datas;
		private int noticeStatus;// 通告状态：0：待审核；1：招募中；2：拍摄中；3：已完成；4：已关闭

		public NoticeAdapter(List<NoticeBean> datas, int noticeStatus) {
			super();
			this.noticeStatus = noticeStatus;
			if (datas == null) {
				this.datas = new ArrayList<NoticeBean>();
			} else {
				this.datas = datas;
			}
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public NoticeBean getItem(int i) {
			return datas.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		public void addDatas(List<NoticeBean> datas) {
			this.datas.addAll(datas);
			notifyDataSetChanged();
		}

		public void updatePayStatus(int index) {
			datas.get(index).setPayStatus("3");
			notifyDataSetChanged();
		}

		public void clearDatas(){
			this.datas.clear();
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			convertView = LayoutInflater.from(NoticeManagmentForModelActivity.this)
					.inflate(R.layout.sg_notice_manag_item, null);
			LinearLayout modelRequestView = (LinearLayout) convertView.findViewById(R.id.modelRequestView);
			ImageView kefuService = (ImageView) convertView.findViewById(R.id.kefuService);
			TextView cameraTheme = (TextView) convertView.findViewById(R.id.cameraTheme);
			TextView cameraDate = (TextView) convertView.findViewById(R.id.cameraDate);
			TextView modelNum = (TextView) convertView.findViewById(R.id.modelNum);
			TextView totalPrice = (TextView) convertView.findViewById(R.id.totalPrice);
			TextView itemTip = (TextView) convertView.findViewById(R.id.itemTip);
			TextView leftViewTip = (TextView) convertView.findViewById(R.id.leftViewTip);
			TextView rightViewTip = (TextView) convertView.findViewById(R.id.rightViewTip);
			View leftView = convertView.findViewById(R.id.leftView);
			View rightView = convertView.findViewById(R.id.rightView);
			View isShowLine = convertView.findViewById(R.id.isShowLine);
			View isShowView = convertView.findViewById(R.id.isShowView);
			View noCameraView = convertView.findViewById(R.id.noCameraView);
			View cameringView = convertView.findViewById(R.id.cameringView);
			View chaKanStatus = convertView.findViewById(R.id.chaKanStatus);

			TextView C_G_modelNum = (TextView) convertView.findViewById(R.id.C_G_modelNum);
			TextView C_G_totalPrice = (TextView) convertView.findViewById(R.id.C_G_totalPrice);
			TextView C_Y_modelNum = (TextView) convertView.findViewById(R.id.C_Y_modelNum);
			TextView C_Y_totalPrice = (TextView) convertView.findViewById(R.id.C_Y_totalPrice);
			TextView C_S_modelNum = (TextView) convertView.findViewById(R.id.C_S_modelNum);
			TextView C_S_totalPrice = (TextView) convertView.findViewById(R.id.C_S_totalPrice);
			modelRequestView.removeAllViews();
			final NoticeBean item = datas.get(i);
			cameraTheme.setText(item.getSubject());
			cameraDate.setText(CalendarUtils.getXieGongDateDis(Long.parseLong(item.getStartTime())).concat("—")
					.concat(CalendarUtils.getXieGongDateDis(Long.parseLong(item.getEndTime()))));

			for (int j = 0; j < item.getNotices().size(); j++) {
				NoticeItemBean it = item.getNotices().get(j);
				View pView = getRequestView(noticeStatus);
				ImageView tagIcon = (ImageView) pView.findViewById(R.id.tagIcon);
				TextView tagText = (TextView) pView.findViewById(R.id.tagText);
				TextView priceValue = (TextView) pView.findViewById(R.id.priceValue);
				TextView numText = (TextView) pView.findViewById(R.id.numText);
				TextView zhaomuTxt = (TextView) pView.findViewById(R.id.zhaomuTxt);
				
				//+++++++++++++++++++++++++++++++++++++++++++
				
				if(noticeStatus!=1){
					TextView statusText=(TextView)pView.findViewById(R.id.statusText);
					TextView jieSuanStatusText=(TextView)pView.findViewById(R.id.jieSuanStatusText);
					TextView jieSuanPriceValue=(TextView)pView.findViewById(R.id.jieSuanPriceValue);
					if("1".equals(it.getReceiptStatus())){
						jieSuanStatusText.setText("待结算");
					}else if("2".equals(it.getReceiptStatus())){
						statusText.setText("(拍摄完成)");
						jieSuanStatusText.setText("已结算");
					}
					
					jieSuanPriceValue.setText("￥".concat(it.getRealAmount()));
				}

				//+++++++++++++++++++++++++++++++++++++++++++
				String[] numArray = Constants.CHARACTE_NUMS;
				if (j > numArray.length - 1) {
					zhaomuTxt.setText("招募 " + (j + 1));
				} else {
					zhaomuTxt.setText("招募".concat(numArray[j]));
				}
				if ("1".equals(it.getGender())) {
					tagIcon.setImageResource(R.drawable.sg_pl_man);
					tagText.setText("男");
					priceValue.setText("￥".concat(it.getReward()));
				} else if ("0".equals(it.getGender())) {
					tagIcon.setImageResource(R.drawable.sg_pl_woman);
					tagText.setText("女");
					priceValue.setText("￥".concat(it.getReward()));
				}
				numText.setText(it.getPersonNum().concat("人"));
				modelRequestView.addView(pView);
			}
			modelNum.setText(item.getTotal());
			totalPrice.setText(item.getAmount());
			isShowLine.setVisibility(View.VISIBLE);
			isShowView.setVisibility(View.VISIBLE);
			chaKanStatus.setVisibility(View.GONE);
			// 1：常规，2：预付费
			// 1：待支付 2：部分支付 3：支付完成4：支付取消 5：退款中 6：退款成功 7：退款失败
			switch (noticeStatus) {
			case 1:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.VISIBLE);
				cameringView.setVisibility(View.GONE);
				leftView.setVisibility(View.GONE);
				rightView.setVisibility(View.GONE);
				itemTip.setText("招募中");
				itemTip.setTextColor(getResources().getColor(R.color.dark_green));
				break;
			case 2:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.GONE);
				cameringView.setVisibility(View.VISIBLE);
				C_G_modelNum.setText(item.getTotal());
				C_G_totalPrice.setText(item.getAmount());
				C_Y_modelNum.setText(item.getHiredAmount());
				C_Y_totalPrice.setText(item.getHiredSum());
				C_S_modelNum.setText(item.getReceiptAmount());
				C_S_totalPrice.setText(item.getReceiptSum());
				itemTip.setText("拍摄中");
				itemTip.setTextColor(getResources().getColor(R.color.dark_green));
				if ("0".equals(item.getSignStatus())||"null".equals(item.getSignStatus())){
					rightView.setVisibility(View.GONE);
					leftView.setVisibility(View.VISIBLE);
					leftViewTip.setText("签到");
					leftView.setOnClickListener(new showSign(item));
				}else{
					leftView.setVisibility(View.GONE);
					rightView.setVisibility(View.VISIBLE);
					rightViewTip.setText("通告完成");
					rightView.setOnClickListener(new IntoPaiSheFinish(item));
				}
				break;
			case 3:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.GONE);
				cameringView.setVisibility(View.GONE);
				isShowLine.setVisibility(View.GONE);
				itemTip.setText("拍摄完成");
				itemTip.setTextColor(getResources().getColor(R.color.dark_green));
				leftView.setVisibility(View.VISIBLE);
				rightView.setVisibility(View.GONE);
				if ("1".equals(item.getEvalFlag())) {
					leftViewTip.setText("查看评价");
					leftView.setOnClickListener(new seePingJia(item));
				} else {
					leftViewTip.setText("评价");
					leftView.setOnClickListener(new intoPingJia(item));
				}
				break;
			case 4:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.GONE);
				cameringView.setVisibility(View.GONE);
				isShowLine.setVisibility(View.GONE);
				itemTip.setText("拍摄完成");
				itemTip.setTextColor(getResources().getColor(R.color.dark_gray));
				leftView.setVisibility(View.GONE);
				rightView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			chaKanStatus.setOnClickListener(new IntoNoticeView(item));
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(NoticeManagmentForModelActivity.this, NoticeDetailActivity.class);
					intent.putExtra("noticeId", item.getNoticeId());
					intent.putExtra("isQuery", true);
					String bgUrl = SGApplication.getInstance().getBgImageUrl(item.getNoticeId());
					intent.putExtra("noticeBgUrl", bgUrl);
					startActivity(intent);
				}
			});
			return convertView;
		}
		
		public void updateCommentNotice(String noticeId){
			for(int i=0;i<datas.size();i++){
				if(noticeId.equals(datas.get(i).getNoticeId())){
					datas.get(i).setEvalFlag("1");
				}
			}
			notifyDataSetChanged();
		}
		
		class IntoPaiSheFinish implements OnClickListener {
			private NoticeBean item;

			public IntoPaiSheFinish(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intoPaiSheFinishPage(item);
			}
		}

		public void updateItemCommentStatus(String noticeId) {
			for (int i = 0; i < datas.size(); i++) {
				if (noticeId.equals(datas.get(i).getNoticeId())) {
					datas.get(i).setEvalFlag("1");
				}
			}
			notifyDataSetChanged();
		}

		public void updateSignStatus(String noticeId){
			for (int i = 0; i < datas.size(); i++) {
				if (noticeId.equals(datas.get(i).getNoticeId())) {
					datas.get(i).setSignStatus("1");
				}
			}
			notifyDataSetChanged();
		}

		public View getRequestView(int noticeStatus2) {

			View pView = null;

			switch (noticeStatus) {
			case 1:
				pView = LayoutInflater.from(NoticeManagmentForModelActivity.this)
						.inflate(R.layout.sg_payment_model_request_item, null);
				break;
			default:
				pView = LayoutInflater.from(NoticeManagmentForModelActivity.this)
						.inflate(R.layout.sg_notice_manag_item_for_model, null);
				break;
			}

			return pView;
		}

		class seePingJia implements OnClickListener {
			private NoticeBean item;

			public seePingJia(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				seePingJiaPage(item);
			}
		}

		class intoPingJia implements OnClickListener {
			private NoticeBean item;

			public intoPingJia(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intoPingJiaPage(item);
			}
		}

		class showSign implements OnClickListener {
			private NoticeBean item;

			public showSign(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSignPage(item);
			}
		}

		class IntoNoticeView implements OnClickListener {
			private NoticeBean item;

			public IntoNoticeView(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toNoticeStatus(item);
			}
		}

	}

	public void intoPaiSheFinishPage(NoticeBean item) {
		Intent intent = new Intent(this, JieSuanForModelActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);
	}
	
	public void seePingJiaPage(NoticeBean item) {
		Intent intent = new Intent(this, JieSuanForModelActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);
//		Intent intent = new Intent(this, SignedForModelActivity.class);
//		intent.putExtra("noticeId", item.getNoticeId());
//		startActivity(intent);
	}

	public void intoPingJiaPage(NoticeBean item) {
//		Intent intent = new Intent(this, CommentForModelActivity.class);
//		intent.putExtra("noticeId", item.getNoticeId());
//		startActivityForResult(intent, 1);
		Intent intent = new Intent(this, JieSuanForModelActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);

	}

	public void showSignPage(NoticeBean item) {
		Intent intent = new Intent(NoticeManagmentForModelActivity.this, MipcaActivityCapture.class);
		intent.putExtra("bean",item);
		startActivityForResult(intent, 2);
	}

	public void toNoticeStatus(NoticeBean item) {

		Intent intent = new Intent(NoticeManagmentForModelActivity.this, NoticeStatusActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);

	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @see [类、类#方法、类#成员]
	 */
	public void getNoticeList(List<Map> listmap) {
		List<NoticeBean> datas = new ArrayList<NoticeBean>();
		for (Map map : listmap) {
			NoticeBean item = new NoticeBean();
			item.setEvalFlag(map.get("evalFlag").toString());
			item.setAmount(String.valueOf(StringUtil.getPrice(Long.parseLong(map.get("amount").toString()))));
			item.setEndTime(map.get("endTime").toString());
			item.setNoticeId(map.get("noticeId").toString());
			item.setNoticeType(map.get("noticeType").toString());
			item.setOpenStatus(map.get("openStatus").toString());
			item.setPayStatus(map.get("payStatus").toString());
			item.setStartTime(map.get("startTime").toString());
			item.setSubject(map.get("subject").toString());
			item.setTotal(map.get("total").toString());
			item.setHiredAmount(map.get("hiredSum").toString());
			item.setSignStatus(map.get("signStatus").toString());
			item.setHiredSum(String.valueOf(StringUtil.getPrice(Long.parseLong(map.get("hiredAmount").toString()))));
			item.setReceiptAmount(map.get("receiptSum").toString());
			item.setReceiptSum(
					String.valueOf(StringUtil.getPrice(Long.parseLong(map.get("receiptAmount").toString()))));
			if ("null".equals(item.getStartTime())) {
				item.setStartTime(String.valueOf(System.currentTimeMillis()));
			}
			List<NoticeItemBean> notices = new ArrayList<NoticeItemBean>();
			List<Map> subMap = (List<Map>) map.get("recruits");
			for (Map ssMap : subMap) {
				NoticeItemBean it = new NoticeItemBean();
				it.setGender(ssMap.get("gender").toString());
				it.setPersonNum(ssMap.get("count").toString());
				it.setRecruitId(ssMap.get("recruitId").toString());
				it.setReward(String.valueOf(StringUtil.getPrice(Long.parseLong(ssMap.get("reward").toString()))));
				it.setReceiptStatus(ssMap.get("receiptStatus").toString());
				it.setRealAmount(String.valueOf(StringUtil.getPrice(Long.parseLong(ssMap.get("realAmount").toString()))));
				notices.add(it);
			}
			item.setNotices(notices);
			datas.add(item);
		}
		switch (loadFlag) {
		case 1:
			if (isRefresh){
				zhaoMList.onRefreshComplete();
				isRefresh=false;
				zhaoMAdapter.clearDatas();
				zhaoMAdapter.addDatas(datas);
			}else{
				zhaoMAdapter.addDatas(datas);
			}

			break;
		case 2:
			if (isRefresh){
				PaiSList.onRefreshComplete();
				isRefresh=false;
				paiSAdapter.clearDatas();
				paiSAdapter.addDatas(datas);
			}else{
				paiSAdapter.addDatas(datas);
			}

			break;
		case 3:
			if (isRefresh){
				finishList.onRefreshComplete();
				isRefresh=false;
				finishAdapter.clearDatas();
				finishAdapter.addDatas(datas);
			}else{
				finishAdapter.addDatas(datas);
			}

			break;
		case 4:
			if (isRefresh){
				closeList.onRefreshComplete();
				isRefresh=false;
				closeAdapter.clearDatas();
				closeAdapter.addDatas(datas);
			}else{
				closeAdapter.addDatas(datas);
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			scanerStr = data.getStringExtra("result");
			break;
		}
		case 1: {
			String noticeId = data.getStringExtra(Constants.COMEBACK);
			if (noticeId != null) {
				finishAdapter.updateItemCommentStatus(noticeId);
			}
			break;
		}
			case 2:{
				boolean flag=data.getBooleanExtra(Constants.COMEBACK,false);
				String noticeId=data.getStringExtra("noticeId");
				if (flag){
					paiSAdapter.updateSignStatus(noticeId);
				}
				break;
			}
		default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		unregisterBroadcastReceiver();
		super.onDestroy();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.sg.finish.coment.formodel.action");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	public void unregisterBroadcastReceiver() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.sg.finish.coment.formodel.action".equals(intent.getAction())) {
				String noticeId=intent.getStringExtra("noticeId");
				finishAdapter.updateCommentNotice(noticeId);
			}
		}
	};

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		//刷新操作
		isRefresh=true;
		loadDatas();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		//加载操作
		falseDatas();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 2:
					switch (loadFlag) {
						case 1:
							zhaoMList.onRefreshComplete();
							break;
						case 2:
							PaiSList.onRefreshComplete();
							break;
						case 3:
							finishList.onRefreshComplete();
							break;
						case 4:
							closeList.onRefreshComplete();
							break;
						default:
							break;
					}
					break;
				default:
					break;
			}

		}
	};


	private void falseDatas(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					android.os.Message m = handler.obtainMessage();
					m.what=2;
					m.sendToTarget();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
}

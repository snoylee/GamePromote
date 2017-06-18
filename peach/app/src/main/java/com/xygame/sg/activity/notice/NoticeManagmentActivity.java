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
import android.content.res.TypedArray;
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

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.NoticeBean;
import com.xygame.sg.activity.notice.bean.NoticeItemBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.define.view.ChoiceNoticeView;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

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
public class NoticeManagmentActivity extends SGBaseActivity implements OnClickListener,PullToRefreshBase.OnRefreshListener2 {

	private TextView titleName, daiSView, zhaoMView, paiSView, finishView, closeView;
	private View backButton,rightButton;
	private ImageView rightbuttonIcon;
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

	private PullToRefreshListView2 daiSList, zhaoMList, PaiSList, finishList, closeList;
	private int loadFlag = 0;// 通告状态：0：待审核；1：招募中；2：拍摄中；3：已完成；4：已关闭
	private NoticeAdapter daiSAdapter, zhaoMAdapter, paiSAdapter, finishAdapter, closeAdapter;
	private NoticeBean noticeBeanItem;
	private int flagIndex;
	private boolean isRefresh=false;
	public int getLoadFlag() {
		return loadFlag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.notice_managment_layout, null));
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
		daiSView.setOnClickListener(this);
		zhaoMView.setOnClickListener(this);
		paiSView.setOnClickListener(this);
		finishView.setOnClickListener(this);
		closeView.setOnClickListener(this);
		rightButton.setOnClickListener(this);

		daiSList.setOnRefreshListener(this);
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
		daiSView = (TextView) findViewById(R.id.daiSView);
		zhaoMView = (TextView) findViewById(R.id.zhaoMView);
		paiSView = (TextView) findViewById(R.id.paiSView);
		finishView = (TextView) findViewById(R.id.finishView);
		closeView = (TextView) findViewById(R.id.closeView);
		rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
		mPager = (ViewPager) findViewById(R.id.mPager);
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		daiSList = (PullToRefreshListView2) listViews.get(0).findViewById(R.id.noticeList);
		zhaoMList = (PullToRefreshListView2) listViews.get(1).findViewById(R.id.noticeList);
		PaiSList = (PullToRefreshListView2) listViews.get(2).findViewById(R.id.noticeList);
		finishList = (PullToRefreshListView2) listViews.get(3).findViewById(R.id.noticeList);
		closeList = (PullToRefreshListView2) listViews.get(4).findViewById(R.id.noticeList);

		daiSList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		zhaoMList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		PaiSList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		finishList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		closeList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 5 - bmpW) / 2;// 计算偏移量
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
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.sg_con_service);
		titleName.setText("通告");
		daiSAdapter = new NoticeAdapter(null, 0);
		zhaoMAdapter = new NoticeAdapter(null, 1);
		paiSAdapter = new NoticeAdapter(null, 2);
		finishAdapter = new NoticeAdapter(null, 3);
		closeAdapter = new NoticeAdapter(null, 4);
		daiSList.setAdapter(daiSAdapter);
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
		} else if (v.getId() == R.id.daiSView) {
			mPager.setCurrentItem(0);
		} else if (v.getId() == R.id.zhaoMView) {
			mPager.setCurrentItem(1);
		} else if (v.getId() == R.id.paiSView) {
			mPager.setCurrentItem(2);
		} else if (v.getId() == R.id.finishView) {
			mPager.setCurrentItem(3);
		} else if (v.getId() == R.id.closeView) {
			mPager.setCurrentItem(4);
		}else if (v.getId()==R.id.rightButton){
//			//添加客服业务
//			if (UserPreferencesUtil.getServicePhone(this)!=null&&!"null".equals(UserPreferencesUtil.getServicePhone(this))){
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPreferencesUtil.getServicePhone(this)));
//				startActivity(intent);
//			}else{
//				Toast.makeText(this, "系统维护中", Toast.LENGTH_SHORT).show();
//			}
//			//添加摄影师业务
//			Intent intent=new Intent(this, NoticeManageView.class);
//			startActivityForResult(intent,1);
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;
		int three = one + two;
		int four = one + three;

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
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
				}
				loadFlag = 0;
				if (daiSAdapter.getCount() == 0) {
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
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, one, 0, 0);
				}
				loadFlag = 1;
				if (zhaoMAdapter.getCount() == 0) {
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
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
				}
				loadFlag = 2;
				if (paiSAdapter.getCount() == 0) {
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
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
				}
				loadFlag = 3;
				if (finishAdapter.getCount() == 0) {
					loadDatas();
				}
				break;
			case 4:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, four, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
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
			daiSView.setTextColor(getResources().getColor(R.color.dark_green));
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 1:
			daiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_green));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 2:
			daiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_green));
			finishView.setTextColor(getResources().getColor(R.color.dark_gray));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 3:
			daiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			zhaoMView.setTextColor(getResources().getColor(R.color.dark_gray));
			paiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			finishView.setTextColor(getResources().getColor(R.color.dark_green));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 4:
			daiSView.setTextColor(getResources().getColor(R.color.dark_gray));
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
		new Action("#.notice.NoticeListTask(${noticeList})", this, null, visit).run();
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

		public void clearDatas(){
			this.datas.clear();
		}

		public void updatePayStatus(NoticeBean noticeBean) {
			for(int i=0;i<datas.size();i++){
				if (noticeBean.getNoticeId().equals(datas.get(i).getNoticeId())){
					datas.get(i).setPayStatus("3");
				}
			}
			notifyDataSetChanged();
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			convertView = LayoutInflater.from(NoticeManagmentActivity.this).inflate(R.layout.sg_notice_manag_item,
					null);
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
			TypedArray bgColors = getResources().obtainTypedArray(R.array.notice_request_bg);
			for (int j = 0; j < item.getNotices().size(); j++) {
				NoticeItemBean it = item.getNotices().get(j);
				View pView = getRequestView();
				ImageView tagIcon = (ImageView) pView.findViewById(R.id.tagIcon);
				TextView tagText = (TextView) pView.findViewById(R.id.tagText);
				TextView priceValue = (TextView) pView.findViewById(R.id.priceValue);
				TextView numText = (TextView) pView.findViewById(R.id.numText);
				RoundTextView zhaomuTxt = (RoundTextView) pView.findViewById(R.id.zhaomuTxt);
//				String[] numArray = Constants.CHARACTE_NUMS;
//				if (j > numArray.length - 1) {
//					zhaomuTxt.setText("招募 " + (j + 1));
//				} else {
//					zhaomuTxt.setText("招募".concat(numArray[j]));
//				}
				zhaomuTxt.setText("招募"+ StringUtils.transition((j + 1) + ""));
				if (j>=20){
					zhaomuTxt.getDelegate().setBackgroundColor(bgColors.getColor(j - 20, 0));
				} else {
					zhaomuTxt.getDelegate().setBackgroundColor(bgColors.getColor(j, 0));
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
			bgColors.recycle();
			modelNum.setText(item.getTotal());
			totalPrice.setText(item.getAmount());
			isShowLine.setVisibility(View.VISIBLE);
			isShowView.setVisibility(View.VISIBLE);
			chaKanStatus.setVisibility(View.GONE);
			// 1：常规，2：预付费
			// 1：待支付 2：部分支付 3：支付完成4：支付取消 5：退款中 6：退款成功 7：退款失败
			switch (noticeStatus) {
			case 0:
				chaKanStatus.setVisibility(View.GONE);
				noCameraView.setVisibility(View.VISIBLE);
				cameringView.setVisibility(View.GONE);
				if ("1".equals(item.getNoticeType())) {
					if ("1".equals(item.getPayStatus())) {
						itemTip.setText("未预付");
						itemTip.setTextColor(getResources().getColor(R.color.dark_gray));
						rightView.setVisibility(View.VISIBLE);
						rightViewTip.setText("立即预付");

					} else if ("3".equals(item.getPayStatus())) {
						itemTip.setText("已预付");
						itemTip.setTextColor(getResources().getColor(R.color.dark_green));
						rightView.setVisibility(View.GONE);
					}

				} else if ("2".equals(item.getNoticeType())) {
					if ("1".equals(item.getPayStatus())) {
						itemTip.setText("预付费通告需提前预付全款后才能审核并招募");
						itemTip.setTextColor(getResources().getColor(R.color.dark_gray));
						rightView.setVisibility(View.VISIBLE);
						rightViewTip.setText("立即预付");
					} else if ("3".equals(item.getPayStatus())) {
						itemTip.setText("已预付");
						itemTip.setTextColor(getResources().getColor(R.color.dark_green));
						rightView.setVisibility(View.GONE);
					}
				}
				leftView.setVisibility(View.VISIBLE);
				leftViewTip.setText("查看状态");
				leftViewTip.setOnClickListener(new IntoNoticeView(item));
				rightView.setOnClickListener(new PaymentNow(item, i,0));
				break;
			case 1:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.VISIBLE);
				cameringView.setVisibility(View.GONE);
				if ("1".equals(item.getPayStatus())) {
					itemTip.setText("未预付");
					itemTip.setTextColor(getResources().getColor(R.color.dark_gray));
					leftView.setVisibility(View.GONE);
					rightView.setVisibility(View.VISIBLE);
					rightViewTip.setText("立即预付");
					rightView.setOnClickListener(new PaymentNow(item, i,1));
				} else if ("3".equals(item.getPayStatus())) {
					itemTip.setText("已预付");
					itemTip.setTextColor(getResources().getColor(R.color.dark_green));
					leftView.setVisibility(View.VISIBLE);
					rightView.setVisibility(View.VISIBLE);
					leftViewTip.setText("拍摄签到");
					leftView.setOnClickListener(new intoSign(item));
					rightViewTip.setText("结算付款");
					rightView.setOnClickListener(new intoJieSuan(item));
				}
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
				itemTip.setText("已预付");
				itemTip.setTextColor(getResources().getColor(R.color.dark_green));
				leftView.setVisibility(View.VISIBLE);
				rightView.setVisibility(View.VISIBLE);
				rightViewTip.setText("结算付款");
				leftViewTip.setText("拍摄签到");
				leftView.setOnClickListener(new intoSign(item));
				rightView.setOnClickListener(new intoJieSuan(item));
				break;
			case 3:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.VISIBLE);
				cameringView.setVisibility(View.GONE);
				itemTip.setText("已预付");
				itemTip.setTextColor(getResources().getColor(R.color.dark_green));
				leftView.setVisibility(View.VISIBLE);
				rightView.setVisibility(View.GONE);
				leftViewTip.setText("评价");
				leftView.setOnClickListener(new intoJieSuan(item));
				break;
			case 4:
				chaKanStatus.setVisibility(View.VISIBLE);
				noCameraView.setVisibility(View.VISIBLE);
				cameringView.setVisibility(View.GONE);
				itemTip.setText("已关闭");
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
					Intent intent = new Intent(NoticeManagmentActivity.this, NoticeDetailActivity.class);
					intent.putExtra("noticeId", item.getNoticeId());
					intent.putExtra("isQuery", true);
					String bgUrl = SGApplication.getInstance().getBgImageUrl(item.getNoticeId());
					intent.putExtra("noticeBgUrl", bgUrl);
					startActivity(intent);
				}
			});
			return convertView;
		}

		public View getRequestView() {
			return LayoutInflater.from(NoticeManagmentActivity.this).inflate(R.layout.sg_payment_model_request_item,
					null);
		}

//		private class ViewHolder {
//			private LinearLayout modelRequestView;
//			private ImageView kefuService;
//			private TextView cameraTheme, cameraDate, modelNum, totalPrice, itemTip, leftViewTip, rightViewTip,
//					C_G_modelNum, C_G_totalPrice, C_Y_modelNum, C_Y_totalPrice, C_S_modelNum, C_S_totalPrice;
//			private View rightView, leftView, isShowLine, isShowView, cameringView, noCameraView, chaKanStatus;
//		}

		class intoJieSuan implements OnClickListener {
			private NoticeBean item;

			public intoJieSuan(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intoJieSuanPage(item);
			}
		}

		class intoSign implements OnClickListener {
			private NoticeBean item;

			public intoSign(NoticeBean item) {
				// TODO Auto-generated constructor stub
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intoSignPage(item);
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

		class PaymentNow implements OnClickListener {
			private NoticeBean item;
			private int payItemIndex;
			private int flag;
			public PaymentNow(NoticeBean item, int payItemIndex,int flag) {
				// TODO Auto-generated constructor stub
				this.item = item;
				this.payItemIndex = payItemIndex;
				this.flag=flag;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toPayItem(item, payItemIndex,flag);
			}
		}

	}

	public void intoJieSuanPage(NoticeBean item) {

		Intent intent = new Intent(NoticeManagmentActivity.this, JieSuanActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivityForResult(intent, 1);

	}

	public void intoSignPage(NoticeBean item) {

		Intent intent = new Intent(NoticeManagmentActivity.this, SignedActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);

	}

	public void toNoticeStatus(NoticeBean item) {

		Intent intent = new Intent(NoticeManagmentActivity.this, NoticeStatusActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);

	}

	public void toPayItem(NoticeBean item, int payItemIndex,int flag) {
		flagIndex=flag;
		noticeBeanItem=item;
		PlushNoticeBean pnBean = new PlushNoticeBean();
		pnBean.setNoticeId(item.getNoticeId());
		pnBean.setCameraTheme(item.getSubject());
		pnBean.setStarTime(item.getStartTime());
		pnBean.setEndTime(item.getEndTime());
		List<ModelRequestBean> modelDatas = new ArrayList<ModelRequestBean>();
		for (int j = 0; j < item.getNotices().size(); j++) {
			NoticeItemBean it = item.getNotices().get(j);
			ModelRequestBean mItem = new ModelRequestBean();
			if ("1".equals(it.getGender())) {
				mItem.setSexId("1");
				mItem.setSexName("男");
			} else if ("0".equals(it.getGender())) {
				mItem.setSexId("0");
				mItem.setSexName("女");
			}
			mItem.setNeedPrice(it.getReward());
			mItem.setNeedNum(it.getPersonNum());
			modelDatas.add(mItem);
		}
		pnBean.setModelBeans(modelDatas);
		Intent intent = new Intent(this, NoticePaymentActivity.class);
		intent.putExtra("bean", pnBean);
		intent.putExtra("strFlag", "fromNoticeManagment");
		startActivityForResult(intent, 0);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void getNoticeList(List<Map> listmap) {
		List<NoticeBean> datas = new ArrayList<NoticeBean>();
		for (Map map : listmap) {
			NoticeBean item = new NoticeBean();
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
			item.setHiredSum(String.valueOf(StringUtil.getPrice(Long.parseLong(map.get("hiredAmount").toString()))));
			item.setReceiptAmount(map.get("receiptSum").toString());
			item.setReceiptSum(String.valueOf(StringUtil.getPrice(Long.parseLong(map.get("receiptAmount").toString()))));
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
				notices.add(it);
			}
			item.setNotices(notices);
			datas.add(item);
		}
		switch (loadFlag) {
		case 0:
			if (isRefresh){
				daiSList.onRefreshComplete();
				isRefresh=false;
				daiSAdapter.clearDatas();
				daiSAdapter.addDatas(datas);
			}else{
				daiSAdapter.addDatas(datas);
			}

			break;
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
			boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
			if (result) {
				if (flagIndex==0){
					daiSAdapter.updatePayStatus(noticeBeanItem);
				}else if (flagIndex==1){
					zhaoMAdapter.updatePayStatus(noticeBeanItem);
				}

			}
			break;
		}
			case 1: {
				String result1 = data.getStringExtra(Constants.COMEBACK);
				if ("createNotice".equals(result1)) {
					Intent intent = new Intent(this, ChoiceNoticeView.class);
					startActivityForResult(intent, 2);
				}else if ("contact_service".equals(result1)){
//					//联系客服
//					if (UserPreferencesUtil.getServicePhone(this)!=null&&!"null".equals(UserPreferencesUtil.getServicePhone(this))){
//						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPreferencesUtil.getServicePhone(this)));
//						startActivity(intent);
//					}else{
//						Toast.makeText(this, "系统维护中", Toast.LENGTH_SHORT).show();
//					}
				}
				break;
			}
			case 2: {
				if (Activity.RESULT_OK != resultCode || null == data) {
					return;
				}
				String flag = data.getStringExtra(Constants.COMEBACK);
				if ("galary".equals(flag)) {
					Intent intent = new Intent(this, PlushNoticeActivity.class);
					intent.putExtra("noticeFlag", "pay");
					startActivity(intent);
				} else if ("camera".equals(flag)) {
					if(Constants.USER_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						Intent intent = new Intent(this, PlushNoticeActivity.class);
						intent.putExtra("noticeFlag", "noPay");
						startActivity(intent);
					}else if(Constants.USER_NO_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						showDialog(getString(R.string.to_publish_no_verify));
					}else if(Constants.USER_VERIFING_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						showOneButtonDialog(getString(R.string.to_publish_verifing));
					}else if(Constants.USER_VERIFING_REFUSED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						showDialog(getString(R.string.to_publish_refused));
					}
				}
				break;
			}
		default:
			break;
		}
	}

	private void showOneButtonDialog(String tip){
		OneButtonDialog dialog=new OneButtonDialog(this, tip, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showDialog(String tip){
		TwoButtonDialog dialog=new TwoButtonDialog(this, "尚未认证的摄影师只能发布预付费通告或立即前往认证噢！", R.style.dineDialog,new ButtonTwoListener() {

			@Override
			public void confrimListener() {
				// TODO Auto-generated method stub
				goToCerti();
			}

			@Override
			public void cancelListener() {
			}
		});
		dialog.show();
	}

	private void goToCerti(){
		Intent intent = new Intent(this, CMIdentyFirstActivity.class);
		startActivity(intent);

	}

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
						case 0:
							daiSList.onRefreshComplete();
							break;
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

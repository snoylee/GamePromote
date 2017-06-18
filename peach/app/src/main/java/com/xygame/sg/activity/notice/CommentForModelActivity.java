package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.CommentBean;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.activity.notice.bean.NoticeStatusBeanForModel;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class CommentForModelActivity extends SGBaseActivity implements OnClickListener, TextWatcher {
	/**
	 * 公用变量部分
	 */
	private TextView allNoticeView, myNoticeView, titleName, showCommentText, showCommentText1, numTips;
	private View backButton, comfirm, comfirm1;
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
	private RatingBar peiheRatingBar, zhuanyeRatingBar, fuheRatingBar, peiheRatingBar1, zhuanyeRatingBar1,
			fuheRatingBar1;
	private EditText commentText, commentText1;
	private TextView comentTip, comentTip1, signTime, signTime1, zhaomuTxt, zhaomuTxt1, daiPrice, daiPrice1, userName,
			userName1;
	private View comentEditorView, comentEditorView1;
	private CircularImage userImage, userImage1;
	private ImageLoader imageLoader;
	String[] numArray = Constants.CHARACTE_NUMS;
	private String noticeId, commentFlag;
	private CommentBean myCommentBean, youCommentBean;
	private NoticeStatusBeanForModel nsBean;

	public String getnoticeId() {
		return noticeId;
	}

	public NoticeStatusBean getBeanInfo() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.comment_formodel_layout, null));
		InitImageView();
		initViews();
		initListensers();
		initDatas();
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a).getWidth();// 获取图片宽度
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
		listViews.add(mInflater.inflate(R.layout.comment_sub_formodel_layout, null));
		listViews.add(mInflater.inflate(R.layout.comment_sub2_formodel_layout, null));

		peiheRatingBar = (RatingBar) listViews.get(0).findViewById(R.id.peiheRatingBar);
		zhuanyeRatingBar = (RatingBar) listViews.get(0).findViewById(R.id.zhuanyeRatingBar);
		fuheRatingBar = (RatingBar) listViews.get(0).findViewById(R.id.fuheRatingBar);
		commentText = (EditText) listViews.get(0).findViewById(R.id.commentText);
		comentTip = (TextView) listViews.get(0).findViewById(R.id.comentTip);
		signTime = (TextView) listViews.get(0).findViewById(R.id.signTime);
		comentEditorView = listViews.get(0).findViewById(R.id.comentEditorView);
		zhaomuTxt = (TextView) listViews.get(0).findViewById(R.id.zhaomuTxt);
		daiPrice = (TextView) listViews.get(0).findViewById(R.id.daiPrice);
		userName = (TextView) listViews.get(0).findViewById(R.id.userName);
		userImage = (CircularImage) listViews.get(0).findViewById(R.id.userImage);
		showCommentText = (TextView) listViews.get(0).findViewById(R.id.showCommentText);
		comfirm = listViews.get(0).findViewById(R.id.comfirm);
		numTips = (TextView) listViews.get(0).findViewById(R.id.numTips);

		peiheRatingBar1 = (RatingBar) listViews.get(1).findViewById(R.id.peiheRatingBar);
		zhuanyeRatingBar1 = (RatingBar) listViews.get(1).findViewById(R.id.zhuanyeRatingBar);
		fuheRatingBar1 = (RatingBar) listViews.get(1).findViewById(R.id.fuheRatingBar);
		commentText1 = (EditText) listViews.get(1).findViewById(R.id.commentText);
		comentTip1 = (TextView) listViews.get(1).findViewById(R.id.comentTip);
		signTime1 = (TextView) listViews.get(1).findViewById(R.id.signTime);
		comentEditorView1 = listViews.get(1).findViewById(R.id.comentEditorView);
		zhaomuTxt1 = (TextView) listViews.get(1).findViewById(R.id.zhaomuTxt);
		daiPrice1 = (TextView) listViews.get(1).findViewById(R.id.daiPrice);
		userName1 = (TextView) listViews.get(1).findViewById(R.id.userName);
		showCommentText1 = (TextView) listViews.get(1).findViewById(R.id.showCommentText);
		userImage1 = (CircularImage) listViews.get(1).findViewById(R.id.userImage);
		comfirm1 = listViews.get(1).findViewById(R.id.comfirm);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void initListensers() {
		allNoticeView.setOnClickListener(new MyOnClickListener(0));
		myNoticeView.setOnClickListener(new MyOnClickListener(1));
		backButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
		commentText.addTextChangedListener(this);
	}

	private void initDatas() {
		nsBean=(NoticeStatusBeanForModel) getIntent().getSerializableExtra("bean");
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		noticeId = getIntent().getStringExtra("noticeId");
		titleName.setText("评价");
		zhuanyeRatingBar1.setIsIndicator(true);
		fuheRatingBar1.setIsIndicator(true);

		peiheRatingBar1.setIsIndicator(true);
		showCommentText1.setVisibility(View.VISIBLE);
		commentText1.setVisibility(View.GONE);
		comfirm1.setVisibility(View.GONE);
		mPager.setCurrentItem(0);
		loadCommentsDatas();
	}


	private void updateAllView() {
		// TODO Auto-generated method stub
		if ("Y".equals(commentFlag)) {
			zhuanyeRatingBar.setIsIndicator(true);
			fuheRatingBar.setIsIndicator(true);
			peiheRatingBar.setIsIndicator(true);
			showCommentText.setVisibility(View.VISIBLE);
			commentText.setVisibility(View.GONE);
			comfirm.setVisibility(View.GONE);
			numTips.setVisibility(View.GONE);
		} else {
			zhuanyeRatingBar.setIsIndicator(false);
			fuheRatingBar.setIsIndicator(false);
			peiheRatingBar.setIsIndicator(false);
			showCommentText.setVisibility(View.GONE);
			commentText.setVisibility(View.VISIBLE);
			comfirm.setVisibility(View.VISIBLE);
			numTips.setVisibility(View.VISIBLE);
		}

		if (myCommentBean != null) {
			if ("Y".equals(commentFlag)) {
				zhuanyeRatingBar.setRating(Float.parseFloat(myCommentBean.getExperienceLevel()));
				fuheRatingBar.setRating(Float.parseFloat(myCommentBean.getPicLevel()));
				peiheRatingBar.setRating(Float.parseFloat(myCommentBean.getCoordinateLevel()));
				showCommentText.setText(myCommentBean.getEvalContent());
			}
		}

		if (youCommentBean != null) {
			if("1".equals(youCommentBean.getFlag())){
				imageLoader.loadImage(youCommentBean.getUserIcon(), userImage1, true);
				userName1.setText(youCommentBean.getUserNick());
				zhuanyeRatingBar1.setRating(Float.parseFloat(youCommentBean.getExperienceLevel()));
				fuheRatingBar1.setRating(Float.parseFloat(youCommentBean.getPicLevel()));
				peiheRatingBar1.setRating(Float.parseFloat(youCommentBean.getCoordinateLevel()));
				showCommentText1.setText(youCommentBean.getEvalContent());
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.comfirm) {
			if (isGo()) {
				upLoadCommentsDatas();
			}
		}
	}

	private void upLoadCommentsDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.UpLoadCommentsForModelTask(${evaluateNotice})", this, null, visit).run();
	}

	public String getContent() {
		return commentText.getText().toString().trim();
	}

	public float getfuheRatingBar() {
		return fuheRatingBar.getRating();
	}

	public float getzhuanyeRatingBar() {
		return zhuanyeRatingBar.getRating();
	}

	public float getpeiheRatingBar() {
		return peiheRatingBar.getRating();
	}

	private boolean isGo() {
		if (fuheRatingBar.getRating() == 0) {
			Toast.makeText(getApplicationContext(), "请给模特与照片符合度打分", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (zhuanyeRatingBar.getRating() == 0) {
			Toast.makeText(getApplicationContext(), "请给模特经验与专业度打分", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (peiheRatingBar.getRating() == 0) {
			Toast.makeText(getApplicationContext(), "请给模特拍摄配合度打分", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(commentText.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void loadCommentsDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadCommentsForModelTask(${queryEvaluate})", this, null, visit).run();
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
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				allNoticeView.setTextColor(getResources().getColor(R.color.dark_gray));
				myNoticeView.setTextColor(getResources().getColor(R.color.dark_green));
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

	public void getCommentsInfo(List<Map> map) {
		for (Map subMap : map) {
			if ("1".equals(subMap.get("ind").toString())) {
				myCommentBean = new CommentBean();
				myCommentBean.setCoordinateLevel(subMap.get("coordinateLevel").toString());
				myCommentBean.setExperienceLevel(subMap.get("experienceLevel").toString());
				myCommentBean.setEvalContent(subMap.get("evalContent").toString());
				myCommentBean.setInd(subMap.get("ind").toString());
				myCommentBean.setPicLevel(subMap.get("picLevel").toString());
				myCommentBean.setUserIcon(subMap.get("userIcon").toString());
				myCommentBean.setUserNick(subMap.get("userNick").toString());
				myCommentBean.setFlag(subMap.get("flag").toString());
				if ("0".equals(myCommentBean.getCoordinateLevel())) {
					commentFlag = "N";
				} else {
					commentFlag = "Y";
				}
			} else if ("2".equals(subMap.get("ind").toString())) {
				youCommentBean = new CommentBean();
				youCommentBean.setCoordinateLevel(subMap.get("coordinateLevel").toString());
				youCommentBean.setExperienceLevel(subMap.get("experienceLevel").toString());
				youCommentBean.setEvalContent(subMap.get("evalContent").toString());
				youCommentBean.setInd(subMap.get("ind").toString());
				youCommentBean.setPicLevel(subMap.get("picLevel").toString());
				youCommentBean.setUserIcon(subMap.get("userIcon").toString());
				youCommentBean.setUserNick(subMap.get("userNick").toString());
				youCommentBean.setFlag(subMap.get("flag").toString());
			}
		}

		updateAllView();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String str = s.toString();
		numTips.setText(str.length() + "/100");
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	public void finishComments() {
		Toast.makeText(getApplicationContext(), "评论发表成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent("com.sg.finish.coment.formodel.action");
		intent.putExtra("userId", nsBean.getUserId());
		intent.putExtra("noticeId", noticeId);
		sendBroadcast(intent);
		finish();
	}
}

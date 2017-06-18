package com.xygame.sg.activity.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.personal.PrisePersonActivity;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.activity.personal.bean.TransferBrowersBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import base.action.Action;
import base.frame.VisitUnit;

public class ImagePagerCircleActivity extends FragmentActivity implements OnClickListener {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public static final String EXTRA_IMAGE_DELETE = "image_delete";
	private TextView titleName, rightButtonText, priseText, priseCount;
	private View backButton, rightButton, priseImage, shareView, priserView,topView,bottomView,topLine;
	private HackyViewPager mPager;
	private TransferBrowersBean tsBean;
	private int pagerPosition;
	private boolean isDelete = false, isEditor = false,isShowBinner=true;
	private List<BrowerPhotoesBean> browersDatas;
	private ImagePagerAdapter mAdapter;
	private List<String> deleteImages;
	private ImageView priseIcon,rightbuttonIcon;
	private String resId, seeUserId,userName;
	private static Animation alphaIn, alphaOut;
	private boolean isUpdate=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.le_image_detail_browers_pager);
		isEditor = getIntent().getBooleanExtra(EXTRA_IMAGE_DELETE, false);
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		priseImage = findViewById(R.id.priseImage);
		bottomView=findViewById(R.id.bottomView);
		topView=findViewById(R.id.topView);
		topLine=findViewById(R.id.topLine);
		titleName = (TextView) findViewById(R.id.titleName);
		priseCount = (TextView) findViewById(R.id.priseCount);
		priseText = (TextView) findViewById(R.id.priseText);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		rightbuttonIcon  = (ImageView) findViewById(R.id.rightbuttonIcon);
		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		priserView = findViewById(R.id.priserView);
		priseIcon = (ImageView) findViewById(R.id.priseIcon);
		shareView = findViewById(R.id.shareView);
		rightButton.setVisibility(View.GONE);
//		glaryId = getIntent().getStringExtra("glaryId");
		seeUserId = getIntent().getStringExtra("seeUserId");
		userName = getIntent().getStringExtra("userName");
		tsBean = (TransferBrowersBean) getIntent().getSerializableExtra("bean");
		browersDatas = tsBean.getBrowersDatas();
		deleteImages = new ArrayList<String>();
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), browersDatas);
		mPager.setAdapter(mAdapter);
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.GONE);
		rightButtonText.setText("...");
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.more_icon);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		titleName.setText(text);
		alphaIn = AnimationUtils.loadAnimation(this,
				R.anim.alpha_in);
		alphaOut = AnimationUtils.loadAnimation(this,
				R.anim.alpha_out);
		rightButton.setVisibility(View.GONE);
		backButton.setOnClickListener(this);
		priserView.setOnClickListener(this);
		shareView.setOnClickListener(this);
		priseImage.setOnClickListener(this);
		bottomView.setVisibility(View.GONE);

		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				pagerPosition = arg0;
				CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
				titleName.setText(text);
//				updateBottomView();
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
//		updateBottomView();
	}

	public String getSeeUserId() {
		return seeUserId;
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @see [类、类#方法、类#成员]
	 */
//	protected void updateBottomView() {
//		// TODO Auto-generated method stub
//		BrowerPhotoesBean item = browersDatas.get(pagerPosition);
//		String countStr = item.getPriseCount();
//		if (countStr == null || "".equals(countStr) || "null".equals(countStr)) {
//			priseCount.setText("0");
//		} else {
//			priseCount.setText(countStr);
//		}
//
//		if (item.isPrise()) {
//			priseIcon.setImageResource(R.drawable.sg_zan_yes);
//			priseText.setTextColor(getResources().getColor(R.color.prise_color));
//		} else {
//			priseIcon.setImageResource(R.drawable.sg_zan_no);
//			priseText.setTextColor(getResources().getColor(R.color.white));
//		}
//	}

//	public String getGlaryId() {
//		return glaryId;
//	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	public class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<BrowerPhotoesBean> datas;

		public List<BrowerPhotoesBean> getDatas() {
			return datas;
		}

		public ImagePagerAdapter(FragmentManager fm, List<BrowerPhotoesBean> datas) {
			super(fm);
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = datas.get(position).getImageUrl();
			ImageDetailFragment fragment=ImageDetailFragment.newInstance(url);
			fragment.addImageListener(new ImagePagerListener() {

				@Override
				public void onTopClickListener(Boolean flag) {
					// TODO Auto-generated method stub
					isShowBinner=!isShowBinner;
					if(isShowBinner){
						topView.startAnimation(alphaIn);
//						bottomView.startAnimation(alphaIn);
						topView.setVisibility(View.VISIBLE);
//						bottomView.setVisibility(View.VISIBLE);
						topLine.setVisibility(View.VISIBLE);
						quitFullScreen();
					}else{
						topView.startAnimation(alphaOut);
//						bottomView.startAnimation(alphaOut);
						topView.setVisibility(View.GONE);
//						bottomView.setVisibility(View.GONE);
						topLine.setVisibility(View.GONE);
						getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
								WindowManager.LayoutParams.FLAG_FULLSCREEN);
					}
				}
			});
			return fragment;
		}

	}

	private void quitFullScreen(){
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 重载方法
	 *
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rightButton) {
			if(UserPreferencesUtil.isOnline(this)){
				BrowerPhotoesBean item = browersDatas.get(pagerPosition);
				resId = item.getImageId();
				Intent intent = new Intent(this, ReportFristActivity.class);
				intent.putExtra("resType", Constants.JUBAO_TYPE_ZUOPING_TUPIAN);
				intent.putExtra("userId",seeUserId);
				intent.putExtra("resourceId",resId );
				startActivity(intent);
			}else{
				Intent intent = new Intent(this, LoginWelcomActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == R.id.backButton) {
			finishResult();
		} else if (v.getId() == R.id.priseImage) {
			boolean islogin = UserPreferencesUtil.isOnline(this);
			if (!islogin) {
				Intent intent = new Intent(this, LoginWelcomActivity.class);
				startActivity(intent);
			} else {
				BrowerPhotoesBean item = browersDatas.get(pagerPosition);
				resId = item.getImageId();
				if (browersDatas.get(pagerPosition).isPrise()) {
					isUpdate=true;
					cancelPriseZnuoPing();
				} else {
					isUpdate=true;
					priseZnuoPing();
				}
			}
		} else if (v.getId() == R.id.shareView) {
			Intent intent = new Intent(this, ShareBoardView.class);
			intent.putExtra(Constants.SHARE_SUBTITLE_KEY,userName+"--来自【模范儿】");
			intent.putExtra(Constants.SHARE_ICONURL_KEY,browersDatas.get(pagerPosition).getImageUrl());
			startActivity(intent);
		} else if (v.getId() == R.id.priserView) {
			Intent intent = new Intent(this, PrisePersonActivity.class);
			intent.putExtra("seeUserId", seeUserId);
			intent.putExtra("imageId", browersDatas.get(pagerPosition).getImageId());
			intent.putExtra("imageUrl", browersDatas.get(pagerPosition).getImageUrl());
//			intent.putExtra("glaryId", glaryId);
			startActivity(intent);
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @see [类、类#方法、类#成员]
	 */
	private void cancelPriseZnuoPing() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.PriseCancelZuoPingTask(${cancelPraiseModelGallery})", this, null, visit).run();
	}

	public String getResId() {
		return resId;
	}

	private void priseZnuoPing() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.PriseZuoPingTask(${praiseModelGallery})", this, null, visit).run();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishResult();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void finishResult() {
		tsBean.setBrowersDatas(browersDatas);
		Intent intent = new Intent("action.update.photoes.page");
		intent.putExtra(Constants.COMEBACK, tsBean);
		intent.putExtra("isUpdate",isUpdate);
		sendBroadcast(intent);
		finish();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @action [请添加内容描述]
	 */
	public void refreshUI() {
		// TODO Auto-generated method stub
		boolean iszan = browersDatas.get(pagerPosition).isPrise();
		browersDatas.get(pagerPosition).setPrise(!iszan);
		if (iszan) {
			Toast.makeText(getApplicationContext(), "已取消点赞", Toast.LENGTH_SHORT).show();
			browersDatas.get(pagerPosition).setPriseCount(
					String.valueOf(Integer.parseInt(browersDatas.get(pagerPosition).getPriseCount()) - 1));
		} else {
			Toast.makeText(getApplicationContext(), "点赞成功", Toast.LENGTH_SHORT).show();
			browersDatas.get(pagerPosition).setPriseCount(
					String.valueOf(Integer.parseInt(browersDatas.get(pagerPosition).getPriseCount()) + 1));
		}
//		updateBottomView();
	}

	@Override
	public void onDestroy() {
		System.gc();
		super.onDestroy();
	}

}
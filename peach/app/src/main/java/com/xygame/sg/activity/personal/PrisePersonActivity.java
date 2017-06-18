package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.PriseAdapter;
import com.xygame.sg.activity.personal.bean.PriseBean;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.task.personal.LoadGalleryPraiseTask;
import com.xygame.sg.utils.Constants;

import android.content.Intent;
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
import base.action.Action;
import base.frame.VisitUnit;

public class PrisePersonActivity extends SGBaseActivity implements OnClickListener{
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
	private PriseAdapter priseAdapter,mypriseAdapter;
	private String seeUserId,glaryId,imageId,typeFlag="1",imageUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_community_layout);
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
		titleName.setText("点赞用户");
		seeUserId=getIntent().getStringExtra("seeUserId");
		glaryId=getIntent().getStringExtra("glaryId");
		imageId=getIntent().getStringExtra("imageId");
		imageUrl=getIntent().getStringExtra("imageUrl");
		priseAdapter=new PriseAdapter(this, null,true);
		mypriseAdapter=new PriseAdapter(this, null,false);
		priseList.setAdapter(priseAdapter);
		myPriseList.setAdapter(mypriseAdapter);
		mPager.setCurrentItem(0);
		if(imageId==null){
			loadPriserDatas();
		}else{
			loadMypriseDatas();
		}
	}
	
	public String getTypeFlag() {
		return typeFlag;
	}

	public String getImageId() {
		return imageId;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} 
	}
	
	public String getSeeUserId() {
		return seeUserId;
	}

	public String getGlaryId() {
		return glaryId;
	}

	private void loadPriserDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
//		new Action("#.personal.LoadGalleryPraiseTask(${queryGalleryPraiseUser})", this, null, visit).run();
		new Action(LoadGalleryPraiseTask.class,"${queryGalleryPraiseUser}", this, null, visit).run();
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
				typeFlag="1";
				if(priseList.getCount()==0){
					if(imageId==null){
						loadPriserDatas();
					}else{
						loadMypriseDatas();
					}
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				typeFlag="2";
				allNoticeView.setTextColor(getResources().getColor(R.color.dark_gray));
				myNoticeView.setTextColor(getResources().getColor(R.color.dark_green));
				if(myPriseList.getCount()==0){
					if(imageId==null){
						loadPriserDatas();
					}else{
						loadMypriseDatas();
					}
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
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void parseDatasRefresh(Map map) {
		// TODO Auto-generated method stub
		List<Map> list=(List<Map>) map.get("praises");
		if(list!=null){
			List<PriseBean> datas=new ArrayList<PriseBean>();
			for(Map sMap:list){
				PriseBean it=new PriseBean();
				it.setPraiseTimeMillis(sMap.get("praiseTimeMillis").toString());
				it.setResUrl(sMap.get("resUrl").toString());
				it.setUserIcon(sMap.get("userIcon").toString());
				it.setUserId(sMap.get("userId").toString());
				it.setUsernick(sMap.get("usernick").toString());
				datas.add(it);
			}
			if("1".equals(typeFlag)){
				priseAdapter.addDatas(datas);
				priseAdapter.notifyDataSetChanged();
			}else{
				mypriseAdapter.addDatas(datas);
				mypriseAdapter.notifyDataSetChanged();
			}
			
		}
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void loadMypriseDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.LoadGalleryMyPraiseTask(${queryGalleryPicPraiseUser})", this, null, visit).run();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void parseMyDatasRefresh(Map map) {
		// TODO Auto-generated method stub
		List<Map> list=(List<Map>) map.get("praises");
		if(list!=null){
			List<PriseBean> datas=new ArrayList<PriseBean>();
			for(Map sMap:list){
				PriseBean it=new PriseBean();
				it.setPraiseTimeMillis(sMap.get("praiseTimeMillis").toString());
				it.setResUrl(imageUrl);
				it.setUserIcon(sMap.get("userIcon").toString());
				it.setUserId(sMap.get("userId").toString());
				it.setUsernick(sMap.get("usernick").toString());
				datas.add(it);
			}
			if("1".equals(typeFlag)){
				priseAdapter.addDatas(datas);
				priseAdapter.notifyDataSetChanged();
			}else{
				mypriseAdapter.addDatas(datas);
				mypriseAdapter.notifyDataSetChanged();
			}
		}
	}
}

package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.SignedAdapter;
import com.xygame.sg.activity.notice.bean.SignedBean;
import com.xygame.sg.activity.personal.bean.PriseBean;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.zxing.encoding.EncodingHandler;

import android.graphics.Bitmap;
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
import base.data.net.http.DesUtils;
import base.frame.VisitUnit;

public class SignedActivity extends SGBaseActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView allNoticeView, myNoticeView, titleName, userName;
	private ListView myPriseList;
	private View backButton;
	/**
	 * viewPage部分
	 */
	private LayoutInflater mInflater;
	private ViewPager mPager;// 页内容
	private List<View> listViews; // 页面列表

	private ImageView cursor, userType, userZxingImage;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private SignedAdapter mypriseAdapter;
	private CircularImage userImage;
	private ImageLoader imageLoader;
	private String noticeId;
	
	public String getNoticeId(){
		return noticeId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.siged_layout, null));
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
		listViews.add(mInflater.inflate(R.layout.camera_zxing_layout, null));
		listViews.add(mInflater.inflate(R.layout.prise_person_my_layout, null));
		userImage = (CircularImage) listViews.get(0).findViewById(R.id.userImage);
		userName = (TextView) listViews.get(0).findViewById(R.id.userName);
		userType = (ImageView) listViews.get(0).findViewById(R.id.userType);
		userZxingImage = (ImageView) listViews.get(0).findViewById(R.id.userZxingImage);
		myPriseList = (ListView) listViews.get(1).findViewById(R.id.myPriseList);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	private void initListensers() {
		allNoticeView.setOnClickListener(new MyOnClickListener(0));
		myNoticeView.setOnClickListener(new MyOnClickListener(1));
		backButton.setOnClickListener(this);
	}

	private void initDatas() {
		noticeId = getIntent().getStringExtra("noticeId");
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		titleName.setText("拍摄签到");
		mypriseAdapter = new SignedAdapter(this, null);
		myPriseList.setAdapter(mypriseAdapter);
		mPager.setCurrentItem(0);
		updateFirstPage();
	}

	private void updateFirstPage() {
		userName.setText(UserPreferencesUtil.getUserNickName(this));
		String typeStr = UserPreferencesUtil.getUserType(this);
		if (Constants.CARRE_MODEL.equals(typeStr) || Constants.PRO_MODEL.equals(typeStr)) {
			userType.setImageResource(R.drawable.model_icon);
		} else if (Constants.CARRE_PHOTOR.equals(typeStr)) {
			userType.setImageResource(R.drawable.cm_icon);
		}
		imageLoader.loadImage(UserPreferencesUtil.getHeadPic(this), userImage, true);
		try {
			String userId = UserPreferencesUtil.getUserId(this);
			String dimenLng = BaiduPreferencesUtil.getLon(this);
			String dimenLat = BaiduPreferencesUtil.getLat(this);
			
			ProvinceBean pbBean=BaiduPreferencesUtil.getLocalProvinceBean(BaiduPreferencesUtil.getProvice(this));
			CityBean ctBean=BaiduPreferencesUtil.getLocalCityBean(pbBean,  BaiduPreferencesUtil.getCity(this));
			String dimenProvince = pbBean.getProvinceCode();
			String dimenCity =ctBean.getCityCode();
			
			String dimenAddress = BaiduPreferencesUtil.getStreet(this);
			String dimenCreateTime = String.valueOf(System.currentTimeMillis());
			JSONObject ob = new JSONObject();
			ob.put("noticeId", noticeId);

			ob.put("userId", userId);
			ob.put("dimenLng", dimenLng);
			ob.put("dimenLat", dimenLat);
			ob.put("dimenProvince", dimenProvince);
			ob.put("dimenCity", dimenCity);
			ob.put("dimenAddress", dimenAddress);
			ob.put("dimenCreateTime", dimenCreateTime);
			String zxingStr = ob.toString();
			String secretStr= DesUtils.encrypt(zxingStr);
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(secretStr, 350);
			userZxingImage.setImageBitmap(qrCodeBitmap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		}
	}

	private void loadSignDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.QueryNoticeSignTask(${queryNoticeSign})", this, null, visit).run();
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
				if(mypriseAdapter.getCount()==0){
					loadSignDatas();
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
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void parseDatasRefresh(Map map) {
		// TODO Auto-generated method stub
		List<Map> list = (List<Map>) map.get("praises");
		if (list != null) {
			List<PriseBean> datas = new ArrayList<PriseBean>();
			for (Map sMap : list) {
				PriseBean it = new PriseBean();
				it.setPraiseTimeMillis(sMap.get("praiseTimeMillis").toString());
				it.setResUrl(sMap.get("resUrl").toString());
				it.setUserIcon(sMap.get("userIcon").toString());
				it.setUserId(sMap.get("userId").toString());
				it.setUsernick(sMap.get("usernick").toString());
				datas.add(it);
			}
		}
	}

	public void getSignInfo(List<Map> map) {
		// TODO Auto-generated method stub
		List<SignedBean> datas=new ArrayList<SignedBean>();
		for(Map subMap:map){
			SignedBean item =new SignedBean();
			item.setSignTime(subMap.get("signTime").toString());
			item.setUserIcon(subMap.get("userIcon").toString());
			item.setUserId(subMap.get("userId").toString());
			item.setUsernick(subMap.get("usernick").toString());
			datas.add(item);
		}
		mypriseAdapter.addDatas(datas);
	}
}

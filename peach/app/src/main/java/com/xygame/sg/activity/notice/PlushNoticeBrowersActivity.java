/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.define.gridview.GridViewInScorllView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
public class PlushNoticeBrowersActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

	private TextView titleName,daoJiShiValues,camreTheme;
	private ProgressBar progress_horizontal_color;
	private ImageView rightbuttonIcon;
	private View backButton, rightButton, comfirmButton,isShowLine;
	private PlushNoticeBean pnBean;
	private String photoName;
	private GridViewInScorllView modelPicGridView;
	private LinkedList<String> picDatas;
	private MyGridViewAdapter adapter;
	private List<PhotoesBean> uploadImages;
	private int currIndex = 0;
	private ImageLoader imageLoader;
	private boolean isSeucss=false;
	private LinearLayout cameraInfoView,modelRequestView;
	private long hours;
	private long minutes;
	private long diff;
	private long days;
	
	private long hours1;
	private long minutes1;
	private long diff1;
	private long days1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_plush_notice_browers_layout, null));

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
		rightButton.setOnClickListener(this);
		comfirmButton.setOnClickListener(this);
		modelPicGridView.setOnItemClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		daoJiShiValues=(TextView)findViewById(R.id.daoJiShiValues);
		camreTheme=(TextView)findViewById(R.id.camreTheme);
		cameraInfoView=(LinearLayout)findViewById(R.id.cameraInfoView);
		modelRequestView=(LinearLayout)findViewById(R.id.modelRequestView);
		progress_horizontal_color=(ProgressBar)findViewById(R.id.progress_horizontal_color);
		backButton = findViewById(R.id.backButton);
		isShowLine=findViewById(R.id.isShowLine);
		rightButton = findViewById(R.id.rightButton);
		rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
		comfirmButton = findViewById(R.id.comfirmButton);
		modelPicGridView = (GridViewInScorllView) findViewById(R.id.modelPicGridView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		picDatas = new LinkedList<String>();
		pnBean = (PlushNoticeBean) getIntent().getSerializableExtra("bean");
		titleName.setText("预览通告");
		rightButton.setVisibility(View.VISIBLE);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.sg_con_service);
		updateAllViews();
	}
	
	private void updateAllViews(){
		getTime();
		setPrograssBar();
		camreTheme.setText(pnBean.getCameraTheme());
		updateNoticeCondition();
		updateModelRequestViews();
		if (pnBean.getPhotoes() != null) {
			uploadImages=pnBean.getPhotoes();
			for(PhotoesBean it:uploadImages){
				picDatas.add(it.getImageUrl());
			}
		} else {
			uploadImages = new ArrayList<PhotoesBean>();
		}
		updateChoiceImages();
	}
	
	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void updateModelRequestViews() {
		LayoutParams lp = new LayoutParams(30, 10);
		List<ModelRequestBean> strList=pnBean.getModelBeans();
		LinearLayout firstLine,secondLine,thirdLine,secondMoreLine;
		TextView priceValue;
		ImageView tagIcon;
		TextView tagText;
		View isShowLine;
		for(int i=0;i<strList.size();i++){
			ModelRequestBean item=strList.get(i);
			View convertView = LayoutInflater.from(this).inflate(R.layout.sg_model_request_item,
					null);
			priceValue = (TextView) convertView
					.findViewById(R.id.priceValue);
			firstLine =(LinearLayout)convertView
					.findViewById(R.id.firstLine);
			isShowLine=convertView.findViewById(R.id.isShowLine);
			secondLine=(LinearLayout)convertView.findViewById(R.id.secondLine);
			thirdLine=(LinearLayout)convertView.findViewById(R.id.thirdLine);
			secondMoreLine=(LinearLayout)convertView.findViewById(R.id.secondMoreLine);
			priceValue.setText(item.getNeedPrice());
			if(item.getSexName()!=null){
				View secondViewsfirst=getFristSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				if("男".equals(item.getSexName())){
					tagIcon.setImageResource(R.drawable.sg_pl_man);
				}else if("女".equals(item.getSexName())){
					tagIcon.setImageResource(R.drawable.sg_pl_woman);
				}
				tagText.setText(item.getSexName());
				firstLine.addView(secondViewsfirst);
			}
			
			if(item.getNeedNum()!=null){
				View secondViewsfirst=getSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_ren_num);
				tagText.setText(item.getNeedNum());
				firstLine.addView(secondViewsfirst);
			}
			
			if(item.getSmallAge()!=null){
				View secondViewsfirst=getFristSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_age);
				tagText.setText(item.getSmallAge().concat("-").concat(item.getBigAge()).concat("岁"));
				secondMoreLine.addView(secondViewsfirst);
			}
			
			if(item.getSmallBodyHight()!=null){
				View secondViewsfirst=getSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_sg);
				tagText.setText(item.getSmallBodyHight().concat("cm").concat("-").concat(item.getBigBodyHight()).concat("cm"));
				secondMoreLine.addView(secondViewsfirst);
			}
			
			if(item.getCityName()!=null){
				View secondViewsfirst=getFristSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_city);
				tagText.setText(item.getCityName());
				secondLine.addView(secondViewsfirst);
			}
			
			if(item.getCountryName()!=null){
				View secondViewsfirst=getSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_country);
				tagText.setText(item.getCountryName());
				secondLine.addView(secondViewsfirst);
			}
			
			if(item.isBaoXiaoCaiLv()){
				View secondViewsfirst=getSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_clf);
				tagText.setText("报销异地差旅费");
				thirdLine.addView(secondViewsfirst);
				View nullView5=new View(this);
				nullView5.setLayoutParams(lp);
				thirdLine.addView(nullView5);
			}
			
			if(item.isBaoXiaoZhuSu()){
				View secondViewsfirst=getSubView();
				tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
				tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
				tagIcon.setImageResource(R.drawable.sg_pl_zsf);
				tagText.setText("报销异地住宿费");
				thirdLine.addView(secondViewsfirst);
			}
			
			if(firstLine.getChildCount()==0){
				firstLine.setVisibility(View.GONE);
			}
			if(secondMoreLine.getChildCount()==0){
				secondMoreLine.setVisibility(View.GONE);
			}
			if(secondLine.getChildCount()==0){
				secondLine.setVisibility(View.GONE);
			}
			if(thirdLine.getChildCount()==0){
				thirdLine.setVisibility(View.GONE);
			}
			if(i==strList.size()-1){
				isShowLine.setVisibility(View.GONE);
			}
			modelRequestView.addView(convertView);
			
		}
	}
	
	public View getSubView(){
		return LayoutInflater.from(this).inflate(R.layout.sg_model_request_item_item,
				null);
	}
	
	public View getFristSubView(){
		return LayoutInflater.from(this).inflate(R.layout.sg_model_request_item_item_item,
				null);
	}

	private void updateNoticeCondition(){
		LayoutParams lp = new LayoutParams(10, 10);
		ImageView tagIcon;
		TextView tagText;
		if(pnBean.getStarTime()!=null){
			View secondViewsfirst=getConditionView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_date);
			tagText.setText(CalendarUtils.getXieGongDateDis(Long.parseLong(pnBean.getStarTime())).concat("—").concat(CalendarUtils.getXieGongDateDis(Long.parseLong(pnBean.getEndTime()))));
			cameraInfoView.addView(secondViewsfirst);
			View nullView5=new View(this);
			nullView5.setLayoutParams(lp);
			cameraInfoView.addView(nullView5);
		}
		if(pnBean.getCameraArea().getProvinceName()!=null){
			View secondViewsfirst=getConditionView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_city);
			if(pnBean.getCameraArea().getProvinceName().equals(pnBean.getCameraArea().getCityName())){
				tagText.setText(pnBean.getCameraArea().getProvinceName().concat(pnBean.getCameraArea().getAddress()==null?"":pnBean.getCameraArea().getAddress()));
			}else{
				tagText.setText(pnBean.getCameraArea().getProvinceName().concat(pnBean.getCameraArea().getCityName()).concat(pnBean.getCameraArea().getAddress()==null?"":pnBean.getCameraArea().getAddress()));
			}
			cameraInfoView.addView(secondViewsfirst);
			View nullView=new View(this);
			nullView.setLayoutParams(lp);
			cameraInfoView.addView(nullView);
		}
		if(pnBean.getNoticeTip()!=null){
			View secondViewsfirst=getConditionView();
			tagIcon=(ImageView)secondViewsfirst.findViewById(R.id.tagIcon);
			tagText=(TextView)secondViewsfirst.findViewById(R.id.tagText);
			tagIcon.setImageResource(R.drawable.sg_pl_ly);
			tagText.setText(pnBean.getNoticeTip());
			cameraInfoView.addView(secondViewsfirst);
		}
	}
	
	public View getConditionView(){
		return LayoutInflater.from(this).inflate(R.layout.sg_notice_condition_item,
				null);
	}
	
	private void getTime() {
		String endTime;
		if(pnBean.getReportTime()!=null){
			endTime=pnBean.getReportTime();
		}else{
			endTime=pnBean.getEndTime();
		}
		try {
			Date d1 = new Date(Long.parseLong(endTime));
			Date d2 = new Date();
			diff = (d1.getTime() - d2.getTime())/1000;// 这样得到的差值是微秒级别
			days = diff / ( 60 * 60 * 24);
			hours = (diff % (24 * 60 * 60)) / (60 * 60);
			minutes = ((diff % (24 * 60 * 60)) % (60 * 60)) / 60;

			String dayStr=days==0?"":String.valueOf(days).concat("天");
			String hourStr=hours==0?"":String.valueOf(hours).concat("时");
			String minStr=minutes==0?"":String.valueOf(minutes).concat("分");
			daoJiShiValues.setText("报名截止时间："+dayStr.concat(hourStr).concat(minStr));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPrograssBar() {
		String endTime=pnBean.getEndTime();
		try {
			Date d1 = new Date(Long.parseLong(endTime));
			Date d2 = new Date(Long.parseLong(pnBean.getStarTime()));
			diff1 = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
			minutes1 = diff1 / (1000 * 60);
			long leftMin= diff / (1000 * 60);
			progress_horizontal_color.setMax(new Long(minutes1).intValue());
			progress_horizontal_color.setProgress(new Long(leftMin).intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateChoiceImages() {
		adapter = new MyGridViewAdapter(this, picDatas);
		modelPicGridView.setAdapter(adapter);
		if (picDatas.size() > 0) {
			isShowLine.setVisibility(View.VISIBLE);
			modelPicGridView.setVisibility(View.VISIBLE);
		}else{
			isShowLine.setVisibility(View.GONE);
			modelPicGridView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 重载方法
	 */
	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, pnBean);
		intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, isSeucss);
		setResult(Activity.RESULT_OK, intent);
		super.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.rightButton) {
//			//联系客服
//			if (UserPreferencesUtil.getServicePhone(this)!=null&&!"null".equals(UserPreferencesUtil.getServicePhone(this))){
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPreferencesUtil.getServicePhone(this)));
//				startActivity(intent);
//			}else{
//				Toast.makeText(this, "系统维护中", Toast.LENGTH_SHORT).show();
//			}
		} else if (v.getId() == R.id.comfirmButton) {
			transferLocationService();
		}
	}

	public class MyGridViewAdapter extends BaseAdapter {

		private Context context;
		private List<String> datas;

		/**
		 * <默认构造函数>
		 */
		public MyGridViewAdapter(Context context, List<String> datas) {
			this.context = context;
			if (datas != null) {
				this.datas = datas;
			} else {
				this.datas = new ArrayList<String>();
			}
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public String getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View picView = inflater.inflate(R.layout.sg_create_modelpic_item, null);
			ImageView picIBtn = (ImageView) picView.findViewById(R.id.pic);
			String headUrl = datas.get(position);
			if (headUrl.contains("http://")) {
				imageLoader.loadImage(headUrl, picIBtn, true);
			} else {
				ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(headUrl, picIBtn);
			}
			return picView;
		}

		public List<String> getImages() {
			List<String> tempImages = new ArrayList<String>();
			tempImages.addAll(datas);
			return tempImages;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		List<String> tempImages = adapter.getImages();
		if (position < tempImages.size()) {
			Constants.imageBrower(PlushNoticeBrowersActivity.this, position,
					tempImages.toArray(new String[tempImages.size()]), false);
		}
	}

	private void uploadImages() {
		ShowMsgDialog.show(this, "图片上传中...", false);
		uploadImages.clear();
		List<String> datas = adapter.getImages();
		for (int i = 0; i < datas.size(); i++) {
			PhotoesBean pb = new PhotoesBean();
			pb.setImageIndex(String.valueOf(i + 1));
			pb.setImageUrl(datas.get(i));
			uploadImages.add(pb);
		}
		doUploadImages();
	}

	public List<PhotoesBean> getUploadImages() {
		return uploadImages;
	}

	private void doUploadImages() {
		if (currIndex < uploadImages.size()) {
			if (!uploadImages.get(currIndex).getImageUrl().contains("http://")) {

				AliySSOHepler.getInstance().uploadImageEngine(this,Constants.NOTICE_PHOTOES_PATH,
						uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

							@Override
							public void onSuccess(String imageUrl, int requestCode) {
								Message msg = new Message();
								msg.obj = imageUrl;
								msg.what = 1;
								handler.sendMessage(msg);
							}

							@Override
							public void onFailure(int errorCode, String msg, int requestCode) {
								// TODO Auto-generated method stub
								Message msg1 = new Message();
								msg1.what = 2;
								handler.sendMessage(msg1);
							}

							@Override
							public void onProgress(String objectKey, int byteCount, int totalSize) {
								// TODO Auto-generated method stub
								
							}
						});
			} else {
				doUploadImages();
			}
		} else {
			ShowMsgDialog.cancel();
			transferLocationService();
		}
	}
	
	

	public PlushNoticeBean getPnBean() {
		return pnBean;
	}



	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (null != msg) {
				switch (msg.what) {
				case 1:
					String imageUrl = (String) msg.obj;
					uploadImages.get(currIndex).setImageUrl(imageUrl);
					currIndex = currIndex + 1;
					doUploadImages();
					break;
				case 2:
					if (currIndex > 0) {
						transferLocationService();
					} else {
						ShowMsgDialog.cancel();
						Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
						finish();
					}
					break;
				default:
					break;
				}
			}
		}
	};
	
	public void finishUpload(String noticeId) {
		pnBean.setNoticeId(noticeId);
		if("1".equals(pnBean.getNoticeType())){
			Intent firstIntent = new Intent(this, PlushNoticeNormalSuccessActivity.class);
			firstIntent.putExtra("bean", pnBean);
			startActivityForResult(firstIntent, 0);
		}else{
			Intent firstIntent = new Intent(this, PlushNoticePaySuccessActivity.class);
			firstIntent.putExtra("bean", pnBean);
			startActivityForResult(firstIntent, 1);
		}
	}

	private void transferLocationService() {
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.UploadNoticeTask(${publishNotice})", this, null, visit).run();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			isSeucss=data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			finish();
			break;
		}
		case 1:
			isSeucss=data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			finish();
			break;

		default:
			break;
		}
		
	}
}

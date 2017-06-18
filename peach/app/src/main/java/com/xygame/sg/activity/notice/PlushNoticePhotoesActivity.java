/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
public class PlushNoticePhotoesActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

	private TextView titleName;
	private ImageView rightbuttonIcon;
	private View backButton, rightButton, comfirmButton;
	private PlushNoticeBean pnBean;
	private EditText introduceText;
	private String photoName;
	private GridView modelPicGridView;
	private LinkedList<String> picDatas;
	private MyGridViewAdapter adapter;
	private List<PhotoesBean> uploadImages;
	private int currIndex = 0;
	private ImageLoader imageLoader;
	private boolean isSeucss=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_plush_notice_photoes_layout, null));

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
		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
		comfirmButton = findViewById(R.id.comfirmButton);
		introduceText = (EditText) findViewById(R.id.introduceText);
		modelPicGridView = (GridView) findViewById(R.id.modelPicGridView);
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
		titleName.setText("招募模特要求");
		rightButton.setVisibility(View.VISIBLE);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.sg_con_service);
		if(pnBean.getNoticeTip()!=null){
			introduceText.setText(pnBean.getNoticeTip());
		}
		if (pnBean.getPhotoes() != null) {
			uploadImages=pnBean.getPhotoes();
			for(PhotoesBean it:uploadImages){
				picDatas.add(it.getImageUrl());
			}
		} else {
			uploadImages = new ArrayList<PhotoesBean>();
		}
		updateChoiceImages();
		imageListeners();

	}

	private void updateChoiceImages() {
		picDatas.add(null);
		adapter = new MyGridViewAdapter(this, picDatas);
		modelPicGridView.setAdapter(adapter);
	}
	
	/**
	 * 重载方法
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		String tipContent=introduceText.getText().toString().trim();
		if(!"".equals(tipContent)){
			pnBean.setNoticeTip(tipContent);
		}
		List<PhotoesBean> tempDatas=new ArrayList<PhotoesBean>();
		List<String> datas = adapter.getImages();
		for (int i = 0; i < datas.size(); i++) {
			PhotoesBean pb = new PhotoesBean();
			pb.setImageIndex(String.valueOf(i + 1));
			pb.setImageUrl(datas.get(i));
			tempDatas.add(pb);
		}
		pnBean.setPhotoes(tempDatas);
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
			String tipContent=introduceText.getText().toString().trim();
			if(!"".equals(tipContent)){
				pnBean.setNoticeTip(tipContent);
			}
			List<PhotoesBean> tempDatas=new ArrayList<PhotoesBean>();
			List<String> datas = adapter.getImages();
			for (int i = 0; i < datas.size(); i++) {
				PhotoesBean pb = new PhotoesBean();
				pb.setImageIndex(String.valueOf(i + 1));
				pb.setImageUrl(datas.get(i));
				tempDatas.add(pb);
			}
			pnBean.setPhotoes(tempDatas);
			Intent firstIntent = new Intent(this, PlushNoticeBrowersActivity.class);
			firstIntent.putExtra("bean", pnBean);
			startActivityForResult(firstIntent, 3);
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

		public void clearDatas() {
			datas.clear();
		}

		public void addDatas(List<String> datas) {
			this.datas.addAll(datas);
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			if (position == datas.size() - 1) {
				View addView = inflater.inflate(R.layout.sg_create_modelpic_item_add, null);
				addView.findViewById(R.id.add).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (AliPreferencesUtil.getBuckekName(context)==null){
							new Action(ResponseAliParams.class,"${ali_params}",PlushNoticePhotoesActivity.this, null, new VisitUnit()).run();
							Toast.makeText(context, "加载配置稍后重试", Toast.LENGTH_SHORT)
									.show();
						}else{
							if (datas.size() - 1 == 8) {
								Toast.makeText(getApplicationContext(), "已达到最多图片数量", Toast.LENGTH_SHORT).show();
								return;
							} else {
								showChoiceWays();
							}
						}
					}
				});
				return addView;
			} else {
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
		}

		public List<String> getImages() {
			List<String> tempImages = new ArrayList<String>();
			tempImages.addAll(datas);
			tempImages.remove(tempImages.size() - 1);
			return tempImages;
		}
	}

	private void showChoiceWays() {// chicePhoto();
		Intent intent = new Intent(this, PickPhotoesView.class);
		startActivityForResult(intent, 1);
	}

	private void chicePhoto() {
		TransferImagesBean dataBean = new TransferImagesBean();
		picDatas.remove(picDatas.size() - 1);
		dataBean.setSelectImagePah(picDatas);
		Intent intent = new Intent(this, ImageChooserActivity.class);
		intent.putExtra(Constants.TRANS_PIC_NUM, 8);
		intent.putExtra("images", dataBean);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				updateChoiceImages();
				return;
			}
			Object result = data.getSerializableExtra(Constants.COMEBACK);
			if (result != null) {
				TransferImagesBean dataBean = (TransferImagesBean) result;
				picDatas.clear();
				LinkedList<String> tempStrs = dataBean.getSelectImagePath();
				for (int i = 0; i < tempStrs.size(); i++) {
					if (tempStrs.get(i).contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
						picDatas.add(tempStrs.get(i));
					} else {
						Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
						if (subBitmap != null) {
							String photoName = Constants.getImageName(this);
							FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
							subBitmap.recycle();
							picDatas.add(FileUtil.getPhotopath(photoName));
						}
					}
				}
				updateChoiceImages();
			}
			break;
		}
		case 1:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String flag = data.getStringExtra(Constants.COMEBACK);
			if ("galary".equals(flag)) {
				chicePhoto();
			} else if ("camera".equals(flag)) {
				takePhoto();
			}
			break;

		case 2:
			try {
				Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
				if (bitmap != null) {
					picDatas.remove(picDatas.size() - 1);
					FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
					String photoPath = FileUtil.getPhotopath(photoName);
					picDatas.add(photoPath);
					updateChoiceImages();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			break;
		case 3:
			pnBean = (PlushNoticeBean) data.getSerializableExtra(Constants.COMEBACK);
			isSeucss = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			if (isSeucss) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, pnBean);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, isSeucss);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void takePhoto() {
		// 跳转至拍照界面
		photoName = String.valueOf(System.currentTimeMillis()).concat(".png");
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		File out = new File(FileUtil.getPhotopath(photoName));
		Uri imageUri = Uri.fromFile(out);
		// 获取拍照后未压缩的原图片，并保存在uri路径中
		intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intentPhote, 2);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void imageListeners() {
		SGApplication.getInstance().addImageListener(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent.getAction())) {
					boolean flag = intent.getBooleanExtra(Constants.IS_DELE_IMAGES, false);
					if (!flag) {
						return;
					}
					Object result = intent.getSerializableExtra(Constants.COMEBACK);
					if (result != null) {
						TransferImagesBean dataBean = (TransferImagesBean) result;
						picDatas.clear();
						LinkedList<String> tempStrs = dataBean.getSelectImagePath();
						for (int i = 0; i < tempStrs.size(); i++) {
							if (tempStrs.get(i).contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
								picDatas.add(tempStrs.get(i));
							} else {
								Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
								if (subBitmap != null) {
									String photoName = Constants.getImageName(PlushNoticePhotoesActivity.this);
									FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
									subBitmap.recycle();
									picDatas.add(FileUtil.getPhotopath(photoName));
								}
							}
						}
						updateChoiceImages();
					}
				}
			}
		});
		SGApplication.getInstance().registerImagesReceiver();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		List<String> tempImages = adapter.getImages();
		if (position < tempImages.size()) {
			Constants.imageBrower(PlushNoticePhotoesActivity.this, position,
					tempImages.toArray(new String[tempImages.size()]), true);
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

	public String getOral() {
		return introduceText.getText().toString().trim();
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
	
	public void finishUpload() {
		isSeucss=true;
		finish();
	}

	private void transferLocationService() {
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.UploadNoticeTask(${publishNotice})", this, null, visit).run();
	}

	/**
	 * 重载方法
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		SGApplication.getInstance().unregisterImagesReceiver();
		super.onDestroy();
	}
}

/*
 * 文 件 名:  RegisterSecondPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.init.UploadDeviceTokenTask;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageKeyHepler;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
 * @date 2015年11月3日
 * @action [用户注册第二个界面]
 */
public class DoUserInfoActivity extends SGBaseActivity implements OnClickListener{

	private View closeLoginWel;

	private Uri imageUri, originalUri;

	private ImageView manIcon, womanIcon;

	private TextView manText, womanText, iamModel, iamPhotor, birthdayView;

	private View manViewBack, womanViewBack, modelBack, photorBack, comfirm;

	/** 头像款 */
	private int w = 200;
	/** 头像高 */
	private int h = 200;

	private CircularImage userImage;

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP_PICTURE = 3;
	private String photoName;

	public String photoPath = "";

	private String sexStr = Constants.SEX_WOMAN;

	private String carrieType = Constants.CARRE_MODEL;

	private ImageLoader imageLoader;

	private EditText nickName;
	
	private byte[] dataToUpload;

	/**
	 * 重载方法
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_jihuo_layout, null));
		initViews();
		initListeners();
		initDatas();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		comfirm = findViewById(R.id.comfirm);
		closeLoginWel = findViewById(R.id.closeLoginWel);
		manViewBack = findViewById(R.id.manViewBack);
		womanViewBack = findViewById(R.id.womanViewBack);
		modelBack = findViewById(R.id.modelBack);
		photorBack = findViewById(R.id.photorBack);

		manIcon = (ImageView) findViewById(R.id.manIcon);
		womanIcon = (ImageView) findViewById(R.id.womanIcon);

		manText = (TextView) findViewById(R.id.manText);
		womanText = (TextView) findViewById(R.id.womanText);
		iamModel = (TextView) findViewById(R.id.iamModel);
		iamPhotor = (TextView) findViewById(R.id.iamPhotor);
		birthdayView = (TextView) findViewById(R.id.birthdayView);

		userImage = (CircularImage) findViewById(R.id.userImage);

		nickName = (EditText) findViewById(R.id.nickName);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		closeLoginWel.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		updateAllViews();
	}

	private void updateAllViews() {
		// TODO Auto-generated method stub
		String imageUrl = UserPreferencesUtil.getHeadPic(this);
		String userNameStr = UserPreferencesUtil.getUserNickName(this);
		String userBorthDay = UserPreferencesUtil.getBirthday(this);
		String sexStr = UserPreferencesUtil.getSex(this);
		String typeStr = UserPreferencesUtil.getUserType(this);
		if (typeStr != null && !"null".equals(typeStr) && !"".equals(typeStr)) {
			if (Constants.CARRE_MODEL.equals(typeStr) || Constants.PRO_MODEL.equals(typeStr)) {
				switchType(0);
			} else if (Constants.CARRE_PHOTOR.equals(typeStr)) {
				switchType(1);
			}
		} else {
			switchType(0);
			modelBack.setOnClickListener(this);
			photorBack.setOnClickListener(this);
		}
		if (userBorthDay != null && !"null".equals(userBorthDay) && !"".equals(userBorthDay)) {
			birthdayView.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(userBorthDay)));
		} else {
			birthdayView.setOnClickListener(this);
		}
		if (sexStr != null && !"null".equals(sexStr) && !"".equals(sexStr)) {
			if (Constants.SEX_WOMAN.equals(sexStr)) {
				switchMale(1);
			} else if (Constants.SEX_MAN.equals(sexStr)) {
				switchMale(0);
			}
		} else {
			manViewBack.setOnClickListener(this);
			womanViewBack.setOnClickListener(this);
			switchMale(1);
		}
		if (userNameStr != null && !"null".equals(userNameStr) && !"".equals(userNameStr)) {
			nickName.setText(userNameStr);
			nickName.setEnabled(false);
			nickName.setKeyListener(null);
			nickName.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
						int dend) {
					// TODO Auto-generated method stub
					return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
				}
			} });
		}
		if (imageUrl != null && !"null".equals(imageUrl) && !"".equals(imageUrl)) {
			photoPath = imageUrl;
			imageLoader.loadImage(imageUrl, userImage, true);
		} else {
			userImage.setOnClickListener(this);
			userImage.setImageResource(R.drawable.sg_login_userpic_icon);
		}
	}

	/**
	 * 重载方法
	 *
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.closeLoginWel) {
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.birthdayView) {
			startActivityForResult(new Intent(this, ChoiceDateView.class), 2);
		} else if (v.getId() == R.id.manViewBack) {
			switchMale(0);
		} else if (v.getId() == R.id.womanViewBack) {
			switchMale(1);
		} else if (v.getId() == R.id.modelBack) {
			switchType(0);
		} else if (v.getId() == R.id.photorBack) {
			switchType(1);
		} else if (v.getId() == R.id.userImage) {
			Intent intent = new Intent(this, PickPhotoesView.class);
			startActivityForResult(intent, 5);
		} else if (v.getId() == R.id.comfirm) {
			if (!"".equals(photoPath)) {
				if (photoPath.contains("http://")) {
					commit();
				} else {
					uploadImages();
				}
			} else {

				Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
			}
		}
	}

//	private void jiaMiDatas() {
//		new Thread(new Runnable() {
//			public void run() {
//				loadData();
//				handler.sendEmptyMessage(1);
//			}
//
//			public void loadData() {
//				try {
//					File imageFile = new File(photoPath);
//					dataToUpload = fileToByte(imageFile);
//					dataToUpload=ImageKeyHepler.setEncrypt(DoUserInfoActivity.this, dataToUpload);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}

	private static byte[] fileToByte(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (null != msg) {
				switch (msg.what) {
				case 1:
					Toast.makeText(getApplicationContext(), "加密完成", Toast.LENGTH_SHORT).show();
					uploadImages();
					break;
				default:
					break;
				}
			}
		}
	};

	private void uploadImages() {
		ShowMsgDialog.show(this, "头像上传中...", true);
		AliySSOHepler.getInstance().uploadImageEngine(this, Constants.AVATAR_PATH, photoPath, new HttpCallBack() {

			@Override
			public void onSuccess(String imageUrl, int requestCode) {
				ShowMsgDialog.cancel();
				photoPath = imageUrl;
				commit();
			}

			@Override
			public void onFailure(int errorCode, String msg, int requestCode) {
				// TODO Auto-generated method stub
				ShowMsgDialog.cancel();
				Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProgress(String objectKey, int byteCount, int totalSize) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void commit() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.login.JiHuoUser(${activateUser})", this, null, visit).run();
	}

	/**
	 * 重载方法
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void takePhoto() {
		// 跳转至拍照界面
		photoName = Constants.getImageName(this);
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		File out = new File(FileUtil.getPhotopath(photoName));
		imageUri = Uri.fromFile(out);
		// 获取拍照后未压缩的原图片，并保存在uri路径中
		intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intentPhote, TAKE_PICTURE);
	}

	private void chicePhoto() {
		LinkedList<String> picDatas = new LinkedList<String>();
		TransferImagesBean dataBean = new TransferImagesBean();
		dataBean.setSelectImagePah(picDatas);
		Intent intent = new Intent(this, ImageChooserActivity.class);
		intent.putExtra(Constants.TRANS_PIC_NUM, 1);
		intent.putExtra("images", dataBean);
		startActivityForResult(intent, CHOOSE_PICTURE);
	}

	private void switchMale(int index) {
		switch (index) {
		case 0:
			manViewBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
			manIcon.setImageResource(R.drawable.sg_man_light_icon);
			manText.setTextColor(getResources().getColor(R.color.white));

			womanViewBack.setBackgroundResource(0);// R.drawable.shape_rect_input_white);
			womanIcon.setImageResource(R.drawable.sg_woman_dark_icon);
			womanText.setTextColor(getResources().getColor(R.color.dark_green));

			sexStr = Constants.SEX_MAN;
			break;
		case 1:
			manViewBack.setBackgroundResource(0);// R.drawable.shape_rect_input_white);
			manIcon.setImageResource(R.drawable.sg_man_dark_icon);
			manText.setTextColor(getResources().getColor(R.color.dark_green));

			womanViewBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
			womanIcon.setImageResource(R.drawable.sg_woman_light_icon);
			womanText.setTextColor(getResources().getColor(R.color.white));
			sexStr = Constants.SEX_WOMAN;
			break;
		default:
			break;
		}
	}

	private void switchType(int index) {
		switch (index) {
		case 0:
			modelBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
			iamModel.setTextColor(getResources().getColor(R.color.white));

			photorBack.setBackgroundResource(R.drawable.shape_rect_input_white);
			iamPhotor.setTextColor(getResources().getColor(R.color.dark_green));
			carrieType = Constants.CARRE_MODEL;
			break;
		case 1:
			modelBack.setBackgroundResource(R.drawable.shape_rect_input_white);
			iamModel.setTextColor(getResources().getColor(R.color.dark_green));

			photorBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
			iamPhotor.setTextColor(getResources().getColor(R.color.white));
			carrieType = Constants.CARRE_PHOTOR;
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			try {
				Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
				if (bitmap != null) {
					FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
					File out = new File(FileUtil.getPhotopath(photoName));
					imageUri = Uri.fromFile(out);
					cropImageUri(imageUri, w, h, CROP_PICTURE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			break;
		case CROP_PICTURE:
			try {
				Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
				if (bitmap != null) {
					FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
					userImage.setImageBitmap(bitmap);
					photoPath = FileUtil.getPhotopath(photoName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			break;
		case CHOOSE_PICTURE:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}

			Object result = data.getSerializableExtra(Constants.COMEBACK);
			if (result != null) {
				TransferImagesBean dataBean = (TransferImagesBean) result;
				LinkedList<String> tempStrs = dataBean.getSelectImagePath();
				for (int i = 0; i < tempStrs.size(); i++) {
					Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
					if (subBitmap != null) {
						photoName = Constants.getImageName(this);
						FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
						File out = new File(FileUtil.getPhotopath(photoName));
						originalUri = Uri.fromFile(out);
					}
				}
				cropPickImageUri(originalUri, w, h, 4);
			}
			break;
		case 4:
			try {
				Bitmap photo = BitmapFactory.decodeFile(FileUtil.getPhotopath(photoName));
				if (photo != null) {
					int angle = FileUtil.getOrientation(this, originalUri);
					if (angle > 0) {
						photo = FileUtil.rotaingImageView(angle, photo);
					}
					userImage.setImageBitmap(photo);
					photoPath = FileUtil.getPhotopath(photoName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			Serializable item2 = data.getSerializableExtra("bean");
			if (item2 != null) {
				FeedbackDateBean ftBean = (FeedbackDateBean) item2;
				String szBirthday = ftBean.getYear() + "-" + ftBean.getMonth() + "-" + ftBean.getDay();
				birthdayView.setText(szBirthday);
			}
			break;
		case 5:
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
		default:
			break;
		}
	}

	private void cropPickImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 4);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 4);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public String getSexType() {
		return sexStr;
	}

	public String getCarrieType() {
		return carrieType;
	}

	public void closePage(boolean b) {
		if(b){
			XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
		}else{
			ShowMsgDialog.cancel();
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, b);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	@Override
	protected void xmppRespose() {
		super.xmppRespose();
		ShowMsgDialog.cancel();
		UserPreferencesUtil.setIsOnline(this, true);
		Toast.makeText(this, "激活成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
		intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void dealWithResult(Integer data) {
		switch (data) {
			case XMPPUtils.LOGIN_SECCESS: // 登录成功
				UserPreferencesUtil.setIsOnline(this, true);
				Toast.makeText(this, "激活成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
				break;
			case XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
				Toast.makeText(this, "账户或者密码错误", Toast.LENGTH_SHORT).show();
				break;
			case XMPPUtils.SERVER_UNAVAILABLE:// 服务器连接失败
				Toast.makeText(this, "服务器连接失败", Toast.LENGTH_SHORT).show();
				break;
			case XMPPUtils.LOGIN_ERROR:// 未知异常
				Toast.makeText(this, "未知异常", Toast.LENGTH_SHORT).show();
				break;
		}
	}
}

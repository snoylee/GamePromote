/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.personal;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.EditorTextContentActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.bean.comm.TransZuoPingBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.photogrid.PhotoAdapter;
import com.xygame.sg.define.photogrid.PhotoEditorAdapter;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.RecycleBitmap;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
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
public class PersonalPhotoesActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName, rightButtonText, userName, picNumCount, priseNumCount, heartText;
	private View backButton, rightButton, uploadImageButton, modifyHeartView, deletePhotosButton, bottomDeleteView,
			arrowsIcon;
	private List<PhotoesTotalBean> browersDatas, editorDatas;
	private List<PhotoesSubBean> deleDatas;
	private CircularImage userImage;
	private PhotoAdapter browersAdapter;
	private PhotoEditorAdapter editorAdapter;
	private ListView photoList;
	private boolean isEditor = false;
	private ImageLoader imageLoader;
	private int i_index, j_index;
	private String photoName;
	private PhotoesTotalBean newItem;
	private int currIndex = 0, currCount;
	private String glaryId, tipText;
	private String editorText;
	private boolean isRefresh=false;
	private TransZuoPingBean tsBean;
	public List<PhotoesSubBean> getDeleDatas() {
		return deleDatas;
	}

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
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_personal_photoes_layout, null));

		initViews();
		initListeners();
		initDatas();

		com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearMemoryCache();
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearDiscCache();
		System.gc();
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
		uploadImageButton.setOnClickListener(this);
		modifyHeartView.setOnClickListener(this);
		deletePhotosButton.setOnClickListener(this);
		arrowsIcon.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		userName = (TextView) findViewById(R.id.userName);
		picNumCount = (TextView) findViewById(R.id.picNumCount);
		priseNumCount = (TextView) findViewById(R.id.priseNumCount);
		heartText = (TextView) findViewById(R.id.heartText);

		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		uploadImageButton = findViewById(R.id.uploadImageButton);
		modifyHeartView = findViewById(R.id.modifyHeartView);
		arrowsIcon = findViewById(R.id.arrowsIcon);

		userImage = (CircularImage) findViewById(R.id.userImage);
		photoList = (ListView) findViewById(R.id.photoList);
		deletePhotosButton = findViewById(R.id.deletePhotosButton);
		bottomDeleteView = findViewById(R.id.bottomDeleteView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		titleName.setText(getResources().getString(R.string.sg_personal_photo_title));
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getResources().getString(R.string.sg_personal_photo_deletip));
		if(UserPreferencesUtil.getHeadPic(this)!=null){
			imageLoader.loadImage(UserPreferencesUtil.getHeadPic(this), userImage, false);
		}else{
			
			userImage.setImageResource(R.drawable.default_avatar);
		}
		userName.setText(UserPreferencesUtil.getUserNickName(this));
		uploadImageButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
		picNumCount.setText(getIntent().getStringExtra("imageCount"));
		priseNumCount.setText(getIntent().getStringExtra("priseCount"));
		deleDatas = new ArrayList<PhotoesSubBean>();
		tipText = getIntent().getStringExtra("oral");
		glaryId = getIntent().getStringExtra("id");
		if (!"null".equals(tipText)&&tipText != null) {
			heartText.setText(tipText);
		} else {
			heartText.setHint("点此可添加相册描述啊！");
//			heartText.setText(R.string.sg_personal_photo_addtip);
		}
		arrowsIcon.setVisibility(View.VISIBLE);
		loadZuoPingDatas();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void loadZuoPingDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.LoadZuoPingTask(${queryModelGalleryDetail})", this, null, visit).run();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void parseDatasRefresh(Map pMap) {
		if(pMap!=null){
			List<Map> mapList = (List<Map>) pMap.get("datePics");
			if (mapList != null && !mapList.isEmpty()) {
				browersDatas = new ArrayList<PhotoesTotalBean>();
				editorDatas = new ArrayList<PhotoesTotalBean>();

				for (Map firstMap : mapList) {
					PhotoesTotalBean ptBean = new PhotoesTotalBean();
					ptBean.setDateTimer(firstMap.get("date").toString());
					List<Map> imageList = (List<Map>) firstMap.get("pics");
					List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
					for (Map secondMap : imageList) {
						PhotoesSubBean psBean = new PhotoesSubBean();
						psBean.setImageId(secondMap.get("resId").toString());
						psBean.setImageUrls(secondMap.get("resUrl").toString());
						psBean.setIsCover(secondMap.get("isCover").toString());
						psBean.setIsSelect(false);
						imageObjects.add(psBean);
					}
					ptBean.setImageObjects(imageObjects);
					ptBean.setIsSelect(false);
					browersDatas.add(ptBean);
					editorDatas.add(ptBean);
				}
				editorAdapter = new PhotoEditorAdapter(this, editorDatas);
				browersAdapter = new PhotoAdapter(this, browersDatas,glaryId);
				photoList.setAdapter(browersAdapter);
			} else {
				if (browersDatas == null) {
					browersDatas = new ArrayList<PhotoesTotalBean>();
				}
				if (editorDatas == null) {
					editorDatas = new ArrayList<PhotoesTotalBean>();
				}

				editorAdapter = new PhotoEditorAdapter(this, null);
				browersAdapter = new PhotoAdapter(this, null,glaryId);
				photoList.setAdapter(browersAdapter);
			}
		}else{
			if (browersDatas == null) {
				browersDatas = new ArrayList<PhotoesTotalBean>();
			}
			if (editorDatas == null) {
				editorDatas = new ArrayList<PhotoesTotalBean>();
			}

			editorAdapter = new PhotoEditorAdapter(this, null);
			browersAdapter = new PhotoAdapter(this, null,glaryId);
			photoList.setAdapter(browersAdapter);
		}
	}

	/**
	 * 重载方法
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
			sendBroadcast(intent);
			finish();
		} else if (v.getId() == R.id.uploadImageButton) {
			if (AliPreferencesUtil.getBuckekName(this)==null){
				new Action(ResponseAliParams.class,"${ali_params}", this, null, new VisitUnit()).run();
				Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
						.show();
			}else{
				Intent intent = new Intent(this, PickPhotoesView.class);
				startActivityForResult(intent, 1);
			}
		} else if (v.getId() == R.id.modifyHeartView) {
			if (!isEditor) {
				Intent intent = new Intent(this, EditorTextContentActivity.class);
				intent.putExtra(Constants.EDITOR_TEXT_TITLE, getResources().getString(R.string.sg_eidtor_heart_tite));
				intent.putExtra("oral", tipText);
				intent.putExtra(Constants.TEXT_EDITOR_NUM, 200);
				startActivityForResult(intent, 3);
			}
		} else if (v.getId() == R.id.rightButton) {

			if (isEditor) {
				arrowsIcon.setVisibility(View.VISIBLE);
				updateBrowersPhotoes();
			} else {
				if (editorDatas.size() > 0) {
					updateEditorPhotoes();
					arrowsIcon.setVisibility(View.GONE);
				} else {
					Toast.makeText(getApplicationContext(), "请先添加图片", Toast.LENGTH_SHORT).show();
				}
			}
			isEditor = !isEditor;
		} else if (v.getId() == R.id.deletePhotosButton) {
			deleteEditorPhotoes();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
			sendBroadcast(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void updateBrowersPhotoes() {
		rightButtonText.setText(getResources().getString(R.string.sg_personal_photo_deletip));
		List<PhotoesTotalBean> editorDatas = editorAdapter.getDatas();
		bottomDeleteView.setVisibility(View.GONE);
		uploadImageButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
		photoList.setAdapter(browersAdapter);
		browersAdapter.deletePhotoes(editorDatas);
		browersAdapter.notifyDataSetChanged();
		int photoesCount=0;
		for(PhotoesTotalBean it:editorDatas){
			photoesCount=photoesCount+it.getImageObjects().size();
		}
		picNumCount.setText(String.valueOf(photoesCount));
	}

	public void updateEditorPhotoes() {
		rightButtonText.setText(getResources().getString(R.string.sg_personal_photo_cancel));
		List<PhotoesTotalBean> editorDatas = browersAdapter.getDatas();
		bottomDeleteView.setVisibility(View.VISIBLE);
		uploadImageButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
		photoList.setAdapter(editorAdapter);
		editorAdapter.deletePhotoes(editorDatas);
		editorAdapter.notifyDataSetChanged();
	}

	private void chicePhoto() {
		LinkedList<String> picDatas = new LinkedList<String>();
		TransferImagesBean dataBean = new TransferImagesBean();
		dataBean.setSelectImagePah(picDatas);
		Intent intent = new Intent(this, ImageChooserActivity.class);
		intent.putExtra(Constants.TRANS_PIC_NUM, Constants.MODEL_PIC_NUM);
		intent.putExtra("images", dataBean);
		startActivityForResult(intent, 0);
	}

	private void takePhoto() {
		// 跳转至拍照界面
		photoName =  Constants.getImageName(this);
		Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		File out = new File(FileUtil.getPhotopath(photoName));
		Uri uri = Uri.fromFile(out);
		// 获取拍照后未压缩的原图片，并保存在uri路径中
		intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intentPhote, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			Object result = data.getSerializableExtra(Constants.COMEBACK);
			if (result != null) {
				TransferImagesBean dataBean = (TransferImagesBean) result;
				currIndex = 0;
				currCount = browersAdapter.getCount();
				LinkedList<String> tempStrs = dataBean.getSelectImagePath();
				String dateTimer = CalendarUtils.getCurrDateStr();
				newItem = new PhotoesTotalBean();
				newItem.setDateTimer(dateTimer);
				List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
				for (int i = 0; i < tempStrs.size(); i++) {
					Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
					if (subBitmap != null) {
						String photoName =  Constants.getImageName(this);
						FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
						subBitmap.recycle();
						String photoPath = FileUtil.getPhotopath(photoName);
						PhotoesSubBean subBean = new PhotoesSubBean();
						subBean.setDateTimer(dateTimer);
						subBean.setImageUrls(photoPath);
						subBean.setIsSelect(false);
						currCount = currCount + 1;
						subBean.setItemIndex(String.valueOf(currCount));
						imageObjects.add(subBean);
					}
				}
				newItem.setImageObjects(imageObjects);
				newItem.setIsSelect(false);
				ShowMsgDialog.show(this, "图片上传中...", false);
				doUploadImages();
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
					FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
					String photoPath = FileUtil.getPhotopath(photoName);
					currIndex = 0;
					currCount = browersAdapter.getCount();
					String dateTimer = CalendarUtils.getCurrDateStr();
					newItem = new PhotoesTotalBean();
					newItem.setDateTimer(dateTimer);
					List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
					PhotoesSubBean subBean = new PhotoesSubBean();
					subBean.setDateTimer(dateTimer);
					subBean.setImageUrls(photoPath);
					subBean.setIsSelect(false);
					subBean.setItemIndex(String.valueOf(currCount + 1));
					imageObjects.add(subBean);
					newItem.setImageObjects(imageObjects);
					newItem.setIsSelect(false);
					ShowMsgDialog.show(this, "图片上传中...", false);
					doUploadImages();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			break;

		case 3:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			editorText = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
			commitEditorText();
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void commitEditorText() {
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.UploadPhotoOralTask(${editModelGallery})", this, null, visit).run();
	}

	public String getEditorText() {
		return editorText;
	}

	public void refreshEditorText() {
		heartText.setText(editorText);
	}

	private void doUploadImages() {
		if (currIndex < newItem.getImageObjects().size()) {

			AliySSOHepler.getInstance().uploadImageEngine(this,Constants.PHOTOBUM_PATH,
					newItem.getImageObjects().get(currIndex).getImageUrls(), new HttpCallBack() {

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
			transferLocationService();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (null != msg) {
				switch (msg.what) {
				case 1:
					isRefresh=true;
					String imageUrl = (String) msg.obj;
					newItem.getImageObjects().get(currIndex).setUrl(imageUrl);
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

	public List<PhotoesSubBean> getNewImages() {
		for(int i=0;i<newItem.getImageObjects().size();i++){
			Bitmap bitmap=ImageTools.getBitmap(newItem.getImageObjects().get(i).getImageUrls());
			newItem.getImageObjects().get(i).setLengthPx(String.valueOf(bitmap.getHeight()));
			newItem.getImageObjects().get(i).setPicSize(FileUtil.getImageByteSize(newItem.getImageObjects().get(i).getImageUrls()));
			newItem.getImageObjects().get(i).setWidthPx(String.valueOf(bitmap.getWidth()));
			bitmap.recycle();
		}
		return newItem.getImageObjects();
	}

	public String getGlaryId() {
		return glaryId;
	}
	
	public void finishUpload(List<Map> map) {
		for(Map sMap:map){
			for(int i=0;i<newItem.getImageObjects().size();i++){
				String uucode=sMap.get("uucode").toString();
				String localUucode=newItem.getImageObjects().get(i).getItemIndex();
				if(uucode.equals(localUucode)){
					newItem.getImageObjects().get(i).setItemIndex(null);
					newItem.getImageObjects().get(i).setImageId(sMap.get("resId").toString());
				}
			}
		}
		updateChoiceImages();
	}

	private void transferLocationService() {
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.UploadZuoPingTask(${cudModelGalleryPic})", this, null, visit).run();
	}

	private void updateChoiceImages() {
		browersAdapter.addItem(newItem);
		List<PhotoesTotalBean> editorDatas = browersAdapter.getDatas();
		int photoesCount=0;
		for(PhotoesTotalBean it:editorDatas){
			photoesCount=photoesCount+it.getImageObjects().size();
		}
		picNumCount.setText(String.valueOf(photoesCount));
	}

	private void deleteEditorPhotoes() {
		// TODO Auto-generated method stub
		deleDatas.clear();
		final List<PhotoesTotalBean> editorDatas = editorAdapter.getDatas();
		for (i_index = 0; i_index < editorDatas.size(); i_index++) {
			if (editorDatas.get(i_index).getIsSelect()) {
				PhotoesTotalBean deleIt = editorDatas.get(i_index);
				List<PhotoesSubBean> imObj = deleIt.getImageObjects();
				for (PhotoesSubBean psb : imObj) {
					deleDatas.add(psb);
				}
			} else {
				for (j_index = 0; j_index < editorDatas.get(i_index).getImageObjects().size(); j_index++) {
					if (editorDatas.get(i_index).getImageObjects().get(j_index).getIsSelect()) {
						deleDatas.add(editorDatas.get(i_index).getImageObjects().get(j_index));
					}
				}
			}
		}
		transferLocationService2();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void transferLocationService2() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.DeleZuoPingTask(${cudModelGalleryPic})", this, null, visit).run();
	}

	public void finishDelete() {
		isRefresh=true;
		final List<PhotoesTotalBean> editorDatas = editorAdapter.getDatas();
		for (i_index = 0; i_index < editorDatas.size(); i_index++) {
			if (editorDatas.get(i_index).getIsSelect()) {
				editorAdapter.delete(i_index);
			} else {
				for (j_index = 0; j_index < editorDatas.get(i_index).getImageObjects().size(); j_index++) {
					if (editorDatas.get(i_index).getImageObjects().get(j_index).getIsSelect()) {
						editorAdapter.deleteItems(i_index, j_index);
					}
				}
			}
		}
		editorAdapter.notifyDataSetChanged();
		int photoesCount=0;
		for(PhotoesTotalBean it:editorDatas){
			photoesCount=photoesCount+it.getImageObjects().size();
		}
		picNumCount.setText(String.valueOf(photoesCount));
	}

	@Override
	public void onDestroy() {

//		int[] ids = new int[]{R.id.id_img};
//		RecycleBitmap.recycleAbsList(photoList, ids);
		System.gc();
		super.onDestroy();
	}

	/**
	 * 重载方法
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if (browersAdapter.getCount()==0){
			isRefresh=true;
		}
		Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
		intent.putExtra("flagStr", isRefresh);
		sendBroadcast(intent);
		super.finish();
	}
}

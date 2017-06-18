package com.xygame.sg.activity.image;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.adapter.comm.PictruesDirAdapter;
import com.xygame.sg.bean.comm.ImageFloder;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.utils.Constants;

public class ImageChooserActivity extends Activity implements
		OnClickListener, OnItemClickListener {

	private TextView showNum;
	private View rightButton, backButton;
	private ListView photoDirList;
	private TransferImagesBean dataBean;
	private int maxCount;
	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	public static List<String> mSelectedImage;
	public LinkedList<String> selectImagePah;
	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	private PictruesDirAdapter adapter;

	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.le_image_chooser_layout);
		initViews();
		initListeners();
		initDatas();
	}

	private void initViews() {
		rightButton = findViewById(R.id.rightButton);
		backButton = findViewById(R.id.backButton);
		showNum = (TextView) findViewById(R.id.showNum);
		photoDirList = (ListView) findViewById(R.id.photoDirList);
	}

	private void initListeners() {
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		photoDirList.setOnItemClickListener(this);
	}

	private void initDatas() {
		dataBean= (TransferImagesBean)getIntent().getSerializableExtra("images");
		maxCount = getIntent().getIntExtra(Constants.TRANS_PIC_NUM, 0);
		selectImagePah =dataBean.getSelectImagePath();
		showNum.setText("(".concat(String.valueOf(selectImagePah.size()))
				.concat("/" + maxCount + ")"));
		getImages();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.rightButton) {
			if (selectImagePah.size()>0){
				TransferImagesBean dataBean = new TransferImagesBean();
				dataBean.setSelectImagePah(selectImagePah);
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, dataBean);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}else{
				Toast.makeText(this,"至少选择一张图片",Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}
					
					String[] fileItems=parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							return filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg");
						}
					});

					int picSize = 0;
					
					if(fileItems!=null){
						 picSize = fileItems.length;	
					}

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize) {
						mPicsSize = picSize;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;
				//
				// // 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0);

			}
		}).start();

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				adapter = new PictruesDirAdapter(mImageFloders,
						ImageChooserActivity.this);
				photoDirList.setAdapter(adapter);
				adapter.addDatas(mImageFloders);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ImageFloder item = adapter.getItem(arg2);
		String chocedDir = item.getDir();
		String dirName = item.getName().replace("/", "");
		TransferImagesBean dataBean = new TransferImagesBean();
		dataBean.setSelectImagePah(selectImagePah);
		Intent intent = new Intent(this, ImageChooserShowActivity.class);
		intent.putExtra("dir", chocedDir);
		intent.putExtra("dirName", dirName);
		intent.putExtra("images", dataBean);
		intent.putExtra(Constants.TRANS_PIC_NUM, maxCount);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			Object obj = data.getSerializableExtra(Constants.COMEBACK);
			if (obj != null) {
				TransferImagesBean dataBean1 = (TransferImagesBean) obj;
				selectImagePah.clear();
				selectImagePah.addAll(dataBean1.getSelectImagePath());
				if (selectImagePah.size()>0){
					TransferImagesBean dataBean = new TransferImagesBean();
					dataBean.setSelectImagePah(selectImagePah);
					Intent intent = new Intent();
					intent.putExtra(Constants.COMEBACK, dataBean);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}else{
					Toast.makeText(this,"至少选择一张图片",Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
		default:
			break;
		}
	}
}

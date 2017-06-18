package com.xygame.sg.activity.image;

import java.io.File;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tendcloud.tenddata.TCAgent;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ImageOptionDialog;
import com.xygame.sg.activity.commen.ImageOptionListener;
import com.xygame.sg.activity.image.PhotoViewAttacher.OnPhotoTapListener;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.HttpDownloadUtil;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;
import com.xygame.sg.utils.RecycleBitmap;

public class ImageDetailFragment extends Fragment{
	private String mImageUrl;
	private ProgressBar progressBar;
	private PhotoView mAttacher;
	private ImagePagerListener listener;
	private ImageLoader mImageLoader;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}
	
	public void addImageListener(ImagePagerListener listener){
		this.listener=listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.le_image_detail_fragment, container, false);
		mAttacher = (PhotoView) v.findViewById(R.id.image);
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				listener.onTopClickListener(true);
			}
		});
		mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showOptionDialog();
				return true;
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		progressBar.setVisibility(View.GONE);
		return v;
	}

	private void showOptionDialog() {
		ImageOptionDialog dialog = new ImageOptionDialog(getActivity(), R.style.dineDialog,
				new ImageOptionListener() {

					@Override
					public void saveImageListener(Dialog dialog) {
						saveImageAct();
					}
				});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void saveImageAct() {
		String dirPath= ConstTaskTag.getSDCardPath();
		if (!TextUtils.isEmpty(dirPath)){
			if (mImageUrl.contains("http://")){
				String[] pathArry=mImageUrl.split("/");
				String photoName=pathArry[pathArry.length-1];
				ThreadPool.getInstance().excuseThread(new SavaPhotoAct(mImageUrl,dirPath,photoName));
			}
		}else{
			Toast.makeText(getActivity(),"请插入SD卡",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		File file = new File(mImageUrl);
		if (file.exists()) {
			ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(mImageUrl, mAttacher);
		}else{
			mImageLoader.loadImageNoThume(mImageUrl, mAttacher, false);
		}
	}

	private class SavaPhotoAct implements Runnable{
		private String mImageUrl;
		private String dirPath;
		private String photoName;
		public SavaPhotoAct(String mImageUrl,String dirPath,String photoName){
			this.mImageUrl=mImageUrl;
			this.dirPath=dirPath;
			this.photoName=photoName;
		}

		@Override
		public void run() {
			HttpDownloadUtil.getInstance().downFile(mImageUrl,dirPath,photoName);
		}
	}
}

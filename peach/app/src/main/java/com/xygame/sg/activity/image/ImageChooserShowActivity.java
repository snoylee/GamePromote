package com.xygame.sg.activity.image;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.ChoosedPhotoes;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;

public class ImageChooserShowActivity extends Activity implements
		OnClickListener {

	private TextView showNum,titleName;
	private View rightButton, backButton;
	private GridView mGirdView;
	private String chocedDir;
	private List<ChoosedPhotoes> datas;
	private PickImageAdapter mAdapter;
	public LinkedList<String> selectImagePah;
	private int maxCount;

	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	private List<String> mImgs;
//	public static List<String> mSelectedImage;

	private TransferImagesBean dataBean;
	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.le_image_chooser_show_layout);
		initViews();
		initListeners();
		initDatas();
	}
	
	private void initViews() {
		rightButton = findViewById(R.id.rightButton);
		backButton = findViewById(R.id.backButton);
		showNum = (TextView) findViewById(R.id.showNum);
		titleName=(TextView)findViewById(R.id.titleName);
		mGirdView = (GridView) findViewById(R.id.id_gridView);
	}

	private void initListeners() {
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}

	private void initDatas() {
		dataBean= (TransferImagesBean)getIntent().getSerializableExtra("images");
		selectImagePah=dataBean.getSelectImagePath();
		chocedDir = getIntent().getStringExtra("dir");
		maxCount = getIntent().getIntExtra(Constants.TRANS_PIC_NUM, 0);
		showNum.setText("(".concat(String.valueOf(selectImagePah.size()))
				.concat("/" + maxCount + ")"));
		mImgDir = new File(chocedDir);
		titleName.setText(getIntent().getStringExtra("dirName"));
		initImagesDatas();
	}

	private void initImagesDatas() {
		datas = new ArrayList<ChoosedPhotoes>();
		if (mImgDir == null) {
			Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			if(mImgDir.list()==null){
				Toast.makeText(getApplicationContext(), "擦，原来空文件夹",
						Toast.LENGTH_SHORT).show();
				return;
			}else{
				mImgs = Arrays.asList(mImgDir.list());
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for (String iIt : mImgs) {
							ChoosedPhotoes it = new ChoosedPhotoes();
							it.setSelect(false);
							it.setImageName(iIt);
							datas.add(it);
						}
						mHandler.sendEmptyMessage(1);
					}
				}).start();
			}

		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				data2View();
				break;
			default:
				break;
			}
		}
	};

	private void data2View() {
		mAdapter = new PickImageAdapter(this, datas, mImgDir.getAbsolutePath()
				.concat("/"));
		mGirdView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.rightButton) {
			if (selectImagePah.size()>0){
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

	private class PickImageAdapter extends BaseAdapter {
		protected LayoutInflater mInflater;
		protected Context mContext;
		protected List<ChoosedPhotoes> mDatas;
		private String dirPath;

		/**
		 * 用户选择的图片，存储为图片的完整路径
		 */

		public PickImageAdapter(Context applicationContext,
				List<ChoosedPhotoes> mSelectedImage, String dirPath) {
			this.dirPath = dirPath;
			this.mContext = applicationContext;
			this.mInflater = LayoutInflater.from(mContext);
			if (mSelectedImage == null) {
				this.mDatas = new ArrayList<ChoosedPhotoes>();
			} else {
				this.mDatas = mSelectedImage;
			}
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public ChoosedPhotoes getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			final HoldView holdView;
			if (view == null) {
				holdView = new HoldView();
				view = mInflater.inflate(R.layout.grid_item, null);
				holdView.selectedButton = (ImageView) view
						.findViewById(R.id.selectedButton);
				holdView.selectButton = (ImageView) view
						.findViewById(R.id.selectButton);
				holdView.id_item_image = (ImageView) view
						.findViewById(R.id.id_item_image);
				view.setTag(holdView);
			} else {
				holdView = (HoldView) view.getTag();
			}

			final ChoosedPhotoes item = mDatas.get(position);
			ImageLocalLoader.getInstance(3, Type.LIFO)
					.loadImage(dirPath.concat(item.getImageName()),
							holdView.id_item_image);

			holdView.id_item_image.setColorFilter(null);
			// 设置ImageView的点击事件
			holdView.id_item_image.setOnClickListener(new OnClickListener() {
				// 选择，则将图片变暗，反之则反之
				@Override
				public void onClick(View v) {
					if (selectImagePah.size() <= maxCount) {
						// 已经选择过该图片
						if (selectImagePah.contains(dirPath.concat(item
								.getImageName()))) {
							selectImagePah.remove(dirPath.concat(item
									.getImageName()));
							item.setSelect(false);
							holdView.selectButton.setVisibility(View.VISIBLE);
							holdView.selectedButton.setVisibility(View.GONE);
							notifyDataSetChanged();
						} else
						// 未选择该图片
						{
							if (selectImagePah.size() < maxCount) {
								selectImagePah.add(dirPath.concat(item
										.getImageName()));
								item.setSelect(true);
								holdView.selectButton.setVisibility(View.GONE);
								holdView.selectedButton.setVisibility(View.VISIBLE);
								notifyDataSetChanged();
							}else{
								Toast.makeText(mContext, "最多选择"+ maxCount +"张图片！",
										Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						Toast.makeText(mContext, "最多选择" + maxCount + "张图片！",
								Toast.LENGTH_SHORT).show();
					}

					showNum.setText("(".concat(
							String.valueOf(selectImagePah.size()))
							.concat("/" + maxCount + ")"));
				}
			});

			/**
			 * 已经选择过的图片，显示出选择过的效果
			 */
			if (selectImagePah.contains(dirPath.concat(item.getImageName()))) {
				holdView.selectButton.setVisibility(View.GONE);
				holdView.selectedButton.setVisibility(View.VISIBLE);
			} else {
				holdView.selectButton.setVisibility(View.VISIBLE);
				holdView.selectedButton.setVisibility(View.GONE);
			}
			return view;
		}

		class HoldView {
			ImageView id_item_image, selectButton;
			ImageView selectedButton;
		}

	}
}

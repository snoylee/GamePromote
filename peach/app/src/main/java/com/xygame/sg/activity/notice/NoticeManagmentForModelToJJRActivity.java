/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.JJRPublisher;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.SelectableRoundedImageView;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class NoticeManagmentForModelToJJRActivity extends SGBaseActivity implements OnClickListener,PullToRefreshBase.OnRefreshListener2 {

	private TextView titleName, daiSView, closeView;
	private View backButton,rightButton;
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
	private int pageSize = 10;//每页显示的数量
	private int pageIndexDai = 1,pageIndexClose = 1;//当前显示页数
	private boolean isLoadingDai=true,isLoadingClose=true;
	private PullToRefreshListView2 daiSList, closeList;
	private int loadFlag = 0;// 通告状态：0：待审核；1：招募中；2：拍摄中；3：已完成；4：已关闭
	private NoticeAdapter daiSAdapter, closeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.notice_modeltojjr_managment_layout, null));
		InitImageView();
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
		daiSView.setOnClickListener(this);
		closeView.setOnClickListener(this);
		rightButton.setOnClickListener(this);

		daiSList.setOnRefreshListener(this);
		closeList.setOnRefreshListener(this);
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
		rightButton=findViewById(R.id.rightButton);
		daiSView = (TextView) findViewById(R.id.daiSView);
		closeView = (TextView) findViewById(R.id.closeView);
		mPager = (ViewPager) findViewById(R.id.mPager);
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
		daiSList = (PullToRefreshListView2) listViews.get(0).findViewById(R.id.noticeList);
		closeList = (PullToRefreshListView2) listViews.get(1).findViewById(R.id.noticeList);

		daiSList.setMode(PullToRefreshBase.Mode.BOTH);
		closeList.setMode(PullToRefreshBase.Mode.BOTH);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
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

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		titleName.setText("通告");
		daiSAdapter = new NoticeAdapter(this,null);
		closeAdapter = new NoticeAdapter(this,null);
		daiSList.setAdapter(daiSAdapter);
		closeList.setAdapter(closeAdapter);
		mPager.setCurrentItem(0);
		updateTitleText(0);
		loadDatas(pageIndexDai);
	}

	private void loadDatas(int pageindex){
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject object=new JSONObject();
			object.put("page",new JSONObject().put("pageIndex",pageindex).put("pageSize",pageSize));
			int qstatus=loadFlag+1;
			object.put("qstatus",qstatus);
			item.setData(object);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		item.setServiceURL(ConstTaskTag.QUEST_JJR_MODEL_MANAGE);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_JJR_MODEL_MANAGE);
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (loadFlag) {
			case 0:
				daiSList.onRefreshComplete();
				break;
			case 1:
				closeList.onRefreshComplete();
				break;
			default:
				break;
		}
		switch (data.getPosionSign()){
			case ConstTaskTag.RESPOSE_JJR_MODEL_MANAGE:
				if ("0000".equals(data.getCode())){
					parseRequestData(data);
				}else{
					Toast.makeText(NoticeManagmentForModelToJJRActivity.this,data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.daiSView) {
			mPager.setCurrentItem(0);
		} else if (v.getId() == R.id.closeView) {
			mPager.setCurrentItem(1);
		}else if (v.getId()==R.id.rightButton){
			Intent intent=new Intent(this, NoticeManagmentForModelActivity.class);
			startActivity(intent);
			finish();
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
				loadFlag = 0;
				if (daiSAdapter.getCount() == 0) {
					loadDatas(pageIndexDai);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				loadFlag = 1;
				if (closeAdapter.getCount() == 0) {
					loadDatas(pageIndexClose);
				}
				break;
			}
			currIndex = arg0;
			updateTitleText(arg0);
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

	private void updateTitleText(int index) {
		switch (index) {
		case 0:
			daiSView.setTextColor(getResources().getColor(R.color.dark_green));
			closeView.setTextColor(getResources().getColor(R.color.dark_gray));
			break;
		case 1:
			daiSView.setTextColor(getResources().getColor(R.color.dark_gray));
			closeView.setTextColor(getResources().getColor(R.color.dark_green));
			break;
		default:
			break;
		}
	}

	class NoticeAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context context;
		private List<JJRNoticeBean> datas;
		private DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer())
				.build();
		public NoticeAdapter(Context context, List<JJRNoticeBean> datas) {
			super();
			this.context = context;
			inflater = LayoutInflater.from(context);
			if (datas!=null ) {
				this.datas=datas;
			}else{
				this.datas=new ArrayList<>();
			}

		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public JJRNoticeBean getItem(int i) {
			return datas.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.jjr_notice_list_item, null);
				holder = new ViewHolder();
				holder.notice_list_item_root_ll=convertView.findViewById(R.id.notice_list_item_root_ll);
				holder.reportNum=convertView.findViewById(R.id.reportNum);
				holder.notice_top_bg_iv = (SelectableRoundedImageView) convertView.findViewById(R.id.notice_top_bg_iv);
				holder.jjrSubject = (TextView) convertView.findViewById(R.id.jjrSubject);
				holder.jjrContent = (TextView) convertView.findViewById(R.id.jjrContent);
				holder.jjrContent1 = (TextView) convertView.findViewById(R.id.jjrContent1);
				holder.cm_avatar_iv = (ImageView) convertView.findViewById(R.id.cm_avatar_iv);
				holder.cm_nick_name_tv = (TextView) convertView.findViewById(R.id.cm_nick_name_tv);
				holder.expand_arrow_ll = (LinearLayout) convertView.findViewById(R.id.expand_arrow_ll);
				holder.collapse_arrow_ll = (LinearLayout) convertView.findViewById(R.id.collapse_arrow_ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final JJRNoticeBean item=datas.get(i);
			holder.notice_top_bg_iv.setImageResource(R.drawable.notice_top_bg);
			holder.jjrSubject.setText(item.getSubject());
			holder.reportNum.setVisibility(View.GONE);
			holder.cm_nick_name_tv.setText(item.getPublisher().getUsernick());
			String userImage=item.getPublisher().getUserIcon();
			if (!StringUtils.isEmpty(userImage)) {
				com.nostra13.universalimageloader.core.ImageLoader.getInstance()
						.displayImage(userImage, holder.cm_avatar_iv,options);
			}else{
				holder.cm_avatar_iv.setImageResource(R.drawable.new_system_icon);
			}
			ExpandAndCollapseListener listener = new ExpandAndCollapseListener(i);
			holder.expand_arrow_ll.setOnClickListener(listener);
			holder.collapse_arrow_ll.setOnClickListener(listener);
			if(item.isExpand()){//要展开
				holder.jjrContent.setVisibility(View.GONE);
				holder.jjrContent1.setVisibility(View.VISIBLE);
				holder.jjrContent1.setText(item.getNoticeContent());
				holder.expand_arrow_ll.setVisibility(View.GONE);
				holder.collapse_arrow_ll.setVisibility(View.VISIBLE);
			} else {
				holder.jjrContent1.setVisibility(View.GONE);
				holder.jjrContent.setVisibility(View.VISIBLE);
				holder.jjrContent.setText(item.getNoticeContent());
				holder.jjrContent.setLines(3);
				holder.expand_arrow_ll.setVisibility(View.VISIBLE);
				holder.collapse_arrow_ll.setVisibility(View.GONE);
			}
			holder.cm_avatar_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, CMPersonInfoActivity.class);
					if (UserPreferencesUtil.isOnline(context)&&UserPreferencesUtil.getUserId(context).equals(item.getPublisher().getUserId()+"")){
						intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.EDIT_INFO_FLAG);
					} else {
						intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
						intent.putExtra("userId",item.getPublisher().getUserId()+"");
						intent.putExtra("userNick",item.getPublisher().getUsernick());
					}
					context.startActivity(intent);
				}
			});
			holder.notice_list_item_root_ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, JJRNoticeDetailActivity.class);
					intent.putExtra("noticeId",item.getNoticeId());
					context.startActivity(intent);
				}
			});
			return convertView;
		}

		public void addDatas(List<JJRNoticeBean> datas,int mCurrentPage) {
			if (mCurrentPage==1){
				this.datas=datas;
			}else {
				this.datas.addAll(datas);
			}
			notifyDataSetChanged();
		}


		class ExpandAndCollapseListener implements OnClickListener {
			private int i;
			public ExpandAndCollapseListener(int i) {
				this.i = i;
			}
			@Override
			public void onClick(View view) {
				if (view.getId() == R.id.expand_arrow_ll) {
					datas.get(i).setExpand(true);
				} else if (view.getId() == R.id.collapse_arrow_ll) {
					datas.get(i).setExpand(false);
				}
				notifyDataSetChanged();
			}
		}

		private class ViewHolder {
			private SelectableRoundedImageView notice_top_bg_iv;
			private TextView jjrSubject,jjrContent1,jjrContent;
			private ImageView cm_avatar_iv;
			private TextView cm_nick_name_tv;
			private LinearLayout expand_arrow_ll;
			private LinearLayout collapse_arrow_ll;
			private View notice_list_item_root_ll,reportNum;
		}
	}
	public void toNoticeStatus(JJRNoticeBean item) {

		Intent intent = new Intent(NoticeManagmentForModelToJJRActivity.this, NoticeStatusActivity.class);
		intent.putExtra("noticeId", item.getNoticeId());
		startActivity(intent);

	}


	private void parseRequestData(ResponseBean data) {
		try {
			switch (loadFlag) {
				case 0:
					List<JJRNoticeBean> datas=new ArrayList<>();
					String notices=data.getRecord();
					if (!TextUtils.isEmpty(notices)&&!"[]".equals(notices)){
						JSONArray array=new JSONArray(notices);
						for (int i=0;i<array.length();i++){
							JSONObject subObj=array.getJSONObject(i);
							JJRNoticeBean item=new JJRNoticeBean();
							item.setNoticeContent(StringUtils.getJsonValue(subObj, "noticeContent"));
							item.setNoticeId(StringUtils.getJsonValue(subObj, "noticeId"));
							item.setSubject(StringUtils.getJsonValue(subObj, "subject"));
							JSONObject publisherObj=new JSONObject(StringUtils.getJsonValue(subObj, "publisher"));
							JJRPublisher publisher=new JJRPublisher();
							publisher.setAuthStatus(StringUtils.getJsonValue(publisherObj, "authStatus"));
							publisher.setUserIcon(StringUtils.getJsonValue(publisherObj, "userIcon"));
							publisher.setUserId(StringUtils.getJsonValue(publisherObj, "userId"));
							publisher.setUsernick(StringUtils.getJsonValue(publisherObj, "usernick"));
							publisher.setUserType(StringUtils.getJsonValue(publisherObj, "userType"));
							item.setPublisher(publisher);
							datas.add(item);
						}
						if (datas.size()<pageSize){
							isLoadingDai=false;
						}
						daiSAdapter.addDatas(datas,pageIndexDai);
					}else{
						isLoadingDai=false;
					}
					break;
				case 1:
					List<JJRNoticeBean> datas2=new ArrayList<>();
					String notices2=data.getRecord();
					if (!TextUtils.isEmpty(notices2)&&!"[]".equals(notices2)){
						JSONArray array=new JSONArray(notices2);
						for (int i=0;i<array.length();i++){
							JSONObject subObj=array.getJSONObject(i);
							JJRNoticeBean item=new JJRNoticeBean();
							item.setNoticeContent(StringUtils.getJsonValue(subObj, "noticeContent"));
							item.setNoticeId(StringUtils.getJsonValue(subObj, "noticeId"));
							item.setSubject(StringUtils.getJsonValue(subObj, "subject"));
							JSONObject publisherObj=new JSONObject(StringUtils.getJsonValue(subObj, "publisher"));
							JJRPublisher publisher=new JJRPublisher();
							publisher.setAuthStatus(StringUtils.getJsonValue(publisherObj, "authStatus"));
							publisher.setUserIcon(StringUtils.getJsonValue(publisherObj, "userIcon"));
							publisher.setUserId(StringUtils.getJsonValue(publisherObj, "userId"));
							publisher.setUsernick(StringUtils.getJsonValue(publisherObj, "usernick"));
							publisher.setUserType(StringUtils.getJsonValue(publisherObj, "userType"));
							item.setPublisher(publisher);
							datas2.add(item);
						}
						if (datas2.size()<pageSize){
							isLoadingClose=false;
						}
						closeAdapter.addDatas(datas2,pageIndexClose);
					}else{
						isLoadingClose=false;
					}
					break;
				default:
					break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void responseFaith(ResponseBean data) {
		super.responseFaith(data);
		switch (loadFlag) {
			case 0:
				daiSList.onRefreshComplete();
				break;
			case 1:
				closeList.onRefreshComplete();
				break;
			default:
				break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		//刷新操作
		switch (loadFlag) {
			case 0:
				isLoadingDai=true;
				pageIndexDai=1;
				loadDatas(pageIndexDai);
				break;
			case 1:
				isLoadingClose=true;
				pageIndexClose=1;
				loadDatas(pageIndexClose);
				break;
			default:
				break;
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		//加载操作
		switch (loadFlag) {
			case 0:
				if (isLoadingDai){
					pageIndexDai=pageIndexDai+ 1;
					loadDatas(pageIndexDai);
				}else{
					falseDatas();
				}
				break;
			case 1:
				if (isLoadingClose){
					pageIndexClose=pageIndexClose+ 1;
					loadDatas(pageIndexClose);
				}else{
					falseDatas();
				}
				break;
			default:
				break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 2:
					switch (loadFlag) {
						case 0:
							daiSList.onRefreshComplete();
							break;
						case 1:
							closeList.onRefreshComplete();
							break;
						default:
							break;
					}
					break;
				default:
					break;
			}

		}
	};


	private void falseDatas(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					android.os.Message m = handler.obtainMessage();
					m.what=2;
					m.sendToTarget();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
}

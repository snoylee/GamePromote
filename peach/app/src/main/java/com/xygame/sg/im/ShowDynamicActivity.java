package com.xygame.sg.im;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.activity.JinPaiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiFuFeiDetailActivity;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.activity.YuePaiDetailActivity;
import com.xygame.second.sg.xiadan.activity.OrderDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.notice.ChaKanShouRuActivity;
import com.xygame.sg.activity.notice.CommentActivity;
import com.xygame.sg.activity.notice.CommentForModelActivity;
import com.xygame.sg.activity.notice.JJRNoticeDetailActivity;
import com.xygame.sg.activity.notice.JieSuanActivity;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.NoticeManagmentForModelActivity;
import com.xygame.sg.activity.notice.RecruitActivity;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.activity.notice.bean.NoticeStatusBeanForModel;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.TimeUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowDynamicActivity extends SGBaseActivity implements OnClickListener{
	private View backButton;
	private View rightButton;
	private ImageView rightbuttonIcon;
	private TextView titleName,rightButtonText;
	private ListView listView;
	private NewsDynamicAdapter adapter;
	private boolean isDelete=false;
	private SGNewsBean sgNewsBean;
	public SGNewsBean getSGNewsBean(){
		return sgNewsBean;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_dynamic_layout);
		backButton=findViewById(R.id.backButton);
		titleName=(TextView)findViewById(R.id.titleName);
		listView=(ListView)findViewById(R.id.listView);
		rightButton=findViewById(R.id.rightButton);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
		titleName.setText("蜜桃社区");
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.GONE);
		rightbuttonIcon.setImageResource(R.drawable.more_icon);
		rightButtonText.setText("完成");
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		adapter=new NewsDynamicAdapter(this,null,isDelete);
		listView.setAdapter(adapter);
		NewsEngine.updateDynamicStatus(this,UserPreferencesUtil.getUserId(this));
	}

	@Override
	protected void onStart() {
		super.onStart();
		List<SGNewsBean> datas=NewsEngine.quaryALLDaymicNews(this, UserPreferencesUtil.getUserId(this));
		adapter.addDatas(datas);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backButton){
			finish();
		}else if (v.getId()==R.id.rightButton){
			isDelete=!isDelete;
			if (isDelete){
				rightbuttonIcon.setVisibility(View.GONE);
				rightButtonText.setVisibility(View.VISIBLE);
			}else{
				rightbuttonIcon.setVisibility(View.VISIBLE);
				rightButtonText.setVisibility(View.GONE);
			}
			adapter.changeListEditor(isDelete);
		}
	}

	public void operateApplyNews(){
		sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
		NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
		adapter.updateOption(sgNewsBean);
	}

	public void agreeSucess() {
		sgNewsBean.setType(Constants.TARGET_XIADAAN_WAITTING_SERVICE);
		NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
		adapter.updateOption(sgNewsBean);
		try{
			JSONObject obj=new JSONObject(sgNewsBean.getNoticeSubject());
			String userId=StringUtils.getJsonValue(obj, "userId");
			String userNick=sgNewsBean.getFriendNickName();
			String userIcon=sgNewsBean.getFriendUserIcon();
			ToChatBean toChatBean = new ToChatBean();
			toChatBean.setRecruitLocIndex("");
			toChatBean.setNoticeId("");
			toChatBean.setNoticeSubject(userNick);
			toChatBean.setUserIcon(userIcon);
			toChatBean.setUserId(userId);
			toChatBean.setUsernick(userNick);
			toChatBean.setRecruitId("");
			XMPPUtils.sendMessgaeForXiaDan(this,UserPreferencesUtil.getUserNickName(this).concat(" 接受了您的订单"),toChatBean);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void timePassed() {
		sgNewsBean.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
		NewsEngine.updateType(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
		adapter.updateOption(sgNewsBean);
	}

	class NewsDynamicAdapter extends BaseAdapter {
		private List<SGNewsBean> strList;
		private Context context;
		private boolean isDelete;
		private ImageLoader imageLoader;
		public NewsDynamicAdapter(Context context, List<SGNewsBean> strList, boolean isDelete) {
			super();
			this.context = context;
			this.isDelete=isDelete;
			imageLoader=ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
			if (strList == null) {
				this.strList = new ArrayList<SGNewsBean>();
			}else{
				this.strList = strList;
			}
		}

		public void changeListEditor(boolean isDelete){
			this.isDelete=isDelete;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return strList.size();
		}

		@Override
		public SGNewsBean getItem(int arg0) {
			// TODO Auto-generated method stub
			return strList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public void clearDatas(){
			strList.clear();
		}

		public void addDatas(List<SGNewsBean> datas){
			this.strList=datas;
			notifyDataSetChanged();
		}

		public void updateOption(SGNewsBean sgNewsBean) {
			for (int i=0;i<strList.size();i++){
				if (strList.get(i).get_id().equals(sgNewsBean.get_id())){
					strList.get(i).setInout(sgNewsBean.getInout());
				}
			}
			notifyDataSetChanged();
		}

		/**
		 * 初始化View
		 */
		private class ViewHolder {
			private CircularImage userImage,godAppIcon;
			private ImageView deleteIcon;
			private TextView userName,nuReadNews,timerText,contentText,agreeButton,refuseButton;
		}

		/**
		 * 添加数据
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.dynamic_news_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.userName = (TextView) convertView
						.findViewById(R.id.userName);
				viewHolder.nuReadNews = (TextView) convertView
						.findViewById(R.id.nuReadNews);
				viewHolder.timerText = (TextView) convertView
						.findViewById(R.id.timerText);
				viewHolder.userImage =(CircularImage)convertView
						.findViewById(R.id.userImage);
				viewHolder.godAppIcon =(CircularImage)convertView
						.findViewById(R.id.godAppIcon);
				viewHolder.deleteIcon=(ImageView)convertView.findViewById(R.id.deleteIcon);
				viewHolder.contentText = (TextView) convertView
						.findViewById(R.id.contentText);
				viewHolder.agreeButton=(TextView)convertView.findViewById(R.id.agreeButton);
				viewHolder.refuseButton=(TextView)convertView.findViewById(R.id.refuseButton);
				convertView.setTag(viewHolder);
			} else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			SGNewsBean item=strList.get(position);
			viewHolder.nuReadNews.setVisibility(View.GONE);
			if(isDelete){
				viewHolder.deleteIcon.setVisibility(View.VISIBLE);
			}else{
				viewHolder.deleteIcon.setVisibility(View.GONE);
			}
			if (Constants.TARGET_XIADAAN_PASSED.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String startTime=StringUtils.getJsonValue(obj,"startTime");
					String holdTime=StringUtils.getJsonValue(obj,"holdTime");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								if (TextUtils.isEmpty(startTime)){
									viewHolder.contentText.setText(item.getMsgContent());
								}else{
									viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
								}
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						if (TextUtils.isEmpty(startTime)){
							viewHolder.contentText.setText(item.getMsgContent());
						}else{
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
						}
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("已过期");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAAN_WAITTING_SERVICE.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String startTime=StringUtils.getJsonValue(obj,"startTime");
					String holdTime=StringUtils.getJsonValue(obj,"holdTime");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								if (TextUtils.isEmpty(startTime)){
									viewHolder.contentText.setText(item.getMsgContent());
								}else{
									viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
								}
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						if (TextUtils.isEmpty(startTime)){
							viewHolder.contentText.setText(item.getMsgContent());
						}else{
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
						}
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("待服务");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAAN_CLOSE.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String startTime=StringUtils.getJsonValue(obj,"startTime");
					String holdTime=StringUtils.getJsonValue(obj,"holdTime");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								if (TextUtils.isEmpty(startTime)){
									viewHolder.contentText.setText(item.getMsgContent());
								}else{
									viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
								}
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						if (TextUtils.isEmpty(startTime)){
							viewHolder.contentText.setText(item.getMsgContent());
						}else{
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
						}
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("已关闭");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAAN_REFUSE.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String startTime=StringUtils.getJsonValue(obj,"startTime");
					String holdTime=StringUtils.getJsonValue(obj,"holdTime");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								if (TextUtils.isEmpty(startTime)){
									viewHolder.contentText.setText(item.getMsgContent());
								}else{
									viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
								}
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						if (TextUtils.isEmpty(startTime)){
							viewHolder.contentText.setText(item.getMsgContent());
						}else{
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
						}
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("已拒绝");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAAN_SERVING.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String startTime=StringUtils.getJsonValue(obj,"startTime");
					String holdTime=StringUtils.getJsonValue(obj,"holdTime");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								if (TextUtils.isEmpty(startTime)){
									viewHolder.contentText.setText(item.getMsgContent());
								}else{
									viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
								}
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						if (TextUtils.isEmpty(startTime)){
							viewHolder.contentText.setText(item.getMsgContent());
						}else{
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
						}
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("服务中");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String startTime=StringUtils.getJsonValue(obj,"startTime");
					String holdTime=StringUtils.getJsonValue(obj,"holdTime");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					String orderType=StringUtils.getJsonValue(obj,"orderType");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								if (TextUtils.isEmpty(startTime)){
									viewHolder.contentText.setText(item.getMsgContent());
								}else{
									viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
								}
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						if (TextUtils.isEmpty(startTime)){
							viewHolder.contentText.setText(item.getMsgContent());
						}else{
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat(" ").concat(holdTime).concat("次"));
						}
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					if ("2".equals(orderType)){
						viewHolder.agreeButton.setVisibility(View.GONE);
						viewHolder.refuseButton.setVisibility(View.GONE);
					}else if ("1".equals(orderType)){
						viewHolder.agreeButton.setVisibility(View.VISIBLE);
						viewHolder.refuseButton.setVisibility(View.GONE);
						viewHolder.agreeButton.setText("接受订单");
						viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.dark_green));
						viewHolder.agreeButton.setOnClickListener(new ARAction(item, orderId, startTime));
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_FEEDBACK_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								viewHolder.contentText.setText(item.getMsgContent());
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						viewHolder.contentText.setText(item.getMsgContent());
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.GONE);
					viewHolder.refuseButton.setVisibility(View.GONE);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_JIESUAN_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								viewHolder.contentText.setText(item.getMsgContent());
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						viewHolder.contentText.setText(item.getMsgContent());
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("已完成");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_CANCEL_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								viewHolder.contentText.setText(item.getMsgContent());
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						viewHolder.contentText.setText(item.getMsgContent());
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setText("已取消");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_APPLY_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					String orderId=StringUtils.getJsonValue(obj,"orderId");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								viewHolder.contentText.setText(item.getMsgContent());
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						viewHolder.contentText.setText(item.getMsgContent());
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					if ("5".equals(item.getInout())){
						viewHolder.agreeButton.setVisibility(View.VISIBLE);
						viewHolder.refuseButton.setVisibility(View.VISIBLE);
						viewHolder.agreeButton.setText("接受");
						viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.dark_green));
						viewHolder.refuseButton.setText("拒绝");
						viewHolder.refuseButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
						viewHolder.agreeButton.setOnClickListener(new AgreeClickListeners(item));
						viewHolder.refuseButton.setOnClickListener(new RefuseClickListeners(item));
					}else{
						viewHolder.agreeButton.setVisibility(View.GONE);
						viewHolder.refuseButton.setVisibility(View.GONE);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_APPLY_REPORT_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String skillCode=StringUtils.getJsonValue(obj,"skillCode");
					List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
					if (jinPaiBigTypeBeans != null) {
						for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
							if (skillCode.equals(it.getId())){
								viewHolder.godAppIcon.setVisibility(View.VISIBLE);
								viewHolder.userName.setText(it.getName());
								viewHolder.contentText.setText(item.getMsgContent());
								imageLoader.loadImage(it.getUrl(), viewHolder.godAppIcon, true);
								break;
							}
						}
					}else{
						viewHolder.userName.setText("未知");
						viewHolder.godAppIcon.setVisibility(View.GONE);
						viewHolder.contentText.setText(item.getMsgContent());
						viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
					}
					viewHolder.agreeButton.setVisibility(View.GONE);
					viewHolder.refuseButton.setVisibility(View.GONE);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else{
				viewHolder.godAppIcon.setVisibility(View.GONE);
				if ("-1".equals(item.getInout())){
					viewHolder.agreeButton.setVisibility(View.VISIBLE);
					viewHolder.agreeButton.setText("同意");
					viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.black));
					viewHolder.refuseButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setText("拒绝");
					viewHolder.refuseButton.setTextColor(context.getResources().getColor(R.color.black));
//					viewHolder.agreeButton.setOnClickListener(new ARAction(item, "0"));
//					viewHolder.refuseButton.setOnClickListener(new ARAction(item, "1"));
				}else{
					viewHolder.agreeButton.setVisibility(View.GONE);
					viewHolder.refuseButton.setVisibility(View.GONE);
				}
				viewHolder.contentText.setText(item.getMsgContent());
				if (item.getNoticeId()!=null&&!"null".equals(item.getNoticeId())&&!"".equals(item.getRecruitId())){
					viewHolder.userName.setText(item.getNoticeId());
				}else{
					viewHolder.userName.setText("系统通知");
				}
			}
			if (item.getRecruitId()!=null&&!"null".equals(item.getRecruitId())&&!"".equals(item.getRecruitId())){
				imageLoader.loadImage(item.getRecruitId(), viewHolder.userImage, true);
				viewHolder.userImage.setOnClickListener(new IntoPersonalDeltail(item));
			} else {
				viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
			}
			viewHolder.timerText.setText(TimeUtils.formatTime(Long.parseLong(item.getTimestamp())));
			viewHolder.deleteIcon.setOnClickListener(new HideMessage(item, position));
			if (!"".equals(item.getType())){
				convertView.setOnClickListener(new IntoChatWindow(item));
			}
			return convertView;
		}

		private class IntoPersonalDeltail implements View.OnClickListener{
			private SGNewsBean presenter;
			public IntoPersonalDeltail(SGNewsBean presenter){
				this.presenter=presenter;
			}

			@Override
			public void onClick(View v) {
				intoPersonalAct(presenter);
			}
		}

		private void intoPersonalAct(SGNewsBean presenter) {
			Intent intent = new Intent(context, PersonalDetailActivity.class);
			intent.putExtra("userNick", presenter.getFriendNickName() == null ? "" : presenter.getFriendNickName());
			intent.putExtra("userId", presenter.getFriendUserId());
			context. startActivity(intent);
		}

		private class ARAction implements OnClickListener{
			private SGNewsBean item;
			private String flag,startTime;
			public ARAction(SGNewsBean item,String flag,String startTime){
				this.item=item;
				this.flag=flag;
				this.startTime=startTime;
			}
			@Override
			public void onClick(View v) {
				arActionTask(item,flag,startTime);
			}
		}

		private class HideMessage implements OnClickListener{
			private SGNewsBean item;
			private int index;
			public HideMessage(SGNewsBean item,int index){
				this.item=item;
				this.index=index;
			}

			@Override
			public void onClick(View v) {
				setHideNew(item, index);
			}
		}

		private void setHideNew(SGNewsBean item,int index){
			item.setIsShow(Constants.NEWS_HIDE);
			NewsEngine.deleteDynamicNew(context, item, UserPreferencesUtil.getUserId(context));
			strList.remove(index);
			notifyDataSetChanged();
		}

		private class IntoChatWindow implements OnClickListener{
			private SGNewsBean item;
			public IntoChatWindow(SGNewsBean item){
				this.item=item;
			}

			@Override
			public void onClick(View v) {
				openChatWindow(item);
			}
		}

		private void openChatWindow(SGNewsBean item){
			if (Constants.TARGET_CAMERA_ENMPLOM_MODEL.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,NoticeDetailActivity.class);
					intent.putExtra("noticeId",obj.getString("noticeId"));
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_NOTICE_DETAIL.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String type= StringUtils.getJsonValue(obj, "type");
					if (!"".equals(type)){
						if ("1".equals(type)){
							Intent intent = new Intent(context, JJRNoticeDetailActivity.class);
							intent.putExtra("noticeId",obj.getString("noticeId"));
							context.startActivity(intent);
						}else{
							Intent intent=new Intent(context,NoticeDetailActivity.class);
							intent.putExtra("noticeId",obj.getString("noticeId"));
							context.startActivity(intent);
						}
					}else{
						Intent intent=new Intent(context,NoticeDetailActivity.class);
						intent.putExtra("noticeId",obj.getString("noticeId"));
						context.startActivity(intent);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_SHOW_EVALUATE.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					if (UserPreferencesUtil.isModel(context)){
						Intent intent=new Intent(context,CommentForModelActivity.class);
						intent.putExtra("noticeId",obj.getString("noticeId"));
						context.startActivity(intent);
					}else{
						NoticeStatusBean itemBean=new NoticeStatusBean();
						itemBean.setUserId(obj.getString("toUserId"));
						Intent intent=new Intent(context,CommentActivity.class);
						intent.putExtra("noticeId",obj.getString("noticeId"));
						intent.putExtra("bean",itemBean);
						intent.putExtra("commentFlag","Y");
						context.startActivity(intent);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_MEMBER.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					String type= StringUtils.getJsonValue(obj, "type");
					if (!"".equals(type)){
						if ("1".equals(type)){
							Intent intent = new Intent(context, JJRNoticeDetailActivity.class);
							intent.putExtra("noticeId",obj.getString("noticeId"));
							context.startActivity(intent);
						}else{
							Intent intent = new Intent(context, RecruitActivity.class);
							intent.putExtra("noticeId",obj.getString("noticeId"));
							intent.putExtra("recruitId",obj.getString("recruitId"));
							context.startActivity(intent);
						}
					}else{
						Intent intent = new Intent(context, RecruitActivity.class);
						intent.putExtra("noticeId",obj.getString("noticeId"));
						intent.putExtra("recruitId",obj.getString("recruitId"));
						context.startActivity(intent);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_HIRED.equals(item.getType())){
				Intent intent=new Intent(context, NoticeManagmentForModelActivity.class);
				context.startActivity(intent);
			}else if (Constants.TARGET_BALANCE.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,JieSuanActivity.class);
					intent.putExtra("noticeId",obj.getString("noticeId"));
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_INCOME.equals(item.getType())){
				try{
					NoticeStatusBeanForModel it=new NoticeStatusBeanForModel();
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context, ChaKanShouRuActivity.class);
					intent.putExtra("noticeId",obj.getString("noticeId"));
					it.setUserIcon(obj.getString("userIcon"));
					it.setUserNick(obj.getString("userNick"));
					it.setOrders(obj.getString("orders"));
					it.setFinalAmount(obj.getString("finalAMount"));
					it.setFinishTime(obj.getString("finishTime"));
					intent.putExtra("bean", it);
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_JINPAI_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,JinPaiDetailActivity.class);
					intent.putExtra("actId",obj.getString("actId"));
					intent.putExtra("userId",obj.getString("userId"));
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_FREE_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,YuePaiDetailActivity.class);
					intent.putExtra("actId",obj.getString("actId"));
					intent.putExtra("userId",obj.getString("userId"));
					intent.putExtra("fromFlag","news");
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_FUFEI_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,JinPaiFuFeiDetailActivity.class);
					intent.putExtra("actId",obj.getString("actId"));
					intent.putExtra("userId",obj.getString("userId"));
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,OrderDetailActivity.class);
					intent.putExtra("orderId",obj.getString("orderId"));
					intent.putExtra("userId",obj.getString("userId"));
					intent.putExtra("skillCode",obj.getString("skillCode"));
					intent.putExtra("newsBean",item);
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_FEEDBACK_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,OrderDetailActivity.class);
					intent.putExtra("orderId",obj.getString("orderId"));
					intent.putExtra("userId",obj.getString("userId"));
					intent.putExtra("skillCode",obj.getString("skillCode"));
					intent.putExtra("newsBean",item);
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_JIESUAN_ACT.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,OrderDetailActivity.class);
					intent.putExtra("orderId",obj.getString("orderId"));
					intent.putExtra("userId",obj.getString("userId"));
					intent.putExtra("skillCode",obj.getString("skillCode"));
					intent.putExtra("newsBean",item);
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if (Constants.TARGET_XIADAN_APPLY_ACT.equals(item.getType())||Constants.TARGET_XIADAAN_PASSED.equals(item.getType())||Constants.TARGET_XIADAAN_WAITTING_SERVICE.equals(item.getType())||Constants.TARGET_XIADAN_CANCEL_ACT.equals(item.getType())||Constants.TARGET_XIADAAN_SERVING.equals(item.getType())||Constants.TARGET_XIADAAN_CLOSE.equals(item.getType())||Constants.TARGET_XIADAAN_REFUSE.equals(item.getType())){
				try{
					JSONObject obj=new JSONObject(item.getNoticeSubject());
					Intent intent=new Intent(context,OrderDetailActivity.class);
					intent.putExtra("orderId",obj.getString("orderId"));
					intent.putExtra("userId",obj.getString("userId"));
					intent.putExtra("skillCode",obj.getString("skillCode"));
					intent.putExtra("newsBean",item);
					context.startActivity(intent);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	private void arActionTask(SGNewsBean item,String flag,String startTime){
		sgNewsBean=item;
		if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(startTime)),CalendarUtils.getHenGongDateDis(System.currentTimeMillis()))){
			requestOpratOrder(flag);
		}else{
			timePassed();
		}
	}

	private class AgreeClickListeners implements OnClickListener{
		private SGNewsBean item;
		public AgreeClickListeners(SGNewsBean item){
			this.item=item;
		}
		@Override
		public void onClick(View v) {
			oprateTwoButtonAct(item,"3");
		}
	}

	private class RefuseClickListeners implements OnClickListener{
		private SGNewsBean item;
		public RefuseClickListeners(SGNewsBean item){
			this.item=item;
		}
		@Override
		public void onClick(View v) {
			oprateTwoButtonAct(item, "4");
		}
	}

	public void oprateTwoButtonAct(SGNewsBean bean,String flag){
		sgNewsBean=bean;
		if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(System.currentTimeMillis()),CalendarUtils.getTimeByTimeAndHour(Long.parseLong(bean.getTimestamp()),48))){
			tipDialog("订单已过期");
		}else{
			RequestBean item = new RequestBean();
			item.setIsPublic(false);
			try {
				JSONObject obj=new JSONObject(bean.getNoticeSubject());
				String orderId=StringUtils.getJsonValue(obj,"orderId");

				JSONObject object = new JSONObject();
				object.put("orderStatus",flag);
				object.put("orderId",orderId);
				item.setData(object);
				item.setServiceURL(ConstTaskTag.QUEST_OPERATE_APPLY);
				ShowMsgDialog.showNoMsg(this,false);
				ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_OPERATE_APPLY);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private void tipDialog(String tip){
		OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						operateApplyNews();
					}
				});
				dialog.show();
	}

	public void requestOpratOrder(String flag) {
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject object = new JSONObject();
			object.put("orderStatus","2");
			object.put("orderId",flag);
			item.setData(object);
			item.setServiceURL(ConstTaskTag.QUEST_ORDER_STATUS);
			ShowMsgDialog.showNoMsg(this,false);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ORDER_STATUS);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.QUERY_ORDER_STATUS:
				if ("0000".equals(data.getCode())){
					agreeSucess();
				}else{
					Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
				}
				break;
			case ConstTaskTag.QUERY_OPERATE_APPLY:
				if ("0000".equals(data.getCode())){
					operateApplyNews();
				}else{
					Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
}
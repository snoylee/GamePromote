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
import com.xygame.second.sg.xiadan.activity.QiangDanDetailActivity;
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

public class ShowQiangDynamicActivity extends SGBaseActivity implements OnClickListener{
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
		NewsGodEngine.updateDynamicStatus(this, UserPreferencesUtil.getUserId(this));
	}

	@Override
	protected void onStart() {
		super.onStart();
		List<SGNewsBean> datas=NewsGodEngine.quaryALLDaymicNews(this, UserPreferencesUtil.getUserId(this));
		for (int i=0;i<datas.size();i++){
			SGNewsBean item=datas.get(i);
			try {
				JSONObject jsonObjectBody=new JSONObject(item.getNoticeSubject());
				String  createTime=StringUtils.getJsonValue(jsonObjectBody,"createTime");
				if (CalendarUtils.isPassed10Min(UserPreferencesUtil.getorderExpireTime(this),createTime)){
					NewsGodEngine.deleteDynamicNew(this, item, UserPreferencesUtil.getUserId(this));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		List<SGNewsBean> newsDatas=NewsGodEngine.quaryALLDaymicNews(this, UserPreferencesUtil.getUserId(this));
		adapter.addDatas(newsDatas);
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

	public void timePassed() {
		sgNewsBean.setInout("0");
		NewsGodEngine.updateOptionDynamic(this, UserPreferencesUtil.getUserId(this), sgNewsBean);
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
			private TextView userName,nuReadNews,timerText,contentText,refuseButton;
			private View agreeButton;
		}

		/**
		 * 添加数据
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SGNewsBean item=strList.get(position);
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.dynamic_news_qiang_item,
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
				viewHolder.agreeButton=convertView.findViewById(R.id.agreeButton);
				viewHolder.refuseButton=(TextView)convertView.findViewById(R.id.refuseButton);
				convertView.setTag(viewHolder);
			} else{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.nuReadNews.setVisibility(View.GONE);
			if(isDelete){
				viewHolder.deleteIcon.setVisibility(View.VISIBLE);
			}else{
				viewHolder.deleteIcon.setVisibility(View.GONE);
			}
			try{
				JSONObject obj=new JSONObject(item.getNoticeSubject());
				String skillCode=StringUtils.getJsonValue(obj,"skillCode");
				String orderId=StringUtils.getJsonValue(obj,"orderId");
				String startTime=StringUtils.getJsonValue(obj,"startTime");
				String holdTime=StringUtils.getJsonValue(obj,"holdTime");
				List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
				if (jinPaiBigTypeBeans != null) {
					for (JinPaiBigTypeBean it:jinPaiBigTypeBeans){
						if (skillCode.equals(it.getId())){
							viewHolder.godAppIcon.setVisibility(View.VISIBLE);
							viewHolder.userName.setText(it.getName());
							viewHolder.contentText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(startTime)).concat("  ").concat(holdTime).concat("小时"));
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
					viewHolder.refuseButton.setVisibility(View.GONE);
					viewHolder.agreeButton.setOnClickListener(new AgreeClickListeners(item));
				}else{
					viewHolder.agreeButton.setVisibility(View.GONE);
					viewHolder.refuseButton.setVisibility(View.VISIBLE);
					viewHolder.refuseButton.setText("订单已抢");
					viewHolder.refuseButton.setTextColor(context.getResources().getColor(R.color.deep_gray));
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			if (item.getRecruitId()!=null&&!"null".equals(item.getRecruitId())&&!"".equals(item.getRecruitId())){
				imageLoader.loadImage(item.getRecruitId(), viewHolder.userImage, true);
				viewHolder.userImage.setOnClickListener(new IntoPersonalDeltail(item));
			} else {
				viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
			}
			viewHolder.timerText.setText(TimeUtils.formatTime(Long.parseLong(item.getTimestamp())));
			viewHolder.deleteIcon.setOnClickListener(new HideMessage(item, position));
			convertView.setOnClickListener(new IntoChatWindow(item));
			return convertView;
		}

		private class IntoPersonalDeltail implements OnClickListener{
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
			NewsGodEngine.deleteDynamicNew(context, item, UserPreferencesUtil.getUserId(context));
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
			String jsonStr=item.getNoticeSubject();
			Intent intent=new Intent(context,QiangDanDetailActivity.class);
			intent.putExtra("jsonStr",jsonStr);
			intent.putExtra("newsBean",item);
			intent.putExtra("userImage",item.getRecruitId());
			intent.putExtra("userName",item.getNoticeId());
			context.startActivity(intent);
		}
	}
	private class AgreeClickListeners implements OnClickListener{
		private SGNewsBean item;
		public AgreeClickListeners(SGNewsBean item){
			this.item=item;
		}
		@Override
		public void onClick(View v) {
			oprateTwoButtonAct(item);
		}
	}
	public void oprateTwoButtonAct(SGNewsBean bean){
		sgNewsBean=bean;
		try {
			JSONObject obj=new JSONObject(bean.getNoticeSubject());
			String orderId=StringUtils.getJsonValue(obj,"orderId");
			String createTime=StringUtils.getJsonValue(obj,"createTime");
			RequestBean item = new RequestBean();
			item.setIsPublic(false);
			JSONObject object = new JSONObject();
			object.put("orderId",orderId);
			object.put("createTime",createTime);
			item.setData(object);
			item.setServiceURL(ConstTaskTag.QUEST_QIANG_FOR_GOD);
			ShowMsgDialog.showNoMsg(this,false);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_QIANG_FOR_GOD);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.QUERY_QIANG_FOR_GOD:
				if ("0000".equals(data.getCode())){
					timePassed();
				}else if ("9500".equals(data.getCode())||"9503".equals(data.getCode())){
					showOneButtonDialog(data.getMsg());
				}else{
					Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}

	private void showOneButtonDialog(String msg) {
		OneButtonDialog dialog = new OneButtonDialog(this, msg, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						timePassed();
					}
				});
				dialog.show();
	}
}
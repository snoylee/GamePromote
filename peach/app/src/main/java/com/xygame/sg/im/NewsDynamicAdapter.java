package com.xygame.sg.im;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.ChaKanShouRuActivity;
import com.xygame.sg.activity.notice.CommentActivity;
import com.xygame.sg.activity.notice.CommentForModelActivity;
import com.xygame.sg.activity.notice.JieSuanActivity;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.NoticeManagmentForModelActivity;
import com.xygame.sg.activity.notice.RecruitActivity;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.activity.notice.bean.NoticeStatusBeanForModel;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.TimeUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsDynamicAdapter extends BaseAdapter {
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
		strList.clear();
		strList.addAll(datas);
		notifyDataSetChanged();
	}

	/**
	 * 初始化View
	 */
	private class ViewHolder {
		private CircularImage userImage;
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
		if ("-1".equals(item.getInout())){
			viewHolder.agreeButton.setVisibility(View.VISIBLE);
			viewHolder.agreeButton.setText("同意");
			viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.black));
			viewHolder.refuseButton.setVisibility(View.VISIBLE);
			viewHolder.refuseButton.setText("拒绝");
			viewHolder.refuseButton.setTextColor(context.getResources().getColor(R.color.black));

		}else if ("1".equals(item.getInout())){
			viewHolder.agreeButton.setVisibility(View.GONE);
			viewHolder.refuseButton.setVisibility(View.VISIBLE);
			viewHolder.refuseButton.setText("已拒绝");
			viewHolder.refuseButton.setTextColor(context.getResources().getColor(R.color.gray));
		}else if ("0".equals(item.getInout())){
			viewHolder.agreeButton.setVisibility(View.VISIBLE);
			viewHolder.agreeButton.setText("已同意");
			viewHolder.agreeButton.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.refuseButton.setVisibility(View.GONE);
		}
		if(isDelete){
			viewHolder.deleteIcon.setVisibility(View.VISIBLE);
		}else{
			viewHolder.deleteIcon.setVisibility(View.GONE);
		}
		viewHolder.contentText.setText(item.getMsgContent());
		if (item.getNoticeId()!=null&&!"null".equals(item.getNoticeId())){
			viewHolder.userName.setText(item.getNoticeId());
		}else{
			viewHolder.userName.setText("模范儿");
		}
		if (item.getRecruitId()!=null&&!"null".equals(item.getRecruitId())){
			imageLoader.loadImage(item.getRecruitId(), viewHolder.userImage, true);
		} else {
			viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
		}
		viewHolder.timerText.setText(TimeUtils.formatTime(Long.parseLong(item.getTimestamp())));
		viewHolder.deleteIcon.setOnClickListener(new HideMessage(item, position));
		convertView.setOnClickListener(new IntoChatWindow(item));
		return convertView;
	}

	private class IntoDynimicNews implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			IntoDynimicNewsAction();
		}
	}

	private void IntoDynimicNewsAction(){
		Intent intent=new Intent(context, ShowDynamicActivity.class);
		context.startActivity(intent);
	}

	private class IntoSystemNews implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			IntoSystemNewsAction();
		}
	}

	private void IntoSystemNewsAction(){
		Intent intent=new Intent(context, ShowSystemNewsActivity.class);
		context.startActivity(intent);
	}

	private class HideMessage implements View.OnClickListener{
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

	private class IntoChatWindow implements View.OnClickListener{
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
				Intent intent=new Intent(context,NoticeDetailActivity.class);
				intent.putExtra("noticeId",obj.getString("noticeId"));
				context.startActivity(intent);
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
				Intent intent = new Intent(context, RecruitActivity.class);
				intent.putExtra("noticeId",obj.getString("noticeId"));
				intent.putExtra("recruitId",obj.getString("recruitId"));
				context.startActivity(intent);
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
		}
	}
}

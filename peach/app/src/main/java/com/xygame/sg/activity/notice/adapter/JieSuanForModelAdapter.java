package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.ModelRequestJieSuanActivity;
import com.xygame.sg.activity.notice.bean.NoticeStatusBeanForModel;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by xy on 2015/11/16.
 */
public class JieSuanForModelAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<NoticeStatusBeanForModel> datas;
	private ImageLoader imageLoader;
	private Context context;
	private String noticeId;
	private String currTime;

	public JieSuanForModelAdapter(Context context, List<NoticeStatusBeanForModel> datas,String noticeId) {
		super();
		inflater = LayoutInflater.from(context);
		this.context=context;
		this.noticeId=noticeId;
		imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if (datas == null) {
			this.datas = new ArrayList<NoticeStatusBeanForModel>();
		} else {
			this.datas = datas;
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public NoticeStatusBeanForModel getItem(int i) {
		return datas.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}
	
	public void clearDatas(){
		datas.clear();
	}
	
	public void setNoticeCommentStatus(String userId){
		for(int i=0;i<datas.size();i++){
			if(datas.get(i).getUserId().equals(userId)){
				datas.get(i).setPrise(true);
			}
		}
		notifyDataSetChanged();
	}

	public void addDatas(List<NoticeStatusBeanForModel> its,String currTime) {
		this.currTime=currTime;
		datas.addAll(its);
		notifyDataSetChanged();
	}
	
	public void addItem(NoticeStatusBeanForModel item) {
		datas.add(item);
		notifyDataSetChanged();
	}
	
	public void updateJiSuanItem(String userId,String liuYan){
		for(int i=0;i<datas.size();i++){
			if(datas.get(i).getUserId().equals(userId)){
				datas.get(i).setEndTime(String.valueOf(System.currentTimeMillis()));
				datas.get(i).setApplyDesc(liuYan);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.daijiesuan_item_for_model, null);
			holder = new ViewHolder();
			holder.userImage = (CircularImage) convertView.findViewById(R.id.userImage);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			holder.zhaoMuView = (LinearLayout) convertView.findViewById(R.id.zhaoMuView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NoticeStatusBeanForModel item = datas.get(i);
		holder.zhaoMuView.removeAllViews();
		View subView=inflater.inflate(R.layout.daijiesuan_for_model_item_item, null);
		
		TextView zhaomuTxt=(TextView)subView.findViewById(R.id.zhaomuTxt);
		ImageView tagIcon=(ImageView)subView.findViewById(R.id.tagIcon);
		TextView tagText=(TextView)subView.findViewById(R.id.tagText);
		TextView priceValue=(TextView)subView.findViewById(R.id.priceValue);
		TextView statusText=(TextView)subView.findViewById(R.id.statusText);
		TextView leftTimer=(TextView)subView.findViewById(R.id.leftTimer);
		TextView rightViewTip=(TextView)subView.findViewById(R.id.rightViewTip);
		TextView jieSuanPriceValue=(TextView)subView.findViewById(R.id.jieSuanPriceValue);
		
		int j = Integer.parseInt(item.getOrders());
		String[] numArray = Constants.CHARACTE_NUMS;
		if (j > numArray.length - 1) {
			zhaomuTxt.setText("招募 " + (j + 1));
		} else {
			zhaomuTxt.setText("招募".concat(numArray[j]));
		}
		if ("1".equals(item.getGender())) {
			tagIcon.setImageResource(R.drawable.sg_pl_man);
			tagText.setText("男");
		} else if ("0".equals(item.getGender())) {
			tagIcon.setImageResource(R.drawable.sg_pl_woman);
			tagText.setText("女");
		}
		priceValue.setText("￥".concat(item.getReward()));
//		if("0".equals(item.getStatus())){
//			statusText.setText("待审核");
//		}else if("1".equals(item.getStatus())){
//			statusText.setText("招募中");
//		}else if("2".equals(item.getStatus())){
//			statusText.setText("拍摄中");
//		}else if("3".equals(item.getStatus())){
//			statusText.setText("已完成");
//		}else if("4".equals(item.getStatus())){
//			statusText.setText("已关闭");
//		}

		statusText.setVisibility(View.INVISIBLE);
		
		if(!"null".equals(item.getEndTime())){
			leftTimer.setVisibility(View.VISIBLE);
			String timerLeft=CalendarUtils.getLeftTimeDistanceForJieSuan(Long.parseLong(item.getEndTime()), Long.parseLong(currTime));
			if (!"".equals(timerLeft)){
				if (timerLeft.contains("-")){
					leftTimer.setText("系统正在结算处理中");
				}else{
					leftTimer.setText(
							"对方确认拍摄完成，还剩".concat(timerLeft).concat("自动结算"));
				}
			}else{
				leftTimer.setVisibility(View.INVISIBLE);
			}
			rightViewTip.setText("查看详情");
			rightViewTip.setOnClickListener(new checkJieSuan(item));
		}else{
			leftTimer.setVisibility(View.INVISIBLE);
			rightViewTip.setText("确认并结算");
			rightViewTip.setOnClickListener(new intoJieSuan(item));
		}
		jieSuanPriceValue.setText("￥".concat(item.getFinalAmount()));
		holder.zhaoMuView.addView(subView);
		holder.userName.setText(item.getUserNick());
		imageLoader.loadImage(item.getUserIcon(), holder.userImage, true);
		return convertView;
	}
	
	class checkJieSuan implements OnClickListener{
		private NoticeStatusBeanForModel item;
		public checkJieSuan(NoticeStatusBeanForModel item) {
			// TODO Auto-generated constructor stub
			this.item=item;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			checkJieSuanPage(item);
		}
	}
	
	private void checkJieSuanPage(NoticeStatusBeanForModel item){
		Intent intent=new Intent(context, ModelRequestJieSuanActivity.class);
		intent.putExtra("bean", item);
		intent.putExtra("checkFlag", "checkFlag");
		intent.putExtra("noticeId", noticeId);
		context.startActivity(intent);
	}
	
	class intoJieSuan implements OnClickListener{
		private NoticeStatusBeanForModel item;
		public intoJieSuan(NoticeStatusBeanForModel item) {
			// TODO Auto-generated constructor stub
			this.item=item;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intoJieSuanPage(item);
		}
	}
	
	private void intoJieSuanPage(NoticeStatusBeanForModel item){
		Intent intent=new Intent(context, ModelRequestJieSuanActivity.class);
		intent.putExtra("noticeId", noticeId);
		intent.putExtra("bean", item);
		context.startActivity(intent);
	}

	private class ViewHolder {
		private CircularImage userImage;
		private TextView userName;
		private LinearLayout zhaoMuView;
	}
}

package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.ChaKanShouRuActivity;
import com.xygame.sg.activity.notice.CommentForModelActivity;
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
import android.widget.TextView;

/**
 * Created by xy on 2015/11/16.
 */
public class YiJieSuanForModelAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<NoticeStatusBeanForModel> datas;
	private ImageLoader imageLoader;
	private Context context;
	private String noticeId;

	public YiJieSuanForModelAdapter(Context context, List<NoticeStatusBeanForModel> datas,String noticeId) {
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

	public void addDatas(List<NoticeStatusBeanForModel> its) {
		datas.addAll(its);
		notifyDataSetChanged();
	}
	
	public void addItem(NoticeStatusBeanForModel item) {
		datas.add(item);
		notifyDataSetChanged();
	}
	
	public NoticeStatusBeanForModel removeDaiToYi(String userId){
		NoticeStatusBeanForModel item=null;
		for(int i=0;i<datas.size();i++){
			if(datas.get(i).getUserId().equals(userId)){
				item=datas.get(i);
				item.setPrise(false);
				datas.remove(i);
			}
		}
		return item;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.jiesuan_item_for_model, null);
			holder = new ViewHolder();
			holder.userImage = (CircularImage) convertView.findViewById(R.id.userImage);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			holder.zhaomuTxt = (TextView) convertView.findViewById(R.id.zhaomuTxt);
			holder.signTime = (TextView) convertView.findViewById(R.id.signTime);
			holder.daiPrice = (TextView) convertView.findViewById(R.id.daiPrice);
			holder.buttonTip = (TextView) convertView.findViewById(R.id.buttonTip);
			holder.jieSuanView = convertView.findViewById(R.id.jieSuanView);
			holder.shouruView=convertView.findViewById(R.id.shouruView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NoticeStatusBeanForModel item = datas.get(i);
		holder.userName.setText(item.getUserNick());
		int j = Integer.parseInt(item.getOrders());
		String[] numArray = Constants.CHARACTE_NUMS;
		if (j > numArray.length - 1) {
			holder.zhaomuTxt.setText("招募 " + (j + 1));
		} else {
			holder.zhaomuTxt.setText("招募".concat(numArray[j]));
		}
		holder.daiPrice.setText("￥".concat(item.getReward()));
		String flag=item.getFlag();
		
		if (!item.isPrise()) {
			holder.buttonTip.setText("去评价");
			holder.jieSuanView.setOnClickListener(new intoComment(item, "N"));
		}else{
			holder.buttonTip.setText("查看评价");
			holder.jieSuanView.setOnClickListener(new intoComment(item, "Y"));
		}
		
		if(!"null".equals(item.getFinishTime())){
			holder.signTime .setVisibility(View.VISIBLE);
			holder.signTime .setText(
					"结算时间：".concat(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getFinishTime()))));
		}else{
			holder.signTime .setVisibility(View.GONE);
		}
		
		holder.shouruView.setOnClickListener(new intoJieSuan(item));
		
		imageLoader.loadImage(item.getUserIcon(), holder.userImage, true);
		
		return convertView;
	}
	
	class intoComment implements OnClickListener{
		private NoticeStatusBeanForModel item;
		private String commentFlag;
		public intoComment(NoticeStatusBeanForModel item,String commentFlag) {
			// TODO Auto-generated constructor stub
			this.item=item;
			this.commentFlag=commentFlag;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intoCommentPage(item,commentFlag);
		}
	}
	
	private void intoCommentPage(NoticeStatusBeanForModel item,String commentFlag){
		Intent intent=new Intent(context, CommentForModelActivity.class);
		intent.putExtra("bean", item);
		intent.putExtra("noticeId", noticeId);
		intent.putExtra("commentFlag", commentFlag);
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
		Intent intent=new Intent(context, ChaKanShouRuActivity.class);
		intent.putExtra("noticeId", noticeId);
		intent.putExtra("bean", item);
		context.startActivity(intent);
	}

	private class ViewHolder {
		private CircularImage userImage;
		private TextView userName, zhaomuTxt, signTime, daiPrice, buttonTip;
		private View jieSuanView,shouruView;
	}
}

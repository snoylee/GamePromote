package com.xygame.second.sg.sendgift.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.bean.GiftBean;
import com.xygame.second.sg.comm.inteface.LuQuListener;
import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.second.sg.sendgift.bean.RankBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class GiftRankAdapter extends BaseAdapter {
	private Context context;
	private List<RankBean> vector;
	private ImageLoader mImageLoader;
	private String fromFlag,publihserId,hireUserId;
	private LuQuListener luQuListener;
	private boolean isLvYunAialable;
	public GiftRankAdapter(Context context, List<RankBean> vector,String fromFlag,String publihserId,boolean isLvYunAialable) {
		this.isLvYunAialable=isLvYunAialable;
		this.context = context;
		this.fromFlag=fromFlag;
		this.publihserId=publihserId;
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		if(vector!=null){
			this.vector = vector;
		}else{
			this.vector=new ArrayList<>();
		}
	}

	public void setLuQuActListener(LuQuListener luQuListener){
		this.luQuListener=luQuListener;
	}

	public void updateUseStatus(String hireUserId) {
		this.hireUserId=hireUserId;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public RankBean getItem(int position) {
		return vector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (null == convertView)
		{
			viewHolder = new ViewHolder();
			convertView =LayoutInflater.from(context).inflate(
					R.layout.gift_rank_item, parent, false);
			viewHolder.orderNo = (TextView) convertView.findViewById(R.id.orderNo);
			viewHolder.gxValue=(TextView)convertView.findViewById(R.id.gxValue);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
			viewHolder.sexAge = (TextView) convertView.findViewById(R.id.sexAge);
			viewHolder.lqText=(TextView)convertView.findViewById(R.id.lqText);
			viewHolder.sexIcon = (ImageView) convertView
					.findViewById(R.id.sexIcon);
			viewHolder.sex_age_bg =convertView
					.findViewById(R.id.sex_age_bg);
			viewHolder.lvQuButton=convertView.findViewById(R.id.lvQuButton);
			viewHolder.avatar_iv=(CircularImage)convertView.findViewById(R.id.avatar_iv);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RankBean item=vector.get(position);
		Presenter presenter = item.getPresenter();
		mImageLoader.loadImage(presenter.getUserIcon(), viewHolder.avatar_iv, true);
		viewHolder. avatar_iv.setOnClickListener(new IntoPersonalDeltail(presenter));
		viewHolder.userName.setText(presenter.getUserNick());
		viewHolder.gxValue.setText(String.valueOf(ConstTaskTag.getDoublePrice(item.getAmount())));
		viewHolder.orderNo.setText("No.".concat(String.valueOf(position+1)));
		String sexStr = presenter.getGender();
		if (Constants.SEX_WOMAN.equals(sexStr)) {
			viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
			viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
			viewHolder.sexAge.setText(presenter.getAge());
		} else if (Constants.SEX_MAN.equals(sexStr)) {
			viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
			viewHolder.sexAge.setText(presenter.getAge());
			viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
		}

		if (publihserId.equals(UserPreferencesUtil.getUserId(context))){
			if (isLvYunAialable){
				viewHolder.lvQuButton.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(hireUserId)){
					if (presenter.getUserId().equals(hireUserId)){
						viewHolder.lqText.setText("已录用");
					}
					viewHolder.lqText.setTextColor(context.getResources().getColor(R.color.gray));
					viewHolder.lvQuButton.setBackgroundResource(R.drawable.shape_rect_gray);
				}else{
					viewHolder.lvQuButton.setOnClickListener(new LuQuActionListener(presenter));
				}
			}else{
				viewHolder.lvQuButton.setVisibility(View.GONE);
			}
		}else{
			viewHolder.lvQuButton.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class IntoPersonalDeltail implements View.OnClickListener{
		private Presenter presenter;
		public IntoPersonalDeltail(Presenter presenter){
			this.presenter=presenter;
		}

		@Override
		public void onClick(View v) {
			intoPersonalAct(presenter);
		}
	}

	private void intoPersonalAct(Presenter presenter) {
		Intent intent = new Intent(context, PersonalDetailActivity.class);
		intent.putExtra("userNick", presenter.getUserNick() == null ? "" : presenter.getUserNick());
		intent.putExtra("userId", presenter.getUserId());
		context. startActivity(intent);
	}

	private class LuQuActionListener implements View.OnClickListener{

		private Presenter presenter;

		public LuQuActionListener(Presenter presenter){
			this.presenter=presenter;
		}

		@Override
		public void onClick(View v) {
			luQuListener.luQuAction(presenter);
		}
	}

	private static class ViewHolder
	{
		CircularImage avatar_iv;
		TextView sexAge,userName,gxValue,orderNo,lqText;
		ImageView sexIcon;
		View sex_age_bg,lvQuButton;
	}

	public void addDatas(List<RankBean> datas, int mCurrentPage,String hireUserId) {
		if (mCurrentPage == 1) {
			this.hireUserId=hireUserId;
			this.vector = datas;
		} else {
			this.vector.addAll(datas);
		}
		notifyDataSetChanged();
	}

	public void clearDatas(){
		this.vector.clear();
		notifyDataSetChanged();
	}
}
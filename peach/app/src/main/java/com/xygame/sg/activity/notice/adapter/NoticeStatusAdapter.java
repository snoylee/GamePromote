package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.CheckStatusBean;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by xy on 2015/11/16.
 */
public class NoticeStatusAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CheckStatusBean> datas;
	private Context context;

	public NoticeStatusAdapter(Context context, List<CheckStatusBean> datas) {
		super();
		inflater = LayoutInflater.from(context);
		this.context=context;
		if (datas == null) {
			this.datas = new ArrayList<CheckStatusBean>();
		} else {
			this.datas = datas;
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CheckStatusBean getItem(int i) {
		return datas.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void addDatas(List<CheckStatusBean> datas) {
		this.datas.addAll(datas);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.notice_status_item, null);
			holder = new ViewHolder();
			holder.tagIcon = (ImageView) convertView.findViewById(R.id.tagIcon);
			holder.tagTitle = (TextView) convertView.findViewById(R.id.tagTitle);
			holder.tagTime = (TextView) convertView.findViewById(R.id.tagTime);
			holder.tagContent = (TextView) convertView.findViewById(R.id.tagContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CheckStatusBean item = datas.get(i);
		holder.tagTitle.setText(item.getFlowTitle());
		holder.tagTime.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getCreateTime())));
		if (UserPreferencesUtil.isModel(context)){
			switch (Integer.parseInt(item.getRelateType())) {
				case 1://报名成功
					holder.tagIcon.setImageResource(R.drawable.status_create_success);
					break;
				case 2://恭喜录用
					holder.tagIcon.setImageResource(R.drawable.status_nu_yun);
					break;
				case 3://通告关闭
					holder.tagIcon.setImageResource(R.drawable.status_guanbi);
					break;
				case 4://拍摄完成
					holder.tagIcon.setImageResource(R.drawable.status_paishewancheng);
					break;
				case 5://摄影师结算
					holder.tagIcon.setImageResource(R.drawable.status_jiesuan);
					break;
				case 6://摄影师自动结算
					holder.tagIcon.setImageResource(R.drawable.status_jiesuan);
					break;
				case 7://通告完成
					holder.tagIcon.setImageResource(R.drawable.status_guanbi);
					break;
				case 8://摄影师申请解除录用
					holder.tagIcon.setImageResource(R.drawable.status_jiechu_nuyun);
					break;
				case 9://解除录用
					holder.tagIcon.setImageResource(R.drawable.status_jiechu_nuyun);
					break;
				default:
					break;
			}
		}else {
			switch (Integer.parseInt(item.getRelateType())) {
				case 1://创建成功
					holder.tagIcon.setImageResource(R.drawable.status_create_success);
					break;
				case 2://等待审核
					holder.tagIcon.setImageResource(R.drawable.status_dai_shenhe);
					break;
				case 3://审核通过
					holder.tagIcon.setImageResource(R.drawable.status_shenghe_tongguo);
					break;
				case 4://通告关闭
					holder.tagIcon.setImageResource(R.drawable.status_guanbi);
					break;
				case 5://预付成功
					holder.tagIcon.setImageResource(R.drawable.status_yufu_chenggong);
					break;
				case 6://追加预付
					holder.tagIcon.setImageResource(R.drawable.status_zhuijia_yufu);
					break;
				case 7://模特结算
					holder.tagIcon.setImageResource(R.drawable.status_jiesuan);
					break;
				case 8://拍摄已完成
					holder.tagIcon.setImageResource(R.drawable.status_paishewancheng);
					break;
				case 9://申请解除录用
					holder.tagIcon.setImageResource(R.drawable.status_jiechu_nuyun);
					break;
				case 10://录用模特
					holder.tagIcon.setImageResource(R.drawable.status_nu_yun);
					break;
				default:
					break;
			}
		}
		buildContent(item.getUserNick(), item.getUserId(), item.getAmount(), item.getFlowDesc(), holder.tagContent);
		return convertView;
	}

	private void buildContent(final String userName,final String userId,String price,String content,TextView textView) {
		// TODO Auto-generated method stub
		String tempPrice=null,tempName=null;
		if(!"0".equals(price)){
			tempPrice="￥".concat(String.valueOf(StringUtil.getPrice(Long.parseLong(price))));
			if (content.contains("{1}")){
				content=content.replace("{1}",tempPrice);
			}
		}

		if(!"null".equals(userName)){
			tempName=userName;
			if (content.contains("{0}")){
				content=content.replace("{0}",tempName);
			}
		}

		SpannableString spannableString= new SpannableString(content);
		if(content.contains(userName)){
			spannableString.setSpan(
					new ClickableSpan() {
						@Override
						public void updateDrawState(TextPaint ds) {
							super.updateDrawState(ds);
							ds.setColor(Color.BLUE); // 设置文件颜色
							ds.setUnderlineText(false); // 设置下划线
						}

						@Override
						public void onClick(View widget) {
							initPersonalCenter(userName,userId);
						}
					},
					content.toString().indexOf(userName),
					content.toString().indexOf(userName)
							+ userName.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		if(tempPrice!=null){
			if(content.contains(tempPrice)){
				spannableString.setSpan(
						new ClickableSpan() {
							@Override
							public void updateDrawState(TextPaint ds) {
								super.updateDrawState(ds);
								ds.setColor(Color.RED); // 设置文件颜色
							ds.setUnderlineText(false); // 设置下划线
							}
							
							@Override
							public void onClick(View widget) {
							}
						},
						content.toString().indexOf(tempPrice),
						content.toString().indexOf(tempPrice)
						+ tempPrice.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		textView.setText(spannableString);
	}

	protected void initPersonalCenter(String userName,String userId) {
		// TODO Auto-generated method stub
		 Intent intent = new Intent(context, PersonInfoActivity.class);
         intent.putExtra(Constants.EDIT_OR_QUERY_FLAG,Constants.QUERY_INFO_FLAG);
         intent.putExtra("userId",userId);
         intent.putExtra("userNick",userName);
         context.startActivity(intent);
	}

	private class ViewHolder {
		private ImageView tagIcon;
		private TextView tagTitle, tagTime, tagContent;
	}
}

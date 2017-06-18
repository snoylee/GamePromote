package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.NoticeMemberVo;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class JJRModelsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader;
    private List<NoticeMemberVo> members;
    private JJRNoticeBean item;
    public JJRModelsAdapter(Context context, List<NoticeMemberVo> members,JJRNoticeBean item) {
        super();
        this.context = context;
        this.item=item;
        if (members!=null){
            this.members = members;
        }else{
            this.members=new ArrayList<>();
        }
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public NoticeMemberVo getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.jjr_model_item, null);
            holder = new ViewHolder();
            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.sex_age_tv = (TextView) convertView.findViewById(R.id.sex_age_tv);
            holder.type_icon_tv = (TextView) convertView.findViewById(R.id.type_icon_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.message_ll = (LinearLayout) convertView.findViewById(R.id.message_ll);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NoticeMemberVo memberVo = members.get(i);
        holder.nick_name_tv.setText(memberVo.getUserNick());
        String userIcon = memberVo.getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.avatar_iv, true);
        }

        String cityStr = memberVo.getCity()+"";
        if (!StringUtils.isEmpty(cityStr)){
            AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(memberVo.getCity());
            if (it.getName()== null){
                holder.location_tv.setText("");
            } else {
                holder.location_tv.setText(it.getName());
            }
        } else {
            holder.location_tv.setText("");
        }
        holder.sex_age_tv.setText(memberVo.getAge()+"");
        String sexStr = memberVo.getGender()+"";
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            Drawable femaleDrawable = context.getResources().getDrawable(R.drawable.female);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            Drawable maleDrawable = context.getResources().getDrawable(R.drawable.male);
            holder.sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
            holder.sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
        }

        String typeStr = memberVo.getUserType()+"";
        if (typeStr.equals(Constants.CARRE_MODEL)){//用户类型：2：模特；4：高级模特；0：未认证的模特（没有认证数据）
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_bg);
        } else if(typeStr.equals(Constants.PRO_MODEL)){
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_bg);
        }else if(typeStr.equals("0")){
            Drawable modelDrawable = context.getResources().getDrawable(R.drawable.model_identy_icon);
            holder.type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
            holder.type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
            holder.type_tv.setText("模特");
            holder.type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
        }

        holder.avatar_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonInfoActivity.class);
                intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
                intent.putExtra("userId", memberVo.getUserId() + "");
                intent.putExtra("userNick", memberVo.getUserNick());
                context.startActivity(intent);
            }
        });
        
        holder.message_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                ToChatBean toChatBean = new ToChatBean();
                toChatBean.setRecruitLocIndex("-1");
                toChatBean.setNoticeId(item.getNoticeId());
                toChatBean.setNoticeSubject(item.getSubject());
                toChatBean.setUserIcon(memberVo.getUserIcon());
                toChatBean.setUserId(String.valueOf(memberVo.getUserId()));
                toChatBean.setUsernick(memberVo.getUserNick());
                toChatBean.setRecruitId("-1");
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("toChatBean", toChatBean);
                context.startActivity(intent);
			}
		});
        return convertView;
    }

    private class ViewHolder {
        private ImageView avatar_iv;
        private TextView nick_name_tv,sex_age_tv,type_icon_tv,location_tv,type_tv;
        private LinearLayout message_ll;
    }

    public void addDatas(List<NoticeMemberVo> datas,int mCurrentPage) {
        if (mCurrentPage==1){
            this.members=datas;
        }else {
            this.members.addAll(datas);
        }
        notifyDataSetChanged();
    }
}

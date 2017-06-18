package com.xygame.sg.activity.notice.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.RecruitActivity;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberLabelVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.notice.fragment.EliminateFragment;
import com.xygame.sg.activity.notice.fragment.EmployFragment;
import com.xygame.sg.activity.notice.fragment.EnrollFragment;
import com.xygame.sg.activity.notice.fragment.TBDFragment;
import com.xygame.sg.activity.notice.fragment.UIHelper;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class RecruitModeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private Fragment fragment;
    private ImageLoader imageLoader;
    private List<NoticeMemberVo> members = new ArrayList<NoticeMemberVo>();
    private int fragmentId;
    private boolean ifShowSelect = true;//通告关闭时不需要任何操作
    private NoticeDetailVo notice;
    private String recruitId;
    private boolean isEliminate = false;
    public RecruitModeAdapter(Context context,Fragment fragment, List<NoticeMemberVo> members,int fragmentId,boolean ifShowSelect) {
        super();
        this.context = context;
        this.fragment = fragment;
        this.members = members;
        this.fragmentId = fragmentId;
        this.ifShowSelect = ifShowSelect;

        if (fragment instanceof EnrollFragment){
        	notice= ((EnrollFragment)fragment).getNotice();
        	recruitId=((EnrollFragment)fragment).getRecruitId();
        } else if (fragment instanceof TBDFragment){
        	notice=((TBDFragment)fragment).getNotice();
        	recruitId=((TBDFragment)fragment).getRecruitId();
        } else if (fragment instanceof EmployFragment){
        	notice=((EmployFragment)fragment).getNotice();
        	recruitId=((EmployFragment)fragment).getRecruitId();
        } else if (fragment instanceof EliminateFragment){
            isEliminate = true;
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
            convertView = inflater.inflate(R.layout.recruit_model_item, null);
            holder = new ViewHolder();
            holder.select_iv = (ImageView) convertView.findViewById(R.id.select_iv);
            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.sex_age_tv = (TextView) convertView.findViewById(R.id.sex_age_tv);
            holder.type_icon_tv = (TextView) convertView.findViewById(R.id.type_icon_tv);
            holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            holder.label_ll = (LinearLayout) convertView.findViewById(R.id.label_ll);
            holder.match_tv = (TextView) convertView.findViewById(R.id.match_tv);
            holder.match_progress = (ProgressBar) convertView.findViewById(R.id.match_progress);
            holder.message_ll = (LinearLayout) convertView.findViewById(R.id.message_ll);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(fragmentId == RecruitActivity.ELIMINATE_FRAGMENT){
            holder.select_iv.setVisibility(View.GONE);
        }
        if(ifShowSelect){
            holder.select_iv.setVisibility(View.VISIBLE);
        } else {
            holder.select_iv.setVisibility(View.GONE);
        }

        if (isEliminate){
            holder.message_ll.setVisibility(View.GONE);
        }

        final NoticeMemberVo memberVo = members.get(i);
        if (memberVo.isSelected()){
            holder.select_iv.setImageResource(R.drawable.gou);
        } else {
            holder.select_iv.setImageResource(R.drawable.gou_null);
        }
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
            holder.location_tv.setText("不限");
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

        List<NoticeMemberLabelVo> labels = memberVo.getLabels();
        if (holder.label_ll.getChildCount()>0){
            holder.label_ll.removeAllViews();
        }
        for (int j = 0;j< (labels.size() > 3 ? 3 :  labels.size());j++){
            TextView label_item = (TextView) inflater.inflate(R.layout.label_item, null);
            LinearLayout.LayoutParams layoutParamsLl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsLl.setMargins(0,0, 10, 0);//4个参数按顺序分别是左上右下
            label_item.setLayoutParams(layoutParamsLl);
            int typeId = Integer.parseInt(labels.get(j).getType());
            label_item.setText(SGApplication.getInstance().getTypeNameByTypeId(typeId));
            holder.label_ll.addView(label_item);
        }

        holder.match_tv.setText(memberVo.getMatch()+"%");
        holder.match_progress.setProgress(memberVo.getMatch());

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
        holder.select_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                members.get(i).setIsSelected(!members.get(i).isSelected());

                if (fragment instanceof EnrollFragment){
                    ((EnrollFragment)fragment).setSelectSatus();
                } else if (fragment instanceof TBDFragment){
                    ((TBDFragment)fragment).setSelectSatus();
                } else if (fragment instanceof EmployFragment){
                    ((EmployFragment)fragment).setSelectSatus();
                }
                notifyDataSetChanged();
            }
        });
        
        holder.message_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                if (isCertify()){
                    ToChatBean toChatBean = new ToChatBean();
                    List<NoticeRecruitVo> nrvDatas=notice.getRecruits();//recruitId
                    for(int i=0;i<nrvDatas.size();i++){
                        if(recruitId.equals(String.valueOf(nrvDatas.get(i).getRecruitId()))){
                            toChatBean.setRecruitLocIndex(nrvDatas.get(i).getLocIndex()+"");
                            break;
                        }
                    }
                    toChatBean.setNoticeId(String.valueOf(notice.getNoticeId()));
                    toChatBean.setNoticeSubject(notice.getSubject());
                    toChatBean.setUserIcon(memberVo.getUserIcon());
                    toChatBean.setUserId(String.valueOf(memberVo.getUserId()));
                    toChatBean.setUsernick(memberVo.getUserNick());
                    toChatBean.setRecruitId(recruitId);
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("toChatBean", toChatBean);
                    context.startActivity(intent);
                }
			}
		});
        return convertView;
    }
    public void setSelectedItem(int index){
        members.get(index).setIsSelected(!members.get(index).isSelected());
        notifyDataSetChanged();
    }
    public List<NoticeMemberVo> getSelectDatas(){
        List<NoticeMemberVo> tempDatas=new ArrayList<NoticeMemberVo>();
        for(NoticeMemberVo it:members){
            if(it.isSelected()){
                tempDatas.add(it);
            }
        }
        return tempDatas;
    }

    public List<NoticeMemberVo> getLeaveDatas(){

        Iterator<NoticeMemberVo> it = members.iterator();
        while(it.hasNext()){
            NoticeMemberVo bean = it.next();
            if(bean.isSelected()){
                it.remove();
            }
        }
        return members;
    }
    private class ViewHolder {
        private ImageView select_iv;
        private ImageView avatar_iv;
        private TextView nick_name_tv;
        private TextView sex_age_tv;
        private TextView type_icon_tv;
        private TextView type_tv;
        private TextView location_tv;
        private LinearLayout label_ll;
        private TextView match_tv;
        private ProgressBar match_progress;
        private LinearLayout message_ll;
    }

    private boolean isCertify(){
        boolean flag=false;
//        if (UserPreferencesUtil.isOnline(context)) {
//            String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(context);
//            if (!StringUtils.isEmpty(jsonStr)){
//                List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//                flag=judge(resultList);
//            }
//
//        } else {
//            flag=false;
//            context.startActivity(new Intent(context, LoginWelcomActivity.class));
//        }

        if (flag||notice.getPayStatus() == 3){
            flag=true;
        }else{
            showDialog0("请先认证或预付款后再试");
        }
        return flag;
    }

    private boolean judge( List<IdentyBean> resultList) {
        boolean flag=false;
        if (resultList == null || resultList.size() == 0) {
            flag=false;
        }
        IdentyBean identyBean = resultList.get(0);
        int authStatus = identyBean.getAuthStatus();
        int userType = identyBean.getUserType();
        if (resultList.size() == 1) {
            switch (userType) {
                case 8://摄影师
                    switch (authStatus) {
                        case 0:
                            flag=false;
                            break;
                        case 1:
                            flag=false;
                            break;
                        case 2:
                            flag=true;
                            break;
                        case 3:
                            flag=false;
                            break;
                    }
            }
        } else {
            flag=false;
        }
        return flag;
    }

    private void showDialog0(String tips){
        Dialog dialog = new AlertDialog.Builder(context).setTitle("提示").setMessage(tips).setPositiveButton("去认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, CMIdentyFirstActivity.class));
            }

        }).setNegativeButton("去预付", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                UIHelper.toFirstPay(fragment.getActivity(), notice);
            }

        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create();

        dialog.show();
    }

    private void showDialog1(String tips){
        Dialog dialog = new AlertDialog.Builder(context).setTitle("提示").setMessage(tips).setPositiveButton("去预付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UIHelper.toFirstPay(fragment.getActivity(), notice);
            }

        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create();

        dialog.show();
    }
}

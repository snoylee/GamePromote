package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.JJRNoticeDetailActivity;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class JJRNoticeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<JJRNoticeBean> datas;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .considerExifParams(true)
            .displayer(new SimpleBitmapDisplayer())
            .build();
    public JJRNoticeAdapter(Context context, List<JJRNoticeBean> datas) {
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
            holder.notice_top_bg_iv = (SelectableRoundedImageView) convertView.findViewById(R.id.notice_top_bg_iv);
            holder.jjrSubject = (TextView) convertView.findViewById(R.id.jjrSubject);
            holder.signed_num_tv = (TextView) convertView.findViewById(R.id.signed_num_tv);
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
        holder.signed_num_tv.setText(item.getApplyCount().concat("人"));
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
        holder.cm_avatar_iv.setOnClickListener(new View.OnClickListener() {
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
        holder.notice_list_item_root_ll.setOnClickListener(new View.OnClickListener() {
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


    class ExpandAndCollapseListener implements View.OnClickListener {
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
        private TextView signed_num_tv;
        private TextView jjrSubject,jjrContent1,jjrContent;
        private ImageView cm_avatar_iv;
        private TextView cm_nick_name_tv;
        private LinearLayout expand_arrow_ll;
        private LinearLayout collapse_arrow_ll;
        private View notice_list_item_root_ll;
    }
}

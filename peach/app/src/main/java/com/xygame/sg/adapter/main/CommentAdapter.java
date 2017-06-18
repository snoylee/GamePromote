package com.xygame.sg.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.AttentUserView;
import com.xygame.sg.bean.circle.CircleBean;
import com.xygame.sg.bean.circle.Commenters;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class CommentAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader;
    private List<Commenters> attentions = new ArrayList<>();


    public CommentAdapter(Context context, List<Commenters> attentions) {
        super();
        this.context = context;
        if (attentions==null){
            this.attentions = new ArrayList<>();
        }else{
            this.attentions = attentions;
        }
        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    public void addItem(Commenters item){
        this.attentions.add(0,item);
        notifyDataSetChanged();
    }

    public void addDatas(List<Commenters> datas,int mCurrentPage) {
        if (mCurrentPage==1){
            this.attentions=datas;
        }else {
            this.attentions.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return attentions.size();
    }

    @Override
    public Commenters getItem(int i) {
        return attentions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.circle_comment_item, null);
            holder = new ViewHolder();
            holder.userImage = (CircularImage) convertView.findViewById(R.id.userImage);
            holder.timerText = (TextView) convertView.findViewById(R.id.timerText);
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.commentContent = (TextView) convertView.findViewById(R.id.commentContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Commenters item=attentions.get(i);
        holder.timerText.setText(CalendarUtils.getHenGongDateDis(item.getCommentTime()));
        holder.userName.setText(item.getCommenter().getUsernick());
        holder.commentContent.setText(item.getContent());
//        String userIcon = attention.getUserIcon();
//        if (!StringUtils.isEmpty(userIcon)) {
//            imageLoader.loadImage(userIcon, holder.avatar_iv, false);
//        }

        return convertView;
    }

    private class ViewHolder {
        private TextView timerText,commentContent,userName;
        private CircularImage userImage;
    }

}

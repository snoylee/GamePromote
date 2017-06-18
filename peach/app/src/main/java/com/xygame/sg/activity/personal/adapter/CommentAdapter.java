package com.xygame.sg.activity.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.ScoreListVo;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.StringUtils;

import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class CommentAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private String utype;

    private List<ScoreListVo> scoreList;

    private ImageLoader imageLoader;


    public CommentAdapter(Context context, List<ScoreListVo> scoreList,String utype) {
        super();
        this.context = context;
        this.scoreList = scoreList;
        this.utype = utype;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }


    @Override
    public int getCount() {
        if (scoreList!=null && scoreList.size() > 0) {
            return scoreList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.all_comment_item, null);
            holder = new ViewHolder();

            holder.avatar_iv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            holder.nick_name_tv = (TextView) convertView.findViewById(R.id.nick_name_tv);
            holder.comment_time_tv = (TextView) convertView.findViewById(R.id.comment_time_tv);
            holder.accord_tip_tv = (TextView) convertView.findViewById(R.id.accord_tip_tv);
            holder.accord_rating = (RatingBar) convertView.findViewById(R.id.accord_rating);
            holder.accord_tv = (TextView) convertView.findViewById(R.id.accord_tv);
            holder.experience_tip_tv = (TextView) convertView.findViewById(R.id.experience_tip_tv);
            holder.experience_rating = (RatingBar) convertView.findViewById(R.id.experience_rating);
            holder.experience_tv = (TextView) convertView.findViewById(R.id.experience_tv);
            holder.coordinate_tip_tv = (TextView) convertView.findViewById(R.id.coordinate_tip_tv);
            holder.coordinate_rating = (RatingBar) convertView.findViewById(R.id.coordinate_rating);
            holder.coordinate_tv = (TextView) convertView.findViewById(R.id.coordinate_tv);
            holder.comment_tv = (TextView) convertView.findViewById(R.id.comment_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScoreListVo scoreListVo = scoreList.get(i);

        String userIcon = scoreListVo.getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.avatar_iv, false);
        }
        holder.nick_name_tv.setText(scoreListVo.getUsernick());
        long time = scoreListVo.getCreateTime();
        holder.comment_time_tv.setText(new DateTime(time).toDateTimeString());

        if (Constants.CARRE_MODEL.equals(utype) || utype.equals(Constants.PRO_MODEL)) {
            holder.accord_tip_tv.setText(context.getString(R.string.model_accord));
            holder.accord_rating.setRating(scoreListVo.getPicLevel());
            holder.accord_tv.setText(StringUtils.getRatingComment(scoreListVo.getPicLevel()));
            holder.experience_tip_tv.setText(context.getString(R.string.model_experience));
            holder.experience_rating.setRating(scoreListVo.getExperienceLevel());
            holder.experience_tv.setText(StringUtils.getRatingComment(scoreListVo.getExperienceLevel()));
            holder.coordinate_tip_tv.setText(context.getString(R.string.model_coordinate));
            holder.coordinate_rating.setRating(scoreListVo.getCoordinateLevel());
            holder.coordinate_tv.setText(StringUtils.getRatingComment(scoreListVo.getCoordinateLevel()));
        }else if(utype.equals(Constants.CARRE_PHOTOR)){
            holder.accord_tip_tv.setText(context.getString(R.string.cm_accord));
            holder.accord_rating.setRating(scoreListVo.getNoticeLevel());
            holder.accord_tv.setText(StringUtils.getRatingComment(scoreListVo.getNoticeLevel()));
            holder.experience_tip_tv.setText(context.getString(R.string.cm_experience));
            holder.experience_rating.setRating(scoreListVo.getPgExperLevel());
            holder.experience_tv.setText(StringUtils.getRatingComment(scoreListVo.getPgExperLevel()));
            holder.coordinate_tip_tv.setText(context.getString(R.string.cm_coordinate));
            holder.coordinate_rating.setRating(scoreListVo.getPayLevel());
            holder.coordinate_tv.setText(StringUtils.getRatingComment(scoreListVo.getPayLevel()));
        }


        if (!StringUtils.isEmpty(scoreListVo.getEvalContent())){
            holder.comment_tv.setText(scoreListVo.getEvalContent());
        }


        return convertView;
    }




    private class ViewHolder {
        private ImageView avatar_iv;
        private TextView nick_name_tv;
        private TextView comment_time_tv;

        private TextView accord_tip_tv;
        private RatingBar accord_rating;
        private TextView accord_tv;

        private TextView experience_tip_tv;
        private RatingBar experience_rating;
        private TextView experience_tv;

        private TextView coordinate_tip_tv;
        private RatingBar coordinate_rating;
        private TextView coordinate_tv;

        private TextView comment_tv;

    }
}

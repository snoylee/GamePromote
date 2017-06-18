package com.xygame.second.sg.personal.blacklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xygame.second.sg.comm.inteface.CancelBlackListListener;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/2.
 */
public class SlideAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<BlackMemberBean> vector;
    private ImageLoader mImageLoader;
    private SlideView.OnSlideListener onSlideListener;
    private CancelBlackListListener cancelBlackListListener;

    public SlideAdapter(Context context, List<BlackMemberBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public void addSlidViewListener(SlideView.OnSlideListener onSlideListener){
        this.onSlideListener=onSlideListener;
    }

    public void addCancelBlackListListener(CancelBlackListListener cancelBlackListListener){
        this.cancelBlackListListener=cancelBlackListListener;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public BlackMemberBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        SlideView slideView = (SlideView) convertView;
        if (null == slideView) {

            View itemView = LayoutInflater.from(context).inflate(
                    R.layout.black_list_item, parent, false);
            slideView = new SlideView(context);
            slideView.setContentView(itemView);
            viewHolder = new ViewHolder(slideView);
            slideView.setOnSlideListener(onSlideListener);
            slideView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) slideView.getTag();
        }
        BlackMemberBean item = vector.get(position);
        item.slideView = slideView;
        item.slideView.shrink();
//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if(position == getPositionForSection(section)){
//            viewHolder.tvLetter.setVisibility(View.VISIBLE);
//            viewHolder.tvLetter.setText(item.getSortLetters());
//        }else{
//            viewHolder.tvLetter.setVisibility(View.GONE);
//        }

        viewHolder.userName.setText(item.getUsernick());
        viewHolder.sexAge.setText(item.getAge());
        String sexStr = item.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }
        mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        viewHolder.deleteHolder.setOnClickListener(new CancelBlackListAction(item));
        return slideView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<BlackMemberBean> datas) {
        this.vector = datas;
        notifyDataSetChanged();
    }

    public void updateDatas(BlackMemberBean tempActionBean) {
        for (int i=0;i<vector.size();i++){
            if (tempActionBean.getUserId().equals(vector.get(i).getUserId())){
                vector.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = vector.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return vector.get(position).getSortLetters().charAt(0);
    }

    private static class ViewHolder {
        public  CircularImage avatar_iv;
        public  TextView userName, sexAge,tvLetter;
        public  ImageView sexIcon;
        public  View sex_age_bg;
        public  ViewGroup deleteHolder;
        ViewHolder(View view) {

            avatar_iv = (CircularImage) view.findViewById(R.id.avatar_iv);
            sexIcon = (ImageView) view
                    .findViewById(R.id.sexIcon);
            userName = (TextView) view.findViewById(R.id.userName);
            sexAge = (TextView) view.findViewById(R.id.sexAge);
            sex_age_bg = view.findViewById(R.id.sex_age_bg);
            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
            tvLetter=(TextView)view.findViewById(R.id.catalog);

        }
    }

    private class CancelBlackListAction implements View.OnClickListener{
        private BlackMemberBean item;
        public CancelBlackListAction(BlackMemberBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            cancelBlackListListener.cancelBlackListListener(item);
        }
    }
}

package com.xygame.second.sg.xiadan.activity.godlist;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.inteface.CancelGodListListener;
import com.xygame.second.sg.comm.inteface.GodListListener;
import com.xygame.second.sg.comm.inteface.JustClickListener;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBean;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTemp;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupBeanTransfer;
import com.xygame.second.sg.personal.guanzhu.member.GZ_GroupListForGCActivity;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
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
    private GodListListener cancelBlackListListener;

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

    public void addCancelBlackListListener(GodListListener cancelBlackListListener){
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
        BlackMemberBean item = vector.get(position);
        ViewHolder viewHolder = null;
        SlideView slideView = (SlideView) convertView;
        if (null == slideView) {

            View itemView = LayoutInflater.from(context).inflate(
                    R.layout.god_list_item, parent, false);
            slideView = new SlideView(context);
            slideView.setContentView(itemView);
            viewHolder = new ViewHolder(slideView);
            slideView.setOnSlideListener(onSlideListener);
            slideView.addClickListener(new AddClickListener(item));
            slideView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) slideView.getTag();
        }
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
        if (!TextUtils.isEmpty(item.getOrderNums())&&!"null".equals(item.getOrderNums())){
            viewHolder.orderNums.setText("接单".concat(item.getOrderNums()).concat("次"));
        }else{
            viewHolder.orderNums.setText("接单0次");
        }
        viewHolder.playVideo.setVisibility(View.VISIBLE);
        viewHolder.playVideo.setOnClickListener(new PlayVideoAction(item));
        viewHolder.playVoice.setVisibility(View.VISIBLE);
        viewHolder.playVoice.setOnClickListener(new PlayVoiceAction(item));
//        if (!TextUtils.isEmpty(item.getVideoUrl())&&!"null".equals(item.getVideoUrl())){
//            viewHolder.playVideo.setVisibility(View.VISIBLE);
//            viewHolder.playVideo.setOnClickListener(new PlayVideoAction(item));
//        }else{
//            viewHolder.playVideo.setVisibility(View.GONE);
//        }
//        if (!TextUtils.isEmpty(item.getVoiceUrl())&&!"null".equals(item.getVoiceUrl())){
//            viewHolder.playVoice.setVisibility(View.VISIBLE);
//            viewHolder.playVoice.setOnClickListener(new PlayVoiceAction(item));
//        }else{
//            viewHolder.playVoice.setVisibility(View.GONE);
//        }
//        List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
//        if (fuFeiDatas!=null){
//            for (PriceBean t:fuFeiDatas){
//                if (t.getId().equals(item.getPriceId())){
//                    int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
//                    viewHolder.priceValue.setText(String.valueOf(value));
//                    break;
//                }
//            }
//        }
//
//        List<JinPaiBigTypeBean> typeDatas= CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
//        for (JinPaiBigTypeBean it:typeDatas) {
//            if (it.getId().equals(item.getSkillCode())) {
//                List<GodLableBean> typeLable=Constants.getGodLableDatas(it.getSubStr());
//                if (typeLable!=null){
//                    for (GodLableBean its:typeLable){
//                        if (its.getTitleId().equals(item.getSkillTitle())){
//                            viewHolder.userLable.setText(its.getTitleName());
//                            break;
//                        }
//                    }
//                }
//                break;
//            }
//        }

        String sexStr = item.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
            viewHolder.sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            viewHolder.sexIcon.setImageResource(R.drawable.sg_man_light_icon);
            viewHolder.sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
        }
        viewHolder.avatar_iv.setImageResource(R.drawable.ic_launcher);
//        mImageLoader.loadImage(item.getUserIcon(), viewHolder.avatar_iv, true);
        viewHolder.deleteHolder.setOnClickListener(new CancelBlackListAction(item));
//        viewHolder.itemBodyView.setOnClickListener(new IntoDetailAction(item));
        return slideView;
    }

    private class AddClickListener implements JustClickListener {
        private BlackMemberBean item;
        public AddClickListener( BlackMemberBean item ){
            this.item=item;
        }

        @Override
        public void justClickAction() {
            Toast.makeText(context,"kicked",Toast.LENGTH_SHORT).show();
        }
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

    public void addItem(BlackMemberBean blackMemberBean) {
        vector.add(blackMemberBean);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public  CircularImage avatar_iv;
        public  TextView userName, sexAge,tvLetter,priceValue,orderNums,userLable;
        public  ImageView sexIcon;
        public  View sex_age_bg,playVoice,playVideo;
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
            priceValue=(TextView)view.findViewById(R.id.priceValue);
            playVoice=view.findViewById(R.id.playVoice);
            playVideo=view.findViewById(R.id.playVideo);
            orderNums=(TextView)view.findViewById(R.id.orderNums);
            userLable=(TextView)view.findViewById(R.id.userLable);
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

    private class IntoDetailAction implements View.OnClickListener{
        private BlackMemberBean item;
        public IntoDetailAction(BlackMemberBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"kicked",Toast.LENGTH_SHORT).show();
//            cancelBlackListListener.intoDetailAction(item);
        }
    }

    private class PlayVideoAction implements View.OnClickListener{
        private BlackMemberBean item;
        public PlayVideoAction(BlackMemberBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            cancelBlackListListener.playGodVideo(item);
        }
    }

    private class PlayVoiceAction implements View.OnClickListener{
        private BlackMemberBean item;
        public PlayVoiceAction(BlackMemberBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            cancelBlackListListener.playGodVoice(item);
        }
    }
}

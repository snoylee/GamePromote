package com.xygame.sg.activity.notice.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.xygame.sg.R;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.bean.NoticeBaseListVo;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.NoticeListVo;
import com.xygame.sg.activity.notice.bean.NoticePublisher;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.notice.bean.NoticeShootListVo;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.widget.LinearLayoutForListView;
import com.xygame.sg.widget.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2015/11/16.
 */
public class NoticeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    private NoticeListBean noticeList;//通告主体数据

    private List<Boolean> isExpandList = new ArrayList<Boolean>();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .considerExifParams(true)
            .displayer(new SimpleBitmapDisplayer())
            .build();

    /**
     * 标志是否从查看的入口进入（因为此页面有摄影师头像点击可查看摄影师资料，因此要区分是摄影师自己进入资料页面还是从查看入口进入）
     */
    private boolean isQuery = false;

//    public NoticeAdapter(Context context, List<Map> shootTypetList, NoticeListBean noticeList, boolean isQuery) {
//        super();
//        this.context = context;
//        this.shootTypetList = shootTypetList;
//        this.noticeList = noticeList;
//        this.isQuery = isQuery;
//        inflater = LayoutInflater.from(context);
//        imageLoader = new ImageLoader(context);
//        if (noticeList.getNotices() != null && noticeList.getNotices().size() > 0 && shootTypetList != null && shootTypetList.size() > 0) {
//            for (int i = 0;i<noticeList.getNotices().size();i++){
//                isExpandList.add(false);//默认都是折叠状态
//            }
//        }
//    }
    public NoticeAdapter(Context context, NoticeListBean noticeList, boolean isQuery) {
        super();
        this.context = context;
        this.noticeList = noticeList;
        this.isQuery = isQuery;
        inflater = LayoutInflater.from(context);
//        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (noticeList.getNotices() != null && noticeList.getNotices().size() > 0 ) {
            for (int i = 0;i<noticeList.getNotices().size();i++){
                isExpandList.add(false);//默认都是折叠状态
            }
        }

    }

//    public void setDatas(NoticeListBean noticeList, List<Map> shootTypetList) {
//        this.noticeList = noticeList;
//        this.shootTypetList.clear();
//        this.shootTypetList = shootTypetList;
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        if (noticeList!=null && noticeList.getNotices() != null && noticeList.getNotices().size() > 0 ) {
            return noticeList.getNotices().size();
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
            convertView = inflater.inflate(R.layout.notice_list_item, null);
            holder = new ViewHolder();
            holder.notice_list_item_root_ll = (LinearLayout) convertView.findViewById(R.id.notice_list_item_root_ll);
            holder.notice_top_bg_iv = (SelectableRoundedImageView) convertView.findViewById(R.id.notice_top_bg_iv);
            holder.pre_payed_icon_iv = (ImageView) convertView.findViewById(R.id.pre_payed_icon_iv);
            holder.remain_time_tv = (TextView) convertView.findViewById(R.id.remain_time_tv);
            holder.signed_num_tv = (TextView) convertView.findViewById(R.id.signed_num_tv);
            holder.time_progress = (ProgressBar) convertView.findViewById(R.id.time_progress);
            holder.shoot_content_tv = (TextView) convertView.findViewById(R.id.shoot_content_tv);
            holder.time_limit_tv = (TextView) convertView.findViewById(R.id.time_limit_tv);
            holder.address_tv = (TextView) convertView.findViewById(R.id.address_tv);
            holder.cm_num_tv = (TextView) convertView.findViewById(R.id.cm_num_tv);
            holder.message_rl = (RelativeLayout) convertView.findViewById(R.id.message_rl);
            holder.message_tv = (TextView) convertView.findViewById(R.id.message_tv);
            holder.publisher_rl = (RelativeLayout) convertView.findViewById(R.id.publisher_rl);
            holder.cm_avatar_iv = (ImageView) convertView.findViewById(R.id.cm_avatar_iv);
            holder.cm_nick_name_tv = (TextView) convertView.findViewById(R.id.cm_nick_name_tv);
            holder.identy_iv = (ImageView) convertView.findViewById(R.id.identy_iv);
//            holder.request_tip_tv = (RoundTextView) convertView.findViewById(R.id.request_tip_tv);
            holder.request_lv = (LinearLayoutForListView) convertView.findViewById(R.id.request_lv);
            holder.expand_arrow_ll = (LinearLayout) convertView.findViewById(R.id.expand_arrow_ll);
            holder.collapse_arrow_ll = (LinearLayout) convertView.findViewById(R.id.collapse_arrow_ll);
            holder.styleText = (RoundTextView) convertView.findViewById(R.id.styleText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        if (isQuery){
//            holder.publisher_rl.setVisibility(View.GONE);

//            FrameLayout.LayoutParams layoutParamsLl = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            if (i ==0){
//                layoutParamsLl.setMargins(0, DataTools.dip2px(context, 20), 0, 0);//4个参数按顺序分别是左上右下
//                holder.notice_list_item_root_ll.setLayoutParams(layoutParamsLl);
//            } else {
//                layoutParamsLl.setMargins(0, DataTools.dip2px(context, 10), 0, 0);//4个参数按顺序分别是左上右下
//            }
//            holder.notice_list_item_root_ll.setLayoutParams(layoutParamsLl);
//        }

        if (!isQuery){

            FrameLayout.LayoutParams layoutParamsLl = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i ==0){
                layoutParamsLl.setMargins(0, DataTools.dip2px(context, 20), 0, 0);//4个参数按顺序分别是左上右下
                holder.notice_list_item_root_ll.setLayoutParams(layoutParamsLl);
            } else {
                layoutParamsLl.setMargins(0, DataTools.dip2px(context, 10), 0, 0);//4个参数按顺序分别是左上右下
            }
            holder.notice_list_item_root_ll.setLayoutParams(layoutParamsLl);
        }

        NoticeListVo noticeListVo = noticeList.getNotices().get(i);

        final NoticeBaseListVo noticeBaseListVo = noticeListVo.getBase();
        //是否预付
        if (noticeListVo.getPrepayStatus() == 3) {
            holder.pre_payed_icon_iv.setVisibility(View.VISIBLE);
        } else {
            holder.pre_payed_icon_iv.setVisibility(View.GONE);
        }
        String bgUrl = "",typeStr="";
        noticeBaseListVo.setShootType(StringUtil.typeIdChange(noticeBaseListVo.getShootType()));
        List<ShootTypeBean>  typeList=SGApplication.getInstance().getTypeList();
        if (typeList==null){
            typeList=new ArrayList<>();
        }
        for (ShootTypeBean bean :typeList ) {
            if(bean.getTypeId()==noticeBaseListVo.getShootType()){
                bgUrl = bean.getNoticeListBg();
                typeStr=bean.getTypeName();
                break;
            }
        }
        if (!StringUtils.isEmpty(typeStr)) {
            holder.styleText.setText(typeStr);
            holder.styleText.setTextColor(context.getResources().getColor(R.color.dark_green));
            holder.styleText.getDelegate().setStrokeColor(context.getResources().getColor(R.color.dark_green));
        }
        if (!StringUtils.isEmpty(bgUrl)) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(bgUrl, holder.notice_top_bg_iv,options);
        }else{
            holder.notice_top_bg_iv.setImageResource(R.drawable.notice_top_bg);
//            ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
//                    imageUrl, viewHolder.image );
        }
        //截止时间
        Date joinStartTime = noticeBaseListVo.getJoinStartTime();
        Date joinEndTime = noticeBaseListVo.getJoinEndTime();
        int max = (int)((joinEndTime.getTime()-joinStartTime.getTime())/(1000*60));
        holder.time_progress.setMax(max);
        long curTime = System.currentTimeMillis();
        int progress = 0;
        String remainTimeStr = "报名截止：已截止";
        if ((joinEndTime.getTime()-curTime)>0){
            remainTimeStr = "报名截止："+DateTime.getLimitTimeStr(joinEndTime.getTime() - curTime);
            progress = (int)((joinEndTime.getTime()-curTime)/(1000*60));
        }
        holder.remain_time_tv.setText(remainTimeStr);
        holder.time_progress.setProgress(progress);

        if(noticeBaseListVo.getPgrapherCount()>0){
            holder.cm_num_tv.setText(noticeBaseListVo.getPgrapherCount() + "位摄影师");
        } else {
            holder.cm_num_tv.setVisibility(View.GONE);
        }

        //已报名人数
        holder.signed_num_tv.setText(noticeListVo.getJoin().getMemCount() + "人");
        //拍摄主题
        holder.shoot_content_tv.setText(noticeBaseListVo.getSubject());
        if (!StringUtils.isEmpty(noticeBaseListVo.getRemark())){
            holder.message_tv.setText(noticeBaseListVo.getRemark());
        } else {
            holder.message_rl.setVisibility(View.GONE);
        }
        final NoticePublisher publisher = noticeBaseListVo.getPublisher();
        holder.cm_nick_name_tv.setText(publisher.getUsernick());
        String cmIcon = publisher.getUserIcon();
        if (!StringUtils.isEmpty(cmIcon)) {
//            imageLoader.loadImage(cmIcon, holder.cm_avatar_iv, true);

            String path;
            if (cmIcon.contains(Constants.ALIY_IMAGE_DMO)){
                cmIcon = cmIcon.replace(Constants.ALIY_IMAGE_DMO, Constants.LOCAL_IMAGE_DMO);
            }
            if (cmIcon.contains(Constants.WEB_IMAGE_DMO)){
                path=cmIcon.concat(Constants.WEB_SMALL_IMAGE_DMO);
            }else{
                path=cmIcon.concat(Constants.LOCAL_SMAL_IMAGE);
            }
            holder.cm_avatar_iv.setImageResource(R.drawable.moren_icon);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, holder.cm_avatar_iv,options);
        }
        if (publisher.getAuthStatus() == 2) {
            holder.identy_iv.setVisibility(View.VISIBLE);
        } else {
            holder.identy_iv.setVisibility(View.GONE);
        }

        NoticeShootListVo noticeShootListVo = noticeListVo.getShoot();
        //开始时间结束时间
        String startTimeStr = new DateTime(noticeShootListVo.getStartTime()).toDateTimeString(DateTime.SLASH_DATE_TIME_HHmm_FORMAT_PATTERN);
        String endTimeStr = new DateTime(noticeShootListVo.getEndTime()).toDateTimeString(DateTime.SLASH_DATE_TIME_HHmm_FORMAT_PATTERN);
        holder.time_limit_tv.setText(startTimeStr + "--" + endTimeStr);
        AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(noticeShootListVo.getAddrCity());
        if (!"null".equals(noticeShootListVo.getAddress())&&noticeShootListVo.getAddress()!=null){
            holder.address_tv.setText(it.getName() + " " + noticeShootListVo.getAddress());
        }else{
            holder.address_tv.setText(it.getName());
        }

        //招募要求
        List<NoticeRecruitVo> recruitList = noticeListVo.getRecruit();

        if (recruitList!= null&& recruitList.size()>0){
            holder.request_lv.bindLinearLayout(recruitList,noticeListVo.isExpand());
            ExpandAndCollapseListener listener = new ExpandAndCollapseListener(holder,i);
            holder.expand_arrow_ll.setOnClickListener(listener);
            holder.collapse_arrow_ll.setOnClickListener(listener);
        }

        if(noticeListVo.isExpand()){//要展开
            holder.expand_arrow_ll.setVisibility(View.GONE);
            holder.collapse_arrow_ll.setVisibility(View.VISIBLE);
        } else {
            holder.expand_arrow_ll.setVisibility(View.VISIBLE);
            holder.collapse_arrow_ll.setVisibility(View.GONE);
        }

        holder.notice_list_item_root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoticeDetailActivity.class);
                intent.putExtra("noticeId",noticeBaseListVo.getNoticeId());
                intent.putExtra("isQuery",isQuery);
                context.startActivity(intent);
            }
        });
        holder.cm_avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CMPersonInfoActivity.class);
                if (UserPreferencesUtil.isOnline(context)&&UserPreferencesUtil.getUserId(context).equals(publisher.getUserId()+"")){
                    intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.EDIT_INFO_FLAG);
                } else {
                    intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
                    intent.putExtra("userId",publisher.getUserId()+"");
                    intent.putExtra("userNick",publisher.getUsernick());
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    class ExpandAndCollapseListener implements View.OnClickListener {
        private ViewHolder holder;
        private int i;
        public ExpandAndCollapseListener(ViewHolder holder,int i) {
            this.holder = holder;
            this.i = i;
        }
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.expand_arrow_ll) {
                noticeList.getNotices().get(i).setIsExpand(true);
            } else if (view.getId() == R.id.collapse_arrow_ll) {
                noticeList.getNotices().get(i).setIsExpand(false);
            }
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        private LinearLayout notice_list_item_root_ll;
//        private ImageView notice_top_bg_iv;
        private SelectableRoundedImageView notice_top_bg_iv;
        private ImageView pre_payed_icon_iv;
        private TextView remain_time_tv;
        private TextView signed_num_tv;
        private ProgressBar time_progress;
        private TextView shoot_content_tv;
        private TextView time_limit_tv;
        private TextView address_tv;
        private TextView cm_num_tv;
        private RelativeLayout message_rl;
        private TextView message_tv;
        private RelativeLayout publisher_rl;
        private ImageView cm_avatar_iv;
        private TextView cm_nick_name_tv;
        private ImageView identy_iv;
//        private RoundTextView request_tip_tv;
        private LinearLayoutForListView request_lv;
        private LinearLayout expand_arrow_ll;
        private LinearLayout collapse_arrow_ll;
        private RoundTextView styleText;
    }
}

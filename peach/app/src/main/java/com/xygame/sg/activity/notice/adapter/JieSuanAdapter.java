package com.xygame.sg.activity.notice.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.CommentActivity;
import com.xygame.sg.activity.notice.NoticeJieSuanActivity;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by xy on 2015/11/16.
 */
public class JieSuanAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<NoticeStatusBean> datas;
    private String status;
    private ImageLoader imageLoader;
    private Context context;
    private String noticeId;
    private String currTime;
    public JieSuanAdapter(Context context, List<NoticeStatusBean> datas, String status, String noticeId) {
        super();
        inflater = LayoutInflater.from(context);
        this.status = status;
        this.context = context;
        this.noticeId = noticeId;
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (datas == null) {
            this.datas = new ArrayList<NoticeStatusBean>();
        } else {
            this.datas = datas;
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public NoticeStatusBean getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clearDatas() {
        datas.clear();
    }

    public void setNoticeCommentStatus(String userId) {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getUserId().equals(userId)) {
                datas.get(i).setPrise(true);
            }
        }
        notifyDataSetChanged();
    }

    public void addDatas(List<NoticeStatusBean> its,String currTime) {
        this.currTime=currTime;
        datas.addAll(its);
        notifyDataSetChanged();
    }

    public void addItem(NoticeStatusBean item) {
        datas.add(item);
        notifyDataSetChanged();
    }

    public NoticeStatusBean removeDaiToYi(String userId) {
        NoticeStatusBean item = null;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getUserId().equals(userId)) {
                item = datas.get(i);
                item.setPrise(false);
                datas.remove(i);
            }
        }
        return item;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.jiesuan_item, null);
            holder = new ViewHolder();
            holder.userImage = (CircularImage) convertView.findViewById(R.id.userImage);
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.zhaomuTxt = (TextView) convertView.findViewById(R.id.zhaomuTxt);
            holder.signTime = (TextView) convertView.findViewById(R.id.signTime);
            holder.daiPrice = (TextView) convertView.findViewById(R.id.daiPrice);
            holder.priceOral = (TextView) convertView.findViewById(R.id.priceOral);
            holder.buttonTip = (TextView) convertView.findViewById(R.id.buttonTip);
            holder.jieSuanView = convertView.findViewById(R.id.jieSuanView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NoticeStatusBean item = datas.get(i);
        holder.userName.setText(item.getUserNick());
        int j = Integer.parseInt(item.getOrders());
        String[] numArray = Constants.CHARACTE_NUMS;
        if (j > numArray.length - 1) {
            holder.zhaomuTxt.setText("招募 " + (j + 1));
        } else {
            holder.zhaomuTxt.setText("招募".concat(numArray[j]));
        }
        holder.daiPrice.setText("￥".concat(String.valueOf(StringUtil.getPrice(Long.parseLong(item.getReward())))));
        if ("1".equals(status)) {
            if (!"null".equals(item.getEndTime())) {
                holder.signTime.setVisibility(View.VISIBLE);
                String timerLeft=CalendarUtils.getLeftTimeDistanceForJieSuan(Long.parseLong(item.getEndTime()), Long.parseLong(currTime));

                if (timerLeft.contains("-")){
                    holder.signTime.setText("系统正在结算处理中");
                }else{
                    holder.signTime.setText(
                            "对方确认拍摄完成，还剩".concat(timerLeft).concat("自动结算"));
                }
            } else {
                holder.signTime.setVisibility(View.GONE);
            }
            holder.buttonTip.setText("结算付款");
            holder.priceOral.setText("待结算：");
            holder.jieSuanView.setOnClickListener(new intoJieSuan(item));
        } else if ("2".equals(status)) {
            if (!"null".equals(item.getEndTime())) {
                holder.signTime.setText("结算时间：".concat(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getFinishTime()))));
            } else {
                holder.signTime.setText("结算时间：");
            }
            holder.priceOral.setText("已结算：");
            if (item.isPrise()) {
                holder.buttonTip.setText("查看评价");
                holder.jieSuanView.setOnClickListener(new intoComment(item, "Y"));
            } else {
                holder.buttonTip.setText("评价");
                holder.jieSuanView.setOnClickListener(new intoComment(item, "N"));
            }
        }
        imageLoader.loadImage(item.getUserIcon(), holder.userImage, true);
        return convertView;
    }

    class intoComment implements OnClickListener {
        private NoticeStatusBean item;
        private String commentFlag;

        public intoComment(NoticeStatusBean item, String commentFlag) {
            // TODO Auto-generated constructor stub
            this.item = item;
            this.commentFlag = commentFlag;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            intoCommentPage(item, commentFlag);
        }
    }

    private void intoCommentPage(NoticeStatusBean item, String commentFlag) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("bean", item);
        intent.putExtra("noticeId", noticeId);
        intent.putExtra("commentFlag", commentFlag);
        context.startActivity(intent);
    }

    class intoJieSuan implements OnClickListener {
        private NoticeStatusBean item;

        public intoJieSuan(NoticeStatusBean item) {
            // TODO Auto-generated constructor stub
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            intoJieSuanPage(item);
        }
    }

    private void intoJieSuanPage(NoticeStatusBean item) {
        Intent intent = new Intent(context, NoticeJieSuanActivity.class);
        intent.putExtra("noticeId", noticeId);
        intent.putExtra("bean", item);
        context.startActivity(intent);
    }

    private class ViewHolder {
        private CircularImage userImage;
        private TextView userName, zhaomuTxt, signTime, daiPrice, priceOral, buttonTip;
        private View jieSuanView;
    }
}

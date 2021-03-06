package com.xygame.sg.im;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.TimeUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private List<SGNewsBean> strList;
    private Context context;
    private boolean isDelete;
    private ImageLoader imageLoader;

    public NewsAdapter(Context context, List<SGNewsBean> strList, boolean isDelete) {
        super();
        this.context = context;
        this.isDelete = isDelete;
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (strList == null) {
            this.strList = new ArrayList<>();
        } else {
            this.strList = strList;
        }
    }

    public void changeListEditor(boolean isDelete) {
        this.isDelete = isDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return strList.size();
    }

    @Override
    public SGNewsBean getItem(int arg0) {
        // TODO Auto-generated method stub
        return strList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void clearDatas() {
        strList.clear();
    }

    public void addDatas(List<SGNewsBean> datas) {
        this.strList = datas;
        notifyDataSetChanged();
    }

    /**
     * 初始化View
     */
    private class ViewHolder {
        private CircularImage userImage;
        private ImageView deleteIcon;
        private TextView userName, nuReadNews, timerText, contentText;
    }

    /**
     * 添加数据
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sg_news_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.userName = (TextView) convertView
                    .findViewById(R.id.userName);
            viewHolder.nuReadNews = (TextView) convertView
                    .findViewById(R.id.nuReadNews);
            viewHolder.timerText = (TextView) convertView
                    .findViewById(R.id.timerText);
            viewHolder.userImage = (CircularImage) convertView
                    .findViewById(R.id.userImage);
            viewHolder.deleteIcon = (ImageView) convertView.findViewById(R.id.deleteIcon);
            viewHolder.contentText = (TextView) convertView
                    .findViewById(R.id.contentText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SGNewsBean item = strList.get(position);
        if (Constants.NEWS_CHAT.equals(item.getNewType())) {
            if (isDelete) {
                if (Constants.NEWS_CHAT.equals(item.getNewType())) {
                    viewHolder.deleteIcon.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.deleteIcon.setVisibility(View.GONE);
                }
            } else {
                viewHolder.deleteIcon.setVisibility(View.GONE);
            }

            if (Constants.NEWS_UNREAD.equals(item.getMessageStatus())) {
                viewHolder.nuReadNews.setVisibility(View.VISIBLE);
                viewHolder.nuReadNews.setText(String.valueOf(NewsEngine.quaryUnReadChatNewsByRecruitLocIndex(context, item, UserPreferencesUtil.getUserId(context))));
            } else {
                viewHolder.nuReadNews.setVisibility(View.GONE);
            }

            if (Constants.SEND_TEXT.equals(item.getType())) {
                viewHolder.contentText.setText(item.getMsgContent());
            } else if (Constants.SEND_SOUND.equals(item.getType())) {
                viewHolder.contentText.setText("语音");
            } else if (Constants.SEND_IMAGE.equals(item.getType())) {
                viewHolder.contentText.setText("图片");
            } else if (Constants.SEND_LOCATION.equals(item.getType())) {
                viewHolder.contentText.setText("地理位置");
            } else if (Constants.SEND_VIDEO.equals(item.getType())) {
                viewHolder.contentText.setText("视频");
            }

            viewHolder.userName.setText(item.getFriendNickName());

            viewHolder.timerText.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getTimestamp())));
            imageLoader.loadImage(item.getFriendUserIcon(), viewHolder.userImage, true);
            viewHolder.deleteIcon.setOnClickListener(new HideMessage(item, position));
            convertView.setOnClickListener(new IntoChatWindow(item));
        } else if (Constants.GROUP_CHAT.equals(item.getNewType())) {
            if (isDelete) {
                if (Constants.GROUP_CHAT.equals(item.getNewType())) {
                    viewHolder.deleteIcon.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.deleteIcon.setVisibility(View.GONE);
                }
            } else {
                viewHolder.deleteIcon.setVisibility(View.GONE);
            }

            if (Constants.NEWS_UNREAD.equals(item.getMessageStatus())) {
                viewHolder.nuReadNews.setVisibility(View.VISIBLE);
                viewHolder.nuReadNews.setText(String.valueOf(TempGroupNewsEngine.quaryUnReadChatNewsByRecruitLocIndex(context, item, UserPreferencesUtil.getUserId(context))));
            } else {
                viewHolder.nuReadNews.setVisibility(View.GONE);
            }

            if (Constants.SEND_TEXT.equals(item.getType())) {
                viewHolder.contentText.setText(item.getMsgContent());
            } else if (Constants.SEND_SOUND.equals(item.getType())) {
                viewHolder.contentText.setText("语音");
            } else if (Constants.SEND_IMAGE.equals(item.getType())) {
                viewHolder.contentText.setText("图片");
            } else if (Constants.SEND_LOCATION.equals(item.getType())) {
                viewHolder.contentText.setText("地理位置");
            } else if (Constants.SEND_VIDEO.equals(item.getType())) {
                viewHolder.contentText.setText("视频");
            } else if (Constants.SEND_TEXT_TIP.equals(item.getType())) {
                viewHolder.contentText.setText(item.getMsgContent());
            }

            viewHolder.userName.setText(item.getNoticeSubject());

            viewHolder.timerText.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getTimestamp())));
            viewHolder.userImage.setImageResource(R.drawable.qunzuda);
//			imageLoader.loadImage(item.getFriendUserIcon(), viewHolder.userImage, true);
            viewHolder.deleteIcon.setOnClickListener(new HideGroupMessage(item, position));
            convertView.setOnClickListener(new IntoGroupChatWindow(item));
        } else if (Constants.NEWS_DYNAMIC.equals(item.getNewType())) {
            if (isDelete) {
                viewHolder.deleteIcon.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.deleteIcon.setVisibility(View.GONE);
            }
//			if (item.getNoticeId()!=null&&!"null".equals(item.getNoticeId())&&!"".equals(item.getRecruitId())){
//				viewHolder.contentText.setText(item.getNoticeId().concat(item.getMsgContent()));
//			}else{
//				viewHolder.contentText.setText(item.getMsgContent());
//			}
            viewHolder.contentText.setText(item.getMsgContent());
            viewHolder.timerText.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getTimestamp())));
//			viewHolder.timerText.setText(TimeUtils.formatTime(Long.parseLong(item.getTimestamp())));

            if (Constants.TARGET_QIANG_PUSH.equals(item.getType())) {
                viewHolder.userName.setText("抢单中心");
                viewHolder.userImage.setImageResource(R.drawable.news_qiang);
                convertView.setOnClickListener(new IntoGodQiangDynimicNews());
                if (Constants.NEWS_UNREAD.equals(item.getMessageStatus())) {
                    viewHolder.nuReadNews.setVisibility(View.VISIBLE);
                    viewHolder.nuReadNews.setText(String.valueOf(NewsGodEngine.quaryUnReadDaymicNews(context, UserPreferencesUtil.getUserId(context))));
                } else {
                    viewHolder.nuReadNews.setVisibility(View.GONE);
                }
            } else {
                viewHolder.userName.setText("蜜桃社区");
                viewHolder.userImage.setImageResource(R.drawable.new_system_icon);
                convertView.setOnClickListener(new IntoDynimicNews());
                if (Constants.NEWS_UNREAD.equals(item.getMessageStatus())) {
                    viewHolder.nuReadNews.setVisibility(View.VISIBLE);
                    viewHolder.nuReadNews.setText(String.valueOf(NewsEngine.quaryUnReadDaymicNews(context, UserPreferencesUtil.getUserId(context))));
                } else {
                    viewHolder.nuReadNews.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    private class IntoGodQiangDynimicNews implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            IntoDynimicNewsActionQiang();
        }
    }

    private void IntoDynimicNewsActionQiang() {
        Intent intent = new Intent(context, ShowQiangDynamicActivity.class);
        context.startActivity(intent);
    }

    private class IntoDynimicNews implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            IntoDynimicNewsAction();
        }
    }

    private void IntoDynimicNewsAction() {
        Intent intent = new Intent(context, ShowDynamicActivity.class);
        context.startActivity(intent);
    }

    private class IntoSystemNews implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            IntoSystemNewsAction();
        }
    }

    private void IntoSystemNewsAction() {
        Intent intent = new Intent(context, ShowSystemNewsActivity.class);
        context.startActivity(intent);
    }

    private class HideMessage implements View.OnClickListener {
        private SGNewsBean item;
        private int index;

        public HideMessage(SGNewsBean item, int index) {
            this.item = item;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            setHideNew(item, index);
        }
    }

    private void setHideNew(SGNewsBean item, int index) {
        item.setIsShow(Constants.NEWS_HIDE);
        NewsEngine.updateHideOrShowChatItem(context, item, UserPreferencesUtil.getUserId(context));
        strList.remove(index);
        notifyDataSetChanged();
    }

    private class HideGroupMessage implements View.OnClickListener {
        private SGNewsBean item;
        private int index;

        public HideGroupMessage(SGNewsBean item, int index) {
            this.item = item;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            setHideGroupNew(item, index);
        }
    }

    private void setHideGroupNew(SGNewsBean item, int index) {
        item.setIsShow(Constants.NEWS_HIDE);
        TempGroupNewsEngine.updateHideOrShowChatItem(context, item, UserPreferencesUtil.getUserId(context));
        strList.remove(index);
        notifyDataSetChanged();
    }

    private class IntoChatWindow implements View.OnClickListener {
        private SGNewsBean item;

        public IntoChatWindow(SGNewsBean item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            openChatWindow(item);
        }
    }

    private void openChatWindow(SGNewsBean item) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("bean", item);
        context.startActivity(intent);
    }

    private class IntoGroupChatWindow implements View.OnClickListener {
        private SGNewsBean item;

        public IntoGroupChatWindow(SGNewsBean item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            openGroupChatWindow(item);
        }
    }

    private void openGroupChatWindow(SGNewsBean item) {
        Intent intent = new Intent(context, TempGroupChatActivity.class);
        intent.putExtra("chatBean", item);
        context.startActivity(intent);
    }
}

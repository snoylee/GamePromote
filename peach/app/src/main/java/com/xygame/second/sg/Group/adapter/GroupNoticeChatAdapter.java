package com.xygame.second.sg.Group.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.second.sg.Group.bean.GroupNoticeMessageBean;
import com.xygame.second.sg.comm.inteface.ResendMsgListener;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.TimeUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/7.
 */
public class GroupNoticeChatAdapter extends BaseAdapter {
    private List<GroupNoticeMessageBean> datas;
    private Context context;
    private ImageLoader mImageLoader;
    private ResendMsgListener resendMsgListener;

    public void addResendListener(ResendMsgListener resendMsgListener){
        this.resendMsgListener=resendMsgListener;
    }

    public GroupNoticeChatAdapter(Context context, List<GroupNoticeMessageBean> datas) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (datas != null) {
            this.datas = datas;
        } else {
            this.datas = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public GroupNoticeMessageBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearDatas() {
        datas.clear();
    }

    public void addDatas(List<GroupNoticeMessageBean> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addItem(GroupNoticeMessageBean it) {
        datas.add(it);
        notifyDataSetChanged();
    }

    public void updateItemSendStatus(GroupNoticeMessageBean it) {
        for (int i = 0; i < datas.size(); i++) {
            if (it.getMsgTimer().equals(datas.get(i).getMsgTimer())) {
                datas.get(i).setMsgStatus(it.getMsgStatus());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupNoticeMessageBean message = datas.get(position);
        String fromUser = message.getSendUserId();
        String fromUserId = null, fromUserIcon = null, fromUserNick = null;
        try {
            JSONObject object = new JSONObject(fromUser);
            fromUserId = StringUtils.getJsonValue(object, "userId");
            fromUserIcon = StringUtils.getJsonValue(object, "userIcon");
            fromUserNick = StringUtils.getJsonValue(object, "usernick");
        } catch (Exception e) {
            e.printStackTrace();
        }
        View sendFaith = null;
        String myUserId = UserPreferencesUtil.getUserId(context);
        if (!myUserId.equals(fromUserId)) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_notice_chat_in, null);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_notice_chat_out, null);
            sendFaith = convertView.findViewById(R.id.sendFaith);
        }
        TextView noticeTitle = (TextView) convertView.findViewById(R.id.noticeTitle);
        TextView noticeTimer = (TextView) convertView.findViewById(R.id.noticeTimer);
        TextView noticeAddress = (TextView) convertView.findViewById(R.id.noticeAddress);
        TextView noticePrice = (TextView) convertView.findViewById(R.id.noticePrice);
        TextView noticeOral = (TextView) convertView.findViewById(R.id.noticeOral);
        View noticeView = convertView.findViewById(R.id.noticeView);

        CircularImage from_head = (CircularImage) convertView.findViewById(R.id.from_head);
        TextView dateView = (TextView) convertView.findViewById(R.id.formclient_row_date);
        TextView msgView = (TextView) convertView.findViewById(R.id.formclient_row_msg);
        TextView messageTimer = (TextView) convertView.findViewById(R.id.messageTimer);

        dateView.setVisibility(View.GONE);
        messageTimer.setText(TimeUtils.formatTime(Long.parseLong(message.getMsgTimer())));
        if (!UserPreferencesUtil.getUserId(context).equals(fromUserId)) {
            from_head.setImageResource(R.drawable.default_avatar);
            if (StringUtil.notEmpty(fromUserIcon)) {
                mImageLoader.loadImage(fromUserIcon, from_head, true);
            }
        } else {
            if (sendFaith != null) {
                if ("2".equals(message.getMsgStatus())) {
                    sendFaith.setVisibility(View.VISIBLE);
                    sendFaith.setOnClickListener(new ResentMessageListener(message));
                } else if (Constants.NEWS_SEND_SCUESS.equals(message.getMsgStatus())) {
                    sendFaith.setVisibility(View.GONE);
                }
            }

            from_head.setImageResource(R.drawable.default_avatar);
            mImageLoader.loadImage(UserPreferencesUtil.getHeadPic(context), from_head, true);
        }

        if (Constants.SEND_TEXT.equals(message.getMsgType())) {
            noticeView.setVisibility(View.GONE);
            msgView.setVisibility(View.VISIBLE);
            msgView.setText(message.getMsgContent());
        } else {
            noticeView.setVisibility(View.VISIBLE);
            msgView.setVisibility(View.GONE);
            try {
                JSONObject contentObj=new JSONObject(message.getMsgContent());
                noticeTitle.setText(StringUtils.getJsonValue(contentObj,"title"));
                noticeTimer.setText(StringUtils.getJsonValue(contentObj,"time"));
                noticeAddress.setText(StringUtils.getJsonValue(contentObj,"address"));
                noticePrice.setText(StringUtils.getJsonValue(contentObj,"price"));
                noticeOral.setText(StringUtils.getJsonValue(contentObj,"oral"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        from_head.setOnClickListener(new IntoPersonalDeltail(message));
        return convertView;
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private GroupNoticeMessageBean presenter;
        public IntoPersonalDeltail(GroupNoticeMessageBean presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(GroupNoticeMessageBean message) {
        String fromUser = message.getSendUserId();
        String fromUserId = null, fromUserIcon = null, fromUserNick = null;
        try {
            JSONObject object = new JSONObject(fromUser);
            fromUserId = StringUtils.getJsonValue(object, "userId");
            fromUserNick = StringUtils.getJsonValue(object, "usernick");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String myUserId = UserPreferencesUtil.getUserId(context);
        if (!myUserId.equals(fromUserId)) {
            Intent intent = new Intent(context, PersonalDetailActivity.class);
            intent.putExtra("userNick",fromUserNick);
            intent.putExtra("userId", fromUserId);
            context. startActivity(intent);
        } else {
            Intent intent = new Intent(context, PersonalDetailActivity.class);
            intent.putExtra("userNick", UserPreferencesUtil.getUserNickName(context));
            intent.putExtra("userId",myUserId);
            context. startActivity(intent);
        }

    }

    private class ResentMessageListener implements View.OnClickListener{
        private GroupNoticeMessageBean message;
        public ResentMessageListener(GroupNoticeMessageBean message){
            this.message=message;
        }
        @Override
        public void onClick(View v) {
            resendMsgListener.reSendMessage(message);
        }
    }
}

package com.xygame.sg.activity.notice;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.JJRPublisher;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.CloseShareOptionsView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class JJRNoticeDetailActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton, look_member_tv,isShowView;
    private TextView titleName, signed_num_tv, cm_nick_name_tv, jjrSubject, jjrContent,encroll_tv;
    private TextView rightButtonText;
    private boolean isShowClose = false;
    private ImageView rightbuttonIcon;
    public static final int CLOSE_SHARE_REQUEST = 1;
    private String vidoPath = "", vidoPathUrl = "";
    private CircularImage cm_avatar_iv;
    private String noticeId;
    private JJRNoticeBean item;
    public static final int TO_CLOSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jjr_notice_detail);
        initViews();
        addListener();
        requestData();
    }

    private void initViews() {
        isShowView=findViewById(R.id.isShowView);
        jjrContent = (TextView) findViewById(R.id.jjrContent);
        jjrSubject = (TextView) findViewById(R.id.jjrSubject);
        cm_nick_name_tv = (TextView) findViewById(R.id.cm_nick_name_tv);
        cm_avatar_iv = (CircularImage) findViewById(R.id.cm_avatar_iv);
        look_member_tv = findViewById(R.id.look_member_tv);
        encroll_tv = (TextView) findViewById(R.id.encroll_tv);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        signed_num_tv = (TextView) findViewById(R.id.signed_num_tv);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        titleName.setText(getText(R.string.title_activity_notice_detail));
        rightButtonText.setVisibility(View.VISIBLE);
    }

    private void addListener() {
        look_member_tv.setOnClickListener(this);
        backButton.setOnClickListener(this);
        rightbuttonIcon.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        cm_avatar_iv.setOnClickListener(this);
    }

    private void requestData() {
        rightbuttonIcon.setVisibility(View.GONE);
        rightButtonText.setVisibility(View.GONE);
        noticeId = getIntent().getStringExtra("noticeId");
        registerListener();
        loadData();
    }

    private void loadData() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            item.setData(new JSONObject().put("noticeId", noticeId));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_JJR_NOTICE_DETAIL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_JJR_NOTICE_DETAIL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cm_avatar_iv:
                if (item!=null){
                    Intent intent = new Intent(this, CMPersonInfoActivity.class);
                    if (UserPreferencesUtil.isOnline(this)&&UserPreferencesUtil.getUserId(this).equals(item.getPublisher().getUserId()+"")){
                        intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.EDIT_INFO_FLAG);
                    } else {
                        intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
                        intent.putExtra("userId",item.getPublisher().getUserId()+"");
                        intent.putExtra("userNick",item.getPublisher().getUsernick());
                    }
                    startActivity(intent);
                }
                break;
            case R.id.look_member_tv:
                Intent intent1 = new Intent(this, JJRModelsActivity.class);
                intent1.putExtra("item", item);
                startActivity(intent1);
                break;
            case R.id.isShowView:
                if ("0".equals(item.getHasApply())) {
                    if (!UserPreferencesUtil.isOnline(this)) {
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    } else {
                        if (UserPreferencesUtil.getUserType(this).equals(Constants.CARRE_PHOTOR) || UserPreferencesUtil.getUserType(this).equals(Constants.CARRE_MERCHANT)) {
                            Toast.makeText(this, "只有模特可以报名", Toast.LENGTH_SHORT).show();
                        } else {
                            if (UserPreferencesUtil.getUserVerifyStatus(this).equals(Constants.USER_VERIFIED_CODE)) {//认证通过
                                TwoButtonDialog dialog = new TwoButtonDialog(this, "是否确认报名", "确定", "放弃报名", R.style.dineDialog, new ButtonTwoListener() {
                                    @Override
                                    public void confrimListener() {
                                        reportNotice();
                                    }

                                    @Override
                                    public void cancelListener() {
                                    }
                                });
                                dialog.show();
                            } else if (Constants.USER_VERIFING_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))) {//认证中
                                OneButtonDialog dialog = new OneButtonDialog(this, "你的账号正在申请认证，请在认证通过后报名！", R.style.dineDialog, new ButtonOneListener() {
                                    @Override
                                    public void confrimListener(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            } else if (Constants.USER_NO_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))) {//未认证
                                showToCertiDialog("报名前请先进行模特认证");
                            } else if (Constants.USER_VERIFING_REFUSED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))) {//认证被拒绝

                                if (Constants.PRO_MODEL.equals(UserPreferencesUtil.getUserType(this))) {
                                    TwoButtonDialog dialog = new TwoButtonDialog(this, "是否确认报名", "确定", "放弃报名", R.style.dineDialog, new ButtonTwoListener() {
                                        @Override
                                        public void confrimListener() {
                                            reportNotice();
                                        }

                                        @Override
                                        public void cancelListener() {
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    showToCertiDialog("模特认证申请被拒绝，请重新申请！");
                                }
                            }

                        }
                    }
                } else {
                    ToChatBean toChatBean = new ToChatBean();
                    toChatBean.setRecruitLocIndex("-1");
                    toChatBean.setNoticeId(String.valueOf(item.getNoticeId()));
                    toChatBean.setNoticeSubject(item.getSubject());
                    toChatBean.setUserIcon(item.getPublisher().getUserIcon());
                    toChatBean.setUserId(item.getPublisher().getUserId());
                    toChatBean.setUsernick(item.getPublisher().getUsernick());
                    toChatBean.setRecruitId("-1");
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("toChatBean", toChatBean);
                    intent.putExtra("isJJR", true);
                    startActivity(intent);
                }
                break;
            case R.id.rightButtonText:
                Intent intent = new Intent(JJRNoticeDetailActivity.this, ShareBoardView.class);
                intent.putExtra(Constants.SHARE_TITLE_KEY, item.getSubject());
                intent.putExtra(Constants.SHARE_SUBTITLE_KEY, "模范儿" + item.getSubject() + "这个通告不错哦，你们有没有兴趣啊？，反正我报名了！");
                startActivity(intent);
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.rightbuttonIcon:
                if (item != null) {
                    if (!UserPreferencesUtil.isOnline(this)) {
                        Intent intent8 = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent8);
                    } else {
                        if (UserPreferencesUtil.getUserId(this).equals(item.getPublisher().getUserId())) {
                            selectAction();
                        } else {
                            Intent intent2 = new Intent(this, ReportFristActivity.class);
                            intent2.putExtra("resType", Constants.JUBAO_TYPE_TONGGAO);
                            intent2.putExtra("userId", UserPreferencesUtil.getUserId(this));
                            intent2.putExtra("resourceId", noticeId);
                            startActivity(intent2);
                        }
                    }
                } else {
                    if (!UserPreferencesUtil.isOnline(this)) {
                        Intent intent8 = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent8);
                    } else {
                        Intent intent2 = new Intent(this, ReportFristActivity.class);
                        intent2.putExtra("resType", Constants.JUBAO_TYPE_TONGGAO);
                        intent2.putExtra("userId", UserPreferencesUtil.getUserId(this));
                        intent2.putExtra("resourceId", noticeId);
                        startActivity(intent2);
                    }
                }
                break;
        }
    }

    private void showToCertiDialog(String tipStr) {
        TwoButtonDialog dialog = new TwoButtonDialog(this, tipStr, "前往认证", "放弃报名", R.style.dineDialog, new ButtonTwoListener() {
            @Override
            public void confrimListener() {
                Intent intent = new Intent(JJRNoticeDetailActivity.this, ModelIdentyFirstActivity.class);
                startActivity(intent);
            }

            @Override
            public void cancelListener() {
            }
        });
        dialog.show();
    }

    private void reportNotice() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            item.setData(new JSONObject().put("noticeId", noticeId).put("userId", UserPreferencesUtil.getUserId(this)));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this,false);
        item.setServiceURL(ConstTaskTag.QUEST_JJR_NOTICE_REPORT);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_JJR_NOTICE_REPORT);
    }

    private void selectAction() {
        Intent intent = new Intent(JJRNoticeDetailActivity.this, CloseShareOptionsView.class);
        intent.putExtra("isShowClose", isShowClose);
        startActivityForResult(intent, CLOSE_SHARE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CLOSE_SHARE_REQUEST:
                    //添加关闭通告接口
                    Intent intent = new Intent(JJRNoticeDetailActivity.this, CloseJJRNoticeActivity.class);
                    intent.putExtra("noticeId", item.getNoticeId());
                    startActivityForResult(intent, TO_CLOSE);
                    break;
                case TO_CLOSE:
                    boolean flag = data.getBooleanExtra("flag", false);
                    if (flag) {
                        item.setNoticeStatus("5");
                        updateAllViews();
                    }
                    break;
            }
        }
    }

    public void showDialogForRecord() {
        TwoButtonDialog dialog = new TwoButtonDialog(this, "亲，没有视频是不能报名通告的哦~！我们现在去拍视频吧！", "前往拍摄", "取消", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
//                        Intent intent = new Intent(JJRNoticeDetailActivity.this, FFmpegRecorderActivity.class);
//                        startActivity(intent);
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialog.show();
    }

    private void uploadVido() {
        ShowMsgDialog.showNoMsg(this, false);
        AliySSOHepler.getInstance().uploadMedia(this, Constants.AVATAR_PATH, vidoPath, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                vidoPathUrl = imageUrl;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                Message msg1 = new Message();
                msg1.what = 2;
                handler.sendMessage(msg1);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {

            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0:
                        uploadVido();
                        break;
                    case 1:
                        requestVideoService();
                        break;
                    case 2:
                        ShowMsgDialog.cancel();
                        uploadVideoFailth();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public void uploadVideoFailth() {
        TwoButtonDialog dialog = new TwoButtonDialog(this, "视频上传失败，是否继续上传？", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        uploadVido();
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
        dialog.show();
    }

    private void uploadVideoSc() {
        OneButtonDialog dialog = new OneButtonDialog(this, "视频上传成功，您可以报名啦", R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                    }
                });
        dialog.show();
    }

    private void requestVideoService() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", UserPreferencesUtil.getUserId(this));
            JSONArray jsonArray = new JSONArray();
            JSONObject objs = new JSONObject();
            objs.put("videoUrl", vidoPathUrl);
            jsonArray.put(objs);
            obj.put("appendVideos", jsonArray);
            IdentyBean identyBean = getIdentyBean();
            if (identyBean != null) {
                int authStatus = identyBean.getAuthStatus();
                int userType = identyBean.getUserType();
                obj.put("userType", userType);
                obj.put("authStatus", authStatus);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_HEAD_VIDEO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_HEAD_VIDEO_COD);
    }

    private IdentyBean getIdentyBean() {
        IdentyBean identyBean = null;
//        String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(this);
//        if (!StringUtils.isEmpty(jsonStr)) {
//            List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//            if (resultList == null || resultList.size() == 0) {
//                return null;
//            }
//            identyBean = resultList.get(0);
//        }
        return identyBean;
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.RESPOSE_HEAD_VIDEO_COD:
                if ("0000".equals(data.getCode())) {
                    uploadVideoSc();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.RESPOSE_JJR_NOTICE_DETAIL:
                if ("0000".equals(data.getCode())) {
                    resposeJson(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.RESPOSE_JJR_NOTICE_REPORT:
                if ("0000".equals(data.getCode())) {
                    Toast.makeText(this, "报名成功", Toast.LENGTH_SHORT).show();
                    item.setHasApply("2");
                    encroll_tv.setText("聊天");
                } else if ("4011".equals(data.getCode())) {
                    showDialogForRecord();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                if (UserPreferencesUtil.isOnline(JJRNoticeDetailActivity.this)) {
                    loadData();
                }
            } else if (Constants.ACTION_RECORDER_SUCCESS.equals(intent.getAction())) {
                //视频地址
                vidoPath = intent.getStringExtra("path");

                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())){
                // 登录失败或取消登录
                updateAllViews();
            } else if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())){
                updateAllViews();
            }
        }
    };

    public void registerListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_RECORDER_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterListener() {
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        unRegisterListener();
        super.onDestroy();
    }

    private void resposeJson(String datas) {
        try {
            if (!TextUtils.isEmpty(datas)) {
                JSONObject subObj = new JSONObject(datas);
                item = new JJRNoticeBean();
                item.setApplyCount(StringUtils.getJsonValue(subObj, "applyCount"));
                item.setExpand(false);
                item.setHasApply(StringUtils.getJsonValue(subObj, "hasApply"));
                item.setNoticeStatus(StringUtils.getJsonValue(subObj, "noticeStatus"));
                item.setNoticeContent(StringUtils.getJsonValue(subObj, "noticeContent"));
                item.setNoticeId(StringUtils.getJsonValue(subObj, "noticeId"));
                item.setSubject(StringUtils.getJsonValue(subObj, "subject"));
                JSONObject publisherObj = new JSONObject(StringUtils.getJsonValue(subObj, "publisher"));
                JJRPublisher publisher = new JJRPublisher();
                publisher.setAuthStatus(StringUtils.getJsonValue(publisherObj, "authStatus"));
                publisher.setUserIcon(StringUtils.getJsonValue(publisherObj, "userIcon"));
                publisher.setUserId(StringUtils.getJsonValue(publisherObj, "userId"));
                publisher.setUsernick(StringUtils.getJsonValue(publisherObj, "usernick"));
                publisher.setUserType(StringUtils.getJsonValue(publisherObj, "userType"));
                item.setPublisher(publisher);
                updateAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAllViews() {
        if (item.getPublisher().getUserId().equals(UserPreferencesUtil.getUserId(this))) {
            isShowView.setVisibility(View.GONE);
            look_member_tv.setVisibility(View.VISIBLE);
            rightbuttonIcon.setVisibility(View.VISIBLE);
            rightbuttonIcon.setImageResource(R.drawable.more_icon);
            if ("3".equals(item.getNoticeStatus())) {
                isShowClose = true;
                if ("0".equals(item.getHasApply())) {
                    encroll_tv.setText("我要报名");
                } else {
                    encroll_tv.setText("聊天");
                }
                isShowView.setOnClickListener(this);
//                encroll_tv.setTextColor(getResources().getColor(R.color.dark_green));
            } else {
                isShowClose = false;
//                encroll_tv.setTextColor(getResources().getColor(R.color.dark_gray));
            }
        } else {
            rightbuttonIcon.setVisibility(View.VISIBLE);
            rightbuttonIcon.setImageResource(R.drawable.title_refuse_icon);
            isShowView.setVisibility(View.VISIBLE);
            look_member_tv.setVisibility(View.GONE);
            if ("3".equals(item.getNoticeStatus())) {
                isShowClose = true;
                if ("0".equals(item.getHasApply())) {
                    encroll_tv.setText("我要报名");
                } else {
                    encroll_tv.setText("聊天");
                }
                isShowView.setOnClickListener(this);
//                encroll_tv.setTextColor(getResources().getColor(R.color.dark_green));
            } else {
                isShowClose = false;
//                encroll_tv.setTextColor(getResources().getColor(R.color.dark_gray));
            }
        }
        rightButtonText.setVisibility(View.VISIBLE);
        signed_num_tv.setText(item.getApplyCount().concat("人"));
        cm_avatar_iv.setImageResource(R.drawable.default_avatar);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage(item.getPublisher().getUserIcon(), cm_avatar_iv);
        cm_nick_name_tv.setText(item.getPublisher().getUsernick());
        jjrSubject.setText(item.getSubject());
        jjrContent.setText(item.getNoticeContent());
    }
}

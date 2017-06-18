package com.xygame.sg.activity.notice;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.notice.adapter.RefWorkAdapter;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.notice.bean.NoticeReferPicVo;
import com.xygame.sg.activity.notice.bean.NoticeShootVo;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.define.view.CloseShareOptionsView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.notice.CollectNotices;
import com.xygame.sg.task.notice.EnrollNotices;
import com.xygame.sg.task.notice.QueryNoticeDeatail;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.RecycleBitmap;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.LinearLayoutForListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class NoticeDetailActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private TextView rightButtonText;
    private ImageView rightbuttonIcon;

    private ScrollView detail_root_sc;
    private ImageView notice_top_bg_iv;
    private ImageView pre_payed_icon_iv;
    private ImageView collect_tip_iv;
    private TextView collect_tip_tv;
    private TextView remain_time_tv;
    private TextView signed_num_tv;
    private ProgressBar time_progress;
    private TextView shoot_content_tv;
    private TextView time_limit_tv;
    private TextView cm_num_tv;
    private TextView address_tv;
    private RelativeLayout message_rl;
    private TextView message_tv;
    private ImageView cm_avatar_iv;
    private TextView cm_nick_name_tv;
    private ImageView identy_iv;

//    private ListView request_lv;

    private LinearLayout request_ll;
    private GridView ref_work_gv;

    VisitUnit visitUnit = new VisitUnit(this);

    private String noticeId = "";
    private String vidoPath = "",vidoPathUrl="";
    private String noticeBgUrl = "";

    private NoticeDetailVo notice;
    private ImageLoader imageLoader;

    private int collectStatus = 0;//状态(1:取消收藏；2：收藏)
    private int ifColect = 0;//是否已收藏：//1:是;0:否
    private int ifEnrolled = 0;//是否已报名：//1:是;0:否

    private boolean isQuery = false,isLoadData=true;
    public static final int CLOSE_SHARE_REQUEST = 1;
    public static final int TO_CLOSE = 2;
    private RelativeLayout new_msg_rl;
    private LinearLayout ref_work_ll;
    private  boolean isShowClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_notice_detail, null));
        registerListener();//监听登录
        noticeId = getIntent().getStringExtra("noticeId");
        if (getIntent().hasExtra("noticeBgUrl")){
            noticeBgUrl = getIntent().getStringExtra("noticeBgUrl");//可以不用传递
        }
        isQuery= getIntent().getBooleanExtra("isQuery",false);
        initViews();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoadData){
            requestData();
        }else{
            isLoadData=true;
        }
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        new_msg_rl = (RelativeLayout) findViewById(R.id.new_msg_rl);

        titleName.setText(getText(R.string.title_activity_notice_detail));
        rightButtonText.setVisibility(View.VISIBLE);

        detail_root_sc = (ScrollView) findViewById(R.id.detail_root_sc);
        notice_top_bg_iv = (ImageView) findViewById(R.id.notice_top_bg_iv);
        pre_payed_icon_iv = (ImageView) findViewById(R.id.pre_payed_icon_iv);
        collect_tip_iv = (ImageView) findViewById(R.id.collect_tip_iv);
        collect_tip_tv = (TextView) findViewById(R.id.collect_tip_tv);
        remain_time_tv = (TextView) findViewById(R.id.remain_time_tv);
        signed_num_tv = (TextView) findViewById(R.id.signed_num_tv);
        time_progress = (ProgressBar) findViewById(R.id.time_progress);
        shoot_content_tv = (TextView) findViewById(R.id.shoot_content_tv);
        time_limit_tv = (TextView) findViewById(R.id.time_limit_tv);
        address_tv = (TextView) findViewById(R.id.address_tv);
        cm_num_tv = (TextView) findViewById(R.id.cm_num_tv);
        message_rl = (RelativeLayout) findViewById(R.id.message_rl);
        message_tv = (TextView) findViewById(R.id.message_tv);
        cm_avatar_iv = (ImageView) findViewById(R.id.cm_avatar_iv);
        cm_nick_name_tv = (TextView) findViewById(R.id.cm_nick_name_tv);
        identy_iv = (ImageView) findViewById(R.id.identy_iv);
//        request_lv = (ListView) findViewById(R.id.request_lv);
        request_ll = (LinearLayout) findViewById(R.id.request_ll);
        ref_work_ll =  (LinearLayout) findViewById(R.id.ref_work_ll);
        ref_work_gv = (GridView) findViewById(R.id.ref_work_gv);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
//        if (!StringUtils.isEmpty(noticeBgUrl)) {
//            imageLoader.DisplayImage(noticeBgUrl, notice_top_bg_iv, false);
//        }

//        if (isQuery){
//            collect_tip_iv.setVisibility(View.GONE);
//            collect_tip_tv.setVisibility(View.GONE);
//        } else {
//            collect_tip_iv.setVisibility(View.VISIBLE);
//            collect_tip_tv.setVisibility(View.VISIBLE);
//        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightbuttonIcon.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        collect_tip_iv.setOnClickListener(this);
        new_msg_rl.setOnClickListener(this);
    }

    private void requestData() {
        visitUnit.getDataUnit().getRepo().put("noticeId", noticeId);
        if (UserPreferencesUtil.isOnline(this)){
            visitUnit.getDataUnit().getRepo().put("curUserId", UserPreferencesUtil.getUserId(this));
        } else {
            visitUnit.getDataUnit().getRepo().put("curUserId", "");
        }
        new Action(QueryNoticeDeatail.class, "${queryNoticeDetail},noticeId=${noticeId},curUserId=${curUserId}", this, null, visitUnit).run();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rightButtonText:
                isLoadData=false;
                Intent intent = new Intent(NoticeDetailActivity.this,ShareBoardView.class);
                intent.putExtra(Constants.SHARE_TITLE_KEY,notice.getSubject());
                intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"模范儿"+notice.getSubject()+"这个通告不错哦，你们有没有兴趣啊？，反正我报名了！");
                startActivity(intent);
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.rightbuttonIcon:
                if (isShowClose){
                    isLoadData=false;
                    selectAction();
                }else{
                    if(!UserPreferencesUtil.isOnline(this)){
                        Intent intent8 = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent8);
                    } else {
                        Intent intent1 = new Intent(this, ReportFristActivity.class);
                        intent1.putExtra("resType", Constants.JUBAO_TYPE_TONGGAO);
                        intent1.putExtra("userId", UserPreferencesUtil.getUserId(this));
                        intent1.putExtra("resourceId", noticeId);
                        startActivity(intent1);
                    }
                }
                break;
            case R.id.collect_tip_iv:
                collect();
            case R.id.new_msg_rl:
                toNewMsg();
                break;
        }
    }

    /**
     * 跳转消息处理
     */
    private void toNewMsg() {
    }

    //收藏
    private void collect(){
        if(!UserPreferencesUtil.isOnline(this)){
            Intent intent = new Intent(this, LoginWelcomActivity.class);
            startActivity(intent);
        } else {
            if (ifColect==1){
                collectStatus = 1;//取消收藏
            } else {
                collectStatus = 2;//收藏
            }
            visitUnit.getDataUnit().getRepo().put("noticeId", noticeId);
            visitUnit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(this));//通告创建者
            visitUnit.getDataUnit().getRepo().put("collectUserId", UserPreferencesUtil.getUserId(this));//当前登录用户ID
            visitUnit.getDataUnit().getRepo().put("status", collectStatus);
            new Action(CollectNotices.class, "${collectNotices},noticeId=${noticeId},userId=${userId},collectUserId=${collectUserId},status=${status}", this, null, visitUnit).run();
        }

    }

    public void responseCollect() {
        if (collectStatus==1){//取消收藏
            ifColect = 0;
            Toast.makeText(this,"已取消收藏",Toast.LENGTH_SHORT).show();
        } else if (collectStatus==2){//收藏
            ifColect = 1;
            Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
        }

        if (ifColect == 0){//未收藏
            collect_tip_iv.setImageResource(R.drawable.uncollected);
            collect_tip_tv.setText("收藏");
        } else{
            collect_tip_iv.setImageResource(R.drawable.collected);
            collect_tip_tv.setText("已收藏");
        }
        Intent intent = new Intent();
        intent.setAction(Constants.COLLECT_STATUS_CHANGE);
        sendBroadcast(intent);
    }

    //报名
    private void enroll(final long recruitId,int applied){//是否已报名：1：报名；0：未报名
        if (applied == 1){
            List<NoticeRecruitVo> recruitList = notice.getRecruits();
            NoticeRecruitVo noticeRecruitVo = new NoticeRecruitVo();
            int locIndex = 0;
            for(int i=0;i<recruitList.size();i++){
                if(recruitId==recruitList.get(i).getRecruitId()){
                    noticeRecruitVo = recruitList.get(i);
                    locIndex = noticeRecruitVo.getLocIndex();
                    break;
                }
            }

            if (noticeRecruitVo.getRecordStatus() == 4){
                ToChatBean toChatBean = new ToChatBean();
                toChatBean.setRecruitLocIndex(""+locIndex);
                toChatBean.setNoticeId(String.valueOf(notice.getNoticeId()));
                toChatBean.setNoticeSubject(notice.getSubject());
                toChatBean.setUserIcon(notice.getUserLogo());
                toChatBean.setUserId(notice.getPublishUserId()+"");
                toChatBean.setUsernick(notice.getNick());
                toChatBean.setRecruitId(recruitId+"");
                Intent intent=new Intent(this, ChatActivity.class);
                intent.putExtra("toChatBean", toChatBean);
                startActivity(intent);
            } else {
                Toast.makeText(this,"已报过名",Toast.LENGTH_SHORT).show();
            }
        } else {
            if(!UserPreferencesUtil.isOnline(this)){
                isLoadData=false;
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            } else {
                if(UserPreferencesUtil.getUserType(this).equals(Constants.CARRE_PHOTOR)||UserPreferencesUtil.getUserType(this).equals(Constants.CARRE_MERCHANT)){
                    Toast.makeText(this,"只有模特可以报名",Toast.LENGTH_SHORT).show();
                } else{
                    if(UserPreferencesUtil.getUserVerifyStatus(this).equals(Constants.USER_VERIFIED_CODE)){//认证通过
                        TwoButtonDialog dialog=new TwoButtonDialog(this, "是否确认报名","确定","放弃报名", R.style.dineDialog,new ButtonTwoListener() {
                            @Override
                            public void confrimListener() {
                                toEnroll(recruitId);
                            }
                            @Override
                            public void cancelListener() {
                            }
                        });
                        dialog.show();
                    } else if(Constants.USER_VERIFING_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){//认证中
                        OneButtonDialog dialog=new OneButtonDialog(this, "你的账号正在申请认证，请在认证通过后报名！", R.style.dineDialog, new ButtonOneListener() {
                            @Override
                            public void confrimListener(Dialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }else if(Constants.USER_NO_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){//未认证
                        showToCertiDialog("报名前请先进行模特认证");
                    } else if(Constants.USER_VERIFING_REFUSED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){//认证被拒绝

                        if (Constants.PRO_MODEL.equals(UserPreferencesUtil.getUserType(this))){
                            TwoButtonDialog dialog=new TwoButtonDialog(this, "是否确认报名","确定","放弃报名", R.style.dineDialog,new ButtonTwoListener() {
                                @Override
                                public void confrimListener() {
                                    toEnroll(recruitId);
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
        }
    }

    private void showToCertiDialog(String tipStr){
        TwoButtonDialog dialog=new TwoButtonDialog(this, tipStr,"前往认证","放弃报名", R.style.dineDialog,new ButtonTwoListener() {
            @Override
            public void confrimListener() {
                Intent intent = new Intent(NoticeDetailActivity.this, ModelIdentyFirstActivity.class);
                startActivity(intent);
            }
            @Override
            public void cancelListener() {
            }
        });
        dialog.show();
    }


    public void toEnroll(long recruitId) {
        visitUnit.getDataUnit().getRepo().put("publishUserId", notice.getPublishUserId());
        visitUnit.getDataUnit().getRepo().put("noticeId", noticeId);
        visitUnit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(this));
        visitUnit.getDataUnit().getRepo().put("recruitId", recruitId);
        new Action(EnrollNotices.class, "${applyNotices},publishUserId=${publishUserId},noticeId=${noticeId},userId=${userId},recruitId=${recruitId}", this, null, visitUnit).run();
    }

    public void responseEnroll() {
        requestData();
        Toast.makeText(this, "报名成功", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.setAction(Constants.ENROLL_SUCCESS);
        sendBroadcast(intent);
    }

    //查看
    private void query(NoticeRecruitVo noticeRecruitVo,int i){
        Intent intent = new Intent(this,RecruitActivity.class);
        intent.putExtra("noticeId", notice.getNoticeId() + "");
        intent.putExtra("recruitId", noticeRecruitVo.getRecruitId() + "");
        intent.putExtra("position", noticeRecruitVo.getLocIndex());
        startActivity(intent);
    }

    public void responseNotice(NoticeDetailVo resultNotice) {
        notice = resultNotice;
        notice.setShootType(StringUtil.typeIdChange(notice.getShootType()));
        List<ShootTypeBean>  typeList=SGApplication.getInstance().getTypeList();
        if (typeList==null){
            typeList=new ArrayList<>();
        }
        for (ShootTypeBean bean : typeList) {
            if(bean.getTypeId()==notice.getShootType()){
                noticeBgUrl = bean.getNoticeListBg();
                break;
            }
        }
        if (!StringUtils.isEmpty(noticeBgUrl)) {
            imageLoader.loadImageNoThume(noticeBgUrl, notice_top_bg_iv, true);
        }
        //是否预付
        if (notice.getNoticeType() == 2) {
            if (notice.getPayStatus() == 1){
                pre_payed_icon_iv.setVisibility(View.VISIBLE);
                pre_payed_icon_iv.setImageResource(R.drawable.pre_unpayed_icon);
            } else {
                pre_payed_icon_iv.setVisibility(View.VISIBLE);
                pre_payed_icon_iv.setImageResource(R.drawable.pre_payed_icon);
            }
        } else {
            pre_payed_icon_iv.setVisibility(View.GONE);
        }

        //截止时间
        int progress = 0;
        String remainTimeStr = "报名截止：已截止";
        if (notice.getStartTime()!= null && notice.getRecordStatus()!=0){
            Long joinStartTime = notice.getStartTime();
            Long joinEndTime = notice.getEndTime();
            int max = (int)((joinEndTime-joinStartTime)/(1000*60));
            time_progress.setMax(max);
            long curTime = System.currentTimeMillis();
            if(notice.getNoticeType() == 2 && notice.getPayStatus() == 1){//预付费 待支付状态
                if ((joinEndTime-curTime)>0){
                    remainTimeStr = getResources().getString(R.string.notice_no_pay_tip);
                    progress = (int)((joinEndTime-curTime)/(1000*60));
                }
            } else {
                if ((joinEndTime-curTime)>0){
                    remainTimeStr = "报名截止："+DateTime.getLimitTimeStr(joinEndTime - curTime);
                    progress = (int)((joinEndTime-curTime)/(1000*60));
                }
            }
        } else if (notice.getRecordStatus()==0){
            time_progress.setMax(100);
            remainTimeStr = "通告正在审核中！";
        }


        if (UserPreferencesUtil.isOnline(this)&&UserPreferencesUtil.getUserType(this).equals(Constants.CARRE_PHOTOR)&&UserPreferencesUtil.getUserId(this).equals(notice.getPublishUserId() + "")){
            if(notice.getRecordStatus() ==4 ){
                remainTimeStr = "通告已关闭";
            }
        }
        remain_time_tv.setText(remainTimeStr);
        time_progress.setProgress(progress);


        signed_num_tv.setText(notice.getAppliedSum()+"人");
        if(notice.getPgCount() != 0){
            cm_num_tv.setText(notice.getPgCount() + "位摄影师");
            cm_num_tv.setVisibility(View.VISIBLE);
        } else {
            cm_num_tv.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(notice.getRemark())){
            message_tv.setText(notice.getRemark());
        } else {
            message_rl.setVisibility(View.GONE);
        }

        ifColect = notice.getHasCollected();
        if (ifColect == 0){//未收藏
            collect_tip_iv.setImageResource(R.drawable.uncollected);
            collect_tip_tv.setText("收藏");
        } else{
            collect_tip_iv.setImageResource(R.drawable.collected);
            collect_tip_tv.setText("已收藏");
        }

        ifEnrolled = notice.getApplied();
        if (UserPreferencesUtil.isOnline(this)){
            if (ifEnrolled == 0){//未报名
                new_msg_rl.setVisibility(View.GONE);
            } else{
                new_msg_rl.setVisibility(View.VISIBLE);
            }
            if (UserPreferencesUtil.getUserId(this).equals(notice.getPublishUserId() + "")){
                new_msg_rl.setVisibility(View.VISIBLE);
            } else{
                new_msg_rl.setVisibility(View.GONE);
            }
        } else {
            new_msg_rl.setVisibility(View.GONE);
        }

        //功能先隐藏
        new_msg_rl.setVisibility(View.GONE);
        //拍摄主题
        shoot_content_tv.setText(notice.getSubject());

        cm_nick_name_tv.setText(notice.getNick());
        String cmIcon = notice.getUserLogo();
        if (!StringUtils.isEmpty(cmIcon)) {
//            imageLoader.loadImage(cmIcon, cm_avatar_iv, true);

            String path;
            if (cmIcon.contains(Constants.ALIY_IMAGE_DMO)){
                cmIcon = cmIcon.replace(Constants.ALIY_IMAGE_DMO, Constants.LOCAL_IMAGE_DMO);
            }
            if (cmIcon.contains(Constants.WEB_IMAGE_DMO)){
                path=cmIcon.concat(Constants.WEB_SMALL_IMAGE_DMO);
            }else{
                path=cmIcon.concat(Constants.LOCAL_SMAL_IMAGE);
            }
            cm_avatar_iv.setImageResource(R.drawable.moren_icon);
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(path, cm_avatar_iv);
        }
        if (notice.getUserStatus() == 2) {
            identy_iv.setVisibility(View.VISIBLE);
        } else {
            identy_iv.setVisibility(View.GONE);
        }
        NoticeShootVo shoot = notice.getShoot();
        //开始时间结束时间
        String startTimeStr = new DateTime(shoot.getStartTime()).toDateTimeString(DateTime.SLASH_DATE_TIME_HHmm_FORMAT_PATTERN);
        String endTimeStr = new DateTime(shoot.getEndTime()).toDateTimeString(DateTime.SLASH_DATE_TIME_HHmm_FORMAT_PATTERN);
        time_limit_tv.setText(startTimeStr + "--" + endTimeStr);
        AssetDataBaseManager.CityBean it = AssetDataBaseManager.getManager().queryCityById(shoot.getCity());
        if (!"null".equals(shoot.getAddress())){
            address_tv.setText(it.getName() + " " + shoot.getAddress());
        }else{
            address_tv.setText(it.getName());
        }

        final List<NoticeRecruitVo> recruitList = notice.getRecruits();

//        if (UserPreferencesUtil.isOnline(this)){
//            if (UserPreferencesUtil.getUserType(this).equals(Constants.CARRE_PHOTOR)){
//                collect_tip_iv.setVisibility(View.GONE);
//                collect_tip_tv.setVisibility(View.GONE);
//            } else{
//                collect_tip_iv.setVisibility(View.VISIBLE);
//                collect_tip_tv.setVisibility(View.VISIBLE);
//            }
//        } else {
//            collect_tip_iv.setVisibility(View.VISIBLE);
//            collect_tip_tv.setVisibility(View.VISIBLE);
//        }

        if (UserPreferencesUtil.isOnline(this)){
            if (UserPreferencesUtil.getUserId(this).equals(notice.getPublishUserId() + "")){
                collect_tip_iv.setVisibility(View.GONE);
                collect_tip_tv.setVisibility(View.GONE);
            }
        }

        if (recruitList!= null&& recruitList.size()>0){
            if(request_ll.getChildCount()>0){
                request_ll.removeAllViews();
            }
            LinearLayoutForListView view=null ;
            if ("报名截止：已截止".equals(remainTimeStr)){
                view= new LinearLayoutForListView(this,false);
            }else{
                view= new LinearLayoutForListView(this,true);
            }
            view.setOrientation(LinearLayout.VERTICAL);
            view.bindLinearLayout(notice);
            request_ll.addView(view);
        }

        List<NoticeReferPicVo> referPics = notice.getReferPics();
        if (referPics != null && referPics.size()>0){
            ref_work_ll.setVisibility(View.VISIBLE);
            RefWorkAdapter refWorkAdapter = new RefWorkAdapter(this,referPics);
            AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(refWorkAdapter);
            alphaInAnimationAdapter.setAbsListView(ref_work_gv);
            ref_work_gv.setAdapter(alphaInAnimationAdapter);
            final List<String> imageUrls=new ArrayList<String>();
            for(NoticeReferPicVo referPic : referPics){
                imageUrls.add(referPic.getPicUrl());
            }
            ref_work_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    isLoadData=false;
                    Constants.imageBrower(NoticeDetailActivity.this, arg2, imageUrls.toArray(new String[imageUrls.size()]), false);
                }
            });
        } else {
            ref_work_ll.setVisibility(View.GONE);
        }

        boolean isOn=UserPreferencesUtil.isOnline(this);
        String userType=UserPreferencesUtil.getUserType(this);
        isShowClose = isOn && Constants.CARRE_PHOTOR.equals(userType) && UserPreferencesUtil.getUserId(this).equals(notice.getPublishUserId() + "") && notice.getRecordStatus() != 4;
        if (isShowClose){
            rightbuttonIcon.setVisibility(View.VISIBLE);
            rightbuttonIcon.setImageResource(R.drawable.more_icon);
        } else{

            rightbuttonIcon.setVisibility(View.VISIBLE);
            rightbuttonIcon.setImageResource(R.drawable.title_refuse_icon);
        }
    }

    public class EnrollItemClickListener implements View.OnClickListener{
        private int i;
        private List<NoticeRecruitVo> recruitList;
        public EnrollItemClickListener(List<NoticeRecruitVo> recruitList,int i) {
            this.recruitList = recruitList;
            this.i = i;
        }

        @Override
        public void onClick(View view) {
            enroll(recruitList.get(i).getRecruitId(), recruitList.get(i).getApplied());
        }
    }

    public class QueryItemClickListener implements View.OnClickListener{
        private int i;
        private List<NoticeRecruitVo> recruitList;
        public QueryItemClickListener(List<NoticeRecruitVo> recruitList,int i) {
            this.recruitList = recruitList;
            this.i = i;
        }
        @Override
        public void onClick(View view) {
            query(recruitList.get(i),i);
        }
    }
    private void selectAction() {
        Intent intent = new Intent(NoticeDetailActivity.this, CloseShareOptionsView.class);
        intent.putExtra("isShowClose", isShowClose);
        startActivityForResult(intent,CLOSE_SHARE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case CLOSE_SHARE_REQUEST:
                    String backStr = data.getStringExtra(Constants.COMEBACK);
                    if (backStr.equals("share")){
                        Intent intent = new Intent(NoticeDetailActivity.this,ShareBoardView.class);
                        intent.putExtra(Constants.SHARE_TITLE_KEY,notice.getSubject());
                        intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"模范儿"+notice.getSubject()+"这个通告不错哦，你们有没有兴趣啊？，反正我报名了！");
                        startActivity(intent);
                    } else if(backStr.equals("close")){
                        Intent intent = new Intent(NoticeDetailActivity.this,NoticeCloseActivity.class);
                        intent.putExtra("notice",notice);
                        startActivityForResult(intent,TO_CLOSE);
                    }
                    break;
                case TO_CLOSE:
                    requestData();
                    break;
            }
        }
    }

    @Override
	public void onDestroy() {
        unRegisterListener();
        RecycleBitmap.recycleImageView(notice_top_bg_iv);
        RecycleBitmap.recycleImageView(cm_avatar_iv);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearMemoryCache();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearDiscCache();
        System.gc();
        super.onDestroy();
    }
    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                if (UserPreferencesUtil.isOnline(NoticeDetailActivity.this)) {
                    requestData();
                }
                boolean isOn=UserPreferencesUtil.isOnline(NoticeDetailActivity.this);
                String userType=UserPreferencesUtil.getUserType(NoticeDetailActivity.this);
                isShowClose = isOn && Constants.CARRE_PHOTOR.equals(userType) && UserPreferencesUtil.getUserId(NoticeDetailActivity.this).equals(notice.getPublishUserId() + "") && notice.getRecordStatus() != 4;
                if (isShowClose){
                    rightbuttonIcon.setVisibility(View.VISIBLE);
                    rightbuttonIcon.setImageResource(R.drawable.more_icon);
                } else{

                    rightbuttonIcon.setVisibility(View.VISIBLE);
                    rightbuttonIcon.setImageResource(R.drawable.title_refuse_icon);
                }
            }else if (Constants.ACTION_RECORDER_SUCCESS.equals(intent.getAction())){
                //视频地址
                vidoPath = intent.getStringExtra("path");

                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }
    };

    public void registerListener(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_RECORDER_SUCCESS);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterListener(){
        unregisterReceiver(mBroadcastReceiver);
    }

    public void showDialogForRecord() {
        TwoButtonDialog dialog = new TwoButtonDialog(this, "亲，没有视频是不能报名通告的哦~！我们现在去拍视频吧！","前往拍摄","取消", R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
//                        Intent intent = new Intent(getApplicationContext(), FFmpegRecorderActivity.class);
//                        startActivity(intent);
					}

					@Override
					public void cancelListener() {
					}
				});
				dialog.show();
    }

    private void uploadVido(){
        ShowMsgDialog.showNoMsg(this, false);
        AliySSOHepler.getInstance().uploadMedia(this, Constants.AVATAR_PATH, vidoPath, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                vidoPathUrl=imageUrl;
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

    private void uploadVideoSc(){
        OneButtonDialog dialog = new OneButtonDialog(this, "视频上传成功，您可以报名啦", R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
					}
				});
				dialog.show();
    }

    private void requestVideoService(){
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId",UserPreferencesUtil.getUserId(this));
            JSONArray jsonArray=new JSONArray();
            JSONObject objs=new JSONObject();
            objs.put("videoUrl", vidoPathUrl);
            jsonArray.put(objs);
            obj.put("appendVideos",jsonArray);
            IdentyBean identyBean=getIdentyBean();
            if (identyBean!=null){
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

    private IdentyBean getIdentyBean(){
        IdentyBean identyBean=null;
//        String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(this);
//        if (!StringUtils.isEmpty(jsonStr)){
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
                if ("0000".equals(data.getCode())){
                    uploadVideoSc();
                }
                break;
        }
    }
}

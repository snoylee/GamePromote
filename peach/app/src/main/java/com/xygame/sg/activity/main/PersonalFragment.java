package com.xygame.sg.activity.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.BangCellPhoneActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.notice.NoticeManagmentActivity;
import com.xygame.sg.activity.notice.NoticeManagmentForJJRActivity;
import com.xygame.sg.activity.notice.NoticeManagmentForModelActivity;
import com.xygame.sg.activity.personal.AttentionActivity;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.CollectActivity;
import com.xygame.sg.activity.personal.JJRIdentyFirstActivity;
import com.xygame.sg.activity.personal.MemberCenterActivity;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.QianBaoActivity;
import com.xygame.sg.activity.personal.QianBaoForModelActivity;
import com.xygame.sg.activity.personal.RecentVisitActivity;
import com.xygame.sg.activity.personal.SettingActivity;
import com.xygame.sg.activity.personal.SettingWhoActivity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.activity.personal.bean.MeOverview;
import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.util.List;


public class PersonalFragment extends SGBaseFragment implements View.OnClickListener {
    private View backButton, settingView, noticeManageView, walletView,newsLayout,about_us_rl,isShengHe,who_rl,bangPhoneView,jjrTypeIcon,userTypeShow;
    private TextView titleName,bangPhoneValue;
    private RelativeLayout basic_info_rl;//顶部信息的布局
    private CircularImage avatar_iv;//用户头像
    private TextView noologin_nick_name_tv;//用户昵称
    private TextView nick_name_tv;//用户昵称
    private TextView sex_age_tv;//用户性别
    private TextView type_icon_tv;//用户类型icon（模特or摄影师）
    private TextView type_tv;//用户类型text（模特or摄影师）
    private RelativeLayout attention_rl;
    private RelativeLayout collect_rl;
    private TextView attention_num_tv;//关注数目
    private TextView favorite_num_tv;//收藏数目
    private ImageLoader imageLoader;
    private RelativeLayout vip_rl;
    private TextView vip_status_tv;//会员
    private RelativeLayout recent_rl;
    private TextView recent_num_tv;//最近来访数目
    private RelativeLayout identy_rl;
    private TextView identy_status_tv;//认证状态
    private TextView identy_type_tip_tv;//认证
    private RelativeLayout tutorial_rl;
    private TextView tutorial_type_tv;//教程
    private TextView nuReadNews;//未读消息


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        LoginBroadcastListener.registerLoginListener(getActivity(), mBroadcastReceiver);//监听登录
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUserVerifyStatus();
        quaryUnReadNews();
    }

    private void quaryUnReadNews() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            int totalCount = 0;
//            int pushCount= PushEngine.getUnreadCount(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int chatNewsCount = NewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int daymicNewsCount = NewsEngine.quaryUnReadDaymicNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int discNewsCount = TempGroupNewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            totalCount = chatNewsCount + daymicNewsCount+discNewsCount;
            if (totalCount != 0) {
                nuReadNews.setVisibility(View.VISIBLE);
                nuReadNews.setText(String.valueOf(totalCount));
            } else {
                nuReadNews.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = mInflater.inflate(R.layout.sg_individual_fragment, null);
        initViews(mView);
        initDatas();
        initListeners();

        return mView;
    }


    private void initViews(View mView) {
        userTypeShow=mView.findViewById(R.id.userTypeShow);
        jjrTypeIcon=mView.findViewById(R.id.jjrTypeIcon);
        bangPhoneView=mView.findViewById(R.id.bangPhoneView);
        who_rl=mView.findViewById(R.id.who_rl);
        about_us_rl=mView.findViewById(R.id.about_us_rl);
        isShengHe=mView.findViewById(R.id.isShengHe);
        noticeManageView = mView.findViewById(R.id.noticeManageView);
        newsLayout=mView.findViewById(R.id.newsLayout);
        walletView = mView.findViewById(R.id.walletView);
        backButton = mView.findViewById(R.id.backButton);
        backButton.setVisibility(View.GONE);
        titleName = (TextView) mView.findViewById(R.id.titleName);
        nuReadNews=(TextView)mView.findViewById(R.id.nuReadNews);
        bangPhoneValue=(TextView)mView.findViewById(R.id.bangPhoneValue);
        titleName.setText("个人");
        settingView = mView.findViewById(R.id.settingView);
        basic_info_rl = (RelativeLayout) mView.findViewById(R.id.basic_info_rl);
        avatar_iv = (CircularImage) mView.findViewById(R.id.avatar_iv);
        nick_name_tv = (TextView) mView.findViewById(R.id.nick_name_tv);
        noologin_nick_name_tv = (TextView) mView.findViewById(R.id.noologin_nick_name_tv);
        sex_age_tv = (TextView) mView.findViewById(R.id.sex_age_tv);
        type_icon_tv = (TextView) mView.findViewById(R.id.type_icon_tv);
        type_tv = (TextView) mView.findViewById(R.id.type_tv);
        attention_num_tv = (TextView) mView.findViewById(R.id.attention_num_tv);
        favorite_num_tv = (TextView) mView.findViewById(R.id.favorite_num_tv);
        vip_rl = (RelativeLayout) mView.findViewById(R.id.vip_rl);
        vip_status_tv = (TextView) mView.findViewById(R.id.vip_status_tv);
        recent_rl = (RelativeLayout) mView.findViewById(R.id.recent_rl);
        recent_num_tv = (TextView) mView.findViewById(R.id.recent_num_tv);
        identy_rl = (RelativeLayout) mView.findViewById(R.id.identy_rl);
        identy_status_tv = (TextView) mView.findViewById(R.id.identy_status_tv);
        identy_type_tip_tv = (TextView) mView.findViewById(R.id.identy_type_tip_tv);
        tutorial_rl = (RelativeLayout) mView.findViewById(R.id.tutorial_rl);
        tutorial_type_tv = (TextView) mView.findViewById(R.id.tutorial_type_tv);
        attention_rl = (RelativeLayout) mView.findViewById(R.id.attention_rl);
        collect_rl = (RelativeLayout) mView.findViewById(R.id.collect_rl);
    }

    private void initDatas() {
        jjrTypeIcon.setVisibility(View.GONE);
        userTypeShow.setVisibility(View.VISIBLE);
        if (UserPreferencesUtil.getCellPhone(getActivity())!=null){
            bangPhoneValue.setText("已绑定手机号码");
            bangPhoneValue.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
        }else{
            bangPhoneValue.setText("未绑定手机号码");
        }
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (UserPreferencesUtil.isOnline(getActivity())) {
            if (UserPreferencesUtil.getHeadPic(getActivity())!=null){
                imageLoader.loadImage(UserPreferencesUtil.getHeadPic(getActivity()), avatar_iv, true);
            }
            nick_name_tv.setText(UserPreferencesUtil.getUserNickName(getActivity()));
            noologin_nick_name_tv.setVisibility(View.GONE);
            nick_name_tv.setVisibility(View.VISIBLE);
            String sexStr = UserPreferencesUtil.getSex(getActivity());
            sex_age_tv.setVisibility(View.VISIBLE);
            if (Constants.SEX_WOMAN.equals(sexStr)) {
                Drawable femaleDrawable = getResources().getDrawable(R.drawable.female);
                sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
                sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
            } else if (Constants.SEX_MAN.equals(sexStr)) {
                Drawable maleDrawable = getResources().getDrawable(R.drawable.male);
                sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
                sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
            }

            String typeStr = UserPreferencesUtil.getUserType(getActivity());
            type_icon_tv.setVisibility(View.VISIBLE);
            type_tv.setVisibility(View.VISIBLE);
            if (Constants.CARRE_MODEL.equals(typeStr)||Constants.PRO_MODEL.equals(typeStr)) {
                jjrTypeIcon.setVisibility(View.GONE);
                userTypeShow.setVisibility(View.VISIBLE);
                identy_type_tip_tv.setText("认证模特");
                tutorial_type_tv.setText("模特教程");
                if (UserPreferencesUtil.getUserVerifyStatus(getActivity()).equals(Constants.USER_VERIFIED_CODE)) {
                    Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
                    type_tv.setText("模特");
                    type_tv.setBackgroundResource(R.drawable.identy_type_bg);
                } else {
                    Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
                    type_tv.setText("模特");
                    type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
                }

            } else if (Constants.CARRE_PHOTOR.equals(typeStr)) {
                jjrTypeIcon.setVisibility(View.GONE);
                userTypeShow.setVisibility(View.VISIBLE);
                identy_type_tip_tv.setText("认证摄影师");
                tutorial_type_tv.setText("摄影师教程");
                if (UserPreferencesUtil.getUserVerifyStatus(getActivity()).equals(Constants.USER_VERIFIED_CODE)) {
                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
                    type_tv.setText("摄影师");
                    type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
                } else {
                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
                    type_tv.setText("摄影师");
                    type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
                }

            }else if (Constants.CARRE_MERCHANT.equals(typeStr)) {
                jjrTypeIcon.setVisibility(View.VISIBLE);
                userTypeShow.setVisibility(View.GONE);
                identy_type_tip_tv.setText("认证经纪人");
                tutorial_type_tv.setText("经纪人教程");
//                if (UserPreferencesUtil.getUserVerifyStatus(getActivity()).equals(Constants.USER_VERIFIED_CODE)) {
//                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
//                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
//                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
//                    type_tv.setText("经纪人");
//                    type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
//                } else {
//                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
//                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
//                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
//                    type_tv.setText("经纪人");
//                    type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
//                }

            }

            updateUserVerifyStatus();

        } else {
            jjrTypeIcon.setVisibility(View.GONE);
            userTypeShow.setVisibility(View.VISIBLE);
            avatar_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
            noologin_nick_name_tv.setText("游客");
            nick_name_tv.setVisibility(View.GONE);
            noologin_nick_name_tv.setVisibility(View.VISIBLE);
            sex_age_tv.setVisibility(View.GONE);
            type_icon_tv.setVisibility(View.GONE);
            type_tv.setVisibility(View.GONE);
            attention_num_tv.setText("0");
            favorite_num_tv.setText("0");
            identy_status_tv.setText("未申请认证");
        }

        requestData();
    }

    private void updateUserVerifyStatus() {
        String identyStr = UserPreferencesUtil.getUserVerifyStatus(getActivity());
        if (Constants.USER_VERIFIED_CODE.equals(identyStr)) {
            isShengHe.setVisibility(View.GONE);
            identy_status_tv.setText("已认证");
        } else if (Constants.USER_NO_VERIFIED_CODE.equals(identyStr)) {
            isShengHe.setVisibility(View.VISIBLE);
            identy_status_tv.setText("未申请认证");
        } else if (Constants.USER_VERIFING_CODE.equals(identyStr)) {
            isShengHe.setVisibility(View.GONE);
            identy_status_tv.setText("审核中");
        } else if (Constants.USER_VERIFING_REFUSED_CODE.equals(identyStr)) {
            isShengHe.setVisibility(View.VISIBLE);
            identy_status_tv.setText("未通过审核");
        }
    }

    private void initListeners() {
        bangPhoneView.setOnClickListener(this);
        who_rl.setOnClickListener(this);
        basic_info_rl.setOnClickListener(this);
        settingView.setOnClickListener(this);
        noticeManageView.setOnClickListener(this);
        walletView.setOnClickListener(this);
        attention_rl.setOnClickListener(this);
        collect_rl.setOnClickListener(this);
        vip_rl.setOnClickListener(this);
        recent_rl.setOnClickListener(this);
        identy_rl.setOnClickListener(this);
        tutorial_rl.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        about_us_rl.setOnClickListener(this);
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                initDatas();
                requestData();
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                initDatas();
                requestData();
            } else if (Constants.ACTION_EDITOR_USER_INFO.equals(intent.getAction())) {
                initDatas();
                requestData();
            }else if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())){
                boolean newFlag=intent.getBooleanExtra("newsMessage",false);
                if (newFlag){
                    quaryUnReadNews();
                }
            }else if ("com.xygame.push.dynamic.message.list.action".equals(intent.getAction())){
                quaryUnReadNews();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
//        LoginBroadcastListener.unRegisterLoginListener(getActivity(), mBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.basic_info_rl:
                if (UserPreferencesUtil.isOnline(getActivity())) {
                    String typeStr = UserPreferencesUtil.getUserType(getActivity());
                    if (Constants.CARRE_MODEL.equals(typeStr) || Constants.PRO_MODEL.equals(typeStr)) {
                        Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                        intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.EDIT_INFO_FLAG);
                        getActivity().startActivity(intent);
                    } else{// if (Constants.CARRE_PHOTOR.equals(typeStr))
                        Intent intent = new Intent(getActivity(), CMPersonInfoActivity.class);
                        intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.EDIT_INFO_FLAG);
                        getActivity().startActivity(intent);
                    }
                }
                break;
            case R.id.settingView:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.noticeManageView:
                String typeStr = UserPreferencesUtil.getUserType(getActivity());
                if (Constants.CARRE_MODEL.equals(typeStr) || Constants.PRO_MODEL.equals(typeStr)) {
                    Intent intent1 = new Intent(getActivity(), NoticeManagmentForModelActivity.class);
                    getActivity().startActivity(intent1);
                } else if (Constants.CARRE_PHOTOR.equals(typeStr)) {
                    Intent intent1 = new Intent(getActivity(), NoticeManagmentActivity.class);
                    getActivity().startActivity(intent1);
                } else if (Constants.CARRE_MERCHANT.equals(typeStr)){
                    Intent intent1 = new Intent(getActivity(), NoticeManagmentForJJRActivity.class);
                    getActivity().startActivity(intent1);
                }
                break;
            case R.id.walletView:
                if (UserPreferencesUtil.isOnline(getActivity())) {
                    String typeStr1 = UserPreferencesUtil.getUserType(getActivity());
                    if (Constants.CARRE_MODEL.equals(typeStr1) || Constants.PRO_MODEL.equals(typeStr1)) {
                        Intent intent1 = new Intent(getActivity(), QianBaoForModelActivity.class);
                        getActivity().startActivity(intent1);
                    } else{//if (Constants.CARRE_PHOTOR.equals(typeStr1))
                        Intent intent2 = new Intent(getActivity(), QianBaoActivity.class);
                        getActivity().startActivity(intent2);
                    }
                }

                break;
            case R.id.attention_rl:
                Intent intent3 = new Intent(getActivity(), AttentionActivity.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.collect_rl:
                Intent intent4 = new Intent(getActivity(), CollectActivity.class);
                getActivity().startActivity(intent4);
                break;
            case R.id.vip_rl:
                Intent memberIntent = new Intent(getActivity(), MemberCenterActivity.class);
                getActivity().startActivity(memberIntent);
                break;
            case R.id.recent_rl:
                recent_num_tv.setVisibility(View.GONE);
                Intent intent5 = new Intent(getActivity(), RecentVisitActivity.class);
                getActivity().startActivity(intent5);
                break;
            case R.id.identy_rl:
               //查询认证状态
//                if (UserPreferencesUtil.isOnline(getActivity())) {
//                    String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(getActivity());
//                    if (!StringUtils.isEmpty(jsonStr)){
//                        List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//                        judge(resultList);
//                    }
//
//                } else {
//                    getActivity().startActivity(new Intent(getActivity(), LoginWelcomActivity.class));
//                }
                break;
            case R.id.tutorial_rl:
                Intent intent6 = new Intent(getActivity(), CommonWebViewActivity.class);
                String type = UserPreferencesUtil.getUserType(getActivity());
                if (Constants.CARRE_MODEL.equals(type)||Constants.PRO_MODEL.equals(type)) {
                    intent6.putExtra("webUrl",Constants.MODEL_FAQ_URL);
                    intent6.putExtra("title","模特教程");
                } else if (Constants.CARRE_PHOTOR.equals(type)) {
                    intent6.putExtra("webUrl",Constants.CM_FAQ_URL);
                    intent6.putExtra("title","摄影师教程");
                } else if (Constants.CARRE_MERCHANT.equals(type)){
                    intent6.putExtra("webUrl",Constants.JJR_FAQ_URL);
                    intent6.putExtra("title","经纪人教程");
                }
                getActivity().startActivity(intent6);
                break;
            case R.id.newsLayout:
                Intent intent7 = new Intent(getActivity(), NewsFragment.class);
                getActivity().startActivity(intent7);
                break;
            case R.id.about_us_rl:
                Uri uri = Uri.parse("market://details?id="+getActivity().getPackageName());
                Intent intent8 = new Intent(Intent.ACTION_VIEW,uri);
                intent8.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent8);
                break;
            case R.id.who_rl:
                Intent intent9=new Intent(getActivity(), SettingWhoActivity.class);
                startActivity(intent9);
                break;
            case R.id.bangPhoneView:
                Intent intent10=new Intent(getActivity(), BangCellPhoneActivity.class);
                startActivityForResult(intent10,0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode != Activity.RESULT_OK|| null == data) {
                    return;
                }
                boolean flag=data.getBooleanExtra("flag",false);
                if (flag){
                    if (UserPreferencesUtil.getCellPhone(getActivity())!=null){
                        bangPhoneValue.setText("已绑定手机号码");
                        bangPhoneValue.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
                    }else{
                        bangPhoneValue.setText("未绑定手机号码");
                    }
                }
                break;

            default:
                break;
        }

    }

    private void requestData() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            RequestBean item = new RequestBean();
            try {
                JSONObject obj = new JSONObject();
                obj.put("userId",UserPreferencesUtil.getUserId(getActivity()));
                item.setData(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            item.setServiceURL(ConstTaskTag.QUERY_MYOVERVIEW_URL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MYOVERVIEW_INT);
        }

    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_MYOVERVIEW_INT:
                if ("0000".equals(data.getCode())) {
                    parseJson(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseJson(ResponseBean data) {
//        MeOverview meOverview = JSON.parseObject(data.getRecord(), MeOverview.class);
//        if (meOverview!=null){
//            String userIcon = meOverview.getUserIcon();
//            if (!userIcon.equals("null") && !userIcon.equals("")) {
//                imageLoader.loadImage(userIcon, avatar_iv, true);
//            }
//            String usernick = meOverview.getUsernick();
//            if (!usernick.equals("null") && !usernick.equals("")) {
//                nick_name_tv.setText(usernick);
//                noologin_nick_name_tv.setVisibility(View.GONE);
//                nick_name_tv.setVisibility(View.VISIBLE);
//            }
//            String age = meOverview.getAge()+"";
//            if (!age.equals("null") && !age.equals("")) {
//                sex_age_tv.setText(age);
//                sex_age_tv.setVisibility(View.VISIBLE);
//            }
//
//            String attenCount = meOverview.getAttenCount()+"";
//            if (!StringUtils.isEmpty(attenCount)) {
//                attention_num_tv.setText(attenCount);
//            }
//            String collectCount = meOverview.getCollectCount()+"";
//            if (!StringUtils.isEmpty(collectCount)) {
//                favorite_num_tv.setText(collectCount);
//            }
//
//            String lastVisitCount = meOverview.getLastVisitCount()+"";
//            if (!StringUtils.isEmpty(lastVisitCount)) {
//                int count = Integer.parseInt(lastVisitCount);
//                if (count > 0) {
//                    recent_num_tv.setVisibility(View.VISIBLE);
//                    recent_num_tv.setBackgroundResource(R.drawable.new_icon);
//                } else {
//                    recent_num_tv.setVisibility(View.GONE);
//                }
//
//            }
////            List<UserTypeVo> utypesList = meOverview.getUtypes();
////            UserTypeVo userTypeVo = new UserTypeVo();
////            if (utypesList.size()>1){
////                userTypeVo = utypesList.get(1);
////            } else {
////                userTypeVo = utypesList.get(0);
////            }
//
////            UserTypeVo userTypeVo = utypesList.get(0);
////
////            String jsonStr = JSON.toJSONString(utypesList);
////            UserPreferencesUtil.setUserTypeJsonStr(getActivity(),jsonStr);
////
////            String userType = userTypeVo.getUserType()+"";
////            String authStatus = userTypeVo.getAuthStatus()+"";
//            type_icon_tv.setVisibility(View.VISIBLE);
//            type_tv.setVisibility(View.VISIBLE);
//
////            if (Constants.CARRE_MODEL.equals(userType)) {
////                identy_type_tip_tv.setText("认证模特");
////                tutorial_type_tv.setText("模特教程");
////                if (UserPreferencesUtil.getUserVerifyStatus(getActivity()).equals(Constants.USER_VERIFIED_CODE)) {
////                    Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
////                    type_tv.setText("模特");
////                    type_tv.setBackgroundResource(R.drawable.identy_type_bg);
////                } else {
////                    Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
////                    type_tv.setText("模特");
////                    type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
////                }
////
////            } else if (Constants.CARRE_PHOTOR.equals(userType)) {
////                identy_type_tip_tv.setText("认证摄影师");
////                tutorial_type_tv.setText("摄影师教程");
////                if (UserPreferencesUtil.getUserVerifyStatus(getActivity()).equals(Constants.USER_VERIFIED_CODE)) {
////                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
////                    type_tv.setText("摄影师");
////                    type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
////                } else {
////                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
////                    type_tv.setText("摄影师");
////                    type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
////                }
////
////            }else if(Constants.PRO_MODEL.equals(userType)){
////                identy_type_tip_tv.setText("认证模特");
////                Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
////                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
////                type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
////                type_tv.setText("模特");
////                type_tv.setBackgroundResource(R.drawable.identy_type_bg);
//////                identy_type_tip_tv.setText("认证高级模特");
//////                if (authStatus.equals("2")){
//////                    Drawable modelDrawable = getResources().getDrawable(R.drawable.model_pro_identy_icon);
//////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
//////                    type_icon_tv.setBackgroundResource(R.drawable.identy_pro_type_icon_bg);
//////                    type_tv.setText("高级模特");
//////                    type_tv.setBackgroundResource(R.drawable.identy_pro_type_bg);
//////                } else {
//////                    Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
//////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
//////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
//////                    type_tv.setText("模特");
//////                    type_tv.setBackgroundResource(R.drawable.identy_type_bg);
//////                }
////
////            }else if (Constants.CARRE_MERCHANT.equals(userType)) {
////                jjrTypeIcon.setVisibility(View.VISIBLE);
////                userTypeShow.setVisibility(View.GONE);
////                identy_type_tip_tv.setText("认证经纪人");
////                tutorial_type_tv.setText("经纪人教程");
//////                if (UserPreferencesUtil.getUserVerifyStatus(getActivity()).equals(Constants.USER_VERIFIED_CODE)) {
//////                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
//////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
//////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
//////                    type_tv.setText("经纪人");
//////                    type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
//////                } else {
//////                    Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
//////                    type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
//////                    type_icon_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
//////                    type_tv.setText("经纪人");
//////                    type_tv.setBackgroundResource(R.drawable.identy_type_gray_bg);
//////                }
////
////            }
//////            if (Constants.USER_VERIFIED_CODE.equals(authStatus)) {
//////                isShengHe.setVisibility(View.GONE);
//////                identy_status_tv.setText("已认证");
//////            } else if (Constants.USER_NO_VERIFIED_CODE.equals(authStatus)) {
//////                isShengHe.setVisibility(View.VISIBLE);
//////                identy_status_tv.setText("未申请认证");
//////            } else if (Constants.USER_VERIFING_CODE.equals(authStatus)) {
//////                isShengHe.setVisibility(View.GONE);
//////                identy_status_tv.setText("审核中");
//////            } else if (Constants.USER_VERIFING_REFUSED_CODE.equals(authStatus)) {
//////                isShengHe.setVisibility(View.VISIBLE);
//////                identy_status_tv.setText("未通过审核");
//////            }
////            UserPreferencesUtil.setUserVerifyStatus(getActivity(),authStatus);
//            updateUserVerifyStatus();
//        }
    }


    private void judge( List<IdentyBean> resultList) {
        if (resultList == null || resultList.size() == 0) {
            return;
        }
        IdentyBean identyBean = resultList.get(0);
        int authStatus = identyBean.getAuthStatus();
        int userType = identyBean.getUserType();
        String refuseReason = identyBean.getRefuseReason();
        switch (userType) {
            case 2://普通模特
                switch (authStatus) {
                    case 0://未申请认证
                        getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                        break;
                    case 1://审核中
                        verifing(refuseReason);
                        break;
                    case 2://普通模特认证通过
                        modelVerified("恭喜, 您已通过模特认证!");
                        break;
                    case 3://普通模特申请认证拒绝
                        modelVerifyRefused( refuseReason);
                        break;
                }
                break;
            case 8://摄影师
                switch (authStatus) {
                    case 0:
                        getActivity().startActivity(new Intent(getActivity(), CMIdentyFirstActivity.class));
                        break;
                    case 1:
                        verifing(refuseReason);
                        break;
                    case 2:
                        proOrCmVerified("恭喜, 您已通过摄影师认证!");
                        break;
                    case 3:
                        cmVerifyRefused(refuseReason);
                        break;
                }
                break;
            case 16:
                switch (authStatus) {
                    case 0:
                        getActivity().startActivity(new Intent(getActivity(), JJRIdentyFirstActivity.class));
                        break;
                    case 1:
                        verifing(refuseReason);
                        break;
                    case 2:
                        proOrCmVerified("恭喜, 您已通过经纪人认证!");
                        break;
                    case 3:
                        cmVerifyRefused1(refuseReason);
                        break;
                }
                break;
        }
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (ShowMsgDialog.isShowing()){
                        ShowMsgDialog.cancel();
//                        getActivity().startActivity(new Intent(getActivity(), ProModelIdentyActivity.class));
                    }
                    break;
                case 2:
                    if (ShowMsgDialog.isShowing()){
                        ShowMsgDialog.cancel();
                    }
                    break;
                case 3:
                    if (ShowMsgDialog.isShowing()){
                        ShowMsgDialog.cancel();
                        getActivity().startActivity(new Intent(getActivity(), CMIdentyFirstActivity.class));
                    }
                    break;
                case 5:
                    if (ShowMsgDialog.isShowing()){
                        ShowMsgDialog.cancel();
                        getActivity().startActivity(new Intent(getActivity(), JJRIdentyFirstActivity.class));
                    }
                    break;
                case 4:
                    if (ShowMsgDialog.isShowing()){
                        ShowMsgDialog.cancel();
                        getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                    }
                    break;
                default:
                    break;
            }

        }
    };
    /**
     * 摄影师或高级模特申请认证通过
     * @param refuseReason
     */
    private void proOrCmVerified(String refuseReason){
        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFIED_CODE);
        ShowMsgDialog.showOne(getActivity(), refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
            }
        });
        handler.sendEmptyMessageDelayed(2,5000);
    }

    /**
     * 普通模特申请认证被拒，重新申请
     * @param refuseReason
     */
    private void modelVerifyRefused(String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
        ShowMsgDialog.showOne(getActivity(), "抱歉，您提交的资料未通过模特认证，原因是：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
                getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));

            }
        });

        handler.sendEmptyMessageDelayed(4, 5000);
    }
    /**
     * 高级模特申请认证被拒，重新申请
     * @param refuseReason
     */
//    private void proModelVerifyRefused(String refuseReason) {
//        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
//        ShowMsgDialog.showOne(getActivity(), "抱歉，您提交的资料未通过模特认证，原因是：" + refuseReason, true, new ButtonOneListener() {
//            @Override
//            public void confrimListener(Dialog dialog) {
//                ShowMsgDialog.cancel();
//                getActivity().startActivity(new Intent(getActivity(), ProModelIdentyActivity.class));
//            }
//        });
//        handler.sendEmptyMessageDelayed(1,5000);
//    }
    /**
     * 摄影师申请认证被拒，重新申请
     * @param refuseReason
     */
    private void cmVerifyRefused(String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
        ShowMsgDialog.showOne(getActivity(), "抱歉，您提交的资料未通过摄影师认证，原因是：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
                getActivity().startActivity(new Intent(getActivity(), CMIdentyFirstActivity.class));

            }
        });
        handler.sendEmptyMessageDelayed(3, 5000);
    }
    private void cmVerifyRefused1(String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFING_REFUSED_CODE);
        ShowMsgDialog.showOne(getActivity(), "抱歉，您提交的资料未通过摄影师认证，原因是：" + refuseReason, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
                getActivity().startActivity(new Intent(getActivity(), JJRIdentyFirstActivity.class));

            }
        });
        handler.sendEmptyMessageDelayed(5, 5000);
    }
    /**
     * 普通模特申请认证通过 跳转申请高级模特
     * @param s
     */
    private void modelVerified(String s) {
        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFIED_CODE);
        ShowMsgDialog.showOne(getActivity(), s, true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                if (ShowMsgDialog.isShowing()){
                    ShowMsgDialog.cancel();
//                    getActivity().startActivity(new Intent(getActivity(), ProModelIdentyActivity.class));
                }
            }
        });
        handler.sendEmptyMessageDelayed(1,5000);
    }

    /**
     * 普通模特、高级模特或摄影师申请认证中
     * @param refuseReason
     */
    private void verifing( String refuseReason) {
        UserPreferencesUtil.setUserVerifyStatus(getActivity(), Constants.USER_VERIFING_CODE);
        ShowMsgDialog.showOne(getActivity(), "审核中!" , true, new ButtonOneListener() {
            @Override
            public void confrimListener(Dialog dialog) {
                ShowMsgDialog.cancel();
            }
        });

        handler.sendEmptyMessageDelayed(2,5000);
    }

    public void updateBang() {
        bangPhoneValue.setText("已绑定手机号码");
        bangPhoneValue.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
    }
}
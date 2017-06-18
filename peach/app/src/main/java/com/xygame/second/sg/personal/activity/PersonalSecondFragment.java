package com.xygame.second.sg.personal.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.comm.activity.ActManagerActivity;
import com.xygame.second.sg.personal.utils.ActionsFilter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.BangCellPhoneActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.main.NewsFragment;
import com.xygame.sg.activity.personal.AttentionActivity;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.QianBaoForModelActivity;
import com.xygame.sg.activity.personal.SettingActivity;
import com.xygame.sg.activity.personal.SettingWhoActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;


public class PersonalSecondFragment extends SGBaseFragment implements View.OnClickListener {

    private View intoPersonal,videoCertify,idCertify,newsLayout,actionLayout,walletView,recent_rl,attention_rl,bangPhoneView,settingView,about_us_rl,who_rl,vido_gou,id_gou,sex_age_bg;
    private ImageView sexIcon;
    private CircularImage avatar_iv;
    private TextView nuReadNews,sexAge,userName,loactionTex,vido_text,id_text,recentNum,attationNums,bangPhoneValue;
    private ImageLoader imageLoader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerLoginListener();//监听登录
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        loactionTex.setText(BaiduPreferencesUtil.getCity(getActivity()));
        quaryUnReadNews();
    }

    private void quaryUnReadNews() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            int totalCount = 0;
//            int pushCount= PushEngine.getUnreadCount(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int chatNewsCount = NewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int daymicNewsCount = NewsEngine.quaryUnReadDaymicNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            totalCount = chatNewsCount + daymicNewsCount;
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
        View mView = mInflater.inflate(R.layout.personal_layout, null);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        initViews(mView);
        initDatas();
        initListeners();
        return mView;
    }


    private void initViews(View mView) {
        sex_age_bg=mView.findViewById(R.id.sex_age_bg);
        intoPersonal = mView.findViewById(R.id.intoPersonal);
        bangPhoneValue=(TextView)mView.findViewById(R.id.bangPhoneValue);
        nuReadNews = (TextView) mView.findViewById(R.id.nuReadNews);
        vido_gou=mView.findViewById(R.id.vido_gou);
        id_gou=mView.findViewById(R.id.id_gou);
        videoCertify = mView.findViewById(R.id.videoCertify);
        idCertify = mView.findViewById(R.id.idCertify);
        actionLayout = mView.findViewById(R.id.actionLayout);
        bangPhoneView = mView.findViewById(R.id.bangPhoneView);
        about_us_rl = mView.findViewById(R.id.about_us_rl);
        who_rl = mView.findViewById(R.id.who_rl);
        newsLayout = mView.findViewById(R.id.newsLayout);
        walletView = mView.findViewById(R.id.walletView);
        recent_rl =  mView.findViewById(R.id.recent_rl);
        settingView = mView.findViewById(R.id.settingView);
        attention_rl = mView.findViewById(R.id.attention_rl);
        sexIcon = (ImageView) mView.findViewById(R.id.sexIcon);
        avatar_iv = (CircularImage) mView.findViewById(R.id.avatar_iv);
        sexAge = (TextView) mView.findViewById(R.id.sexAge);
        userName = (TextView) mView.findViewById(R.id.userName);
        loactionTex = (TextView) mView.findViewById(R.id.loactionTex);
        vido_text = (TextView) mView.findViewById(R.id.vido_text);
        id_text = (TextView) mView.findViewById(R.id.id_text);
        recentNum = (TextView) mView.findViewById(R.id.recentNum);
        attationNums = (TextView) mView.findViewById(R.id.attationNums);
    }

    private void initDatas() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            String visitCount=UserPreferencesUtil.getUserVisitCount(getActivity());
            if (visitCount!=null&&!"0".equals(visitCount)){
                recentNum.setVisibility(View.VISIBLE);
                recentNum.setText(visitCount);
            }

            String attentCount=UserPreferencesUtil.getUserAttentCount(getActivity());
            if (attentCount!=null&&!"0".equals(attentCount)){
                attationNums.setVisibility(View.VISIBLE);
                attationNums.setText(attentCount);
            }

            String idStatus=UserPreferencesUtil.getUserIDAuth(getActivity());
            if (idStatus!=null){
                if ("0".equals(idStatus)){
                    id_text.setText("点击认证");
                    id_gou.setVisibility(View.GONE);
                }else if ("1".equals(idStatus)){
                    id_text.setText("审核中");
                    id_gou.setVisibility(View.GONE);
                }else if ("4".equals(idStatus)){
                    id_text.setText("审核中");
                    id_gou.setVisibility(View.GONE);
                }else if ("2".equals(idStatus)){
                    id_text.setText("审核通过");
                    id_gou.setVisibility(View.VISIBLE);
                }else if ("3".equals(idStatus)){
                    id_text.setText("审核拒绝");
                    id_gou.setVisibility(View.GONE);
                }
            }else{
                id_text.setText("点击认证");
                id_gou.setVisibility(View.GONE);
            }
           String videoStatus=UserPreferencesUtil.getUserVideoAuth(getActivity());
            if (videoStatus!=null){
                if ("0".equals(videoStatus)){
                    vido_text.setText("点击认证");
                    vido_gou.setVisibility(View.GONE);
                }else if ("1".equals(videoStatus)){
                    vido_text.setText("审核中");
                    vido_gou.setVisibility(View.GONE);
                }else if ("4".equals(videoStatus)){
                    vido_text.setText("审核中");
                    vido_gou.setVisibility(View.GONE);
                }else if ("2".equals(videoStatus)){
                    vido_text.setText("审核通过");
                    vido_gou.setVisibility(View.VISIBLE);
                }else if ("3".equals(videoStatus)){
                    vido_text.setText("审核拒绝");
                    vido_gou.setVisibility(View.GONE);
                }
            }else{
                vido_text.setText("点击认证");
                vido_gou.setVisibility(View.GONE);
            }

            if (UserPreferencesUtil.getCellPhone(getActivity())!=null){
                bangPhoneValue.setText("已绑定手机号码");
                bangPhoneValue.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
            }else{
                bangPhoneValue.setText("未绑定手机号码");
            }
            if (UserPreferencesUtil.getHeadPic(getActivity())!=null){
                imageLoader.loadImage(UserPreferencesUtil.getHeadPic(getActivity()), avatar_iv, true);
            }
            loactionTex.setText(BaiduPreferencesUtil.getCity(getActivity()));
            userName.setText(UserPreferencesUtil.getUserNickName(getActivity()));
            String sexStr = UserPreferencesUtil.getSex(getActivity());
            if (Constants.SEX_WOMAN.equals(sexStr)) {
                sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
                sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
                sexAge.setText(UserPreferencesUtil.getUserAge(getActivity()));
            } else if (Constants.SEX_MAN.equals(sexStr)) {
                sexIcon.setImageResource(R.drawable.sg_man_light_icon);
                sexAge.setText(UserPreferencesUtil.getUserAge(getActivity()));
                sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
            }
        }else {
            avatar_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
            userName.setText("游客");
            id_text.setText("点击认证");
            id_gou.setVisibility(View.GONE);
            vido_text.setText("点击认证");
            vido_gou.setVisibility(View.GONE);
        }
    }

    private void initListeners() {
        intoPersonal.setOnClickListener(this);
        bangPhoneView.setOnClickListener(this);
        videoCertify.setOnClickListener(this);
        idCertify.setOnClickListener(this);
        actionLayout.setOnClickListener(this);
        settingView.setOnClickListener(this);
        walletView.setOnClickListener(this);
        attention_rl.setOnClickListener(this);
        who_rl.setOnClickListener(this);
        recent_rl.setOnClickListener(this);
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
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                initDatas();
            }else if (ActionsFilter.ACTION_ID_SUCCESS.equals(intent.getAction())) {
                UserPreferencesUtil.setUserIDAuth(getActivity(), "1");
                initDatas();
            }else if (ActionsFilter.ACTION_VIDEO_SUCCESS.equals(intent.getAction())) {
                UserPreferencesUtil.setUserVideoAuth(getActivity(),"1");
                initDatas();
            }
        }
    };

    public void registerLoginListener(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
        myIntentFilter.addAction(ActionsFilter.ACTION_ID_SUCCESS);
        myIntentFilter.addAction(ActionsFilter.ACTION_VIDEO_SUCCESS);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener(){
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterLoginListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.intoPersonal:

                break;
            case R.id.settingView:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.videoCertify:
                String videoStatus=UserPreferencesUtil.getUserVideoAuth(getActivity());
                if (videoStatus!=null){
                    if ("0".equals(videoStatus)){
                        Intent intent12 = new Intent(getActivity(), VideoCertifyActivity.class);
                        startActivity(intent12);
                    }else if ("1".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else if ("4".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else if ("2".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else if ("3".equals(videoStatus)){
                        Intent intent11 = new Intent(getActivity(), VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }
                }else{
                    Intent intent13 = new Intent(getActivity(), VideoCertifyActivity.class);
                    startActivity(intent13);
                }
                break;
            case R.id.walletView:
                Intent intent1 = new Intent(getActivity(), QianBaoForModelActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.attention_rl:
                Intent intent3 = new Intent(getActivity(), AttentionActivity.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.idCertify:
                String idStatus=UserPreferencesUtil.getUserIDAuth(getActivity());
                if (idStatus!=null){
                    if ("0".equals(idStatus)){
                        getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                    }else if ("1".equals(idStatus)){
                        showOneButtonDialog("您的身份认证审核中，请耐心等待");
                    }else if ("4".equals(idStatus)){
                        showOneButtonDialog("您的身份认证审核中，请耐心等待");
                    }else if ("2".equals(idStatus)){
                        showOneButtonDialog("恭喜您，您的身份认证已通过");
                    }else if ("3".equals(idStatus)){
                        id_text.setText("审核拒绝");
                        id_gou.setVisibility(View.GONE);
                        showTwoButtonDialog("抱歉！您的身份认证被拒绝，原因：".concat(UserPreferencesUtil.getUserIDAuthRefuseReason(getActivity())), "下次再说", "前往认证");
                    }
                }else{
                    getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                }

                break;
            case R.id.actionLayout:
                Intent intent11 = new Intent(getActivity(), ActManagerActivity.class);
                startActivity(intent11);
                break;
            case R.id.recent_rl:
                recentNum.setVisibility(View.GONE);
                UserPreferencesUtil.setUserVisitCount(getActivity(),null);
                Intent intent5 = new Intent(getActivity(), GroupTestActivity.class);
                getActivity().startActivity(intent5);
                break;
            case R.id.newsLayout:
                Intent intent7 = new Intent(getActivity(), NewsFragment.class);
                getActivity().startActivity(intent7);
                break;
            case R.id.about_us_rl:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent8 = new Intent(Intent.ACTION_VIEW, uri);
                intent8.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent8);
                break;
            case R.id.who_rl:
                Intent intent9 = new Intent(getActivity(), SettingWhoActivity.class);
                startActivity(intent9);
                break;
            case R.id.bangPhoneView:
                Intent intent10 = new Intent(getActivity(), BangCellPhoneActivity.class);
                startActivityForResult(intent10, 0);
                break;
        }
    }

    private void showTwoButtonDialog(String content, String leftText, String rightText) {
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(), content, leftText, rightText, R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                    }
                });
        dialog.show();
    }

    private void showOneButtonDialog(String content){
        OneButtonDialog dialog = new OneButtonDialog(getActivity(), content, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
					}
				});
				dialog.show();
    }

    public void requestData() {
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
        ShowMsgDialog.cancel();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_MYOVERVIEW_INT:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                }
                break;
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)){
            try {
                JSONObject object=new JSONObject(record);
                UserPreferencesUtil.setUserAttentCount(getActivity(), object.getString("attentCount"));
                UserPreferencesUtil.setUserVisitCount(getActivity(), object.getString("visitCount"));
                JSONObject object1=new JSONObject(object.getString("authIdcard"));
                UserPreferencesUtil.setUserIDAuth(getActivity(), object1.getString("authStatus"));
                if (!object1.isNull("refuseReason")){
                    UserPreferencesUtil.setUserIDAuthRefuseReason(getActivity(), object1.getString("refuseReason"));
                }
                JSONObject object2=new JSONObject(object.getString("authVideo"));
                UserPreferencesUtil.setUserVideoAuth(getActivity(), object2.getString("authStatus"));
                UserPreferencesUtil.setUserVideoAuthUrl(getActivity(), object2.getString("videoUrl"));
                if (!object2.isNull("refuseReason")){
                    UserPreferencesUtil.setUserVideoAuthRefuseReason(getActivity(),object2.getString("refuseReason"));
                }
                initDatas();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
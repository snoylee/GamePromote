package com.xygame.second.sg.personal.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tendcloud.tenddata.TCAgent;
import com.xygame.second.sg.Group.GroupFrament;
import com.xygame.second.sg.biggod.activity.GodApplactionEditorActivity;
import com.xygame.second.sg.biggod.activity.GodApplactionFristActivity;
import com.xygame.second.sg.comm.activity.ActManagerActivity;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupListActivity;
import com.xygame.second.sg.personal.utils.ActionsFilter;
import com.xygame.second.sg.xiadan.activity.ManagementActivity;
import com.xygame.second.sg.xiadan.activity.XiaDanManageActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.QianBaoForModelActivity;
import com.xygame.sg.activity.personal.SettingActivity;
import com.xygame.sg.activity.personal.SettingWhoActivity;
import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MeFragment extends SGBaseFragment implements View.OnClickListener {

    private View playIcon,intoFansZhuView,videoCertify, idCertify, newsLayout, sex_age_bg, editorView, photoesView, vido_gou, id_gou,
            IZuanZhuRen, recent_rl, actionLayout, walletView, settingView,earnMoneyLine,earnMoneyView,helpView,isReadGetMoney;
    private ImageView sexIcon, idCertifyFlagImage,personalIcon;
    private CircularImage videoCertifyImage;
    private TextView userAcount, sexAge, userName, fenSiNums, heartText, recentNum,id_text,video_id;
    private ImageLoader imageLoader;
    private String fansCount;
    private ScrollView scrollView;

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
        initDatas();
    }

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = mInflater.inflate(R.layout.me_layout, null);
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        initViews(mView);
        initDatas();
        initListeners();
        return mView;
    }

    private void initViews(View mView) {
        video_id=(TextView)mView.findViewById(R.id.video_id);
        isReadGetMoney=mView.findViewById(R.id.isReadGetMoney);
        helpView=mView.findViewById(R.id.helpView);
        earnMoneyView=mView.findViewById(R.id.earnMoneyView);
        earnMoneyLine=mView.findViewById(R.id.earnMoneyLine);
        playIcon=mView.findViewById(R.id.playIcon);
        scrollView = (ScrollView) mView.findViewById(R.id.scrollView);
        intoFansZhuView=mView.findViewById(R.id.intoFansZhuView);
        recentNum = (TextView) mView.findViewById(R.id.recentNum);
        sex_age_bg = mView.findViewById(R.id.sex_age_bg);
        personalIcon = (ImageView) mView.findViewById(R.id.personalIcon);
        userAcount = (TextView) mView.findViewById(R.id.userAcount);
        editorView = mView.findViewById(R.id.editorView);
        fenSiNums = (TextView) mView.findViewById(R.id.fenSiNums);
        heartText = (TextView) mView.findViewById(R.id.heartText);
        videoCertify = mView.findViewById(R.id.videoCertify);
        idCertify = mView.findViewById(R.id.idCertify);
        idCertifyFlagImage = (ImageView) mView.findViewById(R.id.idCertifyFlagImage);
        id_text = (TextView)mView.findViewById(R.id.id_text);
        videoCertifyImage = (CircularImage) mView.findViewById(R.id.videoCertifyImage);
        sexIcon = (ImageView) mView.findViewById(R.id.sexIcon);
        newsLayout = mView.findViewById(R.id.newsLayout);
        sexAge = (TextView) mView.findViewById(R.id.sexAge);
        userName = (TextView) mView.findViewById(R.id.userName);
        photoesView = mView.findViewById(R.id.photoesView);
        vido_gou = mView.findViewById(R.id.vido_gou);
        id_gou = mView.findViewById(R.id.id_gou);
        IZuanZhuRen = mView.findViewById(R.id.IZuanZhuRen);
        recent_rl = mView.findViewById(R.id.recent_rl);
        actionLayout = mView.findViewById(R.id.actionLayout);
        walletView = mView.findViewById(R.id.walletView);
        settingView = mView.findViewById(R.id.settingView);
    }

    private void initDatas() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            userAcount.setText(UserPreferencesUtil.getUserPin(getActivity()));
            String visitCount = UserPreferencesUtil.getUserVisitCount(getActivity());
            String heartStr = UserPreferencesUtil.getHeartText(getActivity());
            if (!TextUtils.isEmpty(heartStr)) {
                heartText.setVisibility(View.VISIBLE);
                heartText.setText(heartStr);
            } else {
                heartText.setVisibility(View.GONE);
            }
            if (visitCount != null && !"0".equals(visitCount)) {
                recentNum.setVisibility(View.VISIBLE);
                recentNum.setText("");

            } else {
                recentNum.setVisibility(View.GONE);
            }

            if (fansCount != null && !"0".equals(fansCount)) {
                fenSiNums.setText(fansCount);
            } else {
                fenSiNums.setText("0");
            }

            String experAuth=UserPreferencesUtil.getExpertAuth(getActivity());
            earnMoneyLine.setVisibility(View.GONE);
            earnMoneyView.setVisibility(View.GONE);
            if ( !TextUtils.isEmpty(experAuth)) {
                if ("0".equals(experAuth)){
                    videoCertifyImage.setImageResource(R.drawable.nodash);
                    video_id.setText("申请大神");
                }else if ("1".equals(experAuth)){
                    videoCertifyImage.setImageResource(R.drawable.nodash);
                    video_id.setText("申请大神");
                }else if ("4".equals(experAuth)){
                    videoCertifyImage.setImageResource(R.drawable.nodash);
                    video_id.setText("申请大神");
                }else if ("2".equals(experAuth)){
                    videoCertifyImage.setImageResource(R.drawable.yesdash);
                    earnMoneyLine.setVisibility(View.VISIBLE);
                    earnMoneyView.setVisibility(View.VISIBLE);
                    video_id.setText("我是大神");
                }else if ("3".equals(experAuth)){
                    videoCertifyImage.setImageResource(R.drawable.nodash);
                    video_id.setText("申请大神");
                }
            } else {
                videoCertifyImage.setImageResource(R.drawable.nodash);
                video_id.setText("申请大神");
            }

            if (UserPreferencesUtil.getHeadPic(getActivity()) != null) {
                imageLoader.loadCircleImage(UserPreferencesUtil.getHeadPic(getActivity()), personalIcon, true);
            }
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
        } else {
            recentNum.setVisibility(View.GONE);
            fenSiNums.setText("0");
            personalIcon.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
            userName.setText("游客");
            id_text.setVisibility(View.VISIBLE);
            id_gou.setVisibility(View.GONE);
            vido_gou.setVisibility(View.GONE);
        }

        delayScorll();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    break;
                default:
                    break;
            }

        }
    };

    private void delayScorll() {
        new ThreadPool().excuseThread(new DelayScorllTime());
    }

    private class DelayScorllTime implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                android.os.Message m = handler.obtainMessage();
                m.what = 1;
                m.sendToTarget();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initListeners() {
        videoCertify.setOnClickListener(this);
        idCertify.setOnClickListener(this);
        actionLayout.setOnClickListener(this);
        settingView.setOnClickListener(this);
        walletView.setOnClickListener(this);
        recent_rl.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        editorView.setOnClickListener(this);
        personalIcon.setOnClickListener(this);
        intoFansZhuView.setOnClickListener(this);
        photoesView.setOnClickListener(this);
        earnMoneyView.setOnClickListener(this);
        helpView.setOnClickListener(this);
        IZuanZhuRen.setOnClickListener(this);
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                requestData();
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                initDatas();
            } else if (ActionsFilter.ACTION_ID_SUCCESS.equals(intent.getAction())) {
                UserPreferencesUtil.setUserIDAuth(getActivity(), "1");
                initDatas();
            } else if (ActionsFilter.ACTION_VIDEO_SUCCESS.equals(intent.getAction())) {
                UserPreferencesUtil.setUserVideoAuth(getActivity(), "1");
                initDatas();
            }else if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())){
            }else if ("com.xygame.push.dynamic.message.list.action".equals(intent.getAction())){
            }
        }
    };

    public void registerLoginListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
        myIntentFilter.addAction(ActionsFilter.ACTION_ID_SUCCESS);
        myIntentFilter.addAction(ActionsFilter.ACTION_VIDEO_SUCCESS);
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_ACTION);
        myIntentFilter.addAction("com.xygame.push.dynamic.message.list.action");
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener() {
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
            case R.id.helpView:
                Intent intent6 = new Intent(getActivity(), CommonWebViewActivity.class);
                intent6.putExtra("webUrl",Constants.ABOUT_HELP);
                intent6.putExtra("title","帮助");
                getActivity().startActivity(intent6);
                break;
            case R.id.earnMoneyView:
                Intent intent9 = new Intent(getActivity(), GodApplactionEditorActivity.class);
                getActivity().startActivity(intent9);
//                UserPreferencesUtil.setIsReadGetMoney(getActivity(),true);
//                Intent intent9 = new Intent(getActivity(), CommonWebViewActivity.class);
//                intent9.putExtra("webUrl",Constants.MAKE_MONEY);
//                intent9.putExtra("title","如何赚钱");
//                getActivity().startActivity(intent9);
                break;
            case R.id.personalIcon:
                if (!TextUtils.isEmpty(UserPreferencesUtil.getHeadPic(getActivity()))){
                    String[] tempImages=new String[1];
                    tempImages[0]=UserPreferencesUtil.getHeadPic(getActivity());
                    Constants.imageBrower(getActivity(),0,tempImages,false);
                }
                break;
            case R.id.editorView:
                Intent intent10 = new Intent(getActivity(), EditorInfoActivity.class);
                getActivity().startActivity(intent10);
                break;
            case R.id.settingView:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.videoCertify:
                if (!TextUtils.isEmpty(UserPreferencesUtil.getUserId(getActivity()))){
                    String experAuth=UserPreferencesUtil.getExpertAuth(getActivity());
                    if (!TextUtils.isEmpty(experAuth)) {
                        if ("0".equals(experAuth)){
                            Intent intent14= new Intent(getActivity(), GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",false);
                            startActivity(intent14);
                        }else if ("1".equals(experAuth)){
                            Intent intent14= new Intent(getActivity(), GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }else if ("4".equals(experAuth)){
                            Intent intent14= new Intent(getActivity(), GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }else if ("2".equals(experAuth)){
                            Intent intent14= new Intent(getActivity(), GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }else if ("3".equals(experAuth)){
                            Intent intent14= new Intent(getActivity(), GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }
                    } else {
                        Intent intent14= new Intent(getActivity(), GodApplactionFristActivity.class);
                        intent14.putExtra("isLoadStatus",false);
                        startActivity(intent14);
                    }
                }else{
                    showOneButtonDialog("请重新登录后重试！");
                }
                break;
            case R.id.walletView:
                Intent intent1 = new Intent(getActivity(), QianBaoForModelActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.idCertify:
                String idStatus = UserPreferencesUtil.getUserIDAuth(getActivity());
                if (TextUtils.isEmpty(idStatus)){
                    getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                }else if ("0".equals(idStatus)){
                    getActivity().startActivity(new Intent(getActivity(), ModelIdentyFirstActivity.class));
                }else if ("1".equals(idStatus)){
                    Toast.makeText(getActivity(),"您的身份认证审核中，请耐心等待",Toast.LENGTH_SHORT).show();
//                    showOneButtonDialog("您的身份认证审核中，请耐心等待");
                }else if ("4".equals(idStatus)){
                    Toast.makeText(getActivity(),"您的身份认证审核中，请耐心等待",Toast.LENGTH_SHORT).show();
//                    showOneButtonDialog("您的身份认证审核中，请耐心等待");
                }else if ("2".equals(idStatus)){
                    Toast.makeText(getActivity(),"您的身份认证已通过",Toast.LENGTH_SHORT).show();
//                    showOneButtonDialog("您的身份认证已通过");
                }else if ("3".equals(idStatus)){
                    id_gou.setVisibility(View.GONE);
                    showTwoButtonDialog("抱歉！您的身份认证被拒绝", "下次再说", "前往认证");
                }
                break;
            case R.id.actionLayout:
                Intent intent11 = new Intent(getActivity(), XiaDanManageActivity.class);
                startActivity(intent11);
                break;
            case R.id.recent_rl:
                recentNum.setVisibility(View.GONE);
                UserPreferencesUtil.setUserVisitCount(getActivity(), null);
                Intent intent5 = new Intent(getActivity(), MyRecentActivity.class);
                getActivity().startActivity(intent5);
                break;
            case R.id.newsLayout:
                Map kv = new HashMap();
                kv.put("统计类型", "通告群");
                TCAgent.onEvent(getActivity(), "首页活动", "通告群位", kv);
                Intent intent16 = new Intent(getActivity(), GroupFrament.class);
                getActivity().startActivity(intent16);
                break;
            case R.id.photoesView:
                Intent intent18 = new Intent(getActivity(), PohoesEditorActivity.class);
                getActivity().startActivity(intent18);
                break;
            case R.id.who_rl:
                Intent intent19 = new Intent(getActivity(), SettingWhoActivity.class);
                startActivity(intent19);
                break;
            case R.id.IZuanZhuRen:
                Intent intent12 = new Intent(getActivity(), GZ_GroupListActivity.class);
                getActivity().startActivity(intent12);
                break;
            case R.id.intoFansZhuView:
                Intent intent13 = new Intent(getActivity(), MyFansActivity.class);
                intent13.putExtra("userId",UserPreferencesUtil.getUserId(getActivity()));
                getActivity().startActivity(intent13);
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

    private void showOneButtonDialog(String content) {
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
                obj.put("userId", UserPreferencesUtil.getUserId(getActivity()));
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
        UserPreferencesUtil.setUserAttentCount(getActivity(),null);
        UserPreferencesUtil.setUserVisitCount(getActivity(), null);
        UserPreferencesUtil.setUserAge(getActivity(), null);
        UserPreferencesUtil.setSex(getActivity(), null);
        UserPreferencesUtil.setHeartText(getActivity(), null);
        UserPreferencesUtil.setHeadPic(getActivity(), null);
        UserPreferencesUtil.setUserPin(getActivity(), null);
        UserPreferencesUtil.setUserNickName(getActivity(), null);
        UserPreferencesUtil.setUserVideoAuthUrl(getActivity(), null);
        UserPreferencesUtil.setUserVideoAuth(getActivity(), null);
        UserPreferencesUtil.setUserIDAuth(getActivity(), null);
        UserPreferencesUtil.setUserCardAuth(getActivity(), null);
        UserPreferencesUtil.setExpertAuth(getActivity(),null);
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object = new JSONObject(record);
                UserPreferencesUtil.setUserAttentCount(getActivity(), StringUtils.getJsonValue(object, "attentCount"));
                UserPreferencesUtil.setUserVisitCount(getActivity(), StringUtils.getJsonValue(object, "visitCount"));
                fansCount = StringUtils.getJsonValue(object, "fansCount");

                JSONObject object2 = new JSONObject(StringUtils.getJsonValue(object, "base"));
                UserPreferencesUtil.setUserAge(getActivity(), StringUtils.getJsonValue(object2, "age"));
                UserPreferencesUtil.setSex(getActivity(), StringUtils.getJsonValue(object2, "gender"));
                UserPreferencesUtil.setHeartText(getActivity(), StringUtils.getJsonValue(object2, "introDesc"));
                UserPreferencesUtil.setHeadPic(getActivity(), StringUtils.getJsonValue(object2, "userIcon"));
                UserPreferencesUtil.setUserPin(getActivity(), StringUtils.getJsonValue(object2, "userPin"));
                UserPreferencesUtil.setUserNickName(getActivity(), StringUtils.getJsonValue(object2, "usernick"));
                UserPreferencesUtil.setUserVideoAuthUrl(getActivity(), StringUtils.getJsonValue(object, "video"));
                String auth=StringUtils.getJsonValue(object, "auth");
                if (!TextUtils.isEmpty(auth)){
                    JSONObject object1 = new JSONObject(auth);
                    UserPreferencesUtil.setUserVideoAuth(getActivity(), StringUtils.getJsonValue(object1, "videoAuth"));
                    UserPreferencesUtil.setUserIDAuth(getActivity(), StringUtils.getJsonValue(object1, "idcardAuth"));
                    UserPreferencesUtil.setUserCardAuth(getActivity(), StringUtils.getJsonValue(object1, "vcardAuth"));
                    UserPreferencesUtil.setExpertAuth(getActivity(),StringUtils.getJsonValue(object1,"expertAuth"));
                }
                initDatas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
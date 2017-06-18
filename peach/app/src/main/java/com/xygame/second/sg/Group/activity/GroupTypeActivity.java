package com.xygame.second.sg.Group.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.second.sg.Group.GroupNewNoticeEngeer;
import com.xygame.second.sg.Group.adapter.GroupTypeAdapter;
import com.xygame.second.sg.Group.bean.BlackGroupBean;
import com.xygame.second.sg.Group.bean.CityGroups;
import com.xygame.second.sg.Group.bean.GoupNoticeBean;
import com.xygame.second.sg.biggod.activity.GodApplactionFristActivity;
import com.xygame.second.sg.comm.activity.CertifyAlert;
import com.xygame.second.sg.personal.activity.VideoCertifyActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.personal.CMCardCertifyActivity;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.List;

/**
 * 聊天对话.
 */
public class GroupTypeActivity extends SGBaseActivity implements OnClickListener ,AdapterView.OnItemClickListener {
    private View backButton;
    private TextView titleName;
    private ListView listView;
    private CityGroups cityGroups;
    private List<GoupNoticeBean> goupNoticeBeans;
    private GroupTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_type_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        listView=(ListView)findViewById(R.id.listView);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void initDatas() {
        cityGroups = (CityGroups) getIntent().getSerializableExtra("bean");
        goupNoticeBeans=cityGroups.getGoupNoticeBeans();
        adapter = new GroupTypeAdapter(this, goupNoticeBeans);
        listView.setAdapter(adapter);
        AssetDataBaseManager.CityBean provinceBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(cityGroups.getProvinceId()));
        titleName.setText(provinceBean.getName());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isGo()) {
            GoupNoticeBean item = adapter.getItem(position);
            List<BlackGroupBean> allBlackGroups = GroupNewNoticeEngeer.quaryBlackGroups(this, UserPreferencesUtil.getUserId(this));
            boolean flag = true;
            for (BlackGroupBean it : allBlackGroups) {
                if (item.getGroupId().equals(it.getGroupId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Intent intent = new Intent(this, GroupNoticeChatActivity.class);
                intent.putExtra("bean", item);
                startActivityForResult(intent, 1);
            } else {
                showComfimDiloag("您已被踢出该群");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("mote".equals(flag)) {
                    String experAuth=UserPreferencesUtil.getExpertAuth(this);
                    if (!TextUtils.isEmpty(experAuth)) {
                        if ("0".equals(experAuth)){
                            Intent intent14= new Intent(this, GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",false);
                            startActivity(intent14);
                        }else if ("1".equals(experAuth)){
                            Intent intent14= new Intent(this, GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }else if ("4".equals(experAuth)){
                            Intent intent14= new Intent(this, GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }else if ("2".equals(experAuth)){
                            Intent intent14= new Intent(this, GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }else if ("3".equals(experAuth)){
                            Intent intent14= new Intent(this, GodApplactionFristActivity.class);
                            intent14.putExtra("isLoadStatus",true);
                            startActivity(intent14);
                        }
                    } else {
                        Intent intent14= new Intent(this, GodApplactionFristActivity.class);
                        intent14.putExtra("isLoadStatus",false);
                        startActivity(intent14);
                    }
                } else if ("jjr".equals(flag)) {
                    Intent intent = new Intent(this, CMCardCertifyActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case 1:
                GoupNoticeBean goupNoticeBean=(GoupNoticeBean)data.getSerializableExtra(Constants.COMEBACK);
                adapter.updateItemNums(goupNoticeBean);
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        List<GoupNoticeBean> tempDatas=adapter.getDatas();
        int subCount=0;
        for (GoupNoticeBean it:tempDatas){
            subCount=subCount+Integer.parseInt(it.getNoticeCount());
        }
        cityGroups.setNoticeCout(subCount);
        cityGroups.setGoupNoticeBeans(tempDatas);
        Intent intent = new Intent();
        intent.putExtra(Constants.COMEBACK, cityGroups);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

    private void showComfimDiloag(String tip) {
        OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                    }
                });
        dialog.show();
    }

    private boolean isGo() {

        boolean islogin = UserPreferencesUtil.isOnline(this);
        if (!islogin) {
            Intent intent = new Intent(this, LoginWelcomActivity.class);
            startActivity(intent);
            return false;
        }
        String experAuth = UserPreferencesUtil.getExpertAuth(this);
        String cardStatus = UserPreferencesUtil.getUserCardAuth(this);
        if ("1".equals(experAuth)) {
            showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
            return false;
        }

        if ("4".equals(experAuth)) {
            showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
            return false;
        }

        if ("1".equals(cardStatus)) {
            showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
            return false;
        }

        if ("4".equals(cardStatus)) {
            showOneButtonDialog("您提交的认证信息正在审核中，请耐心等待");
            return false;
        }

        if ("2".equals(cardStatus)||"2".equals(experAuth)){
            return true;
        }

        if (TextUtils.isEmpty(cardStatus) || TextUtils.isEmpty(experAuth) || "0".equals(cardStatus) || "0".equals(experAuth) || "3".equals(cardStatus) || "3".equals(experAuth)) {
            Intent intent = new Intent(this, CertifyAlert.class);
            startActivityForResult(intent, 0);
            return false;
        }
        return true;
    }

    private void showOneButtonDialog(String content) {
        OneButtonDialog dialog = new OneButtonDialog(this, content, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                    }
                });
        dialog.show();
    }
}

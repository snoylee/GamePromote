package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.guanzhu.group.GZ_GroupListForGroupChatActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

public class ChatDetailActivity extends SGBaseActivity implements OnClickListener {
    private SGNewsBean msgBean;
    private CircularImage userImage;
    /**
     * 公用变量部分
     */
    private TextView titleName, userName;
    private View backButton, clearButton, juBaoButton, addMemeberButton;
    private boolean flag = false;
    private ImageLoader imageLoader;
    private String whereFromFlag,attentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_detail_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        addMemeberButton = findViewById(R.id.addMemeberButton);
        backButton = findViewById(R.id.backButton);
        clearButton = findViewById(R.id.clearButton);
        juBaoButton = findViewById(R.id.juBaoButton);
        titleName = (TextView) findViewById(R.id.titleName);
        userName = (TextView) findViewById(R.id.userName);
        userImage = (CircularImage) findViewById(R.id.userImage);
    }

    private void initListensers() {
        juBaoButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        userImage.setOnClickListener(this);
        addMemeberButton.setOnClickListener(this);
    }

    private void initDatas() {
        whereFromFlag = getIntent().getStringExtra("whereFromFlag");
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("聊天详情");
        msgBean = (SGNewsBean) getIntent().getSerializableExtra("bean");
        userName.setText(msgBean.getFriendNickName());
        imageLoader.loadImage(msgBean.getFriendUserIcon(), userImage, true);
        String experAuth = UserPreferencesUtil.getExpertAuth(this);
        String cardStatus = UserPreferencesUtil.getUserCardAuth(this);
        if ( "2".equals(cardStatus) || "2".equals(experAuth)) {
            addMemeberButton.setVisibility(View.VISIBLE);
        } else {
            addMemeberButton.setVisibility(View.GONE);
        }
        loadDatas();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.clearButton) {
            showTipDiloag();
        } else if (v.getId() == R.id.juBaoButton) {
            Intent intent1 = new Intent(this, ReportFristActivity.class);
            intent1.putExtra("resType", Constants.JUBAO_TYPE_YUNHU);
            intent1.putExtra("userId", msgBean.getFriendUserId());
            intent1.putExtra("resourceId", msgBean.getFriendUserId());
            startActivity(intent1);
        } else if (v.getId() == R.id.userImage) {
            Intent intent = new Intent(this, PersonalDetailActivity.class);
            intent.putExtra("userNick", msgBean.getFriendNickName());
            intent.putExtra("userId", msgBean.getFriendUserId());
            startActivity(intent);
        } else if (v.getId() == R.id.addMemeberButton) {
            if (!TextUtils.isEmpty(attentResult)){
                if ("11".equals(attentResult)){
                    Intent intent = new Intent(this, GZ_GroupListForGroupChatActivity.class);
                    intent.putExtra("currUserId",msgBean);
                    startActivity(intent);
                }else if ("01".equals(attentResult)){
                    Toast.makeText(this,"您邀请的好友并未关注您，相互关注之后才能创建讨论组哦",Toast.LENGTH_SHORT).show();
                }else if ("10".equals(attentResult)){
                    Toast.makeText(this,"您并未关注您的好友，相互关注之后才能创建讨论组哦",Toast.LENGTH_SHORT).show();
                }else if ("00".equals(attentResult)){
                    Toast.makeText(this,"相互关注之后才能创建讨论组哦",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"网络故障，稍后刷新界面重试。",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, flag);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

    private void showTipDiloag() {
        TwoButtonDialog dialog = new TwoButtonDialog(ChatDetailActivity.this, "确定要删除所有聊天记录吗？", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        // TODO Auto-generated method stub
                        if ("orderChat".equals(whereFromFlag)) {
                            NewsEngine.deleteNoticeChatsHistory(ChatDetailActivity.this, msgBean, UserPreferencesUtil.getUserId(ChatDetailActivity.this));
                        } else {
                            NewsEngine.deleteChatsHistory(ChatDetailActivity.this, msgBean, UserPreferencesUtil.getUserId(ChatDetailActivity.this));
                        }
                        Toast.makeText(getApplicationContext(), "清除成功", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }

                    @Override
                    public void cancelListener() {

                    }
                });
        dialog.show();
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("fansId", msgBean.getFriendUserId());
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_HAS_ATTENT);
            ShowMsgDialog.showNoMsg(this, true);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_HAS_ATTENT);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_HAS_ATTENT:
                if ("0000".equals(data.getCode())) {
                    //返回两位数字，第一位是：对方是否关注我，第二位是：我是否关注对方     为0表示未关注，非0表示关注
                    attentResult=data.getRecord();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }
}

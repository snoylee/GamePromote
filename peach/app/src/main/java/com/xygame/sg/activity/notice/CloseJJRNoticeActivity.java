package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.bean.NoticeCloseVo;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.notice.CloseNotice;
import com.xygame.sg.task.notice.ToCloseNotice;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class CloseJJRNoticeActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private TextView rightButtonText;
    private ImageView rightbuttonIcon;
    private RelativeLayout rightButton;
    private EditText propose_et;
    private TextView num_count_tv;
    private Button ensure_close_bt;
    private String noticeId;
    private int maxNum = 100;
    private NoticeDetailVo notice;

    VisitUnit visitUnit = new VisitUnit(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.close_jjr_notice_close);
        notice = (NoticeDetailVo) getIntent().getSerializableExtra("notice");
        initViews();
        addListener();
    }

    private void initViews() {
        noticeId=getIntent().getStringExtra("noticeId");
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        rightButton = (RelativeLayout) findViewById(R.id.rightButton);

        titleName.setText(getText(R.string.title_activity_notice_close));
        rightButtonText.setVisibility(View.GONE);
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.sg_con_service);

        propose_et = (EditText) findViewById(R.id.propose_et);
        num_count_tv = (TextView) findViewById(R.id.num_count_tv);
        ensure_close_bt = (Button) findViewById(R.id.ensure_close_bt);

        num_count_tv.setText("0/".concat(String.valueOf(maxNum)));
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        ensure_close_bt.setOnClickListener(this);
        propose_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String proposeStr = s.toString();
                if ("".equals(proposeStr)) {
                    num_count_tv.setText("0/".concat(String.valueOf(maxNum)));
                } else {
                    num_count_tv.setText(String.valueOf(proposeStr.length()).concat("/").concat(String.valueOf(maxNum)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.rightButton:
                break;
            case R.id.ensure_close_bt:
                TwoButtonDialog dialog = new TwoButtonDialog(this, "是否确认关闭通告？", R.style.dineDialog, new ButtonTwoListener() {
                    @Override
                    public void confrimListener() {
                        closeNotice();
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
                dialog.show();
                break;

        }
    }

    private void closeNotice() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj=new JSONObject();
            obj.put("noticeId", noticeId);
            if (!"".equals(propose_et.getText().toString().trim())){
                obj.put("statusNote", propose_et.getText().toString().trim());
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_JJR_NOTICE_CLOSE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_JJR_NOTICE_CLOSE);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.RESPOSE_JJR_NOTICE_CLOSE:
                if ("0000".equals(data.getCode())) {
                    responseClose();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void responseClose() {
        Toast.makeText(this, "关闭成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("flag", true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

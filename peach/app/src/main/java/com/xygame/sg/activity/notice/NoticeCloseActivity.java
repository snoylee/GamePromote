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
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.task.notice.CloseNotice;
import com.xygame.sg.task.notice.ToCloseNotice;
import com.xygame.sg.utils.StringUtils;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class NoticeCloseActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private TextView rightButtonText;
    private ImageView rightbuttonIcon;
    private RelativeLayout rightButton;

    private TextView plan_num_tv;
    private TextView pre_payed_money_tv;
    private TextView employ_settle_num_tv;
    private TextView settle_money_tv;
    private TextView coupon_money_tv;
    private TextView refund_money_tv;
    private EditText propose_et;
    private TextView num_count_tv;
    private Button ensure_close_bt;

    private int maxNum = 100;
    private NoticeDetailVo notice;

    VisitUnit visitUnit = new VisitUnit(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_notice_close, null));
        notice = (NoticeDetailVo) getIntent().getSerializableExtra("notice");
        initViews();
        requestDate();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        rightButton = (RelativeLayout) findViewById(R.id.rightButton);

        titleName.setText(getText(R.string.title_activity_notice_close));
        rightButtonText.setVisibility(View.GONE);
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.sg_con_service);

        plan_num_tv = (TextView) findViewById(R.id.plan_num_tv);
        pre_payed_money_tv = (TextView) findViewById(R.id.pre_payed_money_tv);
        employ_settle_num_tv = (TextView) findViewById(R.id.employ_settle_num_tv);
        settle_money_tv = (TextView) findViewById(R.id.settle_money_tv);
        coupon_money_tv = (TextView) findViewById(R.id.coupon_money_tv);
        refund_money_tv = (TextView) findViewById(R.id.refund_money_tv);
        propose_et = (EditText) findViewById(R.id.propose_et);
        num_count_tv = (TextView) findViewById(R.id.num_count_tv);
        ensure_close_bt = (Button) findViewById(R.id.ensure_close_bt);

        num_count_tv.setText("0/".concat(String.valueOf(maxNum)));

//        int pre_payed_money = notice.getPayAmount();
//        int plan_num = 0;
//        int employ_settle_num= 0;
//        int settle_money = 0;
//        int coupon_money = 0;
//        for (NoticeRecruitVo noticeRecruitVo :notice.getRecruits()){
//            plan_num += noticeRecruitVo.getCount();
//            employ_settle_num += noticeRecruitVo.getOkSum();
//            settle_money += noticeRecruitVo.getOkSum()*noticeRecruitVo.getReward();
//        }
//
//        int refund_money_temp = pre_payed_money - settle_money - coupon_money;
//        int refund_money = refund_money_temp> 0 ? refund_money_temp : 0;
//
//        plan_num_tv.setText(plan_num+"");
//        pre_payed_money_tv.setText("￥"+pre_payed_money);
//        employ_settle_num_tv.setText(employ_settle_num+"");
//        settle_money_tv.setText("-￥"+settle_money);
//        coupon_money_tv.setText("-￥"+coupon_money);
//        refund_money_tv.setText("￥"+refund_money);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

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
    private void requestDate() {
        visitUnit.getDataUnit().getRepo().put("noticeId", notice.getNoticeId());
        new Action(ToCloseNotice.class, "${toCloseNotice},noticeId=${noticeId}", this, null, visitUnit).run();
    }
    private void requestClose() {
        visitUnit.getDataUnit().getRepo().put("noticeId", notice.getNoticeId());
        visitUnit.getDataUnit().getRepo().put("reason", propose_et.getText().toString());
        new Action(CloseNotice.class, "${closeNotice},noticeId=${noticeId},reason=${reason}", this, null, visitUnit).run();
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
                        requestClose();
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
                dialog.show();
                break;

        }
    }

    public void responseClose() {
        Toast.makeText(this, "关闭成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void responseToClose(NoticeCloseVo toCloseNotice) {
        plan_num_tv.setText(toCloseNotice.getTotal()+"");
        pre_payed_money_tv.setText("￥"+ StringUtils.getFormatMoney(toCloseNotice.getPrepay()+""));
        employ_settle_num_tv.setText(toCloseNotice.getReceiptNum()+"");
        settle_money_tv.setText("-￥" + StringUtils.getFormatMoney(toCloseNotice.getReceiptAmount() + ""));
        coupon_money_tv.setText("-￥" + StringUtils.getFormatMoney(toCloseNotice.getCouponAmount() + ""));
        refund_money_tv.setText("￥" + StringUtils.getFormatMoney(toCloseNotice.getBalance() + ""));

        ensure_close_bt.setOnClickListener(this);
    }

}

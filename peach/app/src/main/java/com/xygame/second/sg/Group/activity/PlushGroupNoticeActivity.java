package com.xygame.second.sg.Group.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.Group.bean.GroupNoticeItemBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.define.view.ChoiceDateWeekTimer;
import com.xygame.sg.utils.Constants;

/**
 * 聊天对话.
 */
public class PlushGroupNoticeActivity extends SGBaseActivity implements OnClickListener {
    private View backButton,rightButton,timeView;
    private TextView titleName,rightButtonText,timerText;
    private EditText titleTxt,oralText,priceText,areaText;
    private PlushNoticeBean pnBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_notice_plush);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        timeView=findViewById(R.id.timeView);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        rightButtonText = (TextView)findViewById(R.id.rightButtonText);
        titleName = (TextView) findViewById(R.id.titleName);
        oralText=(EditText)findViewById(R.id.oralText);
        priceText=(EditText)findViewById(R.id.priceText);
        areaText=(EditText)findViewById(R.id.areaText);
        timerText=(TextView)findViewById(R.id.timerText);
        titleTxt=(EditText)findViewById(R.id.titleTxt);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        timeView.setOnClickListener(this);
    }

    private void initDatas() {
        pnBean = new PlushNoticeBean();
        titleName.setText("发布通告");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("确定");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            if (isGo()){
                GroupNoticeItemBean itemBean=new GroupNoticeItemBean();
                itemBean.setAddress(areaText.getText().toString().trim());
                itemBean.setOral(oralText.getText().toString().trim());
                itemBean.setPrice(priceText.getText().toString().trim());
                itemBean.setTimer(pnBean.getStartTimeDes());
                itemBean.setTitle(titleTxt.getText().toString().trim());
                Intent intent = new Intent();
                intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, itemBean);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }else if (v.getId()==R.id.timeView){
            Intent firstIntent = new Intent(this, ChoiceDateWeekTimer.class);
            startActivityForResult(firstIntent, 0);
        }
    }

    private boolean isGo() {
        String titleTxtStr=titleTxt.getText().toString().trim();
        if (TextUtils.isEmpty(titleTxtStr)){
            Toast.makeText(this,"请输入标题",Toast.LENGTH_SHORT).show();
            return false;
        }
        String timerTextStr=timerText.getText().toString().trim();
        if (TextUtils.isEmpty(timerTextStr)){
            Toast.makeText(this,"请选择时间",Toast.LENGTH_SHORT).show();
            return false;
        }
        String areaTextStr=areaText.getText().toString().trim();
        if (TextUtils.isEmpty(areaTextStr)){
            Toast.makeText(this,"请输入地址",Toast.LENGTH_SHORT).show();
            return false;
        }
        String priceTextStr=priceText.getText().toString().trim();
        if (TextUtils.isEmpty(priceTextStr)){
            Toast.makeText(this,"请输入价格",Toast.LENGTH_SHORT).show();
            return false;
        }
        String oralTextStr=oralText.getText().toString().trim();
        if (TextUtils.isEmpty(oralTextStr)){
            Toast.makeText(this,"请输入说明",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                FeedbackDateBean ftBean2 = (FeedbackDateBean) data.getSerializableExtra("bean");
                if (ftBean2 != null) {
                    pnBean.setStarTime(ftBean2.getTimeLong());
                    pnBean.setStartTimeDes(ftBean2.getDateAllDesc());
                    pnBean.setFormatStartTime(ftBean2.getFormatDateStr());
                    timerText.setText(pnBean.getStartTimeDes());
                }
                break;
            default:
                break;
        }
    }
}

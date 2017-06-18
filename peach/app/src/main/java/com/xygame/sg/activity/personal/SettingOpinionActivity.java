package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.SystemServiceBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.PatternUtils;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

public class SettingOpinionActivity extends SGBaseActivity implements View.OnClickListener{
    private View backButton,rightButton;
    private TextView titleName;
    private EditText opinion_et;
    private TextView num_count_tv;
    private EditText contact_way_et;
    private TextView service_phone_tv;
    private TextView service_qq_tv;
    private TextView service_mail_tv;
    private TextView service_time_tv;
    private Button submit_bt;
    private ImageView rightbuttonIcon;
    private int maxNum = 500;
    private SystemServiceBean serviceBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_opinion);
        initViews();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        rightButton=findViewById(R.id.rightButton);
        rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getText(R.string.title_activity_setting_opinion));

        opinion_et = (EditText) findViewById(R.id.opinion_et);
        num_count_tv = (TextView) findViewById(R.id.num_count_tv);
        contact_way_et = (EditText) findViewById(R.id.contact_way_et);
        service_phone_tv = (TextView) findViewById(R.id.service_phone_tv);
        service_qq_tv = (TextView) findViewById(R.id.service_qq_tv);
        service_mail_tv = (TextView) findViewById(R.id.service_mail_tv);
        service_time_tv = (TextView) findViewById(R.id.service_time_tv);
        submit_bt = (Button) findViewById(R.id.submit_bt);
//        rightbuttonIcon.setVisibility(View.VISIBLE);
//        rightbuttonIcon.setImageResource(R.drawable.sg_con_service);
        num_count_tv.setText("0/".concat(String.valueOf(maxNum)));

        serviceBean= CacheService.getInstance().getCacheSystemServiceBean(ConstTaskTag.CACHE_SERVICE_BEAN);

        if (serviceBean!=null){
            service_phone_tv.setText(serviceBean.getServicePhone());
            service_qq_tv.setText(serviceBean.getServiceQQ());
            service_mail_tv.setText(serviceBean.getServiceEmail());
            service_time_tv.setText(serviceBean.getServiceTime());
        }else{
            queryCustomerServicePhone();
        }
    }

    @Override
    public void showServiceInfo() {
        super.showServiceInfo();
        serviceBean= CacheService.getInstance().getCacheSystemServiceBean(ConstTaskTag.CACHE_SERVICE_BEAN);

        if (serviceBean!=null){
            service_phone_tv.setText(serviceBean.getServicePhone());
            service_qq_tv.setText(serviceBean.getServiceQQ());
            service_mail_tv.setText(serviceBean.getServiceEmail());
            service_time_tv.setText(serviceBean.getServiceTime());
        }
    }

    private void addListener() {
//        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        opinion_et.addTextChangedListener(new TextWatcher() {
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
        service_phone_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + service_phone_tv.getText().toString()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.submit_bt:
                commit();
                break;
//            case R.id.rightButton:
//                if (serviceBean!=null){
//                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + serviceBean.getServicePhone()));
//                    startActivity(intent);
//                }else{
//                    requestServicePhone();
//                    Toast.makeText(this, "数据请求中，稍后重试", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }

    private void commit() {
        String opinion = opinion_et.getText().toString();
        if ("".equals(opinion)) {
            Toast.makeText(getApplicationContext(), "请输入您的意见或建议！", Toast.LENGTH_SHORT).show();
            return ;
        }

        if (opinion.length() < 10 ) {
            Toast.makeText(getApplicationContext(), "请至少输入10个字啊！", Toast.LENGTH_SHORT).show();
            return ;
        }
        String contact = contact_way_et.getText().toString();
        if (!contact.equals("")){
            if (!PatternUtils.getMobilePhonePattern().matcher(contact).matches() && !PatternUtils.getEmailPattern().matcher(contact).matches()){
                Toast.makeText(getApplicationContext(), "请输入正确的手机号或邮箱！", Toast.LENGTH_SHORT).show();
                return ;
            }
        }
        coimmitAction(opinion, contact);
    }

    private void coimmitAction(String content,String contact) {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("content", content);
            if (!contact.equals("")){
                obj.put("contact", contact);
            }
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_FEEDBACK);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_FEEDBACK);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_FEEDBACK:
                if ("0000".equals(data.getCode())) {
                    Toast.makeText(this,"反馈成功，谢谢！",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }
}

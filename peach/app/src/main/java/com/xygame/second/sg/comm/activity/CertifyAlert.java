package com.xygame.second.sg.comm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

/**
 * Created by tony on 2016/9/12.
 */
public class CertifyAlert extends SGBaseActivity implements View.OnClickListener {

    private View closeLoginWel,jjrCertify,moteCertify;

    /**
     * 重载方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certify_alert_layout);
        initViews();
        initListeners();
        initDatas();
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        moteCertify=findViewById(R.id.moteCertify);
        jjrCertify=findViewById(R.id.jjrCertify);
        closeLoginWel=findViewById(R.id.closeLoginWel);
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        moteCertify.setOnClickListener(this);
        jjrCertify.setOnClickListener(this);
        closeLoginWel.setOnClickListener(this);
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @action [请添加内容描述]
     */
    private void initDatas() {
        // TODO Auto-generated method stub

    }

    /**
     * 重载方法
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
       if(v.getId()==R.id.moteCertify){
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK, "mote");
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else if(v.getId()==R.id.closeLoginWel){
            finish();
        }else if (v.getId()==R.id.jjrCertify){
           Intent intent = new Intent();
           intent.putExtra(Constants.COMEBACK, "jjr");
           setResult(Activity.RESULT_OK, intent);
           finish();
        }
    }
}
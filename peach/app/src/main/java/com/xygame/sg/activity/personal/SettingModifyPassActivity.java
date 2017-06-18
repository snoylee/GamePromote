package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.task.indivual.EditUserPassword;
import com.xygame.sg.task.notice.CloseNotice;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.action.CenterRepo;
import base.frame.VisitUnit;

public class SettingModifyPassActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton, comfirm;
    private EditText firstPwd, secondPwd,oldPwd;
    private String fP ;
    private String sp;
    private String op;
    VisitUnit visitUnit = new VisitUnit(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_setting_modify_pass, null));
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        comfirm = findViewById(R.id.comfirm);
        firstPwd = (EditText) findViewById(R.id.firstPwd);
        secondPwd = (EditText) findViewById(R.id.secondPwd);
        oldPwd=(EditText)findViewById(R.id.oldPwd);
    }

    private void initListensers() {
        backButton.setOnClickListener(this);
        comfirm.setOnClickListener(this);
    }

    private void initDatas() {
        titleName.setText(getResources().getString(R.string.title_activity_setting_modify_pass));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.comfirm) {
            if (isgo()) {
                commitPay();
            }
        }
    }

    private boolean isgo() {
        boolean flag = true;
        fP = firstPwd.getText().toString().trim();
        sp = secondPwd.getText().toString().trim();
        op=oldPwd.getText().toString().trim();
        if ("".equals(op)) {
            Toast.makeText(getApplicationContext(), "请输入旧登录密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(fP)) {
            Toast.makeText(getApplicationContext(), "请输入6-15位新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fP.length() < 6 ||fP.length() > 15) {
            Toast.makeText(getApplicationContext(), "密码长度需在6-15位之内", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(sp)) {
            Toast.makeText(getApplicationContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sp.length() < 6||sp.length() > 15) {
            Toast.makeText(getApplicationContext(), "密码长度需在6-15位之内", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!fP.equals(sp)) {
            Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fP.equals(sp)) {
            return true;
        }
        return flag;
    }

    private void commitPay() {

        visitUnit.getDataUnit().getRepo().put("telephone", UserPreferencesUtil.getCellPhone(this));
        visitUnit.getDataUnit().getRepo().put("oldPwd", DigestUtils.md5Hex(op.concat("sgappkey")));
        visitUnit.getDataUnit().getRepo().put("newPwd", DigestUtils.md5Hex(sp.concat("sgappkey")));
        new Action(EditUserPassword.class, "${editUserPassword},telephone=${telephone},oldPwd=${oldPwd},newPwd=${newPwd}", this, null, visitUnit).run();
    }

    public void resultInfo() {
        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        finish();
    }

}

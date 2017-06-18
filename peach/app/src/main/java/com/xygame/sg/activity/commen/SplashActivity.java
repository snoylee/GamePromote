package com.xygame.sg.activity.commen;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.main.MainFrameActivity;
import com.xygame.sg.activity.testpay.LensFocusActivity;
import com.xygame.sg.task.comm.QueryModelStyleType;
import com.xygame.sg.task.indivual.QueryModelShootType;

import java.util.List;

import base.action.Action;
import base.frame.VisitUnit;

public class SplashActivity extends SGBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("startcount", MODE_MULTI_PROCESS);
                int count = preferences.getInt("startcount", 0);
                if (count == 0) {// 判断程序第几次运行，如果是第一次运行则跳转到引导页面
                    Intent intent = new Intent();
                    intent = new Intent(SplashActivity.this, IndexActivity.class);
                    startActivity(intent);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("startcount", ++count);// 存入数据
                    editor.apply();// 提交修改
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent = new Intent(SplashActivity.this, MainFrameActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1 * 1000);
    }
}

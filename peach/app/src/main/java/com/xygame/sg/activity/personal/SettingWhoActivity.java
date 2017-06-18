package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.webview.BaseWebChromeClient;
import com.xygame.sg.activity.webview.BaseWebViewClient;
import com.xygame.sg.activity.webview.WebSetting;
import com.xygame.sg.utils.Constants;

import base.ViewBinder;
import base.frame.VisitUnit;

public class SettingWhoActivity extends  SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton;
    private WebView mWebView;
    private String webUrl;
    VisitUnit visitUnit = new VisitUnit(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_setting_who, null));
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        titleName.setText(getResources().getString(R.string.title_activity_setting_who));

        WebSettings webSettings = mWebView.getSettings();
        WebSetting.setWebSettings(webSettings);
        webUrl = Constants.WHO_IS_URL;

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setWebViewClient(new BaseWebViewClient(this));
        mWebView.setWebChromeClient(new BaseWebChromeClient(this));
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        mWebView.loadUrl(webUrl);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        mWebView.removeAllViews();
        mWebView.destroy();
        super.onDestroy();
    }
}

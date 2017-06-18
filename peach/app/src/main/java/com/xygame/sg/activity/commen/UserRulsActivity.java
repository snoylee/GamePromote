/*
 * 文 件 名:  UserRulsActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.webview.BaseWebChromeClient;
import com.xygame.sg.activity.webview.BaseWebViewClient;
import com.xygame.sg.activity.webview.InJavaScript;
import com.xygame.sg.activity.webview.WebSetting;
import com.xygame.sg.utils.Constants;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月3日
 * @action  [用户协议界面]
 */
public class UserRulsActivity extends SGBaseActivity implements OnClickListener{
	
	private View backLoginWel,closeLoginWel;
	private WebView mWebView;
	private String webUrl= Constants.AGREEMENT_URL;

	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_user_ruls_layout);
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
		backLoginWel=findViewById(R.id.backLoginWel);
		closeLoginWel=findViewById(R.id.closeLoginWel);
		mWebView = (WebView) findViewById(R.id.webView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backLoginWel.setOnClickListener(this);
		closeLoginWel.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		WebSettings webSettings = mWebView.getSettings();
		WebSetting.setWebSettings(webSettings);

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
		// TODO Auto-generated method stub
		if(v.getId()==R.id.backLoginWel){
			finish();
		}else if(v.getId()==R.id.closeLoginWel){
			finish();
		}
	}
}

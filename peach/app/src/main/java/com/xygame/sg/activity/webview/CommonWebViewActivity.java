package com.xygame.sg.activity.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.activity.model.bean.Param;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.model.JoinBDActivity;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class CommonWebViewActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton;
    private WebView mWebView;
    private String webUrl = "";
    private String title = "";
    private BannerBean bannerBean;
    VisitUnit visitUnit = new VisitUnit(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_common_web_view, null));
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        mWebView = (WebView) findViewById(R.id.webView);
        if (!StringUtils.isEmpty(getIntent().getStringExtra("webUrl"))) {
            webUrl = getIntent().getStringExtra("webUrl");
        }
        if (!StringUtils.isEmpty(getIntent().getStringExtra("title"))) {
            title = getIntent().getStringExtra("title");
        }
        titleName.setText(title);
        if (getIntent().hasExtra("bannerBean")) {
            bannerBean = (BannerBean) getIntent().getSerializableExtra("bannerBean");
        }
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
    }

    private void initDatas() {

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);// 这个很关键
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setVerticalScrollBarEnabled(false);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
//        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.addJavascriptInterface(new InJavaScript(this, mHandler, mWebView), "_auth");

//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.setWebChromeClient(new HellowebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(CommonWebViewActivity.this)
                        .setTitle("提示：").setMessage(message)
                        .setPositiveButton("ok",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                        // MyWebView.this.finish();
                                    }
                                });

                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
        });
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
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:

                        break;
                    case 1:
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mWebView.removeAllViews();
        mWebView.destroy();
        super.onDestroy();
    }

    public BannerBean getBannerBean() {
        return bannerBean;
    }

    public void setBannerBean(BannerBean bannerBean) {
        this.bannerBean = bannerBean;
    }

    public void joinAct() {
        try{
//            Param param = JSON.parseObject(bannerBean.getParam(), Param.class);
            JSONObject object=new JSONObject(bannerBean.getParam());
            Param param = new Param();
            param.setActId(StringUtils.getJsonIntValue(object,"actId"));
            param.setJoinType(StringUtils.getJsonIntValue(object,"joinType"));
            visitUnit.getDataUnit().getRepo().put("actId", param.getActId());
            visitUnit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(this));
            new Action(JoinBDActivity.class, "${joinBDActivity},actId=${actId},userId=${userId}", this, null, visitUnit).run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resAct(String code, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (code.equals("0000")) {
            mWebView.loadUrl(String.format("javascript:signupResult(1)"));//这里是java端调用webview的JS
        } else if (code.equals("5000")) {//已报名
            mWebView.loadUrl(String.format("javascript:signupResult(2)"));//这里是java端调用webview的JS
        } else {
            mWebView.loadUrl(String.format("javascript:signupResult(3)"));//这里是java端调用webview的JS
        }

    }

    public void requestCommend(String url, String param) {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject(param);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setOsType("android-web");
        item.setServiceURL(Constants.baseUrl.concat("/").concat(url));
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_JS_COD);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.RESPOSE_JS_COD:
                try {
                    JSONObject object = new JSONObject();
                    object.put("code", data.getCode());
                    object.put("functionCode", data.getFunctionCode());
                    object.put("record", data.getRecord());
                    object.put("timeStamp", data.getTimeStamp());
                    String result = object.toString();
                    mWebView.loadUrl(String.format("javascript:responseData(" + result + ")"));//通用调用接口反回值
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
//        String result = "";
//        mWebView.loadUrl(String.format("javascript:responseData(" + result + ")"));//通用调用接口反回值
    }

    /**
     * 解决webview中无法弹出alert的问题
     */
    private class HellowebViewClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            AlertDialog.Builder b2 = new AlertDialog.Builder(CommonWebViewActivity.this)
                    .setTitle("提示：").setMessage(message)
                    .setPositiveButton("确定",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.confirm();
                                }
                            });
            b2.setCancelable(false);
            b2.create();
            b2.show();
            return true;
        }
    }
}

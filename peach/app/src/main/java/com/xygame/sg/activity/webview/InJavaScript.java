package com.xygame.sg.activity.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.activity.model.bean.Param;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;


/**
 * Created by yingsoft on 2015/3/24.
 */
public class InJavaScript {
    private Activity mContext;
    private Handler mHandler;
    private WebView mWebView;


    public InJavaScript(Activity mContext, Handler mHandler, WebView mWebView) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWebView = mWebView;
    }


    @JavascriptInterface
    public void reload() {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.reload();
            }
        });
    }


    /**
     * 进入模特或摄影师详情
     *
     * @param userId
     * @param userType 类别：0模特，1摄影师
     */
    @JavascriptInterface
    public void goUserDetail(String userId, String userType) {
        if (userType.equals("0")){
            Intent intent = new Intent(mContext, PersonInfoActivity.class);
            intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
            intent.putExtra("userId",userId+"");
            mContext.startActivity(intent);
        } else if(userType.equals("1")){
            Intent intent = new Intent(mContext, CMPersonInfoActivity.class);
            intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
            intent.putExtra("userId",userId+"");
            mContext.startActivity(intent);
        }

    }

    /**
     * 进入通告详情
     *
     * @param noticeId
     */
    @JavascriptInterface
    public void goMsgDetail(String noticeId) {
        Intent intent = new Intent(mContext, NoticeDetailActivity.class);
        intent.putExtra("noticeId",noticeId);
        intent.putExtra("isQuery",true);
        mContext.startActivity(intent);

    }

    /**
     * 通用调用接口
     */
    @JavascriptInterface
    public void requestData(String url,String param) {
        ((CommonWebViewActivity)mContext).requestCommend(url, param);
    }

    @JavascriptInterface
    public void eventSignup() {
        if(!UserPreferencesUtil.isOnline(mContext)){
            Intent intent = new Intent(mContext, LoginWelcomActivity.class);
            mContext.startActivity(intent);
        } else {
            BannerBean bannerBean = ((CommonWebViewActivity)mContext).getBannerBean();
            if (!StringUtils.isEmpty(bannerBean.getParam())){
                try {
                    JSONObject object=new JSONObject(bannerBean.getParam());
                    Param param = new Param();
                    param.setActId(StringUtils.getJsonIntValue(object,"actId"));
                    param.setJoinType(StringUtils.getJsonIntValue(object,"joinType"));
                    //JSON.parseObject(bannerBean.getParam(),Param.class);
                    if (canSignup(param)){
                        ((CommonWebViewActivity)mContext).joinAct();
                    } else {
                        Toast.makeText(mContext,"抱歉您不能参与本次活动!",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean canSignup(Param param){
        String joinType = param.getJoinType()+"";//2表示模特，3表示未认证模特，4表示高级模特，5表示未认证高级模特，8表示摄影师，9表示未认证摄影师，1：全部
        if (joinType.contains("1")){
            return true;
        }
//        String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(mContext);
//        if (!StringUtils.isEmpty(jsonStr)){
//            List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//            if (resultList == null || resultList.size() == 0) {
//                return false;
//            }
//            for (IdentyBean identyBean:resultList){
//                String type = null;
//                if (identyBean.getAuthStatus() == 2) {
//                    type = String.valueOf(identyBean.getUserType());
//                } else {
//                    type = String.valueOf(identyBean.getUserType() + 1);
//                }
//                if (joinType.contains(type)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }


}

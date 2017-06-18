package com.xygame.sg.activity.testpay;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.sg.R;

public class LensFocusActivity extends Activity {


    private ImageView imageView_pic;
    private View parentView;
    /**
     * 三个切换的动画
     */
    private Animation mFadeIn;
    private Animation mFadeInScale;

    /**
     * 三个图片
     */
//	private Drawable mPicture_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_splash_lens_focus, null);
        setContentView(parentView);
        initView();
    }

    public void initView() {
        imageView_pic = (ImageView) parentView.findViewById(R.id.imageView_pic);
        imageView_pic.setImageResource(R.drawable.animtion_page);
        init();
    }

    private void init() {
        initAnim();
        setListener();
        parentView.startAnimation(mFadeIn);
    }


    /**
     * 初始化动画
     */
    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
        mFadeIn.setDuration(1000);
        mFadeInScale = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(3000);
    }

    /**
     * 监听事件
     */
    public void setListener() {
        /**
         * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
         * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
         */
        mFadeIn.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                parentView.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
//
//				!!!!!!!!!!!!!!!!!!
            }
        });
    }
}

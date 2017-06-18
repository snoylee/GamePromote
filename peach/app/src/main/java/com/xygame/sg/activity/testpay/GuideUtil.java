package com.xygame.sg.activity.testpay;

/**
 * Created by xy on 2016/2/17.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xygame.sg.R;

/**
 * @类名:GuideUtil * @类描述:引导工具界面 * @修改人: * @修改时间: * @修改备注: * @版本:
 */
public class GuideUtil {
    private Context context;
    private ImageView imgView;
    private WindowManager windowManager;
    private static GuideUtil instance = null;
    /**
     * 是否第一次进入该程序
     **/
    private boolean isFirst = true;

    /**
     * 采用私有的方式，只保证这种通过单例来引用，同时保证这个对象不会存在多个
     **/
    private GuideUtil() {
    }

    /**
     * 采用单例的设计模式，同时用了同步锁
     **/
    public static GuideUtil getInstance() {
        synchronized (GuideUtil.class) {
            if (null == instance) {
                instance = new GuideUtil();
            }
        }
        return instance;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:                // 设置LayoutParams参数
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    // 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;                // 设置显示格式
                    params.format = PixelFormat.RGBA_8888;                // 设置对齐方式
                    params.gravity = Gravity.LEFT | Gravity.TOP;                // 设置宽高
                    params.width = ScreenUtils.getScreenWidth(context);
                    params.height = ScreenUtils.getScreenHeight(context);                // 设置动画
//                    params.windowAnimations = R.style.view_anim;                // 添加到当前的窗口上
                    windowManager.addView(imgView, params);
                    break;
            }
        }

    };

    /**
     * @方法说明:初始化 * @方法名称:initGuide     * @param context     * @param drawableRourcesId：引导图片的资源Id     * @返回值:void
     */
    public void initGuide(Activity context, int drawableRourcesId) {
        /**如果不是第一次进入该界面**/
        if (!isFirst) {
            return;
        }
        this.context = context;
        windowManager = context.getWindowManager();        /** 动态初始化图层**/
        imgView = new ImageView(context);
        imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setImageResource(drawableRourcesId);        /**这里我特意用了一个handler延迟显示界面，主要是为了进入界面后，你能看到它淡入得动画效果，不然的话，引导界面就直接显示出来**/
        handler.sendEmptyMessageDelayed(1, 1000);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {                /** 点击图层之后，将图层移除**/
                windowManager.removeView(imgView);
            }
        });
    }

    public boolean isFirst() {
        return isFirst;
    }

    /**
     * @方法说明:设置是否第一次进入该程序 * @方法名称:setFirst     * @param isFirst     * @返回值:void
     */
    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }
}

class ScreenUtils {
    /**
     * @方法说明:获取DisplayMetrics对象 * @方法名称:getDisPlayMetrics     * @param context     * @return     * @返回值:DisplayMetrics
     */
    public static DisplayMetrics getDisPlayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        if (null != context) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        }
        return metric;
    }

    /**
     * @方法说明:获取屏幕的宽度（像素） * @方法名称:getScreenWidth     * @param context     * @return     * @返回值:int
     */
    public static int getScreenWidth(Context context) {
        int width = getDisPlayMetrics(context).widthPixels;
        return width;
    }

    /**
     * @方法说明:获取屏幕的高（像素） * @方法名称:getScreenHeight     * @param context     * @return     * @返回值:int
     */
    public static int getScreenHeight(Context context) {
        int height = getDisPlayMetrics(context).heightPixels;
        return height;
    }

    /**
     * @方法说明:屏幕密度(0.75 / 1.0 / 1.5)     * @方法名称:getDensity     * @param context     * @return     * @返回 float
     */
    public static float getDensity(Context context) {
        float density = getDisPlayMetrics(context).density;
        return density;
    }

    /**
     * @方法说明:屏幕密度DPI(120 / 160 / 240)     * @方法名称:getDensityDpi     * @param context     * @return     * @返回 int
     */
    public static int getDensityDpi(Context context) {
        int densityDpi = getDisPlayMetrics(context).densityDpi;
        return densityDpi;
    }
}

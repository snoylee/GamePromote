package base.log;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import  android.app.Activity;

/**
 * Created by minhua on 2015/10/21.
 */
public class Logui {
    public
    static Logui logUI = new Logui();
    public static Logui getInstance(){
        return logUI;
    }
    Activity activity;
    TextView tv;
    public void setActivity(Activity activity) {
        this.activity = activity;
        if(activity.getWindow().findViewById(Integer.parseInt("9999"))==null){
            LayoutParams lp = new LayoutParams(-1,60);
            lp.gravity = Gravity.BOTTOM;
            tv = new TextView(activity);
            tv.setText("====");
            tv.setId(Integer.parseInt("9999"));
            LinearLayout ll =  new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);
            View view = ((ViewGroup)activity.getWindow().getDecorView()).getChildAt(0);
            ((ViewGroup)activity.getWindow().getDecorView()).removeView(view);


            ll.addView(tv);
            ll.addView(view,0);
            ((ViewGroup)activity.getWindow().getDecorView()).addView(ll);
        }

    }

    public void log(String msg){


        tv.setText(msg);
    }
}

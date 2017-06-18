package base.action;


import android.view.View;
import android.widget.TextView;

import base.adapter.Logs;

/**
 * Created by minhua on 2015/10/23.
 */
public class FocusListener extends Listener implements View.OnFocusChangeListener {

    public FocusListener(Action action) {
        super(action);
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if(b){
            runAction(view);
            Logs.e("-------------------- b"+((TextView)view).getText());
        }
    }
}

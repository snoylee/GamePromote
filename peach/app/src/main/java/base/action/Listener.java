package base.action;

import android.view.View;

/**
 * Created by minhua on 2015/10/23.
 */
public class Listener {

    public Listener(Action action) {
        this.action = action;
    }
    Action action;
    public void setAction(Action runner){
        this.action = runner;
    }
    public void run(){

    }

    public Action getAction() {
        return action;
    }
    public void runAction(View v) {
        getAction().setOnview(v);
        getAction().setBaseActivity((android.app.Activity) v.getContext());
        getAction().run();
    }
}

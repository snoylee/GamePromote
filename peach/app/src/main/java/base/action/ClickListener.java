package base.action;


import android.view.View;
import android.widget.TextView;

/**
 * Created by minhua on 2015/10/23.
 */
public class ClickListener extends Listener implements View.OnClickListener {

    public ClickListener(Action action) {
        super(action);
    }

    @Override
    public void onClick(View v) {

        runAction(v);
    }


}

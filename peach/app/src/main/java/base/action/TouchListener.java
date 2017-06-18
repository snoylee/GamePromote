package base.action;

        import android.view.MotionEvent;
        import android.view.View;

        import base.action.Action;
        import base.action.Listener;

/**
 * Created by minhua on 2015/11/25.
 */
public class TouchListener extends Listener implements View.OnTouchListener {
    long start;
    long end;
    private int mEventDown = -1;

    public TouchListener(Action action) {
        super(action);
    }

    int mEvent = -1;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (start==0) {
            start = System.currentTimeMillis();

        }

        end = System.currentTimeMillis();
        if (mEvent != MotionEvent.ACTION_UP) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                mEvent = MotionEvent.ACTION_UP;
            }
        }

        if (end - start > 150) {
            if (mEvent == MotionEvent.ACTION_UP) {
                runAction(view);
                start = 0;
                mEvent = -1;
                mEventDown = -1;
                return true;
            }
        }
        return true;
    }
}

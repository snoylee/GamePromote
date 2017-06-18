package base.action;

import java.util.List;

import android.app.Activity;
import android.os.Message;

import base.action.Action.Param;
import base.action.asyc.BackRunnable;
import base.action.asyc.CallbackRunnable;
import base.action.asyc.Compt;

public abstract class AsynTask extends AbsTask {
    public Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object run(final String methodname, final List<String> params, final Param param) {

        new Compt().putTask(new BackRunnable() {

            @Override
            public void run() throws Exception {
                // TODO Auto-generated method stub
                AsynTask.this.setMethodname(methodname);
                AsynTask.this.setParams(params);
                AsynTask.this.setParam(param);
                this.object = AsynTask.this.threadRun(methodname, params, param);
            }

        }, new CallbackRunnable() {

            @Override
            public boolean run(Message msg, boolean error, Activity aty) throws Exception {
                // TODO Auto-generated method stub
                if (getBackRunnable().getObject() instanceof Exception) {
                    catchException(param, ((Exception) getBackRunnable().getObject()));
                }
                if (callbackControl(param, getBackRunnable().getObject()) == null) {
                    return false;
                }
                if (getBackRunnable().getObject() == null) {
                    return false;
                }
                callback(param, getBackRunnable().getObject());
                back(methodname, params, param);
                if (getAction() != null && getAction().getNextAction() != null) {
                    getAction().getNextAction().param.setResultObject(getBackRunnable().getObject());
                    getAction().getNextAction().run();
                }
                return false;
            }

        }).run();
        return param;
    }

    public void catchException(Param aparam, Exception object) {
        getThisAction().catchException(aparam, object);
    }

    public void back(String methodname, List<String> params, Param param) {
        getThisAction().back(methodname, params, param);
    }

    protected Object callbackControl(Param param, Object object) {
        return "";
    }

    public abstract void callback(Param aparam, Object object);

    @Override
    protected Object invoke(String methodname, List<String> params, Param param) {
        // TODO Auto-generated method stub
        return run(methodname, params, param);
    }

    public abstract Object threadRun(String methodname, List<String> params, Param param);
}

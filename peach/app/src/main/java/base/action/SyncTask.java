package base.action;

import java.util.List;

import base.action.Action.Param;

public class SyncTask extends Task {
    public Action nextAction;

    public Action getNextAction() {
        return nextAction;
    }

    public void setNextAction(Action nextAction) {
        this.nextAction = nextAction;
    }

    @Override
    protected Object invoke(String methodname, List<String> params, Param param) {
        // TODO Auto-generated method stub
        return run(methodname, params, param);
    }

    @Override
    public Object run(String methodname, List<String> params, Param param) {
        // TODO Auto-generated method stub
        return "";
    }

    public void runNext() {
        if (getNextAction() != null) {
            getNextAction().run();
        }
    }
}

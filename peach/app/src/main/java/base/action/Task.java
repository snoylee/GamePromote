package base.action;

import java.util.List;

import base.action.Action.Param;

public class Task extends AbsTask {
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
        Object result = null;
        try {
            result = run(methodname, params, param);
        } catch (Exception e) {
            catchException(param, e);

        } finally {
            back(methodname, params, param);
        }
        return result;

    }

    private void catchException(Param param, Exception e) {
        getThisAction().catchException(param, e);
    }

    public void back(String methodname, List<String> params, Param param) {
        getThisAction().back(methodname, params, param);
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

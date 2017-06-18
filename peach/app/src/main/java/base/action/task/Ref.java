package base.action.task;

import java.util.List;

import base.Actions;
import base.RRes;
import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/24.
 */
public class Ref extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {

        String t = param.getActivity().getString(RRes.get("R.string."+params.get(0).replace("R.string.","")).getAndroidValue());
        return Actions.parseActions(t,param.getActivity(),param.getFragment(),param.getVisitunit()).run();

    }
}

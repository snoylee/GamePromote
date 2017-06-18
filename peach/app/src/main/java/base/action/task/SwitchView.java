package base.action.task;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xygame.sg.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.RRes;
import base.action.Action;
import base.action.Task;


/**
 * Created by minhua on 2015/10/27.
 */
public class SwitchView extends Task {
    public View sliceView;
    public static Map<View, Boolean> show = new HashMap<View, Boolean>();
    public static View prevview;
    public static String closestr;

    @Override
    public Object run(String methodname, List<String> params, final Action.Param param) {
        final int id = RRes.get("R.id." + params.get(0).replace("id.", "").replace("R.", "")).getAndroidValue();


        if ((params.size() >= 3 && params.get(params.size() - 1).equals("false")) || (show.get(param.getOnview()) != null)) {
            show.clear();
            new Action("#CloseView(" + params.get(0) +
                    "," + params.get(1) +
                    ")", param.getActivity(), param.getFragment(), param.getVisitunit()).run();
            textColorSet(param, params.size() >= 3 ? RRes.get(params.get(2)).getAndroidValue() : 0, R.color.dark_gray);
            closestr = "";
        } else {
            if (((ViewGroup) param.getActivity().findViewById(id)).getChildCount() != 0) {
                show.clear();
                new Action("#CloseView(" + params.get(0) +
                        "," + params.get(1) +
                        ")", param.getActivity(), param.getFragment(), param.getVisitunit()).run();
                textColorSet(param, params.size() >= 3 ? RRes.get(params.get(2)).getAndroidValue() : 0, R.color.dark_gray);
            }
            show.put(param.getOnview(), true);
            new Action("#OpenView(" + params.get(0) +
                    "," + params.get(1) +
                    ")", param.getActivity(), param.getFragment(), param.getVisitunit()).run();
            textColorSet(param, params.size() >= 3 ? RRes.get(params.get(2)).getAndroidValue() : 0, R.color.dark_green);
            closestr = params.get(0) +
                    "," + params.get(1) + "," + params.get(2) + ",false";
        }

        return "";
    }

    public void textColorSet(Action.Param param, int i, int color) {
        if (i == 0) return;
        if (param.getActivity().findViewById(i) instanceof TextView) {
            ((TextView) param.getActivity().findViewById(i)).setTextColor(param.getActivity().getResources().getColor(color));
        }
    }

}

package com.xygame.sg.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.CommonActivity;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import base.Actions;
import base.ViewBinder;
import base.action.Action;
import base.action.CenterRepo;
import base.action.Task;
import base.action.task.Http;
import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/11/23.
 */
public class SelectorAvity extends CommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = "";

        if (!(UserPreferencesUtil.getUserType(getBaseContext()).equals(""))) {
            type = (UserPreferencesUtil.getUserType(getBaseContext()).equals("8") ? 2 : 1) + "";
        }
        Actions.parseActions("#" +SelectOccup.class.getName()+
                "(${baseurl}/sys/queryUserOccupType, exclusType=" + type + ");#" + SetContentView.class.getName() + "()", this, null, new VisitUnit()).run();
    }
    public static class SelectOccup extends Http {
        @Override
        public void callback(Action.Param aparam, Object object) {
            Map map  = new TreeMap();
            map.put("typeName","不限");
            ((List)((Map)object).get("record")).add(0, map);
            super.callback(aparam, object);
        }
    }
    public static class SetContentView extends Task {
        @Override
        public Object run(String methodname, List<String> params, Action.Param param) {
            param.getActivity().setContentView(new ViewBinder(param.getActivity(), param.getVisitunit()).inflate(R.layout.selector_occup, null));
            ((TextView) param.getActivity().findViewById(R.id.titleName)).setText("选择职业");
            return super.run(methodname, params, param);
        }
    }

    public static class OccupItemClick extends Task {
        @Override
        public Object run(String methodname, List<String> params, Action.Param param) {
            if(params.get(1).equals("不限")){
                CenterRepo.getInsatnce().getRepo().put("occupType", "");
                CenterRepo.getInsatnce().getRepo().put("occupType_name", "");
            }else{
            CenterRepo.getInsatnce().getRepo().put("occupType", params.get(0));
            CenterRepo.getInsatnce().getRepo().put("occupType_name", params.get(1));
            }
            param.getActivity().finish();
            return super.run(methodname, params, param);
        }
    }
}

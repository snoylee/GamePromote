package base.action;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import base.action.task.TaskPool;
import base.data.net.http.JsonUtil;
import base.frame.DataUnit;
import base.frame.ParentFragment;
import base.frame.VisitUnit;
import base.init.TelliUtil;

/**
 * Created by minhua on 2015/10/23.
 */
public class Action extends Epr {
    private Action nextAction;

    public Action getNextAction() {
        return nextAction;
    }

    public void setNextAction(Action nextAction) {
        this.nextAction = nextAction;
    }

    private String raw;
    Param param = new Param();

    public void catchException(Param aparam,Exception object) {
//        Toast.makeText(aparam.getActivity(), "网络异常，连接超时", Toast.LENGTH_SHORT).show();
    }

    public static class Param {
        public Object resultObject;

        public Object getResultObject() {
            return resultObject;
        }

        public void setResultObject(Object resultObject) {
            this.resultObject = resultObject;
        }

        public VisitUnit visitunit;
        public DataUnit dataUnit;
        public ParentFragment fragment;
        public View onview;
        public List<String> businessParamsString;
        public String originfMethodname;
        private Activity activity;
        public JsonUtil resultunit;

        public JsonUtil getResultunit() {
            return resultunit;
        }

        public DataUnit getDataUnit() {
            return dataUnit;
        }

        public VisitUnit getVisitunit() {
            return visitunit;
        }

        public void setVisitunit(VisitUnit visitunit) {
            this.visitunit = visitunit;
            this.dataUnit = visitunit.getDataUnit();
        }

        public ParentFragment getFragment() {
            return fragment;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public View getOnview() {
            return onview;
        }

        public List<String> getBusinessParamsString() {
            return businessParamsString;
        }

        public void setFragment(ParentFragment fragment) {
            this.fragment = fragment;
        }

        public void setOnview(View onview) {
            this.onview = onview;
        }

        public void setBusinessParamsString(List<String> businessParamsString) {
            this.businessParamsString = businessParamsString;
        }

        public String getOriginfMethodname() {
            return originfMethodname;
        }

        public void setOriginfMethodname(String originfMethodname) {
            this.originfMethodname = originfMethodname;
        }
    }

    public List<String> params = new ArrayList<String>();
    public String taskname;
    public AbsTask task;
    private VisitUnit visit;

    public Action(Class clazz, String raw, Activity aty, Object display, VisitUnit visit) {
        this("#" + clazz.getName() + "(" + raw + ")", aty, display, visit);
    }

    public Action(String raw, Activity aty, Object display, VisitUnit visit) {
        this.raw = raw;
        this.visit = visit;
        setBaseActivity(aty);
        if (display instanceof ParentFragment) {
            setBaseFragment((ParentFragment) display);
            setBaseActivity(((ParentFragment) display).getActivity());

        } else if (display instanceof Activity) {
            setBaseActivity((Activity) display);
        }
        try {
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Action() {
    }

    public List<String> getParams() {
        return params;
    }

    public View onview;
    public ParentFragment baseactivity;

    public Action setOnview(View onview) {
        this.onview = onview;
        param.setOnview(onview);
        return this;
    }

    public Action setBaseFragment(ParentFragment baseactivity) {
        this.baseactivity = baseactivity;
        param.setFragment(baseactivity);
        return this;
    }

    public Action setBaseActivity(Activity baseactivity) {

        param.setActivity(baseactivity);
        return this;
    }

    public Object run() {
        preparedParams = parseEpr(ps, param.getActivity(), param.getFragment());
        preparedParams.set(preparedParams.size() - 1, preparedParams.get(preparedParams.size() - 1));
        Object o = null;
        if (clazz != null) {

            try {
                List<String> rut = new ArrayList<String>();
                for (Object ao : preparedParams) {
                    if (ao == null) {
                        continue;
                    }
                    if (ao instanceof base.action.Param) {
                        ao = ((base.action.Param) ao).run();
                        if (ao != null) {
                            rut.add(ao.toString());
                        } else {
                            rut.add(null);
                        }
                    } else {
                        rut.add(ao.toString());
                    }

                }
                param.setVisitunit(visit);
                if (methodscope == methodscopeDyn || task == null) {
                    task = ((AbsTask) clazz.newInstance());
                    task.setThisAction(this);
                    o = task.invoke(taskname, rut, param);
                } else {
                    task.setThisAction(this);
                    o = task.invoke(taskname, rut, param);
                }

                if (task instanceof AsynTask) {
                    ((AsynTask) task).setAction(this);
                } else {
                    ((Task) task).setNextAction(getNextAction());
                    if (o != null && getNextAction() != null) {
                        getNextAction().param.setResultObject(o);
                        getNextAction().param.setOnview(onview);
                        getNextAction().run();
                    }

                }
            } catch (Exception e) {
                catchException(param,e);
            }
        }
        return o;
    }

    public void back(String methodname, List<String> params, Param param) {

    }

    int methodscope = -1;
    public static final int methodscopeDyn = 0;
    public static final int methodscopeUI = 1;
    String ps;

    public void parse() throws Exception {

        ps = raw.substring(raw.indexOf("(") + 1, raw.length() - 1);
        taskname = raw.substring(raw.indexOf("#") + 1, raw.indexOf("("));
        String clazzn = "";

        boolean isInnerTask = false;
        if (!taskname.contains(":")) {
            clazzn = taskname;
            methodscope = methodscopeUI;
        } else if (taskname.split(":")[0].equals("dyn")) {
            methodscope = methodscopeDyn;
            taskname = taskname.split(":")[1];
        }

        for (Class cz : TaskPool.getTaskclazz()) {
            if (cz.getSimpleName().toLowerCase().replace("task", "").equalsIgnoreCase(clazzn.toLowerCase())) {
                clazzn = cz.getName();
                isInnerTask = true;
                break;
            }
        }

        if (!isInnerTask) {
            if (!TelliUtil.getBuisnessPackage().isEmpty() && clazzn.startsWith(".")) {
                int dot = clazzn.lastIndexOf(".");
                if (Character.isLowerCase(clazzn.charAt(dot + 1))) {
                    clazzn = clazzn.replaceFirst(clazzn.charAt(dot + 1) + "", Character.toUpperCase(clazzn.charAt(dot + 1)) + "");
                }
                clazzn = TelliUtil.getBuisnessPackage() + clazzn;
            }
        }

        try {
            clazz = Class.forName(clazzn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public List<Object> parseEpr(String param, Activity activity, ParentFragment fragment) {

        // List<String> ps = new ArrayList<String>();
        // String[] mhs = param.split(":");
        // String str = mhs[0];
        // int ptr;int starts = 0;
        // if(mhs.length==1){
        // ps.add(mhs[0]);
        // }else {
        // for (int i = 1; i < param.length(); i++) {
        // str += ":" + mhs[i];
        // ptr = str.lastIndexOf(",");
        // ps.add(param.substring(starts, ptr));
        // starts = ptr + 1;
        // }
        // }

        List<Object> ps = new ArrayList<Object>();
        int start = 0;
        int pre = 0;
        int l;
        int r;
        int end;
        int ll;
        String ls;
        String str;
        boolean enter = false;
        while ((l = param.indexOf("(", start)) != -1) {
            enter = true;
            r = param.indexOf(")", start);
            if (r == param.length() - 1) {
                ls = param.substring(0);
            } else {
                ls = param.substring(0, r);
            }
            ll = ls.lastIndexOf("(");
            while (ll != l) {
                r = param.indexOf(")", r + 1);
                ls = param.substring(start, r);
                ll = param.substring(0, ll).lastIndexOf("(");
            }
            str = param.substring(start, l);

            String[] rry = str.split(",");

            Epr.addAll(ps, rry, activity, fragment, visit);
            ps.remove(ps.size() - 1);
            end = str.lastIndexOf(",") + 1 + (start);
            start = param.indexOf(",", r);
            if (start == -1) {
                ps.add(Epr.parse(param.substring(end), activity, fragment, visit));
                return ps;
            } else {
                Object parsep = null;

                parsep = Epr.parse(param.substring(end, start), activity, fragment, visit);
                ps.add(parsep);
                start++;
            }

        }

        if (enter) {

            if (param.substring(start).length() > 1) {
                if (param.substring(start).contains(",")) {
                    Epr.addAll(ps, param.substring(start).split(","), activity, fragment, visit);
                } else {
                    ps.add(param.substring(start));

                }
            }
        } else {
            Epr.addAll(ps, param.split(","), activity, fragment, visit);
        }

        return ps;
    }

    List<Object> preparedParams;
    Class clazz = null;

    public List<Object> getPreparedParams() {
        return preparedParams;
    }
}

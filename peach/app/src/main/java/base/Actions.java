package base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.xygame.sg.task.utils.Alert;

import base.action.Action;
import base.frame.ParentFragment;
import base.frame.VisitUnit;

public class Actions {
    String text = "";
    private Activity aty;
    private Object display;
    private VisitUnit visit;

    public void run(Activity aty, Fragment fagment, VisitUnit visitunit) {

        this.aty = aty;
        this.display = fagment;
        this.visit = visitunit;
        Actions.parseActions(text, aty, display, visit).run();
    }

    public void run(Object obj) {
        if (text.equals("")) {
            Toast.makeText(aty, "未设置 Actions 文本", Toast.LENGTH_SHORT).show();
            return;
        }
        if (obj instanceof Activity) {
            this.aty = (Activity) obj;
            this.display = obj;
            this.visit = new VisitUnit();

        } else if (obj instanceof ParentFragment) {
            this.aty = ((ParentFragment) obj).getActivity();
            this.display = obj;
            this.visit = ((ParentFragment) obj).getVisitUnit();
        }
        Actions.parseActions(text, aty, display, visit).run();
    }

    public Actions append(String text) {
        this.text += text;
        if(!text.endsWith(";")){
            this.text += ";";
        }
        return this;
    }
    public Actions append(Class clazz, String text) {
        text = text.replace("(","").replace(")","");
        this.text += "#" + clazz.getName() + "(" + text + ");";
        return this;
    }
    public Actions append(Class clazz) {
        return append(clazz,"()");
    }
    public Actions appenDialaogdOpen(String msg) {
        this.text += "#" + Alert.class.getName() + "(" + text + ",true,true);";
        return this;
    }

    public Actions appenDialaogdClose() {
        this.text += "#" + Alert.class.getName() + "(false);";
        return this;
    }

    public static Action parseActions(String raw, Activity aty, Object display, VisitUnit visit) {
        try {
            if (raw.endsWith(";")) {
                raw = raw.substring(0, raw.length() - 1);
            }
            if (raw.startsWith("#") && raw.contains("(") && raw.endsWith(")")) {
                if (raw.contains(";")) {
                    Action firstaction;
                    Action action;
                    Action nextAction = null;
                    String[] ms = raw.split(";");
                    firstaction = action = new Action(ms[0], aty, display, visit);
                    for (int i = 0; i < ms.length; i++) {

                        if (i != ms.length - 1) {

                            nextAction = new Action(ms[i + 1], aty, display, visit);
                        } else {
                            nextAction = null;
                        }


                        if (nextAction != null) {
                            action.setNextAction(nextAction);
                        }
                        action = nextAction;
                    }
                    return firstaction;
                } else {
                    return new Action(raw, aty, display, visit);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package base.action.task;


import java.util.Set;
import java.util.TreeSet;

/**
 * Created by minhua on 2015/10/27.
 */
public class TaskPool {
    private static Class[] taskclazz = new Class[]{
            Http.class,
            StartActivity.class,
            Back.class,
            Check.class,
            PutCenter.class,
            Ref.class,
            Put.class,
            SwitchView.class,
            FinishActivity.class,
            HttpTest.class
    };

    public static Class[] getTaskclazz() {
        return taskclazz;
    }
}

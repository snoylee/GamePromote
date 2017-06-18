package base.action.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by minhua on 2015/11/28.
 */
public class LabelContainer extends LinearLayout {
    public Set<String> sets = new TreeSet<String>();

    public Set<String> getSets() {
        return sets;
    }

    public LabelContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

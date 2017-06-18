package base.action.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xygame.sg.R;

/**
 * Created by minhua on 2015/11/25.
 */
public class RadioTextGroup extends RadioGroup {

    static final String resauto = "http://schemas.android.com/apk/res-auto";
    public RadioTextGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        final String othercolor = attrs.getAttributeValue(resauto, "othercolor");
        final String clickedcolor = attrs.getAttributeValue(resauto, "clickedcolor");

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    View view = radioGroup.getChildAt(j);
                    if (view instanceof RadioButton) {
                        ((RadioButton) view).setTextColor(Color.parseColor(othercolor));
                    }
                }
                ((RadioButton) radioGroup.findViewById(i)).setTextColor(Color.parseColor(clickedcolor));
            }
        });
    }
}

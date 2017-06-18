package base.action.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.xygame.sg.R;

/**
 * Created by minhua on 2015/11/25.
 */
public class RadioText extends RadioButton {
    public RadioText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setButtonDrawable(android.R.drawable.screen_background_dark_transparent);
    }
}

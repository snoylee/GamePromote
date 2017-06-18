package base.action.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SimpleAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xygame.sg.utils.ImageLocalLoader;

/**
 * Created by minhua on 2015/11/18.
 */
public class ImageView extends android.widget.ImageView {
    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        display();
    }
    public void display(){
        if(!uri.startsWith("http")){
        ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(uri,this);
        }else {
            ImageLoader.getInstance().displayImage(uri,this);
        }
    }

}

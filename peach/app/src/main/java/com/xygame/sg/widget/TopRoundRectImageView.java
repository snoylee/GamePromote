package com.xygame.sg.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xygame.sg.define.draggrid.DataTools;

public class TopRoundRectImageView extends ImageView {

    private Paint paint;
    private Context context;

    public TopRoundRectImageView(Context context) {
        this(context, null);
    }

    public TopRoundRectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopRoundRectImageView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Drawable drawable = getDrawable();
            if (null != drawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
                Canvas canvas1 = new Canvas(output);

                int cornerx = DataTools.dip2px(context, 7);
                int cornery = DataTools.dip2px(context, 4);
                float[] outerR = new float[]{cornerx, cornery, cornerx, cornery, 0, 0, 0, 0};
                RoundRectShape rectShape = new RoundRectShape(outerR, null, null);
                ShapeDrawable mDrawables = new ShapeDrawable(rectShape);
                mDrawables.getPaint().setAntiAlias(true);
                mDrawables.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawables.draw(canvas1);

                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                paint.setAntiAlias(true);
                canvas1.drawARGB(0, 0, 0, 0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                canvas1.drawBitmap(bitmap, rect, rect, paint);
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                final Rect rectSrc = new Rect(0, 0, output.getWidth(), output.getHeight());
                final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
                paint.reset();
                canvas.drawBitmap(output, rectSrc, rectDest, paint);
                // 先判断是否已经回收
                output.recycle();
            } else {
                super.onDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int cornerx = DataTools.dip2px(context, 7);
        int cornery = DataTools.dip2px(context, 4);
        float[] outerR = new float[]{cornerx, cornery, cornerx, cornery, 0, 0, 0, 0};
        RoundRectShape rectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable mDrawables = new ShapeDrawable(rectShape);
        mDrawables.getPaint().setAntiAlias(true);
        mDrawables.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDrawables.draw(canvas);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }



}

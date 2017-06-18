package com.xygame.sg.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XCRoundSmallRectImageView extends ImageView {

	private Paint paint;

	public XCRoundSmallRectImageView(Context context) {
		this(context, null);
	}

	public XCRoundSmallRectImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XCRoundSmallRectImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
	}
	
	

	@Override
	public ScaleType getScaleType() {
		// TODO Auto-generated method stub
		return super.getScaleType();
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		// TODO Auto-generated method stub
		super.setScaleType(scaleType);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			Drawable drawable = getDrawable();
			if (null != drawable) {
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				Bitmap b = getRoundBitmap(bitmap, 12);
				final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
				final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
				paint.reset();
				canvas.drawBitmap(b, rectSrc, rectDest, paint);

			} else {
				super.onDraw(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
//		if (bitmap == null) {
//			bitmap = BitmapFactory.decodeResource(getResources(),
//					R.drawable.empty_photo);
//		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		int x = bitmap.getWidth();

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;

	}
}

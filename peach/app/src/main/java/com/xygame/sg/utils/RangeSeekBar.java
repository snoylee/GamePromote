package com.xygame.sg.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;


import com.xygame.sg.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Widget that lets users select a minimum and maximum value on a given
 * numerical range. The range value types can be one of Long, Double, Integer,
 * Float, Short, Byte or BigDecimal.<br />
 * <br />
 * Improved {@link MotionEvent} handling for smoother use, anti-aliased painting
 * for improved aesthetics.
 *
 * @param <T> The Number type of the range values. One of Long, Double, Integer,
 *            Float, Short, Byte or BigDecimal.
 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
 * @author Peter Sinnott (psinnott@gmail.com)
 * @author Thomas Barrasso (tbarrasso@sevenplusandroid.org)
 */
public class RangeSeekBar<T extends Number> extends ImageView {
    public static final int PADDING_IN_TEXT = 20;
    public static final int OFFSET = 25 - 1;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint textPaint = new TextPaint();
    private final Paint textBkgPaint = new Paint();
    private final Bitmap thumbImage = BitmapFactory.decodeResource(
            getResources(), R.drawable.rangeblock);
    private final Bitmap thumbPressedImage = BitmapFactory.decodeResource(
            getResources(), R.drawable.rangeblock);
    private final float thumbWidth = thumbImage.getWidth();
    private final float thumbHalfWidth = 0.5f * thumbWidth;
    private final float thumbHalfHeight = 0.5f * thumbImage.getHeight();
    private final float lineHeight = 0.3f * thumbHalfHeight;
    private final float padding = thumbHalfWidth + OFFSET;
    private T absoluteMinValue, absoluteMaxValue;
    private NumberType numberType;
    private double absoluteMinValuePrim, absoluteMaxValuePrim;
    private double normalizedMinValue = 0d;
    private double normalizedMaxValue = 1d;
    private Thumb pressedThumb = null;
    private boolean notifyWhileDragging = false;
    private OnRangeSeekBarChangeListener<T> listener;

    private int textSize = 24;  //px
    private int textColor = 0x159ceb;
    private int textMarginBottom = 24; //px
    private int textHeight = 40; //px

    private String minText = "";
    private String maxText = "";

    /**
     * Default color of a {@link RangeSeekBar}, #FF33B5E5. This is also known as
     * "Ice Cream Sandwich" blue.
     */
    public static final int DEFAULT_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5);

    /**
     * An invalid pointer id.
     */
    public static final int INVALID_POINTER_ID = 255;

    // Localized constants from MotionEvent for compatibility
    // with API < 8 "Froyo".
    public static final int ACTION_POINTER_UP = 0x6,
            ACTION_POINTER_INDEX_MASK = 0x0000ff00,
            ACTION_POINTER_INDEX_SHIFT = 8;

    private float mDownMotionX;
    private int mActivePointerId = INVALID_POINTER_ID;

    /**
     * On touch, this offset plus the scaled value from the position of the
     * touch will form the progress value. Usually 0.
     */
    float mTouchProgressOffset;

    private int mScaledTouchSlop;
    private boolean mIsDragging;
    private List<String> sequence;

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context);
    }


    /**
     * Creates a new RangeSeekBar.
     *
     * @param absoluteMinValue The minimum value of the selectable range.
     * @param absoluteMaxValue The maximum value of the selectable range.
     * @param context
     * @throws IllegalArgumentException Will be thrown if min/max value type is not one of Long,
     *                                  Double, Integer, Float, Short, Byte or BigDecimal.
     */

    public RangeSeekBar(T absoluteMinValue, T absoluteMaxValue, Context context, AttributeSet attr)
            throws IllegalArgumentException {
        super(context, attr);
        init(absoluteMinValue, absoluteMaxValue);
    }

    public void setSequence(List<String> sequence) {
        this.sequence = sequence;
    }

    public void setAbsoluteMinValue(T absoluteMinValue) {
        this.absoluteMinValue = absoluteMinValue;
    }

    public void setAbsoluteMaxValue(T absoluteMaxValue) {
        this.absoluteMaxValue = absoluteMaxValue;
    }

    public void init(T absoluteMinValue, T absoluteMaxValue) {
        this.absoluteMinValue = absoluteMinValue;
        this.absoluteMaxValue = absoluteMaxValue;
        absoluteMinValuePrim = absoluteMinValue.doubleValue();
        absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
        numberType = NumberType.fromNumber(absoluteMinValue);

        // make RangeSeekBar focusable. This solves focus handling issues in
        // case EditText widgets are being used along with the RangeSeekBar
        // within ScollViews.
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public void reset() {

        setSelectedMinValue(absoluteMinValue);
        setSelectedMaxValue(absoluteMaxValue);
        if (listener != null) {
            listener.onRangeSeekBarValuesChanged(this,
                    getSelectedMinValue(), getSelectedMaxValue());
        }

    }

    private final void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext())
                .getScaledTouchSlop();

        Resources res = getResources();
        textSize = (int) res.getDimension(R.dimen.text_size_14);
        textMarginBottom = (int) res.getDimension(R.dimen.text_marginbottom_in_rangeseekbar);
        textColor = Color.parseColor("#40d1c5");
        textHeight = (int) res.getDimension(R.dimen.text_height_in_rangeseekbar);

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        /*

         // draw seek bar background line
        final RectF rect = new RectF(padding,
                getLineTop(), getWidth() - padding,
                getLineTop()+ lineHeight);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rect, 10, 10, paint);


         */
        textBkgPaint.setStyle(Style.FILL);
        textBkgPaint.setColor(Color.WHITE);
    }

    public boolean isNotifyWhileDragging() {
        return notifyWhileDragging;
    }

    /**
     * Should the widget notify the listener callback while the user is still
     * dragging a thumb? Default is false.
     *
     * @param flag
     */
    public void setNotifyWhileDragging(boolean flag) {
        this.notifyWhileDragging = flag;
    }

    /**
     * Returns the absolute minimum value of the range that has been set at
     * construction time.
     *
     * @return The absolute minimum value of the range.
     */
    public T getAbsoluteMinValue() {
        return absoluteMinValue;
    }

    /**
     * Returns the absolute maximum value of the range that has been set at
     * construction time.
     *
     * @return The absolute maximum value of the range.
     */
    public T getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    /**
     * Returns the currently selected min value.
     *
     * @return The currently selected min value.
     */
    public T getSelectedMinValue() {
        return normalizedToValue(normalizedMinValue);
    }

    /**
     * Sets the currently selected minimum value. The widget will be invalidated
     * and redrawn.
     *
     * @param value The Number value to set the minimum value to. Will be clamped
     *              to given absolute minimum/maximum range.
     */
    public void setSelectedMinValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero
        // when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMinValue(0d);
        } else {
            setNormalizedMinValue(valueToNormalized(value));
        }
    }

    /**
     * Returns the currently selected max value.
     *
     * @return The currently selected max value.
     */
    public T getSelectedMaxValue() {
        return normalizedToValue(normalizedMaxValue);
    }

    /**
     * Sets the currently selected maximum value. The widget will be invalidated
     * and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped
     *              to given absolute minimum/maximum range.
     */
    public void setSelectedMaxValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero
        // when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMaxValue(1d);
        } else {
            setNormalizedMaxValue(valueToNormalized(value));
        }
    }

    /**
     * Registers given listener callback to notify about changed selected
     * values.
     *
     * @param listener The listener to notify about changed selected values.
     */
    public void setOnRangeSeekBarChangeListener(
            OnRangeSeekBarChangeListener<T> listener) {
        this.listener = listener;
    }

    /**
     * Handles thumb selection and movement. Notifies listener callback on
     * certain events.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnabled())
            return false;

        int pointerIndex;

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // Remember where the motion event started
                mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
                pointerIndex = event.findPointerIndex(mActivePointerId);
                mDownMotionX = event.getX(pointerIndex);

                pressedThumb = evalPressedThumb(mDownMotionX);

                // Only handle thumb presses.
                if (pressedThumb == null)
                    return super.onTouchEvent(event);

                setPressed(true);
                invalidate();
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();

                break;
            case MotionEvent.ACTION_MOVE:
                if (pressedThumb != null) {

                    if (mIsDragging) {
                        trackTouchEvent(event);
                    } else {
                        // Scroll to follow the motion event
                        pointerIndex = event.findPointerIndex(mActivePointerId);
                        final float x = event.getX(pointerIndex);

                        if (Math.abs(x - mDownMotionX) > mScaledTouchSlop) {
                            setPressed(true);
                            invalidate();
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            attemptClaimDrag();
                        }
                    }

                    if (notifyWhileDragging && listener != null) {
                        listener.onRangeSeekBarValuesChanged(this,
                                getSelectedMinValue(), getSelectedMaxValue());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }

                pressedThumb = null;
                invalidate();
                if (listener != null) {
                    listener.onRangeSeekBarValuesChanged(this,
                            getSelectedMinValue(), getSelectedMaxValue());
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = event.getPointerCount() - 1;
                // final int index = ev.getActionIndex();
                mDownMotionX = event.getX(index);
                mActivePointerId = event.getPointerId(index);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
        return true;
    }

    private final void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;

        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose
            // a new active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mDownMotionX = ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private final void trackTouchEvent(MotionEvent event) {
        final int pointerIndex = event.findPointerIndex(mActivePointerId);
        final float x = event.getX(pointerIndex);

        if (Thumb.MIN.equals(pressedThumb)) {
            setNormalizedMinValue(screenToNormalized(x));
            if (normalizedToScreen(normalizedMaxValue) - x < 3) {
                setNormalizedMaxValue(screenToNormalized(x + 3));
            }
        } else if (Thumb.MAX.equals(pressedThumb)) {
            setNormalizedMaxValue(screenToNormalized(x));
            if (x - normalizedToScreen(normalizedMinValue) < 3) {
                setNormalizedMinValue(screenToNormalized(x - 3));
            }
        }
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;
    }

    /**
     * Ensures correct size of the widget.
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        int width = 250;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = thumbImage.getHeight() + textHeight + textMarginBottom;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Draws the widget on the given canvas.
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw seek bar background line
        final RectF rect = new RectF(padding - OFFSET,
                getLineTop(), getWidth() - padding + OFFSET,
                getLineTop() + lineHeight);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.parseColor("#e6e6e7"));
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rect, 10, 10, paint);

        // draw seek bar active range line
        rect.right = normalizedToScreen(normalizedMaxValue) + 24;
        rect.left = normalizedToScreen(normalizedMinValue) - 24;

        // orange color
        paint.setColor(Color.parseColor("#40d1c5"));
        canvas.drawRect(rect, paint);

//        canvas.drawText("",);

        // draw minimum thumb
        float minCoordX = normalizedToScreen(normalizedMinValue);
        float maxCoordX = normalizedToScreen(normalizedMaxValue);

        drawThumb(minCoordX - 24,
                Thumb.MIN.equals(pressedThumb), canvas);

        // draw maximum thumb
        drawThumb(maxCoordX + 24,
                Thumb.MAX.equals(pressedThumb), canvas);

//        float textWidth = textPaint.measureText(minText,0,minText.length());
//        float textMinCoordX = minCoordX - textWidth / 2;
//
//        float textMinBkgCoordX = textMinCoordX - PADDING_IN_TEXT;
//
//        if(textMinBkgCoordX < 0){
//            textMinBkgCoordX = 0;
//        }else if(textMinBkgCoordX > getWidth() - textWidth - PADDING_IN_TEXT * 2){
//            textMinBkgCoordX = getWidth() - textWidth - PADDING_IN_TEXT * 2;
//        }
//
//        textWidth = textPaint.measureText(maxText,0,maxText.length());
//        float textMaxCoordX = maxCoordX - textWidth / 2;
//        float textMaxBkgCoordX = textMaxCoordX - PADDING_IN_TEXT;
//        if(textMaxBkgCoordX < 0){
//            textMaxBkgCoordX = 0;
//        }else if(textMaxBkgCoordX > getWidth() - textWidth - PADDING_IN_TEXT * 2){
//            textMaxBkgCoordX = getWidth() - textWidth - PADDING_IN_TEXT * 2;
//        }
//
//        RectF textMinBkg = new RectF(textMinBkgCoordX,0,textMinBkgCoordX + PADDING_IN_TEXT * 2 + textWidth,textHeight);
//        canvas.drawRoundRect(textMinBkg,textHeight / 2,textHeight / 2,textBkgPaint);
//        RectF textMaxBkg = new RectF(textMaxBkgCoordX,0,textMaxBkgCoordX + PADDING_IN_TEXT * 2 + textWidth,textHeight);
//        canvas.drawRoundRect(textMaxBkg,textHeight / 2,textHeight / 2,textBkgPaint);
//
//        textMinCoordX = textMinBkgCoordX + PADDING_IN_TEXT;
//        textMaxCoordX = textMaxBkgCoordX + PADDING_IN_TEXT;
//        canvas.drawText(minText,textMinCoordX,textSize,textPaint);
//        canvas.drawText(maxText,textMaxCoordX,textSize,textPaint);
        drawText(normalizedToScreen(0d), getMinText(minText, getSelectedMinValue()), canvas, OFFSET - 10);
        drawText(normalizedToScreen(1d), getMaxText(maxText, getSelectedMaxValue()), canvas, 0);
    }

    private void drawText(float nomalCoordx, String text, Canvas canvas, int OFFSET) {
        float textWidth = textPaint.measureText(text, 0, text.length());
        float textCoordX = nomalCoordx - textWidth / 2;
        float textBkgCoordX = textCoordX - PADDING_IN_TEXT;
        if (textBkgCoordX < 0) {
            textBkgCoordX = 0;
        } else if (textBkgCoordX > getWidth() - textWidth - PADDING_IN_TEXT * 2) {
            textBkgCoordX = getWidth() - textWidth - PADDING_IN_TEXT * 2;
        }

        RectF textMinBkg = new RectF(textBkgCoordX, 0, textBkgCoordX + PADDING_IN_TEXT * 2 + textWidth, textHeight);
        canvas.drawRoundRect(textMinBkg, textHeight / 2, textHeight / 2, textBkgPaint);

        textCoordX = textBkgCoordX + PADDING_IN_TEXT;
        canvas.drawText(text, textCoordX - OFFSET, textSize, textPaint);
    }

    /**
     * Overridden to save instance state when device orientation changes. This
     * method is called automatically if you assign an id to the RangeSeekBar
     * widget using the {@link #setId(int)} method. Other members of this class
     * than the normalized min and max values don't need to be saved.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", normalizedMinValue);
        bundle.putDouble("MAX", normalizedMaxValue);
        return bundle;
    }

    /**
     * Overridden to restore instance state when device orientation changes.
     * This method is called automatically if you assign an id to the
     * RangeSeekBar widget using the {@link #setId(int)} method.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        final Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        normalizedMinValue = bundle.getDouble("MIN");
        normalizedMaxValue = bundle.getDouble("MAX");
    }

    /**
     * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
     *
     * @param screenCoord The x-coordinate in screen space where to draw the image.
     * @param pressed     Is the thumb currently in "pressed" state?
     * @param canvas      The canvas to draw upon.
     */
    private void drawThumb(float screenCoord, boolean pressed, Canvas canvas) {
        canvas.drawBitmap(pressed ? thumbPressedImage : thumbImage, screenCoord
                        - thumbHalfWidth,
                getThumbTop(), paint);
    }

    private float getThumbTop() {
        return textHeight + textMarginBottom;
    }

    private float getLineTop() {
        return textHeight + textMarginBottom + thumbHalfHeight - lineHeight / 2;
    }

    /**
     * Decides which (if any) thumb is touched by the given x-coordinate.
     *
     * @param touchX The x-coordinate of a touch event in screen space.
     * @return The pressed thumb or null if none has been touched.
     */
    private Thumb evalPressedThumb(float touchX) {
        Thumb result = null;
        boolean minThumbPressed = isInThumbRange(touchX, normalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue);
        if (minThumbPressed && maxThumbPressed) {
            // if both thumbs are pressed (they lie on top of each other),
            // choose the one with more room to drag. this avoids "stalling" the
            // thumbs in a corner, not being able to drag them apart anymore.
            result = (touchX / getWidth() > 0.5f) ? Thumb.MIN : Thumb.MAX;
        } else if (minThumbPressed) {
            result = Thumb.MIN;
        } else if (maxThumbPressed) {
            result = Thumb.MAX;
        }
        return result;
    }

    /**
     * Decides if given x-coordinate in screen space needs to be interpreted as
     * "within" the normalized thumb x-coordinate.
     *
     * @param touchX               The x-coordinate in screen space to check.
     * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
     * @return true if x-coordinate is in thumb range, false otherwise.
     */
    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= thumbHalfWidth;
    }

    /**
     * Sets normalized min value to value so that 0 <= value <= normalized max
     * value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized min value to set.
     */
    double lastmin;

    public void setNormalizedMinValue(double value) {
//        if (lastmin == 0) {
//            lastmin = value;
//        }
//        lastmax = 0;
//        double balance = value - normalizedMaxValue;
//        if (-balance < 0.0102515335083 && value - lastmin > 0) {
//            normalizedMinValue = Math.max(0d,
//                    Math.min(1d, Math.min(value, normalizedMaxValue)));
//            if (normalizedMaxValue - balance < 1d) {
//                normalizedMaxValue += -balance;
//
//            }
////            if (normalizedMinValue > normalizedMaxValue) {
////                normalizedMinValue = Math.max(0d,
////                        Math.min(1d, Math.min(value, normalizedMaxValue)));
////            }
//        } else {
        normalizedMinValue = Math.max(0d,
                Math.min(1d, Math.min(value, normalizedMaxValue)));
//        }
//        lastmin = normalizedMinValue;
        invalidate();
    }

    /**
     * Sets normalized max value to value so that 0 <= normalized min value <=
     * value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized max value to set.
     */
    double lastmax;

    public void setNormalizedMaxValue(double value) {

        normalizedMaxValue = Math.max(0d,
                Math.min(1d, Math.max(value, normalizedMinValue)));

        invalidate();
    }

    /**
     * Converts a normalized value to a Number object in the value space between
     * absolute minimum and maximum.
     *
     * @param normalized
     * @return
     */
    @SuppressWarnings("unchecked")
    private T normalizedToValue(double normalized) {
        return (T) numberType.toNumber(absoluteMinValuePrim + normalized
                * (absoluteMaxValuePrim - absoluteMinValuePrim));
    }

    /**
     * Converts the given Number value to a normalized double.
     *
     * @param value The Number value to normalize.
     * @return The normalized double.
     */
    private double valueToNormalized(T value) {
        if (0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
            // prevent division by zero, simply return 0.
            return 0d;
        }
        return (value.doubleValue() - absoluteMinValuePrim)
                / (absoluteMaxValuePrim - absoluteMinValuePrim);
    }

    /**
     * Converts a normalized value into screen space.
     *
     * @param normalizedCoord The normalized value to convert.
     * @return The converted value in screen space.
     */
    private float normalizedToScreen(double normalizedCoord) {
        return (float) (padding + normalizedCoord * (getWidth() - 2 * padding));
    }

    /**
     * Converts screen space x-coordinates into normalized values.
     *
     * @param screenCoord The x-coordinate in screen space to convert.
     * @return The normalized value.
     */
    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        if (width <= 2 * padding) {
            // prevent division by zero, simply return 0.
            return 0d;
        } else {
            double result = (screenCoord - padding) / (width - 2 * padding);
            return Math.min(1d, Math.max(0d, result));
        }
    }

    /**
     * Callback listener interface to notify about changed range values.
     *
     * @param <T> The Number type the RangeSeekBar has been declared with.
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    public interface OnRangeSeekBarChangeListener<T> {
        void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
                                         T minValue, T maxValue);
    }

    /**
     * Thumb constants (min and max).
     */
    private enum Thumb {
        MIN, MAX
    }

    /**
     * Utility enumaration used to convert between Numbers and doubles.
     *
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    private enum NumberType {
        LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

        public static <E extends Number> NumberType fromNumber(E value)
                throws IllegalArgumentException {
            if (value instanceof Long) {
                return LONG;
            }
            if (value instanceof Double) {
                return DOUBLE;
            }
            if (value instanceof Integer) {
                return INTEGER;
            }
            if (value instanceof Float) {
                return FLOAT;
            }
            if (value instanceof Short) {
                return SHORT;
            }
            if (value instanceof Byte) {
                return BYTE;
            }
            if (value instanceof BigDecimal) {
                return BIG_DECIMAL;
            }
            throw new IllegalArgumentException("Number class '"
                    + value.getClass().getName() + "' is not supported");
        }

        public Number toNumber(double value) {
            switch (this) {
                case LONG:
                    return new Long((long) value);
                case DOUBLE:
                    return value;
                case INTEGER:
                    return new Integer((int) value);
                case FLOAT:
                    return new Float(value);
                case SHORT:
                    return new Short((short) value);
                case BYTE:
                    return new Byte((byte) value);
                case BIG_DECIMAL:
                    return new BigDecimal(value);
            }
            throw new InstantiationError("can't convert " + this
                    + " to a Number object");
        }
    }

    public String getMinText(String minText, T value) {
        return (sequence == null ? value + minText : sequence.get(value.intValue() - 1));
    }

    public void setMinText(String minText) {
        this.minText = minText;
    }

    public String getMaxText(String maxText, T value) {
        return (sequence == null ? value + maxText : sequence.get(value.intValue() - 1));
    }

    public void setMaxText(String maxText) {
        this.maxText = maxText;
    }
}

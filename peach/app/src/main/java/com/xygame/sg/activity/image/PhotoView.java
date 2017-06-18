/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xygame.sg.activity.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.xygame.sg.activity.image.PhotoViewAttacher.OnMatrixChangedListener;
import com.xygame.sg.activity.image.PhotoViewAttacher.OnPhotoTapListener;
import com.xygame.sg.activity.image.PhotoViewAttacher.OnViewTapListener;

public class PhotoView extends ImageView implements IPhotoView {

	private final PhotoViewAttacher mAttacher;

	private ScaleType mPendingScaleType;

	public PhotoView(Context context) {
		this(context, null);
	}

	public PhotoView(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}
	
	public PhotoView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		super.setScaleType(ScaleType.MATRIX);
		mAttacher = new PhotoViewAttacher(this);

		if (null != mPendingScaleType) {
			setScaleType(mPendingScaleType);
			mPendingScaleType = null;
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		try {
			return super.dispatchTouchEvent(event);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean canZoom() {
		return mAttacher.canZoom();
	}

	@Override
	public RectF getDisplayRect() {
		return mAttacher.getDisplayRect();
	}

	@Override
	public float getMinScale() {
		return mAttacher.getMinScale();
	}

	@Override
	public float getMidScale() {
		return mAttacher.getMidScale();
	}

	@Override
	public float getMaxScale() {
		return mAttacher.getMaxScale();
	}

	@Override
	public float getScale() {
		return mAttacher.getScale();
	}

	@Override
	public ScaleType getScaleType() {
		return mAttacher.getScaleType();
	}

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
	public void setMinScale(float minScale) {
		mAttacher.setMinScale(minScale);
	}

	@Override
	public void setMidScale(float midScale) {
		mAttacher.setMidScale(midScale);
	}

	@Override
	public void setMaxScale(float maxScale) {
		mAttacher.setMaxScale(maxScale);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}
	
	@Override
	// setImageBitmap calls through to this method
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
		mAttacher.setOnMatrixChangeListener(listener);
	}

//	@Override
//	public void setOnClickListener(OnClickListener l) {
//		// TODO Auto-generated method stub
//		mAttacher.setOnClickListener(l);
//	}
	
	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mAttacher.setOnLongClickListener(l);
	}

	@Override
	public void setOnPhotoTapListener(OnPhotoTapListener listener) {
		mAttacher.setOnPhotoTapListener(listener);
	}

	@Override
	public void setOnViewTapListener(OnViewTapListener listener) {
		mAttacher.setOnViewTapListener(listener);
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (null != mAttacher) {
			mAttacher.setScaleType(scaleType);
		} else {
			mPendingScaleType = scaleType;
		}
	}

	@Override
	public void setZoomable(boolean zoomable) {
		mAttacher.setZoomable(zoomable);
	}

//	@Override
//	public void zoomTo(float scale, float focalX, float focalY) {
//		mAttacher.zoomTo(scale, focalX, focalY);
//	}

	@Override
	protected void onDetachedFromWindow() {
		mAttacher.cleanup();
		super.onDetachedFromWindow();
	}

	@Override
	public boolean setDisplayMatrix(Matrix finalMatrix) {
		// TODO Auto-generated method stub
		return mAttacher.setDisplayMatrix(finalMatrix);
	}

	@Override
	public Matrix getDisplayMatrix() {
		// TODO Auto-generated method stub
		return mAttacher.getDisplayMatrix();
	}

	@Override
	public float getMinimumScale() {
		// TODO Auto-generated method stub
		return mAttacher.getMinimumScale();
	}

	@Override
	public float getMediumScale() {
		// TODO Auto-generated method stub
		return mAttacher.getMediumScale();
	}

	@Override
	public float getMaximumScale() {
		// TODO Auto-generated method stub
		return getMaximumScale();
	}

	@Override
	public void setMinimumScale(float minimumScale) {
		// TODO Auto-generated method stub
		mAttacher.setMinimumScale(minimumScale);
	}

	@Override
	public void setMediumScale(float mediumScale) {
		// TODO Auto-generated method stub
		mAttacher.setMediumScale(mediumScale);
	}

	@Override
	public void setMaximumScale(float maximumScale) {
		// TODO Auto-generated method stub
		mAttacher.setMaximumScale(maximumScale);
	}

	@Override
	public OnPhotoTapListener getOnPhotoTapListener() {
		// TODO Auto-generated method stub
		return mAttacher.getOnPhotoTapListener();
	}

	@Override
	public OnViewTapListener getOnViewTapListener() {
		// TODO Auto-generated method stub
		return mAttacher.getOnViewTapListener();
	}

	@Override
	public void setScale(float scale) {
		// TODO Auto-generated method stub
		mAttacher.setScale(scale);
	}

	@Override
	public void setScale(float scale, boolean animate) {
		// TODO Auto-generated method stub
		mAttacher.setScale(scale, animate);
	}

	@Override
	public void setScale(float scale, float focalX, float focalY,
			boolean animate) {
		// TODO Auto-generated method stub
		mAttacher.setScale(scale, focalX, focalY, animate);
	}

	@Override
	public void setPhotoViewRotation(float rotationDegree) {
		// TODO Auto-generated method stub
		mAttacher.setPhotoViewRotation(rotationDegree);
	}

	@Override
	public Bitmap getVisibleRectangleBitmap() {
		// TODO Auto-generated method stub
		return mAttacher.getVisibleRectangleBitmap();
	}

	@Override
	public void setZoomTransitionDuration(int milliseconds) {
		// TODO Auto-generated method stub
		mAttacher.setZoomTransitionDuration(milliseconds);
	}

}
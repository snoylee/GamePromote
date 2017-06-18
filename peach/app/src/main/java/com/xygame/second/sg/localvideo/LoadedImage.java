package com.xygame.second.sg.localvideo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/12.
 */
public class LoadedImage implements Serializable{
   private Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}

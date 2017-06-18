package com.xygame.sg.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xygame.sg.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoImageLoader {

    public static VideoImageLoader imageloader;
//    private Context context;
    // 线程池
    private ExecutorService executorService;
//    private String path = "";

    private VideoImageLoader() {
//        this.context = context;
        executorService = Executors.newFixedThreadPool(5);
    }

    public static VideoImageLoader getInstance() {
        if (imageloader == null) {
            imageloader = new VideoImageLoader();
        }
        return imageloader;
    }

    // 最主要的方法
    public void DisplayImage(String url, ImageView imageView) {
//        imageView.setImageResource(R.drawable.moren_icon);
        String key = MD5Util.md5(url);
        String path = ConstTaskTag.imageCacher()+key+ ".png";
        File file = new File(path);
        if (file.exists()){
            ImageLoader.getInstance()
                    .displayImage("file://"+path, imageView, ImageLoadOptions.options);
        } else {
            queuePhoto(path,url, imageView);
        }
    }

    boolean lanscape;



    private void queuePhoto(String path,String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(path,url, imageView, lanscape);
        executorService.submit(new PhotosLoader(path,p));
//        new Thread(new PhotosLoader(p)).start();
    }





    // Task for the queue
    private class PhotoToLoad {
        public String url,path;
        public ImageView imageView;
        public boolean isLandscape;

        public PhotoToLoad(String path,String u, ImageView i, boolean isLandscape) {
            this.path=path;
            url = u;
            imageView = i;
            this.isLandscape = isLandscape;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        String path;
        PhotosLoader(String path,PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
            this.path=path;
        }

        @Override
        public void run() {

            Bitmap bmp = VidoHelperUtils.createVideoThumbnail(photoToLoad.url, 400, 400);
            bmp = rotatePortrait(bmp, photoToLoad.isLandscape, photoToLoad.url);

            try {
                saveBitmap(path,bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            BitmapNewDisplayer bd = new BitmapNewDisplayer(path, photoToLoad);
            // 更新的操作放在UI线程中
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
            a = null;
        }


    }

    public Bitmap rotatePortrait(Bitmap bmp, boolean isLandscape, String path) {
        if (isLandscape) {
            int angle = FileUtil.readPictureDegree(path);
            if (angle == 0) {
                if (bmp.getHeight() > bmp.getWidth()) {
                    bmp = FileUtil.rotaingImageView(90, bmp);
                }
            }
        }
        return bmp;
    }



    // 用于在UI线程中更新界面
//    class BitmapDisplayer implements Runnable {
//        Bitmap bitmap;
//        PhotoToLoad photoToLoad;
//
//        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
//            bitmap = b;
//            photoToLoad = p;
//        }
//
//        public void run() {
//            File file = new File(path);
//            if (file.exists()){
//                com.nostra13.universalimageloader.core.ImageLoader.getInstance()
//                        .displayImage("file://"+path, photoToLoad.imageView, ImageLoadOptions.options);
//            } else {
//                if (bitmap != null)
//                    photoToLoad.imageView.setImageBitmap(bitmap);
//            }
//
//        }
//    }

    class BitmapNewDisplayer implements Runnable {
        PhotoToLoad photoToLoad;
        String path;
        public BitmapNewDisplayer(String path,PhotoToLoad p) {
            photoToLoad = p;
            this.path=path;
        }

        public void run() {
            File file = new File(path);
            if (file.exists()){
                ImageLoader.getInstance()
                        .displayImage("file://"+path, photoToLoad.imageView, ImageLoadOptions.options);
            }
        }
    }

    public void saveBitmap(String path,Bitmap mBitmap) throws IOException {
        File f = new File(path);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
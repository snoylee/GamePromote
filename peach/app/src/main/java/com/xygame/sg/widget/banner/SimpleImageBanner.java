package com.xygame.sg.widget.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.banner.widget.Banner.BaseIndicaorBanner;
import com.xygame.sg.R;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.utils.ViewFindUtils;
import com.xygame.sg.widget.banner.entity.BannerItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleImageBanner extends BaseIndicaorBanner<BannerItem, SimpleImageBanner> {
    private ColorDrawable colorDrawable;
    private VideoImageLoader videoImageLoader;
    private ImageLoader imageLoader;
    public SimpleImageBanner(Context context) {
        this(context, null, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        colorDrawable = new ColorDrawable(Color.parseColor("#555555"));
        videoImageLoader = VideoImageLoader.getInstance();
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        final BannerItem item = list.get(position);
        tv.setText(item.title);
    }

    @Override
    public View onCreateItemView(int position) {

        View inflate = null;
        final BannerItem item = list.get(position);
        if (item.getType() == 0){
            inflate = View.inflate(context, R.layout.adapter_simple_image, null);
            ImageView iv = ViewFindUtils.find(inflate, R.id.iv);
            String imgUrl = item.imgUrl;

            if (!TextUtils.isEmpty(imgUrl)) {
                imageLoader.loadImageNoThume(imgUrl, iv, false);
            } else {
                iv.setImageDrawable(colorDrawable);
            }
        } else if (item.getType() == 1){
            inflate = View.inflate(context, R.layout.adapter_simple_video_banner, null);
            ImageView videoIv = ViewFindUtils.find(inflate, R.id.video_iv);
            final String imgUrl = item.getVideoUrl();
            if (!TextUtils.isEmpty(imgUrl)) {
                videoImageLoader.DisplayImage(imgUrl,videoIv);
            } else {
                videoIv.setImageDrawable(colorDrawable);
            }
        }
        return inflate;
    }

//    Handler myHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//
//                case 0:
////                    videoIv.setImageBitmap(bitmap);
//
//                    try {
//                        saveMyBitmap("hhhhhhhhhhhhhh",bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage("/sdcard/hhhhhhhhhhhhhh.png", videoIv);
//                    break;
//
//            }
//
//            super.handleMessage(msg);
//        }
//    };

    public void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {
        File f = new File("/sdcard/" + bitName + ".png");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

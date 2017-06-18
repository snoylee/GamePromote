package com.xygame.second.sg.localvideo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

import java.util.List;

public class LocalVideoActivity extends SGBaseActivity implements OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName;
    private View backButton;
    private ListView mJieVideoListView;
    private JieVideoListViewAdapter mJieVideoListViewAdapter;
    private List<Video> listVideos;
    private int videoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_video_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        mJieVideoListView = (ListView) findViewById(R.id.jievideolistfile);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
    }

    private void initListensers() {
        backButton.setOnClickListener(this);
        mJieVideoListView.setOnItemClickListener(this);
    }

    private void initDatas() {
        titleName.setText("本地视频");
        AbstructProvider provider = new VideoProvider(this);
        listVideos = provider.getList();
        videoSize = listVideos.size();
        mJieVideoListViewAdapter = new JieVideoListViewAdapter(this, listVideos);
        mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
        loadImages();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Video item=listVideos.get(position);
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_RECORDER_SUCCESS);
        intent.putExtra("flag", "local");
        intent.putExtra("temp",item.getPath());
        intent.putExtra("bean", item);
        sendBroadcast(intent);
        finish();
    }

    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
            new LoadImagesFromSDCard().execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                new LoadImagesFromSDCard().execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }

    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            mJieVideoListViewAdapter.addPhoto(image);
            mJieVideoListViewAdapter.notifyDataSetChanged();
        }
    }

//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        final ListView grid = mJieVideoListView;
//        final int count = grid.getChildCount();
//        final LoadedImage[] list = new LoadedImage[count];
//
//        for (int i = 0; i < count; i++) {
//            final ImageView v = (ImageView) grid.getChildAt(i);
//            list[i] = new LoadedImage(
//                    ((BitmapDrawable) v.getDrawable()).getBitmap());
//        }
//
//        return list;
//    }

    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bitmap = null;
            for (int i = 0; i < videoSize; i++) {
                bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, Thumbnails.MINI_KIND);
                if (bitmap != null) {
                    publishProgress(new LoadedImage(bitmap));
                }
            }
            return null;
        }

        @Override
        public void onProgressUpdate(LoadedImage... value) {
            addImage(value);
        }
    }
}

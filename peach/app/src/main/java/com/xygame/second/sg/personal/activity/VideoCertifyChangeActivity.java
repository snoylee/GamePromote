package com.xygame.second.sg.personal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.vido.VideoPlayerActivity;

public class VideoCertifyChangeActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName,rightButtonText;
    private View backButton, rightButton,playButton;
    private ImageView id_img;
    private VideoImageLoader videoImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_change_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        playButton=findViewById(R.id.playButton);
        rightButton = findViewById(R.id.rightButton);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        id_img = (ImageView) findViewById(R.id.id_img);
    }

    private void initListensers() {
        playButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        id_img.setImageResource(R.drawable.default_avatar);
        videoImageLoader  = VideoImageLoader.getInstance();
        titleName.setText("形像视频");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("更改");
        videoImageLoader.DisplayImage(UserPreferencesUtil.getUserVideoAuthUrl(this), id_img);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            Intent intent = new Intent(this, VideoCertifyActivity.class);
            startActivity(intent);
            finish();
        }else if (v.getId()==R.id.playButton){
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("vidoUrl", UserPreferencesUtil.getUserVideoAuthUrl(this));
            startActivity(intent);
        }
    }
}

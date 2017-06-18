package com.xygame.second.sg.comm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xygame.sg.R;

public class MoreMusicActivity extends Activity implements View.OnClickListener{

    private View backButton;
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_music);
        initViews();
        initListeners();
        initDatas();

    }

    private void initViews(){
        backButton=findViewById(R.id.backButton);
        titleName=(TextView)findViewById(R.id.titleName);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        titleName.setText("更多音乐");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.backButton){
            finish();
        }
    }
}

package com.xygame.sg.activity.commen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.main.MainFrameActivity;
import com.xygame.sg.widget.banner.SimpleGuideBanner;

import java.util.ArrayList;

public class IndexActivity extends SGBaseActivity {
    private SimpleGuideBanner sgb;
    private Class<? extends ViewPager.PageTransformer> transformerClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
    }

    private void initView() {
        sgb = (SimpleGuideBanner) findViewById(R.id.sgb);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.guide_img_1);
        list.add(R.drawable.guide_img_2);
        list.add(R.drawable.guide_img_3);
        sgb.setSource(list)
                .startScroll();
        sgb.setOnJumpClickL(new SimpleGuideBanner.OnJumpClickL() {
            @Override
            public void onJumpClick() {
                Intent intent = new Intent(IndexActivity.this, MainFrameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

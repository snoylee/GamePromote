package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

public class SortPriceView extends SGBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_price_view);
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "null");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.sort_category_tv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "sort");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.delete_category_tv).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, "delete");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

}

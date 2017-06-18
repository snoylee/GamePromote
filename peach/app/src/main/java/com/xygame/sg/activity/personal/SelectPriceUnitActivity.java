package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.SelectPriceUnitListAdapter;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.ClearEditText;

import java.util.Map;

public class SelectPriceUnitActivity extends SGBaseActivity implements View.OnClickListener {
    private ImageView backView;
    private TextView backViewText;
    private TextView titleName;
    private TextView rightButtonText;

    private ListView category_lv;
    private ClearEditText category_et;
    private SelectPriceUnitListAdapter adapter;
    private String[] priceUnitArr;
    private int selectUnitId = -1;
    private String selectUnitName = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_price_unit);
        initViews();
        addListener();
    }

    private void initViews() {

        backView = (ImageView) findViewById(R.id.backView);
        backViewText = (TextView) findViewById(R.id.backViewText);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        backView.setVisibility(View.GONE);
        backViewText.setVisibility(View.VISIBLE);
        backViewText.setText(getText(R.string.cancel));
        backViewText.setTextColor(getResources().getColor(R.color.tab_select));

        titleName.setText(getText(R.string.title_activity_select_price_unit));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.sure));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));

        category_lv = (ListView) findViewById(R.id.category_lv);
        category_et = (ClearEditText) findViewById(R.id.category_et);
        priceUnitArr = getResources().getStringArray(R.array.price_unit);

        adapter = new SelectPriceUnitListAdapter(this,priceUnitArr,selectUnitId);
        category_lv.setAdapter(adapter);

    }

    private void addListener() {
        backViewText.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectedId(i);
                adapter.notifyDataSetChanged();

                category_et.setText(priceUnitArr[i]);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backViewText:
                finish();
                break;
            case R.id.rightButtonText:
                sureUnit();
                break;

        }
    }

    private void sureUnit() {
        if (category_et.getText().equals("")){
            Toast.makeText(this, category_et.getHint(), Toast.LENGTH_SHORT).show();
        }else {
            String categoryName = category_et.getText().toString();
            selectUnitName = categoryName;
            Intent mIntent = new Intent();
            mIntent.putExtra("selectUnitName", selectUnitName);
            setResult(RESULT_OK, mIntent);
            finish();
        }
    }
}
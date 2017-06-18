package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.haarman.listviewanimations.view.DynamicListView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.DeleteSubPriceAdapter;
import com.xygame.sg.activity.personal.adapter.OrderResumeAdapter;
import com.xygame.sg.activity.personal.adapter.SortSubPriceAdapter;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.task.indivual.OrderModelPrice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class SortCategoryActivity extends SGBaseActivity implements View.OnClickListener{
    private LayoutInflater inflater;

    private ImageView backView;
    private ImageView select_category_right_arrow_iv;
    private TextView category_tv;
    private TextView backViewText;
    private TextView titleName;
    private TextView rightButtonText;

    private DynamicListView mListView;
    private LinearLayout listContentView;

    private SortSubPriceAdapter adapter;
    private List<PriceBean> priceBeanList = new ArrayList<PriceBean>();



    private List<PriceBean> orderDatas= new ArrayList<PriceBean>();
    private String priceTypeName;
    VisitUnit visitUnit = new VisitUnit(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_sort_category, null));
        priceBeanList = (List<PriceBean>) getIntent().getSerializableExtra("priceBeanList");
        priceTypeName = getIntent().getStringExtra("priceTypeName");
        inflater = getLayoutInflater();
        initViews();
        addListener();
    }

    private void initViews() {
        select_category_right_arrow_iv  = (ImageView) findViewById(R.id.select_category_right_arrow_iv);
        category_tv = (TextView) findViewById(R.id.category_tv);
        backView = (ImageView) findViewById(R.id.backView);
        backViewText = (TextView) findViewById(R.id.backViewText);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        select_category_right_arrow_iv.setVisibility(View.GONE);
        category_tv.setText(priceTypeName);

        backView.setVisibility(View.GONE);
        backViewText.setVisibility(View.VISIBLE);
        backViewText.setText(getText(R.string.cancel));
        backViewText.setTextColor(getResources().getColor(R.color.tab_select));

        titleName.setText(getText(R.string.title_activity_sort_category));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.save));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));
        listContentView=(LinearLayout)findViewById(R.id.listContentView);


        mListView = new DynamicListView(this);
        mListView.setDivider(null);

        adapter = new SortSubPriceAdapter(this,priceBeanList);

        AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(adapter);
        animAdapter.setAbsListView(mListView);
        mListView.setAdapter(animAdapter);
        listContentView.addView(mListView);
    }

    private void addListener() {
        backViewText.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backViewText:
                finish();
                break;
            case R.id.rightButtonText:
                saveSort();
                break;
        }
    }

    private void saveSort() {
        List<PriceBean> tempDatas=adapter.getAll();
        for(int i=0;i<tempDatas.size();i++){
            PriceBean it=tempDatas.get(i);
            it.setLocIndex(i+1);
            orderDatas.add(it);
        }
//        new Action("#.indivual.OrderModelPrice(${cudModelPrice})", this, null, visitUnit).run();
        new Action(OrderModelPrice.class,"${cudModelPrice}", this, null, visitUnit).run();
    }

    public void responseHandler(List<Map> resultListMap) {

        Intent intent = new Intent();
        intent.putExtra("priceBeanList", (Serializable) orderDatas);
        Toast.makeText(getApplicationContext(), "排序成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, intent);
        finish();
    }

    public List<PriceBean> getOrderDatas() {
        return orderDatas;
    }

}

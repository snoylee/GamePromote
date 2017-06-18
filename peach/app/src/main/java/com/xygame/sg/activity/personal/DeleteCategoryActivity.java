package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.adapter.DeleteSubPriceAdapter;
import com.xygame.sg.activity.personal.adapter.ManageSubPriceAdapter;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.task.indivual.DeleteModelPrice;
import com.xygame.sg.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class DeleteCategoryActivity extends Activity implements View.OnClickListener {
    private LayoutInflater inflater;

    private View backButton;
    private ImageView select_category_right_arrow_iv;
    private TextView category_tv;
    private TextView backViewText;
    private TextView titleName;
    private TextView rightButtonText;
    private ListView second_category_lv;
    private DeleteSubPriceAdapter deleteSubPriceAdapter;
    private List<PriceBean> priceBeanList = new ArrayList<PriceBean>();



    private List<PriceBean> deleteDatas;
    /**
     * 拍摄大类name
     */
    private String priceTypeName;
    VisitUnit visitUnit = new VisitUnit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_delete_category, null));
        priceBeanList = (List<PriceBean>) getIntent().getSerializableExtra("priceBeanList");
        priceTypeName = getIntent().getStringExtra("priceTypeName");
        inflater = getLayoutInflater();
        initViews();
        addListener();
    }

    private void initViews() {
        select_category_right_arrow_iv = (ImageView) findViewById(R.id.select_category_right_arrow_iv);
        category_tv = (TextView) findViewById(R.id.category_tv);
        backButton = findViewById(R.id.backButton);
        backViewText = (TextView) findViewById(R.id.backViewText);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);

        select_category_right_arrow_iv.setVisibility(View.GONE);
        category_tv.setText(priceTypeName);

        backViewText.setVisibility(View.GONE);
        backViewText.setText(getText(R.string.finish));
        backViewText.setTextColor(getResources().getColor(R.color.tab_select));

        titleName.setText(getText(R.string.title_activity_delete_category));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.delete));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));

        second_category_lv = (ListView) findViewById(R.id.second_category_lv);


        deleteSubPriceAdapter = new DeleteSubPriceAdapter(this,priceBeanList);
        second_category_lv.setAdapter(deleteSubPriceAdapter);

    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        second_category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteSubPriceAdapter.setSelectedItem(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.rightButtonText:
                deletePrice();
                break;
        }
    }

    private void deletePrice() {
        deleteDatas=deleteSubPriceAdapter.getSelectDatas();
        if(deleteDatas.size()==0){
            Toast.makeText(getApplicationContext(), "请至少选择一项", Toast.LENGTH_SHORT).show();
        }else{
            new Action(DeleteModelPrice.class,"${cudModelPrice}", this, null, visitUnit).run();
        }

    }

    public void responseHandler(List< Map > resultListMap) {
//        Intent mIntent = new Intent();
//        if (fromFlag == Constants.ADD_PRICE){
//            Map resultMap = resultListMap.get(0);
//            priceBean.setId((String) resultMap.get("id"));
//            priceBean.setUucode((String) resultMap.get("uucode"));
//
//        }
//
//        mIntent.putExtra("priceBean",priceBean);
//
//
//
        Intent deleteIntent = new Intent();
        deleteIntent.putExtra("priceBeanList", (Serializable) deleteSubPriceAdapter.getLeaveDatas());
        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, deleteIntent);
        finish();
    }

    public List<PriceBean> getDeleteDatas() {
        return deleteDatas;
    }

}
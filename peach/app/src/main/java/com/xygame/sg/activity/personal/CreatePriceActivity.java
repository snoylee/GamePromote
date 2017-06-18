package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.personal.adapter.SubPriceAdapter;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreatePriceActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;

    private LayoutInflater inflater;
    private RelativeLayout first_category_rl;
    private TextView category_tv;
    private ListView second_category_lv;
    private RelativeLayout add_second_category_rl;

    private String selectFirstCategoryId = "-1";

    private ShootTypeBean shootTypeBean;

    private SubPriceAdapter subPriceAdapter;
    private List<PriceBean> priceBeanList = new ArrayList<PriceBean>();

    private static final int SELECT_FIRST_CATEGORY = 1;
//    public static final int ADD_PRICE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_price);
        inflater = getLayoutInflater();
        initViews();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);

        titleName.setText(getText(R.string.title_activity_create_price));

        first_category_rl = (RelativeLayout) findViewById(R.id.first_category_rl);
        category_tv = (TextView) findViewById(R.id.category_tv);
        second_category_lv = (ListView) findViewById(R.id.second_category_lv);
        add_second_category_rl = (RelativeLayout) findViewById(R.id.add_second_category_rl);
        subPriceAdapter = new SubPriceAdapter(this,priceBeanList);
        second_category_lv.setAdapter(subPriceAdapter);

    }

    private void addListener() {
        backButton.setOnClickListener(this);
        first_category_rl.setOnClickListener(this);
        add_second_category_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                if (priceBeanList.size()>0){//有数据变动
                    Intent intent=new Intent();
                    intent.setAction(Constants.ACTION_PRICE_SUCCESS);
                    sendBroadcast(intent);
                }
                finish();
                break;
            case R.id.first_category_rl:
                Intent firstIntent = new Intent(this,SelectFirstCategoryActivity.class);
                firstIntent.putExtra("selectFirstCategoryId",selectFirstCategoryId);
                startActivityForResult(firstIntent, SELECT_FIRST_CATEGORY);
                break;
            case R.id.add_second_category_rl:
                if (category_tv.getText().equals("")){
                    Toast.makeText(this,"请先选择拍摄大类",Toast.LENGTH_SHORT).show();
                }else {
                    Intent addintent = new Intent(this,EditSecondCategoryActivity.class);
//                    List<Map> mapList = new ArrayList<Map>();
//                    mapList.add(selectedMap);
                    addintent.putExtra("selectedMap",shootTypeBean);
                    addintent.putExtra("fromFlag",Constants.ADD_PRICE);
                    startActivityForResult(addintent, Constants.ADD_PRICE);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (priceBeanList.size()>0){//有数据变动
            Intent intent=new Intent();
            intent.setAction(Constants.ACTION_PRICE_SUCCESS);
            sendBroadcast(intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_FIRST_CATEGORY:
                    shootTypeBean = (ShootTypeBean) data.getSerializableExtra("selectedMap");
                    String typeName = shootTypeBean.getTypeName();
                    category_tv.setText(typeName);
                    selectFirstCategoryId = shootTypeBean.getTypeId()+"";
                    break;
                case Constants.ADD_PRICE:
                    PriceBean priceBean = (PriceBean) data.getExtras().get("priceBean");
                    subPriceAdapter.addData(priceBean);
                    subPriceAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }
}

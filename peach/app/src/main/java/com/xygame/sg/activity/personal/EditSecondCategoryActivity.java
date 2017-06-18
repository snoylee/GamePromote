package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.define.view.SingleWheelView;
import com.xygame.sg.task.indivual.CudModelPrice;
import com.xygame.sg.task.notice.CloseNotice;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class EditSecondCategoryActivity extends SGBaseActivity implements View.OnClickListener {
    private ImageView backView;
    private TextView backViewText;
    private TextView titleName;
    private TextView rightButtonText;

    private RelativeLayout second_category_rl;
    private TextView category_tv;
    private RelativeLayout price_rl;
    private EditText price_tv;
    private RelativeLayout price_unit_rl;
    private TextView price_unit_tv;
    private RelativeLayout per_num_rl;
    private TextView per_num_tv;

    private ShootTypeBean firstCategoryMap;
    private int fromFlag;
    private PriceBean selectedPriceBean = new PriceBean();

    private String selectSecondCategoryId = "-1";
    private String selectSecondCategoryName = "-1";

    private String selectUnitName = "-1";

    private static final int SELECT_SECOND_CATEGORY = 1;
    private static final int SELECT_PEOPLE_NUM = 2;
    private static final int SELECT_PRICE_UNIT = 3;



    private PriceBean priceBean = new PriceBean();
    VisitUnit visitUnit = new VisitUnit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_edit_second_category, null));
//        firstCategoryMap = (Map)((List) getIntent().getExtras().get("selectedMap")).get(0);
        firstCategoryMap = (ShootTypeBean) getIntent().getSerializableExtra("selectedMap");
        fromFlag = getIntent().getIntExtra("fromFlag",-1);
        if (fromFlag == Constants.EDIT_PRICE){
            priceBean = (PriceBean) getIntent().getSerializableExtra("selectedPriceBean");
        }
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


        if (fromFlag == Constants.ADD_PRICE){
            titleName.setText("新增拍摄小类");
        } else {
            titleName.setText(getText(R.string.title_activity_edit_second_category));
        }
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.sure));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));

        second_category_rl = (RelativeLayout) findViewById(R.id.second_category_rl);
        category_tv = (TextView) findViewById(R.id.category_tv);
        price_rl = (RelativeLayout) findViewById(R.id.price_rl);
        price_tv = (EditText) findViewById(R.id.price_tv);
        price_unit_rl = (RelativeLayout) findViewById(R.id.price_unit_rl);
        price_unit_tv = (TextView) findViewById(R.id.price_unit_tv);
        per_num_rl = (RelativeLayout) findViewById(R.id.per_num_rl);
        per_num_tv = (TextView) findViewById(R.id.per_num_tv);

        if (fromFlag == Constants.EDIT_PRICE){
            category_tv.setText(priceBean.getItemName());
            price_tv.setText(priceBean.getPrice()+"");
            price_unit_tv.setText(priceBean.getPriceUnit());
            if (!priceBean.getLimitParter().equals("")){
                per_num_tv.setText(priceBean.getLimitParter());
            }
        }

    }

    private void addListener() {
        backViewText.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        second_category_rl.setOnClickListener(this);
        price_unit_rl.setOnClickListener(this);
        per_num_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backViewText:
                finish();
                break;
            case R.id.rightButtonText:

                savePrice();

                break;
            case R.id.second_category_rl:
                Intent secondIntent = new Intent(EditSecondCategoryActivity.this,SelectSecondCategoryActivity.class);
                secondIntent.putExtra("selectSecondCategoryId", selectSecondCategoryId);
                List<ShootSubTypeBean> secondCategoryMap = firstCategoryMap.getSubTypes();
                secondIntent.putExtra("secondCategoryMap",(Serializable)secondCategoryMap);
                startActivityForResult(secondIntent, SELECT_SECOND_CATEGORY);
                break;
            case R.id.price_unit_rl:
                Intent priceUnitIntent = new Intent(EditSecondCategoryActivity.this,SelectPriceUnitActivity.class);
                startActivityForResult(priceUnitIntent,SELECT_PRICE_UNIT);
                break;
            case R.id.per_num_rl:
                String title;
                String[] scope = new String[100] ;
                scope[0] = "不限";
                int j = 1;
                int[] init = new int[1];
                title = "拍摄人数不得多于";
                for (int i=1;i<100;i++){
                    scope[i] = ""+(i+1);
                }
                init[0] = 0;
                Intent intent4 = new Intent(this, SingleWheelView.class);
                if (scope != null) {
                    for (int i = 1; i <= j; i++) {
                        intent4.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
                                .putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
                                .putExtra(Constants.SIGLE_WHEEL_ITEM + i,
                                        init[i - 1]);
                    }
                    startActivityForResult(intent4, SELECT_PEOPLE_NUM);

                }

                break;
        }
    }

    private void savePrice() {
        priceBean.setPriceType(firstCategoryMap.getTypeId()+"");
        priceBean.setItemName(category_tv.getText().toString());
        if(!price_tv.getText().toString().equals("")){
            priceBean.setPrice(Integer.parseInt(price_tv.getText().toString()));
        } else{
            priceBean.setPrice(-1);
        }

        priceBean.setPriceUnit(price_unit_tv.getText().toString());


        if (per_num_tv.getText().toString().equals("")||per_num_tv.getText().toString().equals("不限") ){
            priceBean.setLimitParter("");
        } else {
            priceBean.setLimitParter(per_num_tv.getText().toString().replace("人", ""));
        }
        if (fromFlag == Constants.ADD_PRICE){
            priceBean.setUucode(System.currentTimeMillis()+ UserPreferencesUtil.getUserId(this));
            priceBean.setLocIndex(1);
        }

//        new Action("#.indivual.CudModelPrice(${cudModelPrice})", this, null, visitUnit).run();
        new Action(CudModelPrice.class, "${cudModelPrice}", this, null, visitUnit).run();
    }


    public void responseHandler(List<Map> resultListMap) {
        Intent mIntent = new Intent();
        if (fromFlag == Constants.ADD_PRICE){
            Map resultMap = resultListMap.get(0);
            priceBean.setId((String) resultMap.get("id"));
            priceBean.setUucode((String) resultMap.get("uucode"));

        }

        mIntent.putExtra("priceBean",priceBean);
        setResult(RESULT_OK, mIntent);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_PEOPLE_NUM:
                    if (data == null) {
                        return;
                    }
                    String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                    if(s.equals("不限")){
                        per_num_tv.setText(s);
                    } else{
                        per_num_tv.setText(s+"人");
                    }

                    break;
                case SELECT_SECOND_CATEGORY:
                    selectSecondCategoryId = data.getStringExtra("selectSecondCategoryId");
                    selectSecondCategoryName = data.getStringExtra("selectSecondCategoryName");
                    category_tv.setText(selectSecondCategoryName);
                    break;
                case SELECT_PRICE_UNIT:
                    selectUnitName = data.getStringExtra("selectUnitName");
                    price_unit_tv.setText(selectUnitName);
                    break;

            }

        }
    }

    public PriceBean getPriceBean() {
        return priceBean;
    }
}

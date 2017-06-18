package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.activity.personal.adapter.ManageSubPriceAdapter;
import com.xygame.sg.activity.personal.bean.ModelPriceVo;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.activity.personal.bean.QueryModelPriceView;
import com.xygame.sg.define.view.SortPriceView;
import com.xygame.sg.task.PopWinHelper;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class EditPriceActivity extends SGBaseActivity implements View.OnClickListener {

    //    private LinearLayout second_category_ll;
    private LayoutInflater inflater;

    private View backButton;
    private TextView titleName;
    private TextView rightButtonText;

    private RelativeLayout first_category_rl;
    private ImageView select_category_right_arrow_iv;
    private TextView category_tv;
    private ListView second_category_lv;

    private RelativeLayout add_second_category_rl;

    private QueryModelPriceView toEditPriceItemMap;
    /**
     * 选择的拍摄大类回传的map
     */
    private ShootTypeBean selectedMap;
    /**
     * 拍摄大类name
     */
    private String priceTypeName;
    /**
     * 拍摄大类id
     */
    private String priceType;
    private ManageSubPriceAdapter manageSubPriceAdapter;
    private List<PriceBean> priceBeanList = new ArrayList<PriceBean>();

    private PopWinHelper popWinHelper = PopWinHelper.newInstance();

    private static final int SELECT_FIRST_CATEGORY = 1;
    VisitUnit visitUnit = new VisitUnit();
    private static final int SORT_DELETE_CATEGORY = 111;
    private int toEditPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_edit_price, null));
        toEditPriceItemMap = (QueryModelPriceView)getIntent().getExtras().get("toEditPriceItemMap");
        inflater = getLayoutInflater();
        initViews();
        addListener();
        initData();
    }

    private void initData() {
        List<ShootTypeBean>  typeList=SGApplication.getInstance().getTypeList();
        if (typeList!=null){
            for (ShootTypeBean shootTypeBean : typeList) {
                String itemId = shootTypeBean.getTypeId()+"";
                if (itemId.equals(priceType)) {
                    List<ShootSubTypeBean> subTypes = shootTypeBean.getSubTypes();
                    selectedMap.setSubTypes(subTypes);
                    break;
                }
            }
        }

        //以防拍摄小类获取不到数据故将下面俩个移到此处
        add_second_category_rl.setOnClickListener(this);
        second_category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toEditPos = i;
                Intent editintent = new Intent(EditPriceActivity.this, EditSecondCategoryActivity.class);
                editintent.putExtra("selectedMap", selectedMap);
                editintent.putExtra("selectedPriceBean", priceBeanList.get(i));
                editintent.putExtra("fromFlag", Constants.EDIT_PRICE);
                startActivityForResult(editintent, Constants.EDIT_PRICE);
            }
        });
    }



    private void initViews() {
        first_category_rl = (RelativeLayout) findViewById(R.id.first_category_rl);
        select_category_right_arrow_iv = (ImageView) findViewById(R.id.select_category_right_arrow_iv);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        category_tv = (TextView) findViewById(R.id.category_tv);

        select_category_right_arrow_iv.setVisibility(View.GONE);

        titleName.setText(getText(R.string.title_activity_edit_price));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.manager));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));

        second_category_lv = (ListView) findViewById(R.id.second_category_lv);
        add_second_category_rl = (RelativeLayout) findViewById(R.id.add_second_category_rl);
        List<ModelPriceVo> subPriceMapList = toEditPriceItemMap.getPrices();
        priceType = toEditPriceItemMap.getPriceType()+"";
        priceTypeName = toEditPriceItemMap.getPriceTypeName();

        selectedMap = new ShootTypeBean();
        selectedMap.setTypeId(Integer.parseInt(priceType));
        selectedMap.setTypeName(priceTypeName);


        category_tv.setText(priceTypeName);

        for (ModelPriceVo map : subPriceMapList) {
            PriceBean priceBean = new PriceBean();
            priceBean.setPriceType(priceType);
            priceBean.setPriceTypeName(priceTypeName);
            priceBean.setId(map.getId()+"");
            priceBean.setItemName(map.getItemName()+"");
            priceBean.setPrice(map.getPrice());
            priceBean.setPriceUnit(map.getPriceUnit());
            if (map.getLimitParter() == null) {
                priceBean.setLimitParter("");
            } else {
                priceBean.setLimitParter(map.getLimitParter()+"");
            }
            if (map.getLocIndex() == null) {
                priceBean.setLocIndex(1);
            } else {
                priceBean.setLocIndex(map.getLocIndex());
            }
            priceBeanList.add(priceBean);
        }

        manageSubPriceAdapter = new ManageSubPriceAdapter(this, priceBeanList);
        second_category_lv.setAdapter(manageSubPriceAdapter);

//        //=================================
//        for (int i = 0; i < 3; i++) {
//            View category_item = inflater.inflate(R.layout.category_item, null);
//            second_category_ll.addView(category_item);
//            category_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(EditPriceActivity.this,EditSecondCategoryActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }
//        //=================================

    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
//        first_category_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_PRICE_SUCCESS);
                sendBroadcast(intent);
                finish();
                break;
            case R.id.rightButtonText:
                selectAction();
                break;
            case R.id.first_category_rl:
                Intent firstIntent = new Intent(this, SelectFirstCategoryActivity.class);
                firstIntent.putExtra("selectFirstCategoryId", priceType);
                startActivityForResult(firstIntent, SELECT_FIRST_CATEGORY);
                break;
            case R.id.add_second_category_rl:
                if (category_tv.getText().equals("")) {
                    Toast.makeText(this, "请先选择拍摄大类", Toast.LENGTH_SHORT).show();
                } else {
                    Intent addintent = new Intent(this, EditSecondCategoryActivity.class);
                    addintent.putExtra("selectedMap", selectedMap);
                    addintent.putExtra("fromFlag", Constants.ADD_PRICE);
                    startActivityForResult(addintent, Constants.ADD_PRICE);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_PRICE_SUCCESS);
        sendBroadcast(intent);
        finish();
    }

    private void selectAction() {
        Intent intent = new Intent(this, SortPriceView.class);
        startActivityForResult(intent, SORT_DELETE_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_FIRST_CATEGORY:
                    selectedMap = (ShootTypeBean) data.getExtras().getSerializable("selectedMap");
                    priceTypeName = selectedMap.getTypeName();
                    category_tv.setText(priceTypeName);
                    priceType = selectedMap.getTypeId()+"";
                    break;
                case Constants.ADD_PRICE:
                    PriceBean priceBean = (PriceBean) data.getExtras().get("priceBean");
                    manageSubPriceAdapter.addData(priceBean);
                    manageSubPriceAdapter.notifyDataSetChanged();
                    break;
                case Constants.EDIT_PRICE:
                    PriceBean editPriceBean = (PriceBean) data.getExtras().get("priceBean");
                    priceBeanList.set(toEditPos, editPriceBean);
                    manageSubPriceAdapter.setData(priceBeanList);
                    manageSubPriceAdapter.notifyDataSetChanged();
                    break;
                case Constants.DELETE_PRICE:
                    priceBeanList = (List<PriceBean>) data.getSerializableExtra("priceBeanList");
                    manageSubPriceAdapter.setData(priceBeanList);
                    manageSubPriceAdapter.notifyDataSetChanged();
                    break;
                case Constants.SORT_PRICE:
                    priceBeanList = (List<PriceBean>) data.getSerializableExtra("priceBeanList");
                    manageSubPriceAdapter.setData(priceBeanList);
                    manageSubPriceAdapter.notifyDataSetChanged();
                    break;
                case SORT_DELETE_CATEGORY:
                    if (Activity.RESULT_OK != resultCode || null == data) {
                        return;
                    }
                    String flag = data.getStringExtra(Constants.COMEBACK);
                    if ("sort".equals(flag)) {
                        if (priceBeanList.size() > 1) {
                            Intent sortIntent = new Intent(EditPriceActivity.this, SortCategoryActivity.class);
                            sortIntent.putExtra("priceBeanList", (Serializable) priceBeanList);
                            sortIntent.putExtra("priceTypeName", priceTypeName);
                            startActivityForResult(sortIntent, Constants.SORT_PRICE);
                        } else {
                            Toast.makeText(EditPriceActivity.this, "请先添加两条以上小类！", Toast.LENGTH_SHORT).show();
                        }
                    } else if ("delete".equals(flag)) {
                        if (priceBeanList.size() > 0) {
                            Intent deleteIntent = new Intent(EditPriceActivity.this, DeleteCategoryActivity.class);
                            deleteIntent.putExtra("priceBeanList", (Serializable) priceBeanList);
                            deleteIntent.putExtra("priceTypeName", priceTypeName);
                            startActivityForResult(deleteIntent, Constants.DELETE_PRICE);
                        } else {
                            Toast.makeText(EditPriceActivity.this,"请先添加小类！",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }

        }
    }


}

package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.ShootSubTypeBean;
import com.xygame.sg.activity.personal.adapter.SelectSecondCategoryListAdapter;
import com.xygame.sg.utils.DateTime;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.ClearEditText;

import java.util.List;
import java.util.Map;

public class SelectSecondCategoryActivity extends SGBaseActivity implements View.OnClickListener {
    private ImageView backView;
    private TextView backViewText;
    private TextView titleName;
    private TextView rightButtonText;

    private LinearLayout select_ll;
    private ClearEditText category_et;
    private ListView category_lv;
    private SelectSecondCategoryListAdapter adapter;

    private String selectSecondCategoryId = "-1";
    private String selectSecondCategoryName = "-1";
    private List<ShootSubTypeBean> dataList;

    private boolean isHasList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_second_category);
        dataList = (List<ShootSubTypeBean>) getIntent().getExtras().get("secondCategoryMap");
        selectSecondCategoryId = getIntent().getStringExtra("selectSecondCategoryId");
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

        titleName.setText(getText(R.string.title_activity_select_second_category));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.sure));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));
        select_ll = (LinearLayout) findViewById(R.id.select_ll);
        category_et = (ClearEditText) findViewById(R.id.category_et);
        category_lv = (ListView) findViewById(R.id.category_lv);
        if (dataList.size()==1 ){
            try {
                ShootSubTypeBean item = dataList.get(0);//防止空指针用
                category_et.setHint("请输入或选取拍摄小类");
                isHasList =true;
                adapter = new SelectSecondCategoryListAdapter(this,dataList,selectSecondCategoryId);
                category_lv.setAdapter(adapter);
            } catch (ClassCastException e){
                e.printStackTrace();
                select_ll.setVisibility(View.GONE);
                category_et.setHint("请输入拍摄小类");
            }
        } else if (dataList.size() >1){
            category_et.setHint("请输入或选取拍摄小类");
            isHasList =true;
            adapter = new SelectSecondCategoryListAdapter(this,dataList,selectSecondCategoryId);
            category_lv.setAdapter(adapter);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void addListener() {
        backViewText.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShootSubTypeBean bean = dataList.get(i);
                adapter.setSelectedId(bean.getTypeId()+"");
                adapter.notifyDataSetChanged();

                category_et.setText(bean.getTypeName());
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
                sureSecondCategory();

                break;

        }
    }

    private void sureSecondCategory() {
        if (category_et.getText().equals("")){
            Toast.makeText(this, category_et.getHint(), Toast.LENGTH_SHORT).show();
        }else {
            String categoryName = category_et.getText().toString();
            boolean isIn = false;
            if (isHasList){
                for (ShootSubTypeBean bean :dataList){
                    if (categoryName.equals(bean.getTypeName())){
                        selectSecondCategoryId = bean.getTypeId()+"";
                        isIn = true;
                        break;
                    }
                }
            }
            if (!isIn){
                selectSecondCategoryId = UserPreferencesUtil.getUserId(this)+DateTime.now().getTimeInMillis()+"";
            }

            selectSecondCategoryName=categoryName;

            Intent mIntent = new Intent();
            mIntent.putExtra("selectSecondCategoryName", selectSecondCategoryName);
            mIntent.putExtra("selectSecondCategoryId", selectSecondCategoryId);
            setResult(RESULT_OK, mIntent);
            finish();
        }

    }

}

package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.xygame.second.sg.biggod.adapter.SetDiscountAdapter;
import com.xygame.second.sg.biggod.adapter.SetPriceAdapter;
import com.xygame.second.sg.biggod.bean.DiscBean;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.OderBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.inteface.DiscountListener;
import com.xygame.second.sg.comm.inteface.PriceListener;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SetPriceActivity extends SGBaseActivity implements View.OnClickListener,PriceListener,DiscountListener {
    private OderBean oderBean;
    private JinPaiBigTypeBean jinPaiBigTypeBean;
    private View dismiss,comfirmButton;
    private GridView priceGrid,discountGrid;
    private TextView priceValue,discText;
    private SetPriceAdapter priceAdapter;
    private SetDiscountAdapter setDiscountAdapter;
    private DiscBean discBean;
    private PriceBean priceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_price);
        initViews();
        initListners();
        initDatas();
    }

    private void initViews() {
        comfirmButton=findViewById(R.id.comfirmButton);
        dismiss=findViewById(R.id.dismiss);
        priceGrid=(GridView)findViewById(R.id.priceGrid);
        discountGrid=(GridView)findViewById(R.id.discountGrid);
        priceValue=(TextView)findViewById(R.id.priceValue);
        discText=(TextView)findViewById(R.id.discText);
    }

    private void initListners() {
        dismiss.setOnClickListener(this);
        comfirmButton.setOnClickListener(this);
    }

    private void initDatas() {
        oderBean=(OderBean)getIntent().getSerializableExtra("bean");
        jinPaiBigTypeBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("JinPaiBigTypeBean");
        List<PriceBean> vector=new ArrayList<>();
        List<PriceBean> fuFeiDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
        List<GodLableBean> tempLables=Constants.getGodLableDatas(jinPaiBigTypeBean.getSubStr());
        List<GodLableBean> resultDatas=new ArrayList<>();
        if (!TextUtils.isEmpty(oderBean.getLocalIndex())){
            for (GodLableBean item:tempLables){
                if (item.getLocalIndex().equals(oderBean.getLocalIndex())){
                    resultDatas.add(item);
                    break;
                }else{
                    resultDatas.add(item);
                }
            }
            for (GodLableBean item1:resultDatas){
                for (PriceBean priceBean:fuFeiDatas){
                    if (item1.getTitleId().equals(priceBean.getTitleId())){
                        vector.add(priceBean);
                        break;
                    }
                }
            }
            if (vector.size()==0){
                for (PriceBean priceBean:fuFeiDatas){
                    if (oderBean.getSkillCode().equals(priceBean.getTypeId())){
                        vector.add(priceBean);
                    }
                }
            }
        }else{
            for (PriceBean priceBean:fuFeiDatas){
                if (oderBean.getSkillCode().equals(priceBean.getTypeId())){
                    vector.add(priceBean);
                }
            }
        }

        priceAdapter=new SetPriceAdapter(this,vector);
        priceGrid.setAdapter(priceAdapter);

        List<DiscBean> discDatas=new ArrayList<>();
        DiscBean item1=new DiscBean();
        item1.setId("100");
        item1.setOral("无折扣");
        discDatas.add(item1);
        DiscBean item2=new DiscBean();
        item2.setId("80");
        item2.setOral("8折");
        discDatas.add(item2);
        DiscBean item3=new DiscBean();
        item3.setId("50");
        item3.setOral("5折");
        discDatas.add(item3);
        setDiscountAdapter=new SetDiscountAdapter(this,discDatas);
        discountGrid.setAdapter(setDiscountAdapter);
        resultPrice();
        priceAdapter.addPriceListener(this);
        setDiscountAdapter.addDiscListener(this);
    }


    @Override
    public void discListener() {
        resultPrice();
    }

    @Override
    public void priceListener() {
        resultPrice();
    }

    private void resultPrice(){
        priceBean=priceAdapter.getSelectedItem();
        discBean=setDiscountAdapter.getSelectedItem();
        int discResut=(Integer.parseInt(priceBean.getPrice())*Integer.parseInt(discBean.getId()))/100;
        priceValue.setText(String.valueOf(discResut).concat("元"));
        discText.setText("(已选".concat(discBean.getOral()).concat(")"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.dismiss){
            finish();
        }else if (v.getId()==R.id.comfirmButton){
            Intent intent = new Intent();
            intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, discBean);
            intent.putExtra("priceBean",priceBean);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}

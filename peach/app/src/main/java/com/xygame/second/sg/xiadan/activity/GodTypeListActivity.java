package com.xygame.second.sg.xiadan.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.activity.GodDetailActivity;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.PopSexBean;
import com.xygame.second.sg.jinpai.adapter.PopSexAdater;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.adapter.GodTypeAdapter;
import com.xygame.second.sg.xiadan.bean.GodUserBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/26.
 */
public class GodTypeListActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2{

    private TextView titleName,jieDanNum1,onLineTimer1,jieDanNum2,onLineTimer2,userNick2,priceValue2,jieDanNum3,userNick3,priceValue3, serviceText, sexText,userLable3,userLable2;
    private View backButton,headView1,headView2,headView21,headView22,serviceView, sexView;
    private PullToRefreshListView3 PaiSList;
    private int pageSize = 15;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private boolean hotIsLoading = true;
    private GodTypeAdapter paiSAdapter;
    private PopupWindow mPopWindow;
    private View headView;
    private ImageLoader mImageLoader;
    private ImageView userImage1,userImage2,userImage3,serviceImage, sexImage;
    private JinPaiBigTypeBean jinPaiBigTypeBean;
    private List<PopSexBean> conditions;
    private  PopSexBean currPopSexBean;
    private  List<GodUserBean> topDatas,bodyDatas;
    private String flag="1";//1为智能，2为榜单
    private ProvinceBean areaBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_list_layout);
        initViews();
        initListeners();
        initDatas();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        PaiSList.setOnRefreshListener(this);
        headView1.setOnClickListener(this);
        headView21.setOnClickListener(this);
        headView22.setOnClickListener(this);
        serviceView.setOnClickListener(this);
        sexView.setOnClickListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        serviceImage = (ImageView) findViewById(R.id.serviceImage);
        sexImage = (ImageView) findViewById(R.id.sexImage);
        serviceText = (TextView) findViewById(R.id.serviceText);
        sexText = (TextView) findViewById(R.id.sexText);
        serviceView =findViewById(R.id.serviceView);
        sexView = findViewById(R.id.sexView);
        // TODO Auto-generated method stub
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        PaiSList = (PullToRefreshListView3)findViewById(R.id.noticeList);
        PaiSList.setMode(PullToRefreshBase.Mode.BOTH);
        headView=LayoutInflater.from(this).inflate(R.layout.godtype_head_layout, null);
        headView1=headView.findViewById(R.id.headView1);
        headView2=headView.findViewById(R.id.headView2);
        headView21=headView.findViewById(R.id.headView21);
        headView22=headView.findViewById(R.id.headView22);
        jieDanNum1=(TextView)headView.findViewById(R.id.jieDanNum1);
        onLineTimer1=(TextView)headView.findViewById(R.id.onLineTimer1);
        jieDanNum2=(TextView)headView.findViewById(R.id.jieDanNum2);
        onLineTimer2=(TextView)headView.findViewById(R.id.onLineTimer2);
        userNick2=(TextView)headView.findViewById(R.id.userNick2);
        priceValue2=(TextView)headView.findViewById(R.id.priceValue2);
        jieDanNum3=(TextView)headView.findViewById(R.id.jieDanNum3);
        userNick3=(TextView)headView.findViewById(R.id.userNick3);
        priceValue3=(TextView)headView.findViewById(R.id.priceValue3);
        userLable2=(TextView)headView.findViewById(R.id.userLable2);
        userLable3=(TextView)headView.findViewById(R.id.userLable3);
        userImage1=(ImageView)headView.findViewById(R.id.userImage1);
        userImage2=(ImageView)headView.findViewById(R.id.userImage2);
        userImage3=(ImageView)headView.findViewById(R.id.userImage3);
        PaiSList.addHeadViewAction(headView);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        jinPaiBigTypeBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("bean");
        titleName.setText(jinPaiBigTypeBean.getName());
        paiSAdapter = new GodTypeAdapter(this, null,jinPaiBigTypeBean);
        PaiSList.setAdapter(paiSAdapter);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        conditions=new ArrayList<>();
        areaBean =
                CacheService.getInstance().getCacheJPProvinceBean("XDProvinceBean");
        PopSexBean bean1=new PopSexBean();
        bean1.setSex("周榜");
        bean1.setId("1");
        conditions.add(bean1);

        PopSexBean bean2=new PopSexBean();
        bean2.setSex("月榜");
        bean2.setId("2");
        conditions.add(bean2);

        currPopSexBean=conditions.get(0);
        sexText.setText(currPopSexBean.getSex());
        updateOptionsViews(1);
        flag="1";
        loadAutoDatas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.headView1){
            Intent intent=new Intent(this,GodDetailActivity.class);
            intent.putExtra("userId", topDatas.get(0).getUserId());
            intent.putExtra("userName", topDatas.get(0).getUsernick());
            intent.putExtra("skillCode",jinPaiBigTypeBean.getId());
            startActivity(intent);
        }else if (v.getId()==R.id.headView21){
            if (topDatas.size()==2){
                Intent intent=new Intent(this,GodDetailActivity.class);
                intent.putExtra("userId", topDatas.get(0).getUserId());
                intent.putExtra("userName",topDatas.get(0).getUsernick());
                intent.putExtra("skillCode",jinPaiBigTypeBean.getId());
                startActivity(intent);
            }else{
                Intent intent=new Intent(this,GodDetailActivity.class);
                intent.putExtra("userId", topDatas.get(1).getUserId());
                intent.putExtra("userName",topDatas.get(1).getUsernick());
                intent.putExtra("skillCode",jinPaiBigTypeBean.getId());
                startActivity(intent);
            }
        }else if (v.getId()==R.id.headView22){
            if (topDatas.size()==2){
                Intent intent=new Intent(this,GodDetailActivity.class);
                intent.putExtra("userId", topDatas.get(1).getUserId());
                intent.putExtra("userName",topDatas.get(1).getUsernick());
                intent.putExtra("skillCode",jinPaiBigTypeBean.getId());
                startActivity(intent);
            }else{
                Intent intent=new Intent(this,GodDetailActivity.class);
                intent.putExtra("userId", topDatas.get(2).getUserId());
                intent.putExtra("userName",topDatas.get(2).getUsernick());
                intent.putExtra("skillCode",jinPaiBigTypeBean.getId());
                startActivity(intent);
            }
        }else if (v.getId()==R.id.serviceView){
            if (mPopWindow != null) {
                mPopWindow.dismiss();
                mPopWindow = null;
//                updateOptionsViews(1);
            } else {
                updateOptionsViews(1);
                flag="1";
                hotIsLoading = true;
                hotPage = 1;
                loadAutoDatas();
            }
        }else if (v.getId()==R.id.sexView){
            if (mPopWindow != null) {
                mPopWindow.dismiss();
                mPopWindow = null;
//                updateOptionsViews(2);
            } else {
                showCommenPOP();
//                updateOptionsViews(2);
            }
        }
    }

    private void showCommenPOP() {
        final PopSexAdater sexAdapter = new PopSexAdater(this, conditions);
        mPopWindow = initPopWindow(this,R.layout.pop_list_layout, true);
        View root = mPopWindow.getContentView();
        root.getBackground().setAlpha(50);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
//                updateOptionsViews(0);
            }
        });
        ListView popListView = (ListView) root.findViewById(R.id.listView);
        View bottomNullView = root.findViewById(R.id.bottomNullView);
        popListView.setAdapter(sexAdapter);

        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currPopSexBean = sexAdapter.getItem(position);
                sexText.setText(currPopSexBean.getSex());
                hotIsLoading = true;
                hotPage = 1;
                flag = "2";
                loadDatas();
                updateOptionsViews(2);
                mPopWindow.dismiss();
            }
        });

        bottomNullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateOptionsViews(0);
                mPopWindow.dismiss();
            }
        });

        if (mPopWindow.isShowing()) {
//            updateOptionsViews(0);
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(findViewById(R.id.optionsView));
        }
    }

    private PopupWindow initPopWindow(Context context, int popWinLayout,
                                      boolean isDismissMenuOutsideTouch) {

        PopupWindow mPopWin = new PopupWindow(
                ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(popWinLayout, null),
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        if (isDismissMenuOutsideTouch)
            mPopWin.setBackgroundDrawable(new BitmapDrawable());
        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
        if (mPopWin.isShowing()) {
            updateOptionsViews(0);
            mPopWin.dismiss();
            mPopWin = null;
        }
        return mPopWin;
    }


    private void updateOptionsViews(Integer index) {

        switch (index) {
            case 0:
                serviceImage.setImageResource(R.drawable.down_gray);
                sexImage.setImageResource(R.drawable.down_gray);

                serviceText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            case 1:
                serviceImage.setImageResource(R.drawable.down_green);
                sexImage.setImageResource(R.drawable.down_gray);

                serviceText.setTextColor(getResources().getColor(R.color.dark_green));
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            case 2:
                serviceImage.setImageResource(R.drawable.down_gray);
                sexImage.setImageResource(R.drawable.down_green);

                serviceText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                sexText.setTextColor(getResources().getColor(R.color.dark_green));
                break;
            default:
                break;
        }
    }

    private void loadAutoDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            obj.put("skillCode",jinPaiBigTypeBean.getId());
            if (hotPage ==1) {
                ShowMsgDialog.showNoMsg(this, true);
            }
            if (!"900".equals(jinPaiBigTypeBean.getId())&&!"1100".equals(jinPaiBigTypeBean.getId())&&!"1200".equals(jinPaiBigTypeBean.getId())){
                if (areaBean!=null){
                    if (areaBean.getProvinceCode()!=null){
                        if (!"1".equals(areaBean.getProvinceCode())){
                            obj.put("province", areaBean.getProvinceCode());
                        }
                    }
                }
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_GOD_LIST_QUTO);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GOD_LIST_QUTO);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            obj.put("skillCode",jinPaiBigTypeBean.getId());
            obj.put("rankType",currPopSexBean.getId());
            if (hotPage ==1) {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_GOD_LIST);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GOD_LIST);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
//                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
//                if (result) {
//                    if (flagIndex==0){
//                        daiSAdapter.updatePayStatus(noticeBeanItem);
//                    }else if (flagIndex==1){
//                        zhaoMAdapter.updatePayStatus(noticeBeanItem);
//                    }
//
//                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新操作
        hotIsLoading = true;
        hotPage = 1;
        if ("2".equals(flag)){
            loadDatas();
        }else{
            loadAutoDatas();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载操作
        if (hotIsLoading) {
            hotPage = hotPage + 1;
            if ("2".equals(flag)){
                loadDatas();
            }else{
                loadAutoDatas();
            }
        } else {
            falseDatas();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    PaiSList.onRefreshComplete();
                    break;
                default:
                    break;
            }
        }
    };


    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 0;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GOD_LIST:
                PaiSList.onRefreshComplete();
                if (hotPage==1){
                    headView1.setVisibility(View.GONE);
                    headView2.setVisibility(View.GONE);
                    paiSAdapter.clearDatas();
                }
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GOD_LIST_QUTO:
                PaiSList.onRefreshComplete();
                if (hotPage==1){
                    headView1.setVisibility(View.GONE);
                    headView2.setVisibility(View.GONE);
                    paiSAdapter.clearDatas();
                }
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseHotModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
            try {
                if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
                    List<GodUserBean> datas = new ArrayList<>();
                    JSONArray array2 = new JSONArray(resposeStr);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        GodUserBean item = new GodUserBean();
                        item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setSillTitle(StringUtils.getJsonValue(object1, "skillTitle"));
                        item.setAge(StringUtils.getJsonValue(object1, "age"));
                        item.setGender(StringUtils.getJsonValue(object1, "gender"));
                        item.setOrderCount(StringUtils.getJsonValue(object1, "orderCount"));
                        item.setPriceId(StringUtils.getJsonValue(object1, "priceId"));
                        item.setPriceRate(StringUtils.getJsonValue(object1, "priceRate"));
                        datas.add(item);
                    }
                    if (datas.size() < pageSize) {
                        hotIsLoading = false;
                    }
                    if (hotPage==1){
                        if (datas.size()>3){
                            topDatas=new ArrayList<>();
                            bodyDatas=new ArrayList<>();
                           for (int i=0;i<datas.size();i++){
                               if (i<3){
                                   topDatas.add(datas.get(i));
                               }else {
                                   bodyDatas.add(datas.get(i));
                               }
                           }
                            updateTopViews(topDatas);
                            paiSAdapter.addDatas(bodyDatas, hotPage);
                        }else{
                            this.topDatas=datas;
                            updateTopViews(datas);
                        }
                    }else{
                        paiSAdapter.addDatas(datas, hotPage);
                    }
                }else {
                    hotIsLoading = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void updateTopViews(List<GodUserBean> topDatas) {
        switch (topDatas.size()){
            case 0:
                headView1.setVisibility(View.GONE);
                headView2.setVisibility(View.GONE);
                break;
            case 1:
                headView1.setVisibility(View.VISIBLE);
                headView2.setVisibility(View.GONE);
                mImageLoader.loadImage(topDatas.get(0).getUserIcon(), userImage1, true);
                jieDanNum1.setText("接单".concat(topDatas.get(0).getOrderCount()).concat("次"));
                break;
            case 2:
                headView1.setVisibility(View.GONE);
                headView2.setVisibility(View.VISIBLE);
                mImageLoader.loadImage(topDatas.get(0).getUserIcon(), userImage2, true);
                mImageLoader.loadImage(topDatas.get(1).getUserIcon(), userImage3, true);
                jieDanNum2.setText("接单".concat(topDatas.get(0).getOrderCount()).concat("次"));
                jieDanNum3.setText("接单".concat(topDatas.get(1).getOrderCount()).concat("次"));
                userNick2.setText(topDatas.get(0).getUsernick());
                userNick3.setText(topDatas.get(1).getUsernick());
                List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                if (fuFeiDatas!=null){
                    for (PriceBean t:fuFeiDatas){
                        if (t.getId().equals(topDatas.get(0).getPriceId())){
                            int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(topDatas.get(0).getPriceRate()))/100;
                            priceValue2.setText(String.valueOf(value));
                            break;
                        }
                    }
                }
                if (fuFeiDatas!=null){
                    for (PriceBean t:fuFeiDatas){
                        if (t.getId().equals(topDatas.get(1).getPriceId())){
                            int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(topDatas.get(1).getPriceRate()))/100;
                            priceValue3.setText(String.valueOf(value));
                            break;
                        }
                    }
                }
                List<GodLableBean> typeLable= Constants.getGodLableDatas(jinPaiBigTypeBean.getSubStr());
                if (typeLable!=null){
                    for (GodLableBean it:typeLable){
                        if (it.getTitleId().equals(topDatas.get(0).getSillTitle())){
                            userLable2.setText(it.getTitleName());
                            break;
                        }
                    }
                }
                if (typeLable!=null){
                    for (GodLableBean it:typeLable){
                        if (it.getTitleId().equals(topDatas.get(1).getSillTitle())){
                            userLable3.setText(it.getTitleName());
                            break;
                        }
                    }
                }
                break;
            case 3:
                headView1.setVisibility(View.VISIBLE);
                headView2.setVisibility(View.VISIBLE);
                mImageLoader.loadImage(topDatas.get(0).getUserIcon(), userImage1, true);
                mImageLoader.loadImage(topDatas.get(1).getUserIcon(), userImage2, true);
                mImageLoader.loadImage(topDatas.get(2).getUserIcon(), userImage3, true);
                jieDanNum1.setText("接单".concat(topDatas.get(0).getOrderCount()).concat("次"));
                jieDanNum2.setText("接单".concat(topDatas.get(1).getOrderCount()).concat("次"));
                jieDanNum3.setText("接单".concat(topDatas.get(2).getOrderCount()).concat("次"));
                userNick2.setText(topDatas.get(1).getUsernick());
                userNick3.setText(topDatas.get(2).getUsernick());
                List<PriceBean> fuFeiDatas1= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                if (fuFeiDatas1!=null){
                    for (PriceBean t:fuFeiDatas1){
                        if (t.getId().equals(topDatas.get(1).getPriceId())){
                            int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(topDatas.get(1).getPriceRate()))/100;
                            priceValue2.setText(String.valueOf(value));
                            break;
                        }
                    }
                }
                if (fuFeiDatas1!=null){
                    for (PriceBean t:fuFeiDatas1){
                        if (t.getId().equals(topDatas.get(2).getPriceId())){
                            int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(topDatas.get(2).getPriceRate()))/100;
                            priceValue3.setText(String.valueOf(value));
                            break;
                        }
                    }
                }
                List<GodLableBean> typeLable1= Constants.getGodLableDatas(jinPaiBigTypeBean.getSubStr());
                if (typeLable1!=null){
                    for (GodLableBean it:typeLable1){
                        if (it.getTitleId().equals(topDatas.get(1).getSillTitle())){
                            userLable2.setText(it.getTitleName());
                            break;
                        }
                    }
                }
                if (typeLable1!=null){
                    for (GodLableBean it:typeLable1){
                        if (it.getTitleId().equals(topDatas.get(2).getSillTitle())){
                            userLable3.setText(it.getTitleName());
                            break;
                        }
                    }
                }
                break;
        }
    }
}
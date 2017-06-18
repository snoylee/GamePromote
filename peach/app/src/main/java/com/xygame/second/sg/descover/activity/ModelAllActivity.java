package com.xygame.second.sg.descover.activity;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.descover.adapter.ChildAreaAdater;
import com.xygame.second.sg.descover.adapter.ParentAreaAdater;
import com.xygame.second.sg.localvideo.LocalVideoActivity;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.model.adapter.ModelAllGvAdapter;
import com.xygame.sg.activity.model.bean.AllModelItemBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ModelAllActivity extends SGBaseActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, View.OnClickListener {
    private LinearLayout sexView;
    private LinearLayout areaView;
    private View search_iv, switch_show_cb;
    private TextView sexText, areaText;
    private ImageView sexImage;
    private ImageView areaImage;
    private PullToRefreshGridView gridView;
    private PopupWindow mPopWindow;
    private ModelAllGvAdapter gvAdapter;
    private int pageSize = 21;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private String reqTime, sexCode, cityCode;
    private boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_model_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        switch_show_cb = findViewById(R.id.switch_show_cb);
        search_iv = findViewById(R.id.search_iv);
        gridView = (PullToRefreshGridView) findViewById(R.id.all_model_gv);
        sexText = (TextView) findViewById(R.id.sexText);
        areaText = (TextView) findViewById(R.id.areaText);
        areaImage = (ImageView) findViewById(R.id.areaImage);
        sexImage = (ImageView) findViewById(R.id.sexImage);
        sexView = (LinearLayout) findViewById(R.id.sexView);
        areaView = (LinearLayout) findViewById(R.id.areaView);
    }

    private void initListeners() {
        switch_show_cb.setOnClickListener(this);
        search_iv.setOnClickListener(this);
        areaView.setOnClickListener(this);
        sexView.setOnClickListener(this);
        gridView.setOnRefreshListener(this);
        gridView.setMode(PullToRefreshBase.Mode.BOTH);
        gridView.setOnItemClickListener(this);
    }

    private void initDatas() {
        gvAdapter = new ModelAllGvAdapter(this, null);
        gridView.setAdapter1(gvAdapter);
        requestAll();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.areaView:
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    updateOptionsViews(2);
                } else {
                    showAreaPOP();
                    updateOptionsViews(1);
                }
                break;
            case R.id.sexView:
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                    updateOptionsViews(2);
                } else {
                    showSexPOP();
                    updateOptionsViews(0);
                }
                break;
            case R.id.search_iv:
                Intent intent = new Intent(this, SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.switch_show_cb:
                finish();
                break;
        }
    }

    private void showAreaPOP() {
        final ChildAreaAdater childTypeAdater;
        final ParentAreaAdater poarentTypeAdater;
        mPopWindow = initPopWindow(this,
                R.layout.le_type_pop_layout, true);
        View root = mPopWindow.getContentView();
        root.getBackground().setAlpha(50);

        ListView parentList = (ListView) root.findViewById(R.id.areaList);
        ListView childList = (ListView) root.findViewById(R.id.areaBlockList);
//        View bottomNullView = root.findViewById(R.id.bottomNullView);

        final List<ProvinceBean> datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
        poarentTypeAdater = new ParentAreaAdater(this, datas);
        parentList.setAdapter(poarentTypeAdater);

        PlushNoticeAreaBean areaBean = new PlushNoticeAreaBean();
        ProvinceBean pb = datas.get(1);
        pb.get();
        areaBean.setProvinceId(pb.getProvinceCode());
        areaBean.setProvinceName(pb.getProvinceName());
        childTypeAdater = new ChildAreaAdater(this, areaBean);
        childList.setAdapter(childTypeAdater);
        parentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 0) {
                    cityCode = null;
                    areaText.setText("不限");
                    updateOptionsViews(2);
                    mPopWindow.dismiss();
                    mCurrentPage = 1;
                    isLoading=true;
                    requestAll();
                } else {
                    ProvinceBean item = poarentTypeAdater.getItem(arg2);
                    poarentTypeAdater.setCurTrue(item);
                    childTypeAdater.updateList(item);
                }
            }
        });

//        bottomNullView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                updateOptionsViews(2);
//                mPopWindow.dismiss();
//            }
//        });

        childList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                CityBean cityBean = childTypeAdater.getItem(arg2);
                cityCode = cityBean.getCityCode();
                areaText.setText(cityBean.getCityName());
                updateOptionsViews(2);
                mPopWindow.dismiss();
                mCurrentPage = 1;
                isLoading=true;
                requestAll();
            }
        });

        mPopWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
                updateOptionsViews(2);
            }
        });


        if (mPopWindow.isShowing()) {
            updateOptionsViews(2);
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(findViewById(R.id.optionsView));
        }
    }

    private void showSexPOP() {
        mPopWindow = initPopWindow(this,
                R.layout.le_sex_pop_layout, true);
        View root = mPopWindow.getContentView();
        root.getBackground().setAlpha(50);
        mPopWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
                updateOptionsViews(2);
            }
        });
        View noLimit = root.findViewById(R.id.noLimit);
        View manView = root.findViewById(R.id.manView);
        View womanView = root.findViewById(R.id.womanView);
        View bottomNullView = root.findViewById(R.id.bottomNullView);

        noLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexCode = null;
                sexText.setText("不限");
                updateOptionsViews(2);
                mPopWindow.dismiss();
                mCurrentPage = 1;
                isLoading=true;
                requestAll();

            }
        });
        manView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexCode = Constants.SEX_MAN;
                sexText.setText("男");
                updateOptionsViews(2);
                mPopWindow.dismiss();
                mCurrentPage = 1;
                isLoading=true;
                requestAll();
            }
        });
        womanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexCode = Constants.SEX_WOMAN;
                sexText.setText("女");
                updateOptionsViews(2);
                mPopWindow.dismiss();
                mCurrentPage = 1;
                isLoading=true;
                requestAll();
            }
        });
        bottomNullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOptionsViews(2);
                mPopWindow.dismiss();
            }
        });

        if (mPopWindow.isShowing()) {
            updateOptionsViews(2);
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(findViewById(R.id.optionsView));
        }
    }

    private void requestAll() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", mCurrentPage).put("pageSize", pageSize));
            if (mCurrentPage > 1) {
                obj.put("reqTime", reqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            JSONObject cond = new JSONObject();
            if (sexCode != null) {
                cond.put("gender", sexCode);
            }
            if (cityCode != null) {
                cond.put("city", cityCode);
            }
            obj.put("cond", cond);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_ALL_MODEL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ALL_MODEL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);


        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ALL_MODEL:
                gridView.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    responseModelsList(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    public void responseModelsList(ResponseBean data) {
        if (!TextUtils.isEmpty(data.getRecord()) && !"null".equals(data.getRecord())) {
            List<AllModelItemBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                reqTime = object.getString("reqTime");
                String users = object.getString("users");
                if (!TextUtils.isEmpty(users) && !"[null]".equals(users) && !"null".equals(users) && !"[]".equals(users)) {
                    JSONArray jsonArray = new JSONArray(users);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        AllModelItemBean item = new AllModelItemBean();
                        item.setUsernick(object1.getString("usernick"));
                        item.setUserIcon(object1.getString("userIcon"));
                        item.setUserId(object1.getString("userId"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                isLoading = false;
            }
            gvAdapter.setData(datas, mCurrentPage);
        } else {
            isLoading = false;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mCurrentPage = 1;
        isLoading=true;
        requestAll();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isLoading) {
            mCurrentPage = mCurrentPage + 1;
            requestAll();
        } else {
            falseDatasModel();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    gridView.onRefreshComplete();
                    Toast.makeText(ModelAllActivity.this, "已全部加载", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    private void falseDatasModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AllModelItemBean modelBean= gvAdapter.getItem(i);
        Intent intent = new Intent(this, PersonalDetailActivity.class);
        intent.putExtra("userNick", modelBean.getUsernick());
        intent.putExtra("userId", modelBean.getUserId());
        intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
        startActivity(intent);
    }

    private PopupWindow initPopWindow(Context context, int popWinLayout,
                                      boolean isDismissMenuOutsideTouch) {

        PopupWindow mPopWin = new PopupWindow(
                ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(popWinLayout, null),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (isDismissMenuOutsideTouch)
            mPopWin.setBackgroundDrawable(new BitmapDrawable());
        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
        if (mPopWin.isShowing()) {
            updateOptionsViews(2);
            mPopWin.dismiss();
            mPopWin = null;
        }
        return mPopWin;
    }

    private void updateOptionsViews(Integer index) {

        switch (index) {
            case 0:
                sexImage.setImageResource(R.drawable.down_green);
                areaImage.setImageResource(R.drawable.down_gray);
                sexText.setTextColor(getResources().getColor(R.color.dark_green));
                areaText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            case 1:
                sexImage.setImageResource(R.drawable.down_gray);
                areaImage.setImageResource(R.drawable.down_green);
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                areaText.setTextColor(getResources().getColor(R.color.dark_green));
                break;
            case 2:
                sexImage.setImageResource(R.drawable.down_gray);
                areaImage.setImageResource(R.drawable.down_gray);
                sexText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                areaText.setTextColor(getResources().getColor(R.color.text_gray_a7a7a7));
                break;
            default:
                break;
        }
    }
}

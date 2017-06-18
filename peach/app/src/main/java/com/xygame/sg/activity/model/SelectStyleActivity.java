package com.xygame.sg.activity.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flyco.roundview.RoundTextView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.OtherAdapter;
import com.xygame.sg.activity.personal.adapter.StyleAdapter;
import com.xygame.sg.activity.personal.bean.StyleBean;
import com.xygame.sg.bean.comm.ModelStyleBean;
import com.xygame.sg.bean.comm.TransStyleBean;
import com.xygame.sg.define.gridview.DragGrid;
import com.xygame.sg.define.gridview.OtherGridView;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class SelectStyleActivity extends SGBaseActivity implements
        OnClickListener, OnItemClickListener {

    private View backButton, rightButton;
    private TextView titleName, rightButtonText;
    /**
     * 用户栏目的GRIDVIEW
     */
    private DragGrid userGridView;
    /**
     * 其它栏目的GRIDVIEW
     */
    private OtherGridView otherGridView;
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    private StyleAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    private OtherAdapter otherAdapter;

    private List<StyleBean> otherChannelList, userChannelList, keepDatas, selectDatas = new ArrayList<StyleBean>();

    /**
     * 是否在移动，由于这边是动更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;
    private String typeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_editor_style_layout);
        initViews();
        initListeners();
        initDatas();
        ((TextView)findViewById(R.id.text)).setText("来几个风格吧");
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
    }

    private void initDatas() {
        titleName.setText("选择风格");
        rightButton.setVisibility(View.VISIBLE);
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getResources().getString(R.string.sg_editor_bodyinfo_comfirm));
        rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
        userChannelList = new ArrayList<StyleBean>();
        keepDatas = new ArrayList<StyleBean>();
        typeFlag = getIntent().getStringExtra("typeFlag");
        TransStyleBean tsBean = (TransStyleBean) getIntent().getSerializableExtra("bean");
        if (tsBean != null) {
            List tempDatas = tsBean.getStyleList();
            if (tempDatas != null) {
                if (selectDatas == null) {
                    selectDatas = new ArrayList<StyleBean>();
                }
                selectDatas.addAll(tempDatas);
                refreshOthers(tempDatas);
            }
        }
//        loadModelStyle();
        initAllStyle();
        updateBinnerView();
    }

    private void initAllStyle() {
        List<StyleBean> datas=new ArrayList<StyleBean>();
        List<ModelStyleBean> modelStyleBeans = new ArrayList<>();
        if("male".equals(typeFlag)){
            modelStyleBeans = SGApplication.getMaleStyleList();
        }else{
            modelStyleBeans = SGApplication.getFemaleStyleList();
        }

        for(ModelStyleBean it:modelStyleBeans){
            StyleBean bean=new StyleBean();
            bean.setColorB(it.getHueB());
            bean.setColorG(it.getHueG());
            bean.setColorR(it.getHueR());
            bean.setStyleId(it.getTypeId()+"");
            bean.setStyleName(it.getTypeName());
            datas.add(bean);
        }
        refreshActivity(datas);
    }

    public String getTypeFlag() {
        return typeFlag;
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @param tempDatas
     * @see [类、类#方法、类#成员]
     */
    private void initBinnar(List<Map> tempDatas) {
        if (typeFlag != null) {
            if ("male".equals(typeFlag)) {
                for (int i = 0; i < tempDatas.size(); i++) {
                    Map it = tempDatas.get(i);
                    if ("1".equals(it.get("exclusType").toString())) {
                        StyleBean bean = new StyleBean();
                        bean.setColorB(Integer.parseInt((String) it.get("hueB")));
                        bean.setColorG(Integer.parseInt((String) it.get("hueG")));
                        bean.setColorR(Integer.parseInt((String) it.get("hueR")));
                        bean.setStyleId((String) it.get("styleId"));
                        bean.setStyleName((String) it.get("styleName"));
                        bean.setSelected(1);
                        bean.setOrderId(i + 1);
                        userChannelList.add(bean);
                    } else {
                        StyleBean bean = new StyleBean();
                        bean.setColorB(Integer.parseInt((String) it.get("hueB")));
                        bean.setColorG(Integer.parseInt((String) it.get("hueG")));
                        bean.setColorR(Integer.parseInt((String) it.get("hueR")));
                        bean.setStyleId((String) it.get("styleId"));
                        bean.setStyleName((String) it.get("styleName"));
                        bean.setSelected(1);
                        bean.setOrderId(i + 1);
                        keepDatas.add(bean);
                    }
                }
            } else {
                for (int i = 0; i < tempDatas.size(); i++) {
                    Map it = tempDatas.get(i);
                    if ("0".equals(it.get("exclusType").toString())) {
                        StyleBean bean = new StyleBean();
                        bean.setColorB(Integer.parseInt((String) it.get("hueB")));
                        bean.setColorG(Integer.parseInt((String) it.get("hueG")));
                        bean.setColorR(Integer.parseInt((String) it.get("hueR")));
                        bean.setStyleId((String) it.get("styleId"));
                        bean.setStyleName((String) it.get("styleName"));
                        bean.setSelected(1);
                        bean.setOrderId(i + 1);
                        userChannelList.add(bean);
                    } else {
                        StyleBean bean = new StyleBean();
                        bean.setColorB(Integer.parseInt((String) it.get("hueB")));
                        bean.setColorG(Integer.parseInt((String) it.get("hueG")));
                        bean.setColorR(Integer.parseInt((String) it.get("hueR")));
                        bean.setStyleId((String) it.get("styleId"));
                        bean.setStyleName((String) it.get("styleName"));
                        bean.setSelected(1);
                        bean.setOrderId(i + 1);
                        keepDatas.add(bean);
                    }
                }
            }
        } else {
            for (int i = 0; i < tempDatas.size(); i++) {
                Map it = tempDatas.get(i);
                StyleBean bean = new StyleBean();
                bean.setColorB(Integer.parseInt((String) it.get("hueB")));
                bean.setColorG(Integer.parseInt((String) it.get("hueG")));
                bean.setColorR(Integer.parseInt((String) it.get("hueR")));
                bean.setStyleId((String) it.get("styleId"));
                bean.setStyleName((String) it.get("styleName"));
                bean.setSelected(1);
                bean.setOrderId(i + 1);
                userChannelList.add(bean);
            }
        }
        updateBinnerView();
    }

    private void updateBinnerView() {
        userAdapter = new StyleAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
    }

    public void setOtherChannelList(List<StyleBean> otherChannelList) {
        this.otherChannelList = otherChannelList;
        refreshOthers(selectDatas);
    }

    public void refreshOthers(List<StyleBean> src) {
        userChannelList.addAll(src);
        updateBinnerView();
        if(otherChannelList==null){
            return;
        }
        for (int j = 0; j < otherChannelList.size(); j++) {
            for (int i = 0; i < src.size(); i++) {
                if (otherChannelList.get(j).getStyleId().equals(src.get(i).getStyleId())) {
                    otherChannelList.remove(i);
                }
            }
        }
        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
    }
    public void refreshActivity(List<StyleBean> datas) {
        otherChannelList = new ArrayList<StyleBean>();
        int k = 0;
        for (int j = 0; j < userChannelList.size(); j++) {
            for (int i = 0; i < datas.size(); i++) {
                if (userChannelList.get(j).getStyleId().equals(datas.get(i).getStyleId())) {
                    datas.remove(i);
                }
            }
        }

        for (StyleBean mIt : datas) {
            k = k + 1;
            mIt.setOrderId(k);
            otherChannelList.add(mIt);
        }

        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
    }

    public void finishActivity() {
        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
        sendBroadcast(intent);
        finish();
    }

    private void transferLocationService() {
//        if (typeFlag.equals("male")) {
//            VisitUnit.get(FilterFragment.class).getDataUnit().getRepo().put("stylemale", getSelectDatas());
//        } else {
//            VisitUnit.get(FilterFragment.class).getDataUnit().getRepo().put("stylefemale", getSelectDatas());
//        }

        Intent intent = new Intent();
        TransStyleBean bean = new TransStyleBean();
        bean.setSelectDatas(getSelectDatas());
        intent.putExtra(Constants.COMEBACK, bean);
        setResult(Activity.RESULT_OK, intent);

    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
//    private void loadModelStyle() {
//        VisitUnit visit = new VisitUnit();
//        new Action("#" + LoadUserStyle.class.getName() + "(${queryModelStyleType})", this, null, visit).run();
//    }

    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        // 如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                // position为 0，1 的不可以进行任何操作
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    RoundTextView newTextView = (RoundTextView) view.findViewById(R.id.styleText);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final StyleBean channel = ((StyleAdapter) parent.getAdapter()).getItem(position);// 获取点击的频道内容
                    otherAdapter.setVisible(false);
                    // 添加到最后一个
                    otherAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                otherGridView.getChildAt(otherGridView.getLastVisiblePosition())
                                        .getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                userAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            case R.id.otherGridView:

                final ImageView moveImageView1 = getView(view);
                if (moveImageView1 != null) {
                    RoundTextView newTextView = (RoundTextView) view.findViewById(R.id.styleText);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final StyleBean channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    // 添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition())
                                        .getLocationInWindow(endLocation);
                                MoveAnim(moveImageView1, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }

                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final StyleBean moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        // 创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout
                .setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;

            case R.id.rightButton:

                transferLocationService();
                finish();
                break;

        }

    }

    public List<StyleBean> getSelectDatas() {
        selectDatas = userAdapter.getSelectedDatas();
        selectDatas.addAll(keepDatas);
        return selectDatas;
    }
}

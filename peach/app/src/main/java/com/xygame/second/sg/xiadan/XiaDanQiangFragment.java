package com.xygame.second.sg.xiadan;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xygame.second.sg.comm.inteface.TransActMain;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.activity.QiangXiaDanDetailActivity;
import com.xygame.second.sg.xiadan.activity.WaitGodActivity;
import com.xygame.second.sg.xiadan.adapter.GodQiangTypeAdapter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/22.
 */
public class XiaDanQiangFragment extends SGBaseFragment implements View.OnClickListener, OnGetGeoCoderResultListener {
    private GridView photoList;
    private GodQiangTypeAdapter typeAdapter;
    private TextView orderDownButton;
    private View myLocation;
    private TransActMain transActMainListener;

    // 定位相关
    LocationClient mLocClient;
    BitmapDescriptor mCurrentMarker;
    private String address;
    private double latitude=0,Longitude=0;
    MapView mMapView;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true;// 是否首次定位
    BitmapDescriptor bitmapIcon = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);

    public void setTransToPersonalListener(TransActMain transActMainListener){
        this.transActMainListener=transActMainListener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        addListener();
        initDatas();
        initMapDatas();
    }

    private void initMapDatas() {
        mMapView = (MapView) getView().findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.showScaleControl(false);//默认是true，显示缩放按钮
        mMapView.showZoomControls(false);//默认是true，显示比例尺
        // 隐藏百度的LOGO
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //设置缩放级别，默认级别为12
        MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(17);;
        mBaiduMap.setMapStatus(mapstatusUpdate);
    }

    public void startLocation(){
        openGPSSettings();
//        PackageManager pm = getActivity().getPackageManager();
//        boolean flag = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", getActivity().getPackageName()));
//        if (flag) {//有这个权限，做相应处理
//            openGPSSettings();
//        }else {//没有权限
//            twoButton1("您的地理位置访问权限已禁用，马上去开启？");
//        }
    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            twoButton1("您的GPS定位已关闭，马上开启？");
        }else{
            if (mLocClient==null){
                mLocClient = new LocationClient(getActivity());
                mLocClient.registerLocationListener(new MyLocationListenner());
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);// 打开gps
                option.setIsNeedAddress(true);//反编译获得具体位置，只有网络定位才可以
                option.setCoorType("bd09ll"); // 设置坐标类型
                option.setScanSpan(1000);//设置发起定位请求的间隔时间为1000ms
                mLocClient.setLocOption(option);
                mLocClient.start();
            }
        }
    }

    private void twoButton1(String tip){
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(),tip, R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
                        Intent mIntent = new Intent();
                        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
                        mIntent.setComponent(comp);
                        mIntent.setAction("android.intent.action.VIEW");
                        startActivityForResult(mIntent, 0); //此为设置完成后返回到获取界面
					}

					@Override
					public void cancelListener() {
                        transActMainListener.toPersonPage();
					}
				});
				dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xiadan_qiang_layout, null);
    }

    private void initViews() {
        photoList = (GridView) getView().findViewById(R.id.photoList);
        orderDownButton=(TextView)getView().findViewById(R.id.orderDownButton);
        myLocation=getView().findViewById(R.id.myLocation);
    }

    private void initDatas() {
        typeAdapter = new GodQiangTypeAdapter(getActivity(), null);
        photoList.setAdapter(typeAdapter);
        photoList.getBackground().setAlpha(200);
    }

    public void loadActType(){
        List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        if (jinPaiBigTypeBeans != null) {
            if (typeAdapter.getCount()==0){
                typeAdapter.updateJinPaiDatas(jinPaiBigTypeBeans);
            }
        } else {
            requestActType();
        }
    }

    private void addListener() {
        orderDownButton.setOnClickListener(this);
        myLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orderDownButton:
                JinPaiBigTypeBean jinPaiBigTypeBean=typeAdapter.getSelectedItem();
                if (jinPaiBigTypeBean!=null){
                    boolean islogin = UserPreferencesUtil.isOnline(getActivity());
                    if (!islogin) {
                        Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
                        intent.putExtra("whereFlag","mainMe");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), QiangXiaDanDetailActivity.class);
                        intent.putExtra("jinPaiBigTypeBean",jinPaiBigTypeBean);
                        startActivityForResult(intent, 0);
                    }
                }else {
                    Toast.makeText(getActivity(),"请先刷新选择品类",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.myLocation:
                if (Longitude!=0&&latitude!=0){
                    LatLng llA = new LatLng(latitude,
                            Longitude);
                    OverlayOptions ooA = new MarkerOptions().position(llA)
                            .icon(bitmapIcon).zIndex(9).draggable(true);
                    mBaiduMap.addOverlay(ooA);
                    LatLngBounds bounds = new LatLngBounds.Builder().include(llA)
                            .build();
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds
                            .getCenter());
                    mBaiduMap.animateMapStatus(u);
                }else{
                    startLocation();
                }
                break;
        }
    }

    public void requestActType() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
            ShowMsgDialog.showNoMsg(getActivity(), true);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_SERVER_TYPE1:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                }else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                List<JinPaiBigTypeBean> fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiBigTypeBean item = new JinPaiBigTypeBean();
                    item.setIsSelect(false);
                    item.setName(StringUtils.getJsonValue(object, "typeName"));
                    item.setId(StringUtils.getJsonValue(object, "typeId"));
                    item.setSubStr(StringUtils.getJsonValue(object, "titles"));
                    item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
                    item.setCategoryName(StringUtils.getJsonValue(object, "categoryName"));
//                    if ("900".equals(item.getId())){
//                        JinPaiBigTypeBean subItem = new JinPaiBigTypeBean();
//                        subItem.setCategoryName(item.getCategoryName());
//                        subItem.setId(Constants.DEFINE_LOL_ID);
//                        subItem.setSubStr(item.getSubStr());
//                        subItem.setUrl(item.getUrl());
//                        subItem.setName("LOL");
//                        fuFeiDatas.add(subItem);
//                    }
                    fuFeiDatas.add(item);
                }
                CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, fuFeiDatas);
                if (typeAdapter.getCount()==0){
                    typeAdapter.updateJinPaiDatas(fuFeiDatas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            if(latitude==0){
                latitude=location.getLatitude();
            }
            if(Longitude==0){
                Longitude=location.getLongitude();
            }
            if (isFirstLoc) {
                address = location.getAddrStr();
                LatLng llA = new LatLng(location.getLatitude(),
                        location.getLongitude());
                OverlayOptions ooA = new MarkerOptions().position(llA)
                        .icon(bitmapIcon).zIndex(9).draggable(true);
                mBaiduMap.addOverlay(ooA);
                LatLngBounds bounds = new LatLngBounds.Builder().include(llA)
                        .build();
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds
                        .getCenter());
                mBaiduMap.animateMapStatus(u);
                if (address != null) {
                    if (mLocClient != null && mLocClient.isStarted()) {
                        mLocClient.stop();
                        mLocClient=null;
                    }
                }
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        double selectLat = result.getLocation().latitude;
        double selectLon = result.getLocation().longitude;
        address = result.getAddress();
        mBaiduMap.clear();
        LatLng llA = new LatLng(selectLat, selectLon);
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(bitmapIcon)
                .zIndex(9).draggable(true);
        mBaiduMap.addOverlay(ooA);
        LatLngBounds bounds = new LatLngBounds.Builder().include(llA).build();
        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                String result = data.getStringExtra("toFirstPage");
                if ("toFirstPage".equals(result)) {
                    transActMainListener.toPersonPage();
                }
                break;
            }
            default:
                break;
        }
    }
}
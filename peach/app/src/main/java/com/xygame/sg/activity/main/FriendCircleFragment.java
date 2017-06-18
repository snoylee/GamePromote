package com.xygame.sg.activity.main;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.second.sg.personal.bean.PayMoneyBean;
import com.xygame.second.sg.personal.bean.PopPresentBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.circle.CirclePlushActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.PresentDialog;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.adapter.main.CircleAdapter;
import com.xygame.sg.bean.circle.CircleBean;
import com.xygame.sg.bean.circle.CircleDatas;
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.CircleOptionView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.xygame.sg.R.id.iconOral;


public class FriendCircleFragment extends SGBaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2, CircleAdapter.ShowPresant, PresentDialog.PresentActionInterface ,ViewPager.OnPageChangeListener {
    private View plushView;
    private PullToRefreshListView2 cicleList;
    private CircleAdapter adapter;
    private PhotoesTotalBean newItem;
    private String photoName;
    //视频地址
    private String path;
    //视频第一帧地址
    private String imagePath;
    private Long videoSize;
    private Long videoTime;
    private int pageSize = 15;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private String reqTime;
    private boolean isLoading = true;

    private PresentDialog presentDialog;
    private ViewPager popPager;
    private List<MyAdapter> mGridViewAdapters = new ArrayList<MyAdapter>();
    private List<View> mAllViews = new ArrayList<View>();
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout popDotView;
    private int pageCount = 1;
    private TextView stoneValues;
    private long stoneValuesNums=0;
    private CircleBean circleBean;
    private PopPresentBean tempPresentBean;

    public FriendCircleFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_RECORDER_CIRCLE);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
        myIntentFilter.addAction(Constants.ACTION_RECORDER_CIRCLE_COMMENT);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
        initViews();
        initDatas();
        addListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_circle_layout, null);
    }

    private void initViews() {
        plushView = getView().findViewById(R.id.plushView);
        cicleList = (PullToRefreshListView2) getView().findViewById(R.id.cicleList);
        cicleList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initDatas() {
        updatePlushView();
        adapter = new CircleAdapter(getActivity(), this, null);
        cicleList.setAdapter(adapter);
    }

    private void updatePlushView() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            String typeStr = UserPreferencesUtil.getUserType(getActivity());
            if (Constants.CARRE_MODEL.equals(typeStr) || Constants.PRO_MODEL.equals(typeStr)) {
                plushView.setVisibility(View.VISIBLE);
            } else {
                plushView.setVisibility(View.INVISIBLE);
            }
        } else {
            if (presentDialog!=null){
                presentDialog.closeDiloag();
            }
            plushView.setVisibility(View.VISIBLE);
        }
    }

    private void addListener() {
        plushView.setOnClickListener(this);
        cicleList.setOnRefreshListener(this);
        adapter.addPresentDialogListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plushView:
                boolean islogin = UserPreferencesUtil.isOnline(getActivity());
                if (!islogin) {
                    Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CircleOptionView.class);
                    startActivityForResult(intent, 0);
                }
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isLoading = true;
        mCurrentPage = 1;
        loadCircleData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isLoading) {
            mCurrentPage = mCurrentPage + 1;
            loadCircleData();
        } else {
            falseDatas();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    cicleList.onRefreshComplete();
                    Toast.makeText(getActivity(), "已全部加载", Toast.LENGTH_SHORT).show();
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
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode != Activity.RESULT_OK || null == data) {
                    return;
                }
                int index = data.getIntExtra(Constants.COMEBACK, 0);
                switch (index) {
                    case 1:
                        chicePhoto();
                        break;
                    case 2:
                        takePhoto();
                        break;
                    case 3:
                        toRecorderVideo();
                        break;
                }
                break;
            case 1: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Object result = data.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    String dateTimer = CalendarUtils.getCurrDateStr();
                    newItem = new PhotoesTotalBean();
                    newItem.setDateTimer(dateTimer);
                    List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
                        if (subBitmap != null) {
                            String photoName = Constants.getImageName(getActivity());
                            FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
                            subBitmap.recycle();
                            String photoPath = FileUtil.getPhotopath(photoName);
                            PhotoesSubBean subBean = new PhotoesSubBean();
                            subBean.setDateTimer(dateTimer);
                            subBean.setImageUrls(photoPath);
                            subBean.setIsSelect(false);
                            imageObjects.add(subBean);
                        }
                    }
                    newItem.setImageObjects(imageObjects);
                    newItem.setIsSelect(false);
                    Intent intent = new Intent(getActivity(), CirclePlushActivity.class);
                    intent.putExtra("flag", "tp");
                    intent.putExtra("bean", newItem);
                    startActivityForResult(intent, 3);
                }
                break;
            }
            case 2:
                try {
                    Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        String dateTimer = CalendarUtils.getCurrDateStr();
                        newItem = new PhotoesTotalBean();
                        newItem.setDateTimer(dateTimer);
                        List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
                        PhotoesSubBean subBean = new PhotoesSubBean();
                        subBean.setDateTimer(dateTimer);
                        subBean.setImageUrls(photoPath);
                        subBean.setIsSelect(false);
                        imageObjects.add(subBean);
                        newItem.setImageObjects(imageObjects);
                        newItem.setIsSelect(false);
                        Intent intent = new Intent(getActivity(), CirclePlushActivity.class);
                        intent.putExtra("bean", newItem);
                        intent.putExtra("flag", "tp");
                        startActivityForResult(intent, 3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                if (resultCode != Activity.RESULT_OK || null == data) {
                    return;
                }
                isLoading = true;
                mCurrentPage = 1;
                loadCircleData();
                break;
            case 4:
                if (resultCode != Activity.RESULT_OK|| null == data) {
                    return;
                }
                boolean flag=data.getBooleanExtra(Constants.COMEBACK,false);
                PayMoneyBean itemBean=(PayMoneyBean)data.getSerializableExtra("bean");
                if (flag){
                    stoneValuesNums=stoneValuesNums+Long.parseLong(itemBean.getVirMoney());
                    stoneValues.setText(String.valueOf(stoneValuesNums));
                }
                break;
            default:
                break;
        }
    }

    private void chicePhoto() {
        LinkedList<String> picDatas = new LinkedList<String>();
        TransferImagesBean dataBean = new TransferImagesBean();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(getActivity(), ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, 9);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 1);
    }

    private void takePhoto() {
        // 跳转至拍照界面
        photoName = Constants.getImageName(getActivity());
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        Uri uri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentPhote, 2);
    }

    private void toRecorderVideo() {
//        Intent intent = new Intent(getActivity(), FFmpegRecorderActivity.class);
//        intent.putExtra("actionFlag", "plushCircle");
//        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        SGApplication.getInstance().unregisterImagesReceiver();
        getActivity().unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_RECORDER_CIRCLE.equals(intent.getAction())) {
                //视频地址
                path = intent.getStringExtra("path");
                //视频第一帧地址
                imagePath = intent.getStringExtra("imagePath");
                videoSize = intent.getLongExtra("videoSize", 0);
                videoTime = intent.getLongExtra("videoTime", 0);

                Intent intent1 = new Intent(getActivity(), CirclePlushActivity.class);
                intent1.putExtra("path", path);
                intent1.putExtra("imagePath", imagePath);
                intent1.putExtra("videoSize", videoSize);
                intent1.putExtra("videoTime", videoTime);
                intent1.putExtra("flag", "video");
                startActivityForResult(intent1, 3);
            } else if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                updatePlushView();
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                updatePlushView();
            } else if (Constants.ACTION_RECORDER_CIRCLE_COMMENT.equals(intent.getAction())) {
                CircleBean item = (CircleBean) intent.getSerializableExtra(Constants.COMEBACK);
                boolean commentFlag = intent.getBooleanExtra("commentFlag", false);
                boolean priseFlag = intent.getBooleanExtra("priseFlag", false);
                if (commentFlag) {
                    adapter.updateComment(item);
                }
                if (priseFlag) {
                    adapter.updatePrise(item);
                }
            }
        }
    };

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        cicleList.onRefreshComplete();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_CIRCLE_DATAS:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_CIRCLE_PRAISE:
                if ("0000".equals(data.getCode())) {
                    adapter.updatePriser();
                }
                break;
            case ConstTaskTag.QUERY_GIFTS:
                if ("0000".equals(data.getCode())) {
                    parseGiftsDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SEND_GIFTS:
                if ("0000".equals(data.getCode())) {
                    sendResultDialog(true, "礼物赠送成功");
                    int tempCount=circleBean.getGiftCount();
                    circleBean.setGiftCount(tempCount+1);
                    //++++++++++++++++++临时加++++++++++++++++++++++
//                    List<CircleCommenters> commenters=circleBean.getCommenters();
//                    if (commenters==null){
//                        commenters=new ArrayList<>();
//                    }
//                    CircleCommenters it=new CircleCommenters();
//                    CircleCommenter ren=new CircleCommenter();
//                    ren.setUsernick(UserPreferencesUtil.getUserNickName(getActivity()));
//                    it.setCommenter(ren);
//                    it.setCommentTime(System.currentTimeMillis());
//                    it.setContent("打赏了 ".concat(tempPresentBean.getName()));
//                    commenters.add(0, it);
//                    circleBean.setCommenters(commenters);
//                    int priserCount=circleBean.getCommentCount();
//                    priserCount=priserCount+1;
//                    circleBean.setCommentCount(priserCount);
                    //++++++++++++++++++临时加++++++++++++++++++++++
                    adapter.updatePresentCount(circleBean);
                } else {
                    sendResultDialog(true,"礼物赠送失败");
                }
                break;
        }
    }

    private void sendResultDialog(final boolean flag,String flagStr){
        OneButtonDialog dialog = new OneButtonDialog(getActivity(),flagStr, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						if (flag){
                            if (presentDialog!=null){
                                presentDialog.closeDiloag();
                            }
                        }
					}
				});
				dialog.show();
    }

    @Override
    protected void responseFaith() {
        super.responseFaith();
        cicleList.onRefreshComplete();
    }

    public int getCountNum() {
        return adapter.getCount();
    }

    public void loadGiftsData() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            ShowMsgDialog.showNoMsg(getActivity(), false);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_GIFTS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GIFTS);
    }

    public void loadCircleData() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", mCurrentPage).put("pageSize", pageSize));
            if (mCurrentPage > 1) {
                obj.put("reqTime", reqTime);
            } else {
                ShowMsgDialog.showNoMsg(getActivity(), true);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_DATAS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CIRCLE_DATAS);
    }

    private void parseDatas(ResponseBean data) {
//        CircleDatas datas = JSON.parseObject(data.getRecord(), CircleDatas.class);
//        if (datas != null) {
//            if (datas.getMoods() != null) {
//                reqTime = String.valueOf(datas.getReqTime());
//                if (datas.getMoods().size() < pageSize) {
//                    isLoading = false;
//                }
//                adapter.addDatas(datas.getMoods(), mCurrentPage);
//            } else {
//                isLoading = false;
//            }
//        } else {
//            isLoading = false;
//        }
    }

    private void parseGiftsDatas(ResponseBean data) {
        List<PopPresentBean> datas = new ArrayList<>();
        try {
            if (!TextUtils.isEmpty(data.getRecord())){
                JSONObject maxJson=new JSONObject(data.getRecord());
                stoneValuesNums=Long.parseLong(maxJson.getString("diamond"));
                String gifts=maxJson.getString("gifts");
                if (!TextUtils.isEmpty(gifts)&&!"[]".equals(gifts)&&!"null".equals(gifts)&&!"[null]".equals(gifts)){
                    JSONArray array=new JSONArray(gifts);
                    for (int i=0;i<array.length();i++){
                        JSONObject obj=array.getJSONObject(i);
                        PopPresentBean item = new PopPresentBean();
                        item.setId(obj.getInt("id"));
                        item.setHasSlect(false);
                        item.setName(obj.getString("name"));
                        item.setShowUrl(obj.getString("showUrl"));
                        item.setWorthAmount(obj.getLong("worthAmount"));
                        datas.add(item);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

       if (datas.size()>0){
           presentDialog = new PresentDialog(getActivity(), R.style.CustomDialog);
           presentDialog.setCancelable(false);
           presentDialog.addPresentActionListener(this);
           presentDialog.show();
           popPager = presentDialog.getPopPager();
           popDotView=presentDialog.getPopDotView();
           stoneValues=presentDialog.getStoneValues();
           stoneValues.setText(String.valueOf(stoneValuesNums));
           initPresentDatas(datas);
       }
    }

    @Override
    public void ShowPresantDialog(CircleBean circleBean) {
        boolean islogin = UserPreferencesUtil.isOnline(getActivity());
        if (!islogin) {
            Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
            startActivity(intent);
        }else{
            this.circleBean=circleBean;
            loadGiftsData();
        }
    }


    public void payAction() {
        Intent intent = new Intent(getActivity(), PayStoneActivity.class);
        intent.putExtra("stoneValue",  String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(stoneValuesNums))));
        startActivityForResult(intent, 4);
    }

    @Override
    public void jianAction() {

    }

    @Override
    public void sendPresentAction() {
        if (stoneValuesNums>=tempPresentBean.getWorthAmount()){
            sendPresent();
        }else{
            warmTip();
        }
    }

    @Override
    public void addAction() {

    }

    public void sendPresent() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("fromUserNick", UserPreferencesUtil.getUserNickName(getActivity()));
            obj.put("toUserNick", circleBean.getPublisher().getUsernick());
            obj.put("moodId", circleBean.getMoodId());
            obj.put("giftId", tempPresentBean.getId());
            ShowMsgDialog.showNoMsg(getActivity(), false);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_SEND_GIFTS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SEND_GIFTS);
    }

    private void warmTip(){
        OneButtonDialog dialog = new OneButtonDialog(getActivity(), "您的钻石数量不够，请充值！", R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
                        Intent intent = new Intent(getActivity(), PayStoneActivity.class);
                        intent.putExtra("stoneValue",  String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(stoneValuesNums))));
                        startActivityForResult(intent, 4);
					}
				});
				dialog.show();
    }

    private void initPresentDatas(List<PopPresentBean> datas) {
        int k = 0, h = 8,l=0;
        if (datas.size() >= 8) {
            int yuShu = datas.size() % 8;
            pageCount = yuShu == 0 ? datas.size() / 8 : (datas.size() / 8) + 1;
        }

        mGridViewAdapters.clear();
        mAllViews.clear();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (datas.size()<h){
            h=datas.size();
        }
        for (int i = 0; i < pageCount; i++) {
            List<PopPresentBean> tempDatas = new ArrayList<>();
            for (int j = k; j < h; j++) {
                tempDatas.add(datas.get(j));
                l = j;
            }
            h = h + 8;
            if (h>datas.size()){
                h=datas.size();
            }
            k =l + 1;
            View mView = inflater.inflate(R.layout.langsi_popup_gridview, null);
            GridView mGridView = (GridView) mView
                    .findViewById(R.id.langsi_popup_gridview);
            MyAdapter adapter = new MyAdapter(getActivity(), tempDatas);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new SelectPersentListener(i));
            adapter.notifyDataSetChanged();
            mGridViewAdapters.add(adapter);
            mAllViews.add(mView);
        }
        updatePopWindownDot(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        popPager.setAdapter(myViewPagerAdapter);
        myViewPagerAdapter.notifyDataSetChanged();
        popPager.setOnPageChangeListener(this);
    }

    private void updatePopWindownDot(int posion){
        popDotView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < pageCount; i++) {
            View mView = inflater.inflate(R.layout.pop_dot_item, null);
            View dotView=mView.findViewById(R.id.dotView);
            if (i==posion){
                dotView.setBackgroundResource(R.drawable.shape_circle_green);
            }else{
                dotView.setBackgroundResource(R.drawable.shape_circle_white);
            }
            popDotView.addView(mView);
        }
    }

    private class SelectPersentListener implements AdapterView.OnItemClickListener {

        private int index;

        public SelectPersentListener(int index){
            this.index=index;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PopPresentBean item=mGridViewAdapters.get(index).getItem(position);
            upGetPresent(index,position,item);
        }
    }

    private void upGetPresent(int index,int postion,PopPresentBean bean) {
        this.tempPresentBean=bean;
        for (int i=0;i<mGridViewAdapters.size();i++){
            if (i==index){
                mGridViewAdapters.get(i).choiceItem(postion);
            }else{
                mGridViewAdapters.get(i).defaultChoice();
            }
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mAllViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            popPager.removeView((View) object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mAllViews.get(position),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mAllViews.get(position);
        }
    }

    private class MyAdapter extends BaseAdapter {

        private Context mContext;
        private List<PopPresentBean> datas;
        private LayoutInflater mInflater;
        private ImageLoader imageLoader;

        private MyAdapter(Context context, List<PopPresentBean> datas) {
            this.mContext = context;
            this.datas = datas;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        }

        public int getCount() {
            return datas.size();
        }

        public PopPresentBean getItem(int position) {
            return datas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public void choiceItem(int posion){
            for (int i=0;i<datas.size();i++){
                if (i==posion){
                    datas.get(i).setHasSlect(true);
                }else{
                    datas.get(i).setHasSlect(false);
                }
            }
            notifyDataSetChanged();
        }

        public void defaultChoice(){
            for (int i=0;i<datas.size();i++){
                datas.get(i).setHasSlect(false);
            }
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.pop_present_item, null);
                holder = new ViewHolder();
                holder.iconOral = (TextView) convertView.findViewById(iconOral);
                holder.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
                holder.stoneNums = (TextView) convertView.findViewById(R.id.stoneNums);
                holder.iconSelect = (ImageView) convertView.findViewById(R.id.iconSelect);
                convertView.setTag(holder);
            }
            PopPresentBean item = datas.get(position);
            holder.iconOral.setText(item.getName());
            holder.stoneNums.setText(String.valueOf(item.getWorthAmount()));
            if (item.isHasSlect()) {
                holder.iconSelect.setVisibility(View.VISIBLE);
            } else {
                holder.iconSelect.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(item.getShowUrl())) {
                imageLoader.loadOrigaImage(item.getShowUrl(), holder.iconImage, true);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iconImage, iconSelect;
        TextView iconOral, stoneNums;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updatePopWindownDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

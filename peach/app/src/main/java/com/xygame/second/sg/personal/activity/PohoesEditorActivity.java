package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.adapter.UserPhotoesEditorAdapter;
import com.xygame.second.sg.personal.bean.PhotoBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tony on 2016/8/22.
 */
public class PohoesEditorActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

    private TextView titleName;
    private View backButton, rightButton;
    private ImageView rightbuttonIcon;
    private PullToRefreshListView2 fansList;
    private int pageSize = 21;//每页显示的数量
    private int fansPage = 1;//当前显示页数
    private String fansReqTime;
    private boolean fansIsLoading = true;
    private UserPhotoesEditorAdapter adapter;
    private String photoName;
    private PhotoesTotalBean newItem;
    private int currIndex = 0, currCount;
    private String timerDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photoes_layout);
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
        fansList.setOnRefreshListener(this);
        rightButton.setOnClickListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        rightButton = findViewById(R.id.rightButton);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        fansList = (PullToRefreshListView2) findViewById(R.id.fansList);
        fansList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        registerBoradcastReceiver();
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.new_notice_icon);
        titleName.setText("相册");
        adapter = new UserPhotoesEditorAdapter(this, null);
        fansList.setAdapter(adapter);
        loadDatas();
    }

    private void loadDatas() {//index:1进行中 2已完成 3已关闭
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", fansPage).put("pageSize", pageSize));
            obj.put("userId", UserPreferencesUtil.getUserId(this));
            if (fansPage > 1) {
                obj.put("reqTime", fansReqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_GALLERY_PICS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GALLERY_PICS);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            Intent intent = new Intent(this, PickPhotoesView.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新操作
        fansIsLoading = true;
        fansPage = 1;
        loadDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载操作
        if (fansIsLoading) {
            fansPage = fansPage + 1;
            loadDatas();
        } else {
            falseDatas();
        }
    }

    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message m = mHandler.obtainMessage();
                    m.what = 1;
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
            case ConstTaskTag.QUERY_GALLERY_PICS:
                fansList.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_GALLERY_ADD:
                if ("0000".equals(data.getCode())) {
                    parseAddPicsDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseAddPicsDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        try {
            if (ConstTaskTag.isTrueForArrayObj(resposeStr)) {
                JSONArray array = new JSONArray(resposeStr);
                for (int j = 0; j < array.length(); j++) {
                    JSONObject photoObject = array.getJSONObject(j);
                    String uucode = StringUtils.getJsonValue(photoObject, "uucode");
                    String resId = StringUtils.getJsonValue(photoObject, "resId");
                    for (int i = 0; i < newItem.getImageObjects().size(); i++) {
                        String localUucode = newItem.getImageObjects().get(i).getItemIndex();
                        if (uucode.equals(localUucode)) {
                            newItem.getImageObjects().get(i).setItemIndex(null);
                            newItem.getImageObjects().get(i).setImageId(resId);
                        }
                    }
                }
            }
            addImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addImages() {
        List<PhotoBean> datas = new ArrayList<>();
        for (PhotoesSubBean item1 : newItem.getImageObjects()) {
            PhotoBean item = new PhotoBean();
            item.setCreateTime(item1.getDateTimer());
            item.setResUrl(item1.getUrl());
            item.setResId(item1.getImageId());
            datas.add(item);
        }
        ThreadPool.getInstance().excuseThread(new AddNewImageIntoViews(datas));
    }

    private class AddNewImageIntoViews implements Runnable {
        private List<PhotoBean> datas;

        private AddNewImageIntoViews(List<PhotoBean> datas) {
            this.datas = datas;
        }

        @Override
        public void run() {
            adapter.addNewDatas(datas);
            mHandler.sendEmptyMessage(0);
        }
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<PhotoBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                fansReqTime = object.getString("reqTime");
                String actions = object.getString("pics");
                if (ConstTaskTag.isTrueForArrayObj(actions)) {
                    JSONArray array = new JSONArray(actions);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject photoObject = array.getJSONObject(i);
                        PhotoBean item = new PhotoBean();
                        item.setCreateTime(StringUtils.getJsonValue(photoObject, "createTime"));
                        item.setResUrl(StringUtils.getJsonValue(photoObject, "resUrl"));
                        item.setResId(StringUtils.getJsonValue(photoObject, "resId"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                fansIsLoading = false;
            }
            ShowMsgDialog.showNoMsg(this, true);
            ThreadPool.getInstance().excuseThread(new AddImageIntoViews(datas));
        } else {
            fansIsLoading = false;
        }
    }

    private class AddImageIntoViews implements Runnable {
        private List<PhotoBean> datas;

        private AddImageIntoViews(List<PhotoBean> datas) {
            this.datas = datas;
        }

        @Override
        public void run() {
            adapter.addDatas(datas, fansPage);
            mHandler.sendEmptyMessage(0);
        }
    }

    public List<PhotoesSubBean> getNewImages() {
        for (int i = 0; i < newItem.getImageObjects().size(); i++) {
            Bitmap bitmap = ImageTools.getBitmap(newItem.getImageObjects().get(i).getImageUrls());
            newItem.getImageObjects().get(i).setLengthPx(String.valueOf(bitmap.getHeight()));
            newItem.getImageObjects().get(i).setPicSize(FileUtil.getImageByteSize(newItem.getImageObjects().get(i).getImageUrls()));
            newItem.getImageObjects().get(i).setWidthPx(String.valueOf(bitmap.getWidth()));
            bitmap.recycle();
        }
        return newItem.getImageObjects();
    }

    private void transferLocationService() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            List<PhotoesSubBean> datas = getNewImages();
            for (PhotoesSubBean it : datas) {
                JSONObject obj = new JSONObject();
                obj.put("resUrl", it.getUrl());
                obj.put("uucode", it.getItemIndex());
                obj.put("lengthPx", it.getLengthPx());
                obj.put("widthPx", it.getWidthPx());
                obj.put("picSize", it.getPicSize());
                jsonArray.put(obj);
            }
            obj1.put("pics", jsonArray);
            item.setData(obj1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_GALLERY_ADD);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GALLERY_ADD);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ShowMsgDialog.cancel();
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    fansList.onRefreshComplete();
                    break;
                case 2:
                    String imageUrl = (String) msg.obj;
                    newItem.getImageObjects().get(currIndex).setUrl(imageUrl);
                    currIndex = currIndex + 1;
                    doUploadImages();
                    break;
                case 3:
                    if (currIndex > 0) {
                        transferLocationService();
                    } else {
                        ShowMsgDialog.cancel();
                        Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void chicePhoto() {
        LinkedList<String> picDatas = new LinkedList<String>();
        TransferImagesBean dataBean = new TransferImagesBean();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, Constants.MODEL_PIC_NUM);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 0);
    }

    private void takePhoto() {
        // 跳转至拍照界面
        photoName = Constants.getImageName(this);
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        Uri uri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentPhote, 2);
    }

    private void doUploadImages() {
        if (currIndex < newItem.getImageObjects().size()) {

            AliySSOHepler.getInstance().uploadImageEngine(this, Constants.PHOTOBUM_PATH,
                    newItem.getImageObjects().get(currIndex).getImageUrls(), new HttpCallBack() {

                        @Override
                        public void onSuccess(String imageUrl, int requestCode) {
                            Message msg = new Message();
                            msg.obj = imageUrl;
                            msg.what = 2;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void onFailure(int errorCode, String msg, int requestCode) {
                            // TODO Auto-generated method stub
                            Message msg1 = new Message();
                            msg1.what = 3;
                            mHandler.sendMessage(msg1);
                        }

                        @Override
                        public void onProgress(String objectKey, int byteCount, int totalSize) {
                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            transferLocationService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Object result = data.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    currIndex = 0;
                    currCount = adapter.getCount();
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    String dateTimer = CalendarUtils.getCurrDateStr();
                    newItem = new PhotoesTotalBean();
                    newItem.setDateTimer(dateTimer);
                    List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
                        if (subBitmap != null) {
                            String photoName = Constants.getImageName(this);
                            FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
                            subBitmap.recycle();
                            String photoPath = FileUtil.getPhotopath(photoName);
                            PhotoesSubBean subBean = new PhotoesSubBean();
                            subBean.setDateTimer(dateTimer);
                            subBean.setImageUrls(photoPath);
                            subBean.setIsSelect(false);
                            currCount = currCount + 1;
                            subBean.setItemIndex(String.valueOf(currCount));
                            imageObjects.add(subBean);
                        }
                    }
                    newItem.setImageObjects(imageObjects);
                    newItem.setIsSelect(false);
                    ShowMsgDialog.show(this, "图片上传中...", false);
                    doUploadImages();
                }
                break;
            }
            case 1:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("galary".equals(flag)) {
                    chicePhoto();
                } else if ("camera".equals(flag)) {
                    takePhoto();
                }
                break;

            case 2:
                try {
                    Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        currIndex = 0;
                        currCount = adapter.getCount();
                        String dateTimer = CalendarUtils.getCurrDateStr();
                        newItem = new PhotoesTotalBean();
                        newItem.setDateTimer(dateTimer);
                        List<PhotoesSubBean> imageObjects = new ArrayList<PhotoesSubBean>();
                        PhotoesSubBean subBean = new PhotoesSubBean();
                        subBean.setDateTimer(dateTimer);
                        subBean.setImageUrls(photoPath);
                        subBean.setIsSelect(false);
                        subBean.setItemIndex(String.valueOf(currCount + 1));
                        imageObjects.add(subBean);
                        newItem.setImageObjects(imageObjects);
                        newItem.setIsSelect(false);
                        ShowMsgDialog.show(this, "图片上传中...", false);
                        doUploadImages();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent.getAction())) {
                boolean flag = intent.getBooleanExtra(Constants.IS_DELE_IMAGES, false);
                if (!flag) {
                    return;
                }
                Object result = intent.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    timerDesc=intent.getStringExtra("timerDesc");
                    List<String> deleteImages = dataBean.getDeleteImages();
                    ShowMsgDialog.showNoMsg(PohoesEditorActivity.this, true);
                    ThreadPool.getInstance().excuseThread(new DeleteImgeViews(deleteImages));
                }
            }
        }
    };

    private class DeleteImgeViews implements Runnable {
        private List<String> datas;

        private DeleteImgeViews(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public void run() {
            adapter.deleteDatas(datas, timerDesc);
            mHandler.sendEmptyMessage(0);
        }
    }
}

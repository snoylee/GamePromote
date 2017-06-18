package com.xygame.sg.activity.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.personal.bean.IdentyTranBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.certification.AuthPhotographerInfo;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class CMIdentySecondActivity extends SGBaseActivity implements View.OnClickListener , AdapterView.OnItemClickListener {
    private View backButton;
    private TextView titleName;
    private IdentyTranBean identyTranBean;
    VisitUnit visitUnit = new VisitUnit();

    private GridView modelPicGridView;
    private LinkedList<String> picDatas;
    private MyGridViewAdapter adapter;
    private List<PhotoesBean> uploadImages;
    private int currIndex = 0;
    private EditText introduceText;
    private String photoName;
    private ImageLoader imageLoader;
    private Button submit_bt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_cmidenty_second, null));
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_cmidenty_second));

        introduceText = (EditText) findViewById(R.id.introduceText);
        modelPicGridView = (GridView) findViewById(R.id.modelPicGridView);


        submit_bt = (Button) findViewById(R.id.submit_bt);
    }

    private void initDatas() {
        identyTranBean = (IdentyTranBean) getIntent().getSerializableExtra("identyTranBean");
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        picDatas = new LinkedList<String>();
        uploadImages = new ArrayList<PhotoesBean>();
        updateChoiceImages();
        imageListeners();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        modelPicGridView.setOnItemClickListener(this);

        submit_bt.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;

            case R.id.submit_bt:
                toSubmit();
                break;
        }
    }

    private void toSubmit() {

        if (StringUtils.isEmpty(getOral())){
            Toast.makeText(this,"请介绍下您的作品！",Toast.LENGTH_SHORT).show();
            return;
        }

        identyTranBean.setOpusDesc(getOral());
        if (adapter.getCount() - 1 > 0) {
            uploadImages();
        } else {
            Toast.makeText(this, "请上传您作品集！", Toast.LENGTH_SHORT).show();
        }
    }

    public String[] genScope(float b, float e, float step) {
        List<String> l = new ArrayList<String>();
        for (float i = b; i <= e; i += step) {
            l.add("" + new Float(i).intValue());
        }
        String[] a = new String[l.size()];
        l.toArray(a);
        return a;
    }
    @Override
    public void onDestroy() {
        SGApplication.getInstance().unregisterImagesReceiver(imageBroadCastListener);
        super.onDestroy();
    }

    public class MyGridViewAdapter extends BaseAdapter {

        private Context context;
        private List<String> datas;

        /**
         * <默认构造函数>
         */
        public MyGridViewAdapter(Context context, List<String> datas) {
            this.context = context;
            if (datas != null) {
                this.datas = datas;
            } else {
                this.datas = new ArrayList<String>();
            }
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public String getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clearDatas() {
            datas.clear();
        }

        public void addDatas(List<String> datas) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (position == datas.size() - 1) {
                View addView = inflater.inflate(
                        R.layout.identy_pic_item_add, null);
                addView.findViewById(R.id.add).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (datas.size() - 1 == Constants.MODEL_PIC_NUM) {
                                    Toast.makeText(getApplicationContext(), "已达到最多图片数量", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    showChoiceWays();
                                }
                            }
                        });
                return addView;
            } else {
                View picView = inflater.inflate(
                        R.layout.identy_pic_item, null);
                ImageView picIBtn = (ImageView) picView.findViewById(R.id.pic);
                String headUrl = datas.get(position);
                if (headUrl.contains("http://")) {
                    imageLoader.loadImage(headUrl, picIBtn, true);
                } else {
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            headUrl, picIBtn);
                }
                return picView;
            }
        }

        public List<String> getImages() {
            List<String> tempImages = new ArrayList<String>();
            tempImages.addAll(datas);
            tempImages.remove(tempImages.size() - 1);
            return tempImages;
        }
    }

    private void showChoiceWays() {//chicePhoto();
        Intent intent = new Intent(this, PickPhotoesView.class);
        startActivityForResult(intent, 1);
    }

    private void chicePhoto() {
        TransferImagesBean dataBean = new TransferImagesBean();
        picDatas.remove(picDatas.size() - 1);
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, Constants.MODEL_PIC_NUM);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    updateChoiceImages();
                    return;
                }
                Object result = data.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    picDatas = new LinkedList<String>();
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        if (tempStrs.get(i)
                                .contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                            picDatas.add(tempStrs.get(i));
                        } else {
                            Bitmap subBitmap = FileUtil.compressImages(tempStrs
                                    .get(i));
                            if (subBitmap != null) {
                                String photoName = Constants.getImageName(this);
                                FileUtil.saveScalePhoto(
                                        FileUtil.getPhotopath(photoName), subBitmap);
                                subBitmap.recycle();
                                picDatas.add(FileUtil.getPhotopath(photoName));
                            }
                        }
                    }
                    updateChoiceImages();
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
                    Bitmap bitmap = FileUtil.compressImages(FileUtil
                            .getPhotopath(photoName));
                    if (bitmap != null) {
                        picDatas.remove(picDatas.size() - 1);
                        FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName),
                                bitmap);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        picDatas.add(photoPath);
                        updateChoiceImages();
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


    private void takePhoto() {
        // 跳转至拍照界面
        photoName = Constants.getImageName(this);
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        Uri imageUri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentPhote, 2);
    }

    private void updateChoiceImages() {
        picDatas.add(null);
        adapter = new MyGridViewAdapter(this, picDatas);
        modelPicGridView.setAdapter(adapter);
    }


    private void imageListeners() {
        SGApplication.getInstance().registerImagesReceiver(imageBroadCastListener);
    }

    private BroadcastReceiver imageBroadCastListener=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent
                    .getAction())) {
                boolean flag = intent.getBooleanExtra(
                        Constants.IS_DELE_IMAGES, false);
                if (!flag) {
                    return;
                }
                Object result = intent.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    picDatas = new LinkedList<String>();
                    LinkedList<String> tempStrs = dataBean
                            .getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        if (tempStrs.get(i).contains(
                                FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                            picDatas.add(tempStrs.get(i));
                        } else {
                            Bitmap subBitmap = FileUtil
                                    .compressImages(tempStrs.get(i));
                            if (subBitmap != null) {
                                String photoName = Constants.getImageName(CMIdentySecondActivity.this);
                                FileUtil.saveScalePhoto(
                                        FileUtil.getPhotopath(photoName),
                                        subBitmap);
                                subBitmap.recycle();
                                picDatas.add(FileUtil
                                        .getPhotopath(photoName));
                            }
                        }
                    }
                    updateChoiceImages();
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        List<String> tempImages = adapter.getImages();
        if (position < tempImages.size()) {
            Constants.imageBrower(CMIdentySecondActivity.this,
                    position, tempImages
                            .toArray(new String[tempImages.size()]), true);
        }
    }

    private void uploadImages() {
        ShowMsgDialog.showNoMsg(this, false);
        uploadImages.clear();
        List<String> datas = adapter.getImages();
        for (int i = 0; i < datas.size(); i++) {
            Bitmap bitmap = ImageTools.getBitmap(datas.get(i));
            PhotoesBean pb = new PhotoesBean();
            pb.setImageIndex(String.valueOf(i + 1));
            pb.setImageUrl(datas.get(i));
            pb.setLengthPx(String.valueOf(bitmap.getHeight()));
            pb.setPicSize(FileUtil.getImageByteSize(datas.get(i)));
            pb.setWidthPx(String.valueOf(bitmap.getWidth()));
            bitmap.recycle();
            uploadImages.add(pb);
        }
        doUploadImages();
    }

    public List<PhotoesBean> getUploadImages() {
        return uploadImages;
    }

    public String getOral() {
        return introduceText.getText().toString().trim();
    }

    private void doUploadImages() {
        if (currIndex < uploadImages.size()) {
            if (!uploadImages.get(currIndex).getImageUrl().contains("http://")) {

                AliySSOHepler.getInstance().uploadImageEngine(this,Constants.PHOTOBUM_PATH, uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

                    @Override
                    public void onSuccess(String imageUrl, int requestCode) {
                        Message msg = new Message();
                        msg.obj = imageUrl;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int errorCode, String msg, int requestCode) {
                        Message msg1 = new Message();
                        msg1.what = 2;
                        handler.sendMessage(msg1);
                    }

                    @Override
                    public void onProgress(String objectKey, int byteCount, int totalSize) {

                    }
                });
            } else {
                doUploadImages();
            }
        } else {
            transferLocationService();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 1:
                        String imageUrl = (String) msg.obj;
                        uploadImages.get(currIndex).setImageUrl(imageUrl);
                        currIndex = currIndex + 1;
                        doUploadImages();
                        break;
                    case 2:
                        if (currIndex > 0) {
                            transferLocationService();
                        } else {
                            ShowMsgDialog.cancel();
                            Toast.makeText(getApplicationContext(), "作品上传失败！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public void finishUpload(String msg) {
        ShowMsgDialog.cancel();
        Toast.makeText(getApplicationContext(), "申请认证成功！", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void transferLocationService() {
        identyTranBean.setUploadImages(uploadImages);
        VisitUnit visit = new VisitUnit();
        new Action(AuthPhotographerInfo.class,"${authPhotographerInfo}", this, null, visit).run();
    }

    public IdentyTranBean getIdentyTranBean() {
        return identyTranBean;
    }

}

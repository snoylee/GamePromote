package com.xygame.sg.activity.base;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.xygame.sg.R;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.define.view.SingleWheelView;
import com.xygame.sg.task.PopWinHelper;
import com.xygame.sg.utils.ActivityPool;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.GridAdapter;
import com.xygame.sg.utils.ImageActivity.Bimp;
import com.xygame.sg.utils.ImageActivity.Constants;
import com.xygame.sg.utils.ImageActivity.ImageGridActivity;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.NoScrollGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import base.RRes;
import base.ViewBinder;
import base.action.Action;
import base.action.CenterRepo;
import base.frame.VisitUnit;

public class CommonActivity extends SGBaseActivity {
    public static final int pickpicsize = 20;
    public NoScrollGridView noScrollgridview;
    public GridAdapter adapter;
    private static final int PHOTOHRAPH = 2;
    private PopWinHelper popWinHelper = PopWinHelper.newInstance();
    private String photoName;
    int max = 1;
    int photoid;

    public static String[] genScope(float b, float e, float step) {
        List<String> l = new ArrayList<String>();
        for (float i = b; i <= e; i += step) {
            l.add("" + new Float(i).intValue());
        }
        String[] a = new String[l.size()];
        l.toArray(a);
        return a;
    }

//    /****
//     * 启动滚轮
//     * @param sentRequestcode 发送出去的requestcode
//     * @param wheelNumber 滚轮类型
//     * @param activvity
//     */
//    public static void startWheelActivity(int sentRequestcode, com.xygame.sg.utils.Constants.WheelNumber wheelNumber,Activity activvity) {
//        String title = wheelNumber.getTitle();
//        String[] scope = wheelNumber.getScope();
//        String init = wheelNumber.getInit();
//        android.app.Activity aty = activvity;
//        if (scope != null) {
//            Intent intent1 = new Intent(aty,
//                    SingleWheelView.class).putExtra(com.xygame.sg.utils.Constants.SIGLE_WHEEL_TITLE, title).putExtra(com.xygame.sg.utils.Constants.SIGLE_WHEEL_VALUE, scope).putExtra(com.xygame.sg.utils.Constants.SIGLE_WHEEL_ITEM, init);
//            aty.startActivityForResult(intent1, sentRequestcode);
//
//        }
//    }

    @Override
    public void onDestroy() {
        ActivityPool.getMap().remove(this.getClass());
        super.onDestroy();
    }

    Map map = new HashMap();

    public View.OnClickListener getOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Activity aty = CommonActivity.this;
                String p2 = "";
                max = 1;
                if (map.containsKey(view.getId())) {
                    com.xygame.sg.utils.Constants.imageBrower(view.getContext(), 0, new String[]{map.get(view.getId()).toString()}, view.getId());

                } else {
                    popWinHelper.showSelectPhotoPopWindow(CommonActivity.this, CommonActivity.this.getWindow().getDecorView(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            switch (v.getId()) {
                                case R.id.dismiss:
                                    popWinHelper.dismissPopWindow(aty);
                                    break;
                                case R.id.camera:
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    photoName = FileUtil.getPhotoName(aty);
                                    File photoFile = getPhotoFile(photoName);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                    aty.startActivityForResult(intent, view.getId());
                                    popWinHelper.dismissPhotoPopWindow(aty);

                                    break;
                                case R.id.galary:
                                    photoName = FileUtil.getPhotoName(aty);
                                    Log.i("jiangTest", photoName);
                                    Intent intent1 = new Intent(aty,
                                            ImageGridActivity.class).putExtra("max", max);
                                    if (Bimp.drr.size() > 0) {
                                        intent1.putExtra(Constants.REFRESH_PHOTO, false);
                                    }
                                    aty.startActivityForResult(intent1, view.getId());
                                    popWinHelper.dismissPhotoPopWindow(aty);
                                    break;
                            }
                        }

                    });
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.update();
        Set s = new HashSet(map.keySet());
        for (Object rcode : s) {
            if (!new File(map.get(rcode).toString()).exists()) {
                ((ImageView) findViewById(Integer.parseInt(rcode.toString()))).setImageResource(R.drawable.addpic);
                map.remove(rcode);



            } else {
                ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                        map.get(rcode).toString(), ((ImageView) findViewById(Integer.parseInt(rcode.toString()))));
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {//camero
            if (map.containsKey(requestCode)) {
                if (!new File(map.get(requestCode).toString()).exists()) {
                    ((ImageView) findViewById(requestCode)).setImageResource(R.drawable.addpic);
                    map.remove(requestCode);
                    return;
                }
            }
            try {

                File photoFile = getPhotoFile(photoName);
                if (photoFile.exists()) {

                    Bitmap bitmap = FileUtil.compressImages(photoFile.getAbsolutePath());
                    FileUtil.saveHighlyScalePhoto(photoFile.getAbsolutePath(), bitmap);

                    String image = photoFile.getAbsolutePath();
                    if (findViewById(requestCode) != null) {
                        ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                                photoFile.getAbsolutePath(), ((ImageView) findViewById(requestCode)));
                    }
                    if (map.containsKey(requestCode)) {
                        return;
                    }
                    switch (requestCode) {

                        case PHOTOHRAPH:
                            if (adapter != null) {

                                List<String> drrs = new ArrayList<String>(Arrays.asList(new String[]{image}));
                                if (adapter.cnt.size() + drrs.size() < pickpicsize) {
                                    drrs.add("end");
                                }
                                adapter.setContent(drrs);
                                adapter.notifyDataSetChanged();
                            }
                            break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


        } else {
            ArrayList<String> list = data.getStringArrayListExtra("bimp");

            if (list != null && list.size() > 0) {
                if (!data.getBooleanExtra("multi", false)) {
                    if (list.size() == 1) {

                        File photoFile = getPhotoFile(photoName);
                        Bitmap bitmap = FileUtil.compressImages(list.get(0));
                        FileUtil.saveHighlyScalePhoto(photoFile.getAbsolutePath(), bitmap);


                        String image = photoFile.getAbsolutePath();
                        ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                                photoFile.getAbsolutePath(), (ImageView) findViewById(requestCode));
                        if (map.containsKey(requestCode)) {
                            return;
                        }

                    }
                } else {
                    List<String> drrs = new ArrayList<String>();
                    for (String s : list) {

                        photoName = FileUtil.getPhotoName(this);
                        File photoFile = getPhotoFile(photoName);
                        Bitmap bitmap = FileUtil.compressImages(s);
                        FileUtil.saveHighlyScalePhoto(photoFile.getAbsolutePath(), bitmap);

                        drrs.add(photoFile.getAbsolutePath());

                    }
                    if (adapter.cnt.size() + drrs.size() < pickpicsize) {
                        drrs.add("end");
                    }
                    doUrls(drrs);
                }
            }
        }

    }

    public void doUrls(List<String> drrs) {
    }

    public static File getPhotoFile(String photoName) {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.PIC_PATH, photoName);
        file.getParentFile().mkdirs();
        return file;
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public VisitUnit visitUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPool.put(this);
        String layout = getIntent().getStringExtra("layout");
        visitUnit();
        if (layout != null) {
            int l = RRes.get(layout).getAndroidValue();
            setContentView(new ViewBinder(this, visitUnit).inflate(l, null));
        }
    }

    public void visitUnit() {
        visitUnit = new VisitUnit();
    }

    public VisitUnit getVisitUnit() {
        return visitUnit;
    }

    public void addNoGridView(ViewGroup v, int i) {
        noScrollgridview = (NoScrollGridView) LayoutInflater.from(this).inflate(R.layout.nogridview, null);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(noScrollgridview.getContext(), handler);

        noScrollgridview.setAdapter(adapter);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                    @Override
                                                    public void onItemClick(final AdapterView<?> arg0, final View view, int position,
                                                                            long arg3) {
                                                        if (arg0.getItemAtPosition(position).toString().equals("end")) {
                                                            final Activity aty = CommonActivity.this;
                                                            String p2 = "";
                                                            max = 1;
                                                            popWinHelper.showSelectPhotoPopWindow(CommonActivity.this, CommonActivity.this.getWindow().getDecorView(), new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    popWinHelper.dismissPhotoPopWindow(aty);
                                                                    switch (v.getId()) {
                                                                        case R.id.dismiss:
                                                                            break;
                                                                        case R.id.camera:
                                                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                                            photoName = FileUtil.getPhotoName(aty);
                                                                            File photoFile = getPhotoFile(photoName);
                                                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                                                            aty.startActivityForResult(intent, PHOTOHRAPH);
                                                                            break;
                                                                        case R.id.galary:
                                                                            Intent intent1 = new Intent(view.getContext(),
                                                                                    ImageGridActivity.class).putExtra("max", pickpicsize - adapter.getCount() + 1).putExtra("multi", true);
                                                                            intent1.putExtra(Constants.REFRESH_PHOTO, true);
                                                                            startActivityForResult(intent1, arg0.getId());
                                                                            break;
                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            com.xygame.sg.utils.Constants.imageBrower(view.getContext(), position, adapter.cnt.toArray(new String[Bimp.drr.size()]), true);
                                                        }


                                                    }
                                                }
        );


//        = (ViewGroup) findViewById(R.id);
        v.addView(noScrollgridview, i);

    }

}

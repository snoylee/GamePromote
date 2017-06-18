package com.xygame.sg.task.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xygame.sg.R;
import com.xygame.sg.task.PopWinHelper;
import com.xygame.sg.utils.ImageActivity.Bimp;
import com.xygame.sg.utils.ImageActivity.Constants;
import com.xygame.sg.utils.ImageActivity.ImageGridActivity;

import java.io.File;
import java.util.List;
import java.util.UUID;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/9.
 */
public class PickImage extends Task {
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int PHOTOHRAPH = 2;

    private PopWinHelper popWinHelper = PopWinHelper.newInstance();
    private String photoName;
    int max = 1;

    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        final android.app.Activity aty = param.getActivity();
        String p2 = "";
        if (params.size() > 1) {
            p2 = params.get(1);
            max = Integer.parseInt(p2);
        }
        popWinHelper.showSelectPhotoPopWindow(param.getActivity(), param.getActivity().getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dismiss:
                        popWinHelper.dismissPopWindow(aty);
                        break;
                    case R.id.camera:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoName = getPhotoName(aty);
                        File photoFile = getPhotoFile(photoName);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        aty.startActivityForResult(intent, PHOTOHRAPH);
                        popWinHelper.dismissPhotoPopWindow(aty);
                        break;
                    case R.id.galary:
                        photoName = getPhotoName(aty);
                        Log.i("jiangTest", photoName);
                        Intent intent1 = new Intent(aty,
                                ImageGridActivity.class).putExtra("max", max);
                        if (Bimp.drr.size() > 0) {
                            intent1.putExtra(Constants.REFRESH_PHOTO, false);
                        }
                        aty.startActivity(intent1);
                        popWinHelper.dismissPhotoPopWindow(aty);
                        break;
                }

            }
        });
        return super.run(methodname, params, param);
    }

    public static File getPhotoFile(String photoName) {
        return new File(Environment.getExternalStorageDirectory() + File.separator + Constants.PIC_PATH, photoName);
    }

    public static String getPhotoName(Context context) {
        return getDeviceUUID(context) + System.currentTimeMillis() + ".jpg";
    }

    public static String getDeviceUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
//    public void showPicturePicker(final Context ctx) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//        builder.setTitle("图片来源");
//        builder.setNegativeButton("取消", null);
//        builder.setItems(new String[] { "拍照", "相册" },
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                takePhoto(ctx);
//                                break;
//                            case 1:
//                                chicePhoto(ctx);
//                                break;
//
//                            default:
//                                break;
//                        }
//                    }
//                });
//        builder.create().show();
//    }

//    String photoName;
//    private void takePhoto(Context ctx) {
//        // 跳转至拍照界面
//        photoName = String.valueOf(System.currentTimeMillis()).concat(".png");
//        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        File out = new File(FileUtil.getPhotopath(photoName));
//        Uri uri = Uri.fromFile(out);
//        // 获取拍照后未压缩的原图片，并保存在uri路径中
//        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        ctx.startActivityForResult(intentPhote, TAKE_PICTURE);
//    }
//
//    private void chicePhoto(Context ctx) {
//        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        openAlbumIntent.setDataAndType(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        ctx.startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
//    }
}

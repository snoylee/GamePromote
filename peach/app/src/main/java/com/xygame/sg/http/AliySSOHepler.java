package com.xygame.sg.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.OSSException;
import com.aliyun.mbaas.oss.model.TokenGenerator;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.storage.OSSData;
import com.aliyun.mbaas.oss.util.OSSToolKit;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.SGApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by tony on 2016/3/10.
 */
public class AliySSOHepler {
    private static AliySSOHepler mInstance;
    public OSSBucket sampleBucket;
    static {
        OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5,
                                        String type, String date, String ossHeaders,
                                        String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type
                        + "\n" + date + "\n" + ossHeaders + resource;

                return OSSToolKit.generateToken(AliPreferencesUtil.getAccessKey(SGApplication.getInstance().getApplicationContext()), AliPreferencesUtil.getScrectKey(SGApplication.getInstance().getApplicationContext()),
                        content);
            }
        });
        OSSClient.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com"); // 设置全局默认数据中心域名
        OSSClient.setGlobalDefaultACL(AccessControlList.PRIVATE); // 设置全局默认bucket访问权限
    }
    public static AliySSOHepler getInstance() {
        if (mInstance == null) {
            mInstance = new AliySSOHepler();
        }
        return mInstance;
    }

    public AliySSOHepler(){
        OSSClient.setApplicationContext(SGApplication.getInstance().getApplicationContext()); // 传入应用程序context

        // 开始单个Bucket的设置
        sampleBucket = new OSSBucket(AliPreferencesUtil.getBuckekName(SGApplication.getInstance().getApplicationContext()));
        sampleBucket.setBucketACL(AccessControlList.PUBLIC_READ_WRITE); // 如果这个Bucket跟全局默认的访问权限不一致，就需要单独设置
    }

    /**
     * 上传图片
     * <一句话功能简述>
     * <功能详细描述>
     *
     * @param ossDirName
     * @param imageStr
     * @param callBack
     * @action [请添加内容描述]
     */
    public void uploadImageEngine(Context context,String ossDirName, String imageStr, HttpCallBack callBack) {
        upload(context,ossDirName, new File(imageStr), callBack, "image/*");
    }

    public void uploadMedia(Context context,String ossDirName, String imageStr, HttpCallBack callBack) {
        uploadRecord(context,ossDirName, new File(imageStr), callBack, "image/*");
    }

//    public void uploadSecretImage(Context context,String ossDirName, String imageStr, HttpCallBack callBack) {
//        uploadSecretImage(context,ossDirName, new File(imageStr), callBack, "image/*");
//    }

    public void uploadSecretImage(Context context,String ossDirName, File imageFile, final HttpCallBack callBack, String type) {
        if (imageFile == null || !imageFile.exists()) {
            callBack.onFailure(-1, imageFile.getAbsolutePath() + " not found!", 0);
            return;
        }
        if (ossDirName == null || ossDirName.trim().length() == 0) {
            ossDirName = "avatar";
        }
        byte[] defultBtyte = fileToByte(imageFile);
//        byte[] dataToUpload= FileUtil.doDecode(defultBtyte, AliPreferencesUtil.getAuthPicKey(context));
        byte[] dataToUpload = fileToByte(imageFile);
        Log.i("jiangTest", "fileName=" + imageFile.getName());
        final OSSData data = new OSSData(sampleBucket, ossDirName
                + File.separator + imageFile.getName());
        if (TextUtils.isEmpty(type)) {
            type = "image/*";
        }
        try {
            data.setData(dataToUpload, type);
        } catch (OSSException e) {
            callBack.onFailure(-1, "上传失败", 0);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        data.uploadInBackground(new SaveCallback() {

            @Override
            public void onSuccess(String objectKey) {
                String imageUrlYuan = data.getResourceURL();
                String decodeUrl = URLDecoder.decode(imageUrlYuan);
                String imageUrl = decodeUrl.replace(Constants.ALIY_IMAGE_DMO, Constants.AIM_URL);
                callBack.onSuccess(imageUrl, 0);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                callBack.onProgress(objectKey, byteCount, totalSize);
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                callBack.onFailure(-1, ossException.getException().toString(), 0);
            }
        });
    }

    public void upload(Context context,String ossDirName, File imageFile, final HttpCallBack callBack, String type) {
        if (imageFile == null || !imageFile.exists()) {
            callBack.onFailure(-1, imageFile.getAbsolutePath() + " not found!", 0);
            return;
        }
        if (ossDirName == null || ossDirName.trim().length() == 0) {
            ossDirName = "avatar";
        }
        byte[] dataToUpload = fileToByte(imageFile);
        Log.i("jiangTest", "fileName=" + imageFile.getName());
        final OSSData data = new OSSData(sampleBucket, ossDirName
                + File.separator + imageFile.getName());
        if (TextUtils.isEmpty(type)) {
            type = "image/*";
        }
        try {
            data.setData(dataToUpload, type);
        } catch (OSSException e) {
            // TODO Auto-generated catch block
            callBack.onFailure(-1, "上传失败", 0);
            e.printStackTrace();
        }

        data.uploadInBackground(new SaveCallback() {

            @Override
            public void onSuccess(String objectKey) {
                Log.d("jiangTest", "data.getResourceURL()======" + data.getResourceURL());
                String imageUrlYuan = data.getResourceURL();
                String decodeUrl = URLDecoder.decode(imageUrlYuan);
                String imageUrl = decodeUrl.replace(Constants.ALIY_IMAGE_DMO, Constants.AIM_URL);
                callBack.onSuccess(imageUrl, 0);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                callBack.onProgress(objectKey, byteCount, totalSize);
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                callBack.onFailure(-1, ossException.getException().toString(), 0);
            }
        });
    }

    public void uploadRecord(Context context,String ossDirName, File imageFile, final HttpCallBack callBack, String type) {
        if (imageFile == null || !imageFile.exists()) {
            callBack.onFailure(-1, imageFile.getAbsolutePath() + " not found!", 0);
            return;
        }
        if (ossDirName == null || ossDirName.trim().length() == 0) {
            ossDirName = "avatar";
        }
        byte[] dataToUpload = fileToByte(imageFile);
        Log.i("jiangTest", "fileName=" + imageFile.getName());
        final OSSData data = new OSSData(sampleBucket, ossDirName
                + File.separator + imageFile.getName());
        if (TextUtils.isEmpty(type)) {
            type = "image/*";
        }
        try {
            data.setData(dataToUpload, type);
        } catch (OSSException e) {
            // TODO Auto-generated catch block
            callBack.onFailure(-1, "上传失败", 0);
            e.printStackTrace();
        }

        data.uploadInBackground(new SaveCallback() {
            @Override
            public void onSuccess(String objectKey) {
                String imageUrlYuan = data.getResourceURL();
                String decodeUrl = URLDecoder.decode(imageUrlYuan);
                String imageUrl = decodeUrl.replace(Constants.ALIY_IMAGE_DMO, Constants.AIM_URL);
                callBack.onSuccess(imageUrl, 0);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                callBack.onProgress(objectKey, byteCount, totalSize);
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                callBack.onFailure(-1, ossException.getException().toString(), 0);
            }
        });
    }

    /**
     * file to byte[]
     */
    public byte[] fileToByte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}

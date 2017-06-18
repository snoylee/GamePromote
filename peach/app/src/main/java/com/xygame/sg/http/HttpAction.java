package com.xygame.sg.http;

import android.text.TextUtils;

import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.TDevice;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import base.data.net.http.DesUtils;

public class HttpAction {

    public static String execute(String getUrl, String jsonObject) {
        String returnLine = "";
        InputStream inputStream = null;
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(getUrl);
            urlConnection = (HttpsURLConnection) url.openConnection();

		/* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

		/* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");
            // read response
        /* for Get request */
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(20000);
            Constants.trustAllHosts();//信任所有

            DataOutputStream out = new DataOutputStream(urlConnection
                    .getOutputStream());

            byte[] content = DesUtils.encrypt(jsonObject.toString()).getBytes("utf-8");

            out.write(content, 0, content.length);
            out.flush();
            out.close(); // flush and close

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

            //StringBuilder builder = new StringBuilder();

            String line = "";

            System.out.println("Contents of post request start");

            while ((line = reader.readLine()) != null) {
                // line = new String(line.getBytes(), "utf-8");
                returnLine += line;

                System.out.println(line);

            }
            reader.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            ShowMsgDialog.cancel();
            returnLine = "2";
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return returnLine;
    }


    public static String doPostRequest(String url, String jsonObject) {
        String response = null;
        HttpResponse httpResponse = null;
        HttpClient defaultHttpClient = Constants.getNewHttpClient();
        defaultHttpClient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("ACCEPT", "application/json");
        try {
            if (jsonObject != null) {
                StringEntity entity = new StringEntity(DesUtils.encrypt(jsonObject.toString()),
                        "utf-8");
                entity.setContentType("application/json");
                entity.setContentEncoding("utf-8");
                httppost.setEntity(entity);
            }
        } catch (Exception ex) {
            ShowMsgDialog.cancel();
            response = "0";
            ex.printStackTrace();
        }
        if (!"0".equals(response)) {
            try {
                httpResponse = defaultHttpClient.execute(httppost);
                if (httpResponse != null) {
                    response = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                }
            } catch (SocketTimeoutException ex) {
                ShowMsgDialog.cancel();
                response = "1";
                ex.printStackTrace();
            } catch (IOException ex) {
                ShowMsgDialog.cancel();
                response = "2";
                ex.printStackTrace();
            }
        }
        return response;
    }

    public static ResponseBean requestService(RequestBean requestBean) {
        ResponseBean item = new ResponseBean();
        String responseBody = "";
        JSONObject json = new JSONObject();
        try {
            json.put("code", "0000");
            if (requestBean.getOsType() != null) {
                json.put("osType", requestBean.getOsType());
            } else {
                json.put("osType", "android");
            }
            String userid = UserPreferencesUtil.getUserId(SGApplication.getInstance().getBaseContext());
            json.put("userId", userid == null || userid.equals("null") ? "" : userid.toString());
            json.put("timeStamp", System.currentTimeMillis());
            json.put("data", requestBean.getData());
            json.put("version", TDevice.getCurrentVersionName(SGApplication.getInstance().getBaseContext()));
            String reqUrl = requestBean.getServiceURL();
            String reqParam = json.toString();
            if (Constants.DEBUG) {
                System.out.println("请求参数:" + reqUrl + reqParam);
            }
            responseBody = doPostRequest(reqUrl, reqParam);
        } catch (Exception ex) {
            item.setMsg("数据格式组装错误");
            ShowMsgDialog.cancel();
            ex.printStackTrace();
        }

        if (!TextUtils.isEmpty(responseBody)) {
            if ("0".equals(responseBody)) {
                item.setMsg("数据格式组装错误");
            } else if ("1".equals(responseBody)) {
                item.setMsg("网络请求超时");
            } else if ("2".equals(responseBody)) {
                item.setMsg("网络请求失败");
            } else {
                try {
                    String decryptStr = DesUtils.decrypt(responseBody);
                    if (Constants.DEBUG) {
                        System.out.println("请求返回:" + decryptStr);
                    }
                    JSONObject jsonObject = new JSONObject(decryptStr);
                    item.setMsg(StringUtils.getJsonValue(jsonObject, "msg"));
                    item.setCode(StringUtils.getJsonValue(jsonObject, "code"));
                    item.setRecord(StringUtils.getJsonValue(jsonObject, "record"));
                    item.setSuccess(StringUtils.getJsonValue(jsonObject, "success"));
                    item.setTimeStamp(StringUtils.getJsonValue(jsonObject, "timeStamp"));
                    item.setVersion(StringUtils.getJsonValue(jsonObject, "version"));
                } catch (Exception e) {
                    item = new ResponseBean();
                    item.setMsg("数据处理失败");
                    ShowMsgDialog.cancel();
                    e.printStackTrace();
                }
            }
        }

        return item;
    }
}

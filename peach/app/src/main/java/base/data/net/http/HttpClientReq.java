package base.data.net.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import base.log.Logs;

public class HttpClientReq {
	public static boolean isEncypt = true;
	public static String doPost(Map<String, String> param, String url) {
		String strResult = null;
		HttpPost httpRequest = new HttpPost(url);
		Logs.i(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : param.keySet()) {
			String value = param.get(key);
			Logs.i(key + "---->" + value);
			params.add(new BasicNameValuePair(key, value));
		}
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	public static String doPostJson(String url, String json) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		StringBuilder result = new StringBuilder();
		HttpPost httpPost = new HttpPost(url);
		StringEntity stringEntity = new StringEntity(isEncypt?DesUtils.encrypt(json):json, "UTF-8");
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");
		httpPost.setEntity(stringEntity);
		HttpResponse res = httpclient.execute(httpPost);
		if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = res.getEntity();
			if (entity != null) {
				InputStream inputStream = entity.getContent();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader reader = new BufferedReader(inputStreamReader);
				String s;
				while (((s = reader.readLine()) != null)) {
					result.append(s);
				}
				reader.close();
			}
		}
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
		return isEncypt?DesUtils.decrypt(result.toString()):result.toString();
	}

	public static String doPostReq(Map<String, String> map, String requestUrl) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		Iterator<?> it = map.entrySet().iterator();
		StringBuffer params = new StringBuffer();
		HttpURLConnection httpURLConnection = null;
		StringBuffer responseResult = new StringBuffer();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry element = (Map.Entry) it.next();
			params.append(element.getKey());
			params.append("=");
			params.append(element.getValue());
			params.append("&");
		}
		Log.i("jiangTest", params.toString());
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}

		try {
			URL url = new URL(requestUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			// httpURLConnection.setRequestProperty("Content-Length",
			// String.valueOf(params.length()));
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			printWriter.write(isEncypt?DesUtils.encrypt(params.toString()):params.toString());
			printWriter.flush();
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode != 200) {
				Logs.i(" Error===" + responseCode);
			} else {
				Logs.i("Post Success!");
			}
			bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				responseResult.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return isEncypt?DesUtils.decrypt(responseResult.toString()):responseResult.toString();
	}

}

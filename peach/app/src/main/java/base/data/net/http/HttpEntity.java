/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.data.net.http;

import com.xygame.sg.utils.Constants;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author minhua
 */
public class HttpEntity {
    public int returntype = 0;
    public static final int TO_OBJECT = 1;
    public static final int TO_JSON = 2;
    
    
    public HttpParam param;
    public boolean issync;
    public String url;
    public Object jsondata;
    public boolean saveCookie;
    public boolean toFormat = true;
    public int requestid;
    public String returnParsName;
    public boolean simplemode;

    public boolean isSimplemode() {
        return simplemode;
    }
    public String continueParam;

    public String getContinueParam() {
        return continueParam;
    }

    public HttpEntity setContinueParam(String continueParam) {
        this.continueParam = continueParam;
        return this;
    }
    public String continueParamValue = "0";

    public String getContinueParamValue() {
        return continueParamValue;
    }

    public HttpEntity setContinueParamValue(String continueParamValue) {
        this.continueParamValue = continueParamValue;
        return this;
    }
    String regex;
    boolean regexfirst;
    boolean continueParamValuefirst;

    public String getRegex() {

        if (regex == null || regex.equals("null")) {

            return continueParam == null ? regex : continueParam + "=";

        }
        return regex;
    }

    public HttpEntity next() {

        regex = getRegex();
        if (continueParam != null && url.contains(regex)) {
            if (!continueParamValuefirst) {
                continueParamValuefirst = true;
            } else {
                String replacement;
                url = url.replaceAll(regex + continueParamValue, regex
                        + (Integer.parseInt(continueParamValue) + 1));

                continueParamValue = (Integer.parseInt(continueParamValue) + 1)
                        + "";
            }
        }
        return this;
    }

    public HttpEntity setSimplemode(boolean simplemode) {
        this.simplemode = simplemode;
        return this;
    }

    public HttpEntity() {
    }
    Map<String,String> data = new TreeMap<String,String>();
    public HttpEntity addData(Map<String,String> map){
    	data.putAll(map);
    	return this;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }
    public static CookieManager cookieManager;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HttpEntity other = (HttpEntity) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    public HttpEntity(String url, String returnParsName,int resulttype) {
        this(true, -1, null, true, url);
        this.returnParsName = returnParsName;
        this.returntype = resulttype;
    }

    public HttpEntity(String url, String returnParsName) {
        this(url, returnParsName, 0);
    }
    public HttpEntity addParam(String param) {

        addParam(param.split("=")[0], param.split("=")[1]);
        return this;
    }
    public HttpEntity addParam(String paramkey, String paramvalue) {
        if (param == null) {
            param = new HttpParam();
        }
        param.addParam(paramkey, paramvalue);
        return this;
    }
    Set<String> ck = new HashSet<String>();

    public String runUrl(String url){
		return url;
    	
    }
    
    public Object postRequest(HttpEntity httpety) throws Exception {
    	
        if (httpety == null) {
            return null;
        }
        String cookie = "";
        String postUrl = httpety.url;
        Object jsonData = httpety.jsondata;
        boolean toFormat = httpety.toFormat;
        boolean simplemode = httpety.simplemode;
        Object result = "";
        try {
            if(httpety.param!=null){
                postUrl = httpety.param.toUrl(postUrl);
            }
            String json = null;
            if (toFormat) {
                json = getJson(data);
            } else if (data != null
                    && !data.toString().trim().equals("")) {
                json = data.toString();
            }
            if (Constants.DEBUG){
                System.out.println(postUrl+"参数"+json);
            }

            String strresult = HttpClientReq.doPostJson(runUrl(postUrl),json);

            jsonUtil = new JsonUtil();
            strresult = filter(strresult);
            result = jsonUtil.findJsonNode(httpety.returnParsName, strresult);
            if(httpety.returntype== TO_JSON){
                result = jsonUtil.toJson(result.toString());
            }else if(httpety.returntype== TO_OBJECT){
                result = jsonUtil.from(result.toString());
            }
            
            this.jsonUtil = jsonUtil;
            filterResult(result);
        } catch (ConnectTimeoutException e) {
            return e;

        } catch (Exception e) {
            throw e;
        }
        
        return result;
    }

    public Object filterResult(Object result) {
return result;
    }

    JsonUtil jsonUtil;
    
	public JsonUtil getJsonUtil() {
		return jsonUtil;
	}

	public String filter(String content) {
		// TODO Auto-generated method stub
		return content;
	}

	public void runConnection(HttpURLConnection connection) throws ProtocolException {
		connection.setDoOutput(true);

		connection.setRequestProperty("Content-Type",
		        "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("POST");
	}
    public static String getJson(Object object) throws JSONException {
        String requestDetail = "";
        Map<String, Object> map = null;
        if (object == null) {
            return null;
        }
        if (object instanceof Map) {
            JSONObject jsonobject = new JSONObject();
            map = (Map<String, Object>) object;
            for (String o : map.keySet()) {

                String v = map.get(o).toString();
                if(v.contains("{")||v.contains("[")){
                    v = v.replaceAll("\"\\[","[").replaceAll("\\]\"","]").replaceAll("\"\\{","{").replaceAll("\\}\"","}");
                }else {
                    v = "\""+v+"\"";
                }
                requestDetail += ",\""+o.toString()+"\""+":"+v;

            }

            requestDetail = "{"+requestDetail.substring(1)+"}";
            requestDetail = requestDetail.replaceAll("\\\\","");
        } else if (object instanceof String) {
            requestDetail = object.toString();
        }

        String jsonString = requestDetail;

//			"{\"Key\":" + "\"00000000-0000-0000-0000-000000000000\"" + ","
//					+ "\"RequestDetail\":{" + requestDetail.toString() + "},"
//					+ "\"Refer\":null," + "\"SessionInfo\":null}";
        return jsonString;
    }

    public static String formatJson(Object object) throws JSONException {
        String requestDetail = null;
        Map<String, Object> map = null;
        if (object == null) {
            return null;
        }
        if (object instanceof Map) {
            JSONObject jsonobject = new JSONObject();
            map = (Map<String, Object>) object;
            for (String o : map.keySet()) {
                jsonobject.put(o, map.get(o));
            }
            requestDetail = jsonobject.toString().replaceAll("\\\\\"\\[","[").replaceAll("\\]\\\\\"","]");
        } else if (object instanceof String) {

            requestDetail = object.toString();
        }

        String jsonString = requestDetail;

//			"{\"Key\":" + "\"00000000-0000-0000-0000-000000000000\"" + ","
//					+ "\"RequestDetail\":{" + requestDetail.toString() + "},"
//					+ "\"Refer\":null," + "\"SessionInfo\":null}";
        return jsonString;
    }
//    public static String formatJson(Object object) throws JSONException {
//        String requestDetail = null;
//        Map<String, Object> map = null;
//        if (object == null) {
//            return null;
//        }
//        if (object instanceof Map) {
//            JSONObject jsonobject = new JSONObject();
//            map = (Map<String, Object>) object;
//            for (String o : map.keySet()) {
//                String v;
//                if(map.get(o).toString().contains("[")){
//v  = map.get(o).toString().replaceAll("\\\\\"\\[","[").replaceAll("\\]\\\\\"","]");
//                }else{
//v = map.get(o).toString();
//                }
//                jsonobject.put(o, v);
//            }
//            requestDetail = jsonobject.toString();
//        } else if (object instanceof String) {
//            requestDetail = object.toString();
//        }
//
//        String jsonString = requestDetail;
//
////			"{\"Key\":" + "\"00000000-0000-0000-0000-000000000000\"" + ","
////					+ "\"RequestDetail\":{" + requestDetail.toString() + "},"
////					+ "\"Refer\":null," + "\"SessionInfo\":null}";
//        return jsonString;
//    }

    private static void setValue(JSONObject jsonObject, String key,
            Object value) throws JSONException {

    }

    public HttpEntity(boolean issync, int requestid, Object jsondata,
            boolean toFormat, String... url) {
        this();

        if (url == null) {
            throw new IllegalArgumentException("url found null");
        }
        if (url.length > 0) {
            this.url = url[0];

        }

        this.jsondata = jsondata;

        this.toFormat = toFormat;
        this.issync = issync;
        this.requestid = requestid;
    }

    public static String getMappedJson(Properties prop) {
        String json = "";
        String key;
        String value;
        Enumeration enu = (Enumeration) prop.propertyNames();
        while (enu.hasMoreElements()) {
            key = enu.nextElement().toString();
            Object obj = prop.get(key);
            if (prop.get(key).toString().startsWith("[")) {
                json += ",\""
                        + key
                        + "\":"
                        + (obj == null || obj.toString().equals("") ? "null"
                        : (prop.get(key).toString()));
            } else {
                json += ",\""
                        + key
                        + "\":"
                        + (obj == null || obj.toString().equals("") ? "null"
                        : ("\"" + prop.get(key).toString() + "\""));
            }
        }
        json = json.substring(1);

        // json="{\"RequestDetail\":{"+json+"},\"Refer\":null,\"Key\":\"00000000-0000-0000-0000-000000000000\",\"SessionInfo\":null}";
        return json;
    }
}

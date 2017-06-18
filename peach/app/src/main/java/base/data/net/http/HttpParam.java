/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.data.net.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author minhua
 */
public class HttpParam {
	public String activityABId;
	public String activitytBId;

	public HttpParam() {

	}
	

	

	

	

	

	static Map<String, HttpParam> activityABId_instance = new TreeMap<String, HttpParam>() {

	};

	
	public HttpParam clearParams(){
		this.params.clear();
		return this;
	}
	public String getActivityABId() {
		return activityABId;
	}

	public void setActivityABId(String activityABId) {
		this.activityABId = activityABId;
	}

	public static HttpParam instance;

	public static HttpParam getIntance() {
		return instance;

	}

	public Map<String, String> params = new HashMap<String, String>();

	public String url;

	public String getUrl() {
		return url;
	}

	public HttpParam setUrl(String url) {
		this.url = url;
		return this;
	}

	public HttpParam addParam(String param, Object paramvalue) {
		if (param == null || param.toString().trim().equals("")
//				|| paramvalue.toString().trim().equals("")
				) {
			new Exception("http's param invalid").printStackTrace();
		}
		if(paramvalue==null){
			paramvalue = "";
		}
		try {
			params.put(param, URLEncoder.encode(new String(paramvalue.toString().getBytes("GBK"),"UTF-8"), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String toUrl(String url) {
		String toaddpart = "";
		for (Object param : this.params.keySet()) {
			toaddpart += "&" + param.toString() + "="
					+ this.params.get(param).toString();
		}
		if (params.size() != 0) {
			toaddpart = toaddpart.substring(1);

			if (url.contains("?")) {
				toaddpart = "&" + toaddpart;
			} else {
				toaddpart = "?" + toaddpart;
			}
		}
		String result = url + toaddpart;
		return result;
	}

}

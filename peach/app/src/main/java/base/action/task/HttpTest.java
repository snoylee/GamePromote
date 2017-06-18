package base.action.task;

import android.widget.Toast;

import com.xygame.sg.imageloader.FileManager;
import com.xygame.sg.utils.ShowMsgDialog;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import base.action.Action.Param;
import base.action.AsynTask;
import base.action.CenterRepo;
import base.data.net.http.HttpEntity;
import base.file.PropUtil;
import base.frame.FillingController;
import base.init.TelliUtil;

/**
 * Created by minhua on 2015/10/26.
 */
public class HttpTest extends AsynTask {

    @Override
    public Object threadRun(String methodname, List<String> params, final Param aparam) {
        final Map<String, Object> data = new HashMap<String, Object>();
        String url = params.get(0);
        aparam.getVisitunit().setTarget(true);
        final HttpEntity httpEntity = new HttpEntity(url, TelliUtil.getReturnparsename(), HttpEntity.TO_OBJECT);
        for (int i = 1; i < params.size(); i++) {
            if (params.get(i) instanceof String) {

                if (params.get(i).contains("=")) {
                    if (params.get(i).split("=").length == 1) {
//                        data.put(params.get(i).split("=")[0], "");
                        continue;
                    } else if (params.get(i).substring(params.get(i).indexOf("=") + 1).equals("null")) {
                        continue;
                    } else if (params.get(i).substring(params.get(i).indexOf("=") + 1).contains("{")) {
                        String value = params.get(i).substring(params.get(i).indexOf("=") + 1);
                        value = value.substring(1, value.length() - 1);
                        data.put(params.get(i).split("=")[0], value);
                    }
                    Object val = params.get(i).split("=")[1];
                    try {
                        val = Integer.parseInt(val.toString());
                    } catch (Exception e) {
                    }
                    data.put(params.get(i).split("=")[0].trim(), val);
                } else {
                    httpEntity.addParam(params.get(i));
                }
            }
        }
        Object userid = CenterRepo.getInsatnce().getRepo().get("userId");
        final Map<String, String> json = new TreeMap<String, String>();
        json.put("version", "1.0");
        json.put("timeStamp", "" + System.currentTimeMillis());
        json.put("userId", userid == null || userid.equals("null") ? "0" : userid.toString());
        json.put("code", "0000");
        json.put("osType", "android");
        try {
            json.put("data", HttpEntity.formatJson(data));
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        HttpEntity http = null;
        Object object = null;
        try {
            object = (http = new HttpEntity() {

                @Override
                public String runUrl(String url) {
                    // TODO Auto-generated method stub
                    return HttpTest.this.runUrl(url);
                }

                @Override
                public void runConnection(HttpURLConnection connection) throws ProtocolException {
                    // TODO Auto-generated method stub
                    HttpTest.this.runConnection(connection);
                }

                @Override
                public String filter(String content) {
                    // TODO Auto-generated method stub
                    try {
                        return runResult(content);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return "";
                }

                @Override
                public Object filterResult(Object result) {
                    return super.filterResult(result);
                }
            }).addData(json).postRequest(httpEntity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        aparam.resultunit = http.getJsonUtil();
        return object;
    }

    public void callback(Param aparam, Object object) {
        // TODO Auto-generated method stub
        String msg = aparam.getResultunit().getString("msg");
        if (msg != null && !msg.contains("成功")) {
            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        FillingController.parseParentFragmentFillingArea(aparam.getVisitunit(), object);


    }

    @Override
    public void catchException(Param aparam, Exception object) {
        if (object instanceof ConnectTimeoutException) {
//            String msg = "";
//            msg += ((Exception) object).getMessage();
//            for (StackTraceElement stacktrace : ((Exception) object).getCause().getStackTrace()) {
//                msg += stacktrace.toString() + "\n";
//            }
            ShowMsgDialog.cancel();
            ShowMsgDialog.show(aparam.getActivity(), "网络异常，连接超时", true);
        }
        super.catchException(aparam, object);
    }

    public void runConnection(HttpURLConnection connection) throws ProtocolException {
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
    }

    public String runUrl(String url) {
        return url;
    }

    public String runResult(String result) {
        // TODO Auto-generated method stub
        return result;
    }

    public Object filterResult(Object result) {
        return result;
    }


}

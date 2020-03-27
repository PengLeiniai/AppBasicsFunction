package com.you.function.network.http;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.you.function.Function;
import com.you.function.network.http.callback.JsonCallback;
import com.you.function.network.http.imp.BaseModel;
import com.you.function.network.http.utlis.CommonUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;

import static android.os.Build.DEVICE;
import static android.os.Build.MODEL;

/**
 * Created by PengLei_PC on 2018/4/29.
 */

public class HttpHelp {

    private static HttpHelp instance = null;

    private static boolean isUpLogin = true;

    private static Activity activity;

    public static HttpHelp getInstance() {
        if (instance == null) instance = new HttpHelp();
        return instance;
    }

    /**
     * @param context Activity 过期后是否跳转到登陆页面
     * @return HttpHelp
     */
    public static HttpHelp getInstance(Activity context) {
        activity = context;
        if (instance == null) instance = new HttpHelp();
        return instance;
    }

    public void requestGet(String url, final JsonCallback jsonCallback) {
        requestGet(Function.appLicationContext, url, jsonCallback);
    }

    public void requestGet(String url, Map params, final JsonCallback jsonCallback) {
        requestGet(Function.appLicationContext, url, params, jsonCallback);
    }
    /**
     * @param url          url
     * @param jsonCallback jsonCallback
     */
    public void requestGet(Context context, String url, final JsonCallback jsonCallback) {
        requestGet(context, url, new HashMap(), jsonCallback);
    }


    /**
     * Http post请求
     *
     * @param url          url
     * @param params       params
     * @param jsonCallback jsonCallback
     */
    public void requestPost(Context context, String url, Map params, final JsonCallback jsonCallback) {
        requestPost(context, url, params, jsonCallback);
    }

    public void requestGet(Context context, String url, Map params, final JsonCallback jsonCallback) {
        request(RequestType.GET, context, url, params, null,jsonCallback);
    }
    public void requestPost(Context context, String url, BaseModel params, JsonCallback jsonCallback) {
        request(RequestType.POST, context, url, null, params,  jsonCallback);
    }

    private void request(RequestType requestType, Context context, String url, Map params, BaseModel baseModelparams, final JsonCallback jsonCallback) {
        if (TextUtils.isEmpty(url)) {
            jsonCallback.onError(null);
            return;
        }
        switch (requestType) {
            case GET:
                if (params == null || params.size() == 0) {
                    OkGo.get(url).tag(context).params(params).execute(jsonCallback);
                } else {
                    Map map = new HashMap();
                    map.putAll(params);
                    map.putAll(getParamsByUrl(url));
                    OkGo.get(url).tag(context).params(params).execute(jsonCallback);
                }
                break;
            case POST:
                if (params != null) {
                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    OkGo.post(url).tag(context).upJson(gson.toJson(params)).execute(jsonCallback);
                } else {
                    String json = new Gson().toJson(baseModelparams);
                    OkGo.post(url).tag(context).upJson(json).execute(jsonCallback);
                }
                break;
        }
    }

    private Map getParamsByUrl(String url) {
        Map<String, Object> map = new HashMap<>();
        try {
            url = url.replace("?", ";");
            if (!url.contains(";")) {
                return map;
            }
            if (url.split(";").length > 0) {
                String[] arr = url.split(";")[1].split("&");
                for (String s : arr) {
                    String key = s.split("=")[0];
                    String value = s.split("=")[1];
                    map.put(key, value);
                }
                return map;

            } else {
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    enum RequestType {
        GET, POST;
    }
    /**
     * URLDecoder 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}

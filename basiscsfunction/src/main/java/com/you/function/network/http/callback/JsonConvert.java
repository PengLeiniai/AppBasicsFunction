package com.you.function.network.http.callback;

import android.text.TextUtils;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;
import com.you.function.network.http.data.ResponseData;
import com.you.function.network.http.utlis.ConvertUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by you on 2020/03/27.
 */

public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {

    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = ConvertUtil.fromJson(jsonReader, rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        String d = "d";
        d.getBytes();
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = ConvertUtil.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) {
            throw new IllegalStateException("错误信息");
        } else {
            JsonReader jsonReader = new JsonReader(body.charStream());

            Type rawType = type.getRawType();                     // 泛型的实际类型
            Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
            if (rawType != ResponseData.class) {
                // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
                T t = ConvertUtil.fromJson(jsonReader, type);
                response.close();
                return t;
            } else {
                ResponseData responseData;
                if (typeArgument == Void.class) {
                    // 泛型格式如下： new JsonCallback<ResponseData<Void>>(this)
//                SimpleResponse simpleResponse = ConvertUtil.fromJson(jsonReader, SimpleResponse.class);
//                response.close();
//                return (T) simpleResponse.toLzyResponse();
                    return null;
                } else {
                    // 泛型格式如下： new JsonCallback<ResponseData<内层JavaBean>>(this)
                    responseData = ConvertUtil.fromJson(jsonReader, type);
                    response.close();
                    int code = responseData.getCode();
                    if (code == ResponseData.OK && responseData.getData() != null)
                        return (T) responseData;
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new IllegalStateException(TextUtils.isEmpty(responseData.getMessage()) ? "" : (responseData.getMessage() + "|") + code);
                }
            }
        }
    }
}

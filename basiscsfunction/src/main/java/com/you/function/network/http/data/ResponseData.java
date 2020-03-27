package com.you.function.network.http.data;

/**
 * Created by wangkai on 2017/10/9.
 */

public class ResponseData<T>{

    public static final int OK = 200;

    private int code;

    private T data;

    private boolean success;


    private int total;

    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

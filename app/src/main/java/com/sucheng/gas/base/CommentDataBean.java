package com.sucheng.gas.base;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CommentDataBean<T> {

    private int code;

    private Object msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

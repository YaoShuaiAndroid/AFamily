package com.family.afamily.entity;

/**
 * 响应实体
 * Created by hp2015-7 on 2017/2/9.
 */
public class ResponseBean<T> {
    private Integer ret_code;
    private String msg;
    private T data;

    public Integer getRet_code() {
        return ret_code;
    }

    public void setRet_code(Integer ret_code) {
        this.ret_code = ret_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "ret_code=" + ret_code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

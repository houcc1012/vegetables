package com.fine.vegetables.net;


import java.util.List;

public class Resp<T> {
    private int code;
    private String msg;
    private int page;
    private int pageSize;
    private int resultCount;
    private int total;

    private T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void setMsg() {
        msg = "";
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public boolean isTokenExpired() {
        return code == 503;
    }

    /**
     * Check if data is null (null or empty if data is a list)
     *
     * @return true if has data
     */
    public boolean hasData() {
        if (data != null && data instanceof List) {
            return ((List) data).size() > 0;
        }
        return data != null;
    }

    @Override
    public String toString() {
        return "Resp{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

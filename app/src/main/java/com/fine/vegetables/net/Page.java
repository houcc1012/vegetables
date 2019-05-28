package com.fine.vegetables.net;

public class Page<T> {

    private int start;
    private int pageSize;
    private int resultCount;
    private int total;
    private T data;

    public int getPageSize() {
        return pageSize;
    }

    public int getResultCount() {
        return resultCount;
    }

    public int getStart() {
        return start;
    }

    public int getTotal() {
        return total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

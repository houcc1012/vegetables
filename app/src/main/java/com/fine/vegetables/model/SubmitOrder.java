package com.fine.vegetables.model;

import java.io.Serializable;
import java.util.List;


public class SubmitOrder implements Serializable {

    private Long startTime;

    private Long endTime;

    private List<DataBean> data;

    public Long getEndTime() {
        return endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private Integer id;

        private String count;

        public String getCount() {
            return count;
        }

        public Integer getId() {
            return id;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}

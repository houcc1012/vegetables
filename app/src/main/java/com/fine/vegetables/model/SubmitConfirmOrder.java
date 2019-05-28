package com.fine.vegetables.model;

import java.io.Serializable;
import java.util.List;


public class SubmitConfirmOrder implements Serializable {

    private String orderId;

    private List<DataBean> data;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private String id;

        private String count;

        public String getCount() {
            return count;
        }

        public String getId() {
            return id;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

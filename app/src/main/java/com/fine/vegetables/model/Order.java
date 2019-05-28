package com.fine.vegetables.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Parcelable {

    public static final int TYPE_NO_SEND = 1;

    private String orderId;

    private double actualMoney;

    private double totalMoney;

    /**
     * 1 待送达 2 已完成
     */
    private int status;

    private int totalCount;

    private long createTime;

    private List<String> urls;

    private List<DataBean> list;

    public long getCreateTime() {
        return createTime;
    }

    public double getActualMoney() {
        return actualMoney;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public int getStatus() {
        return status;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<DataBean> getList() {
        return list;
    }

    public List<String> getUrls() {
        return urls;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setActualMoney(double actualMoney) {
        this.actualMoney = actualMoney;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public static class DataBean implements Parcelable {

        private double actualWeight;

        private double expectWeight;

        private double price;

        private String vegetableId;

        private String vegetableLogo;

        private String vegetableName;

        private String unitName;

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public double getActualWeight() {
            return actualWeight;
        }

        public double getPrice() {
            return price;
        }

        public double getExpectWeight() {
            return expectWeight;
        }

        public String getVegetableLogo() {
            return vegetableLogo;
        }

        public String getVegetableName() {
            return vegetableName;
        }

        public void setActualWeight(double actualWeight) {
            this.actualWeight = actualWeight;
        }

        public void setExpectWeight(double expectWeight) {
            this.expectWeight = expectWeight;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setVegetableLogo(String vegetableLogo) {
            this.vegetableLogo = vegetableLogo;
        }

        public void setVegetableName(String vegetableName) {
            this.vegetableName = vegetableName;
        }

        public String getVegetableId() {
            return vegetableId;
        }

        public void setVegetableId(String vegetableId) {
            this.vegetableId = vegetableId;
        }

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.actualWeight);
            dest.writeDouble(this.expectWeight);
            dest.writeDouble(this.price);
            dest.writeString(this.vegetableId);
            dest.writeString(this.vegetableLogo);
            dest.writeString(this.vegetableName);
            dest.writeString(this.unitName);
        }

        protected DataBean(Parcel in) {
            this.actualWeight = in.readDouble();
            this.expectWeight = in.readDouble();
            this.price = in.readDouble();
            this.vegetableId = in.readString();
            this.vegetableLogo = in.readString();
            this.vegetableName = in.readString();
            this.unitName = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }


    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeDouble(this.actualMoney);
        dest.writeDouble(this.totalMoney);
        dest.writeInt(this.status);
        dest.writeInt(this.totalCount);
        dest.writeLong(this.createTime);
        dest.writeStringList(this.urls);
        dest.writeTypedList(list);
    }

    protected Order(Parcel in) {
        this.orderId = in.readString();
        this.actualMoney = in.readDouble();
        this.totalMoney = in.readDouble();
        this.status = in.readInt();
        this.totalCount = in.readInt();
        this.createTime = in.readLong();
        this.urls = in.createStringArrayList();
        this.list = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

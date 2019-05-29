package com.fine.vegetables.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class SellerOrder implements Parcelable {

    private String orderId;

    private int totalCount;

    private String concatName;

    private long createTime;

    private String phone;

    private String addr;

    private String name;

    /**
     * 0 待确认 1 待送达 2 已完成
     */
    private int status;

    private int confirm;

    private List<String> urls;

    private List<DataBean> list;

    private String actualMoney;

    private String totalMoney;

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        this.list = list;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getConcatName() {
        return concatName;
    }

    public void setConcatName(String concatName) {
        this.concatName = concatName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }


    public static class DataBean implements Parcelable {

        private int id;

        private String actualWeight;

        private String expectWeight;

        private double price;

        private String vegetableId;

        private String vegetableLogo;

        private String vegetableName;

        private String unitName;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getActualWeight() {
            return actualWeight;
        }

        public double getPrice() {
            return price;
        }

        public String getExpectWeight() {
            return expectWeight;
        }


        public String getVegetableLogo() {
            return vegetableLogo;
        }

        public String getVegetableName() {
            return vegetableName;
        }

        public void setActualWeight(String actualWeight) {
            this.actualWeight = actualWeight;
        }

        public void setExpectWeight(String expectWeight) {
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
            dest.writeString(this.actualWeight);
            dest.writeString(this.expectWeight);
            dest.writeDouble(this.price);
            dest.writeString(this.vegetableId);
            dest.writeString(this.vegetableLogo);
            dest.writeString(this.vegetableName);
            dest.writeString(this.unitName);
        }

        protected DataBean(Parcel in) {
            this.actualWeight = in.readString();
            this.expectWeight = in.readString();
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


    public SellerOrder() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeDouble(this.totalCount);
        dest.writeString(this.concatName);
        dest.writeLong(this.createTime);
        dest.writeString(this.phone);
        dest.writeString(this.addr);
        dest.writeString(this.name);
        dest.writeInt(this.status);
        dest.writeInt(this.confirm);
        dest.writeStringList(this.urls);
        dest.writeTypedList(list);
        dest.writeString(this.actualMoney);
        dest.writeString(this.totalMoney);
    }

    protected SellerOrder(Parcel in) {
        this.orderId = in.readString();
        this.totalCount = in.readInt();
        this.concatName = in.readString();
        this.createTime = in.readLong();
        this.phone = in.readString();
        this.addr = in.readString();
        this.name = in.readString();
        this.status = in.readInt();
        this.confirm = in.readInt();
        this.urls = in.createStringArrayList();
        this.list = in.createTypedArrayList(DataBean.CREATOR);
        this.actualMoney = in.readString();
        this.totalMoney = in.readString();
    }

    public static final Creator<SellerOrder> CREATOR = new Creator<SellerOrder>() {
        public SellerOrder createFromParcel(Parcel source) {
            return new SellerOrder(source);
        }

        public SellerOrder[] newArray(int size) {
            return new SellerOrder[size];
        }
    };
}

package com.fine.vegetables.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Commodity implements Parcelable {
    private Integer id;
    private Integer groupId;
    private String name;
    private String logo;
    private Double price;
    private String unitName;

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getLogo() {
        return logo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.groupId);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeValue(this.price);
        dest.writeString(this.unitName);
    }

    public Commodity() {
    }

    protected Commodity(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.groupId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.logo = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.unitName = in.readString();
    }

    public static final Creator<Commodity> CREATOR = new Creator<Commodity>() {
        public Commodity createFromParcel(Parcel source) {
            return new Commodity(source);
        }

        public Commodity[] newArray(int size) {
            return new Commodity[size];
        }
    };
}
